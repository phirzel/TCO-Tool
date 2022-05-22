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

/**
 * Interface for a utility using an AutoScrollingJTree implementing the relevant generic functions dealing with Tree-Objects.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public interface TreeNodeUtility {

    /**
     * Add element to owner.
     *
     * @param owner ch.softenvironment.tree.core.Namespace
     * @param element owned Element to be added to owner
     * @return given element
     */
    Object addOwnedElement(Object owner, Object element) throws Exception;

    /**
     * Compare two classes for sorting.
     */
    int compareDefinition(Class<?> o1, Class<?> o2);

    /**
     * Return the parent of the given node. There must always be one except of the root-element.
     *
     * @return owner of given node
     */
    Object findParent(Object node);

    /**
     * Returns the appropriate icon.
     *
     * @param nodeType (concrete TreeNode type)
     * @param expanded (whether node is expanded or not, for e.g. an opened Package)
     */
    javax.swing.Icon getIcon(Class<?> nodeType, boolean expanded);

    /**
     * Returns the string to display for this object.
     */
    String getName(Object node);

    /**
     * Return the root-element of the Model-Tree.
     */
    Object getRoot();

    /**
     * Returns the a ToolTip if cursor is on node.
     */
    String getToolTip(Object node);

    /**
     * Return whether child my be owned by owner.
     *
     * @param child
     * @param owner
     * @return non-acceptable reason or null if ok
     */
    String isAddable(Object child, Object owner);

    /**
     * Return whether Node must not have any children.
     */
    boolean isLeaf(Object node);

    /**
     * Return whether a node might have changed in model-tree visually. For e.g. a represented speaking name of a TreeNode.
     *
     * @param event => source and property changed
     */
    boolean isNodeChanged(java.beans.PropertyChangeEvent event);

    /**
     * Return whether Node mapping this TreeElement might be renamed.
     */
    boolean isNodeEditable(Object node);

    /**
     * Return whether Node mapping this TreeElement might be moved.
     */
    boolean isNodeMovable(Object node);

    /**
     * Return whether Node mapping this TreeElement might be removed.
     */
    boolean isNodeRemovable(Object node);

    /**
     * Return whether the structure might have changed in model-tree visually.
     *
     * @param event => source and property changed
     */
    boolean isNodeStructureChanged(java.beans.PropertyChangeEvent event);

    /**
     * Return the iterator of children of given node (Namespace).
     */
    java.util.Iterator<?> iteratorChildren(Object node);

    /**
     * Move an object from its current-namespace to target-namespace. Perhaps show selection path of given object after relocation in Tree.
     *
     * @param object ModelElement
     * @param targetNamespace Package
     */
    void relocateElement(Object object, Object targetNamespace) throws Exception;

    /**
     * Remove ownedElement from owner.
     *
     * @param element ch.softenvironment.tree.core.Element
     */
    void removeOwnedElement(Object element);

    /**
     * Sets the string to display for this object.
     */
    void setName(Object node, String newName);
}
