package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * @deprecated
 */

/**
 * Attach a ProjectPhase of a certain process-model to a CostDriver.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class ProjectPhase extends DbCode {

    public static final String INITIALISATION = "Initialisation";
    public static final String ANALYSIS = "Analysis";
    public static final String CONCEPT = "Concept";
    public static final String REALISATION = "Realisation";
    public static final String TRANSITION = "Transition";
    public static final String DEPLOYMENT = "Deployment";

    public ProjectPhase(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this Object.
     *
     * @return DbDescriptor
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        DbDescriptor descriptor = DbCode.createDefaultDescriptor(ProjectPhase.class);
        descriptor.setOrdered(true);
        return descriptor;
    }
}
