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

import ch.softenvironment.math.FinancialUtils;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;

/**
 * Accounting calculator for "linear depreciation with compound interest".
 *
 * @author Peter Hirzel
 */
public class CalculatorDegressiveDepreciation extends Calculator {

	/**
	 * Initialize calculator for given rootObject and TotalCosts only.
	 *
	 * @param utility
	 */
	public CalculatorDegressiveDepreciation(ModelUtility utility) {
		this(utility, (TcoObject) utility.getRoot(), 0);
	}

	/**
	 * @see Calculator
	 */
	public CalculatorDegressiveDepreciation(ModelUtility utility, TcoObject rootObject, long maxDurationMonths) {
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
			FactCost fCost = (FactCost) cost;
			// boolean repeatable = (cost.getRepeatable() != null) &&
			// cost.getRepeatable().booleanValue();
			// int baseOffset = (cost.getBaseOffset() == null ? 0 :
			// cost.getBaseOffset().intValue());
			// int baseYear = baseOffset / 12;
			long depreciationDuration = fCost.getDepreciationDuration() == null ? 0 : fCost.getDepreciationDuration().longValue();

			if (depreciationDuration > 12) {
				// 1) [0] total FactCost independent of depreciation (baseOffset
				// irrelevant)
				accumulateCodes(service, driver, cost, INDEX_TOTAL, capital); // storeIntoCodeList(serviceMap,
				// KIND_FC,
				// DEPRECIATION_LINEAR,
				// INDEX_TOTAL,
				// capital);

				// 2) [1+baseOffset..n] year
				// [1..fCost.depreciationYears] of first period of FactCost's
				// depreciationDuration
				int year = calcDepreciation(service, driver, fCost, capital, INDEX_TOTAL);

				// [after fCost.depreciationYears years] repeatable periods
				if ((cost.getRepeatable() != null) && cost.getRepeatable().booleanValue() && (year < getDurationYears())) {
					// repeat the costs until maxDuration
					while (year < getDurationYears()) {
						// TODO Check: probably declining depreciation costs
						// from duration before are not calculated further on,
						// which would go infinitely down to 0
						year = calcDepreciation(service, driver, fCost, capital, year);
					}
				}
			}
		}
	}

	/**
	 * Calculate the <b>linear depreciation</b> over the whole FactCost-DepreciationDuration. Algorithm: - Linear Depreciation
	 *
	 * @param costCapital
	 * @return year of last Cost-entry
	 */
	private int calcDepreciation(Service service, CostDriver driver, FactCost cost, double costCapital, int yearIndex) {
		int duration = cost.getDepreciationDuration().intValue();
		int completeYears = duration / 12;
		if (duration % 12 > 0) {
			// consider a partial duration after last complete year as one more
			// year
			completeYears++;
		}

		int year = 0;
		for (; year < completeYears; year++) {
			int period = year + 1;
			double amount = FinancialUtils.calcDepreciationGeometricDegressive(costCapital, utility.getInterestRate(), period); // [de]
			// Buchwert
			// depreciation over full year
			accumulateCodes(service, driver, cost, yearIndex + period, amount);
		}

		return year + yearIndex;
	}
}
