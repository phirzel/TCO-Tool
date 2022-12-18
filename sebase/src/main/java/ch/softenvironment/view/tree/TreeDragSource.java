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

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.tree.TreePath;

/**
 * Tool for Mouse-Drag within a JTree.
 *
 * @author Peter Hirzel
 */
class TreeDragSource implements DragSourceListener, DragGestureListener {

    private final DragSource source;
    // private DragGestureRecognizer recognizer = null;
    private final AutoScrollingTree sourceTree;
    private TransferableTreeNode transferable = null;

    public TreeDragSource(AutoScrollingTree tree, final int actions) {
        super();
        sourceTree = tree;
        source = new DragSource();
        /* DragGestureRecognizer recognizer = */
        source.createDefaultDragGestureRecognizer(sourceTree, actions, this);
    }

    /*
     * Only MOVE action supported. Drag Gesture Handler.
     *
     * @see TreeDropTarget#drop()
     */
    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        TreePath path = sourceTree.getSelectionPath();
        if ((path == null) || (path.getPathCount() <= 1)) {
            // do NOT move the ROOT node or an empty selection
            return;
        }
        // Object /*DefaultMutableTreeNode*/ selectedNode =
        // /*(DefaultMutableTreeNode)*/path.getLastPathComponent();
        transferable = new TransferableTreeNode(path);
        // if you support dropping the node anywhere, you should probably start
        // with a valid move cursor
        source.startDrag(dge, DragSource.DefaultMoveDrop, transferable, this);
    }

    // Drag Event Handlers
    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
        // Tracer.getInstance().debug("DragSource#Enter");
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde) {
        // Tracer.getInstance().debug("DragSource#Over");
        // TODO setDragDropCursor(dsde); //=> buggy
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    /**
     * @see TreeDropTarget#drop(DropTargetDropEvent)
     */
    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
        // if (dsde.getDropSuccess()) { "OK" }
        transferable = null;
    }

    /*
     * Verify whether current dropZone is ok for current transferable.
     *
     * @param dtde
     * @return private boolean isDropAcceptable(DragSourceDragEvent dsde) {
    try {
    Point p = dsde.getLocation();
    DragSourceContext dtc = dsde.getDragSourceContext();
    JTree tree = (JTree)dtc.getComponent(); // by default => targetTree
    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
    // TreeNode
    Object dropTargetNode = path.getLastPathComponent();
    Object source = ((TreePath)transferable.getTransferData(TransferableTreeNode.TREE_PATH_FLAVOR)).getLastPathComponent();

    String msg = sourceTree.getUtility().isAddable(source, dropTargetNode);
    if (msg == null) {
    return true;
    } else {
    Tracer.getInstance().debug("isDropAcceptable()->false: " + msg);
    return false;
    }
    } catch (Throwable e) {
    Tracer.getInstance().developerWarning(e.getLocalizedMessage());
    return false;
    }
    }
     */

    /*
     * Show appropriate cursor depending on drop-zone.
     *
     * @param dsde

    private void setDragDropCursor(DragSourceDragEvent dsde) {
    DragSourceContext dsc = dsde.getDragSourceContext();
    if (isDropAcceptable(dsde)) {
    dsc.setCursor(DragSource.DefaultMoveDrop);
    } else {
    dsc.setCursor(DragSource.DefaultMoveNoDrop);
    }
    }
     */
}