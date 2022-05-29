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

import ch.ehi.basics.i18n.ResourceBundle;
import ch.ehi.basics.view.FileChooser;
import ch.ehi.basics.view.GenericFileFilter;
import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbUserTransactionBlock;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.serialize.CsvSerializer;
import ch.softenvironment.jomm.target.xml.IliBasket;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.tcotool.tco.SEPlugin;
import ch.softenvironment.util.ListUtils;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.ParserCSV;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.BaseFrame;
import ch.softenvironment.view.CommonUserAccess;
import ch.softenvironment.view.DetailView;
import ch.softenvironment.view.FileHistoryListener;
import ch.softenvironment.view.FileHistoryMenu;
import ch.softenvironment.view.JInternalFrameUtils;
import ch.softenvironment.view.PlatformInfoPanel;
import ch.softenvironment.view.SimpleEditorPanel;
import ch.softenvironment.view.StatusBar;
import ch.softenvironment.view.ToolBar;
import ch.softenvironment.view.ViewOptions;
import ch.softenvironment.view.WaitDialog;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import org.tcotool.model.Activity;
import org.tcotool.model.CostCause;
import org.tcotool.model.CostCentre;
import org.tcotool.model.Dependency;
import org.tcotool.model.Process;
import org.tcotool.model.Responsibility;
import org.tcotool.model.Role;
import org.tcotool.model.ServiceCategory;
import org.tcotool.model.Site;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.pluginsupport.ApplicationPlugin;
import org.tcotool.presentation.Diagram;
import org.tcotool.standard.charts.ChartTool;
import org.tcotool.standard.drawing.DependencyView;
import org.tcotool.standard.drawing.Layout;
import org.tcotool.standard.report.ReportComplete;
import org.tcotool.standard.report.ReportInvestment;
import org.tcotool.standard.report.ReportStaff;
import org.tcotool.standard.report.ReportTco;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.ModelUtility;

/**
 * Entry Point for TCO-Tool.
 *
 * <pre>
 * JVM arguments:
 * -Xmx512m (to increase Memory for Reports)
 * -Xdock:icon=./src/org/tcotool/application/resources/TCO_Icon.png (iMac only)
 * </pre>
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class LauncherView extends ch.softenvironment.jomm.mvc.view.DbBaseFrame implements FileHistoryListener, java.beans.PropertyChangeListener {

    @Deprecated
    protected static final String TEST_RELEASE = "TEST_FEATURE";
    private static LauncherView instance = null; // Singleton
    // private PluginManager pluginManager = null;
    private ModelUtility utility = null;
    private TcoObject currentElement = null;
    private ApplicationOptions settings = null;
    private FileHistoryMenu mnuFileHistory = null;
    private Image image = null;
    private JMenu mnuEdit = null;
    private JMenu mnuWindow = null;
    private JMenuItem mniFind = null;
    private JMenuItem mniWindowTile = null;
    private JMenuItem mniCurrency = null;
    private JMenuItem mniDependencyGraphTCO = null;
    private JPanel ivjBaseFrameContentPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private JMenuBar ivjLauncherViewJMenuBar = null;
    private JMenuItem ivjMniAbout = null;
    private JMenu ivjMnuExtras = null;
    private JMenu ivjMnuHelp = null;
    private JMenu ivjMnuLookAndFeel = null;
    private JMenu ivjMnuView = null;
    private StatusBar ivjStbStatus = null;
    private ToolBar ivjTlbStandard = null;
    private JMenuItem ivjMniHelp = null;
    private JMenu ivjMnuFile = null;
    private NavigationView ivjPnlNavigation = null;
    private JSeparator ivjJSeparator1 = null;
    private SimpleEditorPanel ivjPnlDocumentation = null;
    private JPanel ivjPnlMain = null;
    private JSplitPane ivjSppLeft = null;
    private JSplitPane ivjSppMain = null;
    private JDesktopPane ivjDtpRoot = null;
    private JMenuItem ivjMniFileExit = null;
    private JMenuItem ivjMniFileOpenWindow = null;
    private JMenuItem ivjMniFileSave = null;
    private JMenuItem ivjMniFileSaveAs = null;
    private JMenu ivjMnuLanguage = null;
    private JRadioButtonMenuItem ivjRbtLanguageEnglish = null;
    private JRadioButtonMenuItem ivjRbtLanguageFrench = null;
    private JRadioButtonMenuItem ivjRbtLanguageGerman = null;
    private JRadioButtonMenuItem ivjRbtLanguageItalian = null;
    private JRadioButtonMenuItem ivjRbtChinese = null;
    private JRadioButtonMenuItem ivjRbtRussian = null;
    private JMenuItem ivjMniFileNewDefault = null;
    private JMenuItem ivjMniNewItil = null;
    private JMenu ivjMniFileNew = null;
    private JMenuItem ivjMniCodes = null;
    private JMenuItem ivjMniReportComplete = null;
    private JMenuItem ivjMniReportPersonalCost = null;
    private JMenuItem ivjMniReportTotalTcoCost = null;
    private JMenuItem ivjMniDependencyGraph = null;
    private JMenuItem ivjMniTcoChart = null;
    private JMenu ivjMnuReports = null;
    private JMenu ivjMnuFont = null;
    private JRadioButtonMenuItem ivjRbtFontBig = null;
    private JRadioButtonMenuItem ivjRbtFontDefault = null;
    private JMenuItem ivjMniSystemParameter = null;
    private JMenu ivjMnuSettings = null;
    private JMenu ivjMnuReportFinance = null;
    private JMenu ivjMnuReportGeneral = null;
    private JMenu ivjMnuReportTco = null;
    private JMenuItem ivjMniFileImportCostCause = null;
    private JMenuItem ivjMniFileImportCostCentre = null;
    private JMenu ivjMnuFileImport = null;

    class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, ch.softenvironment.view.ToolBarListener, java.awt.event.ActionListener,
        java.awt.event.ItemListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == LauncherView.this.getMniAbout()) {
                connEtoC3(e);
            }
            if (e.getSource() == LauncherView.this.getMniHelp()) {
                connEtoC18(e);
            }
            if (e.getSource() == LauncherView.this.getMniFileExit()) {
                connEtoC6(e);
            }
            if (e.getSource() == LauncherView.this.getMniFileNewDefault()) {
                connEtoC1(e);
            }
            if (e.getSource() == LauncherView.this.getMniFileSave()) {
                connEtoC5(e);
            }
            if (e.getSource() == LauncherView.this.getMniFileSaveAs()) {
                connEtoC7(e);
            }
            if (e.getSource() == LauncherView.this.getMniFileOpenWindow()) {
                connEtoC8(e);
            }
            if (e.getSource() == LauncherView.this.getMniReportTotalTcoCost()) {
                connEtoC12(e);
            }
            if (e.getSource() == LauncherView.this.getMniSystemParameter()) {
                connEtoC14(e);
            }
            if (e.getSource() == LauncherView.this.getMniCodes()) {
                connEtoC4(e);
            }
            if (e.getSource() == LauncherView.this.getMniReportComplete()) {
                connEtoC15(e);
            }
            if (e.getSource() == LauncherView.this.getMniReportPersonalCost()) {
                connEtoC24(e);
            }
            if (e.getSource() == LauncherView.this.getMniDependencyGraph()) {
                connEtoC26(e);
            }
            if (e.getSource() == LauncherView.this.getMniTcoChart()) {
                connEtoC27(e);
            }
            if (e.getSource() == LauncherView.this.getRbtFontDefault()) {
                connEtoC29(e);
            }
            if (e.getSource() == LauncherView.this.getRbtFontBig()) {
                connEtoC30(e);
            }
            if (e.getSource() == LauncherView.this.getMniFileImportCostCentre()) {
                connEtoC16(e);
            }
            if (e.getSource() == LauncherView.this.getMniFileImportCostCause()) {
                connEtoC17(e);
            }
        }

        @Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == LauncherView.this.getRbtLanguageGerman()) {
                connEtoC19(e);
            }
            if (e.getSource() == LauncherView.this.getRbtLanguageFrench()) {
                connEtoC20(e);
            }
            if (e.getSource() == LauncherView.this.getRbtLanguageItalian()) {
                connEtoC21(e);
            }
            if (e.getSource() == LauncherView.this.getRbtLanguageEnglish()) {
                connEtoC22(e);
            }
            if (e.getSource() == LauncherView.this.getRbtChinese()) {
                connEtoC13(e);
            }
            if (e.getSource() == LauncherView.this.getRbtRussian()) {
                connEtoC23(e);
            }
        }

        @Override
        public void tbbCopyAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbCutAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbDeleteAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbFindAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbNewAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == LauncherView.this.getTlbStandard()) {
                connEtoC11(newEvent);
            }
        }

        @Override
        public void tbbOpenAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == LauncherView.this.getTlbStandard()) {
                connEtoC10(newEvent);
            }
        }

        @Override
        public void tbbPasteAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbPrintAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbRedoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbSaveAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == LauncherView.this.getTlbStandard()) {
                connEtoC9(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
            if (newEvent.getSource() == LauncherView.this.getPnlDocumentation()) {
                connEtoC2(newEvent);
            }
        }

    }

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public LauncherView(ch.softenvironment.view.ViewOptions viewOptions) {
        super(viewOptions);
        initialize();
    }

    /**
     * Constructor
     *
     * @param viewOptions Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public LauncherView(ch.softenvironment.view.ViewOptions viewOptions, ApplicationOptions settings) {
        super(viewOptions);
        instance = this;
        this.settings = settings;
        initialize();
    }

    /**
     * Add a MenuItem to create a Bar- & Pie-Chart for given dbCodeType.
     *
     * @param menu
     * @param dbCodeType
     */
    private void addMenuChartReports(JMenu menu, final Class dbCodeType) {
        final String title = ModelUtility.getTypeString(dbCodeType);

        // Bar-Chart
        JMenuItem item = new JMenuItem();
        item.setText(title);
        item.setIcon(ResourceBundle.getImageIcon(LauncherView.class, "bar_chart.png"));
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showBusy(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ChartTool tool = new ChartTool(getUtility(), getSettings());
                            addReport(tool.createTcoBarChart(dbCodeType), title /*
                             * getResourceString ( "MniTcoCostTypePieChart_text" )
                             */, getDtpRoot().getWidth(), getDtpRoot().getHeight());
                        } catch (Exception ex) {
                            handleException(ex);
                        }
                    }
                });
            }
        });
        menu.add(item);

        // Pie-Chart
        item = new JMenuItem();
        item.setText(title);
        item.setIcon(ResourceBundle.getImageIcon(LauncherView.class, "pie_chart.png"));
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showBusy(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ChartTool tool = new ChartTool(getUtility(), getSettings());
                            addReport(tool.createTcoPieChart(dbCodeType), title /*
                             * getResourceString ( "MniTcoCostTypePieChart_text" )
                             */, getDtpRoot().getWidth(), getDtpRoot().getHeight());
                        } catch (Exception ex) {
                            handleException(ex);
                        }
                    }
                });
            }
        });
        menu.add(item);
    }

    /**
     * Add a MenuItem to create a bar- & pie-chart of a (sub/second)-code within another code attached to a Service.
     *
     * @param menu
     * @param dbCodeTypeSecond (for e.g. CostCause.class)
     * @param dbCodeTypePrimary (for e.g. ServiceCategory.class)
     */
    private void addMenuChartReports(JMenu menu, final Class dbCodeTypeSecond, final Class dbCodeTypePrimary) {
        Object[] tokens = {ModelUtility.getTypeString(dbCodeTypeSecond), ModelUtility.getTypeString(dbCodeTypePrimary)};
        final String title = NlsUtils.formatMessage(getResourceString("MniSubordinateCodesWithin_text"), tokens);
        JMenuItem item = new JMenuItem();
        item.setText(title);
        item.setIcon(ResourceBundle.getImageIcon(LauncherView.class, "bar_chart.png"));
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                final java.util.List primaryCodes = ((TcoModel) getUtility().getRoot()).getObjectServer().retrieveCodes(dbCodeTypePrimary);
                if ((primaryCodes == null) || (primaryCodes.size() == 0)) {
                    BaseDialog.showWarning(getInstance(),
                        ResourceManager.getResource(ChartTool.class, "CWNoCode_title") + " " + ModelUtility.getTypeString(dbCodeTypePrimary),
                        ResourceManager.getResource(ChartTool.class, "CWNoCode_text"));
                    return;
                }
                showBusy(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Iterator it = primaryCodes.iterator();
                            // TODO if (!it.hasNext()) {
                            // showNoDataFor(dbCodeType) }
                            while (it.hasNext()) {
                                DbCodeType maskCode = (DbCodeType) it.next();
                                ChartTool tool = new ChartTool(getUtility(), getSettings(), maskCode);
                                JPanel panel = tool.createTcoBarChart(dbCodeTypeSecond);
                                addReport(panel, title + ": " + maskCode.getNameString(), getDtpRoot().getWidth(), getDtpRoot().getHeight() / 2);
                            }
                        } catch (Exception ex) {
                            handleException(ex);
                        }
                    }
                });
            }
        });
        menu.add(item);

        item = new JMenuItem();
        item.setText(title);
        item.setIcon(ResourceBundle.getImageIcon(LauncherView.class, "pie_chart.png"));
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                final java.util.List primaryCodes = ((TcoModel) getUtility().getRoot()).getObjectServer().retrieveCodes(dbCodeTypePrimary);
                if ((primaryCodes == null) || (primaryCodes.size() == 0)) {
                    BaseDialog.showWarning(getInstance(),
                        ResourceManager.getResource(ChartTool.class, "CWNoCode_title") + " " + ModelUtility.getTypeString(dbCodeTypePrimary),
                        ResourceManager.getResource(ChartTool.class, "CWNoCode_text"));
                    return;
                }
                showBusy(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Iterator it = primaryCodes.iterator();
                            while (it.hasNext()) {
                                DbCodeType maskCode = (DbCodeType) it.next();
                                ChartTool tool = new ChartTool(getUtility(), getSettings(), maskCode);
                                JPanel panel = tool.createTcoPieChart(dbCodeTypeSecond);
                                addReport(panel, title + ": " + maskCode.getNameString(), getDtpRoot().getWidth(), getDtpRoot().getHeight() / 2);
                            }
                        } catch (Exception ex) {
                            handleException(ex);
                        }
                    }
                });
            }
        });
        menu.add(item);
    }

    /**
     * Adds an internal Frame to DesktopPane.
     */
    public void addReport(JInternalFrame internalFrame) {
        // add Frame to Desktop
        getDtpRoot().add(internalFrame);
        try {
            internalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            Tracer.getInstance().runtimeWarning("PropertyVetoEception ignored");
        }

        internalFrame.setVisible(true);
    }

    /**
     * Add given graphical report panel and show by an InternalFrame.
     *
     * @param panel
     */
    public GraphicReportFrame addReport(JPanel panel, final String title, int width, int height) {
        GraphicReportFrame internalFrame = new GraphicReportFrame(title, panel);
        internalFrame.setSize(width, height);
        addReport(internalFrame);
        return internalFrame;
    }

    /**
     * Creates a new InternalFrame displaying a Report in HTML.
     */
    public void addReport(ReportTool tool) {
        // create a new JInternalFrame
        JInternalFrame internalFrame = new HTMLReportFrame(getViewOptions(), tool);
        internalFrame.setSize(getDtpRoot().getWidth(), getDtpRoot().getHeight());

        addReport(internalFrame);
    }

    /**
     * Shows a file dialog and opens a drawing.
     */
    public void changeObjects(Object source) {
        if (saveCurrentChanges()) {
            FileChooser openDialog = new FileChooser(getSettings().getWorkingDirectory());
            openDialog.setDialogTitle(CommonUserAccess.getTitleFileOpen());
            openDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());

            if (openDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                getSettings().setWorkingDirectory(openDialog.getCurrentDirectory().getAbsolutePath());
                openFile(openDialog.getSelectedFile().getAbsolutePath());
            }
        }
    }

    /**
     * If language or codes changes force refresh in sub GUI's.
     */
    private void closeSubWindows() throws Exception {
        super.dispose();
        showInstance(getViewOptions(), getUtility(), getSettings());
    }

    /**
     * connEtoC1: (MniNew.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniNew()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniFileNewDefault());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC10: (TlbStandard.toolBar.tbbOpenAction_actionPerformed(java.util. EventObject) --> LauncherView.mniOpenFile()V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC10(java.util.EventObject arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getTlbStandard());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC11: (TlbStandard.toolBar.tbbNewAction_actionPerformed(java.util.EventObject) --> LauncherView.mniNew()V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC11(java.util.EventObject arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getTlbStandard());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC12: (JMenuItem1.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniTotalCost()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC12(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniReportTotalCost();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC13: (RbtChinese.item.itemStateChanged(java.awt.event.ItemEvent) --> LauncherView.setLanguage()V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC13(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setLanguage();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC14: (JMenuItem41.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniSystem()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC14(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniSystem();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC15: (MniReportComplete.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniReportComplete()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC15(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniReportComplete();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC16: (MniFileImportCostCentre.action.actionPerformed(java.awt.event .ActionEvent) --> LauncherView.mniFileImportCostCentre()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC16(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniFileImportCostCentre();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC17: (MniFileImportCostCause.action.actionPerformed(java.awt.event. ActionEvent) --> LauncherView.mniFileImportCostCause()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC17(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniFileImportCostCause();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC18: (MniHelp.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniHelp()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC18(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniHelp();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC19: (RbtLanguageGerman.item.itemStateChanged(java.awt.event.ItemEvent) --> LauncherView.setLanguage()V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC19(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setLanguage();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (PnlDocumentation.simpleEditorPanel.txaEditorKey_keyReleased(java .util.EventObject) --> LauncherView.saveDocumentation()V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.util.EventObject arg1) {
        try {
            // user code begin {1}
            // user code end
            this.saveDocumentation();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC20: (RbtLanguageFrench.item.itemStateChanged(java.awt.event.ItemEvent) --> LauncherView.setLanguage()V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC20(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setLanguage();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC21: (RbtLanguageItalian.item.itemStateChanged(java.awt.event.ItemEvent) --> LauncherView.setLanguage()V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC21(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setLanguage();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC22: (RbtLanguageEnglish.item.itemStateChanged(java.awt.event.ItemEvent) --> LauncherView.setLanguage()V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC22(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setLanguage();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC23: (RbtRussian.item.itemStateChanged(java.awt.event.ItemEvent) --> LauncherView.setLanguage()V)
     *
     * @param arg1 java.awt.event.ItemEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC23(java.awt.event.ItemEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setLanguage();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC24: (MniPersonalCost.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniPersonalCost()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC24(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniReportPersonalCost();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC26: (MniDependencyGraph.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniDependencyGraph()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC26(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniDependencyGraph();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC27: (MniTcoChart.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniTcoChart()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC27(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniTcoChart();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC29: (RbtFontDefault.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.setFont()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC29(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setFont();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (MniAbout.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniAbout()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniAbout();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC30: (RbtFontBig.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.setFont()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC30(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.setFont();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC4: (MniCodes.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniCodes()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniCodes();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5: (MniFileSave.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.executeSaveObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.saveObject();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC6: (MniExit.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.dispose()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
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
     * connEtoC7: (MniFileSaveAs.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniSaveAs()Z)
     *
     * @param arg1 java.awt.event.ActionEvent
     * @return boolean
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private boolean connEtoC7(java.awt.event.ActionEvent arg1) {
        boolean connEtoC7Result = false;
        try {
            // user code begin {1}
            // user code end
            connEtoC7Result = this.mniSaveAs();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
        return connEtoC7Result;
    }

    /**
     * connEtoC8: (MniFileOpenWindow.action.actionPerformed(java.awt.event.ActionEvent) --> LauncherView.mniOpenFile()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getMniFileOpenWindow());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC9: (TlbStandard.toolBar.tbbSaveAction_actionPerformed(java.util. EventObject) --> LauncherView.executeSaveObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC9(java.util.EventObject arg1) {
        try {
            // user code begin {1}
            // user code end
            this.saveObject();
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
        try {
            if (saveCurrentChanges()) {
                saveSettings();
                if ((getUtility() != null) && (getUtility().getServer() != null)) {
                    disposeApplication(getUtility().getServer());
                }
            }
        } catch (Exception e) {
            super.dispose();
        }
    }

    /**
     * Find a given Menu in MenuBar.
     * @param name
     * @return JMenu
     * @deprecated (public for ApplicationPlugin only)
     */
    public JMenu findMenu(final String name) {
        JMenuBar menuBar = getLauncherViewJMenuBar();
        int mc = menuBar.getMenuCount();
        for (int i = 0; i < mc; i++) {
            JMenu m = menuBar.getMenu(i);
            if (m.getName().equals(name)) {
                return m;
            } else {
                // TODO NYI: go down recursively
            }
        }
        // TODO HACK
        if (name.equals("MnuReportTco")) {
            return getMnuReportTco();
        } else if (name.equals("MnuReportFinance")) {
            return getMnuReportFinance();
        }
        return null;
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
                ivjBaseFrameContentPane.setLayout(new java.awt.BorderLayout());
                getBaseFrameContentPane().add(getStbStatus(), "South");
                getBaseFrameContentPane().add(getPnlMain(), "Center");
                getBaseFrameContentPane().add(getTlbStandard(), "North");
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
     * Return the JDesktopPane1 property value.
     *
     * @return javax.swing.JDesktopPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JDesktopPane getDtpRoot() {
        if (ivjDtpRoot == null) {
            try {
                ivjDtpRoot = new javax.swing.JDesktopPane();
                ivjDtpRoot.setName("DtpRoot");
                // user code begin {1}
                setBackgroundImage(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Tool.jpg").getImage());
                ivjDtpRoot = new javax.swing.JDesktopPane() {
                    @Override
                    public void paintComponent(java.awt.Graphics g) {
                        super.paintComponent(g);
                        int w = getWidth();
                        int h = getHeight();
                        int x = (w - image.getWidth(this)) / 2; // new
                        // Double(g.getClipBounds().getCenterX()
                        // -
                        // (image.getWidth(this)
                        // /
                        // 2)).intValue()
                        int y = (h - image.getHeight(this)) / 2; // new
                        // Double(g.getClipBounds().getCenterY()
                        // -
                        // (image.getHeight(this)
                        // /
                        // 2)).intValue()

                        g.drawImage(image, x, y, this);
                    }
                };
                ivjDtpRoot.setName("DtpRoot");
                ivjDtpRoot.setBackground(Color.white);
                /*
                 * BasePanel root = new BasePanel(); root.setImage(Toolkit.getDefaultToolkit ().createImage("org/tcotool/application/resources/TCO_Tool.jpg" ));
                 */
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjDtpRoot;
    }

    /**
     * Singleton pattern.
     */
    public static LauncherView getInstance() {
        return instance;
    }

    /**
     * Return the JSeparator1 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator1() {
        if (ivjJSeparator1 == null) {
            try {
                ivjJSeparator1 = new javax.swing.JSeparator();
                ivjJSeparator1.setName("JSeparator1");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSeparator1;
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
                ivjLauncherViewJMenuBar.add(getMnuReports());
                ivjLauncherViewJMenuBar.add(getMnuSettings());
                ivjLauncherViewJMenuBar.add(getMnuExtras());
                ivjLauncherViewJMenuBar.add(getMnuHelp());
                // user code begin {1}
                ivjLauncherViewJMenuBar.add(getMnuEdit(), 1);
                ivjLauncherViewJMenuBar.add(getMnuWindow(), 6);
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
     * Return the MniAbout property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniAbout() {
        if (ivjMniAbout == null) {
            try {
                ivjMniAbout = new javax.swing.JMenuItem();
                ivjMniAbout.setName("MniAbout");
                ivjMniAbout.setText("Info...");
                ivjMniAbout.setActionCommand("MniAbout");
                // user code begin {1}
                ivjMniAbout.setText(CommonUserAccess.getMniHelpAboutText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniAbout;
    }

    /**
     * Return the MniCodes property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniCodes() {
        if (ivjMniCodes == null) {
            try {
                ivjMniCodes = new javax.swing.JMenuItem();
                ivjMniCodes.setName("MniCodes");
                ivjMniCodes.setText("Codes ändern...");
                // user code begin {1}
                ivjMniCodes.setText(getResourceString("MinCodes_text") + "...");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniCodes;
    }

    /**
     * Return the MniDependencyGraph property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniDependencyGraph() {
        if (ivjMniDependencyGraph == null) {
            try {
                ivjMniDependencyGraph = new javax.swing.JMenuItem();
                ivjMniDependencyGraph.setName("MniDependencyGraph");
                ivjMniDependencyGraph.setText("Abhängigkeits-Graph");
                // user code begin {1}
                ivjMniDependencyGraph.setText(getResourceString("MniDependencyGraph_text") + " (--[%]-->)");
                ivjMniDependencyGraph.setToolTipText(ResourceManager.getResourceAsNonLabeled(DependencyView.class, "MniDependencyOverall_toolTipText"));
                ivjMniDependencyGraph.setIcon(ResourceBundle.getImageIcon(ModelUtility.class, "Dependency.png"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniDependencyGraph;
    }

    /**
     * This menu shows the DependencyGraph for each TCO year.
     *
     * @return
     * @see #getMniDependencyGraph() for Overall calculation
     */
    private javax.swing.JMenuItem getMniDependencyGraphTCO() {
        if (mniDependencyGraphTCO == null) {
            try {
                mniDependencyGraphTCO = new javax.swing.JMenuItem();
                mniDependencyGraphTCO.setName("MniDependencyGraphTCO");
                mniDependencyGraphTCO.setText(getResourceString("MniDependencyGraph_text") + " (--[%]-->) : "
                    + ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblUsageDuration_text"));
                mniDependencyGraphTCO.setToolTipText(ResourceManager.getResourceAsNonLabeled(DependencyView.class, "MniDependencyTCO_toolTipText"));
                mniDependencyGraphTCO.setIcon(ResourceBundle.getImageIcon(ModelUtility.class, "Dependency.png"));
                mniDependencyGraphTCO.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        mniDependencyGraphTCO(Boolean.FALSE);
                    }
                });
            } catch (Exception ivjExc) {
                handleException(ivjExc);
            }
        }
        return mniDependencyGraphTCO;
    }

    /**
     * Return the MniExit property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFileExit() {
        if (ivjMniFileExit == null) {
            try {
                ivjMniFileExit = new javax.swing.JMenuItem();
                ivjMniFileExit.setName("MniFileExit");
                ivjMniFileExit.setText("Beenden");
                // user code begin {1}
                ivjMniFileExit.setText(CommonUserAccess.getMniFileExit());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileExit;
    }

    /**
     * Return the MniFileImportCostCause property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFileImportCostCause() {
        if (ivjMniFileImportCostCause == null) {
            try {
                ivjMniFileImportCostCause = new javax.swing.JMenuItem();
                ivjMniFileImportCostCause.setName("MniFileImportCostCause");
                ivjMniFileImportCostCause.setText("Cost type...");
                // user code begin {1}
                ivjMniFileImportCostCause.setText(ModelUtility.getTypeString(CostCause.class) + "...");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileImportCostCause;
    }

    /**
     * Return the MniFileImportCostCentre property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFileImportCostCentre() {
        if (ivjMniFileImportCostCentre == null) {
            try {
                ivjMniFileImportCostCentre = new javax.swing.JMenuItem();
                ivjMniFileImportCostCentre.setName("MniFileImportCostCentre");
                ivjMniFileImportCostCentre.setText("Cost centre...");
                // user code begin {1}
                ivjMniFileImportCostCentre.setText(ModelUtility.getTypeString(CostCentre.class) + "...");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileImportCostCentre;
    }

    /**
     * Return the MnuNew property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMniFileNew() {
        if (ivjMniFileNew == null) {
            try {
                ivjMniFileNew = new javax.swing.JMenu();
                ivjMniFileNew.setName("MniFileNew");
                ivjMniFileNew.setText("Neu");
                ivjMniFileNew.add(getMniFileNewDefault());
                ivjMniFileNew.add(getMniNewItil());
                // user code begin {1}
                ivjMniFileNew.setText(CommonUserAccess.getMniFileNewText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileNew;
    }

    /**
     * Return the MniNew property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFileNewDefault() {
        if (ivjMniFileNewDefault == null) {
            try {
                ivjMniFileNewDefault = new javax.swing.JMenuItem();
                ivjMniFileNewDefault.setName("MniFileNewDefault");
                ivjMniFileNewDefault.setText("Default-Konfiguration");
                // user code begin {1}
                ivjMniFileNewDefault.setText(getResourceString("MniFileNewDefault_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileNewDefault;
    }

    /**
     * Return the MniFileOpenWindow property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFileOpenWindow() {
        if (ivjMniFileOpenWindow == null) {
            try {
                ivjMniFileOpenWindow = new javax.swing.JMenuItem();
                ivjMniFileOpenWindow.setName("MniFileOpenWindow");
                ivjMniFileOpenWindow.setText("Öffnen...");
                // user code begin {1}
                ivjMniFileOpenWindow.setText(CommonUserAccess.getMniFileOpenWindowText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileOpenWindow;
    }

    /**
     * Return the MniSave property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFileSave() {
        if (ivjMniFileSave == null) {
            try {
                ivjMniFileSave = new javax.swing.JMenuItem();
                ivjMniFileSave.setName("MniFileSave");
                ivjMniFileSave.setText("Speichern...");
                // user code begin {1}
                ivjMniFileSave.setText(CommonUserAccess.getMniFileSaveText());
                ivjMniFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileSave;
    }

    /**
     * Return the MniFileSaveAs property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFileSaveAs() {
        if (ivjMniFileSaveAs == null) {
            try {
                ivjMniFileSaveAs = new javax.swing.JMenuItem();
                ivjMniFileSaveAs.setName("MniFileSaveAs");
                ivjMniFileSaveAs.setText("Speichern unter...");
                // user code begin {1}
                ivjMniFileSaveAs.setText(CommonUserAccess.getMniFileSaveAsText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFileSaveAs;
    }

    private javax.swing.JMenuItem getMniFind() {
        if (mniFind == null) {
            try {
                mniFind = new javax.swing.JMenuItem();
                mniFind.setName("MniFind");
                mniFind.setText(CommonUserAccess.getMniEditFindText());
                mniFind.setIcon(CommonUserAccess.getIconFind());
                mniFind.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        mniFind();
                    }
                });
            } catch (Exception ivjExc) {
                handleException(ivjExc);
            }
        }
        return mniFind;
    }

    private javax.swing.JMenuItem getMniWindowTile() {
        if (mniWindowTile == null) {
            try {
                mniWindowTile = new javax.swing.JMenuItem();
                mniWindowTile.setName("MniWindowTile");
                mniWindowTile.setText(CommonUserAccess.getMniWindowTileText());
                // mniFind.setIcon(CommonUserAccess.getIconFind());
                mniWindowTile.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        mniTile();
                    }
                });
            } catch (Exception ivjExc) {
                handleException(ivjExc);
            }
        }
        return mniWindowTile;
    }

    /**
     * Return the MniHelp property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniHelp() {
        if (ivjMniHelp == null) {
            try {
                ivjMniHelp = new javax.swing.JMenuItem();
                ivjMniHelp.setName("MniHelp");
                ivjMniHelp.setText("Hilfe...");
                // user code begin {1}
                ivjMniHelp.setText(CommonUserAccess.getMniHelpText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniHelp;
    }

    /**
     * Return the MniNewItil property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniNewItil() {
        if (ivjMniNewItil == null) {
            try {
                ivjMniNewItil = new javax.swing.JMenuItem();
                ivjMniNewItil.setName("MniNewItil");
                ivjMniNewItil.setText("ITIL-Konfiguration");
                ivjMniNewItil.setEnabled(false);
                // user code begin {1}
                // TODO NLS
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniNewItil;
    }

    /**
     * Return the MniReportComplete property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniReportComplete() {
        if (ivjMniReportComplete == null) {
            try {
                ivjMniReportComplete = new javax.swing.JMenuItem();
                ivjMniReportComplete.setName("MniReportComplete");
                ivjMniReportComplete.setText("Bericht (komplett)");
                // user code begin {1}
                ivjMniReportComplete.setText(getResourceString("MniReportComplete_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniReportComplete;
    }

    /**
     * Return the MniPersonalCost property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniReportPersonalCost() {
        if (ivjMniReportPersonalCost == null) {
            try {
                ivjMniReportPersonalCost = new javax.swing.JMenuItem();
                ivjMniReportPersonalCost.setName("MniReportPersonalCost");
                ivjMniReportPersonalCost.setText("Personalkosten");
                // user code begin {1}
                ivjMniReportPersonalCost.setText(ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text"));
                ivjMniReportPersonalCost.setIcon(ResourceBundle.getImageIcon(ModelUtility.class, "PersonalCost.png")); // getUtility().getIcon(PersonalCost.class,
                // false)
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniReportPersonalCost;
    }

    /**
     * Return the MniReportTotalTcoCost property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniReportTotalTcoCost() {
        if (ivjMniReportTotalTcoCost == null) {
            try {
                ivjMniReportTotalTcoCost = new javax.swing.JMenuItem();
                ivjMniReportTotalTcoCost.setName("MniReportTotalTcoCost");
                ivjMniReportTotalTcoCost.setText("Gesamtkosten TCO (Zusammenfassung)");
                // user code begin {1}
                ivjMniReportTotalTcoCost.setText(getResourceString("MniReportTotalTcoCost_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniReportTotalTcoCost;
    }

    /**
     * Return the JMenuItem41 property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniSystemParameter() {
        if (ivjMniSystemParameter == null) {
            try {
                ivjMniSystemParameter = new javax.swing.JMenuItem();
                ivjMniSystemParameter.setName("MniSystemParameter");
                ivjMniSystemParameter.setText("System-Parameter...");
                // user code begin {1}
                ivjMniSystemParameter.setText(ResourceManager.getResource(SystemParameterDetailView.class, "FrmWindow_text") + "...");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniSystemParameter;
    }

    /**
     * Return the MniTcoChart property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniTcoChart() {
        if (ivjMniTcoChart == null) {
            try {
                ivjMniTcoChart = new javax.swing.JMenuItem();
                ivjMniTcoChart.setName("MniTcoChart");
                ivjMniTcoChart.setText("Gesamtkosten TCO (Balkendiagramm)");
                // user code begin {1}
                ivjMniTcoChart.setText(getResourceString("MniTcoChart_text"));
                ivjMniTcoChart.setIcon(ResourceBundle.getImageIcon(LauncherView.class, "bar_chart.png"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniTcoChart;
    }

    /**
     * Additional Edit Window
     *
     * @return
     */
    private javax.swing.JMenu getMnuEdit() {
        if (mnuEdit == null) {
            try {
                mnuEdit = new javax.swing.JMenu();
                mnuEdit.setName("MnuEdit");
                mnuEdit.setText(CommonUserAccess.getMnuEditText());
                mnuEdit.add(getMniFind());
            } catch (Exception ivjExc) {
                handleException(ivjExc);
            }
        }
        return mnuEdit;
    }

    private javax.swing.JMenu getMnuWindow() {
        if (mnuWindow == null) {
            try {
                mnuWindow = new javax.swing.JMenu();
                mnuWindow.setName("MnuWindow");
                mnuWindow.setText(CommonUserAccess.getMnuWindowText());
                mnuWindow.add(getMniWindowTile());
            } catch (Exception ivjExc) {
                handleException(ivjExc);
            }
        }
        return mnuWindow;
    }

    /**
     * Return the MnuExtras property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public javax.swing.JMenu getMnuExtras() {
        if (ivjMnuExtras == null) {
            try {
                ivjMnuExtras = new javax.swing.JMenu();
                ivjMnuExtras.setName("MnuExtras");
                ivjMnuExtras.setText("Extras");
                ivjMnuExtras.add(getMnuLanguage());
                // user code begin {1}
                ivjMnuExtras.setText(CommonUserAccess.getMnuExtrasText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuExtras;
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
                ivjMnuFile.add(getMniFileNew());
                ivjMnuFile.add(getMniFileOpenWindow());
                ivjMnuFile.add(getMnuFileImport());
                ivjMnuFile.add(getMniFileSave());
                ivjMnuFile.add(getMniFileSaveAs());
                ivjMnuFile.add(getJSeparator1());
                ivjMnuFile.add(getMniFileExit());
                // user code begin {1}
                ivjMnuFile.setText(CommonUserAccess.getMnuFileText());
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
     * Return the MnuFileImport property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuFileImport() {
        if (ivjMnuFileImport == null) {
            try {
                ivjMnuFileImport = new javax.swing.JMenu();
                ivjMnuFileImport.setName("MnuFileImport");
                ivjMnuFileImport.setText("Import");
                ivjMnuFileImport.add(getMniFileImportCostCentre());
                ivjMnuFileImport.add(getMniFileImportCostCause());
                // user code begin {1}
                ivjMnuFileImport.setText(getResourceString("MnuImport_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuFileImport;
    }

    /**
     * Return the MnuFont property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuFont() {
        if (ivjMnuFont == null) {
            try {
                ivjMnuFont = new javax.swing.JMenu();
                ivjMnuFont.setName("MnuFont");
                ivjMnuFont.setText("Schrift");
                ivjMnuFont.add(getRbtFontDefault());
                ivjMnuFont.add(getRbtFontBig());
                // user code begin {1}
                // TODO move MnuFont to CommonUserAccess
                ivjMnuFont.setText(getResourceString("MnuFont_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuFont;
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
                ivjMnuHelp.setText("?");
                ivjMnuHelp.setActionCommand("MnuHelp");
                ivjMnuHelp.add(getMniHelp());
                ivjMnuHelp.add(getMniAbout());
                // user code begin {1}
                ivjMnuHelp.setText(CommonUserAccess.getMnuHelpText());

                ivjMnuHelp.add(new JSeparator());
                JMenuItem item = new JMenuItem("Platform Info...");
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        (PlatformInfoPanel.createDialog(getInstance())).show();
                    }
                });
                ivjMnuHelp.add(item);
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
     * Return the JMenu2 property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuLanguage() {
        if (ivjMnuLanguage == null) {
            try {
                ivjMnuLanguage = new javax.swing.JMenu();
                ivjMnuLanguage.setName("MnuLanguage");
                ivjMnuLanguage.setText("Sprache");
                ivjMnuLanguage.add(getRbtLanguageGerman());
                ivjMnuLanguage.add(getRbtLanguageEnglish());
                ivjMnuLanguage.add(getRbtLanguageFrench());
                ivjMnuLanguage.add(getRbtLanguageItalian());
                ivjMnuLanguage.add(getRbtChinese());
                ivjMnuLanguage.add(getRbtRussian());
                // user code begin {1}
                ivjMnuLanguage.setText(CommonUserAccess.getMnuLanguageText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuLanguage;
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
                ivjMnuLookAndFeel.setText(CommonUserAccess.getMnuViewLookAndFeelText());
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
     * Return the MnuReportFinance property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuReportFinance() {
        if (ivjMnuReportFinance == null) {
            try {
                ivjMnuReportFinance = new javax.swing.JMenu();
                ivjMnuReportFinance.setName("MnuReportFinance");
                ivjMnuReportFinance.setText("Finanzen");
                ivjMnuReportFinance.add(getMniReportPersonalCost());
                // user code begin {1}
                ivjMnuReportFinance.setText(getResourceString("MnuReportFinance_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuReportFinance;
    }

    /**
     * Return the MnuReportGeneral property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuReportGeneral() {
        if (ivjMnuReportGeneral == null) {
            try {
                ivjMnuReportGeneral = new javax.swing.JMenu();
                ivjMnuReportGeneral.setName("MnuReportGeneral");
                ivjMnuReportGeneral.setText("Konfiguration");
                ivjMnuReportGeneral.add(getMniReportComplete());
                ivjMnuReportGeneral.add(getMniDependencyGraph());
                // user code begin {1}
                ivjMnuReportGeneral.setText(getResourceString("MnuReportGeneral_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuReportGeneral;
    }

    /**
     * Return the MnuReports property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuReports() {
        if (ivjMnuReports == null) {
            try {
                ivjMnuReports = new javax.swing.JMenu();
                ivjMnuReports.setName("MnuReports");
                ivjMnuReports.setText("Berechnung");
                ivjMnuReports.add(getMnuReportGeneral());
                ivjMnuReports.add(getMnuReportTco());
                ivjMnuReports.add(getMnuReportFinance());
                // user code begin {1}
                ivjMnuReports.setText(CommonUserAccess.getMnuReportsText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuReports;
    }

    /**
     * Return the MnuReportTco property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuReportTco() {
        if (ivjMnuReportTco == null) {
            try {
                ivjMnuReportTco = new javax.swing.JMenu();
                ivjMnuReportTco.setName("MnuReportTco");
                ivjMnuReportTco.setToolTipText("Total Cost of Ownership");
                ivjMnuReportTco.setText("TCO");
                ivjMnuReportTco.add(getMniReportTotalTcoCost());
                ivjMnuReportTco.add(getMniTcoChart());
                // user code begin {1}
                ivjMnuReportTco.setText(getResourceString("MnuReportTco_text"));
                ivjMnuReportTco.setToolTipText(getResourceString("MnuReportTco_toolTipText"));

                ivjMnuReportTco.insert(getMniDependencyGraphTCO(), 1);

                JMenu codeCharts = new JMenu();
                codeCharts.setText(getResourceString("MinCodes_text"));
                codeCharts.setIcon(ResourceBundle.getImageIcon(LauncherView.class, "bar_chart.png"));
                ivjMnuReportTco.add(codeCharts);
                // Service charts
                addMenuChartReports(codeCharts, ServiceCategory.class);
                addMenuChartReports(codeCharts, Responsibility.class);
                addMenuChartReports(codeCharts, CostCentre.class);
                codeCharts.add(new JSeparator());
                // CostDriver charts
                addMenuChartReports(codeCharts, Process.class);
                // TODO unwanted for CAB
                // addMenuChartReports(codeCharts, ProjectPhase.class);
                // addMenuChartReports(codeCharts, LifeCycle.class);
                addMenuChartReports(codeCharts, Site.class);
                codeCharts.add(new JSeparator());
                // *Cost charts
                addMenuChartReports(codeCharts, CostCause.class);
                addMenuChartReports(codeCharts, Activity.class);
                addMenuChartReports(codeCharts, Role.class);

                // Subordinate-Codes within Service-codes
                codeCharts = new JMenu();
                codeCharts.setText(NlsUtils.formatMessage(getResourceString("MniSubordinateCodes_text"), getResourceString("MinCodes_text")));
                codeCharts.setIcon(ResourceBundle.getImageIcon(LauncherView.class, "bar_chart.png"));
                ivjMnuReportTco.add(codeCharts);
                addMenuChartReports(codeCharts, CostCause.class, Responsibility.class);
                codeCharts.add(new JSeparator());
                addMenuChartReports(codeCharts, CostCause.class, ServiceCategory.class);
                codeCharts.add(new JSeparator());
                addMenuChartReports(codeCharts, CostCause.class, CostCentre.class);
                codeCharts.add(new JSeparator());
                addMenuChartReports(codeCharts, Process.class, ServiceCategory.class);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuReportTco;
    }

    /**
     * Return the JMenu1 property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuSettings() {
        if (ivjMnuSettings == null) {
            try {
                ivjMnuSettings = new javax.swing.JMenu();
                ivjMnuSettings.setName("MnuSettings");
                ivjMnuSettings.setText("Steuerung");
                ivjMnuSettings.add(getMniCodes());
                ivjMnuSettings.add(getMniSystemParameter());
                // user code begin {1}
                ivjMnuSettings.setText(getResourceString("MnuSettings_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuSettings;
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
                ivjMnuView.add(getMnuLookAndFeel());
                ivjMnuView.add(getMnuFont());
                // user code begin {1}
                ivjMnuView.setText(CommonUserAccess.getMnuViewText());
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
     * Return the PnlDocumentation property value.
     *
     * @return ch.softenvironment.view.SimpleEditorPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.SimpleEditorPanel getPnlDocumentation() {
        if (ivjPnlDocumentation == null) {
            try {
                ivjPnlDocumentation = new ch.softenvironment.view.SimpleEditorPanel();
                ivjPnlDocumentation.setName("PnlDocumentation");
                ivjPnlDocumentation.setToolTipText("Notiz zum aktuell selektieren Element");
                // user code begin {1}
                ivjPnlDocumentation.setToolTipText(getResourceString("PnlDocumentation_toolTipText"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlDocumentation;
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
                ivjPnlMain.setLayout(new java.awt.BorderLayout());
                getPnlMain().add(getSppMain(), "Center");
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
     * Return the NavigationView1 property value.
     *
     * @return org.tcotool.application.NavigationView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public NavigationView getPnlNavigation() {
        if (ivjPnlNavigation == null) {
            try {
                ivjPnlNavigation = new org.tcotool.application.NavigationView();
                ivjPnlNavigation.setName("PnlNavigation");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlNavigation;
    }

    /**
     * Return the RbtChinese property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtChinese() {
        if (ivjRbtChinese == null) {
            try {
                ivjRbtChinese = new javax.swing.JRadioButtonMenuItem();
                ivjRbtChinese.setName("RbtChinese");
                ivjRbtChinese.setText("Chinese");
                // user code begin {1}
                ivjRbtChinese.setText(ResourceManager.getResource(CommonUserAccess.class, "MniLanguageChinese_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtChinese;
    }

    /**
     * Return the RbtFontBig property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtFontBig() {
        if (ivjRbtFontBig == null) {
            try {
                ivjRbtFontBig = new javax.swing.JRadioButtonMenuItem();
                ivjRbtFontBig.setName("RbtFontBig");
                ivjRbtFontBig.setText("Gross");
                // user code begin {1}
                ivjRbtFontBig.setText(getResourceString("RbtFontBig_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtFontBig;
    }

    /**
     * Return the RbtFontDefault property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtFontDefault() {
        if (ivjRbtFontDefault == null) {
            try {
                ivjRbtFontDefault = new javax.swing.JRadioButtonMenuItem();
                ivjRbtFontDefault.setName("RbtFontDefault");
                ivjRbtFontDefault.setText("Default");
                // user code begin {1}
                ivjRbtFontDefault.setText(getResourceString("RbtFontDefault_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtFontDefault;
    }

    /**
     * Return the RbtLanguageEnglish property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtLanguageEnglish() {
        if (ivjRbtLanguageEnglish == null) {
            try {
                ivjRbtLanguageEnglish = new javax.swing.JRadioButtonMenuItem();
                ivjRbtLanguageEnglish.setName("RbtLanguageEnglish");
                ivjRbtLanguageEnglish.setSelected(false);
                ivjRbtLanguageEnglish.setText("English");
                // user code begin {1}
                ivjRbtLanguageEnglish.setText(ResourceManager.getResource(CommonUserAccess.class, "MniLanguageEnglish_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtLanguageEnglish;
    }

    /**
     * Return the RbtLanguageFrench property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtLanguageFrench() {
        if (ivjRbtLanguageFrench == null) {
            try {
                ivjRbtLanguageFrench = new javax.swing.JRadioButtonMenuItem();
                ivjRbtLanguageFrench.setName("RbtLanguageFrench");
                ivjRbtLanguageFrench.setText("Francais");
                // user code begin {1}
                ivjRbtLanguageFrench.setText(ResourceManager.getResource(CommonUserAccess.class, "MniLanguageFrench_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtLanguageFrench;
    }

    /**
     * Return the RbtLanguageGerman property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtLanguageGerman() {
        if (ivjRbtLanguageGerman == null) {
            try {
                ivjRbtLanguageGerman = new javax.swing.JRadioButtonMenuItem();
                ivjRbtLanguageGerman.setName("RbtLanguageGerman");
                ivjRbtLanguageGerman.setSelected(false);
                ivjRbtLanguageGerman.setText("Deutsch");
                // user code begin {1}
                ivjRbtLanguageGerman.setText(ResourceManager.getResource(CommonUserAccess.class, "MniLanguageGerman_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtLanguageGerman;
    }

    /**
     * Return the RbtLanguageItalian property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtLanguageItalian() {
        if (ivjRbtLanguageItalian == null) {
            try {
                ivjRbtLanguageItalian = new javax.swing.JRadioButtonMenuItem();
                ivjRbtLanguageItalian.setName("RbtLanguageItalian");
                ivjRbtLanguageItalian.setText("Italiano");
                // user code begin {1}
                ivjRbtLanguageItalian.setText(ResourceManager.getResource(CommonUserAccess.class, "MniLanguageItaliano_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtLanguageItalian;
    }

    /**
     * Return the RbtRussian property value.
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButtonMenuItem getRbtRussian() {
        if (ivjRbtRussian == null) {
            try {
                ivjRbtRussian = new javax.swing.JRadioButtonMenuItem();
                ivjRbtRussian.setName("RbtRussian");
                ivjRbtRussian.setText("Russian");
                // user code begin {1}
                ivjRbtRussian.setText(ResourceManager.getResource(CommonUserAccess.class, "MniLanguageRussian_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtRussian;
    }

    /**
     * Return UserSettings. Design Pattern: Singleton
     */
    public ApplicationOptions getSettings() {
        return settings;
    }

    /**
     * Return the SpiLeft property value.
     *
     * @return javax.swing.JSplitPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSplitPane getSppLeft() {
        if (ivjSppLeft == null) {
            try {
                ivjSppLeft = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
                ivjSppLeft.setName("SppLeft");
                getSppLeft().add(getPnlNavigation(), "top");
                getSppLeft().add(getPnlDocumentation(), "bottom");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjSppLeft;
    }

    /**
     * Return the SpiMain property value.
     *
     * @return javax.swing.JSplitPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSplitPane getSppMain() {
        if (ivjSppMain == null) {
            try {
                ivjSppMain = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
                ivjSppMain.setName("SppMain");
                getSppMain().add(getSppLeft(), "left");
                getSppMain().add(getDtpRoot(), "right");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjSppMain;
    }

    /**
     * Return the StbStatus property value.
     *
     * @return ch.softenvironment.view.StatusBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.StatusBar getStbStatus() {
        if (ivjStbStatus == null) {
            try {
                ivjStbStatus = new ch.softenvironment.view.StatusBar();
                ivjStbStatus.setName("StbStatus");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjStbStatus;
    }

    /**
     * Return the TlbStandard property value.
     *
     * @return ch.softenvironment.view.ToolBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.ToolBar getTlbStandard() {
        if (ivjTlbStandard == null) {
            try {
                ivjTlbStandard = new ch.softenvironment.view.ToolBar();
                ivjTlbStandard.setName("TlbStandard");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTlbStandard;
    }

    /**
     * A Launcher points to one Utility containing the root model.
     */
    public ModelUtility getUtility() {
        return utility;
    }

    /**
     * Return Version.
     */
    public static String getVersion() {
        return "V1.5.2";
    }

    /**
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    @Override
    public void handleException(java.lang.Throwable exception) {
        super.handleException(exception);
    }

    /**
     * Import given DbCode type into as BusinessObjects.
     *
     * @param type
     * @param csvFormat
     */
    private void importCode(Class<? extends DbCodeType> type, final String csvFormat) {
        try {
            // TODO NLS
            if (BaseDialog.showConfirm(this, "Import Code", ModelUtility.getTypeString(type) + "\n\nFormat: \"" + csvFormat
                + "\"\n\n[wobei die Kopfzeile ignoriert wird und eindeutige Nummern überschrieben werden!]")) {
                FileChooser openDialog = new FileChooser(LauncherView.getInstance().getSettings().getWorkingDirectory());
                openDialog.setDialogTitle(CommonUserAccess.getTitleFileOpen());
                openDialog.addChoosableFileFilter(GenericFileFilter.createCsvFilter());
                if (openDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    java.util.Locale locale = ModelUtility.getCodeTypeLocale();
                    XmlObjectServer server = (XmlObjectServer) utility.getServer();
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(openDialog.getSelectedFile()));
                    Iterator<String> it = ParserCSV.readFile(bis, CsvSerializer.CELL_SEPARATOR).iterator();
                    bis.close();
                    if (it.hasNext()) {
                        // skip header
                        it.next();
                    }
                    while (it.hasNext()) {
                        String line = it.next();
                        ParserCSV parser = new ParserCSV(line, CsvSerializer.CELL_SEPARATOR);
                        String iliCode = parser.getNextString();
                        String name = parser.getNextString();
                        Boolean direct = Boolean.TRUE;
                        if (type == CostCause.class) {
                            Boolean tmp = parser.getNextBoolean();
                            if (tmp != null) {
                                direct = tmp;
                            }
                        }
                        /*
                         * String note = ""; String tmp = null; while (!StringUtils.isNullOrEmpty(tmp = parser.getNextString())) { if (note.length() > 0) { note
                         * = note + "; "; } note = note + tmp; }
                         */
                        if (!StringUtils.isNullOrEmpty(name)) {
                            Iterator<? extends DbCodeType> codes = utility.getServer().retrieveCodes(type).iterator();
                            boolean found = false;
                            while (codes.hasNext()) {
                                DbCodeType code = codes.next();
                                if ((code.getIliCode() != null) && code.getIliCode().equals(iliCode)) {
                                    // update text
                                    code.getName().setValue(name, locale);
                                    if (type == CostCause.class) {
                                        ((CostCause) code).setDirect(direct);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                // add new code
                                if (type == CostCause.class) {
                                    ModelUtility.createCostCause(server, locale, iliCode, name, direct);
                                } else {
                                    ModelUtility.createCostCenter(server, locale, iliCode, name);
                                }
                            }
                        }
                    }
                    closeSubWindows();
                    LauncherView.getInstance().setModelChanged(true);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
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
        getMniAbout().addActionListener(ivjEventHandler);
        getMniHelp().addActionListener(ivjEventHandler);
        getMniFileExit().addActionListener(ivjEventHandler);
        getMniFileNewDefault().addActionListener(ivjEventHandler);
        getPnlDocumentation().addSimpleEditorPanelListener(ivjEventHandler);
        getMniFileSave().addActionListener(ivjEventHandler);
        getMniFileSaveAs().addActionListener(ivjEventHandler);
        getMniFileOpenWindow().addActionListener(ivjEventHandler);
        getTlbStandard().addToolBarListener(ivjEventHandler);
        getMniReportTotalTcoCost().addActionListener(ivjEventHandler);
        getMniSystemParameter().addActionListener(ivjEventHandler);
        getRbtLanguageGerman().addItemListener(ivjEventHandler);
        getRbtLanguageFrench().addItemListener(ivjEventHandler);
        getRbtLanguageItalian().addItemListener(ivjEventHandler);
        getRbtLanguageEnglish().addItemListener(ivjEventHandler);
        getMniCodes().addActionListener(ivjEventHandler);
        getMniReportComplete().addActionListener(ivjEventHandler);
        getMniReportPersonalCost().addActionListener(ivjEventHandler);
        getMniDependencyGraph().addActionListener(ivjEventHandler);
        getMniTcoChart().addActionListener(ivjEventHandler);
        getRbtFontDefault().addActionListener(ivjEventHandler);
        getRbtFontBig().addActionListener(ivjEventHandler);
        getRbtChinese().addItemListener(ivjEventHandler);
        getMniFileImportCostCentre().addActionListener(ivjEventHandler);
        getMniFileImportCostCause().addActionListener(ivjEventHandler);
        getRbtRussian().addItemListener(ivjEventHandler);
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
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            setJMenuBar(getLauncherViewJMenuBar());
            setSize(777, 505);
            setTitle("TCO-Tool");
            setContentPane(getBaseFrameContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setIconImage(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png").getImage());
        setTitle(getResourceString("FrmWindow_text"));
        refreshDocumentation();

        setSize(getSettings().getWindowWidth().intValue(), getSettings().getWindowHeight().intValue());
        getSppMain().setDividerLocation(280); // width of Tree
        getSppLeft().setDividerLocation(450); // height of Tree
        getTlbStandard().setTbbNewEnabled(true);
        getTlbStandard().setTbbChangeEnabled(true);
        getTlbStandard().setTbbFindEnabled(true);

        mnuFileHistory = new FileHistoryMenu(this, 9, getSettings().getLastFiles());
        getMnuFile().insert(mnuFileHistory, 2 /* second */);

        mniCurrency = new JMenuItem();
        mniCurrency.setText(ResourceManager.getResource(CourseDetailView.class, "FrmWindow_text") + "...");
        mniCurrency.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    if (!getViewOptions().getViewManager().activateView(CourseDetailView.class)) {
                        BaseFrame view = new CourseDetailView(getViewOptions(), ListUtils.createList(getUtility().getSystemParameter()));
                        getViewOptions().getViewManager().checkIn(null, view);
                        view.setRelativeLocation(getInstance());
                        view.setVisible(true);
                    }
                } catch (Throwable ex) {
                    handleException(ex);
                }
            }
        });
        getMnuSettings().add(mniCurrency);

        javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
        group.add(getRbtLanguageGerman());
        group.add(getRbtLanguageEnglish());
        group.add(getRbtLanguageFrench());
        group.add(getRbtLanguageItalian());
        group.add(getRbtChinese());
        group.add(getRbtRussian());
        if (java.util.Locale.getDefault().getLanguage().equals(java.util.Locale.GERMAN.getLanguage())) {
            getRbtLanguageGerman().setSelected(true);
        } else if (java.util.Locale.getDefault().getLanguage().equals(java.util.Locale.FRENCH.getLanguage())) {
            getRbtLanguageFrench().setSelected(true);
        } else if (java.util.Locale.getDefault().getLanguage().equals(java.util.Locale.ITALIAN.getLanguage())) {
            getRbtLanguageItalian().setSelected(true);
        } else if (java.util.Locale.getDefault().getLanguage().equals(java.util.Locale.CHINESE.getLanguage())) {
            getRbtChinese().setSelected(true);
        } else if (java.util.Locale.getDefault().getLanguage().equals("ru")) {
            getRbtRussian().setSelected(true);
        } else {
            // default
            getRbtLanguageEnglish().setSelected(true);
        }

        group = new javax.swing.ButtonGroup();
        group.add(getRbtFontDefault());
        group.add(getRbtFontBig());
        if (getSettings().getFont() == null) {
            getRbtFontDefault().setSelected(true);
        } else {
            getRbtFontBig().setSelected(true);
        }
        // user code end
    }

    /**
     * Initialize the persistence layer
     *
     * @param userId java.lang.String
     */
    private static DbObjectServer initializeDatabase(String userId, String password, String url) throws Exception {
        javax.jdo.PersistenceManagerFactory pmFactory = new ch.softenvironment.jomm.target.xml.XmlObjectServerFactory();
        pmFactory.setConnectionURL(url);
        pmFactory.setNontransactionalRead(true); // NO Commit at all
        pmFactory.setNontransactionalWrite(true);

        DbObjectServer server = (DbObjectServer) pmFactory.getPersistenceManager(userId, password);
        org.tcotool.TcotoolUtility.registerClasses(server);
        return server;
    }

    /**
     * Initialize View. Call this method in a View's standard initialize method to setup your stuff.
     *
     * @see #initialize() // user code begin {1}..user code end
     */
    @Override
    protected void initializeView() throws Exception {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dispose();
            }

        });

        /*
         * setIconImage(new ImageIcon(getClass() .getResource("app.gif")).getImage()); // 24*24pixel image
         */
        createLookAndFeelMenu(getMnuLookAndFeel());
        getPnlNavigation().setViewOptions(getViewOptions());

        JMenuItem item = new JMenuItem();
        item.setText(getResourceString("MniReportInvestment_text"));
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mniReportInvestment();
            }
        });
        getMnuReportFinance().add(item);
    }

    /**
     * main entrypoint - starts the part when it is run as an application
     *
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
        try {
            Tracer.start(args);

            ApplicationOptions options = new ApplicationOptions(System.getProperty("user.home") + java.io.File.separator + ".TCO_Tool");
            NlsUtils.changeLocale(new Locale(options.getLanguage()));
            /*
             * if (Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("ru")) { // @see #setLanguage()
             * ResourceManager.setCharacterSet(StringUtils.CHARSET_UTF8); }
             */

            setSystemLookAndFeel();

            // establish and initialize database connection
            DbObjectServer objSrv = initializeDatabase(System.getProperty("user.name"), null, "tcotool.model");

            String splashImage = "splash.jpg";
            if (Locale.getDefault().getLanguage().equals(Locale.GERMAN.getLanguage())) {
                splashImage = "splash_de.jpg";
            }
            showSplashScreen(new java.awt.Dimension(624, 400), ch.ehi.basics.i18n.ResourceBundle.getImageIcon(LauncherView.class, splashImage));

            ch.softenvironment.jomm.mvc.controller.ConsistencyController.setCascaded(true);

            ViewOptions viewOptions = new ViewOptions();
            // viewOptions.setOption(TEST_RELEASE);

            showInstance(viewOptions, ModelUtility.createDefaultConfiguration(objSrv), options);
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
            showException(instance, exception);
            Tracer.getInstance().stop();
            System.exit(-1);
        }
    }

    /**
     * Show About Dialog.
     */
    private void mniAbout() {
        try {
            new AboutDialog(this, getResourceString("FrmWindow_text"), getVersion(), "softEnvironment GmbH  {2004-2017}");
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Globally change Codes for current Configuration.
     *
     * @see CodeDetailView#dispose()
     */
    private void mniCodes() {
        getViewOptions().getViewManager().closeAll();

        javax.swing.JDialog dialog = new CodeDetailView(this, getViewOptions(), getUtility().getServer());
        getViewOptions().getViewManager().checkIn(null, dialog);
        dialog.setVisible(true);
    }

    /**
     * Draw a Graph of Dependencies.
     */
    private void mniDependencyGraph() {
        mniDependencyGraphTCO(Boolean.TRUE);
    }

    private void mniDependencyGraphTCO(final Boolean overallOnly) {
        final BaseFrame view = this;
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    WaitDialog.updateProgress(10, getResourceString("CIDependencyInit"));
                    java.util.Set<Dependency> dependencies = getUtility().findDependencies((TcoObject) getUtility().getRoot());
                    if (dependencies.size() == 0) {
                        BaseDialog.showWarning(view, getResourceString("MniDependencyGraph_text"), getResourceString("CEDependenciesMissing"));
                    } else {
                        WaitDialog.updateProgress(30, getResourceString("CIInitGraph"));
                        boolean doLayout = false;
                        if (getUtility().getDependencyDiagram() == null) {
                            doLayout = true;
                            // save new PresentationElements
                            setModelChanged(true);
                            getUtility().setDependencyDiagram((Diagram) ModelUtility.createDbObject(getUtility().getServer(), Diagram.class));
                        }
                        org.tcotool.standard.drawing.DependencyView depView = new org.tcotool.standard.drawing.DependencyView(getUtility(), getUtility()
                            .getDependencyDiagram(), dependencies, overallOnly, getDtpRoot().getWidth(), getDtpRoot().getHeight());
                        WaitDialog.updateProgress(50, getResourceString("CIReportInit"));
                        WaitDialog.updateProgress(65, getResourceString("CIAutoLayout"));
                        depView.setSize(getDtpRoot().getWidth() - 70, getDtpRoot().getHeight() - 60);
                        if (doLayout) {
                            // only do it the very first time (otherwise
                            // User-Layout should be available)
                            Layout.doLayout(depView);
                            depView.refresh();
                        }
                        GraphicReportFrame internalFrame = addReport(depView, getResourceString("MniDependencyGraph_text")
                            + (overallOnly ? " => " + ResourceManager.getResourceAsNonLabeled(DependencyView.class, "MniDependencyOverall_toolTipText")
                            : " => " + ResourceManager.getResourceAsNonLabeled(DependencyView.class, "MniDependencyTCO_toolTipText")), getDtpRoot()
                            .getWidth(), getDtpRoot().getHeight());
                        // get DataBrowser changes for "currentObject"
                        internalFrame.getTlbToolbar().addPropertyChangeListener(depView);

                        List<Integer> usageDuration = new ArrayList<Integer>();
                        if (overallOnly) {
                            // provide overall only
                            usageDuration.add(Calculator.INDEX_TOTAL);
                        } else {
                            // browse through TCO-Usage years without overall
                            for (int year = 0; year < depView.getCalculator().getDurationYears(); year++) {
                                usageDuration.add(Calculator.INDEX_TOTAL + 1 + year);
                            }
                        }
                        internalFrame.getTlbToolbar().setObjects(usageDuration);
                    }
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    private void mniFileImportCostCause() {
        importCode(CostCause.class, "Nr(UNIQUE!);Name;Direct(Yes/No);");
    }

    /**
     * Import code "CostCentre" from CSV-file. Replace text if CostCentre#iliCode matches.
     */
    private void mniFileImportCostCentre() {
        importCode(CostCentre.class, "Nr(UNIQUE!);Name;");
    }

    private void mniFind() {
        try {
            if (!getViewOptions().getViewManager().activateView(FindDialog.class)) {
                javax.swing.JDialog dialog = new FindDialog(getInstance(), getViewOptions());
                getViewOptions().getViewManager().checkIn(null, dialog);
                dialog.setVisible(true);
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    private void mniTile() {
        try {
            JInternalFrameUtils.tile(getDtpRoot());
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /**
     * Show Help.
     */
    private void mniHelp() {
        try {
            ch.ehi.basics.view.BrowserControl.displayURL("http://www.tcotool.org/index.html");
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Generate the inventory "Report (complete)".
     */
    private void mniReportComplete() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    addReport(ReportComplete.createComplete(getUtility(), (TcoPackage) getUtility().getRoot(), 1));
                    // getUtility().getTcoMaxUsage());
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    private void mniReportInvestment() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    addReport(ReportInvestment.createInvestment(LauncherView.getInstance().getUtility(), (TcoPackage) getUtility().getRoot()));
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    /**
     * Generate the "Report PersonalCost".
     */
    private void mniReportPersonalCost() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    addReport(ReportStaff.createPersonalCosts(LauncherView.getInstance().getUtility(), (TcoPackage) getUtility().getRoot()));
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    /**
     * Total TCO-Cost over whole Configuration.
     */
    private void mniReportTotalCost() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    addReport(ReportTco.createBlockTco(LauncherView.getInstance().getUtility(), (TcoPackage) getUtility().getRoot(), getUtility()
                        .getUsageDuration()));
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    /**
     *
     */
    private void mniRisk() {
        (new RiskDialog(this, null, true)).show();
    }

    /**
     * Show FileSaveDialog to save Model.
     */
    private boolean mniSaveAs() {
        try {
            FileChooser saveDialog = new FileChooser(getSettings().getWorkingDirectory());
            saveDialog.setDialogTitle(CommonUserAccess.getMniFileSaveAsText());
            saveDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());

            if (saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    getSettings().setWorkingDirectory(saveDialog.getCurrentDirectory().getAbsolutePath());
                    saveFile(saveDialog.getSelectedFile().getAbsolutePath());
                } catch (java.io.IOException e) {
                    handleException(e);
                } catch (Exception e) {
                    handleException(e);
                }
                return true;
            } else {
                // canceled
                return false;
            }
        } catch (Exception e) {
            handleException(e);
            return false;
        }
    }

    /**
     * Saves model and drawings into a file.
     */
    protected boolean mniSaveFile() {
        try {
            if (getUtility().getFilename() == null) {
                return mniSaveAs();
            } else {
                try {
                    saveFile(getUtility().getFilename());
                } catch (java.io.IOException e) {
                    handleException(e);
                } catch (Exception e) {
                    handleException(e);
                }
                return true;
            }
        } catch (Exception e) {
            handleException(e);
            return false;
        }
    }

    private void mniSystem() {
        try {
            java.util.List list = ch.softenvironment.util.ListUtils.createList(getUtility().getSystemParameter());
            if (!getViewOptions().getViewManager().activateView(list)) {
                // create new View
                ch.softenvironment.view.BaseFrame view = new SystemParameterDetailView(getViewOptions(), list);
                view.setRelativeLocation(LauncherView.getInstance());
                view.setVisible(true);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Generate a graphical Report for TCO. Based on jFreeChart
     */
    private void mniTcoChart() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    ChartTool tool = new ChartTool(getUtility(), getSettings());
                    addReport(tool.createTcoBarChart(), getResourceString("MniTcoChart_text"), 500, 500);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    /**
     * Start with a new standard model.
     */
    public void newObject(Object source) {
        try {
            if (saveCurrentChanges()) {
                DbObjectServer server = getUtility().getServer();
                server.reconnect();
                // set the Default Model
                setUtility(ModelUtility.createDefaultConfiguration(server) /*
                 * createTestTemplate ( server
                 */);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Return a Model read from XML-Instance by given filename.
     *
     * @param filename including absolute path
     * @return TcoModel (null if failed)
     */
    protected List openFile(ch.softenvironment.jomm.target.xml.XmlObjectServer server, java.lang.String filename) throws Exception {
        try {
            server.reconnect();
            ModelUtility.mapEnumerations(server);

            server.setUserObject(new ch.softenvironment.jomm.target.xml.XmlUserObject(filename));
            List baskets = ListUtils.createList(new ModelBasket(server));
            baskets.add(new PresentationBasket());
            server.retrieveAll(baskets);
            baskets.set(0, /* (TcoModel) */((IliBasket) baskets.get(0)).getModelRoot());
            baskets.set(1, /* (Diagram) */((IliBasket) baskets.get(1)).getModelRoot());
            server.setUserObject(null);
            return baskets;
        } catch (javax.jdo.JDODataStoreException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                BaseDialog.showWarning(this, ResourceManager.getResource(FileHistoryMenu.class, "CTFileMissing"),
                    ResourceManager.getResource(FileHistoryMenu.class, "CWFileMissing"));
                mnuFileHistory.removeRecent(filename);
            } else {
                handleException(e);
            }
            return null;
        }
    }

    /**
     * This method will be called by FileHistoryMenu, when a Filename was chosen in recent File-List.
     *
     * @param filename including absolute Path
     * @see FileHistoryMenu#addRecent(String)
     */
    @Override
    public void openFile(final java.lang.String filename) {
        try {
            if (saveCurrentChanges()) {
                final DbUserTransactionBlock block = getUtility().getServer().createUserTransactionBlock(true);
                block.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            block.setReturnValue(openFile((XmlObjectServer) block.getObjectServer(), filename));
                        } catch (Exception e) {
                            block.abort("openFile(" + filename + ")", e);
                        }
                    }
                });
                List baskets = (List) block.getReturnValue();
                if (baskets == null) {
                    BaseDialog.showWarning(this, getResourceString("CEConfigFault"), getResourceString("CIConfigFault"));
                    return;
                } else {
                    setUtility(new ModelUtility((TcoModel) baskets.get(0), filename));
                    getUtility().setDependencyDiagram((Diagram) baskets.get(1));
                    mnuFileHistory.addRecent(filename);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent event) {
        setModelChanged(true);
        /*
         * // adapt Window title if (event.getSource() instanceof Model) { setTitle(); }
         */

        // Keep documentation up to date
        if (event.getPropertyName().equals("documentation")) {
            refreshDocumentation();
        }
    }

    /**
     * Show refreshed documentation of the currently selected Element in Navigationtree.
     */
    private void refreshDocumentation() {
        if (currentElement == null) {
            getPnlDocumentation().setEditable(false);
            getPnlDocumentation().setText(null);
        } else {
            if ((getPnlDocumentation().getText() != null) && (!getPnlDocumentation().getText().equals(currentElement.getDocumentation()))) {
                getPnlDocumentation().setText(currentElement.getDocumentation());
            }
            getPnlDocumentation().setEditable(true);
        }
    }

    /**
     * @see ApplicationPlugin#showBusy(Runnable)
     */
    @Deprecated
    public void runBlock(Runnable block) {
        super.showBusy(block);
    }

    /**
     * Check whether model has changed.
     *
     * @return boolean (whether proceed or not)
     */
    private boolean saveCurrentChanges() {
        try {
            if (getTlbStandard().getTbbSaveEnabled()) {
                Boolean answer = BaseDialog.showConfirmExit(this);
                if (answer == null) {
                    // canceled -> do nothing yet!
                    return false;
                } else if (answer.booleanValue()) {
                    // depends whether save is ok!
                    return mniSaveFile();
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        // discard (if answer==false) or nothing changed yet
        return true;
    }

    /**
     * Save the documentation of the currentElement. Triggered at #keyReleased-Event of PnlDocumentation.
     */
    private void saveDocumentation() {
        // update current documentation
        if ((currentElement != null) && getPnlDocumentation().hasContentsChanged()) {
            currentElement.setDocumentation(getPnlDocumentation().getText());
        }
    }

    /**
     * Save Model into file.
     */
    private void saveFile(final String filename) throws Exception {
        // ((TcoModel)getUtility().getRoot()).setName(StringUtils.getPureFileName(filename));

        ch.softenvironment.jomm.target.xml.XmlUserObject userObject = new ch.softenvironment.jomm.target.xml.XmlUserObject(filename);
        userObject.setSender(ModelUtility.getIliSender());
        // TODO NYI: XSD
        userObject.setXsd(ModelUtility.XSD);
        // userObject.setXsl("http://www.tcotool.org/XSLT/TCOModel_Tree.xsl");
        final DbObjectServer server = getUtility().getServer();
        server.setUserObject(userObject);
        final DbUserTransactionBlock block = server.createUserTransactionBlock(true);
        block.execute(new Runnable() {
            @Override
            public void run() {
                IliBasket basket = new ModelBasket(server);
                basket.setModelRoot(/* LauncherView.getInstance(). */getUtility().getRoot());
                List baskets = ListUtils.createList(basket);
                if (getUtility().getDependencyDiagram() != null) {
                    basket = new PresentationBasket();
                    basket.setModelRoot(getUtility().getDependencyDiagram());
                    baskets.add(basket);
                }
                block.getObjectServer().makePersistentAll(baskets);
            }
        });
        // server.makePersistent(getUtility().getRoot());
        server.setUserObject(null);
        // PersistenceService service = new PersistenceService();
        // service.writeFile(filename, getModel());

        // setTitle(file); // set title before changed flag is cleared
        setModelChanged(false);
        //	log(getResourceString("CIModelSaved"), getResourceString("CIInFile") + file.getAbsolutePath()); //$NON-NLS-2$ //$NON-NLS-1$
        mnuFileHistory.addRecent(filename);

        getUtility().setFilename(filename);

        ch.softenvironment.jomm.mvc.controller.ConsistencyController.cascadedSaved();
    }

    /**
     * @see DetailView
     */
    public void saveObject() {
        showBusy(new Runnable() {
            @Override
            public void run() {
                mniSaveFile();
            }
        });
    }

    private void saveSettings() {
        getSettings().setLastFiles(mnuFileHistory.getHistory());
        // getSettings().setNavigationSort(NavigationTreeModel.SORT_BY_KIND_NAME);
        getSettings().setWindowHeight(Integer.valueOf(getHeight()));
        getSettings().setWindowWidth(Integer.valueOf(getWidth()));
        // getSettings().setWindowX(Integer.valueOf(getX()));
        // getSettings().setWindowY(Integer.valueOf(getY()));
        getSettings().save();
    }

    /**
     * Set the image to be displayed in the Report-Desktop-Pane.
     *
     * @param image
     */
    public void setBackgroundImage(Image image) {
        this.image = image;
    }

    /**
     * Register currently selected treeNode in NavigationTree for listening to PropertyChanges to update DocumentationPanel.
     */
    protected void setDocumentation(TcoObject element) {
        if (currentElement != element) {

            if (currentElement != null) {
                currentElement.removePropertyChangeListener(this);
            }
            // saveDocumentation(); -> done at keyReleased

            // replace current element
            currentElement = element;
            refreshDocumentation();

            if (currentElement != null) {
                currentElement.addPropertyChangeListener(this);
            }
        }
    }

    /**
     *
     */
    private void setFont() {
        try {
            java.awt.Font font = null;
            if (getRbtFontBig().isSelected()) {
                font = new java.awt.Font("Default", java.awt.Font.PLAIN, 14);
            }
            getSettings().setFont(font);
            getPnlNavigation().getTreNavigation().setFont(font);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Set the Language of User Interface.
     *
     * @see #main(String[])
     */
    private void setLanguage() {
        try {
            // change Locale-Default;
            boolean changed = false;
            // ResourceManager.setCharacterSet(null);
            if (getRbtLanguageGerman().isSelected()) {
                changed = NlsUtils.changeLocale(Locale.GERMAN);
            } else if (getRbtLanguageFrench().isSelected()) {
                changed = NlsUtils.changeLocale(Locale.FRENCH);
            } else if (getRbtLanguageItalian().isSelected()) {
                changed = NlsUtils.changeLocale(Locale.ITALIAN);
            } else if (getRbtChinese().isSelected()) {
                // ResourceManager.setCharacterSet(StringUtils.CHARSET_UTF8); //
                changed = NlsUtils.changeLocale(Locale.CHINESE);
            } else if (getRbtRussian().isSelected()) {
                // ResourceManager.setCharacterSet("Windows-1251"); //
                // ResourceManager.setCharacterSet(StringUtils.CHARSET_UTF8);
                changed = NlsUtils.changeLocale(new Locale("ru"));
            } else {
                changed = NlsUtils.changeLocale(Locale.ENGLISH);
            }

            // do generic translations
            mnuFileHistory.translate();
            mniCurrency.setText(ResourceManager.getResource(CourseDetailView.class, "FrmWindow_text") + "...");

            if (changed) {
                // generically resetable
                getSettings().setLanguage(Locale.getDefault().getLanguage());
                saveSettings();
                closeSubWindows();
                // updateStringComponent(this);
                // manual reset
            }
        } catch (Exception e) {
            Tracer.getInstance().runtimeError(null, e);
        }
    }

    /**
     * Keep changes of Model in mind.
     */
    public void setModelChanged(boolean changed) {
        // hasModelChanged = changed;
        boolean saveable = getViewOptions().getViewManager().getRights(TcoModel.class).isSaveObjectAllowed();
        getMniFileSave().setEnabled(saveable && changed);
        getMniFileSaveAs().setEnabled(saveable);
        getTlbStandard().setTbbSaveEnabled(saveable && changed);
    }

    /**
     * Set a new Utility to handle Model.
     *
     * @param utility Model (contains root-Element)
     */
    private void setUtility(ModelUtility utility) {
        this.utility = utility;

        // close all View's of old model
        setDocumentation(null);
        getDtpRoot().removeAll();
        getDtpRoot().revalidate();
        getDtpRoot().repaint();
        getViewOptions().getViewManager().closeAll();
        /*
         * if (utility.getFilename() != null) { ((TcoModel)this.utility.getRoot()).setName(new java.io.File(utility.getFilename()).getName()); }
         */
        getPnlNavigation().setUtility(getUtility());

        setModelChanged(false);
    }

    private static void showInstance(ViewOptions viewOptions, ModelUtility utility, ApplicationOptions options) throws Exception {
        new LauncherView(viewOptions, options);
        // getInstance().addDefaultClosingListener();
        // getInstance().setLookAndFeel(settings.getLookAndFeel());
        java.awt.Insets insets = getInstance().getInsets();
        getInstance().setSize(getInstance().getWidth() + insets.left + insets.right, getInstance().getHeight() + insets.top + insets.bottom);
        // /getInstance().setLocation(10, 10);
        getInstance().setVisible(true);
        getInstance().setUtility(utility);

        // viewOptions.getViewManager().setRights(new
        // UserActionRights(UserActionRights.READONLY)); // Read-Only Version
        getInstance().getStbStatus().setRights(viewOptions.getViewManager().getRights(TcoModel.class));

        try {
            // ApplicationPlugin#createAndShowGUI()
            /* getInstance().pluginManager = */
            //PluginUtility.invokePlugins("org.tcotool.core.runtime");
            ApplicationPlugin.invokePlugins(new SEPlugin());
        } catch (Exception e) {
            // TODO NLS
            BaseDialog.showError(getInstance(), "Plugin-Initialisierung",
                "Es ist ein Fehler beim Laden allfälliger Plugins aufgetreten.\nMöglicherweise sind nicht alle Plugin-Optionen verfügbar.", e);
        }
    }

    public void showStatus(String status) {
        getStbStatus().setStatus(status);
    }

    /**
     * Return the given object as package or root if null. Convenience method for e.g. to determine Report scope for a selected element or whole configuration.
     *
     * @param object
     * @return
     */
    public TcoPackage getTcoPackageOrRoot(Object object) {
        if (object == null) {
            return (TcoPackage) getUtility().getRoot();
        } else {
            return (TcoPackage) object;
        }
    }
}
