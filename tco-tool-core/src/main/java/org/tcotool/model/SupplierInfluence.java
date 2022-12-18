package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;

/**
 * Influence of a Dependency-supplier.
 *
 * @author Peter Hirzel
 */
public final class SupplierInfluence extends DbEnumeration {

    public static final String AIDING2 = "Aiding2";
    public static final String AIDING1 = "Aiding1";
    public static final String NEUTRAL0 = "Neutral0";
    public static final String REPRESSIVE1 = "Repressive1";
    public static final String REPRESSIVE2 = "Repressive2";

    public SupplierInfluence(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     * <p>
     * see ch.softenvironment.jomm.DbConnection.addDescriptor()
     *
     * @see ch.softenvironment.jomm.DbObjectServer#register(Class, String)
     */
    public static DbDescriptor createDescriptor(/*Class<DbEnumeration> dbCode*/) {
        DbDescriptor descriptor = DbEnumeration.createDefaultDescriptor(SupplierInfluence.class);
        descriptor.setOrdered(true);
        return descriptor;
    }
}
