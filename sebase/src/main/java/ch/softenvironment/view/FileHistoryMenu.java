package ch.softenvironment.view;

/*
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 */

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.StringUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenuItem;

/**
 * Generic FileMenu to provide a list of recently used files.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class FileHistoryMenu extends javax.swing.JMenu {

    private FileHistoryListener listener = null;
    private int numberOfEntries = 0;
    private final java.util.List<String> history = new ArrayList<String>();

    // use List to keep order (actually SET)

    /**
     * FileHistoryMenu constructor.
     *
     * @param listener GUI-Class showing a FileHistoryMenu
     * @param maxNumberOfEntries max files to be historized
     * @param currentEntries given history
     */
    public FileHistoryMenu(FileHistoryListener listener, int maxNumberOfEntries, java.util.List<String> currentEntries) {
        super();

        this.listener = listener;
        this.numberOfEntries = maxNumberOfEntries;

        translate();

        for (int i = 0; (i < currentEntries.size()) && (i < maxNumberOfEntries); i++) {
            String filename = currentEntries.get(i);
            if (!StringUtils.isNullOrEmpty(filename)) {
                history.add(filename);
            }
        }
        buildSubmenu();
    }

    /**
     * Translate to current locale.
     */
    public void translate() {
        setText(ResourceManager.getResource(FileHistoryMenu.class, "MnuFileHistory_text"));
        setToolTipText(ResourceManager.getResource(FileHistoryMenu.class, "MnuFileHistory_toolTipText"));
    }

    /**
     * Add most recently opened file to checkIn at top of History.
     *
     * @param filename (incl. absolute path) to be kept in history
     */
    public void addRecent(final String filename) {
        removeRecent(filename); // prevent double entries

        history.add(0, filename); // shuffle last at top
        if (history.size() > numberOfEntries) {
            history.remove(numberOfEntries);
        }
        buildSubmenu();
    }

    /**
     * Create generic JMenuItem's.
     */
    private void buildSubmenu() {
        removeAll();

        Iterator<String> iterator = history.iterator();
        while (iterator.hasNext()) {
            final String filename = iterator.next();
            JMenuItem menuItem = new JMenuItem(filename);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    listener.openFile(filename);
                }
            });
            add(menuItem);
        }
    }

    /**
     * Return the current history in last used Order.
     *
     * @return java.util.List
     */
    public java.util.List<String> getHistory() {
        return history;
    }

    /**
     * Remove a file from History (for e.g. if non existing any more).
     *
     * @param filename (incl. absolute path) to be removed from history
     */
    public void removeRecent(String filename) {
        history.remove(filename);
        buildSubmenu();
    }
}
