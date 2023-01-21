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

/**
 * Listener for any Implementors who need to open a file when chosen in the File History.
 *
 * @author Peter Hirzel <i>soft</i> Environment
 * @since 1.1 (2004-05-08)
 */
public interface FileHistoryListener {

    /**
     * This method will be called by FileHistoryMenu, when a Filename was chosen in recent File-List.
     *
     * @param filename including absolute Path
     * @see FileHistoryMenu#addRecent(String)
     */
    void openFile(String filename);
}
