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

import java.io.IOException;
import java.io.StringWriter;
import junit.framework.TestCase;

/**
 * TestCase for CsvSerializer.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2008-04-18 18:30:16 $
 */
public class CsvSerializerTestCase extends TestCase {

	private StringWriter out = null;
	private CsvSerializer ser = null;

	@Override
	public void setUp() {
		out = new StringWriter();
		ser = new CsvSerializer(out);
	}

	public void testEncoding() {
		try {
			ser.cell("my Name");
			ser.cell("hello;world");
			ser.newline();
			assertTrue(ser.getWriter().toString().startsWith("my Name;hello,world;"));
		} catch (IOException ex) {
			fail(ex.getLocalizedMessage());
		}
	}
}
