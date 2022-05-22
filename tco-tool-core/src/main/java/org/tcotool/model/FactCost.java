package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;

/**
 * Any costs created by machines, material or production which cannot be expressed as human costs.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see PersonalCost
 */
public class FactCost extends Cost {

    public FactCost(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(FactCost.class);
        descriptor.add("depreciationDuration", "depreciationDuration",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(1, 1));
        descriptor.add("usageDuration", "usageDuration",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(1, 1));
        descriptor.add("expendable", "expendable", new DbFieldTypeDescriptor(
            java.lang.Boolean.class), new DbMultiplicityRange(1, 1));
        // TODO @deprecated
        descriptor.add("serialNumber", "serialNumber",
            new DbTextFieldDescriptor(20), new DbMultiplicityRange(0, 1));
        descriptor.add("portsUseable", "portsUseable",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("portsISL", "portsISL", new DbNumericFieldDescriptor(
                java.lang.Long.class, 0.0, 9999999.0, 0),
            new DbMultiplicityRange(0, 1));
        descriptor.add("portsServer", "portsServer",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.addCode("catalogue", "T_Id_catalogue",
            new DbMultiplicityRange(0, 1));
        return descriptor;
    }

    private Catalogue fieldCatalogue;

    /**
     * Gets the catalogue property (org.tcotool.model.Catalogue) value.
     *
     * @return The catalogue property value.
     * @see #setCatalogue
     */
    public Catalogue getCatalogue() {
        refresh(false);
        return fieldCatalogue;
    }

    /**
     * Sets the catalogue property (org.tcotool.model.Catalogue) value.
     *
     * @param catalogue The new value for the property.
     * @see #getCatalogue
     */
    public void setCatalogue(Catalogue catalogue) {
        Catalogue oldValue = fieldCatalogue;
        fieldCatalogue = catalogue;
        firePropertyChange("catalogue", oldValue, catalogue);
    }

    private java.lang.Long fieldDepreciationDuration; // NOT NULL

    public java.lang.Long getDepreciationDuration() {
        refresh(false); // read lazy initialized objects
        return fieldDepreciationDuration;
    }

    /**
     * Time of accountancy depreciation time in months.
     *
     * @param depreciationDuration
     */
    public void setDepreciationDuration(java.lang.Long depreciationDuration) {
        java.lang.Long oldValue = fieldDepreciationDuration;
        fieldDepreciationDuration = depreciationDuration;
        firePropertyChange("depreciationDuration", oldValue,
            fieldDepreciationDuration);
    }

    private java.lang.Long fieldPortsISL;

    @Deprecated
    public java.lang.Long getPortsISL() {
        refresh(false); // read lazy initialized objects
        return fieldPortsISL;
    }

    @Deprecated
    public void setPortsISL(java.lang.Long portsISL) {
        java.lang.Long oldValue = fieldPortsISL;
        fieldPortsISL = portsISL;
        firePropertyChange("portsISL", oldValue, fieldPortsISL);
    }

    private java.lang.Long fieldPortsServer;

    @Deprecated
    public java.lang.Long getPortsServer() {
        refresh(false); // read lazy initialized objects
        return fieldPortsServer;
    }

    @Deprecated
    public void setPortsServer(java.lang.Long portsServer) {
        java.lang.Long oldValue = fieldPortsServer;
        fieldPortsServer = portsServer;
        firePropertyChange("portsServer", oldValue, fieldPortsServer);
    }

    private java.lang.Long fieldPortsUseable;

    @Deprecated
    public java.lang.Long getPortsUseable() {
        refresh(false); // read lazy initialized objects
        return fieldPortsUseable;
    }

    @Deprecated
    public void setPortsUseable(java.lang.Long portsUseable) {
        java.lang.Long oldValue = fieldPortsUseable;
        fieldPortsUseable = portsUseable;
        firePropertyChange("portsUseable", oldValue, fieldPortsUseable);
    }

    private String fieldSerialNumber;

    @Deprecated
    public String getSerialNumber() {
        refresh(false); // read lazy initialized objects
        return fieldSerialNumber;
    }

    @Deprecated
    public void setSerialNumber(String serialNumber) {
        String oldValue = fieldSerialNumber;
        fieldSerialNumber = serialNumber;
        firePropertyChange("serialNumber", oldValue, fieldSerialNumber);
    }

    private java.lang.Long fieldUsageDuration; // NOT NULL

    public java.lang.Long getUsageDuration() {
        refresh(false); // read lazy initialized objects
        return fieldUsageDuration;
    }

    /**
     * Time of life-cycle in months.
     *
     * @param usageDuration
     */
    public void setUsageDuration(java.lang.Long usageDuration) {
        java.lang.Long oldValue = fieldUsageDuration;
        fieldUsageDuration = usageDuration;
        firePropertyChange("usageDuration", oldValue, fieldUsageDuration);
    }

    private Boolean fieldExpendable;

    /**
     * Define whether a thing is to be used as tool (for e.g. Computer) or "throw away material" like paper in a printer. (de "Verbrauchsmaterial").
     */
    public void setExpendable(Boolean expendable) {
        Boolean oldValue = fieldExpendable;
        fieldExpendable = expendable;
        firePropertyChange("expendable", oldValue, expendable);
    }

    public Boolean getExpendable() {
        return fieldExpendable;
    }
}