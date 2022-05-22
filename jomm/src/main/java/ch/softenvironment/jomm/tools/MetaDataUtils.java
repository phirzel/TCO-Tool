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

import ch.softenvironment.jomm.DbObjectServer;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL-Schema utilitiy.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.1 $ $Date: 2006-11-14 22:51:56 $
 */
public class MetaDataUtils {

    //    private DbObjectServer server = null;
    private DatabaseMetaData meta = null;

    public MetaDataUtils(DbObjectServer server) throws SQLException {
        //        this.server = server;
        meta = server.getMetaData();
    }

    /**
     * Return whether given tableName is contained in Schema.
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public boolean findTable(String tableName) throws SQLException {
        boolean exist = false;

        ResultSet rs = meta.getCatalogs();
        while (rs.next()) {
            String catalog = rs.getString(1);

            String[] types = {"TABLE"};  // null for all types
            ResultSet rsTables = meta.getTables(catalog, null, null, types);
            while (rsTables.next()) {
                //          String tableType = rsTables.getString("TABLE_TYPE");
                //          if (tableType.equals("TABLE")) {
                if (tableName.equals(rsTables.getString("TABLE_NAME"))) {
                    exist = true;
                    break;
                }
                //          }
            }
            rsTables.close();
        }
        rs.close();
        return exist;
    }
}