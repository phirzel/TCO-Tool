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
 * Simple Panel to represent a certain Object which allows changes to be saved.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.4 $ $Date: 2004-04-27 18:49:45 $
 */
public interface DataPanel {

    /**
     * Return the changed object displayed.
     */
    java.lang.Object getObject();

    /**
     * Set the Object to be displayed by panel.
     */
    void setObject(java.lang.Object object);
}
