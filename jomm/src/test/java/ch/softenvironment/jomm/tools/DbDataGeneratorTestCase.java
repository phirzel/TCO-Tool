package ch.softenvironment.jomm.tools;
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
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.util.NlsUtils;
import java.util.Date;
import java.util.GregorianCalendar;
import junit.framework.TestCase;

/**
 * Test DbDataGenerator.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2007-01-24 17:12:42 $
 */
public class DbDataGeneratorTestCase extends TestCase {

	DbDataGenerator gen = new DbDataGenerator();

	public void testRandomString() {
		for (int i = 0; i < 50; i++) {
			String s = gen.getRandomString(255);
			assertTrue("Random String(" + s.length() + "<=255) =>", s.length() <= 255);
		}
	}

	public void testRandomDate() {
		long start = (new GregorianCalendar(1990, GregorianCalendar.JANUARY, 1)).getTimeInMillis();
		long end = (new GregorianCalendar(2016, GregorianCalendar.DECEMBER, 31)).getTimeInMillis();
		for (int i = 0; i < 100; i++) {
			Date date = gen.getRandomDate(1990, 2015);
			assertTrue("Random Date(" + NlsUtils.formatDate(date) + "1990<=x<=2015) =>", (date.getTime() >= start) && (date.getTime() <= end));
		}
	}

	public void testInt() {
		for (int i = 0; i < 100; i++) {
			int n = gen.getGenerator().nextInt(1000);
			assertTrue("Random int(" + n + "<=1000", n <= 1000);
		}
	}
}
