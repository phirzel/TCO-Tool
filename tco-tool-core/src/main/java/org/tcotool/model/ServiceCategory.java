package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Category of a Service.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class ServiceCategory extends DbCode {

    /**
     * Return the database mappings for this persistence object.
     *
     * @see ch.softenvironment.jomm.DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        return DbCode.createDefaultDescriptor(ServiceCategory.class);
    }

    public ServiceCategory(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }
}
