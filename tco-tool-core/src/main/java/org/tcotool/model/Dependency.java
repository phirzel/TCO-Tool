package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;

/**
 * Dependency between 2 Service's or TcoPackage's. Useful to distribute costs from a supplier among different client's.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class Dependency extends DbRelationshipBean {

    public Dependency(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private org.tcotool.model.SupplierInfluence fieldSupplierInfluence; // NOT NULL

    public org.tcotool.model.SupplierInfluence getSupplierInfluence() {
        refresh(false); // read lazy initialized objects
        return fieldSupplierInfluence;
    }

    public void setSupplierInfluence(org.tcotool.model.SupplierInfluence supplierInfluence) {
        org.tcotool.model.SupplierInfluence oldValue = fieldSupplierInfluence;
        fieldSupplierInfluence = supplierInfluence;
        firePropertyChange("supplierInfluence", oldValue, fieldSupplierInfluence);
    }

    private java.lang.Double fieldDistribution;

    public java.lang.Double getDistribution() {
        refresh(false); // read lazy initialized objects
        return fieldDistribution;
    }

    public void setDistribution(java.lang.Double distribution) {
        java.lang.Double oldValue = fieldDistribution;
        fieldDistribution = distribution;
        firePropertyChange("distribution", oldValue, fieldDistribution);
    }

    private String fieldDocumentation;

    public String getDocumentation() {
        refresh(false); // read lazy initialized objects
        return fieldDocumentation;
    }

    public void setDocumentation(String documentation) {
        String oldValue = fieldDocumentation;
        fieldDocumentation = documentation;
        firePropertyChange("documentation", oldValue, fieldDocumentation);
    }

    private java.lang.Boolean fieldVariant; // NOT NULL

    public java.lang.Boolean getVariant() {
        refresh(false); // read lazy initialized objects
        return fieldVariant;
    }

    public void setVariant(java.lang.Boolean variant) {
        java.lang.Boolean oldValue = fieldVariant;
        fieldVariant = variant;
        firePropertyChange("variant", oldValue, fieldVariant);
    }

    private java.lang.Long fieldSupplierId;

    public java.lang.Long getSupplierId() {
        refresh(false); // read lazy initialized objects
        return fieldSupplierId;
    }

    public void setSupplierId(java.lang.Long supplier) {
        java.lang.Long oldValue = fieldSupplierId;
        fieldSupplierId = supplier;
        firePropertyChange("supplierId", oldValue, fieldSupplierId);
    }

    private java.lang.Long fieldClientId;

    public java.lang.Long getClientId() {
        refresh(false); // read lazy initialized objects
        return fieldClientId;
    }

    public void setClientId(java.lang.Long client) {
        java.lang.Long oldValue = fieldClientId;
        fieldClientId = client;
        firePropertyChange("clientId", oldValue, fieldClientId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(Dependency.class);
        descriptor.addCode("supplierInfluence", "supplierInfluence", new DbMultiplicityRange(1, 1));
        descriptor.add("distribution", "distribution", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 100.0, 2), new DbMultiplicityRange(0, 1));
        descriptor.add("documentation", "documentation", new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        descriptor.add("variant", "variant", new DbFieldTypeDescriptor(java.lang.Boolean.class), new DbMultiplicityRange(1, 1));
        descriptor.addAssociationEnd(TcoObject.class, "supplierId", "T_Id_supplier");
        descriptor.addAssociationEnd(TcoObject.class, "clientId", "T_Id_client");
        return descriptor;
    }
}
