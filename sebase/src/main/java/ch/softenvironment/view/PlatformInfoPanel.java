package ch.softenvironment.view;

import ch.softenvironment.client.ResourceManager;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Panel to show some important Info about Operating System and Java. Use {@link #createDialog(JFrame) } to open Panel within a Dialog.
 *
 * @author Peter Hirzel
 */
public class PlatformInfoPanel extends BasePanel {

    private javax.swing.JLabel ivjLblJavaVersion = null;
    private javax.swing.JTextField ivjTxtJavaVersion = null;
    private javax.swing.JLabel ivjLblJavaVMVersion = null;
    private javax.swing.JTextField ivjTxtJavaVMVersion = null;
    private javax.swing.JLabel ivjLblOSArchitecture = null;
    private javax.swing.JLabel ivjLblOSLocale = null;
    private javax.swing.JLabel ivjLblOSName = null;
    private javax.swing.JLabel ivjLblOSVersion = null;
    private javax.swing.JPanel ivjPnlJava = null;
    private javax.swing.JPanel ivjPnlOS = null;
    private javax.swing.JTextField ivjTxtOSArchitecture = null;
    private javax.swing.JTextField ivjTxtOSLocale = null;
    private javax.swing.JTextField ivjTxtOSName = null;
    private javax.swing.JTextField ivjTxtOSVersion = null;

    /**
     * PlatformInfoPanel constructor comment.
     */
    public PlatformInfoPanel() {
        super();
        initialize();
    }

    /**
     * PlatformInfoPanel constructor comment.
     *
     * @param layout java.awt.LayoutManager
     */
    public PlatformInfoPanel(java.awt.LayoutManager layout) {
        super(layout);
    }

    /**
     * PlatformInfoPanel constructor comment.
     *
     * @param layout java.awt.LayoutManager
     * @param isDoubleBuffered boolean
     */
    public PlatformInfoPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
     * PlatformInfoPanel constructor comment.
     *
     * @param isDoubleBuffered boolean
     */
    public PlatformInfoPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
     * Return the LblJavaVersion property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblJavaVersion() {
        if (ivjLblJavaVersion == null) {
            try {
                ivjLblJavaVersion = new javax.swing.JLabel();
                ivjLblJavaVersion.setName("LblJavaVersion");
                ivjLblJavaVersion.setText("Version:");
                ivjLblJavaVersion.setBounds(10, 20, 140, 14);
                // user code begin {1}
                ivjLblJavaVersion.setText(ResourceManager.getResource(PlatformInfoPanel.class, "LblJavaVersion_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblJavaVersion;
    }

    /**
     * Return the LblJavaVmVersion property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblJavaVMVersion() {
        if (ivjLblJavaVMVersion == null) {
            try {
                ivjLblJavaVMVersion = new javax.swing.JLabel();
                ivjLblJavaVMVersion.setName("LblJavaVMVersion");
                ivjLblJavaVMVersion.setToolTipText("Virtual Machine");
                ivjLblJavaVMVersion.setText("VM Version:");
                ivjLblJavaVMVersion.setBounds(10, 47, 140, 14);
                // user code begin {1}
                ivjLblJavaVMVersion.setText(ResourceManager.getResource(PlatformInfoPanel.class, "LblJavaVMVersion_text"));
                ivjLblJavaVMVersion.setToolTipText(ResourceManager.getResource(PlatformInfoPanel.class, "LblJavaVMVersion_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblJavaVMVersion;
    }

    /**
     * Return the LblOSArchitecture property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblOSArchitecture() {
        if (ivjLblOSArchitecture == null) {
            try {
                ivjLblOSArchitecture = new javax.swing.JLabel();
                ivjLblOSArchitecture.setName("LblOSArchitecture");
                ivjLblOSArchitecture.setText("Architecture:");
                ivjLblOSArchitecture.setBounds(10, 44, 140, 14);
                // user code begin {1}
                ivjLblOSArchitecture.setText(ResourceManager.getResource(PlatformInfoPanel.class, "LblOSArchitecture_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblOSArchitecture;
    }

    /**
     * Return the LblOSLocale property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblOSLocale() {
        if (ivjLblOSLocale == null) {
            try {
                ivjLblOSLocale = new javax.swing.JLabel();
                ivjLblOSLocale.setName("LblOSLocale");
                ivjLblOSLocale.setText("Locale:");
                ivjLblOSLocale.setBounds(10, 94, 140, 14);
                // user code begin {1}
                ivjLblOSLocale.setText(ResourceManager.getResource(PlatformInfoPanel.class, "LblOSLocale_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblOSLocale;
    }

    /**
     * Return the LblOS property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblOSName() {
        if (ivjLblOSName == null) {
            try {
                ivjLblOSName = new javax.swing.JLabel();
                ivjLblOSName.setName("LblOSName");
                ivjLblOSName.setText("Name:");
                ivjLblOSName.setBounds(10, 20, 140, 14);
                // user code begin {1}
                ivjLblOSName.setText(ResourceManager.getResource(PlatformInfoPanel.class, "LblOSName_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblOSName;
    }

    /**
     * Return the LblOSVersion property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblOSVersion() {
        if (ivjLblOSVersion == null) {
            try {
                ivjLblOSVersion = new javax.swing.JLabel();
                ivjLblOSVersion.setName("LblOSVersion");
                ivjLblOSVersion.setText("Version:");
                ivjLblOSVersion.setBounds(10, 70, 140, 14);
                // user code begin {1}
                ivjLblOSVersion.setText(ResourceManager.getResource(PlatformInfoPanel.class, "LblOSVersion_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblOSVersion;
    }

    /**
     * Return the PnlJava property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlJava() {
        if (ivjPnlJava == null) {
            try {
                ivjPnlJava = new javax.swing.JPanel();
                ivjPnlJava.setName("PnlJava");
                ivjPnlJava.setLayout(null);
                ivjPnlJava.setBounds(5, 150, 336, 76);
                getPnlJava().add(getLblJavaVersion(), getLblJavaVersion().getName());
                getPnlJava().add(getLblJavaVMVersion(), getLblJavaVMVersion().getName());
                getPnlJava().add(getTxtJavaVersion(), getTxtJavaVersion().getName());
                getPnlJava().add(getTxtJavaVMVersion(), getTxtJavaVMVersion().getName());
                // user code begin {1}
                getPnlJava().setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder("Java"),
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlJava;
    }

    /**
     * Return the PnlOS property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlOS() {
        if (ivjPnlOS == null) {
            try {
                ivjPnlOS = new javax.swing.JPanel();
                ivjPnlOS.setName("PnlOS");
                ivjPnlOS.setLayout(null);
                ivjPnlOS.setBounds(5, 16, 338, 120);
                getPnlOS().add(getLblOSName(), getLblOSName().getName());
                getPnlOS().add(getTxtOSName(), getTxtOSName().getName());
                getPnlOS().add(getLblOSArchitecture(), getLblOSArchitecture().getName());
                getPnlOS().add(getTxtOSArchitecture(), getTxtOSArchitecture().getName());
                getPnlOS().add(getLblOSVersion(), getLblOSVersion().getName());
                getPnlOS().add(getTxtOSVersion(), getTxtOSVersion().getName());
                getPnlOS().add(getLblOSLocale(), getLblOSLocale().getName());
                getPnlOS().add(getTxtOSLocale(), getTxtOSLocale().getName());
                // user code begin {1}
                getPnlOS().setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder(ResourceManager.getResource(PlatformInfoPanel.class, "PnlOS_title")),
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlOS;
    }

    /**
     * Return the TxtJavaVersion property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtJavaVersion() {
        if (ivjTxtJavaVersion == null) {
            try {
                ivjTxtJavaVersion = new javax.swing.JTextField();
                ivjTxtJavaVersion.setName("TxtJavaVersion");
                ivjTxtJavaVersion.setBounds(160, 17, 168, 20);
                ivjTxtJavaVersion.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtJavaVersion;
    }

    /**
     * Return the TxtJavaVmVersion property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtJavaVMVersion() {
        if (ivjTxtJavaVMVersion == null) {
            try {
                ivjTxtJavaVMVersion = new javax.swing.JTextField();
                ivjTxtJavaVMVersion.setName("TxtJavaVMVersion");
                ivjTxtJavaVMVersion.setBounds(160, 44, 168, 20);
                ivjTxtJavaVMVersion.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtJavaVMVersion;
    }

    /**
     * Return the TxtOSArchitecture property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtOSArchitecture() {
        if (ivjTxtOSArchitecture == null) {
            try {
                ivjTxtOSArchitecture = new javax.swing.JTextField();
                ivjTxtOSArchitecture.setName("TxtOSArchitecture");
                ivjTxtOSArchitecture.setBounds(160, 41, 168, 20);
                ivjTxtOSArchitecture.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtOSArchitecture;
    }

    /**
     * Return the TxtOSLocale property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtOSLocale() {
        if (ivjTxtOSLocale == null) {
            try {
                ivjTxtOSLocale = new javax.swing.JTextField();
                ivjTxtOSLocale.setName("TxtOSLocale");
                ivjTxtOSLocale.setBounds(160, 90, 168, 20);
                ivjTxtOSLocale.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtOSLocale;
    }

    /**
     * Return the TxtOS property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtOSName() {
        if (ivjTxtOSName == null) {
            try {
                ivjTxtOSName = new javax.swing.JTextField();
                ivjTxtOSName.setName("TxtOSName");
                ivjTxtOSName.setBounds(160, 17, 168, 20);
                ivjTxtOSName.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtOSName;
    }

    /**
     * Return the TxtOSVersion property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtOSVersion() {
        if (ivjTxtOSVersion == null) {
            try {
                ivjTxtOSVersion = new javax.swing.JTextField();
                ivjTxtOSVersion.setName("TxtOSVersion");
                ivjTxtOSVersion.setBounds(160, 65, 168, 20);
                ivjTxtOSVersion.setEditable(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtOSVersion;
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
            setName("PlatformInfoPanel");
            setLayout(null);
            setSize(353, 240);
            add(getPnlOS(), getPnlOS().getName());
            add(getPnlJava(), getPnlJava().getName());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        getTxtOSName().setText(System.getProperty("os.name"));
        getTxtOSArchitecture().setText(System.getProperty("os.arch"));
        getTxtOSVersion().setText(System.getProperty("os.version"));
        getTxtOSLocale().setText(java.util.Locale.getDefault().toString());
        getTxtJavaVersion().setText(System.getProperty("java.version"));
        getTxtJavaVMVersion().setText(System.getProperty("java.vm.version"));
        // user code end
    }

    /**
     * Create a Dialog showing this Panel.
     *
     * @param owner
     * @return
     */
    public static JDialog createDialog(JFrame owner) {
        PlatformInfoPanel panel = new PlatformInfoPanel();
        JDialog dialog = new JDialog(owner, ResourceManager.getResource(PlatformInfoPanel.class, "Dialog_title"));
        dialog.getContentPane().add(panel);
        dialog.setSize(panel.getWidth() + 10, panel.getHeight() + 30);
        return dialog;
    }

}
