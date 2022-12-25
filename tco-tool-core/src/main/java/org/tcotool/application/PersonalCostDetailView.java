package org.tcotool.application;

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.controller.ConsistencyController;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import org.tcotool.model.*;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.ModelUtility;

import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * DetailView of PersonalCost's.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class PersonalCostDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView,
        java.beans.PropertyChangeListener {

    private ch.softenvironment.view.DetailView caller = null;
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjPnlDetail = null;
    private boolean ivjConnPtoP1Aligning = false;
    private boolean ivjConnPtoP2Aligning = false;
    private javax.swing.JPanel ivjJPanel1 = null;
    private PersonalCost ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JLabel ivjLblMultitude = null;
    private ch.softenvironment.view.SimpleEditorPanel ivjPnlNote = null;
    private javax.swing.JLabel ivjLblName = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtMultitude = null;
    private javax.swing.JTextField ivjTxtName = null;
    private javax.swing.JComboBox ivjCbxActivity = null;
    private javax.swing.JComboBox ivjCbxRole = null;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP5Aligning = false;
    private boolean ivjConnPtoP7Aligning = false;
    private boolean ivjConnPtoP8Aligning = false;
    private javax.swing.JComboBox ivjCbxCurrency = null;
    private javax.swing.JCheckBox ivjChxEstimated = null;
    private boolean ivjConnPtoP10Aligning = false;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtCost = null;
    private boolean ivjConnPtoP11Aligning = false;
    private javax.swing.JComboBox ivjCbxCurrencyRole = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtHours = null;
    private boolean ivjConnPtoP12Aligning = false;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtHourlyRate = null;
    private javax.swing.JComboBox ivjCbxCurrencyTotal = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtCostTotal = null;
    private javax.swing.JLabel ivjLblTotal = null;
    private javax.swing.JComboBox ivjCbxCostType = null;
    private boolean ivjConnPtoP14Aligning = false;
    private javax.swing.JLabel ivjLblAmount = null;
    private boolean ivjConnPtoP15Aligning = false;
    private javax.swing.JCheckBox ivjChxRepeatable = null;
    private boolean ivjConnPtoP16Aligning = false;
    private javax.swing.JPanel ivjPnlRole = null;
    private javax.swing.JLabel ivjLblRole = null;
    private javax.swing.JCheckBox ivjChxInternal = null;
    private javax.swing.JLabel ivjLblActivity = null;
    private javax.swing.JLabel ivjLblHourlyRate = null;
    private javax.swing.JLabel ivjLblHours = null;
    private javax.swing.JLabel ivjLblAvailableHours = null;
    private javax.swing.JLabel ivjLblAvailableHoursUnit = null;
    private javax.swing.JLabel ivjLblHoursUnit = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtAvailableHours = null;
    private javax.swing.JLabel ivjLblCause = null;
    private javax.swing.JLabel ivjLblBaseOffset = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtBaseOffset = null;
    private javax.swing.JLabel ivjLblMonthBaseOffset = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ItemListener,
        java.awt.event.KeyListener, java.beans.PropertyChangeListener {

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == PersonalCostDetailView.this.getCbxRole()) {
                connPtoP3SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getCbxActivity()) {
                connPtoP5SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getChxEstimated()) {
                connPtoP10SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getCbxCurrency()) {
                connPtoP11SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getCbxCurrency()) {
                connPtoP13SetTarget();
            }
            if (e.getSource() == PersonalCostDetailView.this.getCbxCostType()) {
                connPtoP14SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getChxInternal()) {
                connPtoP15SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getChxRepeatable()) {
                connPtoP16SetSource();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == PersonalCostDetailView.this.getTxtName()) {
                connPtoP2SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getTxtCost()) {
                connPtoP8SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getTxtHourlyRate()) {
                connPtoP12SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getTxtHours()) {
                connPtoP7SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getTxtMultitude()) {
                connPtoP4SetSource();
            }
            if (e.getSource() == PersonalCostDetailView.this.getTxtBaseOffset()) {
                connPtoP9SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == PersonalCostDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == PersonalCostDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("name"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("role"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("activity"))) {
                connPtoP5SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("estimated"))) {
                connPtoP10SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("currency"))) {
                connPtoP11SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("cause"))) {
                connPtoP14SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("internal"))) {
                connPtoP15SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("repeatable"))) {
                connPtoP16SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("amount"))) {
                connPtoP8SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("hourlyRate"))) {
                connPtoP12SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("hours"))) {
                connPtoP7SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("multitude"))) {
                connPtoP4SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("currency"))) {
                connPtoP18SetTarget();
            }
            if (evt.getSource() == PersonalCostDetailView.this.getObject() && (evt.getPropertyName().equals("baseOffset"))) {
                connPtoP9SetTarget();
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
            if (newEvent.getSource() == PersonalCostDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == PersonalCostDetailView.this.getPnlNote()) {
                connPtoP1SetSource();
            }
        }

    }

    private boolean ivjConnPtoP9Aligning = false;

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public PersonalCostDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public PersonalCostDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects, ch.softenvironment.view.DetailView caller) {
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
     * connEtoC7: (PnlStandardToolbar.toolBar.tbbSaveAction_actionPerformed(java.util.EventObject) --> ServiceDetailView.executeSaveObject()V)
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
     * connPtoP10SetSource: (Object.costEstimated <--> ChxEstimated.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP10SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP10Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP10Aligning = true;
                if ((getObject() != null)) {
                    getObject().setEstimated(Boolean.valueOf(getChxEstimated().isSelected()));
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
     * connPtoP10SetTarget: (Object.costEstimated <--> ChxEstimated.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP10SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP10Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP10Aligning = true;
                    if ((getObject() != null)) {
                        getChxEstimated().setSelected((getObject().getEstimated()).booleanValue());
                    }
                    // user code begin {2}
                } catch (NullPointerException e) {
                    if (getObject() == null) {
                        // ignore -> too early
                    }
                }
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
     * connPtoP11SetSource: (Object.costCurrency <--> CbxCurrency1.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP11SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP11Aligning) {
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
     * connPtoP11SetTarget: (Object.costCurrency <--> CbxCurrency1.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP11SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP11Aligning) {
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
     * connPtoP12SetSource: (Object.hourlyRate <--> TxtHourlyRate.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP12SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP12Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP12Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setHourlyRate(Double.valueOf(getTxtHourlyRate().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setHourlyRate(getTxtHourlyRate().getDoubleValue()); // or getIntegerValue() or get DoubleValue()
                }
                // user code end
                ivjConnPtoP12Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP12Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP12SetTarget: (Object.hourlyRate <--> TxtHourlyRate.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP12SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP12Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP12Aligning = true;
                if ((getObject() != null)) {
                    getTxtHourlyRate().setText(String.valueOf(getObject().getHourlyRate()));
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP12Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP12Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP13SetTarget: (CbxCurrency.selectedItem <--> CbxCurrencyTotal.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP13SetTarget() {
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
     * connPtoP14SetSource: (Object.type <--> CbxCostType.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP14SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP14Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP14Aligning = true;
                if ((getObject() != null)) {
                    getObject().setCause((org.tcotool.model.CostCause) getCbxCostType().getSelectedItem());
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
     * connPtoP14SetTarget: (Object.type <--> CbxCostType.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP14SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP14Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP14Aligning = true;
                if ((getObject() != null)) {
                    getCbxCostType().setSelectedItem(getObject().getCause());
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
     * connPtoP15SetSource: (Object.internal <--> ChInternal.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP15SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP15Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP15Aligning = true;
                if ((getObject() != null)) {
                    getObject().setInternal(getChxInternal().isSelected());
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
     * connPtoP15SetTarget: (Object.internal <--> ChInternal.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP15SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP15Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP15Aligning = true;
                if ((getObject() != null)) {
                    getChxInternal().setSelected(getObject().getInternal());
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
     * connPtoP16SetSource: (Object.repeatable <--> ChxRepeatable.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP16SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP16Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP16Aligning = true;
                if ((getObject() != null)) {
                    getObject().setRepeatable(getChxRepeatable().isSelected());
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
     * connPtoP16SetTarget: (Object.repeatable <--> ChxRepeatable.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP16SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP16Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP16Aligning = true;
                if ((getObject() != null)) {
                    getChxRepeatable().setSelected(getObject().getRepeatable());
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
     * connPtoP17SetTarget: (Object.mark <--> PnlStatusBar.mark)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP17SetTarget() {
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
     * connPtoP18SetTarget: (Object.currency <--> CbxCurrencyRole.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP18SetTarget() {
        /* Set the target from the source */
        try {
            if ((getObject() != null)) {
                getCbxCurrencyRole().setSelectedItem(getObject().getCurrency());
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
     * connPtoP1SetSource: (object.documentation <--> SimpleEditorPanel1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP1SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP1Aligning) {
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
     * connPtoP1SetTarget: (object.documentation <--> SimpleEditorPanel1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP1SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP1Aligning) {
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
     * connPtoP2SetSource: (object.name <--> JTextField1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP2SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP2Aligning) {
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
     * connPtoP2SetTarget: (object.name <--> JTextField1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP2SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP2Aligning) {
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
     * connPtoP3SetSource: (Object.role <--> CbxRole.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP3SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP3Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP3Aligning = true;
                if ((getObject() != null)) {
                    getObject().setRole((org.tcotool.model.Role) getCbxRole().getSelectedItem());
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
     * connPtoP3SetTarget: (Object.role <--> CbxRole.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP3SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP3Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP3Aligning = true;
                if ((getObject() != null)) {
                    getCbxRole().setSelectedItem(getObject().getRole());
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
     * connPtoP4SetSource: (Object.multitude <--> TxtMultitude.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP4SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP4Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP4Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setMultitude(Double.valueOf(getTxtMultitude().getText()));
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
     * connPtoP4SetTarget: (Object.multitude <--> TxtMultitude.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP4SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP4Aligning) {
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
     * connPtoP5SetSource: (Object.activity <--> CbxActivity.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP5SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP5Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP5Aligning = true;
                if ((getObject() != null)) {
                    getObject().setActivity((org.tcotool.model.Activity) getCbxActivity().getSelectedItem());
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
     * connPtoP5SetTarget: (Object.activity <--> CbxActivity.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP5SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP5Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP5Aligning = true;
                if ((getObject() != null)) {
                    getCbxActivity().setSelectedItem(getObject().getActivity());
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
            if (!ivjConnPtoP6Aligning) {
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
            if (!ivjConnPtoP6Aligning) {
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
     * connPtoP7SetSource: (Object.hours <--> TxtMultitude1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP7SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP7Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP7Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setHours(Double.valueOf(getTxtHours().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setHours(getTxtHours().getDoubleValue()); // or getIntegerValue() or get DoubleValue()
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
     * connPtoP7SetTarget: (Object.hours <--> TxtMultitude1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP7SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP7Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP7Aligning = true;
                if ((getObject() != null)) {
                    getTxtHours().setText(String.valueOf(getObject().getHours()));
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
     * connPtoP8SetSource: (Object.cost <--> CostPanel11.txtCostText)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP8SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP8Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP8Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setAmount(Double.valueOf(getTxtCost().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setAmount(getTxtCost().getDoubleValue());
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
     * connPtoP8SetTarget: (Object.cost <--> CostPanel11.txtCostText)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP8SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP8Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP8Aligning = true;
                if ((getObject() != null)) {
                    getTxtCost().setText(String.valueOf(getObject().getAmount()));
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
     * connPtoP9SetSource: (Object.baseOffset <--> TxtBaseOffset.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP9SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP9Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP9Aligning = true;
                if ((getObject() != null)) {
                    getObject().setBaseOffset(Long.valueOf(getTxtBaseOffset().getText()));
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
     * connPtoP9SetTarget: (Object.baseOffset <--> TxtBaseOffset.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP9SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP9Aligning) {
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
     * Return the CbxActivity property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxActivity() {
        if (ivjCbxActivity == null) {
            try {
                ivjCbxActivity = new javax.swing.JComboBox();
                ivjCbxActivity.setName("CbxActivity");
                ivjCbxActivity.setBounds(166, 34, 398, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxActivity;
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
                ivjCbxCostType.setBounds(166, 6, 398, 23);
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
                ivjCbxCurrency.setBounds(366, 223, 61, 23);
                ivjCbxCurrency.setEnabled(true);
                // user code begin {1}
                ivjCbxCurrency.setBounds(366, 223, 70, 23);
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
     * Return the CbxCurrency property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyRole() {
        if (ivjCbxCurrencyRole == null) {
            try {
                ivjCbxCurrencyRole = new javax.swing.JComboBox();
                ivjCbxCurrencyRole.setName("CbxCurrencyRole");
                ivjCbxCurrencyRole.setBounds(244, 51, 84, 23);
                ivjCbxCurrencyRole.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyRole;
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
                ivjCbxCurrencyTotal.setBounds(366, 247, 61, 23);
                ivjCbxCurrencyTotal.setEnabled(false);
                // user code begin {1}
                ivjCbxCurrencyTotal.setBounds(366, 247, 70, 23);
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
     * Return the CbxRole property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxRole() {
        if (ivjCbxRole == null) {
            try {
                ivjCbxRole = new javax.swing.JComboBox();
                ivjCbxRole.setName("CbxRole");
                ivjCbxRole.setBounds(159, 23, 394, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxRole;
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
                ivjChxEstimated.setBounds(434, 222, 144, 22);
                ivjChxEstimated.setEnabled(true);
                // user code begin {1}
                ivjChxEstimated.setText(ResourceManager.getResource(FactCostDetailView.class, "ChxEstimated_text"));
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
     * Return the ChInternal property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxInternal() {
        if (ivjChxInternal == null) {
            try {
                ivjChxInternal = new javax.swing.JCheckBox();
                ivjChxInternal.setName("ChxInternal");
                ivjChxInternal.setToolTipText("sonst externe MitarbeiterIn");
                ivjChxInternal.setText("MitarbeiterIn intern");
                ivjChxInternal.setBounds(385, 51, 169, 22);
                // user code begin {1}
                ivjChxInternal.setToolTipText(getResourceString("ChxInternal_toolTipText"));
                ivjChxInternal.setText(getResourceString("ChxInternal_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxInternal;
    }

    /**
     * Return the ChxRepeatable property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxRepeatable() {
        if (ivjChxRepeatable == null) {
            try {
                ivjChxRepeatable = new javax.swing.JCheckBox();
                ivjChxRepeatable.setName("ChxRepeatable");
                ivjChxRepeatable.setToolTipText("Kosten wiederholen sich nach Ablauf der Nutzungsdauer");
                ivjChxRepeatable.setText("Wiederkehrend");
                ivjChxRepeatable.setBounds(434, 194, 144, 22);
                // user code begin {1}
                ivjChxRepeatable.setToolTipText(ResourceManager.getResource(FactCostDetailView.class, "LblRepeatable_toolTipText"));
                ivjChxRepeatable.setText(ResourceManager.getResource(FactCostDetailView.class, "LblRepeatable_text"));
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
                ivjJTabbedPane1.setBounds(9, 92, 586, 308);
                ivjJTabbedPane1.insertTab("Detail", null, getPnlDetail(), null, 0);
                ivjJTabbedPane1.insertTab("Notiz", null, getPnlNote(), null, 1);
                // user code begin {1}
                ivjJTabbedPane1.setBounds(9, 92, 590, 320);
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
     * Return the JLabel511 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblActivity() {
        if (ivjLblActivity == null) {
            try {
                ivjLblActivity = new javax.swing.JLabel();
                ivjLblActivity.setName("LblActivity");
                ivjLblActivity.setToolTipText("Workflow");
                ivjLblActivity.setText("Aktivitaet:");
                ivjLblActivity.setBounds(11, 39, 150, 14);
                // user code begin {1}
                ivjLblActivity.setText(ModelUtility.getTypeString(Activity.class) + ":" /* getResourceString("LblActivity_text") */);
                ivjLblActivity.setToolTipText("");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblActivity;
    }

    /**
     * Return the JLabel3111 property value.
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
                ivjLblAmount.setBounds(12, 228, 143, 14);
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
     * Return the LblAvailableHours property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblAvailableHours() {
        if (ivjLblAvailableHours == null) {
            try {
                ivjLblAvailableHours = new javax.swing.JLabel();
                ivjLblAvailableHours.setName("LblAvailableHours");
                ivjLblAvailableHours.setText("Noch verfuegbar:");
                ivjLblAvailableHours.setBounds(8, 82, 143, 14);
                // user code begin {1}
                ivjLblAvailableHours.setText(getResourceString("LblAvailableHours_text"));
                ivjLblAvailableHours.setToolTipText(getResourceString("LblAvailableHours_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblAvailableHours;
    }

    /**
     * Return the LblAvailableHoursUnit property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblAvailableHoursUnit() {
        if (ivjLblAvailableHoursUnit == null) {
            try {
                ivjLblAvailableHoursUnit = new javax.swing.JLabel();
                ivjLblAvailableHoursUnit.setName("LblAvailableHoursUnit");
                ivjLblAvailableHoursUnit.setText("h");
                ivjLblAvailableHoursUnit.setBounds(339, 82, 19, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblAvailableHoursUnit;
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
                ivjLblBaseOffset.setBounds(17, 63, 156, 14);
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
     * Return the JLabel521132 property value.
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
                ivjLblCause.setBounds(11, 11, 148, 14);
                // user code begin {1}
                ivjLblCause.setText(ModelUtility.getTypeString(CostCause.class) + ":" /*
                 * ch.softenvironment.client.ResourceManager.getResource(CostDriverDetailView
                 * .class, "TbcCosttype_text", true)
                 */);
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
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblHourlyRate() {
        if (ivjLblHourlyRate == null) {
            try {
                ivjLblHourlyRate = new javax.swing.JLabel();
                ivjLblHourlyRate.setName("LblHourlyRate");
                ivjLblHourlyRate.setText("Stundenansatz:");
                ivjLblHourlyRate.setBounds(8, 57, 143, 14);
                // user code begin {1}
                ivjLblHourlyRate.setText(ch.softenvironment.client.ResourceManager.getResource(RoleDetailView.class, "LblHourlyRate_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblHourlyRate;
    }

    /**
     * Return the JLabel311 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblHours() {
        if (ivjLblHours == null) {
            try {
                ivjLblHours = new javax.swing.JLabel();
                ivjLblHours.setName("LblHours");
                ivjLblHours.setText("Anzahl:");
                ivjLblHours.setBounds(12, 199, 143, 14);
                // user code begin {1}
                ivjLblHours.setText(getResourceString("LblHours_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblHours;
    }

    /**
     * Return the JLabel411 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblHoursUnit() {
        if (ivjLblHoursUnit == null) {
            try {
                ivjLblHoursUnit = new javax.swing.JLabel();
                ivjLblHoursUnit.setName("LblHoursUnit");
                ivjLblHoursUnit.setText("h");
                ivjLblHoursUnit.setBounds(252, 199, 19, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblHoursUnit;
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
                ivjLblMonthBaseOffset.setBounds(263, 63, 73, 14);
                // user code begin {1}
                ivjLblMonthBaseOffset.setText(ResourceManager.getResource(FactCostDetailView.class, "LblMonths_text"));
                ivjLblMonthBaseOffset.setBounds(263, 63, 73, 14);
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
                ivjLblMultitude.setBounds(17, 41, 156, 14);
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
                ivjLblName.setBounds(17, 16, 156, 14);
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
     * Return the JLabel51 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblRole() {
        if (ivjLblRole == null) {
            try {
                ivjLblRole = new javax.swing.JLabel();
                ivjLblRole.setName("LblRole");
                ivjLblRole.setText("Name:");
                ivjLblRole.setBounds(8, 27, 143, 14);
                // user code begin {1}
                ivjLblRole.setText(ResourceManager.getResource(ServiceDetailView.class, "LblName_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblRole;
    }

    /**
     * Return the JLabel31111 property value.
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
                ivjLblTotal.setBounds(12, 252, 143, 14);
                ivjLblTotal.setForeground(java.awt.Color.red);
                // user code begin {1}
                ivjLblTotal.setToolTipText(ResourceManager.getResource(FactCostDetailView.class, "LblTotal_toolTipText"));
                ivjLblTotal.setText(ResourceManager.getResourceAsLabel(ServiceDetailView.class, "TbcTotal_text"));
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
     * Return the Object property value.
     *
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.PersonalCost getObject() {
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
                getPnlDetail().add(getLblActivity(), getLblActivity().getName());
                getPnlDetail().add(getCbxActivity(), getCbxActivity().getName());
                getPnlDetail().add(getTxtCost(), getTxtCost().getName());
                getPnlDetail().add(getCbxCurrency(), getCbxCurrency().getName());
                getPnlDetail().add(getChxEstimated(), getChxEstimated().getName());
                getPnlDetail().add(getLblAmount(), getLblAmount().getName());
                getPnlDetail().add(getLblTotal(), getLblTotal().getName());
                getPnlDetail().add(getTxtCostTotal(), getTxtCostTotal().getName());
                getPnlDetail().add(getCbxCurrencyTotal(), getCbxCurrencyTotal().getName());
                getPnlDetail().add(getLblCause(), getLblCause().getName());
                getPnlDetail().add(getCbxCostType(), getCbxCostType().getName());
                getPnlDetail().add(getChxRepeatable(), getChxRepeatable().getName());
                getPnlDetail().add(getPnlRole(), getPnlRole().getName());
                getPnlDetail().add(getLblHours(), getLblHours().getName());
                getPnlDetail().add(getTxtHours(), getTxtHours().getName());
                getPnlDetail().add(getLblHoursUnit(), getLblHoursUnit().getName());
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
     * Return the PnlRole property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlRole() {
        if (ivjPnlRole == null) {
            try {
                ivjPnlRole = new javax.swing.JPanel();
                ivjPnlRole.setName("PnlRole");
                ivjPnlRole.setLayout(null);
                ivjPnlRole.setBounds(7, 70, 560, 113);
                getPnlRole().add(getLblRole(), getLblRole().getName());
                getPnlRole().add(getCbxRole(), getCbxRole().getName());
                getPnlRole().add(getChxInternal(), getChxInternal().getName());
                getPnlRole().add(getLblHourlyRate(), getLblHourlyRate().getName());
                getPnlRole().add(getTxtHourlyRate(), getTxtHourlyRate().getName());
                getPnlRole().add(getCbxCurrencyRole(), getCbxCurrencyRole().getName());
                getPnlRole().add(getLblAvailableHours(), getLblAvailableHours().getName());
                getPnlRole().add(getTxtAvailableHours(), getTxtAvailableHours().getName());
                getPnlRole().add(getLblAvailableHoursUnit(), getLblAvailableHoursUnit().getName());
                // user code begin {1}
                getPnlRole().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createTitledBorder(ModelUtility.getTypeString(Role.class) /*
                         * ch.softenvironment.client.ResourceManager.
                         * getResource(RoleDetailView.class,
                         * "FrmWindow_text")
                         */),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlRole;
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
     * Return the TxtAvailableHours property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtAvailableHours() {
        if (ivjTxtAvailableHours == null) {
            try {
                ivjTxtAvailableHours = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtAvailableHours.setName("TxtAvailableHours");
                ivjTxtAvailableHours.setBounds(159, 79, 170, 20);
                ivjTxtAvailableHours.setEditable(false);
                ivjTxtAvailableHours.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtAvailableHours;
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
                ivjTxtBaseOffset.setBounds(180, 60, 73, 20);
                ivjTxtBaseOffset.setEditable(true);
                // user code begin {1}
                ivjTxtBaseOffset.setBounds(180, 60, 73, 20);
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
                ivjTxtCost.setBounds(167, 225, 194, 20);
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
                ivjTxtCostTotal.setBounds(167, 249, 194, 20);
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
     * Return the TxtHourlyRate property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtHourlyRate() {
        if (ivjTxtHourlyRate == null) {
            try {
                ivjTxtHourlyRate = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtHourlyRate.setName("TxtHourlyRate");
                ivjTxtHourlyRate.setBounds(159, 53, 73, 20);
                ivjTxtHourlyRate.setEditable(true);
                ivjTxtHourlyRate.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtHourlyRate;
    }

    /**
     * Return the TxtHours property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtHours() {
        if (ivjTxtHours == null) {
            try {
                ivjTxtHours = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtHours.setName("TxtHours");
                ivjTxtHours.setBounds(167, 197, 73, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtHours;
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
                ivjTxtMultitude.setBounds(180, 36, 73, 20);
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
                ivjTxtName.setBounds(180, 12, 397, 20);
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
        getPnlStandardToolbar().addPropertyChangeListener(ivjEventHandler);
        getPnlNote().addSimpleEditorPanelListener(ivjEventHandler);
        getTxtName().addKeyListener(ivjEventHandler);
        getPnlStandardToolbar().addToolBarListener(ivjEventHandler);
        getCbxRole().addItemListener(ivjEventHandler);
        getCbxActivity().addItemListener(ivjEventHandler);
        getChxEstimated().addItemListener(ivjEventHandler);
        getCbxCurrency().addItemListener(ivjEventHandler);
        getCbxCostType().addItemListener(ivjEventHandler);
        getChxInternal().addItemListener(ivjEventHandler);
        getChxRepeatable().addItemListener(ivjEventHandler);
        getTxtCost().addKeyListener(ivjEventHandler);
        getTxtHourlyRate().addKeyListener(ivjEventHandler);
        getTxtHours().addKeyListener(ivjEventHandler);
        getTxtMultitude().addKeyListener(ivjEventHandler);
        getTxtBaseOffset().addKeyListener(ivjEventHandler);
        connPtoP6SetTarget();
        connPtoP1SetTarget();
        connPtoP2SetTarget();
        connPtoP3SetTarget();
        connPtoP5SetTarget();
        connPtoP10SetTarget();
        connPtoP11SetTarget();
        connPtoP13SetTarget();
        connPtoP14SetTarget();
        connPtoP15SetTarget();
        connPtoP16SetTarget();
        connPtoP17SetTarget();
        connPtoP8SetTarget();
        connPtoP12SetTarget();
        connPtoP7SetTarget();
        connPtoP4SetTarget();
        connPtoP18SetTarget();
        connPtoP9SetTarget();
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
            setName("PersonalCostDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(607, 499);
            setTitle("Personal-Aufwand");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setIconImage((new javax.swing.ImageIcon(LauncherView.getInstance().getUtility().getImageURL(PersonalCost.class))).getImage()); // setIconImage(ResourceManager.getImageIcon(ModelUtility.class,
        // "PersonalCost.png").getImage());
        setTitle(getResourceString("FrmWindow_text"));
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(PersonalCost.class));
        getPnlStandardToolbar().setObjects(getObjects());
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
    }

    /*
     * (non-Javadoc)
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        try {
            if (event.getPropertyName().equals("amount") || event.getPropertyName().equals("multitude") || event.getPropertyName().equals("role")
                || event.getPropertyName().equals("hours") || event.getPropertyName().equals("hourlyRate")) {
                if ((event.getPropertyName().equals("role")) && (getCbxRole().getSelectedItem() != null)) {
                    // set default
                    getObject().setName(((Role) getCbxRole().getSelectedItem()).getNameString());
                }
                refreshCosts();
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
     * Refresh Costs according to role.
     */
    private void refreshCosts() {
        getPnlStatusBar().setStatus(null);
        java.text.NumberFormat af = AmountFormat.getAmountInstance(LauncherView.getInstance().getSettings().getPlattformLocale());
        if (getObject().getRole() == null) {
            getTxtHourlyRate().setEditable(true);
            getTxtHourlyRate().setEnabled(true);
            // getCbxCurrencyRole().setSelectedIndex(-1);
            getCbxCurrency().setEnabled(true);
            getChxInternal().setEnabled(true);
            getTxtCost().setEditable(true);
            getTxtCost().setEnabled(true);
            getTxtAvailableHours().setText(null);
            getTxtAvailableHours().setBackground(getTxtHourlyRate().getBackground());

            if ((getObject().getHourlyRate() != null) && (getObject().getHours() != null)) {
                // calc total
                getTxtCost().setEditable(false);
                getTxtCost().setEnabled(false);
                getObject().setAmount(Double.valueOf(getObject().getHourlyRate() * getObject().getHours()));
            } else {
                // allow setting total
                getTxtCost().setEditable(true);
                getTxtCost().setEnabled(true);
            }
        } else {
            getTxtHourlyRate().setEditable(false);
            getTxtHourlyRate().setEnabled(false);
            // getCbxCurrencyRole().setEnabled(false);
            getCbxCurrency().setEnabled(false);
            getChxInternal().setEnabled(false);
            getTxtCost().setEditable(false);
            getTxtCost().setEnabled(false);
            double availableHours = Calculator.calculateFreeHours(LauncherView.getInstance().getUtility(), getObject().getRole());
            getTxtAvailableHours().setText(af.format(availableHours) + " / " + af.format(Calculator.calculateAvailableHours(getObject().getRole())));
            if (availableHours < 0.0) {
                getTxtAvailableHours().setBackground(Color.RED);
            } else {
                getTxtAvailableHours().setBackground(Color.GREEN);
            }

            if (ModelUtility.updateRole(getObject())) {
                getPnlStatusBar().setStatus(
                    "FTE-" + ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblYearlyHours_text") + "="
                        + getObject().getRole().getYearlyHours() + " [" + getLblHoursUnit().getText() + "]");
            }
        }

        getTxtCostTotal().setText(af.format(org.tcotool.tools.Calculator.calculate(getObject())));
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

                    LauncherView.getInstance().saveObject();
                    closeOnSave();
                } catch (Throwable e) {
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
     * @param object Example Code: try { if ((object != null) && object.equals(getObject())) { return; } if (getObject() != null) { getObject().removeChangeListener(getConsistencyController()); }
     *     ((DbObject)object).refresh(true); setObject(object); object.addChangeListener(getconsistencyController()); } catch(Throwable e) { handleException(e); }
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
            JComboBoxUtility.initComboBox(getCbxCostType(), server.retrieveCodes(CostCause.class), "nameString", false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxRole(), server.retrieveCodes(Role.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxActivity(), server.retrieveCodes(Activity.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(
                ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyRole(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrency(), server.retrieveCodes(Currency.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(
                ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyTotal(), server.retrieveCodes(Currency.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(
                ModelUtility.getCodeTypeLocale()));

            setObject((org.tcotool.model.PersonalCost) object);
            refreshCosts();

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
    private void setObject(org.tcotool.model.PersonalCost newValue) {
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
                connPtoP3SetTarget();
                connPtoP5SetTarget();
                connPtoP10SetTarget();
                connPtoP11SetTarget();
                connPtoP14SetTarget();
                connPtoP15SetTarget();
                connPtoP16SetTarget();
                connPtoP17SetTarget();
                connPtoP8SetTarget();
                connPtoP12SetTarget();
                connPtoP7SetTarget();
                connPtoP4SetTarget();
                connPtoP18SetTarget();
                connPtoP9SetTarget();
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
