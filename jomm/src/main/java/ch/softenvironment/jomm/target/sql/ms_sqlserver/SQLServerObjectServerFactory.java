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
 * JDO-Implementation of a Microsoft SQL Server 2005 Factory.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see javax.jdo.PersistencManagerFactory
 */
@SuppressWarnings("serial")
public class SQLServerObjectServerFactory extends ch.softenvironment.jomm.DbDomainNameServer {

    /**
     * Initiated via:
     *
     * @see DbDomainNameServer#createInstance(String)
     */
    public SQLServerObjectServerFactory() {
        // TODO make protected
        super();

        // original Microsoft SQL Server JDBC Driver
        setConnectionDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // alternative jdts open source JDBC Driver
        // setConnectionDriverName("net.sourceforge.jtds.jdbc.Driver");
    }

    @Override
    protected javax.jdo.PersistenceManager createPersistenceManager() {
        DbObjectServer objectServer = null;
        try {
            objectServer = new ch.softenvironment.jomm.target.sql.SqlObjectServer(this, connectionPassword, new SQLServerMapper(),
                ch.softenvironment.jomm.target.sql.SqlQueryBuilder.class, ch.softenvironment.jomm.target.sql.SqlConnection.class);
            // Date/Time => ISO 8601 Standard
            // objectServer.execute("Set session Date-Format <'YYYY-MM-DD'>",
            // ListUtils.createList("SET datestyle TO ISO"));
        } catch (Exception e) {
            throw new ch.softenvironment.util.DeveloperException(e.getLocalizedMessage());
        }
        return objectServer;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public PersistenceManager getPersistenceManagerProxy() {
        return null;
    }

    @Override
    public void setMapping(String s) {

    }

    @Override
    public String getMapping() {
        return null;
    }

    @Override
    public boolean getDetachAllOnCommit() {
        return false;
    }

    @Override
    public void setDetachAllOnCommit(boolean b) {

    }

    @Override
    public boolean getCopyOnAttach() {
        return false;
    }

    @Override
    public void setCopyOnAttach(boolean b) {

    }

    @Override
    public void setName(String s) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setPersistenceUnitName(String s) {

    }

    @Override
    public String getPersistenceUnitName() {
        return null;
    }

    @Override
    public void setServerTimeZoneID(String s) {

    }

    @Override
    public String getServerTimeZoneID() {
        return null;
    }

    @Override
    public void setTransactionType(String s) {

    }

    @Override
    public String getTransactionType() {
        return null;
    }

    @Override
    public boolean getReadOnly() {
        return false;
    }

    @Override
    public void setReadOnly(boolean b) {

    }

    @Override
    public String getTransactionIsolationLevel() {
        return null;
    }

    @Override
    public void setTransactionIsolationLevel(String s) {

    }

    @Override
    public void setDatastoreReadTimeoutMillis(Integer integer) {

    }

    @Override
    public Integer getDatastoreReadTimeoutMillis() {
        return null;
    }

    @Override
    public void setDatastoreWriteTimeoutMillis(Integer integer) {

    }

    @Override
    public Integer getDatastoreWriteTimeoutMillis() {
        return null;
    }

    @Override
    public DataStoreCache getDataStoreCache() {
        return null;
    }

    @Override
    public void addInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener, Class[] classes) {

    }

    @Override
    public void removeInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener) {

    }

    @Override
    public void addFetchGroups(FetchGroup... fetchGroups) {

    }

    @Override
    public void removeFetchGroups(FetchGroup... fetchGroups) {

    }

    @Override
    public void removeAllFetchGroups() {

    }

    @Override
    public FetchGroup getFetchGroup(Class aClass, String s) {
        return null;
    }

    @Override
    public Set getFetchGroups() {
        return null;
    }

    @Override
    public void registerMetadata(JDOMetadata jdoMetadata) {

    }

    @Override
    public JDOMetadata newMetadata() {
        return null;
    }

    @Override
    public TypeMetadata getMetadata(String s) {
        return null;
    }

    @Override
    public Collection<Class> getManagedClasses() {
        return null;
    }
}
