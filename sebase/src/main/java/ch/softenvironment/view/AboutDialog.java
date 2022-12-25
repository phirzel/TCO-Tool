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
 * Product Info Dialog.
 *
 * @author Peter Hirzel
 */

public class AboutDialog extends BaseDialog {

    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private javax.swing.JLabel ivjLblAbout = null;
    private javax.swing.JLabel ivjLblCopyright = null;
    private javax.swing.JLabel ivjLblLicencedFor = null;
    private javax.swing.JLabel ivjLblTitle = null;
    private javax.swing.JTextArea ivjTxaLicence = null;
    private javax.swing.JLabel ivjLblThankOSS = null;
    private javax.swing.JPanel ivjPnlDeveloperInfo = null;
    private PlatformInfoPanel ivjPnlPlatform = null;
    private javax.swing.JPanel ivjPnlProduct = null;
    private javax.swing.JTabbedPane ivjTbpMain = null;
    private javax.swing.JTextArea ivjTxaOSS = null;

    /**
     * AboutDialog constructor comment.
     *
     * @param owner java.awt.Frame
     * @param application java.lang.String
     */
    public AboutDialog(java.awt.Frame owner, String application, String version, String copyrightPeriod, String licence) {
        super(owner, true);
        initialize();

        setTitle(getResourceString("DlgTitle") + " " + application);
        getLblTitle().setText(/* application + " " + */version);
        //TODO HIP upgrade lib/copyright
        getLblCopyright().setText("Copyright (c) softEnvironment " + copyrightPeriod);
        getTxaLicence().setText(licence);

        // TODO make OSS-list more flexible
        getTxaOSS().setText("- http://sourceforge.net/projects/umleditor" + "\n" + "- http://sourceforge.net/projects/jomm");
        setVisible(true);
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
                getBaseDialogContentPane().add(getTbpMain(), "Center");
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
                ivjJPanel1.setBorder(new javax.swing.border.EtchedBorder());
                ivjJPanel1.setLayout(null);
                ivjJPanel1.setBounds(62, 359, 383, 105);
                getJPanel1().add(getTxaLicence(), getTxaLicence().getName());
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
     * Return the LblAbout property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblAbout() {
        if (ivjLblAbout == null) {
            try {
                ivjLblAbout = new javax.swing.JLabel();
                ivjLblAbout.setName("LblAbout");
                ivjLblAbout.setText("");
                ivjLblAbout.setBounds(31, 81, 462, 240);
                // user code begin {1}
                ivjLblAbout.setIcon(ResourceManager.getImageIcon(AboutDialog.class, "about.png"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblAbout;
    }

    /**
     * Return the LblCopyright property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCopyright() {
        if (ivjLblCopyright == null) {
            try {
                ivjLblCopyright = new javax.swing.JLabel();
                ivjLblCopyright.setName("LblCopyright");
                ivjLblCopyright.setText("Copyright (c) softEnvironment 2001-2003");
                ivjLblCopyright.setBounds(125, 469, 280, 27);
                // user code begin {1}
                ivjLblCopyright.setText("Copyright (c) softEnvironment 2001-2004");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCopyright;
    }

    /**
     * Return the LblLicencedFor property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblLicencedFor() {
        if (ivjLblLicencedFor == null) {
            try {
                ivjLblLicencedFor = new javax.swing.JLabel();
                ivjLblLicencedFor.setName("LblLicencedFor");
                ivjLblLicencedFor.setFont(new java.awt.Font("Arial", 1, 14));
                ivjLblLicencedFor.setText("Dieses Produkt ist lizenziert fuer:");
                ivjLblLicencedFor.setBounds(62, 328, 285, 26);
                // user code begin {1}
                ivjLblLicencedFor.setText(getResourceString("LblLicencedFor_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblLicencedFor;
    }

    /**
     * Return the LblThankOSS property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblThankOSS() {
        if (ivjLblThankOSS == null) {
            try {
                ivjLblThankOSS = new javax.swing.JLabel();
                ivjLblThankOSS.setName("LblThankOSS");
                ivjLblThankOSS.setText("This product contains software developed by:");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblThankOSS;
    }

    /**
     * Return the LblTitle property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblTitle() {
        if (ivjLblTitle == null) {
            try {
                ivjLblTitle = new javax.swing.JLabel();
                ivjLblTitle.setName("LblTitle");
                ivjLblTitle.setFont(new java.awt.Font("Arial", 1, 18));
                ivjLblTitle.setText("Lepra-Projektadministration Vx.x.x");
                ivjLblTitle.setBounds(13, 24, 497, 38);
                // user code begin {1}
                ivjLblTitle.setText("<your product name>");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblTitle;
    }

    /**
     * Return the PnlDeveloperInfo property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlDeveloperInfo() {
        if (ivjPnlDeveloperInfo == null) {
            try {
                ivjPnlDeveloperInfo = new javax.swing.JPanel();
                ivjPnlDeveloperInfo.setName("PnlDeveloperInfo");
                ivjPnlDeveloperInfo.setLayout(new java.awt.GridBagLayout());

                java.awt.GridBagConstraints constraintsLblThankOSS = new java.awt.GridBagConstraints();
                constraintsLblThankOSS.gridx = 1;
                constraintsLblThankOSS.gridy = 1;
                constraintsLblThankOSS.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsLblThankOSS.ipadx = 164;
                constraintsLblThankOSS.insets = new java.awt.Insets(41, 28, 10, 80);
                getPnlDeveloperInfo().add(getLblThankOSS(), constraintsLblThankOSS);

                java.awt.GridBagConstraints constraintsTxaOSS = new java.awt.GridBagConstraints();
                constraintsTxaOSS.gridx = 1;
                constraintsTxaOSS.gridy = 2;
                constraintsTxaOSS.fill = java.awt.GridBagConstraints.BOTH;
                constraintsTxaOSS.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsTxaOSS.weightx = 1.0;
                constraintsTxaOSS.weighty = 1.0;
                constraintsTxaOSS.ipadx = 475;
                constraintsTxaOSS.ipady = 389;
                constraintsTxaOSS.insets = new java.awt.Insets(10, 25, 47, 29);
                getPnlDeveloperInfo().add(getTxaOSS(), constraintsTxaOSS);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlDeveloperInfo;
    }

    /**
     * Return the PnlPlatform property value.
     *
     * @return ch.softenvironment.view.PlatformInfoPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private PlatformInfoPanel getPnlPlatform() {
        if (ivjPnlPlatform == null) {
            try {
                ivjPnlPlatform = new ch.softenvironment.view.PlatformInfoPanel();
                ivjPnlPlatform.setName("PnlPlatform");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlPlatform;
    }

    /**
     * Return the PnlProduct property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlProduct() {
        if (ivjPnlProduct == null) {
            try {
                ivjPnlProduct = new javax.swing.JPanel();
                ivjPnlProduct.setName("PnlProduct");
                ivjPnlProduct.setLayout(null);
                getPnlProduct().add(getLblTitle(), getLblTitle().getName());
                getPnlProduct().add(getLblLicencedFor(), getLblLicencedFor().getName());
                getPnlProduct().add(getJPanel1(), getJPanel1().getName());
                getPnlProduct().add(getLblCopyright(), getLblCopyright().getName());
                getPnlProduct().add(getLblAbout(), getLblAbout().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlProduct;
    }

    /**
     * Return the TbpMain property value.
     *
     * @return javax.swing.JTabbedPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTabbedPane getTbpMain() {
        if (ivjTbpMain == null) {
            try {
                ivjTbpMain = new javax.swing.JTabbedPane();
                ivjTbpMain.setName("TbpMain");
                ivjTbpMain.insertTab("Produkt-Info", null, getPnlProduct(), null, 0);
                ivjTbpMain.insertTab("PnlPlatform", null, getPnlPlatform(), null, 1);
                ivjTbpMain.insertTab("Entwickler-Info", null, getPnlDeveloperInfo(), null, 2);
                // user code begin {1}
                ivjTbpMain.setTitleAt(0, ResourceManager.getResource(AboutDialog.class, "PnlProductInfo_text"));
                ivjTbpMain.setTitleAt(1, ResourceManager.getResource(AboutDialog.class, "PnlTechnicalInfo_text"));
                ivjTbpMain.setTitleAt(2, ResourceManager.getResource(AboutDialog.class, "PnlDeveloperInfo_text"));
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
     * Return the TxaLicence property value.
     *
     * @return javax.swing.JTextArea
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextArea getTxaLicence() {
        if (ivjTxaLicence == null) {
            try {
                ivjTxaLicence = new javax.swing.JTextArea();
                ivjTxaLicence.setName("TxaLicence");
                ivjTxaLicence.setFont(new java.awt.Font("Arial", 1, 12));
                ivjTxaLicence.setBounds(13, 12, 354, 78);
                ivjTxaLicence.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxaLicence;
    }

    /**
     * Return the TxaOSS property value.
     *
     * @return javax.swing.JTextArea
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextArea getTxaOSS() {
        if (ivjTxaOSS == null) {
            try {
                ivjTxaOSS = new javax.swing.JTextArea();
                ivjTxaOSS.setName("TxaOSS");
                // user code begin {1}
                ivjTxaOSS.setEditable(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxaOSS;
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
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("AboutDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(534, 553);
            setTitle("Info zu ");
            setContentPane(getBaseDialogContentPane());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("DlgTitle"));
        setSize(534, 567);
        // user code end
    }
}
