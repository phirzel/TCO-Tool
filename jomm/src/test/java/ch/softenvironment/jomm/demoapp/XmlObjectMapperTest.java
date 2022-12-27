package ch.softenvironment.jomm.demoapp;

import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbUserTransactionBlock;
import ch.softenvironment.jomm.demoapp.model.CompanyType;
import ch.softenvironment.jomm.demoapp.model.Phase;
import ch.softenvironment.jomm.demoapp.model.Project;
import ch.softenvironment.jomm.demoapp.model.RoleType;
import ch.softenvironment.jomm.demoapp.xml.DemoAppTest;
import ch.softenvironment.jomm.demoapp.xml.XmlDemoAppBasket;
import ch.softenvironment.jomm.demoapp.xml.XmlDemoAppModel;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.target.xml.IliBasket;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@Slf4j
public class XmlObjectMapperTest {
    public static final String SCHEMA = "DemoApp"; // "_DemoApp_";
    private static final String XML_INSTANCE = "./target/" + SCHEMA + ".xml";
    private static XmlObjectServer server;


    private XmlDemoAppModel root = null;

    @BeforeClass
    public static void beforeClass() {
        // DbLoginDialog dialog = new DbLoginDialog(null,
        // "jdbc:sqlserver://SANDFLYER\\SQLEXPRESS" /*+
        // ";databasename=unknown defaultSchema" */);
        // if (dialog.isSaved()) {
        try {
            DbObjectServer server = initializeTarget(System.getProperty("user.name"), null, "testsuite." + SCHEMA);
            server.register(XmlDemoAppModel.class, "XmlDemoAppModel");
        } catch (Throwable e) {
            log.error("initializeTarget()", e);
            fail("Schema creation failed: " + e.getLocalizedMessage());
        }
        // } else { fail("login aborted by user"); }
    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            try {
                // no schema drop necessary
                server.close();
            } catch (Throwable e) {
                fail("logout failed: " + e.getLocalizedMessage());
            }
        }
    }

    @Before
    public void setUp() {
        server = (XmlObjectServer) DbDomainNameServer.getDefaultServer();
        try {
            root = (XmlDemoAppModel) server
                    .createInstance(XmlDemoAppModel.class);
            root.save();
        } catch (Exception e) {
            fail("creation of root failed: " + e.getLocalizedMessage());
        }
    }

    /**
     * @see DemoAppTest#modelMapping()  for generic SQL based case
     */
    @Test
    public void modelMapping() {
        try {
            Project project = DemoAppTest.createProject(server);

            List<Project> projects = DemoAppTest.generatData(server, DemoAppTest.COUNTER);
            projects.add(project);
            root.setProjects(projects);
            reload();
            assertEquals("Number of generated Projects's", (1 + DemoAppTest.COUNTER), root.getProjects()
                    .size());

            DbEnumeration enumeration = server.retrieveEnumeration(
                    RoleType.class, RoleType.DEVELOPER);
            assertTrue("RoleType->Developer", (enumeration != null)
                    && DbEnumeration.isIliCode(enumeration, RoleType.DEVELOPER));

            // List nps = queryData();
            // assertTrue("NaturalPerson's", (nps != null) && (nps.size() == 8
            // /*depends on counter*/));

            // List prjs = querySessionBean();
            // assertTrue("ProjectSessionBean's", (prjs != null) && (prjs.size()
            // == 1));

            project.remove(true);
            assertTrue("Project->removed", project.getPersistencyState().isRemoved());
        } catch (Exception e) {
            log.error("testModelMapping", e);
            fail("testModelMapping()");
        }
    }

    protected XmlDemoAppModel openFile() {
        try {
            server.reconnect();
            mapEnumerations(server);

            server.setUserObject(new ch.softenvironment.jomm.target.xml.XmlUserObject(XML_INSTANCE));
            List baskets = ListUtils.createList(new XmlDemoAppBasket());
            server.retrieveAll(baskets);
            server.setUserObject(null);
            return (XmlDemoAppModel) (((IliBasket) baskets.get(0))
                    .getModelRoot());
        } catch (javax.jdo.JDODataStoreException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                fail("File missing: " + XML_INSTANCE + "->"
                        + e.getLocalizedMessage());
            } else {
                fail("unknown read error for: " + XML_INSTANCE + "->"
                        + e.getLocalizedMessage());
            }
            return null;
        }
    }

    private void saveFile() throws Exception {
        // ((TcoModel)getUtility().getRoot()).setName(StringUtils.getPureFileName(filename));

        ch.softenvironment.jomm.target.xml.XmlUserObject userObject = new ch.softenvironment.jomm.target.xml.XmlUserObject(
                XML_INSTANCE);
        userObject.setSender("ch.softenvironment.jomm.xml.testsuite (V1.0.0)");
        // TODO NYI: XSD
        userObject.setXsd("http://www.interlis.ch/INTERLIS2.2 " + SCHEMA + ".xsd");
        // userObject.setXsl("http://www.tcotool.org/XSLT/TCOModel_Tree.xsl");
        final DbObjectServer server = DbDomainNameServer.getDefaultServer();
        server.setUserObject(userObject);
        final DbUserTransactionBlock block = server
                .createUserTransactionBlock(true);
        block.execute(new Runnable() {
            @Override
            public void run() {
                IliBasket basket = new XmlDemoAppBasket();
                basket.setModelRoot(/* LauncherView.getInstance(). */root);
                block.getObjectServer().makePersistentAll(
                        ListUtils.createList(basket));
            }
        });
        // server.makePersistent(getUtility().getRoot());
        server.setUserObject(null);
        // PersistenceService service = new PersistenceService();
        // service.writeFile(filename, getModel());

        ch.softenvironment.jomm.mvc.controller.ConsistencyController
                .cascadedSaved();
    }

    private void reload() {
        try {
            saveFile();
            root = openFile();
        } catch (Exception e) {
            fail("reload()->" + e.getLocalizedMessage());
        }
    }

    private static DbObjectServer initializeTarget(String userId, String password, String url) {
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
