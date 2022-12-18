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

import ch.softenvironment.jomm.target.sql.SqlConnection;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Peter Hirzel
 */
public final class HSQLDBConnection extends SqlConnection {

    /**
     * SqlConnection constructor comment.
     *
     * @param objectServer ch.softenvironment.jomm.DbObjectServer
     */
    public HSQLDBConnection(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    @Override
    protected void setConnection(String userId, String password) throws SQLException {
        super.setConnection(userId, password);

        // HSQLDB supports only READ_UNCOMMITTED
        getJdbcConnection().setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
    }
}
