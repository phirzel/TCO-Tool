package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * The Root package manages the system Parameters for the whole Configuration.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class SystemParameter extends DbEntityBean {

    private java.lang.Long fieldDefaultDepreciationDuration; // NOT NULL
    private org.tcotool.model.Currency fieldDefaultCurrency; // NOT NULL
    private java.lang.Long fieldDefaultUsageDuration; // NOT NULL
    private org.tcotool.model.CostExponent fieldReportCostExponent; // NOT NULL
    private java.lang.Double fieldDepreciationInterestRate;
    private java.lang.Long fieldManYearHoursInternal;
    private java.lang.Long fieldManYearHoursExternal;
    private java.lang.Long fieldReportUsageDuration;
    private java.lang.Long fieldReportDepreciationDuration;
    private java.lang.Long fieldModelId;
    private java.util.List<Course> fieldCourse = new java.util.ArrayList<Course>();

    public SystemParameter(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(SystemParameter.class);
        descriptor.add("defaultDepreciationDuration", "defaultDepreciationDrtion", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(1, 1));
        descriptor.addCode("defaultCurrency", "defaultCurrency", new DbMultiplicityRange(1, 1));
        descriptor.add("defaultUsageDuration", "defaultUsageDuration", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(1, 1));
        descriptor.addCode("reportCostExponent", "reportCostExponent", new DbMultiplicityRange(1, 1));
        descriptor.add("depreciationInterestRate", "depreciationInterestRate", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 100.0, 2), new DbMultiplicityRange(0, 1));
        descriptor.add("manYearHoursInternal", "manYearHoursInternal", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("manYearHoursExternal", "manYearHoursExternal", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("reportUsageDuration", "reportUsageDuration", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("reportDepreciationDuration", "reportDepreciationDurtion", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(0, 1));
        descriptor.addOneToOneReferenceId(DbDescriptor.ASSOCIATION, "modelId", "T_Id_model", new DbMultiplicityRange(1, 1), false);
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "course", "systemParameterId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), Course.class, false);
        return descriptor;
    }

    public org.tcotool.model.Currency getDefaultCurrency() {
        refresh(false); // read lazy initialized objects
        return fieldDefaultCurrency;
    }

    public java.lang.Long getDefaultDepreciationDuration() {
        refresh(false); // read lazy initialized objects
        return fieldDefaultDepreciationDuration;
    }

    public java.lang.Long getDefaultUsageDuration() {
        refresh(false); // read lazy initialized objects
        return fieldDefaultUsageDuration;
    }

    public java.lang.Double getDepreciationInterestRate() {
        refresh(false); // read lazy initialized objects
        return fieldDepreciationInterestRate;
    }

    public java.lang.Long getManYearHoursExternal() {
        refresh(false); // read lazy initialized objects
        return fieldManYearHoursExternal;
    }

    public java.lang.Long getManYearHoursInternal() {
        refresh(false); // read lazy initialized objects
        return fieldManYearHoursInternal;
    }

    public java.lang.Long getModelId() {
        refresh(false); // read lazy initialized objects
        return fieldModelId;
    }

    public org.tcotool.model.CostExponent getReportCostExponent() {
        refresh(false); // read lazy initialized objects
        return fieldReportCostExponent;
    }

    public java.lang.Long getReportDepreciationDuration() {
        refresh(false); // read lazy initialized objects
        return fieldReportDepreciationDuration;
    }

    public java.lang.Long getReportUsageDuration() {
        refresh(false); // read lazy initialized objects
        return fieldReportUsageDuration;
    }

    public void setDefaultCurrency(org.tcotool.model.Currency defaultCurrency) {
        org.tcotool.model.Currency oldValue = fieldDefaultCurrency;
        fieldDefaultCurrency = defaultCurrency;
        firePropertyChange("defaultCurrency", oldValue, fieldDefaultCurrency);
    }

    public void setDefaultDepreciationDuration(java.lang.Long defaultDepreciationDuration) {
        java.lang.Long oldValue = fieldDefaultDepreciationDuration;
        fieldDefaultDepreciationDuration = defaultDepreciationDuration;
        firePropertyChange("defaultDepreciationDuration", oldValue, fieldDefaultDepreciationDuration);
    }

    public void setDefaultUsageDuration(java.lang.Long defaultUsageDuration) {
        java.lang.Long oldValue = fieldDefaultUsageDuration;
        fieldDefaultUsageDuration = defaultUsageDuration;
        firePropertyChange("defaultUsageDuration", oldValue, fieldDefaultUsageDuration);
    }

    public void setDepreciationInterestRate(java.lang.Double depreciationInterestRate) {
        java.lang.Double oldValue = fieldDepreciationInterestRate;
        fieldDepreciationInterestRate = depreciationInterestRate;
        firePropertyChange("depreciationInterestRate", oldValue, fieldDepreciationInterestRate);
    }

    public void setManYearHoursExternal(java.lang.Long manYearHoursExternal) {
        java.lang.Long oldValue = fieldManYearHoursExternal;
        fieldManYearHoursExternal = manYearHoursExternal;
        firePropertyChange("manYearHoursExternal", oldValue, fieldManYearHoursExternal);
    }

    public void setManYearHoursInternal(java.lang.Long manYearHoursInternal) {
        java.lang.Long oldValue = fieldManYearHoursInternal;
        fieldManYearHoursInternal = manYearHoursInternal;
        firePropertyChange("manYearHoursInternal", oldValue, fieldManYearHoursInternal);
    }

    public void setModelId(java.lang.Long model) {
        java.lang.Long oldValue = fieldModelId;
        fieldModelId = model;
        firePropertyChange("modelId", oldValue, fieldModelId);
    }

    public void setReportCostExponent(org.tcotool.model.CostExponent reportCostExponent) {
        org.tcotool.model.CostExponent oldValue = fieldReportCostExponent;
        fieldReportCostExponent = reportCostExponent;
        firePropertyChange("reportCostExponent", oldValue, fieldReportCostExponent);
    }

    public void setReportDepreciationDuration(java.lang.Long reportDepreciationDuration) {
        java.lang.Long oldValue = fieldReportDepreciationDuration;
        fieldReportDepreciationDuration = reportDepreciationDuration;
        firePropertyChange("reportDepreciationDuration", oldValue, fieldReportDepreciationDuration);
    }

    public void setReportUsageDuration(java.lang.Long reportTcoDuration) {
        java.lang.Long oldValue = fieldReportUsageDuration;
        fieldReportUsageDuration = reportTcoDuration;
        firePropertyChange("reportUsageDuration", oldValue, fieldReportUsageDuration);
    }

    public java.util.List<Course> getCourse() {
        refresh(false); // read lazy initialized objects
        return fieldCourse;
    }

    public void setCourse(java.util.List<Course> course) {
        java.util.List<Course> oldValue = fieldCourse;
        fieldCourse = course;
        firePropertyChange("course", oldValue, fieldCourse);
    }
}
