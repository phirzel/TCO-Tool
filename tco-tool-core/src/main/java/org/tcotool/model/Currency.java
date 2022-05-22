package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;

/**
 * Currency Enumeration according to ISO 4217 (3 letter alphanumeric code).
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see http://www.iso.org/iso/en/prods-services/popstds/currencycodeslist.html
 */
public final class Currency extends DbEnumeration {

    // IliCode definition according to ISO 4217:
    public final static String CHF = "CHF"; // Swiss Franc
    public final static String EUR = "EUR"; // Euro
    public final static String USD = "USD"; // American Dollar
    public final static String JPY = "JPY"; // Japanese Yen
    public final static String CFA = "CFA"; // Cameroun CFA Franc
    public final static String INR = "INR"; // Indian Rupee
    public final static String AUD = "AUD"; // Australian Dollar
    public final static String CAD = "CAD"; // Canada Dollar
    public final static String XAF = "XAF"; // Central African Republic
    public final static String BRL = "BRL"; // Brasil Real
    public final static String NZD = "NZD"; // New Zealand Dollar
    public final static String HKD = "HKD"; // Hong Kong Dollar
    public final static String NOK = "NOK"; // Norwegian Krone
    public final static String DKK = "DKK"; // Danish Krone
    public final static String SEK = "SEK"; // Swedish Krone
    public final static String ISK = "ISK"; // Iceland Krone
    public final static String CNY = "CNY"; // Chinese Yuan Renminbi
    public final static String RUB = "RUB"; // Russian Ruble

    public Currency(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return the database mappings for this persistence object.
     *
     * @see DbObjectServer.register()
     * @see ch.softenvironment.jomm.DbConnection.addDescriptor()
     */
    public static DbDescriptor createDescriptor() {
        return DbEnumeration.createDefaultDescriptor(Currency.class);
    }
}
