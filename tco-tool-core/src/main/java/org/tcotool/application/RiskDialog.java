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

/**
 * Dialog for Risk-Parameter settings.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.3 $ $Date: 2008-05-18 15:44:58 $
 * @deprecated
 */

public class RiskDialog extends ch.softenvironment.view.BaseDialog {

    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JLabel ivjJLabel3 = null;
    private javax.swing.JLabel ivjJLabel31 = null;
    private javax.swing.JLabel ivjJLabel4 = null;
    private javax.swing.JTextField ivjJTextField1 = null;
    private javax.swing.JLabel ivjJLabel5 = null;
    private javax.swing.JLabel ivjJLabel6 = null;
    private javax.swing.JLabel ivjJLabel61 = null;
    private javax.swing.JLabel ivjJLabel62 = null;
    private javax.swing.JLabel ivjJLabel7 = null;
    private javax.swing.JTextField ivjJTextField2 = null;

    /**
     * RiskDialog constructor comment.
     *
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     * @param modal boolean
     */
    public RiskDialog(java.awt.Frame owner, String title, boolean modal) {
        super(owner, modal);
        initialize();
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
                ivjBaseDialogContentPane.setLayout(null);
                getBaseDialogContentPane().add(getJLabel3(), getJLabel3().getName());
                getBaseDialogContentPane().add(getJLabel4(), getJLabel4().getName());
                getBaseDialogContentPane().add(getJTextField1(), getJTextField1().getName());
                getBaseDialogContentPane().add(getJLabel31(), getJLabel31().getName());
                getBaseDialogContentPane().add(getJLabel5(), getJLabel5().getName());
                getBaseDialogContentPane().add(getJLabel6(), getJLabel6().getName());
                getBaseDialogContentPane().add(getJLabel61(), getJLabel61().getName());
                getBaseDialogContentPane().add(getJLabel62(), getJLabel62().getName());
                getBaseDialogContentPane().add(getJLabel7(), getJLabel7().getName());
                getBaseDialogContentPane().add(getJTextField2(), getJTextField2().getName());
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
     * Return the JLabel3 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel3() {
        if (ivjJLabel3 == null) {
            try {
                ivjJLabel3 = new javax.swing.JLabel();
                ivjJLabel3.setName("JLabel3");
                ivjJLabel3.setFont(new java.awt.Font("Arial", 1, 14));
                ivjJLabel3.setText("\"Weiche\" Risiken:");
                ivjJLabel3.setBounds(11, 60, 170, 22);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel3;
    }

    /**
     * Return the JLabel31 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel31() {
        if (ivjJLabel31 == null) {
            try {
                ivjJLabel31 = new javax.swing.JLabel();
                ivjJLabel31.setName("JLabel31");
                ivjJLabel31.setFont(new java.awt.Font("Arial", 1, 14));
                ivjJLabel31.setText("\"Harte\" Risiken:");
                ivjJLabel31.setBounds(11, 11, 170, 22);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel31;
    }

    /**
     * Return the JLabel4 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel4() {
        if (ivjJLabel4 == null) {
            try {
                ivjJLabel4 = new javax.swing.JLabel();
                ivjJLabel4.setName("JLabel4");
                ivjJLabel4.setText("Risiko-Index =>");
                ivjJLabel4.setBounds(18, 209, 99, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel4;
    }

    /**
     * Return the JLabel5 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel5() {
        if (ivjJLabel5 == null) {
            try {
                ivjJLabel5 = new javax.swing.JLabel();
                ivjJLabel5.setName("JLabel5");
                ivjJLabel5.setText("Ausfall/Monat:");
                ivjJLabel5.setBounds(22, 349, 89, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel5;
    }

    /**
     * Return the JLabel6 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel6() {
        if (ivjJLabel6 == null) {
            try {
                ivjJLabel6 = new javax.swing.JLabel();
                ivjJLabel6.setName("JLabel6");
                ivjJLabel6.setText("gelegentl. Benutzer:");
                ivjJLabel6.setBounds(118, 350, 122, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel6;
    }

    /**
     * Return the JLabel61 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel61() {
        if (ivjJLabel61 == null) {
            try {
                ivjJLabel61 = new javax.swing.JLabel();
                ivjJLabel61.setName("JLabel61");
                ivjJLabel61.setText("Standard Benutzer:");
                ivjJLabel61.setBounds(252, 349, 122, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel61;
    }

    /**
     * Return the JLabel62 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel62() {
        if (ivjJLabel62 == null) {
            try {
                ivjJLabel62 = new javax.swing.JLabel();
                ivjJLabel62.setName("JLabel62");
                ivjJLabel62.setText("Haupt-Benutzer:");
                ivjJLabel62.setBounds(393, 349, 122, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel62;
    }

    /**
     * Return the JLabel7 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel7() {
        if (ivjJLabel7 == null) {
            try {
                ivjJLabel7 = new javax.swing.JLabel();
                ivjJLabel7.setName("JLabel7");
                ivjJLabel7.setText("XP");
                ivjJLabel7.setBounds(43, 380, 45, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel7;
    }

    /**
     * Return the JTextField1 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getJTextField1() {
        if (ivjJTextField1 == null) {
            try {
                ivjJTextField1 = new javax.swing.JTextField();
                ivjJTextField1.setName("JTextField1");
                ivjJTextField1.setBackground(java.awt.Color.pink);
                ivjJTextField1.setBounds(283, 207, 57, 20);
                ivjJTextField1.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTextField1;
    }

    /**
     * Return the JTextField2 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getJTextField2() {
        if (ivjJTextField2 == null) {
            try {
                ivjJTextField2 = new javax.swing.JTextField();
                ivjJTextField2.setName("JTextField2");
                ivjJTextField2.setText("0.05");
                ivjJTextField2.setBounds(127, 370, 92, 20);
                ivjJTextField2.setEditable(false);
                ivjJTextField2.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTextField2;
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
            setName("RiskDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(594, 489);
            setTitle("Risiko-Parameter");
            setContentPane(getBaseDialogContentPane());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        //TODO setIconImage(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png").getImage());
        // user code end
    }
}
