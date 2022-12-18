package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * Map a course factor between source- and target-Currency where: target = source * factor
 *
 * @author Peter Hirzel
 */
public class Course extends DbEntityBean {

    public Course(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private org.tcotool.model.Currency fieldSource; // NOT NULL

    public org.tcotool.model.Currency getSource() {
        refresh(false); // read lazy initialized objects
        return fieldSource;
    }

    public void setSource(org.tcotool.model.Currency source) {
        org.tcotool.model.Currency oldValue = fieldSource;
        fieldSource = source;
        firePropertyChange("source", oldValue, fieldSource);
    }

    private org.tcotool.model.Currency fieldTarget; // NOT NULL

    public org.tcotool.model.Currency getTarget() {
        refresh(false); // read lazy initialized objects
        return fieldTarget;
    }

    public void setTarget(org.tcotool.model.Currency target) {
        org.tcotool.model.Currency oldValue = fieldTarget;
        fieldTarget = target;
        firePropertyChange("target", oldValue, fieldTarget);
    }

    private java.lang.Double fieldFactor; // NOT NULL

    public java.lang.Double getFactor() {
        refresh(false); // read lazy initialized objects
        return fieldFactor;
    }

    public void setFactor(java.lang.Double factor) {
        java.lang.Double oldValue = fieldFactor;
        fieldFactor = factor;
        firePropertyChange("factor", oldValue, fieldFactor);
    }

    public java.lang.Long fieldSystemParameterId;

    public java.lang.Long getSystemParameterId() {
        refresh(false); // read lazy initialized objects
        return fieldSystemParameterId;
    }

    public void setSystemParameterId(java.lang.Long systemParameter) {
        java.lang.Long oldValue = fieldSystemParameterId;
        fieldSystemParameterId = systemParameter;
        firePropertyChange("systemParameterId", oldValue, fieldSystemParameterId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(Course.class);
        descriptor.addCode("source", "source", new DbMultiplicityRange(1, 1));
        descriptor.addCode("target", "target", new DbMultiplicityRange(1, 1));
        descriptor.add("factor", "factor", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 99999.0, 5), new DbMultiplicityRange(1, 1));
        descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, "systemParameterId", "T_Id_systemParameter", new DbMultiplicityRange(1, 1));
        return descriptor;
    }
}
