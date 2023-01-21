package ch.softenvironment.util;
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
 * Interface to specify generic evaluation for given object and property.
 *
 * @author Peter Hirzel
 * @since 1.1 (2007-02-27)
 */
public interface Evaluator {

    /**
     * Visitor-Pattern. The value given implicitely by owner's property will be evaluated and perhaps preformatted to be rendered later by a displayable component. For e.g. to display any value that
     * has to be calculated somehow and depends on other current data.
     *
     * @param owner Object having the given property
     * @param property public property of owner (#getProperty())
     * @return Object the formatted value
     */
    Object evaluate(Object owner, final String property);
}
