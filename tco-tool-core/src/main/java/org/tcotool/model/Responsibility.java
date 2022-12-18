package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Service responsible.
 *
 * @author Peter Hirzel
 */
public final class Responsibility extends DbCode {

    /**
     * Return the database mappings for this persistence object.
     * <p>
     * see ch.softenvironment.jomm.DbConnection#addDescriptor(Class)
     *
     * @see ch.softenvironment.jomm.DbObjectServer#register(Class, String)
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        return DbCode.createDefaultDescriptor(Responsibility.class);
    }

    public Responsibility(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }
}
