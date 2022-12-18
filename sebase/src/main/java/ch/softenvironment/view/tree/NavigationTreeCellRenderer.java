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

import java.awt.Component;
import javax.swing.JTree;
import lombok.extern.slf4j.Slf4j;

/**
 * TreeCellRenderer for a TreeNode.
 *
 * @author Peter Hirzel
 */

@Slf4j
public class NavigationTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {

    /**
     * This is messaged from JTree whenever it needs to get the size of the component or it wants to draw it. This attempts to set the font based on value, which will be a TreeNode.
     */
    public NavigationTreeCellRenderer() {
        super();
    }

    /**
     * Adapt the correct LeafIcon.
     * <p>
     * see NavigationView.initializeTree()
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if ((tree instanceof AutoScrollingTree) && (((AutoScrollingTree) tree).getUtility() != null)) {
            TreeNodeUtility utility = ((AutoScrollingTree) tree).getUtility();
            javax.swing.Icon icon = utility.getIcon(value.getClass(), expanded);
            if (icon != null) {
                setIcon(icon);
            }
            String name = utility.getName(value);
            setText(name);

            if (value != null) {
                setToolTipText(utility.getToolTip(value));
            }
        } else {
            log.warn("Developer warning: tree expected as instanceof AutoScrollingTree with a non-null #getUtility()");
        }

        return this;
    }

    /**
     * paint is subclassed to draw the background correctly.
     * <p>
     * Hint for JRE 1.2.2 JLabel currently does not allow backgrounds other than white, and it will also fill behind the icon. Something that isn't desirable.
     */
    @Override
    public void paint(java.awt.Graphics g) {
        /*
         * java.awt.Color bColor; javax.swing.Icon currentI = getIcon();
         *
         * if (selected) bColor = java.awt.Color.yellow; else if (getParent() !=
         * null) // Pick background color up from parent (which will come from
         * // the JTree we're contained in). bColor =
         * getParent().getBackground(); else bColor = getBackground();
         * g.setColor(bColor); if (currentI != null && getText() != null) { int
         * offset = (currentI.getIconWidth() + getIconTextGap());
         *
         * g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1); }
         * else g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
         */
        super.paint(g);
    }
}
