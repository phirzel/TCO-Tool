package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Code for Activities a Person can do (for e.g. "Support")
 *
 * @author Peter Hirzel
 */
public final class Activity extends DbCode {

    public Activity(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     * <p>
     * see ch.softenvironment.jomm.DbConnection#addDescriptor(Class)
     *
     * @see ch.softenvironment.jomm.DbObjectServer#register(Class, String)
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        return DbCode.createDefaultDescriptor(Activity.class);
    }

}
