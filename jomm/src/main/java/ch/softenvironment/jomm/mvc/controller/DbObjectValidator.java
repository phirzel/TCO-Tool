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

import ch.softenvironment.jomm.implementation.DbPropertyChange;

/**
 * Definition for a User specific ConsistencyController. Register a DbObjectValidator in DbObjectListener.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.1.1.1 $ $Date: 2005-04-25 10:01:20 $
 * @see DbObjectListener#addValidator()
 */
public interface DbObjectValidator {

    /**
     * Check whether object#property defined by change is consistent. If not return a User-friendly message.
     *
     * @return inconsistency message (null if correct)
     */
    String checkConsistency(DbPropertyChange change);
}
