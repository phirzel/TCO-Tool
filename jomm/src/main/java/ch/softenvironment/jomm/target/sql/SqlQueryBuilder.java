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
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.util.Locale;

/**
 * A Relational-DBMS Target-System provides its own language (SQL) to interact with. This Utility provides a Target Language independent API to formulate any requests to be communicated to the
 * Target-System.
 * <p>
 * SQL knows three different language-types: - Data Definition Language (DDL) => CREATE/ALTER/DROP - Data Manipulation Language (DML) => INSERT/UPDATE/DELETE - Data Query Language (DQL) => SELECT
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class SqlQueryBuilder extends DbQueryBuilder {

	// SQL Constants
	public final static String SQL_NULL = "NULL";
	public final static String SQL_ATTRIBUTE_SEPARATOR = ", ";
	public final static String SQL_QUALIFIER_SEPARATOR = ".";
	public final static String SQL_WHERE = "WHERE";
	public final static String SQL_COMPARISON_EQUAL = "=";
	public final static String SQL_AND = "AND";
	public final static String SQL_OR = "OR";
	public final static String SQL_GREATER_THAN = ">";
	public final static String SQL_LOWER_THAN = "<";
	public final static String SQL_GREATER_EQUAL = ">=";
	public final static String SQL_LESS_EQUAL = "<=";
	public final static String SQL_NOT_EQUAL = "<>";

	// current Query substrings
	private transient StringBuffer tableList = new StringBuffer();
	private transient StringBuffer attributeList = new StringBuffer();
	private final transient StringBuffer valueList = new StringBuffer();
	private transient StringBuffer whereClause = new StringBuffer();
	private final transient StringBuffer orderBy = new StringBuffer();
	private transient boolean distinct = false;

	public SqlQueryBuilder(DbObjectServer objectServer, final Integer queryType, final String useCase) {
		super(objectServer, queryType, useCase);
	}

	@Override
	public void addFilter(final String attribute, Object[] dbObjects) {
		if ((dbObjects != null) && (dbObjects.length > 0)) {
			if (dbObjects.length == 1) {
				addFilter(tagTargetExpression(attribute), (DbObject) dbObjects[0]);
			} else {
				String clause = "";
				for (int i = 0; i < dbObjects.length; i++) {
					if (clause.length() > 0) {
						clause = clause + " " + SQL_OR + " ";
					}
					clause = clause + "(" + tagTargetExpression(attribute) + SQL_COMPARISON_EQUAL + ((DbObject) dbObjects[i]).getId().longValue() + ")";

				}
				addFilterRaw(clause);
			}
		}
	}

	@Override
	public void addFilter(final String attribute, DbNlsString nlsString) {
		if (nlsString != null) {
			String qualifier = "";
			int index = attribute.indexOf(SQL_QUALIFIER_SEPARATOR);
			if (index > 0) {
				// keep qualifying table
				qualifier = attribute.substring(0, index) + SQL_QUALIFIER_SEPARATOR;
			}

			if ((nlsString.getId() != null) && (nlsString.getId().longValue() > 0 /*
			 * non
			 * -
			 * temporary
			 */)) {
				// compare by NlsId
				addFilter(qualifier + DbNlsString.ATTRIBUTE_TRANSLATION_ID, nlsString.getId());
			} else if (!StringUtils.isNullOrEmpty(nlsString.getValue())) {
				// (query for the language the text-value is written in) AND
				// (the text-value itself
				addFilterRaw("(" + getStringClause(qualifier + DbNlsString.ATTRIBUTE_LANGUAGE, nlsString.getLanguage(), STRICT) + ") " + SQL_AND + " ("
					+ getStringClause(attribute, nlsString.getValue(), WILD) + ")");
			}
		}
	}

	@Override
	public void addFilter(final String attribute, DbObject object) {
		if (object != null) {
			addFilter(attribute, object.getId());
		}
	}

	@Override
	public void addFilter(final String attribute, Boolean value) {
		if (value != null) {
			addFilterRaw(tagTargetExpression(attribute) + SQL_COMPARISON_EQUAL + getObjectServer().getMapper().mapToTarget(value));
		}
	}

	@Override
	public void addFilter(final String attribute, Number value) {
		if (value != null) {
			addFilterRaw(tagTargetExpression(attribute) + SQL_COMPARISON_EQUAL + getObjectServer().getMapper().mapToTarget(value));
		}
	}

	@Override
	public void addFilter(final String attribute, final String value, final int type) {
		if (!StringUtils.isNullOrEmpty(value)) {
			addFilterRaw(getStringClause(tagTargetExpression(attribute), value, type));
		}
	}

	/**
	 * AND's a new WHERE-Clause condition as a <b>Sub-SELECT</b> condition: For e.g.: WHERE attribute IN (values[1], values[2], values[3], ..)
	 * <p>
	 * values[i].toString() will be used for query building.
	 */
	@Override
	public void addFilter(final String attribute, java.util.Set<?> values) {
		if ((values != null) && (values.size() > 0)) {
			/*
			 * if (values.size() == 1) { // optimize query by =Comparison
			 * instead of further Sub-SELECT //TODO BUG sometimes throws a
			 * ClassCast-Exception addFilter(attribute,
			 * (Number)values.toArray()[0]); } else {
			 */
			java.util.Iterator<?> iterator = values.iterator();
			String sublist = "";
			while (iterator.hasNext()) {
				if (sublist.length() > 0) {
					sublist = sublist + ", ";
				}
				sublist = sublist + iterator.next().toString();
			}
			addFilterRaw(tagTargetExpression(attribute) + " IN (" + sublist + ")");
			// }
		} else {
			Tracer.getInstance().developerWarning("Filter suppressed because of empty Set!");
			// TODO evtl. better use: addFilterNull(attribute, Boolean.False);
			// addFilterRaw(tagTargetExpression(attribute) + " IN (" + SQL_NULL
			// + ")"); // IN compared to null-ID to prevent a ResultSet over all
			// existing Objects!
		}
	}

	@Override
	public void addFilterBetweenAndDate(final String attribute, java.util.Date from, java.util.Date to) {
		// try Between And
		if ((from != null) && (to != null)) {
			// TODO evtl. swap date if wrong sense
			addFilterRaw(tagTargetExpression(attribute) + " BETWEEN " + getObjectServer().getMapper().mapToTarget(from, DbDateFieldDescriptor.DATE) + " "
				+ SQL_AND + " " + getObjectServer().getMapper().mapToTarget(to, DbDateFieldDescriptor.DATE));
		} else if (from != null) {
			addFilterRaw(tagTargetExpression(attribute) + SQL_GREATER_EQUAL + getObjectServer().getMapper().mapToTarget(from, DbDateFieldDescriptor.DATE));
		} else if (to != null) {
			// from was invalid => try <to
			addFilterRaw(tagTargetExpression(attribute) + SQL_LESS_EQUAL + getObjectServer().getMapper().mapToTarget(to, DbDateFieldDescriptor.DATE));
		}
	}

	@Override
	public void addFilterGreater(final String attribute, Number value) {
		if (value != null) {
			addFilterRaw(tagTargetExpression(attribute) + SQL_GREATER_THAN + getObjectServer().getMapper().mapToTarget(value));
		}
	}

	@Override
	public void addFilterNotEquals(final String attribute, java.lang.Number value) {
		if (value != null) {
			addFilterRaw(tagTargetExpression(attribute) + SQL_NOT_EQUAL + getObjectServer().getMapper().mapToTarget(value));
		}
	}

	/**
	 * - value == null => ignore resp. no WHERE clause at all - value == true => attribute IS NULL - value == false => attribute IS NOT NULL
	 */
	@Override
	public void addFilterNull(final String attribute, Boolean value) {
		if (value != null) {
			if (value.booleanValue()) {
				addFilterRaw(tagTargetExpression(attribute) + " IS " + SQL_NULL);
			} else {
				addFilterRaw(tagTargetExpression(attribute) + " IS NOT " + SQL_NULL);
			}
		}
	}

	@Override
	public void addFilterRaw(final String condition) {
		if (!StringUtils.isNullOrEmpty(condition)) {
			if (whereClause.length() > 0) {
				// AND another condition
				whereClause.append(" " + SQL_AND + " ");
			}
			whereClause.append("(" + condition + ")");
		}
	}

	@Override
	public void addOrder(final String attribute, boolean ascending) {
		if (orderBy.length() > 0) {
			orderBy.append(SQL_ATTRIBUTE_SEPARATOR);
		}
		orderBy.append(tagTargetExpression(attribute) + (ascending ? " ASC" : " DESC"));
	}

	private void appendAttribute(final String attribute) {
		if (attributeList.length() > 0) {
			attributeList.append(SQL_ATTRIBUTE_SEPARATOR);
		}
		attributeList.append(checkAlias(attribute));
		suppressQuery = false;
	}

	@Override
	protected void appendRaw(final String attribute, final String valueFormated) {
		if ((getType() == INSERT) || (getType() == SELECT)) {
			appendAttribute(tagTargetExpression(attribute));
			if (getType() == INSERT) {
				// do not skip valueFormated here
				if (valueList.length() > 0) {
					valueList.append(SQL_ATTRIBUTE_SEPARATOR);
				}
				valueList.append(valueFormated);
			}
		} else if (getType() == UPDATE) {
			appendAttribute(attribute + SQL_COMPARISON_EQUAL + valueFormated);
		} else {
			throw new DeveloperException("append not possible");
		}
	}

	/**
	 * Define correct ALIASing according to DBMS-Type. In SQL92 both is correct - SELECT Field AS MyAlias,.. - SELECT Field MyAlias,..
	 */
	protected String checkAlias(final String sqlString) {
		return sqlString;
	}

	/**
	 * Return a generic SQL-SELECT Query for owners aggregated DbObject(s).
	 *
	 * @param owner The owner of the aggregation
	 * @param entry
	 * @baseClass Type of Aggregate
	 */
	protected static final DbQueryBuilder createQueryGetForeignInstances(DbObject owner, DbDescriptorEntry ownerEntry,
		Class<? extends DbObject> aggregateBaseClass) {
		DbObjectServer objectServer = owner.getObjectServer();
		DbQueryBuilder builder = objectServer.createQueryBuilder(DbQueryBuilder.SELECT, aggregateBaseClass.getName() + " by Foreign Key Owner");
		// builder.setAttributeList(owner.getObjectServer().getConnection().getDescriptor(aggregateBaseClass).getAttribute(DbObject.PROPERTY_ID));
		builder.setTableList(aggregateBaseClass);
		// WHERE (Aggregate.T_ID_ForeignKey=owner.T_ID)
		builder.addFilter(owner.getObjectServer().getDescriptor(aggregateBaseClass).getAttribute(owner.getObjectServer(), ownerEntry.getOtherPropertyName()),
			owner.getId());

		return builder;
	}

	/**
	 * Return a generic SQL-SELECT Query for this DbObject only.
	 *
	 * @param dbObject Type of DbObject (necessary for inheritance Chains)
	 * @param instance Concrete Object of type dbObject
	 */
	protected static final DbQueryBuilder createQueryGetObject(Class<? extends DbObject> dbObject, DbObject instance) {
		DbQueryBuilder builder = instance.getObjectServer().createQueryBuilder(DbQueryBuilder.SELECT, dbObject.getName() + " by ID");
		builder.setTableList(dbObject);
		if (dbObject.equals(DbNlsString.class)) {
			// NASTY: to do this here!
			builder.addFilter(DbNlsString.ATTRIBUTE_TRANSLATION_ID, instance.getId());
		} else {
			builder.addFilter(instance.getObjectServer().getDescriptor(instance.getClass()).getAttribute(instance.getObjectServer(), DbObject.PROPERTY_ID /*
				 * DbMapper
				 * .
				 * ATTRIBUTE_ID
				 */),
				instance.getId());
		}
		return builder;
	}

	private String getSelectAttributeList() {
		String tmp = attributeList.toString();
		if (StringUtils.isNullOrEmpty(attributeList.toString())) {
			tmp = "*";
		}
		return (distinct ? "DISTINCT " : "") + tmp;
	}

	@Override
	public String getQuery() {
		StringBuffer query = new StringBuffer();

		switch (getType()) {
			case SELECT:
				query.append("SELECT " + getSelectAttributeList() + " FROM " + tableList.toString() + getWhereClause());
				if (orderBy.length() > 0) {
					query.append(" ORDER BY " + orderBy.toString());
				}
				if (getFetchBlockSize() > FETCHBLOCK_UNLIMITED) {
					// TODO overwrite => non standard command
				}
				break;
			case INSERT:
				query.append("INSERT INTO " + tableList.toString() + " (" + attributeList.toString() + ") VALUES (" + valueList.toString() + ")");
				break;
			case UPDATE:
				query.append("UPDATE " + tableList.toString() + " SET " + attributeList.toString() + getWhereClause());
				break;
			case DELETE:
				query.append("DELETE FROM " + tableList.toString() + getWhereClause());
				break;
			case LOCK:
				// rootTODO MySQL specific
				query.append("LOCK TABLES " + tableList.toString() + " WRITE");
				break;
			case UNLOCK:
				// TODO MySQL specific
				query.append("UNLOCK TABLES " /* + tableList.toString() */);
				break;
			default:
				// @see Constructor => must be "raw"
				query.append(tableList.toString());
				break;
		}

		return query.toString();
	}

	@Override
	public String getStringClause(final String attribute, final String value, final int type) {
		if (type == STRICT) {
			return attribute + SQL_COMPARISON_EQUAL + getObjectServer().getMapper().mapToTarget(value);
		} else if (type == WILD) {
			// TODO treat masking characters for *,? and ' in case they are part
			// of an expression
			// make sure SQL-String won't be separated
			String tmp = value.replace('\'', '`');
			// Replace proper Wildcards
			tmp = tmp.replace('*', '%');
			tmp = tmp.replace('?', '_');
			if (!tmp.startsWith("%")) {
				tmp = "%" + tmp;
			}
			if (!tmp.endsWith("%")) {
				tmp = tmp + "%";
			}

			return attribute + " LIKE " + getObjectServer().getMapper().mapToTarget(tmp);
		} else {
			// TODO NYI: <type> => trying <WILD>
			return getStringClause(attribute, value, WILD);
		}
	}

	/**
	 * Return the built SQL-Query.
	 */
	private String getWhereClause() {
		if (whereClause.length() > 0) {
			return " " + SQL_WHERE + " " + whereClause.toString();
		} else {
			return "";
		}
	}

	/**
	 * An UPDATE- or DELETE-Statement should always be restricted on one or a few records only.
	 */
	@Override
	public final boolean isDangerous() {
		if (isUpdate() || isDelete()) {
			String query = getQuery();
			int index = query.indexOf(SQL_WHERE, 7);
			return (index <= 0) ||
				// case: usual Data-Table
				((query.indexOf(getObjectServer().getMapper().getTargetIdName() + SQL_COMPARISON_EQUAL, index) <= 0) &&
					// case: new entity
					(query.indexOf(DbMapper.ATTRIBUTE_KEY_TABLE + SQL_COMPARISON_EQUAL, index) <= 0) &&
					// case: DbNlsString Translation-Update
					(query.indexOf(DbNlsString.ATTRIBUTE_TRANSLATION_ID + SQL_COMPARISON_EQUAL, index) <= 0) &&
					// case: T_Map_* manipulation
					(query.indexOf(DbMapper.ATTRIBUTE_MAP_OWNER_ID + SQL_COMPARISON_EQUAL, index) <= 0) &&
					// case: at least some foreign key constraint not
					// generically defined (WHERE T_Id_MyFk=..)
					(query.toUpperCase().indexOf((getObjectServer().getMapper().getTargetIdName().toUpperCase() + "_"), index) <= 0));
		}

		return false;
	}

	@Override
	public final boolean isSelection() {
		String query = getQuery();
		return super.isSelection() || ((getType() == RAW) && (query != null) && (query.trim().toUpperCase().startsWith("SELECT")));
	}

	@Override
	public void setAttributeList(final String attributeList, boolean distinct) {
		if (attributeList != null) {
			if ((this.attributeList != null) && (this.attributeList.length() > 0)) {
				Tracer.getInstance().developerWarning("Overwriting given attributeList!");
			}
			this.attributeList = new StringBuffer(checkAlias(attributeList));
			suppressQuery = false;
		}
		this.distinct = distinct;
	}

	/**
	 * Create tableList as INNER JOIN. Typically combines Parent and Child Entity (for e.g. Person extends LegalEntity). SQL: "A INNER JOIN B ON A.ID=B.ID"
	 *
	 * @deprecated
	 */
	public void setInnerJoin(final String leftTable, final String rightTable) {
		if (tableList.length() > 0) {
			// TODO
			throw new DeveloperException("NYI");
		}
		setTableList(leftTable + " INNER JOIN " + rightTable + " ON " + leftTable + SQL_QUALIFIER_SEPARATOR + getObjectServer().getMapper().getTargetIdName()
			+ SQL_COMPARISON_EQUAL + rightTable + SQL_QUALIFIER_SEPARATOR + getObjectServer().getMapper().getTargetIdName());
	}

	/**
	 * Create a NATURAL JOIN of type: SELECT * FROM A, B WHERE condition where condition might be one of the equivalent forms: 1) A JOIN B ON A.FKID = B.FKID (produces 2 columns for FKID) 2) A JOIN B
	 * USING(FKID) (produces 1 column for FKID) 3) A NATURAL JOIN B (produces 1 column for FKID)
	 * <p>
	 * This method uses the Variant 1).
	 * <p>
	 * Typically used to Join Addresses for a Person.
	 */
	@Override
	public void setNaturalJoin(final String leftTable, final String rightTable, final String leftKeySuffix, final String rightKeySuffix) {
		if (tableList.length() > 0) {
			// TODO
			throw new DeveloperException("multi-tables not yet implemented");
		}
		setTableList(leftTable + " AS A, " + rightTable + " AS B");

		// add another condition
		addFilterRaw("A" + SQL_QUALIFIER_SEPARATOR + leftKeySuffix + SQL_COMPARISON_EQUAL + "B" + SQL_QUALIFIER_SEPARATOR + rightKeySuffix);
	}

	@Override
	public void setRaw(final String statement) {
		// use the query as is
		this.tableList.append(statement);
	}

	@Override
	public void setTableList(final String tableList) {
		this.tableList = new StringBuffer(checkAlias(tableList));
	}

	@Override
	public String getTableList() {
		return tableList.toString();
	}

	/**
	 * Complete this builder Builder for a "SELECT * from CodeTable" Query.
	 */
	@Override
	public DbQueryBuilder toCodeNlsBuilder(Class<DbCodeType> dbCode, Locale locale) {
		setAttributeList("B.*");
		setNaturalJoin(getObjectServer().getTargetName(dbCode), getObjectServer().getTargetName(DbNlsString.class), DbMapper.ATTRIBUTE_NAME_ID,
			DbNlsString.ATTRIBUTE_TRANSLATION_ID);
		addFilter("B." + DbNlsString.ATTRIBUTE_LANGUAGE, locale.getLanguage(), DbQueryBuilder.STRICT);
		// TODO NYI Country for code <" + dbCode.getName() + "> not considered
		// builder.addFilter("B." + DbNlsString.ATTRIBUTE_COUNTRY,
		// locale.getCountry(), DbQueryBuilder.STRICT);
		return this;
	}

	@Override
	public int getCount(java.lang.Class<? extends DbObject> dbObject) {
		try {
			if (isSelection()) {
				SqlQueryBuilder builder = (SqlQueryBuilder) getObjectServer().createQueryBuilder(SELECT, getQueryDescription() + " as COUNT(*)");
				builder.setTableList(tableList.toString());
				builder.whereClause = whereClause;
				// ignore fetchBlockSize
				// ignore ORDER By
				if (distinct) {
					// TODO TUNE might be inefficient for real large COUNT
					builder.setAttributeList(getSelectAttributeList());
					// builder.setAttributeList(getObjectServer().getTargetName(dbObject)
					// + "." + getObjectServer().getMapper().getTargetIdName());
					return getObjectServer().getFirstValues(builder).size();
				} else {
					// faster than distinct version
					builder.setAttributeList("COUNT(*)");
					return (Long.valueOf(builder.getObjectServer().getFirstValue(builder).toString())).intValue();
				}
			} else {
				Tracer.getInstance().developerError("SELECT-Queries only!");
			}
		} catch (Exception e) {
			Tracer.getInstance().runtimeError("Count could not be determined", e);
		}
		return -1;
	}

	@Override
	public void addFilterLower(final String attribute, Number value) {
		if (value != null) {
			addFilterRaw(tagTargetExpression(attribute) + SQL_LOWER_THAN + getObjectServer().getMapper().mapToTarget(value));
		}
	}

	/**
	 * @param subQuery must result in a SELECT with one Attribute in AttributeList
	 */
	@Override
	public void addFilter(final String attribute, DbQueryBuilder subQuery) {
		if ((subQuery != null) && (subQuery.isSelection()) && (subQuery.getQuery() != null)) {
			addFilterRaw(tagTargetExpression(attribute) + " IN (" + subQuery.getQuery() + ")");
		}
	}
}
