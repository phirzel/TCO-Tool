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

import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.util.DeveloperException;
import java.util.Iterator;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.Occurance;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;

/**
 * Utility to copy a Model-Tree.
 *
 * @author Peter Hirzel
 */
public class ModelCopyTool {

	// make sure continuous copying creates continuous numbers
	private static int copyCounter = 0;
	private final ModelUtility utility;

	private ModelCopyTool(ModelUtility utility) {
		this.utility = utility;
	}

	private static void setName(TcoObject copy) {
		copy.setName("<" + (++copyCounter) + "> " + copy.getName());
	}

	/**
	 * Copy TcoPackage with sub-packages and Service's.
	 *
	 * @param original
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws Exception
	 * @see DbChangeableBean#clone()
	 */
	private TcoPackage copy(TcoPackage original) throws CloneNotSupportedException, Exception {
		// shallow clone
		TcoPackage copy = (TcoPackage) original.clone();
		setName(copy);

		Iterator<TcoPackage> groups = original.getOwnedElement().iterator();
		while (groups.hasNext()) {
			utility.addOwnedElement(copy, copy(groups.next()));
		}

		Iterator<Service> services = original.getService().iterator();
		while (services.hasNext()) {
			utility.addOwnedElement(copy, copy(services.next()));
		}

		return copy;
	}

	private Service copy(Service original) throws Exception {
		// shallow clone
		Service copy = (Service) original.clone();
		setName(copy);

		// clone aggregates
		java.util.Iterator<CostDriver> iterator = original.getDriver().iterator();
		while (iterator.hasNext()) {
			utility.addOwnedElement(copy, copy(iterator.next()));
		}
		// TODO Dependency getSupplierId

		return copy;
	}

	private CostDriver copy(CostDriver original) throws Exception {
		// shallow clone
		CostDriver copy = (CostDriver) original.clone();
		setName(copy);

		java.util.Iterator<Cost> costs = original.getCost().iterator();
		while (costs.hasNext()) {
			utility.addOwnedElement(copy, copy(costs.next()));
		}
		java.util.Iterator<Occurance> occurances = original.getOccurrance().iterator();
		while (occurances.hasNext()) {
			utility.addOwnedElement(copy, occurances.next().clone());
		}
		return copy;
	}

	private Cost copy(Cost original) throws CloneNotSupportedException {
		// shallow clone
		Cost copy = (Cost) original.clone();
		setName(copy);

		return copy;
	}

	/**
	 * Return a copy of the given original.
	 *
	 * @param original
	 * @param utility
	 * @return
	 */
	public static TcoObject copy(ModelUtility utility, TcoObject original) throws Exception {
		ModelCopyTool copyTool = new ModelCopyTool(utility);

		if (original instanceof TcoPackage) {
			return copyTool.copy((TcoPackage) original);
		} else if (original instanceof Service) {
			return copyTool.copy((Service) original);
		} else if (original instanceof CostDriver) {
			return copyTool.copy((CostDriver) original);
		} else if (original instanceof Cost) {
			return copyTool.copy((Cost) original);
		} else {
			// should not happen
			throw new DeveloperException("copy not implemented yet for: " + original.getClass());
		}
	}
}
