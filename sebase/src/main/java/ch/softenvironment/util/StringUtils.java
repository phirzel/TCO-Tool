package ch.softenvironment.util;

import ch.softenvironment.client.ResourceManager;
import java.io.File;

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
 * Set of reusable String Utilities.
 *
 * @author Peter Hirzel
 */
public abstract class StringUtils {

	// @see java.nio.charset.Charset
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";

	/**
	 * Return PackageName of given class.
	 */
	public static String getPackageName(java.lang.Class<?> type) {
		String className = type.getName();
		int index = className.lastIndexOf('.');
		return className.substring(0, index);
	}

	/**
	 * Return PackageName of an Instance.
	 */
	public static String getPackageName(Object object) {
		return getPackageName(object.getClass());
	}

	/**
	 * Return ClassName of given class without package path.
	 */
	public static String getPureClassName(java.lang.Class<?> type) {
		/*
		 * StringBuffer className = new StringBuffer(aclass.getName());
		 * className.delete(0, aclass.getName().lastIndexOf('.') + 1); return
		 * className.toString();
		 */
		String className = type.getName();
		int index = className.lastIndexOf('.');
		return className.substring(index + 1);
	}

	/**
	 * Return ClassName of an Instance without package path.
	 */
	public static String getPureClassName(Object object) {
		return getPureClassName(object.getClass());
	}

	/**
	 * Return either String of value or empty String if nothing contained.
	 *
	 * @return String
	 */
	public static String getString(Object value) {
		if (value == null) {
			return "";
		} else if (value instanceof Boolean) {
			if (((Boolean) value).booleanValue()) {
				return ResourceManager.getResource(StringUtils.class, "CI_Yes_text");
			} else {
				return ResourceManager.getResource(StringUtils.class, "CI_No_text");
			}
		} else {
			return value.toString();
		}
	}

	/**
	 * Cut off value if longer than given length for e.g. getStringLimited("abcdefgh", 5) => "abcd.."
	 *
	 * @param value
	 * @param length
	 * @return
	 */
	public static String getStringLimited(String value, int length) {
		if (!isNullOrEmpty(value) && (value.length() > length)) {
			// replace last character by elipsis
			return value.substring(0, length - 1) + ((char) 1461) /* elipsis=".." */;
		}
		return value;
	}

	/**
	 * Return next extracted textual word in value String.
	 *
	 * @param value
	 * @param startAt
	 * @return "" if no next word
	 */
	public static String getNextWord(String value, int startAt) {
		if (!isNullOrEmpty(value)) {
			if (value.length() > startAt) {
				String temp = value.substring(startAt).trim();
				int index = temp.indexOf(" ");
				// TODO check other word separators such as .,;!?:
				if (index > 0) {
					return temp.substring(0, index);
				}
				if (temp.length() > 0) {
					char last = temp.charAt(temp.length() - 1);
					if ((last == '.') || (last == '!') || (last == ',') || (last == ';') || (last == ':') || (last == '?')) {
						temp = temp.substring(0, temp.length() - 1);
					}
					return temp;
				}
			}
		}
		return "";
	}

	/**
	 * Return whether String is null or contains nothing.
	 *
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(String value) {
		return ((value == null) || (value.trim().length() == 0));
	}

	/**
	 * @see #replace(StringBuffer, String, String)
	 */
	public static String replace(String source, String searchTerm, String replacement) {
		if (source == null) {
			return null;
		} else {
			return replace(new StringBuffer(source), searchTerm, replacement);
		}
	}

	/**
	 * Replace all occurrences of searchTerm by replacement in buffer, for e.g. StringUtils.replace("X AS C, Attr1 AS Dummy", " AS ", " ") => "X C, Attr1 Dummy"
	 *
	 * @return String
	 */
	public static String replace(StringBuffer buffer, String searchTerm, String replacement) {
		if (buffer == null) {
			return null;
		}
		if (!isNullOrEmpty(buffer.toString())) {
			int index = -1;
			while ((index = buffer.indexOf(searchTerm, (index == -1 ? index : index + replacement.length()))) > -1) {
				buffer.delete(index, index + searchTerm.length());
				buffer.insert(index, replacement);
			}
		}
		return buffer.toString();
	}

	/**
	 * Return a String representation of a boolean.
	 *
	 * @return X => true; <emptyString> => false
	 */
	public static String getBooleanString(Boolean value) {
		if (value == null) {
			return "";
		} else if (value.booleanValue()) {
			return ch.softenvironment.client.ResourceManager.getResource(StringUtils.class, "CI_Yes_text");
		} else {
			return ch.softenvironment.client.ResourceManager.getResource(StringUtils.class, "CI_No_text");
		}
	}

	/**
	 * Append the given suffix to given filename if not already added and return expression.
	 *
	 * @param filename
	 * @param suffix
	 * @return
	 */
	public static String tryAppendSuffix(String filename, String suffix) {
		if (filename.toLowerCase().endsWith(suffix.toLowerCase())) {
			return filename;
		} else {
			return filename + suffix;
		}
	}

	public static String firstLetterToLowercase(String value) {
		if (isNullOrEmpty(value)) {
			return value;
		} else {
			StringBuffer buffer = new StringBuffer(value);
			buffer.replace(0, 1, buffer.substring(0, 1).toLowerCase());
			return buffer.toString();
		}
	}

	public static String firstLetterToUppercase(String value) {
		if (isNullOrEmpty(value)) {
			return value;
		} else {
			StringBuffer buffer = new StringBuffer(value);
			buffer.replace(0, 1, buffer.substring(0, 1).toUpperCase());
			return buffer.toString();
		}
	}

	/**
	 * Make the given filename a real creatable filename in OS filesystem. (Some characters are not welcome, such as: ':', '/' etc)
	 *
	 * @param filename
	 * @param replacmenetSeparator
	 * @return
	 */
	public static String makeValidFileName(String filename, String replacmenetSeparator) {
		// remove any kind of File-separator that would spoil the meant
		// structure
		String fn = StringUtils.replace(filename, ":", replacmenetSeparator);
		fn = StringUtils.replace(fn, "\"", replacmenetSeparator);
		fn = StringUtils.replace(fn, "\\", replacmenetSeparator);
		fn = StringUtils.replace(fn, "/", replacmenetSeparator);
		return StringUtils.replace(fn, File.pathSeparator, replacmenetSeparator);
	}
}
