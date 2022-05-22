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
 * Define a set of typical Toolbar actions, such as new, save, print, etc.
 * <p>
 * Usage for a GUI-Component: <code> myToolbar = new ToolBar(); myToolbar.addToolBarListener(new ch.softenvironment.view.ToolBarListener() { public void
 * tbbPrintAction_actionPerformed(java.util.EventObject e) { printObject(); } .. }
 * </code>
 *
 * @author Peter Hirzel softEnvironment GmbH
 */
public interface ToolBarListener extends java.util.EventListener {

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbCopyAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbCutAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbDeleteAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbFindAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbNewAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbOpenAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbPasteAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbPrintAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbRedoAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbSaveAction_actionPerformed(java.util.EventObject newEvent);

    /**
     * @param newEvent java.util.EventObject
     */
    void tbbUndoAction_actionPerformed(java.util.EventObject newEvent);
}
