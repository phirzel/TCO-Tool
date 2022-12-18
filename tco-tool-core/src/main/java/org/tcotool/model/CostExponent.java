package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;

/**
 * Number representation in 1E, 1E3, 1E6. Useful to enhance readability of big amounts.
 *
 * @author Peter Hirzel
 */
public final class CostExponent extends DbEnumeration {

    public final static String ASIS = "AsIs";
    public final static String INTHOUSAND = "InThousand";
    public final static String INMILLION = "InMillion";

    public CostExponent(ch.softenvironment.jomm.DbObjectServer objectServer) {
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
        DbDescriptor descriptor = DbEnumeration.createDefaultDescriptor(CostExponent.class);

        descriptor.setOrdered(true);
        return descriptor;
    }
}
