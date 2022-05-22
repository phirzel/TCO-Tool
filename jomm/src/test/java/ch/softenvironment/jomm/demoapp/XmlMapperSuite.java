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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.demoapp.model.CompanyType;
import ch.softenvironment.jomm.demoapp.model.Phase;
import ch.softenvironment.jomm.demoapp.model.RoleType;
import ch.softenvironment.jomm.demoapp.testsuite.XmlDemoAppModel;
import ch.softenvironment.jomm.demoapp.testsuite.XmlDemoAppTestCase;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.util.Tracer;
import java.util.Locale;
import junit.extensions.TestSetup;
import junit.framework.TestSuite;

/**
 * Run this JUnit TestSuite to testsuite JOMM with MS SQL Server. Make sure appropriate vendor specific JDBC-Driver is found in classpath at runtime and a MySQL-Server (V4.1.10 or higher) is running
 * in the background.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class XmlMapperSuite extends junit.framework.TestSuite {

    public static final String SCHEMA = "DemoApp"; // "_DemoApp_";

    /**
     * DemoTestSuite constructor comment.
     */
    public XmlMapperSuite() {
        super();
    }

    /**
     * DemoTestSuite constructor comment.
     *
     * @param arg1 java.lang.Class
     */
    public XmlMapperSuite(Class arg1) {
        super(arg1);
    }

    /**
     * DemoTestSuite constructor comment.
     *
     * @param arg1 java.lang.String
     */
    public XmlMapperSuite(String arg1) {
        super(arg1);
    }

    /**
     * Start TestSuite with this main() to establish proper Target-System connection.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        //junit.swingui.TestRunner.run(XmlMapperSuite.class);
    }

    /**
     * Define all tests for desired Target.
     */
    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("XML mapping tests");
        suite.addTest(new IndependentTestSuite());
        suite.addTest(new TestSuite(XmlDemoAppTestCase.class));

        // define setUp() for all TestCases in this suite()
        TestSetup wrapper = new TestSetup(suite) {
            private DbObjectServer server = null;

            @Override
            protected void setUp() {
                Tracer.start(Tracer.ALL);
                // DbLoginDialog dialog = new DbLoginDialog(null,
                // "jdbc:sqlserver://SANDFLYER\\SQLEXPRESS" /*+
                // ";databasename=unknown defaultSchema" */);
                // if (dialog.isSaved()) {
                try {
                    server = initializeTarget(System.getProperty("user.name"), null, "testsuite." + SCHEMA);
                    server.register(XmlDemoAppModel.class, "XmlDemoAppModel");
                } catch (Throwable e) {
                    Tracer.getInstance().runtimeError("initializeTarget()", e);
                    fail("Schema creation failed: " + e.getLocalizedMessage());
                }
                // } else { fail("login aborted by user"); }
            }

            @Override
            protected void tearDown() {
                if (server != null) {
                    try {
                        // no schema drop necessary
                        server.close();
                    } catch (Throwable e) {
                        fail("logout failed: " + e.getLocalizedMessage());
                    }
                }
                Tracer.getInstance().stop();
            }
        };

        return wrapper;
    }

    private static DbObjectServer initializeTarget(String userId, String password, String url) throws Exception {
        // establish a connection according to JDO
        javax.jdo.PersistenceManagerFactory pmFactory = new ch.softenvironment.jomm.target.xml.XmlObjectServerFactory();
        pmFactory.setConnectionURL(url);
        // pmFactory.setNontransactionalRead(false); // NO autoCommit while
        // reading
        // pmFactory.setNontransactionalWrite(false); // NO autoCommit while
        // writing

        // no schema creation necessary (will be mapped out of memory)
        XmlObjectServer server = (XmlObjectServer) pmFactory.getPersistenceManager(userId, password);

        // register Model (all persistent DbObject's)
        RegisterUtility.registerClasses(server);

        mapEnumerations(server); // createData()

        return server;
    }

    /**
     * Define default DbEnumeration's
     *
     * @param server
     */
    public static void mapEnumerations(ch.softenvironment.jomm.target.xml.XmlObjectServer server) {

        java.util.Locale locale = Locale.GERMAN;

        server.createEnumeration(CompanyType.class, CompanyType.AG, locale, "AG");
        server.createEnumeration(CompanyType.class, CompanyType.GMBH, locale, "GmbH");
        server.createEnumeration(CompanyType.class, CompanyType.EINZELFIRMA, locale, "Einzelfirma");
        server.createEnumeration(CompanyType.class, CompanyType.VEREIN, locale, "Verein");

        server.createEnumeration(Phase.class, Phase.INCEPTION, locale, "Inception");
        server.createEnumeration(Phase.class, Phase.CONSTRUCTION, locale, "Construction");
        server.createEnumeration(Phase.class, Phase.ELABORATION, locale, "Elaboration");
        server.createEnumeration(Phase.class, Phase.TRANSITION, locale, "Transition");

        server.createEnumeration(RoleType.class, RoleType.QUALITYRESPONSIBLE, locale, "Qualitäts-Verantwortlicher");
        server.createEnumeration(RoleType.class, RoleType.PROJECTMANAGER, locale, "Projektleiter");
        server.createEnumeration(RoleType.class, RoleType.DEVELOPER, locale, "Entwickler");
        server.createEnumeration(RoleType.class, RoleType.TESTER, locale, "Tester");
    }
}