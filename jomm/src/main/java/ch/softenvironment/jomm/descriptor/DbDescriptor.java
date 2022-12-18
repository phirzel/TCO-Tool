package ch.softenvironment.jomm.descriptor;

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
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;
import ch.softenvironment.util.BeanReflector;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Each DbObject should have a Description of its persistence behaviour. That is why each DbObject type (and therefore all Instances of it) will be added such a Descriptor. The Descriptor will be used
 * by DbMapper for mapping between the Database Schema and the Object-Model and vice versa.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DbDescriptor {

	public static final int IRRELEVANT = 0; // in DbSessionBeans for e.g.
	public static final int ASSOCIATION = 1;
	public static final int AGGREGATION = 2;
	public static final int COMPOSITION = 3;

	public static final int USER_ID_LENGTH = 30;

	private transient Class<? extends DbObject> mappedClass = null;
	private transient boolean ordered = false;

	// there is always an ID
	private final transient Map<String, DbDescriptorEntry> flatPropertyMap = new HashMap<>();
	// DbNlsString aggregations
	private transient Map<String, DbDescriptorEntry> nlsStringMap = null;
	// 1:1 aggregates
	private transient Map<String, DbDescriptorEntry> aggregateMap = null;
	// 1:n aggregates
	private transient Map<String, DbDescriptorEntry> oneToManyMap = null;
	// n:n aggregates
	private transient Map<String, DbDescriptorEntry> attributedMap = null;
	// UNIQUE-Key Properties
	private transient java.util.Set<String> uniqueSet = null;

	/**
	 * Construct a DbDescriptor with default ObjectID and Lock/History Attributes.
	 */
	public DbDescriptor(Class<? extends DbObject> dbObject) {
		this(dbObject, true);
	}

	/**
	 * Construct a DbDescriptor with unique ObjectID, and optional Lock/History Attributes.
	 */
	public DbDescriptor(Class<? extends DbObject> dbObject, boolean timestampMapping) {
		super();

		mappedClass = dbObject;

		add(DbObject.PROPERTY_ID, null /*
		 * targetIdName will be determined at
		 * runtime @see #getAttribute()
		 */, new DbIdFieldDescriptor(), new DbMultiplicityRange(0, 1/*
		 * 1
		 * =
		 * >
		 * will
		 * be
		 * set
		 * by
		 * save
		 * (
		 * )
		 */));

		if (timestampMapping) {
			add(DbObject.PROPERTY_CREATE_DATE, DbMapper.ATTRIBUTE_CREATE_DATE, new DbDateFieldDescriptor(DbDateFieldDescriptor.DATETIME),
				new DbMultiplicityRange(1));
			add(DbObject.PROPERTY_LAST_CHANGE, DbMapper.ATTRIBUTE_LAST_CHANGE, new DbDateFieldDescriptor(DbDateFieldDescriptor.DATETIME),
				new DbMultiplicityRange(1));
			add(DbObject.PROPERTY_USER_ID, DbMapper.ATTRIBUTE_USER_ID, new DbTextFieldDescriptor(USER_ID_LENGTH), new DbMultiplicityRange(1));
		}
	}

	/**
	 * Map a Property of DbObject to a corresponding Database Attribute.
	 *
	 * @param propertyName Property of appropriate DbObject
	 * @param attributeName Attribute of appropriate Table
	 * @param range Multiplicity [0..1]
	 */
	public DbDescriptorEntry add(final String propertyName, final String attributeName, DbFieldTypeDescriptor fieldType, DbMultiplicityRange range) {
		if (range.getUpper() > 1) {
			throw new ch.softenvironment.util.DeveloperException(getMappedClass() + "#add(<" + propertyName + ">) => use #add(.., mapName) for ranges > 1");
		}
		if (fieldType == null) {
			throw new ch.softenvironment.util.DeveloperException(getMappedClass() + "#add(<" + propertyName + ">) => fieldType missing!");
		}

		DbDescriptorEntry entry = new DbDescriptorEntry(attributeName, range, fieldType);
		flatPropertyMap.put(checkNames(propertyName, attributeName), entry);
		return entry;
	}

	/**
	 * This is for plain Attributes with Cardinality with upper range > 1. The Attributes are kept in java.util.List.
	 *
	 * @param propertyName Property of appropriate DbObject
	 * @param attributeName => see T_Map*.T_Attribute_Owner
	 * @param fieldType Type of aggregated Objects
	 * @param range Multiplicity [0..*]
	 * @param mapName Name of T_MAP_Table where 1:n Data is to be resolved
	 */
	public void add(final String propertyName, final String attributeName, DbFieldTypeDescriptor fieldType, DbMultiplicityRange range, final String mapName) {
		if (range.getUpper() <= 1) {
			log.warn("Developer warning: {} #add(<{}>.., range,..) ->check if additional map is really necessary", getMappedClass(), propertyName);
		}

		if (oneToManyMap == null) {
			oneToManyMap = new java.util.HashMap<>();
		}

		oneToManyMap.put(DbObject.convertName(propertyName), new DbDescriptorEntry(attributeName, range, fieldType, mapName));
	}

	/**
	 * Case: A:B=n:n from the view of A or B, where C is the (evtl. attributed) Association-Class (DbRelationshipBean) between A and B.
	 * <p>
	 * Therefore propertyName points to a List of C's!
	 * <p>
	 * If (A:B=n:n) via Map C => use this description on either side of A and B.
	 *
	 * @param associationType
	 * @param propertyName Property in A (or B) containing the opposite Elements via the map C
	 * @param rangeA Multiplicity on (this) side A
	 * @param rangeB Multiplicity on (other) side B
	 * @param dbRelationshipBean n:n Map C between A and B (where A:C=1:n and B:C=1:n)
	 * @param referenceProperty Name of Property this end A (or B) is referenced within DbRelationshipBean
	 * @see #checkType(int)
	 * @see #addAssociationEnd(Class, String, String)  for C
	 */
	public void addAssociationAttributed(final int associationType, final String propertyName, DbMultiplicityRange rangeA, DbMultiplicityRange rangeB,
		Class<? extends DbRelationshipBean> dbRelationshipBean, final String referenceProperty) {
		checkType(associationType);

		if (attributedMap == null) {
			attributedMap = new java.util.HashMap<>();
		}
		if (!BeanReflector.isInherited(dbRelationshipBean, DbRelationshipBean.class)) {
			throw new ch.softenvironment.util.DeveloperException("Bad type for dbRelationshipBean");
		}

		attributedMap.put(DbObject.convertName(propertyName), new DbDescriptorEntry(referenceProperty, rangeA, associationType, dbRelationshipBean, rangeB));
	}

	/**
	 * Case: A:B=n:n from the view of C, where C is the Association-Class (DbRelationshipBean) between A and B.
	 * <p>
	 * Two ends must be added in C once for A and once for B.
	 *
	 * @param dbEntityBean Represents the AssociationEnd A (or B)
	 * @param propertyRoleName RoleName of A (or B) in C
	 * @param attributeRoleName RoleName of A (or B) in C
	 * @see #addAssociationAttributed(int, String, DbMultiplicityRange, DbMultiplicityRange, Class, String)
	 */
	public void addAssociationEnd(Class<? extends DbEntityBean> dbEntityBean, final String propertyRoleName, final String attributeRoleName) {
		if (!BeanReflector.isInherited(dbEntityBean, DbEntityBean.class)) {
			throw new ch.softenvironment.util.DeveloperException("Bad type for dbEntityBean");
		}

		addUniqueProperty(propertyRoleName);
		DbDescriptorEntry entry = addManyToOneReferenceId(IRRELEVANT, propertyRoleName, attributeRoleName, new DbMultiplicityRange(1));
		entry.setBaseClass(dbEntityBean);
	}

	/**
	 * Case: A:Code=1:[0..1] from the view of A.
	 * <p>
	 * DbCodeType's will be cached automatically by DbObjectServer.
	 *
	 * @param propertyName Name of property of this Object
	 * @param attributeRoleName A.T_ID_codeRole (FK) <-- Code.T_ID (PK)
	 * @param range [0..1 || 1]
	 */
	public void addCode(final String propertyName, final String attributeRoleName, DbMultiplicityRange range) {
		if (range.getUpper() > 1) {
			throw new ch.softenvironment.util.DeveloperException(getMappedClass() + "#addCode(<" + propertyName
				+ ">) => use #addCode(.., ,dbCode, mapName) for ranges > 1");
		}
		addOneToOneReference(AGGREGATION, propertyName, attributeRoleName, range, true, true);
	}

	/**
	 * Case: A:Code=1:[0..n] from the view of A.
	 *
	 * @param propertyName Name of the java.util.List of B's and T_Attribute_Owner in T_MAP-Table
	 * @param attributeName => this is a fictive Attribute in this special case, will server as value for T_MAP_*.T_Attribute_Owner
	 * @param range [0..n]
	 * @param dbCode Aggregated DbEnumeration-Type
	 * @param mapName Name of T_Map_<code>-Table
	 */
	public void addCode(final String propertyName, final String attributeName, DbMultiplicityRange range, Class<? extends DbCode> dbCode, final String mapName) {
		if (oneToManyMap == null) {
			oneToManyMap = new java.util.HashMap<>();
		}

		oneToManyMap.put(DbObject.convertName(propertyName), new DbDescriptorEntry(attributeName, range, dbCode, mapName));
	}

	/**
	 * Case: A:B=[0..1]:n from the view of B.
	 * <p>
	 * It is assumed that B has a ForeignKey of A, therefore Cardinality is assumed exactly [1] from the view of B.
	 * <p>
	 * The A Object is cached.
	 *
	 * @param associationType
	 * @param propertyName Name of property of this Object
	 * @param attributeRoleName (ForeignKey B.T_Id_ARole of A.T_Id)
	 * @param range [0..1] as Aggregator or [1] if owned (by referential integrity)
	 * @see #checkType(int)
	 */
	public void addManyToOneReference(final int associationType, final String propertyName, final String attributeRoleName, DbMultiplicityRange range) {
		if (range.getLower() == 1) {
			// TODO NYI: range[1] only technically treated
		}
		addOneToOneReference(associationType, propertyName, attributeRoleName, new DbMultiplicityRange(0, 1) /*
		 * ignore
		 * -
		 * >
		 * technically
		 * treated
		 */, true /*
		 * in
		 * INTERLIS
		 * XML
		 * -
		 * Schema
		 * the
		 * many
		 * Ends
		 * always
		 * point
		 * to
		 * [
		 * 0.
		 * .1
		 * ]
		 * End
		 */, true /*
		 * since
		 * many
		 * share
		 * the
		 * same
		 * "owner"
		 * caching
		 * makes
		 * sense
		 */);
	}

	/**
	 * Case: A:B=1:n from the view of B.
	 * <p>
	 * Only the reference (via ID) is mapped for efficiency reasons. It is assumed that B has a ForeignKey of A, therefore Cardinality is assumed exactly [1].
	 *
	 * @param associationType
	 * @param propertyName Name of property in B containing the reference to A
	 * @param attributeRoleName Name of ForeignKey in B pointing to A's Primary key (B.<b>T_Id_ARole(FK)</b>=A.T_Id(PK))
	 * @see #checkType(int)
	 */
	public DbDescriptorEntry addManyToOneReferenceId(final int associationType, final String propertyName, final String attributeRoleName, DbMultiplicityRange range) {
		checkType(associationType);

		if (range.getUpper() > 1) {
			// TODO NYI: for upper-range > 1
			return null;
		}

		if (!range.isNullAllowed()) {
			// HACK suppress Mandatory-Test 'cause new Object-References can be
			// determined only at #save(), when parent aggregate is saved yet!
			return add(propertyName, attributeRoleName, new DbIdFieldDescriptor(), new DbMultiplicityRange(0, range.getUpper()));
		}

		return add(propertyName, attributeRoleName, new DbIdFieldDescriptor(), range);
	}

	/**
	 * Case: A:DbNlsString=1:[0..1] from the view of A.
	 * <p>
	 * A is assumed to have a ForeignKey-ID of the Table NLS_TABLE.
	 *
	 * @param propertyName
	 * @param attributeRoleName A.T_ID_NlsName (FK) <-- NLS_TABLE.T_ID (PK)
	 * @param range
	 */
	public void addNlsString(final String propertyName, final String attributeRoleName, DbMultiplicityRange range) {
		if (nlsStringMap == null) {
			nlsStringMap = new java.util.HashMap<>();
		}

		nlsStringMap.put(checkNames(propertyName, attributeRoleName), new DbDescriptorEntry(attributeRoleName, range));
	}

	/**
	 * Case: A:B=1:[0..n] from the view of A.
	 * <p>
	 * All B's are assumed to have a ForeignKey of A: A.T_ID (PK) --> B.T_ID_Role (FK).
	 *
	 * @param associationType
	 * @param propertyName Name of the java.util.List of B's.
	 * @param otherPropertyName Property in B pointing to A
	 * @param range of B's
	 * @param aggregationBaseClass BaseClass of B's
	 * @param ordered false (B's are not ordered); true (B's are ordered according to B.T_Seq_role)
	 * @see #checkType(int)
	 */
	public void addOneToMany(final int associationType, final String propertyName, final String otherPropertyName, DbMultiplicityRange range,
		java.lang.Class<? extends DbObject> aggregationBaseClass, boolean ordered) {
		checkType(associationType);

		if (oneToManyMap == null) {
			oneToManyMap = new java.util.HashMap<>();
		}
		if (range.getUpper() == 1) {
			log.warn("Developer warning: {} #addOneToMany({})->use #addOneToOne(..) eventually", getMappedClass(), propertyName);
		}
		oneToManyMap.put(DbObject.convertName(propertyName), new DbDescriptorEntry(DbObject.convertName(otherPropertyName), range, associationType,
			aggregationBaseClass, ordered));
	}

	/**
	 * @see #addOneToOne(int, String, String, DbMultiplicityRange, boolean)
	 * @deprecated
	 */
	public void addOneToOne(final int associationType, final String propertyName, final String otherPropertyName, DbMultiplicityRange range) {
		addOneToOne(associationType, propertyName, otherPropertyName, range, false);
	}

	/**
	 * @see #addOneToOne(int, String, String, DbMultiplicityRange, boolean, boolean)
	 */
	public void addOneToOne(final int associationType, final String propertyName, final String otherPropertyName, DbMultiplicityRange range, boolean keepReferenceToOtherEnd) {
		addOneToOne(associationType, propertyName, otherPropertyName, range, keepReferenceToOtherEnd, false);
	}

	/**
	 * Case: A:B=1:[0..1] <b>from the view of A</b>.
	 * <p>
	 * A does not have a ForeignKey Reference of B, but B gets a ForeignKey of A with the given attributeRoleName (similar in 1:n): A.T_Id (PK) => B.T_Id_&lg;roleOfA&gt; (FK)
	 * <p>
	 * An instance of B will be mapped in A when A is instantiated.
	 *
	 * @param associationType
	 * @param propertyName in A pointing to an instance of B
	 * @param otherPropertyName Property in B pointing to A
	 * @param range of AssociationEnd B
	 * @param keepReferenceToOtherEnd in case one AssociationEnd should point to other end only (for e.g. in INTERLIS XML Schema)
	 * @param cached
	 * @see #checkType(int)
	 * @see #addOneToOneReference(int, String, String, DbMultiplicityRange, boolean)  for the view of B
	 */
	public void addOneToOne(final int associationType, final String propertyName, final String otherPropertyName, DbMultiplicityRange range,
		boolean keepReferenceToOtherEnd, boolean cached) {
		checkType(associationType);

		if (aggregateMap == null) {
			aggregateMap = new java.util.HashMap<>();
		}
		if (range.getUpper() > 1) {
			throw new ch.softenvironment.util.DeveloperException(getMappedClass() + "#addOneToOne(<" + propertyName + ">) => use #addOneToMany(..) instead");
		}
		aggregateMap.put(DbObject.convertName(propertyName), new DbDescriptorEntry(DbObject.convertName(otherPropertyName), range, associationType, cached));

		// TODO NYI: keepReferenceToOtherEnd
	}

	/**
	 * Case: A:B=1:[0..1] <b>from the view of B</b>.
	 * <p>
	 * A gets a ForeignKey Reference of B A.T_Id_&lg;roleOfB&gt; (FK) <-- B.T_Id (PK)
	 * <p>
	 * In case if A aggregates B and the physical Foreign-key will still be defined in A.
	 *
	 * @param associationType
	 * @param propertyName in B pointing to a reference of A
	 * @param otherBaseClass BaseClass of A containing the Reference to B
	 * @param otherPropertyName Property in A pointing to B (roleOfB)
	 * @param range of AssociationEnd A
	 * @param keepReferenceToOtherEnd in case one AssociationEnd should point to other end only (for e.g. in INTERLIS XML Schema)
	 * @param cached
	 * @see #checkType(int)
	 * @see #addOneToOneReference(int, String, String, DbMultiplicityRange, boolean)  for the view of A
	 */
	public void addOneToOneId(final int associationType, final String propertyName, Class<? extends DbObject> otherBaseClass, final String otherPropertyName, DbMultiplicityRange range,
		boolean keepReferenceToOtherEnd, boolean cached) {
		// TODO NYI
		throw new ch.softenvironment.util.DeveloperException("nyi");
	}

	/**
	 * @see #addOneToOneReference(int, String, String, DbMultiplicityRange, boolean, boolean)
	 */
	public void addOneToOneReference(final int associationType, final String propertyName, final String attributeRoleName, DbMultiplicityRange range,
		boolean keepReferenceToOtherEnd) {
		addOneToOneReference(associationType, propertyName, attributeRoleName, range, keepReferenceToOtherEnd, false);
	}

	/**
	 * Case: A:B=1:[0..1] <b>from the view of B</b>.
	 * <p>
	 * The whole Object of type A is instantiated in B.
	 *
	 * @param associationType
	 * @param propertyName
	 * @param attributeRoleName of B.T_Id_RoleOfA (FK) <-- A.T_Id (PK)
	 * @param range
	 * @param keepReferenceToOtherEnd in case one AssociationEnd should point to other end only (for e.g. in INTERLIS XML Schema)
	 * @param cached (DbObjectServer will cache such Objects for performance reasons)
	 * @see #checkType(int)
	 * @see #addOneToOne(int, String, String, DbMultiplicityRange, boolean, boolean)  for the view of A
	 * @see #addOneToOneReferenceId(int, String, String, DbMultiplicityRange, boolean)  to map Id of A only
	 */
	public void addOneToOneReference(final int associationType, final String propertyName, final String attributeRoleName, DbMultiplicityRange range,
		boolean keepReferenceToOtherEnd, boolean cached) {
		checkType(associationType);

		if (aggregateMap == null) {
			aggregateMap = new java.util.HashMap<>();
		}
		if (range.getUpper() > 1) {
			throw new ch.softenvironment.util.DeveloperException(getMappedClass() + "#addOneToOneReference(<" + propertyName
				+ ">) => use #addOneToMany(..) instead!");
		}
		aggregateMap.put(checkNames(propertyName, attributeRoleName), new DbDescriptorEntry(attributeRoleName, range, new DbIdFieldDescriptor(),
			associationType, cached));

		// TODO NYI: keepReferenceToOtherEnd
	}

	/**
	 * @see #addOneToOneReferenceId(int, String, String, DbMultiplicityRange, boolean)
	 */
	@Deprecated
	public void addOneToOneReferenceId(final int type, final String propertyName, final String attributeRoleName, DbMultiplicityRange range) {
		addOneToOneReferenceId(type, propertyName, attributeRoleName, range, false);
	}

	/**
	 * Case: A:B=1:[0..1] <b>from the view of B</b>.
	 * <p>
	 * Add a Reference to B, where B is assumed to have a ForeignKey of Table A.
	 * <p>
	 * Only the Id of A (type java.lang.Long) is aggregated (instead of the whole instance of type A) for more efficiency (may enhance performance).
	 *
	 * @param type
	 * @param propertyName Name of B pointing to A
	 * @param attributeRoleName Foreign Key of B pointing to A's Primary Key (B.<b>T_Id_RoleOfA</b>(FK)==A.T_Id (PK))
	 * @param range of role A
	 * @param keepReferenceToOtherEnd in case one AssociationEnd should point to other end only (for e.g. in INTERLIS XML Schema)
	 * @see #checkType(int)
	 * @see #addOneToOne  for the view of A
	 * @see #addOneToOneReference(int, String, String, DbMultiplicityRange, boolean, boolean) ce(..) to map whole instance of A
	 */
	public void addOneToOneReferenceId(final int type, final String propertyName, final String attributeRoleName, DbMultiplicityRange range, boolean keepReferenceToOtherEnd) {
		add(propertyName, attributeRoleName, new DbIdFieldDescriptor(), range);
		// TODO NYI keepReferenceToOtherEnd
	}

	/**
	 * Add a Key to the UNIQUE Key of the described Object. Each Object has ONE set of all Key-Properties ONLY.
	 * <p>
	 * For DbEntityBeans an "Id"-Key is assumed by default.
	 * <p>
	 * Define own uniqueness especially for DbRelationshipBeans.
	 */
	public void addUniqueProperty(final String keyProperty) {
		if (uniqueSet == null) {
			uniqueSet = new java.util.HashSet<>();
		}
		uniqueSet.add(checkNames(keyProperty, null));
	}

	/**
	 * Verify naming of target attributes. Some Databases do not like names refering to types and proprietary Functions.
	 *
	 * @return property Name according to Bean-Specification
	 */
	private static String checkNames(final String property, final String attributeName) {
		if (attributeName != null) {
			if ((attributeName.startsWith("_")) || (attributeName.equalsIgnoreCase("IDENTITY") /*
			 * MS
			 * SQL
			 * Server
			 */) || (attributeName.equalsIgnoreCase("VALUE") /*
			 * MS
			 * Access
			 */)
				|| (attributeName.equalsIgnoreCase("SCHEMA") /* MySQL */) || (attributeName.equalsIgnoreCase("USER"))
				|| (attributeName.equalsIgnoreCase("DATE")) || (attributeName.equalsIgnoreCase("TIME")) || (attributeName.equalsIgnoreCase("DATETIME"))
				|| (attributeName.equalsIgnoreCase("TIMESTAMP")) || (attributeName.equalsIgnoreCase("LEVEL")) || (attributeName.equalsIgnoreCase("FROM"))
				|| (attributeName.equalsIgnoreCase("ORDER")) || (attributeName.equalsIgnoreCase("BY")) || (attributeName.equalsIgnoreCase("TO"))
				|| (attributeName.equalsIgnoreCase("XMIN" /* PostgreSQL */)) || (attributeName.equalsIgnoreCase("XMAX" /* PostgreSQL */))) {
				log.warn("Developer warning: BAD Attribute name <{}> may confuse SQL-API of your DBMS", attributeName);
			}
		}

		return DbObject.convertName(property);
	}

	/**
	 * Verify correct Association-Type.
	 *
	 * @param associationType ASSOCIATION, AGGREGATION or COMPOSITION
	 */
	private static void checkType(final int associationType) {
		if ((associationType == IRRELEVANT) || (associationType == ASSOCIATION) || (associationType == AGGREGATION) || (associationType == COMPOSITION)) {
			// ok
		} else {
			throw new ch.softenvironment.util.DeveloperException("Unexpected type", null);
		}
	}

	/**
	 * @see #cloneEntry(Class, String, String)
	 */
	public void cloneEntry(Class<? extends DbObject> dbObject, final String propertyName) {
		cloneEntry(dbObject, propertyName, null);
	}

	/**
	 * Reuse a Mapping done by another DbObject. A cloned entry is assumed like an ALIAS, therefore it cannot be mapped at saving.
	 * <p>
	 * Set propertyOwnerId to clone a DbCodeType[0..*] association organized by T_MAP_
	 * <code> by given (@see DbDescriptor#addCode(String, String, DbMultiplicityRange, Class, String)).
	 * <p>
	 * For e.g. a Project:CostCentre=n:n class Project extends DbChangeableCode { private java.util.List fieldCostCentre=new java.util.ArrayList(); // get/setCostCentre() public static DbDescriptor
	 * createDescriptor() { .. descriptor.addCode("costCentre", "costCentre", new DbMultiplicityRange(1,DbMultiplicityRange.UNBOUND), CostCentre.class, "T_MAP_CostCentre"); } } class ProjectSB extends
	 * DbSessionBean { private java.util.List fieldCostCentre=new java.util.ArrayList(); // getCostCentre() public static DbDescriptor createDescriptor() { .. descriptor.cloneEntry(Project.class,
	 * "costCentre", "id"); // where ProjectSB#id equals to Project#id here and points to T_MAP_CostCentre#T_Id_Owner } }
	 *
	 * @param dbObject OriginalClass mapping the real Description
	 * @param propertyName Property of appropriate DbObject in OriginalClass
	 * @param propertyOwnerId Reference-Property corresponding to "T_Id_Owner" in a T_MAP_<code>
	 * @see DbSessionBean
	 */
	public void cloneEntry(Class<? extends DbObject> dbObject, final String propertyName, final String propertyOwnerId) {
		String prop = checkNames(propertyName, null);
		try {
			DbDescriptor descriptor = getDescriptor(dbObject);

			DbDescriptorEntry entry = (DbDescriptorEntry) descriptor.getEntry(prop).clone();
			entry.setCloned(dbObject, propertyOwnerId);

			if (descriptor.flatPropertyMap.containsKey(prop)) {
				flatPropertyMap.put(prop, entry);
			} else if ((descriptor.aggregateMap != null) && (descriptor.aggregateMap.containsKey(prop))) {
				if (aggregateMap == null) {
					aggregateMap = new java.util.HashMap<String, DbDescriptorEntry>();
				}
				aggregateMap.put(prop, entry);
			} else if ((descriptor.nlsStringMap != null) && (descriptor.nlsStringMap.containsKey(prop))) {
				if (nlsStringMap == null) {
					nlsStringMap = new java.util.HashMap<String, DbDescriptorEntry>();
				}
				nlsStringMap.put(prop, entry);
			} else if ((descriptor.oneToManyMap != null) && (descriptor.oneToManyMap.containsKey(prop))) {
				if (oneToManyMap == null) {
					oneToManyMap = new java.util.HashMap<String, DbDescriptorEntry>();
				}
				oneToManyMap.put(prop, entry);
			} else {
				throw new ch.softenvironment.util.DeveloperException(dbObject + "->" + getMappedClass() + "#cloneEntry(<" + propertyName
					+ ">) => Property not defined in <" + dbObject.getName() + "#createDescriptor()>");
			}
		} catch (Throwable e) {
			throw new ch.softenvironment.util.DeveloperException(dbObject + "->" + getMappedClass() + "#cloneEntry(<" + propertyName
				+ ">) => createDescriptor() does not map Property: " + e.getLocalizedMessage(), null, e);
		}
	}

	/**
	 * Return a Database Attribute for a certain Object-Property. Parent Properties in case of Inheritance are not considered here.
	 */
	public boolean contains(final String property) {
		String prop = checkNames(property, null);
		if (flatPropertyMap.containsKey(prop)) {
			return true;
		}
		if ((nlsStringMap != null) && (nlsStringMap.containsKey(prop))) {
			return true;
		}
		// includes aggregateParentMap
		return (aggregateMap != null) && (aggregateMap.containsKey(prop));

		// oneToMany is not changed at Parent directly, therefore not really
		// contained
	}

	/**
	 * Return the corresponding property target Attribute name for a certain Object-Property. Any Parent-Descriptors in case of Inheritance are not considered.
	 */
	public String getAttribute(DbObjectServer server, final String property) {
		String prop = checkNames(property, null);

		DbDescriptorEntry entry = getEntry(prop);
		if (entry != null) {
			// oneToMany makes no sense!!!
			if (prop.equals(DbObject.PROPERTY_ID)) {
				return server.getMapper().getTargetIdName();
			} else {
				return entry.getAttributeName();
			}
		}

		throw new ch.softenvironment.util.DeveloperException(getMappedClass() + "#getAttribute(<" + prop + ">) => mapping-error with property");
	}

	/**
	 * Return Descriptor of given dbObject.
	 */
	private DbDescriptor getDescriptor(Class<? extends DbObject> dbObject) throws NoSuchMethodException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
		Class[] parameterTypes = {};
		Object[] parameters = {};
		java.lang.reflect.Method method = dbObject.getMethod("createDescriptor", parameterTypes);

		return (DbDescriptor) ((method).invoke(dbObject, parameters));
	}

	/**
	 * Return Description of an aggregated Model <-> Database mapping.
	 */
	public DbDescriptorEntry getEntry(final String property) {
		DbDescriptorEntry entry = null;

		// for efficiency reasons try the most expected case first
		entry = flatPropertyMap.get(property);
		if (entry != null) {
			return entry;
		}
		if (aggregateMap != null) {
			entry = aggregateMap.get(property);
			if (entry != null) {
				return entry;
			}
		}
		if (nlsStringMap != null) {
			entry = nlsStringMap.get(property);
			if (entry != null) {
				return entry;
			}
		}
		if (oneToManyMap != null) {
			entry = oneToManyMap.get(property);
			if (entry != null) {
				return entry;
			}
		}
		if (attributedMap != null) {
			entry = attributedMap.get(property);
		}

		return entry;
	}

	/**
	 * @return Class This descriptors appropriate Class of type DbObject.
	 */
	public Class<? extends DbObject> getMappedClass() {
		return mappedClass;
	}

	/**
	 * Return whether aggregate with given Property has to be cached. Interesting for 1:n mappings only.
	 */
	public boolean isCached(final String property) {
		if (aggregateMap != null) {
			DbDescriptorEntry entry = aggregateMap.get(property);
			return entry.isCached();
		} else {
			return false;
		}
	}

	/**
	 * Return whether Entry for Property is cloned => therefore not saveable in described Object.
	 */
	public boolean isCloned(final String propertyName) {
		return getEntry(propertyName).getCloned() != null;
	}

	/**
	 * @see #setOrdered(boolean)
	 */
	public boolean isOrdered() {
		return ordered;
	}

	/**
	 * @return Iterator
	 */
	public java.util.Iterator<String> iteratorAggregatedProperties() {
		if (aggregateMap == null) {
			return null;
		} else {
			return aggregateMap.keySet().iterator();
		}
	}

	/**
	 * Return n:n properties (resulting in T_MAP_*).
	 *
	 * @return Iterator
	 */
	public java.util.Iterator<String> iteratorAttributedProperties() {
		if (attributedMap == null) {
			return null;
		} else {
			return attributedMap.keySet().iterator();
		}
	}

	/**
	 * Return an Iterator containing all flat properties (like T_Id, T_User, etc) for a DbObject.
	 *
	 * @return Iterator
	 */
	public java.util.Iterator<String> iteratorFlatProperties() {
		return flatPropertyMap.keySet().iterator();
	}

	/**
	 * Return all multi language properties (0:0..1).
	 *
	 * @return Iterator to all DbNlsString properties.
	 */
	public java.util.Iterator<String> iteratorNlsStringProperties() {
		if (nlsStringMap == null) {
			return null;
		} else {
			return nlsStringMap.keySet().iterator();
		}
	}

	/**
	 * Return all List properties.
	 *
	 * @return HashMap All flat properties
	 */
	public java.util.Iterator<String> iteratorOneToManyProperties() {
		if (oneToManyMap == null) {
			return null;
		} else {
			return oneToManyMap.keySet().iterator();
		}
	}

	/**
	 * Return an Iterator over ALL persistent Properties.
	 */
	public java.util.Iterator<String> iteratorProperties() {
		java.util.Set<String> properties = new java.util.HashSet<>();

		properties.addAll(flatPropertyMap.keySet());
		if (nlsStringMap != null) {
			properties.addAll(nlsStringMap.keySet());
		}
		if (aggregateMap != null) {
			properties.addAll(aggregateMap.keySet());
		}
		// TODO NYI: not yet DECIDED whether oneToMany makes Sense
		// properties.addAll(oneToMany.keySet());

		return properties.iterator();
	}

	/**
	 * @return Iterator
	 */
	public java.util.Iterator<String> iteratorUniqueProperties() {
		if (uniqueSet == null) {
			uniqueSet = new java.util.HashSet<>(1);
			uniqueSet.add(DbObject.PROPERTY_ID);
		}
		return uniqueSet.iterator();
	}

	/**
	 * If set to true an Attribute <b>T_Sequence</b> (UNIQUE) is assumed in table and in case of a SELECT the ResultSet will be ordered according to this Sequence.
	 * <p>
	 * This is helpful for DbEnumeration's which are ordered uniquely within a Schema, for e.g. Days of the Week = { Monday, Tuesday, .. }.
	 *
	 * @param ordered (true->ORDER BY T_Sequence)
	 */
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}
}
