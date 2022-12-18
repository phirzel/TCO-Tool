package ch.softenvironment.view;

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
 */

/**
 * Method-Set for a DetailView
 *
 * @author Peter Hirzel
 * @see BaseFrame (as a Parent-Class)
 */
public interface DetailView {

    /**
     * Assign a set of aggregates given in objects.
     *
     * @param objects
     */
    void assignObjects(java.util.List<?> objects);

    /**
     * Save an Object represented by DetailView.
     */
    void saveObject();

    /**
     * Make the View represent the given Object.
     * <p>
     * Any Listeners added to the object should be removed, of course (for e.g. by #dispose()).
     *
     * @param object <example> try { if ((object != null) && object.equals(getObject())) { return; } if (getObject() != null) { getObject().removeChangeListener(getConsistencyController()); }
     *     ((DbObject)object).refresh(true); setObject(object); object.addChangeListener(getconsistencyController()); } catch(Throwable e) { handleException(e); } </example>
     */
    void setCurrentObject(Object object);

    /**
     * Undo the changes of an Object represented by this GUI.
     */
    void undoObject();

    /**
     * Redo the last undoing changes of an Object represented by this GUI.
     */
    void redoObject();
}
