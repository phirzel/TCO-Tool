package ch.softenvironment.jomm.mvc.controller;

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

import ch.softenvironment.util.Evaluator;
import javax.swing.table.TableCellRenderer;

/**
 * Overwrites TableColumn of a JTable.
 *
 * @author Peter Hirzel
 */

class DbTableColumn extends javax.swing.table.TableColumn {

    private final Evaluator evaluator;
    private final String property;
    private String codeProperty = null;

    /**
     * Define a column where its value will be determined at rendering by a generic Lookup.
     *
     * @param evaluator
     * @see DbTableModel#addColumn(String, String, int)
     */
    protected DbTableColumn(final String columnName, final String property,
        int defaultWidth, TableCellRenderer renderer, Evaluator evaluator) {
        super();
        this.property = property;
        setHeaderValue(columnName);
        setPreferredWidth(defaultWidth);
        setCellRenderer(renderer);
        this.evaluator = evaluator;
    }

    /**
     * Map a DbCodeType. If (codeProperty!=null) make sure the DbCodeType has such a property, otherwise the DbObject.PROPERTY_NAME property will be displayed by default.
     *
     * @see DbTableModel#addColumnCodeType(String, String, int, String)
     */
    protected DbTableColumn(final String columnName, final String property,
        int defaultWidth, final String codeProperty) {
        this(columnName, property, defaultWidth, null, null);
        this.codeProperty = codeProperty;
    }

    /**
     * Return the generic lookup class.
     */
    protected Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Return the Property of given DbObject to show value.
     */
    protected String getProperty() {
        return property;
    }

    /**
     * Return whether the CodeType's property to show.
     */
    protected String getCodeProperty() {
        return codeProperty;
    }
}
