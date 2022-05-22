package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Attach a Process of a certain process-model to a CostDriver.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class Process extends DbCode {

    /**
     * Return the database mappings for this persistence object.
     *
     * @see ch.softenvironment.jomm.DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        return DbCode.createDefaultDescriptor(Process.class);
    }

    public Process(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }
}
