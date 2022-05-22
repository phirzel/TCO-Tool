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
import ch.softenvironment.jomm.tools.DbDataGenerator;
import ch.softenvironment.util.ListUtils;
import ch.softenvironment.util.Tracer;
import junit.extensions.TestSetup;
import junit.framework.TestSuite;

/**
 * Run this JUnit TestSuite to testsuite JOMM with HSQLDB in "Memory-Mode", by means same JVM and therefore no separate HSQLDB-Server must be running. Make sure appropriate vendor specific JDBC-Driver
 * is found in classpath at runtime and a HSQLDB (V1.8.2 or higher)
 * <p>
 * Default login: "sa" (without password)
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class HSQLDBTestSuite extends junit.framework.TestSuite {

    private static final String SCHEMA = "demoapp"; // "_DemoApp_";

    /**
     * DemoTestSuite constructor comment.
     */
    public HSQLDBTestSuite() {
        super();
    }

    /**
     * DemoTestSuite constructor comment.
     *
     * @param arg1 java.lang.Class
     */
    public HSQLDBTestSuite(Class arg1) {
        super(arg1);
    }

    /**
     * DemoTestSuite constructor comment.
     *
     * @param arg1 java.lang.String
     */
    public HSQLDBTestSuite(String arg1) {
        super(arg1);
    }

    /**
     * Start TestSuite with this main() to establish proper Target-System connection.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Define all tests for desired Target.
     */
    public static junit.framework.Test suite() {
        TestSuite suite = new IndependentTestSuite();
        suite.addTest(new SqlSuite()); // suite.addTestSuite(SqlSuite.class);
        suite.addTest(new TestSuite(DemoAppTestCase.class));

        // define setUp() for all TestCases in this suite()
        TestSetup wrapper = new TestSetup(suite) {
            private DbObjectServer server = null;

            @Override
            protected void setUp() {
                Tracer.start(Tracer.ALL);

                // Var. I) non persistent db ("mem") for testsuite-reasons only in
                // same JVM as this TestSuite
                DbLoginDialog dialog = new DbLoginDialog(null,
                    "jdbc:hsqldb:mem:" + SCHEMA);
                // TODO Var. II) testsuite on separate HSQLDB-server with persistent
                // data: url="jdbc:hsqldb:hsql://localhost/xdb"

                if (dialog.isSaved()) {
                    try {
                        server = initializeTarget(dialog.getUserId(),
                            dialog.getPassword(), dialog.getUrl());
                    } catch (Throwable e) {
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
                        server.execute("DROP Test SCHEMA",
                            ListUtils.createList("DROP SCHEMA " + SCHEMA));
                        server.execute("Shutdown HSQLDB-Server",
                            ListUtils.createList("SHUTDOWN COMPACT")); // HSQLDB
                        // specific
                        server.close();
                    } catch (Throwable e) {
                        fail("drop schema/logout failed: "
                            + e.getLocalizedMessage());
                    }
                }
                Tracer.getInstance().stop();
            }
        };

        return wrapper;
    }

    private static DbObjectServer initializeTarget(String userId,
        String password, String url) throws Exception {
        // establish a connection according to JDO
        javax.jdo.PersistenceManagerFactory pmFactory = DbDomainNameServer
            .createInstance(url);
        pmFactory.setNontransactionalRead(false); // NO autoCommit while reading
        pmFactory.setNontransactionalWrite(false); // NO autoCommit while
        // writing
        DbObjectServer server = (DbObjectServer) pmFactory
            .getPersistenceManager(userId, password);

        RegisterUtility.registerClasses(server);// register Model (all
        // persistent DbObject's)

        // create SQL-Schema corresponding to Java-Model
        server.execute(
            "Create Test SCHEMA",
            ListUtils.createList("CREATE SCHEMA " + SCHEMA
                + " AUTHORIZATION DBA"));

        DbDataGenerator.executeSqlCode(server, "sql/NLS_Schema_HSQLDB.sql");
        DbDataGenerator.executeSqlCode(server,
            "demo_app/sql/DemoApp_HSQLDB.sql");
        DbDataGenerator.executeSqlCode(server, "demo_app/sql/CreateData.sql");

        return server;
    }
}
