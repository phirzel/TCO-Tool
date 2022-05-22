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

import java.util.Locale;

/**
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.4 $ $Date: 2008-04-18 18:45:44 $
 */
public class AmountFormatTestCase extends junit.framework.TestCase {

    /**
     * ApplicationFrameTestCase constructor comment.
     *
     * @param arg1 java.lang.String
     */
    public AmountFormatTestCase(String arg1) {
        super(arg1);
    }

    public void testFormat() {
        Locale.setDefault(new Locale("de", "CH"));

        java.text.NumberFormat af = AmountFormat.getAmountInstance(/*default*/);
        //	assertTrue("format(null) => Exception", af.format(null).equals(""));
        assertTrue("format(Double)", af.format(new Double(12345.563)).equals("12’345.56"));
        assertTrue("toString(double)", af.format(1343492.3422).equals("1’343’492.34"));
        assertTrue("toString(Double)", af.format(new Double(1343492.3422)).equals("1’343’492.34"));
        assertTrue("toString(long)", af.format(1343492).equals("1’343’492.00"));
        assertTrue("toString(Long)", af.format(Long.valueOf(1343492)).equals("1’343’492.00"));
        assertTrue("toString(Double)", af.format(new Double(1343492.3472)).equals("1’343’492.35"));
        assertTrue("toString(Double)", af.format(new Double(1343492.367822)).equals("1’343’492.37"));

        af = AmountFormat.getAmountInstance(new Locale("de", "CH"));
        //  assertTrue("format(null) => Exception", af.format(null).equals(""));
        assertTrue("format(Double)", af.format(new Double(12345.563)).equals("12’345.56"));
        assertTrue("toString(double)", af.format(1343492.3422).equals("1’343’492.34"));
        assertTrue("toString(Double)", af.format(new Double(1343492.3422)).equals("1’343’492.34"));
        assertTrue("toString(long)", af.format(1343492).equals("1’343’492.00"));
        assertTrue("toString(Long)", af.format(Long.valueOf(1343492)).equals("1’343’492.00"));

        af = AmountFormat.getAmountInstance(new Locale("en", "US"));
        //  assertTrue("format(null) => Exception", af.format(null).equals(""));
        assertTrue("format(Double)", af.format(new Double(12345.563)).equals("12,345.56"));
        assertTrue("toString(double)", af.format(1343492.3422).equals("1,343,492.34"));
        assertTrue("toString(Double)", af.format(new Double(1343492.3422)).equals("1,343,492.34"));
        assertTrue("toString(long)", af.format(1343492).equals("1,343,492.00"));
        assertTrue("toString(Long)", af.format(Long.valueOf(1343492)).equals("1,343,492.00"));

        assertTrue("toString(null)", AmountFormat.toString(null).equals(""));
        //  assertTrue("toString(Double, 3, 5)", AmountFormat.toString(new Double(1343492.3422567), 3, 5).equals("1'343'492.34226"));
        //  assertTrue("toString(Double,3 ,3)", AmountFormat.toString(new Double(1343492.3), 3, 3).equals("1'343'492.300"));

    }
}
