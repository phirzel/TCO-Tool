package ch.softenvironment.view.tree;

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

import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.dnd.DnDConstants;

/**
 * User Interface to choose a Package within Model-Tree.
 *
 * @author Peter Hirzel
 */

public class TreeSelectionDialog extends ch.softenvironment.view.BaseDialog {

    private Object selectedPackage = null;
    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JButton ivjBtnCancel = null;
    private javax.swing.JButton ivjBtnOk = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JScrollPane ivjScpTree = null;
    private javax.swing.JTree ivjTreTree = null;

    class IvjEventHandler implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == TreeSelectionDialog.this.getBtnOk()) {
                connEtoC1(e);
            }
            if (e.getSource() == TreeSelectionDialog.this.getBtnCancel()) {
                connEtoC2(e);
            }
        }
    }

    public TreeSelectionDialog(java.awt.Frame owner, String title, TreeNodeUtility utility, boolean packagesOnly) {
        super(owner, true);

        initialize();

        if (title != null) {
            setTitle(title);
        }

        initializeTree(utility, packagesOnly);

        setVisible(true);
    }

    /**
     * Constructor
     *
     * @param owner Symbol
     * @param modal Symbol
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public TreeSelectionDialog(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
     * connEtoC1: (BtnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> PackageSelectionDialog.cancelPressed()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            this.okPressed();
            // user code begin {2}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2: (BtnOk.action.actionPerformed(java.awt.event.ActionEvent) --> PackageSelectionDialog.okPressed()V)
     *
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
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
                ivjBaseDialogContentPane.setLayout(new java.awt.GridBagLayout());

                java.awt.GridBagConstraints constraintsBtnOk = new java.awt.GridBagConstraints();
                constraintsBtnOk.gridx = 1;
                constraintsBtnOk.gridy = 2;
                constraintsBtnOk.ipadx = 60;
                constraintsBtnOk.insets = new java.awt.Insets(8, 87, 20, 4);
                getBaseDialogContentPane().add(getBtnOk(), constraintsBtnOk);

                java.awt.GridBagConstraints constraintsBtnCancel = new java.awt.GridBagConstraints();
                constraintsBtnCancel.gridx = 2;
                constraintsBtnCancel.gridy = 2;
                constraintsBtnCancel.ipadx = 14;
                constraintsBtnCancel.insets = new java.awt.Insets(8, 4, 20, 109);
                getBaseDialogContentPane().add(getBtnCancel(), constraintsBtnCancel);

                java.awt.GridBagConstraints constraintsScpTree = new java.awt.GridBagConstraints();
                constraintsScpTree.gridx = 1;
                constraintsScpTree.gridy = 1;
                constraintsScpTree.gridwidth = 2;
                constraintsScpTree.fill = java.awt.GridBagConstraints.BOTH;
                constraintsScpTree.weightx = 1.0;
                constraintsScpTree.weighty = 1.0;
                constraintsScpTree.ipadx = 376;
                constraintsScpTree.ipady = 254;
                constraintsScpTree.insets = new java.awt.Insets(12, 13, 7, 15);
                getBaseDialogContentPane().add(getScpTree(), constraintsScpTree);
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
     * Return the BtnOk property value.
     *
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBtnOk() {
        if (ivjBtnOk == null) {
            try {
                ivjBtnOk = new javax.swing.JButton();
                ivjBtnOk.setName("BtnOk");
                ivjBtnOk.setText("OK");
                // user code begin {1}
                ivjBtnOk.setText(getOKString());
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBtnOk;
    }

    /**
     * Return the ScpTree property value.
     *
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getScpTree() {
        if (ivjScpTree == null) {
            try {
                ivjScpTree = new javax.swing.JScrollPane();
                ivjScpTree.setName("ScpTree");
                getScpTree().setViewportView(getTreTree());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScpTree;
    }

    /**
     * Returns the Package selected by the user
     */
    public Object getSelectedObject() {
        return selectedPackage;
    }

    /**
     * Return the TreTree property value.
     *
     * @return javax.swing.JTree
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTree getTreTree() {
        if (ivjTreTree == null) {
            try {
                /*
                 * ivjTreTree = new javax.swing.JTree();
                 * ivjTreTree.setName("TreTree"); ivjTreTree.setBounds(0, 0,
                 * 165, 148);
                 */
                // user code begin {1}
                ivjTreTree = new AutoScrollingTree(DnDConstants.ACTION_NONE);
                ivjTreTree.setName("TreTree");
                ivjTreTree.setBounds(0, 0, 165, 148);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjTreTree;
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
     * Initializes connections
     *
     *
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() {
        // user code begin {1}
        // user code end
        getBtnOk().addActionListener(ivjEventHandler);
        getBtnCancel().addActionListener(ivjEventHandler);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("PackageSelectionDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(426, 348);
            setContentPane(getBaseDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle("<PackageSelection>");
        // user code end
    }

    /**
     * Initialize the JTree.
     * <p>
     * see EditorTreeCellRenderer
     */
    private void initializeTree(TreeNodeUtility utility, boolean packagesOnly) {
        // set Listener's
        /*
         * MouseListener ml = new MouseAdapter() { public void
         * mouseClicked(MouseEvent e) { int selectedRow =
         * getTreTree().getRowForLocation(e.getX(), e.getY()); if (selectedRow
         * != -1) { TreePath selectedPath =
         * getTreTree().getPathForLocation(e.getX(), e.getY()); MutableTreeNode
         * selectedNode = (MutableTreeNode)selectedPath.getLastPathComponent();
         * if (selectedNode != null) { if (e.getClickCount() == 2) { // HACK:
         * this event comes twicely doubleClickTreated = !doubleClickTreated; if
         * (!doubleClickTreated) { // doubleClick(selRow, selPath)
         * NavigationTreeNode element = (NavigationTreeNode)selectedNode; if
         * (element.hasSpecification()) { showSpecification(); } else if
         * (element.isClassDiagram()) { openDiagram(element.getElement()); } } }
         * } } } }; getTreTree().addMouseListener(ml);
         */
        getTreTree().setModel(new NavigationTreeModel(packagesOnly, utility));
        getTreTree().setCellRenderer(new NavigationTreeCellRenderer());

        // NO Cell editing
        // getTreNavigation().setCellEditor(new
        // NavigationTreeCellEditor(getTreNavigation(), renderer));

        // make tree ask for the height of each row
        getTreTree().setRowHeight(-1);
        getTreTree().setRootVisible(true);

        // set Single Row selection model
        DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        getTreTree().setSelectionModel(selectionModel);

        // Enable tool tips for the tree
        javax.swing.ToolTipManager.sharedInstance().registerComponent(getTreTree());

        // show lines from Parent to leaf
        getTreTree().putClientProperty("JTree.lineStyle", "Angled");//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * In case OK-Button was pressed.
     *
     * @return boolean whether saving was successful or not
     */
    @Override
    protected boolean save() {
        if (getTreTree().getSelectionPath() == null) {
            cancelPressed();
            return false;
        } else {
            selectedPackage = getTreTree().getSelectionPath().getLastPathComponent();
            return super.save();
        }
    }
}
