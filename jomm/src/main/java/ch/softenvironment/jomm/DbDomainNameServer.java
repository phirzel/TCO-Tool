package ch.softenvironment.jomm;

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

import ch.softenvironment.jomm.target.sql.hsqldb.HSQLDBObjectServerFactory;
import ch.softenvironment.jomm.target.sql.msaccess.MsAccessObjectServerFactory;
import ch.softenvironment.jomm.target.sql.mssqlserver.SQLServerObjectServerFactory;
import ch.softenvironment.jomm.target.sql.mysql.MySqlObjectServerFactory;
import ch.softenvironment.jomm.target.sql.oracle.OracleObjectServerFactory;
import ch.softenvironment.jomm.target.sql.postgresql.PostgreSqlObjectServerFactory;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Statistic;
import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Distributed Name Service. Several DbObjectServer's may be managed by DbDomainNameServer.
 *
 * @author Peter Hirzel
 * @see javax.jdo.PersistenceManagerFactory
 */

@Slf4j
public abstract class DbDomainNameServer implements javax.jdo.PersistenceManagerFactory {

    private static final String VERSION = "V1.3.0";
    private static final java.util.Map<String, javax.jdo.PersistenceManager> locations = new java.util.HashMap<>();

    private PersistenceManager manager = null;
    private String connectionDriverName = null;
    private String connectionURL = null;
    private String connectionUserName = null;
    protected String connectionPassword = null;

    private final Object connectionFactory = null;
    private final Object connectionFactory2 = null;
    private final String connectionFactoryName = null;
    private String connectionFactory2Name = null;

    private boolean optimistic = true;
    private final boolean ignoreCache = false;
    private boolean multithreaded = false;

    private final boolean restoreValues = true;
    private final boolean retainValues = false;

    private boolean nonTransactionalRead = false;
    private boolean nonTransactionalWrite = false;

    /**
     * Create proper PersistencyManager according to URL-Name.
     *
     * @param url
     * @return
     */
    public static PersistenceManagerFactory createInstance(final String url) {
        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        PersistenceManagerFactory factory;
        // TODO Nasty to use Class-Refs of sub-packages here
        if (url.startsWith("jdbc:hsqldb:")) {
            factory = new HSQLDBObjectServerFactory();
        } else if (url.startsWith("jdbc:odbc:")) {
            factory = new MsAccessObjectServerFactory();
        } else if (url.startsWith("jdbc:mysql:")) {
            factory = new MySqlObjectServerFactory();
        } else if (url.startsWith("jdbc:oracle:")) {
            factory = new OracleObjectServerFactory();
        } else if (url.startsWith("jdbc:postgresql:")) {
            factory = new PostgreSqlObjectServerFactory();
        } else if (url.startsWith("jdbc:sqlserver:")) {
            factory = new SQLServerObjectServerFactory();
        } else {
            throw new IllegalArgumentException("Target-Driver not supported yet!");
        }

        factory.setConnectionURL(url);
        return factory;
    }

    /**
     * Return a sample-URL list of connection-String's for connecting a target-server.
     *
     * @return
     */
    public static java.util.List<String> createSampleConnections() {
        java.util.List<String> urls = new java.util.ArrayList<>();
        urls.add("jdbc:mysql://localhost:3306/<optional Database>");
        urls.add("jdbc:postgresql://localhost:5432/<Database>");
        urls.add("jdbc:hsqldb:mem:<Database>");
        urls.add("jdbc:oracle:thin:@localhost:1521:orcl");
        urls.add("jdbc:sqlserver://localhost\\SQLEXPRESS;databasename=<Database>");
        urls.add("jdbc:odbc:<ODBC-Source>");
        return urls;
    }

    /**
     * Constructor.
     */
    protected DbDomainNameServer() {
        super();
    }

    /**
     * Close this PersistenceManagerFactory. Check for JDOPermission("closePersistenceManagerFactory") and if not authorized, throw SecurityException.
     * <p>
     * If the authorization check succeeds, check to see that all PersistenceManager instances obtained from this PersistenceManagerFactory have no active transactions. If any PersistenceManager
     * instances have an active transaction, throw a JDOUserException, with one nested JDOUserException for each PersistenceManager with an active Transaction.
     * <p>
     * If there are no active transactions, then close all PersistenceManager instances obtained from this PersistenceManagerFactory, mark this PersistenceManagerFactory as closed, disallow
     * getPersistenceManager methods, and allow all other get methods. If a set method or getPersistenceManager method is called after close, then JDOUserException is thrown.
     */
    @Override
    public final void close() {
        try {
            java.io.StringWriter stringWriter = new java.io.StringWriter();
            java.io.PrintWriter writer = new java.io.PrintWriter(stringWriter);
            Statistic.dump(writer);
            Statistic.clearAll();
            log.info("DBMS-Statistics\n{}", stringWriter);
        } catch (Exception e) {
            log.error("Statistic-dump failed", e);
        }

        unregister(manager);
        connectionURL = null;
        manager = null;
    }

    /**
     * Create an ObjectServer. Overwrite for any specific DBMS.
     */
    protected abstract javax.jdo.PersistenceManager createPersistenceManager();

    /**
     * Get the driver name for the data store connection.
     *
     * @return the driver name for the data store connection.
     */
    @Override
    public java.lang.String getConnectionDriverName() {
        return connectionDriverName;
    }

    /**
     * Get the data store connection factory.
     *
     * @return the data store connection factory.
     */
    @Override
    public java.lang.Object getConnectionFactory() {
        // Factory for DbConnection ???
        return connectionFactory;
    }

    /**
     * Get the second data store connection factory. This is needed for managed environments to get non-transactional connections for optimistic transactions.
     *
     * @return the data store connection factory.
     */
    @Override
    public java.lang.Object getConnectionFactory2() {
        return connectionFactory2;
    }

    /**
     * Get the name for the second data store connection factory. This is needed for managed environments to get non-transactional connections for optimistic transactions.
     *
     * @return the name of the data store connection factory.
     */
    @Override
    public java.lang.String getConnectionFactory2Name() {
        return connectionFactory2Name;
    }

    /**
     * Get the name for the data store connection factory.
     *
     * @return the name of the data store connection factory.
     */
    @Override
    public java.lang.String getConnectionFactoryName() {
        return connectionFactoryName;
    }

    /**
     * Get the URL for the data store connection.
     *
     * @return the URL for the data store connection.
     */
    @Override
    public java.lang.String getConnectionURL() {
        return connectionURL;
    }

    /**
     * Get the user name for the data store connection.
     *
     * @return the user name for the data store connection.
     */
    @Override
    public java.lang.String getConnectionUserName() {
        return connectionUserName;
    }

    /**
     * Get the default IgnoreCache setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @return the default IngoreCache setting.
     */
    @Override
    public boolean getIgnoreCache() {
        return ignoreCache;
    }

    /**
     * Get the default multi-threaded setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @return the default multi-threaded setting.
     */
    @Override
    public boolean getMultithreaded() {
        return multithreaded;
    }

    /**
     * Get the default NontransactionalRead setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @return the default NontransactionalRead setting.
     */
    @Override
    public boolean getNontransactionalRead() {
        return nonTransactionalRead;
    }

    /**
     * Get the default NontransactionalWrite setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @return the default NontransactionalWrite setting.
     */
    @Override
    public boolean getNontransactionalWrite() {
        return nonTransactionalWrite;
    }

    /**
     * Get the default Optimistic setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @return the default Optimistic setting.
     */
    @Override
    public boolean getOptimistic() {
        return optimistic;
    }

    /**
     * Get an instance of <code>PersistenceManager</code> from this factory. The instance has default values for options.
     *
     * <p>
     * After the first use of <code>getPersistenceManager</code>, no "set" methods will succeed.
     *
     * @return a <code>PersistenceManager</code> instance with default options.
     */
    @Override
    public javax.jdo.PersistenceManager getPersistenceManager() {
        return manager; // getServer(urlAlias /*getConnectionURL()*/);
    }

    /**
     * @see #getPersistenceManager(String, String, String)
     */
    @Override
    public javax.jdo.PersistenceManager getPersistenceManager(final String userid, final String password) {
        return getPersistenceManager(userid, password, getConnectionURL());
    }

    /**
     * Get an instance of <code>PersistenceManager</code> from this factory. The instance has default values for options. The parameters
     * <code>userid</code> and <code>password</code> are used when obtaining
     * datastore connections from the connection pool.
     *
     * <p>
     * After the first use of <code>getPersistenceManager</code>, no "set" methods will succeed.
     *
     * @param userid the userid for the connection
     * @param password the password for the connection
     * @param alias to store this server
     * @return a <code>PersistenceManager</code> instance with default options.
     */
    public javax.jdo.PersistenceManager getPersistenceManager(final String userid, final String password, final String alias) {
        setConnectionUserName(userid);
        setConnectionPassword(password);

        manager = createPersistenceManager();
        register(alias, manager);

        return getPersistenceManager();
    }

    /**
     * Return non-configurable properties of this
     * <code>PersistenceManagerFactory</code>. Properties with keys
     * <code>VendorName</code> and <code>VersionNumber</code> are required.
     * Other keys are optional.
     *
     * @return the non-configurable properties of this
     *     <code>PersistenceManagerFactory</code>.
     */
    @Override
    public java.util.Properties getProperties() {
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty("VendorName", "www.softenvironment.ch");
        properties.setProperty("VersionNumber", VERSION);

        return properties;
    }

    /**
     * Get the default value for the RestoreValues property.
     *
     * @return the value of the restoreValues property
     */
    @Override
    public boolean getRestoreValues() {
        return restoreValues;
    }

    /**
     * Get the default RetainValues setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @return the default RetainValues setting.
     */
    @Override
    public boolean getRetainValues() {
        return retainValues;
    }

    /**
     * Return the ObjectServer for a given Location specified by URL.
     */
    public static DbObjectServer getServer(final String dbURL) {
        if (locations.containsKey(dbURL)) {
            return (DbObjectServer) locations.get(dbURL);
        } else {
            log.error("URL not contained for registered servers: {}", dbURL);
            return null;
        }
    }

    /**
     * Overwrite this method in case Target-System needs to reconnect from time to time for Performance- or Session-Reasons.
     *
     * @see DbObjectServer#checkConnection()
     */
    @Deprecated
    public void needsReconnect() {
        // do nothing by default
    }

    /**
     * Register a specific ObjectServer to this DomainNameServer.
     */
    protected static void register(final String alias, javax.jdo.PersistenceManager objectServer) {
        if ((alias == null) || (objectServer == null) || (getServer(alias) != null)) {
            throw new DeveloperException("PersistenceManager for URL-alias=" + alias + " already registered!");
        } else {
            locations.put(alias, objectServer);
            log.info("PersistenceManager[DbObjectServer@" + alias + "] created");
        }
    }

    /**
     * Set the driver name for the data store connection.
     *
     * @param driverName the driver name for the data store connection.
     */
    @Override
    public void setConnectionDriverName(final String driverName) {
        if (getPersistenceManager() != null) {
            throw new DeveloperException("Not allowed after first instantiation");
        }
        this.connectionDriverName = driverName;
    }

    /**
     * Set the data store connection factory. JDO implementations will support specific connection factories. The connection factory interfaces are not part of the JDO specification.
     *
     * @param connectionFactory the data store connection factory.
     */
    @Override
    public void setConnectionFactory(java.lang.Object connectionFactory) {
        throw new JDOUnsupportedOptionException("ConnectionFactory");
    }

    /**
     * Set the second data store connection factory. This is needed for managed environments to get non-transactional connections for optimistic transactions. JDO implementations will support specific
     * connection factories. The connection factory interfaces are not part of the JDO specification.
     *
     * @param connectionFactory the data store connection factory.
     */
    @Override
    public void setConnectionFactory2(java.lang.Object connectionFactory) {
        throw new JDOUnsupportedOptionException("ConnectionFactory2");
    }

    /**
     * Set the name for the second data store connection factory. This is needed for managed environments to get non-transactional connections for optimistic transactions.
     *
     * @param connectionFactoryName the name of the data store connection factory.
     */
    @Override
    public void setConnectionFactory2Name(final String connectionFactoryName) {
        if (getPersistenceManager() != null) {
            throw new DeveloperException("Not allowed after first instantiation");
        }
        this.connectionFactory2Name = connectionFactoryName;
    }

    /**
     * Set the name for the data store connection factory.
     *
     * @param connectionFactoryName the name of the data store connection factory.
     */
    @Override
    public void setConnectionFactoryName(final String connectionFactoryName) {
        throw new JDOUnsupportedOptionException("ConnectionFactoryName");
    }

    /**
     * Set the password for the data store connection.
     *
     * @param password the password for the data store connection.
     */
    @Override
    public void setConnectionPassword(final String password) {
        if (getPersistenceManager() != null) {
            throw new DeveloperException("Not allowed after first instantiation");
        }
        // TODO NYI: not yet ciphered!
        this.connectionPassword = password;
    }

    /**
     * Set the URL for the data store connection.
     *
     * @param url the URL for the data store connection.
     */
    @Override
    public void setConnectionURL(final String url) {
        if (this.connectionURL == null) {
            this.connectionURL = url;
        } else if (!this.connectionURL.equals(url)) {
            throw new DeveloperException("ConnectionURL must not be reset!");
        }
    }

    /**
     * Set the user name for the data store connection.
     *
     * @param userName the user name for the data store connection.
     */
    @Override
    public void setConnectionUserName(final String userName) {
        if (getPersistenceManager() != null) {
            throw new DeveloperException("Not allowed after first instantiation");
        }
        this.connectionUserName = userName;
    }

    /**
     * Set the default IgnoreCache setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @param flag the default IgnoreCache setting.
     */
    @Override
    public void setIgnoreCache(boolean flag) {
        throw new JDOUnsupportedOptionException("IngoreCache");
    }

    /**
     * Set the default multi-threaded setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @param flag the default multi-threaded setting.
     */
    @Override
    public void setMultithreaded(boolean flag) {
        if (flag) {
            // TODO NYI
            throw new JDOUnsupportedOptionException("Multithreading not supported yet");
        }
        multithreaded = flag;
    }

    /**
     * Set the default NontransactionalRead setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @param flag the default NontransactionalRead setting.
     */
    @Override
    public void setNontransactionalRead(boolean flag) {
        if (getPersistenceManager() != null) {
            throw new DeveloperException("Not allowed after first instantiation");
        }

        // influence on DbConnection->autoCommit
        nonTransactionalRead = flag;
    }

    /**
     * Set the default NontransactionalWrite setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @param flag the default NontransactionalWrite setting.
     */
    @Override
    public void setNontransactionalWrite(boolean flag) {
        if (getPersistenceManager() != null) {
            throw new DeveloperException("Not allowed after first instantiation");
        }
        // influence on DbConnection->autoCommit
        nonTransactionalWrite = flag;
    }

    /**
     * Set the default Optimistic setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @param flag the default Optimistic setting.
     */
    @Override
    public void setOptimistic(boolean flag) {
        if (!flag) {
            // TODO NYI
            throw new JDOUnsupportedOptionException("Pessimistic lock not supported yet");
        }
        optimistic = flag;
    }

    /**
     * Set the default value for the RestoreValues property. If
     * <code>true</code>, at rollback, fields of newly persistent instances are
     * restored to their values as of the beginning of the transaction, and the instances revert to transient. Additionally, fields of modified instances of primitive types and immutable reference
     * types are restored to their values as of the beginning of the transaction.
     * <p>
     * If <code>false</code>, at rollback, the values of fields of newly persistent instances are unchanged and the instances revert to transient. Additionally, dirty instances transition to hollow.
     * If an implementation does not support this option, a
     * <code>JDOUnsupportedOptionException</code> is thrown.
     *
     * @param restoreValues the value of the restoreValues property
     */
    @Override
    public void setRestoreValues(boolean restoreValues) {
        throw new JDOUnsupportedOptionException("RestoreValues");
    }

    /**
     * Set the default RetainValues setting for all
     * <code>PersistenceManager</code> instances obtained from this factory.
     *
     * @param flag the default RetainValues setting.
     */
    @Override
    public void setRetainValues(boolean flag) {
        throw new JDOUnsupportedOptionException("RetainValues");
    }

    /**
     * The application can determine from the results of this method which optional features, and which query languages are supported by the JDO implementation.
     * <p>
     * Each supported JDO optional feature is represented by a
     * <code>String</code> with one of the following values:
     *
     * <p>
     * <code>javax.jdo.option.TransientTransactional
     * <BR>javax.jdo.option.NontransactionalRead
     * <BR>javax.jdo.option.NontransactionalWrite
     * <BR>javax.jdo.option.RetainValues
     * <BR>javax.jdo.option.Optimistic
     * <BR>javax.jdo.option.ApplicationIdentity
     * <BR>javax.jdo.option.DatastoreIdentity
     * <BR>javax.jdo.option.NonDatastoreIdentity
     * <BR>javax.jdo.option.ArrayList
     * <BR>javax.jdo.option.HashMap
     * <BR>javax.jdo.option.Hashtable
     * <BR>javax.jdo.option.LinkedList
     * <BR>javax.jdo.option.TreeMap
     * <BR>javax.jdo.option.TreeSet
     * <BR>javax.jdo.option.Vector
     * <BR>javax.jdo.option.Map
     * <BR>javax.jdo.option.List
     * <BR>javax.jdo.option.Array
     * <BR>javax.jdo.option.NullCollection</code>
     *
     * <p>
     * The standard JDO query language is represented by a <code>String</code>:
     * <p>
     * <code>javax.jdo.query.JDOQL</code>
     *
     * @return the <code>List</code> of <code>String</code>s representing the supported options.
     */
    @Override
    public java.util.Collection<String> supportedOptions() {
        java.util.Set<String> set = new java.util.HashSet<String>();
        set.add("javax.jdo.option.Optimistic");
        set.add("javax.jdo.option.ApplicationIdentity");
        set.add("javax.jdo.option.NontransactionalRead");
        set.add("javax.jdo.option.NontransactionalWrite");
        return set;
    }

    /**
     * Unregister a specific ObjectServer to this DomainNameServer.
     */
    private static void unregister(PersistenceManager manager) {
        java.util.Iterator<String> iterator = locations.keySet().iterator();
        while (iterator.hasNext()) {
            String alias = iterator.next();
            if (locations.get(alias).equals(manager)) {
                iterator.remove();
                log.info("PersistencManager[DbObjectServer@{}] removed", alias);
                return;
            }
        }
    }

    /**
     * Return the default DbObjectServer.
     *
     * @deprecated (dangerous if more than one server is involved)
     */
    public static DbObjectServer getDefaultServer() {
        java.util.Iterator<javax.jdo.PersistenceManager> iterator = locations.values().iterator();
        while (iterator.hasNext()) {
            // TODO 1st entry might not be default
            return (DbObjectServer) iterator.next();
        }
        log.warn("no default server");
        return null;
    }
}
