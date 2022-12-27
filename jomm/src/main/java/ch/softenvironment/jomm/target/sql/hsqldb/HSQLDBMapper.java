package ch.softenvironment.jomm.target.sql.hsqldb;
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
 * Specific Target-Mapper for HSQLDB V1.8.2.
 *
 * @author Peter Hirzel
 */
public class HSQLDBMapper extends SqlMapper {

    protected HSQLDBMapper() {
        super();
    }

    /**
     * HSQLDB provides SEQUENCE's. (The IDENTITY type/function is not used here, instead separate SEQUENCE's are provided to keep compatibility to ORACLE and PostgreSQL within JOMM.)
     */
    @Override
    public Long getNewId(javax.jdo.PersistenceManager objectServer, javax.jdo.Transaction transaction, final String key) {
        DbQueryBuilder builder = ((DbObjectServer) objectServer).createQueryBuilder(DbQueryBuilder.RAW, "Create UNIQUE ID (HSQLDB SEQUENCE)");
        //TODO seems buggy with newest H2 -> upgrade ID sequence @see sql/NLS_Schema_HSQLDB.sql => introducing PATCH with pseudo-"DUAL"-table
        String patchTable = "DUAL" /*like ORACLE's implementation*/; // correct should be  SYSTEM_SEQUENCES here!
        builder.setRaw("SELECT NEXT VALUE FOR " + /* public. */ DbMapper.ATTRIBUTE_KEY_TABLE + "_" + key + " FROM " + patchTable);
        Number tmp = (Number) ((DbObjectServer) objectServer).getFirstValue(builder);
        return tmp.longValue();
    }
}
