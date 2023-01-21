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

/**
 * ASCII-Stream-Generator.
 *
 * @author Peter Hirzel
 * @since 1.1.1.1 (2005-04-25)
 */
public abstract class SimpleSerializer implements Serializer {

    private String tab = "  "; // default by spaces
    protected String lineSeparator = System.getProperty("line.separator");
    private int lineNumber = 1;
    protected int inline = 0;

    protected java.io.Writer out = null;

    /**
     * SimpleSerializer constructor comment.
     */
    public SimpleSerializer(java.io.Writer out) {
        super();

        this.out = out;
    }

    /**
     * Return the Writer.
     */
    public final java.io.Writer getWriter() {
        return out;
    }

    /**
     * Write a newline separator to the Stream and increments LineCounter.
     *
     * @throws throws java.io.IOException
     */
    public void newline() throws java.io.IOException {
        out.write(lineSeparator);
        lineNumber++;
    }

    /**
     * Set the Tabulator String.
     *
     * @see #indent()
     */
    public final void setTab(String tabulator) {
        tab = tabulator;
    }

    /**
     * Indent cursor at current Position n times.
     *
     * @throws throws java.io.IOException
     * @see #setTab() for distance holder String
     */
    protected final void indent(int n) throws java.io.IOException {
        for (int i = 0; i < n; i++) {
            out.write(tab);
        }
    }

    /**
     * Write given value "as is" into Stream. No Validations will be done to given value.
     *
     * @throws java.io.IOException
     */
    protected void nativeContent(String value) throws java.io.IOException {
        out.write(value);
    }
}
