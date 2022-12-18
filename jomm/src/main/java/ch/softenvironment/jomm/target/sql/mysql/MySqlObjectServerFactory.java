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

import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbObjectServer;
import java.util.Collection;
import java.util.Set;
import javax.jdo.FetchGroup;
import javax.jdo.PersistenceManager;
import javax.jdo.datastore.DataStoreCache;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.metadata.JDOMetadata;
import javax.jdo.metadata.TypeMetadata;

/**
 * JDO-Implementation of a MySQL V4.1 Factory.
 *
 * @author Peter Hirzel
 */

public class MySqlObjectServerFactory extends ch.softenvironment.jomm.DbDomainNameServer {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * Initiated via:
     *
     * @see DbDomainNameServer#createInstance(String)
     */
    public MySqlObjectServerFactory() {
        // TODO make protected
        super();

        setConnectionDriverName(JDBC_DRIVER);
    }

    /**
     * In MySQL for schemas of type InnoDB the default Transaction Isolation Level is REPEATABLE READ.
     */
    @Override
    protected javax.jdo.PersistenceManager createPersistenceManager() {
        final DbObjectServer objectServer;
        try {
            /*
             * Don't use autoReconnect=true, it's going away eventually and it's
             * a crutch for older connection pools that couldn't testsuite
             * connections.
             */
            objectServer = new ch.softenvironment.jomm.target.sql.SqlObjectServer(this, connectionPassword, new MySqlMapper(), MySqlQueryBuilder.class,
                ch.softenvironment.jomm.target.sql.SqlConnection.class);
        } catch (Exception e) {
            throw new ch.softenvironment.util.DeveloperException(e.getLocalizedMessage());
        }
        return objectServer;
    }

    @Override
    public boolean isClosed() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public PersistenceManager getPersistenceManagerProxy() {
        //TODO HIP just added to compile
        return null;
    }

    @Deprecated
    @Override
    public java.lang.String getConnectionURL() {
        //TODO HIP just added to compile
        // old MySQL JDBC-Driver needed a suffix for user & password for URL
        return super.getConnectionURL(); /* "?user=<userId>&password=" */
    }

    @Override
    public void setMapping(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getMapping() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public boolean getDetachAllOnCommit() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public void setDetachAllOnCommit(boolean b) {
        //TODO HIP just added to compile
    }

    @Override
    public boolean getCopyOnAttach() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public void setCopyOnAttach(boolean b) {
        //TODO HIP just added to compile
    }

    @Override
    public void setName(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getName() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setPersistenceUnitName(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getPersistenceUnitName() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setServerTimeZoneID(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getServerTimeZoneID() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setTransactionType(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getTransactionType() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public boolean getReadOnly() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public void setReadOnly(boolean b) {
        //TODO HIP just added to compile
    }

    @Override
    public String getTransactionIsolationLevel() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setTransactionIsolationLevel(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public void setDatastoreReadTimeoutMillis(Integer integer) {
        //TODO HIP just added to compile
    }

    @Override
    public Integer getDatastoreReadTimeoutMillis() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setDatastoreWriteTimeoutMillis(Integer integer) {
        //TODO HIP just added to compile
    }

    @Override
    public Integer getDatastoreWriteTimeoutMillis() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public DataStoreCache getDataStoreCache() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void addInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener, Class[] classes) {
        //TODO HIP just added to compile
    }

    @Override
    public void removeInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener) {
        //TODO HIP just added to compile
    }

    @Override
    public void addFetchGroups(FetchGroup... fetchGroups) {
        //TODO HIP just added to compile
    }

    @Override
    public void removeFetchGroups(FetchGroup... fetchGroups) {
        //TODO HIP just added to compile
    }

    @Override
    public void removeAllFetchGroups() {
        //TODO HIP just added to compile
    }

    @Override
    public FetchGroup getFetchGroup(Class aClass, String s) {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public Set getFetchGroups() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void registerMetadata(JDOMetadata jdoMetadata) {
        //TODO HIP just added to compile
    }

    @Override
    public JDOMetadata newMetadata() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public TypeMetadata getMetadata(String s) {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public Collection<Class> getManagedClasses() {
        //TODO HIP just added to compile
        return null;
    }
}
