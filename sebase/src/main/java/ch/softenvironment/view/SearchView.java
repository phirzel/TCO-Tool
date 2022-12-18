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
 * Method-Set for a SearchView
 * <p>
 * see ApplicationFrame (as a Parent-Class)
 *
 * @author Peter Hirzel
 */
public interface SearchView extends ListMenuChoice {

    /**
     * Assign the selected Objects in a SearchTable to the caller.
     *
     * see DbTableModel
     */
    void assignObjects();

    /**
     * A SearchView usually offers a set of Query-Fields to make the searching of objects more accurate and performant. Therefore a reset of all SearchArguments may be initialized here.
     */
    void resetSearchArguments();

    /**
     * Search for Objects.
     *
     * see DbTableModel#setQuery(..)
     */
    void searchObjects();
}
