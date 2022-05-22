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
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;

/**
 * Calculator for specific TCO-Calculations.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class CalculatorTco extends Calculator {

	/**
	 * Make calculations without any TCO usageDuration!
	 * <p>
	 * Important: do not make any calculations for a specific TCO-year relying on this constructor!!! Useful for general overall estimations ONLY!
	 *
	 * @param utility
	 * @see org.tcotool.tools.Calculator#calculateOverallCosts(TcoObject)
	 */
	public CalculatorTco(ModelUtility utility) {
		this(utility, (TcoObject) utility.getRoot(), 0);
	}

	/**
	 * Make calculations for the range of TCO-years given by maxDurationMonths!
	 * <p>
	 * Each year might result in separate calculations figures according to the given configuration.
	 *
	 * @param utility
	 */
	public CalculatorTco(ModelUtility utility, TcoObject rootObject, long maxDurationMonths) {
		this(utility, rootObject, maxDurationMonths, null);
	}

	public CalculatorTco(ModelUtility utility, TcoObject rootObject, long maxDurationMonths, DbCodeType maskCode) {
		super(utility, rootObject, maxDurationMonths, maskCode);
	}

	/**
	 * Accumulate the TCO-Costs for given cost' CostType:
	 *
	 * @param driver owner of given cost
	 * @param cost
	 * @param totalCost (costs * factor)
	 */
	@Override
	protected void calculate(Service service, CostDriver driver, Cost cost, double totalCost) {
		// double totalCost = cost.getAmount().doubleValue() * factor;
		boolean repeatable = (cost.getRepeatable() != null) && cost.getRepeatable().booleanValue();
		int baseOffset = cost.getBaseOffset().intValue();
		int baseYear = baseOffset / 12;
		int year = INDEX_TOTAL + 1 + baseYear;

		if (cost instanceof PersonalCost) { // usage=12 months!
			// 1) [0] total PersonalCost for a year (baseOffset irrelevant)
			accumulateCodes(service, driver, cost, INDEX_TOTAL, totalCost);

			// [1..n years] personal effort is the SAME for each following
			// TCO-Year (usage=12 months!)
			/*
			 * if (repeatable) { calcTcoRepeatable(service, driver, cost,
			 * PERSONAL_TCO, totalCost, baseOffset); } else {
			 */
			// 2) calculate [1+baseOffset] year
			if (baseOffset % 12 == 0) {
				accumulateCodes(service, driver, cost, year++, totalCost);
			} else {
				// treat first year partially ONLY!
				double part = totalCost / 12.0 * (12.0 - (baseOffset - baseYear * 12.0));
				accumulateCodes(service, driver, cost, year++, part);
				if (!repeatable) {
					// 3.1) book rest part of the costs in the following year
					accumulateCodes(service, driver, cost, year, totalCost - part);
				}
			}
			// 3.2) [2+baseOffset..n] years personal cost is the SAME for each
			// following TCO-Year
			if (repeatable) {
				long completeYears = getMaxDurationMonths() / 12;

				for (; year < completeYears + 1; year++) {
					accumulateCodes(service, driver, cost, year, totalCost);
				}
				// if the last year is not even in maxUsage
				long partialMonthInYear = getMaxDurationMonths() % 12;
				if (partialMonthInYear > 0) {
					// ignore partial costs in case of (baseOffset%12!=0)
					// because repeatable anyway
					accumulateCodes(service, driver, cost, year, totalCost / 12.0 * partialMonthInYear);
				}
			}
			// }
		} else {
			FactCost fCost = (FactCost) cost;
			if ((fCost.getUsageDuration() != null) && (fCost.getUsageDuration().longValue() > 0)) {
				// 1) [0] total FactCost independent of usage (baseOffset
				// irrelevant)
				accumulateCodes(service, driver, cost, INDEX_TOTAL, totalCost);

				// 2) [1+baseOffset..n] year
				double costPerMonth = totalCost / fCost.getUsageDuration().doubleValue();
				for (int month = 0; month < getMaxDurationMonths(); month++) {
					if ((month < fCost.getUsageDuration().intValue()) || repeatable) {
						if ((month > 0) && (month % 12 == 0)) {
							// next year
							year++;
						}
						accumulateCodes(service, driver, cost, year, costPerMonth);
					} else {
						break;
					}
				}
				/*
				 * // [1..n years] FactCost's divided by usage if (repeatable) {
				 * // usageDuration is in maxUsage range anyway if
				 * (fCost.getUsageDuration().longValue() < 12) { // the given
				 * cost-amount counts for whole year calcTcoRepeatable(service,
				 * driver, cost, FACT_TCO, totalCost, baseOffset); } else {
				 * calcTcoRepeatable(service, driver, cost, FACT_TCO,
				 * costPerMonth * 12.0, baseOffset); } } else { // once in
				 * maxUsage for whole UsageDuration long completeYears =
				 * fCost.getUsageDuration().longValue() / 12; double
				 * correctionCosts = 0.0; if (baseYear > 0) { // if baseOffset
				 * points to middle of year, only calculate costs partially in
				 * first relevant year double months = 12.0 -
				 * (double)(baseOffset % 12); correctionCosts = correctionCosts
				 * + costPerMonth * months; cummulateCodes(service, driver,
				 * cost, FACT_TCO, year, costPerMonth * months); year++; } for
				 * (; year<completeYears; year++) { // usage over full year
				 * correctionCosts = correctionCosts + costPerMonth * 12.0;
				 * cummulateCodes(service, driver, cost, FACT_TCO, year,
				 * costPerMonth * 12.0); } long partialMonthInYear =
				 * fCost.getUsageDuration().longValue() % 12; if
				 * (partialMonthInYear > 0) { // last Usage year adds only
				 * fraction part correctionCosts = correctionCosts +
				 * costPerMonth * (double)partialMonthInYear;
				 * cummulateCodes(service, driver, cost, FACT_TCO, year,
				 * costPerMonth * (double)partialMonthInYear); year++; } if
				 * (correctionCosts > totalCost) { // NASTY: because of
				 * baseOffset to many costs were booked in last year
				 * cummulateCodes(service, driver, cost, FACT_TCO, year - 1,
				 * totalCost - correctionCosts); } }
				 */
			}
		}
	}
}
