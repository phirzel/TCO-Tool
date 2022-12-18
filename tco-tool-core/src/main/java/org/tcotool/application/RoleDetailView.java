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
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import org.tcotool.model.Currency;
import org.tcotool.model.Role;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.ModelUtility;

/**
 * DetailView of a Personal Role.
 *
 * @author Peter Hirzel
 */

public class RoleDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView {

    private ch.softenvironment.view.DetailView caller = null;
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjJPanel1 = null;
    private Role ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private javax.swing.JLabel ivjLblName = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtMultitude = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtHourlyRate = null;
    private ch.softenvironment.view.SimpleEditorPanel ivjPnlNote1 = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtHourlyRate1 = null;
    private javax.swing.JComboBox ivjCbxCurrency = null;
    private boolean ivjConnPtoP12Aligning = false;
    private boolean ivjConnPtoP15Aligning = false;
    private boolean ivjConnPtoP2Aligning = false;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JPanel ivjPnlYearlySettings = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtFte = null;
    private boolean ivjConnPtoP5Aligning = false;
    private ch.softenvironment.jomm.mvc.view.DbNlsStringView ivjDbNlsStringView1 = null;
    private boolean ivjConnPtoP1Aligning = false;
    private javax.swing.JCheckBox ivjChxInternal = null;
    private javax.swing.JLabel ivjLblDocumentation = null;
    private javax.swing.JLabel ivjLblEmploymentPercentageAvailable = null;
    private javax.swing.JLabel ivjLblFullTimeEquivalent = null;
    private javax.swing.JLabel ivjLblHourlyRate = null;
    private javax.swing.JLabel ivjLblYearlyHours = null;
    private boolean ivjConnPtoP8Aligning = false;
    private javax.swing.JComboBox ivjCbxCurrencyHourlyRate = null;
    private javax.swing.JLabel ivjLblHour = null;
    private javax.swing.JLabel ivjLblPercentage = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ItemListener,
        java.awt.event.KeyListener, java.beans.PropertyChangeListener {

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == RoleDetailView.this.getChxInternal()) {
                connPtoP15SetSource();
            }
            if (e.getSource() == RoleDetailView.this.getCbxCurrency()) {
                connPtoP2SetSource();
            }
            if (e.getSource() == RoleDetailView.this.getCbxCurrency()) {
                connPtoP9SetTarget();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == RoleDetailView.this.getTxtFte()) {
                connPtoP5SetSource();
            }
            if (e.getSource() == RoleDetailView.this.getTxtHourlyRate1()) {
                connPtoP4SetSource();
            }
            if (e.getSource() == RoleDetailView.this.getTxtHourlyRate()) {
                connPtoP12SetSource();
            }
            if (e.getSource() == RoleDetailView.this.getTxtMultitude()) {
                connPtoP3SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == RoleDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("internal"))) {
                connPtoP15SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("currency"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("fullTimeEquivalent"))) {
                connPtoP5SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("employmentPercentageAvailable"))) {
                connPtoP4SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("hourlyRate"))) {
                connPtoP12SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("yearlyHours"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getObject()) {
                connEtoC2(evt);
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP8SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == RoleDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == RoleDetailView.this.getObject() && (evt.getPropertyName().equals("name"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == RoleDetailView.this.getDbNlsStringView1() && (evt.getPropertyName().equals("dbNlsString"))) {
                connPtoP1SetSource();
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
            if (newEvent.getSource() == RoleDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == RoleDetailView.this.getPnlNote1()) {
                connPtoP8SetSource();
            }
        }

    }

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public RoleDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public RoleDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects, ch.softenvironment.view.DetailView caller) {
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
     * Calculate the cost relevant dependencies automatically, where FTE = yearlyHours * hourlyRate
     */
    private void calcSalary(java.beans.PropertyChangeEvent evt) throws Exception {
        if (evt != null) {
            // be aware of ping-pong effect
            if ((evt.getPropertyName().equals("fullTimeEquivalent") || evt.getPropertyName().equals("yearlyHours"))
                && (getObject().getFullTimeEquivalent() != null) && (getObject().getYearlyHours() != null)) {
                getObject().setHourlyRate(new Double(Calculator.calculateHourlyRate(getObject())));
                // update PersonalCost entries
                LauncherView.getInstance().getUtility().updateCost((TcoObject) LauncherView.getInstance().getUtility().getRoot());
            } else if (evt.getPropertyName().equals("currency") || evt.getPropertyName().equals("hourlyRate")) {
                if ((getObject().getHourlyRate() != null) && (getObject().getYearlyHours() != null)) {
                    // recalculate FTE
                    getObject().setFullTimeEquivalent(
                        new Double(AmountFormat.round(getObject().getHourlyRate().doubleValue() * getObject().getYearlyHours().doubleValue())));
                }
                // update PersonalCost entries
                LauncherView.getInstance().getUtility().updateCost((TcoObject) LauncherView.getInstance().getUtility().getRoot());
            }
        }
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
     * connEtoC2: (Object.propertyChange.propertyChange(java.beans.PropertyChangeEvent) --> RoleDetailView.calcSalary(Ljava.beans.PropertyChangeEvent;)V)
     *
     * @param arg1 java.beans.PropertyChangeEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.beans.PropertyChangeEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.calcSalary(arg1);
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
     * connPtoP12SetSource: (Object.hourlyRate <--> TxtHourlyRate.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP12SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP12Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP12Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setHourlyRate(new java.lang.Double(getTxtHourlyRate().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setHourlyRate(getTxtHourlyRate().getDoubleValue());
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
            if (ivjConnPtoP12Aligning == false) {
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
     * connPtoP15SetSource: (Object.internal <--> ChInternal.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP15SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP15Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP15Aligning = true;
                if ((getObject() != null)) {
                    getObject().setInternal(new java.lang.Boolean(getChxInternal().isSelected()));
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
            if (ivjConnPtoP15Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP15Aligning = true;
                if ((getObject() != null)) {
                    getChxInternal().setSelected((getObject().getInternal()).booleanValue());
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
     * connPtoP1SetSource: (Object.name <--> DbNlsStringView1.dbNlsString)
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
                    getObject().setName(getDbNlsStringView1().getDbNlsString());
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
     * connPtoP1SetTarget: (Object.name <--> JTextField1.text)
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
                    getDbNlsStringView1().setDbNlsString(getObject().getName());
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
            if (ivjConnPtoP2Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP2Aligning = true;
                if ((getObject() != null)) {
                    getObject().setCurrency((org.tcotool.model.Currency) getCbxCurrency().getSelectedItem());
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
            if (ivjConnPtoP2Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP2Aligning = true;
                if ((getObject() != null)) {
                    getCbxCurrency().setSelectedItem(getObject().getCurrency());
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
     * connPtoP3SetSource: (Object.yearlyHours <--> TxtMultitude.text)
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
                        getObject().setYearlyHours(new java.lang.Long(getTxtMultitude().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setYearlyHours(getTxtMultitude().getLongValue());
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
     * connPtoP3SetTarget: (Object.yearlyHours <--> TxtMultitude.text)
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
                    getTxtMultitude().setText(String.valueOf(getObject().getYearlyHours()));
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
     * connPtoP4SetSource: (Object.employmentPercentageAvailable <--> TxtHourlyRate1.text)
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
                        getObject().setEmploymentPercentageAvailable(new java.lang.Long(getTxtHourlyRate1().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setEmploymentPercentageAvailable(getTxtHourlyRate1().getLongValue());
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
     * connPtoP4SetTarget: (Object.employmentPercentageAvailable <--> TxtHourlyRate1.text)
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
                    getTxtHourlyRate1().setText(String.valueOf(getObject().getEmploymentPercentageAvailable()));
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
     * connPtoP5SetSource: (Object.fullTimeEquivalent <--> TxtFte.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP5SetSource() {
        /* Set the source from the target */
        try {
            if (ivjConnPtoP5Aligning == false) {
                // user code begin {1}
                try {
                    // user code end
                    ivjConnPtoP5Aligning = true;
                    if ((getObject() != null)) {
                        getObject().setFullTimeEquivalent(new java.lang.Double(getTxtFte().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setFullTimeEquivalent(getTxtFte().getDoubleValue());
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
     * connPtoP5SetTarget: (Object.fullTimeEquivalent <--> TxtFte.text)
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
                    getTxtFte().setText(String.valueOf(getObject().getFullTimeEquivalent()));
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
     * connPtoP7SetTarget: (Object.mark <--> PnlStatusBar.mark)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP7SetTarget() {
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
     * connPtoP8SetSource: (Object.documentation <--> PnlNote1.text)
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
                    getObject().setDocumentation(getPnlNote1().getText());
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
     * connPtoP8SetTarget: (Object.documentation <--> PnlNote1.text)
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
                    getPnlNote1().setText(getObject().getDocumentation());
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
     * connPtoP9SetTarget: (CbxCurrency.selectedItem <--> CbxCurrencyHourlyRate.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP9SetTarget() {
        /* Set the target from the source */
        try {
            getCbxCurrencyHourlyRate().setSelectedItem(getCbxCurrency().getSelectedItem());
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
                ivjCbxCurrency.setBounds(288, 17, 84, 23);
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
     * Return the CbxCurrencyHourlyRate property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyHourlyRate() {
        if (ivjCbxCurrencyHourlyRate == null) {
            try {
                ivjCbxCurrencyHourlyRate = new javax.swing.JComboBox();
                ivjCbxCurrencyHourlyRate.setName("CbxCurrencyHourlyRate");
                ivjCbxCurrencyHourlyRate.setBounds(288, 43, 84, 23);
                ivjCbxCurrencyHourlyRate.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyHourlyRate;
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
                ivjChxInternal.setToolTipText("sonst externe Mitarbeiter");
                ivjChxInternal.setText("Intern");
                // user code begin {1}
                ivjChxInternal.setToolTipText(ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_toolTipText"));
                ivjChxInternal.setText(ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text"));
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
     * Return the ConsistencyController property value.
     *
     * @return ch.softenvironment.jomm.mvc.controller.ConsistencyController
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.controller.ConsistencyController getConsistencyController() {
        // user code begin {1}
        // user code end
        return ivjConsistencyController;
    }

    /**
     * Return the DbNlsStringView1 property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbNlsStringView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbNlsStringView getDbNlsStringView1() {
        if (ivjDbNlsStringView1 == null) {
            try {
                ivjDbNlsStringView1 = new ch.softenvironment.jomm.mvc.view.DbNlsStringView();
                ivjDbNlsStringView1.setName("DbNlsStringView1");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjDbNlsStringView1;
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
                ivjJPanel1.setLayout(new java.awt.GridBagLayout());

                java.awt.GridBagConstraints constraintsLblName = new java.awt.GridBagConstraints();
                constraintsLblName.gridx = 1;
                constraintsLblName.gridy = 1;
                constraintsLblName.ipadx = 88;
                constraintsLblName.insets = new java.awt.Insets(16, 10, 9, 5);
                getJPanel1().add(getLblName(), constraintsLblName);

                java.awt.GridBagConstraints constraintsChxInternal = new java.awt.GridBagConstraints();
                constraintsChxInternal.gridx = 2;
                constraintsChxInternal.gridy = 2;
                constraintsChxInternal.ipadx = 212;
                constraintsChxInternal.insets = new java.awt.Insets(3, 8, 2, 138);
                getJPanel1().add(getChxInternal(), constraintsChxInternal);

                java.awt.GridBagConstraints constraintsPnlNote1 = new java.awt.GridBagConstraints();
                constraintsPnlNote1.gridx = 1;
                constraintsPnlNote1.gridy = 5;
                constraintsPnlNote1.gridwidth = 2;
                constraintsPnlNote1.fill = java.awt.GridBagConstraints.BOTH;
                constraintsPnlNote1.weightx = 1.0;
                constraintsPnlNote1.weighty = 1.0;
                constraintsPnlNote1.ipadx = 554;
                constraintsPnlNote1.ipady = 90;
                constraintsPnlNote1.insets = new java.awt.Insets(3, 10, 17, 9);
                getJPanel1().add(getPnlNote1(), constraintsPnlNote1);

                java.awt.GridBagConstraints constraintsLblDocumentation = new java.awt.GridBagConstraints();
                constraintsLblDocumentation.gridx = 1;
                constraintsLblDocumentation.gridy = 4;
                constraintsLblDocumentation.ipadx = 92;
                constraintsLblDocumentation.insets = new java.awt.Insets(6, 10, 2, 46);
                getJPanel1().add(getLblDocumentation(), constraintsLblDocumentation);

                java.awt.GridBagConstraints constraintsPnlYearlySettings = new java.awt.GridBagConstraints();
                constraintsPnlYearlySettings.gridx = 1;
                constraintsPnlYearlySettings.gridy = 3;
                constraintsPnlYearlySettings.gridwidth = 2;
                constraintsPnlYearlySettings.fill = java.awt.GridBagConstraints.BOTH;
                constraintsPnlYearlySettings.weightx = 1.0;
                constraintsPnlYearlySettings.weighty = 1.0;
                constraintsPnlYearlySettings.ipadx = 576;
                constraintsPnlYearlySettings.ipady = 126;
                constraintsPnlYearlySettings.insets = new java.awt.Insets(3, 10, 6, 9);
                getJPanel1().add(getPnlYearlySettings(), constraintsPnlYearlySettings);

                java.awt.GridBagConstraints constraintsDbNlsStringView1 = new java.awt.GridBagConstraints();
                constraintsDbNlsStringView1.gridx = 2;
                constraintsDbNlsStringView1.gridy = 1;
                constraintsDbNlsStringView1.fill = java.awt.GridBagConstraints.BOTH;
                constraintsDbNlsStringView1.weightx = 1.0;
                constraintsDbNlsStringView1.weighty = 1.0;
                constraintsDbNlsStringView1.ipadx = 190;
                constraintsDbNlsStringView1.insets = new java.awt.Insets(10, 5, 3, 10);
                getJPanel1().add(getDbNlsStringView1(), constraintsDbNlsStringView1);
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
     * Return the JLabel12 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDocumentation() {
        if (ivjLblDocumentation == null) {
            try {
                ivjLblDocumentation = new javax.swing.JLabel();
                ivjLblDocumentation.setName("LblDocumentation");
                ivjLblDocumentation.setText("Notiz:");
                // user code begin {1}
                ivjLblDocumentation.setText(ch.softenvironment.client.ResourceManager.getResourceAsLabel(ServiceDetailView.class, "PnlNote_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDocumentation;
    }

    /**
     * Return the JLabel11 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblEmploymentPercentageAvailable() {
        if (ivjLblEmploymentPercentageAvailable == null) {
            try {
                ivjLblEmploymentPercentageAvailable = new javax.swing.JLabel();
                ivjLblEmploymentPercentageAvailable.setName("LblEmploymentPercentageAvailable");
                ivjLblEmploymentPercentageAvailable.setToolTipText("Anzahl Stellen von dieser Rolle jährlich?");
                ivjLblEmploymentPercentageAvailable.setText("Stellenprozente verfügbar:");
                ivjLblEmploymentPercentageAvailable.setBounds(9, 72, 160, 14);
                // user code begin {1}
                ivjLblEmploymentPercentageAvailable.setToolTipText(getResourceString("LblEmploymentPercentageAvailable_toolTipText"));
                ivjLblEmploymentPercentageAvailable.setText(getResourceString("LblEmploymentPercentageAvailable_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblEmploymentPercentageAvailable;
    }

    /**
     * Return the JLabel131 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblFullTimeEquivalent() {
        if (ivjLblFullTimeEquivalent == null) {
            try {
                ivjLblFullTimeEquivalent = new javax.swing.JLabel();
                ivjLblFullTimeEquivalent.setName("LblFullTimeEquivalent");
                ivjLblFullTimeEquivalent.setToolTipText("Full Time Equivalent");
                ivjLblFullTimeEquivalent.setText("Vollkosten (FTE):");
                ivjLblFullTimeEquivalent.setBounds(9, 20, 160, 14);
                // user code begin {1}
                ivjLblFullTimeEquivalent.setText(getResourceString("LblFullTimeEquivalent_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblFullTimeEquivalent;
    }

    /**
     * Return the LblHour property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblHour() {
        if (ivjLblHour == null) {
            try {
                ivjLblHour = new javax.swing.JLabel();
                ivjLblHour.setName("LblHour");
                ivjLblHour.setText("h");
                ivjLblHour.setBounds(255, 98, 26, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblHour;
    }

    /**
     * Return the JLabel13 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblHourlyRate() {
        if (ivjLblHourlyRate == null) {
            try {
                ivjLblHourlyRate = new javax.swing.JLabel();
                ivjLblHourlyRate.setName("LblHourlyRate");
                ivjLblHourlyRate.setToolTipText("Salär pro h");
                ivjLblHourlyRate.setText("Stundenansatz:");
                ivjLblHourlyRate.setBounds(9, 43, 160, 14);
                // user code begin {1}
                ivjLblHourlyRate.setToolTipText("");
                ivjLblHourlyRate.setText(getResourceString("LblHourlyRate_text"));
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
     * Return the JLabel2 property value.
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
                ivjLblPercentage.setBounds(255, 74, 26, 14);
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
     * Return the LblMultitude property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblYearlyHours() {
        if (ivjLblYearlyHours == null) {
            try {
                ivjLblYearlyHours = new javax.swing.JLabel();
                ivjLblYearlyHours.setName("LblYearlyHours");
                ivjLblYearlyHours.setToolTipText("Max. Anzahl Arbeitsstunden pro 100% dieser Rolle?");
                ivjLblYearlyHours.setText("Mannjahr:");
                ivjLblYearlyHours.setBounds(9, 96, 160, 14);
                // user code begin {1}
                ivjLblYearlyHours.setToolTipText(getResourceString("LblYearlyHours_toolTipText"));
                ivjLblYearlyHours.setText(getResourceString("LblYearlyHours_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblYearlyHours;
    }

    /**
     * Return the Object property value.
     *
     * @return org.tcotool.model.Role
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.Role getObject() {
        // user code begin {1}
        // user code end
        return ivjObject;
    }

    /**
     * Return the PnlNote1 property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.SimpleEditorPanel getPnlNote1() {
        if (ivjPnlNote1 == null) {
            try {
                ivjPnlNote1 = new ch.softenvironment.view.SimpleEditorPanel();
                ivjPnlNote1.setName("PnlNote1");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlNote1;
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
     * Return the PnlYearlySettings property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlYearlySettings() {
        if (ivjPnlYearlySettings == null) {
            try {
                ivjPnlYearlySettings = new javax.swing.JPanel();
                ivjPnlYearlySettings.setName("PnlYearlySettings");
                ivjPnlYearlySettings.setLayout(null);
                getPnlYearlySettings().add(getLblHourlyRate(), getLblHourlyRate().getName());
                getPnlYearlySettings().add(getTxtFte(), getTxtFte().getName());
                getPnlYearlySettings().add(getLblEmploymentPercentageAvailable(), getLblEmploymentPercentageAvailable().getName());
                getPnlYearlySettings().add(getTxtHourlyRate1(), getTxtHourlyRate1().getName());
                getPnlYearlySettings().add(getLblPercentage(), getLblPercentage().getName());
                getPnlYearlySettings().add(getLblFullTimeEquivalent(), getLblFullTimeEquivalent().getName());
                getPnlYearlySettings().add(getTxtHourlyRate(), getTxtHourlyRate().getName());
                getPnlYearlySettings().add(getCbxCurrency(), getCbxCurrency().getName());
                getPnlYearlySettings().add(getLblYearlyHours(), getLblYearlyHours().getName());
                getPnlYearlySettings().add(getTxtMultitude(), getTxtMultitude().getName());
                getPnlYearlySettings().add(getCbxCurrencyHourlyRate(), getCbxCurrencyHourlyRate().getName());
                getPnlYearlySettings().add(getLblHour(), getLblHour().getName());
                // user code begin {1}
                getPnlYearlySettings().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlYearlySettings_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlYearlySettings;
    }

    /**
     * Return the TxtFte property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtFte() {
        if (ivjTxtFte == null) {
            try {
                ivjTxtFte = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtFte.setName("TxtFte");
                ivjTxtFte.setBounds(175, 19, 102, 20);
                ivjTxtFte.setEditable(true);
                ivjTxtFte.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtFte;
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
                ivjTxtHourlyRate.setBounds(175, 42, 73, 20);
                ivjTxtHourlyRate.setEditable(true);
                ivjTxtHourlyRate.setEnabled(true);
                // user code begin {1}
                ivjTxtHourlyRate.setToolTipText("~" + ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblFullTimeEquivalent_text") + " / "
                    + ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblYearlyHours_text"));
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
     * Return the TxtHourlyRate1 property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtHourlyRate1() {
        if (ivjTxtHourlyRate1 == null) {
            try {
                ivjTxtHourlyRate1 = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtHourlyRate1.setName("TxtHourlyRate1");
                ivjTxtHourlyRate1.setBounds(175, 71, 73, 20);
                ivjTxtHourlyRate1.setEditable(true);
                ivjTxtHourlyRate1.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtHourlyRate1;
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
                ivjTxtMultitude.setBounds(175, 94, 73, 20);
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
        getPnlStandardToolbar().addToolBarListener(ivjEventHandler);
        getChxInternal().addItemListener(ivjEventHandler);
        getCbxCurrency().addItemListener(ivjEventHandler);
        getTxtFte().addKeyListener(ivjEventHandler);
        getTxtHourlyRate1().addKeyListener(ivjEventHandler);
        getTxtHourlyRate().addKeyListener(ivjEventHandler);
        getTxtMultitude().addKeyListener(ivjEventHandler);
        getPnlNote1().addSimpleEditorPanelListener(ivjEventHandler);
        getDbNlsStringView1().addPropertyChangeListener(ivjEventHandler);
        connPtoP15SetTarget();
        connPtoP2SetTarget();
        connPtoP7SetTarget();
        connPtoP5SetTarget();
        connPtoP4SetTarget();
        connPtoP12SetTarget();
        connPtoP3SetTarget();
        connPtoP8SetTarget();
        connPtoP6SetTarget();
        connPtoP1SetTarget();
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
            setName("RoleDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(595, 427);
            setTitle("Rolle");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        setIconImage(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png").getImage());
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(Role.class));
        getPnlStandardToolbar().setObjects(getObjects());
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
     * @see #calcSalary(java.beans.PropertyChangeEvent)
     */
    @Override
    public void saveObject() {
        try {
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
     * @param newValue ch.softenvironment.jomm.mvc.controller.ConsistencyController
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

            if (((Role) object).getPersistencyState().isNew()) {
                ((Role) object).refresh(false);
                ModelUtility.initializeRole(((Role) object), LauncherView.getInstance().getUtility().getSystemParameter());
            }
            JComboBoxUtility.initComboBox(getCbxCurrency(), ((Role) object).getObjectServer().retrieveCodes(Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyHourlyRate(), ((Role) object).getObjectServer().retrieveCodes(Currency.class), DbObject.PROPERTY_NAME,
                false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            setObject((Role) object);
            getObject().addPropertyChangeListener(getConsistencyController());
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Set the Object to a new value.
     *
     * @param newValue org.tcotool.model.Role
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void setObject(org.tcotool.model.Role newValue) {
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
                connPtoP15SetTarget();
                connPtoP2SetTarget();
                connPtoP7SetTarget();
                connPtoP5SetTarget();
                connPtoP4SetTarget();
                connPtoP12SetTarget();
                connPtoP3SetTarget();
                connPtoP8SetTarget();
                connPtoP1SetTarget();
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
