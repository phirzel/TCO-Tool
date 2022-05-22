package ch.softenvironment.jomm;
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
import ch.softenvironment.jomm.descriptor.DbFieldType;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.model.DbObject;

/**
 * Map <b>Entities from Target-System</b> to <b>Java</b> and vice versa in an encoding/decoding manner. The <b>different types are mapped from one world to the other</b>. Also the technical Fields
 * like T_* are considered while mapping. by this utility, for e.g. an SQL CHAR to java.lang.String. Design Pattern: <b>Adapter</b>
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public interface DbMapper {

    // Technical Attributes corresponding to DbObject.PROPERTY_*
    String ATTRIBUTE_CREATE_DATE = "T_CreateDate";
    String ATTRIBUTE_LAST_CHANGE = "T_LastChange";
    String ATTRIBUTE_USER_ID = "T_User";
    String ATTRIBUTE_VERSION = "T_Version";        // Hibernate Timestamp locking (of type "int")
    String ATTRIBUTE_NAME_ID = "T_Id_Name";        // FK to DbNlsString in DbCodeType's
    String ATTRIBUTE_ORDERED = "T_Sequence";
    String ATTRIBUTE_CONCRETE_CLASS = "T_Type";    // root class must keep concrete child table here
    String ATTRIBUTE_ORDER_PREFIX = "T_Seq";       // for ordered lists
    String ATTRIBUTE_KEY_TABLE = "T_Key";
    // T_MAP_<code> Attributes
    String ATTRIBUTE_MAP_OWNER_ID = "T_Id_Owner";
    String ATTRIBUTE_MAP_OWNER_TYPE = "T_Type_Owner";
    String ATTRIBUTE_MAP_OWNER_ATTRIBUTE = "T_Attribute_Owner";
    String ATTRIBUTE_MAP_VALUE_ID = "T_Id_Value";
    // DbFieldType Attribute
    String ATTRIBUTE_MAP_VALUE = "T_Value";

    /**
     * Usually each Target-System uses its own Error-Codes thrown as an Exception in case of illegal interaction. Therefore a User might be interested in a more understandable (say less technical)
     * verbose explanation of the exception.
     *
     * @return String User-like error-explanation
     */
    String describeTargetException(Exception exception);

    /**
     * Return an Id for a given Key (also referred as Sequence sometimes).
     *
     * @param objectServer
     * @param transaction
     * @param key
     */
    Long getNewId(javax.jdo.PersistenceManager objectServer, javax.jdo.Transaction transaction, final String key);

    /**
     * Return whether the given collection has a next element.
     */
    boolean hasNext(Object collection);

    /**
     * Map <i>Collection of Objects</i>: <b>Target-System => Java</b>. Maps values of given collection to given DbObject's Properties.
     *
     * <b>Only Properties of the given Descriptor are considered</b>, therefore
     * the correct Object hierarchy must be managed by the caller.
     *
     * @param object
     * @param descriptor
     * @param collection (Query Results -> only plain attributes, such as fields and foreign key ID's)
     */
    void mapFromTarget(DbObject instance, DbDescriptor descriptor, Object collection) throws Exception;

    /**
     * @see #mapFromTargetString(..)
     */
    java.math.BigDecimal mapFromTargetBigDecimal(Object collection, String attribute);

    /**
     * Map <i>Type</i>: <b>Target-System => Java</b>. Assumes Textdb-Field with 'T' for true and 'F' for false.
     *
     * @see #mapFromTargetString(..)
     */
    Boolean mapFromTargetBoolean(Object collection, String attribute);

    /**
     * @see #mapFromTargetString(..)
     */
    java.util.Date mapFromTargetDate(Object collection, String attribute, final int type);

    /**
     * Map special types.
     */
    DbFieldType mapFromTargetDbFieldType(Object collection, String attribute);

    /**
     * @see #mapFromTargetString(..)
     */
    Double mapFromTargetDouble(Object collection, String attribute);

    /**
     * @see #mapFromTargetString(..)
     */
    Integer mapFromTargetInteger(Object collection, String attribute);

    /**
     * @see #mapFromTargetString(..)
     */
    Long mapFromTargetLong(Object collection, String attribute);

    /**
     * @see mapFromTargetBoolean()
     * @deprecated
     */
    boolean mapFromTargetNativeBoolean(Object collection, String attribute);

    /**
     * @see mapFromTargetInteger()
     * @deprecated
     */
    int mapFromTargetNativeInt(Object collection, String attribute);

    /**
     * Maps from <b>Target-System => Java</b>.
     *
     * @param collection ResultSet from a query
     * @param attribute expected Attribute in collection
     */
    String mapFromTargetString(Object collection, String attribute);

    /**
     * Map <i>Type</i>: <b>Java => Target-System</b>.
     *
     * @param value Application value of a Model
     * @return String given value as an SQL-Query argument
     */
    Object mapToTarget(DbFieldType value);

    /**
     * Map <i>Type</i>: <b>Java => Target-System</b>. Only the Reference-Type of the given value will be mapped.
     *
     * @param value Aggregated Object as Application value of a Model
     * @return String given value as an SQL-Query argument
     */
    Object mapToTarget(DbObject value);

    /**
     * Map <i>Type</i>: <b>Java => Target-System</b>. The value of given change will be mapped here.
     *
     * @deprecated should be done in DbQueryBuilder
     */
    Object mapToTarget(DbPropertyChange change);

    /**
     * Map <i>Type</i>: <b>Java => Target-System</b>. Tri-State (TRUE, FALSE or NULL).
     *
     * @param value Application value of a Model
     * @return String given value as an SQL-Query argument
     */
    Object mapToTarget(Boolean value);

    /**
     * Map <i>Type</i>: <b>Java => Target-System</b>.
     *
     * @param value Application value of a Model
     * @return String given value as an SQL-Query argument
     */
    Object mapToTarget(Number value);

    /**
     * Map <i>Type</i>: <b>Java => Target-System</b>.
     *
     * @param value Application value of a Model
     * @return String given value as an SQL-Query argument
     */
    Object mapToTarget(String value);

    /**
     * Map <i>Type</i>: <b>Java => Target-System</b>.
     *
     * @param value Application value of a Model
     * @param type see DbDateFieldDescriptor
     * @return String given value as an SQL-Query argument
     */
    Object mapToTarget(java.util.Date value, final int type);

    /**
     * Return a qualified attribute-name on Target-System.
     */
    String mapToTargetQualified(String qualifier, String attribute);

    /**
     * Return the technical name of the "Identity" property.
     *
     * @return
     */
    String getTargetIdName();

    /**
     * Return the name where NLS-String's are managed.
     *
     * @return
     * @see #getTargetNlsTranslationName()
     */
    String getTargetNlsName();

    /**
     * Return the name where NLS-Translations's are managed.
     *
     * @return
     * @see #getTargetNlsName()
     */
    String getTargetNlsTranslationName();

    /**
     * Return the name where removed objects are historized.
     *
     * @return
     * @see #getTargetNlsTranslationName()
     */
    String getTargetRemoveHistoryName();
}
