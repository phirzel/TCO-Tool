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
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.util.ParserCSV;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Manage the Application Settings by Properties file.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class ApplicationOptions extends java.util.Properties implements UserSettings {

	// values for Key-Values
	private final static char SEPARATOR = ';'; // @see
	// CsvSerializer#CELL_SEPARATOR
	protected final static String HOME_DIRECTORY = "user.home";

	// Property Keys (non-NLS)
	// @see getKeySet()
	private final static String LOOK_AND_FEEL = "LOOK_AND_FEEL";
	private final static String BACKGROUND_COLOR = "BACKGROUND_COLOR";
	private final static String FONT = "FONT";
	private final static String FOREGROUND_COLOR = "FOREGROUND_COLOR";
	private final static String IMPORT_DIRECTORY = "IMPORT_DIRECTORY";
	private final static String LANGUAGE = "LANGUAGE";
	private final static String COUNTRY = "COUNTRY";
	private final static String SHOW_STATUS_BAR = "SHOW_STATUS_BAR";
	private final static String SHOW_TOOLBAR = "SHOW_TOOLBAR";
	private final static String WORKING_DIRECTORY = "WORKING_DIRECTORY";
	private final static String LAST_FILES = "LAST_FILES";
	// private final static String QUERY_DELETION = "QUERY_DELETION";
	private final static String WINDOW_HEIGHT = "WINDOW_HEIGHT";
	private final static String WINDOW_WIDTH = "WINDOW_WIDTH";
	private final static String WINDOW_X = "WINDOW_X";
	private final static String WINDOW_Y = "WINDOW_Y";

	// variables
	private String filename = null;

	/**
	 * Create new Default Settings.
	 */
	protected ApplicationOptions() {
		super();

		setPlattformLocale(Locale.getDefault());

		// create Default
		setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		setBackgroundColor(java.awt.Color.white);
		// setFont("Default-PLAIN-9");
		setForegroundColor(java.awt.Color.black);
		setImportDirectory(System.getProperty(HOME_DIRECTORY));
		// setLanguage(java.util.Locale.GERMAN.getLanguage());
		// setCountry("CH");

		setShowStatusBar(Boolean.TRUE);
		setShowToolBar(Boolean.TRUE);
		setWorkingDirectory(System.getProperty(HOME_DIRECTORY));
		setLastFiles(new ArrayList<String>());

		setWindowHeight(Integer.valueOf(600));
		setWindowWidth(Integer.valueOf(800));
		setWindowX(Integer.valueOf(10));
		setWindowY(Integer.valueOf(10));
	}

	/**
	 * Load given Settings by filename.
	 */
	public ApplicationOptions(final String filename) {
		this(filename, new ApplicationOptions());
	}

	/**
	 * Load given Settings by filename.
	 */
	protected ApplicationOptions(final String filename, java.util.Properties defaults) {
		// create Default
		super(defaults);

		setPlattformLocale(Locale.getDefault());

		try {
			// load the persistent Properties -> overwrite keys
			this.filename = filename;

			FileInputStream inputStream = new FileInputStream(filename);
			/* tmp= */
			super.load(inputStream);
		} catch (FileNotFoundException fe) {
			Tracer.getInstance().runtimeWarning("File not found: " + fe.getLocalizedMessage());
		} catch (IOException ioe) {
			Tracer.getInstance().runtimeWarning("IO failure: " + ioe.getLocalizedMessage());
		}
	}

	/**
	 * Return whether the User is allowed to use Application or not.
	 *
	 * @return boolean
	 */
	@Override
	public boolean getActive() {
		return true;
	}

	/**
	 * Return whether the User is the Administrator himself.
	 *
	 * @return boolean
	 */
	@Override
	public boolean getAdmin() {
		return false;
	}

	/**
	 * Gets the backgroundColor property (java.awt.Color) value.
	 *
	 * @return The backgroundColor property value.
	 * @see #setBackgroundColor
	 */
	public java.awt.Color getBackgroundColor() {
		return new java.awt.Color(Integer.valueOf(getProperty(BACKGROUND_COLOR)).intValue());
	}

	/**
	 * Gets the Country property (java.lang.String) value.
	 *
	 * @return The Country String
	 * @see #setCountry
	 */
	@Override
	public java.lang.String getCountry() {
		String country = getProperty(COUNTRY);
		if (StringUtils.isNullOrEmpty(country)) {
			return Locale.getDefault().getCountry();
		} else {
			return country;
		}
	}

	/**
	 * Return font.
	 *
	 * @return The font property value.
	 * @see #setFont
	 */
	public Font getFont() {
		if (StringUtils.isNullOrEmpty(getFontString())) {
			return null;
		} else {
			return Font.decode(getFontString());
		}
	}

	/**
	 * Return font in a descriptive String.
	 *
	 * @return The font property value.
	 * @see #setFont
	 */
	public String getFontString() {
		return getProperty(FONT);
	}

	/**
	 * Gets the foregroundColor property (java.awt.Color) value.
	 *
	 * @return The foregroundColor property value.
	 * @see #setForegroundColor
	 */
	public java.awt.Color getForegroundColor() {
		return new java.awt.Color(Integer.valueOf(getProperty(FOREGROUND_COLOR)).intValue());
	}

	/**
	 * Gets the importDirectory property (java.lang.String) value.
	 *
	 * @return The importDirectory property value.
	 * @see #setImportDirectory
	 */
	public java.lang.String getImportDirectory() {
		return getProperty(IMPORT_DIRECTORY);
	}

	/**
	 * Gets the language property (java.lang.String) value.
	 *
	 * @return The language property value.
	 * @see #setLanguage
	 */
	@Override
	public java.lang.String getLanguage() {
		String language = getProperty(LANGUAGE);
		if (StringUtils.isNullOrEmpty(language)) {
			return Locale.getDefault().getLanguage();
		} else {
			return language;
		}
	}

	/**
	 * Gets the lastFiles opened property (java.lang.String) value.
	 *
	 * @see #setWorkingDirectory
	 */
	public java.util.List<String> getLastFiles() {
		return ParserCSV.stringToArray(getProperty(LAST_FILES), SEPARATOR);
	}

	/**
	 * Gets the 'Look & Feel' property (java.lang.String) value.
	 *
	 * @return The language property value.
	 * @see #setLookAndFeel(String)
	 */
	@Override
	public java.lang.String getLookAndFeel() {
		return getProperty(LOOK_AND_FEEL);
	}

	/**
	 * Return the e-Mail Provider host to send e-Mails.
	 *
	 * @return java.lang.String (for e.g. "mail.bluewin.ch")
	 */
	@Override
	public java.lang.String getProviderSMTP() {
		return null;
	}

	/**
	 * Gets the showStatusBar property (java.lang.Boolean) value.
	 *
	 * @return The showStatusBar property value.
	 * @see #setShowStatusBar
	 */
	public java.lang.Boolean getShowStatusBar() {
		return Boolean.valueOf(getProperty(SHOW_STATUS_BAR));
	}

	/**
	 * Gets the showToolBar property (java.lang.Boolean) value.
	 *
	 * @return The showToolBar property value.
	 * @see #setShowToolBar
	 */
	public java.lang.Boolean getShowToolBar() {
		return Boolean.valueOf(getProperty(SHOW_TOOLBAR));
	}

	/**
	 * Return the User's id, by means the login Id to the current application.
	 *
	 * @return String (for e.g. "phirzel")
	 * @see java.util.Locale
	 */
	@Override
	public java.lang.String getUserId() {
		// there is no specific User or Login-Id
		return "<NONE>";
	}

	/**
	 * Return property.
	 */
	public java.lang.Integer getWindowHeight() {
		return Integer.valueOf(getProperty(WINDOW_HEIGHT));
	}

	/**
	 * Return property.
	 */
	public java.lang.Integer getWindowWidth() {
		return Integer.valueOf(getProperty(WINDOW_WIDTH));
	}

	/**
	 * Return property.
	 */
	public java.lang.Integer getWindowX() {
		return Integer.valueOf(getProperty(WINDOW_X));
	}

	/**
	 * Return property.
	 */
	public java.lang.Integer getWindowY() {
		return Integer.valueOf(getProperty(WINDOW_Y));
	}

	/**
	 * Gets the workingDirectory property (java.lang.String) value.
	 *
	 * @return The workingDirectory property value.
	 * @see #setWorkingDirectory
	 */
	@Override
	public java.lang.String getWorkingDirectory() {
		String tmp = getProperty(WORKING_DIRECTORY);
		if (tmp == null) {
			return System.getProperty("user.home");
		} else {
			return tmp;
		}
	}

	/**
	 * Save the UserSettings.
	 */
	@Override
	public final void save() {
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			super.store(outputStream, "User Properties <" + filename + ">");
		} catch (Exception e) {
			Tracer.getInstance().runtimeWarning("IGNORE: Failed for User Properties <" + filename + ">");
		}
	}

	/**
	 * Sets the backgroundColor property (java.awt.Color) value.
	 *
	 * @param backgroundColor The new value for the property.
	 * @see #getBackgroundColor
	 */
	public void setBackgroundColor(java.awt.Color backgroundColor) {
		setProperty(BACKGROUND_COLOR, (Integer.valueOf(backgroundColor.getRGB())).toString());
	}

	/**
	 * Sets the Country property (java.lang.String) value.
	 *
	 * @param language (for e.g. "CH"; "FR", etc)
	 * @see #getCountry
	 */
	public void setCountry(java.lang.String country) {
		setProperty(COUNTRY, country);
	}

	/**
	 * Transform given font into String-Description.
	 *
	 * @param font
	 * @see setFont(String)
	 */
	public void setFont(java.awt.Font font) {
		if (font == null) {
			setProperty(FONT, null);
		} else {
			String s = font.getFamily() + "-";
			switch (font.getStyle()) {
				case Font.BOLD:
					s = s + "BOLD";
					break;
				case Font.ITALIC:
					s = s + "ITALIC";
					break;
				case (Font.BOLD + Font.ITALIC):
					s = s + "BOLDITALIC";
					break;
				default:
					s = s + "PLAIN";
					break;
			}
			setProperty(FONT, s + "-" + font.getSize());
		}
	}

	/**
	 * Sets the foregroundColor property (java.awt.Color) value.
	 *
	 * @param foregroundColor The new value for the property.
	 * @see #getForegroundColor
	 */
	public void setForegroundColor(java.awt.Color foregroundColor) {
		setProperty(FOREGROUND_COLOR, (Integer.valueOf(foregroundColor.getRGB())).toString());
	}

	/**
	 * Sets the importDirectory property (java.lang.String) value.
	 *
	 * @param importDirectory The new value for the property.
	 * @see #getImportDirectory
	 */
	public void setImportDirectory(java.lang.String importDirectory) {
		setProperty(IMPORT_DIRECTORY, importDirectory);
	}

	/**
	 * Sets the language property (java.lang.String) value.
	 *
	 * @param language (for e.g. "de"; "fr", etc)
	 * @see #getLanguage
	 */
	public void setLanguage(java.lang.String language) {
		setProperty(LANGUAGE, language);
	}

	/**
	 * Sets the lastFiles opened property (java.lang.String) value.
	 *
	 * @param 0..n Files separated by semicolon ';'.
	 * @see #getLastFiles
	 */
	public void setLastFiles(java.util.List<String> lastFiles) {
		setProperty(LAST_FILES, ParserCSV.arrayToString(lastFiles, SEPARATOR));
	}

	/**
	 * Sets the 'Look & Feel' property (java.lang.String) value. This Font is used for graphical nodes and edges.
	 *
	 * @param font The new value for the property.
	 * @see #getLookAndFeel
	 */
	public void setLookAndFeel(String string) {
		setProperty(LOOK_AND_FEEL, string);
	}

	/**
	 * Sets the showStatusBar property (java.lang.Boolean) value.
	 *
	 * @param showStatusBar The new value for the property.
	 * @see #getShowStatusBar
	 */
	public void setShowStatusBar(java.lang.Boolean showStatusBar) {
		setProperty(SHOW_STATUS_BAR, showStatusBar.toString());
	}

	/**
	 * Sets the showToolBar property (java.lang.Boolean) value.
	 *
	 * @param showToolBar The new value for the property.
	 * @see #getShowToolBar
	 */
	public void setShowToolBar(java.lang.Boolean showToolBar) {
		setProperty(SHOW_TOOLBAR, showToolBar.toString());
	}

	/**
	 * Sets a property (java.lang.String) value.
	 *
	 * @param value
	 */
	public void setWindowHeight(java.lang.Integer value) {
		setProperty(WINDOW_HEIGHT, value.toString());
	}

	/**
	 * Sets a property (java.lang.String) value.
	 *
	 * @param value
	 */
	public void setWindowWidth(java.lang.Integer value) {
		setProperty(WINDOW_WIDTH, value.toString());
	}

	/**
	 * Sets a property (java.lang.String) value.
	 *
	 * @param value
	 */
	public void setWindowX(java.lang.Integer value) {
		setProperty(WINDOW_X, value.toString());
	}

	/**
	 * Sets a property (java.lang.String) value.
	 *
	 * @param value
	 */
	public void setWindowY(java.lang.Integer value) {
		setProperty(WINDOW_Y, value.toString());
	}

	/**
	 * Sets the workingDirectory property (java.lang.String) value.
	 *
	 * @param workingDirectory The new value for the property.
	 * @see #getWorkingDirectory
	 */
	public void setWorkingDirectory(java.lang.String workingDirectory) {
		setProperty(WORKING_DIRECTORY, workingDirectory);
	}

	@Override
	public synchronized Object setProperty(String key, String value) {
		return super.setProperty(key, value == null ? "" : value);
	}

	private transient Locale plattformLocale = null;

	/**
	 * Cache OS locale before a reset of default Locale might happen by these settings.
	 *
	 * @see UserSettings#setPlattformLocale(Locale)
	 */
	protected final void setPlattformLocale(Locale locale) {
		if (locale == null) {
			throw new IllegalArgumentException("locale must not be null!");
		}
		plattformLocale = locale;
		Tracer.getInstance().debug("Platform Locale: " + (locale == null ? "null" : locale.toString()));
	}

	/**
	 * In multi-language applications the change of the default locale because of the language only might also influence other platform settings badly (such as NumberFormat for e.g.).
	 * <p>
	 * This field allows temporary keeping the operating systems default region for reuse in any number-formats.
	 *
	 * @param locale
	 * @see ch.softenvironment.util.NlsUtils#changeLocale(Locale)
	 */
	public final Locale getPlattformLocale() {
		return (plattformLocale == null ? Locale.getDefault() : plattformLocale);
	}
}
