package org.tcotool.application;

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

import ch.ehi.basics.i18n.ResourceBundle;
import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.mvc.controller.ConsistencyController;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import org.tcotool.model.Catalogue;
import org.tcotool.model.Currency;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.ModelUtility;

/**
 * DetailView of Catalogue.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class CatalogueDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView {

    private ch.softenvironment.view.DetailView caller = null;
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjPnlDetail = null;
    private boolean ivjConnPtoP2Aligning = false;
    private javax.swing.JPanel ivjJPanel1 = null;
    private Catalogue ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private ch.softenvironment.view.SimpleEditorPanel ivjPnlNote = null;
    private javax.swing.JLabel ivjLblName = null;
    private javax.swing.JComboBox ivjCbxCurrency = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtCost = null;
    private boolean ivjConnPtoP11Aligning = false;
    private javax.swing.JTextField ivjTxtName1 = null;
    private boolean ivjConnPtoP13Aligning = false;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JTextField ivjJTextField1 = null;
    private javax.swing.JLabel ivjLblOrderNumber = null;
    private javax.swing.JLabel ivjLblProducer = null;
    private boolean ivjConnPtoP1Aligning = false;
    private javax.swing.JLabel ivjLblPrice = null;
    private ch.softenvironment.jomm.mvc.view.DbNlsStringView ivjPnlName = null;
    private javax.swing.JLabel ivjLblDepreciationDuration = null;
    private javax.swing.JLabel ivjLblMonthDepreciation = null;
    private javax.swing.JLabel ivjLblMonthTco = null;
    private javax.swing.JLabel ivjLblUsageDuration = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtDepreciation = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtUsage = null;
    private boolean ivjConnPtoP5Aligning = false;
    private boolean ivjConnPtoP7Aligning = false;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ItemListener,
        java.awt.event.KeyListener, java.beans.PropertyChangeListener {

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == CatalogueDetailView.this.getCbxCurrency()) {
                connPtoP11SetSource();
            }
            if (e.getSource() == CatalogueDetailView.this.getChxExpendable()) {
                connPtoP8SetSource();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == CatalogueDetailView.this.getTxtName1()) {
                connPtoP13SetSource();
            }
            if (e.getSource() == CatalogueDetailView.this.getTxtCost()) {
                connPtoP3SetSource();
            }
            if (e.getSource() == CatalogueDetailView.this.getJTextField1()) {
                connPtoP4SetSource();
            }
            if (e.getSource() == CatalogueDetailView.this.getTxtDepreciation()) {
                connPtoP5SetSource();
            }
            if (e.getSource() == CatalogueDetailView.this.getTxtUsage()) {
                connPtoP7SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == CatalogueDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == CatalogueDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == CatalogueDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("currency"))) {
                connPtoP11SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("orderNumber"))) {
                connPtoP13SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("price"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("producer"))) {
                connPtoP4SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("name"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getPnlName() && (evt.getPropertyName().equals("dbNlsString"))) {
                connPtoP2SetSource();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject()) {
                connEtoC2(evt);
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("depreciationDuration"))) {
                connPtoP5SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("usageDuration"))) {
                connPtoP7SetTarget();
            }
            if (evt.getSource() == CatalogueDetailView.this.getObject() && (evt.getPropertyName().equals("expendable"))) {
                connPtoP8SetTarget();
            }
        }

        @Override
        public void tbbCopyAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbCutAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbDeleteAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbFindAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbNewAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbOpenAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbPasteAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbPrintAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbRedoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbSaveAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == CatalogueDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == CatalogueDetailView.this.getPnlNote()) {
                connPtoP1SetSource();
            }
        }

    }

    private javax.swing.JCheckBox ivjChxExpendable = null;
    private boolean ivjConnPtoP8Aligning = false;

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public CatalogueDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public CatalogueDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects, ch.softenvironment.view.DetailView caller) {
        super(viewOptions, objects);
        this.caller = caller;
        initialize();
    }

    /**
     * Assign a set of aggregates given in objects.
     *
     * @param objects
     */
    @Override
    public void assignObjects(java.util.List objects) {
    }

    /**
     * connEtoC1: (PnlStandardToolbar.currentObject --> ServiceDetailView.executeSetCurrentObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.beans.PropertyChangeEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.beans.PropertyChangeEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setCurrentObject(getPnlStandardToolbar().getCurrentObject());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (Object.propertyChange.propertyChange(java.beans.PropertyChangeEvent) --> CatalogueDetailView.updateModel()V)
     *
     * @param arg1 java.beans.PropertyChangeEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.beans.PropertyChangeEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.updateModel(arg1);
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC7: (PnlStandardToolbar.toolBar.tbbSaveAction_actionPerformed(java. util.EventObject) --> ServiceDetailView.executeSaveObject()V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC7(java.util.EventObject arg1) {
        try {
            // user code begin {1}
            // user code end
            this.saveObject();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoM1: (ConsistencyController.inconsistencies --> PnlStandardToolbar.setItems(Ljava.util.Vector;)V)
     *
     * @param arg1 java.beans.PropertyChangeEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoM1(java.beans.PropertyChangeEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            getPnlStandardToolbar().setItems(getConsistencyController().getInconsistencies());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP11SetSource: (Object.currency <--> CbxCurrency.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP11SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP11Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP11Aligning = true;
                if ((getObject() != null)) {
                    getObject().setCurrency((org.tcotool.model.Currency) getCbxCurrency().getSelectedItem());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP11Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP11Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP11SetTarget: (Object.currency <--> CbxCurrency.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP11SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP11Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP11Aligning = true;
                if ((getObject() != null)) {
                    getCbxCurrency().setSelectedItem(getObject().getCurrency());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP11Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP11Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP13SetSource: (Object.orderNumber <--> TxtName1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP13SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP13Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP13Aligning = true;
                if ((getObject() != null)) {
                    getObject().setOrderNumber(getTxtName1().getText());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP13Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP13Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP13SetTarget: (Object.orderNumber <--> TxtName1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP13SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP13Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP13Aligning = true;
                if ((getObject() != null)) {
                    getTxtName1().setText(getObject().getOrderNumber());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP13Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP13Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP18SetTarget: (Object.mark <--> PnlStatusBar.mark)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP18SetTarget() {
        /* Set the target from the source */
        try {
            if ((getObject() != null)) {
                getPnlStatusBar().setMark(getObject().getMark());
            }
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP1SetSource: (Object.documentation <--> PnlNote.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP1SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP1Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP1Aligning = true;
                if ((getObject() != null)) {
                    getObject().setDocumentation(getPnlNote().getText());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP1Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP1Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP1SetTarget: (Object.documentation <--> PnlNote.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP1SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP1Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP1Aligning = true;
                if ((getObject() != null)) {
                    getPnlNote().setText(getObject().getDocumentation());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP1Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP1Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP2SetSource: (Object.name <--> PnlName.name)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP2SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP2Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP2Aligning = true;
                if ((getObject() != null)) {
                    getObject().setName(getPnlName().getDbNlsString());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP2Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP2Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP2SetTarget: (Object.name <--> TxtName.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP2SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP2Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP2Aligning = true;
                if ((getObject() != null)) {
                    getPnlName().setDbNlsString(getObject().getName());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP2Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP2Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP3SetSource: (Object.price <--> TxtCost.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP3SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP3Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP3Aligning = true;
                if ((getObject() != null)) {
                    getObject().setPrice(new java.lang.Double(getTxtCost().getText()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP3Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP3Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP3SetTarget: (Object.price <--> TxtCost.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP3SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP3Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP3Aligning = true;
                if ((getObject() != null)) {
                    getTxtCost().setText(String.valueOf(getObject().getPrice()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP3Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP3Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP4SetSource: (Object.producer <--> JTextField1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP4SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP4Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP4Aligning = true;
                if ((getObject() != null)) {
                    getObject().setProducer(getJTextField1().getText());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP4Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP4Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP4SetTarget: (Object.producer <--> JTextField1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP4SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP4Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP4Aligning = true;
                if ((getObject() != null)) {
                    getJTextField1().setText(getObject().getProducer());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP4Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP4Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP5SetSource: (Object.depreciationDuration <--> TxtDepreciation.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP5SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP5Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP5Aligning = true;
                if ((getObject() != null)) {
                    getObject().setDepreciationDuration(new java.lang.Long(getTxtDepreciation().getText()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP5Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP5Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP5SetTarget: (Object.depreciationDuration <--> TxtDepreciation.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP5SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP5Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP5Aligning = true;
                if ((getObject() != null)) {
                    getTxtDepreciation().setText(String.valueOf(getObject().getDepreciationDuration()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP5Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP5Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP6SetSource: (ConsistencyController.isSaveable <--> PnlStandardToolbar.tbbSaveEnabled)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP6SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP6Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP6Aligning = true;
                if ((getConsistencyController() != null)) {
                    getConsistencyController().setIsSaveable(getPnlStandardToolbar().getTbbSaveEnabled());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP6Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP6Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP6SetTarget: (ConsistencyController.isSaveable <--> PnlStandardToolbar.tbbSaveEnabled)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP6SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP6Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP6Aligning = true;
                if ((getConsistencyController() != null)) {
                    getPnlStandardToolbar().setTbbSaveEnabled(getConsistencyController().getIsSaveable());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP6Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP6Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP7SetSource: (Object.usageDuration <--> TxtUsage.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP7SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP7Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP7Aligning = true;
                if ((getObject() != null)) {
                    getObject().setUsageDuration(new java.lang.Long(getTxtUsage().getText()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP7Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP7Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP7SetTarget: (Object.usageDuration <--> TxtUsage.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP7SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP7Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP7Aligning = true;
                if ((getObject() != null)) {
                    getTxtUsage().setText(String.valueOf(getObject().getUsageDuration()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP7Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP7Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP8SetSource: (Object.expendable <--> ChxExpendable.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP8SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP8Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP8Aligning = true;
                if ((getObject() != null)) {
                    getObject().setExpendable(new java.lang.Boolean(getChxExpendable().isSelected()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP8Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP8Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP8SetTarget: (Object.expendable <--> ChxExpendable.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP8SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP8Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP8Aligning = true;
                if ((getObject() != null)) {
                    getChxExpendable().setSelected((getObject().getExpendable()).booleanValue());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP8Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP8Aligning = false;
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
    public void copyObject(java.lang.Object source) {
    }

    @Override
    public void dispose() {
        getObject().removePropertyChangeListener(getConsistencyController());
        super.dispose();
    }

    /**
     * Return the BaseDialogContentPane property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getBaseDialogContentPane() {
        if (ivjBaseDialogContentPane == null) {
            try {
                ivjBaseDialogContentPane = new javax.swing.JPanel();
                ivjBaseDialogContentPane.setName("BaseDialogContentPane");
                ivjBaseDialogContentPane.setLayout(new java.awt.BorderLayout());
                getBaseDialogContentPane().add(getJPanel1(), "Center");
                getBaseDialogContentPane().add(getPnlStandardToolbar(), "North");
                getBaseDialogContentPane().add(getPnlStatusBar(), "South");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBaseDialogContentPane;
    }

    /**
     * Return the CbxCurrency1 property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrency() {
        if (ivjCbxCurrency == null) {
            try {
                ivjCbxCurrency = new javax.swing.JComboBox();
                ivjCbxCurrency.setName("CbxCurrency");
                ivjCbxCurrency.setBounds(364, 163, 61, 23);
                ivjCbxCurrency.setEnabled(true);
                // user code begin {1}
                ivjCbxCurrency.setBounds(364, 163, 70, 23);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrency;
    }

    /**
     * Return the ChxExpendable property value.
     *
     * @return javax.swing.JCheckBox
     */
    @Deprecated
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxExpendable() {
        if (ivjChxExpendable == null) {
            try {
                ivjChxExpendable = new javax.swing.JCheckBox();
                ivjChxExpendable.setName("ChxExpendable");
                ivjChxExpendable.setText("Verbrauchsmaterial");
                ivjChxExpendable.setBounds(161, 133, 263, 22);
                // user code begin {1}
                ivjChxExpendable.setText(getResourceString("ChxExpendable_text"));
                ivjChxExpendable.setToolTipText(getResourceString("ChxExpendable_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxExpendable;
    }

    /**
     * Return the ConsistencyController property value.
     *
     * @return ch.softenvironment.jomm.controls.ConsistencyController
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.controller.ConsistencyController getConsistencyController() {
        // user code begin {1}
        // user code end
        return ivjConsistencyController;
    }

    /**
     * Return the JPanel1 property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getJPanel1() {
        if (ivjJPanel1 == null) {
            try {
                ivjJPanel1 = new javax.swing.JPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(null);
                getJPanel1().add(getLblName(), getLblName().getName());
                getJPanel1().add(getJTabbedPane1(), getJTabbedPane1().getName());
                getJPanel1().add(getPnlName(), getPnlName().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
    }

    /**
     * Return the JTabbedPane1 property value.
     *
     * @return javax.swing.JTabbedPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTabbedPane getJTabbedPane1() {
        if (ivjJTabbedPane1 == null) {
            try {
                ivjJTabbedPane1 = new javax.swing.JTabbedPane();
                ivjJTabbedPane1.setName("JTabbedPane1");
                ivjJTabbedPane1.setBounds(7, 51, 457, 226);
                ivjJTabbedPane1.insertTab("Detail", null, getPnlDetail(), null, 0);
                ivjJTabbedPane1.insertTab("Notiz", null, getPnlNote(), null, 1);
                // user code begin {1}
                ivjJTabbedPane1.setBounds(7, 51, 457, 240);
                ivjJTabbedPane1.setTitleAt(0, ResourceManager.getResource(ServiceDetailView.class, "PnlDetail_text"));
                ivjJTabbedPane1.setTitleAt(1, ResourceManager.getResource(ServiceDetailView.class, "PnlNote_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTabbedPane1;
    }

    /**
     * Return the JTextField1 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getJTextField1() {
        if (ivjJTextField1 == null) {
            try {
                ivjJTextField1 = new javax.swing.JTextField();
                ivjJTextField1.setName("JTextField1");
                ivjJTextField1.setBounds(161, 24, 199, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTextField1;
    }

    /**
     * Return the LblDepreciationDuration property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDepreciationDuration() {
        if (ivjLblDepreciationDuration == null) {
            try {
                ivjLblDepreciationDuration = new javax.swing.JLabel();
                ivjLblDepreciationDuration.setName("LblDepreciationDuration");
                ivjLblDepreciationDuration.setToolTipText("Buchhalterische Abschreibungsdauer");
                ivjLblDepreciationDuration.setText("Abschreibung (BH):");
                ivjLblDepreciationDuration.setBounds(11, 108, 141, 14);
                // user code begin {1}
                ivjLblDepreciationDuration.setToolTipText(ResourceManager.getResource(FactCostDetailView.class, "LblDepreciationDuration_toolTipText"));
                ivjLblDepreciationDuration.setText(ResourceManager.getResource(FactCostDetailView.class, "LblDepreciationDuration_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDepreciationDuration;
    }

    /**
     * Return the LblMonthDepreciation property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblMonthDepreciation() {
        if (ivjLblMonthDepreciation == null) {
            try {
                ivjLblMonthDepreciation = new javax.swing.JLabel();
                ivjLblMonthDepreciation.setName("LblMonthDepreciation");
                ivjLblMonthDepreciation.setText("Monate");
                ivjLblMonthDepreciation.setBounds(228, 108, 73, 14);
                // user code begin {1}
                ivjLblMonthDepreciation.setText(ResourceManager.getResource(FactCostDetailView.class, "LblMonths_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblMonthDepreciation;
    }

    /**
     * Return the LblMonthTco property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblMonthTco() {
        if (ivjLblMonthTco == null) {
            try {
                ivjLblMonthTco = new javax.swing.JLabel();
                ivjLblMonthTco.setName("LblMonthTco");
                ivjLblMonthTco.setText("Monate");
                ivjLblMonthTco.setBounds(229, 89, 73, 14);
                // user code begin {1}
                ivjLblMonthTco.setText(ResourceManager.getResource(FactCostDetailView.class, "LblMonths_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblMonthTco;
    }

    /**
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblName() {
        if (ivjLblName == null) {
            try {
                ivjLblName = new javax.swing.JLabel();
                ivjLblName.setName("LblName");
                ivjLblName.setText("Bezeichnung:");
                ivjLblName.setBounds(17, 16, 131, 14);
                // user code begin {1}
                ivjLblName.setText(ResourceManager.getResource(ServiceDetailView.class, "LblName_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblName;
    }

    /**
     * Return the LblSerialNumber property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblOrderNumber() {
        if (ivjLblOrderNumber == null) {
            try {
                ivjLblOrderNumber = new javax.swing.JLabel();
                ivjLblOrderNumber.setName("LblOrderNumber");
                ivjLblOrderNumber.setText("Artikel-Nummer:");
                ivjLblOrderNumber.setBounds(11, 52, 143, 14);
                // user code begin {1}
                ivjLblOrderNumber.setText(getResourceString("LblOrderNumber_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblOrderNumber;
    }

    /**
     * Return the LblAmount property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPrice() {
        if (ivjLblPrice == null) {
            try {
                ivjLblPrice = new javax.swing.JLabel();
                ivjLblPrice.setName("LblPrice");
                ivjLblPrice.setText("Kosten:");
                ivjLblPrice.setBounds(11, 167, 141, 14);
                // user code begin {1}
                ivjLblPrice.setText(ResourceManager.getResourceAsLabel(ServiceDetailView.class, "PnlCost_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPrice;
    }

    /**
     * Return the LblProducer property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblProducer() {
        if (ivjLblProducer == null) {
            try {
                ivjLblProducer = new javax.swing.JLabel();
                ivjLblProducer.setName("LblProducer");
                ivjLblProducer.setText("Hersteller:");
                ivjLblProducer.setBounds(11, 25, 143, 14);
                // user code begin {1}
                ivjLblProducer.setText(getResourceString("LblProducer_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblProducer;
    }

    /**
     * Return the LblUsageDuration property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblUsageDuration() {
        if (ivjLblUsageDuration == null) {
            try {
                ivjLblUsageDuration = new javax.swing.JLabel();
                ivjLblUsageDuration.setName("LblUsageDuration");
                ivjLblUsageDuration.setToolTipText("Geplante Einsatzdauer bis zur Abloesung");
                ivjLblUsageDuration.setText("TCO-Nutzungsdauer:");
                ivjLblUsageDuration.setBounds(11, 89, 141, 14);
                // user code begin {1}
                ivjLblUsageDuration.setText(ResourceManager.getResource(FactCostDetailView.class, "LblUsageDuration_text"));
                ivjLblUsageDuration.setToolTipText(ResourceManager.getResource(FactCostDetailView.class, "LblUsageDuration_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblUsageDuration;
    }

    /**
     * Return the Object property value.
     *
     * @return org.tcotool.model.Catalogue
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.Catalogue getObject() {
        // user code begin {1}
        // user code end
        return ivjObject;
    }

    /**
     * Return the PnlDetail property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlDetail() {
        if (ivjPnlDetail == null) {
            try {
                ivjPnlDetail = new javax.swing.JPanel();
                ivjPnlDetail.setName("PnlDetail");
                ivjPnlDetail.setLayout(null);
                getPnlDetail().add(getLblPrice(), getLblPrice().getName());
                getPnlDetail().add(getTxtCost(), getTxtCost().getName());
                getPnlDetail().add(getCbxCurrency(), getCbxCurrency().getName());
                getPnlDetail().add(getLblOrderNumber(), getLblOrderNumber().getName());
                getPnlDetail().add(getTxtName1(), getTxtName1().getName());
                getPnlDetail().add(getLblProducer(), getLblProducer().getName());
                getPnlDetail().add(getJTextField1(), getJTextField1().getName());
                getPnlDetail().add(getLblUsageDuration(), getLblUsageDuration().getName());
                getPnlDetail().add(getLblDepreciationDuration(), getLblDepreciationDuration().getName());
                getPnlDetail().add(getTxtUsage(), getTxtUsage().getName());
                getPnlDetail().add(getTxtDepreciation(), getTxtDepreciation().getName());
                getPnlDetail().add(getLblMonthTco(), getLblMonthTco().getName());
                getPnlDetail().add(getLblMonthDepreciation(), getLblMonthDepreciation().getName());
                getPnlDetail().add(getChxExpendable(), getChxExpendable().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlDetail;
    }

    /**
     * Return the PnlName property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbNlsStringView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbNlsStringView getPnlName() {
        if (ivjPnlName == null) {
            try {
                ivjPnlName = new ch.softenvironment.jomm.mvc.view.DbNlsStringView();
                ivjPnlName.setName("PnlName");
                ivjPnlName.setBounds(149, 11, 309, 26);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlName;
    }

    /**
     * Return the SimpleEditorPanel1 property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.SimpleEditorPanel getPnlNote() {
        if (ivjPnlNote == null) {
            try {
                ivjPnlNote = new ch.softenvironment.view.SimpleEditorPanel();
                ivjPnlNote.setName("PnlNote");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlNote;
    }

    /**
     * Return the PnlStandardToolbar property value.
     *
     * @return ch.softenvironment.view.ToolBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.ToolBar getPnlStandardToolbar() {
        if (ivjPnlStandardToolbar == null) {
            try {
                ivjPnlStandardToolbar = new ch.softenvironment.view.ToolBar();
                ivjPnlStandardToolbar.setName("PnlStandardToolbar");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlStandardToolbar;
    }

    /**
     * Return the PnlStatusBar property value.
     *
     * @return ch.softenvironment.view.StatusBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.StatusBar getPnlStatusBar() {
        if (ivjPnlStatusBar == null) {
            try {
                ivjPnlStatusBar = new ch.softenvironment.view.StatusBar();
                ivjPnlStatusBar.setName("PnlStatusBar");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlStatusBar;
    }

    /**
     * Return the TxtCost property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtCost() {
        if (ivjTxtCost == null) {
            try {
                ivjTxtCost = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtCost.setName("TxtCost");
                ivjTxtCost.setBounds(161, 166, 194, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtCost;
    }

    /**
     * Return the TxtDepreciation property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtDepreciation() {
        if (ivjTxtDepreciation == null) {
            try {
                ivjTxtDepreciation = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtDepreciation.setName("TxtDepreciation");
                ivjTxtDepreciation.setBounds(161, 108, 63, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtDepreciation;
    }

    /**
     * Return the TxtName1 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtName1() {
        if (ivjTxtName1 == null) {
            try {
                ivjTxtName1 = new javax.swing.JTextField();
                ivjTxtName1.setName("TxtName1");
                ivjTxtName1.setBounds(161, 49, 198, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtName1;
    }

    /**
     * Return the TxtUsage property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtUsage() {
        if (ivjTxtUsage == null) {
            try {
                ivjTxtUsage = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtUsage.setName("TxtUsage");
                ivjTxtUsage.setBounds(161, 86, 63, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtUsage;
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
     * @throws java.lang.Exception The exception description.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() throws java.lang.Exception {
        // user code begin {1}
        // user code end
        getPnlStandardToolbar().addPropertyChangeListener(ivjEventHandler);
        getPnlStandardToolbar().addToolBarListener(ivjEventHandler);
        getCbxCurrency().addItemListener(ivjEventHandler);
        getTxtName1().addKeyListener(ivjEventHandler);
        getPnlNote().addSimpleEditorPanelListener(ivjEventHandler);
        getTxtCost().addKeyListener(ivjEventHandler);
        getJTextField1().addKeyListener(ivjEventHandler);
        getPnlName().addPropertyChangeListener(ivjEventHandler);
        getTxtDepreciation().addKeyListener(ivjEventHandler);
        getTxtUsage().addKeyListener(ivjEventHandler);
        getChxExpendable().addItemListener(ivjEventHandler);
        connPtoP6SetTarget();
        connPtoP18SetTarget();
        connPtoP11SetTarget();
        connPtoP13SetTarget();
        connPtoP1SetTarget();
        connPtoP3SetTarget();
        connPtoP4SetTarget();
        connPtoP2SetTarget();
        connPtoP5SetTarget();
        connPtoP7SetTarget();
        connPtoP8SetTarget();
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
            setName("CatalogueDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(474, 365);
            setTitle("Katalog-Teil");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        setIconImage(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png").getImage());
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(Catalogue.class));
        getPnlStandardToolbar().setObjects(getObjects());
        // TODO remove this flag (@see CataloguePart#setExpendable())
        getChxExpendable().setVisible(false);
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
    }

    /**
     * Redo the last undoing changes of an Object represented by this GUI.
     */
    @Override
    public void redoObject() {
    }

    /**
     * Save an Object represented by DetailView.
     *
     * @see #updateModel(java.beans.PropertyChangeEvent)
     */
    @Override
    public void saveObject() {
        try {
            // boolean isNew = getObject().getPersistencyState().isNew();
            getObject().save();
            getObject().getObjectServer().cacheCode(getObject());

            if (caller != null) {
                caller.assignObjects(ch.softenvironment.util.ListUtils.createList(getObject()));
            }

            LauncherView.getInstance().saveObject();
            closeOnSave();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Set the ConsistencyController to a new value.
     *
     * @param newValue ch.softenvironment.jomm.controls.ConsistencyController
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void setConsistencyController(ch.softenvironment.jomm.mvc.controller.ConsistencyController newValue) {
        if (ivjConsistencyController != newValue) {
            try {
                /* Stop listening for events from the current object */
                if (ivjConsistencyController != null) {
                    ivjConsistencyController.removePropertyChangeListener(ivjEventHandler);
                }
                ivjConsistencyController = newValue;

                /* Listen for events from the new object */
                if (ivjConsistencyController != null) {
                    ivjConsistencyController.addPropertyChangeListener(ivjEventHandler);
                }
                connPtoP6SetTarget();
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        // user code begin {3}
        // user code end
    }

    /**
     * Make the View represent the given Object.
     *
     * @param currentObject Example Code: try { if ((object != null) && object.equals(getObject())) { return; } if (getObject() != null) {
     *     getObject().removeChangeListener(getConsistencyController()); } ((DbObject)object).refresh(true); setObject(object); object.addChangeListener(getconsistencyController()); } catch(Exception
     *     e) { handleException(e); }
     */
    @Override
    public void setCurrentObject(java.lang.Object object) {
        try {
            if (getObject() != null) {
                getObject().removePropertyChangeListener(getConsistencyController());
            }

            JComboBoxUtility.initComboBox(getCbxCurrency(), ((Catalogue) object).getObjectServer().retrieveCodes(Currency.class), DbObject.PROPERTY_NAME,
                false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));

            if (((Catalogue) object).getPersistencyState().isNew()) {
                ((Catalogue) object).refresh(false);
                ((Catalogue) object).setCurrency(LauncherView.getInstance().getUtility().getSystemParameter().getDefaultCurrency());

                ((Catalogue) object).setExpendable(Boolean.FALSE);
                ((Catalogue) object).setDepreciationDuration(LauncherView.getInstance().getUtility().getSystemParameter().getDefaultDepreciationDuration());
                ((Catalogue) object).setUsageDuration(LauncherView.getInstance().getUtility().getSystemParameter().getDefaultUsageDuration());
            }

            setObject((org.tcotool.model.Catalogue) object);
            getObject().addPropertyChangeListener(getConsistencyController());
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Set the Object to a new value.
     *
     * @param newValue org.tcotool.model.Catalogue
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void setObject(org.tcotool.model.Catalogue newValue) {
        if (ivjObject != newValue) {
            try {
                /* Stop listening for events from the current object */
                if (ivjObject != null) {
                    ivjObject.removePropertyChangeListener(ivjEventHandler);
                }
                ivjObject = newValue;

                /* Listen for events from the new object */
                if (ivjObject != null) {
                    ivjObject.addPropertyChangeListener(ivjEventHandler);
                }
                connPtoP18SetTarget();
                connPtoP11SetTarget();
                connPtoP13SetTarget();
                connPtoP1SetTarget();
                connPtoP3SetTarget();
                connPtoP4SetTarget();
                connPtoP2SetTarget();
                connPtoP5SetTarget();
                connPtoP7SetTarget();
                connPtoP8SetTarget();
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        // user code begin {3}
        // user code end
    }

    /**
     * Undo the changes of an Object represented by this GUI.
     */
    @Override
    public void undoObject() {
        setCurrentObject(getObject());
    }

    /**
     * Make sure any FactCost entries are recalculated.
     */
    private synchronized void updateModel(java.beans.PropertyChangeEvent evt) {
        try {
            if (evt != null) {
                // be aware of ping-pong effect
                // @see #saveObject() for event "name"
                if (evt.getPropertyName().equals("price") || evt.getPropertyName().equals("currency") || evt.getPropertyName().equals("usageDuration")
                    || evt.getPropertyName().equals("depreciationDuration") /*
                 * ||
                 * expendable
                 */) {

                    // update FactCost entries
                    LauncherView.getInstance().getUtility().updateCost((TcoObject) LauncherView.getInstance().getUtility().getRoot());
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }
}
