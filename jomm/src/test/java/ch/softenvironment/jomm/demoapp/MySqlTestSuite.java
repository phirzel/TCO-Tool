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
import ch.softenvironment.jomm.demoapp.testsuite.DemoAppTestCase;
import ch.softenvironment.jomm.mvc.view.DbLoginDialog;
import ch.softenvironment.jomm.target.sql.mysql.AutoIncrementTestCase;
import ch.softenvironment.jomm.tools.DbDataGenerator;
import ch.softenvironment.util.ListUtils;
import java.util.ArrayList;
import java.util.List;
import junit.extensions.TestSetup;
import junit.framework.TestSuite;
import lombok.extern.slf4j.Slf4j;

/**
 * Run this JUnit TestSuite to testsuite JOMM with MySQL. Make sure appropriate vendor specific JDBC-Driver is found in classpath at runtime and a MySQL-Server (V4.1.10 or higher) is running in the
 * background.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class MySqlTestSuite extends junit.framework.TestSuite {

	private static final String SCHEMA = "_DMY_DemoApp_";

	/**
	 * DemoTestSuite constructor comment.
	 */
	public MySqlTestSuite() {
		super();
	}

	/**
	 * DemoTestSuite constructor comment.
	 *
	 * @param arg1 java.lang.Class
	 */
	public MySqlTestSuite(Class arg1) {
		super(arg1);
	}

	/**
	 * DemoTestSuite constructor comment.
	 *
	 * @param arg1 java.lang.String
	 */
	public MySqlTestSuite(String arg1) {
		super(arg1);
	}

	/**
	 * Start TestSuite with this main() to establish proper Target-System connection.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
		// junit.swingui.TestRunner.run(MySqlTestSuite.class);
	}

	/**
	 * Define all tests for desired Target.
	 */
	public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("MySQL tests");
		suite.addTest(new IndependentTestSuite());
		suite.addTest(new SqlSuite()); // suite.addTestSuite(SqlSuite.class);
		suite.addTest(new TestSuite(DemoAppTestCase.class));

		// MySql Specific TestCase's
		suite.addTest(new TestSuite(AutoIncrementTestCase.class));

		// define setUp() for all TestCases in this suite()
		TestSetup wrapper = new TestSetup(suite) {
			private DbObjectServer server = null;

			@Override
			protected void setUp() {
				DbLoginDialog dialog = new DbLoginDialog(null,
					"jdbc:mysql://localhost:3306/" /*
				 * +
				 * "unknown defaultSchema"
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
						// remove the SQL-Schema in Target
						server.execute("DROP Test SCHEMA", ListUtils
							.createList("DROP DATABASE " + SCHEMA /*
							 * MySQL
							 * specific
							 */));
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
			.createInstance(url); // new MySqlObjectServerFactory();
		// pmFactory.setConnectionURL(url /* + "?user=" + userId +
		// "&password="*/);
		pmFactory.setNontransactionalRead(false); // NO autoCommit while reading
		pmFactory.setNontransactionalWrite(false); // NO autoCommit while
		// writing
		DbObjectServer server = (DbObjectServer) pmFactory
			.getPersistenceManager(userId, password);

		RegisterUtility.registerClasses(server);// register Model (all
		// persistent DbObject's)
		// create SQL-Schema corresponding to Java-Model
		List<String> createSchema = new ArrayList<String>();
		// TODO MySQL specific
		createSchema.add("DROP DATABASE IF EXISTS " + SCHEMA);
		createSchema.add("CREATE DATABASE " + SCHEMA);
		createSchema.add("USE " + SCHEMA);
		server.execute("Create Test SCHEMA", createSchema);

		DbDataGenerator.executeSqlCode(server, "sql/T_Key_Object_MySQL.sql");
		DbDataGenerator.executeSqlCode(server, "sql/NLS_Schema_MySQL.sql");
		DbDataGenerator
			.executeSqlCode(server, "demo_app/sql/DemoApp_MySQL.sql");
		DbDataGenerator.executeSqlCode(server, "demo_app/sql/CreateData.sql");

		return server;
	}
}
