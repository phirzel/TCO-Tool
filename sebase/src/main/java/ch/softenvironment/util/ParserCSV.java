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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import javax.swing.JTable;
import lombok.extern.slf4j.Slf4j;

/**
 * Parser-Tool to parse comma separated files (*.CSV).
 *
 * @author Peter Hirzel
 */
@Slf4j
public class ParserCSV {

    public static final char DEFAULT_SEPARATOR = ';'; // @see
    // CsvSerializer#CELL_SEPARATOR
    private char separator = DEFAULT_SEPARATOR;
    private String line = null;
    private int lastIndex = -1;

    /**
     * ParserCSV constructor comment.
     *
     * @param line (for e.g. "field1;field2;...")
     * @param separator (for e.g. ";")
     */
    public ParserCSV(String line, char separator) {
        super();
        if (line == null) {
            throw new IllegalArgumentException("line must not be null");
        }
        this.line = line;
        this.separator = separator;
    }

    /**
     * Transform a List into a String with items (according to their #toString()) separated by a Separator.
     *
     * @param items
     * @return String
     */
    public static String arrayToString(java.util.List<String> items, char separator) {
        String serializedList = "";
        if (items == null) {
            return serializedList;
        } else {
            java.util.Iterator<?> iterator = items.iterator();
            while (iterator.hasNext()) {
                serializedList = serializedList + iterator.next() + separator;
            }
            return serializedList;
        }
    }

    /**
     * Return next Integer between to Separators.
     *
     * @return null if no next
     */
    public Integer getNextInteger() throws NumberFormatException {
        String s = getNextString();
        if (StringUtils.isNullOrEmpty(s)) {
            return null;
        } else {
            return Integer.valueOf(s);
        }
    }

    /**
     * Return a Substring between to Separators.
     *
     * @return null if no next or empty
     */
    public String getNextString() {
        String result = null;
        int to = line.indexOf(separator, lastIndex + 1);

        if (to > -1) {
            result = line.substring(lastIndex + 1, to);
            lastIndex = to;
        } else if ((lastIndex < line.length()) && (lastIndex + 1 < line.length())) {
            // treat end of line as Separator
            result = line.substring(lastIndex + 1);
            lastIndex = line.length();
            log.warn("Developer warning: Parser-content not terminated by expected separator");
        } else {
            // end of line reached
            return null;
        }
        /*
         * // eliminate "double-quotes" around String => quotes // Excel
         * introduces such quotes in case a cell contains separator-like
         * Characters if (result.startsWith("\"", 0) && result.endsWith("\"")) {
         * result = result.substring(1, result.length() - 1); }
         *
         * // strip falsely generated double quotes int from = 0; while ((to =
         * result.indexOf("\"\"", from)) > -1) { // always eliminate 2nd '\"'
         * result = result.substring(0, to + 1) + result.substring(to + 2,
         * result.length()); from = to + 1; }
         */
        return result.trim();
    }

    /**
     * Return next Boolean value.
     *
     * @return
     */
    public Boolean getNextBoolean() {
        String value = getNextString();
        if (!StringUtils.isNullOrEmpty(value)) {
            value = value.trim().toUpperCase();
            if (value.equals("TRUE") || value.equals("YES") || value.equals("Y") || value.equals("1")) {
                return Boolean.TRUE;
            } else if (value.equals("FALSE") || value.equals("NO") || value.equals("N") || value.equals("0")) {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    /**
     * Replace separator in text (by means '\n' and ';' will be replaced by ',').
     *
     * @deprecated (see CsvSerializer # encodeString () instead)
     */
    @Deprecated
    public static String maskSeparator(Object object, char sep) {
        if (object == null) {
            return "";
        }

        String text = null;
        char replacement = ',';
        if (object instanceof String) {
            text = (String) object;
        } else {
            text = object.toString();
        }
        // TODO NewLine tested for Windows only!
        text = text.replace('\n', replacement);
        if (sep == DEFAULT_SEPARATOR) {
            // TODO mask other separators
            return text.replace(DEFAULT_SEPARATOR, replacement);
        } else {
            log.warn("Developer warning: non-default CSV-Separator <{}> not yet masked", sep);
            return text;
        }
    }

    /**
     * Read a File where fields/cells are separated by a given separator and return a List where each item contains a line of the given Stream.
     *
     * @param stream
     * @param separator
     * @return list of lines
     */
    public static java.util.List<String> readFile(InputStream stream, char separator) {
        java.util.List<String> arrayList = new java.util.ArrayList<String>();
        BufferedReader inFile = new BufferedReader(new InputStreamReader(stream));

        try {
            String line = "";

            while ((line = inFile.readLine()) != null) {
                if (line.indexOf(separator) >= 0) {
                    // only add if at least on "cell" is contained
                    arrayList.add(line);
                }
            }

            /*
             * // try if a Cell may contain LF as possible character int c;
             * while ((c = inFile.read()) != -1) { if (c == '\r') { //CR c =
             * inFile.read(); if (c == -1) { // EOF reached if (line.length() >
             * 0) { arrayList.add(line); } return arrayList; } else if (c ==
             * '\n') { // LF // Windows: EndOfLine reached if
             * (line.indexOf(separator) >= 0) { // only add if "cells" contained
             * arrayList.add(line); } line = ""; } else { // Linux evtl. other
             * EndOfLine // throw new DeveloperException(ParserCSV.class,
             * "readFile(..)", "'\n' was expected!"); arrayList.add(line); line
             * = "" + (char)c; } } else { line = line + (char)c; } }
             */
            inFile.close();
        } catch (IOException e) {
            log.warn("Could not read from Stream", e);
        }
        return arrayList;
    }

    /**
     * Skip over Separator.
     *
     * @param nrOfFields to be overread
     */
    public void skip(int nrOfFields) {
        for (int i = 0; i < nrOfFields; i++) {
            int to = line.indexOf(separator, lastIndex + 1);
            if (to > -1) {
                lastIndex = to;
            } else {
                log.warn("no more separators after: {}/{}", i, nrOfFields);
                break;
            }
        }
    }

    /**
     * Transform a String with separated items into a List.
     *
     * @param serializedList
     * @return java.util.List
     */
    public static java.util.List<String> stringToArray(String serializedList, char separator) {
        if (StringUtils.isNullOrEmpty(serializedList)) {
            return new java.util.ArrayList<>();
        } else {
            ParserCSV parser = new ParserCSV(serializedList, separator);

            java.util.List<String> list = new java.util.ArrayList<>();
            String item = parser.getNextString();
            while (item != null) {
                // if (!StringUtils.isNullOrEmpty(item)) {
                list.add(item);
                // Tracer.getInstance().debug(ParserCSV.class,
                // "stringToArray(..)", "adding: " + item);
                // }
                item = parser.getNextString();
            }
            return list;
        }
    }

    /**
     * Convert the given table as is in CSV-Stream. Useful for dumping contents within a JTable generically into CSV-file. If table selection is made, then only the selected elements will be printed
     * otherwise the whole contents is written.
     *
     * @param stream
     * @param table
     * @param separator
     */
    public static void writeFile(PrintStream stream, final JTable table, final char separator) {
        // header
        int columnCount = table.getModel().getColumnCount();
        for (int col = 0; col < columnCount; col++) {
            stream.print(table.getModel().getColumnName(col) + separator);
        }
        stream.println();

        // data
        int[] list = table.getSelectedRows();
        if (list.length == 0) {
            // print all rows
            int rowCount = table.getModel().getRowCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Object value = table.getModel().getValueAt(row, col);
                    // !!! same algorithm for "selected rows" below
                    String field = StringUtils.getString(value);
                    field = ParserCSV.maskSeparator(field, separator);
                    stream.print(field + separator);
                }
                stream.println();
            }
        } else {
            // print selected rows only
            for (int i = 0; i < list.length; i++) {
                for (int col = 0; col < columnCount; col++) {
                    Object value = table.getModel().getValueAt(list[i], col);
                    // !!! same algorithm for "all rows" above
                    String field = StringUtils.getString(value);
                    field = ParserCSV.maskSeparator(field, separator);
                    stream.print(field + separator);
                }
                stream.println();
            }
        }
    }
}