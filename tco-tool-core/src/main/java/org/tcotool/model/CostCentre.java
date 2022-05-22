package org.tcotool.model;
/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;

/**
 * Cost centre (de: Kostenstelle). Accounting category.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class CostCentre extends DbCode {

    public static final String INTERNAL = "Internal";
    public static final String INFRASTRUCTURE = "Infrastructure";
    public static final String MERCHANDISING = "Merchandising";
    public static final String EDUCATION = "Education";
    public static final String ADMINISTRATION = "Administration";
    public static final String PRODUCTION = "Production";

    /**
     * Return the database mappings for this persistence object.
     *
     * @see ch.softenvironment.jomm.DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        DbDescriptor descriptor = DbCode.createDefaultDescriptor(CostCentre.class);

        descriptor.add("documentation", "documentation", new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        descriptor.add(DbCodeType.PROPERTY_ILICODE, DbCode.ATTRIBUTE_ILICODE, new DbTextFieldDescriptor(DbCode.ILI_CODE_LENGTH), new DbMultiplicityRange(0, 1));
        return descriptor;
    }

    public CostCentre(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private String fieldIliCode;

    @Override
    public final String getIliCode() {
        return fieldIliCode;
    }

    /**
     * Makes sense here because morphed from CostType-DbEnumeration.
     *
     * @param iliCode
     */
    public final void setIliCode(String iliCode) {
        //TODO check whether new code is UNIQUE among all CostCentre's
        String oldValue = fieldIliCode;
        fieldIliCode = iliCode;
        firePropertyChange("iliCode", oldValue, iliCode);
    }

    private String fieldDocumentation;

    public String getDocumentation() {
        refresh(false); // read lazy initialized objects
        return fieldDocumentation;
    }

    /**
     * Note about CostCause.
     *
     * @param documentation
     */
    public void setDocumentation(String documentation) {
        String oldValue = fieldDocumentation;
        fieldDocumentation = documentation;
        firePropertyChange("documentation", oldValue, fieldDocumentation);
    }
}
