package ch.softenvironment.jomm.target.sql.msaccess;

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

import ch.ehi.basics.view.GenericFileFilter;
import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.util.StringUtils;
import java.util.Collection;
import java.util.Set;
import javax.jdo.FetchGroup;
import javax.jdo.PersistenceManager;
import javax.jdo.datastore.DataStoreCache;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.metadata.JDOMetadata;
import javax.jdo.metadata.TypeMetadata;

/**
 * JDO-Implementation of an MS-Access Factory.
 *
 * @author Peter Hirzel
 * @see javax.jdo.PersistenceManagerFactory
 */
public class MsAccessObjectServerFactory extends ch.softenvironment.jomm.DbDomainNameServer {

	// count number of BEGIN's since last Database-Connection
	private long beginCounter = 0;

	/**
	 * Initiated via:
	 *
	 * @see DbDomainNameServer#createInstance(String)
	 */
	public MsAccessObjectServerFactory() {
		// TODO make protected
		super();

		setConnectionDriverName("sun.jdbc.odbc.JdbcOdbcDriver");
	}

	@Override
	protected javax.jdo.PersistenceManager createPersistenceManager() {
		try {
			return new ch.softenvironment.jomm.target.sql.SqlObjectServer(this, connectionPassword, new MsAccessMapper(), MsAccessQueryBuilder.class,
				ch.softenvironment.jomm.target.sql.SqlConnection.class);
		} catch (Exception e) {
			throw new ch.softenvironment.util.DeveloperException("DbObjectServer could not be established", null, e);
		}
	}

	/**
	 * MS Acces becomes slower and slower the more SELECT-Queries are performed. (BUG MS-Access ???) => reconnect from time to time seems to solve the problem.
	 */
	@Deprecated
	@Override
	public void needsReconnect() {
		if ((getPersistenceManager() != null) && (beginCounter++ > 7 /*
		 * empirical
		 * value for
		 * MS Access
		 */)) {
			((DbObjectServer) getPersistenceManager()).reconnect();
			beginCounter = 0; // reset
		}
	}

	/**
	 * Return a default GenericFileFilter for Microsoft MS Access (*.mdb) Databases.
	 *
	 * @return
	 */
	public static GenericFileFilter createGenericFileFilter() {
		// TODO evtl. move to GenericFileFilter
		return new GenericFileFilter("MS Access (*.mdb)", "mdb");
	}

	/**
	 * Create an URL to establish an JDBC/ODBC-Bridge connection by a given database-file.
	 *
	 * @param absoluteFilePath of database-file
	 * @return
	 */
	public static String createOdbcUrl(String absoluteFilePath) {
		return "jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb)};DBQ=" + StringUtils.replace(absoluteFilePath, "\\", "/");
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
