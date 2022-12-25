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
 */

/**
 * Display a String with Cardinality's upper-Range > 1.
 *
 * @author Peter Hirzel
 */

public class MultiStringPanel extends javax.swing.JPanel {

    private javax.swing.JComboBox ivjCbxCount = null;
    private java.util.List<String> fieldList = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private SimpleEditorPanel ivjPnlEditor = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener {

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == MultiStringPanel.this.getPnlEditor()) {
                connEtoC1(newEvent);
            }
        }
    }

    /**
     * MultiStringPanel constructor comment.
     */
    public MultiStringPanel() {
        super();
        initialize();
    }

    /**
     * connEtoC1:  (TxtValue.simpleEditorPanel.txaEditorKey_keyReleased(java.util.EventObject) --> MultiStringPanel.valueChanged()V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.util.EventObject arg1) {
        try {
            // user code begin {1}
            // user code end
            this.valueChanged();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * Return the CbxCount property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxCount() {
        if (ivjCbxCount == null) {
            try {
                ivjCbxCount = new javax.swing.JComboBox();
                ivjCbxCount.setName("CbxCount");
                ivjCbxCount.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxCount;
    }

    /**
     * Gets the list property (java.util.List) value.
     *
     * @return The list property value.
     * @see #setList
     */
    public java.util.List<String> getList() {
        return fieldList;
    }

    /**
     * Return the PnlEditor property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private SimpleEditorPanel getPnlEditor() {
        if (ivjPnlEditor == null) {
            try {
                ivjPnlEditor = new ch.softenvironment.view.SimpleEditorPanel();
                ivjPnlEditor.setName("PnlEditor");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlEditor;
    }

    /**
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {
        BaseFrame.showException(null, exception);
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
        getPnlEditor().addSimpleEditorPanelListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("MultiStringPanel");
            setLayout(new java.awt.GridBagLayout());
            setSize(213, 24);

            java.awt.GridBagConstraints constraintsCbxCount = new java.awt.GridBagConstraints();
            constraintsCbxCount.gridx = 2;
            constraintsCbxCount.gridy = 1;
            constraintsCbxCount.anchor = java.awt.GridBagConstraints.NORTHWEST;
            constraintsCbxCount.weightx = 1.0;
            constraintsCbxCount.ipadx = -59;
            constraintsCbxCount.insets = new java.awt.Insets(0, 2, 1, 2);
            add(getCbxCount(), constraintsCbxCount);

            java.awt.GridBagConstraints constraintsPnlEditor = new java.awt.GridBagConstraints();
            constraintsPnlEditor.gridx = 1;
            constraintsPnlEditor.gridy = 1;
            constraintsPnlEditor.fill = java.awt.GridBagConstraints.BOTH;
            constraintsPnlEditor.anchor = java.awt.GridBagConstraints.NORTHWEST;
            constraintsPnlEditor.weightx = 1.0;
            constraintsPnlEditor.weighty = 1.0;
            constraintsPnlEditor.ipadx = 119;
            constraintsPnlEditor.ipady = 2;
            constraintsPnlEditor.insets = new java.awt.Insets(0, 0, 0, 1);
            add(getPnlEditor(), constraintsPnlEditor);
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        // user code end
    }

    /**
     * Sets the list property (java.util.List) value.
     *
     * @param list The new value for the property.
     * @see #getList
     */
    public void setList(java.util.List<String> list) {
        if (list == null) {
            throw new IllegalArgumentException("null is not valid");
        }

        //	java.util.List oldValue = fieldList;
        fieldList = list;
        //	firePropertyChange("list", oldValue, list);

        java.util.Vector<Integer> elements = new java.util.Vector<Integer>(1);
        elements.add(1);
        getCbxCount().setModel(new javax.swing.DefaultComboBoxModel(elements));
        //TODO only ONE element is implemented yet
        if (list.size() >= 1) {
            getPnlEditor().setText(list.get(0));
        }
    }

    /**
     * Update List and signal change.
     */
    private void valueChanged() {
        if (ch.softenvironment.util.StringUtils.isNullOrEmpty(getPnlEditor().getText())) {
            if (getList().size() > 0) {
                getList().remove(0);
            } else {
                getList().set(0, getPnlEditor().getText());
            }
        } else {
            if (getList().size() == 0) {
                getList().add(getPnlEditor().getText());
            } else {
                getList().set(0, getPnlEditor().getText());
            }
        }
        firePropertyChange("list", null /* cheat to really trigger event */, getList());
    }
}
