package ch.softenvironment.jomm.target.sql;

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
import ch.softenvironment.jomm.DbObjectId;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQuery;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.DbTransaction;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbFieldType;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.UserException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Map <b>Entities from SQL-Target-DBMS</b> to <b>Java Objects</b> and vice versa. The <b>different types are mapped from one world to the other</b>. Also the technical Fields like T_* are considered
 * while mapping. by this utility, for e.g. an SQL CHAR to java.lang.String.
 * <p>
 * This is meant for <b>Standard SQL92 (99, 2003)</b> mappings.
 * <p>
 * Design Pattern: <b>Adapter</b>
 *
 * @author Peter Hirzel
 */
@Slf4j
public class SqlMapper implements DbMapper {

	protected static final String DATE_SQL_FORMAT = "yyyy-MM-dd";
	protected static final String TIMESTAMP_SQL_FORMAT = DATE_SQL_FORMAT + " "
		+ NlsUtils.TIME_24HOURS_PATTERN;
	// Transforming Database -> Model
	// private transient Class dbObject = null;
	// private transient DbObject instance = null;
	// private transient DbDescriptor currentDescriptor;
	// private DbQueryBuilder builder = null;
	// private transient ResultSet resultSet = null;

	// Technical stuff
	private final static String DB_VALUE_TRUE = "T";
	private final static String DB_VALUE_FALSE = "F";

	/**
	 * DbMapper constructor for: <b>OO-Model <==> Database Schema</b>. Maps values of given DbObject to a SELECT/INSERT/UPDATE/DELETE SQL-Query.
	 */
	protected SqlMapper() {
		super();
	}

	/**
	 * Usually each Target-System uses its own Error-Codes thrown as an Exception in case of illegal interaction. Therefore a User might be interested in a more understandable (say less technical)
	 * verbose explanation of the exception.
	 *
	 * @return String User-like error-explanation
	 */
	@Override
	public java.lang.String describeTargetException(
		java.lang.Exception exception) {
		return exception.getLocalizedMessage();
	}

	/**
	 * Get a new technical ID (T_Id) from a defined own technical table named
	 * <b>T_Key_Object</b>. For any persistent root class must be an entry
	 * within this key-table. Anytime the a new key-value is requested an incremental counter is maintained.
	 * <p>
	 * Note that this is just a pragmatic Workaround in case the Target-DBMS does not provide a better mechanism such as <b>Sequences</b> or <b>SELECT FOR UPDATE</b>.
	 */
	@Override
	public synchronized Long getNewId(
		javax.jdo.PersistenceManager objectServer,
		javax.jdo.Transaction transaction, final String key) {
		// TODO synch actually for equal key only; ROWLOCK
		String keyTable = DbMapper.ATTRIBUTE_KEY_TABLE + "_Object"; // T_Key_Object
		String keyAttribute = DbMapper.ATTRIBUTE_KEY_TABLE;
		String lastId = "T_LastUniqueId";
		long id = -1;

		// 1) Determine latest ID
		DbQueryBuilder builder = ((DbObjectServer) objectServer)
			.createQueryBuilder(DbQueryBuilder.SELECT, "Create UNIQUE ID");
		builder.setAttributeList(lastId);
		builder.setTableList(keyTable);
		builder.addFilter(keyAttribute, key, DbQueryBuilder.STRICT);
		DbQuery query = new DbQuery((DbTransaction) transaction, builder);
		ResultSet queryResult = (ResultSet) query.execute();
		if (!((DbObjectServer) objectServer).getMapper().hasNext(queryResult)) {
			query.closeAll();
			// TODO NLSs
			throw new UserException(
				"Es kann keine Identitaet fuer ein neues Objekt <" + key
					+ "> geloest werden.", "Allozierungs-Fehler");
		}

		// 2) Create Sequence ID
		try {
			id = (queryResult.getLong(1)) + (long) 1;
			query.closeAll();
		} catch (SQLException e) {
			// TODO NLS
			throw new UserException(
				"Es kann keine Identitaet fuer ein neues Objekt <" + key
					+ "> geloest werden.", "Allozierungs-Fehler");
		}
		builder = ((DbObjectServer) objectServer).createQueryBuilder(
			DbQueryBuilder.UPDATE, "SEQUENCE/UNIQUE ID");
		builder.setTableList(keyTable);
		builder.append(lastId, Long.valueOf(id));
		builder.appendInternal((String) null);
		builder.addFilter(keyAttribute, key, DbQueryBuilder.STRICT);
		query = new DbQuery((DbTransaction) transaction, builder);
		query.execute();

		return Long.valueOf(id);
	}

	/**
	 * Return whether the given collection has a next element.
	 */
	@Override
	public final boolean hasNext(java.lang.Object collection) {
		boolean next = false;

		if (collection instanceof ResultSet) {
			try {
				next = ((ResultSet) collection).next();

				// check Warnings after next()
				if (((ResultSet) collection).getWarnings() != null) {
					log.warn("{}", ((ResultSet) collection).getWarnings());
				}
			} catch (SQLException e) {
				throw new DeveloperException("ResultSet problem", null, e);
			}
		}

		return next;
	}

	/**
	 * Map <i>Collection of Objects</i>: <b>Target-System => Java</b>. Maps values of given SQL-ResultSet to given DbObject's Properties.
	 * <p>
	 * Only Properties of the given Descriptor are considered, therefore the correct Object hierarchy must be managed by the caller.
	 *
	 * @param instance
	 * @param descriptor
	 * @param collection (Query Results -> only plain attributes, such as fields and foreign key ID's)
	 */
	@Override
	public synchronized void mapFromTarget(DbObject instance,
		DbDescriptor descriptor, Object collection) throws Exception {
		ResultSet resultSet = (ResultSet) collection;

		// sequence IMPORTANT because of SQL-Cursor!!!
		String property = null;

		// 1) get flat attributes directly from ResulSet
		mapFromTargetFlatTypes(instance, descriptor, resultSet,
			descriptor.iteratorFlatProperties());
		// 2) DbNlsString: get Foreign Key's directly from ResultSet
		Map<String, Long> nlsForeignKeys = new HashMap<String, Long>();
		Iterator<String> iterator = descriptor.iteratorNlsStringProperties();
		while ((iterator != null) && iterator.hasNext()) {
			property = iterator.next();
			nlsForeignKeys.put(
				property,
				mapFromTargetLong(resultSet, descriptor.getAttribute(
					instance.getObjectServer(), property)));
		}

		// 3) 1:[0..1] Aggregated DbObject
		Map<String, Long> aggregatesRemoteLocal = new HashMap<String, Long>();
		Set<String> aggregatesRemote = new HashSet<String>();
		iterator = descriptor.iteratorAggregatedProperties();
		while ((iterator != null) && iterator.hasNext()) {
			property = iterator.next();
			if (descriptor.getEntry(property).isLocal()) {
				// @see DbDescriptor#addOneToOneReference(..) mapping where
				// aggregate-ID is known here
				// => get local Foreign Key's directly from ResultSet
				// View B where B.T_ID_A (FK) is known yet
				aggregatesRemoteLocal.put(
					property,
					mapFromTargetLong(
						resultSet,
						descriptor.getAttribute(
							instance.getObjectServer(), property)));
			} else {
				// @see DbDescriptor#addOneToOne(..) => see below 3)
				// View of A where B.T_ID_roleOfA (FK) is unknown yet
				aggregatesRemote.add(property);
			}
		}

		// non-critical to open new SQL-Cursors now
		// (except of the maximum open cursors possible)
		mapNlsStrings(instance, nlsForeignKeys);
		mapLocalAggregates(instance, descriptor, aggregatesRemoteLocal);
		mapRemoteAggregates(instance, descriptor, aggregatesRemote);
		// 4) 1:n
		mapOneToMany(instance, descriptor,
			descriptor.iteratorOneToManyProperties());
		// 5) n:n
		mapManyToMany(instance, descriptor,
			descriptor.iteratorAttributedProperties());
	}

	/**
	 * @see #mapFromTargetString(Object, String)
	 */
	@Override
	public java.math.BigDecimal mapFromTargetBigDecimal(Object collection,
		final String attribute) {
		try {
			java.math.BigDecimal value = ((ResultSet) collection)
				.getBigDecimal(attribute);
			if (((ResultSet) collection).wasNull()) {
				return null;
			} else {
				return value;
			}
		} catch (SQLException e) {
			throw new DeveloperException("Failed to map attribute <"
				+ attribute + ">", null, e);
		}
	}

	/**
	 * Map <i>Type</i>: <b>Target-System => Java</b>. Assumes Textdb-Field with 'T' for true and 'F' for false.
	 *
	 * @see #mapFromTargetString(Object, String)
	 */
	@Override
	public Boolean mapFromTargetBoolean(Object collection,
		final String attribute) {
		String value = mapFromTargetString(collection, attribute);
		if (value == null) {
			return null;
		} else {
			return (value.equals(DB_VALUE_TRUE) ? Boolean.TRUE : Boolean.FALSE);
		}
	}

	/**
	 * @see #mapFromTargetString(Object, String)
	 */
	@Override
	public java.util.Date mapFromTargetDate(Object collection,
		final String attribute, final int type) {
		try {
			java.util.Date date = null;
			switch (type) {
				case DbDateFieldDescriptor.DATE: {
					date = ((ResultSet) collection).getDate(attribute);
					break;
				}
				case DbDateFieldDescriptor.DATETIME: {
					date = ((ResultSet) collection).getTimestamp(attribute);
					break;
				}
				case DbDateFieldDescriptor.TIME: {
					date = ((ResultSet) collection).getTime(attribute);
					break;
				}
				default: {
					throw new DeveloperException("Unknown <type>");
				}
			}
			if (((ResultSet) collection).wasNull()) {
				return null;
			} else {
				return date;
			}

		} catch (SQLException e) {
			throw new DeveloperException("Failed to map attribute <"
				+ attribute + ">", null, e);
		}
	}

	/**
	 * Map special types.
	 */
	@Override
	public DbFieldType mapFromTargetDbFieldType(java.lang.Object collection,
		final String attribute) {
		throw new DeveloperException("subclass must implement");
	}

	/**
	 * @see #mapFromTargetString(Object, String)
	 */
	@Override
	public Double mapFromTargetDouble(Object collection, final String attribute) {
		try {
			double value = ((ResultSet) collection).getDouble(attribute);
			if (((ResultSet) collection).wasNull()) {
				return null;
			} else {
				return new Double(value);
			}
		} catch (SQLException e) {
			throw new DeveloperException("Failed to map attribute <"
				+ attribute + ">", null, e);
		}
	}

	/**
	 * Map <i>Attribute-Types</i>: <b>Target-System => Java</b>.
	 * <p>
	 * Map flat Database Attributes to properties.
	 *
	 * @param itProperties List of flat Property names
	 */
	private final void mapFromTargetFlatTypes(DbObject instance,
		DbDescriptor descriptor, ResultSet resultSet,
		Iterator<String> itProperties) throws Exception {
		// do not trigger consistency checks
		String property = null;
		try {
			// 1) generic types
			while (itProperties.hasNext()) {
				property = itProperties.next();
				DbPropertyChange change = new DbPropertyChange(instance,
					property);

				if (property.equals(DbObject.PROPERTY_ID)
					&& (change.getValue() != null)) {
					// this value might have been read from ResultSet before
					// (and is therefore not in ResultSet anymore now)
					// @see DbObjectServer#retrieve(..)
					continue;
				}

				Class<?> type = change.getGetterReturnType();
				String attribute = descriptor.getAttribute(
					instance.getObjectServer(), property);

				if (type == String.class) {
					change.setProperty(mapFromTargetString(resultSet, attribute));
					continue;
				}
				if (type == java.util.Date.class) {
					DbDescriptorEntry entry = instance.getObjectServer()
						.getDescriptor(instance.getClass())
						.getEntry(property);
					if ((entry != null)
						&& (entry.getFieldType() instanceof DbDateFieldDescriptor)) {
						change.setProperty(mapFromTargetDate(resultSet,
							descriptor.getAttribute(
								instance.getObjectServer(), property),
							((DbDateFieldDescriptor) entry.getFieldType())
								.getType()));
						continue;
					} else {
						change.setProperty(mapFromTargetDate(
							resultSet,
							descriptor.getAttribute(
								instance.getObjectServer(), property),
							DbDateFieldDescriptor.DATE));
						continue;
					}
				}
				if (type == Integer.class) {
					change.setProperty(mapFromTargetInteger(resultSet,
						attribute));
					continue;
				}
				if (type == Long.class) {
					change.setProperty(mapFromTargetLong(resultSet, attribute));
					continue;
				}
				if (type == Double.class) {
					change.setProperty(mapFromTargetDouble(resultSet, attribute));
					continue;
				}
				if (type == java.math.BigDecimal.class) {
					change.setProperty(mapFromTargetBigDecimal(resultSet,
						attribute));
					continue;
				}
				if (type == Boolean.class) {
					change.setProperty(mapFromTargetBoolean(resultSet,
						attribute));
					continue;
				}
				if (type.getName().equals("boolean")) {
					// TODO @deprecated type
					change.getPrivateField().setBoolean(instance,
						mapFromTargetNativeBoolean(resultSet, attribute));
					continue;
				}
				if (type.getName().equals("int")) {
					// TODO @deprecated type
					change.getPrivateField().setInt(instance,
						mapFromTargetNativeInt(resultSet, attribute));
					continue;
				}
				/*
				 * } else if (type.getName().equals("long")) {
				 * field.setLong(instance, getNativeLong(property)); } else if
				 * (type.getName().equals("double")) { field.setDouble(instance,
				 * getNativeDouble(property));
				 */

				// onw types implementing DbFieldType
				Class[] interfaces = type.getInterfaces();
				boolean isDbFieldType = false;
				for (int i = 0; i < interfaces.length; i++) {
					if (interfaces[i].equals(DbFieldType.class)) {
						change.setProperty(mapFromTargetDbFieldType(resultSet,
							attribute));
						isDbFieldType = true;
						break;
					}
				}
				if (isDbFieldType) {
					continue;
				}

				throw new DeveloperException("Type for <" + instance
					+ "-> Property <" + property + "> not yet mapped");
			}
		} catch (Exception e) {
			throw new DeveloperException("mapGenericTypes failed <" + instance
				+ "-> Property <" + property + ">", null, e);
		}
	}

	/**
	 * @see #mapFromTargetString(Object, String)
	 */
	@Override
	public Integer mapFromTargetInteger(Object collection,
		final String attribute) {
		try {
			int value = ((ResultSet) collection).getInt(attribute);
			if (((ResultSet) collection).wasNull()) {
				return null;
			} else {
				return Integer.valueOf(value);
			}
		} catch (SQLException e) {
			throw new DeveloperException("Failed to map attribute <"
				+ attribute + ">", null, e);
		}
	}

	/**
	 * @see #mapFromTargetString(Object, String)
	 */
	@Override
	public Long mapFromTargetLong(Object collection, final String attribute) {
		try {
			long value = ((ResultSet) collection).getLong(attribute);
			if (((ResultSet) collection).wasNull()) {
				return null;
			} else {
				return Long.valueOf(value);
			}
		} catch (SQLException e) {
			throw new DeveloperException("Failed to map attribute <"
				+ attribute + ">", null, e);
		}
	}

	/**
	 * @see #mapFromTargetBoolean(Object, String)
	 * @deprecated
	 */
	@Override
	public boolean mapFromTargetNativeBoolean(Object collection,
		final String attribute) {
		Boolean value = mapFromTargetBoolean(collection, attribute);
		if (value == null) {
			log.warn("Developer warning: attribute [{}] was not properly initalized", attribute);
			return false;
		} else {
			return value.booleanValue();
		}
	}

	/**
	 * @see #mapFromTargetInteger(Object, String)
	 * @deprecated
	 */
	@Override
	public int mapFromTargetNativeInt(Object collection, final String attribute) {
		Integer value = mapFromTargetInteger(collection, attribute);
		if (value == null) {
			log.warn("Developer warning: attribute={} was not properly initalized", attribute);
			return -1;
		} else {
			return value.intValue();
		}
	}

	/**
	 * Maps from <b>Target-System => Java</b>.
	 *
	 * @param collection ResultSet from a query
	 * @param attribute expected Attribute in collection
	 */
	@Override
	public String mapFromTargetString(Object collection, final String attribute) {
		try {
			String value = ((ResultSet) collection).getString(attribute);
			if (((ResultSet) collection).wasNull()) {
				return null;
			} else {
				return value;
			}
		} catch (SQLException e) {
			throw new DeveloperException("Failed to map attribute <"
				+ attribute + ">", null, e);
		}
	}

	/**
	 * Map: <b>Target-System => Java</b>.
	 * <p>
	 * Map Database Aggregations (case 1:[0..1]) of type DbObject to a Property. The ForeignKey is on this side of the given Relationship.
	 */
	private final void mapLocalAggregates(DbObject instance,
		DbDescriptor descriptor, Map<String, Long> foreignKeys)
		throws Exception {
		// do not trigger consistency checks
		String property = null;
		try {
			// object faulting (don't worry about SQL-Cursor anymore)
			Iterator<String> iterator = foreignKeys.keySet().iterator();
			while (iterator.hasNext()) {
				property = iterator.next();
				Long foreignKeyId = foreignKeys.get(property);
				DbPropertyChange<DbObject> change = new DbPropertyChange<DbObject>(
					instance, property);
				if (foreignKeyId == null) {
					change.setProperty(null);
				} else {
					// assign the aggregated object
					java.lang.Class aggregateType = change
						.getGetterReturnType();
					change.setProperty(instance
						.getObjectServer()
						.getObjectById(
							new DbObjectId(aggregateType, foreignKeyId),
							false, descriptor.isCached(property)));
				}
			}
		} catch (Exception e) {
			throw new DeveloperException("failed for <" + instance
				+ " => property <" + property + ">", null, e);
		}
	}

	/**
	 * Map: <b>Target-System => Java</b>.
	 * <p>
	 * Set the list of B-Id's for case A:B=n:n.
	 *
	 * @param itPropertiesNN List of n:n Property names
	 * @see DbDescriptor#addAssociationAttributed(int, String, DbMultiplicityRange, DbMultiplicityRange, Class, String)
	 * @see DbObjectServer#allocate(DbTransaction, DbChangeableBean) 
	 */
	private final void mapManyToMany(DbObject instance,
		DbDescriptor descriptor, Iterator<String> itPropertiesNN)
		throws Exception {
		String property = null; // A.property points to n B's
		try {
			while ((itPropertiesNN != null) && itPropertiesNN.hasNext()) {
				property = itPropertiesNN.next();
				DbDescriptorEntry entry = descriptor.getEntry(property);

				// n:n Map
				java.lang.Class assocClass = entry.getBaseClass();

				// FK of A in n:n Map
				Long idA = instance.getId();
				String foreignKeyNameA = entry.getOtherPropertyName();

				// select from n:n Map
				DbQueryBuilder builder = instance.getObjectServer()
					.createQueryBuilder(DbQueryBuilder.SELECT,
						assocClass.getName() + " as n:n Map");
				// builder.setAttributeList(DbMapper.ATTRIBUTE_ID +
				// DbQueryBuilder.SQL_ATTRIBUTE_SEPARATOR + // Assoc-PK
				// instance.getObjectServer().getConnection().getDescriptor(assocClass).getAttribute(property));//
				// List of B.T_Id's
				builder.setTableList(assocClass);
				// WHERE (Assoc.T_FK_A=A.T_Id)
				builder.addFilter(
					instance.getObjectServer()
						.getDescriptor(assocClass)
						.getAttribute(instance.getObjectServer(),
							foreignKeyNameA), idA);

				// transform Assoc.T_Id in ReferenceObject
				/*
				 * List assocIds =
				 * instance.getObjectServer().getAllValues(builder, 2); List
				 * refsA = new ArrayList(assocIds.size()); Iterator
				 * assocIterator = assocIds.iterator(); while
				 * (assocIterator.hasNext()) { List values =
				 * (List)assocIterator.next(); Long assocId =
				 * Long.valueOf(values.get(0).toString()); Long foreignKeyIdB =
				 * Long.valueOf(values.get(1).toString()); refsA.add(new
				 * DbRelationshipReference(assocId, foreignKeyIdB); }
				 */
				// TODO Tune: n:n could be mapped directly out of query instead
				// of refreshing each one in resultSet
				// set the list of many
				DbPropertyChange change = new DbPropertyChange(instance,
					property);
				change.setProperty(instance.getObjectServer().retrieveAll(
					assocClass, builder /* refsA */));
			}
		} catch (Exception e) {
			throw new DeveloperException("failed <" + instance
				+ " => Property(B) <" + property + ">", null, e);
		}
	}

	/**
	 * Map: <b>Target-System => Java</b>.
	 * <p>
	 * Map Database Aggregations of type DbNlsString to a Property.
	 */
	private final void mapNlsStrings(DbObject instance,
		Map<String, Long> foreignKeys) throws Exception {
		String property = null;
		try {
			Iterator<String> iterator = foreignKeys.keySet().iterator();
			while (iterator.hasNext()) {
				property = iterator.next();
				Long foreignKeyId = foreignKeys.get(property);

				// do not trigger consistency checks
				DbPropertyChange change = new DbPropertyChange(instance,
					property);
				DbNlsString nlsString = null;
				if (foreignKeyId == null) {
					// @see DbObject#refresh(boolean)
					nlsString = (DbNlsString) instance.getObjectServer()
						.createInstance(DbNlsString.class);
				} else {
					// be aware of open straight forward SQL-Cursor
					nlsString = instance.getObjectServer().getNlsString(
						foreignKeyId);
				}
				if (nlsString == null) {
					log.error("Developer error: no NLS-Entry found in DBMS for " + instance + "."
						+ property + " and T_Id_Nls="
						+ foreignKeyId);
				} else {
					nlsString.setChange(change);
				}
				change.setProperty(nlsString);
			}
		} catch (Exception e) {
			throw new DeveloperException("failed for <" + instance
				+ "=> Property <" + property + ">", null, e);
		}
	}

	/**
	 * Map: <b>Target-System => Java</b>.
	 * <p>
	 * Set the list of B's for case A:B=1:n.
	 *
	 * @param itProperties1N List of 1:n Property names
	 * @see DbDescriptor#addOneToMany(int, String, String, DbMultiplicityRange, Class, boolean)
	 */
	private final void mapOneToMany(DbObject instance, DbDescriptor descriptor,
		Iterator<String> itProperties1N) throws Exception {
		String property = null;
		try {
			while ((itProperties1N != null) && itProperties1N.hasNext()) {
				property = itProperties1N.next();
				DbDescriptorEntry entry = descriptor.getEntry(property);

				if (entry.getMapName() == null) {
					// BaseClass of many part
					java.lang.Class baseClass = entry.getBaseClass();
					String foreignKeyName = instance
						.getObjectServer()
						.getDescriptor(baseClass)
						.getAttribute(instance.getObjectServer(),
							entry.getOtherPropertyName());

					// select the many objects
					// be aware of open straight forward SQL-Cursor -> DO NOT
					// map children here!
					DbQueryBuilder builder = SqlQueryBuilder
						.createQueryGetForeignInstances(instance, entry,
							baseClass);
					if (entry.isOrdered()) {
						// replace RoleName T_ID_Role to T_Seq_Role
						builder.addOrder(ATTRIBUTE_ORDER_PREFIX
							+ foreignKeyName.substring(getTargetIdName()
							.length()));
					}

					// set the list of many
					DbPropertyChange change = new DbPropertyChange(instance,
						property);
					change.setProperty(instance.getObjectServer().retrieveAll(
						baseClass, builder));
				} else {
					// get Aggregates via T_MAP_<code> Table
					if (entry.getBaseClass() == null) {
						// many-Type
						DbQueryBuilder builder = DbQueryBuilder
							.createMapClause(DbQueryBuilder.SELECT,
								instance, entry);
						builder.setAttributeList(DbMapper.ATTRIBUTE_MAP_VALUE);
						// set the list of many
						DbPropertyChange change = new DbPropertyChange(
							instance, property);
						List<Object> values = instance.getObjectServer()
							.getFirstValues(builder);
						change.setProperty(values);
					} else {
						// many-Code
						DbQueryBuilder builder = DbQueryBuilder
							.createMapClause(DbQueryBuilder.SELECT,
								instance, entry);
						builder.setAttributeList(DbMapper.ATTRIBUTE_MAP_VALUE_ID
							+ " AS " + getTargetIdName());
						// set the list of many
						DbPropertyChange change = new DbPropertyChange(
							instance, property);
						List<Object> codeIds = instance.getObjectServer()
							.getFirstValues(builder);
						List<Object> codes = new ArrayList<Object>(
							codeIds.size());
						Iterator<Object> ids = codeIds.iterator();
						while (ids.hasNext()) {
							codes.add(instance.getObjectServer().getObjectById(
								new DbObjectId(entry.getBaseClass(), Long
									.valueOf(ids.next().toString())),
								false));
						}
						change.setProperty(codes);
					}
				}
			}
		} catch (Exception e) {
			throw new DeveloperException("failed <" + instance
				+ " => Property <" + property + ">", null, e);
		}
	}

	/**
	 * Map: <b>Target-System => Java</b>.
	 * <p>
	 * Map Database Aggregations (case 1:[0..1]) of type DbObject to a Property. The ForeignKey is on the other side of the given Relationship.
	 */
	private final void mapRemoteAggregates(DbObject instance,
		DbDescriptor descriptor, Set<String> aggregates) throws Exception {
		// do not trigger conistency checks
		String property = null;
		try {
			// Determine the B object if any (don't worry about SQL-Cursor
			// anymore)
			Iterator<String> iterator = aggregates.iterator();
			while (iterator.hasNext()) {
				property = iterator.next();
				DbPropertyChange change = new DbPropertyChange(instance,
					property);
				java.lang.Class baseClass = change.getGetterReturnType();

				DbQueryBuilder builder = SqlQueryBuilder
					.createQueryGetForeignInstances(instance,
						descriptor.getEntry(property), baseClass);
				Object otherPrimaryKey = instance.getObjectServer()
					.getFirstValue(builder);
				if (otherPrimaryKey == null) {
					change.setProperty(null);
				} else {
					change.setProperty(instance.getObjectServer()
						.getObjectById(
							new DbObjectId(baseClass,
								Long.valueOf(otherPrimaryKey
									.toString())), false));
				}
			}
		} catch (Exception e) {
			throw new DeveloperException("failed for <" + instance
				+ " => property <" + property + ">", null, e);
		}
	}

	/**
	 * Map <i>Type</i>: <b>Java => Target-System</b>.
	 *
	 * @param value Application value of a Model
	 * @return String given value as an SQL-Query argument
	 */
	@Override
	public java.lang.Object mapToTarget(
		ch.softenvironment.jomm.descriptor.DbFieldType value) {
		throw new DeveloperException("subclass must implement");
	}

	/**
	 * Map <i>Type</i>: <b>Java => Target-System</b>. Only the Reference-Type of the given value will be mapped.
	 *
	 * @param value Aggregated Object as Application value of a Model
	 * @return String given value as an SQL-Query argument
	 */
	@Override
	public Object mapToTarget(DbObject value) {
		if (value == null) {
			return SqlQueryBuilder.SQL_NULL;
		} else {
			/*
			 * if (value instanceof DbNlsString) { int stop=0; }
			 */
			return value.getId().toString();
		}
	}

	/**
	 * Map <i>Type</i>: <b>Java => Target-System</b>. The value of given change will be mapped here.
	 */
	@Override
	public Object mapToTarget(DbPropertyChange change) {
		try {
			DbObject object = (DbObject) change.getSource();
			Class type = change.getGetterReturnType();
			Object value = change.getValue();

			// 1) Aggregations
			if (type.equals(DbNlsString.class)) {
				// use ForeignKey-Id
				DbNlsString nlsString = (DbNlsString) value;
				if (nlsString != null) {
					nlsString.save();
					if (nlsString.getPersistencyState().isPersistent()) {
						// empty new DbNlsString's will not be INSERTed!
						return mapToTarget(nlsString.getId());
					} else {
						return SqlQueryBuilder.SQL_NULL;
					}
				} else {
					return SqlQueryBuilder.SQL_NULL;
				}
			} else if (BeanReflector.isInherited(type, DbObject.class)) {
				return mapToTarget((DbObject) value);
			}

			// 2) flat Properties
			if (type.equals(String.class)) {
				return mapToTarget((String) value);
			}
			if (type.equals(Boolean.class) || type.equals(boolean.class)) {
				return mapToTarget((Boolean) value);
			}
			if (type.equals(Number.class)
				|| ((type.getSuperclass() != null /*
			 * primitive types
			 */) && type
				.getSuperclass().equals(Number.class))) {
				return mapToTarget((Number) value);
			}
			if (type.equals(java.util.Date.class)) {
				java.util.Date date = (java.util.Date) (value);
				DbDescriptorEntry entry = object.getObjectServer()
					.getDescriptor(object.getClass())
					.getEntry(change.getProperty());
				if ((entry != null)
					&& (entry.getFieldType() instanceof DbDateFieldDescriptor)) {
					return mapToTarget(date,
						((DbDateFieldDescriptor) entry.getFieldType())
							.getType());
				} else {
					// Default Date
					return mapToTarget(date, DbDateFieldDescriptor.DATE);
				}
			}
			if (value instanceof DbFieldType) {
				return mapToTarget((DbFieldType) value);
			}

			throw new DeveloperException("Return Type <" + type.toString()
				+ "> for <" + change.getSource() + " => getter <"
				+ change.getProperty() + "> not yet mapped");
		} catch (Exception e) {
			throw new DeveloperException("mapping <" + change.getSource()
				+ ">#<" + change.getProperty() + "> ", null, e);
		}
	}

	/**
	 * Map <i>Type</i>: <b>Java => Target-System</b>. Tri-State (TRUE, FALSE or NULL).
	 *
	 * @param value Application value of a Model
	 * @return String given value as an SQL-Query argument
	 */
	@Override
	public Object mapToTarget(Boolean value) {
		if (value == null) {
			return SqlQueryBuilder.SQL_NULL;
		} else if (value.booleanValue()) {
			return mapToTarget(DB_VALUE_TRUE);
		} else {
			return mapToTarget(DB_VALUE_FALSE);
		}
	}

	/**
	 * Map <i>Type</i>: <b>Java => Target-System</b>.
	 *
	 * @param value Application value of a Model
	 * @return String given value as an SQL-Query argument
	 */
	@Override
	public Object mapToTarget(Number value) {
		if (value == null) {
			// prevent value==null to "null" mapping by Java
			return SqlQueryBuilder.SQL_NULL;
		} else {
			return value.toString();
		}
	}

	/**
	 * Map <i>Type</i>: <b>Java => Target-System</b>.
	 *
	 * @param value Application value of a Model
	 * @return String given value as an SQL-Query argument
	 */
	@Override
	public Object mapToTarget(String value) {
		if ((value == null) || (value.length() == 0)) {
			// prevent value==null to "null" mapping by Java
			return SqlQueryBuilder.SQL_NULL;
		} else {
			// String tmp = value.trim().replace('\'', '`'); // make sure
			// SQL-String won't be separated
			return "'" + value.replaceAll("'", "''") + "'";
		}
	}

	/**
	 * Map <i>Type</i>: <b>Java => Target-System</b>.
	 *
	 * @param value Application value of a Model
	 * @param type DbDateFieldTypeDescriptor=>types
	 * @return Object given value as an SQL-Query argument
	 */
	@Override
	public Object mapToTarget(java.util.Date value, final int type) {
		if (value == null) {
			// prevent value==null to "null" mapping by Java
			return SqlQueryBuilder.SQL_NULL;
		} else {
			switch (type) {
				case DbDateFieldDescriptor.DATE: {
					java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(
						DATE_SQL_FORMAT);
					String date = "'" + sf.format(value) + "'";
					return date;
				}
				case DbDateFieldDescriptor.TIME: {
					return "'" + (new java.sql.Time(value.getTime())).toString()
						+ "'";
					// java.text.SimpleDateFormat sf = new
					// java.text.SimpleDateFormat(NlsUtils.TIME_24HOURS_PATTERN);
					// return "'" + sf.format(value) + "'";
				}
				case DbDateFieldDescriptor.DATETIME: {
					// return "'" + (new
					// java.sql.Timestamp(value.getTime())).toString() + "'";
					java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(
						TIMESTAMP_SQL_FORMAT);
					String ts = "'" + sf.format(value) + "'";
					return ts;
				}
				default:
					throw new DeveloperException("Unknown type");
			}
		}
	}

	/**
	 * Return a qualified attribute-name on Target-System.
	 */
	@Override
	public final java.lang.String mapToTargetQualified(
		java.lang.String qualifier, final String attribute) {
		if (StringUtils.isNullOrEmpty(qualifier)) {
			return attribute;
		} else {
			return qualifier + SqlQueryBuilder.SQL_QUALIFIER_SEPARATOR
				+ attribute;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.softenvironment.jomm.DbMapper#getTargetIdName()
	 */
	@Override
	public final String getTargetIdName() {
		return "T_Id";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.softenvironment.jomm.DbMapper#getTargetNlsName()
	 */
	@Override
	public final String getTargetNlsName() {
		return "T_NLS";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.softenvironment.jomm.DbMapper#getTargetNlsTranslationName()
	 */
	@Override
	public final String getTargetNlsTranslationName() {
		return "T_Translation";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.softenvironment.jomm.DbMapper#getTargetRemoveHistoryName()
	 */
	@Override
	public String getTargetRemoveHistoryName() {
		return "T_RemoveHistory";
	}
}
