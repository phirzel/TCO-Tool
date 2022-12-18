package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Role of a human Resource (for e.g. function of employee).
 *
 * @author Peter Hirzel
 */
public final class Role extends DbCode {

    public java.lang.Boolean fieldInternal; // NOT NULL
    public java.lang.Long fieldYearlyHours; // NOT NULL
    public java.lang.Long fieldEmploymentPercentageAvailable; // NOT NULL
    public java.lang.Double fieldHourlyRate; // NOT NULL
    public org.tcotool.model.Currency fieldCurrency; // NOT NULL
    public String fieldDocumentation;
    public java.lang.Double fieldFullTimeEquivalent;

    //  public java.util.List fieldRessource=new java.util.ArrayList();
    public Role(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     *
     * @see ch.softenvironment.jomm.DbObjectServer#register(Class, String)
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        DbDescriptor descriptor = DbCode.createDefaultDescriptor(Role.class);
        descriptor.add("internal", "internal", new DbFieldTypeDescriptor(java.lang.Boolean.class), new DbMultiplicityRange(1, 1));
        descriptor.add("yearlyHours", "yearlyHours", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(1, 1));
        descriptor.add("employmentPercentageAvailable", "employmentPercentagevlble", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 9.99999999E8, 0), new DbMultiplicityRange(1, 1));
        descriptor.add("hourlyRate", "hourlyRate", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 9.99999999999E11, 2), new DbMultiplicityRange(1, 1));
        descriptor.addCode("currency", "currency", new DbMultiplicityRange(1, 1));
        descriptor.add("documentation", "documentation", new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        descriptor.add("fullTimeEquivalent", "fullTimeEquivalent", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 9.99999999999E11, 2), new DbMultiplicityRange(1, 1));
        //    descriptor.addOneToMany(DbDescriptor.ASSOCIATION, "ressource", "roleId", new DbMultiplicityRange(0,DbMultiplicityRange.UNBOUND), PersonalCost.class, false);
        return descriptor;
    }

    public org.tcotool.model.Currency getCurrency() {
        refresh(false); // read lazy initialized objects
        return fieldCurrency;
    }

    public String getDocumentation() {
        refresh(false); // read lazy initialized objects
        return fieldDocumentation;
    }

    public java.lang.Long getEmploymentPercentageAvailable() {
        refresh(false); // read lazy initialized objects
        return fieldEmploymentPercentageAvailable;
    }

    public java.lang.Double getFullTimeEquivalent() {
        refresh(false); // read lazy initialized objects
        return fieldFullTimeEquivalent;
    }

    public java.lang.Double getHourlyRate() {
        refresh(false); // read lazy initialized objects
        return fieldHourlyRate;
    }

    public java.lang.Boolean getInternal() {
        refresh(false); // read lazy initialized objects
        return fieldInternal;
    }

    public java.lang.Long getYearlyHours() {
        refresh(false); // read lazy initialized objects
        return fieldYearlyHours;
    }

    public void setCurrency(org.tcotool.model.Currency currency) {
        org.tcotool.model.Currency oldValue = fieldCurrency;
        fieldCurrency = currency;
        firePropertyChange("currency", oldValue, fieldCurrency);
    }

    public void setDocumentation(String documentation) {
        String oldValue = fieldDocumentation;
        fieldDocumentation = documentation;
        firePropertyChange("documentation", oldValue, fieldDocumentation);
    }

    public void setEmploymentPercentageAvailable(java.lang.Long employmentPercentageAvailable) {
        java.lang.Long oldValue = fieldEmploymentPercentageAvailable;
        fieldEmploymentPercentageAvailable = employmentPercentageAvailable;
        firePropertyChange("employmentPercentageAvailable", oldValue, fieldEmploymentPercentageAvailable);
    }

    public void setFullTimeEquivalent(java.lang.Double fullTimeEquivalent) {
        java.lang.Double oldValue = fieldFullTimeEquivalent;
        fieldFullTimeEquivalent = fullTimeEquivalent;
        firePropertyChange("fullTimeEquivalent", oldValue, fieldFullTimeEquivalent);
    }

    /**
     * Costs per hour for this role. Rounded(2)! value = fullTimeEquivalent / yearlyHours
     *
     * @param hourlyRate
     */
    public void setHourlyRate(java.lang.Double hourlyRate) {
        java.lang.Double oldValue = fieldHourlyRate;
        fieldHourlyRate = hourlyRate;
        firePropertyChange("hourlyRate", oldValue, fieldHourlyRate);
    }

    public void setInternal(java.lang.Boolean internal) {
        java.lang.Boolean oldValue = fieldInternal;
        fieldInternal = internal;
        firePropertyChange("internal", oldValue, fieldInternal);
    }

    public void setYearlyHours(java.lang.Long yearlyHours) {
        java.lang.Long oldValue = fieldYearlyHours;
        fieldYearlyHours = yearlyHours;
        firePropertyChange("yearlyHours", oldValue, fieldYearlyHours);
    }
}
