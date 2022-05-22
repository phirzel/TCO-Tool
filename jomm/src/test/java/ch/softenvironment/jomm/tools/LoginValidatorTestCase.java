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

import ch.softenvironment.util.UserException;
import junit.framework.TestCase;

/**
 * TestCase for LoginValidator.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class LoginValidatorTestCase extends TestCase {

	private static final String SESSION_ID = "<NONE>";

	private LoginValidator validator = null;

	@Override
	public void setUp() {
		validator = new LoginValidator();
	}

	@Override
	public void tearDown() {
		validator = null;
	}

	public void testTryLoginInfinite() {
		for (int i = 1; i < 101; i++) {
			try {
				validator.login("phirzel", SESSION_ID, -1);
				// assertTrue(validator.getSessionCount("phirzel").equals(Integer.valueOf(i)));
			} catch (UserException e) {
				fail(e.getLocalizedMessage());
			}

			try {
				if (i % 2 == 0) {
					validator.login("dummy", SESSION_ID, -1);
				}
				// assertTrue(validator.getSessionCount("dummy").equals(Integer.valueOf(i
				// / 2)));
			} catch (UserException e) {
				fail(e.getLocalizedMessage());
			}
		}

		// assertTrue(validator.getSessionCount("phirzel").equals(Integer.valueOf(100)));
		validator.logout("phirzel", SESSION_ID, true);
		// assertTrue(validator.getSessionCount("phirzel").equals(Integer.valueOf(99)));
		// assertTrue(validator.getSessionCount("dummy").equals(Integer.valueOf(50)));
		validator.logout("dummy", SESSION_ID, true);
		// assertTrue(validator.getSessionCount("dummy").equals(Integer.valueOf(49)));

		assertTrue("check history", validator.getStatistics()
			.indexOf("phirzel") >= 0);
		assertTrue("check history",
			validator.getStatistics().indexOf("dummy") >= 0);
	}

	public void testTryLoginMax() {
		for (int i = 1; i < 6; i++) {
			try {
				validator.login("phirzel", SESSION_ID, 5);
				// assertTrue(validator.getSessionCount("phirzel").equals(Integer.valueOf(i)));
			} catch (UserException e) {
				fail(e.getLocalizedMessage());
			}
			/*
			 * try { validator.tryLogin("dummy", 10); }
			 * assertFalse(validator.tryLogin("phirzel", 5));
			 * assertTrue(validator.tryLogin("dummy", 10));
			 *
			 * validator.logout("phirzel", null);
			 * assertTrue(validator.getSessionCount
			 * ("phirzel").equals(Integer.valueOf(4)));
			 *
			 * validator.logout("dummy", null);
			 * assertTrue(validator.getSessionCount
			 * ("dummy").equals(Integer.valueOf(5)));
			 */
		}
		assertTrue("check history", validator.getStatistics()
			.indexOf("phirzel") >= 0);
	}

	/*
	 * public void testLogout() { validator.login("dummy",SESSION_ID, -1);
	 * assertTrue
	 * (validator.getSessionCount("dummy").equals(Integer.valueOf(1)));
	 *
	 * validator.logout("dummy",SESSION_ID, true);
	 * assertTrue(validator.getSessionCount
	 * ("dummy").equals(Integer.valueOf(0)));
	 * validator.logout("dummy",SESSION_ID, false);
	 * assertTrue("too many logouts ignored",
	 * validator.getSessionCount("dummy").equals(Integer.valueOf(0)));
	 *
	 * assertTrue("check history", validator.getStatistics().indexOf("dummy") >=
	 * 0); assertTrue("check history",
	 * validator.getStatistics().indexOf("logouts=2") >= 0);
	 * assertTrue("check history",
	 * validator.getStatistics().indexOf("(forced by timeout=1)") >= 0); }
	 */
	public void testStatistics() {
		/*
		 * LoginValidator v = LoginValidator.testStatistics();
		 * System.out.println(v.getStatistics());
		 */
		assertTrue("check history",
			validator.getStatistics().indexOf("none") >= 0);
		for (int i = 1; i < 6; i++) {
			validator.login("phirzel", SESSION_ID,
				LoginValidator.MAX_LOGIN_INFINITE);
			// assertTrue(validator.getSessionCount("phirzel").equals(Integer.valueOf(1)));
			assertTrue(
				"check history",
				validator.getStatistics().indexOf(
					"Logins total:</b></td><td><b>" + i) >= 0);
			assertTrue(
				"check history",
				validator.getStatistics().indexOf(
					"Logouts total: " + (i - 1)) >= 0);
			assertTrue(
				"check history",
				validator.getStatistics().indexOf(
					"Concurrent max:</b></td><td><b>" + 1) >= 0);
			assertTrue(
				"check history",
				validator.getStatistics().indexOf(
					"Concurrent now:</td><td>1") >= 0);
			validator.logout("phirzel", SESSION_ID, true);
			assertTrue(
				"check history",
				validator.getStatistics().indexOf("Logouts total: " + i) >= 0);
			// assertTrue(validator.getSessionCount("phirzel").equals(Integer.valueOf(0)));
			assertTrue(
				"check history",
				validator.getStatistics().indexOf(
					"Concurrent now:</td><td>" + 0) >= 0);
		}
		assertTrue("check history", validator.getStatistics()
			.indexOf("phirzel") >= 0);
		assertTrue(
			"check history",
			validator.getStatistics().indexOf(
				"Logins total:</b></td><td><b>5") >= 0);
		assertTrue("check history",
			validator.getStatistics().indexOf("Logouts total: 5") >= 0);
		assertTrue(
			"check history",
			validator.getStatistics().indexOf("Concurrent now:</td><td>0") >= 0);
		// assertTrue("check history",
		// validator.getStatistics().indexOf("Logins total: 5") >= 0);
		assertTrue(
			"check history",
			validator.getStatistics().indexOf(
				"Concurrent max:</b></td><td><b>1") >= 0);

		validator.login("phirzel", SESSION_ID,
			LoginValidator.MAX_LOGIN_INFINITE);
		validator.login("phirzel", SESSION_ID,
			LoginValidator.MAX_LOGIN_INFINITE);
		validator.login("phirzel", SESSION_ID,
			LoginValidator.MAX_LOGIN_INFINITE);
		assertTrue(
			"check history",
			validator.getStatistics().indexOf(
				"Logins total:</b></td><td><b>8") >= 0);
		assertTrue(
			"check history",
			validator.getStatistics().indexOf(
				"Concurrent max:</b></td><td><b>3") >= 0);
	}
}
