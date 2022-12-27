package ch.softenvironment.jomm.datatypes;

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
 */

import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbMapper;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.demoapp.model.WorkProduct;
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.sql.SqlQueryBuilderTest;
import ch.softenvironment.jomm.target.sql.hsqldb.HSQLDBObjectServerFactory;
import ch.softenvironment.jomm.target.sql.oracle.OracleObjectServerFactory;
import ch.softenvironment.jomm.target.sql.postgresql.PostgreSqlObjectServerFactory;
import ch.softenvironment.util.DeveloperException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Test DbNlsString.
 * <p>
 * see ch.softenvironment.demoapp.*TestSuite to run this TestCase
 *
 * @author Peter Hirzel
 * @deprecated not used by TCO-Tool
 */
@Deprecated(since = "1.6.0")
public class DbNlsStringTest {

    private DbObjectServer server = null;

    @Before
    public void setUp() {
        if (DbDomainNameServer.getDefaultServer() == null) {
            throw new DeveloperException("must be executed within a <DB-specific> *TestSuite");
        }
        server = DbDomainNameServer.getDefaultServer();
    }

    @Test
    @Ignore
    public void multiTranslationMap() {
        try {
            WorkProduct wp = (WorkProduct) server
                    .createInstance(WorkProduct.class);

            DbNlsString nls = wp.getName();
            assertTrue((nls != null) && nls.getPersistencyState().isNew());

            nls.setValue("Gestern", Locale.GERMAN);
            nls.setValue("Yesterday", Locale.ENGLISH);
            nls.setValue("Hier", Locale.FRENCH);

            // nls.save(); => will not save because no owner
            wp.save(); // INSERT

            Locale.setDefault(Locale.ITALIAN);
            nls.setValue("<non lo so>");
            wp.save(); // UPDATE

            assertEquals("<non lo so>", nls.getValue());

            assertEquals("Yesterday", nls.getValue(Locale.ENGLISH));
            assertEquals("Hier", nls.getValue(Locale.FRENCH));
            assertEquals("Gestern", nls.getValue(Locale.GERMAN));

            assertEquals("<non lo so>", nls.getValue(Locale.ITALIAN));
        } catch (Exception e) {
            fail(e.getLocalizedMessage());
        }
    }

    /**
     * Expected cases with DbNlsString's
     */
    @Test
    @Ignore
    public void emptyNlsString() {
        try {
            WorkProduct wp = (WorkProduct) server
                    .createInstance(WorkProduct.class);
            assertNull(wp.getName().getValue());
            assertNull(wp.getOptionalName().getValue());

            wp.setDescription("no optionalName");
            wp.getName().setValue("mandatory"); // T_Id_Name[1] is Mandatory

            wp.save();

            assertTrue("wp itself!", wp.getPersistencyState().isSaved());
            assertTrue("wp.T_Id_Name must not be saved!", wp.getName()
                    .getPersistencyState().isSaved());
            assertTrue("wp.T_Id_optionalName must not be saved because empty!",
                wp.getOptionalName().getPersistencyState().isNew());

            wp.getOptionalName().setValue("Optional french", Locale.FRENCH);
            wp.save();
            assertTrue("wp.T_Id_optionalName now saved!", wp.getOptionalName()
                    .getPersistencyState().isSaved());
        } catch (Throwable e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    @Ignore
    public void resetValue() {
        try {
            WorkProduct wp = (WorkProduct) server.createInstance(WorkProduct.class);
            wp.getName().setValue("Mandatory");
            wp.getOptionalName().setValue("optional");
            wp.save();
            assertTrue(wp.getOptionalName().getPersistencyState().isSaved());

            wp.getOptionalName().setValue(null);
            assertTrue(wp.getOptionalName().getPersistencyState().isChanged());
            wp.save();
            assertTrue(wp.getOptionalName().getPersistencyState().isSaved());
            // TODO is Translation updated as empty or removed?
        } catch (Throwable e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    @Ignore
    public void reAssignment() {
        try {
            WorkProduct wp = (WorkProduct) server.createInstance(WorkProduct.class);
            wp.getName().setValue("Mandatory");
            wp.setName(wp.getName());
            wp.save();
            assertTrue("reassignment BFFORE save()", wp.getName()
                    .getPersistencyState().isSaved());

            wp = (WorkProduct) server.createInstance(WorkProduct.class);
            wp.getName().setValue("Mandatory");
            wp.save();
            wp.setName(wp.getName());
            wp.save();
            assertTrue("reassignment AFTER save()", wp.getName()
                .getPersistencyState().isSaved());
        } catch (Throwable e) {
            fail(e.getLocalizedMessage());
        }
    }

    /**
     * Non expected cases => developer-error!
     */
    @Test
    @Ignore
    public void ownerChange() {
        try {
            WorkProduct wp = (WorkProduct) server.createInstance(WorkProduct.class);
            wp.getName().setValue("MANDATORY");

            wp.setOptionalName(null);
            assertTrue("getter should lazy initialize", wp.getName()
                    .getPersistencyState().isNew());

            DbNlsString nls = (DbNlsString) server.createInstance(DbNlsString.class);
            wp.setOptionalName(nls);
            assertTrue("setChange() should have happened",
                nls.hasChangeableOwner());

            wp.save();
            assertTrue(wp.getName().getPersistencyState().isSaved());
            assertTrue("NEW and unchanged", wp.getOptionalName()
                .getPersistencyState().isNew());

            wp.getOptionalName().setValue("my option");
            wp.save();
            assertTrue("changed value", wp.getOptionalName()
                .getPersistencyState().isSaved());

            Long optionaNameId = wp.getOptionalName().getId();
            wp.setOptionalName((DbNlsString) server
                .createInstance(DbNlsString.class));
            wp.getOptionalName().setValue("new option");
            wp.save();
            assertNotEquals("new DbNlsString => different Id", optionaNameId, wp.getOptionalName().getId());
            // TODO old NLS should have been removed because not used any more

            // TODO DbSessionBean's must not own DbNlsString's
        } catch (Throwable e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    @Ignore
    public void developerError() throws Exception {
        WorkProduct wp1 = (WorkProduct) server.createInstance(WorkProduct.class);
        wp1.getName().setValue("wp1");
        wp1.save();

        WorkProduct wp2 = (WorkProduct) server.createInstance(WorkProduct.class);
        wp2.getName().setValue("wp2");
        wp2.save();
        try {
            wp1.setName(wp2.getName());
        } catch (DeveloperException e) {
            // OK! forbidden
            return;
        }
        fail("forbidden case worked");
    }

    @Ignore
    public void dbCodeMultiText() {
        // TODO Workproduct.setMultiName()

    }

    @Ignore
    public void testEvict() {
        // server.evict();
        // reread must be ok
    }

    /**
     * Adds entries for DbNlsString in a very low level way to testsuite Schema capabilities. Do not copy this code fragment in any real application, just use rather: DbNlsString nls =
     * server.createInstance(DbNlsString.class); nls.set*(); nls.save();
     */
    @Test
    @Ignore
    public void sqlDmlDql() {
        Long nlsId = 1L; // fine for T_Key_Object in this
        // testdata-szenario
        // = server.getMapper().getNewId(server, transaction,
        // server.getMapper().getTargetNlsName()).longValue();
        if ((server.getPersistenceManagerFactory() instanceof HSQLDBObjectServerFactory)
                || (server.getPersistenceManagerFactory() instanceof OracleObjectServerFactory)
                || (server.getPersistenceManagerFactory() instanceof PostgreSqlObjectServerFactory)) {
            nlsId = 10000L;
        }

        // sequence important
        insertTranslations(nlsId);
        updateTranslations(nlsId);
        selectTranslations(nlsId);
        deleteTranslations(nlsId);
    }

    private void insertTranslations(Long nlsId) {
        // NLS
        // Date timestamp = new Date();
        DbQueryBuilder builder = server.createQueryBuilder(
            DbQueryBuilder.INSERT, "Insert NLS");
        builder.setTableList(server.getMapper().getTargetNlsName());
        builder.append(server.getMapper().getTargetIdName(), nlsId);
        builder.append("Symbol", "Moergeli");
        // builder.appendInternal((String)null);
        SqlQueryBuilderTest.insertTechnicalFields(builder);
        server.execute(builder);

        // NLS-Translation
        builder = server.createQueryBuilder(DbQueryBuilder.INSERT,
            "Insert Translation(en)");
        builder.setTableList(server.getMapper().getTargetNlsTranslationName());
        builder.append(DbNlsString.ATTRIBUTE_TRANSLATION_ID, nlsId);
        builder.append(DbNlsString.ATTRIBUTE_LANGUAGE, "en");
        builder.append(DbNlsString.ATTRIBUTE_NLS_TEXT, "Morning");
        // builder.appendInternal((String)null);
        SqlQueryBuilderTest.insertTechnicalFields(builder);
        server.execute(builder);

        builder = server.createQueryBuilder(DbQueryBuilder.INSERT,
            "Insert Translation(de)");
        builder.setTableList(server.getMapper().getTargetNlsTranslationName());
        builder.append(DbNlsString.ATTRIBUTE_TRANSLATION_ID, nlsId);
        builder.append(DbNlsString.ATTRIBUTE_LANGUAGE, "de");
        builder.append(DbNlsString.ATTRIBUTE_NLS_TEXT, "Morgen");
        SqlQueryBuilderTest.insertTechnicalFields(builder);
        server.execute(builder);
    }

    private void updateTranslations(Long nlsId) {
        DbQueryBuilder builder = server.createQueryBuilder(
            DbQueryBuilder.UPDATE, "Update Translation(de)");
        builder.setTableList(server.getMapper().getTargetNlsTranslationName());
        builder.append(DbNlsString.ATTRIBUTE_COUNTRY, "CH");
        builder.append(DbMapper.ATTRIBUTE_LAST_CHANGE, new Date(),
            DbDateFieldDescriptor.DATETIME);
        builder.addFilter(DbNlsString.ATTRIBUTE_TRANSLATION_ID, nlsId);
        builder.addFilter(DbNlsString.ATTRIBUTE_LANGUAGE, "de",
            DbQueryBuilder.STRICT);
        server.execute(builder);
    }

    private void selectTranslations(Long nlsId) {
        DbQueryBuilder builder = server.createQueryBuilder(
            DbQueryBuilder.SELECT, "Select Translations");
        builder.setTableList(server.getMapper().getTargetNlsName());
        builder.setAttributeList("COUNT(*)");
        builder.addFilter(server.getMapper().getTargetIdName(), nlsId);
        assertEquals("1 record in T_NLS", 1, ((Number) server.getFirstValue(builder))
                .longValue());

        builder = server.createQueryBuilder(DbQueryBuilder.SELECT,
            "Select Translations");
        builder.setTableList(server.getMapper().getTargetNlsTranslationName());
        builder.setAttributeList("COUNT(*)");
        builder.addFilter("T_Id_Nls", nlsId);
        assertEquals("2 records in T_NLS_Translation", 2, ((Number) server.getFirstValue(builder))
                .longValue());
    }

    private void deleteTranslations(Long nlsId) {
        DbQueryBuilder builder = server.createQueryBuilder(
            DbQueryBuilder.DELETE, "Delete Translations");
        builder.setTableList(server.getMapper().getTargetNlsName());
        builder.addFilter(server.getMapper().getTargetIdName(), nlsId);
        server.execute(builder);

        builder = server.createQueryBuilder(DbQueryBuilder.SELECT,
            "Select Translations");
        builder.setTableList(server.getMapper().getTargetNlsTranslationName());
        builder.setAttributeList("COUNT(*)");
        builder.addFilter(DbNlsString.ATTRIBUTE_TRANSLATION_ID, nlsId);
        assertEquals("ReferentialIntegrity should have removed T_NLS_Translation entries", 0, ((Number) server.getFirstValue(builder))
                .longValue());
    }
}