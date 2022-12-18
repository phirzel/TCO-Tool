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

import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Peter Hirzel
 */
@Slf4j
public class SqlConnection extends ch.softenvironment.jomm.DbConnection {

    private String password = null;

    /**
     * SqlConnection constructor comment.
     *
     * @param objectServer ch.softenvironment.jomm.DbObjectServer
     */
    public SqlConnection(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Close this database-connection.
     */
    @Override
    public synchronized void close() throws javax.jdo.JDOException {
        try {
            if (getObjectServer().getPersistenceManagerFactory().getConnectionURL() != null) {
                pop(getObjectServer().getPersistenceManagerFactory().getConnectionURL());
                getJdbcConnection().close();
            }
            log.info("Database Connection closed for URL: {}", getObjectServer().getPersistenceManagerFactory().getConnectionURL());
        } catch (SQLException e) {
            log.error("closing", e);
            throw new javax.jdo.JDOFatalException("SqlConnection.close()", e);
        }
    }

    /**
     * Open a database connection.
     *
     * @param userId
     * @param password
     */
    @Override
    public synchronized void open(String userId, String password) throws javax.jdo.JDOException {
        try {
            //Driver driver = (Driver)Class.forName(driver).newInstance();
            Class.forName(getObjectServer().getPersistenceManagerFactory().getConnectionDriverName());
        } catch (ClassNotFoundException e) {
            throw new ch.softenvironment.util.DeveloperException("Driver <" + getObjectServer().getPersistenceManagerFactory().getConnectionDriverName() + "> not found", null, e);
        }

        try {
            this.password = password; /* NASTY: needed for reconnect */

            setConnection(userId, password);
            log.info("Database Connection opened for URL: {}", getObjectServer().getPersistenceManagerFactory().getConnectionURL());
/*		
		if (getJdbcConnection() instanceof sun.jdbc.odbc.JdbcOdbcConnection) {
			Tracer.getInstance().logBackendCommand("ODBC-Version = " + ((sun.jdbc.odbc.JdbcOdbcConnection)getJdbcConnection()).getODBCVer());
		}
*/
            push(this);
        } catch (SQLException e) {
            log.error("cannot connect to Target-System by url: {}", getObjectServer().getPersistenceManagerFactory().getConnectionURL(), e);
            throw new javax.jdo.JDOFatalException("SqlConnection.open()", e);
        }
    }

    /**
     * Reconnect Target-System.
     */
    protected synchronized void reconnect() throws SQLException {
        try {
            getJdbcConnection().close();
        } catch (Exception e) {
            //ignore connection might be dead anyway
            log.warn("could not close JDBC-connection at reconnecting", e);
        }
        setConnection(getObjectServer().getUserId(), password /*NASTY*/);
    }

    /**
     * Open a Target-System connection.
     *
     * @param userId
     * @param password
     */
    protected void setConnection(String userId, String password) throws SQLException {
        setJdbcConnection(DriverManager.getConnection(getObjectServer().getPersistenceManagerFactory().getConnectionURL(),
            userId,
            password));

        // set the default
        getJdbcConnection().setAutoCommit(getObjectServer().getPersistenceManagerFactory().getNontransactionalWrite());
    }
}
