package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;

/**
 * Enumeration definition of a company branch.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class Branch extends DbEnumeration {

    public static final String INDUSTRY = "Industry";
    public static final String GOVERNMENT = "Government";
    public static final String HEALTHCARE = "HealthCare";
    public static final String CHEMICALINDUSTRY = "ChemicalIndustry";
    public static final String BANKINSURANCE = "BankInsurance";
    public static final String TRAVELTRANSPORTATION = "TravelTransportation";
    public static final String CONSULTING = "Consulting";
    public static final String INFORMATIONTECHNOLOGY = "InformationTechnology";

    public Branch(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     *
     * @see DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor() {
        return DbEnumeration.createDefaultDescriptor(Branch.class);
    }
}
