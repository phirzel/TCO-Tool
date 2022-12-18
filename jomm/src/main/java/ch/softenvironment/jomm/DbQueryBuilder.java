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

import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbFieldType;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.ListUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Every target Persistence System (for e.g. a Relational Database Management System) provides its own language to interact with. This Utility provides a Target Language independent API to formulate
 * any requests to be communicated to the Target-System.
 * <p>
 * SQL knows three different language-types: - Data Definition Language (DDL) => CREATE/ALTER/DROP - Data Manipulation Language (DML) => INSERT/UPDATE/DELETE - Data Query Language (DQL) => SELECT
 *
 * @author Peter Hirzel
 */
public abstract class DbQueryBuilder {

    // SQL DQL
    public final static int SELECT = 100;
    // SQL DML
    public final static int INSERT = 101;
    public final static int UPDATE = 102;
    public final static int DELETE = 103;
    // SQL DDL
    public final static int CREATE = 104;
    public final static int DROP = 105;
    public final static int ALTER = 106;
    public final static int LOCK = 107;
    public final static int UNLOCK = 108;
    public final static int RAW = 109;
    private transient int type = -1;

    // String comparison types
    public final static int STRICT = 101;
    public final static int WILD = 102;
    public final static int PHONETIC = 103;

    public final static int FETCHBLOCK_UNLIMITED = 0;

    private transient DbObjectServer objectServer = null;
    private transient String useCase = null;
    protected transient boolean suppressQuery = false;
    private transient int fetchBlockSize = FETCHBLOCK_UNLIMITED; // default
    private transient Locale defaultLocale = null;

    /**
     * DbQueryBuilder constructor comment.
     *
     * @param objectServer
     * @param queryType Type of SQL Syntax (SELECT, INSERT, UPDATE, DELETE, etc)
     * @param useCase Developer Description of query (used as title in performance Statistics)
     */
    public DbQueryBuilder(DbObjectServer objectServer, final Integer queryType, final String useCase) {
        super();

        this.objectServer = objectServer;
        this.type = queryType.intValue();
        this.useCase = useCase;

        if ((type < SELECT) || (type > RAW)) {
            throw new ch.softenvironment.util.DeveloperException("non-supported QueryType");
        }

        if (type == UPDATE) {
            // do not UPDATE until at least ONE Attribute shall be changed in
            // Target-System
            // @see #appendAttribute(..)
            suppressQuery = true;
        }
    }

    /**
     * Add a filter where each DbObject within dbObjects-List will be compared by OR.
     */
    public abstract void addFilter(final String attribute, Object[] dbObjects);

    /**
     * Add a filter to compare to a NLS-String.
     */
    public abstract void addFilter(final String attribute, DbNlsString nlsString);

    /**
     * Add a filter to compare to a given DbObject.
     */
    public abstract void addFilter(final String attribute, DbObject object);

    /**
     * Add a filter to compare with a Boolean value.
     */
    public abstract void addFilter(final String attribute, Boolean value);

    /**
     * Add a filter condition to the Query where the given attribute's value
     * <b>must equal (==)</b> the given value in the Target-System. If
     * concatenated with other Conditions this condition will be appended by a
     * <b>logical AND</b>.
     * <p>
     * The value will be Transformed in a Target-System understandable Format.
     *
     * @param attribute the FieldName in the Target-System
     * @param value the Value of the referenced Field
     */
    public abstract void addFilter(final String attribute, Number value);

    /**
     * Add a filter to compare to a String. According to given type, the Query-Result may conclude more or less Search-Objects.
     * <p>
     * If type is WILD wildcards such as '*' or '?' may be used in value.
     *
     * @param type (STRICT, WILD, PHONETIC)
     */
    public abstract void addFilter(final String attribute, final String value, final int type);

    /**
     * AND's a filter to compare attribute to a sublist, for e.g. as a
     * <b>Sub-SELECT</b> like WHERE attribute IN (values[1], values[2],
     * values[3], ..)
     *
     * @param attribute
     * @param values Suppress query if Set is empty
     */
    public abstract void addFilter(final String attribute, java.util.Set<?> values);

    /**
     * AND's a filter to compare attribute to a sub-query, for e.g. as a
     * <b>Sub-SELECT</b> like WHERE attribute IN (SELECT x FROM..)
     *
     * @param attribute
     * @param subQuery (typically of type SELECT with one attribute in resultSet)
     */
    public abstract void addFilter(final String attribute, DbQueryBuilder subQuery);

    /**
     * Add a filter to compare to a Date-Range.
     *
     * @param from see DbDateFieldDescriptor.DATE
     * @param to see DbDateFieldDescriptor.DATE
     */
    public abstract void addFilterBetweenAndDate(final String attribute, java.util.Date from, java.util.Date to);

    /**
     * Add a filter where only Children of the given concrete type in a inheritance-Chain are desired.
     * <p>
     * Use this clause only for Abstract-Root-Tables (by means having a 'T_Type' Attribute).
     *
     * @param dbObject concrete child expected
     */
    public final void addFilterChildren(java.lang.Class<? extends DbObject> dbObject) {
        addFilter(DbMapper.ATTRIBUTE_CONCRETE_CLASS, getObjectServer().getTargetName(dbObject), STRICT);
    }

    /**
     * Attribute must be greater (>) than value.
     *
     * @see #addFilter(String, Number)
     */
    public abstract void addFilterGreater(final String attribute, Number value);

    /**
     * Attribute must be lower (<) than value.
     *
     * @see #addFilter(String, Number)
     */
    public abstract void addFilterLower(final String attribute, Number value);

    /**
     * Attribute must not equal to value.
     *
     * @see #addFilter(String, Number)
     */
    public abstract void addFilterNotEquals(final String attribute, Number value);

    /**
     * Add a filter condition to the Query where the given. - value == null => ignore resp. no WHERE clause at all - value == true => attribute IS NULL - value == false => attribute IS NOT NULL
     */
    public abstract void addFilterNull(final String attribute, Boolean value);

    /**
     * Vice versa of {@link #addFilterNull(String, Boolean)})
     *
     * @see #addFilterNull(String, Boolean)
     */
    public final void addFilterNotNull(final String attribute, Boolean value) {
        if (value == null) {
            addFilterNull(attribute, value);
        } else {
            addFilterNull(attribute, Boolean.valueOf(!value.booleanValue()));
        }
    }

    /**
     * Add a filter condition to the Query where the given condition will just be passed "as is" to the Target-System.
     *
     * @param condition well-formated for Target system
     */
    public abstract void addFilterRaw(final String condition);

    /**
     * Order after the given attribute in ASCending order.
     *
     * @see #addOrder(String, boolean)
     */
    public final void addOrder(final String attribute) {
        addOrder(attribute, true);
    }

    /**
     * Add an Attribute to Sort Order ASCendingly (for e.g. SQL ORDER BY) The sort Order is built in the same sequence as added by this method. (A javax.jdo.Query.setOrdering(String) method would
     * expect the sum of all these Ordering in a separated String.)
     *
     * @param attribute Attribute to sort a certain select.
     * @param ascending true->ASCending; false=>DESCending
     * @see javax.jdo.Query#setOrdering(String)
     */
    public abstract void addOrder(final String attribute, boolean ascending);

    /**
     * Append an Attribute to AttributeList.
     *
     * @param attribute Database-Field (for e.g. "ATTR1")
     * @param value DbObject's Id to be set for Field
     */
    public final void append(final String attribute, DbFieldType value) {
        appendRaw(attribute, (String) getObjectServer().getMapper().mapToTarget(value));
    }

    /**
     * Append an Attribute to AttributeList.
     *
     * @param attribute Database-Field (for e.g. "ATTR1")
     * @param value DbObject's Id to be set for Field
     */
    public final void append(final String attribute, DbObject value) {
        appendRaw(attribute, (String) getObjectServer().getMapper().mapToTarget(value));
    }

    /**
     * Append an Attribute to AttributeList (for e.g. SQL INSERT- or UPDATE-Queries).
     *
     * @param attribute Database-Field (for e.g. "T_Id")
     * @param value Long to be set for Field
     */
    public final void append(final String attribute, Number value) {
        appendRaw(attribute, (String) getObjectServer().getMapper().mapToTarget(value));
    }

    /**
     * Append an Attribute to AttributeList (for e.g. SQL INSERT- or UPDATE-Queries).
     *
     * @param attribute Database-Field (for e.g. "ATTR1")
     * @param value String to be set for Field
     */
    public final void append(final String attribute, String value) {
        appendRaw(attribute, (String) getObjectServer().getMapper().mapToTarget(value));
    }

    /**
     * Append an Attribute to AttributeList (for e.g. SQL INSERT- or UPDATE-Queries).
     *
     * @param attribute Database-Field (for e.g. "ATTR1")
     * @param value Boolean to be set for Field
     */
    public final void append(final String attribute, Boolean value) {
        appendRaw(attribute, (String) getObjectServer().getMapper().mapToTarget(value));
    }

    /**
     * Append an Attribute to AttributeList.
     *
     * @param attribute Database-Field (for e.g. "ATTR1")
     * @param value
     * @param type
     * @see DbDateFieldDescriptor
     */
    public final void append(final String attribute, java.util.Date value, final int type) {
        appendRaw(attribute, (String) getObjectServer().getMapper().mapToTarget(value, type));
    }

    /**
     * @see #appendInternal(String)
     */
    public final void appendInternal(Class<? extends DbObject> dbObject) {
        appendInternal(getObjectServer().getTargetName(dbObject));
    }

    /**
     * Append Technical Fields for Timestamp-Locking and Historizing in both cases: querying or saving persistently. To implement a correct optimistic Locking Philosophy, the following Attributes are
     * always transferred to Target-System (or middleware if any): - T_User - T_LastChange - T_CreateDate
     *
     * @param qualifier if (given) "QUALIFIER.T_Id" else "T_Id"
     * @return in case of saving the created timestamp value will be returned, otherwise null
     */
    public final java.util.Date appendInternal(final String qualifier) {
        //TODO make protected
        if (type == SELECT) {
            appendRaw(getObjectServer().getMapper().mapToTargetQualified(qualifier, getObjectServer().getMapper().getTargetIdName()), null);
            appendRaw(getObjectServer().getMapper().mapToTargetQualified(qualifier, DbMapper.ATTRIBUTE_CREATE_DATE), null);
            appendRaw(getObjectServer().getMapper().mapToTargetQualified(qualifier, DbMapper.ATTRIBUTE_LAST_CHANGE), null);
            appendRaw(getObjectServer().getMapper().mapToTargetQualified(qualifier, DbMapper.ATTRIBUTE_USER_ID), null);
        } else if ((type == UPDATE) || (type == INSERT)) {
            append(DbMapper.ATTRIBUTE_USER_ID, getObjectServer().getUserId());
            // TODO some DBMS offer server-side functions to set current
            // timestamp -> make this customizable
            java.util.Date timestamp = new java.util.Date();
            String ts = (String) getObjectServer().getMapper().mapToTarget(timestamp, DbDateFieldDescriptor.DATETIME);
            appendRaw(getObjectServer().getMapper().mapToTargetQualified(qualifier, DbMapper.ATTRIBUTE_LAST_CHANGE), ts);
            if (type == INSERT) {
                appendRaw(getObjectServer().getMapper().mapToTargetQualified(qualifier, DbMapper.ATTRIBUTE_CREATE_DATE), ts);
            }
            return timestamp;
        }

        return null;
    }

    /**
     * Append an already pre-formated value for the Target-System. No Transformation will be done on the valueFormated any more.
     *
     * @param attribute Database-Field (for e.g. "ATTR1")
     * @param valueFormated String to be set for Field
     */
    protected abstract void appendRaw(final String attribute, final String valueFormated);

    /**
     * Convert list of DbObjects to a Set of their Id's.
     *
     * @param dbObjects
     * @return
     */
    public static final java.util.Set<Long> convertToIdSet(java.util.List<? extends DbObject> dbObjects) {
        java.util.Set<Long> ids = new HashSet<Long>(dbObjects.size());
        Iterator<? extends DbObject> it = dbObjects.iterator();
        while (it.hasNext()) {
            DbObject obj = it.next();
            ids.add(obj.getId());
        }
        return ids;
    }

    /**
     * Return a Query for Code-entries[0..*] kept in a T_MAP_
     * <code> for a specific owner-instance.
     * For e.g.: Adjudication.category[0..*] SELECT DbMapper.ATTRIBUTE_MAP_VALUE_ID,* FROM T_MAP_Category WHERE (T_Type_Owner='Adjudication') AND (T_Id_Owner=12) AND
     * (T_Attribute_Owner='T_Id_Category')"
     *
     * @param queryType .SELECT/INSERT/DELETE
     * @return DbQueryBuilder
     */
    public static final DbQueryBuilder createMapClause(final int queryType, DbObject instance, DbDescriptorEntry entry) throws Exception {
        DbQueryBuilder builder = instance.getObjectServer().createQueryBuilder(queryType, "1:n by <" + entry.getMapName() + ">");
        builder.setTableList(entry.getMapName());
        java.lang.Class<? extends DbObject> rootClass = null;
        Long foreignKeyId = null;
        if (entry.getCloned() == null) {
            // entry is for instance itself
            rootClass = instance.getObjectServer().getConnection().getRootClassFor(instance.getClass());
            foreignKeyId = instance.getId();
        } else {
            // entry might be for another Class-Mapping
            rootClass = instance.getObjectServer().getConnection().getRootClassFor(entry.getCloned());
            DbPropertyChange change = new DbPropertyChange(instance, entry.getOwnerIdPropertyName());
            foreignKeyId = (Long) change.getValue();
        }
        if (queryType == INSERT) {
            builder.append(DbMapper.ATTRIBUTE_MAP_OWNER_TYPE, instance.getObjectServer().getTargetName(rootClass));
            builder.append(DbMapper.ATTRIBUTE_MAP_OWNER_ID, foreignKeyId);
            builder.append(DbMapper.ATTRIBUTE_MAP_OWNER_ATTRIBUTE, entry.getOtherPropertyName());
        } else if ((queryType == SELECT) || (queryType == DELETE)) {
            // @see #createQueryAllCodeOwner()
            builder.addFilter(DbMapper.ATTRIBUTE_MAP_OWNER_TYPE, instance.getObjectServer().getTargetName(rootClass), DbQueryBuilder.STRICT);
            builder.addFilter(DbMapper.ATTRIBUTE_MAP_OWNER_ID, foreignKeyId);
            builder.addFilter(DbMapper.ATTRIBUTE_MAP_OWNER_ATTRIBUTE, entry.getOtherPropertyName(), DbQueryBuilder.STRICT);
        } else {
            // TODO testsuite whether UPDATE makes sense
            throw new DeveloperException("not yet tested!");
        }

        return builder;
    }

    /**
     * Return a Query to find all DbObjects of type rootClass having a DbCodeType associated for given property, such as rootClass#property:DbCodeType, where Range is either: - [0..1] direct
     * association - [0..*] via T_MAP_<code>.
     * <p>
     * Useful as Sub-SELECT, such as "..WHERE rootClass.T_Id_<property> IN (#createQueryAllCodeOwner())"
     *
     * @param server
     * @param dbObject the class having the property in its DbDescriptor
     * @param property of DbCodeType
     * @param codeIds
     * @return
     * @see #createIntersectionAllOwnerForCode(DbObjectServer, Class, String, Set) for INTERSECT resp. "AND" owners
     */
    public static DbQueryBuilder createQueryAllCodeOwner(DbObjectServer server, Class<? extends DbObject> dbObject, final String property, Set<Long> codeIds) {
        // @see #createMapClause()
        DbDescriptorEntry entry = server.getDescriptorEntry(dbObject, property);
        if (entry.getRange().getUpper() > 1) {
            // property[0..*]: DbCodeType => use T_MAP_<code>
            DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT,
                dbObject + " associating DbCodeType's <" + property + "> via <" + entry.getMapName() + ">");
            builder.setTableList(entry.getMapName());
            builder.setAttributeList(DbMapper.ATTRIBUTE_MAP_OWNER_ID);
            builder.addFilter(DbMapper.ATTRIBUTE_MAP_OWNER_TYPE, server.getTargetName(dbObject), DbQueryBuilder.STRICT);
            builder.addFilter(DbMapper.ATTRIBUTE_MAP_OWNER_ATTRIBUTE, entry.getOtherPropertyName(), DbQueryBuilder.STRICT);

            // if codeIds is empty => return all instances associating all codes
            builder.addFilter(DbMapper.ATTRIBUTE_MAP_VALUE_ID, codeIds);
            return builder;
        } else {
            // property[0..1]: DbCodeType
            DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, dbObject + " associating DbCodeType <" + property + ">");
            builder.setTableList(dbObject);
            builder.addFilter(server.getAttribute(dbObject, property), codeIds);
            return builder;
        }
    }

    /**
     * Create a Set of all Owners having a DbCodeType given in a Set of codeIDs. The resulting Set contains a List of Owner-Id's which INTERSECTs (resp. AND-operator) the codeIDs.
     *
     * @param server
     * @param dbObject
     * @param property
     * @param codeIDs Should be Set of Long
     * @return set of dbObject.T_Id_<property> values
     * @see #createQueryAllCodeOwner(DbObjectServer, Class, String, Set) for UNION resp. "OR" owners
     */
    public static java.util.Set<Object> createIntersectionAllOwnerForCode(DbObjectServer server, java.lang.Class<? extends DbObject> dbObject, final String property,
        Set<Long> codeIDs) {
        // TODO return Set<Long>; tune if target-system is able to
        // "(SELECT T_Id FROM ..) INTERSECT (SELECT T_Id FROM ..)"
        Set<Object> intersection = new HashSet<Object>();
        Iterator<Long> it = codeIDs.iterator();
        if (it.hasNext()) {
            // initialize set with hits from first codeId
            intersection.addAll(getIntersectionIdResults(server, dbObject, property, it.next()));
            while (it.hasNext()) {
                Set<Object> tmp = new HashSet<Object>();
                tmp.addAll(getIntersectionIdResults(server, dbObject, property, it.next()));
                intersection = ListUtils.createIntersection(intersection, tmp);
            }
        }
        return intersection;
    }

    /**
     * Helper method for {@link #createIntersectionAllOwnerForCode(DbObjectServer, Class, String, Set)} )
     */
    private static List<Object> getIntersectionIdResults(DbObjectServer server, Class<? extends DbObject> dbObject, final String property, Long codeId) {
        Set<Long> singleCode = new HashSet<Long>(1);
        singleCode.add(codeId);
        return server.getFirstValues(createQueryAllCodeOwner(server, dbObject, property, singleCode));
    }

    /**
     * Return a generic SELECT-Query to select all Instances of given Class (subtype of DbObject).
     *
     * @param dbObject Subclass of DbObject (will be used as FROM-Table in SELECT)
     * @return DbQueryBuilder
     */
    public static final DbQueryBuilder createQueryAllInstances(DbObjectServer objectServer, Class<? extends DbObject> dbObject) {
        DbQueryBuilder builder = objectServer.createQueryBuilder(DbQueryBuilder.SELECT, dbObject.getName());
        builder.setTableList(dbObject);
        return builder;
    }

    /**
     * Return a generic SQL-INSERT/UPDATE Query for this DbObject.
     *
     * @param dbObject (concrete Object in a inheritance chain)
     * @param instance of given type dbObject
     */
    public static final DbQueryBuilder createChangeObject(Class<? extends DbObject> dbObject, DbChangeableBean instance) {
        int type = DbQueryBuilder.UPDATE;
        if (instance.jdoIsNew()) {
            type = DbQueryBuilder.INSERT;
        }
        DbQueryBuilder builder = instance.getObjectServer().createQueryBuilder(type, dbObject.getName());
        DbDescriptor currentDescriptor = instance.getObjectServer().getDescriptor(dbObject);
        Iterator<String> properties = null;
        if (instance.jdoIsNew()) {
            // INSERT
            // if (!(instance instanceof DbCodeType)) {
            // store concrete child in root-object for inheritance chains
            Class<? extends DbObject> rootClass = instance.getObjectServer().getConnection().getRootClassFor(dbObject);
            if ((!DbObject.isCode(dbObject) /*
             * must not be inherited and need
             * therefore no T_Type Attribute
             */) && ((rootClass.equals(dbObject) /*
             * case
             * :
             * BaseClass
             * itself
             */) || (currentDescriptor.getMappedClass().equals(rootClass) /*
             * case
             * :
             * Inheritance
             * Chain
             */))) {
                builder.append(DbMapper.ATTRIBUTE_CONCRETE_CLASS, instance.getObjectServer().getTargetName(instance.getClass()));
            }
            // }

            // insert ALL Properties (prevent Database-Default Values)
            properties = currentDescriptor.iteratorProperties();
        } else {
            // UPDATE
            builder.addFilter(instance.getObjectServer().getMapper().getTargetIdName(), instance); // setUniqueConstraints(currentDescriptor,
            // instance);

            // update CHANGED properties ONLY for performance reasons
            // (DB-Referentials, DB-Index, etc)
            properties = instance.iteratorChangedProperties();
        }

        builder.setTableList(dbObject);

        // append properties to change
        while (properties.hasNext()) {
            String property = properties.next();
            if (currentDescriptor.contains(property) && (!currentDescriptor.isCloned(property)) && currentDescriptor.getEntry(property).isLocal()) {
                builder.appendRaw(currentDescriptor.getAttribute(instance.getObjectServer(), property), (String) instance.getObjectServer().getMapper()
                    .mapToTarget(new DbPropertyChange(instance, property)));
            } // else { might be mapped by inheritance chain descriptor or
            // delayed because of remote Foreign Key }
        }

        return builder;
    }

    /**
     * Return a generic Deletion Query for a given DbObject (for e.g. SQL DELETE). Aggregates and Composites must be treated separately, resp. Referential Integrity Conditions are assumed by
     * Target-Schema.
     *
     * @param object DbObject to be removed
     */
    public static final DbQueryBuilder createRemoveObject(DbObject /* DbChangeableBean */object) {
        DbQueryBuilder builder = object.getObjectServer().createQueryBuilder(DELETE, object.getClass().getName() + " by ID");
        builder.setTableList(object.getObjectServer().getConnection().getRootClassFor(object.getClass()));
        builder.addFilter(object.getObjectServer().getMapper().getTargetIdName(), object); // .setUniqueConstraints(object.getObjectServer().getDescriptor(object.getClass()),
        // object);
        return builder;
    }

    /**
     * Create a query to add a history entry of the given removable object.
     *
     * @param object
     * @return
     */
    public static final DbQueryBuilder createChangeRemoveHistory(DbObject /* DbChangeableBean */object) {
        DbQueryBuilder builder = object.getObjectServer().createQueryBuilder(DbQueryBuilder.INSERT, "Historize removed object");
        builder.setTableList(object.getObjectServer().getMapper().getTargetRemoveHistoryName());
        // TODO DbMapper.ATTRIBUTE_MAP_OWNER_TYPE
        builder.append(/* DbMapper.ATTRIBUTE_MAP_OWNER_TYPE */"T_TypeOwner", object.getObjectServer().getTargetName(object.getClass()));
        builder.append(DbMapper.ATTRIBUTE_MAP_VALUE_ID, object.getId());
        builder.appendInternal((String) null);
        return builder;
    }

    /**
     * Return the DbObjectServer this query is meant for.
     */
    public final DbObjectServer getObjectServer() {
        return objectServer;
    }

    /**
     * Return the built Query abstracted by this Builder, null if Query type is not supported.
     */
    public abstract String getQuery();

    /**
     * Return description of Query.
     */
    protected final String getQueryDescription() {
        switch (type) {
            case INSERT: {
                return "INSERT=> " + useCase;
            }
            case UPDATE: {
                return "UPDATE => " + useCase;
            }
            case DELETE: {
                return "DELETE => " + useCase;
            }
            case SELECT: {
                return "SELECT => " + useCase;
            }
            case CREATE: {
                return "CREATE => " + useCase;
            }
            case DROP: {
                return "DROP => " + useCase;
            }
            case ALTER: {
                return "ALTER => " + useCase;
            }
            case LOCK: {
                return "LOCK => " + useCase;
            }
            case UNLOCK: {
                return "UNLOCK => " + useCase;
            }
            case RAW: {
                return "RAW => " + useCase;
            }
            default:
                // TODO
                throw new DeveloperException("NYI");
        }
    }

    /**
     * Create a String comparison for a certain Attribute with a given String-value. Wildcards such as <b>'*'</b> or <b>'?'</b> are possible in String-value.
     * <p>
     * Example: - getStringClause("MyTable.myAttribute", "H?l*") => SQL-String: "MyTable.myAttribute LIKE 'H_l%" - getStringClause("attr", "H") => SQL-String: "attr LIKE 'H%'"
     *
     * @param attribute Attribute of type String
     * @param value String-Value of attribute
     */
    public abstract String getStringClause(final String attribute, final String value, final int type);

    /**
     * Return the built Query abstracted by this Builder.
     */
    protected final int getType() {
        return type;
    }

    /**
     * Return whether this query is dangerous, for e.g. multi-Update etc.
     * <p>
     * This is mainly a Data-Safety-Method as an Emergency-Break for Developers in case the seDb-Framework has a failure (not recognized in Test-Phase).
     *
     * @return boolean true->the query should be suppressed instead of execution against Target-System
     * @see DbQuery#execute()
     */
    public abstract boolean isDangerous();

    /**
     * Return whether this query abstracts a DELETE-Manipulation.
     *
     * @return boolean true->DELETE (SQL DML)
     */
    protected final boolean isDelete() {
        return type == DELETE;
    }

    /**
     * Return whether this builder encapsulate a Data-Selection query.
     *
     * @return boolean true (for e.g. a SQL DQL SELECT)
     */
    public boolean isSelection() {
        return type == SELECT;
    }

    /**
     * Return whether this Query is worth to be executed against DBMS. For e.g. a generic Update in an inheritance Chain may not influence the whole chain. Case: Child extends Parent (and only one of
     * them was changed during an Update)
     */
    public final boolean isSuppressable() {
        return suppressQuery;
    }

    /**
     * Return whether this query abstracts an UPDATE-Manipulation.
     *
     * @return boolean true->UPDATE (SQL DML)
     */
    public final boolean isUpdate() {
        return type == UPDATE;
    }

    /**
     * @see #setAttributeList(String, boolean)
     */
    public final void setAttributeList(final String attributeList) {
        setAttributeList(attributeList, false);
    }

    /**
     * Sets a list of attributes (for e.g. in a SELECT-statement).
     *
     * @param attributeList For e.g. "ATTR1, ATTR2"
     * @param distinct true=>Suppress multiple similar results
     */
    public abstract void setAttributeList(final String attributeList, boolean distinct);

    /**
     * Create a NATURAL JOIN of type: SELECT * FROM A, B WHERE condition where condition might be one of the equivalent forms: 1) A JOIN B ON A.FKID = B.FKID (produces 2 columns for FKID) 2) A JOIN B
     * USING(FKID) (produces 1 column for FKID) 3) A NATURAL JOIN B (produces 1 column for FKID)
     * <p>
     * This method uses the Variant 1).
     * <p>
     * Typically used to Join Addresses for a Person.
     */
    public abstract void setNaturalJoin(final String leftTable, final String rightTable, final String leftKeySuffix, final String rightKeySuffix);

    /**
     * Set a well formated Query for Target-System. The statement will not be transformed any further.
     */
    public abstract void setRaw(final String statement);

    /**
     * Set the the Target-System Table of the given DbObject-type.
     */
    public final void setTableList(Class<? extends DbObject> dbObject) {
        String targetName = getObjectServer().getTargetName(dbObject);
        setTableList(tagTargetExpression(targetName));
    }

    /**
     * Set the list of DB-Tables where Query resolves to.
     *
     * @param tableList (for e.g. "A, B")
     */
    public abstract void setTableList(final String tableList);

    /**
     * Return current tablelist.
     *
     * @return
     */
    public abstract String getTableList();

    /**
     * Optionally define desired default locale for DbNlsString-retrievings.
     *
     * @param locale
     * @see #getDefaultLocale()
     */
    public final void setDefaultLocale(Locale locale) {
        defaultLocale = locale;
    }

    /**
     * @see #setDefaultLocale(Locale)
     */
    public final Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Return a query filter using all unique constraints but T_Id.
     */
    protected final void setUniqueConstraints(DbDescriptor descriptor, DbObject object) {
        try {
            java.util.Iterator<String> unique = descriptor.iteratorUniqueProperties();

            while (unique.hasNext()) {
                String keyProperty = unique.next();
                DbPropertyChange change = new DbPropertyChange(object, keyProperty);
                Object value = change.getValue();
                Long key = null;
                if (change.getGetterReturnType().equals(Long.class)) {
                    key = (Long) value;
                } else if (value instanceof DbObject) {
                    key = ((DbObject) value).getId();
                } else {
                    // TODO NYI
                    throw new DeveloperException("NYI: key-type => " + value);
                }
                addFilter(descriptor.getAttribute(object.getObjectServer(), keyProperty), key);
            }
        } catch (Exception e) {
            throw new DeveloperException("Constraint error", null, e);
        }
    }

    /**
     * For e.g. if Fields/Tablenames with whitespaces must be put in brackets [] as MS Access or MS SQL Server needs this.
     */
    protected String tagTargetExpression(final String value) {
        return value;
    }

    /**
     * Define number of objects to be returned by a certain SELECT-Builder. Default: FETCHBLOCK_UNLIMITED
     *
     * @param size FETCHBLOCK_UNLIMITED or >0
     */
    public final void setFetchBlockSize(final int size) {
        if (size < FETCHBLOCK_UNLIMITED) {
            throw new IllegalArgumentException("Fetchblock size must >0 for limited range or FETCHBLOCK_UNLIMITED");
        }
        this.fetchBlockSize = size;
    }

    public final int getFetchBlockSize() {
        return fetchBlockSize;
    }

    /**
     * Return all DbNlsString's for field nlsText of all codes given by dbCode. Used for caching.
     *
     * @param dbCode DbEnumeration type
     * see DbObjectServer#cacheNlsStrings(..)
     */
    public abstract DbQueryBuilder toCodeNlsBuilder(Class<DbCodeType> dbCode, Locale locale);

    /**
     * Convert the query to a COUNT-Query. Usually almost the same statement may be reused to get the effective size of Objects really referenced by a Query.
     *
     * @param dbObject target type to query for
     * @return count or -1 if not determinable
     */
    public abstract int getCount(java.lang.Class<? extends DbObject> dbObject);
}
