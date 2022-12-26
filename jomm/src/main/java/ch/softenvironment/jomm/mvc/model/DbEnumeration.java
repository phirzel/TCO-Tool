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
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.util.DeveloperException;
import lombok.extern.slf4j.Slf4j;

/**
 * Enumeration type, which is <b>strictly ReadOnly</b> since it is defined by a proper Model. An Attribute <b>IliCode</b> guarantees for Unique and correct assignment. These Objects are meant to be
 * READ-ONLY!
 * <p>
 * When implementing a concrete DbEnumeration subclass: 1) Declare the next extending Class as final (no subclasses allowed) 2) implement this method: Return the database mappings for this persistence
 * object.
 *
 * @author Peter Hirzel
 * @see DbObjectServer#register(Class, String)
 */
@Slf4j
public abstract class DbEnumeration extends DbObject implements DbCodeType {

    // read only fields written by DbObjectServer when mapped from Target
    private DbNlsString fieldName;
    private String fieldIliCode;

    /**
     * Check whether given enumeration matches to UNIQUE iliCode.
     *
     * @param enumeration DbEnumeration
     * @param iliCode
     * @return
     */
    public static boolean isIliCode(DbCodeType enumeration, final String iliCode) {
        if ((enumeration == null) || (iliCode == null)) {
            throw new IllegalArgumentException("enumeration and iliCode must not be null");
        }
        if (enumeration.getIliCode() == null) {
            log.warn("Developer warning: DbEnumeration <" + enumeration + "]> without IliCode");
            return false;
        }
        return enumeration.getIliCode().equals(iliCode);
    }

    /**
     * @see DbObject(DbObjectServer)
     */
    protected DbEnumeration(DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDefaultDescriptor(Class<? extends DbEnumeration> dbCode) {
        DbDescriptor descriptor = new DbDescriptor(dbCode);
        descriptor.addNlsString(PROPERTY_NAME, DbMapper.ATTRIBUTE_NAME_ID, new DbMultiplicityRange(1, 1));
        descriptor.add(DbCodeType.PROPERTY_ILICODE, DbCode.ATTRIBUTE_ILICODE, new DbTextFieldDescriptor(DbCode.ILI_CODE_LENGTH), new DbMultiplicityRange(1, 1));
        return descriptor;
    }

    /**
     * Return the UNIQUE ILI-Code constant. Use {@link #isIliCode(DbCodeType, String)} for comparisons.
     */
    @Override
    public final String getIliCode() {
        return fieldIliCode;
    }

    /**
     * Return the ID of a code uniquely identified by ILI-Code.
     *
     * @deprecated @see DbObjectServer#getIliCode(Class, ilicode)
     */
    public static Long getIliCodeId(DbObjectServer objectServer, Class<? extends DbCodeType> dbCode, final String iliCode) {
        try {
            DbQueryBuilder builder = objectServer.createQueryBuilder(DbQueryBuilder.SELECT, "by IliCode");
            builder.setTableList((Class<? extends DbObject>) dbCode);
            builder.addFilter(DbCode.ATTRIBUTE_ILICODE, iliCode, DbQueryBuilder.STRICT);

            Object id = objectServer.getFirstValue(builder);
            if (id == null) {
                log.warn("IliCode-Id not found for: {} -> {}", dbCode.getName(), iliCode);
                return null;
            } else {
                return Long.valueOf(id.toString());
            }
        } catch (Exception e) {
            log.error("{}->{}", dbCode.getName(), iliCode, e);
            throw new DeveloperException("Fehler beim Lesen des Codes: " + dbCode.getName() + "->" + iliCode, "Code-Fehler", e);
        }
    }

    /**
     * Gets the name property (ch.softenvironment.jomm.DbNlsString) value.
     *
     * @return The name property value.
     */
    @Override
    public DbNlsString getName() {
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
     * @return NLS-Text
     */
    @Override
    public String getNameString(java.util.Locale locale) {
        return getName() == null ? "" : getName().getValue(locale);
    }
}
