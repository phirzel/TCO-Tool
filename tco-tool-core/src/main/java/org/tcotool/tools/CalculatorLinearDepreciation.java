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

import ch.softenvironment.math.MathUtils;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;

/**
 * Accounting calculator for "linear depreciation".
 *
 * @author Peter Hirzel
 */
public class CalculatorLinearDepreciation extends Calculator {

	/**
	 * Initialize calculator for given rootObject and TotalCosts only.
	 *
	 * @param utility
	 */
	public CalculatorLinearDepreciation(ModelUtility utility) {
		this(utility, (TcoObject) utility.getRoot(), 0);
	}

	/**
	 * @see Calculator
	 */
	public CalculatorLinearDepreciation(ModelUtility utility, TcoObject rootObject, long maxDurationMonths) {
		super(utility, rootObject, maxDurationMonths, null);
	}

	/**
	 * Accumulate costs of financial matters for e.g. accounting, interests, depreciation and the like for the given cost in driver and service.
	 * <p>
	 * (PersonalCost cannot be depreciated!)
	 *
	 * @param service
	 * @param driver
	 * @param cost Consider FactCost's with a depreciationDuration > 1 year
	 * @param capital (cost * factor)
	 */
	@Override
	protected void calculate(Service service, CostDriver driver, Cost cost, double capital) {
		if (cost instanceof FactCost) {
			boolean repeatable = (cost.getRepeatable() != null) && cost.getRepeatable().booleanValue();
			int baseOffset = (cost.getBaseOffset() == null ? 0 : cost.getBaseOffset().intValue());
			int baseYear = baseOffset / 12;
			long depreciationDuration = ((FactCost) cost).getDepreciationDuration() == null ? 0 : ((FactCost) cost).getDepreciationDuration().longValue();

			if (depreciationDuration > 12) {
				// 1) [0] total FactCost independent of depreciation (baseOffset
				// irrelevant)
				accumulateCodes(service, driver, cost, INDEX_TOTAL, capital); // storeIntoCodeList(serviceMap,
				// KIND_FC,
				// DEPRECIATION_LINEAR,
				// INDEX_TOTAL,
				// capital);

				// 2) [1+baseOffset..n] year
				double depreciationPerMonth = MathUtils.negate(new Double(capital / (double) depreciationDuration)).doubleValue(); // de:
				// Abschreibungsbetrag
				// per
				// month
				// FinancialUtils.calcDepreciationLinear(capital,
				// cost.getDepreciationDuration().intValue(),
				// 1);
				double currentCapital = capital;
				int year = INDEX_TOTAL + 1 + baseYear;
				// first relevant year
				accumulateCodes(service, driver, cost, year, capital); // storeIntoCodeList(serviceMap,
				// KIND_FC,
				// DEPRECIATION_LINEAR,
				// year,
				// capital);
				for (int month = 0; month < getMaxDurationMonths(); month++) {
					if ((month > 0) && (month % 12 == 0)) {
						// next year
						year++;
						// following years
						accumulateCodes(service, driver, cost, year, currentCapital); // storeIntoCodeList(serviceMap,
						// KIND_FC,
						// DEPRECIATION_LINEAR,
						// year,
						// currentCapital);
					}
					accumulateCodes(service, driver, cost, year, depreciationPerMonth); // storeIntoCodeList(serviceMap,
					// KIND_FC,
					// DEPRECIATION_LINEAR,
					// year,
					// depreciationPerMonth);

					currentCapital = currentCapital + depreciationPerMonth;
					if ((MathUtils.compare(currentCapital, 0.0) == 0) || (currentCapital < 0.0 /*
					 * should
					 * not
					 * happen
					 */)) {
						if (repeatable) {
							// re-invest
							currentCapital = capital;
							accumulateCodes(service, driver, cost, year, capital); // storeIntoCodeList(serviceMap,
							// KIND_FC,
							// DEPRECIATION_LINEAR,
							// year,
							// capital);
						} else {
							// no more depreciation: currentCapital => 0.0;
							break;
						}
					}
				}
			}
		}
	}
}
