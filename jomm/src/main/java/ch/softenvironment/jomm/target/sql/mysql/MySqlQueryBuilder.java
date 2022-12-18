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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.target.sql.SqlQueryBuilder;

/**
 * MySQL specific Query builder.
 *
 * @author Peter Hirzel
 */
public class MySqlQueryBuilder extends SqlQueryBuilder {

    public MySqlQueryBuilder(DbObjectServer objectServer, final Integer queryType, String useCase) {
        super(objectServer, queryType, useCase);
    }

    @Override
    public String getQuery() {
        String query = super.getQuery();

        if ((getType() == SELECT) && (getFetchBlockSize() > 0)) {
            query = query + " LIMIT " + getFetchBlockSize();
        }
        return query;
    }
}
