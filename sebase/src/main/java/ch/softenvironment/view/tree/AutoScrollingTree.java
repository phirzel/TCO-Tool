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

import java.awt.dnd.DnDConstants;

/**
 * Extension to JTree to enable auto-scrolling and Drag and Drop.
 *
 * @author ce
 * @author Peter Hirzel
 */

public class AutoScrollingTree extends javax.swing.JTree implements java.awt.dnd.Autoscroll {

	private final int margin = 12;
	@SuppressWarnings("unused")
	private TreeDragSource ds = null;
	@SuppressWarnings("unused")
	private TreeDropTarget dt = null;

	/**
	 * Create an AutoScrolling JTree with "drag and drop" support.
	 *
	 * @param dndType according to java.awt.dnd.DnDConstants
	 */
	public AutoScrollingTree(final int dndType) {
		super();
		// drag/drop support
		if (dndType == DnDConstants.ACTION_MOVE) {
			ds = new TreeDragSource(this, dndType);
			dt = new TreeDropTarget(this);
		} else {
			// TODO implement other dnd-types
		}
	}

	/**
	 * Ok, we've been told to scroll because the mouse cursor is in our scroll zone.
	 */
	@Override
	public void autoscroll(java.awt.Point p) {
		// Figure out which row we're on.
		int realrow = getClosestRowForLocation(p.x, p.y);
		java.awt.Rectangle outer = getBounds();
		// Now decide if the row is at the top of the screen or at the
		// bottom. We do this to make the previous row (or the next
		// row) visible as appropriate. If we're at the absolute top or
		// bottom, just return the first or last row respectively.
		realrow = (p.y + outer.y <= margin ? realrow - 1 <= 0 ? 0 : realrow - 1 : realrow + 1 >= getRowCount() - 1 ? getRowCount() - 1 : realrow + 1);
		scrollRowToVisible(realrow);
	}

	/**
	 * Calculate the insets for the *JTRee*, not the viewport the tree is in. This makes it a bit messy.
	 */
	@Override
	public java.awt.Insets getAutoscrollInsets() {
		java.awt.Rectangle outer = getBounds();
		java.awt.Rectangle inner = getParent().getBounds();
		return new java.awt.Insets(inner.y - outer.y + margin, inner.x - outer.x + margin, outer.height - inner.height - inner.y + outer.y + margin,
			outer.width - inner.width - inner.x + outer.x + margin);
	}

	/**
	 * Convenience Method.
	 */
	protected TreeNodeUtility getUtility() {
		return ((NavigationTreeModel) getModel()).getUtility();
	}
}
