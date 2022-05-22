package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Production site.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class Site extends DbCode {

    private String fieldAddress;
    private ch.softenvironment.jomm.datatypes.interlis.IliCoord2d fieldCoordinates;

    //  public java.util.List fieldOccurance=new java.util.ArrayList();
    public Site(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     *
     * @see ch.softenvironment.jomm.DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        DbDescriptor descriptor = DbCode.createDefaultDescriptor(Site.class);
        descriptor.add("address", "address", new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        descriptor.add("coordinates", "coordinates", new DbFieldTypeDescriptor(ch.softenvironment.jomm.datatypes.interlis.IliCoord2d.class), new DbMultiplicityRange(0, 1));
        //    descriptor.addOneToMany(DbDescriptor.ASSOCIATION, "occurance", "siteId", new DbMultiplicityRange(0,DbMultiplicityRange.UNBOUND), Occurance.class, false);
        return descriptor;
    }

    public String getAddress() {
        refresh(false); // read lazy initialized objects
        return fieldAddress;
    }

    public ch.softenvironment.jomm.datatypes.interlis.IliCoord2d getCoordinates() {
        refresh(false); // read lazy initialized objects
        return fieldCoordinates;
    }

    public void setAddress(String address) {
        String oldValue = fieldAddress;
        fieldAddress = address;
        firePropertyChange("address", oldValue, fieldAddress);
    }

    public void setCoordinates(ch.softenvironment.jomm.datatypes.interlis.IliCoord2d coordinates) {
        ch.softenvironment.jomm.datatypes.interlis.IliCoord2d oldValue = fieldCoordinates;
        fieldCoordinates = coordinates;
        firePropertyChange("coordinates", oldValue, fieldCoordinates);
    }
}
