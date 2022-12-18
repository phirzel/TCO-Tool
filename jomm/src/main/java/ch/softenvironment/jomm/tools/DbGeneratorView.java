package ch.softenvironment.jomm.tools;

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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.view.AboutDialog;
import ch.softenvironment.view.FileNamePanel;
import ch.softenvironment.view.SimpleEditorPanel;
import ch.softenvironment.view.StatusBar;
import ch.softenvironment.view.ToolBar;
import ch.softenvironment.view.ViewOptions;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;

/**
 * Tool to execute any-SQL-Code.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DbGeneratorView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame {

    // Launcher as Singleton
    private DbObjectServer server = null;
    private JPanel ivjJFrameContentPane = null;
    private JMenuBar ivjLauncherViewJMenuBar = null;
    private JMenuItem ivjMniAboutBox = null;
    private JMenuItem ivjMniExit = null;
    private JMenuItem ivjMniHelpTopics = null;
    private JMenu ivjMnuFile = null;
    private JMenu ivjMnuHelp = null;
    private JMenu ivjMnuView = null;
    private JPanel ivjPnlMain = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private JCheckBoxMenuItem ivjMncToolbar = null;
    private ToolBar ivjTlbToolbar = null;
    private JCheckBoxMenuItem ivjMncStatusbar = null;
    private StatusBar ivjStbStatusbar = null;
    private JMenu ivjMnuLookAndFeel = null;
    private JButton ivjBtnExecute = null;
    private FileNamePanel ivjPnlFile = null;
    private JLabel ivjJLabel1 = null;
    private JLabel ivjJLabel2 = null;
    private SimpleEditorPanel ivjPnlLog = null;
    private JLabel ivjJLabel3 = null;
    private SimpleEditorPanel ivjPnlExecuting = null;

    class IvjEventHandler implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == DbGeneratorView.this.getMncStatusbar()) {
                connEtoM2(e);
            }
            if (e.getSource() == DbGeneratorView.this.getMniAboutBox()) {
                connEtoC1(e);
            }
            if (e.getSource() == DbGeneratorView.this.getMniHelpTopics()) {
                connEtoC5(e);
            }
            if (e.getSource() == DbGeneratorView.this.getMncToolbar()) {
                connEtoM3(e);
            }
            if (e.getSource() == DbGeneratorView.this.getMniExit()) {
                connEtoC8(e);
            }
            if (e.getSource() == DbGeneratorView.this.getBtnExecute()) {
                connEtoC2(e);
            }
        }
    }

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     * @param objectServer Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public DbGeneratorView(ch.softenvironment.view.ViewOptions viewOptions, ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(viewOptions);
        this.server = objectServer;
        initialize();
    }

    /**
     * connEtoC1: (MniAboutBox.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniAboutBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniAboutBox_ActionPerformed(arg1);
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (MniStatistic.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniStatistic_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.executeFile();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5: (MniHelpTopics.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView .mniHelpTopics_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniHelpTopics_ActionPerformed(arg1);
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC8: (MniExit.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.dispose()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.dispose();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoM2: (MniStatusbar.action.actionPerformed(java.awt.event.ActionEvent) --> StbStatus.toggleVisbility()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoM2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            getStbStatusbar().toggleVisbility();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoM3: (MncToolbar.action.actionPerformed(java.awt.event.ActionEvent) --> TlbToolbar.toggleVisbility()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoM3(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            getTlbToolbar().toggleVisbility();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    @Override
    public void dispose() {
        disposeApplication(server);
    }

    /**
     * Execute a SQL-Syntax file, where each statement is assumed to be terminated by a semikolon.
     *
     * @see DbDataGenerator #executeFile()
     */
    public void executeFile() {
        String fileName = getPnlFile().getText();
        if (!StringUtils.isNullOrEmpty(fileName)) {
            try {
                DbDataGenerator.executeSqlCode(server, fileName);
            } catch (Throwable e) {
                log.error("Generating failure <{}>", fileName, e);
                getPnlLog().setText("read error: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Return the BtnExecute property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnExecute() {
        if (ivjBtnExecute == null) {
            try {
                ivjBtnExecute = new javax.swing.JButton();
                ivjBtnExecute.setName("BtnExecute");
                ivjBtnExecute.setText("Execute");
                ivjBtnExecute.setBounds(279, 65, 85, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnExecute;
    }

    /**
     * Return the JFrameContentPane property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getJFrameContentPane() {
        if (ivjJFrameContentPane == null) {
            try {
                ivjJFrameContentPane = new javax.swing.JPanel();
                ivjJFrameContentPane.setName("JFrameContentPane");
                ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
                getJFrameContentPane().add(getTlbToolbar(), "North");
                getJFrameContentPane().add(getPnlMain(), "Center");
                getJFrameContentPane().add(getStbStatusbar(), "South");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJFrameContentPane;
    }

    /**
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel1() {
        if (ivjJLabel1 == null) {
            try {
                ivjJLabel1 = new javax.swing.JLabel();
                ivjJLabel1.setName("JLabel1");
                ivjJLabel1.setText("Text-Datei mit SQL-Code (z.B. 'MySchema.sql'):");
                ivjJLabel1.setBounds(13, 12, 296, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel1;
    }

    /**
     * Return the JLabel2 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel2() {
        if (ivjJLabel2 == null) {
            try {
                ivjJLabel2 = new javax.swing.JLabel();
                ivjJLabel2.setName("JLabel2");
                ivjJLabel2.setText("Log:");
                ivjJLabel2.setBounds(17, 200, 130, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel2;
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
                ivjJLabel3.setText("Executing:");
                ivjJLabel3.setBounds(16, 93, 137, 14);
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
     * Return the LauncherViewJMenuBar property value.
     *
     * @return javax.swing.JMenuBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuBar getLauncherViewJMenuBar() {
        if (ivjLauncherViewJMenuBar == null) {
            try {
                ivjLauncherViewJMenuBar = new javax.swing.JMenuBar();
                ivjLauncherViewJMenuBar.setName("LauncherViewJMenuBar");
                ivjLauncherViewJMenuBar.add(getMnuFile());
                ivjLauncherViewJMenuBar.add(getMnuView());
                ivjLauncherViewJMenuBar.add(getMnuHelp());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLauncherViewJMenuBar;
    }

    /**
     * Return the MniStatusbar property value.
     *
     * @return javax.swing.JCheckBoxMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBoxMenuItem getMncStatusbar() {
        if (ivjMncStatusbar == null) {
            try {
                ivjMncStatusbar = new javax.swing.JCheckBoxMenuItem();
                ivjMncStatusbar.setName("MncStatusbar");
                ivjMncStatusbar.setSelected(true);
                ivjMncStatusbar.setText("Statusleiste");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMncStatusbar;
    }

    /**
     * Return the MncToolbar property value.
     *
     * @return javax.swing.JCheckBoxMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBoxMenuItem getMncToolbar() {
        if (ivjMncToolbar == null) {
            try {
                ivjMncToolbar = new javax.swing.JCheckBoxMenuItem();
                ivjMncToolbar.setName("MncToolbar");
                ivjMncToolbar.setSelected(false);
                ivjMncToolbar.setText("Werkzeugleiste");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMncToolbar;
    }

    /**
     * Return the MniAboutBox property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniAboutBox() {
        if (ivjMniAboutBox == null) {
            try {
                ivjMniAboutBox = new javax.swing.JMenuItem();
                ivjMniAboutBox.setName("MniAboutBox");
                ivjMniAboutBox.setText("Info");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniAboutBox;
    }

    /**
     * Return the MniExit property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniExit() {
        if (ivjMniExit == null) {
            try {
                ivjMniExit = new javax.swing.JMenuItem();
                ivjMniExit.setName("MniExit");
                ivjMniExit.setText("Beenden");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniExit;
    }

    /**
     * Return the MniHelpTopics property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniHelpTopics() {
        if (ivjMniHelpTopics == null) {
            try {
                ivjMniHelpTopics = new javax.swing.JMenuItem();
                ivjMniHelpTopics.setName("MniHelpTopics");
                ivjMniHelpTopics.setText("Hilfethemen");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniHelpTopics;
    }

    /**
     * Return the MnuFile property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuFile() {
        if (ivjMnuFile == null) {
            try {
                ivjMnuFile = new javax.swing.JMenu();
                ivjMnuFile.setName("MnuFile");
                ivjMnuFile.setText("Datei");
                ivjMnuFile.setEnabled(true);
                ivjMnuFile.add(getMniExit());
                // user code begin {1}
                // ivjMnuFile.setText(resLauncherView.getString("MnuFile_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuFile;
    }

    /**
     * Return the MnuHelp property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuHelp() {
        if (ivjMnuHelp == null) {
            try {
                ivjMnuHelp = new javax.swing.JMenu();
                ivjMnuHelp.setName("MnuHelp");
                ivjMnuHelp.setText("Hilfe");
                ivjMnuHelp.setEnabled(true);
                ivjMnuHelp.add(getMniHelpTopics());
                ivjMnuHelp.add(getMniAboutBox());
                // user code begin {1}
                // ivjMnuHelp.setText(resLauncherView.getString("Hilfe"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuHelp;
    }

    /**
     * Return the MnuLookAndFeel property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuLookAndFeel() {
        if (ivjMnuLookAndFeel == null) {
            try {
                ivjMnuLookAndFeel = new javax.swing.JMenu();
                ivjMnuLookAndFeel.setName("MnuLookAndFeel");
                ivjMnuLookAndFeel.setText("Look & Feel");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuLookAndFeel;
    }

    /**
     * Return the MnuView property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuView() {
        if (ivjMnuView == null) {
            try {
                ivjMnuView = new javax.swing.JMenu();
                ivjMnuView.setName("MnuView");
                ivjMnuView.setText("Ansicht");
                ivjMnuView.add(getMncToolbar());
                ivjMnuView.add(getMncStatusbar());
                ivjMnuView.add(getMnuLookAndFeel());
                // user code begin {1}
                // ivjMnuView.setText(resLauncherView.getString("MnuView_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuView;
    }

    /**
     * Return the PnlExecuting property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.SimpleEditorPanel getPnlExecuting() {
        if (ivjPnlExecuting == null) {
            try {
                ivjPnlExecuting = new ch.softenvironment.view.SimpleEditorPanel();
                ivjPnlExecuting.setName("PnlExecuting");
                ivjPnlExecuting.setBounds(15, 117, 371, 74);
                ivjPnlExecuting.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlExecuting;
    }

    /**
     * Return the PnlFile property value.
     *
     * @return ch.softenvironment.view.FileNamePanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.FileNamePanel getPnlFile() {
        if (ivjPnlFile == null) {
            try {
                ivjPnlFile = new ch.softenvironment.view.FileNamePanel();
                ivjPnlFile.setName("PnlFile");
                ivjPnlFile.setBounds(13, 31, 371, 28);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlFile;
    }

    /**
     * Return the PnlLog property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.SimpleEditorPanel getPnlLog() {
        if (ivjPnlLog == null) {
            try {
                ivjPnlLog = new ch.softenvironment.view.SimpleEditorPanel();
                ivjPnlLog.setName("PnlLog");
                ivjPnlLog.setBounds(13, 223, 371, 106);
                ivjPnlLog.setEnabled(false);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlLog;
    }

    /**
     * Return the PnlMain property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlMain() {
        if (ivjPnlMain == null) {
            try {
                ivjPnlMain = new javax.swing.JPanel();
                ivjPnlMain.setName("PnlMain");
                ivjPnlMain.setLayout(null);
                getPnlMain().add(getPnlFile(), getPnlFile().getName());
                getPnlMain().add(getBtnExecute(), getBtnExecute().getName());
                getPnlMain().add(getJLabel1(), getJLabel1().getName());
                getPnlMain().add(getJLabel2(), getJLabel2().getName());
                getPnlMain().add(getPnlLog(), getPnlLog().getName());
                getPnlMain().add(getJLabel3(), getJLabel3().getName());
                getPnlMain().add(getPnlExecuting(), getPnlExecuting().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlMain;
    }

    /**
     * Return the StbStatus property value.
     *
     * @return ch.softenvironment.view.StatusBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.StatusBar getStbStatusbar() {
        if (ivjStbStatusbar == null) {
            try {
                ivjStbStatusbar = new ch.softenvironment.view.StatusBar();
                ivjStbStatusbar.setName("StbStatusbar");
                ivjStbStatusbar.setMinimumSize(new java.awt.Dimension(8, 14));
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjStbStatusbar;
    }

    /**
     * Return the TlbToolbar property value.
     *
     * @return ch.softenvironment.view.ToolBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.ToolBar getTlbToolbar() {
        if (ivjTlbToolbar == null) {
            try {
                ivjTlbToolbar = new ch.softenvironment.view.ToolBar();
                ivjTlbToolbar.setName("TlbToolbar");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTlbToolbar;
    }

    /**
     * Return the Version of this Application.
     */
    protected static String getVersion() {
        return "V1.2.0 beta";
    }

    /**
     * Called whenever the part throws an exception.
     * <p>
     * VAJ (Java 1.2) generates Throwable for connection mappings
     *
     * @param exception java.lang.Throwable
     */
    @Override
    protected void handleException(Throwable exception) {
        super.handleException(new Exception(exception));
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
        getMncStatusbar().addActionListener(ivjEventHandler);
        getMniAboutBox().addActionListener(ivjEventHandler);
        getMniHelpTopics().addActionListener(ivjEventHandler);
        getMncToolbar().addActionListener(ivjEventHandler);
        getMniExit().addActionListener(ivjEventHandler);
        getBtnExecute().addActionListener(ivjEventHandler);
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
            setName("LauncherView");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("DB-Generator");
            setSize(403, 447);
            setJMenuBar(getLauncherViewJMenuBar());
            setContentPane(getJFrameContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        // user code end
    }

    /**
     * Setup the database connection.
     */
    private static DbObjectServer initializeDatabase(String userId, String password, String url) throws Exception {
        // ch.softenvironment.business.persistency.Registry.registerAll();

        javax.jdo.PersistenceManagerFactory pmFactory = new ch.softenvironment.jomm.target.sql.msaccess.MsAccessObjectServerFactory();
        pmFactory.setConnectionURL(url);
        pmFactory.setNontransactionalRead(false); // NO autoCommit while reading
        pmFactory.setNontransactionalWrite(false); // NO autoCommit while
        // writing
        DbObjectServer objSrv = (DbObjectServer) pmFactory.getPersistenceManager(userId, password);

        // settings = getUserProfile(objSrv, userId);

        return objSrv;
    }

    /**
     * Initialize the view.
     */
    @Override
    protected void initializeView() {
        createLookAndFeelMenu(getMnuLookAndFeel());
        getTlbToolbar().toggleVisbility();
    }

    /**
     * main entrypoint - starts the part when it is run as an application
     *
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
        DbGeneratorView instance = null;
        try {
            setSystemLookAndFeel();

            ch.softenvironment.jomm.mvc.view.DbLoginDialog dialog = new ch.softenvironment.jomm.mvc.view.DbLoginDialog(null, "jdbc:odbc:MyOdbcDSN");
            if (!dialog.isSaved()) {
                System.exit(0);
            }

            // establish an initialize database connection
            DbObjectServer objSrv = initializeDatabase(dialog.getUserId(), dialog.getPassword(), dialog.getUrl());

            showSplashScreen(new java.awt.Dimension(500, 400), ch.ehi.basics.i18n.ResourceBundle.getImageIcon(DbGeneratorView.class, "splash.png"));

            instance = new DbGeneratorView(new ViewOptions(), objSrv);
            // getInstance().addDefaultClosingListener();
            // instance.setLookAndFeel(settings.getLookAndFeel());
            java.awt.Insets insets = instance.getInsets();
            instance.setSize(instance.getWidth() + insets.left + insets.right, instance.getHeight() + insets.top + insets.bottom);
            // ViewOptions
            // instance.getViewOptions().setOption("ShowProjectLepra");
            instance.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JFrame");//$NON-NLS-1$
            exception.printStackTrace(System.out);
            showException(instance, /*
             * resLauncherView.getString("CTLoginFailure"
             * ), "...",
             */exception); //$NON-NLS-1$
            System.exit(-1);
        }
    }

    /**
     * Show About Dialog
     */
    private void mniAboutBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        new AboutDialog(this, "DB-Generator", getVersion(), "2006", null);
    }

    /**
     * Comment
     */
    private void mniHelpTopics_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        ch.ehi.basics.view.BrowserControl.displayURL("http://www.softenvironment.ch");
    }
}
