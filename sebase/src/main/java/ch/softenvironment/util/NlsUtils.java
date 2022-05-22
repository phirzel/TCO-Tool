package ch.softenvironment.util;

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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Set of reusable String Utilities.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.9 $ $Date: 2007-02-20 12:50:52 $
 */
public abstract class NlsUtils {

    public final static String TIME_24HOURS_PATTERN = "HH:mm:ss";    // 24 hours

    /**
     * Bind a String with appropriate parameters. For e.g. Pattern = "This are a {0} {1}"; tokens={Integer.valueOf(17),  "messages"}
     */
    public static String formatMessage(String pattern, Object[] tokens) {
        return MessageFormat.format(pattern, tokens);
    }

    /**
     * Bind a String with appropriate parameters. For e.g. formatMessage("This is number {0}", 1)
     *
     * @param pattern Complete String containing any Variables by {0}..{n}
     * @param arg0 integer value to be replaced in pattern
     */
    public static String formatMessage(String pattern, int arg0) {
        Object[] tokens = {Integer.valueOf(arg0)};
        return formatMessage(pattern, tokens);
    }

    /**
     * Bind a String with appropriate parameters. For e.g. Pattern = "This is an {0} message"; tokens={"english"}
     */
    public static String formatMessage(String pattern, String arg0) {
        Object[] tokens = {arg0};
        return formatMessage(pattern, tokens);
    }

    /**
     * Change Default Locale (only if different from current default). Might influence all NLS-Settings, like Number-, Date/Time-, Currency-Formatting, etc.
     *
     * @param locale Local to be switched to
     * @return whether new Locale was different from old one
     */
    public static boolean changeLocale(Locale locale) {
        if (Locale.getDefault().getLanguage().equals(locale.getLanguage())) {
            if ((!StringUtils.isNullOrEmpty(locale.getCountry())) && (!locale.getCountry().equals(Locale.getDefault().getCountry()))) {
                //TODO NYI: country not considered yet
            }
        } else {
/*			Locale locales[] = Locale.getAvailableLocales();
		for (int i=0; i<locales.length; i++) {
			Locale l = locales[i];
			if (l.getLanguage().equals(locale.getLanguage())) {

				Locale.setDefault(l);
				return true;
			}
		}
*/
            Locale.setDefault(locale);
            Tracer.getInstance().runtimeInfo("Locale changed to: " + Locale.getDefault());
            return true;
        }

        return false;
    }

    /**
     * Format a given Date in numeric Format with numeric <b>day, month, year</b> only (Time suppressed) in current Locale representation. Example for de_CH: "23.03.2005"
     *
     * @return String
     */
    public static String formatDate(java.util.Date date) {
        if (date == null) {
            return "";
        } else {
            //		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd.MM.yyyy");
            //		return sf.format(date);
            return java.text.DateFormat.getDateInstance(/*java.text.DateFormat.SHORT*/).format(date);
        }
    }

    /**
     * Return a date in localized format String.
     */
    public static String formatDate(GregorianCalendar date) {
        return formatDate(date.getTime());
    }

    /**
     * Return a date in localized format String.
     */
    public static String formatTime(java.util.Date date) {
        if (date == null) {
            return "";
        } else {
            return DateFormat.getTimeInstance().format(date);
        }
    }

    /**
     * Return a date in "HH:mm:ss" format.
     */
    public static String formatTime24Hours(java.util.Date date) {
        return formatTime24Hours(date, true);
    }

    /**
     * Return a date in "HH:mm[:ss]" format.
     */
    public static String formatTime24Hours(java.util.Date date, boolean includingSeconds) {
        if (date == null) {
            return "";
        } else {
            String pattern = "HH:mm";
            if (includingSeconds) {
                pattern = TIME_24HOURS_PATTERN;
            }
            java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(pattern);
            return sf.format(date);
        }
    }

    /**
     * Return a Timestamp in localized format String.
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        } else {
            //		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd.MM.yyyy" + " " + TIME_24HOURS_PATTERN);
            //		return sf.format(date);
            return DateFormat.getDateTimeInstance().format(date);
        }
    }
}
