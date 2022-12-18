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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.implementation.DbState;
import ch.softenvironment.jomm.mvc.controller.DbObjectListener;
import ch.softenvironment.util.DeveloperException;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Persistent Object type with Writable Character. In an ejb Architecture these Objects are also called EntityBean's.
 *
 * @author Peter Hirzel
 */
@Slf4j
public abstract class DbChangeableBean extends DbObject implements Cloneable {

	// keep reference to DbObjectListener registered
	private transient Set<DbObjectListener> objectListener = null;
	// fire bean changes
	private transient java.beans.PropertyChangeSupport propertyChange = null;
	// keep changed attributes in mind
	private transient java.util.Set<String> changedProperties = new java.util.HashSet<String>(); // History
	// @see #remove(boolean, boolean)
	private boolean removeObjectHistoryPending = false;

	/**
	 * @see DbObject(DbObjectServer)
	 */
	protected DbChangeableBean(DbObjectServer objectServer) {
		super(objectServer);

		getPersistencyState().setNext(DbState.NEW);
	}

	/**
	 * Utility method to add an aggregate to the list given by propertyName. Also promote any registered listeners to the aggregate.
	 *
	 * @param propertyName
	 * @param aggregate
	 */
	public void addAggregate(final String propertyName, DbObject aggregate) throws Exception {
		// 1) add listeners to new aggregate
		java.util.Iterator<DbObjectListener> iterator = iteratorObjectListener();
		if (iterator != null) {
			while (iterator.hasNext()) {
				promoteChangeListener(aggregate, iterator.next(), true);
			}
		}

		// 2) add the aggregate to the list
		DbPropertyChange change = new DbPropertyChange(this, propertyName);
		((List) change.getValue()).add(aggregate);
	}

	/**
	 * Utility method to remove an aggregate from the list given by propertyName. Also promote any registered listeners to the aggregate.
	 *
	 * @param propertyName
	 * @param aggregate
	 */
	public void removeAggregate(final String propertyName, DbChangeableBean aggregate) throws Exception {
		// 1) remove listeners to removable aggregate
		java.util.Iterator<DbObjectListener> iterator = iteratorObjectListener();
		if (iterator != null) {
			while (iterator.hasNext()) {
				promoteChangeListener(aggregate, iterator.next(), false);
			}
		}
		// 2) remove the aggregate to the list
		DbPropertyChange change = new DbPropertyChange(this, propertyName);
		((List) change.getValue()).remove(aggregate);

		// TODO 1 no persistent removal happens yet => provide at caller
		// aggregate.remove(false);
	}

	/**
	 * The addPropertyChangeListener method was generated to support the propertyChange field. Add an "outer" listener listening to this persistent Object.
	 *
	 * @see java.beans Specification
	 * @see #removePropertyChangeListener(PropertyChangeListener)
	 */
	public final synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().addPropertyChangeListener(listener);

		// keep DbObjectListener in mind for further Consistency-Checks
		if (listener instanceof DbObjectListener) {
			if (objectListener == null) {
				// unfortunately propertyChange#listener is not visible here
				objectListener = new HashSet<>();
			}
			if (!objectListener.contains(listener)) {
				// be aware of aggregation ping-pong
				objectListener.add((DbObjectListener) listener);
				defineConsistencyChecks((DbObjectListener) listener, true);
				((DbObjectListener) listener).resetActions();
			}
		}
	}

	/**
	 * Consistency Check of this EntityBean. This method is called via Setter-Method resp. #firePropertyChange(..). Keeps related testId in mind for update if testId is a property of this object
	 *
	 * @param testId Identification of the consistencyTest (usually the name of the property to be changed)
	 * @see #firePropertyChange(String, Object, Object)
	 */
	protected void check(String testId) {
		jdoMakeDirty(testId);
	}

	/**
	 * Use this method if testId and property are two attributes dependent of each other in point of consistency. Therefore if property has changed, any validation is expected to be done by testId's
	 * consistency checks.
	 * <p>
	 * Any registered {@link DbObjectListener} will be informed that testId must be validated.
	 *
	 * @param testId Identification of the main consistencyTest (usually the name of the property to be changed)
	 * @param property The effective property that was changed
	 * @see #check(String)
	 */
	protected void check(String testId, String property) {
		check(property);

		if (objectListener != null) {
			// notify them to change dependent ConsistencyTest's
			java.util.Iterator<DbObjectListener> iterator = objectListener.iterator();
			while (iterator.hasNext()) {
				DbObjectListener listener = iterator.next();
				listener.propertyChange(new java.beans.PropertyChangeEvent(this, testId, null, null));
			}
		}
	}

	/**
	 * Define a Set of ConsistencyChecks for this DbObject generically out of Descriptor. Promote the listener to all aggregates and super-hierarchy.
	 *
	 * @param register (true=>register checks; false=>unregister checks)
	 */
	private final void defineConsistencyChecks(DbObjectListener listener, boolean register) {
		//TODO move method to DbObjectListener
		Class current = getClass();
		while ((!current.equals(DbEntityBean.class)) && (!current.equals(DbRelationshipBean.class)) && (!current.equals(DbChangeableBean.class))
			&& (!current.equals(DbCode.class))) {
			DbDescriptor descriptor = getObjectServer().getDescriptor(current);
			// 1) check for changed local properties
			Iterator<String> properties = descriptor.iteratorFlatProperties();
			while (properties.hasNext()) {
				String property = properties.next();
				DbDescriptorEntry entry = descriptor.getEntry(property);
				// add property only once if several inconsistency-possibilities
				if (entry.isMandatory() ||
					// String-length might be restricted
					(entry.getFieldType() instanceof DbTextFieldDescriptor) ||
					// Number-range might be restricted
					(entry.getFieldType() instanceof DbNumericFieldDescriptor)) {
					if (register) {
						listener.addTest(new DbPropertyChange(this, property), null);
					} else {
						listener.removeTest(new DbPropertyChange(this, property));
					}
				}
			}

			// 2) check for code's or 1:1 properties
			properties = descriptor.iteratorAggregatedProperties();
			while ((properties != null) && properties.hasNext()) {
				String property = properties.next();
				DbPropertyChange change = new DbPropertyChange(this, property);
				DbDescriptorEntry entry = descriptor.getEntry(property);
				if (entry.isMandatory()) {
					if (register) {
						listener.addTest(change, null);
					} else {
						listener.removeTest(change);
					}
				}
				promoteChangeListener(change, listener, register);
			}

			// DbNlsString properties
			properties = descriptor.iteratorNlsStringProperties();
			while ((properties != null) && properties.hasNext()) {
				String property = properties.next();
				DbPropertyChange change = new DbPropertyChange(this, property);
				DbDescriptorEntry entry = descriptor.getEntry(property);
				if (entry.isMandatory()) {
					if (register) {
						listener.addTest(change, null);
					} else {
						listener.removeTest(change);
					}
				}
				promoteChangeListener(change, listener, register);
			}

			// 1:n properties
			properties = descriptor.iteratorOneToManyProperties();
			while ((properties != null) && properties.hasNext()) {
				String property = properties.next();
				DbPropertyChange change = new DbPropertyChange(this, property);
				DbDescriptorEntry entry = descriptor.getEntry(property);
				if (entry.isMandatory()) {
					if (register) {
						listener.addTest(change, null);
					} else {
						listener.removeTest(change);
					}
				}
				// promote Listener among all instances
				List<?> list = null;
				try {
					list = (List<?>) change.getValue();
					if (list == null) {
						list = new ArrayList();
						change.setProperty(list);
					}
				} catch (Exception e) {
					throw new DeveloperException("property listener problem", null, e);
				}
				Iterator<?> iterator = list.iterator();
				while (iterator.hasNext()) {
					promoteChangeListener(iterator.next(), listener, register);
				}
			}
			// register also parent's consistency conditions
			current = current.getSuperclass();
		}
	}

	/**
	 * Support the propertyChange field. Call this method in each bound
	 * <b>setter(Object myArg)</b>.
	 */
	public final void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
		check(propertyName);

		if ((oldValue != null) && (oldValue instanceof DbNlsString)) {
			// special case for DbNlsString => reference to owner
			if (((DbNlsString) oldValue).getPersistencyState().isPersistent()) {
				((DbNlsString) oldValue).setChange(null);
				if (!oldValue.equals(newValue)) {
					// TODO remove at next this.save()
					((DbNlsString) oldValue).getPersistencyState().setNext(DbState.REMOVED_PENDING);
				}
			} else {
				if (!oldValue.equals(newValue)) {
					// no more in use
					((DbNlsString) oldValue).getPersistencyState().setNext(DbState.REMOVED);
				} // else reassign
			}
		}
		if ((newValue != null) && (newValue instanceof DbNlsString)) {
			((DbNlsString) newValue).setChange(new DbPropertyChange(this, propertyName));
		}

		getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);

		if ((newValue instanceof List) && getPersistencyState().isChanged() && (newValue != null) && newValue.equals(oldValue)) {
			// TODO Hack/Bug fix: cheat ConsistencyController#propertyChange()
			// because event gets absorbed otherwise (in case ONLY element in
			// List has changed)
			// HACK @see JavaBeans Specification chapter "7.3 indexed arrays"
			// => getList returns a copy of the items
			Iterator<DbObject> it = ((List) newValue).iterator();
			while (it.hasNext()) {
				// @see DbBaseFrame#removeAssociations()
				if (it.next().getPersistencyState().isRemovedPending()) {
					log.warn("Developer warning: Cheating changed-event for List<{}> because REMOVED_PENDING", propertyName);
					getPropertyChange().firePropertyChange(propertyName, null /*
					 * CHEAT
					 * propertyChange
					 * -
					 * handler
					 */, newValue);
					break;
				}
			}
		}
	}

	/**
	 * Support the propertyChange field. Call this method in each bound
	 * <b>setter(boolean myArg)</b>.
	 *
	 * @deprecated (use java.lang.Boolean instead of primitive type)
	 */
	@Deprecated
	public final void firePropertyChange(java.lang.String propertyName, boolean oldValue, boolean newValue) {
		check(propertyName);
		getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Forward this DbChangeableBean to another DbObjectServer, by means it does not exist yet on targetServer. This DbChangeableBean will become persistencyState=>NewForwarded (transient) on
	 * targetServer. Also forwards any superclass instances.
	 *
	 * @param targetServer
	 * @param includingAggregates (false->only this object without any related aggregates; true->this object AND aggregates)
	 */
	public/* final */void forward(DbObjectServer targetServer, boolean includingAggregates) throws Exception {
		if (includingAggregates) {
			// TODO NYI: what happens to deep hierarchy? forward aggregates as
			// well
			throw new DeveloperException("forwarding aggregates is NOT IMPLEMENTED YET, do overwrite if you need such functionality");
		}
		// last chance to do refresh from original server
		refresh(true);

		// remove from cache => points on wrong server otherwise
		getObjectServer().evict(this);

		objectServer = targetServer;
		getPersistencyState().setNext(DbState.NEW_FORWARDED);

		// TODO currently ID remains the same on both servers, always ok?
		save();
	}

	/**
	 * Return all changed Properties since last save.
	 */
	public final Set<String> getChangedProperties() {
		return changedProperties;
	}

	/**
	 * Support JavaBeans Specification. Changes will be signaled to any Listener.
	 *
	 * @see #firePropertyChange(String, Object, Object)
	 */
	private final java.beans.PropertyChangeSupport getPropertyChange() {
		if (propertyChange == null) {
			propertyChange = new java.beans.PropertyChangeSupport(this);
		}
		return propertyChange;
	}

	/**
	 * The hasListeners method was generated to support the propertyChange field.
	 *
	 * @deprecated
	 */
	public final synchronized boolean hasListeners(java.lang.String propertyName) {
		return getPropertyChange().hasListeners(propertyName);
	}

	/**
	 * Return whether Object was changed since last persistent save.
	 */
	@Override
	public boolean isModified() {
		return (changedProperties.size() > 0) || getPersistencyState().isNewForwarded(); // =>
		// nothing
		// might
		// have
		// changed
		// on
		// new
		// server,
		// but
		// meant
		// to
		// be
		// changed
	}

	/**
	 * Return whether the deletion history is not yet written.
	 *
	 * @return
	 */
	public boolean isRemoveObjectHistoryPending() {
		return removeObjectHistoryPending;
	}

	/**
	 * Return an Iterator of all changed Properties since last save.
	 */
	public final Iterator<String> iteratorChangedProperties() {
		return changedProperties.iterator();
	}

	/**
	 *
	 */
	protected final synchronized Iterator<DbObjectListener> iteratorObjectListener() {
		if (objectListener == null) {
			return null;
		} else {
			return objectListener.iterator();
		}
	}

	/**
	 * @param fieldName Property changed
	 * @see #check(String)
	 */
	@Override
	public final void jdoMakeDirty(java.lang.String fieldName) {
		// Keep changed Properties in mind to optimize later UPDATE for
		// efficiency.
		// Tracer.getInstance().debug(this, "jdoMakeDirty(String)",
		// "property changed/historized: <" + property + ">");
		getPersistencyState().setNext(DbState.CHANGED);
		changedProperties.add(fieldName /* property */);
	}

	/**
	 * Utility method to add the Listener to given object.
	 *
	 * @listener
	 */
	protected static void promoteChangeListener(Object object, DbObjectListener listener, boolean register) {
		try {
			Object value = object;
			if (object instanceof DbPropertyChange) {
				// NASTY: object is a multi-meaning parameter
				value = ((DbPropertyChange) object).getValue();
			}

			if ((value != null) && (value instanceof DbChangeableBean /*
			 * !DbObject
			 * . isCode(
			 * value
			 * .getClass
			 * ())
			 */)) {
				if (register) {
					((DbChangeableBean) value).addPropertyChangeListener(listener);
				} else {
					((DbChangeableBean) value).removePropertyChangeListener(listener);
				}
			}
		} catch (Exception e) {
			log.error("Could not promote Listener to aggregate", e);
		}
	}

	/**
	 * Delete this Object persistently.
	 *
	 * @param immediately true->remove immediately; false->delay removal until next #save()
	 */
	public synchronized void remove(boolean immediately) throws Exception {
		remove(immediately, false);
	}

	/**
	 * With spread databases it might be useful to historize deleted objects just in case of further synchronisation actions later.
	 *
	 * @param immediately
	 * @param historized (true->write history record)
	 * @see #remove(boolean)
	 */
	public synchronized void remove(boolean immediately, boolean historized) throws Exception {
		if (getPersistencyState().isNew() || getPersistencyState().isUndefined() || getPersistencyState().isRemoved()) {
			// no persistence actions necessary
			// => always treat immediately
			getPersistencyState().setNext(DbState.REMOVED);
		} else if (getPersistencyState().isSaved() || getPersistencyState().isChanged() || getPersistencyState().isRemovedPending()) {
			if (immediately) {
				// delete in database immediately
				if (historized) {
					List<String> deletions = new ArrayList<String>(2);
					// add history entry
					deletions.add(DbQueryBuilder.createChangeRemoveHistory(this).getQuery());

					// delete the object
					// jdoGetPersistenceManager().deletePersistent(this);
					getObjectServer().evict(this);
					deletions.add(DbQueryBuilder.createRemoveObject(this).getQuery());

					getObjectServer().execute("Remove historized object", deletions);

					getPersistencyState().setNext(DbState.REMOVED);
				} else {
					jdoGetPersistenceManager().deletePersistent(this);
					getPersistencyState().setNext(DbState.REMOVED);
				}
			} else {
				removeObjectHistoryPending = historized; // make a
				// remove-history entry
				// later at save
				// delay removal until next #save()
				getPersistencyState().setNext(DbState.REMOVED_PENDING);
			}
		} else {
			throw new DeveloperException("Object is ReadOnly!", "Deletion-Error");
		}
	}

	/**
	 * The removePropertyChangeListener method was generated to support the propertyChange field. Remove an "outer" listener listening from this persistent Object.
	 *
	 * @see java.beans Specification
	 * @see #addPropertyChangeListener(PropertyChangeListener)
	 */
	public final synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().removePropertyChangeListener(listener);

		if ((objectListener != null) && (objectListener.contains(listener))) {
			// be aware of aggregation ping-pong
			objectListener.remove(listener);
			defineConsistencyChecks((DbObjectListener) listener, false);
		}
	}

	/**
	 * @see #save()
	 * @see #refresh(boolean)
	 */
	@Override
	public final synchronized void reset(boolean refreshed) {
		super.reset(refreshed);

		changedProperties = new HashSet<String>();

		Iterator<DbObjectListener> iterator = iteratorObjectListener();
		if (iterator != null) {
			while (iterator.hasNext()) {
				iterator.next().resetActions();
			}
		}
	}

	/**
	 * Save this Object persistently.
	 *
	 * see javax.ejb.EntityBean#ejbStore()
	 */
	public synchronized void save() throws Exception {
		getObjectServer().makePersistent(this);
	}

	/**
	 * Set the Technical Fields (lastChange and UserId) relevant for Locking. This method should be called from DbObjectServer only!
	 *
	 * @deprecated
	 */
	public final void setLockFields(String userId, java.util.Date lastChange) {
		fieldUserId = userId;
		fieldLastChange = lastChange;
		// signal change stamp
		getPropertyChange().firePropertyChange("mark", getMark(), getMark());
	}

	/**
	 * Try to undo the Removal in state REMOVE_PENDING.
	 *
	 * @see #remove(boolean)
	 */
	public void undoRemove() {
		if (getPersistencyState().isRemovedPending()) {
			getPersistencyState().setNext(DbState.CHANGED);
			removeObjectHistoryPending = false;
		} else {
			throw new ch.softenvironment.util.DeveloperException("Undo of removal is not possible in current state <" + getPersistencyState().toString() + ">",
				"Remove not undoable");
		}
	}
}
