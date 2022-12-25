package ch.softenvironment.jomm.mvc.view;
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
import ch.softenvironment.client.UserActionRights;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.controller.DbObjectListener;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.CommonUserAccess;
import ch.softenvironment.view.ListMenuChoice;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import ch.softenvironment.view.swingext.JListUtility;
import lombok.extern.slf4j.Slf4j;

import java.util.EventObject;
import java.util.List;

/**
 * Allow selection of multiple Codes, especially for Code-Cardinalities [0..*].
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DbMultiCodeView extends ch.softenvironment.view.BasePanel implements ListMenuChoice {

	private static final long serialVersionUID = 1L;
	private String propertyToShow = null;
	private java.lang.Class ownerType = null;
	private DbEntityBean owner = null;
	private String propertyOther = null;
	private DbObjectListener listener = null;
	private DbBaseFrame frame = null;
	private UserActionRights rights = null;
	// generated code
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JList ivjLstEntries = null;
	private javax.swing.JMenuItem ivjMniRemove = null;
	private javax.swing.JPopupMenu ivjMnuPopup = null;
	private javax.swing.JScrollPane ivjScpEntries = null;
	private javax.swing.JMenuItem ivjMniNew = null;
	private java.util.List fieldList = null;
	private javax.swing.JButton ivjBtnCancel = null;
	private javax.swing.JButton ivjBtnOk = null;
	private javax.swing.JComboBox ivjCbxCodes = null;
	private javax.swing.JDialog ivjDlgCode = null;
	private javax.swing.JPanel ivjJDialogContentPane = null;

	class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.MouseListener {

		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DbMultiCodeView.this.getMniNew()) {
				connEtoC3(e);
			}
			if (e.getSource() == DbMultiCodeView.this.getMniRemove()) {
				connEtoC4(e);
			}
			if (e.getSource() == DbMultiCodeView.this.getBtnOk()) {
				connEtoC5(e);
			}
			if (e.getSource() == DbMultiCodeView.this.getBtnCancel()) {
				connEtoM1(e);
			}
		}

		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
		}

		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
		}

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getSource() == DbMultiCodeView.this.getLstEntries()) {
				connEtoC2(e);
			}
		}

		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			if (e.getSource() == DbMultiCodeView.this.getLstEntries()) {
				connEtoC1(e);
			}
		}
	}

	/**
	 * DbMultiCodeView constructor comment.
	 */
	public DbMultiCodeView() {
		super();
		initialize();
	}

	/**
	 * @deprecated (generated VAJ code)
	 */
	private void assignObject() {
		newObject(null);
	}

	/**
	 * connEtoC1:  (LstEntries.mouse.mouseReleased(java.awt.event.MouseEvent) --> DbMultiCodeView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
	 *
	 * @param arg1 java.awt.event.MouseEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.genericPopupDisplay(arg1, getMnuPopup());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2:  (LstEntries.mouse.mousePressed(java.awt.event.MouseEvent) --> DbMultiCodeView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
	 *
	 * @param arg1 java.awt.event.MouseEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC2(java.awt.event.MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.genericPopupDisplay(arg1, getMnuPopup());
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3:  (MniNew1.action.actionPerformed(java.awt.event.ActionEvent) --> DbMultiCodeView.assignObject()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC3(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.assignObject();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4:  (MniRemove.action.actionPerformed(java.awt.event.ActionEvent) --> DbMultiCodeView.removeObject()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC4(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.removeObject();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5:  (BtnOk.action.actionPerformed(java.awt.event.ActionEvent) --> DbMultiCodeView.okPressed()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC5(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.okPressed();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoM1:  (BtnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> DlgCode.dispose()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getDlgCode().dispose();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * Return the BtnCancel property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnCancel() {
		if (ivjBtnCancel == null) {
			try {
				ivjBtnCancel = new javax.swing.JButton();
				ivjBtnCancel.setName("BtnCancel");
				ivjBtnCancel.setText("Abbrechen");
				ivjBtnCancel.setBounds(193, 110, 104, 25);
				// user code begin {1}
				ivjBtnCancel.setText(ResourceManager.getResource(BaseDialog.class, "BtnCancel_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnCancel;
	}

	/**
	 * Return the BtnOk property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnOk() {
		if (ivjBtnOk == null) {
			try {
				ivjBtnOk = new javax.swing.JButton();
				ivjBtnOk.setName("BtnOk");
				ivjBtnOk.setText("OK");
				ivjBtnOk.setBounds(82, 110, 104, 25);
				// user code begin {1}
				ivjBtnOk.setText(ResourceManager.getResource(BaseDialog.class, "BtnOK_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnOk;
	}

	/**
	 * Return the CbxCodes property value.
	 *
	 * @return javax.swing.JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getCbxCodes() {
		if (ivjCbxCodes == null) {
			try {
				ivjCbxCodes = new javax.swing.JComboBox();
				ivjCbxCodes.setName("CbxCodes");
				ivjCbxCodes.setBounds(10, 12, 348, 23);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbxCodes;
	}

	/**
	 * Return the DlgCode property value.
	 *
	 * @return javax.swing.JDialog
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JDialog getDlgCode() {
		if (ivjDlgCode == null) {
			try {
				ivjDlgCode = new javax.swing.JDialog();
				ivjDlgCode.setName("DlgCode");
				ivjDlgCode.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				ivjDlgCode.setBounds(71, 131, 377, 175);
				ivjDlgCode.setTitle("Code zuordnen");
				getDlgCode().setContentPane(getJDialogContentPane());
				// user code begin {1}
				ivjDlgCode.setTitle(getResourceString("DlgCode_title"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjDlgCode;
	}

	/**
	 * Return the JDialogContentPane property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJDialogContentPane() {
		if (ivjJDialogContentPane == null) {
			try {
				ivjJDialogContentPane = new javax.swing.JPanel();
				ivjJDialogContentPane.setName("JDialogContentPane");
				ivjJDialogContentPane.setLayout(null);
				getJDialogContentPane().add(getCbxCodes(), getCbxCodes().getName());
				getJDialogContentPane().add(getBtnOk(), getBtnOk().getName());
				getJDialogContentPane().add(getBtnCancel(), getBtnCancel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJDialogContentPane;
	}

	/**
	 * Return the list of chosen Objects.
	 *
	 * @return java.util.List
	 * @see #setList
	 */
	public java.util.List getList() {
		return fieldList;
	}

	/**
	 * Return the JList1 property value.
	 *
	 * @return javax.swing.JList
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JList getLstEntries() {
		if (ivjLstEntries == null) {
			try {
				ivjLstEntries = new javax.swing.JList();
				ivjLstEntries.setName("LstEntries");
				ivjLstEntries.setBounds(0, 0, 160, 120);
				ivjLstEntries.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLstEntries;
	}

	/**
	 * Return the MniNew1 property value.
	 *
	 * @return javax.swing.JMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuItem getMniNew() {
		if (ivjMniNew == null) {
			try {
				ivjMniNew = new javax.swing.JMenuItem();
				ivjMniNew.setName("MniNew");
				ivjMniNew.setText("Neu...");
				// user code begin {1}
				ivjMniNew.setText(CommonUserAccess.getMniFileNewWindowText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMniNew;
	}

	/**
	 * Return the MniRemove property value.
	 *
	 * @return javax.swing.JMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuItem getMniRemove() {
		if (ivjMniRemove == null) {
			try {
				ivjMniRemove = new javax.swing.JMenuItem();
				ivjMniRemove.setName("MniRemove");
				ivjMniRemove.setText("L�schen");
				ivjMniRemove.setEnabled(false);
				// user code begin {1}
				ivjMniRemove.setText(CommonUserAccess.getMniEditRemoveText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMniRemove;
	}

	/**
	 * Return the MnuPopup property value.
	 *
	 * @return javax.swing.JPopupMenu
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPopupMenu getMnuPopup() {
		if (ivjMnuPopup == null) {
			try {
				ivjMnuPopup = new javax.swing.JPopupMenu();
				ivjMnuPopup.setName("MnuPopup");
				ivjMnuPopup.add(getMniNew());
				ivjMnuPopup.add(getMniRemove());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMnuPopup;
	}

	/**
	 * Return the JScrollPane1 property value.
	 *
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getScpEntries() {
		if (ivjScpEntries == null) {
			try {
				ivjScpEntries = new javax.swing.JScrollPane();
				ivjScpEntries.setName("ScpEntries");
				ivjScpEntries.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				getScpEntries().setViewportView(getLstEntries());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjScpEntries;
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
		getLstEntries().addMouseListener(ivjEventHandler);
		getMniNew().addActionListener(ivjEventHandler);
		getMniRemove().addActionListener(ivjEventHandler);
		getBtnOk().addActionListener(ivjEventHandler);
		getBtnCancel().addActionListener(ivjEventHandler);
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("DbMultiCodeView");
			setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
			setSize(240, 120);
			add(getScpEntries(), getScpEntries().getName());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Add chosen Element(s) to owner.
	 */
	private void okPressed() {
		if ((getList() != null) && (getCbxCodes().getSelectedItem() != null)) {
			boolean contained = false;
			for (int i = 0; i < getList().size(); i++) {
				if (getList().get(i).equals(getCbxCodes().getSelectedItem())) {
					// already contained in List
					contained = true;
					break;
				}
			}
			// update displayable List
			if (!contained) {
				java.util.List changedList = new java.util.ArrayList(getList()); // make sure changed-event will be triggered
				DbObject selection = (DbObject) getCbxCodes().getSelectedItem();
				if (frame == null) {
					// 1:n
					changedList.add(selection);
				} else {
					try {
						// n:n
						frame.assignAssociations(ownerType, owner, propertyOther, ch.softenvironment.util.ListUtils.createList(selection), listener);

						// display the changed list
					} catch (Exception e) {
						frame.handleException(e);
					}
				}
				setList(changedList);
			}
		}
		getDlgCode().dispose();
	}

	/**
	 * Set the list of currently chosen Objects (subset of all possible entries).
	 *
	 * @param list
	 * @see #getList()
	 * @see #setListType(List, String)  which precedes this method
	 */
	public void setList(java.util.List list) {
		java.util.List oldValue = fieldList;
		fieldList = list;
		firePropertyChange("list", oldValue, list);

		//	java.util.Iterator iterator = list.iterator();
		//	while (iterator.hasNext()) {
		//		DbObject object = (DbObject)iterator.next();
		if (frame == null) {
			// case 1:n => object to display directly in the list
			//			object.setPropertyToShow(propertyToShow);
			JListUtility.initList(getLstEntries(), list, propertyToShow, new DbObjectEvaluator());
		} else {
			try {
				// case n:n => object to display is "hided" in a DbRelationshipBean
				JListUtility.initList(getLstEntries(), list, propertyOther, new DbObjectEvaluator());
			} catch (Throwable e) {
				log.warn("Developer warning", e);
			}
		}
		//	}

		//	getLstEntries().setListData(list.toArray());
	}

	/**
	 * Initialize the part for 1:n relationships. Set the total possible entries to be selected by List.
	 *
	 * @param entries Selection-List (all possible entries of DbObject's)
	 * @property Property to be displayed of each Item in allEntries
	 */
	public void setListType(java.util.List entries, final String property) {
		propertyToShow = property;
		JComboBoxUtility.initComboBox(getCbxCodes(), entries, property, false, new DbObjectEvaluator());
	}

	/**
	 * Initialize the Part for n:n Relationships, therefore elements must be instances of DbRelationship. Set the total possible entries to be selected by List.
	 *
	 * @see DbBaseFrame#assignAssociations(Class, DbEntityBean, String, List, DbObjectListener)
	 */
	public void setListType(java.util.List entries, final String property, java.lang.Class ownerType, DbEntityBean owner, final String propertyOther, DbObjectListener listener, DbBaseFrame frame) {
		setListType(entries, property);

		this.ownerType = ownerType;
		this.owner = owner;
		this.propertyOther = propertyOther;
		this.listener = listener;
		this.frame = frame;
	}

	public void setRights(UserActionRights rights) {
		this.rights = rights;
	}

	@Override
	public void adaptUserAction(EventObject event, Object control) {
		// enable/disable buttons
		if (rights != null) {
			getMniNew().setEnabled(rights.isNewObjectAllowed());
			if (!rights.isRemoveObjectsAllowed()) {
				getMniRemove().setEnabled(false);
				return;
			}
		}

		getMniRemove().setEnabled(getLstEntries().getSelectedValue() != null);
		//  getMniSelectAll().setEnabled(getTxaEditor().getText().length() > 0);
	}

	@Override
	public void changeObjects(Object source) {
	}

	@Override
	public void copyObject(Object source) {
	}

	@Override
	public void newObject(Object source) {
		getDlgCode().setVisible(true);
	}

	/*
	 * @deprecated (generated VAJ code)
	 */
	private void removeObject() {
		removeObjects(null);
	}

	@Override
	public void removeObjects(Object source) {
		if (frame == null) {
			try {
				// case 1:n
				java.util.List changedList = new java.util.ArrayList(getList());
				changedList.remove(getLstEntries().getSelectedValue());
				setList(changedList);
			} catch (Throwable e) {
				handleException(e);
			}
		} else {
			// case n:n
			try {
				// deletion by model which triggers changed list event
				frame.removeAssociations(ch.softenvironment.util.ListUtils.createList(getLstEntries().getSelectedValue()), owner, propertyOther, listener);
			} catch (Throwable e) {
				throw new ch.softenvironment.util.DeveloperException(e.getLocalizedMessage());
			}
			getLstEntries().setListData(getList().toArray());
		}
	}
}
