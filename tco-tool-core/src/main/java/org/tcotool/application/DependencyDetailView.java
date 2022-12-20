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
import ch.softenvironment.math.MathUtils;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import ch.softenvironment.view.tree.TreeSelectionDialog;
import org.tcotool.model.Currency;
import org.tcotool.model.Dependency;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.ModelUtility;

import java.util.Iterator;

/**
 * DetailView of a Dependency between 2 TcoObject's. The client depends on supplier by a distribution in %.
 *
 * @author Peter Hirzel
 */

public class DependencyDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView,
        java.beans.PropertyChangeListener {

    private ch.softenvironment.view.DetailView caller = null;
    private TcoObject supplier = null;
    private Service client = null;
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjJPanel1 = null;
    private Dependency ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private javax.swing.JComboBox ivjCbxCurrencyDistribution = null;
    private javax.swing.JComboBox ivjCbxDegree = null;
    private ch.softenvironment.view.SimpleEditorPanel ivjSimpleEditorPanel1 = null;
    private javax.swing.JButton ivjBtnChooseSupplier = null;
    private javax.swing.JComboBox ivjCbxCurrencySupplier = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtSupplierCost = null;
    private javax.swing.JTextField ivjTxtSupplierName = null;
    private boolean ivjConnPtoP2Aligning = false;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtClientCost = null;
    private javax.swing.JPanel ivjPnlClient = null;
    private javax.swing.JPanel ivjPnlSupplier = null;
    private javax.swing.JTextField ivjTxtClientName = null;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP1Aligning = false;
    private javax.swing.JLabel ivjJLabel1 = null;
    private javax.swing.JLabel ivjJLabel11 = null;
    private javax.swing.JLabel ivjLblDistribution = null;
    private javax.swing.JSlider ivjSliClient = null;
    private javax.swing.JSlider ivjSliSupplier = null;
    private javax.swing.JCheckBox ivjChxVariant = null;
    private boolean ivjConnPtoP4Aligning = false;
    private boolean ivjConnPtoP5Aligning = false;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtClientDistribution = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtSupplierCostDistribution = null;
    private javax.swing.JLabel ivjLblClientName = null;
    private javax.swing.JLabel ivjLblDistributedCost = null;
    private javax.swing.JLabel ivjLblNote = null;
    private javax.swing.JLabel ivjLblSupplierDistribution = null;
    private javax.swing.JLabel ivjLblSupplierInfluence = null;
    private javax.swing.JLabel ivjLblSupplierName = null;
    private javax.swing.JLabel ivjLblSupplierTotal = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ActionListener,
        java.awt.event.ItemListener, java.awt.event.MouseListener, java.beans.PropertyChangeListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == DependencyDetailView.this.getBtnChooseSupplier()) {
                connEtoC2(e);
            }
        }

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == DependencyDetailView.this.getCbxDegree()) {
                connPtoP3SetSource();
            }
            if (e.getSource() == DependencyDetailView.this.getChxVariant()) {
                connPtoP4SetSource();
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
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.getSource() == DependencyDetailView.this.getSliClient()) {
                connPtoP1SetSource();
            }
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == DependencyDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == DependencyDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == DependencyDetailView.this.getObject() && (evt.getPropertyName().equals("distribution"))) {
                connEtoC3(evt);
            }
            if (evt.getSource() == DependencyDetailView.this.getObject() && (evt.getPropertyName().equals("supplierInfluence"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == DependencyDetailView.this.getObject() && (evt.getPropertyName().equals("distribution"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == DependencyDetailView.this.getObject() && (evt.getPropertyName().equals("variant"))) {
                connPtoP4SetTarget();
            }
            if (evt.getSource() == DependencyDetailView.this.getObject() && (evt.getPropertyName().equals("distribution"))) {
                connPtoP5SetTarget();
            }
            if (evt.getSource() == DependencyDetailView.this.getTxtClientDistribution() && (evt.getPropertyName().equals("text"))) {
                connPtoP5SetSource();
            }
            if (evt.getSource() == DependencyDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == DependencyDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == DependencyDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
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
            if (newEvent.getSource() == DependencyDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == DependencyDetailView.this.getSimpleEditorPanel1()) {
                connPtoP2SetSource();
            }
        }

    }

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public DependencyDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public DependencyDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List<Dependency> objects, ch.softenvironment.view.DetailView caller,
        Service client) {
        super(viewOptions, objects);
        this.caller = caller;
        this.client = client;
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
     * Calculate the distribution percentage and Costs of Supplier and Client of a Dependency.
     */
    private void calcDistribution() throws Exception {
        Currency currency = LauncherView.getInstance().getUtility().getSystemParameter().getDefaultCurrency();
        getCbxCurrencyDistribution().setSelectedItem(currency);
        getCbxCurrencySupplier().setSelectedItem(currency);

        double supplierCost = 0.0;

        if (supplier == null) {
            getTxtSupplierName().setText(null);
            getTxtSupplierCost().setText(null);
            getTxtClientCost().setText(null);
            getObject().setDistribution(Double.valueOf(0.0));
            getSliClient().setValue(0);
            getTxtSupplierCostDistribution().setBackground(java.awt.Color.white);
        } else {
            java.text.NumberFormat af = AmountFormat.getInstance(LauncherView.getInstance().getSettings().getPlattformLocale());
            getTxtSupplierName().setText(supplier.getName());
            org.tcotool.tools.Calculator calculator = new org.tcotool.tools.CalculatorTco(LauncherView.getInstance().getUtility());
            supplierCost = calculator.calculateOverallCosts(supplier);
            getTxtSupplierCost().setText(af.format(supplierCost));

            // Client-Distribution
            getTxtClientCost().setText(af.format(supplierCost / 100.0 * getObject().getDistribution()));
            getSliClient().setValue(getObject().getDistribution().intValue());

            // Supplier-Distribution
            Iterator<Dependency> iterator = supplier.getClientId().iterator();
            double distribution = 0.0;
            while (iterator.hasNext()) {
                Dependency dependency = iterator.next();
                if (dependency.getDistribution() != null) {
                    distribution = distribution + dependency.getDistribution();
                }
                // TcoObject object =
                // LauncherView.getInstance().getUtility().findClient(dependency);
            }
            getSliSupplier().setValue((int) distribution);
            getTxtSupplierCostDistribution().setText("" + MathUtils.fix(distribution, 1));
            if (distribution > 100.0) {
                // over distributed
                getTxtSupplierCostDistribution().setBackground(java.awt.Color.red);
            } else {
                getTxtSupplierCostDistribution().setBackground(java.awt.Color.green);
            }
        }
    }

    /**
     * Supplier for new Dependency was chosen
     */
    private void chooseSupplier() {
        try {
            TreeSelectionDialog dialog = new TreeSelectionDialog(this, getResourceString("CTSelectObject"), LauncherView.getInstance().getUtility(), false);
            if (dialog.isSaved()) {
                String fault = LauncherView.getInstance().getUtility().addDependencyEnds(getObject(), client, (TcoObject) dialog.getSelectedObject());
                if (!StringUtils.isNullOrEmpty(fault)) {
                    ch.softenvironment.view.BaseDialog.showWarning(this, getResourceString("CWSelectObject"), fault);
                } else {
                    supplier = (TcoObject) dialog.getSelectedObject();
                    calcDistribution();
                    getPnlStandardToolbar().setTbbSaveEnabled(true);
                    if (caller != null) {
                        // TODO Patch: update by PropertyChange
                        ((ServiceDetailView) caller).refreshCosts();
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
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
     * connEtoC2: (BtnChooseSupplier.action.actionPerformed(java.awt.event.ActionEvent) --> DependencyDetailView.chooseSupplier()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.chooseSupplier();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (Object.distribution --> DependencyDetailView.calcDistribution(Ljava.lang.Double;)V)
     *
     * @param arg1 java.beans.PropertyChangeEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.beans.PropertyChangeEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.calcDistribution();
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
     * connPtoP1SetSource: (Object.distribution <--> Slider.value)
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
                    getObject().setDistribution(Integer.valueOf(getSliClient().getValue()).doubleValue());
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
     * connPtoP1SetTarget: (Object.distribution <--> PnlDistribution1.sliderValue)
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
                    getSliClient().setValue(getObject().getDistribution().intValue());
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
     * connPtoP2SetSource: (Object.documentation <--> SimpleEditorPanel1.text)
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
                    getObject().setDocumentation(getSimpleEditorPanel1().getText());
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
     * connPtoP2SetTarget: (Object.documentation <--> SimpleEditorPanel1.text)
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
                    getSimpleEditorPanel1().setText(getObject().getDocumentation());
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
     * connPtoP3SetSource: (Object.supplierInfluence <--> CbxDegree.selectedItem)
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
                    getObject().setSupplierInfluence((org.tcotool.model.SupplierInfluence) getCbxDegree().getSelectedItem());
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
     * connPtoP3SetTarget: (Object.supplierInfluence <--> CbxDegree.selectedItem)
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
                    getCbxDegree().setSelectedItem(getObject().getSupplierInfluence());
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
     * connPtoP4SetSource: (Object.variant <--> ChxVariant.selected)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP4SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP4Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP4Aligning = true;
                if ((getObject() != null)) {
                    getObject().setVariant(Boolean.valueOf(getChxVariant().isSelected()));
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
     * connPtoP4SetTarget: (Object.variant <--> ChxVariant.selected)
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
                    getChxVariant().setSelected(getObject().getVariant());
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
     * connPtoP5SetSource: (Object.distribution <--> TxtClientCost11.text)
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
                    getObject().setDistribution(Double.valueOf(getTxtClientDistribution().getText()));
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
     * connPtoP5SetTarget: (Object.distribution <--> TxtClientCost11.text)
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
                    getTxtClientDistribution().setText(String.valueOf(getObject().getDistribution()));
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
     * Return the BtnChooseSupplier property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnChooseSupplier() {
        if (ivjBtnChooseSupplier == null) {
            try {
                ivjBtnChooseSupplier = new javax.swing.JButton();
                ivjBtnChooseSupplier.setName("BtnChooseSupplier");
                ivjBtnChooseSupplier.setToolTipText("Basis auswaehlen");
                ivjBtnChooseSupplier.setText("...");
                ivjBtnChooseSupplier.setBounds(402, 16, 40, 25);
                // user code begin {1}
                ivjBtnChooseSupplier.setToolTipText(getResourceString("BtnChooseSupplier_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnChooseSupplier;
    }

    /**
     * Return the CbxCurrencyDistribution property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyDistribution() {
        if (ivjCbxCurrencyDistribution == null) {
            try {
                ivjCbxCurrencyDistribution = new javax.swing.JComboBox();
                ivjCbxCurrencyDistribution.setName("CbxCurrencyDistribution");
                ivjCbxCurrencyDistribution.setBounds(398, 93, 61, 23);
                ivjCbxCurrencyDistribution.setEnabled(false);
                // user code begin {1}
                ivjCbxCurrencyDistribution.setBounds(398, 93, 70, 25);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyDistribution;
    }

    /**
     * Return the CbxCurrency property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencySupplier() {
        if (ivjCbxCurrencySupplier == null) {
            try {
                ivjCbxCurrencySupplier = new javax.swing.JComboBox();
                ivjCbxCurrencySupplier.setName("CbxCurrencySupplier");
                ivjCbxCurrencySupplier.setBounds(402, 46, 61, 23);
                ivjCbxCurrencySupplier.setEnabled(false);
                // user code begin {1}
                ivjCbxCurrencySupplier.setBounds(402, 46, 70, 25);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencySupplier;
    }

    /**
     * Return the CbxDegree property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxDegree() {
        if (ivjCbxDegree == null) {
            try {
                ivjCbxDegree = new javax.swing.JComboBox();
                ivjCbxDegree.setName("CbxDegree");
                ivjCbxDegree.setBounds(166, 120, 262, 23);
                ivjCbxDegree.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxDegree;
    }

    /**
     * Return the ChxVariant property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxVariant() {
        if (ivjChxVariant == null) {
            try {
                ivjChxVariant = new javax.swing.JCheckBox();
                ivjChxVariant.setName("ChxVariant");
                ivjChxVariant.setToolTipText("Ja->Verteilung nur zum Variantenvergleich (keine zwingende Verteilung)");
                ivjChxVariant.setText("Variante");
                ivjChxVariant.setBounds(400, 17, 86, 22);
                // user code begin {1}
                ivjChxVariant.setToolTipText(getResourceString("ChxVariant_toolTipText"));
                ivjChxVariant.setText(getResourceString("ChxVariant_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxVariant;
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
                ivjJLabel1.setText("%");
                ivjJLabel1.setBounds(469, 79, 21, 14);
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
     * Return the JLabel11 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel11() {
        if (ivjJLabel11 == null) {
            try {
                ivjJLabel11 = new javax.swing.JLabel();
                ivjJLabel11.setName("JLabel11");
                ivjJLabel11.setText("%");
                ivjJLabel11.setBounds(464, 51, 23, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel11;
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
                getJPanel1().add(getPnlSupplier(), getPnlSupplier().getName());
                getJPanel1().add(getPnlClient(), getPnlClient().getName());
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
     * Return the LblName1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblClientName() {
        if (ivjLblClientName == null) {
            try {
                ivjLblClientName = new javax.swing.JLabel();
                ivjLblClientName.setName("LblClientName");
                ivjLblClientName.setText("Bezeichnung:");
                ivjLblClientName.setBounds(12, 23, 140, 14);
                // user code begin {1}
                ivjLblClientName.setText(ResourceManager.getResource(ServiceDetailView.class, "LblName_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblClientName;
    }

    /**
     * Return the JLabel5 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDistributedCost() {
        if (ivjLblDistributedCost == null) {
            try {
                ivjLblDistributedCost = new javax.swing.JLabel();
                ivjLblDistributedCost.setName("LblDistributedCost");
                ivjLblDistributedCost.setText("Abgewaelzter Betrag:");
                ivjLblDistributedCost.setBounds(12, 96, 140, 14);
                // user code begin {1}
                ivjLblDistributedCost.setText(getResourceString("LblDistributedCost_text"));
                ivjLblDistributedCost.setToolTipText(getResourceString("LblDistributedCost_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDistributedCost;
    }

    /**
     * Return the JLabel4 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDistribution() {
        if (ivjLblDistribution == null) {
            try {
                ivjLblDistribution = new javax.swing.JLabel();
                ivjLblDistribution.setName("LblDistribution");
                ivjLblDistribution.setText("Verteilung Kosten:");
                ivjLblDistribution.setBounds(12, 54, 140, 14);
                // user code begin {1}
                ivjLblDistribution.setText(getResourceString("SliDistribution_text") + ":");
                ivjLblDistribution.setToolTipText(getResourceString("LblDistribution_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDistribution;
    }

    /**
     * Return the JLabel21 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblNote() {
        if (ivjLblNote == null) {
            try {
                ivjLblNote = new javax.swing.JLabel();
                ivjLblNote.setName("LblNote");
                ivjLblNote.setText("Notiz:");
                ivjLblNote.setBounds(12, 142, 140, 14);
                // user code begin {1}
                ivjLblNote.setText(ResourceManager.getResource(ServiceDetailView.class, "PnlNote_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblNote;
    }

    /**
     * Return the JLabel7 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSupplierDistribution() {
        if (ivjLblSupplierDistribution == null) {
            try {
                ivjLblSupplierDistribution = new javax.swing.JLabel();
                ivjLblSupplierDistribution.setName("LblSupplierDistribution");
                ivjLblSupplierDistribution.setText("Gesamtverteilung:");
                ivjLblSupplierDistribution.setBounds(10, 82, 137, 14);
                // user code begin {1}
                ivjLblSupplierDistribution.setText(getResourceString("LblSupplierDistribution_text"));
                ivjLblSupplierDistribution.setToolTipText(getResourceString("LblSupplierDistribution_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSupplierDistribution;
    }

    /**
     * Return the JLabel2 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSupplierInfluence() {
        if (ivjLblSupplierInfluence == null) {
            try {
                ivjLblSupplierInfluence = new javax.swing.JLabel();
                ivjLblSupplierInfluence.setName("LblSupplierInfluence");
                ivjLblSupplierInfluence.setText("Einfluss von Verteiler:");
                ivjLblSupplierInfluence.setBounds(12, 124, 140, 14);
                // user code begin {1}
                ivjLblSupplierInfluence.setText(getResourceString("LblSupplierInfluence"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSupplierInfluence;
    }

    /**
     * Return the LblName property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSupplierName() {
        if (ivjLblSupplierName == null) {
            try {
                ivjLblSupplierName = new javax.swing.JLabel();
                ivjLblSupplierName.setName("LblSupplierName");
                ivjLblSupplierName.setText("Bezeichnung:");
                ivjLblSupplierName.setBounds(10, 24, 137, 14);
                // user code begin {1}
                ivjLblSupplierName.setText(ResourceManager.getResource(ServiceDetailView.class, "LblName_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSupplierName;
    }

    /**
     * Return the JLabel3 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSupplierTotal() {
        if (ivjLblSupplierTotal == null) {
            try {
                ivjLblSupplierTotal = new javax.swing.JLabel();
                ivjLblSupplierTotal.setName("LblSupplierTotal");
                ivjLblSupplierTotal.setText("Kosten:");
                ivjLblSupplierTotal.setBounds(10, 49, 137, 14);
                // user code begin {1}
                ivjLblSupplierTotal.setText(ResourceManager.getResource(ServiceDetailView.class, "PnlCost_text") + ":");
                ivjLblSupplierTotal.setToolTipText(getResourceString("LblSupplierCost_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSupplierTotal;
    }

    /**
     * Return the Object property value.
     *
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.Dependency getObject() {
        // user code begin {1}
        // user code end
        return ivjObject;
    }

    /**
     * Return the PnlClient property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlClient() {
        if (ivjPnlClient == null) {
            try {
                ivjPnlClient = new javax.swing.JPanel();
                ivjPnlClient.setName("PnlClient");
                ivjPnlClient.setLayout(null);
                ivjPnlClient.setBounds(12, 179, 497, 227);
                getPnlClient().add(getLblDistribution(), getLblDistribution().getName());
                getPnlClient().add(getLblDistributedCost(), getLblDistributedCost().getName());
                getPnlClient().add(getTxtClientCost(), getTxtClientCost().getName());
                getPnlClient().add(getCbxCurrencyDistribution(), getCbxCurrencyDistribution().getName());
                getPnlClient().add(getLblSupplierInfluence(), getLblSupplierInfluence().getName());
                getPnlClient().add(getCbxDegree(), getCbxDegree().getName());
                getPnlClient().add(getLblNote(), getLblNote().getName());
                getPnlClient().add(getSimpleEditorPanel1(), getSimpleEditorPanel1().getName());
                getPnlClient().add(getLblClientName(), getLblClientName().getName());
                getPnlClient().add(getTxtClientName(), getTxtClientName().getName());
                getPnlClient().add(getJLabel11(), getJLabel11().getName());
                getPnlClient().add(getSliClient(), getSliClient().getName());
                getPnlClient().add(getTxtClientDistribution(), getTxtClientDistribution().getName());
                getPnlClient().add(getChxVariant(), getChxVariant().getName());
                // user code begin {1}
                getPnlClient().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlClient_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlClient;
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
     * Return the PnlReuse property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlSupplier() {
        if (ivjPnlSupplier == null) {
            try {
                ivjPnlSupplier = new javax.swing.JPanel();
                ivjPnlSupplier.setName("PnlSupplier");
                ivjPnlSupplier.setLayout(null);
                ivjPnlSupplier.setBounds(12, 24, 497, 124);
                getPnlSupplier().add(getLblSupplierName(), getLblSupplierName().getName());
                getPnlSupplier().add(getLblSupplierTotal(), getLblSupplierTotal().getName());
                getPnlSupplier().add(getTxtSupplierCost(), getTxtSupplierCost().getName());
                getPnlSupplier().add(getCbxCurrencySupplier(), getCbxCurrencySupplier().getName());
                getPnlSupplier().add(getTxtSupplierName(), getTxtSupplierName().getName());
                getPnlSupplier().add(getBtnChooseSupplier(), getBtnChooseSupplier().getName());
                getPnlSupplier().add(getLblSupplierDistribution(), getLblSupplierDistribution().getName());
                getPnlSupplier().add(getTxtSupplierCostDistribution(), getTxtSupplierCostDistribution().getName());
                getPnlSupplier().add(getJLabel1(), getJLabel1().getName());
                getPnlSupplier().add(getSliSupplier(), getSliSupplier().getName());
                // user code begin {1}
                getPnlSupplier().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlSupplier_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlSupplier;
    }

    /**
     * Return the SimpleEditorPanel1 property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.SimpleEditorPanel getSimpleEditorPanel1() {
        if (ivjSimpleEditorPanel1 == null) {
            try {
                ivjSimpleEditorPanel1 = new ch.softenvironment.view.SimpleEditorPanel();
                ivjSimpleEditorPanel1.setName("SimpleEditorPanel1");
                ivjSimpleEditorPanel1.setBounds(12, 163, 465, 51);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjSimpleEditorPanel1;
    }

    /**
     * Return the Slider property value.
     *
     * @return javax.swing.JSlider
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSlider getSliClient() {
        if (ivjSliClient == null) {
            try {
                ivjSliClient = new javax.swing.JSlider();
                ivjSliClient.setName("SliClient");
                ivjSliClient.setPaintLabels(true);
                ivjSliClient.setPaintTicks(true);
                ivjSliClient.setValue(0);
                ivjSliClient.setPaintTrack(true);
                ivjSliClient.setMajorTickSpacing(10);
                ivjSliClient.setPreferredSize(new java.awt.Dimension(100, 16));
                ivjSliClient.setBounds(168, 49, 227, 44);
                ivjSliClient.setMinorTickSpacing(10);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjSliClient;
    }

    /**
     * Return the SliSupplier property value.
     *
     * @return javax.swing.JSlider
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSlider getSliSupplier() {
        if (ivjSliSupplier == null) {
            try {
                ivjSliSupplier = new javax.swing.JSlider();
                ivjSliSupplier.setName("SliSupplier");
                ivjSliSupplier.setPaintLabels(true);
                ivjSliSupplier.setInverted(false);
                ivjSliSupplier.setPaintTicks(true);
                ivjSliSupplier.setValue(0);
                ivjSliSupplier.setPaintTrack(true);
                ivjSliSupplier.setMajorTickSpacing(10);
                ivjSliSupplier.setPreferredSize(new java.awt.Dimension(100, 16));
                ivjSliSupplier.setBounds(167, 75, 228, 43);
                ivjSliSupplier.setMinorTickSpacing(10);
                ivjSliSupplier.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjSliSupplier;
    }

    /**
     * Return the TxtClientCost property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtClientCost() {
        if (ivjTxtClientCost == null) {
            try {
                ivjTxtClientCost = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtClientCost.setName("TxtClientCost");
                ivjTxtClientCost.setBounds(166, 94, 227, 20);
                ivjTxtClientCost.setEditable(false);
                ivjTxtClientCost.setEnabled(true);
                // user code begin {1}
                ivjTxtClientCost.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtClientCost;
    }

    /**
     * Return the TxtClientDistribution property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtClientDistribution() {
        if (ivjTxtClientDistribution == null) {
            try {
                ivjTxtClientDistribution = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtClientDistribution.setName("TxtClientDistribution");
                ivjTxtClientDistribution.setText("");
                ivjTxtClientDistribution.setBounds(400, 48, 61, 20);
                ivjTxtClientDistribution.setEditable(false);
                ivjTxtClientDistribution.setEnabled(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtClientDistribution;
    }

    /**
     * Return the TxtSupplierName1 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtClientName() {
        if (ivjTxtClientName == null) {
            try {
                ivjTxtClientName = new javax.swing.JTextField();
                ivjTxtClientName.setName("TxtClientName");
                ivjTxtClientName.setBounds(168, 21, 227, 20);
                ivjTxtClientName.setEditable(false);
                // user code begin {1}
                ivjTxtClientName.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtClientName;
    }

    /**
     * Return the TxtSupplierCost property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtSupplierCost() {
        if (ivjTxtSupplierCost == null) {
            try {
                ivjTxtSupplierCost = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtSupplierCost.setName("TxtSupplierCost");
                ivjTxtSupplierCost.setBounds(168, 47, 228, 20);
                ivjTxtSupplierCost.setEditable(false);
                ivjTxtSupplierCost.setEnabled(true);
                // user code begin {1}
                ivjTxtSupplierCost.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSupplierCost;
    }

    /**
     * Return the TxtSupportCostDistribution property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtSupplierCostDistribution() {
        if (ivjTxtSupplierCostDistribution == null) {
            try {
                ivjTxtSupplierCostDistribution = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtSupplierCostDistribution.setName("TxtSupplierCostDistribution");
                ivjTxtSupplierCostDistribution.setBounds(403, 76, 61, 20);
                ivjTxtSupplierCostDistribution.setEditable(false);
                ivjTxtSupplierCostDistribution.setEnabled(true);
                // user code begin {1}
                ivjTxtSupplierCostDistribution.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSupplierCostDistribution;
    }

    /**
     * Return the TxtSupplier property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSupplierName() {
        if (ivjTxtSupplierName == null) {
            try {
                ivjTxtSupplierName = new javax.swing.JTextField();
                ivjTxtSupplierName.setName("TxtSupplierName");
                ivjTxtSupplierName.setBounds(168, 20, 228, 20);
                ivjTxtSupplierName.setEditable(false);
                // user code begin {1}
                ivjTxtSupplierName.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSupplierName;
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
        getBtnChooseSupplier().addActionListener(ivjEventHandler);
        getSimpleEditorPanel1().addSimpleEditorPanelListener(ivjEventHandler);
        getCbxDegree().addItemListener(ivjEventHandler);
        getSliClient().addMouseListener(ivjEventHandler);
        getChxVariant().addItemListener(ivjEventHandler);
        getTxtClientDistribution().addPropertyChangeListener(ivjEventHandler);
        connPtoP2SetTarget();
        connPtoP3SetTarget();
        connPtoP1SetTarget();
        connPtoP4SetTarget();
        connPtoP7SetTarget();
        connPtoP5SetTarget();
        connPtoP6SetTarget();
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
            setName("DependencyDetailView");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(518, 489);
            setTitle("Abhaengigkeit");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        setIconImage(ResourceManager.getImageIcon(ModelUtility.class, "Dependency.png").getImage());
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(Dependency.class));
        getPnlStandardToolbar().setObjects(getObjects());
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param event A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent event) {
        int stop = 0;
        /*
         * if (event.getPropertyName().equals("driver")) { getPnlCostDriver().showDriver(); }
         */
    }

    /**
     * Redo the last undoing changes of an Object represented by this GUI.
     */
    @Override
    public void redoObject() {
    }

    /**
     * Save Dependency from Client-side view.
     */
    @Override
    public void saveObject() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    LauncherView.getInstance().mniSaveFile();

                    if (caller != null) {
                        // TODO Patch: update by PropertyChange
                        ((ServiceDetailView) caller).refreshCosts();
                    }

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
     * @param object Example Code: <code> try { if ((object != null) && object.equals(getObject())) { return; } if (getObject() !=null) {
     *     getObject().removeChangeListener(getConsistencyController()); } ((DbObject)object).refresh(true); setObject(object); object.addChangeListener(getconsistencyController()); } catch(Exception
     *     e) { handleException(e); }
     *     </code>
     */
    @Override
    public void setCurrentObject(java.lang.Object object) {
        try {
            if (getObject() != null) {
                getObject().removePropertyChangeListener(this);
            }
            setObject(null);
            DbObjectServer server = ((Dependency) object).getObjectServer();
            JComboBoxUtility.initComboBox(getCbxCurrencySupplier(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyDistribution(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxDegree(), server.retrieveCodes(org.tcotool.model.SupplierInfluence.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()), JComboBoxUtility.SORT_KEEP_ORDER);
            setObject((org.tcotool.model.Dependency) object);

            getTxtClientName().setText(client.getName());
            if (getObject().getSupplierId() != null) {
                getBtnChooseSupplier().setEnabled(false);
                supplier = LauncherView.getInstance().getUtility().findSupplier(getObject());
                getTxtSupplierName().setText(supplier.getName());
                calcDistribution();
            }

            getObject().addPropertyChangeListener(getConsistencyController());

            // ((DbChangeableBean)object).addPropertyChangeListener(this);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Set the Object to a new value.
     *
     * @param newValue org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void setObject(org.tcotool.model.Dependency newValue) {
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
                connPtoP2SetTarget();
                connPtoP3SetTarget();
                connPtoP1SetTarget();
                connPtoP4SetTarget();
                connPtoP7SetTarget();
                connPtoP5SetTarget();
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
