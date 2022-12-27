package ch.softenvironment.jomm;

import ch.softenvironment.jomm.target.sql.msaccess.MsAccessObjectServerFactory;
import ch.softenvironment.jomm.target.sql.mysql.MySqlObjectServerFactory;
import junit.framework.TestCase;

import java.util.Iterator;
import java.util.List;

/**
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DbObjectServerTestCase extends TestCase {
    /**
     * Only for Schemas with self implemented Identity/Sequence by Table
     * "T_Key_Object".
     */
    public void testGetFirstValue() {
        DbObjectServer server = DbDomainNameServer.getDefaultServer();
        if ((server.getPersistenceManagerFactory() instanceof MsAccessObjectServerFactory)
                || (server.getPersistenceManagerFactory() instanceof MySqlObjectServerFactory)) {
            DbQueryBuilder builder = server.createQueryBuilder(
                    DbQueryBuilder.SELECT, "Select T_Key_Object");
            builder.setTableList("T_Key_Object"); // server.getMapper().getTargetNlsName());
            builder.setAttributeList("COUNT(*)");
            assertTrue("Count as Integer",
                    ((Number) server.getFirstValue(builder)).longValue() > 0);

            builder.setAttributeList("T_LastUniqueId");
            builder.addFilter("T_Key", "T_NLS", DbQueryBuilder.STRICT);
            assertTrue("T_Id as Long",
                    ((Number) server.getFirstValue(builder)).longValue() > 0);
        }
    }

    /**
     * Only for Schemas with self implemented Identity/Sequence by Table
     * "T_Key_Object".
     */
    public void testGetFirstValues() {
        DbObjectServer server = DbDomainNameServer.getDefaultServer();
        if ((server.getPersistenceManagerFactory() instanceof MsAccessObjectServerFactory)
                || (server.getPersistenceManagerFactory() instanceof MySqlObjectServerFactory)) {
            DbQueryBuilder builder = server.createQueryBuilder(
                    DbQueryBuilder.SELECT, "Select T_Key_Object");
            builder.setTableList("T_Key_Object"); // server.getMapper().getTargetNlsName());
            builder.setAttributeList("T_LastUniqueId");
            // builder.addFilter("T_Key", "T_NLS", DbQueryBuilder.STRICT);
            List<Object> values = server.getFirstValues(builder);
            assertTrue("List", values.size() > 1);

            Iterator<Object> it = values.iterator();
            while (it.hasNext()) {
                assertTrue("T_Id as Long", it.next() instanceof Number);
            }
        }
    }
}
