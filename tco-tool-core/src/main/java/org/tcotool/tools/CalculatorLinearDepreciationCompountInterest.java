package org.tcotool.tools;

/*
 * Copyright (C) 2004-2008 Peter Hirzel softEnvironment (http://www.softenvironment.ch).
 * All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import ch.softenvironment.math.FinancialUtils;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.Responsibility;
import org.tcotool.model.Service;
import org.tcotool.model.ServiceCategory;
import org.tcotool.model.TcoObject;

/**
 * Calculate Costs of TCO-Configuration by storing Depreciation-costs for each Service individually.
 * <p>
 * Calculates: Depreciation PLUS Interest => compound interest (de: Zinseszins)
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class CalculatorLinearDepreciationCompountInterest extends Calculator {

    /**
     * @see #Calculator(ModelUtility, TcoObject, long, ServiceCategory, Responsibility)
     */
    public CalculatorLinearDepreciationCompountInterest(ModelUtility utility, TcoObject rootObject, long maxDurationMonths) {
        super(utility, rootObject, maxDurationMonths);
    }

    /**
     * Accumulate the Depreciation-Costs for given cost.
     */
    @Override
    protected void calculate(Service service, CostDriver driver, Cost cost, double capital) {
        if (cost instanceof FactCost) {
            FactCost fCost = (FactCost) cost;
            long depreciationDuration = fCost.getDepreciationDuration() == null ? 0 : fCost.getDepreciationDuration().longValue();

            if (depreciationDuration > 12) {
                // [0] total FactCost independent of depreciation & interest
                accumulateCodes(service, driver, fCost, INDEX_TOTAL, capital); // storeIntoCodeList(serviceMap,
                // KIND_FC,
                // DEPRECIATION_LINEAR_COMPOUND_INTEREST,
                // INDEX_TOTAL,
                // capital);

                // [1..fCost.depreciationYears] of first period of FactCost's
                // depreciationDuration
                boolean repeatable = (fCost.getRepeatable() != null) && fCost.getRepeatable().booleanValue();
                int year = calcDepreciationPeriod(service, driver, fCost, capital, repeatable, fCost.getDepreciationDuration(), INDEX_TOTAL);
                // TODO consider baseOffset
                // [after fCost.depreciationYears years] repeatable periods
                if (repeatable && (year < getDurationYears())) {
                    // repeat the costs until maxDuration
                    while (year < getDurationYears()) {
                        year = calcDepreciationPeriod(service, driver, fCost, capital, true, fCost.getDepreciationDuration(), year);
                    }
                }
            }
        }
    }

    /**
     * Calculate the depreciation over a whole Duration Period of FactCost. Algorithm (rather non-conform method): - Linear Depreciation, where Interests are added to last year additionally
     *
     * @param costCapital
     * @param costDuration
     * @return year of last Cost-entry
     */
    private int calcDepreciationPeriod(Service service, CostDriver driver, Cost cost, double costCapital, boolean repeatable, Long costDuration, int yearIndex) {
        long completeYears = costDuration.longValue() / 12;
        double duration = costDuration.doubleValue();
        int year = 0;

        if (duration <= 12.0) {
            // add simply the interest of last year
            double yearlyCapital = 0.0;
            if (repeatable) {
                // re-invest capital
                yearlyCapital = costCapital;
            }
            // plus interest year before which was the same capital
            yearlyCapital += FinancialUtils.calcInterest(costCapital, utility.getInterestRate());
            accumulateCodes(service, driver, cost, year + yearIndex + 1, yearlyCapital); // storeIntoCodeList(serviceMap,
            // KIND_FC,
            // DEPRECIATION_LINEAR_COMPOUND_INTEREST,
            // year
            // +
            // yearIndex
            // +
            // 1,
            // yearlyCapital);
            year++;
            return year + yearIndex;
        }

        double capitalYearBefore = costCapital;
        for (; year < completeYears; year++) {
            // depreciation over full year
            double yearlyCapital = costCapital / duration * (duration - ((year + 1.0) * 12.0)); // capitalYearBefore
            // would
            // add
            // 'interest
            // on
            // interest'
            // add interest from last Year
            yearlyCapital += FinancialUtils.calcInterest(capitalYearBefore, utility.getInterestRate());
            accumulateCodes(service, driver, cost, year + yearIndex + 1, yearlyCapital); // storeIntoCodeList(serviceMap,
            // KIND_FC,
            // DEPRECIATION_LINEAR_COMPOUND_INTEREST,
            // year
            // +
            // yearIndex
            // +
            // 1,
            // yearlyCapital);
            capitalYearBefore = yearlyCapital;
        }
        long partialMonthInYear = costDuration.longValue() % 12;
        if (partialMonthInYear > 0) {
            // depreciation over fraction part of year should be 0
            double yearlyCapital = costCapital / duration * (duration - ((year + 1.0) * 12.0) + (double) partialMonthInYear); // capitalYearBefore
            // would
            // add
            // 'interest
            // on
            // interest'

            // add interest from last Year
            yearlyCapital += FinancialUtils.calcInterest(capitalYearBefore, utility.getInterestRate());
            accumulateCodes(service, driver, cost, year + yearIndex + 1, yearlyCapital); // storeIntoCodeList(serviceMap,
            // KIND_FC,
            // DEPRECIATION_LINEAR_COMPOUND_INTEREST,
            // year
            // +
            // yearIndex
            // +
            // 1,
            // yearlyCapital);
            capitalYearBefore = yearlyCapital;
            year++;
        }
        return year + yearIndex;
    }
}