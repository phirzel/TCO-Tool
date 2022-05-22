package ch.softenvironment.jomm.target.xml;

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

import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.DbTransaction;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.implementation.DbState;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Tracer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
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

/**
 * Concrete ObjectServer for XML-Target-System.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class XmlObjectServer extends ch.softenvironment.jomm.DbObjectServer {

	private static final Long MODEL_ROOT_ID = Long.valueOf(1000000);
	private volatile long idCounter = MODEL_ROOT_ID.longValue();

	private java.util.Set<Class<? extends DbCodeType>> codeTypes = new HashSet<Class<? extends DbCodeType>>();

	/**
	 * XmlObjectServer constructor comment.
	 *
	 * @param factory javax.jdo.PersistenceManagerFactory
	 * @param password java.lang.String
	 * @param mapper ch.softenvironment.jomm.DbMapper
	 * @param queryBuilder java.lang.Class
	 * @param dbConnection java.lang.Class
	 */
	public XmlObjectServer(javax.jdo.PersistenceManagerFactory factory, String password, ch.softenvironment.jomm.DbMapper mapper, Class queryBuilder,
		Class dbConnection) throws Exception {
		super(factory, password, mapper, queryBuilder, dbConnection);
	}

	@Override
	public void cacheCode(DbCodeType code) {
		try {
			super.cacheCode(code);
		} catch (ch.softenvironment.util.DeveloperException e) {
			// TODO @deprecated (only for XML-Files with DbNlsString-Ref's)
			Tracer.getInstance().developerWarning("IGNORE: Nls-name of DbCode is not yet aggregated -> will be cached later");
		}
		if (code instanceof DbCode) {
			codeTypes.add(code.getClass());
		} // else do not cache DbEnumeration
	}

	/**
	 * Create an Enumeration which is defined by an ILI-XML-Schema via IliCode. An Enumeration is never saved into XML-Instance, but it is part of XSD-Schema.
	 */
	@Deprecated
	public synchronized DbEnumeration createEnumeration(java.lang.Class<? extends DbEnumeration> dbEnumeration, final String iliCode, Locale locale, String name) {
		DbEnumeration enumeration = null;
		try {
			enumeration = (DbEnumeration) createInstance(dbEnumeration);
			setUniqueId(enumeration, getNewId(enumeration));

			DbNlsString nls = (DbNlsString) createInstance(DbNlsString.class);
			nls.setValue(name, locale);
			// save(nls);

			DbPropertyChange change = new DbPropertyChange(enumeration, DbObject.PROPERTY_NAME);
			change.setProperty(nls); // enum.setName(name);
			// nls.setChange(change);
			change = new DbPropertyChange(enumeration, DbCodeType.PROPERTY_ILICODE);
			change.setProperty(iliCode); // enum.setIliCode(name);

			enumeration.getPersistencyState().setNext(DbState.READ_ONLY /*
			 * typical
			 * enum
			 * state
			 */);

			cacheCode(enumeration);
		} catch (Exception e) {
			throw new ch.softenvironment.util.DeveloperException("Error at creation of Enumeration.", "Create DbEnumeration", e);
		}
		return enumeration;
	}

	@Override
	public void evictAll(boolean b, Class aClass) {
		//TODO HIP just added to compile
	}

	@Override
	public void refreshAll(JDOException e) {
		//TODO HIP just added to compile
	}

	@Override
	public Query newQuery(String s) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Query newNamedQuery(Class aClass, String s) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public <T> Extent<T> getExtent(Class<T> aClass) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public <T> T getObjectById(Class<T> aClass, Object o) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Object getObjectById(Object o) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Object newObjectIdInstance(Class aClass, Object o) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Collection getObjectsById(Collection collection, boolean b) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Collection getObjectsById(Collection collection) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Object[] getObjectsById(Object[] objects, boolean b) {
		//TODO HIP just added to compile
		return new Object[0];
	}

	@Override
	public Object[] getObjectsById(boolean b, Object... objects) {
		//TODO HIP just added to compile
		return new Object[0];
	}

	@Override
	public Object[] getObjectsById(Object... objects) {
		//TODO HIP just added to compile
		return new Object[0];
	}

	@Override
	public void deletePersistent(Object pc) {
		evict(pc);
		((DbObject) pc).getPersistencyState().setNext(DbState.REMOVED_PENDING);
		// TODO NYI: Re-Save root-element instead
	}

	@Override
	public void makeTransient(Object o, boolean b) {
		//TODO HIP just added to compile
	}

	@Override
	public void makeTransientAll(Object[] objects, boolean b) {
		//TODO HIP just added to compile
	}

	@Override
	public void makeTransientAll(boolean b, Object... objects) {
		//TODO HIP just added to compile
	}

	@Override
	public void makeTransientAll(Collection collection, boolean b) {
		//TODO HIP just added to compile
	}

	@Override
	public void retrieve(Object o, boolean b) {
		//TODO HIP just added to compile
	}

	/**
	 * Return all cached DbCode types.
	 *
	 * @return set of DbCode-Class types
	 * @deprecated
	 */
	public java.util.Set<Class<? extends DbCodeType>> getCodeTypes() {
		return codeTypes;
	}

	/**
	 * Get a unique technical id (T_Id) for a Persistency Object.
	 *
	 * @deprecated (@ see DbMapper # getNewId ())
	 */
	protected Long getNewId(Object po) {
		if (po instanceof DbObject) {
			if ((((DbObject) po).getId() == null) || (((DbObject) po).getId().longValue() < 0 /*
			 * temporary
			 * only
			 */)) {
				Long id = Long.valueOf(idCounter++);
				setUniqueId(po, id);
			}
			return ((DbObject) po).getId();
		} else {
			if (po != null) {
				Tracer.getInstance().developerError("non DbObject");
			}
			Long id = Long.valueOf(idCounter++);
			return id;
		}
	}

	@Override
	protected void allocate(DbTransaction trans, DbChangeableBean po) throws Exception {
		po.setLockFields(getUserId(), new java.util.Date());
		if (po.getPersistencyState().isNew()) {
			// set a UNIQUE ID
			getNewId(po);
			po.getPersistencyState().setNext(DbState.SAVED);
			// cache such objects
			// objectCache.put(pc.getClass(), (DbObject)pc);
		}

		// TODO
		// 1) 1:1 (as DbNlsString's)

		// 2) 1:n

		// 3) n:n
	}

	/**
	 * Write an XML instance file with given IliBasket's and all its Bean-attributes incl. subtree.
	 *
	 * @param pcs List of IliBasket
	 */
	@Override
	public Collection makePersistentAll(Collection pcs) {
		if ((getUserObject() != null) && (getUserObject() instanceof XmlUserObject)) {
			String filename = ((XmlUserObject) getUserObject()).getFilename();
			try {
				if (((XmlUserObject) getUserObject()).getMode() == XmlUserObject.MODE_XML) {
					java.io.Writer out = new java.io.BufferedWriter(new java.io.FileWriter(filename));
					XmlEncoder encoder = new XmlEncoder(this);
					encoder.encode(pcs.iterator(), out);
					out.close();
				} else {
					// XmlUserObject.MODE_DUMP
					java.io.FileOutputStream out = new java.io.FileOutputStream(filename);
					java.io.ObjectOutputStream s = new java.io.ObjectOutputStream(out);
					s.writeObject(pcs);
					s.flush();
					out.close();
				}
			} catch (Exception e) {
				throw new javax.jdo.JDODataStoreException("XmlObjectServer#makePersistentAll()", e);
			}
		} else {
			throw new ch.softenvironment.util.DeveloperException("XmlUserObject should have been setup");
		}

		//TODO HIP just added to compile
		return pcs;
	}

	/**
	 * Define a set of new DbCode's for a given Locale.
	 *
	 * @param dbCode concrete dbCode type
	 * @param nlsText Array of translations where each element of the array represents a single enumeration
	 * @param locale for e.g. Locale.GERMAN
	 */
	@Deprecated
	public java.util.List mapCodes(Class dbCode, String[] nlsText, java.util.Locale locale) throws Exception {
		for (int i = 0; i < nlsText.length; i++) {
			DbCode code = (DbCode) createInstance(dbCode);
			code.getName().setValue(nlsText[i], locale);
			code.save();
			cacheCode(code);
		}

		return retrieveCodes(dbCode);
	}

	/**
	 * Reset TID-Counter & uncache Codes.
	 */
	@Override
	public synchronized void reconnect() {
		// TODO do NOT restart counting Technical-ID (ONLY for OTHER Model ok)
		idCounter = MODEL_ROOT_ID.longValue();

		evictAll();
		codeTypes = new HashSet<Class<? extends DbCodeType>>();
	}

	@Override
	public synchronized void removeCode(DbCode code) {
		try {
			DbNlsString nls = code.getName();

			// remove code
			// evict(code); => don't remove all here
			Collection list = codeCache.getAll(code.getClass());
			list.remove(code);
			code.getPersistencyState().setNext(DbState.REMOVED);

			// remove its NLS-String
			evict(nls);
			nls.getPersistencyState().setNext(DbState.REMOVED);
		} catch (Exception e) {
			// TODO NLS
			throw new ch.softenvironment.util.UserException("Wahrscheinlich wird der zu loeschende Code noch irgendwo referenziert.", "Code nicht loeschbar", e);
		}
	}

	/**
	 * Read the given collection of Basket's from an XML-Instance, by means basket#setModelRoot() will be called.
	 *
	 * @param pcs List of IliBasket
	 */
	@Override
	public void retrieveAll(java.util.Collection pcs) {
		if ((getUserObject() != null) && (getUserObject() instanceof XmlUserObject)) {
			String filename = ((XmlUserObject) getUserObject()).getFilename();
			try {
				if (((XmlUserObject) getUserObject()).getMode() == XmlUserObject.MODE_XML) {
					XmlDecoder decoder = new XmlDecoder(this, ((XmlObjectServerFactory) getPersistenceManagerFactory()).getXMLReaderImpl());
					decoder.decode(pcs, filename);
				} else {
					// TODO extract specific Baskets in XmlUserObject.MODE_DUMP
					/*
					 * try { java.io.FileInputStream in = new
					 * java.io.FileInputStream(filename);
					 * java.io.ObjectInputStream s = new
					 * java.io.ObjectInputStream(in); Object object =
					 * s.readObject(); in.close();
					 *
					 * } catch(ClassNotFoundException e) {
					 */
					throw new javax.jdo.JDOObjectNotFoundException("XmlObjectServer#retrieveAll(MODE_DUMP)" /*
					 * ,
					 * e
					 */);
					// }
				}
			} catch (Exception e) {
				throw new javax.jdo.JDODataStoreException("XmlObjectServer#retrieveAll()", e);
			}
		} else {
			throw new ch.softenvironment.util.DeveloperException("XmlUserObject should have been setup");
		}
	}

	@Override
	public void retrieveAll(boolean b, Object... objects) {
		//TODO HIP just added to compile
	}

	@Override
	public void setDatastoreReadTimeoutMillis(Integer integer) {
		//TODO HIP just added to compile
	}

	@Override
	public Integer getDatastoreReadTimeoutMillis() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public void setDatastoreWriteTimeoutMillis(Integer integer) {
		//TODO HIP just added to compile
	}

	@Override
	public Integer getDatastoreWriteTimeoutMillis() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public boolean getDetachAllOnCommit() {
		//TODO HIP just added to compile
		return false;
	}

	@Override
	public void setDetachAllOnCommit(boolean b) {
		//TODO HIP just added to compile
	}

	@Override
	public boolean getCopyOnAttach() {
		//TODO HIP just added to compile
		return false;
	}

	@Override
	public void setCopyOnAttach(boolean b) {
		//TODO HIP just added to compile
	}

	@Override
	public <T> T detachCopy(T t) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public <T> Collection<T> detachCopyAll(Collection<T> collection) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public <T> T[] detachCopyAll(T... ts) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Object putUserObject(Object o, Object o1) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Object getUserObject(Object o) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Object removeUserObject(Object o) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public void flush() {
		//TODO HIP just added to compile
	}

	@Override
	public void checkConsistency() {
		//TODO HIP just added to compile
	}

	@Override
	public FetchPlan getFetchPlan() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public <T> T newInstance(Class<T> aClass) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Sequence getSequence(String s) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public JDOConnection getDataStoreConnection() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public void addInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener, Class... classes) {
		//TODO HIP just added to compile
	}

	@Override
	public void removeInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener) {
		//TODO HIP just added to compile
	}

	@Override
	public Date getServerDate() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Set getManagedObjects() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Set getManagedObjects(EnumSet<ObjectState> enumSet) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Set getManagedObjects(Class... classes) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Set getManagedObjects(EnumSet<ObjectState> enumSet, Class... classes) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public FetchGroup getFetchGroup(Class aClass, String s) {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public void setProperty(String s, Object o) {
		//TODO HIP just added to compile
	}

	@Override
	public Map<String, Object> getProperties() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public Set<String> getSupportedProperties() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public List<? extends DbCodeType> retrieveCodes(Class<? extends DbCodeType> dbCodeType, Locale locale) {
		Collection codes = codeCache.getAll(dbCodeType);
		if (codes == null) {
			Tracer.getInstance().runtimeWarning("expected code <" + dbCodeType + "> not available");
			return new ArrayList(); // PATCH
		} else {
			return new ArrayList(codes); // keep sort order
		}
	}

	@Override
	@Deprecated
	public DbEnumeration retrieveEnumeration(Class<? extends DbEnumeration> dbEnumeration, final String iliCode) throws Exception {
		List codes = retrieveCodes(dbEnumeration);
		Iterator<? extends DbEnumeration> iterator = codes.iterator();
		while (iterator.hasNext()) {
			DbEnumeration enumeration = iterator.next();
			if (DbEnumeration.isIliCode(enumeration, iliCode)) {
				return enumeration;
			}
		}
		Tracer.getInstance().developerWarning("Code not found with iliCode: " + iliCode);
		return null;
	}

	@Override
	@Deprecated
	public final void setUniqueId(Object object, Long id) {
		// TODO should not be public
		super.setUniqueId(object, id);
		if (idCounter <= id.longValue()) {
			// make sure next given id by #getNewId() won't be an existing one
			idCounter = id.longValue() + 1;
			return;
		}
		// TODO @deprecated => Bug fix for an older TCO-Tool Release (<V1.4.0)
		// where negative T_Id' might have been saved to XML-instance
		if (temporaryId >= id.longValue()) {
			// make sure next given id by #getNewTemporaryId() won't be an
			// existing one
			temporaryId = id.longValue() - 1;
		}
	}

	@Override
	public <T extends DbObject> List<T> retrieveAll(Class<T> dbObject, DbQueryBuilder builder, boolean cached) throws Exception {
		// TODO NYI
		throw new DeveloperException("NYI");
	}

	@Override
	public boolean checkConnection() {
		// since there is no real connection (file based), assume always true.
		return true;
	}
}
