package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;

/**
 * Costs created by human employees (de: Personalkosten (PK))
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class PersonalCost extends Cost {

    private org.tcotool.model.Activity fieldActivity;
    private java.lang.Double fieldHours;
    private java.lang.Double fieldHourlyRate;
    private java.lang.Boolean fieldInternal; // NOT NULL
    private Role fieldRole;

    public PersonalCost(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(PersonalCost.class);
        descriptor.addCode("activity", "activity", new DbMultiplicityRange(0, 1));
        descriptor.add("hours", "hours", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 9.99999999E8, 2), new DbMultiplicityRange(0, 1));
        descriptor.add("hourlyRate", "hourlyRate", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 9.99999999999E11, 2), new DbMultiplicityRange(0, 1));
        descriptor.add("internal", "internal", new DbFieldTypeDescriptor(java.lang.Boolean.class), new DbMultiplicityRange(1, 1));
        //    descriptor.addManyToOneReference(DbDescriptor.AGGREGATION, "role", "T_Id_role", new DbMultiplicityRange(0,1));
        descriptor.addCode("role", "T_Id_role", new DbMultiplicityRange(0, 1));
        return descriptor;
    }

    public org.tcotool.model.Activity getActivity() {
        refresh(false); // read lazy initialized objects
        return fieldActivity;
    }

    public java.lang.Double getHourlyRate() {
        refresh(false); // read lazy initialized objects
        return fieldHourlyRate;
    }

    public java.lang.Double getHours() {
        refresh(false); // read lazy initialized objects
        return fieldHours;
    }

    public java.lang.Boolean getInternal() {
        refresh(false); // read lazy initialized objects
        return fieldInternal;
    }

    public Role getRole() {
        refresh(false); // read lazy initialized objects
        return fieldRole;
    }

    public void setActivity(org.tcotool.model.Activity activity) {
        org.tcotool.model.Activity oldValue = fieldActivity;
        fieldActivity = activity;
        firePropertyChange("activity", oldValue, fieldActivity);
    }

    public void setHourlyRate(java.lang.Double hourlyRate) {
        java.lang.Double oldValue = fieldHourlyRate;
        fieldHourlyRate = hourlyRate;
        firePropertyChange("hourlyRate", oldValue, fieldHourlyRate);
    }

    public void setHours(java.lang.Double hours) {
        java.lang.Double oldValue = fieldHours;
        fieldHours = hours;
        firePropertyChange("hours", oldValue, fieldHours);
    }

    public void setInternal(java.lang.Boolean internal) {
        java.lang.Boolean oldValue = fieldInternal;
        fieldInternal = internal;
        firePropertyChange("internal", oldValue, fieldInternal);
    }

    public void setRole(Role role) {
        Role oldValue = fieldRole;
        fieldRole = role;
        firePropertyChange("role", oldValue, fieldRole);
    }
}
