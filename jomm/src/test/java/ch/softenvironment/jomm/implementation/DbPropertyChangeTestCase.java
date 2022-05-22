package ch.softenvironment.jomm.implementation;

import junit.framework.TestCase;

/**
 * Test DbPropertyChange.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DbPropertyChangeTestCase extends TestCase {

    private static class TestBean {

        private String fieldText = null;
        @SuppressWarnings("unused")
        private final boolean fieldBoolf = false;
        @SuppressWarnings("unused")
        private final int fieldIntf = 69;

        @SuppressWarnings("unused")
        public void setText(final String text) {
            fieldText = text;
        }

        public String getText() {
            return fieldText;
        }
    }

    public void testSetPrivateField() throws Exception {
        TestBean bean = new TestBean();
        String msg = "TEST";
        DbPropertyChange<TestBean> chg = new DbPropertyChange<TestBean>(bean, "text");
        chg.setProperty(msg);
        assertTrue("DbPropertyChange->setField", bean.getText().equals(msg));
    }

    public void testPrimitiveTypes() {
        try {
            TestBean bean = new TestBean();
            DbPropertyChange<TestBean> chg = new DbPropertyChange<TestBean>(bean, "boolf");
            chg.getPrivateField().setBoolean(bean, true);

            chg = new DbPropertyChange<TestBean>(bean, "intf");
            chg.getPrivateField().setInt(bean, 45);
        } catch (Exception e) {
            fail(e.getLocalizedMessage());
        }
    }
}
