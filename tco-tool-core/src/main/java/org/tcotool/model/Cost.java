package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;

/**
 * Effective calculable Cost-Parameters. Abstraction for fact- or personal costs.
 *
 * @author Peter Hirzel
 */
public abstract class Cost extends TcoObject {

    public Cost(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private java.lang.Double fieldAmount; // NOT NULL

    public java.lang.Double getAmount() {
        refresh(false); // read lazy initialized objects
        return fieldAmount;
    }

    public void setAmount(java.lang.Double amount) {
        java.lang.Double oldValue = fieldAmount;
        fieldAmount = amount;
        firePropertyChange("amount", oldValue, fieldAmount);
    }

    private org.tcotool.model.Currency fieldCurrency; // NOT NULL

    public org.tcotool.model.Currency getCurrency() {
        refresh(false); // read lazy initialized objects
        return fieldCurrency;
    }

    public void setCurrency(org.tcotool.model.Currency currency) {
        org.tcotool.model.Currency oldValue = fieldCurrency;
        fieldCurrency = currency;
        firePropertyChange("currency", oldValue, fieldCurrency);
    }

    private java.lang.Boolean fieldEstimated; // NOT NULL

    public java.lang.Boolean getEstimated() {
        refresh(false); // read lazy initialized objects
        return fieldEstimated;
    }

    public void setEstimated(java.lang.Boolean estimated) {
        java.lang.Boolean oldValue = fieldEstimated;
        fieldEstimated = estimated;
        firePropertyChange("estimated", oldValue, fieldEstimated);
    }

    private java.lang.Boolean fieldRepeatable; // NOT NULL

    public java.lang.Boolean getRepeatable() {
        refresh(false); // read lazy initialized objects
        return fieldRepeatable;
    }

    public void setRepeatable(java.lang.Boolean repeatable) {
        java.lang.Boolean oldValue = fieldRepeatable;
        fieldRepeatable = repeatable;
        firePropertyChange("repeatable", oldValue, fieldRepeatable);
    }

    private org.tcotool.model.CostCause fieldCause;

    public org.tcotool.model.CostCause getCause() {
        refresh(false); // read lazy initialized objects
        return fieldCause;
    }

    public void setCause(CostCause cause) {
        CostCause oldValue = fieldCause;
        fieldCause = cause;
        firePropertyChange("cause", oldValue, fieldCause);
    }

    private java.lang.Long fieldDriverId;

    public java.lang.Long getDriverId() {
        refresh(false); // read lazy initialized objects
        return fieldDriverId;
    }

    public void setDriverId(java.lang.Long driver) {
        java.lang.Long oldValue = fieldDriverId;
        fieldDriverId = driver;
        firePropertyChange("driverId", oldValue, fieldDriverId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(Cost.class);
        descriptor.add("baseOffset", "baseOffset",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(1, 1));
        descriptor.add("amount", "amount", new DbNumericFieldDescriptor(
                java.lang.Double.class, 0.0, 9.99999999999E11, 2),
            new DbMultiplicityRange(1, 1));
        descriptor.addCode("currency", "currency",
            new DbMultiplicityRange(1, 1));
        descriptor.add("estimated", "estimated", new DbFieldTypeDescriptor(
            java.lang.Boolean.class), new DbMultiplicityRange(1, 1));
        descriptor.add("repeatable", "repeatable", new DbFieldTypeDescriptor(
            java.lang.Boolean.class), new DbMultiplicityRange(1, 1));
        descriptor.addCode("cause", "cause", new DbMultiplicityRange(0, 1));
        // descriptor.addCode("type", "type", new DbMultiplicityRange(0,1));
        descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION,
            "driverId", "T_Id_driver", new DbMultiplicityRange(1, 1));
        return descriptor;
    }

    private java.lang.Long fieldBaseOffset;

    public java.lang.Long getBaseOffset() {
        return fieldBaseOffset;
    }

    /**
     * Offset of cost impact from the very beginning of the whole TCO-Configuration in months.
     *
     * @param baseOffset
     */
    public void setBaseOffset(java.lang.Long baseOffset) {
        java.lang.Long oldValue = fieldBaseOffset;
        fieldBaseOffset = baseOffset;
        firePropertyChange("baseOffset", oldValue, fieldBaseOffset);
    }
}
