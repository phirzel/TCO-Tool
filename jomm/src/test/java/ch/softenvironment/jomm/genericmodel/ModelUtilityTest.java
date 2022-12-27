package ch.softenvironment.jomm.genericmodel;

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.genericmodel.agg.A2b_1;
import ch.softenvironment.jomm.genericmodel.comp.ClassB_1;
import ch.softenvironment.jomm.genericmodel.structattr.ClassA_1;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModelUtilityTest {
    @Test
    public void registerClasses() throws ClassNotFoundException {
        final DbObjectServer server = ModelUtility.buildModel();
        assertNotNull(server);
        assertTrue(server instanceof XmlObjectServer);

        assertNotNull(server.getMapper());
        assertNotNull(server.getConnection());

        assertEquals("softEnvironment.Translation", server.getTargetName(DbNlsString.class));

        assertEquals(ch.softenvironment.jomm.genericmodel.assoc.A2b_1.class, server.getDbObject("ModelAssoc_a2b_1"));

        assertEquals("ModelAgg_a2b_1", server.getTargetName(A2b_1.class));
        assertEquals("ModelAssoc_a2b_1", server.getTargetName(ch.softenvironment.jomm.genericmodel.assoc.A2b_1.class));
        assertEquals("ModelComp_ClassB_1", server.getTargetName(ClassB_1.class));
        assertEquals("ModelStructAttr_ClassA_1", server.getTargetName(ClassA_1.class));

        assertNotNull(server.getConnection().getDescriptor(server.getDbObject("ModelAssoc_a2b_1")));
        assertNotNull(server.getConnection().getDescriptor(server.getDbObject("ModelAssoc_a2b_1")).getEntry("aId"));
    }
}
