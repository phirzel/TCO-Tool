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

import ch.ehi.basics.view.FileChooser;
import ch.ehi.basics.view.GenericFileFilter;
import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.client.UserActionRights;
import ch.softenvironment.jomm.DbUserTransactionBlock;
import ch.softenvironment.jomm.mvc.controller.DbTableModel;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.util.ListUtils;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.CommonUserAccess;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import org.tcotool.model.Activity;
import org.tcotool.model.Catalogue;
import org.tcotool.model.CostCause;
import org.tcotool.model.CostCentre;
import org.tcotool.model.ProjectPhase;
import org.tcotool.model.Responsibility;
import org.tcotool.model.Role;
import org.tcotool.model.ServiceCategory;
import org.tcotool.model.Site;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.ModelUtility;

/**
 * Code changing Dialog.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class CodeDetailView extends ch.softenvironment.view.BaseDialog implements ch.softenvironment.view.SearchView {

    private ch.softenvironment.jomm.DbObjectServer server = null;
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JPanel ivjPnlCodes = null;
    private ch.softenvironment.view.StatusBar ivjStbStatusVar = null;
    private ch.softenvironment.view.ToolBar ivjTlbToolbar = null;
    private javax.swing.JLabel ivjLblCategory = null;
    private javax.swing.JLabel ivjLblCostCentre = null;
    private javax.swing.JPanel ivjPnlService = null;
    private javax.swing.JLabel ivjLblPhase = null;
    private javax.swing.JLabel ivjLblProcess = null;
    private javax.swing.JLabel ivjLblSite = null;
    private javax.swing.JPanel ivjPnlCostDriver = null;
    private javax.swing.JPanel ivjPnlCost = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JLabel ivjLblResponsibility = null;
    private javax.swing.JLabel ivjLblRole = null;
    private javax.swing.JLabel ivjLblCatalogue = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlProcess = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlCatalogue = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlCategory = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlCostCentre = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlPhase = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlResponsibility = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlRole = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlSite = null;
    private javax.swing.JLabel ivjLblActivity = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlActivity = null;
    private javax.swing.JButton ivjBtnExport = null;
    private javax.swing.JButton ivjBtnImport = null;

    class IvjEventHandler implements ch.softenvironment.view.ToolBarListener, java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == CodeDetailView.this.getBtnImport()) {
                connEtoC1(e);
            }
            if (e.getSource() == CodeDetailView.this.getBtnExport()) {
                connEtoC2(e);
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
        }

        @Override
        public void tbbOpenAction_actionPerformed(java.util.EventObject newEvent) {
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
            if (newEvent.getSource() == CodeDetailView.this.getTlbToolbar()) {
                connEtoC7(newEvent);
            }
        }

        @Override
        public void tbbUndoAction_actionPerformed(java.util.EventObject newEvent) {
        }

    }

    private javax.swing.JLabel ivjLblCostCause = null;
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView ivjPnlCostCause = null;

    /**
     * CodeDetailView constructor comment.
     */
    public CodeDetailView(java.awt.Frame owner, ch.softenvironment.view.ViewOptions viewOptions, ch.softenvironment.jomm.DbObjectServer server) {
        super(owner, false, viewOptions);
        this.server = server;
        initialize();
    }

    @Override
    public void adaptUserAction(EventObject event, Object control) {
    }

    /**
     * Assign the selected Objects in a SearchTable to the caller.
     *
     * @throws Throwable Handled by this GUI-Method.
     * @see DbTableModel
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
     * connEtoC1: (BtnImport.action.actionPerformed(java.awt.event.ActionEvent) --> CodeDetailView.importCodes()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.importCodes();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (BtnExport.action.actionPerformed(java.awt.event.ActionEvent) --> CodeDetailView.exportCodes()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.exportCodes();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC7: (TlbToolbar.toolBar.tbbSaveAction_actionPerformed(java.util.EventObject) --> CodeDetailView.saveObject()V)
     *
     * @param arg1 java.util.EventObject
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC7(java.util.EventObject arg1) {
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
     * Create a new Object as a "copy" of a selected one (and open it for e.g. in a DetailView).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void copyObject(java.lang.Object source) {
    }

    /**
     * Export codes from a file.
     */
    private void exportCodes() {
        try {
            FileChooser saveDialog = new FileChooser(LauncherView.getInstance().getSettings().getWorkingDirectory());
            saveDialog.setDialogTitle(CommonUserAccess.getMniFileSaveAsText());
            saveDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());
            if (saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                ch.softenvironment.jomm.target.xml.XmlUserObject userObject = new ch.softenvironment.jomm.target.xml.XmlUserObject(saveDialog.getSelectedFile()
                    .getAbsolutePath());
                userObject.setSender(ModelUtility.getIliSender());
                // TODO NYI: XSD
                userObject.setXsd(ModelUtility.XSD);
                // userObject.setXsl("http://www.tcotool.org/XSLT/TCOModel_Tree.xsl");
                server.setUserObject(userObject);
                final DbUserTransactionBlock block = server.createUserTransactionBlock(true);
                block.execute(new Runnable() {
                    @Override
                    public void run() {
                        List baskets = ListUtils.createList(new ModelBasket(server)); // no
                        // root!
                        block.getObjectServer().makePersistentAll(baskets);
                    }
                });
                // server.makePersistent(getUtility().getRoot());
                server.setUserObject(null);
                // PersistenceService service = new PersistenceService();
                // service.writeFile(filename, getModel());
            }
        } catch (Exception e) {
            handleException(e);
        }
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
                getBaseDialogContentPane().add(getTlbToolbar(), "North");
                getBaseDialogContentPane().add(getStbStatusVar(), "South");
                getBaseDialogContentPane().add(getPnlCodes(), "Center");
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
     * Return the BtnExport property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnExport() {
        if (ivjBtnExport == null) {
            try {
                ivjBtnExport = new javax.swing.JButton();
                ivjBtnExport.setName("BtnExport");
                ivjBtnExport.setToolTipText("Export all Business-Object's into one Configuration.xml file.");
                ivjBtnExport.setText("Export...");
                ivjBtnExport.setBounds(190, 475, 113, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnExport;
    }

    /**
     * Return the BtnImport property value.
     *
     * @return javax.swing.JButton
     * @deprecated (@ see # importCodes ())
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnImport() {
        if (ivjBtnImport == null) {
            try {
                ivjBtnImport = new javax.swing.JButton();
                ivjBtnImport.setName("BtnImport");
                ivjBtnImport.setToolTipText("Saemtliche Geschaeftsobjekte aus einer anderen Konfiguration importieren.");
                ivjBtnImport.setText("Import...");
                ivjBtnImport.setBounds(320, 475, 113, 25);
                // user code begin {1}
                ivjBtnImport.setEnabled(false);
                // TODO NLS
                ivjBtnImport.setToolTipText("Saemtliche Geschaeftsobjekte aus einer anderen Konfiguration importieren.");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnImport;
    }

    /**
     * Return the LblActivity property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblActivity() {
        if (ivjLblActivity == null) {
            try {
                ivjLblActivity = new javax.swing.JLabel();
                ivjLblActivity.setName("LblActivity");
                ivjLblActivity.setToolTipText("");
                ivjLblActivity.setText("Aktivitaet");
                ivjLblActivity.setBounds(11, 112, 150, 14);
                // user code begin {1}
                ivjLblActivity.setText(ResourceManager.getResource(PersonalCostDetailView.class, "LblActivity_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblActivity;
    }

    /**
     * Return the LblCatalogue property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCatalogue() {
        if (ivjLblCatalogue == null) {
            try {
                ivjLblCatalogue = new javax.swing.JLabel();
                ivjLblCatalogue.setName("LblCatalogue");
                ivjLblCatalogue.setText("Katalog-Teil:");
                ivjLblCatalogue.setBounds(11, 51, 150, 14);
                // user code begin {1}
                ivjLblCatalogue.setText(ResourceManager.getResourceAsLabel(CatalogueDetailView.class, "FrmWindow_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCatalogue;
    }

    /**
     * Return the LblCategory property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCategory() {
        if (ivjLblCategory == null) {
            try {
                ivjLblCategory = new javax.swing.JLabel();
                ivjLblCategory.setName("LblCategory");
                ivjLblCategory.setText("Kategorie:");
                ivjLblCategory.setBounds(11, 28, 147, 14);
                // user code begin {1}
                ivjLblCategory.setText(ModelUtility.getTypeString(ServiceCategory.class) + ":" /*
                 * getResourceString
                 * (
                 * ServiceDetailView
                 * .
                 * class
                 * ,
                 * "LblCategory_text"
                 * )
                 */);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCategory;
    }

    /**
     * Return the LblCostCause property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCostCause() {
        if (ivjLblCostCause == null) {
            try {
                ivjLblCostCause = new javax.swing.JLabel();
                ivjLblCostCause.setName("LblCostCause");
                ivjLblCostCause.setText("Katalog-Teil:");
                ivjLblCostCause.setBounds(11, 21, 150, 14);
                // user code begin {1}
                ivjLblCostCause.setText(ResourceManager.getResourceAsLabel(CostDriverDetailView.class, "TbcCosttype_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCostCause;
    }

    /**
     * Return the LblCostCentre property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblCostCentre() {
        if (ivjLblCostCentre == null) {
            try {
                ivjLblCostCentre = new javax.swing.JLabel();
                ivjLblCostCentre.setName("LblCostCentre");
                ivjLblCostCentre.setText("Kostenstelle:");
                ivjLblCostCentre.setBounds(11, 86, 147, 14);
                // user code begin {1}
                ivjLblCostCentre.setText(ResourceManager.getResource(ServiceDetailView.class, "LblCostCentre_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblCostCentre;
    }

    /**
     * Return the LblPhase property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblPhase() {
        if (ivjLblPhase == null) {
            try {
                ivjLblPhase = new javax.swing.JLabel();
                ivjLblPhase.setName("LblPhase");
                ivjLblPhase.setToolTipText("nach HERMES");
                ivjLblPhase.setText("Projekt-Phase:");
                ivjLblPhase.setBounds(10, 56, 148, 14);
                // user code begin {1}
                ivjLblPhase.setText(ResourceManager.getResource(CostDriverDetailView.class, "LblPhase_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblPhase;
    }

    /**
     * Return the LblProcess property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblProcess() {
        if (ivjLblProcess == null) {
            try {
                ivjLblProcess = new javax.swing.JLabel();
                ivjLblProcess.setName("LblProcess");
                ivjLblProcess.setText("Prozess:");
                ivjLblProcess.setBounds(10, 28, 148, 14);
                // user code begin {1}
                ivjLblProcess.setText(ResourceManager.getResource(CostDriverDetailView.class, "LblProcess_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblProcess;
    }

    /**
     * Return the LblCatalogue1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblResponsibility() {
        if (ivjLblResponsibility == null) {
            try {
                ivjLblResponsibility = new javax.swing.JLabel();
                ivjLblResponsibility.setName("LblResponsibility");
                ivjLblResponsibility.setText("Zustaendigkeit:");
                ivjLblResponsibility.setBounds(11, 60, 147, 14);
                // user code begin {1}
                ivjLblResponsibility.setText(ResourceManager.getResource(ServiceDetailView.class, "LblResponsibility_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblResponsibility;
    }

    /**
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblRole() {
        if (ivjLblRole == null) {
            try {
                ivjLblRole = new javax.swing.JLabel();
                ivjLblRole.setName("LblRole");
                ivjLblRole.setToolTipText("Siehe Personalkosten");
                ivjLblRole.setText("Rolle:");
                ivjLblRole.setBounds(11, 80, 150, 14);
                // user code begin {1}
                ivjLblRole.setText(ResourceManager.getResourceAsLabel(RoleDetailView.class, "FrmWindow_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblRole;
    }

    /**
     * Return the LblSite property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblSite() {
        if (ivjLblSite == null) {
            try {
                ivjLblSite = new javax.swing.JLabel();
                ivjLblSite.setName("LblSite");
                ivjLblSite.setText("Standort:");
                ivjLblSite.setBounds(10, 86, 148, 14);
                // user code begin {1}
                ivjLblSite.setText(ResourceManager.getResource(CostDriverDetailView.class, "LblSite_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblSite;
    }

    /**
     * Return the PnlActivity property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlActivity() {
        if (ivjPnlActivity == null) {
            try {
                ivjPnlActivity = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlActivity.setName("PnlActivity");
                ivjPnlActivity.setBounds(166, 106, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlActivity;
    }

    /**
     * Return the PnlCatalogue property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlCatalogue() {
        if (ivjPnlCatalogue == null) {
            try {
                ivjPnlCatalogue = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlCatalogue.setName("PnlCatalogue");
                ivjPnlCatalogue.setBounds(166, 48, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCatalogue;
    }

    /**
     * Return the PnlCategory property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlCategory() {
        if (ivjPnlCategory == null) {
            try {
                ivjPnlCategory = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlCategory.setName("PnlCategory");
                ivjPnlCategory.setBounds(164, 24, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCategory;
    }

    /**
     * Return the PnlCodes property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCodes() {
        if (ivjPnlCodes == null) {
            try {
                ivjPnlCodes = new javax.swing.JPanel();
                ivjPnlCodes.setName("PnlCodes");
                ivjPnlCodes.setLayout(null);
                getPnlCodes().add(getPnlService(), getPnlService().getName());
                getPnlCodes().add(getPnlCostDriver(), getPnlCostDriver().getName());
                getPnlCodes().add(getPnlCost(), getPnlCost().getName());
                getPnlCodes().add(getBtnExport(), getBtnExport().getName());
                getPnlCodes().add(getBtnImport(), getBtnImport().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCodes;
    }

    /**
     * Return the PnlCostDriver1 property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCost() {
        if (ivjPnlCost == null) {
            try {
                ivjPnlCost = new javax.swing.JPanel();
                ivjPnlCost.setName("PnlCost");
                ivjPnlCost.setLayout(null);
                ivjPnlCost.setBounds(7, 312, 582, 150);
                getPnlCost().add(getLblRole(), getLblRole().getName());
                getPnlCost().add(getPnlRole(), getPnlRole().getName());
                getPnlCost().add(getLblActivity(), getLblActivity().getName());
                getPnlCost().add(getPnlActivity(), getPnlActivity().getName());
                getPnlCost().add(getLblCatalogue(), getLblCatalogue().getName());
                getPnlCost().add(getPnlCatalogue(), getPnlCatalogue().getName());
                getPnlCost().add(getLblCostCause(), getLblCostCause().getName());
                getPnlCost().add(getPnlCostCause(), getPnlCostCause().getName());
                // user code begin {1}
                getPnlCost().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createTitledBorder(ResourceManager.getResource(ServiceDetailView.class, "PnlCost_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCost;
    }

    /**
     * Return the PnlCostCause property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlCostCause() {
        if (ivjPnlCostCause == null) {
            try {
                ivjPnlCostCause = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlCostCause.setName("PnlCostCause");
                ivjPnlCostCause.setBounds(166, 18, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCostCause;
    }

    /**
     * Return the PnlCostCentre property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlCostCentre() {
        if (ivjPnlCostCentre == null) {
            try {
                ivjPnlCostCentre = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlCostCentre.setName("PnlCostCentre");
                ivjPnlCostCentre.setBounds(164, 81, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCostCentre;
    }

    /**
     * Return the PnlCostDriver property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlCostDriver() {
        if (ivjPnlCostDriver == null) {
            try {
                ivjPnlCostDriver = new javax.swing.JPanel();
                ivjPnlCostDriver.setName("PnlCostDriver");
                ivjPnlCostDriver.setLayout(null);
                ivjPnlCostDriver.setBounds(6, 175, 582, 117);
                getPnlCostDriver().add(getLblPhase(), getLblPhase().getName());
                getPnlCostDriver().add(getLblProcess(), getLblProcess().getName());
                getPnlCostDriver().add(getLblSite(), getLblSite().getName());
                getPnlCostDriver().add(getPnlProcess(), getPnlProcess().getName());
                getPnlCostDriver().add(getPnlPhase(), getPnlPhase().getName());
                getPnlCostDriver().add(getPnlSite(), getPnlSite().getName());
                // user code begin {1}
                getPnlCostDriver().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createTitledBorder(ResourceManager.getResource(ServiceDetailView.class, "PnlCostDriver_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlCostDriver;
    }

    /**
     * Return the PnlPhase property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlPhase() {
        if (ivjPnlPhase == null) {
            try {
                ivjPnlPhase = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlPhase.setName("PnlPhase");
                ivjPnlPhase.setBounds(164, 52, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlPhase;
    }

    /**
     * Return the PnlProcess property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlProcess() {
        if (ivjPnlProcess == null) {
            try {
                ivjPnlProcess = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlProcess.setName("PnlProcess");
                ivjPnlProcess.setBounds(164, 22, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlProcess;
    }

    /**
     * Return the PnlResponsibility property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlResponsibility() {
        if (ivjPnlResponsibility == null) {
            try {
                ivjPnlResponsibility = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlResponsibility.setName("PnlResponsibility");
                ivjPnlResponsibility.setBounds(164, 54, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlResponsibility;
    }

    /**
     * Return the PnlRole property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlRole() {
        if (ivjPnlRole == null) {
            try {
                ivjPnlRole = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlRole.setName("PnlRole");
                ivjPnlRole.setBounds(166, 76, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlRole;
    }

    /**
     * Return the PnlService property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getPnlService() {
        if (ivjPnlService == null) {
            try {
                ivjPnlService = new javax.swing.JPanel();
                ivjPnlService.setName("PnlService");
                ivjPnlService.setLayout(null);
                ivjPnlService.setBounds(5, 34, 582, 122);
                getPnlService().add(getLblCostCentre(), getLblCostCentre().getName());
                getPnlService().add(getLblCategory(), getLblCategory().getName());
                getPnlService().add(getLblResponsibility(), getLblResponsibility().getName());
                getPnlService().add(getPnlCategory(), getPnlCategory().getName());
                getPnlService().add(getPnlResponsibility(), getPnlResponsibility().getName());
                getPnlService().add(getPnlCostCentre(), getPnlCostCentre().getName());
                // user code begin {1}
                getPnlService().setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createTitledBorder(ResourceManager.getResource(ServiceDetailView.class, "FrmWindow_text")),
                        javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlService;
    }

    /**
     * Return the PnlSite property value.
     *
     * @return ch.softenvironment.jomm.mvc.view.DbCodeManageView
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.jomm.mvc.view.DbCodeManageView getPnlSite() {
        if (ivjPnlSite == null) {
            try {
                ivjPnlSite = new ch.softenvironment.jomm.mvc.view.DbCodeManageView();
                ivjPnlSite.setName("PnlSite");
                ivjPnlSite.setBounds(164, 81, 404, 25);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPnlSite;
    }

    /**
     * Return the StbStatusVar property value.
     *
     * @return ch.softenvironment.view.StatusBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private ch.softenvironment.view.StatusBar getStbStatusVar() {
        if (ivjStbStatusVar == null) {
            try {
                ivjStbStatusVar = new ch.softenvironment.view.StatusBar();
                ivjStbStatusVar.setName("StbStatusVar");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjStbStatusVar;
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
    @Override
    protected void handleException(java.lang.Throwable exception) {
        super.handleException(exception);
    }

    /**
     * Import codes from a file. Keep DbEnumeration's and Model-Objects.
     *
     * @deprecated (this function is no help so far : update / extend codes acc. to IliCode would be nice)
     */
    private void importCodes() {
        try {
            if (BaseDialog.showConfirm(this, getResourceString("CWImportCodes_title"), getResourceString("CWImportCodes_text"))) {
                FileChooser openDialog = new FileChooser(LauncherView.getInstance().getSettings().getWorkingDirectory());
                openDialog.setDialogTitle(CommonUserAccess.getTitleFileOpen());
                openDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());
                if (openDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    // TODO Nasty removes given configuration except root to
                    // prevent TID-duplication and Code-Reference problems!
                    // COMPARE IliCode where possible instead
                    ((TcoModel) LauncherView.getInstance().getUtility().getRoot()).setOwnedElement(new ArrayList<TcoPackage>());

                    // 1) remove all codes
                    Iterator<Class<? extends DbCodeType>> it = ((XmlObjectServer) server).getCodeTypes().iterator();
                    while (it.hasNext()) {
                        Class codeType = it.next();
                        Iterator codes = server.retrieveCodes(codeType).iterator();
                        while (codes.hasNext()) {
                            server.removeCode((DbCode) codes.next());
                        }
                    }

                    // 2) import new code-set
                    server.setUserObject(new ch.softenvironment.jomm.target.xml.XmlUserObject(openDialog.getSelectedFile().getAbsolutePath()));
                    // TODO HACK does not solve TID-Problem in case enumeration
                    // ID's match with imported Code-Id's
                    List<ModelBasket> baskets = ListUtils.createList(new ModelBasket(server));
                    // baskets.add(new PresentationBasket());
                    server.retrieveAll(baskets);
                    // baskets.set(0, /*(TcoModel)*/
                    // ((IliBasket)baskets.get(0)).getModelRoot());
                    // baskets.set(1, /*(Diagram)*/
                    // ((IliBasket)baskets.get(1)).getModelRoot());
                    server.setUserObject(null);
                    // return baskets;

                    // TODO HACK: rebuild default tree to fix equal TID problems
                    // (Codes <--> Model)
                    ModelUtility.createDefaultTree(LauncherView.getInstance().getUtility());

                    // 3) refresh Codes in this view
                    refreshCodes();

                    LauncherView.getInstance().setModelChanged(true);
                }
            }
        } catch (Exception e) {
            handleException(e);
            // return null;
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
        getTlbToolbar().addToolBarListener(ivjEventHandler);
        getBtnImport().addActionListener(ivjEventHandler);
        getBtnExport().addActionListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            refreshCodes();
            // user code end
            setName("CodeDetailView");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(602, 597);
            setTitle("Codes verwalten (global)");
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(getResourceString("FrmWindow_text"));
        UserActionRights rights = getViewOptions().getViewManager().getRights(ProjectPhase.class);
        if (!rights.isSaveObjectAllowed()) {
            getLblPhase().setVisible(false);
            getPnlPhase().setVisible(false);
        }
        // TODO Patch: allow saving of DbChangeableCodeView changes
        rights = getViewOptions().getViewManager().getRights(TcoModel.class);
        getTlbToolbar().setTbbSaveEnabled(rights.isSaveObjectAllowed());
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

    private void refreshCodes() throws Exception {
        getPnlCategory().setCode(server, org.tcotool.model.ServiceCategory.class, ModelUtility.getTypeString(ServiceCategory.class) /*
             * ResourceManager
             * .
             * getResource
             * (
             * ServiceDetailView
             * .
             * class
             * ,
             * "LblCategory_text"
             * ,
             * false
             * )
             */,
            DbObject.PROPERTY_NAME, null, getViewOptions());
        getPnlResponsibility().setCode(server, org.tcotool.model.Responsibility.class, ModelUtility.getTypeString(Responsibility.class) /*
             * ResourceManager
             * .
             * getResource
             * (
             * ServiceDetailView
             * .
             * class
             * ,
             * "LblResponsibility_text"
             * ,
             * false
             * )
             */,
            DbObject.PROPERTY_NAME, null, getViewOptions());
        getPnlCostCentre().setCode(server, org.tcotool.model.CostCentre.class, ModelUtility.getTypeString(CostCentre.class) /*
             * ResourceManager
             * .
             * getResource
             * (
             * ServiceDetailView
             * .
             * class
             * ,
             * "LblCostCentre_text"
             * ,
             * false
             * )
             */, DbObject.PROPERTY_NAME,
            CostCentreDetailView.class, getViewOptions());

        getPnlPhase().setCode(server, ProjectPhase.class, ModelUtility.getTypeString(ProjectPhase.class) /*
         * ResourceManager
         * .
         * getResource
         * (
         * CostDriverDetailView
         * .
         * class
         * ,
         * "LblPhase_text"
         * ,
         * false
         * )
         */, DbObject.PROPERTY_NAME, null, getViewOptions());
        getPnlSite().setCode(server, Site.class, ModelUtility.getTypeString(Site.class) /*
         * ResourceManager
         * .
         * getResource
         * (
         * CostDriverDetailView
         * .
         * class
         * ,
         * "LblSite_text"
         * ,
         * false
         * )
         */, DbObject.PROPERTY_NAME, SiteDetailView.class, getViewOptions());
        getPnlProcess().setCode(server, org.tcotool.model.Process.class,
            ResourceManager.getResourceAsNonLabeled(CostDriverDetailView.class, "LblProcess_text"), DbObject.PROPERTY_NAME, null, getViewOptions());

        getPnlCostCause().setCode(server, org.tcotool.model.CostCause.class, ModelUtility.getTypeString(CostCause.class), "nameString",
            CostCauseDetailView.class, getViewOptions());
        getPnlCatalogue().setCode(server, org.tcotool.model.Catalogue.class, ModelUtility.getTypeString(Catalogue.class), DbObject.PROPERTY_NAME,
            CatalogueDetailView.class, getViewOptions());
        getPnlRole().setCode(server, org.tcotool.model.Role.class, ModelUtility.getTypeString(Role.class), DbObject.PROPERTY_NAME, RoleDetailView.class,
            getViewOptions());
        getPnlActivity().setCode(server, org.tcotool.model.Activity.class, ModelUtility.getTypeString(Activity.class), DbObject.PROPERTY_NAME, null,
            getViewOptions());
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

    /**
     * A SearchView usually offers a set of Query-Fields to make the searching of objects more accurate and performant. Therefore a reset of all SearchArguments may be initialized here.
     */
    @Override
    public void resetSearchArguments() {
    }

    /**
     * Save an Object represented by DetailView.
     */
    public void saveObject() {
        try {
            LauncherView.getInstance().saveObject();
            // closeOnSave();
            dispose();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.view.SearchView#searchObjects()
     */
    @Override
    public void searchObjects() {
    }
}
