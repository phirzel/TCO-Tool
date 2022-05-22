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

import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.util.Tracer;
import java.util.Iterator;
import org.tcotool.model.Catalogue;
import org.tcotool.model.Cost;
import org.tcotool.model.CostCause;
import org.tcotool.model.CostDriver;
import org.tcotool.model.Dependency;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Role;
import org.tcotool.model.Service;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;

/**
 * Fix model changes for current version of the TCO-Tool against older versions.
 * <p>
 * These fixes should not be necessary if a configuration is made with the newest tool.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class FixBrokenModelTool {

	// private TcoModel model = null;
	private ModelUtility utility = null;

	/**
	 * Fix/Migrate loading of older TCO-Configurations or minor XML-Format inconsistencies or force resetting of values depending on Codes, such as Role.
	 *
	 * @param model Start with the root
	 * @see #fixModel(TcoObject, boolean)
	 */
	public static void fixModel(ModelUtility utility, TcoModel model) throws Exception {
		FixBrokenModelTool fixer = new FixBrokenModelTool();
		// fixer.model = model;
		fixer.utility = utility;

		if ((model.getClientId().size() > 0) || (model.getSupplierId().size() > 0)) {
			Tracer.getInstance()
				.logBackendCommand("Dependency to TcoModel is wrong and must be removed (bug of older TCO-Tool, sorry for this invonvenience)!");
			// TODO remove Dependency
		}

		fixer.fixCodes(model);
		fixer.fixModel(model);
	}

	/**
	 * Fix any Code model changes.
	 * <p>
	 * Fixes before V1.4.5
	 *
	 * @param model
	 */
	@Deprecated
	private void fixCodes(TcoModel model) throws Exception {
		Iterator<? extends DbCodeType> it = model.getObjectServer().retrieveCodes(CostCause.class).iterator();
		while (it.hasNext()) {
			CostCause cause = (CostCause) it.next();
			if (cause.getDirect() == null) {
				cause.setDirect(Boolean.TRUE);
			}
		}

		it = model.getObjectServer().retrieveCodes(Catalogue.class).iterator();
		while (it.hasNext()) {
			Catalogue catalogue = (Catalogue) it.next();
			if (catalogue.getExpendable() == null) {
				// not supported anymore
				catalogue.setExpendable(Boolean.FALSE);
			}
			if (catalogue.getUsageDuration() == null) {
				catalogue.setUsageDuration(Long.valueOf(utility.getUsageDuration()));
			}
			if (catalogue.getDepreciationDuration() == null) {
				catalogue.setDepreciationDuration(Long.valueOf(utility.getDepreciationDuration()));
			}
		}

		it = model.getObjectServer().retrieveCodes(Role.class).iterator();
		while (it.hasNext()) {
			Role role = (Role) it.next();
			if (role.getCurrency() == null) {
				role.setCurrency(utility.getSystemParameter().getDefaultCurrency());
			}
			if (role.getEmploymentPercentageAvailable() == null) {
				role.setEmploymentPercentageAvailable(Long.valueOf(100));
			}
			if (role.getFullTimeEquivalent() == null) {
				role.setFullTimeEquivalent(new Double(170000.0));
			}
			if (role.getHourlyRate() == null) {
				role.setHourlyRate(new Double(100.0));
			}
			if (role.getInternal() == null) {
				role.setInternal(Boolean.TRUE);
			}
			if (role.getYearlyHours() == null) {
				role.setYearlyHours(Long.valueOf(1700));
			}
		}
	}

	/**
	 * Fixes until and including V1.5.0
	 *
	 * @param owner
	 * @throws Exception
	 */
	private void fixModel(TcoObject owner) throws Exception {
		fixDependency(owner);

		if (owner instanceof TcoPackage) {
			Iterator<TcoPackage> itOwnedElement = ((TcoPackage) owner).getOwnedElement().iterator();
			while (itOwnedElement.hasNext()) {
				TcoPackage ownedElement = itOwnedElement.next();
				fixModel(ownedElement);
			}

			Iterator<Service> itService = ((TcoPackage) owner).getService().iterator();
			while (itService.hasNext()) {
				fixModel(itService.next());
			}
		} else if (owner instanceof Service) {
			Iterator<CostDriver> itCostDriver = ((Service) owner).getDriver().iterator();
			while (itCostDriver.hasNext()) {
				CostDriver driver = itCostDriver.next();
				fixDependency(driver);
				Iterator<Cost> itCost = driver.getCost().iterator();
				while (itCost.hasNext()) {
					Cost cost = itCost.next();
					fixDependency(cost);
					if (cost.getBaseOffset() == null) {
						// <= V1.4.4
						cost.setBaseOffset(Long.valueOf(0));
					}
					if ((cost instanceof FactCost) && (((FactCost) cost).getExpendable() == null)) {
						// <= V1.4.4
						((FactCost) cost).setExpendable(Boolean.FALSE);
					}
					if (cost.getEstimated() == null) {
						cost.setEstimated(Boolean.FALSE);
					}

					if (cost instanceof PersonalCost) {
						PersonalCost pCost = (PersonalCost) cost;
						if (pCost.getInternal() == null) {
							pCost.setInternal(Boolean.TRUE);
						}
						if (pCost.getRepeatable() == null) {
							pCost.setRepeatable(Boolean.TRUE);
						}
						// correct amount according to its role
						ModelUtility.updateRole(pCost);
					} else {
						FactCost fCost = (FactCost) cost;
						if (fCost.getRepeatable() == null) {
							fCost.setRepeatable(Boolean.FALSE);
						}
						// determine the max Usage for TCO-Reports
						// setMaxUsage((fCost).getUsageDuration());
						// setMaxDepreciation((fCost).getDepreciationDuration());
						ModelUtility.updateCatalogue(fCost);
					}
				}
			}
		}
	}

	/**
	 * Fixes until and including V1.5.0
	 *
	 * @param object
	 */
	private void fixDependency(TcoObject object) {
		// Service or TcoPakcage supplier
		Iterator<Dependency> itDependency = object.getSupplierId().iterator();
		while (itDependency.hasNext()) {
			Dependency dependency = itDependency.next();
			// dialog allows adding dependencies for Service's only => client ok
			// but for Supplier any TcoObject could be chosen in earlier
			// versions
			TcoObject supplier = utility.findSupplier(dependency);
			if (!((supplier instanceof Service) || (supplier instanceof TcoPackage))) {
				// <= 1.5.0 because of missing validation
				Tracer.getInstance()
					.logBackendCommand(
						"Dependency at Supplier="
							+ supplier.getName()
							+ " is not a <Service> or <Group> and must be removed for proper calculation (bug of older TCO-Tool, sorry for this invonvenience)!");
				// TODO remove Dependency
			}
			if (dependency.getVariant() == null) {
				dependency.setVariant(Boolean.FALSE);
			}
		}
		// Service-client
		itDependency = object.getClientId().iterator();
		while (itDependency.hasNext()) {
			Dependency dependency = itDependency.next();
			TcoObject client = utility.findClient(dependency);
			if (!(client instanceof Service)) {
				// <= 1.5.0 because of missing validation
				Tracer.getInstance().logBackendCommand(
					"Dependency at Client=" + client.getName()
						+ " is not a <Service> and must be removed for proper calculation (bug of older TCO-Tool, sorry for this invonvenience)!");
				// TODO remove Dependency
			}
			if (dependency.getVariant() == null) {
				dependency.setVariant(Boolean.FALSE);
			}
		}

		// TDOO DependencyValidator.validate(client, supplier, utility)
	}
}
