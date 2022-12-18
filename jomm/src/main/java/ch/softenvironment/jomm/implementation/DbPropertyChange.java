package ch.softenvironment.jomm.implementation;

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
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbIdFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;
import ch.softenvironment.util.DeveloperException;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;

/**
 * Specifies the change done by DbObject of a Property. T can be anything like "? extends DbObject"
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DbPropertyChange<T> extends ch.softenvironment.util.BeanReflector<T> {

	private PropertyChangeEvent event = null;

	/**
	 * DbPropertyChange constructor.
	 *
	 * @param source Object to be changed
	 * @param property property of interest of given source
	 */
	public DbPropertyChange(T source, final String property) {
		super(source, DbObject.convertName(property));
	}

	/**
	 * Create PropertyChange out from a Bean-Event.
	 *
	 * @param event
	 */
	public DbPropertyChange(PropertyChangeEvent event) {
		this((T) event.getSource(), event.getPropertyName());
		this.event = event;
	}

	/**
	 * Check the value according to its Descriptor-Entry.
	 *
	 * @return String (null if correct)
	 */
	private String check(DbDescriptorEntry entry, Object value) {
		if ((entry.getFieldType() instanceof DbTextFieldDescriptor) && (((String) value).length() > ((DbTextFieldDescriptor) entry.getFieldType()).getLength())) {
			return getResourceString("CI_toLong") + ((DbTextFieldDescriptor) entry.getFieldType()).getLength();
		}
		if (entry.getFieldType() instanceof DbIdFieldDescriptor) {
			return null;
		}
		if (entry.getFieldType() instanceof DbNumericFieldDescriptor) {
			if (((Number) value).doubleValue() < ((DbNumericFieldDescriptor) entry.getFieldType()).getMinValue()) {
				return getResourceString("CI_toSmall") + ((DbNumericFieldDescriptor) entry.getFieldType()).getMinValue();
			}
			if (((Number) value).doubleValue() > ((DbNumericFieldDescriptor) entry.getFieldType()).getMaxValue()) {
				return getResourceString("CI_toBig") + ((DbNumericFieldDescriptor) entry.getFieldType()).getMaxValue();
			}
		}

		// everything is OK
		return null;
	}

	/**
	 * Validate the generic consistency-checks out of the DbDescriptor of the given source DbObject. Return a "User-friendly" Inconsistency-Message if source#property is not consistent according to
	 * the DbDescriptor specification.
	 *
	 * @return String (null if correct)
	 */
	public String checkConsistency() throws IllegalAccessException, java.lang.reflect.InvocationTargetException {
		if ((getSource() instanceof DbObject)
			&& (((DbObject) getSource()).getPersistencyState().isSaved() || ((DbObject) getSource()).getPersistencyState().isReadOnly())) {
			// no check necessary
			// -> reduces Object faulting, especially for cascaded relationships
			return null;
		}

		Class/* <T|? extends DbObject> */current = getSource().getClass();
		DbDescriptorEntry entry = null;
		while (!current.equals(DbChangeableBean.class)) {
			entry = ((DbObject) getSource()).getObjectServer().getDescriptor(current).getEntry(getProperty());
			if (entry != null) {
				break;
			}
			// try Parent
			current = current.getSuperclass();
		}
		if (entry == null) {
			log.error("Developer error: entry should be mapped for property <{}}> in DbDescriptor", getProperty());
			return null;
		} else {
			Object value = getValue();
			// 1) check Mandatory
			if (entry.isMandatory()) {
				if ((value == null)
					|| ((entry.getFieldType() instanceof DbTextFieldDescriptor) && (ch.softenvironment.util.StringUtils.isNullOrEmpty((String) value)))
					|| ((value instanceof DbNlsString) && (ch.softenvironment.util.StringUtils.isNullOrEmpty(((DbNlsString) value).getValue())))) {
					return getResourceString("CI_mandatory");
				}
			}

			// 2) check range
			if (value != null) {
				if (value instanceof java.util.List) {
					java.util.List<?> list = (java.util.List<?>) value;
					if (list.size() < entry.getRange().getLower()) {
						return getResourceString("CI_missingEntries") + entry.getRange().getLower();
					}
					if (list.size() > entry.getRange().getUpper()) {
						return getResourceString("CI_toManyEntries") + entry.getRange().getUpper();
					}
					String message = "";
					for (int i = 0; i < list.size(); i++) {
						String error = check(entry, list.get(i));
						if (error != null) {
							message = message + " [" + i + 1 + ".] " + error;
						}
					}
					if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(message)) {
						return getResourceString("CI_emptyEntries") + message;
					}
				} else {
					// Property is flat
					String message = check(entry, value);
					return message;
				}
			}
			/*
			 * // 3) check special testsuite cases (non-mappable by DbDescriptor) if
			 * (getSource() instanceof DbChangeableBean) { return
			 * ((DbChangeableBean)getSource()).testsuite(getProperty()); } else {
			 */
			// everything is OK
			return null;
			// }
		}
	}

	/**
	 * Case A:B=n:n mapped via C (where A or B are AssociationEnd's and C is the n:n Map). From the view of A: - source represents A itself - property represents the List of B's mapped by C (property
	 * is a List in A and a Long in C)
	 * <p>
	 * Return a <b>Set of associations C's resp. the Relationship's which are in non-removable persistence State</b>.
	 *
	 * @return java.util.Set DbRelationshipBean's
	 */
	public java.util.Set<DbRelationshipBean> getAssocations() throws IllegalAccessException, java.lang.reflect.InvocationTargetException {
		java.util.Set<DbRelationshipBean> cMap = new java.util.HashSet<DbRelationshipBean>();
		java.util.Iterator<DbRelationshipBean> assocEnds = ((java.util.List) getValue()).iterator();
		while (assocEnds.hasNext()) {
			DbRelationshipBean association = assocEnds.next();
			if ((!association.getPersistencyState().isRemoved()) && (!association.getPersistencyState().isRemovedPending())) {
				cMap.add(association);
			}
		}

		return cMap;
	}

	/**
	 * Case A:B=n:n mapped via C (where A or B are AssociationEnd's and C is the n:n Map). From the view of A: - source represents A itself - property represents the List of B's mapped by C (property
	 * is a List in A and a Long in C)
	 * <p>
	 * Return a <b>Set of associated B.T_Id's by a Relationship which are in non-removable persistence State</b>.
	 *
	 * @return java.util.Set T-Id's of associated Elements on opposite side of Association
	 * see #getAssociations()
	 */
	public java.util.Set<Long> getAssocationsId() throws IllegalAccessException, java.lang.reflect.InvocationTargetException {
		java.util.Set<Long> fkB = new java.util.HashSet<Long>();
		java.util.Iterator<DbRelationshipBean> associations = getAssocations().iterator();
		while (associations.hasNext()) {
			DbRelationshipBean association = associations.next();
			DbPropertyChange mapElement = new DbPropertyChange(association, getProperty());
			fkB.add((Long) mapElement.getValue());
		}

		return fkB;
	}

	/**
	 * Return "source#property".
	 */
	public String getDescription() {
		return ((DbObject) getSource()).getObjectServer().getTargetName(getSource().getClass()) + "#" + getProperty();
	}

	/**
	 *
	 */
	private String getResourceString(final String property) {
		return ResourceManager.getResource(DbPropertyChange.class, property);
	}

	/**
	 * Dangerous function -> allows access to private declared fields.
	 *
	 * @return Field
	 */
	public Field getPrivateField() {
		Class<?> current = getSource().getClass();
		while (!current.equals(Object.class)) {
			try {
				// field anywhere in hierarchy cannot be determined like this,
				// therefore proper class must be found
				Field field = current.getDeclaredField("field" + getPropertyUpper());
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				// ignore and try one hierarchy-level higher
				current = current.getSuperclass();
			}
		}
		throw new DeveloperException("<" + getSource().getClass().getName() + "#field" + getPropertyUpper() + ">  does not exist!");
	}

	/**
	 * Set the Property by its <b>Field</b> with given value. The field is assumed to be private according to Beans-Specification, therefore
	 * <b>accessibility is ignored here to suppress the change-event</b> a
	 * setter-Method would cause.
	 */
	public void setProperty(Object value) {
		try {
			getPrivateField().set(getSource(), value);
		} catch (IllegalAccessException e) {
			throw new DeveloperException("<" + getSource().getClass().getName() + "#field" + getPropertyUpper() + ">  must be PUBLIC!");
		}
	}

	/**
	 * Return whether the value changed at creating this instance.
	 *
	 * @return null->Unknown; Boolean.TRUE->yes; Boolean.FALSE->no
	 * @see #DbPropertyChange(PropertyChangeEvent)
	 */
	public Boolean hasChanged() {
		if (event == null) {
			return null;
		} else {
			if (event.getOldValue() == null) {
				if (event.getNewValue() != null) {
					return Boolean.TRUE;
				}
			} else {
				if (!event.getOldValue().equals(event.getNewValue())) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}
}
