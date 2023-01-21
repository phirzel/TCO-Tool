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
 * @author Peter Hirzel
 * @since 1.2 (2004-02-05) $ $Date: 2004-02-05 11:32:59 $
 */
public interface FileNamePanelListener extends java.util.EventListener {

    /**
     * @param newEvent java.util.EventObject
     */
    void textKeyReleased(java.util.EventObject newEvent);
}
