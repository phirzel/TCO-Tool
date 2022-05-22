package ch.softenvironment.util;

import java.util.Calendar;

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

/**
 * Utilities for Date calculations.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.8 $ $Date: 2008-01-14 21:54:14 $
 */
public class DateUtils {

    /**
     * Return the difference between end- and start-Time.
     *
     * @return String ("[3h]15min.27s")
     */
    public static String calcTimeDifference(java.util.Date start, java.util.Date end) {
        if ((start == null) || (end == null)) {
            throw new DeveloperException("<start> nor <end> must be null!");
        }
        if (end.getTime() < start.getTime()) {
            throw new DeveloperException("<start> must be earlier than <end>!");
        }

        long seconds = (end.getTime() - start.getTime()) / 1000;
        String value = "";

        long hours = seconds / 3600;
        if (hours > 0) {
            // hours are optional
            value = value + hours + "h";
            seconds = seconds % 3600;
        }

        long minutes = seconds / 60;
        value = value + minutes + "min. ";
        seconds = seconds % 60;

        return value + seconds + "s";
    }

    /**
     * Return the last Monday.
     *
     * @return java.util.Date
     */
    public static java.util.Date getBeginingOfWeek() {
        //java.util.Date now = new java.util.Date();
        //int day = now.getDay() + 1; // 1 for Monday
        java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        int day = now.get(java.util.Calendar.DAY_OF_WEEK);

        long lastMondayDiff = day - java.util.Calendar.MONDAY;
        return new java.util.Date(now.getTimeInMillis() - lastMondayDiff * 24 * 60 * 60 * 1000);
    }

    /**
     * Return the last day of current month.
     *
     * @return java.util.Date
     */
    public static java.util.Date getEndOfMonth() {
        //java.util.Date now = new java.util.Date();
        //java.util.Date firstNextMonth = new java.util.Date(now.getYear(), now.getMonth() + 1, 1);

        java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        java.util.GregorianCalendar firstNextMonth = new java.util.GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, 1);

        return new java.util.Date(firstNextMonth.getTimeInMillis() - 24 * 60 * 60 * 1000);
    }

    /**
     * Return the next Sunday.
     *
     * @return java.util.Date
     */
    public static java.util.Date getEndOfWeek() {
        java.util.Date monday = getBeginingOfWeek();
        return new java.util.Date(monday.getTime() + (java.util.Calendar.DAY_OF_WEEK - 1) * 24 * 60 * 60 * 1000);
    }

    /**
     * Return the first day of current month.
     *
     * @return java.util.Date
     */
    public static java.util.Date getFirstOfMonth() {
        //java.util.Date now = new java.util.Date();
        //return (new java.util.GregorianCalendar(now.getYear(), now.getMonth(), 1)).getTime();
        java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        return (new java.util.GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1)).getTime();
    }

    /**
     * Return the last day of current year.
     *
     * @return java.util.Date
     */
    public static java.util.Date getEndOfYear() {
        //java.util.Date now = new java.util.Date();
        //return new java.util.Date(now.getYear(), java.util.Calendar.DECEMBER, 31);
        java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        return (new java.util.GregorianCalendar(now.get(Calendar.YEAR), java.util.Calendar.DECEMBER, 31)).getTime();
    }

    /**
     * Return the first day of current year, say "1.1.currentYear".
     *
     * @return java.util.Date
     */
    public static java.util.Date getFirstOfYear() {
        //java.util.Date now = new java.util.Date();
        //return new java.util.Date(now.getYear(), java.util.Calendar.JANUARY, 1);
        java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        return (new java.util.GregorianCalendar(now.get(Calendar.YEAR), java.util.Calendar.JANUARY, 1)).getTime();
    }

    /**
     * Return the difference between end- and start-Time in hours with given precision after comma.
     *
     * @return Double
     */
    public static Double calcHours(java.util.Date start, java.util.Date end, int precision) {
        if ((start == null) || (end == null)) {
            throw new DeveloperException("start nor end must be null!");
        }
        if (end.getTime() < start.getTime()) {
            throw new DeveloperException("start must be earlier end!");
        }

        double milliSeconds = (end.getTime() - start.getTime()) / 1000.0 / 3600.0;
        double accuracy = Math.pow(10, precision);

        return new Double((Math.round(milliSeconds * accuracy)) / accuracy);
    }

    /**
     * Return the day in month of given date.
     *
     * @return java.lang.Integer
     */
    public static Integer getDayInMonth(java.util.Date date) {
        //	java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd");
        //	sf.getCalendar().get(java.util.Calendar.YEAR);
        //	sf.applyPattern("dd");   // day only
        //	return Integer.valueOf(sf.format(date));
        if (date == null) {
            return null;
        } else {
            java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
            cal.setTime(date);
            return Integer.valueOf(cal.get(java.util.Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * Return the year of the Date, for e.g. 2004.
     *
     * @return java.lang.Integer
     */
    public static Integer getYear(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
            cal.setTime(date);
            return Integer.valueOf(cal.get(java.util.Calendar.YEAR));
        }
    }

    /**
     * Return a date in a formatted string, like "2010-11-29"
     *
     * @param date
     * @return
     */
    public static String getSortingDateString(java.util.Date date) {
        if (date == null) {
            return "";
        } else {
            java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sf.format(date);
        }
    }

    /**
     * Return whether given date is between from..to range.
     *
     * @param date
     * @param from
     * @param to
     * @return
     */
    public static boolean inRange(java.util.Date date, java.util.Date from, java.util.Date to) {
        if (date == null) {
            throw new IllegalArgumentException("date must no be null");
        }

        if (from != null) {
            if (date.getTime() < from.getTime()) {
                return false;
            }
        }
        if (to != null) {
            return date.getTime() <= to.getTime();
        }
        return true;
    }
}
