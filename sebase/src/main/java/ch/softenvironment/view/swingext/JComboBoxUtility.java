package ch.softenvironment.view.swingext;

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

import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Evaluator;
import ch.softenvironment.util.ListUtils;
import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * JComboBox utility to display sorted objects in items by its property determined by evaluator.
 *
 * @author Peter Hirzel
 */
public final class JComboBoxUtility {

    private JComboBoxUtility() {
        throw new IllegalStateException("utility class");
    }

    public static final int SORT_KEEP_ORDER = 100;
    public static final int SORT_ASCENDING = 101;
    //public static final int SORT_DESCENDING = 102;

    /**
     * Default generic generic Evaluator assuming getter-method.
     *
     * @see BeanReflector
     */
    protected static class BeanEvaluator implements Evaluator {

        @Override
        public Object evaluate(Object owner, final String property) {
            try {
                if (owner != null) {
                    ch.softenvironment.util.BeanReflector br = new ch.softenvironment.util.BeanReflector(owner, property);
                    return br.getValue();
                }
            } catch (Throwable e) {
                throw new DeveloperException("cannot determine value: " + e.getLocalizedMessage());
            }
            return null;
        }
    }

    /**
     * Display the value of a given public property of the represented object.
     */

    protected static class ObjectComboBoxRenderer extends BasicComboBoxRenderer {

        private Evaluator evaluator = null;
        private String property = null;

        /**
         * Set the evaluator to return display value fo given property.
         *
         * @param evaluator
         * @param property
         */
        protected ObjectComboBoxRenderer(Evaluator evaluator, final String property) {
            super();
            this.evaluator = evaluator;
            this.property = property;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Icon) {
                // @see super
                setIcon((Icon) value);
            } else {
                if (value == null) {
                    setText("");
                } else {
                    Object result = evaluator.evaluate(value, property);
                    if (result == null) {
                        setText("");
                    } else {
                        setText(result.toString());
                    }
                }
            }
            return this;
        }
    }

    /**
     * Extended DefaultComboBoxModel to sort ascending by displayable value of given by an object's property.
     */

    protected static class SortedComboBoxModel extends DefaultComboBoxModel {

        /**
         * Initialize the list.
         *
         * @param items
         * @param addNullElement add a null-element as very first element
         * @param evaluator
         * @param property
         * @param sort
         */
        protected SortedComboBoxModel(java.util.List<?> items, Evaluator evaluator, final String property, boolean addNullElement, final int sort) {
            super();
            if (addNullElement) {
                // insert an empty object as very first element
                addElement(null);
            }

            java.util.Iterator<?> iterator = null;
            if (sort == SORT_ASCENDING) {
                iterator = ListUtils.sort(items, evaluator, property).iterator();
            } else {
                // keep given order
                iterator = items.iterator();
            }
            while (iterator.hasNext()) {
                addElement(iterator.next());
            }
        }
    }

    /**
     * @see #initComboBox(JComboBox, java.util.List, String, boolean, Evaluator)
     */
    public static void initComboBox(JComboBox comboBox, java.util.List<?> items, final String property) {
        initComboBox(comboBox, items, property, false);
    }

    /**
     * @see #initComboBox(JComboBox, java.util.List, String, boolean, Evaluator)
     */
    public static void initComboBox(JComboBox comboBox, java.util.List<?> items, final String property, boolean addNullElement) {
        initComboBox(comboBox, items, property, addNullElement, new BeanEvaluator());
    }

    /**
     * Sort items ascending by default.
     * <p>
     * see #initComboBox(JComboBox, java.util.List, String, boolean, Evaluator, boolean)
     */
    public static void initComboBox(JComboBox comboBox, java.util.List<?> items, final String property, boolean addNullElement, Evaluator evaluator) {
        initComboBox(comboBox, items, property, addNullElement, evaluator, SORT_ASCENDING);
    }

    /**
     * Setup a JComboBox with an optionally sorted list of items and a renderer which shows value of each object#property of each element in items.
     *
     * @param comboBox
     * @param items
     * @param property contained in each object within items
     * @param addNullElement whether items list shall have a very first null-Element
     * @param evaluator uses default BeanEvaluator if null
     * @param sort sort text items according to SORT_*
     */
    public static void initComboBox(JComboBox comboBox, java.util.List<?> items, final String property, boolean addNullElement, Evaluator evaluator, final int sort) {
        Evaluator eval = evaluator;
        if (eval == null) {
            // set the standard evaluator #get*()
            eval = new BeanEvaluator();
        }
        comboBox.setRenderer(new ObjectComboBoxRenderer(eval, property));
        comboBox.setModel(new SortedComboBoxModel(items, eval, property, addNullElement, sort));
    }
}
