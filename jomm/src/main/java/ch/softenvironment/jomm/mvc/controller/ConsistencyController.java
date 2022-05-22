package ch.softenvironment.jomm.mvc.controller;

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

import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Controller enabling Consistency Management. Interface between a View (DetailView) and its Model (DbChangeableBean) according to MVC-Pattern.
 *
 * @author Peter Hirzel softEnvironment GmbH
 */
public class ConsistencyController implements ch.softenvironment.jomm.mvc.controller.DbObjectListener {

	// Singleton -> @see #setCascaded()
	private static Set<ConsistencyController> cascadedControllers = null;
	private static Set<DbObjectValidator> validators = null;
	/*
	 * Key=object to be monitored for changes
	 * Entry=Map (Key=property of object; Entry=TestEntry)
	 */
	private final transient Map<Object, Map<String, TestEntry>> objectTests = new HashMap<Object, Map<String, TestEntry>>();
	/*
	 * The attached View (DetailView) to this Controller.
	 */
	private transient javax.swing.JFrame view = null;

	private transient java.util.Vector<String> fieldInconsistencies = new Vector<String>();
	private transient boolean fieldIsSaveable = false;
	private transient boolean fieldIsUndoable = false;

	protected transient java.beans.PropertyChangeSupport propertyChange = null;

	/**
	 * TestEntry-Structure
	 */
	static class TestEntry {

		private String propertyUiName = null;
		private String inconsistencyMessage = null;

		// Constructor.
		TestEntry(String inconsistencyMessage, String uiName) {
			super();
			this.propertyUiName = uiName;
			setInconsistencyMessage(inconsistencyMessage);
		}

		// Translation for property to be understood by User.
		protected void setUiName(String uiName) {
			this.propertyUiName = uiName;
		}

		protected String getUiName() {
			return propertyUiName;
		}

		// Update Inconsistency-Message.
		protected void setInconsistencyMessage(String message) {
			this.inconsistencyMessage = message;
		}

		protected String getInconsistencyMessage() {
			return inconsistencyMessage;
		}

		protected boolean hasPassed() {
			return inconsistencyMessage == null;
		}
	}

	/**
	 * Create a new ConsistencyController.
	 *
	 * @param view View to Control (used for generic Inconsistency-Properties)
	 */
	public ConsistencyController(javax.swing.JFrame view) {
		super();
		this.view = view;
	}

	/**
	 * The addPropertyChangeListener method was generated to support the propertyChange field. This is needed to attach a Controller to a View visually.
	 */
	public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().addPropertyChangeListener(listener);
	}

	/**
	 * Add a Property which must tested for Consistency at each change of it.
	 *
	 * @see #removeTest()
	 * @see #propertyChange()
	 */
	@Override
	public void addTest(DbPropertyChange change, DbDescriptorEntry ownerEntry) {
		if (cascadedControllers != null) {
			// start cascading as soon as tests are to be done
			cascadedControllers.add(this);
		}

		// only add if Owner defines change as Mandatory
		if (ownerEntry != null) {
			if (!ownerEntry.isMandatory()) {
				// TODO change is mandatory but supplier-owner is not=> might
				// trigger wrong checks
			}
		}

		// determine User speakable name for eventual inconsistent property
		String uiName = change.getProperty(); // use technical Value as default
		if (change.getSource() instanceof DbNlsString) {
			// otherwise generic "value" will be misunderstood
			uiName = ((DbNlsString) change.getSource()).getOwnerProperty();
		}
		if (view != null) {
			// search in attached View for a Label with a speaking Name for
			// property
			String label = getLabelString(view.getContentPane().getComponents(), uiName.toUpperCase());
			if (label != null) {
				uiName = label;
			}
		}
		TestEntry entry = new TestEntry(null, uiName);

		// build the list of objects monitored by this Controller
		Map<String, TestEntry> properties = null;
		if (objectTests.containsKey(change.getSource())) {
			properties = objectTests.get(change.getSource());
		} else {
			properties = new HashMap<String, TestEntry>();
			objectTests.put(change.getSource(), properties);
		}
		properties.put(change.getProperty(), entry);

		try {
			// do the initial check though value not changed yet
			propertyChange(new PropertyChangeEvent(change.getSource(), change.getProperty(), change.getValue(), change.getValue()));
		} catch (Throwable e) {
			ch.softenvironment.util.Tracer.getInstance()
				.developerError(change.getSource() + "#get" + change.getProperty() + "() => " + e.getLocalizedMessage());
		}
	}

	/**
	 * Register an additional validator to check special consistency conditions for given change which are not to be assumed by generic DbDescriptor settings.
	 *
	 * @param validator (for e.g. a GUI representing the change)
	 * @param change DbChangeableBean to be validated
	 * @see #addTest()
	 * @see #removeValidator()
	 */
	@Override
	public void addValidator(DbObjectValidator validator) {
		if (validators == null) {
			validators = new HashSet<DbObjectValidator>();
		}
		validators.add(validator);
	}

	/**
	 * Inform all cascaded Controllers in saveable state, that save has happened. Call this method when central Saving is done.
	 *
	 * @see #setCascaded(boolean)
	 */
	public static void cascadedSaved() {
		if (cascadedControllers != null) {
			// inform cascaded Controllers that save was done
			Iterator<ConsistencyController> iterator = cascadedControllers.iterator();
			while (iterator.hasNext()) {
				ConsistencyController controller = iterator.next();
				if (controller.getIsSaveable()) {
					controller.resetActions(); // setIsSaveable(false);
				}
			}
		}
	}

	/**
	 * The firePropertyChange method was generated to support the propertyChange field.
	 *
	 * @see #inconsistencies
	 */
	public void firePropertyChange(final String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
		getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * The firePropertyChange method was generated to support the propertyChange field.
	 *
	 * @see #setis*()
	 */
	public void firePropertyChange(final String propertyName, boolean oldValue, boolean newValue) {
		getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Gets the inconsistencies property (java.util.Vector) value.
	 *
	 * @return The inconsistencies property value.
	 * @see #setInconsistencies
	 */
	public java.util.Vector<String> getInconsistencies() {
		return fieldInconsistencies;
	}

	/**
	 * Gets the isSaveable property (boolean) value.
	 *
	 * @return The isSaveable property value.
	 * @see #setIsSaveable
	 */
	public boolean getIsSaveable() {
		return fieldIsSaveable;
	}

	/**
	 * Gets the isUndoable property (boolean) value.
	 *
	 * @return The isUndoable property value.
	 * @see #setIsUndoable
	 */
	public boolean getIsUndoable() {
		return fieldIsUndoable;
	}

	/**
	 * Recursively go through Components to find a Label on View corresponding to given property. A visual Component should be named with a 3 letter Prefix such as: "Lbl", "Cbx", "Chx", "Tbl" etc
	 * (according to softEnvironment Conventions).
	 */
	private String getLabelString(java.awt.Component[] components, final String property) {
		for (int i = 0; i < components.length; i++) {
			if ((components[i] instanceof javax.swing.JLabel) && (components[i].getName() != null) && (components[i].getName().length() > 3)) {
				if (components[i].getName().substring(3).toUpperCase().equals(/*
				 * "Lbl"
				 * +
				 */property)) {
					// this way the "speaking name" must be set up by GUI in
					// proper language
					return ((javax.swing.JLabel) components[i]).getText();
				}
			} else if (components[i] instanceof javax.swing.AbstractButton) {
				if ((components[i].getName() != null) && (components[i].getName().length() > 3)) {
					if (components[i].getName().substring(3).toUpperCase().equals(/*
					 * "Btn"
					 * +
					 */property)) {
						// this way the "speaking name" must be set up by GUI in
						// proper language
						return ((javax.swing.AbstractButton) components[i]).getText();
					}
				}
			} else if (components[i] instanceof java.awt.Container) {
				// recursively search sub-Container
				String label = getLabelString(((java.awt.Container) components[i]).getComponents(), property);
				if (label != null) {
					return label;
				}
			}
		}
		return null;
	}

	/**
	 * Accessor for the propertyChange field.
	 */
	private java.beans.PropertyChangeSupport getPropertyChange() {
		if (propertyChange == null) {
			propertyChange = new java.beans.PropertyChangeSupport(this);
		}
		return propertyChange;
	}

	/**
	 * This method gets called when a bound property is changed.
	 *
	 * @param evt event source and the property that has changed.
	 * @see #addTest()
	 * @see DbObjectValidator#checkConsistency()
	 */
	@Override
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		DbPropertyChange change = new DbPropertyChange(evt);

		if (objectTests.containsKey(change.getSource())) {
			Map<String, TestEntry> testProperties = objectTests.get(change.getSource());
			if (testProperties.containsKey(change.getProperty())) {
				// update the inconsistency-Message for given Object#property
				// changed
				try {
					// 1) do the generic tests
					TestEntry entry = testProperties.get(change.getProperty());
					String tmp = change.checkConsistency();
					if ((tmp == null) && (validators != null)) {
						// 2) do the optional tests
						Iterator<DbObjectValidator> iterator = validators.iterator();
						while (iterator.hasNext()) {
							tmp = iterator.next().checkConsistency(change);
							if (tmp != null) {
								// one message at once is enough (ignore further
								// dependencies)
								break;
							}
						}
					}
					entry.setInconsistencyMessage(tmp);

					updateInconsistencies();
				} catch (Throwable e) {
					ch.softenvironment.util.Tracer.getInstance().developerError("get" + change.getProperty() + "() => " + e.getLocalizedMessage());
				}
			}
		} // else { not registered for checking }

		// 1:n

		setIsSaveable(getInconsistencies().size() == 0);
		// there must have been a change
		setIsUndoable(true);
	}

	/**
	 * The removePropertyChangeListener method was generated to support the propertyChange field. This is needed to detach a Controller from a View.
	 */
	public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChange().removePropertyChangeListener(listener);
	}

	/**
	 * Unregister all Property-Test for given source in change from testing.
	 *
	 * @see #addTest()
	 */
	@Override
	public void removeTest(DbPropertyChange change) {
		if (cascadedControllers != null) {
			// if no tests are registered, cascading makes no sense
			cascadedControllers.remove(this);
		}

		objectTests.remove(change.getSource());
	}

	/**
	 * Unregister a validator for any changes.
	 *
	 * @param validator (for e.g. a GUI representing the change)
	 * @see #addValidator()
	 */
	@Override
	public void removeValidator(ch.softenvironment.jomm.mvc.controller.DbObjectValidator validator) {
		if (validators != null) {
			validators.remove(validator);
		}
	}

	/**
	 * Reset Controller.
	 */
	@Override
	public void resetActions() {
		updateInconsistencies();
		setIsSaveable(false);
		setIsUndoable(false);
	}

	/**
	 * Cascaded Controllers are useful if there is a central saving mechanism, such as saving the complete Model (several instances represented by different View's) at once for e.g. in a XML-File.
	 * <p>
	 * Usually it makes no sense to use cascaded behavior in DBMS-Clients because each Model-Instance may be saved separately.
	 *
	 * @see #addTest() activates cascading
	 * @see #removeTest() deactivates cascading
	 */
	public static void setCascaded(boolean cascade) {
		if (cascade) {
			cascadedControllers = new HashSet<ConsistencyController>();
		} else {
			cascadedControllers = null;
		}
	}

	/**
	 * Speaking Inconsistency User-Messages.
	 *
	 * @param inconsistencies (containing Nls-Strings)
	 * @see #getInconsistencies
	 */
	private void setInconsistencies(java.util.Vector<String> inconsistencies) {
		Vector<String> oldValue = fieldInconsistencies;
		fieldInconsistencies = inconsistencies;
		firePropertyChange("inconsistencies", oldValue, inconsistencies);
	}

	/**
	 * Sets the isSaveable property (boolean) value.
	 *
	 * @param isSaveable The new value for the property.
	 * @see #getIsSaveable
	 */
	public void setIsSaveable(boolean isSaveable) {
		boolean oldValue = fieldIsSaveable;
		fieldIsSaveable = isSaveable;
		firePropertyChange("isSaveable", Boolean.valueOf(oldValue), Boolean.valueOf(isSaveable));
	}

	/**
	 * Sets the isUndoable property (boolean) value.
	 *
	 * @param isUndoable The new value for the property.
	 * @see #getIsUndoable
	 */
	public void setIsUndoable(boolean isUndoable) {
		boolean oldValue = fieldIsUndoable;
		fieldIsUndoable = isUndoable;
		firePropertyChange("isUndoable", Boolean.valueOf(oldValue), Boolean.valueOf(isUndoable));
	}

	/**
	 * Force an Update of #inconsistencies.
	 */
	private void updateInconsistencies() {
		Vector<String> errors = new Vector<String>();

		// update inconsistency-List
		Iterator<Object> objects = objectTests.keySet().iterator();
		while (objects.hasNext()) {
			Object source = objects.next();
			Map<String, TestEntry> map = objectTests.get(source);
			Iterator<String> properties = map.keySet().iterator();
			while (properties.hasNext()) {
				String property = properties.next();
				TestEntry entry = map.get(property);
				if (!entry.hasPassed()) {
					errors.add("<" + entry.getUiName() + "> " + entry.getInconsistencyMessage());
				}
			}
		}

		setInconsistencies(errors);
	}
}
