package ch.softenvironment.licence.vendor;

/*
 * Copyright (C) 2006 Peter Hirzel softEnvironment (http://www.softenvironment.ch).
 * All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import ch.softenvironment.view.BaseDialog;

/**
 * GUI for a Producer to licence a Product.
 *
 * @author Peter Hirzel
 */

public class LicenceGeneratorView extends javax.swing.JFrame {

    private final java.util.List<String> products;
    private javax.swing.JPanel ivjBaseFrameContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JTextField ivjTxtKey = null;
    private javax.swing.JTextField ivjTxtNumberOfUsers = null;
    private javax.swing.JButton ivjBtnGenerate = null;
    private javax.swing.JLabel ivjLblExpirationDate = null;
    private javax.swing.JLabel ivjLblNrUsers = null;
    private javax.swing.JLabel ivjLblProduct = null;
    private javax.swing.JLabel ivjLblKey = null;
    private javax.swing.JComboBox ivjCbxMonth = null;
    private javax.swing.JComboBox ivjCbxProducts = null;

    class IvjEventHandler implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == LicenceGeneratorView.this.getBtnGenerate()) {
                connEtoC1(e);
            }
        }
    }

    /**
     * LicenceGeneratorView constructor comment.
     */
    public LicenceGeneratorView(java.util.List<String> products) {
        super();
        this.products = products;
        initialize();
    }

    /**
     * connEtoC1:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> LicenceGeneratorView.generateLicence()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.generateLicence();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * Generate a licence.
     */
    private void generateLicence() {
        LicenceGenerator generator = new LicenceGenerator();
        generator.setExpirationDuration(getCbxMonth().getSelectedIndex());
        Integer nrUser = null;
        try {
            nrUser = Integer.valueOf(getTxtNumberOfUsers().getText());
            if ((nrUser <= 0) || (nrUser > 999)) {
                throw new RuntimeException();
            }
        } catch (Throwable e) {
            //TODO NLS
            BaseDialog.showWarning(this, "Anzahl Benutzer", "Bitte eine Zahl zwischen 1..999 eingeben");
            getTxtKey().setText(null);
            return;
        }
        generator.setNumberOfUsers(nrUser);

        // @see #main() for sequence
        switch (getCbxProducts().getSelectedIndex()) {
            case 0:
                generator.setProductName("TCOp");   // softEnvironment-Plugins VBS for TCO-Tool
                generator.setProductVersion("010400");
                break;
            case 1:
                generator.setProductName("TRAr");   // regional
                generator.setProductVersion("010000");
                break;
            case 2:
                generator.setProductName("TRAn");   // national
                generator.setProductVersion("010000");
                break;
            default:
                break;
        }

        getTxtKey().setText(generator.getKey());
    }

    /**
     * Return the BaseFrameContentPane property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getBaseFrameContentPane() {
        if (ivjBaseFrameContentPane == null) {
            try {
                ivjBaseFrameContentPane = new javax.swing.JPanel();
                ivjBaseFrameContentPane.setName("BaseFrameContentPane");
                ivjBaseFrameContentPane.setLayout(null);
                getBaseFrameContentPane().add(getLblExpirationDate(), getLblExpirationDate().getName());
                getBaseFrameContentPane().add(getLblProduct(), getLblProduct().getName());
                getBaseFrameContentPane().add(getBtnGenerate(), getBtnGenerate().getName());
                getBaseFrameContentPane().add(getTxtKey(), getTxtKey().getName());
                getBaseFrameContentPane().add(getLblKey(), getLblKey().getName());
                getBaseFrameContentPane().add(getCbxMonth(), getCbxMonth().getName());
                getBaseFrameContentPane().add(getCbxProducts(), getCbxProducts().getName());
                getBaseFrameContentPane().add(getLblNrUsers(), getLblNrUsers().getName());
                getBaseFrameContentPane().add(getTxtNumberOfUsers(), getTxtNumberOfUsers().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBaseFrameContentPane;
    }

    /**
     * Return the JButton1 property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnGenerate() {
        if (ivjBtnGenerate == null) {
            try {
                ivjBtnGenerate = new javax.swing.JButton();
                ivjBtnGenerate.setName("BtnGenerate");
                ivjBtnGenerate.setText("Generate");
                ivjBtnGenerate.setBounds(10, 156, 140, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnGenerate;
    }

    /**
     * Return the CbxMonth property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxMonth() {
        if (ivjCbxMonth == null) {
            try {
                ivjCbxMonth = new javax.swing.JComboBox();
                ivjCbxMonth.setName("CbxMonth");
                ivjCbxMonth.setBounds(158, 10, 130, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxMonth;
    }

    /**
     * Return the CbxProducts property value.
     *
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getCbxProducts() {
        if (ivjCbxProducts == null) {
            try {
                ivjCbxProducts = new javax.swing.JComboBox();
                ivjCbxProducts.setName("CbxProducts");
                ivjCbxProducts.setBounds(158, 40, 247, 23);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjCbxProducts;
    }

    /**
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblExpirationDate() {
        if (ivjLblExpirationDate == null) {
            try {
                ivjLblExpirationDate = new javax.swing.JLabel();
                ivjLblExpirationDate.setName("LblExpirationDate");
                ivjLblExpirationDate.setText("Anzahl Monate g�ltig:");
                ivjLblExpirationDate.setBounds(10, 12, 140, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblExpirationDate;
    }

    /**
     * Return the LblKey property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblKey() {
        if (ivjLblKey == null) {
            try {
                ivjLblKey = new javax.swing.JLabel();
                ivjLblKey.setName("LblKey");
                ivjLblKey.setText("Schl�ssel:");
                ivjLblKey.setBounds(160, 140, 145, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblKey;
    }

    /**
     * Return the JLabel2 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblNrUsers() {
        if (ivjLblNrUsers == null) {
            try {
                ivjLblNrUsers = new javax.swing.JLabel();
                ivjLblNrUsers.setName("LblNrUsers");
                ivjLblNrUsers.setText("Anzahl Benutzer:");
                ivjLblNrUsers.setBounds(10, 76, 140, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblNrUsers;
    }

    /**
     * Return the JLabel3 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblProduct() {
        if (ivjLblProduct == null) {
            try {
                ivjLblProduct = new javax.swing.JLabel();
                ivjLblProduct.setName("LblProduct");
                ivjLblProduct.setText("Produkt:");
                ivjLblProduct.setBounds(10, 44, 140, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblProduct;
    }

    /**
     * Return the JTextField21 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtKey() {
        if (ivjTxtKey == null) {
            try {
                ivjTxtKey = new javax.swing.JTextField();
                ivjTxtKey.setName("TxtKey");
                ivjTxtKey.setText("");
                ivjTxtKey.setBounds(160, 159, 197, 20);
                ivjTxtKey.setEnabled(true);
                ivjTxtKey.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtKey;
    }

    /**
     * Return the TxtNumberOfUsers property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtNumberOfUsers() {
        if (ivjTxtNumberOfUsers == null) {
            try {
                ivjTxtNumberOfUsers = new javax.swing.JTextField();
                ivjTxtNumberOfUsers.setName("TxtNumberOfUsers");
                ivjTxtNumberOfUsers.setToolTipText("1..999 Benutzer");
                ivjTxtNumberOfUsers.setText("1");
                ivjTxtNumberOfUsers.setBounds(158, 72, 79, 20);
                ivjTxtNumberOfUsers.setEditable(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtNumberOfUsers;
    }

    /**
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    protected void handleException(java.lang.Throwable exception) {
        System.out.println(exception.getLocalizedMessage());
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
        getBtnGenerate().addActionListener(ivjEventHandler);
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
            setName("LicenceGeneratorView");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(426, 228);
            setTitle("Lizenz Generator");
            setContentPane(getBaseFrameContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        getTxtNumberOfUsers().setEditable(false);
        // user code end
    }

    /**
     * Initialize View. Call this method in a View's standard initialize method to setup your stuff.
     *
     * @see #initialize() // user code begin {1}..user code end
     */
    protected void initializeView() throws Exception {
        java.util.Vector<String> months = new java.util.Vector<>(7);
        months.add("1");
        months.add("2");
        months.add("3");
        months.add("6");
        months.add("12");
        months.add("24");
        //TODO NLS
        months.add("unbeschr�nkt");
        getCbxMonth().setModel(new javax.swing.DefaultComboBoxModel(months));
        getCbxProducts().setModel(new javax.swing.DefaultComboBoxModel(products.toArray()));
    }

    /**
     * main entrypoint - starts the part when it is run as an application
     *
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
        try {
            // @see #generateLicence()
            java.util.List<String> products = new java.util.ArrayList<>(3);
            products.add("TCO-Tool-Plugin (softEnvironment) V01.04.00");
            products.add("Fahrschultheorie (Regional)-V01.00.00");
            products.add("Fahrschultheorie (National)-V01.00.00");

            LicenceGeneratorView view = new LicenceGeneratorView(products);
            view.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            view.setVisible(true);
            java.awt.Insets insets = view.getInsets();
            view.setSize(view.getWidth() + insets.left + insets.right, view.getHeight() + insets.top + insets.bottom);
            view.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of ch.softenvironment.view.BaseFrame");
            exception.printStackTrace(System.out);
        }
    }
}
