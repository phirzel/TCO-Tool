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

import ch.softenvironment.client.ResourceManager;

/**
 * Panel for structured Phone Number representation.
 *
 * @author Peter Hirzel
 */
public class PhoneNumberPanel extends javax.swing.JPanel {

    private final String separator = "/";//$NON-NLS-1$
    private javax.swing.JTextField ivjTxtCountryPrefix = null;
    private javax.swing.JTextField ivjTxtNumber = null;
    private java.lang.String fieldText = "";
    private javax.swing.JTextField ivjTxtSitePrefix = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private java.util.List<String> fieldList = null;

    class IvjEventHandler implements java.awt.event.FocusListener, java.awt.event.KeyListener {

        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (e.getSource() == PhoneNumberPanel.this.getTxtCountryPrefix()) {
                connEtoM1(e);
            }
            if (e.getSource() == PhoneNumberPanel.this.getTxtSitePrefix()) {
                connEtoM2(e);
            }
            if (e.getSource() == PhoneNumberPanel.this.getTxtNumber()) {
                connEtoM3(e);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == PhoneNumberPanel.this.getTxtSitePrefix()) {
                connEtoC2(e);
            }
            if (e.getSource() == PhoneNumberPanel.this.getTxtCountryPrefix()) {
                connEtoC1(e);
            }
            if (e.getSource() == PhoneNumberPanel.this.getTxtNumber()) {
                connEtoC3(e);
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }
    }

    private javax.swing.JComboBox ivjCbxCount = null;

    /**
     * PhoneNumberPanel constructor comment.
     */
    public PhoneNumberPanel() {
        super();
        initialize();
    }

    /**
     * connEtoC1:  (TxtCountryPrefix.key.keyReleased(java.awt.event.KeyEvent) --> PhoneNumberPanel.update(Ljava.awt.event.KeyEvent;)V)
     *
     * @param arg1 java.awt.event.KeyEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.KeyEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setPhoneNumber();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2:  (TxtSitePrefix.key.keyReleased(java.awt.event.KeyEvent) --> PhoneNumberPanel.update(Ljava.awt.event.KeyEvent;)V)
     *
     * @param arg1 java.awt.event.KeyEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.KeyEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setPhoneNumber();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3:  (TxtNumber.key.keyReleased(java.awt.event.KeyEvent) --> PhoneNumberPanel.update(Ljava.awt.event.KeyEvent;)V)
     *
     * @param arg1 java.awt.event.KeyEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.KeyEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setPhoneNumber();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoM1:  (TxtCountryPrefix.focus.focusGained(java.awt.event.FocusEvent) --> TxtCountryPrefix.selectAll()V)
     *
     * @param arg1 java.awt.event.FocusEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoM1(java.awt.event.FocusEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            getTxtCountryPrefix().selectAll();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoM2:  (TxtSitePrefix.focus.focusGained(java.awt.event.FocusEvent) --> TxtSitePrefix.selectAll()V)
     *
     * @param arg1 java.awt.event.FocusEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoM2(java.awt.event.FocusEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            getTxtSitePrefix().selectAll();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoM3:  (TxtNumber.focus.focusGained(java.awt.event.FocusEvent) --> TxtNumber.selectAll()V)
     *
     * @param arg1 java.awt.event.FocusEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoM3(java.awt.event.FocusEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            getTxtNumber().selectAll();
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
                ivjCbxCount.setBounds(230, 0, 63, 23);
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
     * Gets the text property (java.lang.String) value.
     *
     * @return The text property value.
     * @see #setText
     */
    public java.lang.String getText() {
        return fieldText;
    }

    /**
     * Return the TxtCountryPrefix property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtCountryPrefix() {
        if (ivjTxtCountryPrefix == null) {
            try {
                ivjTxtCountryPrefix = new javax.swing.JTextField();
                ivjTxtCountryPrefix.setName("TxtCountryPrefix");
                ivjTxtCountryPrefix.setToolTipText("Landesvorwahl");
                ivjTxtCountryPrefix.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                ivjTxtCountryPrefix.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
                ivjTxtCountryPrefix.setBounds(0, 1, 45, 20);
                ivjTxtCountryPrefix.setNextFocusableComponent(getTxtSitePrefix());
                // user code begin {1}
                ivjTxtCountryPrefix.setToolTipText(ResourceManager.getResource(PhoneNumberPanel.class, "TxtCountryPrefix_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtCountryPrefix;
    }

    /**
     * Return the TxtNumber property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtNumber() {
        if (ivjTxtNumber == null) {
            try {
                ivjTxtNumber = new javax.swing.JTextField();
                ivjTxtNumber.setName("TxtNumber");
                ivjTxtNumber.setToolTipText("Ortsnummer");
                ivjTxtNumber.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                ivjTxtNumber.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
                ivjTxtNumber.setBounds(95, 1, 129, 20);
                ivjTxtNumber.setColumns(6);
                // user code begin {1}
                ivjTxtNumber.setToolTipText(ResourceManager.getResource(PhoneNumberPanel.class, "TxtNumber_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtNumber;
    }

    /**
     * Return the TxtSizePrefix property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtSitePrefix() {
        if (ivjTxtSitePrefix == null) {
            try {
                ivjTxtSitePrefix = new javax.swing.JTextField();
                ivjTxtSitePrefix.setName("TxtSitePrefix");
                ivjTxtSitePrefix.setToolTipText("Ortsvorwahl");
                ivjTxtSitePrefix.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
                ivjTxtSitePrefix.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
                ivjTxtSitePrefix.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                ivjTxtSitePrefix.setBounds(48, 1, 43, 20);
                ivjTxtSitePrefix.setNextFocusableComponent(getTxtNumber());
                // user code begin {1}
                ivjTxtSitePrefix.setToolTipText(ResourceManager.getResource(PhoneNumberPanel.class, "TxtSitePrefix_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtSitePrefix;
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
     * @throws java.lang.Exception The exception description.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() {
        // user code begin {1}
        // user code end
        getTxtSitePrefix().addKeyListener(ivjEventHandler);
        getTxtCountryPrefix().addKeyListener(ivjEventHandler);
        getTxtNumber().addKeyListener(ivjEventHandler);
        getTxtCountryPrefix().addFocusListener(ivjEventHandler);
        getTxtSitePrefix().addFocusListener(ivjEventHandler);
        getTxtNumber().addFocusListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("PhoneNumberPanel");
            setLayout(null);
            setSize(299, 24);
            add(getTxtCountryPrefix(), getTxtCountryPrefix().getName());
            add(getTxtSitePrefix(), getTxtSitePrefix().getName());
            add(getTxtNumber(), getTxtNumber().getName());
            add(getCbxCount(), getCbxCount().getName());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        // user code end
    }

    private void setInternalText(String text) {
        String oldValue = fieldText;
        fieldText = text;
        firePropertyChange("text", oldValue, text);//$NON-NLS-1$

        if (getList() != null) {
            if (getList().size() == 0) {
                getList().add(text);
            } else {
                getList().set(0, text);
            }
            firePropertyChange("list", null /* cheat to trigger */, getList());//$NON-NLS-1$
        }
    }

    /**
     * Sets the list property (java.util.List) value.
     *
     * @param list The new value for the property.
     * @see #getList
     */
    public void setList(java.util.List<String> list) {
        //TODO Only ONE entry is supported yet
        java.util.List<String> oldValue = fieldList;
        fieldList = list;
        firePropertyChange("list", oldValue, list);

        java.util.Vector<Integer> elements = new java.util.Vector<>(1);
        elements.add(1);
        getCbxCount().setModel(new javax.swing.DefaultComboBoxModel(elements));
        if ((list != null) && (list.size() >= 1)) {
            setText(list.get(0));
        }
    }

    /**
     * Change the value by this GUI
     */
    private void setPhoneNumber() {
        setInternalText(getTxtCountryPrefix().getText() + separator + getTxtSitePrefix().getText() + separator + getTxtNumber().getText());
    }

    /**
     * Sets the text property (java.lang.String) value.
     *
     * @param text "<internationalPrefix>/<nationalPrefix>/<localNumber>".
     * @see #getText
     */
    public void setText(java.lang.String text) {
        if ((text != null) && (text.length() > 0)) {
            // show Phonenumber
            int countrySeparator = text.indexOf(separator);
            if (countrySeparator >= 0) {
                getTxtCountryPrefix().setText(text.substring(0, countrySeparator));
                if (text.length() > countrySeparator + 1) {
                    int siteSeparator = text.indexOf(separator, countrySeparator + 1);
                    if (siteSeparator >= 0) {
                        getTxtSitePrefix().setText(text.substring(countrySeparator + 1, siteSeparator));
                        if ((siteSeparator + 1) < text.length()) {
                            getTxtNumber().setText(text.substring(siteSeparator + 1));
                        } else {
                            getTxtNumber().setText(null);
                        }
                    }
                }
            }
        } else {
            getTxtCountryPrefix().setText(null);
            getTxtSitePrefix().setText(null);
            getTxtNumber().setText(null);
        }

        setInternalText(text);
    }
}
