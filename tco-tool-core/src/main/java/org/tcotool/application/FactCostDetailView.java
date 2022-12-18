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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.controller.ConsistencyController;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import java.beans.PropertyChangeEvent;
import org.tcotool.model.Catalogue;
import org.tcotool.model.CostCause;
import org.tcotool.model.Currency;
import org.tcotool.model.FactCost;
import org.tcotool.tools.ModelUtility;

/**
 * DetailView of Fact-Costs.
 *
 * @author Peter Hirzel
 */

public class FactCostDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView, java.beans.PropertyChangeListener {

    private ch.softenvironment.view.DetailView caller = null;
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjPnlDetail = null;
    private boolean ivjConnPtoP1Aligning = false;
    private boolean ivjConnPtoP2Aligning = false;
    private javax.swing.JPanel ivjJPanel1 = null;
    private FactCost ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JLabel ivjLblMultitude = null;
    private boolean ivjConnPtoP9Aligning = false;
    private ch.softenvironment.view.SimpleEditorPanel ivjPnlNote = null;
    private javax.swing.JLabel ivjLblName = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtMultitude = null;
    private javax.swing.JTextField ivjTxtName = null;
    private javax.swing.JComboBox ivjCbxCurrency = null;
    private javax.swing.JCheckBox ivjChxEstimated = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtCost = null;
    private javax.swing.JComboBox ivjCbxCostType = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtDepreciation = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtUsage = null;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP5Aligning = false;
    private boolean ivjConnPtoP7Aligning = false;
    private boolean ivjConnPtoP8Aligning = false;
    private boolean ivjConnPtoP10Aligning = false;
    private javax.swing.JComboBox ivjCbxCurrencyTotal = null;
    private javax.swing.JLabel ivjLblAmount = null;
    private javax.swing.JLabel ivjLblTotal = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtCostTotal = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjJTextField31111 = null;
    private boolean ivjConnPtoP11Aligning = false;
    private javax.swing.JLabel ivjLblSerialNumber = null;
    private javax.swing.JPanel ivjPnlFactTechInfo = null;
    private javax.swing.JPanel ivjPnlStorage = null;
    private javax.swing.JTextField ivjTxtName1 = null;
    private boolean ivjConnPtoP13Aligning = false;
    private boolean ivjConnPtoP14Aligning = false;
    private boolean ivjConnPtoP15Aligning = false;
    private boolean ivjConnPtoP16Aligning = false;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtPortsISL = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtPortsServer = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtPortsUseable = null;
    private boolean ivjConnPtoP17Aligning = false;
    private javax.swing.JCheckBox ivjChxRepeatable = null;
    private javax.swing.JLabel ivjLblDepreciationDuration = null;
    private javax.swing.JLabel ivjLblPortsIsl = null;
    private javax.swing.JLabel ivjLblPortsServer = null;
    private javax.swing.JLabel ivjLblPortsTotal = null;
    private javax.swing.JLabel ivjLblPortsUsable = null;
    private javax.swing.JLabel ivjLblUsageDuration = null;
    private javax.swing.JLabel ivjLblMonthDepreciation = null;
    private javax.swing.JLabel ivjLblMonthTco = null;
    private javax.swing.JLabel ivjLblCatalogue = null;
    private boolean ivjConnPtoP19Aligning = false;
    private javax.swing.JComboBox ivjCbxCatalogue = null;
    private javax.swing.JLabel ivjLblCause = null;
    private javax.swing.JLabel ivjLblBaseOffset = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtBaseOffset = null;
    private javax.swing.JLabel ivjLblMonthBaseOffset = null;
    private javax.swing.JCheckBox ivjChxExpendable = null;
    private boolean ivjConnPtoP20Aligning = false;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ItemListener, java.awt.event.KeyListener,
        java.beans.PropertyChangeListener {

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == FactCostDetailView.this.getChxEstimated()) {
                connPtoP5SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getCbxCostType()) {
                connPtoP10SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getCbxCurrency()) {
                connPtoP11SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getChxRepeatable()) {
                connPtoP17SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getCbxCatalogue()) {
                connPtoP19SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getCbxCurrency()) {
                connPtoP12SetTarget();
            }
            if (e.getSource() == FactCostDetailView.this.getChxExpendable()) {
                connPtoP20SetSource();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == FactCostDetailView.this.getTxtName()) {
                connPtoP2SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtName1()) {
                connPtoP13SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtCost()) {
                connPtoP3SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtUsage()) {
                connPtoP8SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtDepreciation()) {
                connPtoP7SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtPortsUseable()) {
                connPtoP14SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtPortsISL()) {
                connPtoP15SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtPortsServer()) {
                connPtoP16SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtMultitude()) {
                connPtoP4SetSource();
            }
            if (e.getSource() == FactCostDetailView.this.getTxtBaseOffset()) {
                connPtoP9SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == FactCostDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == FactCostDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == FactCostDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("name"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("estimated"))) {
                connPtoP5SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("cause"))) {
                connPtoP10SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("currency"))) {
                connPtoP11SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("serialNumber"))) {
                connPtoP13SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("repeatable"))) {
                connPtoP17SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("amount"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("usageDuration"))) {
                connPtoP8SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("depreciationDuration"))) {
                connPtoP7SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("portsUseable"))) {
                connPtoP14SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("portsISL"))) {
                connPtoP15SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("portsServer"))) {
                connPtoP16SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("multitude"))) {
                connPtoP4SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("catalogue"))) {
                connPtoP19SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("baseOffset"))) {
                connPtoP9SetTarget();
            }
            if (evt.getSource() == FactCostDetailView.this.getObject() && (evt.getPropertyName().equals("expendable"))) {
                connPtoP20SetTarget();
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
            if (newEvent.getSource() == FactCostDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == FactCostDetailView.this.getPnlNote()) {
                connPtoP1SetSource();
            }
        }
    }

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public FactCostDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public FactCostDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects, ch.softenvironment.view.DetailView caller) {
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
     * connEtoC1:  (PnlStandardToolbar.currentObject --> ServiceDetailView.executeSetCurrentObject(Ljava.lang.Object;)V)
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
     * connEtoC7:  (PnlStandardToolbar.toolBar.tbbSaveAction_actionPerformed(java.util.EventObject) --> ServiceDetailView.executeSaveObject()V)
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
     * connEtoM1:  (ConsistencyController.inconsistencies --> PnlStandardToolbar.setItems(Ljava.util.Vector;)V)
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
     * connPtoP10SetSource:  (Object.type <--> CbxCostType.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP10SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP10Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP10Aligning = true;
                if ((getObject() != null)) {
                    getObject().setCause((org.tcotool.model.CostCause) getCbxCostType().getSelectedItem());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP10Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP10Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP10SetTarget:  (Object.type <--> CbxCostType.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP10SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP10Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP10Aligning = true;
                if ((getObject() != null)) {
                    getCbxCostType().setSelectedItem(getObject().getCause());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP10Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP10Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP11SetSource:  (Object.currency <--> CbxCurrency.visible)
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
     * connPtoP11SetTarget:  (Object.currency <--> CbxCurrency.visible)
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
     * connPtoP12SetTarget:  (CbxCurrency.selectedItem <--> CbxCurrencyTotal.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP12SetTarget() {
        /* Set the target from the source */
        try {
            getCbxCurrencyTotal().setSelectedItem(getCbxCurrency().getSelectedItem());
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP13SetSource:  (Object.serialNumber <--> TxtName1.text)
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
                    getObject().setSerialNumber(getTxtName1().getText());
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
     * connPtoP13SetTarget:  (Object.serialNumber <--> TxtName1.text)
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
                    getTxtName1().setText(getObject().getSerialNumber());
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
     * connPtoP14SetSource:  (Object.portsUseable <--> JTextField311112.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP14SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP14Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP14Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setPortsUseable(new java.lang.Long(getTxtPortsUseable().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    // ingore
                    getObject().setPortsUseable(getTxtPortsUseable().getLongValue());
                }
                // user code end
                ivjConnPtoP14Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP14Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP14SetTarget:  (Object.portsUseable <--> JTextField311112.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP14SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP14Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP14Aligning = true;
                if ((getObject() != null)) {
                    getTxtPortsUseable().setText(String.valueOf(getObject().getPortsUseable()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP14Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP14Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP15SetSource:  (Object.portsISL <--> JTextField311111.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP15SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP15Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP15Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setPortsISL(new java.lang.Long(getTxtPortsISL().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    // ingore
                    getObject().setPortsISL(getTxtPortsISL().getLongValue());
                }
                // user code end
                ivjConnPtoP15Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP15Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP15SetTarget:  (Object.portsISL <--> JTextField311111.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP15SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP15Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP15Aligning = true;
                if ((getObject() != null)) {
                    getTxtPortsISL().setText(String.valueOf(getObject().getPortsISL()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP15Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP15Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP16SetSource:  (Object.portsServer <--> JTextField3111111.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP16SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP16Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP16Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setPortsServer(new java.lang.Long(getTxtPortsServer().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    // ingore
                    getObject().setPortsServer(getTxtPortsServer().getLongValue());
                }
                // user code end
                ivjConnPtoP16Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP16Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP16SetTarget:  (Object.portsServer <--> JTextField3111111.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP16SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP16Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP16Aligning = true;
                if ((getObject() != null)) {
                    getTxtPortsServer().setText(String.valueOf(getObject().getPortsServer()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP16Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP16Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP17SetSource:  (Object.repeatable <--> JCheckBox1.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP17SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP17Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP17Aligning = true;
                if ((getObject() != null)) {
                    getObject().setRepeatable(new java.lang.Boolean(getChxRepeatable().isSelected()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP17Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP17Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP17SetTarget:  (Object.repeatable <--> JCheckBox1.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP17SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP17Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP17Aligning = true;
                if ((getObject() != null)) {
                    getChxRepeatable().setSelected((getObject().getRepeatable()).booleanValue());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP17Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP17Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP18SetTarget:  (Object.mark <--> PnlStatusBar.mark)
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
     * connPtoP19SetSource:  (Object.catalogue <--> JComboBox1.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP19SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP19Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP19Aligning = true;
                if ((getObject() != null)) {
                    getObject().setCatalogue((org.tcotool.model.Catalogue) getCbxCatalogue().getSelectedItem());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP19Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP19Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP19SetTarget:  (Object.catalogue <--> JComboBox1.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP19SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP19Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP19Aligning = true;
                if ((getObject() != null)) {
                    getCbxCatalogue().setSelectedItem(getObject().getCatalogue());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP19Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP19Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP1SetSource:  (object.documentation <--> SimpleEditorPanel1.text)
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
     * connPtoP1SetTarget:  (object.documentation <--> SimpleEditorPanel1.text)
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
     * connPtoP20SetSource:  (Object.expendable <--> ChxExpendable.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP20SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP20Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP20Aligning = true;
                if ((getObject() != null)) {
                    getObject().setExpendable(new java.lang.Boolean(getChxExpendable().isSelected()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP20Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP20Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP20SetTarget:  (Object.expendable <--> ChxExpendable.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP20SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP20Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP20Aligning = true;
                if ((getObject() != null)) {
                    getChxExpendable().setSelected((getObject().getExpendable()).booleanValue());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP20Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP20Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP2SetSource:  (object.name <--> JTextField1.text)
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
                    getObject().setName(getTxtName().getText());
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
     * connPtoP2SetTarget:  (object.name <--> JTextField1.text)
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
                    getTxtName().setText(getObject().getName());
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
     * connPtoP3SetSource:  (Object.amount <--> TxtCost.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP3SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP3Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP3Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setAmount(new java.lang.Double(getTxtCost().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setAmount(getTxtCost().getDoubleValue()); // or getIntegerValue() or get DoubleValue()
                }
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
     * connPtoP3SetTarget:  (Object.amount <--> TxtCost.text)
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
                    getTxtCost().setText(String.valueOf(getObject().getAmount()));
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
     * connPtoP4SetSource:  (Object.multitude <--> TxtMultitude.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP4SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP4Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP4Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setMultitude(new java.lang.Double(getTxtMultitude().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    // ingore
                    getObject().setMultitude(getTxtMultitude().getDoubleValue());
                }
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
     * connPtoP4SetTarget:  (Object.multitude <--> TxtMultitude.text)
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
                    getTxtMultitude().setText(String.valueOf(getObject().getMultitude()));
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
     * connPtoP5SetSource:  (Object.estimated <--> ChxEstimated.selected)
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
                    getObject().setEstimated(new java.lang.Boolean(getChxEstimated().isSelected()));
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
     * connPtoP5SetTarget:  (Object.estimated <--> ChxEstimated.selected)
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
                    getChxEstimated().setSelected((getObject().getEstimated()).booleanValue());
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
     * connPtoP6SetSource:  (ConsistencyController.isSaveable <--> PnlStandardToolbar.tbbSaveEnabled)
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
     * connPtoP6SetTarget:  (ConsistencyController.isSaveable <--> PnlStandardToolbar.tbbSaveEnabled)
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
     * connPtoP7SetSource:  (Object.depreciationDuration <--> TxtDepreciation.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP7SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP7Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP7Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setDepreciationDuration(new java.lang.Long(getTxtDepreciation().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setDepreciationDuration(getTxtDepreciation().getLongValue()); // or getIntegerValue() or get DoubleValue()
                }
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
     * connPtoP7SetTarget:  (Object.depreciationDuration <--> TxtDepreciation.text)
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
                    getTxtDepreciation().setText(String.valueOf(getObject().getDepreciationDuration()));
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
     * connPtoP8SetSource:  (Object.usageDuration <--> TxtUsage.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP8SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP8Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP8Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setUsageDuration(new java.lang.Long(getTxtUsage().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setUsageDuration(getTxtUsage().getLongValue()); // or getIntegerValue() or get DoubleValue()
                }
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
     * connPtoP8SetTarget:  (Object.usageDuration <--> TxtUsage.text)
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
                    getTxtUsage().setText(String.valueOf(getObject().getUsageDuration()));
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
     * connPtoP9SetSource:  (Object.baseDate <--> TxtDate.date)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP9SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP9Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP9Aligning = true;
                if ((getObject() != null)) {
                    getObject().setBaseOffset(new java.lang.Long(getTxtBaseOffset().getText()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP9Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP9Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP9SetTarget:  (Object.baseDate <--> TxtDate.date)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP9SetTarget() {
        /* Set the target from the source */
        try {
            if (ivjConnPtoP9Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP9Aligning = true;
                if ((getObject() != null)) {
                    getTxtBaseOffset().setText(String.valueOf(getObject().getBaseOffset()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP9Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP9Aligning = false;
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
     * Return the JComboBox1 property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCatalogue() {
        if (ivjCbxCatalogue == null) {
            try {
                ivjCbxCatalogue = new javax.swing.JComboBox();
                ivjCbxCatalogue.setName("CbxCatalogue");
                ivjCbxCatalogue.setBounds(178, 124, 260, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCatalogue;
    }

    /**
     * Return the CbxCostType property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCostType() {
        if (ivjCbxCostType == null) {
            try {
                ivjCbxCostType = new javax.swing.JComboBox();
                ivjCbxCostType.setName("CbxCostType");
                ivjCbxCostType.setBounds(178, 16, 260, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCostType;
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
                ivjCbxCurrency.setBounds(375, 155, 61, 23);
                ivjCbxCurrency.setEnabled(true);
                // user code begin {1}
                ivjCbxCurrency.setBounds(375, 155, 70, 23);
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
     * Return the CbxCurrencyTotal property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyTotal() {
        if (ivjCbxCurrencyTotal == null) {
            try {
                ivjCbxCurrencyTotal = new javax.swing.JComboBox();
                ivjCbxCurrencyTotal.setName("CbxCurrencyTotal");
                ivjCbxCurrencyTotal.setBounds(375, 183, 61, 23);
                ivjCbxCurrencyTotal.setEnabled(false);
                // user code begin {1}
                ivjCbxCurrencyTotal.setBounds(375, 183, 70, 23);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyTotal;
    }

    /**
     * Return the ChxEstimated property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxEstimated() {
        if (ivjChxEstimated == null) {
            try {
                ivjChxEstimated = new javax.swing.JCheckBox();
                ivjChxEstimated.setName("ChxEstimated");
                ivjChxEstimated.setText("Geschaetzt (!)");
                ivjChxEstimated.setBounds(460, 154, 182, 22);
                ivjChxEstimated.setEnabled(true);
                // user code begin {1}
                ivjChxEstimated.setText(getResourceString("ChxEstimated_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxEstimated;
    }

    /**
     * Return the ChxEstimated1 property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxExpendable() {
        if (ivjChxExpendable == null) {
            try {
                ivjChxExpendable = new javax.swing.JCheckBox();
                ivjChxExpendable.setName("ChxExpendable");
                ivjChxExpendable.setText("Verbrauchsmaterial");
                ivjChxExpendable.setBounds(460, 127, 182, 22);
                ivjChxExpendable.setEnabled(true);
                // user code begin {1}
                ivjChxExpendable.setText(ResourceManager.getResource(CatalogueDetailView.class, "ChxExpendable_text"));
                ivjChxExpendable.setToolTipText(ResourceManager.getResource(CatalogueDetailView.class, "ChxExpendable_toolTipText"));
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
     * Return the JCheckBox1 property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxRepeatable() {
        if (ivjChxRepeatable == null) {
            try {
                ivjChxRepeatable = new javax.swing.JCheckBox();
                ivjChxRepeatable.setName("ChxRepeatable");
                ivjChxRepeatable.setToolTipText("Kosten wiederholen sich nach Ablauf der Nutzungs- bzw. Abschreibungsdauer");
                ivjChxRepeatable.setText("Wiederkehrend");
                ivjChxRepeatable.setBounds(460, 16, 165, 22);
                // user code begin {1}
                ivjChxRepeatable.setToolTipText(getResourceString("LblRepeatable_toolTipText"));
                ivjChxRepeatable.setText(getResourceString("LblRepeatable_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxRepeatable;
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
                getJPanel1().add(getTxtName(), getTxtName().getName());
                getJPanel1().add(getJTabbedPane1(), getJTabbedPane1().getName());
                getJPanel1().add(getLblMultitude(), getLblMultitude().getName());
                getJPanel1().add(getLblBaseOffset(), getLblBaseOffset().getName());
                getJPanel1().add(getTxtMultitude(), getTxtMultitude().getName());
                getJPanel1().add(getTxtBaseOffset(), getTxtBaseOffset().getName());
                getJPanel1().add(getLblMonthBaseOffset(), getLblMonthBaseOffset().getName());
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
                ivjJTabbedPane1.setBounds(9, 92, 656, 248);
                ivjJTabbedPane1.insertTab("Detail", null, getPnlDetail(), null, 0);
                ivjJTabbedPane1.insertTab("Technische Angaben", null, getPnlFactTechInfo(), null, 1);
                ivjJTabbedPane1.insertTab("Notiz", null, getPnlNote(), null, 2);
                // user code begin {1}
                ivjJTabbedPane1.setBounds(9, 92, 656, 260);
                ivjJTabbedPane1.setTitleAt(0, ResourceManager.getResource(ServiceDetailView.class, "PnlDetail_text"));
                ivjJTabbedPane1.setTitleAt(1, getResourceString("PnlTechInfo_text"));
                ivjJTabbedPane1.setTitleAt(2, ResourceManager.getResource(ServiceDetailView.class, "PnlNote_text"));
                //TODO		ivjJTabbedPane1.setTitleAt(3, getResourceString("PnlRiskHW_text"));
                //			ivjJTabbedPane1.setTitleAt(4, getResourceString("PnlRiskSW_text"));
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
     * Return the JTextField31111 property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getJTextField31111() {
        if (ivjJTextField31111 == null) {
            try {
                ivjJTextField31111 = new ch.softenvironment.view.swingext.NumberTextField();
                ivjJTextField31111.setName("JTextField31111");
                ivjJTextField31111.setBounds(145, 48, 52, 20);
                ivjJTextField31111.setEditable(false);
                ivjJTextField31111.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTextField31111;
    }

    /**
     * Return the LblAmount property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblAmount() {
        if (ivjLblAmount == null) {
            try {
                ivjLblAmount = new javax.swing.JLabel();
                ivjLblAmount.setName("LblAmount");
                ivjLblAmount.setText("Kosten:");
                ivjLblAmount.setBounds(15, 159, 154, 14);
                // user code begin {1}
                ivjLblAmount.setText(ch.softenvironment.client.ResourceManager.getResourceAsLabel(ServiceDetailView.class, "PnlCost_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblAmount;
    }

    /**
     * Return the JLabel18 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblBaseOffset() {
        if (ivjLblBaseOffset == null) {
            try {
                ivjLblBaseOffset = new javax.swing.JLabel();
                ivjLblBaseOffset.setName("LblBaseOffset");
                ivjLblBaseOffset.setText("Bezugsdatum:");
                ivjLblBaseOffset.setBounds(17, 63, 163, 14);
                // user code begin {1}
                ivjLblBaseOffset.setText(ResourceManager.getResource(FactCostDetailView.class, "LblBaseOffset_text"));
                ivjLblBaseOffset.setToolTipText(ResourceManager.getResource(FactCostDetailView.class, "LblBaseOffset_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblBaseOffset;
    }

    /**
     * Return the LblCatalogue property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCatalogue() {
        if (ivjLblCatalogue == null) {
            try {
                ivjLblCatalogue = new javax.swing.JLabel();
                ivjLblCatalogue.setName("LblCatalogue");
                ivjLblCatalogue.setText("Katalog-Teil:");
                ivjLblCatalogue.setBounds(15, 126, 154, 14);
                // user code begin {1}
                ivjLblCatalogue.setText(ModelUtility.getTypeString(Catalogue.class) + ":" /*ResourceManager.getResource(CatalogueDetailView.class, "FrmWindow_text", true)*/);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCatalogue;
    }

    /**
     * Return the LblCostType property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCause() {
        if (ivjLblCause == null) {
            try {
                ivjLblCause = new javax.swing.JLabel();
                ivjLblCause.setName("LblCause");
                ivjLblCause.setText("Kostenart:");
                ivjLblCause.setBounds(15, 21, 154, 14);
                // user code begin {1}
                ivjLblCause.setText(ModelUtility.getTypeString(CostCause.class) + ":" /*ch.softenvironment.client.ResourceManager.getResource(CostDriverDetailView.class, "TbcCosttype_text", true)*/);
                ivjLblCause.setToolTipText(ch.softenvironment.client.ResourceManager.getResource(CostDriverDetailView.class, "TbcCosttype_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCause;
    }

    /**
     * Return the JLabel312 property value.
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
                ivjLblDepreciationDuration.setBounds(15, 80, 154, 14);
                // user code begin {1}
                ivjLblDepreciationDuration.setToolTipText(getResourceString("LblDepreciationDuration_toolTipText"));
                ivjLblDepreciationDuration.setText(getResourceString("LblDepreciationDuration_text"));
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
     * Return the LblMonthBaseOffset property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblMonthBaseOffset() {
        if (ivjLblMonthBaseOffset == null) {
            try {
                ivjLblMonthBaseOffset = new javax.swing.JLabel();
                ivjLblMonthBaseOffset.setName("LblMonthBaseOffset");
                ivjLblMonthBaseOffset.setText("Monate");
                ivjLblMonthBaseOffset.setBounds(266, 64, 73, 14);
                // user code begin {1}
                ivjLblMonthBaseOffset.setText(getResourceString("LblMonths_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblMonthBaseOffset;
    }

    /**
     * Return the JLabel413 property value.
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
                ivjLblMonthDepreciation.setBounds(245, 80, 73, 14);
                // user code begin {1}
                ivjLblMonthDepreciation.setText(getResourceString("LblMonths_text"));
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
     * Return the JLabel41 property value.
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
                ivjLblMonthTco.setBounds(246, 60, 73, 14);
                // user code begin {1}
                ivjLblMonthTco.setText(getResourceString("LblMonths_text"));
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
     * Return the LblMultitude property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblMultitude() {
        if (ivjLblMultitude == null) {
            try {
                ivjLblMultitude = new javax.swing.JLabel();
                ivjLblMultitude.setName("LblMultitude");
                ivjLblMultitude.setText("Menge:");
                ivjLblMultitude.setBounds(17, 41, 163, 14);
                // user code begin {1}
                ivjLblMultitude.setText(ResourceManager.getResource(ServiceDetailView.class, "LblMultitude_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblMultitude;
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
                ivjLblName.setBounds(17, 16, 163, 14);
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
     * Return the JLabel81111 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPortsIsl() {
        if (ivjLblPortsIsl == null) {
            try {
                ivjLblPortsIsl = new javax.swing.JLabel();
                ivjLblPortsIsl.setName("LblPortsIsl");
                ivjLblPortsIsl.setToolTipText("");
                ivjLblPortsIsl.setText("Fuer ISL:");
                ivjLblPortsIsl.setBounds(289, 21, 118, 14);
                // user code begin {1}
                ivjLblPortsIsl.setText(getResourceString("LblPortsIsl_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPortsIsl;
    }

    /**
     * Return the JLabel811111 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPortsServer() {
        if (ivjLblPortsServer == null) {
            try {
                ivjLblPortsServer = new javax.swing.JLabel();
                ivjLblPortsServer.setName("LblPortsServer");
                ivjLblPortsServer.setToolTipText("");
                ivjLblPortsServer.setText("Fuer Server/Reserve:");
                ivjLblPortsServer.setBounds(289, 53, 132, 14);
                // user code begin {1}
                ivjLblPortsServer.setText(getResourceString("LblPortsServer_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPortsServer;
    }

    /**
     * Return the JLabel8111 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPortsTotal() {
        if (ivjLblPortsTotal == null) {
            try {
                ivjLblPortsTotal = new javax.swing.JLabel();
                ivjLblPortsTotal.setName("LblPortsTotal");
                ivjLblPortsTotal.setToolTipText("");
                ivjLblPortsTotal.setText("Ports Total:");
                ivjLblPortsTotal.setBounds(9, 53, 118, 14);
                // user code begin {1}
                ivjLblPortsTotal.setText(getResourceString("LblPortsTotal_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPortsTotal;
    }

    /**
     * Return the JLabel81112 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPortsUsable() {
        if (ivjLblPortsUsable == null) {
            try {
                ivjLblPortsUsable = new javax.swing.JLabel();
                ivjLblPortsUsable.setName("LblPortsUsable");
                ivjLblPortsUsable.setToolTipText("");
                ivjLblPortsUsable.setText("Ports Nutzbar:");
                ivjLblPortsUsable.setBounds(9, 21, 118, 14);
                // user code begin {1}
                ivjLblPortsUsable.setText(getResourceString("LblPortsUsable_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPortsUsable;
    }

    /**
     * Return the LblSerialNumber property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSerialNumber() {
        if (ivjLblSerialNumber == null) {
            try {
                ivjLblSerialNumber = new javax.swing.JLabel();
                ivjLblSerialNumber.setName("LblSerialNumber");
                ivjLblSerialNumber.setText("Serien-Nummer (Geraet):");
                ivjLblSerialNumber.setBounds(5, 16, 143, 14);
                // user code begin {1}
                ivjLblSerialNumber.setText(getResourceString("LblSerialNumber_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSerialNumber;
    }

    /**
     * Return the LblTotal property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotal() {
        if (ivjLblTotal == null) {
            try {
                ivjLblTotal = new javax.swing.JLabel();
                ivjLblTotal.setName("LblTotal");
                ivjLblTotal.setToolTipText("Kosten * Menge");
                ivjLblTotal.setText("Total:");
                ivjLblTotal.setBounds(15, 188, 154, 14);
                ivjLblTotal.setForeground(java.awt.Color.red);
                // user code begin {1}
                ivjLblTotal.setToolTipText(getResourceString("LblTotal_toolTipText"));
                ivjLblTotal.setText(ch.softenvironment.client.ResourceManager.getResourceAsLabel(ServiceDetailView.class, "TbcTotal_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTotal;
    }

    /**
     * Return the JLabel31 property value.
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
                ivjLblUsageDuration.setBounds(15, 60, 154, 14);
                // user code begin {1}
                ivjLblUsageDuration.setToolTipText(getResourceString("LblUsageDuration_toolTipText"));
                ivjLblUsageDuration.setText(getResourceString("LblUsageDuration_text"));
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
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.FactCost getObject() {
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
                getPnlDetail().add(getLblAmount(), getLblAmount().getName());
                getPnlDetail().add(getTxtCost(), getTxtCost().getName());
                getPnlDetail().add(getCbxCurrency(), getCbxCurrency().getName());
                getPnlDetail().add(getChxEstimated(), getChxEstimated().getName());
                getPnlDetail().add(getLblUsageDuration(), getLblUsageDuration().getName());
                getPnlDetail().add(getTxtUsage(), getTxtUsage().getName());
                getPnlDetail().add(getLblMonthTco(), getLblMonthTco().getName());
                getPnlDetail().add(getLblDepreciationDuration(), getLblDepreciationDuration().getName());
                getPnlDetail().add(getTxtDepreciation(), getTxtDepreciation().getName());
                getPnlDetail().add(getLblMonthDepreciation(), getLblMonthDepreciation().getName());
                getPnlDetail().add(getLblCause(), getLblCause().getName());
                getPnlDetail().add(getCbxCostType(), getCbxCostType().getName());
                getPnlDetail().add(getLblTotal(), getLblTotal().getName());
                getPnlDetail().add(getTxtCostTotal(), getTxtCostTotal().getName());
                getPnlDetail().add(getCbxCurrencyTotal(), getCbxCurrencyTotal().getName());
                getPnlDetail().add(getChxRepeatable(), getChxRepeatable().getName());
                getPnlDetail().add(getLblCatalogue(), getLblCatalogue().getName());
                getPnlDetail().add(getCbxCatalogue(), getCbxCatalogue().getName());
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
     * Return the PnlFactStorage property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlFactTechInfo() {
        if (ivjPnlFactTechInfo == null) {
            try {
                ivjPnlFactTechInfo = new javax.swing.JPanel();
                ivjPnlFactTechInfo.setName("PnlFactTechInfo");
                ivjPnlFactTechInfo.setLayout(null);
                getPnlFactTechInfo().add(getLblSerialNumber(), getLblSerialNumber().getName());
                getPnlFactTechInfo().add(getTxtName1(), getTxtName1().getName());
                getPnlFactTechInfo().add(getPnlStorage(), getPnlStorage().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlFactTechInfo;
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
     * Return the PnlStorage property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlStorage() {
        if (ivjPnlStorage == null) {
            try {
                ivjPnlStorage = new javax.swing.JPanel();
                ivjPnlStorage.setName("PnlStorage");
                ivjPnlStorage.setLayout(null);
                ivjPnlStorage.setBounds(9, 61, 626, 83);
                getPnlStorage().add(getLblPortsTotal(), getLblPortsTotal().getName());
                getPnlStorage().add(getJTextField31111(), getJTextField31111().getName());
                getPnlStorage().add(getLblPortsUsable(), getLblPortsUsable().getName());
                getPnlStorage().add(getTxtPortsUseable(), getTxtPortsUseable().getName());
                getPnlStorage().add(getLblPortsIsl(), getLblPortsIsl().getName());
                getPnlStorage().add(getTxtPortsISL(), getTxtPortsISL().getName());
                getPnlStorage().add(getLblPortsServer(), getLblPortsServer().getName());
                getPnlStorage().add(getTxtPortsServer(), getTxtPortsServer().getName());
                // user code begin {1}
                getPnlStorage().setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlStorage_text")),
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlStorage;
    }

    /**
     * Return the TxtBaseOffset property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtBaseOffset() {
        if (ivjTxtBaseOffset == null) {
            try {
                ivjTxtBaseOffset = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtBaseOffset.setName("TxtBaseOffset");
                ivjTxtBaseOffset.setBounds(188, 60, 73, 20);
                ivjTxtBaseOffset.setEditable(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtBaseOffset;
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
                ivjTxtCost.setBounds(178, 157, 194, 20);
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
     * Return the TxtCostTotal property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtCostTotal() {
        if (ivjTxtCostTotal == null) {
            try {
                ivjTxtCostTotal = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtCostTotal.setName("TxtCostTotal");
                ivjTxtCostTotal.setBounds(178, 185, 194, 20);
                ivjTxtCostTotal.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtCostTotal;
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
                ivjTxtDepreciation.setBounds(178, 76, 63, 20);
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
     * Return the TxtMultitude property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtMultitude() {
        if (ivjTxtMultitude == null) {
            try {
                ivjTxtMultitude = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtMultitude.setName("TxtMultitude");
                ivjTxtMultitude.setBounds(188, 36, 73, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtMultitude;
    }

    /**
     * Return the JTextField1 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtName() {
        if (ivjTxtName == null) {
            try {
                ivjTxtName = new javax.swing.JTextField();
                ivjTxtName.setName("TxtName");
                ivjTxtName.setBounds(188, 13, 480, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtName;
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
                ivjTxtName1.setBounds(155, 13, 198, 20);
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
     * Return the TxtPortsISL property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtPortsISL() {
        if (ivjTxtPortsISL == null) {
            try {
                ivjTxtPortsISL = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtPortsISL.setName("TxtPortsISL");
                ivjTxtPortsISL.setBounds(434, 20, 52, 20);
                ivjTxtPortsISL.setEditable(true);
                ivjTxtPortsISL.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtPortsISL;
    }

    /**
     * Return the TxtPortsServer property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtPortsServer() {
        if (ivjTxtPortsServer == null) {
            try {
                ivjTxtPortsServer = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtPortsServer.setName("TxtPortsServer");
                ivjTxtPortsServer.setBounds(434, 46, 52, 20);
                ivjTxtPortsServer.setEditable(true);
                ivjTxtPortsServer.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtPortsServer;
    }

    /**
     * Return the TxtPortsUseable property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtPortsUseable() {
        if (ivjTxtPortsUseable == null) {
            try {
                ivjTxtPortsUseable = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtPortsUseable.setName("TxtPortsUseable");
                ivjTxtPortsUseable.setBounds(145, 18, 52, 20);
                ivjTxtPortsUseable.setEditable(true);
                ivjTxtPortsUseable.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtPortsUseable;
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
                ivjTxtUsage.setBounds(178, 54, 63, 20);
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
    private void initConnections() {
        // user code begin {1}
        // user code end
        getPnlStandardToolbar().addPropertyChangeListener(ivjEventHandler);
        getPnlNote().addSimpleEditorPanelListener(ivjEventHandler);
        getTxtName().addKeyListener(ivjEventHandler);
        getPnlStandardToolbar().addToolBarListener(ivjEventHandler);
        getChxEstimated().addItemListener(ivjEventHandler);
        getCbxCostType().addItemListener(ivjEventHandler);
        getCbxCurrency().addItemListener(ivjEventHandler);
        getTxtName1().addKeyListener(ivjEventHandler);
        getChxRepeatable().addItemListener(ivjEventHandler);
        getTxtCost().addKeyListener(ivjEventHandler);
        getTxtUsage().addKeyListener(ivjEventHandler);
        getTxtDepreciation().addKeyListener(ivjEventHandler);
        getTxtPortsUseable().addKeyListener(ivjEventHandler);
        getTxtPortsISL().addKeyListener(ivjEventHandler);
        getTxtPortsServer().addKeyListener(ivjEventHandler);
        getTxtMultitude().addKeyListener(ivjEventHandler);
        getCbxCatalogue().addItemListener(ivjEventHandler);
        getTxtBaseOffset().addKeyListener(ivjEventHandler);
        getChxExpendable().addItemListener(ivjEventHandler);
        connPtoP6SetTarget();
        connPtoP1SetTarget();
        connPtoP2SetTarget();
        connPtoP5SetTarget();
        connPtoP10SetTarget();
        connPtoP11SetTarget();
        connPtoP13SetTarget();
        connPtoP17SetTarget();
        connPtoP18SetTarget();
        connPtoP3SetTarget();
        connPtoP8SetTarget();
        connPtoP7SetTarget();
        connPtoP14SetTarget();
        connPtoP15SetTarget();
        connPtoP16SetTarget();
        connPtoP4SetTarget();
        connPtoP19SetTarget();
        connPtoP12SetTarget();
        connPtoP9SetTarget();
        connPtoP20SetTarget();
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
            setName("FactCostDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(679, 436);
            setTitle("Sachkosten");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setIconImage((new javax.swing.ImageIcon(LauncherView.getInstance().getUtility().getImageURL(FactCost.class)))
            .getImage()); //setIconImage(ResourceBundle.getImageIcon(ModelUtility.class, "FactCost.png").getImage());
        setTitle(getResourceString("FrmWindow_text"));
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(FactCost.class));
        getPnlStandardToolbar().setObjects(getObjects());
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        try {
            if (event.getPropertyName().equals("amount") || event.getPropertyName().equals("multitude") ||
                event.getPropertyName().equals("catalogue")) {
                if (event.getPropertyName().equals("catalogue") && (getCbxCatalogue().getSelectedItem() != null)) {
                    // set defaults according to Catalogue
                    Catalogue catalogue = (Catalogue) getCbxCatalogue().getSelectedItem();
                    // other catalog assigned -> reuse name initially
                    getObject().setName(catalogue.getNameString());
                }
                refreshTotal(); // will also set catalogue changes
                if (caller != null) {
                    ((CostDriverDetailView) caller).refreshCost();
                }
            }
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
     * Redo the last undoing changes of an Object represented by this GUI.
     */
    @Override
    public void redoObject() {
    }

    /**
     * Refresh Costs.
     */
    private void refreshTotal() {
        try {
            boolean catalogMissing = (getCbxCatalogue().getSelectedItem() == null);
            getTxtCost().setEditable(catalogMissing);
            getTxtCost().setEnabled(catalogMissing);
            getCbxCurrency().setEnabled(catalogMissing);
            getTxtUsage().setEditable(catalogMissing);
            getTxtUsage().setEnabled(catalogMissing);
            getTxtDepreciation().setEditable(catalogMissing);
            getTxtDepreciation().setEnabled(catalogMissing);
            if (!catalogMissing) {
                ModelUtility.updateCatalogue(getObject());
            }
            java.text.NumberFormat af = AmountFormat.getAmountInstance(LauncherView.getInstance().getSettings().getPlattformLocale());
            getTxtCostTotal().setText(af.format(org.tcotool.tools.Calculator.calculate(getObject())));
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
     * Save an Object represented by DetailView.
     */
    @Override
    public void saveObject() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getObject().getPersistencyState().isNew()) {
                        getObject().save();
                    }

                    LauncherView.getInstance().mniSaveFile();
                    closeOnSave();
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
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
     *     getObject().removeChangeListener(getConsistencyController()); } ((DbObject)object).refresh(true); setObject(object); object.addChangeListener(getconsistencyController()); } catch(Throwable
     *     e) { handleException(e); }
     */
    @Override
    public void setCurrentObject(java.lang.Object object) {
        try {
            if (getObject() != null) {
                getObject().removePropertyChangeListener(getConsistencyController());
                getObject().removePropertyChangeListener(this);
                setObject(null);
            }

            DbObjectServer server = ((DbObject) object).getObjectServer();
            JComboBoxUtility.initComboBox(getCbxCostType(), server.retrieveCodes(CostCause.class), "nameString", false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrency(), server.retrieveCodes(Currency.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyTotal(), server.retrieveCodes(Currency.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCatalogue(), server.retrieveCodes(Catalogue.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));

            setObject((org.tcotool.model.FactCost) object);
            //TODO @deprecated properties => remove Tabbed Pane definitively
            if ((getObject().getPortsISL() == null) &&
                (getObject().getPortsServer() == null) &&
                (getObject().getPortsUseable() == null) &&
                (getObject().getSerialNumber() == null)) {
                // only show if data available from older configurations
                ivjJTabbedPane1.remove(1);
            }

            refreshTotal();

            getObject().addPropertyChangeListener(getConsistencyController());
            // to be informed to recalc sums
            getObject().addPropertyChangeListener(this);
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
     * Set the Object to a new value.
     *
     * @param newValue org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void setObject(org.tcotool.model.FactCost newValue) {
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
                connPtoP1SetTarget();
                connPtoP2SetTarget();
                connPtoP5SetTarget();
                connPtoP10SetTarget();
                connPtoP11SetTarget();
                connPtoP13SetTarget();
                connPtoP17SetTarget();
                connPtoP18SetTarget();
                connPtoP3SetTarget();
                connPtoP8SetTarget();
                connPtoP7SetTarget();
                connPtoP14SetTarget();
                connPtoP15SetTarget();
                connPtoP16SetTarget();
                connPtoP4SetTarget();
                connPtoP19SetTarget();
                connPtoP9SetTarget();
                connPtoP20SetTarget();
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
}
