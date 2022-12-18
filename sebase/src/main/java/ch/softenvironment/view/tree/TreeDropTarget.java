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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.BaseFrame;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * Listener for Mouse-Drop within a JTree.
 *
 * @author Peter Hirzel
 */
class TreeDropTarget implements DropTargetListener {

    // private DropTarget target = null;
    private AutoScrollingTree targetTree = null;

    public TreeDropTarget(AutoScrollingTree tree) {
        targetTree = tree;

        // register tree as DropTarget @see #drop()
        /* target = */
        new DropTarget(targetTree, this);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        // Tracer.getInstance().debug("dragEnter");
        // Object /*TreeNode*/ dropTargetNode = getNodeForEvent(dtde);
        /*
         * if (targetTree.getUtility().isAddable(dropTargetNode)) { dtde.rejectDrag(); } else { // start by supporting move operations
         * dtde.acceptDrag(dtde.getDropAction()); //=DnDConstants.ACTION_MOVE }
         */
    }

    /**
     * @see #dragEnter(DropTargetDragEvent)
     */
    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        /*
         * if (dsde.getDropAction() != DnDConstants.ACTION_MOVE) {
         *
         * }
         */
    }

    /**
     * Only MOVE supported.
     *
     * @see TreeDragSource#dragGestureRecognized(DragGestureEvent)
     */
    @Override
    public void drop(DropTargetDropEvent dtde) {
        // find target node to drop source to
        Point pt = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent(); // by default => targetTree
        TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
        Object /* DefaultMutableTreeNode */target = /* (DefaultMutableTreeNode) */parentpath.getLastPathComponent();

        try {
            Transferable tr = dtde.getTransferable();
            DataFlavor[] flavors = tr.getTransferDataFlavors();
            if ((flavors.length == 1) && tr.isDataFlavorSupported(flavors[0]) && (dtde.getDropAction() == DnDConstants.ACTION_MOVE)) {
                TreePath p = (TreePath) tr.getTransferData(flavors[0]);
                Object /* DefaultMutableTreeNode */sourceNode = /* (DefaultMutableTreeNode) */p.getLastPathComponent();
                String msg = targetTree.getUtility().isAddable(sourceNode, target);
                if (msg == null) {
                    // TODO prevent strange Move-Effect ((select node, release Mouse, go over a '+' to open package, the sometimes drag is triggered) by an
                    // additional Confirm
                    if (BaseDialog.showConfirm(targetTree, ResourceManager.getResource(TreeDropTarget.class, "CTMoveConfirm"),
                        ResourceManager.getResource(TreeDropTarget.class, "CIMoveConfirm"))) {
                        dtde.acceptDrop(dtde.getDropAction());
                        dtde.dropComplete(true);
                        targetTree.getUtility().relocateElement(sourceNode, target); // model.insertNodeInto(node, parent, 0);
                    } else {
                        dtde.rejectDrop();
                    }
                } else {
                    dtde.rejectDrop();
                    BaseDialog.showWarning(targetTree, null, msg); // part of view mechanism => Dialog is ok here
                }
            } else {
                dtde.rejectDrop();
            }
        } catch (Exception e) {
            dtde.rejectDrop();
            BaseFrame.showException(targetTree, e); // part of view mechanism => Dialog is ok here
        }
    }
}