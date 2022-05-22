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
 */

/**
 * Interface for a minimal UserProfile resp. User-Settings for a specific Application.
 *
 * @author Peter Hirzel softEnvironment GmbH
 */
public interface UserSettings {

    /**
     * Return whether the User is allowed to use Application or not.
     *
     * @return boolean
     */
    boolean getActive();

    /**
     * Return whether the User is the Administrator himself.
     *
     * @return boolean
     */
    boolean getAdmin();

    /**
     * Return the User's Country.
     *
     * @return String (for e.g. "CH", "FR", etc)
     * @see java.util.Locale
     */
    String getCountry();

    /**
     * Return the User's working language with the Application.
     *
     * @return java.lang.String (for e.g. "de", "fr" etc)
     * @see java.util.Locale
     */
    String getLanguage();

    /**
     * Gets the 'Look & Feel' property (java.lang.String) value.
     *
     * @return The language property value.
     * @see BaseFrame#setLookAndFeel(String)
     */
    java.lang.String getLookAndFeel();

    /**
     * Return the e-Mail Provider host to send e-Mails.
     *
     * @return java.lang.String (for e.g. "mail.bluewin.ch")
     */
    String getProviderSMTP();

    /**
     * Return the User's id, by means the login Id to the current application.
     *
     * @return String (for e.g. "phirzel")
     * @see java.util.Locale
     */
    String getUserId();

    /**
     * Return the WorkingDirectory of the current application.
     *
     * @return String (for e.g. "C:\\TEMP")
     * @see java.util.Locale
     */
    String getWorkingDirectory();

    /**
     * Save the UserProfile.
     */
    void save() throws Exception;
}
