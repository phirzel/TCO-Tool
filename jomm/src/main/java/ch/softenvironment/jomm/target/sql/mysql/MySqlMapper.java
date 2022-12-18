package ch.softenvironment.jomm.target.sql.mysql;

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
import ch.softenvironment.util.NlsUtils;
import javax.jdo.Transaction;

/**
 * Mapper for MySQL V4.1 (or higher) Specialities.
 *
 * @author Peter Hirzel
 */
public class MySqlMapper extends ch.softenvironment.jomm.target.sql.SqlMapper {

	/**
	 * MySqlMapper constructor comment.
	 */
	protected MySqlMapper() {
		super();
	}

	/**
	 * An own Sequence table T_Key_Object is preferred above the AUTO_INCREMENT mechanism with LAST_INSERT_ID() function, because of recursive or aggregated Insertions of objects.
	 */
	@Override
	public synchronized Long getNewId(javax.jdo.PersistenceManager objectServer, javax.jdo.Transaction transaction, final String key) {
		String keyTable = DbMapper.ATTRIBUTE_KEY_TABLE + "_Object"; // T_Key_Object

		// boolean optimistic = transaction.getOptimistic();
		// transaction.setOptimistic(false);

		// TODO Lock row for given key in keyTable would be enough
		lock(transaction, keyTable);
		Long id = super.getNewId(objectServer, transaction, key);
		unlock(transaction, keyTable);
		// transaction.setOptimistic(optimistic); // restore
		return id; // null; if AUTO_INCREMENT would have been defined for Schema
	}

	/**
	 * Table-Lock.
	 */
	protected void lock(Transaction transaction, String tableList) {
		DbQueryBuilder builder = ((DbObjectServer) transaction.getPersistenceManager()).createQueryBuilder(DbQueryBuilder.LOCK, "Lock Table => Isolation");
		builder.setTableList(tableList);
		DbQuery query = new DbQuery((DbTransaction) transaction, builder);
		query.execute();
	}

	/**
	 * Table-UNLOCK.
	 */
	protected void unlock(Transaction transaction, String tableList) {
		DbQueryBuilder builder = ((DbObjectServer) transaction.getPersistenceManager()).createQueryBuilder(DbQueryBuilder.UNLOCK, "Unlock Table");
		builder.setTableList(tableList);
		DbQuery query = new DbQuery((DbTransaction) transaction, builder);
		query.execute();
	}

	@Override
	public Object mapToTarget(java.util.Date value, final int type) {
		if (value == null) {
			return super.mapToTarget(value, type);
		} else if (type == DbDateFieldDescriptor.TIME) {
			// MySQL converts 'HH:mm:ss' to "[yy]yy-MM-dd" => dangerous pitfall
			// (some TIME-values will convert to a valid date others won't)!
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(NlsUtils.TIME_24HOURS_PATTERN);
			return "'" + "0000-00-00 " /* special value for MySQL */ + sf.format(value) + "'";
		} else {
			return super.mapToTarget(value, type);
		}
	}
}
