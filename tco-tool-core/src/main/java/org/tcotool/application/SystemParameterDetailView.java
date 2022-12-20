package org.tcotool.application;


import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.controller.ConsistencyController;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import org.tcotool.model.SystemParameter;
import org.tcotool.tools.ModelUtility;

/**
 * DetailView for SystemParameters.
 *
 * @author Peter Hirzel
 */

public class SystemParameterDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView, java.beans.PropertyChangeListener {

    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjJPanel1 = null;
    private org.tcotool.model.SystemParameter ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private javax.swing.JComboBox ivjCbxCurrency = null;
    private boolean ivjConnPtoP2Aligning = false;
    private javax.swing.JPanel ivjPnlCommon = null;
    private javax.swing.JPanel ivjPnlFactCost = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtDepreciation = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtUsage = null;
    private boolean ivjConnPtoP5Aligning = false;
    private javax.swing.JPanel ivjPnlBenchmarking = null;
    private javax.swing.JPanel ivjPnlReporting = null;
    private javax.swing.JComboBox ivjCbxAmountExp = null;
    private boolean ivjConnPtoP1Aligning = false;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private boolean ivjConnPtoP7Aligning = false;
    private javax.swing.JLabel ivjLblDefaultCurrency = null;
    private javax.swing.JLabel ivjLblDefaultUsageDuration = null;
    private javax.swing.JLabel ivjLblReportCostExponent = null;
    private boolean ivjConnPtoP8Aligning = false;
    private javax.swing.JLabel ivjLblDefaultDepreciationDuration = null;
    private javax.swing.JLabel ivjLblDepreciationInterestRate = null;
    private javax.swing.JLabel ivjLblManYearHoursExternal = null;
    private javax.swing.JLabel ivjLblManYearHoursInternal = null;
    private boolean ivjConnPtoP10Aligning = false;
    private boolean ivjConnPtoP11Aligning = false;
    private javax.swing.JRadioButton ivjRbtLinear = null;
    private javax.swing.JRadioButton ivjRbtProgrssive = null;
    private javax.swing.JRadioButton ivjRbtRegressive = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtInterestRate = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtManYearExternal = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtManYearInternal = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtReportDepreciation = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtReportUsage = null;

    class IvjEventHandler implements ch.softenvironment.view.ToolBarListener, java.awt.event.ItemListener, java.awt.event.KeyListener, java.beans.PropertyChangeListener {

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == SystemParameterDetailView.this.getCbxCurrency()) {
                connPtoP3SetSource();
            }
            if (e.getSource() == SystemParameterDetailView.this.getCbxAmountExp()) {
                connPtoP7SetSource();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == SystemParameterDetailView.this.getTxtUsage()) {
                connPtoP1SetSource();
            }
            if (e.getSource() == SystemParameterDetailView.this.getTxtDepreciation()) {
                connPtoP2SetSource();
            }
            if (e.getSource() == SystemParameterDetailView.this.getTxtManYearInternal()) {
                connPtoP4SetSource();
            }
            if (e.getSource() == SystemParameterDetailView.this.getTxtManYearExternal()) {
                connPtoP5SetSource();
            }
            if (e.getSource() == SystemParameterDetailView.this.getTxtInterestRate()) {
                connPtoP8SetSource();
            }
            if (e.getSource() == SystemParameterDetailView.this.getTxtReportUsage()) {
                connPtoP11SetSource();
            }
            if (e.getSource() == SystemParameterDetailView.this.getTxtReportDepreciation()) {
                connPtoP10SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == SystemParameterDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == SystemParameterDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("defaultCurrency"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("reportCostExponent"))) {
                connPtoP7SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("defaultUsageDuration"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("defaultDepreciationDuration"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("manYearHoursInternal"))) {
                connPtoP4SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("manYearHoursExternal"))) {
                connPtoP5SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("depreciationInterestRate"))) {
                connPtoP8SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("reportUsageDuration"))) {
                connPtoP11SetTarget();
            }
            if (evt.getSource() == SystemParameterDetailView.this.getObject() && (evt.getPropertyName().equals("reportDepreciationDuration"))) {
                connPtoP10SetTarget();
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
            if (newEvent.getSource() == SystemParameterDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

    }

    private javax.swing.JLabel ivjLblCurrencyUnit = null;
    private javax.swing.JLabel ivjLblHoursExternal = null;
    private javax.swing.JLabel ivjLblHoursInternal = null;
    private javax.swing.JLabel ivjLblMonthDepreciation = null;
    private javax.swing.JLabel ivjLblMonthFinancial = null;
    private javax.swing.JLabel ivjLblMonthTco = null;
    private javax.swing.JLabel ivjLblMonthUsage = null;
    private javax.swing.JLabel ivjLblPercentage = null;
    private javax.swing.JLabel ivjLblReportDepreciationDuration = null;
    private javax.swing.JLabel ivjLblReportUsageDuration = null;

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public SystemParameterDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public SystemParameterDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects) {
        super(viewOptions, objects);
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
     * connPtoP10SetSource:  (Object.reportDepreciationDuration <--> TxtDepreciation2.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP10SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP10Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP10Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setReportDepreciationDuration(Long.valueOf(getTxtReportDepreciation().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setReportDepreciationDuration(getTxtReportDepreciation().getLongValue());
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
     * connPtoP10SetTarget:  (Object.reportDepreciationDuration <--> TxtDepreciation2.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP10SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP10Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP10Aligning = true;
                if ((getObject() != null)) {
                    getTxtReportDepreciation().setText(String.valueOf(getObject().getReportDepreciationDuration()));
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
     * connPtoP11SetSource:  (Object.reportUsageDuration <--> TxtUsage2.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP11SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP11Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP11Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setReportUsageDuration(Long.valueOf(getTxtReportUsage().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setReportUsageDuration(getTxtReportUsage().getLongValue());
                }
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
     * connPtoP11SetTarget:  (Object.reportUsageDuration <--> TxtUsage2.text)
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
                    getTxtReportUsage().setText(String.valueOf(getObject().getReportUsageDuration()));
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
     * connPtoP1SetSource:  (Object.defaultUsageDuration <--> TxtUsage.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP1SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP1Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP1Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setDefaultUsageDuration(Long.valueOf(getTxtUsage().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setDefaultUsageDuration(getTxtUsage().getLongValue());
                }
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
     * connPtoP1SetTarget:  (Object.defaultUsageDuration <--> TxtUsage.text)
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
                    getTxtUsage().setText(String.valueOf(getObject().getDefaultUsageDuration()));
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
     * connPtoP2SetSource:  (Object.defaultDepreciationDuration <--> TxtMultitude1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP2SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP2Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP2Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setDefaultDepreciationDuration(Long.valueOf(getTxtDepreciation().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setDefaultDepreciationDuration(getTxtDepreciation().getLongValue());
                }
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
     * connPtoP2SetTarget:  (Object.defaultDepreciationDuration <--> TxtMultitude1.text)
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
                    getTxtDepreciation().setText(String.valueOf(getObject().getDefaultDepreciationDuration()));
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
     * connPtoP3SetSource:  (Object.defaultCurrency <--> CbxCurrency.selectedItem)
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
                    getObject().setDefaultCurrency((org.tcotool.model.Currency) getCbxCurrency().getSelectedItem());
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
     * connPtoP3SetTarget:  (Object.defaultCurrency <--> CbxCurrency.selectedItem)
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
                    getCbxCurrency().setSelectedItem(getObject().getDefaultCurrency());
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
     * connPtoP4SetSource:  (Object.manYearHoursInternal <--> TxtUsage1.text)
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
                        getObject().setManYearHoursInternal(Long.valueOf(getTxtManYearInternal().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setManYearHoursInternal(getTxtManYearInternal().getLongValue());
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
     * connPtoP4SetTarget:  (Object.manYearHoursInternal <--> TxtUsage1.text)
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
                    getTxtManYearInternal().setText(String.valueOf(getObject().getManYearHoursInternal()));
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
     * connPtoP5SetSource:  (Object.manYearHoursExternal <--> TxtUsage11.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP5SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP5Aligning) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP5Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setManYearHoursExternal(Long.valueOf(getTxtManYearExternal().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setManYearHoursExternal(getTxtManYearExternal().getLongValue());
                }
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
     * connPtoP5SetTarget:  (Object.manYearHoursExternal <--> TxtUsage11.text)
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
                    getTxtManYearExternal().setText(String.valueOf(getObject().getManYearHoursExternal()));
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
     * connPtoP6SetTarget:  (ConsistencyController.isSaveable <--> PnlStandardToolbar.tbbSaveEnabled)
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
     * connPtoP7SetSource:  (Object.reportCostExponent <--> CbxAmountExp.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP7SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP7Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP7Aligning = true;
                if ((getObject() != null)) {
                    getObject().setReportCostExponent((org.tcotool.model.CostExponent) getCbxAmountExp().getSelectedItem());
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
     * connPtoP7SetTarget:  (Object.reportCostExponent <--> CbxAmountExp.selectedItem)
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
                    getCbxAmountExp().setSelectedItem(getObject().getReportCostExponent());
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
     * connPtoP8SetSource:  (Object.depreciationInterestRate <--> TxtDepreciation1.text)
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
                        getObject().setDepreciationInterestRate(Double.valueOf(getTxtInterestRate().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setDepreciationInterestRate(getTxtInterestRate().getDoubleValue());
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
     * connPtoP8SetTarget:  (Object.depreciationInterestRate <--> TxtDepreciation1.text)
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
                    getTxtInterestRate().setText(String.valueOf(getObject().getDepreciationInterestRate()));
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
     * connPtoP9SetTarget:  (Object.mark <--> PnlStatusBar.mark)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP9SetTarget() {
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
     * Return the CbxAmountExp property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxAmountExp() {
        if (ivjCbxAmountExp == null) {
            try {
                ivjCbxAmountExp = new javax.swing.JComboBox();
                ivjCbxAmountExp.setName("CbxAmountExp");
                ivjCbxAmountExp.setBounds(190, 15, 176, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxAmountExp;
    }

    /**
     * Return the CbxCurrency property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrency() {
        if (ivjCbxCurrency == null) {
            try {
                ivjCbxCurrency = new javax.swing.JComboBox();
                ivjCbxCurrency.setName("CbxCurrency");
                ivjCbxCurrency.setBounds(182, 22, 93, 23);
                ivjCbxCurrency.setEnabled(true);
                // user code begin {1}
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
                getJPanel1().add(getPnlFactCost(), getPnlFactCost().getName());
                getJPanel1().add(getPnlCommon(), getPnlCommon().getName());
                getJPanel1().add(getPnlBenchmarking(), getPnlBenchmarking().getName());
                getJPanel1().add(getPnlReporting(), getPnlReporting().getName());
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
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCurrencyUnit() {
        if (ivjLblCurrencyUnit == null) {
            try {
                ivjLblCurrencyUnit = new javax.swing.JLabel();
                ivjLblCurrencyUnit.setName("LblCurrencyUnit");
                ivjLblCurrencyUnit.setText("[Währung]");
                ivjLblCurrencyUnit.setBounds(377, 19, 91, 14);
                // user code begin {1}
                ivjLblCurrencyUnit.setText("[" + ch.softenvironment.client.ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblCurrency_text") + "]");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCurrencyUnit;
    }

    /**
     * Return the LblBaseDate1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDefaultCurrency() {
        if (ivjLblDefaultCurrency == null) {
            try {
                ivjLblDefaultCurrency = new javax.swing.JLabel();
                ivjLblDefaultCurrency.setName("LblDefaultCurrency");
                ivjLblDefaultCurrency.setText("Währung:");
                ivjLblDefaultCurrency.setBounds(16, 25, 131, 14);
                // user code begin {1}
                ivjLblDefaultCurrency.setText(ch.softenvironment.client.ResourceManager.getResourceAsLabel(FactCostDetailView.class, "LblCurrency_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDefaultCurrency;
    }

    /**
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDefaultDepreciationDuration() {
        if (ivjLblDefaultDepreciationDuration == null) {
            try {
                ivjLblDefaultDepreciationDuration = new javax.swing.JLabel();
                ivjLblDefaultDepreciationDuration.setName("LblDefaultDepreciationDuration");
                ivjLblDefaultDepreciationDuration.setText("Abschreibungsdauer:");
                ivjLblDefaultDepreciationDuration.setBounds(15, 54, 163, 14);
                // user code begin {1}
                ivjLblDefaultDepreciationDuration.setText(ResourceManager.getResource(FactCostDetailView.class, "LblDepreciationDuration_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDefaultDepreciationDuration;
    }

    /**
     * Return the LblName1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDefaultUsageDuration() {
        if (ivjLblDefaultUsageDuration == null) {
            try {
                ivjLblDefaultUsageDuration = new javax.swing.JLabel();
                ivjLblDefaultUsageDuration.setName("LblDefaultUsageDuration");
                ivjLblDefaultUsageDuration.setText("Nutzungsdauer:");
                ivjLblDefaultUsageDuration.setBounds(15, 26, 163, 14);
                // user code begin {1}
                ivjLblDefaultUsageDuration.setText(ResourceManager.getResource(FactCostDetailView.class, "LblUsageDuration_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDefaultUsageDuration;
    }

    /**
     * Return the LblDepreciationInterestRate property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDepreciationInterestRate() {
        if (ivjLblDepreciationInterestRate == null) {
            try {
                ivjLblDepreciationInterestRate = new javax.swing.JLabel();
                ivjLblDepreciationInterestRate.setName("LblDepreciationInterestRate");
                ivjLblDepreciationInterestRate.setText("Abschreibungs-Zinssatz:");
                ivjLblDepreciationInterestRate.setBounds(17, 100, 166, 14);
                // user code begin {1}
                ivjLblDepreciationInterestRate.setText(getResourceString("LblDepreciationInterestRate_text"));
                ivjLblDepreciationInterestRate.setToolTipText(getResourceString("LblDepreciationInterestRate_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDepreciationInterestRate;
    }

    /**
     * Return the LblName1121 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblHoursExternal() {
        if (ivjLblHoursExternal == null) {
            try {
                ivjLblHoursExternal = new javax.swing.JLabel();
                ivjLblHoursExternal.setName("LblHoursExternal");
                ivjLblHoursExternal.setText("h");
                ivjLblHoursExternal.setBounds(273, 43, 25, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblHoursExternal;
    }

    /**
     * Return the LblName112 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblHoursInternal() {
        if (ivjLblHoursInternal == null) {
            try {
                ivjLblHoursInternal = new javax.swing.JLabel();
                ivjLblHoursInternal.setName("LblHoursInternal");
                ivjLblHoursInternal.setText("h");
                ivjLblHoursInternal.setBounds(274, 18, 25, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblHoursInternal;
    }

    /**
     * Return the LblName121 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblManYearHoursExternal() {
        if (ivjLblManYearHoursExternal == null) {
            try {
                ivjLblManYearHoursExternal = new javax.swing.JLabel();
                ivjLblManYearHoursExternal.setName("LblManYearHoursExternal");
                ivjLblManYearHoursExternal.setText("Mannjahr EXTERN:");
                ivjLblManYearHoursExternal.setBounds(15, 43, 163, 14);
                // user code begin {1}
                ivjLblManYearHoursExternal.setText(getResourceString("LblManYearHoursExternal_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblManYearHoursExternal;
    }

    /**
     * Return the LblName12 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblManYearHoursInternal() {
        if (ivjLblManYearHoursInternal == null) {
            try {
                ivjLblManYearHoursInternal = new javax.swing.JLabel();
                ivjLblManYearHoursInternal.setName("LblManYearHoursInternal");
                ivjLblManYearHoursInternal.setText("Mannjahr INTERN:");
                ivjLblManYearHoursInternal.setBounds(15, 18, 163, 14);
                // user code begin {1}
                ivjLblManYearHoursInternal.setText(getResourceString("LblManYearHoursInternal_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblManYearHoursInternal;
    }

    /**
     * Return the LblName1111 property value.
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
                ivjLblMonthDepreciation.setBounds(272, 76, 107, 14);
                // user code begin {1}
                ivjLblMonthDepreciation.setText(getResourceString("CIMonth"));
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
     * Return the LblName111 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblMonthFinancial() {
        if (ivjLblMonthFinancial == null) {
            try {
                ivjLblMonthFinancial = new javax.swing.JLabel();
                ivjLblMonthFinancial.setName("LblMonthFinancial");
                ivjLblMonthFinancial.setText("Monate");
                ivjLblMonthFinancial.setBounds(266, 54, 131, 14);
                // user code begin {1}
                ivjLblMonthFinancial.setText(getResourceString("CIMonth"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblMonthFinancial;
    }

    /**
     * Return the LblName11 property value.
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
                ivjLblMonthTco.setBounds(266, 26, 131, 14);
                // user code begin {1}
                ivjLblMonthTco.setText(getResourceString("CIMonth"));
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
     * Return the LblName113 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblMonthUsage() {
        if (ivjLblMonthUsage == null) {
            try {
                ivjLblMonthUsage = new javax.swing.JLabel();
                ivjLblMonthUsage.setName("LblMonthUsage");
                ivjLblMonthUsage.setText("Monate");
                ivjLblMonthUsage.setBounds(272, 46, 131, 14);
                // user code begin {1}
                ivjLblMonthTco.setText(getResourceString("CIMonth"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblMonthUsage;
    }

    /**
     * Return the LblName11211 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPercentage() {
        if (ivjLblPercentage == null) {
            try {
                ivjLblPercentage = new javax.swing.JLabel();
                ivjLblPercentage.setName("LblPercentage");
                ivjLblPercentage.setText("%");
                ivjLblPercentage.setBounds(273, 100, 34, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPercentage;
    }

    /**
     * Return the LblName122 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblReportCostExponent() {
        if (ivjLblReportCostExponent == null) {
            try {
                ivjLblReportCostExponent = new javax.swing.JLabel();
                ivjLblReportCostExponent.setName("LblReportCostExponent");
                ivjLblReportCostExponent.setToolTipText("Darstellung von Beträgen");
                ivjLblReportCostExponent.setText("Kosten-\"Grösse\":");
                ivjLblReportCostExponent.setBounds(17, 19, 166, 14);
                // user code begin {1}
                ivjLblReportCostExponent.setToolTipText(getResourceString("LblReportCostExponent_toolTipText"));
                ivjLblReportCostExponent.setText(getResourceString("LblReportCostExponent_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblReportCostExponent;
    }

    /**
     * Return the LblDefaultDepreciationDuration1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblReportDepreciationDuration() {
        if (ivjLblReportDepreciationDuration == null) {
            try {
                ivjLblReportDepreciationDuration = new javax.swing.JLabel();
                ivjLblReportDepreciationDuration.setName("LblReportDepreciationDuration");
                ivjLblReportDepreciationDuration.setToolTipText("Zeitraum für die Betrachtung der Abschreibungs-Kosten.");
                ivjLblReportDepreciationDuration.setText("Max. Abschreibungsdauer:");
                ivjLblReportDepreciationDuration.setBounds(17, 76, 166, 14);
                // user code begin {1}
                ivjLblReportDepreciationDuration.setToolTipText(getResourceString("LblReportDepreciationDuration_toolTipText"));
                ivjLblReportDepreciationDuration.setText(getResourceString("LblReportDepreciationDuration_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblReportDepreciationDuration;
    }

    /**
     * Return the LblDefaultUsageDuration1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblReportUsageDuration() {
        if (ivjLblReportUsageDuration == null) {
            try {
                ivjLblReportUsageDuration = new javax.swing.JLabel();
                ivjLblReportUsageDuration.setName("LblReportUsageDuration");
                ivjLblReportUsageDuration.setToolTipText("Zeitraum für die Betrachtung der TCO-Kosten.");
                ivjLblReportUsageDuration.setText("Max. Nutzungsdauer:");
                ivjLblReportUsageDuration.setBounds(17, 46, 166, 14);
                // user code begin {1}
                ivjLblReportUsageDuration.setToolTipText(getResourceString("LblReportUsageDuration_toolTipText"));
                ivjLblReportUsageDuration.setText(getResourceString("LblReportUsageDuration_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblReportUsageDuration;
    }

    /**
     * Return the Object property value.
     *
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.SystemParameter getObject() {
        // user code begin {1}
        // user code end
        return ivjObject;
    }

    /**
     * Return the PnlBenchmarking property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlBenchmarking() {
        if (ivjPnlBenchmarking == null) {
            try {
                ivjPnlBenchmarking = new javax.swing.JPanel();
                ivjPnlBenchmarking.setName("PnlBenchmarking");
                ivjPnlBenchmarking.setLayout(null);
                ivjPnlBenchmarking.setBounds(11, 256, 487, 86);
                getPnlBenchmarking().add(getLblManYearHoursInternal(), getLblManYearHoursInternal().getName());
                getPnlBenchmarking().add(getTxtManYearInternal(), getTxtManYearInternal().getName());
                getPnlBenchmarking().add(getLblHoursInternal(), getLblHoursInternal().getName());
                getPnlBenchmarking().add(getLblManYearHoursExternal(), getLblManYearHoursExternal().getName());
                getPnlBenchmarking().add(getTxtManYearExternal(), getTxtManYearExternal().getName());
                getPnlBenchmarking().add(getLblHoursExternal(), getLblHoursExternal().getName());
                // user code begin {1}
                getPnlBenchmarking().setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlBenchmarking_text")),
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlBenchmarking;
    }

    /**
     * Return the PnlCommon property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCommon() {
        if (ivjPnlCommon == null) {
            try {
                ivjPnlCommon = new javax.swing.JPanel();
                ivjPnlCommon.setName("PnlCommon");
                ivjPnlCommon.setLayout(null);
                ivjPnlCommon.setBounds(13, 20, 487, 109);
                getPnlCommon().add(getLblDefaultCurrency(), getLblDefaultCurrency().getName());
                getPnlCommon().add(getCbxCurrency(), getCbxCurrency().getName());
                // user code begin {1}
                getPnlCommon().setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlCommon_text")),
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCommon;
    }

    /**
     * Return the PnlFactCost property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlFactCost() {
        if (ivjPnlFactCost == null) {
            try {
                ivjPnlFactCost = new javax.swing.JPanel();
                ivjPnlFactCost.setName("PnlFactCost");
                ivjPnlFactCost.setLayout(null);
                ivjPnlFactCost.setBounds(12, 147, 487, 91);
                getPnlFactCost().add(getLblDefaultDepreciationDuration(), getLblDefaultDepreciationDuration().getName());
                getPnlFactCost().add(getLblDefaultUsageDuration(), getLblDefaultUsageDuration().getName());
                getPnlFactCost().add(getTxtUsage(), getTxtUsage().getName());
                getPnlFactCost().add(getTxtDepreciation(), getTxtDepreciation().getName());
                getPnlFactCost().add(getLblMonthTco(), getLblMonthTco().getName());
                getPnlFactCost().add(getLblMonthFinancial(), getLblMonthFinancial().getName());
                // user code begin {1}
                getPnlFactCost().setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlFactCost_text")),
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlFactCost;
    }

    /**
     * Return the PnlReporting property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlReporting() {
        if (ivjPnlReporting == null) {
            try {
                ivjPnlReporting = new javax.swing.JPanel();
                ivjPnlReporting.setName("PnlReporting");
                ivjPnlReporting.setLayout(null);
                ivjPnlReporting.setBounds(13, 359, 487, 154);
                getPnlReporting().add(getLblReportCostExponent(), getLblReportCostExponent().getName());
                getPnlReporting().add(getCbxAmountExp(), getCbxAmountExp().getName());
                getPnlReporting().add(getLblCurrencyUnit(), getLblCurrencyUnit().getName());
                getPnlReporting().add(getLblDepreciationInterestRate(), getLblDepreciationInterestRate().getName());
                getPnlReporting().add(getTxtInterestRate(), getTxtInterestRate().getName());
                getPnlReporting().add(getLblPercentage(), getLblPercentage().getName());
                getPnlReporting().add(getRbtLinear(), getRbtLinear().getName());
                getPnlReporting().add(getRbtProgrssive(), getRbtProgrssive().getName());
                getPnlReporting().add(getRbtRegressive(), getRbtRegressive().getName());
                getPnlReporting().add(getLblReportUsageDuration(), getLblReportUsageDuration().getName());
                getPnlReporting().add(getTxtReportUsage(), getTxtReportUsage().getName());
                getPnlReporting().add(getLblMonthUsage(), getLblMonthUsage().getName());
                getPnlReporting().add(getLblReportDepreciationDuration(), getLblReportDepreciationDuration().getName());
                getPnlReporting().add(getTxtReportDepreciation(), getTxtReportDepreciation().getName());
                getPnlReporting().add(getLblMonthDepreciation(), getLblMonthDepreciation().getName());
                // user code begin {1}
                getPnlReporting().setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlReporting_text")),
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlReporting;
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
     * Return the JRadioButton13 property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtLinear() {
        if (ivjRbtLinear == null) {
            try {
                ivjRbtLinear = new javax.swing.JRadioButton();
                ivjRbtLinear.setName("RbtLinear");
                ivjRbtLinear.setSelected(true);
                ivjRbtLinear.setText("linear");
                ivjRbtLinear.setBounds(190, 121, 75, 22);
                ivjRbtLinear.setEnabled(false);
                // user code begin {1}
                //TODO NLS
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtLinear;
    }

    /**
     * Return the JRadioButton11 property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtProgrssive() {
        if (ivjRbtProgrssive == null) {
            try {
                ivjRbtProgrssive = new javax.swing.JRadioButton();
                ivjRbtProgrssive.setName("RbtProgrssive");
                ivjRbtProgrssive.setText("progressiv");
                ivjRbtProgrssive.setBounds(273, 121, 93, 22);
                ivjRbtProgrssive.setEnabled(false);
                // user code begin {1}
                //TODO NLS
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtProgrssive;
    }

    /**
     * Return the JRadioButton12 property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtRegressive() {
        if (ivjRbtRegressive == null) {
            try {
                ivjRbtRegressive = new javax.swing.JRadioButton();
                ivjRbtRegressive.setName("RbtRegressive");
                ivjRbtRegressive.setText("regressiv");
                ivjRbtRegressive.setBounds(373, 121, 108, 22);
                ivjRbtRegressive.setEnabled(false);
                // user code begin {1}
                //TODO NLS
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtRegressive;
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
                ivjTxtDepreciation.setBounds(185, 51, 73, 20);
                ivjTxtDepreciation.setEditable(true);
                ivjTxtDepreciation.setEnabled(true);
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
     * Return the TxtInterestRate property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtInterestRate() {
        if (ivjTxtInterestRate == null) {
            try {
                ivjTxtInterestRate = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtInterestRate.setName("TxtInterestRate");
                ivjTxtInterestRate.setBounds(190, 98, 73, 20);
                ivjTxtInterestRate.setEditable(true);
                ivjTxtInterestRate.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtInterestRate;
    }

    /**
     * Return the TxtManYearExternal property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtManYearExternal() {
        if (ivjTxtManYearExternal == null) {
            try {
                ivjTxtManYearExternal = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtManYearExternal.setName("TxtManYearExternal");
                ivjTxtManYearExternal.setBounds(190, 42, 73, 20);
                ivjTxtManYearExternal.setEditable(true);
                ivjTxtManYearExternal.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtManYearExternal;
    }

    /**
     * Return the TxtManYearInternal property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtManYearInternal() {
        if (ivjTxtManYearInternal == null) {
            try {
                ivjTxtManYearInternal = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtManYearInternal.setName("TxtManYearInternal");
                ivjTxtManYearInternal.setBounds(190, 16, 73, 20);
                ivjTxtManYearInternal.setEditable(true);
                ivjTxtManYearInternal.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtManYearInternal;
    }

    /**
     * Return the TxtReportDepreciation property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtReportDepreciation() {
        if (ivjTxtReportDepreciation == null) {
            try {
                ivjTxtReportDepreciation = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtReportDepreciation.setName("TxtReportDepreciation");
                ivjTxtReportDepreciation.setBounds(190, 73, 73, 20);
                ivjTxtReportDepreciation.setEditable(true);
                ivjTxtReportDepreciation.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtReportDepreciation;
    }

    /**
     * Return the TxtReportUsage property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtReportUsage() {
        if (ivjTxtReportUsage == null) {
            try {
                ivjTxtReportUsage = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtReportUsage.setName("TxtReportUsage");
                ivjTxtReportUsage.setBounds(190, 43, 73, 20);
                ivjTxtReportUsage.setEditable(true);
                ivjTxtReportUsage.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtReportUsage;
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
                ivjTxtUsage.setBounds(185, 21, 73, 20);
                ivjTxtUsage.setEditable(true);
                ivjTxtUsage.setEnabled(true);
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
     *
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() {
        // user code begin {1}
        // user code end
        getPnlStandardToolbar().addPropertyChangeListener(ivjEventHandler);
        getPnlStandardToolbar().addToolBarListener(ivjEventHandler);
        getCbxCurrency().addItemListener(ivjEventHandler);
        getCbxAmountExp().addItemListener(ivjEventHandler);
        getTxtUsage().addKeyListener(ivjEventHandler);
        getTxtDepreciation().addKeyListener(ivjEventHandler);
        getTxtManYearInternal().addKeyListener(ivjEventHandler);
        getTxtManYearExternal().addKeyListener(ivjEventHandler);
        getTxtInterestRate().addKeyListener(ivjEventHandler);
        getTxtReportUsage().addKeyListener(ivjEventHandler);
        getTxtReportDepreciation().addKeyListener(ivjEventHandler);
        connPtoP6SetTarget();
        connPtoP3SetTarget();
        connPtoP7SetTarget();
        connPtoP9SetTarget();
        connPtoP1SetTarget();
        connPtoP2SetTarget();
        connPtoP4SetTarget();
        connPtoP5SetTarget();
        connPtoP8SetTarget();
        connPtoP11SetTarget();
        connPtoP10SetTarget();
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
            setName("SystemParameterDetailView");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(512, 600);
            setTitle("System-Parameter (Defaults)");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        setIconImage(ResourceManager.getImageIcon(LauncherView.class, "TCO_Icon.png").getImage());
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(SystemParameter.class));
        getPnlStandardToolbar().setObjects(getObjects());
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
/*	java.util.List list = new java.util.ArrayList(3);
	list.add("unverändert");
	list.add("in Kilo");
	list.add("in Mio.");
	getCbxAmountExp().setModel(new javax.swing.DefaultComboBoxModel(list.toArray()));
*/
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param event A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent event) {
/*	if (event.getPropertyName().equals("driver")) {
		getPnlCostDriver().showDriver();
	}
*/
    }

    /**
     * Redo the last undoing changes of an Object represented by this GUI.
     */
    @Override
    public void redoObject() {
    }

    /**
     * Save an Object represented by DetailView.
     */
    @Override
    public void saveObject() {
        LauncherView.getInstance().saveObject();
        closeOnSave();
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
                getObject().removePropertyChangeListener(this);
            }

            setObject(null);

            DbObjectServer server = ((DbObject) object).getObjectServer();
            JComboBoxUtility
                .initComboBox(getCbxCurrency(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility
                .initComboBox(getCbxAmountExp(), server.retrieveCodes(org.tcotool.model.CostExponent.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()),
                    JComboBoxUtility.SORT_KEEP_ORDER);

            setObject((org.tcotool.model.SystemParameter) object);
            getObject().addPropertyChangeListener(getConsistencyController());

            //	((DbChangeableBean)object).addPropertyChangeListener(this);
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
    private void setObject(org.tcotool.model.SystemParameter newValue) {
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
                connPtoP3SetTarget();
                connPtoP7SetTarget();
                connPtoP9SetTarget();
                connPtoP1SetTarget();
                connPtoP2SetTarget();
                connPtoP4SetTarget();
                connPtoP5SetTarget();
                connPtoP8SetTarget();
                connPtoP11SetTarget();
                connPtoP10SetTarget();
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
