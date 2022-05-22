package ch.softenvironment.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DateUtilsTestCase extends junit.framework.TestCase {

	java.util.Date currentDate = null;
	java.util.GregorianCalendar march15_2004 = null;

	/**
	 * StringUtilsTestCase constructor comment.
	 *
	 * @param name java.lang.String
	 */
	public DateUtilsTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		currentDate = new java.util.Date();
		march15_2004 = new java.util.GregorianCalendar(2004, java.util.Calendar.MARCH, 15);
	}

	/**
	 *
	 */
	public void testCalcTime() {
		assertTrue("DateUtils#calcTimeDifference",
			"0min. 33s".equals(DateUtils.calcTimeDifference(currentDate, new java.util.Date(currentDate.getTime() + (33 * 1000)))));
		assertTrue("DateUtils#calcTimeDifference",
			"57min. 42s".equals(DateUtils.calcTimeDifference(currentDate, new java.util.Date(currentDate.getTime() + ((57 * 60 + 42) * 1000)))));
		assertTrue("DateUtils#calcTimeDifference",
			"2h0min. 0s".equals(DateUtils.calcTimeDifference(currentDate, new java.util.Date(currentDate.getTime() + (2 * 60 * 60 * 1000)))));
		assertTrue(
			"DateUtils#calcTimeDifference",
			"14h13min. 5s".equals(DateUtils.calcTimeDifference(currentDate, new java.util.Date(currentDate.getTime()
				+ ((14 * 60 * 60 + 13 * 60 + 5) * 1000)))));
	}

	public void testGetYear() {
		assertTrue("DateUtils#getYear", DateUtils.getYear(march15_2004.getTime()).intValue() == 2004);
	}

	public void testGetDayInMonth() {
		assertTrue("DateUtils#getDayInMonth", DateUtils.getDayInMonth(march15_2004.getTime()).intValue() == 15);
	}

	public void testCalcHours() {
		assertTrue("DateUtils#calcHours", 2.0 == DateUtils.calcHours(currentDate, new java.util.Date(currentDate.getTime() + (2 * 60 * 60 * 1000)), 1)
			.doubleValue());
	}

	public void testBeginningOfWeek() {
		GregorianCalendar now = new GregorianCalendar();
		java.util.Date monday = DateUtils.getBeginingOfWeek();
		assertTrue(monday.getDay() + 1 == Calendar.MONDAY);
		assertTrue(monday.getYear() + 1900 == now.get(Calendar.YEAR));

		// the deprecated way
		java.util.Date nowOld = new java.util.Date();
		int day = nowOld.getDay() + 1;
		long lastMondayDiff = day - java.util.Calendar.MONDAY;
		assertTrue("might differ in some millis", (new java.util.Date(now.getTimeInMillis() - lastMondayDiff * 24 * 60 * 60 * 1000)).getTime() <= monday.getTime());
	}

	public void testEndOfMonth() {
		// use deprecated way here
		java.util.Date now = new java.util.Date();
		java.util.Date firstNextMonth = new java.util.Date(now.getYear(), now.getMonth() + 1, 1);
		java.util.Date last = new java.util.Date(firstNextMonth.getTime() - 24 * 60 * 60 * 1000);

		assertTrue(last.getTime() == DateUtils.getEndOfMonth().getTime());
	}

	public void testEndOfYear() {
		// use deprecated form to test
		java.util.Date now = new java.util.Date();
		assertTrue((new java.util.Date(now.getYear(), java.util.Calendar.DECEMBER, 31)).getTime() == DateUtils.getEndOfYear().getTime());
	}

	public void testFirstOfMonth() {
		// use deprecated form to test
		java.util.Date now = new java.util.Date();
		assertTrue((new java.util.Date(now.getYear(), now.getMonth(), 1)).getTime() == DateUtils.getFirstOfMonth().getTime());
	}

	public void testFirstOfYear() {
		// use deprecated form to test
		java.util.Date now = new java.util.Date();
		assertTrue((new java.util.Date(now.getYear(), java.util.Calendar.JANUARY, 1)).getTime() == DateUtils.getFirstOfYear().getTime());
	}

	public void testInRange() {
		java.util.Date date = new java.util.Date(2007, 3, 15);
		assertTrue("no range", DateUtils.inRange(date, null, null));
		assertTrue("greater", DateUtils.inRange(date, new java.util.Date(2007, 2, 15), null));
		assertTrue("lesser", DateUtils.inRange(date, null, new java.util.Date(2007, 4, 15)));
		assertTrue("in range", DateUtils.inRange(date, new java.util.Date(2007, 2, 15), new java.util.Date(2007, 4, 15)));
		assertFalse("later", DateUtils.inRange(date, new java.util.Date(2007, 5, 15), new java.util.Date(2007, 6, 15)));
		assertFalse("sooner", DateUtils.inRange(date, new java.util.Date(2007, 1, 15), new java.util.Date(2007, 2, 15)));
		assertFalse("later", DateUtils.inRange(date, null, new java.util.Date(2007, 2, 15)));
		assertFalse("sooner", DateUtils.inRange(date, new java.util.Date(2007, 4, 15), null));
	}
}
