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
 * Product Info Dialog.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class AboutDialog extends ch.softenvironment.view.BaseDialog {

    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JLabel ivjLblCopyright = null;
    private javax.swing.JLabel ivjLblLicencedFor = null;
    private javax.swing.JLabel ivjLblTitle = null;
    private ch.softenvironment.view.SimpleEditorPanel ivjTxaLicence = null;
    private javax.swing.JLabel ivjLblIsb = null;
    private javax.swing.JLabel ivjLblPHW = null;
    private javax.swing.JLabel ivjLblThanks = null;
    private javax.swing.JTextArea ivjTxaAbout = null;

    /**
     * AboutDialog constructor comment.
     *
     * @param owner java.awt.Frame
     * @param title java.lang.String
     * @param modal boolean
     */
    public AboutDialog(java.awt.Frame owner, final String application, final String version, final String copyrightPeriod) {
        super(owner, true);
        initialize();

        setTitle(getResourceString("FrmWindow_text") + " " + application);
        getLblTitle().setText(application + " " + version);
        getLblCopyright().setText(/* "Copyright (c) softEnvironment " + */copyrightPeriod);
        // getTxaLicence().setText(licence);

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
                ivjBaseDialogContentPane.setLayout(null);
                getBaseDialogContentPane().add(getLblTitle(), getLblTitle().getName());
                getBaseDialogContentPane().add(getLblLicencedFor(), getLblLicencedFor().getName());
                getBaseDialogContentPane().add(getLblCopyright(), getLblCopyright().getName());
                getBaseDialogContentPane().add(getTxaAbout(), getTxaAbout().getName());
                getBaseDialogContentPane().add(getLblIsb(), getLblIsb().getName());
                getBaseDialogContentPane().add(getLblPHW(), getLblPHW().getName());
                getBaseDialogContentPane().add(getLblThanks(), getLblThanks().getName());
                getBaseDialogContentPane().add(getTxaLicence(), getTxaLicence().getName());
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
                ivjLblCopyright.setText("Copyright (c) softEnvironment GmbH 2004-2012");
                ivjLblCopyright.setBounds(309, 560, 327, 27);
                // user code begin {1}
                ivjLblCopyright.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
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
     * Return the LblIsb property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblIsb() {
        if (ivjLblIsb == null) {
            try {
                ivjLblIsb = new javax.swing.JLabel();
                ivjLblIsb.setName("LblIsb");
                ivjLblIsb.setToolTipText("http://www.isb.admin.ch/internet/strategien/00665/01491/index.html?lang=de");
                ivjLblIsb.setText("ISB");
                ivjLblIsb.setBounds(15, 222, 630, 87);
                // user code begin {1}
                ivjLblIsb.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(AboutDialog.class, "isb_de.gif"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblIsb;
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
                ivjLblLicencedFor.setText("Open Source");
                ivjLblLicencedFor.setBounds(12, 403, 285, 26);
                // user code begin {1}
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
     * Return the LblPHW property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPHW() {
        if (ivjLblPHW == null) {
            try {
                ivjLblPHW = new javax.swing.JLabel();
                ivjLblPHW.setName("LblPHW");
                ivjLblPHW.setToolTipText("http://www.phw.info/");
                ivjLblPHW.setText("PHW");
                ivjLblPHW.setBounds(15, 321, 113, 38);
                // user code begin {1}
                ivjLblPHW.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(AboutDialog.class, "phw_logo.gif"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPHW;
    }

    /**
     * Return the LblThanks property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblThanks() {
        if (ivjLblThanks == null) {
            try {
                ivjLblThanks = new javax.swing.JLabel();
                ivjLblThanks.setName("LblThanks");
                ivjLblThanks.setFont(new java.awt.Font("Arial", 1, 12));
                ivjLblThanks.setText("Herzlichen Dank ans ISB und die PHW, welche die Entwicklung des TCO-Tools ermoeglicht haben!");
                ivjLblThanks.setBounds(15, 190, 630, 27);
                // user code begin {1}
                ivjLblThanks.setText(getResourceString("LblThanks_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblThanks;
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
                ivjLblTitle.setText("Prototyp Vx.x.x");
                ivjLblTitle.setBounds(15, 16, 461, 38);
                // user code begin {1}
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
     * Return the JTextArea1 property value.
     *
     * @return javax.swing.JTextArea
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextArea getTxaAbout() {
        if (ivjTxaAbout == null) {
            try {
                ivjTxaAbout = new javax.swing.JTextArea();
                ivjTxaAbout.setName("TxaAbout");
                ivjTxaAbout.setLineWrap(true);
                ivjTxaAbout.setWrapStyleWord(true);
                ivjTxaAbout
                    .setText("Dies ist ein Werkzeug zur Berechnung der Total Cost of Ownership fuer Unternehmen mit IT-Infrastruktur.\n\nWeitere Infos unter: http://www.tcotool.org");
                ivjTxaAbout.setBounds(15, 63, 630, 72);
                ivjTxaAbout.setEditable(false);
                ivjTxaAbout.setEnabled(true);
                // user code begin {1}
                ivjTxaAbout.setText(getResourceString("TxaAbout_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxaAbout;
    }

    /**
     * Return the TxaLicence property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.SimpleEditorPanel getTxaLicence() {
        if (ivjTxaLicence == null) {
            try {
                ivjTxaLicence = new ch.softenvironment.view.SimpleEditorPanel();
                ivjTxaLicence.setName("TxaLicence");
                ivjTxaLicence.setFont(new java.awt.Font("Arial", 1, 12));
                ivjTxaLicence.setText("Dieses Produkt enthaelt Software, die entwickelt wurde durch:\n- Apache Software Foundation (http://www.apache.org)");
                ivjTxaLicence.setBounds(12, 431, 630, 119);
                // user code begin {1}
                ivjTxaLicence.setEditable(false);
                ivjTxaLicence.setText(getResourceString("TxaLicence_text") + "\n- softEnvironment GmbH (http://www.softenvironment.ch)"
                    + "\n- Eisenhut Informatik AG (http://www.eisenhutinformatik.ch)" + "\n- Apache Software Foundation (http://www.apache.org)"
                    + "\n- JHotDraw (http://www.jhotdraw.org" + "\n- JFreeChart (http://www.jfree.org/jfreechart)"
                    + "\n- Java Plugin Framework (http://jpf.sourceforge.net/)");
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
            setSize(662, 646);
            setTitle("Info zum TCO-Tool");
            setContentPane(getBaseDialogContentPane());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        // user code end
    }
}
