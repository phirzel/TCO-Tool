package ch.softenvironment.jomm.target.sql.mysql;

import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.util.ListUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * see ch.softenvironment.demoapp.MySqlTestSuite to run this TestCase.
 *
 * @author Peter Hirzel
 * @deprecated not used by TCO-Tool
 */
@Deprecated(since = "1.6.0")
public class AutoIncrementTestCase {

    private DbObjectServer server = null;

    @Before
    public void setUp() throws java.lang.Exception {
        server = DbDomainNameServer.getDefaultServer();
    }

    /**
     * Test MySQL specific LAST_INSERT_ID() Function for AUTO_INCREMENT PK's. SELECT LAST_INSERT_ID()
     */
    @Ignore("not used for TCO-Tool")
    @Test
    public void sequence() {
        try {
            //        server.execute("Create Test SCHEMA", ListUtils.createList("USE " + SCHEMA));
            server.execute("create auto table", ListUtils.createList("CREATE TABLE X (T_Id int not null auto_increment PRIMARY KEY, val CHAR(1))"));
            server.execute("auto key", ListUtils.createList("INSERT INTO X (T_Id, val) VALUES (17, 'p')"));
            server.execute("auto key", ListUtils.createList("INSERT INTO X (T_Id, val) VALUES (null, 'p')"));
            DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.RAW, "Last-id");
            builder.setRaw("SELECT LAST_INSERT_ID()");
            assertEquals("last insert id", Long.valueOf(18), server.getFirstValue(builder));
            server.execute("auto key", ListUtils.createList("INSERT INTO X (T_Id, val) VALUES (null, 'p')"));
            server.execute("auto key", ListUtils.createList("INSERT INTO X (T_Id, val) VALUES (null, 'p')"));
            assertEquals("last insert id", Long.valueOf(20), server.getFirstValue(builder));
        } catch (Throwable e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Ignore("not used for TCO-Tool")
    @Test
    public void testDefaultTechFields() {
        try {
            //        server.execute("Create Test SCHEMA", ListUtils.createList("USE " + SCHEMA));
            server.execute("Create Y", ListUtils.createList("CREATE TABLE Y (T_Id int not null auto_increment PRIMARY KEY, val CHAR(1), T_LastChange TIMESTAMP NOT NULL)"));
            server.execute("Test TIMESTAMP", ListUtils.createList("INSERT INTO Y (T_Id, val) VALUES (null, 'p')"));
            DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.RAW, "T_LastChange");
            builder.setRaw("SELECT T_LastChange FROM Y");
            Object ts = server.getFirstValue(builder);
            assertNotNull("T_LastChange value", ts);
            assertTrue("T_LastChange type", ts instanceof Date);
            //TODO T_User.SYSTEM_USER() & T_CreateDate (TIME.NOW()
        } catch (Throwable e) {
            fail(e.getLocalizedMessage());
        }
    }
}
