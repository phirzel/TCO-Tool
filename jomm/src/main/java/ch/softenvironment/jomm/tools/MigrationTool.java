package ch.softenvironment.jomm.tools;

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
import ch.softenvironment.jomm.DbQuery;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.DbTransaction;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper Utility for Data Migrations.
 *
 * @author Peter Hirzel
 */
public class MigrationTool {

	private DbObjectServer server;

	{
		server = null;
	}

	public MigrationTool(DbObjectServer server) {
		super();
		this.server = server;
	}

	/**
	 * In case of a migration all kinds of existing enumerations are known while migrating only, therefore an insertion can be provided like this.
	 *
	 * @param dbEnumeration
	 * @param idEnum
	 * @param idNls
	 * @param iliCode
	 * @param nls (new DbNlsString())
	 * @return
	 */
	public DbEnumeration createEnumeration(Class dbEnumeration, long idEnum, long idNls, String iliCode, DbNlsString nls) throws Exception {
		// TODO update Java class with IliCode constants
		String type = StringUtils.getPureClassName(dbEnumeration);
		String createDate = (String) server.getMapper().mapToTarget(new Date(), DbDateFieldDescriptor.DATETIME);
		String user = (String) server.getMapper().mapToTarget(server.getUserId());
		String tid = server.getMapper().getTargetIdName();

		List<String> sql = new ArrayList<String>();
		sql.add("INSERT INTO " + server.getMapper().getTargetNlsName() + " (" + tid + ", Symbol, " + DbMapper.ATTRIBUTE_CREATE_DATE + ", "
			+ DbMapper.ATTRIBUTE_USER_ID + ") VALUES  (" + idNls + ", " + server.getMapper().mapToTarget(type) + ", " + createDate + ", " + user
			+ ")");
		java.util.Iterator<String> translations = nls.getAllValuesByLanguage().keySet().iterator();
		while (translations.hasNext()) {
			// TODO key == language => might change soon
			String key = translations.next();
			String nlsText = nls.getTranslation(key);
			if (StringUtils.isNullOrEmpty(nlsText) || StringUtils.isNullOrEmpty(iliCode)) {
				throw new DeveloperException(dbEnumeration + "->iliCode: " + iliCode);
			}
			sql.add("INSERT INTO " + server.getMapper().getTargetNlsTranslationName() + " (" + DbNlsString.ATTRIBUTE_TRANSLATION_ID + ", "
				+ DbNlsString.ATTRIBUTE_LANGUAGE + ", " + DbNlsString.ATTRIBUTE_NLS_TEXT + ", " + DbMapper.ATTRIBUTE_CREATE_DATE + ", "
				+ DbMapper.ATTRIBUTE_USER_ID + ") VALUES (" + idNls + ", '" + key + "', " + server.getMapper().mapToTarget(nlsText) + ", " + createDate
				+ ", " + user + ")");
		}
		sql.add("INSERT INTO " + server.getTargetName(dbEnumeration) + " (" + tid + ", T_Id_Name, IliCode, " + DbMapper.ATTRIBUTE_CREATE_DATE + ", "
			+ DbMapper.ATTRIBUTE_USER_ID + ") VALUES (" + idEnum + ", " + idNls + ", '" + iliCode + "', " + createDate + ", " + user + ")");

		// create new instance of a DbEnumeration
		server.execute("Create " + type, sql);
		server.evictAll(); // => force recache of Enumerations

		return server.getIliCode(dbEnumeration, iliCode);
	}

	/**
	 * Replace bad Characters for an IliCode Constant. Useful if DbEnumeration's are known at migration time.
	 *
	 * @param text
	 * @return
	 */
	public static String convertIliCode(String text) {
		String tmp = StringUtils.replace(text, " ", "");
		tmp = StringUtils.replace(tmp, ".", "");
		tmp = StringUtils.replace(tmp, ":", "");
		tmp = StringUtils.replace(tmp, ",", "");
		tmp = StringUtils.replace(tmp, ";", "");
		tmp = StringUtils.replace(tmp, "?", "");
		tmp = StringUtils.replace(tmp, "!", "");
		tmp = StringUtils.replace(tmp, "-", "");
		tmp = StringUtils.replace(tmp, "'", "");
		// TODO Umlaute
		tmp = StringUtils.replace(tmp, "(", "");
		tmp = StringUtils.replace(tmp, ")", "");
		tmp = StringUtils.replace(tmp, "[", "");
		tmp = StringUtils.replace(tmp, "]", "");
		tmp = StringUtils.replace(tmp, "{", "");
		tmp = StringUtils.replace(tmp, "}", "");
		return tmp;
	}

	/**
	 * Retrieve all objects for given dbObject class according to rawFilter and create INSERT-Statements for each instance. The DbDescriptor is considered strongly to determine instance-data. These
	 * INSERT's will be generated on target-Server. => DbNlsString's or Multi-Codes will be generated for corresponding technical Tables (T_NLS, T_MAP_*) => be aware that any Modifications to
	 * SQL-Schema out of Descriptor will not be dumped!
	 *
	 * @param dbObject (inheritant of DbObject)
	 * @param rawFilter (optional, null if none)
	 * @return
	 */
	public void createInsertStatements(DbObjectServer target, Class<? extends DbObject> dbObject, String rawFilter) throws Exception {
		DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, "All " + dbObject.getName());
		String tableName = server.getTargetName(dbObject);
		builder.setTableList(tableName);
		if (rawFilter != null) {
			builder.addFilterRaw(rawFilter);
		}

		DbDescriptor descriptor = server.getDescriptor(dbObject);
		java.util.Iterator<? extends DbObject> it = server.retrieveAll(dbObject, builder, false).iterator();
		while (it.hasNext()) {
			java.util.List<String> inserts = new ArrayList<String>(); // keep
			// all
			// data
			// together
			// for
			// one
			// object
			// with
			// related
			// data
			java.util.List maps = new ArrayList();

			DbObject obj = it.next();
			StringBuffer attrList = new StringBuffer();
			StringBuffer values = new StringBuffer();

			java.util.Iterator<String> properties = descriptor.iteratorFlatProperties();
			while (properties.hasNext()) {
				String property = properties.next();
				if (attrList.length() > 0) {
					attrList.append(", ");
					values.append(", ");
				}
				attrList.append(descriptor.getAttribute(server, property));
				DbPropertyChange val = new DbPropertyChange(obj, property);
				// java.lang.Class ebType = ebChange.getType();
				values.append(server.getMapper().mapToTarget(val));
			}

			properties = descriptor.iteratorNlsStringProperties();
			while ((properties != null) && properties.hasNext()) {
				String property = properties.next();
				if (attrList.length() > 0) {
					attrList.append(", ");
					values.append(", ");
				}
				attrList.append(descriptor.getAttribute(server, property));
				DbPropertyChange val = new DbPropertyChange(obj, property);
				createInsertStatementNLS(inserts, ((DbObject) val.getValue()).getId());
				values.append(server.getMapper().mapToTarget(val));
			}

			properties = descriptor.iteratorAggregatedProperties();
			while ((properties != null) && properties.hasNext()) {
				String property = properties.next();
				if (attrList.length() > 0) {
					attrList.append(", ");
					values.append(", ");
				}
				attrList.append(descriptor.getAttribute(server, property));
				DbPropertyChange val = new DbPropertyChange(obj, property);
				values.append(server.getMapper().mapToTarget(val));
			}

			// finally write the records for given dbClass
			// TODO use DbQueryBuilder for creating INSERT
			StringBuffer sqlDml = new StringBuffer();
			sqlDml.append("INSERT INTO " + tableName + " (");
			if (server.getConnection().getRootClassFor(dbObject).equals(dbObject) && (!dbObject.getSuperclass().equals(DbEnumeration.class))) {
				attrList.append(", " + DbMapper.ATTRIBUTE_CONCRETE_CLASS);
				values.append(", " + server.getMapper().mapToTarget(tableName));
			}
			sqlDml.append(attrList.toString());
			sqlDml.append(") VALUES (");
			sqlDml.append(values.toString());
			sqlDml.append(");\n");

			properties = descriptor.iteratorOneToManyProperties();
			while ((properties != null) && properties.hasNext()) {
				String property = properties.next();

				DbDescriptorEntry entry = server.getDescriptor(dbObject).getEntry(property);
				if (entry.getMapName() != null) {
					createInsertStatementMAP(maps, obj.getId(), server.getTargetName(dbObject), entry.getOtherPropertyName(), entry.getMapName());
				} // else assignment on other side of relationship
			}

			properties = descriptor.iteratorAttributedProperties();
			while ((properties != null) && properties.hasNext()) {
				String property = properties.next();
				// TODO NYI
			}

			inserts.add(sqlDml.toString()); // must precede T_MAP_<code> entries
			inserts.addAll(maps);
			target.execute("Instance of: " + dbObject, inserts);
		}
	}

	/**
	 * T_NLS & T_Translation.
	 *
	 * @param inserts
	 * @param id
	 */
	private void createInsertStatementNLS(java.util.List<String> inserts, Long id) throws Exception {
		Exception exception = null;
		DbTransaction trans = null;
		try {
			String tableNLS = server.getMapper().getTargetNlsName();
			String targetId = server.getMapper().getTargetIdName();
			DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, tableNLS);
			builder.setTableList(tableNLS);
			builder.addFilter(targetId, id);

			trans = server.openTransactionThread(false);
			DbQuery query = new DbQuery(trans, builder);
			java.sql.ResultSet queryResult = (java.sql.ResultSet) query.execute();
			if (server.getMapper().hasNext(queryResult)) {
				String tableTranslation = server.getTargetName(DbNlsString.class);
				// TODO use constants from DbMapper
				// TODO use DbQueryBuilder for creating INSERT

				// 0..1 T_NLS
				StringBuffer nls = new StringBuffer();
				nls.append("INSERT INTO " + tableNLS + " (T_Id, Symbol, T_CreateDate, T_LastChange, T_User) VALUES (");
				nls.append(queryResult.getString(targetId) + ", ");
				nls.append(server.getMapper().mapToTarget(queryResult.getString("Symbol")) + ", ");
				// TODO missing DateTime transformation
				nls.append(server.getMapper().mapToTarget(queryResult.getString("T_CreateDate")) + ", ");
				nls.append(server.getMapper().mapToTarget(queryResult.getString("T_LastChange")) + ", ");
				nls.append(server.getMapper().mapToTarget(queryResult.getString("T_User")) + ");\n");
				query.closeAll();
				inserts.add(nls.toString());

				// with 1..* T_Translation's
				StringBuffer translation = new StringBuffer();
				builder = server.createQueryBuilder(DbQueryBuilder.SELECT, tableTranslation);
				builder.setTableList(tableTranslation);
				builder.addFilter("T_Id_Nls", id);
				query = new DbQuery(trans, builder);
				queryResult = (java.sql.ResultSet) query.execute();
				String comma = "INSERT INTO T_Translation (T_Id_Nls, Language, Country, NlsText, T_CreateDate, T_LastChange, T_User) VALUES";
				while (server.getMapper().hasNext(queryResult)) {
					// 1 INSERT with n VALUES => INSERT () VALUES (), (),..();
					translation.append(comma + " (");
					comma = ",\n";
					translation.append(queryResult.getString("T_Id_Nls") + ", ");
					translation.append(server.getMapper().mapToTarget(queryResult.getString(DbNlsString.ATTRIBUTE_LANGUAGE)) + ", ");
					translation.append(server.getMapper().mapToTarget(queryResult.getString(DbNlsString.ATTRIBUTE_COUNTRY)) + ", ");
					translation.append(server.getMapper().mapToTarget(queryResult.getString(DbNlsString.ATTRIBUTE_NLS_TEXT)) + ", ");
					// TODO missing DateTime transformation
					translation.append(server.getMapper().mapToTarget(queryResult.getString("T_CreateDate")) + ", ");
					translation.append(server.getMapper().mapToTarget(queryResult.getString("T_LastChange")) + ", ");
					translation.append((String) server.getMapper().mapToTarget(queryResult.getString("T_User")));
					translation.append(")");
				}
				query.closeAll();
				if (translation.length() > 0) {
					translation.append(";");
					inserts.add(translation.toString());
				}
			}

		} catch (Exception e) {
			exception = e;
		}
		server.closeTransactionThread(exception, trans);
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * INSERT INTO T_MAP_<code>...
	 *
	 * @param ownerId
	 * @param tableMap "T_MAP_<code>" name
	 * @return
	 */
	private void createInsertStatementMAP(java.util.List inserts, Long ownerId, String ownerType, String ownerAttribute, String tableMap) throws Exception {
		Exception exception = null;
		DbTransaction trans = null;
		try {
			DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, tableMap);
			builder.setTableList(tableMap);
			// TODO use constants from DbMapper
			builder.addFilter("T_Id_Owner", ownerId);
			builder.addFilter("T_Type_Owner", ownerType, DbQueryBuilder.STRICT);
			builder.addFilter("T_Attribute_Owner", ownerAttribute, DbQueryBuilder.STRICT);

			// TODO use DbQueryBuilder for creating INSERT
			StringBuffer sqlDml = new StringBuffer();
			String values = ownerId + ", " + server.getMapper().mapToTarget(ownerType) + ", " + server.getMapper().mapToTarget(ownerAttribute) + ", ";

			trans = server.openTransactionThread(false);
			DbQuery query = new DbQuery(trans, builder);
			java.sql.ResultSet queryResult = (java.sql.ResultSet) query.execute();
			String comma = "INSERT INTO " + tableMap + " (T_Id_Owner, T_Type_Owner, T_Attribute_Owner, T_Id_Value, T_CreateDate, T_LastChange, T_User) VALUES"; // =>
			// 1
			// INSERT
			// with
			// n
			// VALUES
			// String's
			while (server.getMapper().hasNext(queryResult)) {
				sqlDml.append(comma + " (");
				comma = ",\n";

				sqlDml.append(values);
				sqlDml.append(queryResult.getString("T_Id_Value") + ", ");
				// TODO missing DateTime transformation
				sqlDml.append(server.getMapper().mapToTarget(queryResult.getString("T_CreateDate")) + ", ");
				sqlDml.append(server.getMapper().mapToTarget(queryResult.getString("T_LastChange")) + ", ");
				sqlDml.append((String) server.getMapper().mapToTarget(queryResult.getString("T_User")));
				sqlDml.append(")");
			}
			query.closeAll();
			if (sqlDml.length() > 0) {
				sqlDml.append(";");
				inserts.add(sqlDml.toString());
			}
		} catch (Exception e) {
			exception = e;
		}
		server.closeTransactionThread(exception, trans);
		if (exception != null) {
			throw exception;
		}
	}
}
