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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import org.tcotool.application.CostCauseDetailView;
import org.tcotool.tools.ModelUtility;

/**
 * Cause/kind of costs. According to Gartner costs are defined by 2 categories relevant in TCO: - direct costs - indirect costs
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class CostCause extends DbCode {

    // IliCode => DIRECT costs: HW, SW, Operation, Administration
    public static final String INSTALLATION = "Installation";
    public static final String SERVICE = "Service";
    public static final String INFRASTRUCTURE = "Infrastructure";
    public static final String EDUCATION = "Education";
    public static final String OPERATION = "Operation";
    public static final String OPERATIONHARDWARE = "OperationHardware";
    public static final String OPERATIONSOFTWARE = "OperationSoftware";
    public static final String INTEGRATION = "Integration";
    public static final String SOFTWARE = "Software";
    public static final String HARDWARE = "Hardware";
    public static final String STORAGE = "Storage";
    // IliCode => INDIRECT costs: 
    public static final String DOWNTIME = "Downtime"; // server down, non-productive time, etc
    public static final String END_USER_OPERATION = "EndUserOperation"; // if peers educate other users, User-Support, Surfing, etc

    /**
     * Return the database mappings for this persistence object.
     *
     * @see ch.softenvironment.jomm.DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        DbDescriptor descriptor = DbCode.createDefaultDescriptor(CostCause.class);
        descriptor.add("documentation", "documentation", new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        descriptor.add(DbCodeType.PROPERTY_ILICODE, DbCode.ATTRIBUTE_ILICODE, new DbTextFieldDescriptor(DbCode.ILI_CODE_LENGTH), new DbMultiplicityRange(0, 1));
        descriptor.add("direct", "direct", new DbFieldTypeDescriptor(java.lang.Boolean.class), new DbMultiplicityRange(1, 1));
        return descriptor;
    }

    public CostCause(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private String fieldIliCode;

    @Override
    public final String getIliCode() {
        return fieldIliCode;
    }

    /**
     * Define UNIQUE identifier (INTERLIS enumerator).
     *
     * @param iliCode
     */
    public final void setIliCode(String iliCode) {
        //TODO check whether new code is UNIQUE among all CostCause's
        String oldValue = fieldIliCode;
        fieldIliCode = iliCode;
        firePropertyChange("iliCode", oldValue, iliCode);
    }

    private Boolean fieldDirect;

    public final Boolean getDirect() {
        return fieldDirect;
    }

    /**
     * According to Gartner costs may be "direct" or "indirect".
     */
    public void setDirect(Boolean direct) {
        Boolean oldValue = fieldDirect;
        fieldDirect = direct;
        firePropertyChange("direct", oldValue, direct);
    }

    @Override
    public String getNameString() {
        String text = "";
        //  return getNameString(java.util.Locale.getDefault());
        if (getName() != null) {
            text = getName().getValue(ModelUtility.getCodeTypeLocale());
            if (getDirect().booleanValue()) {
                text = text + " [" + ResourceManager.getResourceAsNonLabeled(CostCauseDetailView.class, "LblDirect_text") + "]";
            } else {
                text = text + " [" + ResourceManager.getResourceAsNonLabeled(CostCauseDetailView.class, "LblIndirect_text") + "]";
            }
        }
        return text;
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