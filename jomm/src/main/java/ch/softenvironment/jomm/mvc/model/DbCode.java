package ch.softenvironment.jomm.mvc.model;

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

import ch.softenvironment.jomm.DbMapper;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.util.DeveloperException;

/**
 * Unique Code which might be changed by User. Classes extending this DbCode may add other persistent Properties to descriptor.
 * <p>
 * When implementing a concrete DbCode subclass: 1) Declare the next extending Class as final (no subclasses allowed) 2) implement this method: Return the database mappings for this persistence
 * object.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see DbObjectServer.register()
 * @see ch.softenvironment.jomm.DbConnection.addDescriptor() public static DbDescriptor createDescriptor() { DbDescriptor descriptor = DbCode.createDefaultDescriptor(MyCode.class); // add your
 *     extensions return descriptor; }
 */
public abstract class DbCode extends DbChangeableBean implements DbCodeType {

    protected static final int ILI_CODE_LENGTH = 254;
    protected static final String ATTRIBUTE_ILICODE = "IliCode";

    private DbNlsString fieldName;

    // private String fieldIliCode;

    /**
     * @see #DbObject(DbObjectServer)
     */
    protected DbCode(DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Create a minimal Descriptor for the given DbCode subclass.
     *
     * @param dbCode
     * @return
     */
    public static DbDescriptor createDefaultDescriptor(Class<? extends DbCodeType> dbCode) {
        DbDescriptor descriptor = new DbDescriptor((Class<? extends DbObject>) dbCode);
        descriptor.addNlsString(PROPERTY_NAME, DbMapper.ATTRIBUTE_NAME_ID, new DbMultiplicityRange(1, 1));
        return descriptor;
    }

    /**
     * Return the UNIQUE ILI-Code constant (according to INTERLIS 2).
     */
    @Override
    public String getIliCode() {
        throw new DeveloperException("Might be implemented by inherited code optionally if making sense");
    }

    /*
     * public final void setIliCode(String iliCode) { if (getIliCode() != null)
     * { throw new DeveloperException(this, "setIliCode()",
     * "IliCode must not be changed!"); }
     *
     * String oldValue = fieldIliCode; fieldIliCode = iliCode;
     * firePropertyChange("iliCode", oldValue, iliCode); }
     */

    /**
     * Gets the name property (ch.softenvironment.jomm.DbNlsString) value.
     *
     * @return The name property value.
     * @see #setName
     */
    @Override
    public DbNlsString getName() {
        refresh(false);
        return fieldName;
    }

    /**
     * @see #getNameString(java.util.Locale)
     */
    @Override
    public String getNameString() {
        return getNameString(java.util.Locale.getDefault());
    }

    /**
     * Return the property "name" for the <b>given</b> Locale.
     *
     * @return NLS-text
     */
    @Override
    public String getNameString(java.util.Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("locale must not be null");
        }
        return getName() == null ? "" : getName().getValue(locale);
    }

    /**
     * Set the NLS-Name of the code.
     */
    public void setName(DbNlsString name) {
        DbNlsString oldValue = fieldName;
        fieldName = name;
        firePropertyChange("name", oldValue, name);
    }
}