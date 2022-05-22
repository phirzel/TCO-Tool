package ch.softenvironment.jomm.target.sql.ms_sqlserver;

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
import ch.softenvironment.util.UserException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Non-Standard-SQL Mappings for MS SQL Server 2005.
 * <p>
 * Design Pattern: <b>Adapter</b>
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class SQLServerMapper extends ch.softenvironment.jomm.target.sql.SqlMapper {

	/**
	 * Ms SQL Server Mapper constructor comment.
	 */
	protected SQLServerMapper() {
		super();
	}

	/**
	 * Analog super#getNewId() but with additional Lock: - HOLDLOCK hint will instruct SQL Server to hold the lock until you commit the transaction. - ROWLOCK hint will lock only this record and not
	 * issue a page or table lock. (The lock will also be released if you close your connection or it times out.)
	 * <p>
	 * Concepts in MS SQL Server to treat ID's: - "identity" column (equivalent to the "AutoNumber" columns in Access) => SELECT @@identity after inserting a row - Uniqueidentifiers resp. GUIDs.
	 * (Globally Unique IDentifier guaranteed to always return a unique value across space and time) => call the NEWID()
	 */
	@Override
	public synchronized Long getNewId(javax.jdo.PersistenceManager objectServer, javax.jdo.Transaction transaction, final String key) {
		// TODO refactor copied code: currently analog super#getNewId() but with
		// additional "LOCK" in 1)
		// TODO //UPDATE keyTable " WITH (HOLDLOCK, ROWLOCK)" SET
		// T_LastUniqueId=(SELECT T_LastUniqueId FROM T_Key_Object WHERE
		// T_Key=key)+1 WHERE T_Key=key;SELECT T_LastUniqueId FROM T_Key_Object
		// WHERE T_Key=key

		String keyTable = DbMapper.ATTRIBUTE_KEY_TABLE + "_Object"; // T_Key_Object
		String keyAttribute = DbMapper.ATTRIBUTE_KEY_TABLE;
		String lastId = "T_LastUniqueId";
		long id = -1;

		// 1) Determine latest ID
		DbQueryBuilder builder = ((DbObjectServer) objectServer).createQueryBuilder(DbQueryBuilder.SELECT, "SEQUENCE/UNIQUE ID");
		builder.setAttributeList(lastId);
		builder.setTableList(keyTable + " WITH (HOLDLOCK, ROWLOCK)");
		builder.addFilter(keyAttribute, key, DbQueryBuilder.STRICT);
		DbQuery query = new DbQuery((DbTransaction) transaction, builder);
		ResultSet queryResult = (ResultSet) query.execute();
		if (!((DbObjectServer) objectServer).getMapper().hasNext(queryResult)) {
			query.closeAll();
			throw new UserException("Es kann keine Identitaet fuer ein neues Objekt <" + key + "> geloest werden.", "Allozierungs-Fehler");
		}

		// 2) Create Sequence (new ID)
		try {
			id = (queryResult.getLong(1)) + (long) 1;
			query.closeAll();
		} catch (SQLException e) {
			throw new UserException("Es kann keine Identitaet fuer ein neues Objekt <" + key + "> geloest werden.", "Allozierungs-Fehler");
		}
		builder = ((DbObjectServer) objectServer).createQueryBuilder(DbQueryBuilder.UPDATE, "SEQUENCE/UNIQUE ID");
		builder.setTableList(keyTable /* + " WITH (ROWLOCK)" */);
		builder.append(lastId, Long.valueOf(id));
		builder.appendInternal((String) null);
		builder.addFilter(keyAttribute, key, DbQueryBuilder.STRICT);
		query = new DbQuery((DbTransaction) transaction, builder);
		query.execute();

		return Long.valueOf(id);
	}
}
