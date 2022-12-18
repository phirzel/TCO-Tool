package ch.softenvironment.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Hirzel
 */
public class ParserCSVTestCase extends junit.framework.TestCase {

	private static final char sep = ';';

	/**
	 * StringUtilsTestCase constructor comment.
	 *
	 * @param name java.lang.String
	 */
	public ParserCSVTestCase(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	public void testMaskSeparator() {
		assertTrue("abc123", "abc123".equals(ParserCSV.maskSeparator("abc123", sep)));
		assertTrue("abc\n123", "abc,123".equals(ParserCSV.maskSeparator("abc\n123", sep)));
		assertTrue("abc\n123;xxx", "abc,123,xxx".equals(ParserCSV.maskSeparator("abc\n123;xxx", sep)));
		assertTrue("abc\n123;xxx\nabc\n123;xxx", "abc,123,xxx,abc,123,xxx".equals(ParserCSV.maskSeparator("abc\n123;xxx\nabc\n123;xxx", sep)));
	}

	public void testArrayToString() {
        assertTrue("empty[]", "".equals(ParserCSV.arrayToString(null, sep)));

        List<String> list = new ArrayList<>();
        list.add("abc");
        assertTrue("[abc]", ("abc" + sep).equals(ParserCSV.arrayToString(list, sep)));
        list.add("xxx");
        assertTrue("[abc, xxx]", ("abc" + sep + "xxx" + sep).equals(ParserCSV.arrayToString(list, sep)));
        list.add("4" /*Integer.valueOf(4)*/);
        assertTrue("[abc, xxx]", ("abc" + sep + "xxx" + sep + "4" + sep).equals(ParserCSV.arrayToString(list, sep)));
    }

	public void testStringToArray() {
		assertTrue("null", 0 == ParserCSV.stringToArray(null, sep).size());
		assertTrue("", 0 == ParserCSV.stringToArray("", sep).size());

		List<String> list = ParserCSV.stringToArray("abc;123", sep);
		assertTrue("abc;123", 2 == list.size());
		assertTrue("abc;123", "abc".equals(list.get(0)));
		assertTrue("abc;123", "123".equals(list.get(1)));
	}

	public void testNextString() {
		ParserCSV parser = new ParserCSV("", sep);
		assertTrue("empty String", null == parser.getNextString());
		parser = new ParserCSV("text", sep);
		assertTrue("text".equals(parser.getNextString()));
		parser = new ParserCSV("text;", sep);
		assertTrue("text".equals(parser.getNextString()));
		parser = new ParserCSV("text1;text2", sep);
		assertTrue("text1".equals(parser.getNextString()));
		assertTrue("text2".equals(parser.getNextString()));
		assertTrue(null == parser.getNextString());
		parser = new ParserCSV("text1;text2;", sep);
		assertTrue("text1".equals(parser.getNextString()));
		assertTrue("text2".equals(parser.getNextString()));
		assertTrue("empty String", null == parser.getNextString());
	}

	public void testNextInteger() {
        ParserCSV parser = new ParserCSV("", sep);
        assertTrue("empty String", null == parser.getNextInteger());
        parser = new ParserCSV("13", sep);
        assertTrue(13 == parser.getNextInteger());
        parser = new ParserCSV("14;", sep);
        assertTrue(14 == parser.getNextInteger());
        parser = new ParserCSV("11;12", sep);
        assertTrue(11 == parser.getNextInteger());
        assertTrue(12 == parser.getNextInteger());
        assertTrue("empty String", null == parser.getNextInteger());
        parser = new ParserCSV("11;12;", sep);
        assertTrue(11 == parser.getNextInteger());
        assertTrue(12 == parser.getNextInteger());
        assertTrue("empty String", null == parser.getNextInteger());
    }

	public void testNextStringAndInteger() {
        ParserCSV parser = new ParserCSV("Hello;12;World;13", sep);
        assertTrue("Hello".equals(parser.getNextString()));
        assertTrue(12 == parser.getNextInteger());
        assertTrue("World".equals(parser.getNextString()));
        assertTrue(13 == parser.getNextInteger());

        parser = new ParserCSV("Hello;12;World;13;", sep);
        assertTrue("Hello".equals(parser.getNextString()));
        assertTrue(12 == parser.getNextInteger());
        assertTrue("World".equals(parser.getNextString()));
        assertTrue(13 == parser.getNextInteger());
    }

	public void testNextBoolean() {
		ParserCSV parser = new ParserCSV("", sep);
		assertTrue("empty String", null == parser.getNextBoolean());
		parser = new ParserCSV("true;", sep);
		assertTrue(Boolean.TRUE == parser.getNextBoolean());
		parser = new ParserCSV("Yes;", sep);
		assertTrue(Boolean.TRUE == parser.getNextBoolean());
		parser = new ParserCSV("Y;", sep);
		assertTrue(Boolean.TRUE == parser.getNextBoolean());
		parser = new ParserCSV("false;", sep);
		assertTrue(Boolean.FALSE == parser.getNextBoolean());
		parser = new ParserCSV("No;", sep);
		assertTrue(Boolean.FALSE == parser.getNextBoolean());
		parser = new ParserCSV("N;", sep);
		assertTrue(Boolean.FALSE == parser.getNextBoolean());
	}

	public void testSkip() {
		ParserCSV parser = new ParserCSV("Hello;12;World;13", sep);
		parser.skip(2);
		assertTrue("World".equals(parser.getNextString()));
		assertTrue(13 == parser.getNextInteger().intValue());
	}
}
