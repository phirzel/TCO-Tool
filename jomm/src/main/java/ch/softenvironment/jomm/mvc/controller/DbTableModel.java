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

import ch.softenvironment.jomm.DbObjectId;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Evaluator;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.view.ViewOptions;
import ch.softenvironment.view.table.NumberTableCellRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import lombok.extern.slf4j.Slf4j;

/**
 * TableModel to represent a List of DbObject's for e.g. in a JTable, where each such DbObject corresponds to the term table-row or data-Object. This Model offers a set of convenience methods to
 * better show the internals of data-Object's.
 * <p>
 * Define a single selection JTable: myJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 *
 * @author Peter Hirzel
 */

@Slf4j
public class DbTableModel extends javax.swing.table.AbstractTableModel implements ch.softenvironment.util.Evaluator {

	private static final String COL_TIME = "*TIME*";
	private List<?> dataVector = null;
	private final List<DbTableColumn> columns = new ArrayList<DbTableColumn>();
	private JTable table = null;
	private Map<String, Object> constants = null;
	private int constantsCounter = 0;

	/**
	 * DbQueryTableModel constructor.
	 */
	public DbTableModel() {
		super();
		// this.columns = new ArrayList();
	}

	/**
	 * Add a property to be formatted as an Amount in a Table-Cell (for e.g."   1'228.85") according to current Locale-Settings. (This is a convenience method.)
	 *
	 * @see #addColumn(String, String, int, TableCellRenderer)
	 */
	public void addColumnAmount(final String columnName, final String property, int defaultWidth) {
		addColumn(columnName, property, defaultWidth, new NumberTableCellRenderer(ch.softenvironment.util.AmountFormat.getAmountInstance()));
	}

	/**
	 * Add a column to TableModel to represent the given Object's aggregated DbCodeType-values.
	 *
	 * @param columnName
	 * @param aggregationProperty Object's property pointing to aggregated DbCodeType
	 * @param defaultWidth
	 * @param codeProperty (null=>DbObject.PROPERTY_NAME; otherwise an existing DbCodeType's property)
	 * @see DbCodeType
	 */
	public void addColumnCodeType(final String columnName, final String aggregationProperty, int defaultWidth, final String codeProperty) {
		columns.add(new DbTableColumn(columnName, DbObject.convertName(aggregationProperty), defaultWidth, DbObject.convertName(codeProperty)));
	}

	/**
	 * @see #addColumn(String, String, int, TableCellRenderer)
	 */
	public void addColumn(final String columnName, final String property, int defaultWidth) {
		addColumn(columnName, property, defaultWidth, null);
	}

	/**
	 * Add a column to TableModel to represent values given by property.
	 *
	 * @see #addColumnEvaluated(String, String, int, TableCellRenderer, Evaluator)
	 */
	public void addColumn(final String columnName, final String property, int defaultWidth, TableCellRenderer renderer) {
		addColumnEvaluated(columnName, property, defaultWidth, renderer, null);
	}

	/**
	 * Add a property to be evaluated by an <b>externally Utility implementing the interface CellEvaluator</b>. This is useful in case a value is known at representation time earliest.
	 * <p>
	 * During cell-rendering the given EvaluatorCell#evaluate() method will be called to determine the actual data-object to be represented.
	 * <p>
	 * Often property-name is a pseudo-name, by means not really a property of the related data-object, which the Utility will map when called within its implementatory #evaluate() method. The
	 * evaluated result may be rendered additionally by specifying a Cell-renderer otherwise a default-renderer will server.
	 *
	 * @param columnName Header-Title
	 * @param property property within Data-Object mapping value to be represented
	 * @param evaluator determining the current value at render-time
	 * @defaultWidth initial with for cell
	 * @renderer Cell-Renderer for type specified by property
	 * @see #addColumn(String, String, int, TableCellRenderer)
	 */
	public void addColumnEvaluated(final String columnName, final String property, int defaultWidth, TableCellRenderer renderer, ch.softenvironment.util.Evaluator evaluator) {
		columns.add(new DbTableColumn(columnName, DbObject.convertName(property), defaultWidth, renderer, evaluator));
	}

	/**
	 * Add a column where all rows have the same constant value.
	 *
	 * @param columnName
	 * @param value
	 * @param defaultWidth
	 * @param renderer
	 */
	public void addColumnConst(final String columnName, Object value, int defaultWidth, TableCellRenderer renderer) {
		String property = "_CONST_" + constantsCounter++;
		if (constants == null) {
			constants = new HashMap<String, Object>();
		}
		constants.put(property, value);
		addColumnEvaluated(columnName, property, defaultWidth, renderer, this);
	}

	/**
	 * Add a property to be formatted as Time in TableCell (for e.g. "11:55:00").
	 *
	 * @see #addColumn(String, String, int, TableCellRenderer)
	 * @deprecated (use TableCellRenderer instead)
	 */
	public void addColumnTime(final String columnName, final String property, int defaultWidth) {
		addColumn(columnName, COL_TIME + property, defaultWidth);
	}

	/**
	 * Adapt TableModel and ColumnModel, for e.g.: <code> DbTableModel tm = new DbTableModel(); tm.addColumn(..); ... tm.adjustTable(new JTable());
	 * </code>
	 */
	public void adjustTable(JTable table) {
		this.table = table;
		table.setModel(this);

		for (int i = 0; i < getColumnCount(); i++) {
			// this is the column the table knows
			TableColumn column = table.getColumnModel().getColumn(i);
			// this is the column we mean
			DbTableColumn dbColumn = columns.get(i);

			/*
			 * // try to compare Header- & CellWidth out of given data and
			 * determine better width java.awt.Component comp =
			 * headerRenderer.getTableCellRendererComponent( null,
			 * column.getHeaderValue(), false, false, 0, 0); int headerWidth =
			 * comp.getPreferredSize().width;
			 *
			 * comp = table.getDefaultRenderer(getColumnClass(i)).
			 * getTableCellRendererComponent( table, "<defaultSizeOfThisValue>",
			 * false, false, 0, i); int cellWidth =
			 * comp.getPreferredSize().width;
			 *
			 * Tracer.getInstance().debug(this, "initColumnSizes()",
			 * "Initializing width of column " + i + ". " + "headerWidth = " +
			 * headerWidth + "; cellWidth = " + cellWidth);
			 *
			 * //XXX: Before Swing 1.1 Beta 2, use setMinWidth instead.
			 * column.setPreferredWidth(Math.max(headerWidth, cellWidth));
			 */

			// 1) @see JTable#sizeColumnsToFit(int)
			column.setPreferredWidth(dbColumn.getPreferredWidth());

			// 2)
			if (dbColumn.getCellRenderer() != null) {
				column.setCellRenderer(dbColumn.getCellRenderer());
			}
		}
	}

	/**
	 * Return the corresponding EntityBean out of the current data-Object. In case data-object is a DbSessionBean the EntityBean will be determined.
	 *
	 * @param rowIndex row to be retrieved at
	 * @see #getRaw(int)
	 */
	public DbObject get(final int rowIndex) throws Exception {
		checkRowIndex(rowIndex);

		DbObject obj = (DbObject) dataVector.get(rowIndex);
		if (obj instanceof DbSessionBean) {
			return ((DbSessionBean) obj).getEntityBean();
		} else {
			return obj;
		}
	}

	/**
	 * Return all dataObject's contained in TableModel.
	 */
	public List getAll() {
		if (dataVector == null) {
			dataVector = new ArrayList();
		}
		return dataVector;
	}

	/**
	 * JTable uses this method to determine the default renderer/ editor for each cell. If we didn't implement this method, then for e.g. column's with boolean values would rather return text
	 * ("true"/"false") instead of a check box.
	 */
	@Override
	public Class getColumnClass(int col) {
		if ((dataVector == null) || (dataVector.size() == 0) || (getValueAt(0, col) == null)) {
			return super.getColumnClass(col);
		} else {
			return getValueAt(0, col).getClass();
		}
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public String getColumnName(final int column) {
		return columns.get(column).getHeaderValue().toString();
	}

	/**
	 * Return the Object as is.
	 *
	 * @param rowIndex
	 * @see #get(int)
	 */
	public Object getRaw(final int rowIndex) {
		checkRowIndex(rowIndex);

		return dataVector.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		if (dataVector == null) {
			return 0;
		} else {
			return dataVector.size();
		}
	}

	/**
	 * Return selected Items of type DbObject in DbTableModel for given indices.
	 *
	 * @param selectedIndeces
	 * @see #get(int)
	 */
	public List<? extends DbObject> getSelectedItems(int[] selectedIndeces) throws Exception {
		List<DbObject> items = new ArrayList<DbObject>(selectedIndeces.length);
		for (int i = 0; i < selectedIndeces.length; i++) {
			items.add(get(selectedIndeces[i]));
		}

		return items;
	}

	/**
	 * Convenience method.
	 *
	 * @see #getSelectedItems(int[])
	 */
	public static java.util.List getSelectedItems(javax.swing.JTable table) throws Exception {
		return ((DbTableModel) table.getModel()).getSelectedItems(table.getSelectedRows());
	}

	/**
	 * Return selected Items in JTable's DbTableModel where Table contains some DbSessionBean's pointing on a DbRelationshipBean.
	 */
	public static java.util.List getSelectedItemsAssociated(javax.swing.JTable table, java.lang.Class target, final String property) throws Exception {
		java.util.List selection = getSelectedItems(table);
		java.util.List ends = new java.util.ArrayList(selection.size());

		java.util.Iterator iterator = selection.iterator();
		while (iterator.hasNext()) {
			DbRelationshipBean association = (DbRelationshipBean) iterator.next();
			DbPropertyChange change = new DbPropertyChange(association, property);
			ends.add(association.getObjectServer().getObjectById(new DbObjectId(target, (Long) change.getValue()), false));
		}

		return ends;
	}

	/**
	 * Return selected Items in JTable's DbTableModel of type DbObject.
	 *
	 * @param selectedIndeces
	 * @see #getRaw(int)
	 */
	public List getSelectedItemsRaw(int[] selectedIndeces) throws Exception {
		List items = new ArrayList(selectedIndeces.length);
		for (int i = 0; i < selectedIndeces.length; i++) {
			items.add(getRaw(selectedIndeces[i]));
		}

		return items;
	}

	/**
	 * Convenience method.
	 *
	 * @see #getSelectedItemsRaw(int[])
	 */
	public static java.util.List getSelectedItemsRaw(javax.swing.JTable table) throws Exception {
		return ((DbTableModel) table.getModel()).getSelectedItemsRaw(table.getSelectedRows());
	}

	/**
	 * Remove selected Items in JTable's DbTableModel. Any owner-Parent's list is not updated.
	 *
	 * @param table
	 * @param viewOptions
	 * @return
	 */
	public static List removeSelectedItems(javax.swing.JTable table, ViewOptions viewOptions, boolean historize) throws Exception {
		List removedItems = ((DbTableModel) table.getModel()).remove(table.getSelectedRows(), historize);
		table.clearSelection();

		if ((viewOptions != null) && (viewOptions.getViewManager() != null)) {
			// related View's should have been closed before deletion of their
			// model-Object's
			viewOptions.getViewManager().closeAll(removedItems);
		}

		return removedItems;
	}

	/**
	 * Return the value of Cell(row, col). Cell-values are determined and rendered according to specified column-entries.
	 *
	 * @see #addColumn*()
	 */
	@Override
	public Object getValueAt(final int rowIndex, final int colIndex) {
		checkRowIndex(rowIndex);

		try {
			DbTableColumn column = columns.get(colIndex);
			Object dataObj = dataVector.get(rowIndex);
			String property = column.getProperty();

			// @deprecated Time format
			if (property.startsWith(COL_TIME)) {
				property = property.substring(COL_TIME.length());
				DbPropertyChange change = new DbPropertyChange(dataObj, property);
				return NlsUtils.formatTime24Hours((java.util.Date) change.getValue(), false);
			}
			/*
			 * // use external formatter if (property.startsWith(COL_FORMATTED))
			 * { property = property.substring(COL_FORMATTED.length()); if
			 * (formatter == null) { Tracer.getInstance().developerError(this,
			 * "getValueAt()",
			 * "DbTableModel(ColumnFormatter) not properly initialized!"); }
			 * else { return formatter.evaluate(dataObj, property); } }
			 */
			// determine cell value
			Object value = null;
			if (column.getEvaluator() == null) {
				// existing property in dataObj
				DbPropertyChange change = new DbPropertyChange(dataObj, property);
				value = change.getValue();
			} else {
				// generic value for pseudo-property
				value = column.getEvaluator().evaluate(dataObj, property);
			}

			// auto-format
			if (value instanceof DbCodeType) {
				if (column.getCodeProperty() == null) {
					// show DbObject#PROPERTY_NAME by default
					return ((DbCodeType) value).getNameString();
				} else {
					BeanReflector code = new BeanReflector(value, column.getCodeProperty());
					return code.getValue();
				}
			}

			if (value instanceof java.util.Date) {
				// by default show neither Time nor Timestamp, just plain date
				return NlsUtils.formatDate((java.util.Date) value);
			}
			if (value instanceof DbNlsString) {
				return ((DbNlsString) value).getValue();
			}
			if (value instanceof java.util.List) {
				java.util.Iterator<?> iterator = ((java.util.List<?>) value).iterator();
				String list = "{";
				while (iterator.hasNext()) {
					if (list.length() > 1) {
						list = list + "; ";
					}
					Object listValue = iterator.next();
					if (listValue instanceof DbCodeType) {
						list = list + ((DbCodeType) listValue).getNameString();
					} else {
						list = list + listValue;
					}
				}
				return list + "}";
			}

			// as is (Boolean, Number, String)
			/*
			 * if (value instanceof Boolean) { return
			 * StringUtils.getBooleanString((Boolean)value); }
			 */
			return value;
		} catch (Exception e) {
			throw new DeveloperException("defined column has no getter for <" + columns.get(colIndex).getProperty() + "()>");
		}
	}

	/**
	 * @see #remove(int[], boolean)
	 */
	public List remove(int[] selectedIndeces) throws Exception {
		return remove(selectedIndeces, false);
	}

	/**
	 * Remove selected Objects from Target-System. ReadOnly Objects are spared from removing.
	 *
	 * @param selectedIndeces typically result of JTable#getSelectedRows()
	 * @param historize (true->add history entry for every removed object)
	 * @return List of all effectively removed items
	 */
	public List<DbChangeableBean> remove(int[] selectedIndeces, boolean historize) throws Exception {
		List<DbChangeableBean> removedItems = new ArrayList<DbChangeableBean>();

		for (int i = 0; i < selectedIndeces.length; i++) {
			Object object = dataVector.get(selectedIndeces[i]);
			if (object instanceof DbSessionBean) {
				// determine real instance to be deleted
				object = ((DbSessionBean) object).getEntityBean(); // does not
				// cause a
				// Refresh-SELECT
			}

			if (object instanceof DbChangeableBean) {
				// ((DbObject)object).getObjectServer().deletePersistent(object);
				((DbChangeableBean) object).remove(true, historize);
				removedItems.add((DbChangeableBean) object);
			} else {
				throw new DeveloperException("Non-Removable Item <" + object + ">");
			}
		}

		return removedItems;
	}

	/**
	 * Set a list of data-Object's to display by this model.
	 *
	 * @see #setAllByCount(Class, DbQueryBuilder)
	 */
	public void setAll(List list) {
		if (table != null) {
			table.clearSelection();
		} else {
			log.warn("Developer warning: DbTableModel should have been adjusted (@see #adjustTable())");
		}

		dataVector = list;
		this.fireTableDataChanged();
		// Generate notification
		// fireTableStructureChanged();
	}

	/**
	 * Do the specialized query and return Count(*).
	 *
	 * @see #setAll(List)
	 */
	public String setAllByCount(java.lang.Class<? extends DbObject> dbObject, DbQueryBuilder builder) throws Exception {
		// 1) query the wanted result
		setAll(builder.getObjectServer().retrieveAll(dbObject, builder));

		int count = getAll().size();
		if (builder.getFetchBlockSize() != DbQueryBuilder.FETCHBLOCK_UNLIMITED) {
			// 2) estimate the potential number of records
			// TODO Tune: implement COUNT(*) in a separate Thread
			count = builder.getCount(dbObject);
			// (Long.valueOf(builder.getObjectServer().getFirstValue(builder.toCountBuilder()).toString()));
		}
		return "# " + (Integer.valueOf(getRowCount())).toString() + "/" + count;
	}

	private void checkRowIndex(final int rowIndex) {
		if ((dataVector == null) || (rowIndex < 0) || (rowIndex > (getRowCount() - 1))) {
			throw new IllegalArgumentException("row does not exist in table-model: " + rowIndex);
		}
	}

	/**
	 * @param owner
	 * @param property
	 * @return
	 * @see #addColumnConst(String, Object, int, TableCellRenderer)
	 */
	@Override
	public Object evaluate(Object owner, final String property) {
		if (constants.containsKey(property)) {
			return constants.get(property);
		} else {
			log.error("Developer error: addColumnConst() buggy!");
			return null;
		}
	}
}
