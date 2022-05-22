package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Code for Activities a Person can do (for e.g. "Support")
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class Activity extends DbCode {

    public Activity(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     *
     * @see ch.softenvironment.jomm.DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        return DbCode.createDefaultDescriptor(Activity.class);
    }

}
