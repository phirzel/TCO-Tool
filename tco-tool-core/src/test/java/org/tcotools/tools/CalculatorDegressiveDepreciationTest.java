package org.tcotools.tools;

import ch.softenvironment.math.FinancialUtils;
import ch.softenvironment.math.MathUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.FactCost;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorDegressiveDepreciation;

/**
 * Tests org.tcotool.tools.CalculatorDegressiveDepreciation
 *
 * @author Peter Hirzel
 */
public class CalculatorDegressiveDepreciationTest {

	private TestModel m = null;

	/**
	 * Create a Test-Model as: TcoModel [2*] -> TcoPackage [3*] -> Service [4*] -> CostDriver driver [5*]
	 */
	@Before
	public void setUp() {
		try {
			m = new TestModel();
		} catch (Throwable e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@After
	public void tearDown() {
		// javax.jdo.PersistenceManagerFactory factory = m.server.getPersistenceManagerFactory();
		m.server.close();
		// factory.close();
	}

	/**
	 * Check with sample: http://www.harri-deutsch.de/verlag/titel/pfeifer/k01_1736.pdf
	 */
	@Test
	public void calcDepreciationGeometricDegressive() {
		try {
            long durationMonths = 60;
            double amount = 10000.0;
            m.utility.getSystemParameter().setDefaultUsageDuration(Long.valueOf(durationMonths));
            double p = 20; // %-Satz der j�hrlich vom Buchwert abgeschrieben werden soll
            m.utility.getSystemParameter().setDepreciationInterestRate(Double.valueOf(p));

            m.model.setMultitude(Double.valueOf(1.0));
            m.group.setMultitude(Double.valueOf(1.0));
            m.clientService.setMultitude(Double.valueOf(1.0));
            m.clientDriver.setMultitude(Double.valueOf(1.0));
            FactCost fCost = m.addClientFactCost();
            fCost.setMultitude(Double.valueOf(1.0));
            fCost.setDepreciationDuration(Long.valueOf(durationMonths)); // !!!
            fCost.setAmount(Double.valueOf(amount)); // Buchwert des Wirtschaftsgutes zu Beginn
            fCost.setBaseOffset(Long.valueOf(0));
            fCost.setRepeatable(Boolean.FALSE);

            Calculator c = new CalculatorDegressiveDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths);
            java.util.List<Double> list = c.getCostBlock(/* getDepreciationCostBlock( */m.clientService /* , Calculator.COST_CAUSE_UNDEFINED */);
            double total = list.get(1 /* FC-Total */).doubleValue();
			Assert.assertEquals("Total FactCost", total, m.utility.getMultitudeFactor(fCost, true) * amount, 0.0);
			Assert.assertEquals("Degressive Begin", FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 0), amount, 0.0);

			Assert.assertEquals("Degressive 1.Year", 8000, (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 1));
			Assert.assertEquals("1.Year", 0, MathUtils.compare(8000.0, list.get(2).doubleValue()));
			Assert.assertEquals("Degressive 2.Year", 6400, (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 2));
			Assert.assertEquals("2.Year", 0, MathUtils.compare(6400.0, list.get(3).doubleValue()));
			Assert.assertEquals("Degressive 3.Year", 5120, (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 3));
			Assert.assertEquals("3.Year", 0, MathUtils.compare(5120.0, list.get(4).doubleValue()));
			Assert.assertEquals("Degressive 4.Year", 4096, (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 4));
			Assert.assertEquals("4.Year", 0, MathUtils.compare(4096.0, list.get(5).doubleValue()));
			Assert.assertEquals("Degressive 5.Year", 3276, (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 5));
			Assert.assertEquals("5.Year", 0, MathUtils.compare(3276.8, list.get(6).doubleValue()));

            amount = 120000.0;
            fCost.setAmount(Double.valueOf(amount));
            p = 5;
            m.utility.getSystemParameter().setDepreciationInterestRate(Double.valueOf(p));
            durationMonths = 48;
			fCost.setDepreciationDuration(Long.valueOf(durationMonths)); // !!!

			c = new CalculatorDegressiveDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths);
			list = c.getCostBlock(/* getDepreciationCostBlock( */m.clientService /* , Calculator.COST_CAUSE_UNDEFINED */);
			Assert.assertEquals("Degressive Begin", FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 0), amount, 0.0);
			Assert.assertEquals("Degressive 1.Year", 114000, (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 1));
			Assert.assertEquals("1.Year", 0, MathUtils.compare(114000.0, list.get(2).doubleValue()));
			Assert.assertEquals("Degressive 2.Year", 108300, (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 2));
			Assert.assertEquals("2.Year", 0, MathUtils.compare(108300.0, list.get(3).doubleValue()));
		} catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}
	}
}
