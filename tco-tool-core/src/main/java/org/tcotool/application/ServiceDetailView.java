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
import ch.softenvironment.jomm.mvc.controller.DbTableModel;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.ParserCSV;
import ch.softenvironment.view.BaseFrame;
import ch.softenvironment.view.CommonUserAccess;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import ch.softenvironment.view.table.NumberTableCellRenderer;
import java.util.EventObject;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.tcotool.model.CostCentre;
import org.tcotool.model.CostDriver;
import org.tcotool.model.Currency;
import org.tcotool.model.Dependency;
import org.tcotool.model.Responsibility;
import org.tcotool.model.Service;
import org.tcotool.model.ServiceCategory;
import org.tcotool.model.TcoPackage;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.Formatter;
import org.tcotool.tools.ModelUtility;

/**
 * DetailView of a Service.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class ServiceDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView,
    ch.softenvironment.view.ListMenuChoice, java.beans.PropertyChangeListener {

    private final Formatter evaluator = new Formatter(LauncherView.getInstance().getUtility()); // must be global -> @see
    // #refresh*()
    private double currentLocalSum = -1;
    private final java.text.NumberFormat af = AmountFormat.getAmountInstance(LauncherView.getInstance().getSettings().getPlattformLocale());
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjPnlCostDriver = null;
    private javax.swing.JPanel ivjPnlDetail = null;
    private javax.swing.JComboBox ivjCbxCostCentre = null;
    private javax.swing.JLabel ivjLblCostCentre = null;
    private javax.swing.JScrollPane ivjScpProducts = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private Service ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private javax.swing.JTable ivjTblServices = null;
    private javax.swing.JLabel ivjLblMultitude = null;
    private javax.swing.JSeparator ivjJSeparator1 = null;
    private javax.swing.JPopupMenu ivjMnpDependency = null;
    private javax.swing.JScrollPane ivjScpCostDriver = null;
    private javax.swing.JTable ivjTblCostDriver = null;
    private javax.swing.JSeparator ivjJSeparator11 = null;
    private javax.swing.JPopupMenu ivjMnpDriver = null;
    private ch.softenvironment.view.SimpleEditorPanel ivjPnlNote = null;
    private javax.swing.JMenuItem ivjMniCopyDriver = null;
    private javax.swing.JMenuItem ivjMniNewDependency = null;
    private javax.swing.JMenuItem ivjMniRemoveDependency = null;
    private javax.swing.JMenuItem ivjMniRemoveDriver = null;
    private javax.swing.JMenuItem ivjMniChangeDependency = null;
    private javax.swing.JMenuItem ivjMniChangeDriver = null;
    private boolean ivjConnPtoP7Aligning = false;
    private javax.swing.JLabel ivjLblName = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtMultitude = null;
    private javax.swing.JTextField ivjTxtName = null;
    private javax.swing.JTextField ivjTxtSumLocal = null;
    private javax.swing.JMenuItem ivjMniNewDriver = null;
    private javax.swing.JComboBox ivjCbxCurrencyFact = null;
    private javax.swing.JComboBox ivjCbxCurrencyResources = null;
    private javax.swing.JTextField ivjTxtSumFact = null;
    private javax.swing.JTextField ivjTxtSumResources = null;
    private javax.swing.JLabel ivjLblCategory = null;
    private javax.swing.JComboBox ivjCbxCategory = null;
    private javax.swing.JPanel ivjPnlDependencies = null;
    private javax.swing.JComboBox ivjCbxCurrencyLocal = null;
    private javax.swing.JComboBox ivjCbxCurrencyAll = null;
    private javax.swing.JComboBox ivjCbxCurrencyDependency = null;
    private javax.swing.JTextField ivjTxtSumAll = null;
    private javax.swing.JTextField ivjTxtSumDependency = null;
    private javax.swing.JPanel ivjPnlCostDriverTotal = null;
    private javax.swing.JComboBox ivjCbxResponsibility = null;
    private javax.swing.JLabel ivjLblResponsibility = null;
    private boolean ivjConnPtoP13Aligning = false;
    private boolean ivjConnPtoP1Aligning = false;
    private boolean ivjConnPtoP2Aligning = false;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JLabel ivjLblDependencySum = null;
    private javax.swing.JLabel ivjLblTotalFactCost = null;
    private javax.swing.JLabel ivjLblTotalPersonalCost = null;
    private javax.swing.JLabel ivjLblTotalWithDependencyCost = null;
    private javax.swing.JLabel ivjLblTotalWithoutDependencyCost = null;
    private javax.swing.JTabbedPane ivjTbpMain = null;
    private javax.swing.JPanel ivjPnlCodes = null;
    private javax.swing.JPanel ivjPnlTotal = null;
    private javax.swing.JPanel ivjPnlDependencyTotal = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ActionListener,
        java.awt.event.ItemListener, java.awt.event.KeyListener, java.awt.event.MouseListener, java.beans.PropertyChangeListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ServiceDetailView.this.getMniNewDependency()) {
                connEtoC4(e);
            }
            if (e.getSource() == ServiceDetailView.this.getMniChangeDependency()) {
                connEtoC5(e);
            }
            if (e.getSource() == ServiceDetailView.this.getMniRemoveDependency()) {
                connEtoC6(e);
            }
            if (e.getSource() == ServiceDetailView.this.getMniChangeDriver()) {
                connEtoC15(e);
            }
            if (e.getSource() == ServiceDetailView.this.getMniRemoveDriver()) {
                connEtoC16(e);
            }
            if (e.getSource() == ServiceDetailView.this.getMniNewDriver()) {
                connEtoC21(e);
            }
        }

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == ServiceDetailView.this.getCbxCostCentre()) {
                connPtoP7SetSource();
            }
            if (e.getSource() == ServiceDetailView.this.getCbxCategory()) {
                connPtoP3SetSource();
            }
            if (e.getSource() == ServiceDetailView.this.getCbxResponsibility()) {
                connPtoP13SetSource();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == ServiceDetailView.this.getTxtName()) {
                connPtoP2SetSource();
            }
            if (e.getSource() == ServiceDetailView.this.getTxtMultitude()) {
                connPtoP4SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
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
            if (e.getSource() == ServiceDetailView.this.getTblCostDriver()) {
                connEtoC8(e);
            }
            if (e.getSource() == ServiceDetailView.this.getScpCostDriver()) {
                connEtoC9(e);
            }
            if (e.getSource() == ServiceDetailView.this.getTblServices()) {
                connEtoC10(e);
            }
            if (e.getSource() == ServiceDetailView.this.getScpProducts()) {
                connEtoC11(e);
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.getSource() == ServiceDetailView.this.getTblServices()) {
                connEtoC2(e);
            }
            if (e.getSource() == ServiceDetailView.this.getScpProducts()) {
                connEtoC3(e);
            }
            if (e.getSource() == ServiceDetailView.this.getTblCostDriver()) {
                connEtoC17(e);
            }
            if (e.getSource() == ServiceDetailView.this.getScpCostDriver()) {
                connEtoC18(e);
            }
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == ServiceDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == ServiceDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == ServiceDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == ServiceDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == ServiceDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == ServiceDetailView.this.getObject() && (evt.getPropertyName().equals("name"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == ServiceDetailView.this.getObject() && (evt.getPropertyName().equals("costCentre"))) {
                connPtoP7SetTarget();
            }
            if (evt.getSource() == ServiceDetailView.this.getObject() && (evt.getPropertyName().equals("category"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == ServiceDetailView.this.getObject() && (evt.getPropertyName().equals("responsibility"))) {
                connPtoP13SetTarget();
            }
            if (evt.getSource() == ServiceDetailView.this.getObject() && (evt.getPropertyName().equals("multitude"))) {
                connPtoP4SetTarget();
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
            if (newEvent.getSource() == ServiceDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ServiceDetailView.this.getPnlNote()) {
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
    public ServiceDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public ServiceDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects) {
        super(viewOptions, objects);
        initialize();
    }

    @Override
    public void adaptUserAction(EventObject event, Object control) {
        if (control != null) {
            if (control.equals(getMnpDriver())) {
                // boolean selected = getTblCostDriver().getSelectedRowCount()
                // == 1;
                getMniChangeDriver().setEnabled(getTblCostDriver().getSelectedRowCount() == 1);
                getMniRemoveDriver().setEnabled(getTblCostDriver().getSelectedRowCount() > 0);
            } else if (control.equals(getMnpDependency())) {
                getMniChangeDependency().setEnabled(getTblServices().getSelectedRowCount() == 1);
                getMniRemoveDependency().setEnabled(getTblServices().getSelectedRowCount() > 0);
            }
        }
    }

    /**
     * Assign a set of aggregates given in objects.
     *
     * @param objects
     */
    @Override
    public void assignObjects(java.util.List objects) {
        try {
            // assign CostDriver
            // assignOneToMany(getObject(), "driver", objects,
            // getConsistencyController());

            // assign Dependency
            // assignAssociations(Service.class, getObject(), "supplierId",
            // objects, getConsistencyController());
            // refreshDependencies();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Do anything at DoubleClick-Event for e.g. open selected Object(s) in a JTable.
     *
     * @see BaseFrame#genericPopupDisplay(..)
     */
    @Override
    public void changeObjects(java.lang.Object source) {
        try {
            if (source.equals(getMniChangeDriver()) || source.equals(getTblCostDriver())) {
                java.util.List objects = DbTableModel.getSelectedItems(getTblCostDriver());
                if (!getViewOptions().getViewManager().activateView(objects)) {
                    openCostDriver(objects);
                }
            } else if (source.equals(getMniChangeDependency()) || source.equals(getTblServices())) {
                java.util.List objects = DbTableModel.getSelectedItems(getTblServices());
                if (!getViewOptions().getViewManager().activateView(objects)) {
                    openDependency(objects);
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
     * connEtoC10: (TblServices.mouse.mousePressed(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC10(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDependency());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC11: (ScpProducts.mouse.mousePressed(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC11(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDependency());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC15: (MniOpen1.action.actionPerformed(java.awt.event.ActionEvent) --> ServiceDetailView.changeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC15(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getMniChangeDriver());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC16: (MniRemove1.action.actionPerformed(java.awt.event.ActionEvent) --> ServiceDetailView.removeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC16(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.removeObjects(getMniRemoveDriver());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC17: (TblCostDriver.mouse.mouseReleased(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC17(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDriver());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC18: (ScpCostDriver.mouse.mouseReleased(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC18(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDriver());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (TblServices.mouse.mouseReleased(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDependency());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC21: (MniNewDriver.action.actionPerformed(java.awt.event.ActionEvent) --> ServiceDetailView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC21(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniNewDriver());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (ScpProducts.mouse.mouseReleased(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDependency());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC4: (MniNew.action.actionPerformed(java.awt.event.ActionEvent) --> ServiceDetailView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniNewDependency());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5: (MniOpen.action.actionPerformed(java.awt.event.ActionEvent) --> ServiceDetailView.changeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getMniChangeDependency());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC6: (MniRemove.action.actionPerformed(java.awt.event.ActionEvent) --> ServiceDetailView.removeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.removeObjects(getMniRemoveDependency());
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
     * connEtoC8: (TblCostDriver.mouse.mousePressed(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDriver());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC9: (ScpCostDriver.mouse.mousePressed(java.awt.event.MouseEvent) --> ServiceDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax .swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC9(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpDriver());
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
     * connPtoP10SetTarget: (CbxCurrencyFact.selectedItem <--> CbxCurrencyLocal.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP10SetTarget() {
        /* Set the target from the source */
        try {
            getCbxCurrencyLocal().setSelectedItem(getCbxCurrencyFact().getSelectedItem());
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP11SetTarget: (CbxCurrencyFact.selectedItem <--> CbxCurrencyDependency.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP11SetTarget() {
        /* Set the target from the source */
        try {
            getCbxCurrencyAll().setSelectedItem(getCbxCurrencyFact().getSelectedItem());
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP12SetTarget: (CbxCurrencyFact.selectedItem <--> CbxCurrencyFact1.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP12SetTarget() {
        /* Set the target from the source */
        try {
            getCbxCurrencyDependency().setSelectedItem(getCbxCurrencyFact().getSelectedItem());
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP13SetSource: (Object.responsibility <--> CbxResponsibility.selectedItem)
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
                    getObject().setResponsibility((org.tcotool.model.Responsibility) getCbxResponsibility().getSelectedItem());
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
     * connPtoP13SetTarget: (Object.responsibility <--> CbxResponsibility.selectedItem)
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
                    getCbxResponsibility().setSelectedItem(getObject().getResponsibility());
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
     * connPtoP14SetTarget: (Object.mark <--> PnlStatusBar.mark)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP14SetTarget() {
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
     * connPtoP2SetSource: (Object.name <--> TxtName.text)
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
     * connPtoP3SetSource: (Object.category <--> CbxCategory.selectedItem)
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
                    getObject().setCategory((org.tcotool.model.ServiceCategory) getCbxCategory().getSelectedItem());
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
     * connPtoP3SetTarget: (Object.category <--> CbxCategory.selectedItem)
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
                    getCbxCategory().setSelectedItem(getObject().getCategory());
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
            if (ivjConnPtoP4Aligning == false) {
                // user code begin {1}
                // user code end
                ivjConnPtoP4Aligning = true;
                if ((getObject() != null)) {
                    getObject().setMultitude(new java.lang.Double(getTxtMultitude().getText()));
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
     * connPtoP4SetTarget: (Object.multitude <--> TxtMultitude.text)
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
     * connPtoP7SetSource: (Object.costCentre <--> CbxCostCentre.selectedItem)
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
                    getObject().setCostCentre((org.tcotool.model.CostCentre) getCbxCostCentre().getSelectedItem());
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
     * connPtoP7SetTarget: (Object.costCentre <--> CbxCostCentre.selectedItem)
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
                    getCbxCostCentre().setSelectedItem(getObject().getCostCentre());
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
     * connPtoP8SetTarget: (CbxCurrencyFact.selectedItem <--> CbxCurrencyResources.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP8SetTarget() {
        /* Set the target from the source */
        try {
            getCbxCurrencyResources().setSelectedItem(getCbxCurrencyFact().getSelectedItem());
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
    @Override
    public void copyObject(java.lang.Object source) {
    }

    /**
     * @see #openCostDriver()
     */
    @Override
    public void dispose() {
        registerPropertyChangeListener(getObject().getDriver(), false);
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
     * Return the CbxCategory property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCategory() {
        if (ivjCbxCategory == null) {
            try {
                ivjCbxCategory = new javax.swing.JComboBox();
                ivjCbxCategory.setName("CbxCategory");
                ivjCbxCategory.setBounds(155, 10, 471, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCategory;
    }

    /**
     * Return the CbxCostCentre property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCostCentre() {
        if (ivjCbxCostCentre == null) {
            try {
                ivjCbxCostCentre = new javax.swing.JComboBox();
                ivjCbxCostCentre.setName("CbxCostCentre");
                ivjCbxCostCentre.setBounds(155, 59, 471, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCostCentre;
    }

    /**
     * Return the CbxCurrencyDependency property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyAll() {
        if (ivjCbxCurrencyAll == null) {
            try {
                ivjCbxCurrencyAll = new javax.swing.JComboBox();
                ivjCbxCurrencyAll.setName("CbxCurrencyAll");
                ivjCbxCurrencyAll.setBounds(490, 36, 61, 23);
                ivjCbxCurrencyAll.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyAll;
    }

    /**
     * Return the CbxCurrencyFact1 property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyDependency() {
        if (ivjCbxCurrencyDependency == null) {
            try {
                ivjCbxCurrencyDependency = new javax.swing.JComboBox();
                ivjCbxCurrencyDependency.setName("CbxCurrencyDependency");
                ivjCbxCurrencyDependency.setBounds(476, 13, 61, 23);
                ivjCbxCurrencyDependency.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyDependency;
    }

    /**
     * Return the CbxCurrencyFact property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyFact() {
        if (ivjCbxCurrencyFact == null) {
            try {
                ivjCbxCurrencyFact = new javax.swing.JComboBox();
                ivjCbxCurrencyFact.setName("CbxCurrencyFact");
                ivjCbxCurrencyFact.setBounds(481, 7, 61, 23);
                ivjCbxCurrencyFact.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyFact;
    }

    /**
     * Return the CbxCurrencyLocal property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyLocal() {
        if (ivjCbxCurrencyLocal == null) {
            try {
                ivjCbxCurrencyLocal = new javax.swing.JComboBox();
                ivjCbxCurrencyLocal.setName("CbxCurrencyLocal");
                ivjCbxCurrencyLocal.setBounds(490, 9, 61, 23);
                ivjCbxCurrencyLocal.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyLocal;
    }

    /**
     * Return the CbxCurrencyResources property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyResources() {
        if (ivjCbxCurrencyResources == null) {
            try {
                ivjCbxCurrencyResources = new javax.swing.JComboBox();
                ivjCbxCurrencyResources.setName("CbxCurrencyResources");
                ivjCbxCurrencyResources.setBounds(481, 31, 61, 23);
                ivjCbxCurrencyResources.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyResources;
    }

    /**
     * Return the CbxResponsibility property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxResponsibility() {
        if (ivjCbxResponsibility == null) {
            try {
                ivjCbxResponsibility = new javax.swing.JComboBox();
                ivjCbxResponsibility.setName("CbxResponsibility");
                ivjCbxResponsibility.setBounds(155, 34, 471, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxResponsibility;
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
                ivjJPanel1.setLayout(new java.awt.GridBagLayout());

                java.awt.GridBagConstraints constraintsLblName = new java.awt.GridBagConstraints();
                constraintsLblName.gridx = 0;
                constraintsLblName.gridy = 0;
                constraintsLblName.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblName.ipadx = 55;
                constraintsLblName.insets = new java.awt.Insets(16, 11, 5, 10);
                getJPanel1().add(getLblName(), constraintsLblName);

                java.awt.GridBagConstraints constraintsTxtName = new java.awt.GridBagConstraints();
                constraintsTxtName.gridx = 1;
                constraintsTxtName.gridy = 0;
                constraintsTxtName.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsTxtName.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTxtName.weightx = 1.0;
                constraintsTxtName.ipadx = 422;
                constraintsTxtName.insets = new java.awt.Insets(13, 10, 2, 30);
                getJPanel1().add(getTxtName(), constraintsTxtName);

                java.awt.GridBagConstraints constraintsTbpMain = new java.awt.GridBagConstraints();
                constraintsTbpMain.gridx = 0;
                constraintsTbpMain.gridy = 2;
                constraintsTbpMain.gridwidth = 2;
                constraintsTbpMain.fill = java.awt.GridBagConstraints.BOTH;
                constraintsTbpMain.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTbpMain.weightx = 1.0;
                constraintsTbpMain.weighty = 1.0;
                constraintsTbpMain.ipadx = -3;
                constraintsTbpMain.ipady = -320;
                constraintsTbpMain.insets = new java.awt.Insets(5, 11, 13, 7);
                getJPanel1().add(getTbpMain(), constraintsTbpMain);

                java.awt.GridBagConstraints constraintsLblMultitude = new java.awt.GridBagConstraints();
                constraintsLblMultitude.gridx = 0;
                constraintsLblMultitude.gridy = 1;
                constraintsLblMultitude.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblMultitude.ipadx = 90;
                constraintsLblMultitude.insets = new java.awt.Insets(6, 11, 3, 10);
                getJPanel1().add(getLblMultitude(), constraintsLblMultitude);

                java.awt.GridBagConstraints constraintsTxtMultitude = new java.awt.GridBagConstraints();
                constraintsTxtMultitude.gridx = 1;
                constraintsTxtMultitude.gridy = 1;
                constraintsTxtMultitude.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsTxtMultitude.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTxtMultitude.weightx = 1.0;
                constraintsTxtMultitude.ipadx = 126;
                constraintsTxtMultitude.insets = new java.awt.Insets(2, 10, 1, 381);
                getJPanel1().add(getTxtMultitude(), constraintsTxtMultitude);
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
     * Return the JSeparator1 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator1() {
        if (ivjJSeparator1 == null) {
            try {
                ivjJSeparator1 = new javax.swing.JSeparator();
                ivjJSeparator1.setName("JSeparator1");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSeparator1;
    }

    /**
     * Return the JSeparator11 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator11() {
        if (ivjJSeparator11 == null) {
            try {
                ivjJSeparator11 = new javax.swing.JSeparator();
                ivjJSeparator11.setName("JSeparator11");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSeparator11;
    }

    /**
     * Return the LblCostCentre1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCategory() {
        if (ivjLblCategory == null) {
            try {
                ivjLblCategory = new javax.swing.JLabel();
                ivjLblCategory.setName("LblCategory");
                ivjLblCategory.setText("Kategorie:");
                ivjLblCategory.setBounds(10, 14, 139, 14);
                // user code begin {1}
                ivjLblCategory.setText(ModelUtility.getTypeString(ServiceCategory.class) + ":" /*
                 * getResourceString ( "LblCategory_text" )
                 */);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCategory;
    }

    /**
     * Return the LblCostCentre property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCostCentre() {
        if (ivjLblCostCentre == null) {
            try {
                ivjLblCostCentre = new javax.swing.JLabel();
                ivjLblCostCentre.setName("LblCostCentre");
                ivjLblCostCentre.setText("Kostenstelle:");
                ivjLblCostCentre.setBounds(10, 62, 139, 14);
                // user code begin {1}
                ivjLblCostCentre.setText(ModelUtility.getTypeString(CostCentre.class) + ":" /*
                 * getResourceString ( "LblCostCentre_text" )
                 */);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCostCentre;
    }

    /**
     * Return the JLabel321 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblDependencySum() {
        if (ivjLblDependencySum == null) {
            try {
                ivjLblDependencySum = new javax.swing.JLabel();
                ivjLblDependencySum.setName("LblDependencySum");
                ivjLblDependencySum.setToolTipText("Summe der Abhängigkeitskosten für diesen Dienst");
                ivjLblDependencySum.setText("Total Abhängigkeiten:");
                ivjLblDependencySum.setBounds(11, 19, 230, 14);
                // user code begin {1}
                ivjLblDependencySum.setText(getResourceString("LblDependencySum_text"));
                ivjLblDependencySum.setToolTipText(getResourceString("LblDependencySum_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblDependencySum;
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
                // user code begin {1}
                ivjLblMultitude.setText(getResourceString("LblMultitude_text"));
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
                // user code begin {1}
                ivjLblName.setText(getResourceString("LblName_text"));
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
     * Return the LblResponsibility property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblResponsibility() {
        if (ivjLblResponsibility == null) {
            try {
                ivjLblResponsibility = new javax.swing.JLabel();
                ivjLblResponsibility.setName("LblResponsibility");
                ivjLblResponsibility.setToolTipText("Dienst-Verantwortlichkeiten");
                ivjLblResponsibility.setText("Zuständigkeit:");
                ivjLblResponsibility.setBounds(10, 37, 139, 14);
                // user code begin {1}
                ivjLblResponsibility.setText(ModelUtility.getTypeString(Responsibility.class) + ":" /*
                 * getResourceString ( "LblResponsibility_text" )
                 */);
                ivjLblResponsibility.setToolTipText(getResourceString("LblResponsibility_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblResponsibility;
    }

    /**
     * Return the JLabel32 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotalFactCost() {
        if (ivjLblTotalFactCost == null) {
            try {
                ivjLblTotalFactCost = new javax.swing.JLabel();
                ivjLblTotalFactCost.setName("LblTotalFactCost");
                ivjLblTotalFactCost.setToolTipText("Summe der Sachkosten nach Menge");
                ivjLblTotalFactCost.setText("Total Sachkosten:");
                ivjLblTotalFactCost.setBounds(7, 11, 309, 14);
                // user code begin {1}
                ivjLblTotalFactCost.setText(getResourceString("LblTotalFactCost_text"));
                ivjLblTotalFactCost.setToolTipText(getResourceString("LblTotalFactCost_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTotalFactCost;
    }

    /**
     * Return the JLabel311 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotalPersonalCost() {
        if (ivjLblTotalPersonalCost == null) {
            try {
                ivjLblTotalPersonalCost = new javax.swing.JLabel();
                ivjLblTotalPersonalCost.setName("LblTotalPersonalCost");
                ivjLblTotalPersonalCost.setToolTipText("Summe des Personalaufwandes nach Menge");
                ivjLblTotalPersonalCost.setText("Total Personalkosten:");
                ivjLblTotalPersonalCost.setBounds(7, 33, 309, 14);
                // user code begin {1}
                ivjLblTotalPersonalCost.setToolTipText(getResourceString("LblTotalPersonalCost_toolTipText"));
                ivjLblTotalPersonalCost.setText(getResourceString("LblTotalPersonalCost_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTotalPersonalCost;
    }

    /**
     * Return the JLabel31 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotalWithDependencyCost() {
        if (ivjLblTotalWithDependencyCost == null) {
            try {
                ivjLblTotalWithDependencyCost = new javax.swing.JLabel();
                ivjLblTotalWithDependencyCost.setName("LblTotalWithDependencyCost");
                ivjLblTotalWithDependencyCost.setToolTipText("Summe (lokal) + Dienstabhängigkeitseinfluss");
                ivjLblTotalWithDependencyCost.setText("Total (mit Abhängigkeiten):");
                ivjLblTotalWithDependencyCost.setBounds(8, 40, 320, 14);
                ivjLblTotalWithDependencyCost.setForeground(java.awt.Color.red);
                // user code begin {1}
                ivjLblTotalWithDependencyCost.setText(getResourceString("LblTotalWithDependencyCost_text"));
                ivjLblTotalWithDependencyCost.setToolTipText(getResourceString("LblTotalWithDependencyCost_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTotalWithDependencyCost;
    }

    /**
     * Return the JLabel3 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotalWithoutDependencyCost() {
        if (ivjLblTotalWithoutDependencyCost == null) {
            try {
                ivjLblTotalWithoutDependencyCost = new javax.swing.JLabel();
                ivjLblTotalWithoutDependencyCost.setName("LblTotalWithoutDependencyCost");
                ivjLblTotalWithoutDependencyCost.setToolTipText("Total der Kostentreiber nach Menge ohne Abhängigkeit");
                ivjLblTotalWithoutDependencyCost.setText("Total (ohne Abhängigkeiten):");
                ivjLblTotalWithoutDependencyCost.setBounds(8, 16, 320, 14);
                ivjLblTotalWithoutDependencyCost.setForeground(java.awt.Color.red);
                // user code begin {1}
                ivjLblTotalWithoutDependencyCost.setText(getResourceString("LblTotalWithoutDependencyCost_text"));
                ivjLblTotalWithoutDependencyCost.setToolTipText(getResourceString("LblTotalWithoutDependencyCost_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTotalWithoutDependencyCost;
    }

    /**
     * Return the MniOpen property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniChangeDependency() {
        if (ivjMniChangeDependency == null) {
            try {
                ivjMniChangeDependency = new javax.swing.JMenuItem();
                ivjMniChangeDependency.setName("MniChangeDependency");
                ivjMniChangeDependency.setText("Öffnen...");
                ivjMniChangeDependency.setEnabled(false);
                // user code begin {1}
                ivjMniChangeDependency.setText(ch.softenvironment.view.CommonUserAccess.getMniEditChangeWindowText());
                ivjMniChangeDependency.setIcon(CommonUserAccess.getIconChange());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniChangeDependency;
    }

    /**
     * Return the MniOpen1 property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniChangeDriver() {
        if (ivjMniChangeDriver == null) {
            try {
                ivjMniChangeDriver = new javax.swing.JMenuItem();
                ivjMniChangeDriver.setName("MniChangeDriver");
                ivjMniChangeDriver.setText("Öffnen...");
                ivjMniChangeDriver.setEnabled(false);
                // user code begin {1}
                ivjMniChangeDriver.setText(ch.softenvironment.view.CommonUserAccess.getMniEditChangeWindowText());
                ivjMniChangeDriver.setIcon(CommonUserAccess.getIconChange());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniChangeDriver;
    }

    /**
     * Return the MniCopy property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniCopyDriver() {
        if (ivjMniCopyDriver == null) {
            try {
                ivjMniCopyDriver = new javax.swing.JMenuItem();
                ivjMniCopyDriver.setName("MniCopyDriver");
                ivjMniCopyDriver.setText("Kopieren...");
                ivjMniCopyDriver.setEnabled(false);
                // user code begin {1}
                ivjMniCopyDriver.setText(ch.softenvironment.view.CommonUserAccess.getMniEditCopyText());
                ivjMniCopyDriver.setIcon(CommonUserAccess.getIconCopy());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniCopyDriver;
    }

    /**
     * Return the MniNew property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniNewDependency() {
        if (ivjMniNewDependency == null) {
            try {
                ivjMniNewDependency = new javax.swing.JMenuItem();
                ivjMniNewDependency.setName("MniNewDependency");
                ivjMniNewDependency.setText("Neu...");
                // user code begin {1}
                ivjMniNewDependency.setText(ch.softenvironment.view.CommonUserAccess.getMniFileNewWindowText());
                ivjMniNewDependency.setIcon(CommonUserAccess.getIconNew());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniNewDependency;
    }

    /**
     * Return the MniNewDriver property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniNewDriver() {
        if (ivjMniNewDriver == null) {
            try {
                ivjMniNewDriver = new javax.swing.JMenuItem();
                ivjMniNewDriver.setName("MniNewDriver");
                ivjMniNewDriver.setText("Neu");
                ivjMniNewDriver.setEnabled(true);
                // user code begin {1}
                ivjMniNewDriver.setText(ch.softenvironment.view.CommonUserAccess.getMniFileNewWindowText());
                ivjMniNewDriver.setIcon(CommonUserAccess.getIconNew());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniNewDriver;
    }

    /**
     * Return the MniRemove property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniRemoveDependency() {
        if (ivjMniRemoveDependency == null) {
            try {
                ivjMniRemoveDependency = new javax.swing.JMenuItem();
                ivjMniRemoveDependency.setName("MniRemoveDependency");
                ivjMniRemoveDependency.setText("Löschen");
                ivjMniRemoveDependency.setEnabled(false);
                // user code begin {1}
                ivjMniRemoveDependency.setText(ch.softenvironment.view.CommonUserAccess.getMniEditRemoveText());
                ivjMniRemoveDependency.setIcon(CommonUserAccess.getIconRemove());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniRemoveDependency;
    }

    /**
     * Return the MniRemove1 property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniRemoveDriver() {
        if (ivjMniRemoveDriver == null) {
            try {
                ivjMniRemoveDriver = new javax.swing.JMenuItem();
                ivjMniRemoveDriver.setName("MniRemoveDriver");
                ivjMniRemoveDriver.setText("Löschen");
                ivjMniRemoveDriver.setEnabled(false);
                // user code begin {1}
                ivjMniRemoveDriver.setText(ch.softenvironment.view.CommonUserAccess.getMniEditRemoveText());
                ivjMniRemoveDriver.setIcon(CommonUserAccess.getIconRemove());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniRemoveDriver;
    }

    /**
     * Return the MnpDependency property value.
     *
     * @return javax.swing.JPopupMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPopupMenu getMnpDependency() {
        if (ivjMnpDependency == null) {
            try {
                ivjMnpDependency = new javax.swing.JPopupMenu();
                ivjMnpDependency.setName("MnpDependency");
                ivjMnpDependency.add(getMniNewDependency());
                ivjMnpDependency.add(getMniChangeDependency());
                ivjMnpDependency.add(getJSeparator1());
                ivjMnpDependency.add(getMniRemoveDependency());
                // user code begin {1}
                ivjMnpDependency.add(new JSeparator());
                JMenuItem item = new JMenuItem();
                item.setText(CommonUserAccess.getMniFileExportText());
                item.setToolTipText(ResourceManager.getResource(BaseFrame.class, "MniExportTableData_toolTip"));
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        // call after extension-points are realized of extension
                        exportTableData(getTblServices(), ParserCSV.DEFAULT_SEPARATOR);
                    }
                });
                ivjMnpDependency.add(item);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnpDependency;
    }

    /**
     * Return the MnpDriver property value.
     *
     * @return javax.swing.JPopupMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPopupMenu getMnpDriver() {
        if (ivjMnpDriver == null) {
            try {
                ivjMnpDriver = new javax.swing.JPopupMenu();
                ivjMnpDriver.setName("MnpDriver");
                ivjMnpDriver.add(getMniNewDriver());
                ivjMnpDriver.add(getMniChangeDriver());
                ivjMnpDriver.add(getMniCopyDriver());
                ivjMnpDriver.add(getJSeparator11());
                ivjMnpDriver.add(getMniRemoveDriver());
                // user code begin {1}
                ivjMnpDriver.add(new JSeparator());
                JMenuItem item = new JMenuItem();
                item.setText(CommonUserAccess.getMniFileExportText());
                item.setToolTipText(ResourceManager.getResource(BaseFrame.class, "MniExportTableData_toolTip"));
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        // call after extension-points are realized of extension
                        exportTableData(getTblCostDriver(), ParserCSV.DEFAULT_SEPARATOR);
                    }
                });
                ivjMnpDriver.add(item);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnpDriver;
    }

    /**
     * Return the Object property value.
     *
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.Service getObject() {
        // user code begin {1}
        // user code end
        return ivjObject;
    }

    /**
     * Return the PnlCodes property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCodes() {
        if (ivjPnlCodes == null) {
            try {
                ivjPnlCodes = new javax.swing.JPanel();
                ivjPnlCodes.setName("PnlCodes");
                ivjPnlCodes.setPreferredSize(new java.awt.Dimension(0, 90));
                ivjPnlCodes.setLayout(null);
                getPnlCodes().add(getLblCategory(), getLblCategory().getName());
                getPnlCodes().add(getLblResponsibility(), getLblResponsibility().getName());
                getPnlCodes().add(getLblCostCentre(), getLblCostCentre().getName());
                getPnlCodes().add(getCbxCategory(), getCbxCategory().getName());
                getPnlCodes().add(getCbxResponsibility(), getCbxResponsibility().getName());
                getPnlCodes().add(getCbxCostCentre(), getCbxCostCentre().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCodes;
    }

    /**
     * Return the PnlCostDriver property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCostDriver() {
        if (ivjPnlCostDriver == null) {
            try {
                ivjPnlCostDriver = new javax.swing.JPanel();
                ivjPnlCostDriver.setName("PnlCostDriver");
                ivjPnlCostDriver.setPreferredSize(new java.awt.Dimension(0, 250));
                ivjPnlCostDriver.setLayout(new java.awt.BorderLayout());
                ivjPnlCostDriver.setMinimumSize(new java.awt.Dimension(0, 200));
                ivjPnlCostDriver.setMaximumSize(new java.awt.Dimension(0, 2147483647));
                getPnlCostDriver().add(getScpCostDriver(), "Center");
                getPnlCostDriver().add(getPnlCostDriverTotal(), "South");
                // user code begin {1}
                getPnlCostDriver().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlCostDriver_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCostDriver;
    }

    /**
     * Return the JPanel2 property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCostDriverTotal() {
        if (ivjPnlCostDriverTotal == null) {
            try {
                ivjPnlCostDriverTotal = new javax.swing.JPanel();
                ivjPnlCostDriverTotal.setName("PnlCostDriverTotal");
                ivjPnlCostDriverTotal.setPreferredSize(new java.awt.Dimension(0, 55));
                ivjPnlCostDriverTotal.setLayout(null);
                getPnlCostDriverTotal().add(getLblTotalFactCost(), getLblTotalFactCost().getName());
                getPnlCostDriverTotal().add(getLblTotalPersonalCost(), getLblTotalPersonalCost().getName());
                getPnlCostDriverTotal().add(getTxtSumFact(), getTxtSumFact().getName());
                getPnlCostDriverTotal().add(getTxtSumResources(), getTxtSumResources().getName());
                getPnlCostDriverTotal().add(getCbxCurrencyFact(), getCbxCurrencyFact().getName());
                getPnlCostDriverTotal().add(getCbxCurrencyResources(), getCbxCurrencyResources().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCostDriverTotal;
    }

    /**
     * Return the PnlDependencies property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlDependencies() {
        if (ivjPnlDependencies == null) {
            try {
                ivjPnlDependencies = new javax.swing.JPanel();
                ivjPnlDependencies.setName("PnlDependencies");
                ivjPnlDependencies.setLayout(new java.awt.BorderLayout());
                getPnlDependencies().add(getScpProducts(), "Center");
                getPnlDependencies().add(getPnlDependencyTotal(), "South");
                // user code begin {1}
                /*
                 * getPnlDependencies().setBorder(javax.swing.BorderFactory. createCompoundBorder( javax.swing.BorderFactory.createTitledBorder
                 * (getResourceString("PnlDependencies_text")), javax.swing.BorderFactory.createEmptyBorder(5,5,5,5)));
                 */
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlDependencies;
    }

    /**
     * Return the PnlDependencyTotal property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlDependencyTotal() {
        if (ivjPnlDependencyTotal == null) {
            try {
                ivjPnlDependencyTotal = new javax.swing.JPanel();
                ivjPnlDependencyTotal.setName("PnlDependencyTotal");
                ivjPnlDependencyTotal.setPreferredSize(new java.awt.Dimension(0, 50));
                ivjPnlDependencyTotal.setLayout(null);
                getPnlDependencyTotal().add(getLblDependencySum(), getLblDependencySum().getName());
                getPnlDependencyTotal().add(getTxtSumDependency(), getTxtSumDependency().getName());
                getPnlDependencyTotal().add(getCbxCurrencyDependency(), getCbxCurrencyDependency().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlDependencyTotal;
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
                ivjPnlDetail.setLayout(new java.awt.BorderLayout());
                getPnlDetail().add(getPnlCodes(), "North");
                getPnlDetail().add(getPnlTotal(), "South");
                getPnlDetail().add(getPnlCostDriver(), "Center");
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
     * Return the PnlTotal property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlTotal() {
        if (ivjPnlTotal == null) {
            try {
                ivjPnlTotal = new javax.swing.JPanel();
                ivjPnlTotal.setName("PnlTotal");
                ivjPnlTotal.setPreferredSize(new java.awt.Dimension(0, 70));
                ivjPnlTotal.setLayout(null);
                getPnlTotal().add(getLblTotalWithoutDependencyCost(), getLblTotalWithoutDependencyCost().getName());
                getPnlTotal().add(getLblTotalWithDependencyCost(), getLblTotalWithDependencyCost().getName());
                getPnlTotal().add(getTxtSumLocal(), getTxtSumLocal().getName());
                getPnlTotal().add(getTxtSumAll(), getTxtSumAll().getName());
                getPnlTotal().add(getCbxCurrencyLocal(), getCbxCurrencyLocal().getName());
                getPnlTotal().add(getCbxCurrencyAll(), getCbxCurrencyAll().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlTotal;
    }

    /**
     * Return the ScpCostDriver property value.
     *
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getScpCostDriver() {
        if (ivjScpCostDriver == null) {
            try {
                ivjScpCostDriver = new javax.swing.JScrollPane();
                ivjScpCostDriver.setName("ScpCostDriver");
                ivjScpCostDriver.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjScpCostDriver.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                ivjScpCostDriver.setMinimumSize(new java.awt.Dimension(22, 80));
                getScpCostDriver().setViewportView(getTblCostDriver());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScpCostDriver;
    }

    /**
     * Return the JScrollPane2 property value.
     *
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getScpProducts() {
        if (ivjScpProducts == null) {
            try {
                ivjScpProducts = new javax.swing.JScrollPane();
                ivjScpProducts.setName("ScpProducts");
                ivjScpProducts.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjScpProducts.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                ivjScpProducts.setMinimumSize(new java.awt.Dimension(22, 180));
                getScpProducts().setViewportView(getTblServices());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScpProducts;
    }

    /**
     * Return the TblCostDriver property value.
     *
     * @return javax.swing.JTable
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTable getTblCostDriver() {
        if (ivjTblCostDriver == null) {
            try {
                ivjTblCostDriver = new javax.swing.JTable();
                ivjTblCostDriver.setName("TblCostDriver");
                getScpCostDriver().setColumnHeaderView(ivjTblCostDriver.getTableHeader());
                getScpCostDriver().getViewport().setBackingStoreEnabled(true);
                ivjTblCostDriver.setBounds(0, 0, 200, 200);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTblCostDriver;
    }

    /**
     * Return the ScrollPaneTable1 property value.
     *
     * @return javax.swing.JTable
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTable getTblServices() {
        if (ivjTblServices == null) {
            try {
                ivjTblServices = new javax.swing.JTable();
                ivjTblServices.setName("TblServices");
                getScpProducts().setColumnHeaderView(ivjTblServices.getTableHeader());
                getScpProducts().getViewport().setBackingStoreEnabled(true);
                ivjTblServices.setBounds(0, 0, 200, 200);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTblServices;
    }

    /**
     * Return the JTabbedPane1 property value.
     *
     * @return javax.swing.JTabbedPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTabbedPane getTbpMain() {
        if (ivjTbpMain == null) {
            try {
                ivjTbpMain = new javax.swing.JTabbedPane();
                ivjTbpMain.setName("TbpMain");
                ivjTbpMain.insertTab("Detail", null, getPnlDetail(), null, 0);
                ivjTbpMain.insertTab("Abhängigkeiten", null, getPnlDependencies(), null, 1);
                ivjTbpMain.insertTab("Notiz", null, getPnlNote(), null, 2);
                // user code begin {1}
                ivjTbpMain.setTitleAt(0, getResourceString("PnlDetail_text"));
                ivjTbpMain.setTitleAt(1, getResourceString("PnlDependencies_text"));
                ivjTbpMain.setTitleAt(2, getResourceString("PnlNote_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbpMain;
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
     * Return the TxtSumAll property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSumAll() {
        if (ivjTxtSumAll == null) {
            try {
                ivjTxtSumAll = new javax.swing.JTextField();
                ivjTxtSumAll.setName("TxtSumAll");
                ivjTxtSumAll.setBounds(338, 36, 144, 20);
                ivjTxtSumAll.setEditable(false);
                // user code begin {1}
                ivjTxtSumAll.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumAll;
    }

    /**
     * Return the TxtSumDependency property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSumDependency() {
        if (ivjTxtSumDependency == null) {
            try {
                ivjTxtSumDependency = new javax.swing.JTextField();
                ivjTxtSumDependency.setName("TxtSumDependency");
                ivjTxtSumDependency.setBounds(316, 13, 154, 20);
                ivjTxtSumDependency.setEditable(false);
                // user code begin {1}
                ivjTxtSumDependency.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumDependency;
    }

    /**
     * Return the TxtSumFact property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSumFact() {
        if (ivjTxtSumFact == null) {
            try {
                ivjTxtSumFact = new javax.swing.JTextField();
                ivjTxtSumFact.setName("TxtSumFact");
                ivjTxtSumFact.setBounds(326, 7, 147, 20);
                ivjTxtSumFact.setEditable(false);
                // user code begin {1}
                ivjTxtSumFact.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumFact;
    }

    /**
     * Return the TxtSumLocal property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSumLocal() {
        if (ivjTxtSumLocal == null) {
            try {
                ivjTxtSumLocal = new javax.swing.JTextField();
                ivjTxtSumLocal.setName("TxtSumLocal");
                ivjTxtSumLocal.setBounds(338, 11, 144, 20);
                ivjTxtSumLocal.setEditable(false);
                // user code begin {1}
                ivjTxtSumLocal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumLocal;
    }

    /**
     * Return the TxtSumResources property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSumResources() {
        if (ivjTxtSumResources == null) {
            try {
                ivjTxtSumResources = new javax.swing.JTextField();
                ivjTxtSumResources.setName("TxtSumResources");
                ivjTxtSumResources.setBounds(326, 31, 147, 20);
                ivjTxtSumResources.setEditable(false);
                // user code begin {1}
                ivjTxtSumResources.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumResources;
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
        getPnlNote().addSimpleEditorPanelListener(ivjEventHandler);
        getTxtName().addKeyListener(ivjEventHandler);
        getTblServices().addMouseListener(ivjEventHandler);
        getScpProducts().addMouseListener(ivjEventHandler);
        getMniNewDependency().addActionListener(ivjEventHandler);
        getMniChangeDependency().addActionListener(ivjEventHandler);
        getMniRemoveDependency().addActionListener(ivjEventHandler);
        getPnlStandardToolbar().addToolBarListener(ivjEventHandler);
        getMniChangeDriver().addActionListener(ivjEventHandler);
        getMniRemoveDriver().addActionListener(ivjEventHandler);
        getTblCostDriver().addMouseListener(ivjEventHandler);
        getScpCostDriver().addMouseListener(ivjEventHandler);
        getCbxCostCentre().addItemListener(ivjEventHandler);
        getMniNewDriver().addActionListener(ivjEventHandler);
        getCbxCategory().addItemListener(ivjEventHandler);
        getCbxResponsibility().addItemListener(ivjEventHandler);
        getTxtMultitude().addKeyListener(ivjEventHandler);
        connPtoP6SetTarget();
        connPtoP1SetTarget();
        connPtoP2SetTarget();
        connPtoP7SetTarget();
        connPtoP3SetTarget();
        connPtoP13SetTarget();
        connPtoP14SetTarget();
        connPtoP4SetTarget();
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
            setName("ServiceDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(673, 583);
            setTitle("Dienst");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setIconImage((new javax.swing.ImageIcon(LauncherView.getInstance().getUtility().getImageURL(Service.class))).getImage()); // ResourceBundle.getImageIcon(ModelUtility.class,
        // "Service.png").getImage()
        setTitle(getResourceString("FrmWindow_text"));
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(Service.class));
        getPnlStandardToolbar().setObjects(getObjects());
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
        NumberTableCellRenderer amountCellRenderer = new NumberTableCellRenderer(ch.softenvironment.util.AmountFormat.getAmountInstance());
        DbTableModel tm = new DbTableModel();
        tm.addColumn(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblName_text"), "name", 80);
        // tm.addFormattedColumn("Kostenart", Formatter.COST_TYPE, 100);
        // tm.addColumn("Typ", "type", 100);
        tm.addColumn(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblMultitude_text"), "multitude", 50);
        tm.addColumnEvaluated(ResourceManager.getResource(FactCostDetailView.class, "FrmWindow_text"), Formatter.COSTDRIVER_FACT_COST, 100, amountCellRenderer,
            evaluator);
        tm.addColumnEvaluated(ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text"), Formatter.COSTDRIVER_PERSONAL_COST, 100,
            amountCellRenderer, evaluator);
        tm.addColumnEvaluated(getResourceString("TbcTotal_text"), Formatter.COSTDRIVER_TOTAL, 100, amountCellRenderer, evaluator);
        tm.addColumnEvaluated(ResourceManager.getResource(FactCostDetailView.class, "LblCurrency_text"), Formatter.COSTDRIVER_CURRENCY, 50, null, evaluator);
        tm.addColumnEvaluated("%", Formatter.COSTDRIVER_PERCENTAGE, 40, new NumberTableCellRenderer(ReportTool.PERCENTAGE_FRACTION_DIGITS), evaluator);
        tm.addColumn(getResourceString("PnlNote_text"), "documentation", 100);
        tm.adjustTable(getTblCostDriver());

        String currency = LauncherView.getInstance().getUtility().getSystemParameter().getDefaultCurrency().getNameString(ModelUtility.getCodeTypeLocale());
        tm = new DbTableModel();
        tm.addColumnEvaluated(ResourceManager.getResource(DependencyDetailView.class, "PnlSupplier_text"), Formatter.SUPPLIER_NAME, 100, null, evaluator);
        tm.addColumnEvaluated(getResourceString("TbcType_text"), Formatter.SUPPLIER_TYPE, 60, null, evaluator);
        tm.addColumnEvaluated(getResourceString("PnlCost_text") + "[" + currency + "]", Formatter.SUPPLIER_COST, 60, amountCellRenderer, evaluator);
        tm.addColumn(ResourceManager.getResource(DependencyDetailView.class, "SliDistribution_text") + " [%]", "distribution", 40, new NumberTableCellRenderer(
            ReportTool.PERCENTAGE_FRACTION_DIGITS));
        tm.addColumnEvaluated(ResourceManager.getResource(DependencyDetailView.class, "SliDistribution_text") + " [" + currency + "]",
            Formatter.SUPPLIER_CLIENT_COST, 60, amountCellRenderer, evaluator);
        tm.addColumn(ResourceManager.getResourceAsNonLabeled(DependencyDetailView.class, "LblSupplierInfluence"), "supplierInfluence", 60);
        tm.addColumn(ResourceManager.getResource(DependencyDetailView.class, "ChxVariant_text"), "variant", 30);
        tm.addColumn(getResourceString("PnlNote_text"), "documentation", 100);
        tm.adjustTable(getTblServices());
    }

    /**
     * Create a new Object (and open it for e.g. in a DetailView).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void newObject(java.lang.Object source) {
        try {
            ch.softenvironment.jomm.DbObjectServer server = getObject().getObjectServer();

            if (source.equals(getMniNewDependency())) {
                Dependency dependency = ModelUtility.createDependency(getObject().getObjectServer());
                openDependency(ch.softenvironment.util.ListUtils.createList(dependency));
            } else if (source.equals(getMniNewDriver())) {
                CostDriver driver = (CostDriver) LauncherView.getInstance().getUtility().createTcoObject(server, CostDriver.class);
                LauncherView.getInstance().getUtility().addOwnedElement(getObject(), driver);
                java.util.List<CostDriver> objects = ch.softenvironment.util.ListUtils.createList(driver);
                registerPropertyChangeListener(objects, true); // to recalc sums

                refreshDriver();

                openCostDriver(objects);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * @see #dispose()
     */
    private void openCostDriver(java.util.List<CostDriver> objs) {
        ch.softenvironment.view.BaseFrame view = new CostDriverDetailView(getViewOptions(), objs, this);
        view.setRelativeLocation(this);
        view.setVisible(true);
    }

    /**
     * @see #dispose()
     */
    private void openDependency(java.util.List<Dependency> objs) {
        ch.softenvironment.view.BaseFrame view = new DependencyDetailView(getViewOptions(), objs, this, getObject());
        view.setRelativeLocation(this);
        view.setVisible(true);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent event) {
        try {
            // if (event.getPropertyName().equals("driver")) {
            refreshCosts();
            // }
        } catch (Exception e) {
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
     * Actualize total Service costs.
     */
    protected void refreshCosts() throws Exception {
        Currency currency = LauncherView.getInstance().getUtility().getSystemParameter().getDefaultCurrency();
        getCbxCurrencyAll().setSelectedItem(currency);
        getCbxCurrencyDependency().setSelectedItem(currency);
        getCbxCurrencyFact().setSelectedItem(currency);
        getCbxCurrencyLocal().setSelectedItem(currency);
        getCbxCurrencyResources().setSelectedItem(currency);

        refreshDriver();
        refreshDependencies();
    }

    /**
     *
     */
    private void refreshDependencies() {
        ((DbTableModel) getTblServices().getModel()).setAll(getObject().getSupplierId());
        // getCbxCurrencyFact().setSelectedItem(LauncherView.getInstance().sysParams.getDefaultCurrency());
        Calculator calculator = new CalculatorTco(LauncherView.getInstance().getUtility(), (TcoPackage) LauncherView.getInstance().getUtility().getRoot(),
            LauncherView.getInstance().getUtility().getUsageDuration());
        java.util.List<Double> dependencies = calculator.calculateDependency(getObject());
        double sum = Calculator.getValue(dependencies, Calculator.INDEX_TOTAL);

        // double factor = TcoFormatter.getMultitudeFactor(getObject());
        // getTxtSumFact().setText(ch.softenvironment.util.AmountFormat.toString(sumFact
        // * factor));
        // getTxtSumResources().setText(ch.softenvironment.util.AmountFormat.toString(sumPersonal
        // * factor));
        // getTxtSumLocal().setText(ch.softenvironment.util.AmountFormat.toString((sumFact
        // + sumPersonal) * factor));

        getTxtSumDependency().setText(af.format(sum));

        getTxtSumAll().setText(af.format(sum + currentLocalSum));
    }

    /**
     * Calculate total Driver cost.
     */
    private void refreshDriver() throws Exception {
        double[] sums = Calculator.calculateWithoutFactor(getObject());
        double sumFact = sums[0];
        double sumPersonal = sums[1];
        /*
         * java.util.Iterator iterator = ((org.tcotool.model.Service)getObject()).getDriver().iterator(); double sumFact = 0.0; double sumPersonal = 0.0; while
         * (iterator.hasNext()) { double sums[] = Calculator.calc((org.tcotool.model.CostDriver)iterator.next()); sumFact = sumFact + sums[0]; sumPersonal =
         * sumPersonal + sums[1]; }
         */
        double factor = ModelUtility.getMultitudeFactor(getObject());
        currentLocalSum = (sumFact + sumPersonal) * factor;
        getTxtSumFact().setText(af.format(sumFact * factor));
        getTxtSumResources().setText(af.format(sumPersonal * factor));
        getTxtSumLocal().setText(af.format(currentLocalSum));

        // Table CostDriver-evaluator
        evaluator.setTotalCostDriver(sumFact + sumPersonal);

        ((DbTableModel) getTblCostDriver().getModel()).setAll(getObject().getDriver());
    }

    /**
     * Remove the selected Object's (for e.g from a JTable).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void removeObjects(final java.lang.Object source) {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO Patch: XmlObjectServer cannot handle
                    // #deletePersistent()
                    if (source.equals(getMniRemoveDriver())) {
                        java.util.List<CostDriver> removedItems = DbTableModel.removeSelectedItems(getTblCostDriver(), getViewOptions(), false);
                        // getObject().getDriver().removeAll(removedItems);
                        java.util.List<CostDriver> list = new java.util.ArrayList<CostDriver>(getObject().getDriver());
                        list.removeAll(removedItems);
                        getObject().setDriver(list);

                        // unlink
                        java.util.Iterator<CostDriver> iterator = removedItems.iterator();
                        while (iterator.hasNext()) {
                            iterator.next().setServiceId(null);
                        }

                        registerPropertyChangeListener(removedItems, false);

                        refreshDriver();
                    } else if (source.equals(getMniRemoveDependency())) {
                        java.util.List<Dependency> removedItems = DbTableModel.removeSelectedItems(getTblServices(), getViewOptions(), false);
                        java.util.Iterator<Dependency> iterator = removedItems.iterator();
                        while (iterator.hasNext()) {
                            LauncherView.getInstance().getUtility().removeDependencyByClient(getObject(), iterator.next());
                        }

                        registerPropertyChangeListener(removedItems, false);

                        refreshDependencies();
                        // PATCH
                        LauncherView.getInstance().setModelChanged(true);
                    }
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
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
            // reset codes
            JComboBoxUtility.initComboBox(getCbxCategory(), server.retrieveCodes(org.tcotool.model.ServiceCategory.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCostCentre(), server.retrieveCodes(org.tcotool.model.CostCentre.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxResponsibility(), server.retrieveCodes(org.tcotool.model.Responsibility.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyFact(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyResources(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyDependency(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyLocal(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyAll(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));

            setObject((org.tcotool.model.Service) object);
            java.util.Iterator<CostDriver> iterator = getObject().getDriver().iterator();
            while (iterator.hasNext()) {
                iterator.next().addPropertyChangeListener(this);
            }
            refreshCosts();

            getObject().addPropertyChangeListener(getConsistencyController());
            getObject().addPropertyChangeListener(this);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Set the Object to a new value.
     *
     * @param newValue org.tcotool.model.Service
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void setObject(org.tcotool.model.Service newValue) {
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
                connPtoP7SetTarget();
                connPtoP3SetTarget();
                connPtoP13SetTarget();
                connPtoP14SetTarget();
                connPtoP4SetTarget();
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
