package ch.softenvironment.jomm.target.sql;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbMapper;
import ch.softenvironment.jomm.DbObjectId;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQuery;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.DbTransaction;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbFieldType;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.implementation.DbState;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.UserException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.jdo.Extent;
import javax.jdo.FetchGroup;
import javax.jdo.FetchPlan;
import javax.jdo.JDOException;
import javax.jdo.ObjectState;
import javax.jdo.Query;
import javax.jdo.datastore.JDOConnection;
import javax.jdo.datastore.Sequence;
import javax.jdo.listener.InstanceLifecycleListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Specific ObjectServer for dealing with SQL Target DBMS.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class SqlObjectServer extends ch.softenvironment.jomm.DbObjectServer {

	/**
	 * SqlObjectServer constructor comment.
	 *
	 * @param factory javax.jdo.PersistenceManagerFactory
	 * @param password java.lang.String
	 * @param mapper ch.softenvironment.jomm.DbMapper
	 * @param queryBuilder java.lang.Class
	 * @param dbConnection java.lang.Class
	 * @throws java.lang.Exception The exception description.
	 */
	public SqlObjectServer(javax.jdo.PersistenceManagerFactory factory, String password, ch.softenvironment.jomm.DbMapper mapper, Class queryBuilder,
		Class dbConnection) throws Exception {
		super(factory, password, mapper, queryBuilder, dbConnection);
	}

	/**
	 * Save any n:n Relations by intermediate Mapping-Table where A:B=n:n via relationObject C.
	 */
	private synchronized void allocateManyToMany(DbChangeableBean object, DbDescriptor descriptor) throws Exception {
		Iterator<String> iterator = descriptor.iteratorAttributedProperties();
		while ((iterator != null) && iterator.hasNext()) {
			String property = iterator.next();
			if (object.getChangedProperties().contains(property)) {
				DbDescriptorEntry entry = descriptor.getEntry(property);
				DbPropertyChange objectChange = new DbPropertyChange(object, property);
				java.util.List values = (java.util.List) objectChange.getValue();
				Iterator items = values.iterator();
				List removables = new ArrayList();
				while (items.hasNext()) {
					Object tmp = items.next();
					if (tmp instanceof DbRelationshipBean) {
						DbRelationshipBean association = (DbRelationshipBean) tmp; // type:
						// entry.getBaseClass()
						if (association.getPersistencyState().isNew()) {
							// set id of A
							DbPropertyChange change = new DbPropertyChange(association, entry.getOtherPropertyName());
							change.setValue(object.getId());
							association.save();
						} else if (association.getPersistencyState().isChanged()) {
							association.save();
						} else if (association.getPersistencyState().isRemovedPending()) {
							if (association.isRemoveObjectHistoryPending()) {
								execute(DbQueryBuilder.createChangeRemoveHistory(association));
							}
							association.remove(true);
							removables.add(association);
						}
					} else {
						log.error("Developer error: DbRelationshipBean expected");
					}
				}
				values.removeAll(removables); // concurrency!
			}
		}
	}

	/**
	 * Save any Aggregates by "REMOTE" Properties (ForeignKey is on other side of Association).
	 */
	private synchronized void allocateOneToMany(DbChangeableBean object, DbDescriptor descriptor) throws Exception {
		// Aggregations 1:[0..1]
		Iterator<String> iterator = descriptor.iteratorAggregatedProperties();
		while ((iterator != null) && iterator.hasNext()) {
			String property = iterator.next();
			DbDescriptorEntry entry = descriptor.getEntry(property);
			DbPropertyChange objectChange = new DbPropertyChange(object, property);
			Object other = objectChange.getValue();
			if (other instanceof DbObject) {
				updateReference(object, entry, (DbObject) other);
			}
		}

		// Aggregations 1:[0..*]
		iterator = descriptor.iteratorOneToManyProperties();
		while ((iterator != null) && iterator.hasNext()) {
			String property = iterator.next();
			DbDescriptorEntry entry = descriptor.getEntry(property);
			DbPropertyChange objectChange = new DbPropertyChange(object, property);
			java.util.List list = (java.util.List) objectChange.getValue();
			Iterator items = list.iterator();
			if (entry.getMapName() != null) {
				if (object.getChangedProperties().contains(property)) {
					// TODO Tune: Do not DELETE and re-INSERT Multi-Codes =>
					// better change difference
					// remove all items => reinsert below
					DbQueryBuilder builder = DbQueryBuilder.createMapClause(DbQueryBuilder.DELETE, object, entry);
					execute(builder);
				}
			}
			while (items.hasNext()) {
				Object value = items.next();

				if (entry.getMapName() == null) {
					// case: save ForeignKey in other Object
					updateReference(object, entry, (DbObject) value);
				} else {
					// save via T_MAP_<code> Table for a specific own
					// DbFieldType
					if (entry.getBaseClass() == null) {
						// case: BaseClass defined in fieldType
						if (object.getChangedProperties().contains(property)) {
							// if
							// (entry.getFieldType().getBaseType().equals(String.class))
							// {
							DbQueryBuilder builder = DbQueryBuilder.createMapClause(DbQueryBuilder.INSERT, object, entry);
							if (value instanceof DbFieldType) {
								builder.append(DbMapper.ATTRIBUTE_MAP_VALUE, (DbFieldType) value);
							} else {
								// try standard
								builder.append(DbMapper.ATTRIBUTE_MAP_VALUE, value.toString());
							}
							builder.appendInternal((String) null);
							execute(builder);
							// }
						}
					} else {
						if (object.getChangedProperties().contains(property)) {
							// case: T_MAP_<code>
							// verify difference between application and
							// database
							DbQueryBuilder builder = DbQueryBuilder.createMapClause(DbQueryBuilder.INSERT, object, entry);
							builder.append(DbMapper.ATTRIBUTE_MAP_VALUE_ID, ((DbObject) value).getId());
							builder.appendInternal((String) null);
							execute(builder);
						}
					}
				}
			}
		}
	}

	/**
	 * Cache all DbNlsString's (NLS-String-property's) for a given DbEnumeration and a given Locale.
	 */
	private void cacheNlsStrings(java.lang.Class dbCode, Locale locale) {
		if (DbObject.isCode(dbCode)) {
			// Tunes DbCode-Reading because dbCode's are usually read as whole
			// group.
			DbQueryBuilder builder = createQueryBuilder(DbQueryBuilder.SELECT,
				"Cache NLS-Strings for code <" + dbCode.getName() + " and language <" + locale.toString() + ">");
			builder.toCodeNlsBuilder(dbCode, locale);

			Exception exception = null;
			DbTransaction trans = null;
			try {
				trans = openTransactionThread(false);

				DbQuery query = new DbQuery(trans, builder);
				ResultSet queryResult = (ResultSet) query.execute();
				while (checkNext(queryResult, false, "cacheNlsStrings(dbCode, locale)")) {
					DbNlsString nlsString = new DbNlsString(this, new Long(queryResult.getLong(DbNlsString.ATTRIBUTE_TRANSLATION_ID)), locale,
						queryResult.getString(DbNlsString.ATTRIBUTE_NLS_TEXT), queryResult.getTimestamp(DbMapper.ATTRIBUTE_CREATE_DATE),
						queryResult.getTimestamp(DbMapper.ATTRIBUTE_LAST_CHANGE), queryResult.getString(DbMapper.ATTRIBUTE_USER_ID));
					nlsCache.put(nlsString.getId(), nlsString);
				}
				query.closeAll();
			} catch (Exception e) {
				exception = e;
			}
			closeTransactionThread(exception, trans, "cacheNlsStrings(dbCode, locale)");
		} else {
			// TODO check iteratorNlsProperties and Join for each
			// DbNlsString-Property
		}
	}

	/**
	 * Delete the persistent instance from the data store. This method must be called in an active transaction. The data store object will be removed at commit. Unlike <code>makePersistent</code>,
	 * which makes the closure of the instance persistent, the closure of the instance is not deleted from the data store. This method has no effect if the instance is already deleted in the current
	 * transaction. This method throws
	 * <code>JDOUserException</code> if the instance is transient or is managed
	 * by another <code>PersistenceManager</code>.
	 *
	 * @param pc a persistent instance
	 * see javax.jdo.PersistentManager
	 */
	@Override
	public void deletePersistent(java.lang.Object pc) {
		try {
			DbObject object = (DbObject) pc;

			evict(object);
			execute(DbQueryBuilder.createRemoveObject(object));
		} catch (Exception e) {
			throw new UserException(ResourceManager.getResource(DbObjectServer.class, "CW_DeletionError"), ResourceManager.getResource(DbObjectServer.class,
				"CT_DeletionError"), e);
		}
	}

	@Override
	public void makeTransient(Object o, boolean b) {

	}

	@Override
	public void makeTransientAll(Object[] objects, boolean b) {

	}

	@Override
	public void makeTransientAll(boolean b, Object... objects) {

	}

	@Override
	public void makeTransientAll(Collection collection, boolean b) {

	}

	@Override
	public void retrieve(Object o, boolean b) {

	}

	@Override
	public void retrieveAll(boolean b, Object... objects) {

	}

	@Override
	public void setDatastoreReadTimeoutMillis(Integer integer) {

	}

	@Override
	public Integer getDatastoreReadTimeoutMillis() {
		return null;
	}

	@Override
	public void setDatastoreWriteTimeoutMillis(Integer integer) {

	}

	@Override
	public Integer getDatastoreWriteTimeoutMillis() {
		return null;
	}

	@Override
	public boolean getDetachAllOnCommit() {
		return false;
	}

	@Override
	public void setDetachAllOnCommit(boolean b) {

	}

	@Override
	public boolean getCopyOnAttach() {
		return false;
	}

	@Override
	public void setCopyOnAttach(boolean b) {

	}

	@Override
	public <T> T detachCopy(T t) {
		return null;
	}

	@Override
	public <T> Collection<T> detachCopyAll(Collection<T> collection) {
		return null;
	}

	@Override
	public <T> T[] detachCopyAll(T... ts) {
		return null;
	}

	@Override
	public Object putUserObject(Object o, Object o1) {
		return null;
	}

	@Override
	public Object getUserObject(Object o) {
		return null;
	}

	@Override
	public Object removeUserObject(Object o) {
		return null;
	}

	@Override
	public void flush() {

	}

	@Override
	public void checkConsistency() {

	}

	@Override
	public FetchPlan getFetchPlan() {
		return null;
	}

	@Override
	public <T> T newInstance(Class<T> aClass) {
		return null;
	}

	@Override
	public Sequence getSequence(String s) {
		return null;
	}

	@Override
	public JDOConnection getDataStoreConnection() {
		return null;
	}

	@Override
	public void addInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener, Class... classes) {

	}

	@Override
	public void removeInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener) {

	}

	@Override
	public Date getServerDate() {
		return null;
	}

	@Override
	public Set getManagedObjects() {
		return null;
	}

	@Override
	public Set getManagedObjects(EnumSet<ObjectState> enumSet) {
		return null;
	}

	@Override
	public Set getManagedObjects(Class... classes) {
		return null;
	}

	@Override
	public Set getManagedObjects(EnumSet<ObjectState> enumSet, Class... classes) {
		return null;
	}

	@Override
	public FetchGroup getFetchGroup(Class aClass, String s) {
		return null;
	}

	@Override
	public void setProperty(String s, Object o) {

	}

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public Set<String> getSupportedProperties() {
		return null;
	}

	@Override
	public synchronized DbNlsString getNlsString(Long id, Locale locale) {
		// TODO tune!
		try {
			if (id == null) {
				// no Instance aggregated yet
				return null;
			}

			DbNlsString nlsString = super.getNlsString(id, locale);
			if (nlsString != null) {
				// known from cache
				return nlsString;
			}

			Exception exception = null;
			DbTransaction trans = null;
			try {
				DbQueryBuilder builder = DbNlsString.createQueryBuilderSelect(this, id, locale);

				trans = openTransactionThread(false);
				DbQuery query = new DbQuery(trans, builder);
				ResultSet queryResult = (ResultSet) query.execute();
				if (checkNext(queryResult, false, "getNlsString(..)")) {
					nlsString = new DbNlsString(this, id, locale, queryResult.getString(DbNlsString.ATTRIBUTE_NLS_TEXT),
						queryResult.getTimestamp(DbMapper.ATTRIBUTE_CREATE_DATE), queryResult.getTimestamp(DbMapper.ATTRIBUTE_LAST_CHANGE),
						queryResult.getString(DbMapper.ATTRIBUTE_USER_ID));
					nlsCache.put(id, nlsString);
				}
				query.closeAll();
			} catch (Exception e) {
				exception = e;
			}
			closeTransactionThread(exception, trans, "getNlsString(..)");

			return nlsString;
		} catch (Exception e) {
			throw new DeveloperException("could not retrieve T_Id=" + id.toString(), null, e);
		}
	}

	@Override
	public synchronized java.lang.Object getObjectById(java.lang.Object oid, boolean validate, boolean cached) {
		Object object = null;
		if (oid instanceof DbObjectId) {
			try {
				object = retrieve((DbObjectId) oid, validate, cached, null);
			} catch (Exception e) {
				throw new UserException(ResourceManager.getResource(DbObjectServer.class, "CW_ReadError") + ((DbObjectId) oid).toString(),
					ResourceManager.getResource(DbObjectServer.class, "CE_ReadError"), e);
			}
		}
		return object;
	}

	@Override
	protected void allocate(DbTransaction trans, DbChangeableBean object) throws Exception {
		// build reverse inheritance chain
		DbDescriptor descriptor = object.getObjectServer().getDescriptor(object.getClass());
		java.util.List<DbDescriptor> inheritanceChain = new java.util.ArrayList<DbDescriptor>();
		inheritanceChain.add(descriptor);
		while (getParentDescriptor(descriptor) != null) {
			descriptor = getParentDescriptor(descriptor);
			inheritanceChain.add(descriptor);
		}

		// INSERT top-down (Root/Parent-Object first) in inheritance Chain
		for (int i = inheritanceChain.size(); i > 0; i--) {
			descriptor = inheritanceChain.get(i - 1);

			// 1) save "LOCAL" Properties (flat Attributes;
			// ForeignKey-References)
			if (object.isModified()) {
				boolean newDbObject = (i == inheritanceChain.size()) && object.jdoIsNew()
					&& object.getObjectServer().getDescriptor(object.getClass()).contains(DbObject.PROPERTY_ID);
				if (newDbObject && (!object.getPersistencyState().isNewForwarded()/*
				 * object
				 * .
				 * isNewForwarded
				 * (
				 * )
				 * should
				 * have
				 * original
				 * ID
				 * already
				 */)) {
					// set Unique ID: use Table-Name of Top-Parent for whole
					// inheritance chain
					// TODO Tune: create empty Objects only in case related
					// objects changed
					setUniqueId(object, getMapper().getNewId(this, trans, getTargetName(descriptor.getMappedClass())));
				}

				DbQueryBuilder builder = DbQueryBuilder.createChangeObject(descriptor.getMappedClass(), object);
				if (newDbObject && getDescriptor(object.getClass()).isOrdered()) {
					// by default set SEQUENCE like id
					builder.append(DbMapper.ATTRIBUTE_ORDERED, object.getId());
				}
				if (!builder.isSuppressable()) {
					if (builder.isUpdate()) {
						object.setLockFields(getUserId(), builder.appendInternal((String) null));
					}
					execute(trans, builder);
				}
			}

			// 2) 1:n
			allocateOneToMany(object, descriptor);

			// 3) n:n
			allocateManyToMany(object, descriptor);
		}
	}

	/**
	 * Disconnect and reconnect to DBMS-Server.
	 */
	@Override
	public synchronized void reconnect() throws javax.jdo.JDOException {
		try {
			log.info("reconnect Target-System");
			// TODO disable access to connection in the meantime
			((SqlConnection) getConnection()).reconnect();
		} catch (SQLException e) {
			log.error("cannot reconnect to Target-System by url: {}", getPersistenceManagerFactory().getConnectionURL(), e);
			throw new javax.jdo.JDOFatalException("SqlConnection.reconnect()", e);
		}
	}

	@Override
	public void evictAll(boolean b, Class aClass) {

	}

	@Override
	public synchronized void refresh(java.lang.Object pc) {
		if (pc instanceof DbObject) {
			DbObject object = (DbObject) pc;

			if (object instanceof DbNlsString) {
				getNlsString(object.getId());
				return;
			}

			if (object.getPersistencyState().isSaved() || object.getPersistencyState().isReadOnly() || object.getPersistencyState().isChanged()
				|| (object.getPersistencyState().isUndefined() && (object.getId() != null))) {
				DbTransaction trans = null;
				Exception exception = null;
				try {
					trans = openTransactionThread(false);

					DbDescriptor descriptor = object.getObjectServer().getDescriptor(object.getClass());
					while (descriptor != null) {
						// SELECT first the Child and then Parent
						DbQueryBuilder builder = SqlQueryBuilder.createQueryGetObject(descriptor.getMappedClass(), object);
						DbQuery query = new DbQuery(trans, builder);

						ResultSet rs = (ResultSet) query.execute();
						if (rs.next()) {
							getMapper().mapFromTarget(object, descriptor, rs);
							query.close(rs);

							// get parent of Object's inherited child as well
							descriptor = getParentDescriptor(descriptor);
						} else {
							query.close(rs);
							throw new DeveloperException("ResultSet incomplete");
						}
					}
					object.reset(true);
				} catch (Exception e) {
					exception = e;
					object.getPersistencyState().setNext(DbState.UNDEFINED);
				}
				closeTransactionThread(exception /* SELECT only */, trans, "refresh(Object)");
			} else {
				super.refresh(pc);
			}
		}
	}

	@Override
	public void refreshAll(JDOException e) {

	}

	@Override
	public Query newQuery(String s) {
		return null;
	}

	@Override
	public Query newNamedQuery(Class aClass, String s) {
		return null;
	}

	@Override
	public <T> Extent<T> getExtent(Class<T> aClass) {
		return null;
	}

	@Override
	public <T> T getObjectById(Class<T> aClass, Object o) {
		return null;
	}

	@Override
	public Object getObjectById(Object o) {
		return null;
	}

	@Override
	public Object newObjectIdInstance(Class aClass, Object o) {
		return null;
	}

	@Override
	public Collection getObjectsById(Collection collection, boolean b) {
		return null;
	}

	@Override
	public Collection getObjectsById(Collection collection) {
		return null;
	}

	@Override
	public Object[] getObjectsById(Object[] objects, boolean b) {
		return new Object[0];
	}

	@Override
	public Object[] getObjectsById(boolean b, Object... objects) {
		return new Object[0];
	}

	@Override
	public Object[] getObjectsById(Object... objects) {
		return new Object[0];
	}

	@Override
	public synchronized void removeCode(DbCode code) {
		// Throwable exception = null;
		// DbTransaction trans = null;
		try {
			// trans = openTransactionThread();

			List queries = new ArrayList(2);

			DbNlsString nls = code.getName();

			// remove code
			DbQueryBuilder builder = createQueryBuilder(DbQueryBuilder.DELETE, code.getClass().getName());
			builder.setTableList(code.getClass());
			builder.addFilter(getMapper().getTargetIdName(), code.getId());
			queries.add(builder.getQuery());

			// remove its NLS-String
			// nls.remove(true);
			builder = createQueryBuilder(DbQueryBuilder.DELETE, "DbNlsString");
			builder.setTableList(getMapper().getTargetNlsName());
			builder.addFilter(getMapper().getTargetIdName(), nls.getId());
			queries.add(builder.getQuery());

			execute("DELETE DbCode & NLS-String", queries);
			// just in case Target-Schema constraints "failed"
			code.getPersistencyState().setNext(DbState.REMOVED);
			nls.getPersistencyState().setNext(DbState.REMOVED);

			// make sure the removed code is not cached any more
			evict(code);
			evict(nls);
		} catch (Exception e) {
			// exception = e;
			throw new UserException("Wahrscheinlich wird der zu löschende Code noch irgendwo referenziert.", "Code nicht löschbar", e);
		}
		// closeTransactionThread(exception, trans, "removeCode()");
	}

	/**
	 * Retrieve an instance of the given class according to its ID. The DbObject of type dbObject is assumed to exist with the given dbId in the Database and therefore not verified here!
	 *
	 * @param cached if true => object will be read from cache or put to cache if missing yet
	 * @param concreteTable Effective Child-Instance type in case of inheritance
	 * @return DbObject
	 */
	private synchronized DbObject retrieve(DbObjectId oid, boolean validate, boolean cached, String concreteTable) throws Exception {
		if (oid == null) {
			throw new IllegalArgumentException("OID must not be null");
		}

		Class dbObject = oid.getModelClass();
		Long dbId = oid.getId();

		if (dbObject.equals(DbNlsString.class)) {
			return getNlsString(dbId);
		}

		if (DbObject.isCode(dbObject)) {
			List objs = retrieveCodes(dbObject);
			if (objs == null) {
				log.warn("DbEnumeration={} with Id={} missing", dbObject.getName(), dbId);
			}
			return codeCache.get(dbObject, dbId);
		}

		DbObject obj = null;

		if (!getIgnoreCache()) {
			// try searching cache first
			obj = getCachedObject(dbObject, dbId);
			if (obj != null) {
				if (validate) {
					// validate whether still existing
					refresh(obj);
				}
				return obj;
			}
		}

		// aggregate fresh from database
		try {
			String tableName = concreteTable;
			java.lang.Class concreteClass = dbObject;

			if (concreteTable == null) {
				java.lang.Class rootClass = getConnection().getRootClassFor(dbObject);
				if (rootClass.equals(dbObject)) {
					if (getDescriptor(rootClass).getAttribute(this, DbObject.PROPERTY_ID).equals(getMapper().getTargetIdName()) /*
					 * means
					 * is
					 * #
					 * id
					 * mapped
					 * to
					 * "T_Id"
					 */) {
						// aggregate might be an inherited or abstract base
						// instance
						// => determine concrete type to be instantiated by
						// technical Attribute T_Type
						DbQueryBuilder builder = createQueryBuilder(DbQueryBuilder.SELECT, "Concrete Child Table");
						builder.setAttributeList(DbMapper.ATTRIBUTE_CONCRETE_CLASS);
						builder.setTableList(rootClass);
						builder.addFilter(getMapper().getTargetIdName(), dbId);
						tableName = (String) getFirstValue(builder);
						if (StringUtils.isNullOrEmpty(tableName)) {
							// Tracer.getInstance().runtimeError(dbObject.getName()
							// + "[Id=" + dbId +
							// "] is missing=>probably owner of 1:n aggregate has been removed with violation of referential integrity -> child seems to point on inexisting owner (Schema constraint evtl. bad!)");
							throw new DeveloperException(
								dbObject.getName()
									+ "[Id="
									+ dbId
									+ "] is missing=>probably owner of 1:n aggregate has been removed with violation of referential integrity -> child seems to point on inexisting owner (Schema constraint evtl. bad!)");
						} else {
							concreteClass = getConnection().getDbObject(tableName);
						}
					} else {
						// assume no inheritance given
						concreteClass = rootClass;
					}
				}
			} else {
				concreteClass = getConnection().getDbObject(tableName);
			}

			obj = createInstance(concreteClass, true);
			setUniqueId(obj, dbId);
			obj.reset(false);

			if (cached) {
				// sequence Important to prevent indefinite Recursion!
				objectCache.put(concreteClass, obj);
				obj.refresh(true);
			}
			return obj;
		} catch (Exception e) {
			throw new DeveloperException("given Class (" + dbObject.getName() + ") cannot be retrieved", null, e);
		}
	}

	@Override
	public List retrieveAll(Class dbObject, DbQueryBuilder builder, boolean cached) {
		if ((builder == null) || (!builder.isSelection())) {
			throw new IllegalArgumentException("builder must be a Select-Query!");
		}

		Locale locale = builder.getDefaultLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}

		// try whether cached DbCodeType
		if (cached && DbObject.isCode(dbObject)) {
			Collection cachedObjs = codeCache.getAll(dbObject);
			if ((cachedObjs != null) && (cachedObjs.size() > 0)) {
				// keep given order => @see below <List objs>
				return new ArrayList(cachedObjs);
			}
		}
		// for other Object's do not use Cache here
		// because there is no Guarantee that the Query-Conditions matches
		// the already cached DbObject's here

		if (DbObject.isCode(dbObject)) {
			// tune code reading knowing that each code
			// has a DbNlsString property "name"
			cacheNlsStrings(dbObject, locale);
		}

		// sort-ORDER is expected by T_Sequence-attribute
		// @see DbDescriptor#setOrdered() (or above in <cachedObjs>)
		List objs = new ArrayList();

		Exception exception = null;
		DbTransaction trans = null;
		try {
			trans = openTransactionThread(false);

			DbQuery query = new DbQuery(trans, builder);
			ResultSet resultSet = (ResultSet) query.execute();

			while (checkNext(resultSet, false, "retrieveAll(..)")) {
				// create new Instance for each record by new
				// DbObject(DbObjectServer, ResultSet)
				DbObject obj = null;
				if (DbObject.isCode(dbObject) || dbObject.getSuperclass().equals(DbSessionBean.class)) {
					obj = createInstance(dbObject, true);
					// map attributes
					getMapper().mapFromTarget(obj, obj.getObjectServer().getDescriptor(obj.getClass()), resultSet);
					obj.reset(false);
				} else {
					// map Argumentlist to DataVector (not yet to DbObject
					// instances)
					long id = resultSet.getLong(getDescriptor(dbObject).getAttribute(this, DbObject.PROPERTY_ID));
					if (resultSet.wasNull()) {
						continue;
					}
					ResultSetMetaData schema = resultSet.getMetaData();
					int columnCount = schema.getColumnCount();
					String concreteTable = null;
					for (int col = 1; col <= columnCount; col++) {
						String tmp = schema.getColumnName(col);
						if (tmp.equals(DbMapper.ATTRIBUTE_CONCRETE_CLASS)) {
							concreteTable = resultSet.getString(col);
							break;
						}
					}
					obj = retrieve(new DbObjectId(dbObject, new Long(id)), false, cached, concreteTable);
					if (!obj.isRefreshed()) {
						// try to map data read in ResulSet
						getMapper().mapFromTarget(obj, obj.getObjectServer().getDescriptor(obj.getClass()), resultSet);
						obj.reset(false);
					}
				}
				objs.add(obj);
			}
			query.close(resultSet);
		} catch (Exception e) {
			exception = e;
		}
		closeTransactionThread(exception, trans, "retrieveAll(..)");

		// cache them for next access
		if (cached) {
			if (DbObject.isCode(dbObject)) {
				// cache as Group
				codeCache.addAll(dbObject, objs);
			} else {
				// cache each for itself
				Iterator iterator = objs.iterator();
				while (iterator.hasNext()) {
					objectCache.put(dbObject, (DbObject) iterator.next());
				}
			}
		}

		return objs;
	}

	@Override
	public synchronized List retrieveCodes(Class dbCodeType, Locale locale) {
		try {
			Collection codes = codeCache.getAll(dbCodeType);
			if ((codes == null) || (codes.size() == 0)) {
				// DbQueryBuilder builder =
				// getQueryBuilder(DbQueryBuilder.SELECT,
				// "Cache Codes <" + dbCodeType.getName() +"> and locale <" +
				// locale.toString() + ">");
				DbQueryBuilder builder = DbQueryBuilder.createQueryAllInstances(this, dbCodeType);
				builder.setDefaultLocale(locale);
				if (getDescriptor(dbCodeType).isOrdered()) {
					// fix ordering independent of attribute text (resp.
					// DbNlsString)
					builder.addOrder(DbMapper.ATTRIBUTE_ORDERED);
				} // else ordering by "nlsText" should be done by current
				// language
				// in application view-Part
				return retrieveAll(dbCodeType, builder, true);
			} else {
				return new ArrayList(codes); // keep sort order
			}
		} catch (Exception e) {
			// TODO ant showed this error, where it is not necessary this
			// Exception in eclipse???
			throw new DeveloperException("Unexpected error", "retrieveCodes(..)", e);
		}
	}

	@Override
	public synchronized DbEnumeration retrieveEnumeration(Class<? extends DbEnumeration> dbEnumeration, final String iliCode) throws Exception {
		Long dbId = DbEnumeration.getIliCodeId(this, dbEnumeration, iliCode);
		return (DbEnumeration) getObjectById(new DbObjectId(dbEnumeration, dbId), false, true /*
		 * cache
		 * Codes
		 * by
		 * default
		 */);
	}

	/**
	 * Keep up the reference: other.objectId = object.id
	 */
	private void updateReference(DbObject object, DbDescriptorEntry objectEntry, DbObject other) throws Exception {
		if (other != null) {
			if ((!objectEntry.isLocal()) && (other.getPersistencyState().isNew() /*
			 * related
			 * Objects
			 * might
			 * have
			 * changed
			 */ || other.isModified())) {
				DbPropertyChange otherChange = new DbPropertyChange(other, objectEntry.getOtherPropertyName());
				if (otherChange.getGetterReturnType().equals(Long.class)) {
					otherChange.setProperty(object.getId());
				} else {
					// assume whole Object Aggregation
					otherChange.setProperty(object);
				}
			}

			// any related objects to other might have been changed as well
			if (other instanceof DbChangeableBean /*
			 * !DbObject.isCode(other.getClass
			 * ())
			 */) {
				((DbChangeableBean) other).save();
			}
		}
	}

	/**
	 * Check whether Target-server might be down!
	 */
	@Override
	public synchronized boolean checkConnection() {
		// for most applications this is okay, the query used is very small and
		// takes
		// no real server resources to process
		Object value = null;
		try {
			DbQueryBuilder builder = createQueryBuilder(DbQueryBuilder.RAW, "Test connection freshness");
			builder.setRaw("SELECT 1");
			value = getFirstValue(builder);
			return value.toString().equals("1"); // (Integer.parseInt(getFirstValue(builder).toString())
			// == 1);
		} catch (Exception e) {
			log.error("Target probably down", e);
			return false;
		}
	}
}
