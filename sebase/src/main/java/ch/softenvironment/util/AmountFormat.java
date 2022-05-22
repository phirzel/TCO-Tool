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

import ch.softenvironment.math.MathUtils;
import java.util.Locale;

/**
 * Format a number to look like a financial value but without the currency itself.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class AmountFormat extends java.text.DecimalFormat {

    private static final int FRACTION_DIGITS = 2;

    /**
     * @see #getAmountInstance(Locale)
     */
    public static java.text.NumberFormat getAmountInstance() {
        return getAmountInstance(Locale.getDefault());
    }

    /**
     * Create a DecimalFormat for a given locale.
     */
    public static java.text.NumberFormat getAmountInstance(Locale locale) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getNumberInstance(locale);
        // java.text.NumberFormat.getCurrencyInstance();

        // @see DecimalFormat#formatter.adjustForCurrencyDefaultFractionDigits()
        formatter.setMinimumFractionDigits(FRACTION_DIGITS);
        formatter.setMaximumFractionDigits(FRACTION_DIGITS);
        // formatter.setGroupingSize(3); // separate thousand's
        formatter.setGroupingUsed(true);
        // formatter.setDecimalSeparatorAlwaysShown(true);

        return formatter;
    }

    /**
     * @see #toString(Number)
     */
    public static String toString(double amount) {
        return toString(new Double(amount));
    }

    /**
     * Convert given amount into formatted String. This is a convenience Method.
     * <p>
     * Alternatively use #getAmountInstance(Locale).format(amount) instead.
     */
    public static String toString(Number amount) {
        if (amount == null) {
            // Format#format(null) => Exception
            return "";
        } else {
            return getAmountInstance().format(amount);
        }
    }

    /**
     * Round to FRACTION_DIGITS decimal value.
     *
     * @param amount
     * @return rounded amount
     */
    public static double round(/* String iso4217Code, */double amount) {
        return MathUtils.round(amount, FRACTION_DIGITS);
        /*
         * try { if (StringUtils.isNullOrEmpty(iso4217Code)) { NumberFormat
         * format = getAmountInstance(); format.setGroupingUsed(false); return
         * Double.parseDouble(format.format(amount)); } else { NumberFormat
         * format = DecimalFormat.getNumberInstance();
         * format.setCurrency(Currency.getInstance(iso4217Code)); String
         * currency = format.format(amount); return
         * Double.parseDouble(StringUtils.replace(currency, iso4217Code, "")); }
         * } catch(NumberFormatException e) {
         * Tracer.getInstance().developerWarning(AmountFormat.class, "round()",
         * e.getLocalizedMessage()); // formatting suppressed return amount; }
         */
    }
}
