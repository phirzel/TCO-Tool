package ch.softenvironment.jomm.serialize;
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
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.StringUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

/**
 * Writer-Tool to encode comma separated files (*.CSV).
 *
 * @author Peter Hirzel
 * @since 1.4 (2008-03-14)
 */
public class CsvSerializer extends ch.softenvironment.jomm.serialize.SimpleSerializer {

	public static final char CELL_SEPARATOR = ';'; // default MS Excel cell separator
	private char separator = CELL_SEPARATOR;

	/**
	 * WriterCSV constructor comment.
	 */
	public CsvSerializer(Writer out) {
		super(out);
	}

	/**
	 * Add a Cell to OutStream.
	 */
	public void cell(Object value) throws IOException {
		if (value != null) {
			if (value instanceof String) {
				nativeContent(encodeString(value.toString()));
			} else if (value instanceof java.util.Date) {
				nativeContent(encodeDate((java.util.Date) value));
			} else if (value instanceof Boolean) {
				nativeContent(encodeBoolean((Boolean) value));
			} else if (value instanceof Number) {
				nativeContent(encodeNumber((Number) value));
			} else {
				// as is
				nativeContent(value.toString());
			}
		}
		colSpan(1);
	}

	/**
	 * Analog HTML colspan in Tables.
	 *
	 * @param numberOfCells to connect together
	 */
	public void colSpan(int numberOfCells) throws IOException {
		for (int i = 0; i < numberOfCells; i++) {
			out.write(separator);
		}
	}

	/**
	 * Encode a Boolean in a serialized manner.
	 *
	 * @return serialized String
	 */
	@Override
	public java.lang.String encodeBoolean(java.lang.Boolean value) {
		return ch.softenvironment.util.StringUtils.getBooleanString(value);
	}

	/**
	 * Encode a Date in a serialized manner.
	 *
	 * @return serialized Date
	 */
	@Override
	public java.lang.String encodeDate(java.util.Date value) {
		return ch.softenvironment.util.NlsUtils.formatDate(value);
	}

	/**
	 * Encode a String in a serialized manner.
	 *
	 * @return serialized String
	 */
	@Override
	public java.lang.String encodeString(java.lang.String value) {
		if (StringUtils.isNullOrEmpty(value)) {
			return "";
		}

		//TODO hardcoded
		char cellSeparatorReplacement = ',';
		if (cellSeparatorReplacement == separator) {
			throw new DeveloperException("use another separator");
		}
		String text = value.replace(separator, cellSeparatorReplacement);

		//TODO tested for Windows only
		return text.replace('\n' /*OK for Windows/XP */, cellSeparatorReplacement);
	}

	/**
	 * Set the separating Character between Cells in a CSV. Default: ';'
	 */
	public void setCellSeparator(char separator) {
		this.separator = separator;
	}

	/* (non-Javadoc)
	 * @see ch.softenvironment.w3.Serializer#encodeTime(java.util.Date)
	 */
	@Override
	public String encodeTime(Date value) {
		return NlsUtils.formatTime(value);
	}

	/* (non-Javadoc)
	 * @see ch.softenvironment.w3.Serializer#encodeDateTime(java.util.Date)
	 */
	@Override
	public String encodeDateTime(Date value) {
		return NlsUtils.formatDateTime(value);
	}

	/* (non-Javadoc)
	 * @see ch.softenvironment.w3.Serializer#encodeNumber(java.lang.Number)
	 */
	@Override
	public String encodeNumber(Number value) {
		return value.toString();
	}
}
