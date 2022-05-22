package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;

/**
 * Life-cycle of a CostDriver.
 */
@Deprecated
public final class LifeCycle extends DbEnumeration {

    public static final String DEVELOPMENT = "Development";
    public static final String OPERATION = "Operation";
    public static final String DISPLACEMENT = "Displacement";

    public LifeCycle(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     *
     * @see DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor(/*Class<LifeCycle> dbCode*/) {
        DbDescriptor descriptor = DbEnumeration.createDefaultDescriptor(LifeCycle.class);
        descriptor.setOrdered(true);
        return descriptor;
    }
}
