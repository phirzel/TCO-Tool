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

import ch.softenvironment.util.DeveloperException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manage a Map of Resources, respectively *.properties for different Classes dealing with UI. Resources are usually desired by an Application for a specific Locale (for e.g. de_CH), therefore the
 * performance may be enhanced if the mapped *.properties files are cached during runtime.
 *
 * @author Peter Hirzel
 */
public class ResourceManager {
	private static final String ELIPSIS = "...";
	// singleton
	private static ResourceManager manager = null;
	// translation cache per language and MyClass.properties content
	private final Map<String, Map<Class<?>, java.util.ResourceBundle>> resources = new HashMap<>();

	/**
	 * Return an application wide ResourceManager. Design Pattern: Singleton.
	 *
	 * @return ResourceManager
	 */
	private static ResourceManager getInstance() {
		if (manager == null) {
			manager = new ResourceManager();
		}
		return manager;
	}

	/**
	 * Return the resource as is in *.properties file for the given Locale (NLS translation) using a specific ClassLoader (useful for reflection frameworks such as JPF).
	 *
	 * @see #getResource(java.lang.Class, Locale, String, ClassLoader)
	 */
	public static String getResource(java.lang.Class<?> owner, String propertyName, ClassLoader loader) {
		return getInstance().getResource(owner, Locale.getDefault(), propertyName, loader);
	}

	/**
	 * @see #getResource(Class, String, Locale)
	 */
	public static String getResource(java.lang.Class<?> owner, String propertyName) {
		return getResource(owner, propertyName, Locale.getDefault());
	}

	/**
	 * Return the resource as is in *.properties file for the given Locale (NLS translation).
	 *
	 * @param owner
	 * @param propertyName
	 * @param locale
	 * @return
	 */
	public static String getResource(java.lang.Class<?> owner, String propertyName, Locale locale) {
		return getInstance().getResource(owner, locale, propertyName, null);
	}

	/**
	 * Return an ImageIcon.
	 *
	 * @return ImageIcon
	 */
	public static javax.swing.ImageIcon getImageIcon(Class aClass, String imageFileName) {
		return new javax.swing.ImageIcon(getURL(aClass, imageFileName));
	}

	/**
	 * Return URL of given filename in a Package named "resources" relative to
	 * the given Class' path.
	 *
	 * @param aClass
	 * @param fileName
	 * @return URL
	 */
	public static URL getURL(Class aClass, String fileName) {
		String className = aClass.getName();
		int index = className.lastIndexOf('.');
		String file = className.substring(0, index).replace('.', '/') + "/" + fileName;

		// var I) from IDE with relative FileSystem or compiled within Jar
		URL url = aClass.getResource("/" + file);

		// var II) from JAR with External FileSystem
		//	java.net.URL url = getClass().getResource(file);

		return url;
	}

	/**
	 * Return NLS-String for a certain Property.
	 *
	 * @param holder       A <holder-Class>[_<language>].properties file must exist
	 * @param locale
	 * @param propertyName
	 * @param loader       null for default ClassLoader
	 * @return String NLS-String
	 */
	private String getResource(java.lang.Class<?> holder, Locale locale, String propertyName, ClassLoader loader) throws MissingResourceException {
		java.util.ResourceBundle bundle = getBundle(holder, locale, loader);
		if (bundle == null) {
			throw new DeveloperException("no bundle for holder=" + holder.getName());
		}
		return bundle.getString(propertyName);
	}

	/**
	 * Return cached bundle or instantiate it otherwise.
	 *
	 * @param holder
	 * @param locale
	 * @param loader
	 * @return
	 */
	private java.util.ResourceBundle getBundle(Class<?> holder, Locale locale, ClassLoader loader) {
		java.util.Map<Class<?>, java.util.ResourceBundle> languages = null;
		if (resources.containsKey(locale.getLanguage())) {
			languages = resources.get(locale.getLanguage());
		} else {
			languages = new HashMap<>();
			resources.put(locale.getLanguage(), languages);
		}
		if (languages.containsKey(holder)) {
			return languages.get(holder);
		} else {
			java.util.ResourceBundle bundle;
			if (loader == null) {
				bundle = ResourceManager.getBundle(holder, locale);
			} else {
				bundle = getBundle(holder, locale, loader);
			}
			languages.put(holder, bundle);
			return bundle;
		}
	}

	/**
	 * Get the appropriate ResourceBundle for a given Locale.
	 * Uses transformName() to get the right bundle name.
	 */

	//TODO HIP ehi version! non wanted
	@Deprecated(since = "NON ehi.basics")
	public static java.util.ResourceBundle getBundle(Class baseClass, java.util.Locale locale) {
		return java.util.ResourceBundle.getBundle(transformName(baseClass.getName()), locale);
	}
	/*
	 * Get the appropriate ResourceBundle for a given Locale and ClassLoader.
	 * Uses transformName() to get the right bundle name.

	@Deprecated(since="NON ehi.basics")
	public static java.util.ResourceBundle getBundle(Class baseClass, java.util.Locale locale, ClassLoader loader) {
		return java.util.ResourceBundle.getBundle(transformName(baseClass.getName()), locale, loader);
	}*/

	/**
	 * transform a class name into a corresponding resource bundle name.
	 * insert 'resources' into the given fully qualified classname.
	 */
	@Deprecated(since = "NON ehi.basics")
	private static String transformName(String className) {
		StringBuffer resourceName = new StringBuffer(className);
		return resourceName.toString();
	}

	private static String getResource(java.lang.Class<?> owner, String propertyName, boolean asLabel) {
		try {
			String resource = getInstance().getResource(owner, Locale.getDefault(), propertyName, null).trim();
			return convertLabel(resource, asLabel);
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Return an NLS-resource for a class. Labels end according to GUI-Standards with ":", therefore this method makes sure, the colon is set, though the resource might be defined otherwise.
	 *
	 * @see #getResource(Class, String, ClassLoader)
	 */
	public static String getResourceAsLabel(java.lang.Class<?> owner, String propertyName) {
		return getResource(owner, propertyName, true);
	}

	/**
	 * Return NLS-Translation with "...", which is common for labels in menu's showing up a sub-window.
	 *
	 * @see #getResourceAsNonLabeled(Class, String)
	 */
	public static String getResourceAsContinued(java.lang.Class<?> owner, String propertyName) {
		String resource = getResourceAsNonLabeled(owner, propertyName);
		if ((resource != null) && (!resource.endsWith(ELIPSIS))) {
			return resource + ELIPSIS;
		} else {
			return resource;
		}
	}

	/**
	 * Make sure the ":" is extracted from NLS-Translation if provided in *.properties file.
	 *
	 * @param owner
	 * @param propertyName
	 * @return
	 */
	public static String getResourceAsNonLabeled(java.lang.Class<?> owner, String propertyName) {
		return getResource(owner, propertyName, false);
	}

	/**
	 * Return the first translation found for key matching given pattern. This might be useful for generic mappings for e.g. from an Object properties to a Label corresponding in a GUI.
	 *
	 * @param owner
	 * @param pattern (for e.g. "LblMy[a-zA-Z0-9]*_text")
	 * @param asLabel
	 * @return
	 * @throws MissingResourceException
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html">Pattern</a>
	 */
	public static String getResourceMatch(java.lang.Class<?> owner, String pattern, boolean asLabel) throws MissingResourceException {
		java.util.ResourceBundle bundle = getInstance().getBundle(owner, Locale.getDefault(), null);
		Enumeration<String> keys = bundle.getKeys();
		Pattern p = Pattern.compile(pattern); // "Lbl*myKey*_text"
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Matcher m = p.matcher(key);
			if (m.matches()) {
				return convertLabel(bundle.getString(key), asLabel);
			}
		}
		throw new MissingResourceException("No key matching pattern: " + pattern, owner.getName(), pattern);
	}

	/**
	 * Add or cut off ':'.
	 *
	 * @param resource
	 * @param asLabel
	 * @return
	 */
	private static String convertLabel(String resource, boolean asLabel) {
		if (resource == null) {
			return resource;
		}

		if (asLabel) {
			if (resource.charAt(resource.length() - 1) != ':') {
				// add ":"
				return resource + ":";
			}
		} else {
			if (resource.charAt(resource.length() - 1) == ':') {
				// cut off ":"
				return resource.substring(0, resource.length() - 1);
			}
		}
		return resource;
	}
}
