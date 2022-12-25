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
import ch.softenvironment.client.UserActionRights;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.controller.ConsistencyController;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.controller.DbTableModel;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.ParserCSV;
import ch.softenvironment.view.*;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import ch.softenvironment.view.table.NumberTableCellRenderer;
import org.tcotool.model.Process;
import org.tcotool.model.*;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Formatter;
import org.tcotool.tools.ModelUtility;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * DetailView of a CostDriver.
 *
 * @author Peter Hirzel
 */

public class CostDriverDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements DetailView, ListMenuChoice, java.beans.PropertyChangeListener {

    private ch.softenvironment.view.DetailView caller = null;
    private final Formatter evaluator = new Formatter(LauncherView.getInstance().getUtility()); // must be global -> see #refresh*()
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjPnlDetail = null;
    private boolean ivjConnPtoP1Aligning = false;
    private boolean ivjConnPtoP2Aligning = false;
    private javax.swing.JPanel ivjJPanel1 = null;
    private CostDriver ivjObject = null;
    private ToolBar ivjPnlStandardToolbar = null;
    private StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JLabel ivjLblMultitude = null;
    private javax.swing.JTextField ivjTxtName = null;
    private javax.swing.JComboBox ivjCbxLifeCycle = null;
    private javax.swing.JComboBox ivjCbxProcess = null;
    private javax.swing.JComboBox ivjCbxProjectPhase = null;
    private javax.swing.JTabbedPane ivjTbpGeneral = null;
    private boolean ivjConnPtoP56Aligning = false;
    private boolean ivjConnPtoP57Aligning = false;
    private boolean ivjConnPtoP58Aligning = false;
    private javax.swing.JLabel ivjLblName = null;
    private javax.swing.JSeparator ivjJSeparator11 = null;
    private javax.swing.JPopupMenu ivjMnpPersonalCosts = null;
    private javax.swing.JMenuItem ivjMniNewPersonalCost = null;
    private javax.swing.JTextField ivjTxtSumPersonal = null;
    private javax.swing.JTextField ivjTxtSumTotal = null;
    private SimpleEditorPanel ivjPnlNote = null;
    private javax.swing.JComboBox ivjCbxCurrencyPersonal = null;
    private javax.swing.JComboBox ivjCbxCurrencyTotal = null;
    private javax.swing.JMenuItem ivjMniNewFactCost = null;
    private javax.swing.JMenu ivjMnuNew = null;
    private javax.swing.JMenuItem ivjMniChangeCost = null;
    private javax.swing.JMenuItem ivjMniCopyCost = null;
    private javax.swing.JMenuItem ivjMniRemoveCost = null;
    private javax.swing.JScrollPane ivjScpCost = null;
    private javax.swing.JTable ivjTblCost = null;
    private javax.swing.JPanel ivjPnlCosts = null;
    private javax.swing.JTextField ivjTxtSumFact = null;
    private javax.swing.JComboBox ivjCbxCurrencyFact = null;
    private javax.swing.JLabel ivjLblCycle = null;
    private javax.swing.JLabel ivjLblPhase = null;
    private javax.swing.JLabel ivjLblProcess = null;
    private javax.swing.JPanel ivjPnlTotal = null;
    private javax.swing.JSeparator ivjJSeparator12 = null;
    private javax.swing.JMenuItem ivjMniChangeOccurance = null;
    private javax.swing.JMenuItem ivjMniNewOccurance = null;
    private javax.swing.JMenuItem ivjMniRemoveOccurance = null;
    private javax.swing.JPopupMenu ivjMnpOccurance = null;
    private javax.swing.JPanel ivjPnlOccurance = null;
    private javax.swing.JScrollPane ivjScpOccurance = null;
    private javax.swing.JTable ivjTblOccurance = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtSumOccurance = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtMultitude = null;
    private javax.swing.JLabel ivjLblOccurance = null;
    private javax.swing.JLabel ivjLblTotal = null;
    private javax.swing.JLabel ivjLblTotalFactCost = null;
    private javax.swing.JLabel ivjLblTotalOccurance = null;
    private javax.swing.JLabel ivjLblTotalPersonalCost = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ActionListener,
        java.awt.event.ItemListener, java.awt.event.KeyListener, java.awt.event.MouseListener, java.beans.PropertyChangeListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == CostDriverDetailView.this.getMniNewPersonalCost()) {
                connEtoC4(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getMniChangeCost()) {
                connEtoC5(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getMniRemoveCost()) {
                connEtoC6(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getMniCopyCost()) {
                connEtoC8(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getMniNewFactCost()) {
                connEtoC9(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getMniNewOccurance()) {
                connEtoC16(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getMniChangeOccurance()) {
                connEtoC17(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getMniRemoveOccurance()) {
                connEtoC18(e);
            }
        }

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == CostDriverDetailView.this.getCbxProcess()) {
                connPtoP56SetSource();
            }
            if (e.getSource() == CostDriverDetailView.this.getCbxProjectPhase()) {
                connPtoP57SetSource();
            }
            if (e.getSource() == CostDriverDetailView.this.getCbxLifeCycle()) {
                connPtoP58SetSource();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == CostDriverDetailView.this.getTxtName()) {
                connPtoP2SetSource();
            }
            if (e.getSource() == CostDriverDetailView.this.getTxtMultitude()) {
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
            if (e.getSource() == CostDriverDetailView.this.getTblCost()) {
                connEtoC10(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getScpCost()) {
                connEtoC11(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getScpOccurance()) {
                connEtoC12(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getTblOccurance()) {
                connEtoC14(e);
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.getSource() == CostDriverDetailView.this.getScpCost()) {
                connEtoC2(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getTblCost()) {
                connEtoC3(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getScpOccurance()) {
                connEtoC13(e);
            }
            if (e.getSource() == CostDriverDetailView.this.getTblOccurance()) {
                connEtoC15(e);
            }
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == CostDriverDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == CostDriverDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == CostDriverDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == CostDriverDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == CostDriverDetailView.this.getObject() && (evt.getPropertyName().equals("name"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == CostDriverDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == CostDriverDetailView.this.getObject() && (evt.getPropertyName().equals("process"))) {
                connPtoP56SetTarget();
            }
            if (evt.getSource() == CostDriverDetailView.this.getObject() && (evt.getPropertyName().equals("phase"))) {
                connPtoP57SetTarget();
            }
            if (evt.getSource() == CostDriverDetailView.this.getObject() && (evt.getPropertyName().equals("cycle"))) {
                connPtoP58SetTarget();
            }
            if (evt.getSource() == CostDriverDetailView.this.getObject() && (evt.getPropertyName().equals("multitude"))) {
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
            if (newEvent.getSource() == CostDriverDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == CostDriverDetailView.this.getPnlNote()) {
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
    public CostDriverDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public CostDriverDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects, ch.softenvironment.view.DetailView caller) {
        super(viewOptions, objects);
        initialize();
        this.caller = caller;
    }

    @Override
    public void adaptUserAction(EventObject event, Object control) {
        if (control != null) {
            if (control.equals(getMnpPersonalCosts())) {
                // boolean selected = getTblCost().getSelectedRowCount() == 1;
                getMniChangeCost().setEnabled(getTblCost().getSelectedRowCount() == 1);
                getMniRemoveCost().setEnabled(getTblCost().getSelectedRowCount() > 0);
            } else if (control.equals(getMnpOccurance())) {
                getMniChangeOccurance().setEnabled(getTblOccurance().getSelectedRowCount() == 1);
                getMniRemoveOccurance().setEnabled(getTblOccurance().getSelectedRowCount() > 0);
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
        /*
         * try {
         *
         * } catch(Throwable e) { handleException(e); }
         */
    }

    /**
     * Do anything at DoubleClick-Event for e.g. open selected Object(s) in a JTable.
     *
     * @see BaseFrame#genericPopupDisplay(MouseEvent, JPopupMenu)
     */
    @Override
    public void changeObjects(java.lang.Object source) {
        try {
            if (source.equals(getMniChangeCost()) || source.equals(getTblCost())) {
                java.util.List objs = DbTableModel.getSelectedItems(getTblCost());
                if (!getViewOptions().getViewManager().activateView(objs)) {
                    ch.softenvironment.view.BaseFrame view = null;
                    Object item = objs.get(0);
                    if (item instanceof FactCost) {
                        view = new FactCostDetailView(getViewOptions(), objs, this);
                    } else {
                        view = new PersonalCostDetailView(getViewOptions(), objs, this);
                    }
                    view.setRelativeLocation(this);
                    view.setVisible(true);
                }
            } else if (source.equals(getMniChangeOccurance()) || source.equals(getTblOccurance())) {
                java.util.List objects = DbTableModel.getSelectedItems(getTblOccurance());
                if (!getViewOptions().getViewManager().activateView(objects)) {
                    openOccurance(objects);
                }
            }
        } catch (Throwable e) {
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
     * connEtoC10: (TblCost.mouse.mousePressed(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC10(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpPersonalCosts());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC11: (ScpCost.mouse.mousePressed(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC11(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpPersonalCosts());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC12: (ScpOccurance.mouse.mousePressed(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC12(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpOccurance());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC13: (ScpOccurance.mouse.mouseReleased(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC13(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpOccurance());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC14: (TblOccurance.mouse.mousePressed(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC14(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpOccurance());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC15: (TblOccurance.mouse.mouseReleased(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC15(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpOccurance());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC16: (MniNewOccurance.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC16(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniNewOccurance());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC17: (MniChangeOccurance.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.changeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC17(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getMniChangeOccurance());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC18: (MniRemoveOccurance.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.removeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC18(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.removeObjects(getMniRemoveOccurance());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (ScpCostDriver.mouse.mouseReleased(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpPersonalCosts());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (TblCostDriver.mouse.mouseReleased(java.awt.event.MouseEvent) --> CostDriverDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpPersonalCosts());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC4: (MniNewDependency.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniNewPersonalCost());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5: (MniChangeDriver.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.changeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getMniChangeCost());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC6: (MniRemoveDriver.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.removeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.removeObjects(getMniRemoveCost());
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
     * connEtoC8: (MniCopyDriver.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.copyObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.copyObject(getMniCopyCost());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC9: (MniNewFactCost.action.actionPerformed(java.awt.event.ActionEvent) --> CostDriverDetailView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC9(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniNewFactCost());
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
     * connPtoP56SetSource: (Object.process <--> CbxProcess.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP56SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP56Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP56Aligning = true;
                if ((getObject() != null)) {
                    getObject().setProcess((org.tcotool.model.Process) getCbxProcess().getSelectedItem());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP56Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP56Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP56SetTarget: (Object.process <--> CbxProcess.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP56SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP56Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP56Aligning = true;
                if ((getObject() != null)) {
                    getCbxProcess().setSelectedItem(getObject().getProcess());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP56Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP56Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP57SetSource: (Object.phase <--> CbxProjectPhase.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP57SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP57Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP57Aligning = true;
                if ((getObject() != null)) {
                    getObject().setPhase((org.tcotool.model.ProjectPhase) getCbxProjectPhase().getSelectedItem());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP57Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP57Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP57SetTarget: (Object.phase <--> CbxProjectPhase.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP57SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP57Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP57Aligning = true;
                if ((getObject() != null)) {
                    getCbxProjectPhase().setSelectedItem(getObject().getPhase());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP57Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP57Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP58SetSource: (Object.cycle <--> CbxLifeCycle.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP58SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP58Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP58Aligning = true;
                if ((getObject() != null)) {
                    getObject().setCycle((org.tcotool.model.LifeCycle) getCbxLifeCycle().getSelectedItem());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP58Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP58Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP58SetTarget: (Object.cycle <--> CbxLifeCycle.selectedItem)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP58SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP58Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP58Aligning = true;
                if ((getObject() != null)) {
                    getCbxLifeCycle().setSelectedItem(getObject().getCycle());
                }
                // user code begin {2}
                // user code end
                ivjConnPtoP58Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP58Aligning = false;
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
    @Override
    public void copyObject(java.lang.Object source) {
    }

    @Override
    public void dispose() {
        registerPropertyChangeListener(getObject().getCost(), false);
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
     * Return the CbxCurrencyPersonal1 property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyFact() {
        if (ivjCbxCurrencyFact == null) {
            try {
                ivjCbxCurrencyFact = new javax.swing.JComboBox();
                ivjCbxCurrencyFact.setName("CbxCurrencyFact");
                ivjCbxCurrencyFact.setBounds(472, 8, 61, 23);
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
     * Return the CbxCurrencyPersonal property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCurrencyPersonal() {
        if (ivjCbxCurrencyPersonal == null) {
            try {
                ivjCbxCurrencyPersonal = new javax.swing.JComboBox();
                ivjCbxCurrencyPersonal.setName("CbxCurrencyPersonal");
                ivjCbxCurrencyPersonal.setBounds(472, 36, 61, 23);
                ivjCbxCurrencyPersonal.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCurrencyPersonal;
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
                ivjCbxCurrencyTotal.setBounds(472, 63, 61, 23);
                ivjCbxCurrencyTotal.setEnabled(false);
                // user code begin {1}
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
     * Return the CbxLifeCycle property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxLifeCycle() {
        if (ivjCbxLifeCycle == null) {
            try {
                ivjCbxLifeCycle = new javax.swing.JComboBox();
                ivjCbxLifeCycle.setName("CbxLifeCycle");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxLifeCycle;
    }

    /**
     * Return the CbxProcess property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxProcess() {
        if (ivjCbxProcess == null) {
            try {
                ivjCbxProcess = new javax.swing.JComboBox();
                ivjCbxProcess.setName("CbxProcess");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxProcess;
    }

    /**
     * Return the CbxProjectPhase property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxProjectPhase() {
        if (ivjCbxProjectPhase == null) {
            try {
                ivjCbxProjectPhase = new javax.swing.JComboBox();
                ivjCbxProjectPhase.setName("CbxProjectPhase");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxProjectPhase;
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
                constraintsLblName.ipadx = 93;
                constraintsLblName.insets = new java.awt.Insets(16, 13, 7, 6);
                getJPanel1().add(getLblName(), constraintsLblName);

                java.awt.GridBagConstraints constraintsTxtName = new java.awt.GridBagConstraints();
                constraintsTxtName.gridx = 1;
                constraintsTxtName.gridy = 0;
                constraintsTxtName.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsTxtName.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTxtName.weightx = 1.0;
                constraintsTxtName.ipadx = 347;
                constraintsTxtName.insets = new java.awt.Insets(15, 7, 2, 30);
                getJPanel1().add(getTxtName(), constraintsTxtName);

                java.awt.GridBagConstraints constraintsTbpGeneral = new java.awt.GridBagConstraints();
                constraintsTbpGeneral.gridx = 0;
                constraintsTbpGeneral.gridy = 2;
                constraintsTbpGeneral.gridwidth = 2;
                constraintsTbpGeneral.fill = java.awt.GridBagConstraints.BOTH;
                constraintsTbpGeneral.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTbpGeneral.weightx = 1.0;
                constraintsTbpGeneral.weighty = 1.0;
                constraintsTbpGeneral.ipadx = -489;
                constraintsTbpGeneral.ipady = -44;
                constraintsTbpGeneral.insets = new java.awt.Insets(7, 5, 13, 2);
                getJPanel1().add(getTbpGeneral(), constraintsTbpGeneral);

                java.awt.GridBagConstraints constraintsLblMultitude = new java.awt.GridBagConstraints();
                constraintsLblMultitude.gridx = 0;
                constraintsLblMultitude.gridy = 1;
                constraintsLblMultitude.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblMultitude.ipadx = 128;
                constraintsLblMultitude.insets = new java.awt.Insets(4, 13, 5, 6);
                getJPanel1().add(getLblMultitude(), constraintsLblMultitude);

                java.awt.GridBagConstraints constraintsTxtMultitude = new java.awt.GridBagConstraints();
                constraintsTxtMultitude.gridx = 1;
                constraintsTxtMultitude.gridy = 1;
                constraintsTxtMultitude.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsTxtMultitude.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTxtMultitude.weightx = 1.0;
                constraintsTxtMultitude.ipadx = 114;
                constraintsTxtMultitude.insets = new java.awt.Insets(2, 7, 1, 374);
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
     * Return the JSeparator12 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator12() {
        if (ivjJSeparator12 == null) {
            try {
                ivjJSeparator12 = new javax.swing.JSeparator();
                ivjJSeparator12.setName("JSeparator12");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSeparator12;
    }

    /**
     * Return the JLabel11 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCycle() {
        if (ivjLblCycle == null) {
            try {
                ivjLblCycle = new javax.swing.JLabel();
                ivjLblCycle.setName("LblCycle");
                ivjLblCycle.setText("Lebenszyklus:");
                // user code begin {1}
                ivjLblCycle.setText(ModelUtility.getTypeString(LifeCycle.class) + ":" /* getResourceString("LblCycle_text") */);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCycle;
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
     * Return the JLabel11 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblOccurance() {
        if (ivjLblOccurance == null) {
            try {
                ivjLblOccurance = new javax.swing.JLabel();
                ivjLblOccurance.setName("LblOccurance");
                ivjLblOccurance.setText("Vorkommnis nach Standorten:");
                // user code begin {1}
                ivjLblOccurance.setText(getResourceString("LblOccurance_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblOccurance;
    }

    /**
     * Return the JLabel21 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPhase() {
        if (ivjLblPhase == null) {
            try {
                ivjLblPhase = new javax.swing.JLabel();
                ivjLblPhase.setName("LblPhase");
                ivjLblPhase.setToolTipText("nach HERMES");
                ivjLblPhase.setText("Projekt-Phase:");
                // user code begin {1}
                ivjLblPhase.setText(ModelUtility.getTypeString(ProjectPhase.class) + ":" /* getResourceString("LblPhase_text") */);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPhase;
    }

    /**
     * Return the JLabel15 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblProcess() {
        if (ivjLblProcess == null) {
            try {
                ivjLblProcess = new javax.swing.JLabel();
                ivjLblProcess.setName("LblProcess");
                ivjLblProcess.setText("Prozess:");
                // user code begin {1}
                ivjLblProcess.setText(ModelUtility.getTypeString(Process.class) + ":" /* getResourceString("LblProcess_text") */);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblProcess;
    }

    /**
     * Return the JLabel321 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotal() {
        if (ivjLblTotal == null) {
            try {
                ivjLblTotal = new javax.swing.JLabel();
                ivjLblTotal.setName("LblTotal");
                ivjLblTotal.setToolTipText("Total der Kostentreiber nach Menge");
                ivjLblTotal.setText("Total:");
                ivjLblTotal.setBounds(8, 66, 308, 14);
                ivjLblTotal.setForeground(java.awt.Color.red);
                // user code begin {1}
                ivjLblTotal.setText(ResourceManager.getResourceAsLabel(ServiceDetailView.class, "TbcTotal_text"));
                ivjLblTotal.setToolTipText(getResourceString("LblTotal_toolTipText"));
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
     * Return the JLabel322 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotalFactCost() {
        if (ivjLblTotalFactCost == null) {
            try {
                ivjLblTotalFactCost = new javax.swing.JLabel();
                ivjLblTotalFactCost.setName("LblTotalFactCost");
                ivjLblTotalFactCost.setToolTipText("Summe Sachkosten nach Menge");
                ivjLblTotalFactCost.setText("Total Sachkosten:");
                ivjLblTotalFactCost.setBounds(8, 12, 308, 14);
                // user code begin {1}
                ivjLblTotalFactCost.setToolTipText(ResourceManager.getResource(ServiceDetailView.class, "LblTotalFactCost_toolTipText"));
                ivjLblTotalFactCost.setText(ResourceManager.getResource(ServiceDetailView.class, "LblTotalFactCost_text"));
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
     * Return the LblSumOccurance property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTotalOccurance() {
        if (ivjLblTotalOccurance == null) {
            try {
                ivjLblTotalOccurance = new javax.swing.JLabel();
                ivjLblTotalOccurance.setName("LblTotalOccurance");
                ivjLblTotalOccurance.setToolTipText("Summe aller Vorkommnisse");
                ivjLblTotalOccurance.setText("Total:");
                // user code begin {1}
                ivjLblTotalOccurance.setToolTipText(getResourceString("LblTotalOccurance_toolTipText"));
                ivjLblTotalOccurance.setText(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text") + ":");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTotalOccurance;
    }

    /**
     * Return the JLabel32 property value.
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
                ivjLblTotalPersonalCost.setBounds(8, 39, 308, 14);
                // user code begin {1}
                ivjLblTotalPersonalCost.setToolTipText(ResourceManager.getResource(ServiceDetailView.class, "LblTotalPersonalCost_toolTipText"));
                ivjLblTotalPersonalCost.setText(ResourceManager.getResource(ServiceDetailView.class, "LblTotalPersonalCost_text"));
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
     * Return the MniChangeDriver property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniChangeCost() {
        if (ivjMniChangeCost == null) {
            try {
                ivjMniChangeCost = new javax.swing.JMenuItem();
                ivjMniChangeCost.setName("MniChangeCost");
                ivjMniChangeCost.setText("Oeffnen...");
                ivjMniChangeCost.setEnabled(false);
                // user code begin {1}
                ivjMniChangeCost.setText(ch.softenvironment.view.CommonUserAccess.getMniEditChangeWindowText());
                ivjMniChangeCost.setIcon(CommonUserAccess.getIconChange());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniChangeCost;
    }

    /**
     * Return the MniChangeOccurance property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniChangeOccurance() {
        if (ivjMniChangeOccurance == null) {
            try {
                ivjMniChangeOccurance = new javax.swing.JMenuItem();
                ivjMniChangeOccurance.setName("MniChangeOccurance");
                ivjMniChangeOccurance.setText("Oeffnen...");
                ivjMniChangeOccurance.setEnabled(false);
                // user code begin {1}
                ivjMniChangeOccurance.setText(ch.softenvironment.view.CommonUserAccess.getMniEditChangeWindowText());
                ivjMniChangeOccurance.setIcon(CommonUserAccess.getIconChange());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniChangeOccurance;
    }

    /**
     * Return the MniCopyDriver property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniCopyCost() {
        if (ivjMniCopyCost == null) {
            try {
                ivjMniCopyCost = new javax.swing.JMenuItem();
                ivjMniCopyCost.setName("MniCopyCost");
                ivjMniCopyCost.setText("Kopieren...");
                ivjMniCopyCost.setEnabled(false);
                // user code begin {1}
                ivjMniCopyCost.setText(ch.softenvironment.view.CommonUserAccess.getMniEditCopyText());
                ivjMniCopyCost.setIcon(CommonUserAccess.getIconCopy());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniCopyCost;
    }

    /**
     * Return the MniNewFactCost property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniNewFactCost() {
        if (ivjMniNewFactCost == null) {
            try {
                ivjMniNewFactCost = new javax.swing.JMenuItem();
                ivjMniNewFactCost.setName("MniNewFactCost");
                ivjMniNewFactCost.setText("Neu Sachkosten...");
                // user code begin {1}
                ivjMniNewFactCost.setText(ResourceManager.getResource(FactCostDetailView.class, "FrmWindow_text") + "...");
                ivjMniNewFactCost.setIcon(ResourceManager.getImageIcon(ModelUtility.class, "FactCost.png"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniNewFactCost;
    }

    /**
     * Return the MniNewOccurance property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniNewOccurance() {
        if (ivjMniNewOccurance == null) {
            try {
                ivjMniNewOccurance = new javax.swing.JMenuItem();
                ivjMniNewOccurance.setName("MniNewOccurance");
                ivjMniNewOccurance.setText("Neu...");
                // user code begin {1}
                ivjMniNewOccurance.setText(ch.softenvironment.view.CommonUserAccess.getMniFileNewWindowText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniNewOccurance;
    }

    /**
     * Return the MniNewDependency property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniNewPersonalCost() {
        if (ivjMniNewPersonalCost == null) {
            try {
                ivjMniNewPersonalCost = new javax.swing.JMenuItem();
                ivjMniNewPersonalCost.setName("MniNewPersonalCost");
                ivjMniNewPersonalCost.setText("Neu Personalaufwand...");
                // user code begin {1}
                ivjMniNewPersonalCost.setText(ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text") + "...");
                ivjMniNewPersonalCost.setIcon(ResourceManager.getImageIcon(ModelUtility.class, "PersonalCost.png"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniNewPersonalCost;
    }

    /**
     * Return the MniRemoveDriver property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniRemoveCost() {
        if (ivjMniRemoveCost == null) {
            try {
                ivjMniRemoveCost = new javax.swing.JMenuItem();
                ivjMniRemoveCost.setName("MniRemoveCost");
                ivjMniRemoveCost.setText("Loeschen");
                ivjMniRemoveCost.setEnabled(false);
                // user code begin {1}
                ivjMniRemoveCost.setText(ch.softenvironment.view.CommonUserAccess.getMniEditRemoveText());
                ivjMniRemoveCost.setIcon(CommonUserAccess.getIconRemove());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniRemoveCost;
    }

    /**
     * Return the MniRemoveOccurance property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniRemoveOccurance() {
        if (ivjMniRemoveOccurance == null) {
            try {
                ivjMniRemoveOccurance = new javax.swing.JMenuItem();
                ivjMniRemoveOccurance.setName("MniRemoveOccurance");
                ivjMniRemoveOccurance.setText("Loeschen");
                ivjMniRemoveOccurance.setEnabled(false);
                // user code begin {1}
                ivjMniRemoveOccurance.setText(ch.softenvironment.view.CommonUserAccess.getMniEditRemoveText());
                ivjMniRemoveOccurance.setIcon(CommonUserAccess.getIconRemove());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniRemoveOccurance;
    }

    /**
     * Return the MnpOccurance property value.
     *
     * @return javax.swing.JPopupMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPopupMenu getMnpOccurance() {
        if (ivjMnpOccurance == null) {
            try {
                ivjMnpOccurance = new javax.swing.JPopupMenu();
                ivjMnpOccurance.setName("MnpOccurance");
                ivjMnpOccurance.add(getMniNewOccurance());
                ivjMnpOccurance.add(getMniChangeOccurance());
                ivjMnpOccurance.add(getJSeparator12());
                ivjMnpOccurance.add(getMniRemoveOccurance());
                // user code begin {1}
                ivjMnpOccurance.add(new JSeparator());
                JMenuItem item = new JMenuItem();
                item.setText(CommonUserAccess.getMniFileExportText());
                item.setToolTipText(ResourceManager.getResource(BaseFrame.class, "MniExportTableData_toolTip"));
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        // call after extension-points are realized of extension
                        exportTableData(getTblOccurance(), ParserCSV.DEFAULT_SEPARATOR);
                    }
                });
                ivjMnpOccurance.add(item);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnpOccurance;
    }

    /**
     * Return the MnpPersonalCosts property value.
     *
     * @return javax.swing.JPopupMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPopupMenu getMnpPersonalCosts() {
        if (ivjMnpPersonalCosts == null) {
            try {
                ivjMnpPersonalCosts = new javax.swing.JPopupMenu();
                ivjMnpPersonalCosts.setName("MnpPersonalCosts");
                ivjMnpPersonalCosts.add(getMnuNew());
                ivjMnpPersonalCosts.add(getMniChangeCost());
                ivjMnpPersonalCosts.add(getMniCopyCost());
                ivjMnpPersonalCosts.add(getJSeparator11());
                ivjMnpPersonalCosts.add(getMniRemoveCost());
                // user code begin {1}
                ivjMnpPersonalCosts.add(new JSeparator());
                JMenuItem item = new JMenuItem();
                item.setText(CommonUserAccess.getMniFileExportText());
                item.setToolTipText(ResourceManager.getResource(BaseFrame.class, "MniExportTableData_toolTip"));
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        // call after extension-points are realized of extension
                        exportTableData(getTblCost(), ParserCSV.DEFAULT_SEPARATOR);
                    }
                });
                ivjMnpPersonalCosts.add(item);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnpPersonalCosts;
    }

    /**
     * Return the MnuNew property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuNew() {
        if (ivjMnuNew == null) {
            try {
                ivjMnuNew = new javax.swing.JMenu();
                ivjMnuNew.setName("MnuNew");
                ivjMnuNew.setText("Neu");
                ivjMnuNew.add(getMniNewPersonalCost());
                ivjMnuNew.add(getMniNewFactCost());
                // user code begin {1}
                ivjMnuNew.setText(CommonUserAccess.getMniFileNewText());
                ivjMnuNew.setIcon(CommonUserAccess.getIconNew());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuNew;
    }

    /**
     * Return the Object property value.
     *
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.CostDriver getObject() {
        // user code begin {1}
        // user code end
        return ivjObject;
    }

    /**
     * Return the PnlCosts property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCosts() {
        if (ivjPnlCosts == null) {
            try {
                ivjPnlCosts = new javax.swing.JPanel();
                ivjPnlCosts.setName("PnlCosts");
                ivjPnlCosts.setLayout(new java.awt.BorderLayout());
                getPnlCosts().add(getScpCost(), "Center");
                getPnlCosts().add(getPnlTotal(), "South");
                // user code begin {1}
                getPnlCosts().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlCost_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCosts;
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
                ivjPnlDetail.setLayout(new java.awt.GridBagLayout());

                java.awt.GridBagConstraints constraintsLblPhase = new java.awt.GridBagConstraints();
                constraintsLblPhase.gridx = 0;
                constraintsLblPhase.gridy = 1;
                constraintsLblPhase.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblPhase.ipadx = 78;
                constraintsLblPhase.insets = new java.awt.Insets(8, 12, 5, 5);
                getPnlDetail().add(getLblPhase(), constraintsLblPhase);

                java.awt.GridBagConstraints constraintsCbxProjectPhase = new java.awt.GridBagConstraints();
                constraintsCbxProjectPhase.gridx = 1;
                constraintsCbxProjectPhase.gridy = 1;
                constraintsCbxProjectPhase.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCbxProjectPhase.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsCbxProjectPhase.weightx = 1.0;
                constraintsCbxProjectPhase.ipadx = 168;
                constraintsCbxProjectPhase.insets = new java.awt.Insets(2, 6, 2, 30);
                getPnlDetail().add(getCbxProjectPhase(), constraintsCbxProjectPhase);

                java.awt.GridBagConstraints constraintsLblCycle = new java.awt.GridBagConstraints();
                constraintsLblCycle.gridx = 0;
                constraintsLblCycle.gridy = 2;
                constraintsLblCycle.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblCycle.ipadx = 81;
                constraintsLblCycle.insets = new java.awt.Insets(8, 12, 8, 5);
                getPnlDetail().add(getLblCycle(), constraintsLblCycle);

                java.awt.GridBagConstraints constraintsCbxLifeCycle = new java.awt.GridBagConstraints();
                constraintsCbxLifeCycle.gridx = 1;
                constraintsCbxLifeCycle.gridy = 2;
                constraintsCbxLifeCycle.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCbxLifeCycle.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsCbxLifeCycle.weightx = 1.0;
                constraintsCbxLifeCycle.ipadx = 168;
                constraintsCbxLifeCycle.insets = new java.awt.Insets(3, 6, 4, 30);
                getPnlDetail().add(getCbxLifeCycle(), constraintsCbxLifeCycle);

                java.awt.GridBagConstraints constraintsLblProcess = new java.awt.GridBagConstraints();
                constraintsLblProcess.gridx = 0;
                constraintsLblProcess.gridy = 0;
                constraintsLblProcess.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblProcess.ipadx = 112;
                constraintsLblProcess.insets = new java.awt.Insets(16, 12, 5, 5);
                getPnlDetail().add(getLblProcess(), constraintsLblProcess);

                java.awt.GridBagConstraints constraintsCbxProcess = new java.awt.GridBagConstraints();
                constraintsCbxProcess.gridx = 1;
                constraintsCbxProcess.gridy = 0;
                constraintsCbxProcess.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCbxProcess.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsCbxProcess.weightx = 1.0;
                constraintsCbxProcess.ipadx = 168;
                constraintsCbxProcess.insets = new java.awt.Insets(10, 6, 2, 30);
                getPnlDetail().add(getCbxProcess(), constraintsCbxProcess);

                java.awt.GridBagConstraints constraintsPnlCosts = new java.awt.GridBagConstraints();
                constraintsPnlCosts.gridx = 0;
                constraintsPnlCosts.gridy = 3;
                constraintsPnlCosts.gridwidth = 2;
                constraintsPnlCosts.fill = java.awt.GridBagConstraints.BOTH;
                constraintsPnlCosts.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsPnlCosts.weightx = 1.0;
                constraintsPnlCosts.weighty = 1.0;
                constraintsPnlCosts.insets = new java.awt.Insets(6, 12, 13, 10);
                getPnlDetail().add(getPnlCosts(), constraintsPnlCosts);
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
     * Return the PnlOccurance property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlOccurance() {
        if (ivjPnlOccurance == null) {
            try {
                ivjPnlOccurance = new javax.swing.JPanel();
                ivjPnlOccurance.setName("PnlOccurance");
                ivjPnlOccurance.setLayout(new java.awt.GridBagLayout());

                java.awt.GridBagConstraints constraintsScpOccurance = new java.awt.GridBagConstraints();
                constraintsScpOccurance.gridx = 1;
                constraintsScpOccurance.gridy = 2;
                constraintsScpOccurance.gridwidth = 2;
                constraintsScpOccurance.fill = java.awt.GridBagConstraints.BOTH;
                constraintsScpOccurance.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsScpOccurance.weightx = 1.0;
                constraintsScpOccurance.weighty = 1.0;
                constraintsScpOccurance.ipadx = 629;
                constraintsScpOccurance.ipady = 342;
                constraintsScpOccurance.insets = new java.awt.Insets(5, 15, 6, 9);
                getPnlOccurance().add(getScpOccurance(), constraintsScpOccurance);

                java.awt.GridBagConstraints constraintsLblOccurance = new java.awt.GridBagConstraints();
                constraintsLblOccurance.gridx = 1;
                constraintsLblOccurance.gridy = 1;
                constraintsLblOccurance.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblOccurance.ipadx = 64;
                constraintsLblOccurance.insets = new java.awt.Insets(17, 16, 5, 36);
                getPnlOccurance().add(getLblOccurance(), constraintsLblOccurance);

                java.awt.GridBagConstraints constraintsLblTotalOccurance = new java.awt.GridBagConstraints();
                constraintsLblTotalOccurance.gridx = 1;
                constraintsLblTotalOccurance.gridy = 3;
                constraintsLblTotalOccurance.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblTotalOccurance.ipadx = 89;
                constraintsLblTotalOccurance.insets = new java.awt.Insets(6, 19, 43, 150);
                getPnlOccurance().add(getLblTotalOccurance(), constraintsLblTotalOccurance);

                java.awt.GridBagConstraints constraintsTxtSumOccurance = new java.awt.GridBagConstraints();
                constraintsTxtSumOccurance.gridx = 2;
                constraintsTxtSumOccurance.gridy = 3;
                constraintsTxtSumOccurance.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsTxtSumOccurance.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTxtSumOccurance.weightx = 1.0;
                constraintsTxtSumOccurance.ipadx = 105;
                constraintsTxtSumOccurance.ipady = -4;
                constraintsTxtSumOccurance.insets = new java.awt.Insets(6, 36, 41, 241);
                getPnlOccurance().add(getTxtSumOccurance(), constraintsTxtSumOccurance);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlOccurance;
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
                ivjPnlTotal.setPreferredSize(new java.awt.Dimension(0, 90));
                ivjPnlTotal.setLayout(null);
                getPnlTotal().add(getLblTotalFactCost(), getLblTotalFactCost().getName());
                getPnlTotal().add(getLblTotalPersonalCost(), getLblTotalPersonalCost().getName());
                getPnlTotal().add(getLblTotal(), getLblTotal().getName());
                getPnlTotal().add(getTxtSumFact(), getTxtSumFact().getName());
                getPnlTotal().add(getCbxCurrencyFact(), getCbxCurrencyFact().getName());
                getPnlTotal().add(getTxtSumPersonal(), getTxtSumPersonal().getName());
                getPnlTotal().add(getCbxCurrencyPersonal(), getCbxCurrencyPersonal().getName());
                getPnlTotal().add(getTxtSumTotal(), getTxtSumTotal().getName());
                getPnlTotal().add(getCbxCurrencyTotal(), getCbxCurrencyTotal().getName());
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
    private javax.swing.JScrollPane getScpCost() {
        if (ivjScpCost == null) {
            try {
                ivjScpCost = new javax.swing.JScrollPane();
                ivjScpCost.setName("ScpCost");
                ivjScpCost.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjScpCost.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                getScpCost().setViewportView(getTblCost());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScpCost;
    }

    /**
     * Return the ScpOccurance property value.
     *
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getScpOccurance() {
        if (ivjScpOccurance == null) {
            try {
                ivjScpOccurance = new javax.swing.JScrollPane();
                ivjScpOccurance.setName("ScpOccurance");
                ivjScpOccurance.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjScpOccurance.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                ivjScpOccurance.setMinimumSize(new java.awt.Dimension(22, 80));
                getScpOccurance().setViewportView(getTblOccurance());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScpOccurance;
    }

    /**
     * Return the TblCostDriver property value.
     *
     * @return javax.swing.JTable
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTable getTblCost() {
        if (ivjTblCost == null) {
            try {
                ivjTblCost = new javax.swing.JTable();
                ivjTblCost.setName("TblCost");
                getScpCost().setColumnHeaderView(ivjTblCost.getTableHeader());
                getScpCost().getViewport().setBackingStoreEnabled(true);
                ivjTblCost.setBounds(0, 0, 200, 200);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTblCost;
    }

    /**
     * Return the TblOccurance property value.
     *
     * @return javax.swing.JTable
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTable getTblOccurance() {
        if (ivjTblOccurance == null) {
            try {
                ivjTblOccurance = new javax.swing.JTable();
                ivjTblOccurance.setName("TblOccurance");
                getScpOccurance().setColumnHeaderView(ivjTblOccurance.getTableHeader());
                getScpOccurance().getViewport().setBackingStoreEnabled(true);
                ivjTblOccurance.setBounds(0, 0, 200, 200);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTblOccurance;
    }

    /**
     * Return the JTabbedPane1 property value.
     *
     * @return javax.swing.JTabbedPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTabbedPane getTbpGeneral() {
        if (ivjTbpGeneral == null) {
            try {
                ivjTbpGeneral = new javax.swing.JTabbedPane();
                ivjTbpGeneral.setName("TbpGeneral");
                ivjTbpGeneral.insertTab("Detail", null, getPnlDetail(), null, 0);
                ivjTbpGeneral.insertTab("Standort-Zuordnung", null, getPnlOccurance(), null, 1);
                ivjTbpGeneral.insertTab("Notiz", null, getPnlNote(), null, 2);
                // user code begin {1}
                ivjTbpGeneral.setTitleAt(0, ResourceManager.getResource(ServiceDetailView.class, "PnlDetail_text"));
                ivjTbpGeneral.setTitleAt(1, getResourceString("PnlSite_text"));
                ivjTbpGeneral.setTitleAt(2, ResourceManager.getResource(ServiceDetailView.class, "PnlNote_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbpGeneral;
    }

    /**
     * Return the JTextField1 property value.
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
                ivjTxtSumFact.setBounds(324, 10, 143, 20);
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
     * Return the TxtSumOccurance property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtSumOccurance() {
        if (ivjTxtSumOccurance == null) {
            try {
                ivjTxtSumOccurance = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtSumOccurance.setName("TxtSumOccurance");
                ivjTxtSumOccurance.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumOccurance;
    }

    /**
     * Return the TxtSumPersonal property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSumPersonal() {
        if (ivjTxtSumPersonal == null) {
            try {
                ivjTxtSumPersonal = new javax.swing.JTextField();
                ivjTxtSumPersonal.setName("TxtSumPersonal");
                ivjTxtSumPersonal.setBounds(324, 36, 143, 20);
                ivjTxtSumPersonal.setEditable(false);
                // user code begin {1}
                ivjTxtSumPersonal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumPersonal;
    }

    /**
     * Return the TxtSumTotal property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSumTotal() {
        if (ivjTxtSumTotal == null) {
            try {
                ivjTxtSumTotal = new javax.swing.JTextField();
                ivjTxtSumTotal.setName("TxtSumTotal");
                ivjTxtSumTotal.setBounds(324, 63, 143, 20);
                ivjTxtSumTotal.setEditable(false);
                // user code begin {1}
                ivjTxtSumTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSumTotal;
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
        getTxtName().addKeyListener(ivjEventHandler);
        getPnlStandardToolbar().addToolBarListener(ivjEventHandler);
        getPnlNote().addSimpleEditorPanelListener(ivjEventHandler);
        getCbxProcess().addItemListener(ivjEventHandler);
        getCbxProjectPhase().addItemListener(ivjEventHandler);
        getCbxLifeCycle().addItemListener(ivjEventHandler);
        getMniNewPersonalCost().addActionListener(ivjEventHandler);
        getMniChangeCost().addActionListener(ivjEventHandler);
        getMniRemoveCost().addActionListener(ivjEventHandler);
        getMniCopyCost().addActionListener(ivjEventHandler);
        getMniNewFactCost().addActionListener(ivjEventHandler);
        getScpCost().addMouseListener(ivjEventHandler);
        getTblCost().addMouseListener(ivjEventHandler);
        getScpOccurance().addMouseListener(ivjEventHandler);
        getTblOccurance().addMouseListener(ivjEventHandler);
        getMniNewOccurance().addActionListener(ivjEventHandler);
        getMniChangeOccurance().addActionListener(ivjEventHandler);
        getMniRemoveOccurance().addActionListener(ivjEventHandler);
        getTxtMultitude().addKeyListener(ivjEventHandler);
        connPtoP6SetTarget();
        connPtoP2SetTarget();
        connPtoP1SetTarget();
        connPtoP56SetTarget();
        connPtoP57SetTarget();
        connPtoP58SetTarget();
        connPtoP7SetTarget();
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
            setName("CostDriverDetailView");
            setSize(687, 712);
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setIconImage((new javax.swing.ImageIcon(LauncherView.getInstance().getUtility().getImageURL(CostDriver.class))).getImage()); // setIconImage(ResourceManager.getImageIcon(ModelUtility.class,
        // "CostDriver.png").getImage());
        setTitle(getResourceString("FrmWindow_text")); // ReportTool.getTypeString(object.getClass()));

        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(CostDriver.class));
        getPnlStandardToolbar().setObjects(getObjects());
        UserActionRights rights = getViewOptions().getViewManager().getRights(ProjectPhase.class);
        if (!rights.isSaveObjectAllowed()) {
            getLblPhase().setVisible(false);
            getCbxProjectPhase().setVisible(false);
        }
        rights = getViewOptions().getViewManager().getRights(LifeCycle.class);
        if (!rights.isSaveObjectAllowed()) {
            getLblCycle().setVisible(false);
            getCbxLifeCycle().setVisible(false);
        }
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
        DbTableModel tm = new DbTableModel();
        tm.addColumnEvaluated(getResourceString("TbcType_text"), Formatter.COST_TYPE, 40, null, evaluator);
        tm.addColumn(ch.softenvironment.client.ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblName_text"), "name", 100);//$NON-NLS-1$
        tm.addColumn(getResourceString("TbcCosttype_text"), "cause", 100);//$NON-NLS-1$
        // tm.addColumn("Rolle", "role", 100);
        // tm.addColumn("Aktivitaet", "activity", 100);
        tm.addColumn(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblMultitude_text"), "multitude", 50);
        tm.addColumnAmount(ResourceManager.getResource(ServiceDetailView.class, "PnlCost_text"), "amount", 100);
        tm.addColumnEvaluated(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text"), Formatter.COST_TOTAL, 100, new NumberTableCellRenderer(
            ch.softenvironment.util.AmountFormat.getAmountInstance()), evaluator);
        tm.addColumnEvaluated(ResourceManager.getResource(FactCostDetailView.class, "LblCurrency_text"), "currency", 40, null, evaluator);
        // tm.addColumnEvaluated(ResourceManager.getResource(FactCostDetailView.class, "LblCurrency_text"), Formatter.COSTDRIVER_CURRENCY, 50, null, evaluator);
        tm.addColumn(ResourceManager.getResource(FactCostDetailView.class, "LblRepeatable_text"), "repeatable", 40);
        tm.addColumnEvaluated("%", Formatter.COST_PERCENTAGE, 40, new ch.softenvironment.view.table.NumberTableCellRenderer(
            ReportTool.PERCENTAGE_FRACTION_DIGITS), evaluator);
        tm.addColumn(ResourceManager.getResource(ServiceDetailView.class, "PnlNote_text"), "documentation", 100);
        tm.adjustTable(getTblCost());

        tm = new DbTableModel();
        tm.addColumn(ResourceManager.getResourceAsNonLabeled(OccuranceDetailView.class, "LblSite_text"), "site", 100);//$NON-NLS-1$
        //	tm.addFormattedColumn("Adresse", TcoFormatter.SERVICE_ADDRESS, 100);//$NON-NLS-1$
        tm.addColumn(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblMultitude_text"), "multitude", 50);
        tm.addColumn(ResourceManager.getResource(ServiceDetailView.class, "PnlNote_text"), "documentation", 50);
        tm.adjustTable(getTblOccurance());
    }

    /**
     * Create a new Object (and open it for e.g. in a DetailView).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void newObject(java.lang.Object source) {
        try {
            if (source.equals(getMniNewOccurance())) {
                Occurance occurance = (Occurance) getObject().getObjectServer().createInstance(Occurance.class);
                occurance.setMultitude(Double.valueOf(0.0));

                LauncherView.getInstance().getUtility().addOwnedElement(getObject(), occurance);
                java.util.List<Occurance> objects = ch.softenvironment.util.ListUtils.createList(occurance);
                registerPropertyChangeListener(objects, true); // to recalc sums

                refreshOccurance();

                openOccurance(objects);
            } else {
                Cost cost = null;
                ch.softenvironment.view.BaseFrame view = null;
                java.util.List<Cost> objects = new java.util.ArrayList<Cost>(1);
                ModelUtility utility = LauncherView.getInstance().getUtility();

                if (source.equals(getMniNewFactCost())) {
                    cost = (FactCost) utility.createTcoObject(getObject().getObjectServer(), FactCost.class);
                    objects.add(cost);
                    view = new FactCostDetailView(getViewOptions(), objects, this);
                } else /* if (source.equals(getMniNewPersonalCost())) */ {
                    cost = (PersonalCost) utility.createTcoObject(getObject().getObjectServer(), PersonalCost.class);
                    objects.add(cost);
                    view = new PersonalCostDetailView(getViewOptions(), objects, this);
                }

                utility.addOwnedElement(getObject(), objects.get(0));
                registerPropertyChangeListener(objects, true);

                refreshCost();

                view.setRelativeLocation(this);
                view.setVisible(true);
            }
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
     * @see #dispose()
     */
    private void openOccurance(java.util.List objs) {
        ch.softenvironment.view.BaseFrame view = new OccuranceDetailView(getViewOptions(), objs, this);
        view.setRelativeLocation(this);
        view.setVisible(true);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param event A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent event) {
        try {
            if (event.getPropertyName().equals("amount") || event.getPropertyName().equals("multitude")) {
                refreshCost();
            } else if (event.getPropertyName().equals("occurrance")) {
                refreshOccurance();
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
     * Update List of FactCost & PersonalCost.
     * <p>
     * see Fact/PersonalCostDetailView#propertyChange()
     */
    protected void refreshCost() throws Exception {
        Currency currency = LauncherView.getInstance().getUtility().getSystemParameter().getDefaultCurrency();
        getCbxCurrencyFact().setSelectedItem(currency);
        getCbxCurrencyPersonal().setSelectedItem(currency);
        getCbxCurrencyTotal().setSelectedItem(currency);

        double[] sums = org.tcotool.tools.Calculator.calculate(getObject());
        double total = sums[0] + sums[1];

        evaluator.setTotalCost(total, getObject().getMultitude());

        java.text.NumberFormat af = AmountFormat.getAmountInstance(LauncherView.getInstance().getSettings().getPlattformLocale());
        getTxtSumFact().setText(af.format(sums[0]));
        getTxtSumPersonal().setText(af.format(sums[1]));
        getTxtSumTotal().setText(af.format(total));

        ((DbTableModel) getTblCost().getModel()).setAll(getObject().getCost());

        if (caller != null) {
            ((ServiceDetailView) caller).refreshCosts();
        }
    }

    /**
     * Refresh Site Occurances.
     */
    protected void refreshOccurance() throws Exception {
        ((DbTableModel) getTblOccurance().getModel()).setAll(getObject().getOccurrance());
        getTxtSumOccurance().setText("" + org.tcotool.tools.Calculator.calculateOccuranceTotal(getObject()));
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
                    if (source.equals(getMniRemoveCost())) {
                        java.util.List<Cost> removedItems = DbTableModel.removeSelectedItems(getTblCost(), getViewOptions(), false);
                        // TODO Patch: XmlObjectServer cannot handle #deletePersistent()
                        // getObject().getCost().removeAll(removedItems);
                        java.util.List<Cost> list = new java.util.ArrayList<Cost>(getObject().getCost());
                        list.removeAll(removedItems);
                        getObject().setCost(list);

                        // unlink
                        java.util.Iterator<Cost> iterator = removedItems.iterator();
                        while (iterator.hasNext()) {
                            iterator.next().setDriverId(null);
                        }

                        registerPropertyChangeListener(removedItems, false);

                        refreshCost();
                    } else if (source.equals(getMniRemoveOccurance())) {
                        java.util.List<Occurance> removedItems = DbTableModel.removeSelectedItems(getTblOccurance(), getViewOptions(), false);
                        // getObject().getDriver().removeAll(removedItems);
                        java.util.List<Occurance> list = new java.util.ArrayList<Occurance>(getObject().getOccurrance());
                        list.removeAll(removedItems);
                        getObject().setOccurrance(list);

                        // unlink
                        java.util.Iterator<Occurance> iterator = removedItems.iterator();
                        while (iterator.hasNext()) {
                            iterator.next().setDriverId(null);
                        }

                        registerPropertyChangeListener(removedItems, false);

                        refreshOccurance();
                    }
                } catch (Throwable e) {
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
     * @param object Example Code: try { if ((object != null) && object.equals(getObject())) { return; } if (getObject() != null) {
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

            DbObjectServer server = (((DbObject) object).getObjectServer());
            // 1) common settings
            JComboBoxUtility.initComboBox(getCbxCurrencyFact(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyPersonal(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxCurrencyTotal(), server.retrieveCodes(org.tcotool.model.Currency.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxProjectPhase(), server.retrieveCodes(ProjectPhase.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(
                ModelUtility.getCodeTypeLocale()), JComboBoxUtility.SORT_KEEP_ORDER);
            JComboBoxUtility.initComboBox(getCbxLifeCycle(), server.retrieveCodes(LifeCycle.class), DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(
                ModelUtility.getCodeTypeLocale()), JComboBoxUtility.SORT_KEEP_ORDER);
            JComboBoxUtility.initComboBox(getCbxProcess(), server.retrieveCodes(org.tcotool.model.Process.class), DbObject.PROPERTY_NAME, false,
                new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));

            setObject((CostDriver) object);
            refreshCost();
            refreshOccurance();

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
    private void setObject(org.tcotool.model.CostDriver newValue) {
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
                connPtoP1SetTarget();
                connPtoP56SetTarget();
                connPtoP57SetTarget();
                connPtoP58SetTarget();
                connPtoP7SetTarget();
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
