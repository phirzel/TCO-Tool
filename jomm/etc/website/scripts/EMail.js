//------------------------------------------------------------------------------

//                   http://www.softEnvironment.ch

//------------------------------------------------------------------------------

//  File: EMail.js

//

//  Edition History:

//  ----------------

//  Version	Date		By		Changes made

//  -------------------------------------------------

//  1.0.0	11.08.2003	Peter Hirzel	Original

//  1.0.1	19.08.2003	Peter Hirzel	getMailLink (NEW)

//  1.0.2	01.09.2003	Peter Hirzel	getMailLinkWithText (NEW)

//------------------------------------------------------------------------------

// Description: Prevent EMail-Spam by coded E-Mail Links.

//------------------------------------------------------------------------------


/**
 * return "address@domain"
 */
function getMail(address, domain) {
    return address + "@" + domain;
}


/**
 * return "mailto:address@domain?subject"
 */
function getMailTo(address, domain, subject) {
    if (subject == null) {
        return "mailto:" + getMail(address, domain);
    } else {
        return "mailto:" + getMail(address, domain) + "?subject=" + subject;
    }
}


/**
 * return "<a href='mailto:address@domain?subject'>Mail to:</a>&nbsp;address@domain"
 */
function getMailToRef(address, domain, subject) {
    return "<a href='" + getMailTo(address, domain, subject) + "'>Mail to:</a>&nbsp;" + getMail(address, domain);
}


/**
 * return "<a href='mailto:address@domain?subject'>Mail to:</a>&nbsp;userText"
 */
function getMailToRef(address, domain, subject, userText) {
    return "<a href='" + getMailTo(address, domain, subject) + "'>Mail to:</a>&nbsp;" + userText;
}

/**
 * return "<a href='mailto:address@domain?subject'>text</a>"
 */
function getMailLinkWithText(address, domain, subject, text) {
    return "<a href='" + getMailTo(address, domain, subject) + "'>" + text + "</a>";
}


/**
 * return "<a href='mailto:address@domain?subject'>address@domain</a>"
 */
function getMailLink(address, domain, subject) {
    return getMailLinkWithText(address, domain, subject, getMail(address, domain));
}