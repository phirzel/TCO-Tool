package ch.softenvironment.jomm.demoapp;

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
import ch.softenvironment.jomm.demoapp.sql.DemoAppConstants;
import ch.softenvironment.jomm.demoapp.xml.DemoAppTest;
import ch.softenvironment.jomm.mvc.view.DbLoginDialog;
import ch.softenvironment.jomm.sql.DbConstants;
import ch.softenvironment.jomm.tools.DbDataGenerator;
import ch.softenvironment.util.ListUtils;
import junit.extensions.TestSetup;
import junit.framework.TestSuite;
import lombok.extern.slf4j.Slf4j;

/**
 * Run this JUnit TestSuite to testsuite JOMM with MS SQL Server. Make sure appropriate vendor specific JDBC-Driver is found in classpath at runtime and a the following services are running in the
 * background: - SQL Server 2003 (SQLExpress or Enterprise server) - SQL Server Browser - default login id: "sa"
 *
 * @author Peter Hirzel
 * @deprecated not used by TCO-Tool
 */
@Slf4j
@Deprecated(since = "1.6.0")
public class MsSQLServerSuite extends junit.framework.TestSuite {

	private static final String SCHEMA = "_DMY_DemoApp_";

	/**
	 * DemoTestSuite constructor comment.
	 */
	public MsSQLServerSuite() {
		super();
	}

	/**
	 * DemoTestSuite constructor comment.
	 *
	 * @param arg1 java.lang.Class
	 */
	public MsSQLServerSuite(Class arg1) {
		super(arg1);
	}

	/**
	 * DemoTestSuite constructor comment.
	 *
	 * @param arg1 java.lang.String
	 */
	public MsSQLServerSuite(String arg1) {
		super(arg1);
	}

	/**
	 * Start TestSuite with this main() to establish proper Target-System connection.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
		// junit.swingui.TestRunner.run(MsSQLServerSuite.class);
	}

	/**
	 * Define all tests for desired Target.
	 */
	public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("MS SQL Server tests");
		//suite.addTest(new IndependentTestSuite());
		suite.addTest(new SqlSuite());
		suite.addTest(new TestSuite(DemoAppTest.class));

		// define setUp() for all TestCases in this suite()
		TestSetup wrapper = new TestSetup(suite) {
			private DbObjectServer server = null;

			@Override
			protected void setUp() {
				DbLoginDialog dialog = new DbLoginDialog(null,
						"jdbc:sqlserver://SANDFLYER\\SQLEXPRESS" /*
				 * +
				 * ";databasename=unknown defaultSchema"
				 */);
				if (dialog.isSaved()) {
					try {
						server = initializeTarget(dialog.getUserId(),
							dialog.getPassword(), dialog.getUrl());
					} catch (Throwable e) {
						log.error("initializeTarget()", e);
						fail("Schema creation failed: "
							+ e.getLocalizedMessage());
					}
				} else {
					fail("login aborted by user");
				}
			}

			@Override
			protected void tearDown() {
				if (server != null) {
					try {
						// TODO remove the SQL-Schema in Target
						// server.execute("DROP Test SCHEMA",
						// ListUtils.createList("DROP DATABASE " + SCHEMA /*MS
						// SQL Server specific*/));
						server.close();
					} catch (Throwable e) {
						fail("drop schema/logout failed: "
							+ e.getLocalizedMessage());
					}
				}
			}
		};

		return wrapper;
	}

	private static DbObjectServer initializeTarget(String userId,
		String password, String url) throws Exception {
		// establish a connection according to JDO
		javax.jdo.PersistenceManagerFactory pmFactory = DbDomainNameServer
			.createInstance(url); // new SQLServerObjectServerFactory();
		// pmFactory.setConnectionURL(url /* + ";user=" + userId + ";password="
		// + password*/);
		pmFactory.setNontransactionalRead(false); // NO autoCommit while reading
		pmFactory.setNontransactionalWrite(false); // NO autoCommit while
		// writing
		DbObjectServer server = (DbObjectServer) pmFactory
			.getPersistenceManager(userId, password);

		RegisterUtility.registerClasses(server);// register Model (all
		// persistent DbObject's)
		// create SQL-Schema corresponding to Java-Model
		// TODO SQL Server specific
		// DROP SCHEMA;
		// server.execute("Create Test SCHEMA",
		// ListUtils.createList("CREATE DATABASE " + SCHEMA)); =>
		// com.microsoft.sqlserver.jdbc.SQLServerException->CREATE DATABASE
		// statement not allowed within multi-statement transaction. [S0005]
		// TODO ALTER DATABASE + "SCHEMA + " SET ALLOW_SNAPSHOT_ISOLATION ON --
		// for optimistic locking
		server.execute("Use Test SCHEMA", ListUtils.createList("USE " + SCHEMA));

		DbDataGenerator.executeSqlCode(server, DbConstants.class, "T_Key_Object_MS_SQL_Server.sql");
		DbDataGenerator.executeSqlCode(server, DbConstants.class, "NLS_Schema_MS_SQL_Server.sql");
		DbDataGenerator.executeSqlCode(server, DemoAppConstants.class, "DemoApp_MS_SQL_Server.sql");
		DbDataGenerator.executeSqlCode(server, DemoAppConstants.class, "CreateData.sql");

		return server;
	}
}
