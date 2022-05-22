package ch.softenvironment.jomm.serialize;
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
 * Interface for any Visitors such as Code-Generators and the like.
 *
 * @author ce
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.1.1.1 $ $Date: 2005-04-25 10:01:11 $
 */
public interface VisitorCallback {

    /**
     * Adds an visited object to be visited and pending to be written. Called by Visitor implementations.
     *
     * @see Visitor@visitObject()
     */
    void addPendingObject(Object object);
}
