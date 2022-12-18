package ch.softenvironment.util;

import ch.softenvironment.client.ResourceManager;
import java.nio.charset.StandardCharsets;

/**
 * @author Peter Hirzel
 */
public class StringUtilsTestCase extends junit.framework.TestCase {

	/**
	 * StringUtilsTestCase constructor comment.
	 *
	 * @param name java.lang.String
	 */
	public StringUtilsTestCase(String name) {
		super(name);
	}

	public void testGetString() {
		// getString(String)
		assertTrue("StringUtils.getString(null)", "".equals(ch.softenvironment.util.StringUtils.getString(null)));
		assertTrue("StringUtils.getString(empty)", "".equals(ch.softenvironment.util.StringUtils.getString("")));
		assertTrue("StringUtils.getString(blaBla)", "blaBla".equals(ch.softenvironment.util.StringUtils.getString("blaBla")));

		// getString(Object)
		assertTrue("StringUtils.getString(null)", "".equals(ch.softenvironment.util.StringUtils.getString(null)));
		assertTrue("StringUtils.getString(17)", "17".equals(ch.softenvironment.util.StringUtils.getString(17)));
		assertTrue("StringUtils.getString(-23.45)", "-23.45".equals(ch.softenvironment.util.StringUtils.getString(-23.45)));
		assertTrue("StringUtils.getString(Boolean.TRUE)",
			ResourceManager.getResource(StringUtils.class, "CI_Yes_text").equals(ch.softenvironment.util.StringUtils.getString(Boolean.TRUE)));
		assertTrue("StringUtils.getString(Boolean.FALSE)",
			ResourceManager.getResource(StringUtils.class, "CI_No_text").equals(ch.softenvironment.util.StringUtils.getString(Boolean.FALSE)));
	}

	public void testGetNextWord() {
		assertTrue("StringUtils.getNextWord(null)", "".equals(ch.softenvironment.util.StringUtils.getNextWord(null, 8)));
		assertTrue("StringUtils.getNextWord(Hello world)", "Hello".equals(ch.softenvironment.util.StringUtils.getNextWord("Hello world", 0)));
		assertTrue("StringUtils.getNextWord(Hello world)", "world".equals(ch.softenvironment.util.StringUtils.getNextWord("Hello world", 5)));
	}

	public void testPackageName() {
		assertTrue("StringUtils", "java.util".equals(ch.softenvironment.util.StringUtils.getPackageName(new java.util.ArrayList<Boolean>())));
		assertTrue("StringUtils", "java.util".equals(ch.softenvironment.util.StringUtils.getPackageName(java.util.ArrayList.class)));
	}

	public void testPureClassName() {
		assertTrue("StringUtils", "Object".equals(ch.softenvironment.util.StringUtils.getPureClassName(new Object())));
		assertTrue("StringUtils",
			"Statistic".equals(ch.softenvironment.util.StringUtils.getPureClassName(ch.softenvironment.util.Statistic.createEntry("test"))));
		assertTrue("StringUtils", "Statistic".equals(ch.softenvironment.util.StringUtils.getPureClassName(ch.softenvironment.util.Statistic.class)));
	}

	/*
	 * public void testPureFileName() { assertTrue("no Path",
	 * "MyFile.xml".equals
	 * (ch.softenvironment.util.StringUtils.getPureFileName("MyFile.xml")));
	 * assertTrue("DOS-Path",
	 * "MyFile.xml".equals(ch.softenvironment.util.StringUtils
	 * .getPureFileName("C:\\tmp\\dummy\\MyFile.xml"))); assertTrue("DOS-Path",
	 * "MyFile.xml".equals(ch.softenvironment.util.StringUtils.getPureFileName(
	 * "C:\\tmp\\dummy\\MyFile.xml"))); assertTrue("Unix-Path",
	 * "MyFile.xml".equals
	 * (ch.softenvironment.util.StringUtils.getPureFileName("/usr/local/MyFile.xml"
	 * ))); assertTrue("System-Property",
	 * "MyFile.xml".equals(ch.softenvironment.
	 * util.StringUtils.getPureFileName(System.getProperty("java.io.tmpdir") +
	 * "MyFile.xml"))); }
	 */
	public void testLimited() {
		char elipsis = (char) 0x5B5;

		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited(null, 4) == null);
		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("", 1).equals(""));

		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("abcdefg", 1).equals("" + elipsis));
		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("abcdefg", 2).equals("a" + elipsis));
		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("abcdefg", 3).equals("ab" + elipsis));
		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("abcdefg", 4).equals("abc" + elipsis));

		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("ab", 6).equals("ab"));
		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("abcdef", 6).equals("abcdef"));
		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("abcdef", 5).equals("abcd" + elipsis));
		assertTrue("StringUtils.getStringLimited()", StringUtils.getStringLimited("abcdef", 7).equals("abcdef"));
	}

	public void testReplace() {
		assertTrue("StringUtils", null == ch.softenvironment.util.StringUtils.replace((String) null, "", ""));
		assertTrue("StringUtils", null == ch.softenvironment.util.StringUtils.replace((StringBuffer) null, "", ""));
		assertTrue("StringUtils", "".equals(ch.softenvironment.util.StringUtils.replace("", "", "")));
		assertTrue("StringUtils", "X C".equals(ch.softenvironment.util.StringUtils.replace("X AS C", " AS ", " ")));
		assertTrue("StringUtils", "X C, Attr1 Dummy".equals(ch.softenvironment.util.StringUtils.replace("X AS C, Attr1 AS Dummy", " AS ", " ")));

		assertTrue("StringUtils", "'as'escape".equals(ch.softenvironment.util.StringUtils.replace("'as'escape", "'", "'")));

		// long replacement could cause StackOverflow by recursive algorithm
		StringBuffer buf = new StringBuffer("Begin");
		for (int i = 0; i < 10000; i++) {
			buf.append(" ");
		}
		buf.append("End");
		assertTrue("StringUtils", "BeginEnd".equals(ch.softenvironment.util.StringUtils.replace(buf.toString(), " ", "")));
	}

	public void testFirstLetterToLowercase() {
		assertTrue("StringUtils", "myProperty".equals(ch.softenvironment.util.StringUtils.firstLetterToLowercase("myProperty")));
		assertTrue("StringUtils", "myProperty".equals(ch.softenvironment.util.StringUtils.firstLetterToLowercase("MyProperty")));
		assertTrue("StringUtils", "123".equals(ch.softenvironment.util.StringUtils.firstLetterToLowercase("123")));
		assertTrue("StringUtils", "".equals(ch.softenvironment.util.StringUtils.firstLetterToLowercase("")));
		assertTrue("StringUtils", "  ".equals(ch.softenvironment.util.StringUtils.firstLetterToLowercase("  ")));
		assertTrue("StringUtils", null == ch.softenvironment.util.StringUtils.firstLetterToLowercase(null));
	}

	public void testFirstLetterToUppercase() {
		assertTrue("StringUtils", "MyProperty".equals(ch.softenvironment.util.StringUtils.firstLetterToUppercase("myProperty")));
		assertTrue("StringUtils", "MyProperty".equals(ch.softenvironment.util.StringUtils.firstLetterToUppercase("MyProperty")));
		assertTrue("StringUtils", "123".equals(ch.softenvironment.util.StringUtils.firstLetterToUppercase("123")));
		assertTrue("StringUtils", "".equals(ch.softenvironment.util.StringUtils.firstLetterToUppercase("")));
		assertTrue("StringUtils", "  ".equals(ch.softenvironment.util.StringUtils.firstLetterToUppercase("  ")));
		assertTrue("StringUtils", null == ch.softenvironment.util.StringUtils.firstLetterToUppercase(null));
	}

	public void testMakeValidFileName() {
		assertTrue("StringUtils", "m_y_F_le(_).txt".equals(ch.softenvironment.util.StringUtils.makeValidFileName("m:y/F\\le(\").txt", "_")));
	}

	/**
	 * Trial method.
	 */
	public void testEncoding() {
		assertTrue("Character same in ANSI & UTF-8", "<".equals(new String("<".getBytes(), StandardCharsets.ISO_8859_1)));
		String valISO = new String("ä".getBytes(), StandardCharsets.ISO_8859_1);
		//assertTrue("Character  same in ANSI & UTF-8", "ä".equals(valISO));
		//assertFalse("Character reversed to UTF-8", "ä".equals(new String("ä".getBytes(), Charset.forName("UTF-8"))));
	}
}
