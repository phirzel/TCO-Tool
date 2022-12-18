package ch.softenvironment.jomm.serialize;
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

import java.io.StringWriter;
import junit.framework.TestCase;

/**
 * TestCase for XmlSerializer.
 *
 * @author Peter Hirzel
 */
public class XmlSerializerTestCase extends TestCase {

    private static final String text = "myMessage ���{}[]��� () $�' \n\r\t \" & <>";

    private StringWriter out = null;
    private XmlSerializer ser = null;

    @Override
    public void setUp() {
        out = new StringWriter();
        ser = new XmlSerializer(out);
    }

    public void testEncodeString() {
        assertTrue(ser.encodeString("<>").equals("&lt;&gt;"));
        assertTrue(ser.encodeString("&").equals("&amp;"));
        assertTrue(ser.encodeString("'").equals("&apos;"));
        assertTrue(ser.encodeString("\"").equals("&quot;"));
        assertTrue(ser.encodeString("ä").equals("&#228;")); // ae

        assertFalse(ser.encodeString(text).equals(text));
    }

    public void testEncodeBoolean() {
        assertTrue(ser.encodeBoolean(Boolean.TRUE).equals("true"));
        assertTrue(ser.encodeBoolean(Boolean.FALSE).equals("false"));
        assertTrue(ser.encodeBoolean(null).equals(""));
    }
    //TODO encodeTime/Date/DateTime/Number
}
