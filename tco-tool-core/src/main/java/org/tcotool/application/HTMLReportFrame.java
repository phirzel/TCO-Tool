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
import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.swingext.JEditorPanePrintUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.tcotool.standard.report.ReportTool;

/**
 * InternalFrame for HTML-Reports.
 *
 * @author Peter Hirzel
 */

public class HTMLReportFrame extends JInternalFrame implements javax.swing.event.HyperlinkListener {

    private JEditorPane editor = null;
    private JPanel ivjJInternalFrameContentPane = null;
    private ch.softenvironment.view.ToolBar ivjTlbToolbar = null;
    private ch.softenvironment.view.ViewOptions viewOptions = null;
    private ReportTool tool = null;

    class IvjEventHandler implements ch.softenvironment.view.ToolBarListener {

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
        }

        @Override
        public void tbbOpenAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbPasteAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbPrintAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == HTMLReportFrame.this.getTlbToolbar()) {
                printObject();
            }
        }

        @Override
        public void tbbRedoAction_actionPerformed(java.util.EventObject newEvent) {
        }

        @Override
        public void tbbSaveAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == HTMLReportFrame.this.getTlbToolbar()) {
                connEtoC1(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

    }

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    /**
     * ReportFrame constructor comment.
     */
    public HTMLReportFrame(ch.softenvironment.view.ViewOptions viewOptions, ReportTool tool) {
        super(tool.getTitle(), true, true, true, true);
        this.viewOptions = viewOptions;
        this.tool = tool;
        initialize();
        setTitle(tool.getTitle());
        setContents(tool.getHTMLContents());
    }

    /**
     * connEtoC1:  (TlbToolbar.toolBar.tbbSaveAction_actionPerformed(java.util.EventObject) --> HTMLReportFrame.saveObject()V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.util.EventObject arg1) {
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
     * Return the JInternalFrameContentPane property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getJInternalFrameContentPane() {
        if (ivjJInternalFrameContentPane == null) {
            try {
                ivjJInternalFrameContentPane = new javax.swing.JPanel();
                ivjJInternalFrameContentPane.setName("JInternalFrameContentPane");
                ivjJInternalFrameContentPane.setLayout(new java.awt.BorderLayout());
                getJInternalFrameContentPane().add(getTlbToolbar(), "North");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJInternalFrameContentPane;
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
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {
        LauncherView.getInstance().handleException(exception);
    }

    /**
     * Called when a hypertext link is updated.
     *
     * @param e the event responsible for the update
     */
    @Override
    public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
        try {
            if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
                String /*Long*/ id = e.getDescription().trim();
                try {
                    // 1) try TcoObject
                    org.tcotool.model.TcoObject object = LauncherView.getInstance().getUtility()
                        .findObject(Long.valueOf(id), (org.tcotool.model.TcoModel) LauncherView.getInstance().getUtility().getRoot());
                    if (object != null) {
                        LauncherView.getInstance().getPnlNavigation().selectElement(object);
                    } else {
                        XmlObjectServer server = ((XmlObjectServer) LauncherView.getInstance().getUtility().getServer());
                        // 2)DbEnumeration
/*                  Iterator codes = server.retrieveCodes(CostType.class).iterator();
                    while (codes.hasNext()) {
                       DbEnumeration code = (DbEnumeration)codes.next();
                       if (code.getId().toString().equals(id)) {
                           JDialog dialog = new FindDialog(this, viewOptions, code);
                           dialog.setVisible(true);
                           return;
                       }
                    }
*/
                        // 3) DbCode
                        Iterator<Class<? extends DbCodeType>> it = server.getCodeTypes().iterator();
                        while (it.hasNext()) {
                            Class<? extends DbCodeType> codeType = it.next();
                            Iterator<? extends DbCodeType> codes = server.retrieveCodes(codeType).iterator();
                            while (codes.hasNext()) {
                                DbCode code = (DbCode) codes.next();
                                if (code.getId().toString().equals(id)) {
                                    JDialog dialog = new FindDialog(LauncherView.getInstance(), viewOptions, code);
                                    dialog.setVisible(true);
                                    return;
                                }
                            }
                        }
                        ch.softenvironment.view.BaseDialog.showWarning(LauncherView.getInstance(), ResourceManager.getResource(HTMLReportFrame.class, "CTObjectRemoved"),
                            ResourceManager.getResource(HTMLReportFrame.class, "CIObjectRemoved"));
                    }
                } catch (NumberFormatException nfe) {
                    // try "UNDEFINED_* Code" as faked id (not a TID of a TcoObject)
                    JDialog dialog = new FindDialog(LauncherView.getInstance(), viewOptions, id);
                    dialog.setVisible(true);
                }
            }
        } catch (Throwable ex) {
            LauncherView.getInstance().handleException(ex);
        }
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
        getTlbToolbar().addToolBarListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("ReportFrame");
            setSize(534, 460);
            setTitle("<Report>");
            setContentPane(getJInternalFrameContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        editor = new JEditorPane();
        editor.setContentType("text/html; charset=iso-8859-1");
        editor.addHyperlinkListener(this);
        // make HTML-area scrollable
        editor.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(editor);
        sp.setAutoscrolls(true);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        getJInternalFrameContentPane().add(sp);
        //	editor.setSize(new Dimension(100, 150));
        setFrameIcon(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png") /*.getImage()*/);
        // user code end
    }

    /**
     * Print the Report to print-device.
     */
    private void printObject() {
        //HTMLDocument doc = (HTMLDocument)editor.getDocument();
        (new JEditorPanePrintUtility(getTitle())).print(/*doc*/ editor);
    /*
    java.awt.print.PrinterJob printJob = java.awt.print.PrinterJob.getPrinterJob();
    printJob.setJobName(getTitle());

    java.awt.print.PageFormat pf = printJob.pageDialog(printJob.defaultPage());
    //pf.setOrientation(java.awt.print.PageFormat.PORTRAIT);
    //printJob.setPrintable(new PrintWrapper(classDiagram),pf);

    if (printJob.printDialog()) {
        // Print the job if the user didn't cancel printing
              // Ask user for page format (e.g., portrait/landscape)
        try {
            printJob.print();
        } catch(java.awt.print.PrinterException ex) {
            handleException(ex);
        }
    }
    */
    }

    /**
     * Save HTML-Report into MyFile.csv/html.
     */
    private void saveObject() {
        try {
            String fileName = getTitle();

            FileChooser saveDialog = new FileChooser(LauncherView.getInstance().getSettings().getWorkingDirectory());
            saveDialog.setDialogTitle(ch.softenvironment.view.CommonUserAccess.getMniFileSaveAsText());
            saveDialog.setSelectedFile(new File(fileName));
            saveDialog.addChoosableFileFilter(ch.ehi.basics.view.GenericFileFilter.createHtmlFilter());
            saveDialog.addChoosableFileFilter(ch.ehi.basics.view.GenericFileFilter.createCsvFilter());

            if (saveDialog.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                String filename = saveDialog.getSelectedFile().getAbsolutePath();
                final String contents;
                if (filename.toUpperCase().endsWith(".CSV")) {
                    contents = tool.getCsvContents();
                } else /*if (filename.toUpperCase().endsWith(".HTML"))*/ {
                    filename = StringUtils.tryAppendSuffix(filename, ".html");
                    contents = tool.getHTMLContents();
                }
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(filename); //saveDialog.getSelectedFile().getName());
                    PrintStream stream = new PrintStream(out);
                    stream.print(contents);
                    stream.flush();
                } catch (Throwable fe) {
                    BaseDialog.showWarning(LauncherView.getInstance(),
                        ResourceManager.getResource(HTMLReportFrame.class, "CTSaveWarning"),
                        NlsUtils.formatMessage(ResourceManager.getResource(HTMLReportFrame.class, "CISaveWarning"), filename));
                    filename = null;
                } finally {
                    if (out != null) {
                        out.close();
                        if (fileName != null) {
                            ch.ehi.basics.view.BrowserControl.displayURL("file://" + filename);
                        }
                    }
                }
            }
            //	} catch(FileNotFoundException fne) {
            //		new ch.softenvironment.view.ErrorDialog(LauncherView.getInstance(), "Verzeichnis fehlt", "Stellen Sie sicher, dass das folgende Verzeichnis existiert:\n" + LauncherView.getSettings().getWorkingDirectory(), fne);
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
     * Set the HTML-contents to be displayed.
     */
    public void setContents(String htmlCode) {
        editor.setText(ch.softenvironment.jomm.serialize.HtmlSerializer.suppressTag(htmlCode, "head"));
        editor.setEditable(false);
        getTlbToolbar().setTbbSaveEnabled(true);
        getTlbToolbar().setTbbPrintEnabled(true);
        // make sure report is scrolled to the beginning
        editor.select(0, 0);
    }
}
