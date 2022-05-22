package ch.softenvironment.view;

import ch.softenvironment.client.ResourceManager;

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
 * Panel allowing entry of 3 different States: - true - false - undefined
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class TriStatePanel extends BasePanel {

    private static final String ACTION_ALL = "ALL";
    private static final String ACTION_YES = "YES";
    private static final String ACTION_NO = "NO";
    private boolean changing = false;
    private final javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
    private javax.swing.JRadioButton ivjRbtFalse = null;
    private javax.swing.JRadioButton ivjRbtTrue = null;
    private javax.swing.JRadioButton ivjRbtAll = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private java.lang.Boolean fieldValue; // @see initialize

    class IvjEventHandler implements java.awt.event.ItemListener {

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == TriStatePanel.this.getRbtAll()) {
                connEtoC1(e);
            }
            if (e.getSource() == TriStatePanel.this.getRbtTrue()) {
                connEtoC2(e);
            }
            if (e.getSource() == TriStatePanel.this.getRbtFalse()) {
                connEtoC3(e);
            }
        }
    }

    /**
     * TriStatePanel constructor comment.
     */
    public TriStatePanel() {
        super();
        initialize();
    }

    /**
     * TriStatePanel constructor comment.
     *
     * @param layout java.awt.LayoutManager
     */
    public TriStatePanel(java.awt.LayoutManager layout) {
        super(layout);
    }

    /**
     * TriStatePanel constructor comment.
     *
     * @param layout java.awt.LayoutManager
     * @param isDoubleBuffered boolean
     */
    public TriStatePanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
     * TriStatePanel constructor comment.
     *
     * @param isDoubleBuffered boolean
     */
    public TriStatePanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
     * Trigger event at User Selection-Change.
     *
     * @see #setValue(Boolean)
     */
    private void changed(java.awt.event.ItemEvent itemEvent) {
        if (!changing) {
            if (group.getSelection().getActionCommand().equals(ACTION_NO)) {
                setValue(Boolean.FALSE);
            } else if (group.getSelection().getActionCommand().equals(ACTION_YES)) {
                setValue(Boolean.TRUE);
            } else {
                setValue(null);
            }
        }
    }

    /**
     * connEtoC1: (RbtAll.item.itemStateChanged(java.awt.event.ItemEvent) --> TriStatePanel.changed(Ljava.awt.event.ItemEvent;)V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changed(arg1);
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (RbtTrue.item.itemStateChanged(java.awt.event.ItemEvent) --> TriStatePanel.changed(Ljava.awt.event.ItemEvent;)V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changed(arg1);
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (RbtFalse.item.itemStateChanged(java.awt.event.ItemEvent) --> TriStatePanel.changed(Ljava.awt.event.ItemEvent;)V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changed(arg1);
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * Return !getValue().
     */
    public java.lang.Boolean getNotValue() {
        if (fieldValue != null) {
            return Boolean.valueOf(!fieldValue.booleanValue());
        } else {
            return null;
        }
    }

    /**
     * Return the RbtNone property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtAll() {
        if (ivjRbtAll == null) {
            try {
                ivjRbtAll = new javax.swing.JRadioButton();
                ivjRbtAll.setName("RbtAll");
                ivjRbtAll.setText("Alle");
                ivjRbtAll.setBounds(0, 0, 93, 22);
                // user code begin {1}
                ivjRbtAll.setActionCommand(ACTION_ALL);
                ivjRbtAll.setText(getResourceString("CI_All_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtAll;
    }

    /**
     * Return the RbtFalse property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtFalse() {
        if (ivjRbtFalse == null) {
            try {
                ivjRbtFalse = new javax.swing.JRadioButton();
                ivjRbtFalse.setName("RbtFalse");
                ivjRbtFalse.setText("Nein");
                ivjRbtFalse.setBounds(200, 0, 93, 22);
                // user code begin {1}
                ivjRbtFalse.setActionCommand(ACTION_NO);
                ivjRbtFalse.setText(ResourceManager.getResource(ch.softenvironment.util.StringUtils.class, "CI_No_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtFalse;
    }

    /**
     * Return the RbtTrue property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtTrue() {
        if (ivjRbtTrue == null) {
            try {
                ivjRbtTrue = new javax.swing.JRadioButton();
                ivjRbtTrue.setName("RbtTrue");
                ivjRbtTrue.setText("Ja");
                ivjRbtTrue.setBounds(100, 0, 93, 21);
                // user code begin {1}
                ivjRbtTrue.setActionCommand(ACTION_YES);
                ivjRbtTrue.setText(ResourceManager.getResource(ch.softenvironment.util.StringUtils.class, "CI_Yes_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtTrue;
    }

    /**
     * Gets the value property (java.lang.Boolean) value.
     *
     * @return The value property value.
     * @see #setValue
     */
    public java.lang.Boolean getValue() {
        return fieldValue;
    }

    /**
     * Return speaking value of {@link #getValue()}
     *
     * @return
     */
    public String getValueString() {
        if (getRbtTrue().isSelected()) {
            return getRbtTrue().getText();
        } else if (getRbtFalse().isSelected()) {
            return getRbtFalse().getText();
        } else {
            return getRbtAll().getText();
        }
    }

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
        getRbtAll().addItemListener(ivjEventHandler);
        getRbtTrue().addItemListener(ivjEventHandler);
        getRbtFalse().addItemListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("TriStatePanel");
            setLayout(null);
            setSize(300, 22);
            add(getRbtAll(), getRbtAll().getName());
            add(getRbtTrue(), getRbtTrue().getName());
            add(getRbtFalse(), getRbtFalse().getName());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        group.add(getRbtAll());
        group.add(getRbtTrue());
        group.add(getRbtFalse());

        // set a matching couple
        setValue(null);
        getRbtAll().setSelected(true);
        // user code end
    }

    /**
     * Sets the value by model change.
     *
     * @param value Tristate
     * @see #initialize()
     * @see #changed(java.awt.event.ItemEvent)
     * @see #getValue
     */
    public void setValue(java.lang.Boolean value) {
        if ((value == null) && (fieldValue != null)) {
            changing = true; // prevent pinging around
            Boolean oldValue = fieldValue;
            fieldValue = value;
            firePropertyChange("value", oldValue, value);
            getRbtAll().setSelected(true);
            changing = false;
            return;
        }

        if ((value != null) && (!value.equals(fieldValue))) {
            changing = true; // prevent pinging around
            Boolean oldValue = fieldValue;
            fieldValue = value;
            firePropertyChange("value", oldValue, value);

            if (value.booleanValue()) {
                getRbtTrue().setSelected(true);
            } else {
                getRbtFalse().setSelected(true);
            }
            changing = false;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getRbtAll().setEnabled(enabled);
        getRbtTrue().setEnabled(enabled);
        getRbtFalse().setEnabled(enabled);
    }

    /**
     * Show whether "All" option is available or not. Default is true => show All button.
     *
     * @param triState
     */
    public void showTriState(boolean triState) {
        getRbtAll().setVisible(triState);
    }
}
