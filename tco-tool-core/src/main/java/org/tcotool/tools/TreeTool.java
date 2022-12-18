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

import ch.softenvironment.util.DeveloperException;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;

/**
 * Utility to walk a (Sub)-Tree within a TcoModel-configuration.
 *
 * @author Peter Hirzel
 */
public class TreeTool {

	private final TreeToolListener listener;

	/**
	 * Register Listener for treating walked nodes and leaves.
	 *
	 * @param listener
	 */
	public TreeTool(TreeToolListener listener) {
		super();
		this.listener = listener;
	}

	/**
	 * Walk the given root (TcoObject) recursively down its subtree. At any TcoObject found a TreeToolListener#tree*() Method is fired.
	 *
	 * @param root
	 */
	public void walkTree(TcoObject root) {
		if (root instanceof TcoPackage) {
			listener.treatTcoPackage((TcoPackage) root);

			// walk sub-groups (recursively)
			java.util.Iterator<TcoPackage> itGroup = ((TcoPackage) root).getOwnedElement().iterator();
			while (itGroup.hasNext()) {
				walkTree(itGroup.next());
			}

			// walk Service's
			java.util.Iterator<Service> itService = ((TcoPackage) root).getService().iterator();
			while (itService.hasNext()) {
				walkTree(itService.next());
			}
		} else if (root instanceof Service) {
			listener.treatService((Service) root);

			java.util.Iterator<CostDriver> it = ((Service) root).getDriver().iterator();
			while (it.hasNext()) {
				CostDriver driver = it.next();
				listener.treatCostDriver(driver);

				java.util.Iterator<Cost> itc = driver.getCost().iterator();
				while (itc.hasNext()) {
					Cost cost = itc.next();
					if (cost instanceof PersonalCost) {
						listener.treatPersonalCost((PersonalCost) cost);
					} else {
						listener.treatFactCost((FactCost) cost);
					}
				}
			}
		} else {
			throw new DeveloperException("TcoPackage or Service expected");
		}
	}
}
