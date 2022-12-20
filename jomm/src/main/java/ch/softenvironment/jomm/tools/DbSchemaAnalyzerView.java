package ch.softenvironment.jomm.tools;

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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQuery;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.DbUserTransactionBlock;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.view.*;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Tool to execute any-SQL-Code.
 *
 * @author Peter Hirzel
 */

public class DbSchemaAnalyzerView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame {

	private DbObjectServer server = null;
	private JPanel ivjJFrameContentPane = null;
	private JMenuBar ivjLauncherViewJMenuBar = null;
	private JMenuItem ivjMniAboutBox = null;
	private JMenuItem ivjMniExit = null;
	private JMenuItem ivjMniHelpTopics = null;
	private JMenu ivjMnuFile = null;
	private JMenu ivjMnuHelp = null;
	private JMenu ivjMnuView = null;
	private JPanel ivjPnlMain = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JCheckBoxMenuItem ivjMncToolbar = null;
	private ToolBar ivjTlbToolbar = null;
	private JCheckBoxMenuItem ivjMncStatusbar = null;
	private StatusBar ivjStbStatusbar = null;
	private JMenu ivjMnuLookAndFeel = null;
	private FileNamePanel ivjPnlFile = null;
	private JLabel ivjJLabel1 = null;
	private JLabel ivjJLabel2 = null;
	private SimpleEditorPanel ivjPnlLog = null;
	private JButton ivjBtnAnalyzeSchema = null;
	private JButton ivjBtnConnect = null;
	private JButton ivjBtnSearch = null;
	private JTextField ivjTxtSearchString = null;
	private JRadioButton ivjRbtNumber = null;
	private JRadioButton ivjRbtText = null;
	private JCheckBox ChxTable = null;
	private JCheckBox ChxView = null;

	class IvjEventHandler implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DbSchemaAnalyzerView.this.getMncStatusbar()) {
				connEtoM2(e);
			}
			if (e.getSource() == DbSchemaAnalyzerView.this.getMniAboutBox()) {
				connEtoC1(e);
			}
			if (e.getSource() == DbSchemaAnalyzerView.this.getMniHelpTopics()) {
				connEtoC5(e);
			}
			if (e.getSource() == DbSchemaAnalyzerView.this.getMncToolbar()) {
				connEtoM3(e);
			}
			if (e.getSource() == DbSchemaAnalyzerView.this.getMniExit()) {
				connEtoC8(e);
			}
			if (e.getSource() == DbSchemaAnalyzerView.this.getBtnAnalyzeSchema()) {
				connEtoC2(e);
			}
			if (e.getSource() == DbSchemaAnalyzerView.this.getBtnConnect()) {
				connEtoC3(e);
			}
			if (e.getSource() == DbSchemaAnalyzerView.this.getBtnSearch()) {
				connEtoC4(e);
			}
		}
	}

	/**
	 * Constructor
	 *
	 * @param viewOptions Symbol
	 * @param objectServer Symbol
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	public DbSchemaAnalyzerView(ch.softenvironment.view.ViewOptions viewOptions, ch.softenvironment.jomm.DbObjectServer objectServer) {
		super(viewOptions);
		this.server = objectServer;
		initialize();
	}

	/**
	 * Analyze Catalog, Schema, and Tables in a Target-System.
	 */
	public void analyze() {
		// final DbObjectServer server = getObjectServer();
		// final BaseFrame view = this;
		showBusy(new Runnable() {
			@Override
			public void run() {
				try {
					// getPnlLog().setEnabled(true);
					getPnlLog().clearAll();
					// getPnlLog().setEnabled(false);

					DatabaseMetaData dbmd = server.getMetaData();
					String productName = dbmd.getDatabaseProductName();
					getPnlLog().append("Database product: " + productName + "\n");
					getPnlLog().append("Database product Version: " + dbmd.getDatabaseProductVersion() + "\n\n");
					getPnlLog().append("Database Major Version: " + dbmd.getDatabaseMajorVersion() + "\n");
					getPnlLog().append("Database Minor Version: " + dbmd.getDatabaseMinorVersion() + "\n");
					getPnlLog().append("Driver name: " + dbmd.getDriverName() + "\n");
					getPnlLog().append("CatalogTerm: " + dbmd.getCatalogTerm() + "\n");
					getPnlLog().append("URL: " + dbmd.getURL() + "\n");

					WaitDialog.updateProgress(5, "Table-types");
					ResultSet rs = dbmd.getTableTypes();
					getPnlLog().append("Available Table-types for Database:\n");
					java.util.Set<String> tableTypes = new java.util.HashSet<>();
					while (rs.next()) {
						// check for TABLE, VIEW, ...
						String type = rs.getString("TABLE_TYPE");
						tableTypes.add(type);
						getPnlLog().append(" => " + type + "\n");
					}
					rs.close();

					WaitDialog.updateProgress(10, "Schemas in Database");
					getPnlLog().append("\n-----------------------\n");
					getPnlLog().append("Schema term: " + dbmd.getSchemaTerm() + "\n");
					rs = dbmd.getSchemas();
					while (rs.next()) {
						String schema = rs.getString(1);
						getPnlLog().append("SCHEMA=" + schema + "\n");
					}

					WaitDialog.updateProgress(15, "Catalogs in Database");
					getPnlLog().append("\n-----------------------\n");
					getPnlLog().append("Catalogs\n");
					rs = dbmd.getCatalogs();
					java.util.List<String> tables = new java.util.ArrayList<>();
					java.util.List<String> views = new java.util.ArrayList<>();
					while (rs.next()) {
						String catalog = rs.getString(1);
						getPnlLog().append("CATALOG=" + catalog + "\n");

						String[] types = null; // { "TABLE" }; // null for all
						// types
						ResultSet rsTables = dbmd.getTables(catalog, null, null, types);
						while (rsTables.next()) {
							String tableName = rsTables.getString("TABLE_NAME");
							String tableType = rsTables.getString("TABLE_TYPE");
							if (tableType.equals("TABLE")) {
								tables.add(tableName);
							} else if (tableType.equals("VIEW")) {
								views.add(tableName);
							}
							// getPnlLog().append("   => " +
							// rsTables.getString("TABLE_CAT") + "\n");
							getPnlLog().append(" =>SCHEMA." + tableType + "=" + rsTables.getString("TABLE_SCHEM") + "." + tableName + "\n");
						}
						rsTables.close();
					}
					rs.close();

					/*
					 * getPnlLog().append("Available Schemas:\n"); rs =
					 * dbmd.getSchemas(); while (rs.next()) {
					 * getPnlLog().append(" => " + rs.getString(1) + "\n"); }
					 * rs.close();
					 */

					if (getChxTable().isSelected()) {
						WaitDialog.updateProgress(40, "Table-Columns");
						getPnlLog().append("\n-----------------------\n");
						getPnlLog().append("COLUMN's of TABLE:\n");
						java.util.Iterator<String> iterator = tables.iterator();
						while (iterator.hasNext()) {
							analyzeTable(iterator.next());
						}

						WaitDialog.updateProgress(60, "Relationships");
						getPnlLog().append("\n-----------------------\n");
						analyzeRelationships(productName, tables);
					}

					if (getChxView().isSelected()) {
						WaitDialog.updateProgress(90, "View-Columns");
						getPnlLog().append("\n-----------------------\n");
						getPnlLog().append("COLUMN's of VIEW:\n");
						java.util.Iterator<String> iterator = views.iterator();
						while (iterator.hasNext()) {
							analyzeTable(iterator.next());
						}
					}

					WaitDialog.updateProgress(100, "");
				} catch (SQLException e) {
					handleException(e);
				}
			}
		});
	}

	/**
	 * Analyze Schema-Information of a Table.
	 */
	public void analyzeRelationships(String productName, java.util.List<String> tables) throws SQLException {
		getPnlLog().append("\nRelationships:\n");

		if (productName.toUpperCase().indexOf("ACCESS") >= 0) {
			try {
				final DbUserTransactionBlock block = server.createUserTransactionBlock(true);
				block.execute(new Runnable() {
					@Override
					public void run() {
						try {
							// #getImportKeys not supported => therefore use
							// non-documented System-Table
							DbQueryBuilder builder = block.getObjectServer().createQueryBuilder(DbQueryBuilder.SELECT, "Access-SystemTable->MSysRelationships");
							builder.setTableList("MSysRelationships");

							DbQuery query = new DbQuery(block.getTransaction(), builder);
							ResultSet rs = (ResultSet) query.execute();

							ResultSetMetaData rsmd = rs.getMetaData();

							// structure of System-Table
							int numberOfColumns = rsmd.getColumnCount();
							for (int i = 1; i <= numberOfColumns; i++) {
								String colName = rsmd.getColumnName(i);
								getPnlLog().append(colName + " ");
							}
							getPnlLog().append("\n");

							// data (FK)
							while (rs.next()) {
								for (int i = 1; i <= numberOfColumns; i++) {
									String s = rs.getString(i);
									getPnlLog().append(s + "  ");
								}
								getPnlLog().append("\n");
							}

							query.closeAll();
						} catch (SQLException e) {
							block.abort(null, e);
						}
					}
				});
			} catch (Throwable e) {
				handleException(e);
			}
		} else {
			// use standard JDBC-function
			DatabaseMetaData dbmd = server.getMetaData();
			java.util.Iterator<String> iterator = tables.iterator();
			while (iterator.hasNext()) {
				ResultSet rs = dbmd.getImportedKeys(null, null, iterator.next());
				while (rs.next()) {
					String pkTable = rs.getString("PKTABLE_NAME");
					String pkColName = rs.getString("PKCOLUMN_NAME");
					String fkTable = rs.getString("FKTABLE_NAME");
					String fkColName = rs.getString("FKCOLUMN_NAME");
					short updateRule = rs.getShort("UPDATE_RULE");
					short deleteRule = rs.getShort("DELETE_RULE");
					getPnlLog().append("primary key table name :  " + pkTable + "\n");
					getPnlLog().append("primary key column name :  " + pkColName + "\n");
					getPnlLog().append("foreign key table name :  " + fkTable + "\n");
					getPnlLog().append("foreign key column name :  " + fkColName + "\n");
					getPnlLog().append("update rule:  " + updateRule + "\n");
					getPnlLog().append("delete rule:  " + deleteRule + "\n");
					getPnlLog().append("\n");
				}
				rs.close();
			}
		}
	}

	/**
	 * Analyze Schema-Information of a Table.
	 */
	public void analyzeTable(final String tableName) {
		getPnlLog().append("\n" + tableName + ":\n");

		try {
			final DbUserTransactionBlock block = server.createUserTransactionBlock(true);
			block.execute(new Runnable() {
				@Override
				public void run() {
					try {
						DbQueryBuilder builder = block.getObjectServer().createQueryBuilder(DbQueryBuilder.SELECT, tableName);
						// TODO use [ brackets ] for MS Access & MS SQL Server
						builder.setTableList(/* "[" + */tableName /* + "]" */);

						DbQuery query = new DbQuery(block.getTransaction(), builder);
						ResultSet rs = (ResultSet) query.execute();

						ResultSetMetaData rsmd = rs.getMetaData();

						int numberOfColumns = rsmd.getColumnCount();
						for (int i = 1; i <= numberOfColumns; i++) {
							String colName = rsmd.getColumnName(i);
							// tableName = rsmd.getTableName(i);
							String targetTypeName = rsmd.getColumnTypeName(i); // DBMS
							// name
							// for
							// type
							int jdbcType = rsmd.getColumnType(i);
							// boolean caseSen = rsmd.isCaseSensitive(i);
							// boolean writable = rsmd.isWritable(i);
							getPnlLog().append(colName);

							getPnlLog().append(" : " + targetTypeName + " (JDBC (java.sql.Types)=" + jdbcType + ")\n");
							// getPnlLog().append("(Is case sensitive: " +
							// caseSen);
							// getPnlLog().append("(Is possibly writable: " +
							// writable + "\n");
						}
						/*
						 * // data while (rs.next()) { for (int i = 1;
						 * i<=numberOfColumns; i++) { String s =
						 * rs.getString(i); System.out.print(s + "  "); }
						 * System.out.println(""); }
						 */
						query.closeAll();
						/*
						 * // primary keys DatabaseMetaData dbmd =
						 * objSrv.getMetaData(); rs = dbmd.getPrimaryKeys(null,
						 * null, tableName); while (rs.next()) { // String name
						 * = rs.getString("TABLE_NAME"); String columnName =
						 * rs.getString("COLUMN_NAME"); String keySeq =
						 * rs.getString("KEY_SEQ"); String pkName =
						 * rs.getString("PK_NAME"); //
						 * getPnlLog().append("table name :  " + name + "\n");
						 * getPnlLog().append("column name:  " + columnName +
						 * "\n"); getPnlLog().append("sequence in key:  " +
						 * keySeq + "\n");
						 * getPnlLog().append("primary key name:  " + pkName +
						 * "\n"); getPnlLog().append("\n"); } rs.close();
						 */
					} catch (SQLException e) {
						block.abort(null, e);
					}
				}
			});
		} catch (Throwable e) {
			handleException(e);
		}

	}

	/**
	 * Comment
	 */
	private void connectTarget() {
		try {
			ch.softenvironment.jomm.mvc.view.DbLoginDialog dialog = // new
				// ch.softenvironment.jomm.mvc.view.DbLoginDialog(null,
				// "jdbc:mysql://localhost:3306/workflow");
				new ch.softenvironment.jomm.mvc.view.DbLoginDialog(null, "jdbc:oracle:thin:@localhost:1521/XE");
			if (!dialog.isSaved()) {
				System.exit(0);
			}

			// establish an initialize database connection
			server = initializeDatabase(dialog.getUserId(), dialog.getPassword(), dialog.getUrl());
			getStbStatusbar().setStatus(server.getPersistenceManagerFactory().getConnectionURL());
		} catch (Throwable e) {
			getStbStatusbar().setStatus("NO connection");
			handleException(e);
		}
	}

	/**
	 * connEtoC1: (MniAboutBox.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniAboutBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.mniAboutBox_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (MniStatistic.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniStatistic_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.analyze();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BtnConnect.action.actionPerformed(java.awt.event.ActionEvent) --> DbSchemaAnalyzerView.connectTarget()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC3(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.connectTarget();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BtnSearch.action.actionPerformed(java.awt.event.ActionEvent) --> DbSchemaAnalyzerView.searchTarget()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC4(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.searchObjects();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (MniHelpTopics.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView .mniHelpTopics_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC5(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.mniHelpTopics_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC8: (MniExit.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.dispose()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC8(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.dispose();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoM2: (MniStatusbar.action.actionPerformed(java.awt.event.ActionEvent) --> StbStatus.toggleVisbility()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getStbStatusbar().toggleVisbility();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoM3: (MncToolbar.action.actionPerformed(java.awt.event.ActionEvent) --> TlbToolbar.toggleVisbility()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM3(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getTlbToolbar().toggleVisbility();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	@Override
	public void dispose() {
		disposeApplication(server);
	}

	/**
	 * Return the BtnExecute property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnAnalyzeSchema() {
		if (ivjBtnAnalyzeSchema == null) {
			try {
				ivjBtnAnalyzeSchema = new javax.swing.JButton();
				ivjBtnAnalyzeSchema.setName("BtnAnalyzeSchema");
				ivjBtnAnalyzeSchema.setText("Analyze Schema");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnAnalyzeSchema;
	}

	/**
	 * Return the BtnConnect property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnConnect() {
		if (ivjBtnConnect == null) {
			try {
				ivjBtnConnect = new javax.swing.JButton();
				ivjBtnConnect.setName("BtnConnect");
				ivjBtnConnect.setText("Connect");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnConnect;
	}

	/**
	 * Return the BtnSearch property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnSearch() {
		if (ivjBtnSearch == null) {
			try {
				ivjBtnSearch = new javax.swing.JButton();
				ivjBtnSearch.setName("BtnSearch");
				ivjBtnSearch.setToolTipText("Full Text Search");
				ivjBtnSearch.setText("Search");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnSearch;
	}

	/**
	 * Return the JFrameContentPane property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJFrameContentPane() {
		if (ivjJFrameContentPane == null) {
			try {
				ivjJFrameContentPane = new javax.swing.JPanel();
				ivjJFrameContentPane.setName("JFrameContentPane");
				ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
				getJFrameContentPane().add(getTlbToolbar(), "North");
				getJFrameContentPane().add(getPnlMain(), "Center");
				getJFrameContentPane().add(getStbStatusbar(), "South");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJFrameContentPane;
	}

	/**
	 * Return the JLabel1 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getJLabel1() {
		if (ivjJLabel1 == null) {
			try {
				ivjJLabel1 = new javax.swing.JLabel();
				ivjJLabel1.setName("JLabel1");
				ivjJLabel1.setText("Text-Datei mit SQL-Code (z.B. 'MySchema.sql'):");
				ivjJLabel1.setBounds(449, 312, 296, 14);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJLabel1;
	}

	/**
	 * Return the JLabel2 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getJLabel2() {
		if (ivjJLabel2 == null) {
			try {
				ivjJLabel2 = new javax.swing.JLabel();
				ivjJLabel2.setName("JLabel2");
				ivjJLabel2.setText("Log:");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJLabel2;
	}

	/**
	 * Return the LauncherViewJMenuBar property value.
	 *
	 * @return javax.swing.JMenuBar
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuBar getLauncherViewJMenuBar() {
		if (ivjLauncherViewJMenuBar == null) {
			try {
				ivjLauncherViewJMenuBar = new javax.swing.JMenuBar();
				ivjLauncherViewJMenuBar.setName("LauncherViewJMenuBar");
				ivjLauncherViewJMenuBar.add(getMnuFile());
				ivjLauncherViewJMenuBar.add(getMnuView());
				ivjLauncherViewJMenuBar.add(getMnuHelp());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLauncherViewJMenuBar;
	}

	/**
	 * Return the MniStatusbar property value.
	 *
	 * @return javax.swing.JCheckBoxMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBoxMenuItem getMncStatusbar() {
		if (ivjMncStatusbar == null) {
			try {
				ivjMncStatusbar = new javax.swing.JCheckBoxMenuItem();
				ivjMncStatusbar.setName("MncStatusbar");
				ivjMncStatusbar.setSelected(true);
				ivjMncStatusbar.setText("Statusleiste");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMncStatusbar;
	}

	/**
	 * Return the MncToolbar property value.
	 *
	 * @return javax.swing.JCheckBoxMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBoxMenuItem getMncToolbar() {
		if (ivjMncToolbar == null) {
			try {
				ivjMncToolbar = new javax.swing.JCheckBoxMenuItem();
				ivjMncToolbar.setName("MncToolbar");
				ivjMncToolbar.setSelected(false);
				ivjMncToolbar.setText("Werkzeugleiste");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMncToolbar;
	}

	/**
	 * Return the MniAboutBox property value.
	 *
	 * @return javax.swing.JMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuItem getMniAboutBox() {
		if (ivjMniAboutBox == null) {
			try {
				ivjMniAboutBox = new javax.swing.JMenuItem();
				ivjMniAboutBox.setName("MniAboutBox");
				ivjMniAboutBox.setText("Info");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMniAboutBox;
	}

	/**
	 * Return the MniExit property value.
	 *
	 * @return javax.swing.JMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuItem getMniExit() {
		if (ivjMniExit == null) {
			try {
				ivjMniExit = new javax.swing.JMenuItem();
				ivjMniExit.setName("MniExit");
				ivjMniExit.setText("Beenden");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMniExit;
	}

	/**
	 * Return the MniHelpTopics property value.
	 *
	 * @return javax.swing.JMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuItem getMniHelpTopics() {
		if (ivjMniHelpTopics == null) {
			try {
				ivjMniHelpTopics = new javax.swing.JMenuItem();
				ivjMniHelpTopics.setName("MniHelpTopics");
				ivjMniHelpTopics.setText("Hilfethemen");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMniHelpTopics;
	}

	/**
	 * Return the MnuFile property value.
	 *
	 * @return javax.swing.JMenu
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenu getMnuFile() {
		if (ivjMnuFile == null) {
			try {
				ivjMnuFile = new javax.swing.JMenu();
				ivjMnuFile.setName("MnuFile");
				ivjMnuFile.setText("Datei");
				ivjMnuFile.setEnabled(true);
				ivjMnuFile.add(getMniExit());
				// user code begin {1}
				// ivjMnuFile.setText(resLauncherView.getString("MnuFile_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMnuFile;
	}

	/**
	 * Return the MnuHelp property value.
	 *
	 * @return javax.swing.JMenu
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenu getMnuHelp() {
		if (ivjMnuHelp == null) {
			try {
				ivjMnuHelp = new javax.swing.JMenu();
				ivjMnuHelp.setName("MnuHelp");
				ivjMnuHelp.setText("Hilfe");
				ivjMnuHelp.setEnabled(true);
				ivjMnuHelp.add(getMniHelpTopics());
				ivjMnuHelp.add(getMniAboutBox());
				// user code begin {1}
				// ivjMnuHelp.setText(resLauncherView.getString("Hilfe"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMnuHelp;
	}

	/**
	 * Return the MnuLookAndFeel property value.
	 *
	 * @return javax.swing.JMenu
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenu getMnuLookAndFeel() {
		if (ivjMnuLookAndFeel == null) {
			try {
				ivjMnuLookAndFeel = new javax.swing.JMenu();
				ivjMnuLookAndFeel.setName("MnuLookAndFeel");
				ivjMnuLookAndFeel.setText("Look & Feel");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMnuLookAndFeel;
	}

	/**
	 * Return the MnuView property value.
	 *
	 * @return javax.swing.JMenu
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenu getMnuView() {
		if (ivjMnuView == null) {
			try {
				ivjMnuView = new javax.swing.JMenu();
				ivjMnuView.setName("MnuView");
				ivjMnuView.setText("Ansicht");
				ivjMnuView.add(getMncToolbar());
				ivjMnuView.add(getMncStatusbar());
				ivjMnuView.add(getMnuLookAndFeel());
				// user code begin {1}
				// ivjMnuView.setText(resLauncherView.getString("MnuView_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMnuView;
	}

	/**
	 * Return the PnlFile property value.
	 *
	 * @return ch.softenvironment.view.FileNamePanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ch.softenvironment.view.FileNamePanel getPnlFile() {
		if (ivjPnlFile == null) {
			try {
				ivjPnlFile = new ch.softenvironment.view.FileNamePanel();
				ivjPnlFile.setName("PnlFile");
				ivjPnlFile.setBounds(449, 331, 371, 28);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlFile;
	}

	/**
	 * Return the PnlLog property value.
	 *
	 * @return ch.softenvironment.view.SimpleEditorPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ch.softenvironment.view.SimpleEditorPanel getPnlLog() {
		if (ivjPnlLog == null) {
			try {
				ivjPnlLog = new ch.softenvironment.view.SimpleEditorPanel();
				ivjPnlLog.setName("PnlLog");
				ivjPnlLog.setTxaEditorEditable(false);
				ivjPnlLog.setEnabled(true);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlLog;
	}

	/**
	 * Return the PnlMain property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getPnlMain() {
		if (ivjPnlMain == null) {
			try {
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 2;
				gridBagConstraints1.gridy = 4;
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 4;
				ivjPnlMain = new javax.swing.JPanel();
				ivjPnlMain.setName("PnlMain");
				ivjPnlMain.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsJLabel2 = new java.awt.GridBagConstraints();
				constraintsJLabel2.gridx = 1;
				constraintsJLabel2.gridy = 5;
				constraintsJLabel2.gridwidth = 2;
				constraintsJLabel2.ipadx = 106;
				constraintsJLabel2.insets = new java.awt.Insets(14, 7, 5, 116);
				java.awt.GridBagConstraints constraintsPnlLog = new java.awt.GridBagConstraints();
				constraintsPnlLog.gridx = 1;
				constraintsPnlLog.gridy = 6;
				constraintsPnlLog.gridwidth = 3;
				constraintsPnlLog.fill = java.awt.GridBagConstraints.BOTH;
				constraintsPnlLog.weightx = 1.0;
				constraintsPnlLog.weighty = 1.0;
				constraintsPnlLog.ipadx = 411;
				constraintsPnlLog.ipady = 398;
				constraintsPnlLog.insets = new java.awt.Insets(5, 7, 15, 11);
				ivjPnlMain.add(getJLabel2(), constraintsJLabel2);
				java.awt.GridBagConstraints constraintsBtnAnalyzeSchema = new java.awt.GridBagConstraints();
				constraintsBtnAnalyzeSchema.gridx = 3;
				constraintsBtnAnalyzeSchema.gridy = 1;
				constraintsBtnAnalyzeSchema.ipadx = 4;
				constraintsBtnAnalyzeSchema.insets = new java.awt.Insets(8, 2, 9, 63);
				ivjPnlMain.add(getPnlLog(), constraintsPnlLog);
				getPnlMain().add(getBtnAnalyzeSchema(), constraintsBtnAnalyzeSchema);

				java.awt.GridBagConstraints constraintsBtnConnect = new java.awt.GridBagConstraints();
				constraintsBtnConnect.gridx = 1;
				constraintsBtnConnect.gridy = 1;
				constraintsBtnConnect.gridwidth = 2;
				constraintsBtnConnect.ipadx = 52;
				constraintsBtnConnect.insets = new java.awt.Insets(8, 19, 9, 101);
				getPnlMain().add(getBtnConnect(), constraintsBtnConnect);

				java.awt.GridBagConstraints constraintsTxtSearchString = new java.awt.GridBagConstraints();
				constraintsTxtSearchString.gridx = 1;
				constraintsTxtSearchString.gridy = 3;
				constraintsTxtSearchString.gridwidth = 2;
				constraintsTxtSearchString.fill = java.awt.GridBagConstraints.HORIZONTAL;
				constraintsTxtSearchString.weightx = 1.0;
				constraintsTxtSearchString.ipadx = 238;
				constraintsTxtSearchString.insets = new java.awt.Insets(2, 10, 14, 1);
				getPnlMain().add(getTxtSearchString(), constraintsTxtSearchString);

				java.awt.GridBagConstraints constraintsBtnSearch = new java.awt.GridBagConstraints();
				constraintsBtnSearch.gridx = 3;
				constraintsBtnSearch.gridy = 2;
				constraintsBtnSearch.gridheight = 2;
				constraintsBtnSearch.ipadx = 10;
				constraintsBtnSearch.insets = new java.awt.Insets(31, 2, 13, 111);
				getPnlMain().add(getBtnSearch(), constraintsBtnSearch);

				java.awt.GridBagConstraints constraintsRbtText = new java.awt.GridBagConstraints();
				constraintsRbtText.gridx = 1;
				constraintsRbtText.gridy = 2;
				constraintsRbtText.ipadx = 58;
				constraintsRbtText.insets = new java.awt.Insets(10, 10, 1, 5);
				getPnlMain().add(getRbtText(), constraintsRbtText);

				java.awt.GridBagConstraints constraintsRbtNumber = new java.awt.GridBagConstraints();
				constraintsRbtNumber.gridx = 2;
				constraintsRbtNumber.gridy = 2;
				constraintsRbtNumber.ipadx = 38;
				constraintsRbtNumber.insets = new java.awt.Insets(10, 5, 1, 17);
				getPnlMain().add(getRbtNumber(), constraintsRbtNumber);
				// user code begin {1}
				ivjPnlMain.add(getChxTable(), gridBagConstraints);
				ivjPnlMain.add(getChxView(), gridBagConstraints1);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlMain;
	}

	/**
	 * Return the RbtNumber property value.
	 *
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getRbtNumber() {
		if (ivjRbtNumber == null) {
			try {
				ivjRbtNumber = new javax.swing.JRadioButton();
				ivjRbtNumber.setName("RbtNumber");
				ivjRbtNumber.setText("Number");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRbtNumber;
	}

	/**
	 * Return the RbtText property value.
	 *
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getRbtText() {
		if (ivjRbtText == null) {
			try {
				ivjRbtText = new javax.swing.JRadioButton();
				ivjRbtText.setName("RbtText");
				ivjRbtText.setText("Text");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRbtText;
	}

	/**
	 * Return the StbStatus property value.
	 *
	 * @return ch.softenvironment.view.StatusBar
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ch.softenvironment.view.StatusBar getStbStatusbar() {
		if (ivjStbStatusbar == null) {
			try {
				ivjStbStatusbar = new ch.softenvironment.view.StatusBar();
				ivjStbStatusbar.setName("StbStatusbar");
				ivjStbStatusbar.setMinimumSize(new java.awt.Dimension(8, 14));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjStbStatusbar;
	}

	/**
	 * Return the TlbToolbar property value.
	 *
	 * @return ch.softenvironment.view.ToolBar
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ch.softenvironment.view.ToolBar getTlbToolbar() {
		if (ivjTlbToolbar == null) {
			try {
				ivjTlbToolbar = new ch.softenvironment.view.ToolBar();
				ivjTlbToolbar.setName("TlbToolbar");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTlbToolbar;
	}

	/**
	 * Return the JTextField2 property value.
	 *
	 * @return javax.swing.JTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextField getTxtSearchString() {
		if (ivjTxtSearchString == null) {
			try {
				ivjTxtSearchString = new javax.swing.JTextField();
				ivjTxtSearchString.setName("TxtSearchString");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtSearchString;
	}

	/**
	 * Return the Version of this Application.
	 */
	protected static String getVersion() {
		return "V1.2.0 beta";
	}

	/**
	 * Called whenever the part throws an exception.
	 *
	 * @param exception java.lang.Throwable
	 */
	@Override
	protected void handleException(java.lang.Throwable exception) {
		super.handleException(exception);
	}

	/**
	 * Initializes connections
	 *
	 *
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() {
		// user code begin {1}
		// user code end
		getMncStatusbar().addActionListener(ivjEventHandler);
		getMniAboutBox().addActionListener(ivjEventHandler);
		getMniHelpTopics().addActionListener(ivjEventHandler);
		getMncToolbar().addActionListener(ivjEventHandler);
		getMniExit().addActionListener(ivjEventHandler);
		getBtnAnalyzeSchema().addActionListener(ivjEventHandler);
		getBtnConnect().addActionListener(ivjEventHandler);
		getBtnSearch().addActionListener(ivjEventHandler);
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			initializeView();
			// user code end
			setName("LauncherView");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("DB-Schema Analyzer");
			setSize(451, 650);
			setJMenuBar(getLauncherViewJMenuBar());
			setContentPane(getJFrameContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		ButtonGroup group = new ButtonGroup();
		group.add(getRbtText());
		group.add(getRbtNumber());
		getRbtText().setSelected(true);
		// user code end
	}

	/**
	 * Setup the database connection.
	 */
	private static DbObjectServer initializeDatabase(String userId, String password, String url) throws Exception {
		// ch.softenvironment.jomm.nlsflat.Registry.registerAll();
		// ch.softenvironment.business.persistency.Registry.registerAll();

		javax.jdo.PersistenceManagerFactory pmFactory = // new
			// ch.softenvironment.jdo.oracle.OracleObjectServerFactory();
			new ch.softenvironment.jomm.target.sql.msaccess.MsAccessObjectServerFactory();
		pmFactory.setConnectionURL(url);
		pmFactory.setNontransactionalRead(false); // NO autoCommit while reading
		pmFactory.setNontransactionalWrite(false); // NO autoCommit while
		// writing
		DbObjectServer objSrv = (DbObjectServer) pmFactory.getPersistenceManager(userId, password);

		// settings = getUserProfile(objSrv, userId);

		return objSrv;
	}

	/**
	 * Initialize the view.
	 */
	@Override
	protected void initializeView() {
		createLookAndFeelMenu(getMnuLookAndFeel());
		getTlbToolbar().toggleVisbility();
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 *
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		DbSchemaAnalyzerView instance = null;
		try {
			setSystemLookAndFeel();

			showSplashScreen(new java.awt.Dimension(500, 400), ResourceManager.getImageIcon(DbSchemaAnalyzerView.class, "splash.png"));

			instance = new DbSchemaAnalyzerView(new ViewOptions(), null);
			// getInstance().addDefaultClosingListener();
			// instance.setLookAndFeel(settings.getLookAndFeel());
			java.awt.Insets insets = instance.getInsets();
			instance.setSize(instance.getWidth() + insets.left + insets.right, instance.getHeight() + insets.top + insets.bottom);
			// ViewOptions
			// instance.getViewOptions().setOption("ShowProjectLepra");
			instance.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of javax.swing.JFrame");//$NON-NLS-1$
			exception.printStackTrace(System.out);
			showException(instance, /*
			 * resLauncherView.getString("CTLoginFailure"
			 * ), "...",
			 */exception); //$NON-NLS-1$
			System.exit(-1);
		}
	}

	/**
	 * Show About Dialog
	 */
	private void mniAboutBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		new AboutDialog(this, "DB-Schema-Analyzer", getVersion(), "2004", null);
	}

	/**
	 * Show Help
	 */
	private void mniHelpTopics_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		BrowserControl.displayURL("http://www.softenvironment.ch");
	}

	/**
	 * Compare column-contents of type String with search-Expression.
	 */
	private void search(final String tableName, final java.util.List<java.util.List<String>> columnContainer) {
		try {
			final DbUserTransactionBlock block = server.createUserTransactionBlock(true);
			block.execute(new Runnable() {
				@Override
				public void run() {
					try {
						java.util.List<String> columns = null;
						if (getRbtText().isSelected()) {
							columns = columnContainer.get(0);
						} else {
							columns = columnContainer.get(1);
						}
						if ((columns == null) || (columns.size() == 0)) {
							// getPnlLog().append("->no Strings at all\n");
							return;
						}
						String searchText = getTxtSearchString().getText();

						java.util.Iterator<String> iterator = columns.iterator();
						while (iterator.hasNext()) {
							String column = iterator.next();
							// updateProgress(-1, tableName + "." + column);

							DbQueryBuilder builder = block.getObjectServer().createQueryBuilder(DbQueryBuilder.SELECT, tableName);
							builder.setTableList(tableName);
							builder.setAttributeList(column);
							if (getRbtText().isSelected()) {
								builder.addFilter(column, searchText, DbQueryBuilder.WILD);
							} else {
								// use number as is
								builder.addFilterRaw(column + "=" + searchText);
							}

							DbQuery query = new DbQuery(block.getTransaction(), builder);
							ResultSet rs = (ResultSet) query.execute();
							while (rs.next()) {
								getPnlLog().append("=> Table/Synonym: " + tableName + "\n");
								getPnlLog().append("   [" + column + "]: " + rs.getString(column) + "\n");
							}

							query.closeAll();
						}
					} catch (SQLException e) {
						block.abort(null, e);
					}
				}
			});
		} catch (Throwable e) {
			handleException(e);
		}
	}

	/**
	 * Search all tables for given Search-String.
	 */
	public void searchObjects() {
		showBusy(new Runnable() {
			@Override
			public void run() {
				try {
					if (StringUtils.isNullOrEmpty(getTxtSearchString().getText())) {
						getPnlLog().append("No search-Text given!\n");
						return;
					}

					getPnlLog().append("Searching...\n");

					// getPnlLog().setEnabled(true);
					getPnlLog().clearAll();
					// getPnlLog().setEnabled(false);

					// 1) determine all tables
					WaitDialog.updateProgress(5, "collect tables...");
					DatabaseMetaData dbmd = server.getMetaData();
					ResultSet rs = dbmd.getCatalogs();
					final java.util.Map<String, java.util.List<java.util.List<String>>> tables = new java.util.HashMap<>();
					while (rs.next()) {
						String catalog = rs.getString(1);
						// getPnlLog().append("\nCATALOG=" + catalog + "\n");

						String[] types = {"TABLE", "SYNONYM" /*
															 * MS Access
															 * Verknuepfungen
															 */};
						ResultSet rsTables = dbmd.getTables(catalog, null, null, types);
						while (rsTables.next()) {
							tables.put(rsTables.getString("TABLE_NAME"), null);
						}
						rsTables.close();
					}
					rs.close();

					// 2) determine String-textColumns in each table
					WaitDialog.updateProgress(15, "collect text columns per table...");
					final DbUserTransactionBlock block = server.createUserTransactionBlock(true);
					block.execute(new Runnable() {
						@Override
						public void run() {
							try {
								java.util.Iterator<String> iterator = tables.keySet().iterator();
								while (iterator.hasNext()) {
									String tableName = iterator.next();

									DbQueryBuilder builder = block.getObjectServer().createQueryBuilder(DbQueryBuilder.SELECT, tableName);
									builder.setTableList(tableName);

									DbQuery query = new DbQuery(block.getTransaction(), builder);
									ResultSet rstextColumns = (ResultSet) query.execute();

									// determine String-textColumns
									ResultSetMetaData rsmd = rstextColumns.getMetaData();
									java.util.List<String> textColumns = new java.util.ArrayList<>();
									java.util.List<String> numberColumns = new java.util.ArrayList<>();
									int numberOftextColumns = rsmd.getColumnCount();
									for (int i = 1; i <= numberOftextColumns; i++) {
										// tableName = rsmd.getTableName(i);
										// String targetTypeName =
										// rsmd.getColumnTypeName(i); // DBMS
										// name for type
										int jdbcType = rsmd.getColumnType(i);
										if ((jdbcType == Types.CHAR) || (jdbcType == Types.VARCHAR) || (jdbcType == Types.LONGVARCHAR)) {
											textColumns.add(rsmd.getColumnName(i));
										} else if ((jdbcType == Types.BIGINT) || (jdbcType == Types.DECIMAL) || (jdbcType == Types.DOUBLE)
											|| (jdbcType == Types.FLOAT) || (jdbcType == Types.INTEGER) || (jdbcType == Types.NUMERIC)
											|| (jdbcType == Types.REAL) || (jdbcType == Types.SMALLINT) || (jdbcType == Types.TINYINT)) {
											/*
											 * if (jdbcType != 8) { int stop =
											 * 0; }
											 */
											numberColumns.add(rsmd.getColumnName(i));
										}
										// boolean caseSen =
										// rsmd.isCaseSensitive(i);
										// getPnlLog().append(colName);
									}
									java.util.List<java.util.List<String>> columnContainer = new java.util.ArrayList<java.util.List<String>>(2);
									columnContainer.add(textColumns);
									columnContainer.add(numberColumns);
									tables.put(tableName, columnContainer);

									query.closeAll();
								}
							} catch (SQLException e) {
								block.abort(null, e);
							}
						}
					});

					// 3) Query column in any Table
					int counter = 25;
					int increment = 75 / tables.size();
					WaitDialog.updateProgress(counter, "querying...");
					java.util.Iterator<String> iterator = tables.keySet().iterator();
					while (iterator.hasNext()) {
						String tableName = iterator.next();
						counter += increment;
						WaitDialog.updateProgress(counter, "table: " + tableName);
						if (!StringUtils.isNullOrEmpty(getTxtSearchString().getText())) {
							java.util.List<java.util.List<String>> columnContainer = tables.get(tableName);
							search(tableName, columnContainer);
						}
					}
				} catch (Throwable e) {
					handleException(e);
				}
			}
		});
	}

	/**
	 * This method initializes ChxTable
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChxTable() {
		if (ChxTable == null) {
			ChxTable = new JCheckBox();
			ChxTable.setText("TABLE");
		}
		return ChxTable;
	}

	/**
	 * This method initializes ChxView
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChxView() {
		if (ChxView == null) {
			ChxView = new JCheckBox();
			ChxView.setText("VIEW");
		}
		return ChxView;
	}
}
