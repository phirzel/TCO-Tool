package ch.softenvironment.client;

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

import java.util.Locale;
import junit.framework.TestCase;

/**
 * TestCase for ResourceManager.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.3 $ $Date: 2007-02-20 12:27:24 $
 */
public class ResourceManagerTestCase extends TestCase {

	public void testGetRessourceAsIs() {
		Locale.setDefault(Locale.US);
		assertTrue("en Translation", "My Label:".equals(ResourceManager.getResource(ResourceManagerTestCase.class, "LblTest_text")));
		assertTrue("en Translation", "My Panel".equals(ResourceManager.getResource(ResourceManagerTestCase.class, "PnlTest_text")));

		Locale.setDefault(Locale.GERMAN);
		assertTrue("de Translation", "Mein Label:".equals(ResourceManager.getResource(ResourceManagerTestCase.class, "LblTest_text")));
		assertTrue("de Translation", "Mein Panel".equals(ResourceManager.getResource(ResourceManagerTestCase.class, "PnlTest_text")));
	}

	public void testGetResourceLabel() {
		Locale.setDefault(Locale.US);
		// as is
		assertTrue("en Translation", "My Label:".equals(ResourceManager.getResourceAsLabel(ResourceManagerTestCase.class, "LblTest_text")));
		assertTrue("en Translation", "My Panel".equals(ResourceManager.getResourceAsNonLabeled(ResourceManagerTestCase.class, "PnlTest_text")));
		// should deal with ending
		assertTrue("en Translation", "My Label".equals(ResourceManager.getResourceAsNonLabeled(ResourceManagerTestCase.class, "LblTest_text")));
		assertTrue("en Translation", "My Panel:".equals(ResourceManager.getResourceAsLabel(ResourceManagerTestCase.class, "PnlTest_text")));

		Locale.setDefault(Locale.GERMAN);
		// as is
		assertTrue("de Translation", "Mein Label:".equals(ResourceManager.getResourceAsLabel(ResourceManagerTestCase.class, "LblTest_text")));
		assertTrue("de Translation", "Mein Panel".equals(ResourceManager.getResourceAsNonLabeled(ResourceManagerTestCase.class, "PnlTest_text")));
		// should deal with ending
		assertTrue("de Translation", "Mein Label".equals(ResourceManager.getResourceAsNonLabeled(ResourceManagerTestCase.class, "LblTest_text")));
		assertTrue("de Translation", "Mein Panel:".equals(ResourceManager.getResourceAsLabel(ResourceManagerTestCase.class, "PnlTest_text")));
	}

	public void testGetResourceMatch() {
		Locale.setDefault(Locale.US);
		// as is
		assertTrue("en Translation", "Xy-Text (en):".equals(ResourceManager.getResourceMatch(ResourceManagerTestCase.class, "Lbl[a-zA-Z0-9]*Xy_text", true)));
		assertTrue("en Translation", "Xy-Text (en)".equals(ResourceManager.getResourceMatch(ResourceManagerTestCase.class, "Lbl[a-zA-Z0-9]*y_text", false)));
	}

	public void testMultiLanguage() {
		assertTrue("en Translation", ResourceManager.getResource(ResourceManagerTestCase.class, "LblTest_text", Locale.ENGLISH).startsWith("My Label"));
		assertTrue("de Translation", ResourceManager.getResource(ResourceManagerTestCase.class, "LblTest_text", Locale.GERMAN).startsWith("Mein Label"));
		// and again
		assertTrue("en Translation", ResourceManager.getResource(ResourceManagerTestCase.class, "LblTest_text", Locale.ENGLISH).startsWith("My Label"));
		assertTrue("de Translation", ResourceManager.getResource(ResourceManagerTestCase.class, "LblTest_text", Locale.GERMAN).startsWith("Mein Label"));
	}
}
