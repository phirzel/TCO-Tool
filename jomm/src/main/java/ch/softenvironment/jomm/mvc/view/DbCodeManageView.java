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

import ch.softenvironment.client.UserActionRights;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.util.ListUtils;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.BaseFrame;
import ch.softenvironment.view.CommonUserAccess;
import ch.softenvironment.view.DetailView;
import ch.softenvironment.view.ViewOptions;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import java.awt.Component;
import java.util.EventObject;
import java.util.List;

/**
 * Show a DbCode in a ComboBox and provide New/Change/Remove-Buttons to manipulate the given code-items. Simple codes (having a DbNlsString name property only) can be managed with this view. Extended
 * codes (other properties than #name) can be viewed with a registered DetailView.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class DbCodeManageView extends ch.softenvironment.view.BasePanel implements ch.softenvironment.view.SearchView, DetailView {

    private java.util.List<? extends DbCodeType> codes = null;
    private ch.softenvironment.jomm.DbObjectServer server = null;
    private String codeName = null;
    private java.lang.Class<? extends DbCode> codeType = null;
    private String propertyToShow = null;
    private java.lang.Class<? extends DetailView> detailView = null;
    private ViewOptions viewOptions = null;
    private javax.swing.JButton ivjBtnChange = null;
    private javax.swing.JButton ivjBtnNew = null;
    private javax.swing.JButton ivjBtnRemove = null;
    private javax.swing.JComboBox ivjCbxCode = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.ItemListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DbCodeManageView.this.getBtnChange()) {
				connEtoC1(e);
			}
			if (e.getSource() == DbCodeManageView.this.getBtnNew()) {
				connEtoC2(e);
			}
			if (e.getSource() == DbCodeManageView.this.getBtnRemove()) {
				connEtoC3(e);
			}
        }

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getSource() == DbCodeManageView.this.getCbxCode()) {
				connEtoC4(e);
			}
        }
    }

    /**
     * DbCodeManageView constructor comment.
     */
    public DbCodeManageView() {
        super();
        initialize();
    }

    /**
     * DbCodeManageView constructor comment.
     *
     * @param layout java.awt.LayoutManager
     */
    public DbCodeManageView(java.awt.LayoutManager layout) {
        super(layout);
    }

    /**
     * DbCodeManageView constructor comment.
     *
     * @param layout java.awt.LayoutManager
     * @param isDoubleBuffered boolean
     */
    public DbCodeManageView(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
     * DbCodeManageView constructor comment.
     *
     * @param isDoubleBuffered boolean
     */
    public DbCodeManageView(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
     * @see ch.softenvironment.view.ListMenuChoice#adaptUserAction(EventObject, Object)
     */
    @Override
    public void adaptUserAction(EventObject event, Object control) {
        UserActionRights rights = viewOptions.getViewManager().getRights(codeType);
        getBtnNew().setEnabled(rights.isNewObjectAllowed());

        boolean validItem = (getCbxCode().getSelectedItem() != null);
        // do not show detail if not saveable for non-extended codes
        boolean detailSenseful = (detailView != null) ||
            ((detailView == null) && rights.isSaveObjectAllowed());
        getBtnChange().setEnabled(rights.isReadObjectAllowed() && validItem && detailSenseful);

        getBtnRemove().setEnabled(rights.isRemoveObjectsAllowed() && validItem);
    }

    /**
     * Assign the selected Objects in a SearchTable to the caller.
     *
     * @throws Throwable Handled by this GUI-Method.
     * @see DbTableModel
     */
    @Override
    public void assignObjects() {
    }

    /**
     * Change the currently selected code in ComboBox.
     *
     * @see BaseFrame#genericPopupDisplay(..)
     */
    @Override
    public void changeObjects(java.lang.Object source) {
        try {
            if (detailView == null) {
                DbSimpleCodeView dialog = new DbSimpleCodeView(this, codeName, ((DbCode) getCbxCode().getSelectedItem()).getName());
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    ((DbCode) getCbxCode().getSelectedItem()).save();
                    refreshCodes();
                } else {
                    //undo change
                }
            } else {
                showCode(((DbCode) getCbxCode().getSelectedItem()), null);
            }
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
     * connEtoC1:  (BtnChange.action.actionPerformed(java.awt.event.ActionEvent) --> DbCodeManageView.changeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getBtnChange());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2:  (BtnNew.action.actionPerformed(java.awt.event.ActionEvent) --> DbCodeManageView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getBtnNew());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3:  (BtnRemove.action.actionPerformed(java.awt.event.ActionEvent) --> DbCodeManageView.removeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.removeObjects(getBtnRemove());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC4:  (CbxCode.item.itemStateChanged(java.awt.event.ItemEvent) --> DbCodeManageView.itemChanged()V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.itemChanged();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * Create a new Object as a "copy" of a selected one (and open it for e.g. in a DetailView).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void copyObject(java.lang.Object source) {
    }

    /**
     * Return the BtnChangeRole property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnChange() {
        if (ivjBtnChange == null) {
            try {
                ivjBtnChange = new javax.swing.JButton();
                ivjBtnChange.setName("BtnChange");
                ivjBtnChange.setToolTipText("Aendern...");
                ivjBtnChange.setPreferredSize(new java.awt.Dimension(25, 25));
                ivjBtnChange.setText("...");
                ivjBtnChange.setBounds(349, 0, 25, 25);
                // user code begin {1}
                ivjBtnChange.setText("");
                ivjBtnChange.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(ch.softenvironment.view.ToolBar.class, "open.gif"));
                ivjBtnChange.setToolTipText(CommonUserAccess.getMniFileOpenWindowText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnChange;
    }

    /**
     * Return the BtnNewRole property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnNew() {
        if (ivjBtnNew == null) {
            try {
                ivjBtnNew = new javax.swing.JButton();
                ivjBtnNew.setName("BtnNew");
                ivjBtnNew.setToolTipText("Neu...");
                ivjBtnNew.setText("");
                ivjBtnNew.setBounds(320, 0, 25, 25);
                // user code begin {1}
                ivjBtnNew.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(ch.softenvironment.view.ToolBar.class, "new.gif"));
                ivjBtnNew.setToolTipText(CommonUserAccess.getMniFileNewWindowText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnNew;
    }

    /**
     * Return the BtnRemoveRole property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnRemove() {
        if (ivjBtnRemove == null) {
            try {
                ivjBtnRemove = new javax.swing.JButton();
                ivjBtnRemove.setName("BtnRemove");
                ivjBtnRemove.setToolTipText("Loeschen...");
                ivjBtnRemove.setText("");
                ivjBtnRemove.setBounds(378, 0, 25, 25);
                // user code begin {1}
                ivjBtnRemove.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(ch.softenvironment.view.ToolBar.class, "delete.gif"));
                ivjBtnRemove.setToolTipText(CommonUserAccess.getMniEditRemoveText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnRemove;
    }

    /**
     * Return the CbxRole property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCode() {
        if (ivjCbxCode == null) {
            try {
                ivjCbxCode = new javax.swing.JComboBox();
                ivjCbxCode.setName("CbxCode");
                ivjCbxCode.setBounds(0, 0, 315, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCode;
    }

    @Override
    protected void handleException(java.lang.Throwable exception) {
        super.handleException(exception);
    }

    /**
     * Initializes connections
     *
     * @throws java.lang.Exception The exception description.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() throws java.lang.Exception {
        // user code begin {1}
        // user code end
        getBtnChange().addActionListener(ivjEventHandler);
        getBtnNew().addActionListener(ivjEventHandler);
        getBtnRemove().addActionListener(ivjEventHandler);
        getCbxCode().addItemListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("DbCodeManageView");
            setLayout(null);
            setSize(404, 25);
            add(getCbxCode(), getCbxCode().getName());
            add(getBtnChange(), getBtnChange().getName());
            add(getBtnNew(), getBtnNew().getName());
            add(getBtnRemove(), getBtnRemove().getName());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        // user code end
    }

    /**
     * @deprecated
     */
    private void itemChanged() {
        adaptUserAction(null, null);
    }

    /**
     * Create a new Object (and open it for e.g. in a DetailView).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void newObject(java.lang.Object source) {
        try {
            if (detailView == null) {
                DbCode code = (DbCode) server.createInstance(codeType);
                DbSimpleCodeView dialog = new DbSimpleCodeView(this, codeName, code.getName());
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    code.save();
                    server.cacheCode(code);
                    refreshCodes();
                    getCbxCode().setSelectedItem(code);
                }
            } else {
                DbCode code = (ch.softenvironment.jomm.mvc.model.DbCode) server.createInstance(codeType);
                code.refresh(false);

                showCode(code, this);
            }
/*	
		DbCode code = (DbCode)server.createInstance(codeType);
		code.setName((ch.softenvironment.jomm.DbNlsString)server.createInstance(ch.softenvironment.jomm.DbNlsString.class));

		java.util.List objects = new java.util.ArrayList();
		objects.add(code);
		ch.softenvironment.view.BaseFrame view = new RoleDetailView(viewOptions, objects, this);
		view.setRelativeLocation(this);
		view.setVisible(true);
		*/
        } catch (Throwable e) {
            handleException(e);
        }
    }

    private void showCode(DbCode code, DetailView caller) throws Exception {
        Class[] types = {ViewOptions.class, java.util.List.class, DetailView.class};
        Object[] args = {viewOptions, ListUtils.createList(code), caller};
        java.lang.reflect.Constructor constructor = detailView.getConstructor(types);
        BaseFrame detail = (BaseFrame) constructor.newInstance(args);
        Component parent = BaseDialog.getFrameOwner(this);
        if (parent == null) {
            detail.setRelativeLocation(BaseDialog.getDialogOwner(this));
        } else {
            detail.setRelativeLocation(parent);
        }
        detail.setVisible(true);
    }

    /**
     * Reload all Codes.
     */
    private void refreshCodes() throws Exception {
        codes = /*DbCodeTypeComparator.sort(*/server.retrieveCodes(codeType); //, propertyToShow);
        // update ComboBox-List
        JComboBoxUtility.initComboBox(getCbxCode(), codes, propertyToShow, false, new DbObjectEvaluator());
        adaptUserAction(null, null);
    }

    /**
     * Remove the selected Object's (for e.g from a JTable).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void removeObjects(java.lang.Object source) {
        if (ch.softenvironment.view.BaseDialog.showConfirmDeletion(
            this, getResourceString("CTRemoveCode"),
            getResourceString("CIRemoveCode"))) {
/*		
		showBusy(new Runnable() {
			public void run() {
*/
            try {
                DbCode code = (ch.softenvironment.jomm.mvc.model.DbCode) getCbxCode().getSelectedItem();
                code.getObjectServer().removeCode(code);
                getCbxCode().removeItem(getCbxCode().getSelectedItem());
            } catch (Throwable e) {
                handleException(e);
            }
/*				
			}
		});
*/
        }
    }

    /**
     * A SearchView usually offers a set of Query-Fields to make the searching of objects more accurate and performant. Therefore a reset of all SearchArguments may be initialized here.
     */
    @Override
    public void resetSearchArguments() {
    }

    /**
     * Search for Objects.
     *
     * @throws Throwable Handled by this GUI-Method.
     * @see DbTableModel#setQuery(..)
     */
    @Override
    public void searchObjects() {
    }

    /**
     * Set all parameters to display and managede the concrete DbCode. If detailView is null the default DetailView is used implicitely, otherwise the given DetailView-Class is assumed to have a
     * Constructor as follows: Constructor(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects, ch.softenvironment.view.DetailView caller)
     *
     * @param server
     * @param type
     * @param codeName
     * @param propertyToShow
     * @param detailView
     */
    public void setCode(ch.softenvironment.jomm.DbObjectServer server, java.lang.Class<? extends DbCode> type, final String codeName, final String propertyToShow,
        java.lang.Class<? extends DetailView> detailView, ViewOptions viewOptions) throws Exception {
        this.server = server;
        this.codeType = type;
        this.codeName = codeName;
        this.propertyToShow = propertyToShow;
        this.detailView = detailView;
        this.viewOptions = viewOptions;

        getCbxCode().setToolTipText(codeName);
        refreshCodes();
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.view.DetailView#assignObjects(java.util.List)
     */
    @Override
    public void assignObjects(List objects) {
        try {
            if ((objects != null) && (objects.size() > 0)) {
                refreshCodes();
                getCbxCode().setSelectedItem(objects.get(0));
            }
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.view.DetailView#saveObject()
     */
    @Override
    public void saveObject() {
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.view.DetailView#setCurrentObject(java.lang.Object)
     */
    @Override
    public void setCurrentObject(Object object) {
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.view.DetailView#undoObject()
     */
    @Override
    public void undoObject() {
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.view.DetailView#redoObject()
     */
    @Override
    public void redoObject() {
    }
}