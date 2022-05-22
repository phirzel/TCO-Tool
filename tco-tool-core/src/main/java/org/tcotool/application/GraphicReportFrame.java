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
import ch.softenvironment.view.BaseFrame;
import ch.softenvironment.view.ToolBar;
import ch.softenvironment.view.swingext.JPanelPrintable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.print.PageFormat;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * InternalFrame to show graphical reports.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class GraphicReportFrame extends JInternalFrame {

    private JPanel pnlContent = null;
    private JPanel graph = null;
    private ToolBar tlbToolbar = null;

    /**
     * This method initializes
     */
    public GraphicReportFrame(final String title, JPanel graph) {
        super(title, true, true, true, true);
        initialize();
        setFrameIcon(ResourceBundle.getImageIcon(LauncherView.class, "TCO_Icon.png"));

        this.graph = graph;
        graph.setAutoscrolls(true);
        JScrollPane spc = new JScrollPane(graph);
        spc.setAutoscrolls(true);
        spc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        spc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        getPnlContent().add(spc, "Center");
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        try {
            this.setSize(new Dimension(442, 188));
            this.setContentPane(getPnlContent());
        } catch (Exception e) {
            BaseFrame.showException(this, e);
        }
    }

    /**
     * This method initializes pnlContent
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPnlContent() {
        if (pnlContent == null) {
            try {
                pnlContent = new JPanel();
                pnlContent.setLayout(new BorderLayout());
                pnlContent.add(getTlbToolbar(), BorderLayout.NORTH);
            } catch (Exception e) {
                BaseFrame.showException(this, e);
            }
        }
        return pnlContent;
    }

    /**
     * This method initializes tlbToolbar
     *
     * @return ch.softenvironment.view.ToolBar
     */
    protected ToolBar getTlbToolbar() {
        if (tlbToolbar == null) {
            try {
                tlbToolbar = new ToolBar();
                // get standard menu buttons events
                tlbToolbar.addToolBarListener(new ch.softenvironment.view.ToolBarListener() {
                    @Override
                    public void tbbPrintAction_actionPerformed(java.util.EventObject e) {
                        printObject();
                    }

                    @Override
                    public void tbbCopyAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbCutAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbDeleteAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbFindAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbNewAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbOpenAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbPasteAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbRedoAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbSaveAction_actionPerformed(java.util.EventObject e) {
                    }

                    @Override
                    public void tbbUndoAction_actionPerformed(java.util.EventObject e) {
                    }
                });
                tlbToolbar.setTbbPrintEnabled(true);
            } catch (Exception e) {
                BaseFrame.showException(this, e);
            }
        }
        return tlbToolbar;
    }

    /**
     * Print the Report to print-device.
     */
    private void printObject() {
        java.awt.print.PrinterJob printJob = java.awt.print.PrinterJob.getPrinterJob();
        printJob.setJobName(getTitle());

        PageFormat pf = new PageFormat(); // printJob.pageDialog(printJob.defaultPage());
        pf.setOrientation(java.awt.print.PageFormat.PORTRAIT);
        printJob.setPrintable(new JPanelPrintable(graph), pf);

        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (java.awt.print.PrinterException ex) {
                BaseFrame.showException(this, ex);
            }
        }
    }

} // @jve:decl-index=0:visual-constraint="10,10"
