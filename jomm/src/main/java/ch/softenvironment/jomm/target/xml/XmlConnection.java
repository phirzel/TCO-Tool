package ch.softenvironment.jomm.target.xml;

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

import lombok.extern.slf4j.Slf4j;

/**
 * @author Peter Hirzel
 */
@Slf4j
public class XmlConnection extends ch.softenvironment.jomm.DbConnection {

    /**
     * XmlConnection constructor comment.
     *
     * @param objectServer ch.softenvironment.jomm.DbObjectServer
     */
    public XmlConnection(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Close this database-connection.
     */
    @Override
    public void close() throws javax.jdo.JDOException {
        if (getObjectServer().getPersistenceManagerFactory().getConnectionURL() != null) {
            pop(getObjectServer().getPersistenceManagerFactory().getConnectionURL());
            try {
                getJdbcConnection().close();
            } catch (java.sql.SQLException e) {
                throw new javax.jdo.JDOException("XmlConnection#close() failed", e);
            }
        }
        log.info("Database Connection closed for URL: {}", getObjectServer().getPersistenceManagerFactory().getConnectionURL());
    }

    /**
     * Open a database connection.
     *
     * @param userId
     * @param password
     */
    @Override
    public void open(String userId, String password) throws javax.jdo.JDOException {
        setJdbcConnection(new XmlJdbcConnection());
        log.info("Backend Connection opened for URL: {}", getObjectServer().getPersistenceManagerFactory().getConnectionURL());

        push(this);
    }
}
