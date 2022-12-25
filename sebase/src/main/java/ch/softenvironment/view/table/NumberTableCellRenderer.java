package ch.softenvironment.view.table;

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

import ch.softenvironment.math.MathUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Format a Number value as formatted String in a JTable-Cell.
 *
 * @author Peter Hirzel
 */

@Slf4j
public class NumberTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

	java.text.NumberFormat numberFormat = null;

	public NumberTableCellRenderer(java.text.NumberFormat format) {
		super();
		numberFormat = format;
	}

	public NumberTableCellRenderer(int fractionDigits) {
		this(java.text.NumberFormat.getNumberInstance());

		numberFormat.setMinimumFractionDigits(fractionDigits);
		numberFormat.setMaximumFractionDigits(fractionDigits);
	}

	@Override
	public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	@Override
	protected void setValue(Object value) {
		try {
			if (value == null) {
				setText("");
			} else if (MathUtils.compare(0.0, ((Number) value).doubleValue()) == 0) {
				// prevent 0.0 is represented as -0.0
				setText(numberFormat.format(Double.valueOf(0.0)));
			} else {
				setText((value == null) ? "" : numberFormat.format(value));
			}
		} catch (ClassCastException e) {
			log.error("Developer error: Number value expected for: {}", value);
		}
	}
}
