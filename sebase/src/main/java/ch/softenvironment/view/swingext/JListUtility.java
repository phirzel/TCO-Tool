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

import ch.softenvironment.util.Evaluator;
import ch.softenvironment.view.swingext.JComboBoxUtility.BeanEvaluator;
import javax.swing.JList;

/**
 * Utility for JList.
 *
 * @author Peter Hirzel
 * @see JComboBoxUtility
 */
public final class JListUtility {

    private JListUtility() {
        throw new IllegalStateException("utility class");
    }

    /**
     * @see #initList(JList, java.util.List, String, Evaluator, int)
     */
    public static void initList(JList list, java.util.List<?> items, final String property, Evaluator evaluator) {
        initList(list, items, property, evaluator, JComboBoxUtility.SORT_ASCENDING);
    }

    /**
     * Setup a JList with an optionally sorted list of items and a renderer which shows value of each object#property of each element in items.
     *
     * @param list
     * @param items
     * @param property contained in each object within items
     * @param evaluator uses default BeanEvaluator if null
     * @param sort sort text items according to JComboBoxUtility#SORT_*
     * @see JComboBoxUtility
     */
    public static void initList(JList list, java.util.List<?> items, final String property, Evaluator evaluator, final int sort) {
        Evaluator eval = evaluator;
        if (eval == null) {
            eval = new BeanEvaluator();
        }
        list.setCellRenderer(new JComboBoxUtility.ObjectComboBoxRenderer(eval, property));
        list.setModel(new JComboBoxUtility.SortedComboBoxModel(items, eval, property, false /*never necessary cause unselectable*/, sort));
    }
}
