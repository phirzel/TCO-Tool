package ch.softenvironment.jomm.implementation;

import junit.framework.TestCase;

/**
 * Test DbPropertyChange.
 *
 * @author Peter Hirzel
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

    public void testSetPrivateField() {
        TestBean bean = new TestBean();
        String msg = "TEST";
        DbPropertyChange<TestBean> chg = new DbPropertyChange<>(bean, "text");
        chg.setProperty(msg);
        assertEquals("DbPropertyChange->setField", bean.getText(), msg);
    }

    public void testPrimitiveTypes() {
        try {
            TestBean bean = new TestBean();
            DbPropertyChange<TestBean> chg = new DbPropertyChange<>(bean, "boolf");
            chg.getPrivateField().setBoolean(bean, true);

            chg = new DbPropertyChange<>(bean, "intf");
            chg.getPrivateField().setInt(bean, 45);
        } catch (Exception e) {
            fail(e.getLocalizedMessage());
        }
    }
}
