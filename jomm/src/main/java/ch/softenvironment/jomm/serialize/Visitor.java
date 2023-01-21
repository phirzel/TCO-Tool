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
 * Visit an Object and register it for later treatment.
 *
 * @author ce
 * @author Peter Hirzel
 * @since 1.1.1.1 (2005-04-25)
 */
public interface Visitor {

    /**
     * Called by the encoder to analyze referenced objects by given object. Implementors should call #addPendingObject(refObj) to keep track to any references to be registered as pending.
     *
     * @param object to be visited
     * @param callback
     * @see VisitorCallback#addPendingObject()
     */
    void visitObject(Object object, VisitorCallback callback);
}
