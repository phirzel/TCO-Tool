package ch.softenvironment.view;

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.I18nTranslator;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Example usage:
 * JFileChooser fc = new JFileChooser();
 * fc.addChoosableFileFilter(new GenericFileFilter("INTERLIS models (*.ili)","ili"));
 */
public class GenericFileFilter extends FileFilter implements java.io.FileFilter, I18nTranslator {
	private final String description;
	private final String extension;

	public GenericFileFilter(String descriptionResourceName, String extension) {
		this.description = getResourceString(descriptionResourceName);
		this.extension = extension;
	}

	@Override
	public String getResourceString(String property) {
		return ResourceManager.getResource(this.getClass(), property);
	}

	/**
	 * Accept all directories, with or without extension, and all files with given extension
	 */
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String ext = getFileExtension(f);
		if (ext != null) {
			return ext.equalsIgnoreCase(extension);
		}

		return false;
	}

	/**
	 * Comma Separated Values (ASCII).
	 *
	 * @return specific File-Filter.
	 */
	public static GenericFileFilter createCsvFilter() {
		return new GenericFileFilter("CICsvFilter", "csv");//$NON-NLS-2$ //$NON-NLS-1$
	}

	/**
	 * MS Excel Spreadsheet (binary).
	 *
	 * @return specific File-Filter.
	 */
	public static GenericFileFilter createXlsFilter() {
		return new GenericFileFilter("CIMSExcel", "xls");//$NON-NLS-2$ //$NON-NLS-1$
	}

	/**
	 * HTML (ASCII).
	 *
	 * @return specific File-Filter.
	 */
	public static GenericFileFilter createHtmlFilter() {
		return new GenericFileFilter("CIHtmlFilter", "html");//$NON-NLS-2$ //$NON-NLS-1$
	}

	/**
	 * SQL (ASCII).
	 *
	 * @return specific File-Filter.
	 */
	public static GenericFileFilter createSqlFilter() {
		return new GenericFileFilter("CISqlFilter", "sql");//$NON-NLS-2$ //$NON-NLS-1$
	}

	/**
	 * XML (ASCII).
	 *
	 * @return specific File-Filter.
	 */
	public static GenericFileFilter createXmlFilter() {
		return new GenericFileFilter("CIXmlFilter", "xml");//$NON-NLS-2$ //$NON-NLS-1$
	}

	/**
	 * XSD (ASCII).
	 *
	 * @return specific File-Filter.
	 */
	public static GenericFileFilter createXmlSchemaFilter() {
		return new GenericFileFilter("CIXsdFilter", "xsd");
	}

	/**
	 * The description of this filter
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The file extension used by this filter
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Get the extension of a file.
	 *
	 * @return e.g. "xml"
	 */
	public static String getFileExtension(File f) {
		String s = f.getName();
		return getFileExtension(s);
	}

	/**
	 * Get the extension of a file name.
	 *
	 * @return e.g. "xml"
	 */
	public static String getFileExtension(String s) {
		String ext = null;
		int i = s.lastIndexOf('.');

		if (i >= 0 && i < s.length()) {
			ext = s.substring(i + 1);
		}
		return ext;
	}

	/**
	 * Get the filename without extension.
	 *
	 * @return e.g. "export"
	 */
	public static String stripFileExtension(String s) {
		String name = s;
		int i = s.lastIndexOf('.');

		if (i >= 0 && i < s.length()) {
			name = s.substring(0, i);
		}
		return name;
	}

	/**
	 * Get the filename without extension.
	 *
	 * @return e.g. "export"
	 */
	public static String stripFileExtension(File f) {
		String s = f.getName();
		return stripFileExtension(s);
	}
}
