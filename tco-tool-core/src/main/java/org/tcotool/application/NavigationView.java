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
import ch.softenvironment.client.UserActionRights;
import ch.softenvironment.client.ViewManager;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.CommonUserAccess;
import ch.softenvironment.view.ToolBar;
import ch.softenvironment.view.WaitDialog;
import ch.softenvironment.view.tree.AutoScrollingTree;
import ch.softenvironment.view.tree.NavigationTreeCellEditor;
import ch.softenvironment.view.tree.NavigationTreeCellRenderer;
import ch.softenvironment.view.tree.NavigationTreeModel;
import ch.softenvironment.view.tree.TreeSelectionDialog;
import java.awt.Component;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.pluginsupport.Menu;
import org.tcotool.standard.report.ReportInvestment;
import org.tcotool.standard.report.ReportTco;
import org.tcotool.tools.ModelCopyTool;
import org.tcotool.tools.ModelUtility;

/**
 * Panel to represent a Model as a Tree.
 *
 * @author Peter Hirzel
 */

public class NavigationView extends ch.softenvironment.view.BasePanel implements ch.softenvironment.view.ListMenuChoice {

    private JMenuItem mniReportInvestment = null;
    private final List<JComponent> pluginExtensions = new ArrayList<JComponent>();
    private NavigationTreeModel modelAdapter = null;
    private ch.softenvironment.view.ViewOptions viewOptions = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JSeparator ivjJSeparator6 = null;
    private javax.swing.JPopupMenu ivjMnpTreeActions = null;
    private javax.swing.JTree ivjTreNavigation = null;
    private javax.swing.JScrollPane ivjScpNavigation = null;
    private javax.swing.JMenuItem ivjMniGroup = null;
    private javax.swing.JSeparator ivjJSeparator2 = null;
    private javax.swing.JMenuItem ivjMniService = null;
    private javax.swing.JSeparator ivjJSeparator3 = null;
    private javax.swing.JMenuItem ivjMniMove = null;
    private javax.swing.JMenuItem ivjMniEditChangeWindow = null;
    private javax.swing.JMenuItem ivjMniEditCopy = null;
    private javax.swing.JMenuItem ivjMniEditRemove = null;
    private javax.swing.JMenuItem ivjMniEditRename = null;
    private javax.swing.JMenuItem ivjMniExportConfiguration = null;
    private javax.swing.JMenuItem ivjMniImportConfiguration = null;
    private javax.swing.JMenuItem ivjMniReportBlockTcoDetailed = null;
    private javax.swing.JMenuItem ivjMniReportBlockTcoTotal = null;
    private javax.swing.JMenuItem ivjMniMorphToService = null;
    private javax.swing.JMenu ivjMnuReportFinance = null;
    private javax.swing.JMenu ivjMnuReportTco = null;
    private javax.swing.JMenu ivjMnuReports = null;
    private javax.swing.JSeparator ivjSepImportExport = null;

    class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.MouseListener, javax.swing.event.TreeSelectionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == NavigationView.this.getMniEditChangeWindow()) {
                connEtoC2(e);
            }
            if (e.getSource() == NavigationView.this.getMniEditRename()) {
                connEtoC4(e);
            }
            if (e.getSource() == NavigationView.this.getMniGroup()) {
                connEtoC5(e);
            }
            if (e.getSource() == NavigationView.this.getMniEditRemove()) {
                connEtoC6(e);
            }
            if (e.getSource() == NavigationView.this.getMniService()) {
                connEtoC14(e);
            }
            if (e.getSource() == NavigationView.this.getMniReportBlockTcoDetailed()) {
                connEtoC3(e);
            }
            if (e.getSource() == NavigationView.this.getMniMove()) {
                connEtoC7(e);
            }
            if (e.getSource() == NavigationView.this.getMniEditCopy()) {
                connEtoC9(e);
            }
            if (e.getSource() == NavigationView.this.getMniImportConfiguration()) {
                connEtoC10(e);
            }
            if (e.getSource() == NavigationView.this.getMniExportConfiguration()) {
                connEtoC11(e);
            }
            if (e.getSource() == NavigationView.this.getMniReportBlockTcoTotal()) {
                connEtoC12(e);
            }
            if (e.getSource() == NavigationView.this.getMniMorphToService()) {
                connEtoC17(e);
            }
            if (e.getSource() == NavigationView.this.getMniCostDriver()) {
                connEtoC15(e);
            }
            if (e.getSource() == NavigationView.this.getMniPersonalCost()) {
                connEtoC16(e);
            }
            if (e.getSource() == NavigationView.this.getMniFactCost()) {
                connEtoC18(e);
            }
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            if (e.getSource() == NavigationView.this.getTreNavigation()) {
                connEtoC30(e);
            }
            if (e.getSource() == NavigationView.this.getTreNavigation()) {
                connEtoC13(e);
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.getSource() == NavigationView.this.getTreNavigation()) {
                connEtoC1(e);
            }
        }

        @Override
        public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
            if (e.getSource() == NavigationView.this.getTreNavigation()) {
                connEtoC8(e);
            }
        }

    }

    private javax.swing.JMenuItem ivjMniCostDriver = null;
    private javax.swing.JMenuItem ivjMniFactCost = null;
    private javax.swing.JMenuItem ivjMniPersonalCost = null;
    private javax.swing.JMenu ivjMnuFileNew = null;

    /**
     * NavTree constructor comment.
     */
    public NavigationView() {
        super();
        initialize();
    }

    @Override
    public void adaptUserAction(EventObject event, Object control) {
        // ModelUtility utility =
        // (ModelUtility)((NavigationTreeModel)modelAdapter.getUtility();
        Object node = getSelectedNode();
        if (node == null) {
            getMnuFileNew().setEnabled(false);
            getMniEditCopy().setEnabled(false);
            getMniEditChangeWindow().setEnabled(false);
            getMniEditRemove().setEnabled(false);
            getMniEditRename().setEnabled(false);

            getMniMove().setEnabled(false);
            getMniMorphToService().setEnabled(false);

            getMniImportConfiguration().setEnabled(false);
            getMniExportConfiguration().setEnabled(false);
            return;
        } else {
            boolean isPackage = node instanceof TcoPackage;
            boolean isService = node instanceof Service;
            UserActionRights rights = viewOptions.getViewManager().getRights(node.getClass());
            ViewManager mgr = viewOptions.getViewManager();

            getMnuFileNew().setEnabled(false);
            if (isPackage) {
                getMnuFileNew().setEnabled(true);
                getMniGroup().setVisible(true);
                getMniService().setVisible(true);
                getMniCostDriver().setVisible(false);
                getMniPersonalCost().setVisible(false);
                getMniFactCost().setVisible(false);
                getMniService().setIcon(LauncherView.getInstance().getUtility().getIcon(Service.class, false));
                getMniGroup().setEnabled(rights.isNewObjectAllowed());
                getMniService().setEnabled((!node.equals(modelAdapter.getUtility().getRoot())) && mgr.getRights(Service.class).isNewObjectAllowed());
            } else if (isService) {
                getMnuFileNew().setEnabled(true);
                getMniGroup().setVisible(false);
                getMniService().setVisible(false);
                getMniCostDriver().setVisible(true);
                getMniCostDriver().setIcon(LauncherView.getInstance().getUtility().getIcon(CostDriver.class, false));
                getMniPersonalCost().setVisible(false);
                getMniFactCost().setVisible(false);
                getMniCostDriver().setEnabled(mgr.getRights(CostDriver.class).isNewObjectAllowed());
            } else if (node instanceof CostDriver) {
                getMnuFileNew().setEnabled(true);
                getMniGroup().setVisible(false);
                getMniService().setVisible(false);
                getMniCostDriver().setVisible(false);
                getMniPersonalCost().setVisible(true);
                getMniPersonalCost().setIcon(LauncherView.getInstance().getUtility().getIcon(PersonalCost.class, false));
                getMniPersonalCost().setEnabled(mgr.getRights(PersonalCost.class).isNewObjectAllowed());
                getMniFactCost().setVisible(true);
                getMniFactCost().setIcon(LauncherView.getInstance().getUtility().getIcon(FactCost.class, false));
                getMniFactCost().setEnabled(mgr.getRights(FactCost.class).isNewObjectAllowed());
            }

            // treat "Main-Menu"
            getMniEditCopy().setEnabled(
                ((isPackage && !(node instanceof TcoModel)) || isService || (node instanceof CostDriver) || (node instanceof Cost))
                    && rights.isSaveObjectAllowed());
            getMniEditChangeWindow().setEnabled(rights.isReadObjectAllowed());
            getMniEditRemove().setEnabled(modelAdapter.getUtility().isNodeRemovable(node) && rights.isRemoveObjectsAllowed());
            getMniEditRename().setEnabled(modelAdapter.getUtility().isNodeEditable(node) && rights.isSaveObjectAllowed());

            getMniMove().setEnabled(modelAdapter.getUtility().isNodeMovable(node) && rights.isSaveObjectAllowed());
            getMniMorphToService().setEnabled(isPackage && (!node.equals(modelAdapter.getUtility().getRoot())) && rights.isSaveObjectAllowed());

            getMniImportConfiguration().setEnabled(isPackage && rights.isSaveObjectAllowed());
            getMniExportConfiguration().setEnabled(isPackage && (!modelAdapter.getUtility().getRoot().equals(node)) && rights.isSaveObjectAllowed());

            adaptReports(getMnuReportTco().getMenuComponents(), isService, isPackage);
            adaptReports(getMnuReportFinance().getMenuComponents(), isService, isPackage);
            // getMniReportBlockTcoTotal().setEnabled(isPackage);
            // getMniReportBlockTcoDetailed().setEnabled(isService ||
            // isPackage);
            // getMniReportInvestment().setEnabled(isPackage);

            // LauncherView.getInstance().adaptSelection(node);
        }
    }

    /**
     * Adapt Plugin-Reports.
     *
     * @param items
     * @param isService
     * @param isPackage
     * @see org.tcotool.pluginsupport.Menu
     */
    private void adaptReports(Component[] items, boolean isService, boolean isPackage) {
        for (int i = 0; i < items.length; i++) {
            JComponent comp = (JComponent) items[i];
            // @see ApplicationPlugin#createMenuItem()
            Boolean isGroup = (Boolean) comp.getClientProperty(org.tcotool.pluginsupport.Menu.GROUP_SPECIFIC);
            if (isGroup == null) {
                // report is available for Group or Package
                comp.setEnabled(isService || isPackage);
            } else if (isGroup.booleanValue()) {
                // only TcoPackage specific
                comp.setEnabled(isPackage);
            } else {
                // only Service specific
                comp.setEnabled(isService);
            }
        }
    }

    /**
     * Add ModelElement to Model.
     */
    private TcoObject addElement(java.lang.Class<? extends TcoObject> elementClass) {
        TcoObject object = null;
        try {
            Object node = getSelectedNode();
            if (node != null) {
                object = ((ModelUtility) modelAdapter.getUtility()).createTcoObject(((DbObject) node).getObjectServer(), elementClass);
                LauncherView.getInstance().getUtility().addOwnedElement(node, object);
                // ((NavigationTreeModel)getTreNavigation().getModel()).fireTreeNodesInserted();
                register(object, true);
                /*
                 * // HACK: List is the same but size has grown if (elementClass.equals(Service.class)) { ((ch.softenvironment.jomm
                 * .DbChangeableBean)node).firePropertyChange("service", null, ((TcoPackage)node).getService()); } else if
                 * (elementClass.equals(TcoPackage.class)) { ((ch.softenvironment .jomm.DbChangeableBean)node).firePropertyChange ("ownedElement", null,
                 * ((TcoPackage)node).getOwnedElement()); }
                 */
            }
        } catch (Exception e) {
            handleException(e);
        }

        return object;
    }

    /**
     * Do anything at DoubleClick-Event for e.g. open selected Object(s) in a JTable.
     *
     * @see ch.softenvironment.view.BaseFrame#genericPopupDisplay(MouseEvent, JPopupMenu)
     */
    @Override
    public void changeObjects(java.lang.Object source) {
        try {
            // if (source.equals(getMniEditChangeWindow())) {
            Object treeNode = getSelectedNode();
            if (treeNode != null) {
                java.util.List list = new java.util.ArrayList();
                list.add(treeNode);

                if (!viewOptions.getViewManager().activateView(list)) {
                    // create new View
                    ch.softenvironment.view.BaseFrame view = null;
                    if (treeNode instanceof TcoModel) {
                        view = new ModelDetailView(viewOptions, list);
                    } else if (treeNode instanceof TcoPackage) {
                        view = new PackageDetailView(viewOptions, list);
                    } else if (treeNode instanceof Service) {
                        view = new ServiceDetailView(viewOptions, list);
                    } else if (treeNode instanceof CostDriver) {
                        view = new CostDriverDetailView(viewOptions, list, null);
                    } else if (treeNode instanceof FactCost) {
                        view = new FactCostDetailView(viewOptions, list, null);
                    } else if (treeNode instanceof PersonalCost) {
                        view = new PersonalCostDetailView(viewOptions, list, null);
                    }
                    view.setRelativeLocation(LauncherView.getInstance());
                    view.setVisible(true);
                }
            }
            // }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * connEtoC1: (TreNavigation.mouse.mouseReleased(java.awt.event.MouseEvent) --> NavigationView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax. swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpTreeActions());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC10: (MniImportConfiguration.action.actionPerformed(java.awt.event. ActionEvent) --> NavigationView.mniImportConfiguration()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC10(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniImportConfiguration();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC11: (MniExportConfiguration.action.actionPerformed(java.awt.event. ActionEvent) --> NavigationView.mniExportConfiguration()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC11(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniExportConfiguration();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC12: (MniReportBlockTotal.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.mniReportBlockTotal()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC12(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniReportBlockTotal();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC13: (TreNavigation.mouse.mousePressed(java.awt.event.MouseEvent) --> NavigationView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax. swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC13(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpTreeActions());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC14: (MniService.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.mniAddService()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC14(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniService());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC15: (MniCostDriver.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC15(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniCostDriver());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC16: (MniPersonalCost.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC16(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniPersonalCost());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC17: (MniMorphToService.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.morphToService()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC17(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.morphToService();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC18: (MniFactCost.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.newObject(Ljava.lang.Object;)V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC18(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniFactCost());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (MniOpenSpecification.action.actionPerformed(java.awt.event.ActionEvent) --> NavTree.openSpecification()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.changeObjects(getMniEditChangeWindow());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3: (MniCostReport.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.mniCostblockReport()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.mniReportBlockDetailed();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC30: (TreNavigation.mouse.mousePressed(java.awt.event.MouseEvent) --> NavigationView.genericPopupDisplay(Ljava.awt.event.MouseEvent;Ljavax. swing.JPopupMenu;)V)
     *
     * @param arg1 java.awt.event.MouseEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC30(java.awt.event.MouseEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.genericPopupDisplay(arg1, getMnpTreeActions());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC4: (MniRename.action.actionPerformed(java.awt.event.ActionEvent) --> NavTree.renameNode()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.renameObject();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5: (MniGroup.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.mniAddGroup()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.newObject(getMniGroup());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC6: (MniRemove.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.mniRemove()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.removeObjects(getMniEditRemove());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC7: (MniMove.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.mniMove()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC7(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.moveObject();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC8: (TreNavigation.treeSelection.valueChanged(javax.swing.event. TreeSelectionEvent) --> NavigationView.selectionChanged(Ljavax.swing.event.TreeSelectionEvent;)V)
     *
     * @param arg1 javax.swing.event.TreeSelectionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(javax.swing.event.TreeSelectionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.selectionChanged(arg1);
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC9: (MniCopy.action.actionPerformed(java.awt.event.ActionEvent) --> NavigationView.mniCopy()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC9(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.copyObject(getMniEditCopy());
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * Create a "copy" of the selected element and add it parallelly.
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void copyObject(java.lang.Object source) {
        try {
            TcoObject original = (TcoObject) getSelectedNode();

            if ((original instanceof TcoPackage) || (original instanceof Service)) {
                if (!BaseDialog.showConfirm(LauncherView.getInstance(), CommonUserAccess.getMniEditCopyText(), getResourceString("CICopyDependency"))) {
                    return;
                }
            }

            ModelUtility utility = LauncherView.getInstance().getUtility();
            TcoObject copy = ModelCopyTool.copy(utility, original);
            utility.addOwnedElement(utility.findParent(original), copy);
            register(copy, true);

            LauncherView.getInstance().setModelChanged(true);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public JMenu findMenu(final String name) {
        // TODO Hardcoded
        if (name.equals("MnuReportTco")) {
            return getMnuReportTco();
        } else if (name.equals("MnuReportFinance")) {
            return getMnuReportFinance();
        }
        return null;
    }

    /**
     * Return the JSeparator2 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator2() {
        if (ivjJSeparator2 == null) {
            try {
                ivjJSeparator2 = new javax.swing.JSeparator();
                ivjJSeparator2.setName("JSeparator2");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSeparator2;
    }

    /**
     * Return the JSeparator3 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator3() {
        if (ivjJSeparator3 == null) {
            try {
                ivjJSeparator3 = new javax.swing.JSeparator();
                ivjJSeparator3.setName("JSeparator3");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSeparator3;
    }

    /**
     * Return the JSeparator6 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator6() {
        if (ivjJSeparator6 == null) {
            try {
                ivjJSeparator6 = new javax.swing.JSeparator();
                ivjJSeparator6.setName("JSeparator6");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSeparator6;
    }

    /**
     * Return the MniCostDriver property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniCostDriver() {
        if (ivjMniCostDriver == null) {
            try {
                ivjMniCostDriver = new javax.swing.JMenuItem();
                ivjMniCostDriver.setName("MniCostDriver");
                ivjMniCostDriver.setText("CostDriver...");
                // user code begin {1}
                ivjMniCostDriver.setText(ResourceManager.getResource(CostDriverDetailView.class, "FrmWindow_text") + "...");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniCostDriver;
    }

    /**
     * Return the MniOpen property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniEditChangeWindow() {
        if (ivjMniEditChangeWindow == null) {
            try {
                ivjMniEditChangeWindow = new javax.swing.JMenuItem();
                ivjMniEditChangeWindow.setName("MniEditChangeWindow");
                ivjMniEditChangeWindow.setText("Oeffnen...");
                // user code begin {1}
                ivjMniEditChangeWindow.setText(CommonUserAccess.getMniEditChangeWindowText());
                ivjMniEditChangeWindow.setIcon(ResourceBundle.getImageIcon(ToolBar.class, "open.gif"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniEditChangeWindow;
    }

    /**
     * Return the MniCopy property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniEditCopy() {
        if (ivjMniEditCopy == null) {
            try {
                ivjMniEditCopy = new javax.swing.JMenuItem();
                ivjMniEditCopy.setName("MniEditCopy");
                ivjMniEditCopy.setText("Kopieren...");
                ivjMniEditCopy.setEnabled(false);
                // user code begin {1}
                ivjMniEditCopy.setText(CommonUserAccess.getMniEditCopyText());
                ivjMniEditCopy.setIcon(CommonUserAccess.getIconCopy());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniEditCopy;
    }

    /**
     * Return the MniRemoveNode property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniEditRemove() {
        if (ivjMniEditRemove == null) {
            try {
                ivjMniEditRemove = new javax.swing.JMenuItem();
                ivjMniEditRemove.setName("MniEditRemove");
                ivjMniEditRemove.setText("Loeschen");
                // user code begin {1}
                ivjMniEditRemove.setText(CommonUserAccess.getMniEditRemoveText());
                ivjMniEditRemove.setIcon(CommonUserAccess.getIconRemove());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniEditRemove;
    }

    /**
     * Return the MniRename property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniEditRename() {
        if (ivjMniEditRename == null) {
            try {
                ivjMniEditRename = new javax.swing.JMenuItem();
                ivjMniEditRename.setName("MniEditRename");
                ivjMniEditRename.setText("Umbenennen");
                ivjMniEditRename.setEnabled(true);
                // user code begin {1}
                ivjMniEditRename.setText(CommonUserAccess.getMniEditRenameText());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniEditRename;
    }

    /**
     * Return the MniCostReport11 property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniExportConfiguration() {
        if (ivjMniExportConfiguration == null) {
            try {
                ivjMniExportConfiguration = new javax.swing.JMenuItem();
                ivjMniExportConfiguration.setName("MniExportConfiguration");
                ivjMniExportConfiguration.setToolTipText("Exportiert eine Gruppe als eigenstaendige TCO-Konfiguraiton");
                ivjMniExportConfiguration.setText("Konfiguration exportieren...");
                ivjMniExportConfiguration.setEnabled(false);
                // user code begin {1}
                // TODO NLS
                ivjMniExportConfiguration.setText("Gruppe exportieren...");
                ivjMniExportConfiguration.setToolTipText("Exportiert eine Gruppe als eigenstaendige TCO-Konfiguration");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniExportConfiguration;
    }

    /**
     * Return the MniFactCost property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniFactCost() {
        if (ivjMniFactCost == null) {
            try {
                ivjMniFactCost = new javax.swing.JMenuItem();
                ivjMniFactCost.setName("MniFactCost");
                ivjMniFactCost.setText("FactCost...");
                // user code begin {1}
                ivjMniFactCost.setText(ResourceManager.getResource(FactCostDetailView.class, "FrmWindow_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniFactCost;
    }

    /**
     * Return the MniUmlPackage property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniGroup() {
        if (ivjMniGroup == null) {
            try {
                ivjMniGroup = new javax.swing.JMenuItem();
                ivjMniGroup.setName("MniGroup");
                ivjMniGroup.setText("Gruppe...");
                // user code begin {1}
                ivjMniGroup.setText(ResourceManager.getResource(PackageDetailView.class, "FrmWindow_text") + "...");
                ivjMniGroup.setIcon(ResourceBundle.getImageIcon(ModelUtility.class, "TcoPackage.png")); // special
                // case
                // in
                // ModelUtility.getIcon()
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniGroup;
    }

    /**
     * Return the MniCostReport1 property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniImportConfiguration() {
        if (ivjMniImportConfiguration == null) {
            try {
                ivjMniImportConfiguration = new javax.swing.JMenuItem();
                ivjMniImportConfiguration.setName("MniImportConfiguration");
                ivjMniImportConfiguration.setToolTipText("Importiert externe TCO-Konfiguraiton");
                ivjMniImportConfiguration.setText("Konfiguration importieren...");
                ivjMniImportConfiguration.setEnabled(false);
                // user code begin {1}
                // TODO NLS
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniImportConfiguration;
    }

    /**
     * Return the MniMorphToService property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniMorphToService() {
        if (ivjMniMorphToService == null) {
            try {
                ivjMniMorphToService = new javax.swing.JMenuItem();
                ivjMniMorphToService.setName("MniMorphToService");
                ivjMniMorphToService.setText("Umwandeln in Dienst");
                // user code begin {1}
                // TODO NLS
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniMorphToService;
    }

    /**
     * Return the MniMove property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniMove() {
        if (ivjMniMove == null) {
            try {
                ivjMniMove = new javax.swing.JMenuItem();
                ivjMniMove.setName("MniMove");
                ivjMniMove.setText("Element verschieben...");
                // user code begin {1}
                ivjMniMove.setText(getResourceString("MniMove_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniMove;
    }

    /**
     * Return the MniPersonalCost property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniPersonalCost() {
        if (ivjMniPersonalCost == null) {
            try {
                ivjMniPersonalCost = new javax.swing.JMenuItem();
                ivjMniPersonalCost.setName("MniPersonalCost");
                ivjMniPersonalCost.setText("Personalcost...");
                // user code begin {1}
                ivjMniPersonalCost.setText(ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text") + "...");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniPersonalCost;
    }

    /**
     * Return the MniCostReport property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniReportBlockTcoDetailed() {
        if (ivjMniReportBlockTcoDetailed == null) {
            try {
                ivjMniReportBlockTcoDetailed = new javax.swing.JMenuItem();
                ivjMniReportBlockTcoDetailed.setName("MniReportBlockTcoDetailed");
                ivjMniReportBlockTcoDetailed.setToolTipText("Berechnet Kostenblock für selektiertes Element");
                ivjMniReportBlockTcoDetailed.setText("Kostenblock detailiert (TCO)");
                // user code begin {1}
                ivjMniReportBlockTcoDetailed.putClientProperty(Menu.GROUP_SPECIFIC, null);
                ivjMniReportBlockTcoDetailed.setToolTipText(getResourceString("CICostBlock"));
                ivjMniReportBlockTcoDetailed.setText(getResourceString("MniReportTcoCostDetailed_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniReportBlockTcoDetailed;
    }

    /**
     * Return the MniReportBlockTotal property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniReportBlockTcoTotal() {
        if (ivjMniReportBlockTcoTotal == null) {
            try {
                ivjMniReportBlockTcoTotal = new javax.swing.JMenuItem();
                ivjMniReportBlockTcoTotal.setName("MniReportBlockTcoTotal");
                ivjMniReportBlockTcoTotal.setToolTipText("Berechnet Kostenblock für selektiertes Element");
                ivjMniReportBlockTcoTotal.setText("Kostenblock gesamt (TCO)");
                // user code begin {1}
                ivjMniReportBlockTcoTotal.putClientProperty(Menu.GROUP_SPECIFIC, Boolean.TRUE);
                ivjMniReportBlockTcoTotal.setToolTipText(getResourceString("CICostBlock"));
                ivjMniReportBlockTcoTotal.setText(ResourceManager.getResource(LauncherView.class, "MniReportTotalTcoCost_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniReportBlockTcoTotal;
    }

    private JMenuItem getMniReportInvestment() {
        if (mniReportInvestment == null) {
            mniReportInvestment = new JMenuItem();
            mniReportInvestment.putClientProperty(Menu.GROUP_SPECIFIC, Boolean.TRUE);
            mniReportInvestment.setText(ResourceManager.getResource(LauncherView.class, "MniReportInvestment_text"));
            mniReportInvestment.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    reportInvestment();
                }
            });
            getMnuReportFinance().add(mniReportInvestment);
        }
        return mniReportInvestment;
    }

    /**
     * Return the MniService property value.
     *
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getMniService() {
        if (ivjMniService == null) {
            try {
                ivjMniService = new javax.swing.JMenuItem();
                ivjMniService.setName("MniService");
                ivjMniService.setText("Service...");
                // user code begin {1}
                ivjMniService.setText(ResourceManager.getResource(ServiceDetailView.class, "FrmWindow_text") + "...");
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMniService;
    }

    /**
     * Return the MnpTreeActions property value.
     *
     * @return javax.swing.JPopupMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPopupMenu getMnpTreeActions() {
        if (ivjMnpTreeActions == null) {
            try {
                ivjMnpTreeActions = new javax.swing.JPopupMenu();
                ivjMnpTreeActions.setName("MnpTreeActions");
                ivjMnpTreeActions.add(getMnuFileNew());
                ivjMnpTreeActions.add(getMniEditChangeWindow());
                ivjMnpTreeActions.add(getMniEditCopy());
                ivjMnpTreeActions.add(getJSeparator6());
                ivjMnpTreeActions.add(getMniEditRemove());
                ivjMnpTreeActions.add(getJSeparator2());
                ivjMnpTreeActions.add(getMniMove());
                ivjMnpTreeActions.add(getMniEditRename());
                ivjMnpTreeActions.add(getMniMorphToService());
                ivjMnpTreeActions.add(getJSeparator3());
                ivjMnpTreeActions.add(getMnuReports());
                ivjMnpTreeActions.add(getSepImportExport());
                ivjMnpTreeActions.add(getMniImportConfiguration());
                ivjMnpTreeActions.add(getMniExportConfiguration());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnpTreeActions;
    }

    /**
     * Return the MnuNewUmlDiagrams property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuFileNew() {
        if (ivjMnuFileNew == null) {
            try {
                ivjMnuFileNew = new javax.swing.JMenu();
                ivjMnuFileNew.setName("MnuFileNew");
                ivjMnuFileNew.setText("Neu");
                ivjMnuFileNew.add(getMniGroup());
                ivjMnuFileNew.add(getMniService());
                ivjMnuFileNew.add(getMniCostDriver());
                ivjMnuFileNew.add(getMniPersonalCost());
                ivjMnuFileNew.add(getMniFactCost());
                // user code begin {1}
                ivjMnuFileNew.setText(CommonUserAccess.getMniFileNewText());
                ivjMnuFileNew.setIcon(CommonUserAccess.getIconNew());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjMnuFileNew;
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
                // user code begin {1}
                ivjMnuReportFinance.setText(ResourceManager.getResource(LauncherView.class, "MnuReportFinance_text"));
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
     * Return the MniReports property value.
     *
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMnuReports() {
        if (ivjMnuReports == null) {
            try {
                ivjMnuReports = new javax.swing.JMenu();
                ivjMnuReports.setName("MnuReports");
                ivjMnuReports.setText("Berechnungen");
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
                ivjMnuReportTco.setText("TCO");
                ivjMnuReportTco.add(getMniReportBlockTcoTotal());
                ivjMnuReportTco.add(getMniReportBlockTcoDetailed());
                // user code begin {1}
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
     * Return the ScpNavigation property value.
     *
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getScpNavigation() {
        if (ivjScpNavigation == null) {
            try {
                ivjScpNavigation = new javax.swing.JScrollPane();
                ivjScpNavigation.setName("ScpNavigation");
                getScpNavigation().setViewportView(getTreNavigation());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScpNavigation;
    }

    /**
     * Returns the TreeNode instance that is selected in the tree. If nothing is selected, null is returned.
     */
    public Object getSelectedNode() {
        TreePath selPath = getTreNavigation().getSelectionPath();

        if (selPath != null) {
            return selPath.getLastPathComponent();
        }
        return null;
    }

    /**
     * Return the JSeparator1 property value.
     *
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getSepImportExport() {
        if (ivjSepImportExport == null) {
            try {
                ivjSepImportExport = new javax.swing.JSeparator();
                ivjSepImportExport.setName("SepImportExport");
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjSepImportExport;
    }

    /**
     * Return the TreNavigation property value.
     *
     * @return javax.swing.JTree
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    protected javax.swing.JTree getTreNavigation() {
        if (ivjTreNavigation == null) {
            try {
                ivjTreNavigation = new javax.swing.JTree();
                ivjTreNavigation.setName("TreNavigation");
                ivjTreNavigation.setVisibleRowCount(50);
                ivjTreNavigation.setBounds(0, 0, 78, 72);
                ivjTreNavigation.setEditable(true);
                ivjTreNavigation.setInvokesStopCellEditing(true);
                // user code begin {1}
                ivjTreNavigation = new AutoScrollingTree(DnDConstants.ACTION_MOVE);
                ivjTreNavigation.setName("TreNavigation");
                ivjTreNavigation.setVisibleRowCount(50);
                ivjTreNavigation.setBounds(0, 0, 78, 72);
                ivjTreNavigation.setEditable(true);
                ivjTreNavigation.setInvokesStopCellEditing(true);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTreNavigation;
    }

    /**
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    @Override
    protected void handleException(java.lang.Throwable exception) {
        LauncherView.getInstance().handleException(exception);
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
        getMniEditChangeWindow().addActionListener(ivjEventHandler);
        getMniEditRename().addActionListener(ivjEventHandler);
        getTreNavigation().addTreeSelectionListener(ivjEventHandler);
        getTreNavigation().addMouseListener(ivjEventHandler);
        getMniGroup().addActionListener(ivjEventHandler);
        getMniEditRemove().addActionListener(ivjEventHandler);
        getMniService().addActionListener(ivjEventHandler);
        getMniReportBlockTcoDetailed().addActionListener(ivjEventHandler);
        getMniMove().addActionListener(ivjEventHandler);
        getMniEditCopy().addActionListener(ivjEventHandler);
        getMniImportConfiguration().addActionListener(ivjEventHandler);
        getMniExportConfiguration().addActionListener(ivjEventHandler);
        getMniReportBlockTcoTotal().addActionListener(ivjEventHandler);
        getMniMorphToService().addActionListener(ivjEventHandler);
        getMniCostDriver().addActionListener(ivjEventHandler);
        getMniPersonalCost().addActionListener(ivjEventHandler);
        getMniFactCost().addActionListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("NavTree");
            setPreferredSize(new java.awt.Dimension(200, 300));
            setLayout(new java.awt.BorderLayout());
            setSize(160, 120);
            add(getScpNavigation(), "Center");
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        getMniReportInvestment();
        // user code end
    }

    /**
     * Initialize the JTree.
     * <p>
     * see EditorTreeCellRenderer
     */
    private void initializeTree(ModelUtility utility) {
        // set Listener's
        /*
         * MouseListener ml = new MouseAdapter() { public void mouseClicked(MouseEvent e) { int selectedRow = getTreNavigation().getRowForLocation(e.getX(),
         * e.getY()); if (selectedRow != -1) { TreePath selectedPath = getTreNavigation().getPathForLocation(e.getX(), e.getY()); MutableTreeNode selectedNode =
         * (MutableTreeNode)selectedPath.getLastPathComponent(); if (selectedNode != null) { if (e.getClickCount() == 2) { // HACK: this event comes twicely
         * doubleClickTreated = !doubleClickTreated; if (!doubleClickTreated) { // doubleClick(selRow, selPath) NavigationTreeNode element =
         * (NavigationTreeNode)selectedNode; if (element.hasSpecification()) { showSpecification(); } else if (element.isClassDiagram()) {
         * openDiagram(element.getElement()); } } } } } } }; getTreNavigation().addMouseListener(ml);
         */

        // Cell Rendering
        NavigationTreeCellRenderer renderer = new NavigationTreeCellRenderer();
        renderer.setFont(LauncherView.getInstance().getSettings().getFont());
        getTreNavigation().setCellRenderer(renderer);

        // Cell editing
        getTreNavigation().setCellEditor(new NavigationTreeCellEditor((AutoScrollingTree) getTreNavigation(), renderer));
        // getTreNavigation().getCellEditor().addCellEditorListener(this);
        getTreNavigation().setEditable(true);
        getTreNavigation().setInvokesStopCellEditing(true);

        // make tree ask for the height of each row
        getTreNavigation().setRowHeight(-1);
        getTreNavigation().setRootVisible(true);

        // set Single Row selection model
        DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        getTreNavigation().setSelectionModel(selectionModel);

        // Enable tool tips for the tree
        javax.swing.ToolTipManager.sharedInstance().registerComponent(getTreNavigation());

        // show lines from Parent to leaf
        getTreNavigation().putClientProperty("JTree.lineStyle", "Angled");//$NON-NLS-2$//$NON-NLS-1$
    }

    private void mniExportConfiguration() {
        try {
            FileChooser saveDialog = new FileChooser(LauncherView.getInstance().getSettings().getWorkingDirectory());
            saveDialog.setDialogTitle("Gruppe exportieren...");
            saveDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());

            if (saveDialog.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                LauncherView.getInstance().getSettings().setWorkingDirectory(saveDialog.getCurrentDirectory().getAbsolutePath());
                TcoPackage object = (TcoPackage) getSelectedNode();
                // morph package to export into model
                TcoModel exportRoot = (TcoModel) object.getObjectServer().createInstance(TcoModel.class);
                if (object.getId() != null) {
                    // TODO Patch: namespace of ownedElements points to this id
                    // already
                    object.getObjectServer().setUniqueId(exportRoot, object.getId());
                }
                exportRoot.setMultitude(object.getMultitude());
                // exportRoot.setBaseDate(object.getBaseDate());
                exportRoot.setOwnedElement(object.getOwnedElement());
                exportRoot.setService(object.getService());
                // RELINK to new Group:
                // exportRoot.setSystemParameter(((TcoUtility)utility).getSystemParameter());
                // TODO NYI: Dependencies

                object.getObjectServer().setUserObject(new ch.softenvironment.jomm.target.xml.XmlUserObject(saveDialog.getSelectedFile().getAbsolutePath()));
                object.getObjectServer().makePersistent(exportRoot);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void mniImportConfiguration() {
        try {
            FileChooser openDialog = new FileChooser(LauncherView.getInstance().getSettings().getWorkingDirectory());
            openDialog.setDialogTitle("Konfiguration importieren...");
            openDialog.addChoosableFileFilter(GenericFileFilter.createXmlFilter());

            if (openDialog.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                ModelUtility utility = LauncherView.getInstance().getUtility();
                // read the model
                java.io.File file = openDialog.getSelectedFile(); // .getAbsolutePath();
                ch.softenvironment.jomm.DbObjectServer rootServer = ((ch.softenvironment.jomm.mvc.model.DbChangeableBean) utility.getRoot()).getObjectServer();
                // import by alternate server
                javax.jdo.PersistenceManagerFactory pmFactory = new ch.softenvironment.jomm.target.xml.XmlObjectServerFactory();
                pmFactory.setConnectionURL(rootServer.getPersistenceManagerFactory().getConnectionURL());
                // pmFactory.setNontransactionalRead(false); // NO autoCommit
                // while reading
                // pmFactory.setNontransactionalWrite(false); // NO autoCommit
                // while writing
                ch.softenvironment.jomm.target.xml.XmlObjectServer server = (ch.softenvironment.jomm.target.xml.XmlObjectServer) pmFactory
                    .getPersistenceManager(System.getProperty("user.name"), "");

                List baskets = LauncherView.getInstance().openFile(server, file.getAbsolutePath());
                if (baskets == null) {
                    ch.softenvironment.view.BaseDialog.showWarning(this, "Import-Fehler", "Der Import ist leider fehl geschlagen.");
                } else {
                    TcoModel importedModel = (TcoModel) baskets.get(0);
                    importedModel.setName("IMPORT: " + file.getAbsolutePath());
                    utility.importModel((TcoPackage) getSelectedNode(), importedModel);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Generate the selected Node as Costblock in a HTML-Report.
     */
    private void mniReportBlockDetailed() {
        LauncherView.getInstance().showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    LauncherView.getInstance().addReport(
                        ReportTco.createBlockDetailedTco(LauncherView.getInstance().getUtility(), (TcoObject) getSelectedNode(), LauncherView.getInstance()
                            .getUtility().getUsageDuration()));
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    private void mniReportBlockTotal() {
        WaitDialog.showBusy(LauncherView.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    LauncherView.getInstance().addReport(
                        ReportTco.createBlockTco(LauncherView.getInstance().getUtility(), (TcoPackage) getSelectedNode(), LauncherView.getInstance()
                            .getUtility().getUsageDuration()));
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    @Deprecated
    private void morphToService() {
        try {
            /*
             * ch.softenvironment.view.QueryDialog dialog = new ch.softenvironment.view.QueryDialog(LauncherView.getInstance(), "Gruppe in Dienst umwandeln",
             * "Wenn die Gruppe in einen Dienst umgewandelt wird, werden Untergruppen und Dienste der Gruppe geloescht." + "\n\n" +
             * "Wollen sie die Aktion wirklich durchfuehren?"); if (dialog.isYes()) { TcoPackage group = (TcoPackage)getSelectedNode(); TcoUtility utility =
             * LauncherView.getInstance().getUtility(); Service service = (Service)utility.addOwnedElement(utility.findParent(group), Service.class);
             * service.setName(group.getName()); service.setMultitude(group.getMultitude()); service.setBaseDate(group.getBaseDate());
             * service.setDocumentation(group.getDocumentation());
             *
             * utility.removeOwnedElement(group);
             *
             * LauncherView.getInstance().setModelChanged(true); }
             */
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Move a TreeNode to another TcoPackage.
     */
    private void moveObject() {
        try {
            ModelUtility utility = LauncherView.getInstance().getUtility();
            TreeSelectionDialog dialog = new TreeSelectionDialog(LauncherView.getInstance(), getResourceString("CTSelectGroup"), utility, true);
            if (dialog.isSaved()) {
                Object target = /* (TcoObject) */dialog.getSelectedObject();
                Object node = getSelectedNode();
                String msg = utility.isAddable(node, target);
                if (msg == null) {
                    utility.relocateElement(node, target);
                    // HACK: inform Launcher that parent of node has changed
                    // because Launcher only listens to currently selected
                    // node, which does not exist any more
                    LauncherView.getInstance().setModelChanged(true);
                } else {
                    BaseDialog.showWarning(this, null, msg);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Create a new Object (and open it for e.g. in a DetailView).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void newObject(java.lang.Object source) {
        try {
            ch.softenvironment.view.BaseFrame view = null;
            java.util.List<TcoObject> list = new java.util.ArrayList<TcoObject>(1);
            if (source.equals(getMniGroup())) {
                list.add(addElement(TcoPackage.class));
                view = new PackageDetailView(viewOptions, list);
            } else if (source.equals(getMniService())) {
                list.add(addElement(Service.class));
                view = new ServiceDetailView(viewOptions, list);
            } else if (source.equals(getMniCostDriver())) {
                list.add(addElement(CostDriver.class));
                view = new CostDriverDetailView(viewOptions, list, null);
            } else if (source.equals(getMniPersonalCost())) {
                list.add(addElement(PersonalCost.class));
                view = new PersonalCostDetailView(viewOptions, list, null);
            } else if (source.equals(getMniFactCost())) {
                list.add(addElement(FactCost.class));
                view = new FactCostDetailView(viewOptions, list, null);
            }

            // ((NavigationTreeModel)getTreNavigation().getModel()).fireTreeNodesInserted(list.get(0));
            // #fireTreeStructureChanged() fired before by
            // propertyChange-mechanism
            // @see NavigationTreeModel#propertyChange()
            selectElement(list.get(0));

            view.setRelativeLocation(LauncherView.getInstance());
            view.setVisible(true);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Register/Unregister callback at Bean-changes for the given element and its subtree (if visible).
     */
    private void register(Object element, boolean isToBeRegistered) {
        ch.softenvironment.jomm.mvc.model.DbChangeableBean object = (ch.softenvironment.jomm.mvc.model.DbChangeableBean) element;

        // register/unregister the object itself
        if (isToBeRegistered) {
            object.addPropertyChangeListener(modelAdapter);
        } else {
            object.removePropertyChangeListener(modelAdapter);
        }

        // register/unregister the object's children
        java.util.Iterator<TcoObject> iterator = LauncherView.getInstance().getUtility().iteratorChildren(object);
        while (iterator.hasNext()) {
            register(iterator.next(), isToBeRegistered);
        }
    }

    public void registerPlugin(JComponent menu) {
        pluginExtensions.add(menu);
    }

    /**
     * Remove the selected Object's (for e.g from a JTable).
     *
     * @param source (for e.g. a Popup-MenuItem)
     */
    @Override
    public void removeObjects(java.lang.Object source) {
        final Object node = getSelectedNode();
        if ((node != null)
            && ch.softenvironment.view.BaseDialog.showConfirmDeletion(LauncherView.getInstance(), getResourceString("CTRemove"),
            getResourceString("CIRemove"))) {
            WaitDialog.showBusy(LauncherView.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {

                        // close View to reduce mess with involved
                        // ConsistencyController's
                        java.util.List list = new java.util.ArrayList(1);
                        list.add(node);
                        viewOptions.getViewManager().closeAll(list);

                        // Object owner = utility.getParent(node);
                        LauncherView.getInstance().getUtility().removeOwnedElement(node);
                        // TODO NYI:
                        // ((NavigationTreeModel)getTreNavigation().getModel()).fireTreeNodesRemoved();
                        register(node, false);
                        // HACK: inform Launcher that parent of node has changed
                        // because Launcher only listens to currently selected
                        // node, which does not exist any more
                        LauncherView.getInstance().setModelChanged(true);
                        /*
                         * // HACK: List is the same but size has shrinked if (node instanceof Service) { ((ch.softenvironment.jomm.
                         * DbChangeableBean)owner).firePropertyChange("service", null, ((TcoPackage)owner).getService()); } else if (node instanceof TcoPackage)
                         * { ((ch.softenvironment.jomm .DbChangeableBean)owner).firePropertyChange ("ownedElement", null,
                         * ((TcoPackage)owner).getOwnedElement()); }
                         */
                    } catch (Exception e) {
                        handleException(e);
                    }
                }
            });
        }
    }

    /**
     * Switch selected Node into editing Mode.
     *
     * see EditorTreeModel#valueForPathChanged(TreePath, Object)
     */
    private void renameObject() {
        getTreNavigation().startEditingAtPath(getTreNavigation().getSelectionPath());
    }

    private void reportInvestment() {
        WaitDialog.showBusy(LauncherView.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    LauncherView.getInstance().addReport(
                        ReportInvestment.createInvestment(LauncherView.getInstance().getUtility(), (TcoPackage) getSelectedNode()));
                } catch (Exception e) {
                    handleException(e);
                }
            }
        });
    }

    /**
     * Select given Element in NavigationTree. If necessary make path visible before.
     */
    public void selectElement(Object element) {
        TreePath foundNode = modelAdapter.getTreePath(element);

        if (foundNode == null) {
            getTreNavigation().setSelectionPath(null);
        } else {
            // getTreNavigation().expandPath(getTreNavigation().getSelectionPath());
            // getTreNavigation().makeVisible(path);
            getTreNavigation().setSelectionPath(foundNode);
            getTreNavigation().scrollPathToVisible(foundNode);
        }
    }

    /**
     * Adapt anything when Tree-Selection changes.
     */
    private void selectionChanged(javax.swing.event.TreeSelectionEvent treeSelectionEvent) {
        Object treeNode = getSelectedNode();

        if (treeNode != null) {
            // rename
            getTreNavigation().setEditable(LauncherView.getInstance().getUtility().isNodeEditable(treeNode));
        }
        // documentation
        if (treeNode instanceof TcoObject) {
            LauncherView.getInstance().setDocumentation((TcoObject) treeNode);
        } else {
            // might happen at initialization
            LauncherView.getInstance().setDocumentation(null);
        }
    }

    /**
     * Build the visual Tree with a new given Model.
     */
    protected void setUtility(ModelUtility utility) {
        try {
            if ((modelAdapter != null) && (modelAdapter.getRoot() != null)) {
                // unregister Listener while building new model
                // MetaModel.getInstance().removeMetaModelListener(modelAdapter);
                register(modelAdapter.getRoot(), false);
            }

            // getTreNavigation().stopEditing();
            // getTreNavigation().clearSelection();

            // set Root-Node (contains no ModelElement, still shows filename
            // kept in UmlModel)
            modelAdapter = new NavigationTreeModel(false, utility);
            // modelAdapter.setOrdering(((UserSettings)LauncherView.getSettings()).getNavigationSort());
            getTreNavigation().setModel(modelAdapter);

            initializeTree(utility);

            // always show elements of Model
            getTreNavigation().expandRow(1);

            // register Listener
            // MetaModel.getInstance().addMetaModelListener(modelAdapter);
            register(modelAdapter.getRoot(), true);
        } catch (Exception e) {
            handleException(e);
        }
    }

    protected void setViewOptions(ch.softenvironment.view.ViewOptions viewOptions) {
        this.viewOptions = viewOptions;
        if (!viewOptions.isSet(LauncherView.TEST_RELEASE)) {
            // TODO not yet robust
            getSepImportExport().setVisible(false);
            getMniImportConfiguration().setVisible(false);
            getMniExportConfiguration().setVisible(false);
            getMniMorphToService().setVisible(false);
        }
    }
}
