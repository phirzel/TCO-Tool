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
import org.tcotool.model.TcoModel;

/**
 * DetailView of a TcoPackage.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class ModelDetailView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements ch.softenvironment.view.DetailView {

    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JPanel ivjPnlDetail = null;
    private boolean ivjConnPtoP1Aligning = false;
    private boolean ivjConnPtoP2Aligning = false;
    private javax.swing.JPanel ivjJPanel1 = null;
    private org.tcotool.model.TcoModel ivjObject = null;
    private ch.softenvironment.view.ToolBar ivjPnlStandardToolbar = null;
    private ch.softenvironment.view.StatusBar ivjPnlStatusBar = null;
    private ConsistencyController ivjConsistencyController = null;
    private boolean ivjConnPtoP6Aligning = false;
    private boolean ivjConnPtoP4Aligning = false;
    private javax.swing.JLabel ivjLblMultitude = null;
    private boolean ivjConnPtoP3Aligning = false;
    private boolean ivjConnPtoP5Aligning = false;
    private javax.swing.JTextField ivjTxtAuthor = null;
    private javax.swing.JTextField ivjTxtName = null;
    private javax.swing.JTextField ivjTxtVersion = null;
    private javax.swing.JLabel ivjLblName = null;
    private ch.softenvironment.view.swingext.NumberTextField ivjTxtMultitude = null;
    private javax.swing.JLabel ivjLblAuthor = null;
    private javax.swing.JLabel ivjLblVersion = null;
    private ch.softenvironment.view.SimpleEditorPanel ivjPnlNote = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.KeyListener, java.beans.PropertyChangeListener {

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == ModelDetailView.this.getTxtName()) {
                connPtoP2SetSource();
            }
            if (e.getSource() == ModelDetailView.this.getTxtAuthor()) {
                connPtoP3SetSource();
            }
            if (e.getSource() == ModelDetailView.this.getTxtVersion()) {
                connPtoP5SetSource();
            }
            if (e.getSource() == ModelDetailView.this.getTxtMultitude()) {
                connPtoP4SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }

        @Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == ModelDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("isSaveable"))) {
                connPtoP6SetTarget();
            }
            if (evt.getSource() == ModelDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("tbbSaveEnabled"))) {
                connPtoP6SetSource();
            }
            if (evt.getSource() == ModelDetailView.this.getConsistencyController() && (evt.getPropertyName().equals("inconsistencies"))) {
                connEtoM1(evt);
            }
            if (evt.getSource() == ModelDetailView.this.getPnlStandardToolbar() && (evt.getPropertyName().equals("currentObject"))) {
                connEtoC1(evt);
            }
            if (evt.getSource() == ModelDetailView.this.getObject() && (evt.getPropertyName().equals("documentation"))) {
                connPtoP1SetTarget();
            }
            if (evt.getSource() == ModelDetailView.this.getObject() && (evt.getPropertyName().equals("name"))) {
                connPtoP2SetTarget();
            }
            if (evt.getSource() == ModelDetailView.this.getObject() && (evt.getPropertyName().equals("author"))) {
                connPtoP3SetTarget();
            }
            if (evt.getSource() == ModelDetailView.this.getObject() && (evt.getPropertyName().equals("version"))) {
                connPtoP5SetTarget();
            }
            if (evt.getSource() == ModelDetailView.this.getObject() && (evt.getPropertyName().equals("multitude"))) {
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
            if (newEvent.getSource() == ModelDetailView.this.getPnlStandardToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ModelDetailView.this.getPnlNote()) {
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
    public ModelDetailView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     */
    public ModelDetailView(ch.softenvironment.view.ViewOptions viewOptions, java.util.List objects) {
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
     * connPtoP3SetSource:  (Object.author <--> JTextField3.text)
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
                    getObject().setAuthor(getTxtAuthor().getText());
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
     * connPtoP3SetTarget:  (Object.author <--> JTextField3.text)
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
                    getTxtAuthor().setText(getObject().getAuthor());
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
     * connPtoP5SetSource:  (Object.version <--> JTextField31.text)
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
                    getObject().setVersion(getTxtVersion().getText());
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
     * connPtoP5SetTarget:  (Object.version <--> JTextField31.text)
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
                    getTxtVersion().setText(getObject().getVersion());
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
     * connPtoP7SetTarget:  (Object.mark <--> PnlStatusBar.mark)
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
                getJPanel1().add(getTxtMultitude(), getTxtMultitude().getName());
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
                ivjJTabbedPane1.setBounds(9, 72, 507, 286);
                ivjJTabbedPane1.insertTab("Detail", null, getPnlDetail(), null, 0);
                ivjJTabbedPane1.insertTab("Notiz", null, getPnlNote(), null, 1);
                // user code begin {1}
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
     * Return the JLabel22 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblAuthor() {
        if (ivjLblAuthor == null) {
            try {
                ivjLblAuthor = new javax.swing.JLabel();
                ivjLblAuthor.setName("LblAuthor");
                ivjLblAuthor.setToolTipText("");
                ivjLblAuthor.setText("Autor:");
                ivjLblAuthor.setBounds(12, 25, 136, 14);
                // user code begin {1}
                ivjLblAuthor.setText(getResourceString("LblAuthor_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblAuthor;
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
                ivjLblMultitude.setBounds(17, 41, 131, 14);
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
     * Return the JLabel21 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblVersion() {
        if (ivjLblVersion == null) {
            try {
                ivjLblVersion = new javax.swing.JLabel();
                ivjLblVersion.setName("LblVersion");
                ivjLblVersion.setToolTipText("");
                ivjLblVersion.setText("Version:");
                ivjLblVersion.setBounds(12, 51, 136, 14);
                // user code begin {1}
                ivjLblVersion.setText(getResourceString("LblVersion_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblVersion;
    }

    /**
     * Return the Object property value.
     *
     * @return org.tcotool.model.TcoObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private org.tcotool.model.TcoModel getObject() {
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
                getPnlDetail().add(getLblAuthor(), getLblAuthor().getName());
                getPnlDetail().add(getLblVersion(), getLblVersion().getName());
                getPnlDetail().add(getTxtAuthor(), getTxtAuthor().getName());
                getPnlDetail().add(getTxtVersion(), getTxtVersion().getName());
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
     * Return the JTextField3 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtAuthor() {
        if (ivjTxtAuthor == null) {
            try {
                ivjTxtAuthor = new javax.swing.JTextField();
                ivjTxtAuthor.setName("TxtAuthor");
                ivjTxtAuthor.setBounds(162, 24, 332, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtAuthor;
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
                ivjTxtMultitude.setBounds(163, 36, 73, 20);
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
                ivjTxtName.setBounds(163, 13, 328, 20);
                ivjTxtName.setEditable(false);
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
     * Return the JTextField31 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtVersion() {
        if (ivjTxtVersion == null) {
            try {
                ivjTxtVersion = new javax.swing.JTextField();
                ivjTxtVersion.setName("TxtVersion");
                ivjTxtVersion.setBounds(162, 49, 113, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtVersion;
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
        getTxtAuthor().addKeyListener(ivjEventHandler);
        getTxtVersion().addKeyListener(ivjEventHandler);
        getTxtMultitude().addKeyListener(ivjEventHandler);
        connPtoP6SetTarget();
        connPtoP1SetTarget();
        connPtoP2SetTarget();
        connPtoP3SetTarget();
        connPtoP5SetTarget();
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
            setName("ModelDetailView");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(528, 452);
            setTitle("TCO-Konfiguration");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        setIconImage(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png").getImage());
        setConsistencyController(new ch.softenvironment.jomm.mvc.controller.ConsistencyController(this));
        getPnlStandardToolbar().adaptRights(getViewOptions().getViewManager().getRights(TcoModel.class));
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
                getObject().removePropertyChangeListener(getConsistencyController());
            }

            setObject((org.tcotool.model.TcoModel) object);

            getObject().addPropertyChangeListener(getConsistencyController());
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
    private void setObject(org.tcotool.model.TcoModel newValue) {
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
