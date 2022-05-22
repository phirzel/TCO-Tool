package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * Define the occurrence of Cost's per Site by a CostDriver.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class Occurance extends DbEntityBean {

    private java.lang.Double fieldMultitude;
    private Site fieldSite;
    private java.lang.Long fieldDriverId;
    private java.lang.String fieldDocumentation;

    public Occurance(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(Occurance.class);
        descriptor.add("multitude", "multitude", new DbNumericFieldDescriptor(
                java.lang.Double.class, 0.0, 9.99999999E8, 2),
            new DbMultiplicityRange(0, 1));
        descriptor.add("documentation", "documentation",
            new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        // descriptor.addManyToOneReferenceId(DbDescriptor.AGGREGATION,
        // "siteId", "T_Id_site", new DbMultiplicityRange(1,1));
        // descriptor.addManyToOneReference(DbDescriptor.AGGREGATION, "site",
        // "T_Id_site", new DbMultiplicityRange(1,1));
        descriptor.addCode("site", "T_Id_site", new DbMultiplicityRange(1, 1));
        descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION,
            "driverId", "T_Id_driver", new DbMultiplicityRange(1, 1));
        return descriptor;
    }

    /**
     * Gets the documentation property (java.lang.String) value.
     *
     * @return The documentation property value.
     * @see #setDocumentation
     */
    public java.lang.String getDocumentation() {
        refresh(false); // read lazy initialized objects
        return fieldDocumentation;
    }

    public java.lang.Long getDriverId() {
        refresh(false); // read lazy initialized objects
        return fieldDriverId;
    }

    public java.lang.Double getMultitude() {
        refresh(false); // read lazy initialized objects
        return fieldMultitude;
    }

    public Site getSite() {
        refresh(false); // read lazy initialized objects
        return fieldSite;
    }

    /**
     * Sets the documentation property (java.lang.String) value.
     *
     * @param documentation The new value for the property.
     * @see #getDocumentation
     */
    public void setDocumentation(java.lang.String documentation) {
        String oldValue = fieldDocumentation;
        fieldDocumentation = documentation;
        firePropertyChange("documentation", oldValue, documentation);
    }

    public void setDriverId(java.lang.Long driver) {
        java.lang.Long oldValue = fieldDriverId;
        fieldDriverId = driver;
        firePropertyChange("driverId", oldValue, fieldDriverId);
    }

    public void setMultitude(java.lang.Double multitude) {
        java.lang.Double oldValue = fieldMultitude;
        fieldMultitude = multitude;
        firePropertyChange("multitude", oldValue, fieldMultitude);
    }

    public void setSite(Site site) {
        Site oldValue = fieldSite;
        fieldSite = site;
        firePropertyChange("site", oldValue, fieldSite);
    }
}
