package ch.softenvironment.util;

import java.util.Date;
import java.util.Locale;

/**
 * Test BeanReflector
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class BeanReflectorTestCase extends junit.framework.TestCase {

	private TestBean bean = null;
	public String fieldTest = null;

	// test object for reflection manipulations
	public/* for reflection access */ static class TestBean {

		public String fieldTest = null;
		private String fieldText = null;
		private Double dVal = null;
		private Long lVal = null;
		private Integer iVal = null;
		private Boolean bVal = null;
		private java.util.Date date = null;
		private boolean bPrimitive;
		private int iPrimitive;

		public Double getDVal() {
			return dVal;
		}

		public void setDVal(Double val) {
			dVal = val;
		}

		public Integer getIVal() {
			return iVal;
		}

		public void setIVal(Integer val) {
			iVal = val;
		}

		public Long getLVal() {
			return lVal;
		}

		public void setLVal(Long val) {
			lVal = val;
		}

		public String getText() {
			return fieldText;
		}

		public void setText(String text) {
			this.fieldText = text;
		}

		public java.util.Date getDate() {
			return date;
		}

		public void setDate(java.util.Date date) {
			this.date = date;
		}

		public Boolean getBVal() {
			return bVal;
		}

		public void setBVal(Boolean val) {
			bVal = val;
		}

		public boolean getBPrimitive() {
			return bPrimitive;
		}

		public void setBPrimitive(boolean val) {
			bPrimitive = val;
		}

		public int getIPrimitive() {
			return iPrimitive;
		}

		public void setIPrimitive(int val) {
			iPrimitive = val;
		}
	}

	/**
	 * StringUtilsTestCase constructor comment.
	 *
	 * @param name java.lang.String
	 */
	public BeanReflectorTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() {
		bean = new TestBean();
		bean.setText("Hello");
		bean.setDVal(new Double(3.2));
		bean.setLVal(Long.valueOf(1989));
		bean.setIVal(Integer.valueOf(38));
		bean.setBVal(Boolean.TRUE);
		bean.setDate(new Date());
		bean.setBPrimitive(true);
		bean.setIPrimitive(25);
	}

	public void testGetValue() throws Exception {
		BeanReflector<TestBean> br = new BeanReflector<TestBean>(bean, "text");
		String value = "Bye";
		br.setValue(value);
		assertTrue("BeanReflector.getValue(String)", br.getValue().equals(value));
		assertTrue("BeanReflector.getValue(String)", br.getValue() == value);
	}

	public void testSetPublicField() throws Exception {
		String msg = "TEST";
		BeanReflector<TestBean> br = new BeanReflector<TestBean>(bean, "test");
		br.setField(msg);
		assertTrue("BeanReflector->setField", bean.fieldTest.equals(msg));
		assertTrue("BeanReflector->getField", br.getField().get(bean).equals(msg));
	}

	public void testCreateInstance() throws Exception {
		Object tb = BeanReflector.createInstance(TestBean.class);
		assertNotNull(tb);
		assertTrue("BeanReflector.createInstance(TestBean)", tb instanceof TestBean);
	}

	public void testHasProperty() throws Exception {
		BeanReflector<TestBean> br = new BeanReflector<TestBean>(bean, "iVal");
		assertTrue("BeanReflector", br.hasProperty() == BeanReflector.GETTER_AND_SETTER);

		BeanReflector<Locale> bl = new BeanReflector<Locale>(Locale.getDefault(), "language");
		assertTrue("BeanReflector", bl.hasProperty() == BeanReflector.GETTER);
	}

	public void testCloneValue() throws Exception {
		BeanReflector<TestBean> br = new BeanReflector<TestBean>(bean, "text");
		Object clonedValue = br.cloneValue();
		assertTrue("BeanReflector->String-Contents is equal", bean.getText().equals(clonedValue));

		br = new BeanReflector<TestBean>(bean, "dVal");
		clonedValue = br.cloneValue();
		assertTrue("BeanReflector->Double-Pointer not equal", bean.getDVal() != clonedValue);
		assertTrue("BeanReflector->Double-Contents is equal", bean.getDVal().equals(clonedValue));

		br = new BeanReflector<TestBean>(bean, "iVal");
		clonedValue = br.cloneValue();
		// assertTrue("depends on new Integer() and Integer.valueOf()",
		// bean.getIVal() != clonedValue);
		assertTrue("BeanReflector->Integer-Contents is equal", bean.getIVal().equals(clonedValue));

		br = new BeanReflector<TestBean>(bean, "lVal");
		clonedValue = br.cloneValue();
		assertTrue("BeanReflector->Long-Pointer not equal", bean.getLVal() != clonedValue);
		assertTrue("BeanReflector->Long-Contents is equal", bean.getLVal().equals(clonedValue));

		br = new BeanReflector<TestBean>(bean, "bVal");
		clonedValue = br.cloneValue();
		assertTrue("BeanReflector->Boolean-Contents is equal", bean.getBVal().equals(clonedValue));

		br = new BeanReflector<TestBean>(bean, "date");
		clonedValue = br.cloneValue();
		assertTrue("BeanReflector->Date-Pointer not equal", bean.getDate() != clonedValue);
		assertTrue("BeanReflector->Date-Contents is equal", bean.getDate().equals(clonedValue));
	}

	public void testPrimitiveBoolean() {
		BeanReflector<TestBean> br = new BeanReflector<TestBean>(bean, "bPrimitive");
		try {
			assertTrue("AMAZING: boolean => Boolean", br.getValue().equals(Boolean.TRUE));
			br.setValue(Boolean.FALSE);
			assertTrue("AMAZING: int => Integer", br.getValue().equals(Boolean.FALSE));

			Class<?> type = br.getGetterReturnType();
			assertTrue("BeanReflector->Boolean-Contents is equal", type == boolean.class);
			assertTrue(type.getName().equals("boolean"));

			// dummy test: => always true!!!
			assertTrue("BeanReflector->Boolean-Contents is equal", String.class instanceof Object);
			assertTrue("BeanReflector->Boolean-Contents is equal", type instanceof Object);
		} catch (Throwable e) {
			System.out.println(e.getLocalizedMessage());
			fail(e.getLocalizedMessage());
		}
	}

	public void testPrimitiveInt() {
		BeanReflector<TestBean> br = new BeanReflector<TestBean>(bean, "iPrimitive");
		try {
			assertTrue("AMAZING: int => Integer", br.getValue().equals(Integer.valueOf(25)));

			br.setValue(Integer.valueOf(-89));
			assertTrue("AMAZING: int => Integer", br.getValue().equals(Integer.valueOf(-89)));

			Class<?> type = br.getGetterReturnType();
			assertTrue("BeanReflector->Integer-Contents is equal", type == int.class);
			assertTrue(type.getName().equals("int"));
		} catch (Throwable e) {
			System.out.println(e.getLocalizedMessage());
			fail(e.getLocalizedMessage());
		}
	}
}
