package ch.softenvironment.util;

import ch.softenvironment.client.ResourceManager;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
		assertEquals("StringUtils.getString(null)", "", StringUtils.getString(null));
		assertEquals("StringUtils.getString(empty)", "", StringUtils.getString(""));
		assertEquals("StringUtils.getString(blaBla)", "blaBla", StringUtils.getString("blaBla"));

		// getString(Object)
		assertEquals("StringUtils.getString(null)", "", StringUtils.getString(null));
		assertEquals("StringUtils.getString(17)", "17", StringUtils.getString(17));
		assertEquals("StringUtils.getString(-23.45)", "-23.45", StringUtils.getString(-23.45));
		assertEquals("StringUtils.getString(Boolean.TRUE)", ResourceManager.getResource(StringUtils.class, "CI_Yes_text"), StringUtils.getString(Boolean.TRUE));
		assertEquals("StringUtils.getString(Boolean.FALSE)", ResourceManager.getResource(StringUtils.class, "CI_No_text"), StringUtils.getString(Boolean.FALSE));
	}

	public void testGetNextWord() {
		assertEquals("StringUtils.getNextWord(null)", "", StringUtils.getNextWord(null, 8));
		assertEquals("StringUtils.getNextWord(Hello world)", "Hello", StringUtils.getNextWord("Hello world", 0));
		assertEquals("StringUtils.getNextWord(Hello world)", "world", StringUtils.getNextWord("Hello world", 5));
	}

	public void testPackageName() {
		assertEquals("StringUtils", "java.util", StringUtils.getPackageName(new ArrayList<Boolean>()));
		assertEquals("StringUtils", "java.util", StringUtils.getPackageName(ArrayList.class));
	}

	public void testPureClassName() {
		assertEquals("Object", StringUtils.getPureClassName(new Object()));
		assertEquals("Statistic", StringUtils.getPureClassName(Statistic.class));
	}

	public void testReplace() {
		assertNull("StringUtils", StringUtils.replace((String) null, "", ""));
		assertNull("StringUtils", StringUtils.replace((StringBuffer) null, "", ""));
		assertEquals("StringUtils", "", StringUtils.replace("", "", ""));
		assertEquals("StringUtils", "X C", StringUtils.replace("X AS C", " AS ", " "));
		assertEquals("StringUtils", "X C, Attr1 Dummy", StringUtils.replace("X AS C, Attr1 AS Dummy", " AS ", " "));

		assertEquals("StringUtils", "'as'escape", StringUtils.replace("'as'escape", "'", "'"));

		// long replacement could cause StackOverflow by recursive algorithm
		StringBuffer buf = new StringBuffer("Begin");
		for (int i = 0; i < 10000; i++) {
			buf.append(" ");
		}
		buf.append("End");
		assertEquals("StringUtils", "BeginEnd", StringUtils.replace(buf.toString(), " ", ""));
	}

	public void testFirstLetterToLowercase() {
		assertEquals("StringUtils", "myProperty", StringUtils.firstLetterToLowercase("myProperty"));
		assertEquals("StringUtils", "myProperty", StringUtils.firstLetterToLowercase("MyProperty"));
		assertEquals("StringUtils", "123", StringUtils.firstLetterToLowercase("123"));
		assertEquals("StringUtils", "", StringUtils.firstLetterToLowercase(""));
		assertEquals("StringUtils", "  ", StringUtils.firstLetterToLowercase("  "));
		assertNull("StringUtils", StringUtils.firstLetterToLowercase(null));
	}

	public void testFirstLetterToUppercase() {
		assertEquals("StringUtils", "MyProperty", StringUtils.firstLetterToUppercase("myProperty"));
		assertEquals("StringUtils", "MyProperty", StringUtils.firstLetterToUppercase("MyProperty"));
		assertEquals("StringUtils", "123", StringUtils.firstLetterToUppercase("123"));
		assertEquals("StringUtils", "", StringUtils.firstLetterToUppercase(""));
		assertEquals("StringUtils", "  ", StringUtils.firstLetterToUppercase("  "));
		assertNull("StringUtils", StringUtils.firstLetterToUppercase(null));
	}

	public void testMakeValidFileName() {
		assertEquals("StringUtils", "m_y_F_le(_).txt", StringUtils.makeValidFileName("m:y/F\\le(\").txt", "_"));
	}

	/**
	 * Trial method.
	 */
	public void testEncoding() {
		assertEquals("Character same in ANSI & UTF-8", "<", new String("<".getBytes(), StandardCharsets.ISO_8859_1));
		String valISO = new String("ä".getBytes(), StandardCharsets.ISO_8859_1);
		//assertTrue("Character  same in ANSI & UTF-8", "ä".equals(valISO));
		//assertFalse("Character reversed to UTF-8", "ä".equals(new String("ä".getBytes(), Charset.forName("UTF-8"))));
	}
}
