package ch.softenvironment.jomm.target.sql.postgresql;
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
import ch.softenvironment.jomm.target.sql.SqlMapper;

/**
 * Specific Target-Mapper for PostgreSQL.
 *
 * @author Peter Hirzel
 */
public class PostgreSqlMapper extends SqlMapper {

    protected PostgreSqlMapper() {
        super();
    }

    /**
     * PostgreSQL provides SEQUENCE Table's.
     */
    @Override
    public Long getNewId(javax.jdo.PersistenceManager objectServer, javax.jdo.Transaction transaction, final String key) {
        DbQueryBuilder builder = ((DbObjectServer) objectServer).createQueryBuilder(DbQueryBuilder.RAW, "Create UNIQUE ID (PostgreSQL SEQUENCE)");
        builder.setRaw("SELECT nextval('" /*+<SCHEMA>.*/ + DbMapper.ATTRIBUTE_KEY_TABLE + "_" + key + "')");
        Number tmp = (Number) ((DbObjectServer) objectServer).getFirstValue(builder);
        return tmp.longValue();
    }
}
