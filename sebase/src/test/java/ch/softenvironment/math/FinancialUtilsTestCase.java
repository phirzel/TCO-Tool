package ch.softenvironment.math;

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

import junit.framework.TestCase;

/**
 * TestCase for FinancialUtils.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.3 $ $Date: 2008-08-04 20:32:52 $
 */
public class FinancialUtilsTestCase extends TestCase {

    /**
     * Check with sample: http://www.harri-deutsch.de/verlag/titel/pfeifer/k01_1736.pdf
     */
    public void testCalcDepreciationLinear() {
        double value = 10000; // Buchwert des Wirtschaftsgutes zu Beginn
        int usageDuration = 5; // years

        assertTrue("Linear Begin", FinancialUtils.calcDepreciationLinear(value, usageDuration, 0) == value);
        assertTrue("Linear 1.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 1) == 8000.0);
        assertTrue("Linear 2.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 2) == 6000.0);
        assertTrue("Linear 3.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 3) == 4000.0);
        assertTrue("Linear 4.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 4) == 2000.0);
        assertTrue("Linear 5.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 5) == 0.0);
        assertTrue("Linear after depreciation", FinancialUtils.calcDepreciationLinear(value, usageDuration, 6) == 0.0);

        // same in months
        usageDuration = 5 * 12; // years

        assertTrue("Linear Begin", FinancialUtils.calcDepreciationLinear(value, usageDuration, 0 * 12) == value);
        assertTrue("Linear 1.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 1 * 12) == 8000.0);
        assertTrue("Linear 2.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 2 * 12) == 6000.0);
        assertTrue("Linear 3.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 3 * 12) == 4000.0);
        assertTrue("Linear 4.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 4 * 12) == 2000.0);
        assertTrue("Linear 5.Year", FinancialUtils.calcDepreciationLinear(value, usageDuration, 5 * 12) == 0.0);
        assertTrue("Linear after depreciation", FinancialUtils.calcDepreciationLinear(value, usageDuration, 6 * 12) == 0.0);
    }

    /**
     * Check with sample: http://www.harri-deutsch.de/verlag/titel/pfeifer/k01_1736.pdf
     */
    public void testCalcDepreciationGeometricDegressive() {
        double value = 10000; // Buchwert des Wirtschaftsgutes zu Beginn
        double p = 20; // %-Satz der jaehrlich vom Buchwert abgeschrieben werden soll

        assertTrue("Degressive Begin", FinancialUtils.calcDepreciationGeometricDegressive(value, p, 0) == value);
        assertTrue("Degressive 1.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(value, p, 1) == 8000);
        assertTrue("Degressive 2.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(value, p, 2) == 6400);
        assertTrue("Degressive 3.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(value, p, 3) == 5120);
        assertTrue("Degressive 4.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(value, p, 4) == 4096);
        assertTrue("Degressive 5.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(value, p, 5) == 3276);

        value = 120000;
        p = 5;
        assertTrue("Degressive Begin", FinancialUtils.calcDepreciationGeometricDegressive(value, p, 0) == value);
        assertTrue("Degressive 1.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(value, p, 1) == 114000);
        assertTrue("Degressive 2.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(value, p, 2) == 108300);
    }

    public void testCalcInterest() {
        double amount = 100.0;
        double interest = 5;
        assertTrue("Degressive Begin", MathUtils.compare(FinancialUtils.calcInterest(amount, 5), amount / 100.0 * interest) == 0);
    }
}
