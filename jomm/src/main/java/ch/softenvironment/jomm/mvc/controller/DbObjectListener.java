package ch.softenvironment.jomm.mvc.controller;
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
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.implementation.DbPropertyChange;

/**
 * Listener to be notified when a DbObject's property changed.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2006-11-14 22:33:59 $
 */
public interface DbObjectListener extends java.beans.PropertyChangeListener {
    // Consistency-Rules
    //	public static final int MANDATORY = 0;

    /**
     * Register a Property which its value must be tested for consistency.
     * <p>
     * Make sure a Consistency Controller implementing this Method checks property at each Change-Event at least for the generic DbDescriptor mappings.
     *
     * @param change Object#property to be validated
     * @param ownerEntry (if aggregated the owner might define some dependent checks)
     * @see DbPropertyChange#getInconsistencyError()
     */
    void addTest(DbPropertyChange change, DbDescriptorEntry ownerEntry);

    /**
     * Register an additional validator to check special consistency conditions for given change which are not to be assumed by generic DbDescriptor settings.
     * <p>
     * Typically a DetailView will add itself as validator for its GUI specific consistency checks. Typically this is done by DetailView via: - #initialize()  => add - #dispose()     => remove
     *
     * @param validator (for e.g. a GUI representing the change or external utility)
     * @param change DbChangeableBean to be validated
     * @see #addTest()
     * @see #removeValidator()
     */
    void addValidator(DbObjectValidator validator);

    /**
     * Unregister a Property from testing.
     *
     * @see #addTest()
     */
    void removeTest(DbPropertyChange change);

    /**
     * Unregister a validator for any changes.
     *
     * @param validator (for e.g. a GUI representing the change)
     * @see #addValidator()
     */
    void removeValidator(DbObjectValidator validator);

    /**
     * Reset Listener.
     */
    void resetActions();
}
