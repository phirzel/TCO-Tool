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
import ch.softenvironment.jomm.mvc.view.DbLoginDialog;
import ch.softenvironment.jomm.target.sql.msaccess.MsAccessQueryBuilderTestCase;
import ch.softenvironment.jomm.tools.DbDataGenerator;
import junit.extensions.TestSetup;
import junit.framework.TestSuite;
import lombok.extern.slf4j.Slf4j;

/**
 * Run this JUnit TestSuite to testsuite JOMM with MS Access. No JDBC-Driver is necessary because connection will be established by ODBC-JDBC-Bridge. Make sure an empty DemoApp.mdb application is
 * available in demo_app/sql directory.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class MsAccessTestSuite extends junit.framework.TestSuite {

	public MsAccessTestSuite() {
		super();
	}

	public MsAccessTestSuite(String arg1) {
		super(arg1);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static junit.framework.Test suite() {
		TestSuite suite = new IndependentTestSuite();
		suite.addTest(new SqlSuite()); // suite.addTestSuite(SqlSuite.class);
		// TODO suite.addTest(new TestSuite(DemoAppTestCase.class));
		suite.addTest(new TestSuite(MsAccessQueryBuilderTestCase.class));

		// define setUp() for all TestCases in this suite()
		TestSetup wrapper = new TestSetup(suite) {
			private DbObjectServer server = null;

			@Override
			protected void setUp() {
				DbLoginDialog dialog = new DbLoginDialog(
					null,
					"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=demo_app/sql/DemoApp.mdb");
				if (dialog.isSaved()) {
					try {
						server = initializeTarget(dialog.getUserId(),
							dialog.getPassword(), dialog.getUrl());
					} catch (Throwable e) {
						log.error("initializeTarget()",
							e);
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
						// TODO NYI: DROP SCHEMA not supported => execute
						// SQL-DDL within MS Access itself => Query
						java.util.List<String> dropSchema = new java.util.ArrayList<String>();

						// dropSchema.add("DROP TABLE T_MAP_NLS");
						// dropSchema.add("DROP TABLE T_Translation");
						// dropSchema.add("DROP TABLE T_NLS");

						dropSchema.add("DELETE FROM WorkProduct");
						dropSchema.add("DELETE FROM Activity");
						dropSchema.add("DELETE FROM Project");
						dropSchema.add("DELETE FROM CompanyType");
						dropSchema.add("DELETE FROM Phase");
						dropSchema.add("DELETE FROM RoleType");
						dropSchema.add("DELETE FROM CompanyType");
						dropSchema.add("DELETE FROM T_NLS");
						// dropSchema.add("DROP TABLE T_Key_Object");
						server.execute("Create Test SCHEMA", dropSchema);

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
			.createInstance(url); // new
		// ch.softenvironment.jomm.target.sql.ms_access.MsAccessObjectServerFactory();
		// pmFactory.setConnectionURL(url);
		pmFactory.setNontransactionalRead(false); // NO autoCommit while reading
		pmFactory.setNontransactionalWrite(false); // NO autoCommit while
		// writing
		DbObjectServer server = (DbObjectServer) pmFactory
			.getPersistenceManager(userId, password);

		RegisterUtility.registerClasses(server); // register Model (all
		// persistent DbObject's)

		// TODO NYI: create schema not supported yet => execute SQL-DDL within
		// MS Access itself => Query
		/*
		 * DbDataGenerator.executeFile(server,
		 * "sql/T_Key_Object_MS_Access.sql");
		 * DbDataGenerator.executeFile(server, "sql/NLS_Schema_MS_Access.sql");
		 * DbDataGenerator.executeFile(server,
		 * "demo_app/sql/DemoApp_MS_Access.sql");
		 */
		DbDataGenerator.executeSqlCode(server,
			"demo_app/sql/CreateData_MS_Access.sql");

		return server;
	}
}
