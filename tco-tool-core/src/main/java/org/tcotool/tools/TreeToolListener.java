package org.tcotool.tools;

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

import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoPackage;

/**
 * Interface for TreeTool when walking a Tree.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see TreeTool
 */
public interface TreeToolListener {

    void treatTcoPackage(TcoPackage group);

    void treatService(Service service);

    void treatCostDriver(CostDriver driver);

    void treatFactCost(FactCost cost);

    void treatPersonalCost(PersonalCost cost);
}