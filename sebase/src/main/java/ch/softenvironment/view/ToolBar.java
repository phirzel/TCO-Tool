package ch.softenvironment.view;

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
import ch.softenvironment.controller.DataBrowser;
import ch.softenvironment.controller.DataBrowserListener;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;

/**
 * Standard JToolbar extended by: - default object functions (new, save, remove, etc) - manage a multiple Object-List to browse - Inconsistency-list of current object
 *
 * @author Peter Hirzel
 */

@Slf4j
public class ToolBar extends javax.swing.JToolBar implements DataBrowserListener {

    private javax.swing.JButton ivjTbbCopy = null;
    private javax.swing.JButton ivjTbbCut = null;
    private javax.swing.JButton ivjTbbOpen = null;
    private javax.swing.JButton ivjTbbPaste = null;
    private javax.swing.JButton ivjTbbRedo = null;
    private javax.swing.JButton ivjTbbSave = null;
    private javax.swing.JButton ivjTbbUndo = null;
    private javax.swing.JButton ivjTbbFind = null;
    protected transient ch.softenvironment.view.ToolBarListener fieldToolBarListenerEventMulticaster = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JButton ivjTbbDelete = null;
    private javax.swing.JButton ivjTbbPrint = null;
    private javax.swing.JComboBox ivjCbxItems = null;
    private java.lang.Object fieldCurrentObject = null;
    private javax.swing.JButton ivjTbbNext = null;
    private javax.swing.JButton ivjTbbPrevious = null;
    private javax.swing.JButton ivjTbbNew = null;
    private javax.swing.JButton ivjTbbFirst = null;
    private javax.swing.JButton ivjTbbLast = null;
    private javax.swing.JLabel ivjLblSelector = null;
    private final DataBrowser browser = new DataBrowser();

    class IvjEventHandler implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ToolBar.this.getTbbOpen()) {
                connEtoC1(e);
            }
            if (e.getSource() == ToolBar.this.getTbbSave()) {
                connEtoC2(e);
            }
            if (e.getSource() == ToolBar.this.getTbbCut()) {
                connEtoC3(e);
            }
            if (e.getSource() == ToolBar.this.getTbbCopy()) {
                connEtoC4(e);
            }
            if (e.getSource() == ToolBar.this.getTbbPaste()) {
                connEtoC5(e);
            }
            if (e.getSource() == ToolBar.this.getTbbUndo()) {
                connEtoC6(e);
            }
            if (e.getSource() == ToolBar.this.getTbbRedo()) {
                connEtoC7(e);
            }
            if (e.getSource() == ToolBar.this.getTbbFind()) {
                connEtoC8(e);
            }
            if (e.getSource() == ToolBar.this.getTbbDelete()) {
                connEtoC9(e);
            }
            if (e.getSource() == ToolBar.this.getTbbPrint()) {
                connEtoC10(e);
            }
            if (e.getSource() == ToolBar.this.getTbbNext()) {
                connEtoC11(e);
            }
            if (e.getSource() == ToolBar.this.getTbbPrevious()) {
                connEtoC12(e);
            }
            if (e.getSource() == ToolBar.this.getTbbNew()) {
                connEtoC13(e);
            }
            if (e.getSource() == ToolBar.this.getTbbLast()) {
                connEtoC14(e);
            }
            if (e.getSource() == ToolBar.this.getTbbFirst()) {
                connEtoC15(e);
            }
        }
    }

    /**
     * ToolBar constructor comment.
     */
    public ToolBar() {
        super();
        initialize();
    }

    /**
     * Set Callback's if new, save, print, etc is clicked in Toolbar.
     *
     * @param newListener ch.softenvironment.view.ToolBarListener
     */
    public void addToolBarListener(ch.softenvironment.view.ToolBarListener newListener) {
        fieldToolBarListenerEventMulticaster = ch.softenvironment.view.ToolBarListenerEventMulticaster.add(fieldToolBarListenerEventMulticaster, newListener);
    }

    /**
     * connEtoC1: (TbbOpen.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbOpenAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbOpenAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC10: (TbbPrint.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbPrintAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC10(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbPrintAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC11: (TbbPrevious.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.getNext()Ljava.lang.Object;)
     *
     * @param arg1 java.awt.event.ActionEvent
     * @return java.lang.Object
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private java.lang.Object connEtoC11(java.awt.event.ActionEvent arg1) {
        Object connEtoC11Result = null;
        try {
            // user code begin {1}
            // user code end
            connEtoC11Result = this.getNext();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
        return connEtoC11Result;
    }

    /**
     * connEtoC12: (TbbNext.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.getPrevious()Ljava.lang.Object;)
     *
     * @param arg1 java.awt.event.ActionEvent
     * @return java.lang.Object
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private java.lang.Object connEtoC12(java.awt.event.ActionEvent arg1) {
        Object connEtoC12Result = null;
        try {
            // user code begin {1}
            // user code end
            connEtoC12Result = this.getPrevious();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
        return connEtoC12Result;
    }

    /**
     * connEtoC13: (TbbNew.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbNewAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC13(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbNewAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC14: (TbbLast.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.getLast()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC14(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.getLast();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC15: (TbbFirst.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.getFirst()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC15(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.getFirst();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (TbbSave.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbSaveAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbSaveAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (TbbCut.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbCutAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbCutAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC4: (TbbCopy.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbCopyAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbCopyAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5: (TbbPaste.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbPasteAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbPasteAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC6: (TbbUndo.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbUndoAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbUndoAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC7: (TbbRedo.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbRedoAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC7(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbRedoAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC8: (TbbFind.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbFindAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbFindAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC9: (TbbDelete.action.actionPerformed(java.awt.event.ActionEvent) --> ToolBar.fireTbbDeleteAction_actionPerformed(Ljava.util.EventObject;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC9(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.fireTbbDeleteAction_actionPerformed(new java.util.EventObject(this));
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    @Override
    public void removeNotify() {
        browser.removeListener(this);
        super.removeNotify();
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbCopyAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbCopyAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbCutAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbCutAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbDeleteAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbDeleteAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbFindAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbFindAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbNewAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbNewAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbOpenAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbOpenAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbPasteAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbPasteAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbPrintAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbPrintAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbRedoAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbRedoAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbSaveAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbSaveAction_actionPerformed(newEvent);
    }

    /**
     * Method to support listener events.
     *
     * @param newEvent java.util.EventObject
     */
    protected void fireTbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldToolBarListenerEventMulticaster == null) {
            return;
        }
        fieldToolBarListenerEventMulticaster.tbbUndoAction_actionPerformed(newEvent);
    }

    /**
     * Return the CbxItems property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxItems() {
        if (ivjCbxItems == null) {
            try {
                ivjCbxItems = new javax.swing.JComboBox();
                ivjCbxItems.setName("CbxItems");
                ivjCbxItems.setToolTipText("");
                ivjCbxItems.setVisible(true);
                ivjCbxItems.setMaximumSize(new java.awt.Dimension(300, 23));
                ivjCbxItems.setForeground(java.awt.Color.red);
                // user code begin {1}
                getCbxItems().setVisible(false);
                ivjCbxItems.setMaximumSize(new java.awt.Dimension(500, 23));
                ivjCbxItems.setToolTipText(ResourceManager.getResource(ToolBar.class, "CbxItems_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxItems;
    }

    /**
     * Gets the currentObject property (java.lang.Object) value.
     *
     * @return The currentObject property value.
     * @see #setCurrentObject(Object)
     */
    public java.lang.Object getCurrentObject() {
        return browser.getCurrentObject(); // return fieldCurrentObject;
    }

    /**
     * Return the first of all objects.
     */
    public Object getFirst() {
        if (browser.isScrollFirstAllowed()) {
            browser.getFirst();
        } else {
            throw new IndexOutOfBoundsException("no first object!");
        }
        return getCurrentObject();
    }

    /**
     * Return the last of all Objects.
     */
    public Object getLast() {
        if (browser.isScrollLastAllowed()) {
            browser.getLast();
        } else {
            throw new IndexOutOfBoundsException("no last object!");
        }
        return getCurrentObject();
    }

    /**
     * Return the LblSelector property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSelector() {
        if (ivjLblSelector == null) {
            try {
                ivjLblSelector = new javax.swing.JLabel();
                ivjLblSelector.setName("LblSelector");
                ivjLblSelector.setText("");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSelector;
    }

    /**
     * Set next Object as currentObject.
     */
    public Object getNext() {
        if (browser.isScrollNextAllowed()) {
            browser.getNext();
        } else {
            throw new IndexOutOfBoundsException("no next object!");
        }
        return getCurrentObject();
    }

    /**
     * Set previous Object as currentObject.
     */
    public Object getPrevious() {
        if (browser.isScrollPreviousAllowed()) {
            browser.getPrevious();
        } else {
            throw new IndexOutOfBoundsException("no previous object!");
        }
        return getCurrentObject();
    }

    /**
     * Return the TbbCopy property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbCopy() {
        if (ivjTbbCopy == null) {
            try {
                ivjTbbCopy = new javax.swing.JButton();
                ivjTbbCopy.setName("TbbCopy");
                ivjTbbCopy.setToolTipText("Copy");
                ivjTbbCopy.setText("");
                ivjTbbCopy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbCopy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbCopy.setIcon(null);
                ivjTbbCopy.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbCopy.setEnabled(false);
                // user code begin {1}
                ivjTbbCopy.setIcon(CommonUserAccess.getIconCopy());
                ivjTbbCopy.setToolTipText(CommonUserAccess.getMniEditCopyText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbCopy;
    }

    /**
     * Method generated to support the promotion of the tbbCopyEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbCopyEnabled() {
        return getTbbCopy().isEnabled();
    }

    /**
     * Return the TbbCut property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbCut() {
        if (ivjTbbCut == null) {
            try {
                ivjTbbCut = new javax.swing.JButton();
                ivjTbbCut.setName("TbbCut");
                ivjTbbCut.setToolTipText("Cut");
                ivjTbbCut.setText("");
                ivjTbbCut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbCut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbCut.setIcon(null);
                ivjTbbCut.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbCut.setEnabled(false);
                // user code begin {1}
                ivjTbbCut.setIcon(CommonUserAccess.getIconCut());
                ivjTbbCut.setToolTipText(CommonUserAccess.getMniEditCutText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbCut;
    }

    /**
     * Method generated to support the promotion of the tbbCutEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbCutEnabled() {
        return getTbbCut().isEnabled();
    }

    /**
     * Return the TbbDelete property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbDelete() {
        if (ivjTbbDelete == null) {
            try {
                ivjTbbDelete = new javax.swing.JButton();
                ivjTbbDelete.setName("TbbDelete");
                ivjTbbDelete.setToolTipText("Delete");
                ivjTbbDelete.setText("");
                ivjTbbDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbDelete.setIcon(null);
                ivjTbbDelete.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbDelete.setEnabled(false);
                // user code begin {1}
                ivjTbbDelete.setIcon(CommonUserAccess.getIconRemove());
                ivjTbbDelete.setToolTipText(CommonUserAccess.getMniEditRemoveText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbDelete;
    }

    /**
     * Method generated to support the promotion of the tbbDeleteEnabled attribute.
     *
     * @return boolean
     * @deprecated (use { @ link # getTbbRemoveEnabled () } instead)
     */
    public boolean getTbbDeleteEnabled() {
        return getTbbRemoveEnabled();
    }

    public boolean getTbbRemoveEnabled() {
        return getTbbDelete().isEnabled();
    }

    /**
     * Return the TbbFindReplace property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbFind() {
        if (ivjTbbFind == null) {
            try {
                ivjTbbFind = new javax.swing.JButton();
                ivjTbbFind.setName("TbbFind");
                ivjTbbFind.setToolTipText("Find");
                ivjTbbFind.setText("");
                ivjTbbFind.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbFind.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbFind.setIcon(null);
                ivjTbbFind.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbFind.setEnabled(false);
                // user code begin {1}
                ivjTbbFind.setIcon(CommonUserAccess.getIconFind());
                ivjTbbFind.setToolTipText(CommonUserAccess.getMniEditFindText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbFind;
    }

    /**
     * Method generated to support the promotion of the tbbFindEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbFindEnabled() {
        return getTbbFind().isEnabled();
    }

    /**
     * Return the TbbFirst property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbFirst() {
        if (ivjTbbFirst == null) {
            try {
                ivjTbbFirst = new javax.swing.JButton();
                ivjTbbFirst.setName("TbbFirst");
                ivjTbbFirst.setToolTipText("First");
                ivjTbbFirst.setText("");
                ivjTbbFirst.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbFirst.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbFirst.setIcon(null);
                ivjTbbFirst.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbFirst.setEnabled(false);
                // user code begin {1}
                ivjTbbFirst.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(ToolBar.class, "first_arrow.gif"));
                ivjTbbFirst.setToolTipText(ResourceManager.getResource(DataSelectorPanel.class, "TbbFirst_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbFirst;
    }

    /**
     * Return the TbbLast property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbLast() {
        if (ivjTbbLast == null) {
            try {
                ivjTbbLast = new javax.swing.JButton();
                ivjTbbLast.setName("TbbLast");
                ivjTbbLast.setToolTipText("Last");
                ivjTbbLast.setText("");
                ivjTbbLast.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbLast.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbLast.setIcon(null);
                ivjTbbLast.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbLast.setEnabled(false);
                // user code begin {1}
                ivjTbbLast.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(ToolBar.class, "last_arrow.gif"));
                ivjTbbLast.setToolTipText(ResourceManager.getResource(DataSelectorPanel.class, "TbbLast_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbLast;
    }

    /**
     * Return the TbbNew property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbNew() {
        if (ivjTbbNew == null) {
            try {
                ivjTbbNew = new javax.swing.JButton();
                ivjTbbNew.setName("TbbNew");
                ivjTbbNew.setToolTipText("New");
                ivjTbbNew.setText("");
                ivjTbbNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbNew.setIcon(null);
                ivjTbbNew.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbNew.setEnabled(false);
                // user code begin {1}
                ivjTbbNew.setIcon(CommonUserAccess.getIconNew());
                ivjTbbNew.setToolTipText(CommonUserAccess.getMniFileNewWindowText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbNew;
    }

    /**
     * Method generated to support the promotion of the tbbNewEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbNewEnabled() {
        return getTbbNew().isEnabled();
    }

    /**
     * Return the TbbSave11 property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbNext() {
        if (ivjTbbNext == null) {
            try {
                ivjTbbNext = new javax.swing.JButton();
                ivjTbbNext.setName("TbbNext");
                ivjTbbNext.setToolTipText("Next");
                ivjTbbNext.setText("");
                ivjTbbNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbNext.setIcon(null);
                ivjTbbNext.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbNext.setEnabled(false);
                // user code begin {1}
                ivjTbbNext.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(ToolBar.class, "right_arrow.gif"));
                ivjTbbNext.setToolTipText(ResourceManager.getResource(DataSelectorPanel.class, "TbbNext_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbNext;
    }

    /**
     * Return the TbbOpen property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbOpen() {
        if (ivjTbbOpen == null) {
            try {
                ivjTbbOpen = new javax.swing.JButton();
                ivjTbbOpen.setName("TbbOpen");
                ivjTbbOpen.setToolTipText("Open...");
                ivjTbbOpen.setText("");
                ivjTbbOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbOpen.setIcon(null);
                ivjTbbOpen.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbOpen.setEnabled(false);
                // user code begin {1}
                ivjTbbOpen.setIcon(CommonUserAccess.getIconChange());
                ivjTbbOpen.setToolTipText(CommonUserAccess.getMniFileOpenWindowText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbOpen;
    }

    /**
     * Method generated to support the promotion of the tbbOpenEnabled attribute.
     *
     * @return boolean
     * @deprecated (use { @ link # getTbbChangeEnabled () } instead)
     */
    public boolean getTbbOpenEnabled() {
        return getTbbChangeEnabled();
    }

    public boolean getTbbChangeEnabled() {
        return getTbbOpen().isEnabled();
    }

    /**
     * Return the TbbPaste property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbPaste() {
        if (ivjTbbPaste == null) {
            try {
                ivjTbbPaste = new javax.swing.JButton();
                ivjTbbPaste.setName("TbbPaste");
                ivjTbbPaste.setToolTipText("Paste");
                ivjTbbPaste.setText("");
                ivjTbbPaste.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbPaste.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbPaste.setIcon(null);
                ivjTbbPaste.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbPaste.setEnabled(false);
                // user code begin {1}
                ivjTbbPaste.setIcon(CommonUserAccess.getIconPaste());
                ivjTbbPaste.setToolTipText(CommonUserAccess.getMniEditPasteText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbPaste;
    }

    /**
     * Method generated to support the promotion of the tbbPasteEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbPasteEnabled() {
        return getTbbPaste().isEnabled();
    }

    /**
     * Return the TbbSave1 property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbPrevious() {
        if (ivjTbbPrevious == null) {
            try {
                ivjTbbPrevious = new javax.swing.JButton();
                ivjTbbPrevious.setName("TbbPrevious");
                ivjTbbPrevious.setToolTipText("Previous");
                ivjTbbPrevious.setText("");
                ivjTbbPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbPrevious.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbPrevious.setIcon(null);
                ivjTbbPrevious.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbPrevious.setEnabled(false);
                // user code begin {1}
                ivjTbbPrevious.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(ToolBar.class, "left_arrow.gif"));
                ivjTbbPrevious.setToolTipText(ResourceManager.getResource(DataSelectorPanel.class, "TbbPrevious_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbPrevious;
    }

    /**
     * Return the TbbPrint property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbPrint() {
        if (ivjTbbPrint == null) {
            try {
                ivjTbbPrint = new javax.swing.JButton();
                ivjTbbPrint.setName("TbbPrint");
                ivjTbbPrint.setToolTipText("Print");
                ivjTbbPrint.setText("");
                ivjTbbPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbPrint.setIcon(null);
                ivjTbbPrint.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbPrint.setEnabled(false);
                // user code begin {1}
                ivjTbbPrint.setIcon(CommonUserAccess.getIconPrint());
                ivjTbbPrint.setToolTipText(CommonUserAccess.getMniFilePrintWindowText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbPrint;
    }

    /**
     * Method generated to support the promotion of the tbbPrintEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbPrintEnabled() {
        return getTbbPrint().isEnabled();
    }

    /**
     * Return the TbbRedo property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbRedo() {
        if (ivjTbbRedo == null) {
            try {
                ivjTbbRedo = new javax.swing.JButton();
                ivjTbbRedo.setName("TbbRedo");
                ivjTbbRedo.setToolTipText("Redo");
                ivjTbbRedo.setText("");
                ivjTbbRedo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbRedo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbRedo.setIcon(null);
                ivjTbbRedo.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbRedo.setEnabled(false);
                // user code begin {1}
                ivjTbbRedo.setIcon(CommonUserAccess.getIconRedo());
                ivjTbbRedo.setToolTipText(CommonUserAccess.getMniEditRedoText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbRedo;
    }

    /**
     * Method generated to support the promotion of the tbbRedoEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbRedoEnabled() {
        return getTbbRedo().isEnabled();
    }

    /**
     * Return the TbbSave property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbSave() {
        if (ivjTbbSave == null) {
            try {
                ivjTbbSave = new javax.swing.JButton();
                ivjTbbSave.setName("TbbSave");
                ivjTbbSave.setToolTipText("Save");
                ivjTbbSave.setText("");
                ivjTbbSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbSave.setIcon(null);
                ivjTbbSave.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbSave.setEnabled(false);
                // user code begin {1}
                ivjTbbSave.setIcon(CommonUserAccess.getIconSave());
                ivjTbbSave.setToolTipText(CommonUserAccess.getMniFileSaveText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbSave;
    }

    /**
     * Method generated to support the promotion of the tbbSaveEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbSaveEnabled() {
        return getTbbSave().isEnabled();
    }

    /**
     * Return the TbbUndo property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getTbbUndo() {
        if (ivjTbbUndo == null) {
            try {
                ivjTbbUndo = new javax.swing.JButton();
                ivjTbbUndo.setName("TbbUndo");
                ivjTbbUndo.setToolTipText("Undo");
                ivjTbbUndo.setText("");
                ivjTbbUndo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                ivjTbbUndo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                ivjTbbUndo.setIcon(null);
                ivjTbbUndo.setMargin(new java.awt.Insets(0, 0, 0, 0));
                ivjTbbUndo.setEnabled(false);
                // user code begin {1}
                ivjTbbUndo.setIcon(CommonUserAccess.getIconUndo());
                ivjTbbUndo.setToolTipText(CommonUserAccess.getMniEditUndoText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTbbUndo;
    }

    /**
     * Method generated to support the promotion of the tbbUndoEnabled attribute.
     *
     * @return boolean
     */
    public boolean getTbbUndoEnabled() {
        return getTbbUndo().isEnabled();
    }

    /**
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {
        BaseDialog.showError(this, null, exception.toString(), exception);
        log.error("uncaugt", exception);//$NON-NLS-1$
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
        getTbbOpen().addActionListener(ivjEventHandler);
        getTbbSave().addActionListener(ivjEventHandler);
        getTbbCut().addActionListener(ivjEventHandler);
        getTbbCopy().addActionListener(ivjEventHandler);
        getTbbPaste().addActionListener(ivjEventHandler);
        getTbbUndo().addActionListener(ivjEventHandler);
        getTbbRedo().addActionListener(ivjEventHandler);
        getTbbFind().addActionListener(ivjEventHandler);
        getTbbDelete().addActionListener(ivjEventHandler);
        getTbbPrint().addActionListener(ivjEventHandler);
        getTbbNext().addActionListener(ivjEventHandler);
        getTbbPrevious().addActionListener(ivjEventHandler);
        getTbbNew().addActionListener(ivjEventHandler);
        getTbbLast().addActionListener(ivjEventHandler);
        getTbbFirst().addActionListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            browser.addListener(this);
            setObjects(new ArrayList());
            // user code end
            setName("ToolBar");
            setFloatable(false);
            setSize(535, 27);
            add(getTbbNew(), getTbbNew().getName());
            add(getTbbOpen(), getTbbOpen().getName());
            add(getTbbSave(), getTbbSave().getName());
            add(getTbbDelete(), getTbbDelete().getName());
            add(getTbbPrint());
            add(getTbbFind(), getTbbFind().getName());
            add(getTbbCut(), getTbbCut().getName());
            add(getTbbCopy(), getTbbCopy().getName());
            add(getTbbPaste(), getTbbPaste().getName());
            add(getTbbUndo(), getTbbUndo().getName());
            add(getTbbRedo(), getTbbRedo().getName());
            add(getCbxItems(), getCbxItems().getName());
            add(getTbbFirst(), getTbbFirst().getName());
            add(getTbbPrevious(), getTbbPrevious().getName());
            add(getLblSelector(), getLblSelector().getName());
            add(getTbbNext(), getTbbNext().getName());
            add(getTbbLast(), getTbbLast().getName());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        treatNextPreviousButtons();
        // user code end
    }

    /**
     * @param newListener ch.softenvironment.view.ToolBarListener
     * @deprecated (use DataBrowserListener instead)
     */
    public void removeToolBarListener(ch.softenvironment.view.ToolBarListener newListener) {
        fieldToolBarListenerEventMulticaster = ch.softenvironment.view.ToolBarListenerEventMulticaster
            .remove(fieldToolBarListenerEventMulticaster, newListener);
    }

    /**
     * Add a PropertyChangeListener to your GUI to catch any "currentObject" events, when changing by navigation buttons.
     *
     * @see DataBrowserListener#setCurrentObject(Object)
     */
    @Override
    public void setCurrentObject(final java.lang.Object currentObject) {
        WaitDialog.showBusy(BaseDialog.getFrameOwner(getParent()), new Runnable() {
            @Override
            public void run() {
                treatNextPreviousButtons();

                Object oldValue = fieldCurrentObject;
                fieldCurrentObject = currentObject;
                firePropertyChange("currentObject", oldValue, currentObject);//$NON-NLS-1$
            }
        });
    }

    /**
     * Show a list of inconsistencies in a ComboBox, which is suppressed if everything is alright.
     *
     * @param items java.util.Vector
     * see ConsistencyController.getInconsistencies();
     */
    public void setItems(java.util.Vector<?> items) {
        getCbxItems().setVisible((items != null) && (items.size() > 0));
        getCbxItems().setModel(new javax.swing.DefaultComboBoxModel(items));
    }

    /**
     * Set the list of Objects to handle (scroll) by Toolbar. The slider is only visible if there is really a scroll-list.
     *
     * @param objects The new value for the property.
     */
    public void setObjects(java.util.List<?> objects) {
        browser.setObjects(objects);
        if ((objects == null) || (objects.size() <= 1)) {
            // suppress browsing completely
            getTbbFirst().setVisible(false);
            getTbbPrevious().setVisible(false);
            getTbbNext().setVisible(false);
            getTbbLast().setVisible(false);
            getLblSelector().setVisible(false);
        } else {
            getTbbFirst().setVisible(true);
            getTbbPrevious().setVisible(true);
            getTbbNext().setVisible(true);
            getTbbLast().setVisible(true);
            getLblSelector().setVisible(true);
        }
    }

    /**
     * Method generated to support the promotion of the tbbCopyEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbCopyEnabled(boolean arg1) {
        getTbbCopy().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbCutEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbCutEnabled(boolean arg1) {
        getTbbCut().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbDeleteEnabled attribute.
     *
     * @param arg1 boolean
     * @deprecated (use { @ link # setTbbRemoveEnabled ( boolean) instead}
     */
    public void setTbbDeleteEnabled(boolean arg1) {
        setTbbRemoveEnabled(arg1);
    }

    /**
     * Set status of button "Remove".
     *
     * @param enabled boolean
     */
    public void setTbbRemoveEnabled(boolean enabled) {
        getTbbDelete().setEnabled(enabled);
    }

    /**
     * Method generated to support the promotion of the tbbFindEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbFindEnabled(boolean arg1) {
        getTbbFind().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbNewEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbNewEnabled(boolean arg1) {
        getTbbNew().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbOpenEnabled attribute.
     *
     * @param arg1 boolean
     * @deprecated (use { @ link # setTbbChangeEnabled ( boolean) instead}
     */
    public void setTbbOpenEnabled(boolean arg1) {
        setTbbChangeEnabled(arg1);
    }

    /**
     * Set status of button "Change".
     *
     * @param enabled boolean
     */
    public void setTbbChangeEnabled(boolean enabled) {
        getTbbOpen().setEnabled(enabled);
    }

    /**
     * Method generated to support the promotion of the tbbPasteEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbPasteEnabled(boolean arg1) {
        getTbbPaste().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbPrintEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbPrintEnabled(boolean arg1) {
        getTbbPrint().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbRedoEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbRedoEnabled(boolean arg1) {
        getTbbRedo().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbSaveEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbSaveEnabled(boolean arg1) {
        getTbbSave().setEnabled(arg1);
    }

    /**
     * Method generated to support the promotion of the tbbUndoEnabled attribute.
     *
     * @param arg1 boolean
     */
    public void setTbbUndoEnabled(boolean arg1) {
        getTbbUndo().setEnabled(arg1);
    }

    /**
     * Comment
     *
     * public void tbbNew_ActionPerformed(java.awt.event.ActionEvent
     * actionEvent) { return; }
     */
    /**
     * Hide or show the Toolbar.
     */
    public void toggleVisbility() {
        setVisible(!(isVisible()));
    }

    /**
     * Treat Next/Previous Buttons.
     */
    private void treatNextPreviousButtons() {
        getTbbFirst().setEnabled(browser.isScrollFirstAllowed());
        getTbbPrevious().setEnabled(browser.isScrollPreviousAllowed());
        getTbbNext().setEnabled(browser.isScrollNextAllowed());
        getTbbLast().setEnabled(browser.isScrollLastAllowed());
        getLblSelector().setText(" " + browser.getScrollIndexString() + " ");
    }

    /**
     * Adapt Toolbar according to given rights for the allowed user actions.
     *
     * @param rights
     */
    public void adaptRights(UserActionRights rights) {
        if (rights != null) {
            if (!rights.isNewObjectAllowed()) {
                remove(getTbbNew());
            }
            if (!rights.isReadObjectAllowed()) {
                remove(getTbbOpen());
                remove(getTbbFind());
            }
            if (!rights.isRemoveObjectsAllowed()) {
                remove(getTbbDelete());
            }
            if (!rights.isSaveObjectAllowed()) {
                remove(getTbbSave());
                remove(getTbbUndo());
                remove(getTbbRedo());
            }
        }
    }

    /**
     * @see DataBrowserListener
     */
    @Override
    public boolean removeObject(Object object) {
        return false;
    }

    /**
     * @see DataBrowserListener
     */
    @Override
    public Object saveChanges(Object object) {
        return object;
    }
}
