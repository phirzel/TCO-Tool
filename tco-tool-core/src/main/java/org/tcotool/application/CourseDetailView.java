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
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.controller.ConsistencyController;
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.controller.DbObjectValidator;
import ch.softenvironment.jomm.mvc.controller.DbTableModel;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.view.CommonUserAccess;
import ch.softenvironment.view.swingext.JComboBoxUtility;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import org.tcotool.model.Course;
import org.tcotool.model.SystemParameter;
import org.tcotool.model.TcoModel;
import org.tcotool.tools.ModelUtility;

/**
 * DetailView of a TcoPackage.
 *
 * @author Peter Hirzel
 */

public class CourseDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements DbObjectValidator, ch.softenvironment.view.DetailView,
    ch.softenvironment.view.SearchView {

    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjJPanel1 = null;
    private org.tcotool.model.Course ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private javax.swing.JComboBox ivjCbxSource = null;
    private javax.swing.JComboBox ivjCbxTarget = null;
    private javax.swing.JLabel ivjLblFactor = null;
    private javax.swing.JLabel ivjLblSource = null;
    private javax.swing.JLabel ivjLblTarget = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtFactor = null;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JPopupMenu ivjJPopupMenu1 = null;
    private javax.swing.JMenuItem ivjMniNew = null;
    private javax.swing.JMenuItem ivjMniRemove = null;
    private javax.swing.JScrollPane ivjScpCourses = null;
    private javax.swing.JTable ivjTblCourses = null;
    private boolean ivjConnPtoP1Aligning = false;
    private boolean ivjConnPtoP2Aligning = false;
    private javax.swing.JPanel ivjPnlDetails = null;

    class IvjEventHandler implements ch.softenvironment.view.ToolBarListener, java.awt.event.ActionListener, java.awt.event.ItemListener,
        java.awt.event.KeyListener, java.awt.event.MouseListener, java.beans.PropertyChangeListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == CourseDetailView.this.getMniNew()) {
                connEtoC2(e);
            }
            if (e.getSource() == CourseDetailView.this.getMniRemove()) {
                connEtoC3(e);
            }
        }

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == CourseDetailView.this.getCbxSource()) {
                connPtoP1SetSource();
            }
            if (e.getSource() == CourseDetailView.this.getCbxTarget()) {
                connPtoP2SetSource();
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == CourseDetailView.this.getTxtFactor()) {
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
            if (e.getSource() == CourseDetailView.this.getTblCourses()) {
                connEtoC6(e);
            }
            if (e.getSource() == CourseDetailView.this.getScpCourses()) {
                connEtoC8(e);
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.getSource() == CourseDetailView.this.getTblCourses()) {
                connEtoC4(e);
            }
            if (e.getSource() == CourseDetailView.this.getScpCourses()) {
                connEtoC5(e);
            }
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == CourseDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == CourseDetailView.this.getObject() && (evt.getPropertyName().equals("factor"))) {
                connPtoP4SetTarget();
            }
            if (evt.getSource() == CourseDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == CourseDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == CourseDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == CourseDetailView.this.getObject() && (evt.getPropertyName().equals("source"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == CourseDetailView.this.getObject() && (evt.getPropertyName().equals("target"))) {
                connPtoP2SetTarget();
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
            if (newEvent.getSource() == CourseDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

    }

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public CourseDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public CourseDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects) {
        super(viewOptions, objects);

        initialize();
    }

    @Override
    public void adaptUserAction(EventObject event, Object control) {
        try {
            if (getTblCourses().getSelectedRow() >= 0) {
                getPnlStandardToolbar().setTbbRemoveEnabled(true);
                getPnlStandardToolbar().setTbbChangeEnabled(true);

                // getMniChange().setEnabled(true);
                // getMniCopy().setEnabled(getTblSearchResults().getSelectedRowCount() == 1);
                getMniRemove().setEnabled(true);
            } else {
                getPnlStandardToolbar().setTbbRemoveEnabled(false);
                getPnlStandardToolbar().setTbbChangeEnabled(false);

                // getMniChange().setEnabled(false);
                // getMniCopy().setEnabled(false);
                getMniRemove().setEnabled(false);
            }
            changeObjects(null);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void assignObjects() {
        // nothing to do
    }

    /**
     * Assign a set of aggregates given in objects.
     *
     * @param objects
     */
    @Override
    public void assignObjects(java.util.List objects) {
    }

    @Override
    public void changeObjects(Object source) {
        try {
            if (getObject() != null) {
                getObject().removePropertyChangeListener(getConsistencyController());
            }

            java.util.List items = DbTableModel.getSelectedItems(getTblCourses());
            if (items.size() > 0) {
                getTxtFactor().setEditable(true);
                // getCbxSource().setEnabled(true);
                getCbxTarget().setEnabled(true);
                setObject((org.tcotool.model.Course) items.get(0));
                getObject().addPropertyChangeListener(getConsistencyController());
            } else {
                getTxtFactor().setEditable(false);
                // getCbxSource().setEnabled(false);
                getCbxTarget().setEnabled(false);
                setObject(null);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public String checkConsistency(DbPropertyChange change) {
        try {
            getPnlStatusBar().setStatus(null);

            if ((change.getSource() != null) && (change.getSource() instanceof Course)) {
                Course course = (Course) change.getSource();
                if (course.getSource() != null) {
                    if (course.getSource().equals(course.getTarget())) {
                        return getResourceString("CWCoursesMustDiffer");
                    }
                    if (course.getTarget() != null) {
                        int ref = 0;
                        Iterator<Course> it = LauncherView.getInstance().getUtility().getSystemParameter().getCourse().iterator();
                        while (it.hasNext()) {
                            Course tmp = it.next();
                            if ((tmp.getSource() == null) || (tmp.getTarget() == null)) {
                                // probably a new course
                                return null; // to early
                            }
                            if (tmp.getSource().equals(course.getSource()) && tmp.getTarget().equals(course.getTarget())) {
                                if (++ref > 1) {
                                    return getResourceString("CWCourseExisting");
                                }
                            }
                            if (tmp.getTarget().equals(course.getSource()) && tmp.getSource().equals(course.getTarget())) {
                                if (++ref > 1) {
                                    return getResourceString("CWCourseExistingReversed");
                                }
                            }
                        }
                        if (course.getFactor() != null) {
                            getPnlStatusBar().setStatus(
                                "1.0" + course.getSource().getNameString() + " = " + course.getFactor() + course.getTarget().getNameString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return null;
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
     * connEtoC2: (MniNew.action.actionPerformed(java.awt.event.ActionEvent) --> CourseDetailView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniNew());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (MniRemove.action.actionPerformed(java.awt.event.ActionEvent) --> CourseDetailView.removeObjects(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.removeObjects(getMniRemove());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC4: (ScrollPaneTable.mouse.mouseReleased(java.awt.event.MouseEvent) --> CourseDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getJPopupMenu1());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5: (JScrollPane1.mouse.mouseReleased(java.awt.event.MouseEvent) --> CourseDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getJPopupMenu1());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC6: (ScrollPaneTable.mouse.mousePressed(java.awt.event.MouseEvent) --> CourseDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC6(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getJPopupMenu1());
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
     * connEtoC8: (JScrollPane1.mouse.mousePressed(java.awt.event.MouseEvent) --> CourseDetailView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax.swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getJPopupMenu1());
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
     * connPtoP1SetSource: (Object.source <--> CbxSource.selectedItem)
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
                    getObject().setSource((org.tcotool.model.Currency) getCbxSource().getSelectedItem());
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
     * connPtoP1SetTarget: (Object.source <--> CbxSource.selectedItem)
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
                    getCbxSource().setSelectedItem(getObject().getSource());
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
     * connPtoP2SetSource: (Object.target <--> CbxTarget.selectedItem)
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
                    getObject().setTarget((org.tcotool.model.Currency) getCbxTarget().getSelectedItem());
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
     * connPtoP2SetTarget: (Object.target <--> CbxTarget.selectedItem)
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
                    getCbxTarget().setSelectedItem(getObject().getTarget());
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
     * connPtoP3SetTarget: (Object.mark <--> PnlStatusBar.mark)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP3SetTarget() {
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
     * connPtoP4SetSource: (Object.factor <--> TxtFactor.text)
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
                        getObject().setFactor(new java.lang.Double(getTxtFactor().getText()));
                    }
                    // user code begin {2}
                } catch (RuntimeException e) {
                    getObject().setFactor(getTxtFactor().getDoubleValue());
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
     * connPtoP4SetTarget: (Object.factor <--> TxtFactor.text)
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
                    getTxtFactor().setText(String.valueOf(getObject().getFactor()));
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
     * Create a new Object as a "copy" of a selected one (and open it for e.g. in a DetailView).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void copyObject(java.lang.Object source) {
    }

    @Override
    public void dispose() {
        getConsistencyController().removeValidator(this);
        if (getObject() != null) {
            getObject().removePropertyChangeListener(getConsistencyController());
        }
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
     * Return the CbxSource property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxSource() {
        if (ivjCbxSource == null) {
            try {
                ivjCbxSource = new javax.swing.JComboBox();
                ivjCbxSource.setName("CbxSource");
                ivjCbxSource.setBounds(204, 19, 88, 23);
                // user code begin {1}
                ivjCbxSource.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxSource;
    }

    /**
     * Return the CbxTarget property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxTarget() {
        if (ivjCbxTarget == null) {
            try {
                ivjCbxTarget = new javax.swing.JComboBox();
                ivjCbxTarget.setName("CbxTarget");
                ivjCbxTarget.setBounds(204, 47, 88, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxTarget;
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

                java.awt.GridBagConstraints constraintsScpCourses = new java.awt.GridBagConstraints();
                constraintsScpCourses.gridx = 1;
                constraintsScpCourses.gridy = 1;
                constraintsScpCourses.fill = java.awt.GridBagConstraints.BOTH;
                constraintsScpCourses.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsScpCourses.weightx = 1.0;
                constraintsScpCourses.weighty = 1.0;
                constraintsScpCourses.ipadx = 496;
                constraintsScpCourses.ipady = 127;
                constraintsScpCourses.insets = new java.awt.Insets(10, 13, 7, 13);
                getJPanel1().add(getScpCourses(), constraintsScpCourses);

                java.awt.GridBagConstraints constraintsPnlDetails = new java.awt.GridBagConstraints();
                constraintsPnlDetails.gridx = 1;
                constraintsPnlDetails.gridy = 2;
                constraintsPnlDetails.fill = java.awt.GridBagConstraints.BOTH;
                constraintsPnlDetails.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsPnlDetails.weightx = 1.0;
                constraintsPnlDetails.weighty = 1.0;
                constraintsPnlDetails.ipadx = 518;
                constraintsPnlDetails.ipady = 121;
                constraintsPnlDetails.insets = new java.awt.Insets(8, 13, 15, 13);
                getJPanel1().add(getPnlDetails(), constraintsPnlDetails);
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
     * Return the JPopupMenu1 property value.
     *
     * @return javax.swing.JPopupMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPopupMenu getJPopupMenu1() {
        if (ivjJPopupMenu1 == null) {
            try {
                ivjJPopupMenu1 = new javax.swing.JPopupMenu();
                ivjJPopupMenu1.setName("JPopupMenu1");
                ivjJPopupMenu1.add(getMniNew());
                ivjJPopupMenu1.add(getMniRemove());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPopupMenu1;
    }

    /**
     * Return the LblFactor property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblFactor() {
        if (ivjLblFactor == null) {
            try {
                ivjLblFactor = new javax.swing.JLabel();
                ivjLblFactor.setName("LblFactor");
                ivjLblFactor.setToolTipText("");
                ivjLblFactor.setText("Faktor (Ziel = Quelle * Faktor):");
                ivjLblFactor.setBounds(16, 81, 182, 14);
                // user code begin {1}
                ivjLblFactor.setText(getResourceString("LblFactor_text"));
                ivjLblFactor.setToolTipText(getResourceString("LblFactor_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblFactor;
    }

    /**
     * Return the LblSource property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSource() {
        if (ivjLblSource == null) {
            try {
                ivjLblSource = new javax.swing.JLabel();
                ivjLblSource.setName("LblSource");
                ivjLblSource.setText("Quell-Waehrung:");
                ivjLblSource.setBounds(16, 22, 182, 14);
                // user code begin {1}
                ivjLblSource.setText(getResourceString("LblSource_text"));
                ivjLblSource.setToolTipText(NlsUtils.formatMessage(getResourceString("LblSource_toolTipText"),
                    ResourceManager.getResource(SystemParameterDetailView.class, "FrmWindow_text")));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSource;
    }

    /**
     * Return the LblTarget property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTarget() {
        if (ivjLblTarget == null) {
            try {
                ivjLblTarget = new javax.swing.JLabel();
                ivjLblTarget.setName("LblTarget");
                ivjLblTarget.setText("Ziel-Waehrung:");
                ivjLblTarget.setBounds(16, 50, 182, 14);
                // user code begin {1}
                ivjLblTarget.setText(getResourceString("LblTarget_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTarget;
    }

    /**
     * Return the MniNew property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniNew() {
        if (ivjMniNew == null) {
            try {
                ivjMniNew = new javax.swing.JMenuItem();
                ivjMniNew.setName("MniNew");
                ivjMniNew.setText("Neu");
                // user code begin {1}
                ivjMniNew.setText(CommonUserAccess.getMniFileNewText());
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
                ivjMniRemove.setText("Loeschen");
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
     * Return the Object property value.
     *
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.Course getObject() {
        // user code begin {1}
        // user code end
        return ivjObject;
    }

    /**
     * Return the JPanel2 property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlDetails() {
        if (ivjPnlDetails == null) {
            try {
                ivjPnlDetails = new javax.swing.JPanel();
                ivjPnlDetails.setName("PnlDetails");
                ivjPnlDetails.setLayout(null);
                getPnlDetails().add(getLblSource(), getLblSource().getName());
                getPnlDetails().add(getLblTarget(), getLblTarget().getName());
                getPnlDetails().add(getLblFactor(), getLblFactor().getName());
                getPnlDetails().add(getCbxSource(), getCbxSource().getName());
                getPnlDetails().add(getCbxTarget(), getCbxTarget().getName());
                getPnlDetails().add(getTxtFactor(), getTxtFactor().getName());
                // user code begin {1}
                getPnlDetails().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlDetails_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));

                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlDetails;
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
     * Return the JScrollPane1 property value.
     *
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getScpCourses() {
        if (ivjScpCourses == null) {
            try {
                ivjScpCourses = new javax.swing.JScrollPane();
                ivjScpCourses.setName("ScpCourses");
                ivjScpCourses.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjScpCourses.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                getScpCourses().setViewportView(getTblCourses());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScpCourses;
    }

    /**
     * Return the ScrollPaneTable property value.
     *
     * @return javax.swing.JTable
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTable getTblCourses() {
        if (ivjTblCourses == null) {
            try {
                ivjTblCourses = new javax.swing.JTable();
                ivjTblCourses.setName("TblCourses");
                getScpCourses().setColumnHeaderView(ivjTblCourses.getTableHeader());
                getScpCourses().getViewport().setBackingStoreEnabled(true);
                ivjTblCourses.setBounds(0, 0, 200, 200);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTblCourses;
    }

    /**
     * Return the TxtMultitude property value.
     *
     * @return ch.softenvironment.view.swingext.NumberTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.swingext.NumberTextField getTxtFactor() {
        if (ivjTxtFactor == null) {
            try {
                ivjTxtFactor = new ch.softenvironment.view.swingext.NumberTextField();
                ivjTxtFactor.setName("TxtFactor");
                ivjTxtFactor.setBounds(204, 76, 143, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtFactor;
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
        getTxtFactor().addKeyListener(ivjEventHandler);
        getMniNew().addActionListener(ivjEventHandler);
        getMniRemove().addActionListener(ivjEventHandler);
        getTblCourses().addMouseListener(ivjEventHandler);
        getScpCourses().addMouseListener(ivjEventHandler);
        getCbxSource().addItemListener(ivjEventHandler);
        getCbxTarget().addItemListener(ivjEventHandler);
        connPtoP3SetTarget();
        connPtoP4SetTarget();
        connPtoP6SetTarget();
        connPtoP1SetTarget();
        connPtoP2SetTarget();
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
            setName("PackageDetailView");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(544, 388);
            setTitle("Kurs");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        setIconImage(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png").getImage());
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getConsistencyController().addValidator(this);
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(Course.class));
        getPnlStandardToolbar().setObjects(getObjects());
        // user code end
    }

    @Override
    public void initializeView() throws Exception {
        DbTableModel tm = new DbTableModel();
        tm.addColumnCodeType(ResourceManager.getResourceAsNonLabeled(CourseDetailView.class, "LblSource_text"), "source", 30, null);
        tm.addColumnCodeType(ResourceManager.getResourceAsNonLabeled(CourseDetailView.class, "LblTarget_text"), "target", 30, null);
        tm.addColumn(ResourceManager.getResourceAsNonLabeled(CourseDetailView.class, "LblFactor_text"), "factor", 80);//$NON-NLS-1$
        tm.adjustTable(getTblCourses());
    }

    /**
     *
     */
    @Override
    public void newObject(Object source) {
        try {
            Course course = (Course) ModelUtility
                .createDbObject(((TcoModel) LauncherView.getInstance().getUtility().getRoot()).getObjectServer(), Course.class);
            course.setSource(LauncherView.getInstance().getUtility().getSystemParameter().getDefaultCurrency());
            LauncherView.getInstance().getUtility().addOwnedElement(LauncherView.getInstance().getUtility().getSystemParameter(), course);
            ((DbTableModel) getTblCourses().getModel()).setAll(LauncherView.getInstance().getUtility().getSystemParameter().getCourse());
            LauncherView.getInstance().setModelChanged(true);
            getPnlStandardToolbar().setTbbSaveEnabled(true);
            changeObjects(null);
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
     *
     */
    @Override
    public void removeObjects(Object source) {
        try {
            LauncherView.getInstance().getUtility().removeCourse(DbTableModel.getSelectedItems(getTblCourses()));
            ((DbTableModel) getTblCourses().getModel()).setAll(LauncherView.getInstance().getUtility().getSystemParameter().getCourse());
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void resetSearchArguments() {
    }

    /**
     * Save an Object represented by DetailView.
     */
    @Override
    public void saveObject() {
        LauncherView.getInstance().saveObject();
        // closeOnSave();
    }

    @Override
    public void searchObjects() {
        // TODO Auto-generated method stub

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
     * @param object
     */
    @Override
    public void setCurrentObject(java.lang.Object object) {
        try {
            // object represents the table-list here
            ((DbTableModel) getTblCourses().getModel()).setAll(((SystemParameter) object).getCourse());
            List currencies = ((TcoModel) LauncherView.getInstance().getUtility().getRoot()).getObjectServer().retrieveCodes(org.tcotool.model.Currency.class);
            JComboBoxUtility.initComboBox(getCbxSource(), currencies, DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            JComboBoxUtility.initComboBox(getCbxTarget(), currencies, DbObject.PROPERTY_NAME, false, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()));
            changeObjects(null);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Set the Object to a new value.
     *
     * @param newValue org.tcotool.model.Course
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void setObject(org.tcotool.model.Course newValue) {
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
                connPtoP4SetTarget();
                connPtoP1SetTarget();
                connPtoP2SetTarget();
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
