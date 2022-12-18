package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;

/**
 * Container-Element which may contain either Service's or other Sub-Packages. A Sub-Package may also point to an imported TCO-Configuration.
 *
 * @author Peter Hirzel
 */
public class TcoPackage extends TcoObject {

    public TcoPackage(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private java.util.List<Service> fieldService = new java.util.ArrayList<>();

    /**
     * List of contained Services of this package.
     *
     * @return
     */
    public java.util.List<Service> getService() {
        refresh(false); // read lazy initialized objects
        return fieldService;
    }

    public void setService(java.util.List<Service> service) {
        java.util.List<Service> oldValue = fieldService;
        fieldService = service;
        firePropertyChange("service", oldValue, fieldService);
    }

    private java.util.List<TcoPackage> fieldOwnedElement = new java.util.ArrayList<>();

    /**
     * List of contained TcoPackage's of this package.
     *
     * @return
     */
    public java.util.List<TcoPackage> getOwnedElement() {
        refresh(false); // read lazy initialized objects
        return fieldOwnedElement;
    }

    public void setOwnedElement(java.util.List<TcoPackage> ownedElement) {
        java.util.List<TcoPackage> oldValue = fieldOwnedElement;
        fieldOwnedElement = ownedElement;
        firePropertyChange("ownedElement", oldValue, fieldOwnedElement);
    }

    private java.lang.Long fieldNamespaceId;

    public java.lang.Long getNamespaceId() {
        refresh(false); // read lazy initialized objects
        return fieldNamespaceId;
    }

    public void setNamespaceId(java.lang.Long namespace) {
        java.lang.Long oldValue = fieldNamespaceId;
        fieldNamespaceId = namespace;
        firePropertyChange("namespaceId", oldValue, fieldNamespaceId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(TcoPackage.class);
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "service", "groupId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), Service.class, false);
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "ownedElement", "namespaceId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), TcoPackage.class, false);
        descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, "namespaceId", "T_Id_namespace", new DbMultiplicityRange(1, 1));
        return descriptor;
    }
}
