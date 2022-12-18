package ch.softenvironment.view.swingext;

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
 * Extended JTextField to show a Time (resp. a java.util.Date).
 *
 * @author Peter Hirzel
 * @see DateTextField
 */
public class TimeTextField extends DateTextField {

    /**
     * TimeTextField constructor comment. Default representation is a European 24h and minute String "HH:mm".
     */
    public TimeTextField() {
        this("HH:mm" /* + ":ss" */);
        // this(ch.softenvironment.util.NlsUtils.TIME_24HOURS_PATTERN);
    }

    /**
     * TimeTextField constructor comment.
     *
     * @param datePattern java.lang.String (for e.g. "HH:mm:ss")
     */
    public TimeTextField(String datePattern) {
        super(datePattern);
    }
}
