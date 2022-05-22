package ch.softenvironment.jomm.target.sql.oracle;

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
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldType;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper for ORACLE 9i Specialities.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class OracleMapper extends ch.softenvironment.jomm.target.sql.SqlMapper {

	/**
	 * OracleMapper constructor.
	 */
	protected OracleMapper() {
		super();
	}

	@Override
	public java.lang.String describeTargetException(java.lang.Exception exception) {
		if (exception instanceof java.sql.SQLException) {
			java.sql.SQLException sqlEx = (java.sql.SQLException) exception;

			if (sqlEx.getSQLState() != null) {
				if (sqlEx.getSQLState().equals("ORA-01034" /* $NON-NLS$ */)) {
					return "ORACLE DBMS-Server scheint nicht verfuegbar.";
				}
			}
			return null;
		} else {
			return super.describeTargetException(exception);
		}
	}

	/**
	 * ORACLE provides SEQUENCE Table's.
	 */
	@Override
	public Long getNewId(javax.jdo.PersistenceManager objectServer, javax.jdo.Transaction transaction, final String key) {
		DbQueryBuilder builder = ((DbObjectServer) objectServer).createQueryBuilder(DbQueryBuilder.SELECT, "Create UNIQUE ID (ORACLE SEQUENCE)");
		builder.setAttributeList(DbMapper.ATTRIBUTE_KEY_TABLE + "_" + key + ".nextval");
		builder.setTableList("DUAL");

		DbQuery query = new DbQuery((DbTransaction) transaction, builder);
		ResultSet queryResult = (ResultSet) query.execute();
		Long id = null;
		if (((DbObjectServer) objectServer).getMapper().hasNext(queryResult)) {
			try {
				id = Long.valueOf(queryResult.getLong(1));
				query.closeAll();
			} catch (SQLException e) {
				// TODO NLS
				throw new ch.softenvironment.util.UserException("Es konnte keine neue Identitaet fuer ein neues Objekt mit Schluessel <" + key
					+ "> geloest werden!", "ORACLE Sequence-Fehler");
			}
		} else {
			query.closeAll();
			throw new ch.softenvironment.util.UserException("Es konnte keine neue Identitaet fuer ein neues Objekt mit Schluessel <" + key
				+ "> geloest werden!", "ORACLE Sequence-Fehler");
		}

		return id;
	}

	@Override
	public DbFieldType mapFromTargetDbFieldType(java.lang.Object collection, java.lang.String attribute) {
		try {
			Object value = ((ResultSet) collection).getObject(attribute);
			if (value instanceof ch.softenvironment.jomm.datatypes.interlis.IliSurface) {
				return null;
			} else if (value instanceof ch.softenvironment.jomm.datatypes.interlis.IliArea) {
				return null;
			} else if (value instanceof ch.softenvironment.jomm.datatypes.interlis.IliPolyline) {
				return null;
			} else {
				// TODO
				throw new ch.softenvironment.util.DeveloperException("nyi");
			}
		} catch (SQLException e) {
			throw new ch.softenvironment.util.DeveloperException("illegal mapping", null, e);
		}
	}

	@Override
	public java.lang.Object mapToTarget(ch.softenvironment.jomm.descriptor.DbFieldType value) {
		if (value instanceof ch.softenvironment.jomm.datatypes.interlis.IliSurface) {
			return null;
		} else if (value instanceof ch.softenvironment.jomm.datatypes.interlis.IliArea) {
			return null;
		} else if (value instanceof ch.softenvironment.jomm.datatypes.interlis.IliPolyline) {
			return null;
		} else {
			// TODO
			throw new ch.softenvironment.util.DeveloperException("nyi");
		}
	}

	@Override
	public Object mapToTarget(java.util.Date value, final int type) {
		if (value == null) {
			return super.mapToTarget(value, type);
		} else {
			switch (type) {
				case DbDateFieldDescriptor.DATE: {
					java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(DATE_SQL_FORMAT);
					String date = "'" + sf.format(value) + "'";
					return "TO_DATE(" + date + ", 'YYYY-MM-DD')";
				}
				case DbDateFieldDescriptor.DATETIME: {
					java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(TIMESTAMP_SQL_FORMAT);
					String ts = "'" + sf.format(value) + "'";
					return "TO_TIMESTAMP(" + ts + ", 'YYYY-MM-DD HH24:MI:SS')";
				}
				default: {
					return super.mapToTarget(value, type);
				}
			}
		}
	}
}
