package ch.softenvironment.jomm.mvc.model;

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

import ch.softenvironment.jomm.DbObjectId;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.implementation.DbState;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.NlsUtils;
import java.util.HashSet;
import javax.jdo.spi.StateManager;
import lombok.extern.slf4j.Slf4j;

/**
 * Behavior of a a persistent Object represented by appropriate entities in a database. Each such object: - has a unique Identity. - is managed by ONE ObjectServer Design Pattern: Data Access Object
 *
 * @author Peter Hirzel
 * @see javax.jdo.spi.PersistenceCapable
 */
@Slf4j
public abstract class DbObject implements javax.jdo.spi.PersistenceCapable {

	// The allocated ObjectServer handling this Object
	protected transient DbObjectServer objectServer = null;
	// Manage Persistence state
	protected transient DbState persistencyState = null;
	private transient boolean refreshed = false;
	// private transient boolean refreshing = false;

	// common Properties
	protected Long fieldId;
	protected java.lang.String fieldUserId;
	protected java.util.Date fieldLastChange = null;
	protected java.util.Date fieldCreateDate = null;

	// reuseable Properties/Attributes
	// TODO move to DbMapper
	public static final String PROPERTY_ACTIVE = "active";
	public static final String ATTRIBUTE_ACTIVE = "Active";
	public static final String PROPERTY_AMOUNT = "amount";
	public static final String PROPERTY_COUNTRY = "country";
	public static final String PROPERTY_CURRENCY = "currency";
	public static final String PROPERTY_DESCRIPTION = "description";
	protected static final String ATTRIBUTE_DESCRIPTION = "Description";
	public static final String PROPERTY_LANGUAGE = "language";
	public static final String PROPERTY_NAME = "name";
	protected static final String ATTRIBUTE_NAME = "Name";
	public static final String PROPERTY_NUMBER = "number";
	// "Number" might be used as a type in some DBMS!
	protected static final String ATTRIBUTE_Number = "Nr";
	public static final String PROPERTY_SHORT_NAME = "shortName";
	protected static final String ATTRIBUTE_SHORT_NAME = "ShortName";
	public static final String PROPERTY_TYPE = "type";
	// TODO Move somewhere else
	protected static final String ATTRIBUTE_TYPE = "T_Id_Type";
	// technical Properties
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_CREATE_DATE = "createDate";
	public static final String PROPERTY_LAST_CHANGE = "lastChange";
	public static final String PROPERTY_USER_ID = "userId";

	@SuppressWarnings("unused")
	private/* final */DbObject() {
		throw new DeveloperException("Use DbObjectServer#createInstance() instead");
	}

	/**
	 * DbObject constructor. Do NOT use this constructor in application. Creation of new transient Objects is part of the responsibility of DbObjectServer.
	 *
	 * @param server managing this instance
	 * @see DbObjectServer#createInstance(Class)
	 */
	protected DbObject(DbObjectServer server) {
		super();

		this.objectServer = server;
		persistencyState = new DbState();

		// will be overwritten if mapped from existing
		fieldCreateDate = new java.util.Date();
		// @see save()
		fieldLastChange = fieldCreateDate;
		fieldUserId = objectServer.getUserId();

		// build default DbNlsString's
		refresh(true);
	}

	/**
	 * Convert "PropertyName" to "propertyName".
	 */
	public static String convertName(final String property) {
		if (property != null) {
			if (!property.substring(0, 1).toLowerCase().equals(property.substring(0, 1))) {
				log.warn("Developer warning: Property must start as lowercase: {}", property);
				return property.substring(0, 1).toLowerCase() + property.substring(1);
			}
		}
		return property;
	}

	/**
	 * Object-Identity is kept by PROPERTY_ID.
	 *
	 * see javax.ejb.EJBObject#isIdentical(EJBObject)
	 */
	@Override
	public boolean equals(Object arg) {
		if ((arg != null) && (getClass() == arg.getClass() /*
		 * implies
		 * DbObject-type
		 */)) {
			if (getId() != null) {
				// compare by persistent Technical-Id
				return getId().equals(((DbObject) arg).getId());
			} else {
				log.error("Developer error: no #ID set in DbObject <{}> (use DbObjectServer#createInstance() instead of new DbObject())", arg);
				return super.equals(arg);
			}
		}

		return false;
	}

	/**
	 * Case A:B=n:n from the view of A or B. If owner is A then owner.property is a List pointing to B's via Map C.
	 *
	 * @return java.lang.Class the Class-type on the other AssociationEnd B
	 */
	public final Class<? extends DbObject> getAssociatedClass(java.lang.Class<? extends DbObject> owner, String property) {
		Class<? extends DbObject> dbRelationShipBean = getAssociationClass(owner, property);
		DbDescriptorEntry otherEntityBean = getObjectServer().getDescriptor(dbRelationShipBean).getEntry(property);
		return otherEntityBean.getBaseClass();
	}

	/**
	 * Case A:B=n:n from the view of A or B mapped by C. If owner is A then owner.property is a List pointing to B's via Map C.
	 *
	 * @return java.lang.Class the Class-type of Map C
	 */
	public final Class<? extends DbObject> getAssociationClass(java.lang.Class<? extends DbObject> owner, String property) {
		DbDescriptorEntry entryAssociation = getObjectServer().getDescriptor(owner).getEntry(property);
		if (entryAssociation == null) {
			throw new DeveloperException("owner <" + owner.getName() + " does not map property <" + property + ">!");
		}
		return entryAssociation.getBaseClass();
	}

	/**
	 * Gets the createDate property (java.util.Date) value.
	 *
	 * @return The createDate property value.
	 */
	public final java.util.Date getCreateDate() {
		return fieldCreateDate;
	}

	/**
	 * Return unique Object-Id.
	 *
	 * @return long
	 * see javax.ejb.EJBObject#getPrimaryKey()
	 */
	public final synchronized Long getId() {
		return fieldId;
	}

	/**
	 * Gets the lastChange property (java.util.Date) value. Useful timestamp for
	 * <b>optimistic Locking</b> and simple change history.
	 *
	 * @return The lastChange property value.
	 */
	public final java.util.Date getLastChange() {
		return fieldLastChange;
	}

	/**
	 * Return a speaking "state"-Message to a User. Depending on the PersistencyState either the state or a timestamp-userId will be returned.
	 *
	 * @return either pelastChange-UserId
	 */
	public final String getMark() {
		if (getPersistencyState().isNew() || getPersistencyState().isUndefined() || getPersistencyState().isRemoved()
			|| getPersistencyState().isRemovedPending()) {
			return getPersistencyState().toString();
		} else {
			if (getLastChange() == null) {
				log.warn("Developer warning: LastChange is null");
				return "<Timestamp>" + " " + getUserId();
			} else {
				return NlsUtils.formatDateTime(getLastChange()) + " " + getUserId();
			}
		}
	}

	/**
	 * Return the Server handling this instance.
	 */
	public final DbObjectServer getObjectServer() {
		return objectServer;
	}

	/**
	 * Return unique Object-Id.
	 *
	 * see javax.ejb.EJBObject#getPrimaryKey()
	 * @return long
	 * @see ch.softenvironment.jomm.DbIdentity
	 * @deprecated
	 */
	public final DbObjectId getOid() {
		return new DbObjectId(getClass(), getId());
	}

	/**
	 * Return the internal, persistence State of this DbObject.
	 */
	public final DbState getPersistencyState() {
		return persistencyState;
	}

	/**
	 * Gets the userId property (java.lang.String) value. Useful for
	 * <b>optimistic Locking</b> and simple change history.
	 *
	 * @return The userId property value.
	 */
	public final java.lang.String getUserId() {
		return fieldUserId;
	}

	/**
	 * Only checks for correct instantiation.
	 */
	@Override
	public final int hashCode() {
		if (getId() == null) {
			log.error("Developer error: DbObject <{}> instantiated by Constructor => use DbObjectServer#createInstance(Class) instead!", getClass());
		}

		return super.hashCode();
	}

	/**
	 * Return whether the given Class-Type is DbCode or DbEnumeration resp. implements DbCodeType.
	 */
	public static boolean isCode(Class<? extends DbObject> dbObject) {
		Class<?> parent = dbObject.getSuperclass();

		while ((parent != null) && (!parent.equals(DbObject.class))) {
			// if (parent.equals(DbEnumeration.class) ||
			// parent.equals(DbCode.class)) {
			Class<?>[] interfaces = parent.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				if (interfaces[i].equals(DbCodeType.class)) {
					return true;
				}
			}
			parent = parent.getSuperclass();
		}
		return false;
	}

	/**
	 * Return whether Object was changed since last persistent save.
	 */
	public boolean isModified() {
		return false;
	}

	/**
	 * Return whether Object was read at least ONCE from Database or not.
	 */
	public final boolean isRefreshed() {
		return refreshed;
	}

	/**
	 * Copy field values from another instance of the same class to this instance.
	 * <p>
	 * This method will throw an exception if the other instance is not managed by the same StateManager as this instance.
	 *
	 * @param other the PC instance from which field values are to be copied
	 * @param fieldNumbers the field numbers to be copied into this instance
	 */
	@Override
	public void jdoCopyFields(java.lang.Object other, int[] fieldNumbers) {
	}

	/**
	 * Copy fields to an outside source from the key fields in the ObjectId. This method is generated in the PersistenceCapable class to generate a call to the field manager for each key field in the
	 * ObjectId. For example, an ObjectId class that has three key fields (int id, String name, and Float salary) would have the method generated:
	 * <p>
	 * void copyKeyFieldsFromObjectId
	 * <p>
	 * (ObjectIdFieldConsumer fm, Object objectId) {
	 * <p>
	 * EmployeeKey oid = (EmployeeKey)objectId;
	 * <p>
	 * fm.storeIntField (0, oid.id);
	 * <p>
	 * fm.storeStringField (1, oid.name);
	 * <p>
	 * fm.storeObjectField (2, oid.salary);
	 * <p>
	 * <p>
	 * The implementation is responsible for implementing the ObjectIdFieldManager to store the values for the key fields.
	 *
	 * @param oid the ObjectId source of the copy.
	 * @param fm the field manager that receives the field values.
	 */
	@Override
	public void jdoCopyKeyFieldsFromObjectId(javax.jdo.spi.PersistenceCapable.ObjectIdFieldConsumer fm, java.lang.Object oid) {
	}

	/**
	 * Copy fields from this PersistenceCapable instance to the Object Id instance.
	 *
	 * @param oid the ObjectId target of the key fields
	 */
	@Override
	public void jdoCopyKeyFieldsToObjectId(java.lang.Object oid) {
	}

	/**
	 * Copy fields from an outside source to the key fields in the ObjectId. This method is generated in the PersistenceCapable class to generate a call to the field manager for each key field in the
	 * ObjectId. For example, an ObjectId class that has three key fields (int id, String name, and Float salary) would have the method generated:
	 * <p>
	 * void jdoCopyKeyFieldsToObjectId
	 * <p>
	 * (ObjectIdFieldSupplier fm, Object objectId) {
	 * <p>
	 * EmployeeKey oid = (EmployeeKey)objectId;
	 * <p>
	 * oid.id = fm.fetchIntField (0);
	 * <p>
	 * oid.name = fm.fetchStringField (1);
	 * <p>
	 * oid.salary = fm.fetchObjectField (2);
	 * <p>
	 * <p>
	 * The implementation is responsible for implementing the ObjectIdFieldSupplier to produce the values for the key fields.
	 *
	 * @param oid the ObjectId target of the copy.
	 * @param fm the field supplier that supplies the field values.
	 */
	@Override
	public void jdoCopyKeyFieldsToObjectId(javax.jdo.spi.PersistenceCapable.ObjectIdFieldSupplier fm, java.lang.Object oid) {
	}

	/**
	 * Return a copy of the JDO identity associated with this instance.
	 *
	 * <p>
	 * Persistent instances of PersistenceCapable classes have a JDO identity managed by the PersistenceManager. This method returns a copy of the ObjectId that represents the JDO identity.
	 *
	 * <p>
	 * Transient instances return null.
	 *
	 * <p>
	 * The ObjectId may be serialized and later restored, and used with a PersistenceManager from the same JDO implementation to locate a persistent instance with the same data store identity.
	 *
	 * <p>
	 * If the JDO identity is managed by the application, then the ObjectId may be used with a PersistenceManager from any JDO implementation that supports the PersistenceCapable class.
	 *
	 * <p>
	 * If the JDO identity is not managed by the application or the data store, then the ObjectId returned is only valid within the current transaction.
	 * <p>
	 * If the JDO identity is being changed in the transaction, this method returns the object id as of the beginning of the current transaction.
	 *
	 * @return a copy of the ObjectId of this instance as of the beginning of the transaction.
	 * @see javax.jdo.PersistenceManager#getObjectId(Object pc)
	 * @see javax.jdo.PersistenceManager#getObjectById(Object oid, boolean validate)
	 */
	@Override
	public java.lang.Object jdoGetObjectId() {
		// known within application only
		return getClass().getName() + "#" + getId();
	}

	/**
	 * Return the associated PersistenceManager if there is one. Transactional and persistent instances return the associated PersistenceManager.
	 *
	 * <p>
	 * Transient non-transactional instances return null.
	 * <p>
	 * This method always delegates to the StateManager if it is non-null.
	 *
	 * @return the PersistenceManager associated with this instance.
	 */
	@Override
	public javax.jdo.PersistenceManager jdoGetPersistenceManager() {
		return getObjectServer();
	}

	/**
	 * Return a copy of the JDO identity associated with this instance. This method is the same as jdoGetObjectId if the identity of the instance has not changed in the current transaction.
	 * <p>
	 * If the JDO identity is being changed in the transaction, this method returns the current object id as modified in the current transaction.
	 *
	 * @return a copy of the ObjectId of this instance as modified in the transaction.
	 * @see #jdoGetObjectId()
	 * @see javax.jdo.PersistenceManager#getObjectId(Object pc)
	 * @see javax.jdo.PersistenceManager#getObjectById(Object oid, boolean validate)
	 */
	@Override
	public java.lang.Object jdoGetTransactionalObjectId() {
		return null;
	}

	/**
	 * Tests whether this object has been deleted.
	 * <p>
	 * Instances that have been deleted in the current transaction return true.
	 *
	 * <p>
	 * Transient instances return false.
	 * <p>
	 *
	 * @return true if this instance was deleted in the current transaction.
	 * @see javax.jdo.JDOHelper#isDeleted(Object pc)
	 * @see javax.jdo.PersistenceManager#deletePersistent(Object pc)
	 */
	@Override
	public boolean jdoIsDeleted() {
		return getPersistencyState().isRemoved() || getPersistencyState().isRemovedPending();
	}

	/**
	 * Tests whether this object is dirty.
	 * <p>
	 * Instances that have been modified, deleted, or newly made persistent in the current transaction return true.
	 *
	 * <p>
	 * Transient instances return false.
	 * <p>
	 *
	 * @return true if this instance has been modified in the current transaction.
	 * @see javax.jdo.JDOHelper#isDirty(Object pc)
	 * @see javax.jdo.JDOHelper#makeDirty(Object pc, String fieldName)
	 * @see #jdoMakeDirty(String fieldName)
	 */
	@Override
	public boolean jdoIsDirty() {
		return !((getPersistencyState().isSaved() || getPersistencyState().isReadOnly()) && isRefreshed());
	}

	/**
	 * Tests whether this object has been newly made persistent.
	 * <p>
	 * Instances that have been made persistent in the current transaction return true.
	 *
	 * <p>
	 * Transient instances return false.
	 * <p>
	 *
	 * @return true if this instance was made persistent in the current transaction.
	 * @see javax.jdo.JDOHelper#isNew(Object pc)
	 * @see javax.jdo.PersistenceManager#makePersistent(Object pc)
	 */
	@Override
	public boolean jdoIsNew() {
		return getPersistencyState().isNew() || getPersistencyState().isNewForwarded();
	}

	/**
	 * Tests whether this object is persistent. Instances that represent persistent objects in the data store return true.
	 *
	 * @return true if this instance is persistent.
	 * @see javax.jdo.JDOHelper#isPersistent(Object pc)
	 * @see javax.jdo.PersistenceManager#makePersistent(Object pc)
	 */
	@Override
	public boolean jdoIsPersistent() {
		return getPersistencyState().isPersistent();
	}

	/**
	 * Tests whether this object is transactional.
	 * <p>
	 * Instances whose state is associated with the current transaction return true.
	 *
	 * <p>
	 * Transient instances return false.
	 * <p>
	 *
	 * @return true if this instance is transactional.
	 * @see javax.jdo.JDOHelper#isTransactional(Object pc)
	 * @see javax.jdo.PersistenceManager#makeTransactional(Object pc)
	 */
	@Override
	public boolean jdoIsTransactional() {
		return getObjectServer().isTransactional(this);
	}

	/**
	 * Explicitly mark this instance and this field dirty. Normally, PersistenceCapable classes are able to detect changes made to their fields. However, if a reference to an array is given to a
	 * method outside the class, and the array is modified, then the persistent instance is not aware of the change. This API allows the application to notify the instance that a change was made to a
	 * field.
	 *
	 * <p>
	 * The field name should be the fully qualified name, including package name and class name of the class declaring the field. This allows unambiguous identification of the field to be marked
	 * dirty. If multiple classes declare the same field, and if the package and class name are not provided by the parameter in this API, then the field marked dirty is the field declared by the most
	 * derived class.
	 * <p>
	 * Transient instances ignore this method.
	 * <p>
	 *
	 * @param fieldName the name of the field to be marked dirty.
	 */
	@Override
	public void jdoMakeDirty(java.lang.String fieldName) {
		throw new DeveloperException("A ReadOnly-DbObject must not be changed!");
	}

	/**
	 * Return a new instance of this class, with the jdoStateManager set to the parameter, and jdoFlags set to LOAD_REQUIRED.
	 * <p>
	 * This method is used as a performance optimization as an alternative to using reflection to construct a new instance. It is used by the JDOImplHelper class method newInstance.
	 *
	 * @param sm the StateManager that will own the new instance.
	 * @return a new instance of this class.
	 * @see javax.jdo.spi.JDOImplHelper#newInstance(Class, StateManager)
	 */
	@Override
	public javax.jdo.spi.PersistenceCapable jdoNewInstance(javax.jdo.spi.StateManager sm) {
		return null;
	}

	/**
	 * Return a new instance of this class, with the jdoStateManager set to the parameter, key fields initialized to the values in the oid, and jdoFlags set to LOAD_REQUIRED.
	 * <p>
	 * This method is used as a performance optimization as an alternative to using reflection to construct a new instance of a class that uses application identity. It is used by the JDOImplHelper
	 * class method newInstance.
	 *
	 * @param sm the StateManager that will own the new instance.
	 * @param oid an instance of the object id class (application identity).
	 * @return a new instance of this class.
	 * @see javax.jdo.spi.JDOImplHelper#newInstance(Class pcClass, StateManager sm)
	 */
	@Override
	public javax.jdo.spi.PersistenceCapable jdoNewInstance(javax.jdo.spi.StateManager sm, java.lang.Object oid) {
		return null;
	}

	/**
	 * Create a new instance of the ObjectId class for this PersistenceCapable class. The fields will have their Java default values.
	 *
	 * @return the new instance created.
	 */
	@Override
	public java.lang.Object jdoNewObjectIdInstance() {
		return new DbObjectId(getClass(), null);
	}

	/**
	 * Create a new instance of the ObjectId class for this PersistenceCapable class, using the String form of the constructor. The fields will have their Java default values.
	 *
	 * @param str the String form of the object identity instance
	 * @return the new instance created.
	 */
	public java.lang.Object jdoNewObjectIdInstance(java.lang.String str) {
		return new DbObjectId(getClass(), Long.valueOf(str));
	}

	/**
	 * The owning StateManager uses this method to ask the instance to provide the value of the single field identified by fieldNumber.
	 *
	 * @param fieldNumber the field whose value is to be provided by a callback to the StateManager's providedXXXField method
	 */
	@Override
	public void jdoProvideField(int fieldNumber) {
	}

	/**
	 * The owning StateManager uses this method to ask the instance to provide the values of the multiple fields identified by fieldNumbers.
	 *
	 * @param fieldNumbers the fields whose values are to be provided by multiple callbacks to the StateManager's providedXXXField method
	 */
	@Override
	public void jdoProvideFields(int[] fieldNumbers) {
	}

	/**
	 * The owning StateManager uses this method to ask the instance to replace the value of the single field identified by number.
	 *
	 * @param fieldNumber the field whose value is to be replaced by a callback to the StateManager's replacingXXXField method
	 */
	@Override
	public void jdoReplaceField(int fieldNumber) {
	}

	/**
	 * The owning StateManager uses this method to ask the instance to replace the values of the multiple fields identified by number.
	 *
	 * @param fieldNumbers the fields whose values are to be replaced by multiple callbacks to the StateManager's replacingXXXField method
	 */
	@Override
	public void jdoReplaceFields(int[] fieldNumbers) {
	}

	/**
	 * The owning StateManager uses this method to ask the instance to replace the value of the flags by calling back the StateManager replacingFlags method.
	 */
	@Override
	public void jdoReplaceFlags() {
	}

	/**
	 * This method sets the StateManager instance that manages the state of this instance. This method is normally used by the StateManager during the process of making an instance persistent,
	 * transient, or transactional.
	 * <p>
	 * The caller of this method must have JDOPermission for the instance, if the instance is not already owned by a StateManager. If the parameter is null, and the StateManager approves the change,
	 * then the jdoFlags field will be reset to READ_WRITE_OK. If the parameter is not null, and the security manager approves the change, then the jdoFlags field will be reset to LOAD_REQUIRED.
	 *
	 * @param sm The StateManager which will own this instance, or null to reset the instance to transient state
	 * @throws SecurityException if the caller does not have JDOPermission
	 * @see javax.jdo.spi.JDOPermission
	 */
	@Override
	public void jdoReplaceStateManager(javax.jdo.spi.StateManager sm) throws java.lang.SecurityException {
	}

	/**
	 * Refresh this Object (reread from Database). If forced the Object is always refreshed immediately from Database. If not forced the Object will only be refreshed, if not yet read once at least.
	 *
	 * @param forced true: actualize from database; false: lazy initialization only
	 */
	public final synchronized void refresh(boolean forced) throws IllegalStateException {
		if (/* (!refreshing) && */((!isRefreshed()) || forced)) {
			// refreshing = true; // prevent recursive refreshing
			try {
				getObjectServer().refresh(this);
			} catch (Exception e) {
				log.error("", e);
				throw new IllegalStateException(e.getLocalizedMessage());
			}
			// refreshing = false;
		}
	}

	/**
	 * Reset internal state <b>of persistent DbObject's</b> to SAVED or READ_ONLY.
	 *
	 * @param refreshed (true->This Object was read at least once COMPLETELY from Target-System)
	 */
	public synchronized void reset(boolean refreshed) {
		if ((getId() == null) || (getId().longValue() < 0 /*
		 * temporaries are not
		 * persistent
		 */)) {
			// IMPORTANT: prevent UPDATE without specific Id!
			log.warn("Developer warning: cannot reset because Id not set");
		} else {
			if (this instanceof DbChangeableBean) {
				getPersistencyState().setNext(DbState.SAVED);
			} else {
				getPersistencyState().setNext(DbState.READ_ONLY);
			}
		}

		if (refreshed) {
			// TODO NASTY: should be get out of DbState
			this.refreshed = true;
		}
	}

	/**
	 * Return the set of properties differing in given object.
	 *
	 * @param object
	 * @return
	 */
	public java.util.Set<String> diff(DbObject object) throws Exception {
		Class current = this.getClass();
		if ((object == null) || (object.getClass() != current)) {
			throw new IllegalArgumentException("given object not of type: " + current);
		}
		java.util.Set<String> changedProperties = new HashSet<String>();
		while ((!current.equals(DbEntityBean.class)) && (!current.equals(DbRelationshipBean.class)) && (!current.equals(DbChangeableBean.class))
			&& (!current.equals(DbCode.class))) {
			java.util.Iterator<String> iterator = this.getObjectServer().getDescriptor(current).iteratorProperties();
			// last chance to refresh
			this.refresh(true);
			object.refresh(true);
			while (iterator.hasNext()) {
				String property = iterator.next();
				if (property.equals(DbObject.PROPERTY_ID) || property.equals(DbObject.PROPERTY_LAST_CHANGE) || property.equals(DbObject.PROPERTY_USER_ID)
					|| property.equals(DbObject.PROPERTY_CREATE_DATE)) {
					// skip technical fields: means no change
					continue;
				}
				BeanReflector thisReflector = new BeanReflector(this, property);
				BeanReflector objectReflector = new BeanReflector(object, property);
				if (thisReflector.getValue() == null) {
					if (objectReflector.getValue() != null) {
						changedProperties.add(property);
					}
				} else if (!thisReflector.getValue().equals(objectReflector.getValue())) {
					changedProperties.add(property);
				}
			}
			// diff also parent's properties
			current = current.getSuperclass();
		}
		// TODO diff aggregates?
		return changedProperties;
	}

	@Override
	public Object jdoGetVersion() {
		//TODO HIP just added to compile
		return null;
	}

	@Override
	public boolean jdoIsDetached() {
		//TODO HIP just added to compile
		return false;
	}

	@Override
	public Object jdoNewObjectIdInstance(Object o) {
		//TODO HIP just added to compile
		return null;
	}
}
