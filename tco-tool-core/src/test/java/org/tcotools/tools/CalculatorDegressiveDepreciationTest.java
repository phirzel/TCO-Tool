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
			m.utility.getSystemParameter().setDepreciationInterestRate(new Double(p));

			m.model.setMultitude(new Double(1.0));
			m.group.setMultitude(new Double(1.0));
			m.clientService.setMultitude(new Double(1.0));
			m.clientDriver.setMultitude(new Double(1.0));
			FactCost fCost = m.addClientFactCost();
			fCost.setMultitude(new Double(1.0));
			fCost.setDepreciationDuration(Long.valueOf(durationMonths)); // !!!
			fCost.setAmount(new Double(amount)); // Buchwert des Wirtschaftsgutes zu Beginn
			fCost.setBaseOffset(Long.valueOf(0));
			fCost.setRepeatable(Boolean.FALSE);

			Calculator c = new CalculatorDegressiveDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths);
			java.util.List<Double> list = c.getCostBlock(/* getDepreciationCostBlock( */m.clientService /* , Calculator.COST_CAUSE_UNDEFINED */);
			double total = list.get(1 /* FC-Total */).doubleValue();
			Assert.assertTrue("Total FactCost", total == m.utility.getMultitudeFactor(fCost, true) * amount);
			Assert.assertTrue("Degressive Begin", FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 0) == amount);

			Assert.assertTrue("Degressive 1.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 1) == 8000);
			Assert.assertTrue("1.Year", MathUtils.compare(8000.0, list.get(2).doubleValue()) == 0);
			Assert.assertTrue("Degressive 2.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 2) == 6400);
			Assert.assertTrue("2.Year", MathUtils.compare(6400.0, list.get(3).doubleValue()) == 0);
			Assert.assertTrue("Degressive 3.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 3) == 5120);
			Assert.assertTrue("3.Year", MathUtils.compare(5120.0, list.get(4).doubleValue()) == 0);
			Assert.assertTrue("Degressive 4.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 4) == 4096);
			Assert.assertTrue("4.Year", MathUtils.compare(4096.0, list.get(5).doubleValue()) == 0);
			Assert.assertTrue("Degressive 5.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 5) == 3276);
			Assert.assertTrue("5.Year", MathUtils.compare(3276.8, list.get(6).doubleValue()) == 0);

			amount = 120000.0;
			fCost.setAmount(new Double(amount));
			p = 5;
			m.utility.getSystemParameter().setDepreciationInterestRate(new Double(p));
			durationMonths = 48;
			fCost.setDepreciationDuration(Long.valueOf(durationMonths)); // !!!

			c = new CalculatorDegressiveDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths);
			list = c.getCostBlock(/* getDepreciationCostBlock( */m.clientService /* , Calculator.COST_CAUSE_UNDEFINED */);
			Assert.assertTrue("Degressive Begin", FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 0) == amount);
			Assert.assertTrue("Degressive 1.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 1) == 114000);
			Assert.assertTrue("1.Year", MathUtils.compare(114000.0, list.get(2).doubleValue()) == 0);
			Assert.assertTrue("Degressive 2.Year", (long) FinancialUtils.calcDepreciationGeometricDegressive(amount, p, 2) == 108300);
			Assert.assertTrue("2.Year", MathUtils.compare(108300.0, list.get(3).doubleValue()) == 0);
		} catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}
	}
}
