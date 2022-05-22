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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.view.TriStatePanel;
import ch.softenvironment.view.ViewOptions;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import org.tcotool.model.CostDriver;
import org.tcotool.model.Service;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.FindTool;
import org.tcotool.tools.ModelUtility;

/**
 * Find-dialog to search for name-String's in Configuration.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class FindDialog extends ch.softenvironment.view.BaseDialog implements ch.softenvironment.view.SearchView, javax.swing.event.HyperlinkListener {

    private JEditorPane editor = null;
    private List<String> searchList = new ArrayList<String>();  //  @jve:decl-index=0:
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JButton ivjBtnCancel = null;
    private javax.swing.JButton ivjBtnSearch = null;
    private javax.swing.JCheckBox ivjChxCaseSensitive = null;
    private javax.swing.JCheckBox ivjChxWholeWord = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private final java.awt.FlowLayout ivjJPanel1FlowLayout = null;
    private javax.swing.JPanel ivjJPanel2 = null;
    private javax.swing.JLabel ivjLblExpression = null;
    private javax.swing.JPanel ivjPnlOptions = null;
    private javax.swing.JTextField ivjTxtExpression = null;
    private javax.swing.JPanel ivjPnlScope = null;
    private javax.swing.JRadioButton ivjRbtAll = null;
    private javax.swing.JRadioButton ivjRbtCost = null;
    private javax.swing.JRadioButton ivjRbtCostDriver = null;
    private javax.swing.JRadioButton ivjRbtGroup = null;
    private javax.swing.JRadioButton ivjRbtService = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    class IvjEventHandler implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == FindDialog.this.getBtnCancel()) {
                connEtoC1(e);
            }
            if (e.getSource() == FindDialog.this.getBtnSearch()) {
                connEtoC2(e);
            }
        }

    }

    private javax.swing.JCheckBox ivjChxDocumentation = null;
    private javax.swing.JCheckBox ivjChxName = null;

    /**
     * Open Dialog for random user entry.
     *
     * @param owner The opening window
     * @param viewOptions
     * @see LauncherView@mniFind()
     */
    public FindDialog(java.awt.Frame owner, ViewOptions viewOptions) {
        super(owner, false, viewOptions);
        initialize();
    }

    /**
     * Search and display all references to given DbCode. For e.g from "Costblock" Report link.
     */
    public FindDialog(java.awt.Frame owner, ViewOptions viewOptions, DbCodeType code) {
        super(owner, false, viewOptions);
        initialize();

        lockEntry();
        getTxtExpression().setText(ModelUtility.getTypeString(code.getClass()) + ": " + code.getNameString());
        searchList = (new FindTool(LauncherView.getInstance().getUtility())).findByCode(code);
        showHits();
    }

    /**
     * If a TcoObject does not have specific Code and costs are accumulated among *_UNDEFINED.
     *
     * @param owner
     * @param viewOptions
     * @param undefinedCode "*_UNDEFINED" code
     * @see HTMLReportFrame#hyperlinkUpdate(HyperlinkEvent)
     * @see Calculator#*_UNDEFINED
     */
    public FindDialog(java.awt.Frame owner, ViewOptions viewOptions, final String undefinedCode) {
        super(owner, false, viewOptions);
        initialize();

        lockEntry();
        FindTool finder = new FindTool(LauncherView.getInstance().getUtility());
        searchList = finder.findByUndefinedCode(undefinedCode);
        getTxtExpression().setText(finder.getUndefinedLabel());
        showHits();
    }

    private void lockEntry() {
        getBtnSearch().setEnabled(false);
        getTxtExpression().setEditable(false);
        getChxName().setEnabled(false);
        getChxName().setSelected(false);
        getChxDocumentation().setEnabled(false);
        getChxCaseSensitive().setEnabled(false);
        getChxWholeWord().setEnabled(false);
    }

    private void showHits() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<h2>" + getResourceString("CMSearchResults") + " [" + searchList.size() + "]</h2>"); // ch.softenvironment.jomm.serialize.HtmlSerializer.suppressTag(htmlCode,
        // "head"));
        if (searchList.size() == 0) {
            buffer.append("<p>" + getResourceString("CMNoHits") + "</p>");
        } else {
            buffer.append("<ul>");
            Iterator<String> it = searchList.iterator();
            while (it.hasNext()) {
                buffer.append("<li>");
                buffer.append(it.next());
                buffer.append("</li>");
            }
            buffer.append("</ul>");
        }
        editor.setText(buffer.toString());
        // make sure report is scrolled to the beginning
        editor.select(0, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.view.SearchView#assignObjects()
     */
    @Override
    public void assignObjects() {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.view.ListMenuChoice#changeObjects(java.lang.Object)
     */
    @Override
    public void changeObjects(Object source) {
    }

    /**
     * connEtoC1: (BtnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> FindDialog.cancelPressed()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.cancelPressed();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (BtnSearch.action.actionPerformed(java.awt.event.ActionEvent) --> FindDialog.applyPressed()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.applyPressed();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.view.ListMenuChoice#copyObject(java.lang.Object)
     */
    @Override
    public void copyObject(Object source) {
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
                getBaseDialogContentPane().add(getJPanel1(), "South");
                getBaseDialogContentPane().add(getJPanel2(), "North");
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
     * Return the BtnCancel property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnCancel() {
        if (ivjBtnCancel == null) {
            try {
                ivjBtnCancel = new javax.swing.JButton();
                ivjBtnCancel.setName("BtnCancel");
                ivjBtnCancel.setText("Abbrechen");
                // user code begin {1}
                ivjBtnCancel.setText(getCancelString());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnCancel;
    }

    /**
     * Return the BtnSearch property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnSearch() {
        if (ivjBtnSearch == null) {
            try {
                ivjBtnSearch = new javax.swing.JButton();
                ivjBtnSearch.setName("BtnSearch");
                ivjBtnSearch.setText("Suchen");
                // user code begin {1}
                ivjBtnSearch.setText(getResourceString("FrmWindow_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnSearch;
    }

    /**
     * Return the ChxCaseSensitive property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxCaseSensitive() {
        if (ivjChxCaseSensitive == null) {
            try {
                ivjChxCaseSensitive = new javax.swing.JCheckBox();
                ivjChxCaseSensitive.setName("ChxCaseSensitive");
                ivjChxCaseSensitive.setToolTipText("Gross-/Kleinschreibung wird beruecksichtigt.");
                ivjChxCaseSensitive.setText("Case sensitive");
                ivjChxCaseSensitive.setBounds(12, 19, 133, 22);
                // user code begin {1}
                ivjChxCaseSensitive.setToolTipText(getResourceString("ChxCaseSensitive_toolTipText"));
                ivjChxCaseSensitive.setText(getResourceString("ChxCaseSensitive_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxCaseSensitive;
    }

    /**
     * Return the ChxDocumentation property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxDocumentation() {
        if (ivjChxDocumentation == null) {
            try {
                ivjChxDocumentation = new javax.swing.JCheckBox();
                ivjChxDocumentation.setName("ChxDocumentation");
                ivjChxDocumentation.setText("Beschreibung");
                ivjChxDocumentation.setBounds(152, 45, 102, 22);
                // user code begin {1}
                ivjChxDocumentation.setText(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "PnlNote_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxDocumentation;
    }

    /**
     * Return the ChxName property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxName() {
        if (ivjChxName == null) {
            try {
                ivjChxName = new javax.swing.JCheckBox();
                ivjChxName.setName("ChxName");
                ivjChxName.setSelected(true);
                ivjChxName.setText("Name");
                ivjChxName.setBounds(152, 19, 102, 22);
                // user code begin {1}
                ivjChxName.setText(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblName_text"));
                // TODO
                // ivjChxName.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxName;
    }

    /**
     * Return the ChxWholeWord property value.
     *
     * @return javax.swing.JCheckBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JCheckBox getChxWholeWord() {
        if (ivjChxWholeWord == null) {
            try {
                ivjChxWholeWord = new javax.swing.JCheckBox();
                ivjChxWholeWord.setName("ChxWholeWord");
                ivjChxWholeWord.setToolTipText("Treffer gilt nur, wenn der ganze Ausdruck uebereinstimmt.");
                ivjChxWholeWord.setText("Ganzes Wort");
                ivjChxWholeWord.setBounds(12, 47, 133, 22);
                // user code begin {1}
                ivjChxWholeWord.setToolTipText(getResourceString("ChxWholeWord_toolTipext"));
                ivjChxWholeWord.setText(getResourceString("ChxWholeWord_text"));
                // TODO
                ivjChxWholeWord.setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjChxWholeWord;
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
                ivjJPanel1.setPreferredSize(new java.awt.Dimension(0, 40));
                ivjJPanel1.setLayout(getJPanel1FlowLayout());
                getJPanel1().add(getBtnSearch(), getBtnSearch().getName());
                getJPanel1().add(getBtnCancel(), getBtnCancel().getName());
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
     * Return the JPanel1FlowLayout property value.
     *
     * @return java.awt.FlowLayout
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private java.awt.FlowLayout getJPanel1FlowLayout() {
        java.awt.FlowLayout ivjJPanel1FlowLayout = null;
        try {
            /* Create part */
            ivjJPanel1FlowLayout = new java.awt.FlowLayout();
            ivjJPanel1FlowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        return ivjJPanel1FlowLayout;
    }

    /**
     * Return the JPanel2 property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getJPanel2() {
        if (ivjJPanel2 == null) {
            try {
                ivjJPanel2 = new javax.swing.JPanel();
                ivjJPanel2.setName("JPanel2");
                ivjJPanel2.setPreferredSize(new java.awt.Dimension(0, 200));
                ivjJPanel2.setLayout(null);
                getJPanel2().add(getLblExpression(), getLblExpression().getName());
                getJPanel2().add(getTxtExpression(), getTxtExpression().getName());
                getJPanel2().add(getPnlOptions(), getPnlOptions().getName());
                getJPanel2().add(getPnlScope(), getPnlScope().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanel2;
    }

    /**
     * Return the LblExpression property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblExpression() {
        if (ivjLblExpression == null) {
            try {
                ivjLblExpression = new javax.swing.JLabel();
                ivjLblExpression.setName("LblExpression");
                ivjLblExpression.setText("Ausdruck:");
                ivjLblExpression.setBounds(10, 15, 127, 14);
                // user code begin {1}
                ivjLblExpression.setText(getResourceString("LblExpression_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblExpression;
    }

    /**
     * Return the PnlOptions property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlOptions() {
        if (ivjPnlOptions == null) {
            try {
                ivjPnlOptions = new javax.swing.JPanel();
                ivjPnlOptions.setName("PnlOptions");
                ivjPnlOptions.setLayout(null);
                ivjPnlOptions.setBounds(275, 41, 182, 153);
                getPnlOptions().add(getChxCaseSensitive(), getChxCaseSensitive().getName());
                getPnlOptions().add(getChxWholeWord(), getChxWholeWord().getName());
                // user code begin {1}
                getPnlOptions().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlOptions_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlOptions;
    }

    /**
     * Return the PnlScope property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlScope() {
        if (ivjPnlScope == null) {
            try {
                ivjPnlScope = new javax.swing.JPanel();
                ivjPnlScope.setName("PnlScope");
                ivjPnlScope.setLayout(null);
                ivjPnlScope.setBounds(9, 41, 259, 153);
                getPnlScope().add(getRbtAll(), getRbtAll().getName());
                getPnlScope().add(getRbtGroup(), getRbtGroup().getName());
                getPnlScope().add(getRbtService(), getRbtService().getName());
                getPnlScope().add(getRbtCostDriver(), getRbtCostDriver().getName());
                getPnlScope().add(getRbtCost(), getRbtCost().getName());
                getPnlScope().add(getChxName(), getChxName().getName());
                getPnlScope().add(getChxDocumentation(), getChxDocumentation().getName());
                // user code begin {1}
                getPnlScope().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(getResourceString("PnlScope_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // TODO
                getRbtGroup().setEnabled(false);
                getRbtService().setEnabled(false);
                getRbtCostDriver().setEnabled(false);
                getRbtCost().setEnabled(false);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlScope;
    }

    /**
     * Return the RbtAll property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtAll() {
        if (ivjRbtAll == null) {
            try {
                ivjRbtAll = new javax.swing.JRadioButton();
                ivjRbtAll.setName("RbtAll");
                ivjRbtAll.setText("Alle");
                ivjRbtAll.setBounds(10, 19, 135, 22);
                // user code begin {1}
                ivjRbtAll.setText(ResourceManager.getResource(TriStatePanel.class, "CI_All_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtAll;
    }

    /**
     * Return the RbtCost property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtCost() {
        if (ivjRbtCost == null) {
            try {
                ivjRbtCost = new javax.swing.JRadioButton();
                ivjRbtCost.setName("RbtCost");
                ivjRbtCost.setText("Kosten (SK & PK)");
                ivjRbtCost.setBounds(10, 123, 135, 22);
                // user code begin {1}
                ivjRbtCost.setText(ResourceManager.getResource(CostDriverDetailView.class, "PnlCost_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtCost;
    }

    /**
     * Return the RbtCostDriver property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtCostDriver() {
        if (ivjRbtCostDriver == null) {
            try {
                ivjRbtCostDriver = new javax.swing.JRadioButton();
                ivjRbtCostDriver.setName("RbtCostDriver");
                ivjRbtCostDriver.setText("Kostentreiber");
                ivjRbtCostDriver.setBounds(10, 98, 135, 22);
                // user code begin {1}
                ivjRbtCostDriver.setText(ModelUtility.getTypeString(CostDriver.class));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtCostDriver;
    }

    /**
     * Return the RbtGroup property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtGroup() {
        if (ivjRbtGroup == null) {
            try {
                ivjRbtGroup = new javax.swing.JRadioButton();
                ivjRbtGroup.setName("RbtGroup");
                ivjRbtGroup.setText("Gruppe");
                ivjRbtGroup.setBounds(10, 45, 135, 22);
                // user code begin {1}
                ivjRbtGroup.setText(ModelUtility.getTypeString(TcoPackage.class));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtGroup;
    }

    /**
     * Return the RbtService property value.
     *
     * @return javax.swing.JRadioButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JRadioButton getRbtService() {
        if (ivjRbtService == null) {
            try {
                ivjRbtService = new javax.swing.JRadioButton();
                ivjRbtService.setName("RbtService");
                ivjRbtService.setText("Service");
                ivjRbtService.setBounds(10, 71, 135, 22);
                // user code begin {1}
                ivjRbtService.setText(ModelUtility.getTypeString(Service.class));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjRbtService;
    }

    /**
     * Return the JTextField1 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getTxtExpression() {
        if (ivjTxtExpression == null) {
            try {
                ivjTxtExpression = new javax.swing.JTextField();
                ivjTxtExpression.setName("TxtExpression");
                ivjTxtExpression.setBounds(150, 12, 262, 20);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTxtExpression;
    }

    @Override
    protected void handleException(java.lang.Throwable exception) {
        super.handleException(exception);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event
     * .HyperlinkEvent)
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            String id = e.getDescription().trim();
            org.tcotool.model.TcoObject object = LauncherView.getInstance().getUtility()
                .findObject(Long.valueOf(id), (org.tcotool.model.TcoModel) LauncherView.getInstance().getUtility().getRoot());
            if (object != null) {
                LauncherView.getInstance().getPnlNavigation().selectElement(object);
            } else {
                ch.softenvironment.view.BaseDialog.showWarning(LauncherView.getInstance(),
                    ResourceManager.getResource(HTMLReportFrame.class, "CTObjectRemoved"),
                    ResourceManager.getResource(HTMLReportFrame.class, "CIObjectRemoved"));
            }
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
        getBtnCancel().addActionListener(ivjEventHandler);
        getBtnSearch().addActionListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("DlgFind");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(468, 382);
            setTitle("Suchen");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setSize(475, 520);
        setTitle(getResourceString("FrmWindow_text"));
        // TODO setFrameIcon(ResourceBundle.getImageIcon(LauncherView.class,
        // "TCO_Icon.png") /*.getImage()*/);

        editor = new JEditorPane();
        editor.setContentType("text/html; charset=iso-8859-1");
        editor.addHyperlinkListener(this);
        // make HTML-area scrollable
        editor.setAutoscrolls(true);
        editor.setEditable(false);
        JScrollPane sp = new JScrollPane(editor);
        sp.setAutoscrolls(true);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getContentPane().add(sp, "Center");

        getChxName().setSelected(true);
        getChxDocumentation().setSelected(true);
        getChxCaseSensitive().setSelected(false);

        ButtonGroup group = new ButtonGroup();
        group.add(getRbtAll());
        group.add(getRbtCost());
        group.add(getRbtCostDriver());
        group.add(getRbtGroup());
        group.add(getRbtService());
        getRbtAll().setSelected(true);
        // user code end
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.view.ListMenuChoice#newObject(java.lang.Object)
     */
    @Override
    public void newObject(Object source) {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.view.ListMenuChoice#removeObjects(java.lang.Object)
     */
    @Override
    public void removeObjects(Object source) {
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.view.SearchView#resetSearchArguments()
     */
    @Override
    public void resetSearchArguments() {
    }

    @Override
    protected boolean save() {
        searchObjects();
        return super.save();
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.view.SearchView#searchObjects()
     */
    @Override
    public void searchObjects() {
        searchList = (new FindTool(LauncherView.getInstance().getUtility()))
            .findByText(getTxtExpression().getText(), getChxName().isSelected(), getChxDocumentation().isSelected(), getChxCaseSensitive().isSelected());
			
			/*new ArrayList<String>();
		expression = getTxtExpression().getText();
		if (!StringUtils.isNullOrEmpty(expression)) {
			TcoObject root = (TcoObject)LauncherView.getInstance().getUtility().getRoot();
			find(root, null);
		} // don't search without expression
	*/
        showHits();
    }

    @Override
    public void adaptUserAction(EventObject event, Object control) {
    }
}
