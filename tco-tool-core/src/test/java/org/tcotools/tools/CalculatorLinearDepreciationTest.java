package org.tcotools.tools;

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
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.FactCost;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorLinearDepreciation;

/**
 * Tests org.tcotool.tools.CalculatorLinearDepreciation
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class CalculatorLinearDepreciationTest {

	private TestModel m = null;

	/**
	 * Create a Test-Model as: TcoModel [2*] -> TcoPackage [3*] -> Service [4*] -> CostDriver driver [5*]
	 */
	@Before
	public void setUp() {
		try {
			m = new TestModel();
		} catch (Exception e) {
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
	 * Test depreciation
	 */
	@Test
	public void financeLinearDepreciation() {
		try {
			long durationMonths = 60;
			m.utility.getSystemParameter().setDefaultUsageDuration(Long.valueOf(durationMonths));

			m.model.setMultitude(new Double(1.0));
			m.group.setMultitude(new Double(1.0));
			m.clientService.setMultitude(new Double(1.0));
			m.clientDriver.setMultitude(new Double(1.0));
			FactCost fCost = m.addClientFactCost();
			fCost.setMultitude(new Double(1.0));
			fCost.setDepreciationDuration(Long.valueOf(durationMonths)); // !!!
			fCost.setAmount(new Double(10000.0));
			fCost.setBaseOffset(Long.valueOf(0));
			fCost.setRepeatable(Boolean.FALSE);

			// 1st TCO-Year
			Calculator c = new CalculatorLinearDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /* must be longer */);
			List<Double> list = c.getCostBlock( /* getDepreciationCostBlock( */(TcoObject) m.utility.getRoot());
			double f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=0", MathUtils.compare(10000.0, f) == 0);
			Assert.assertTrue("Total cost; offset=0", MathUtils.compare(10000.0, list.get(1 /* FC-Totall */).doubleValue()) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=0", MathUtils.compare(8000.0, f) == 0);
			Assert.assertTrue("1. year; offset=0", MathUtils.compare(8000.0, list.get(2).doubleValue()) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=0", MathUtils.compare(6000.0, f) == 0);
			Assert.assertTrue("2. year; offset=0", MathUtils.compare(6000.0, list.get(3).doubleValue()) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("3. year; offset=0", MathUtils.compare(4000.0, f) == 0);
			Assert.assertTrue("3. year; offset=0", MathUtils.compare(4000.0, list.get(4).doubleValue()) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertTrue("4. year; offset=0", MathUtils.compare(2000.0, f) == 0);
			Assert.assertTrue("4. year; offset=0", MathUtils.compare(2000.0, list.get(5).doubleValue()) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 5);
			Assert.assertTrue("5. year; offset=0", MathUtils.compare(0.0, f) == 0);
			Assert.assertTrue("5. year; offset=0", MathUtils.compare(0.0, list.get(6).doubleValue()) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 6);
			Assert.assertTrue("6. year => no more depreciatable; offset=0", MathUtils.compare(0.0, f) == 0);
			// assertTrue("6. year => no more depreciatable; offset=0", MathUtils.compare(0.0, ((Double)list.get(6)).doubleValue()) == 0);

			// TODO test repeatable;
			fCost.setRepeatable(Boolean.TRUE);
			c = new CalculatorLinearDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /* must be longer */);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=0", MathUtils.compare(10000.0, f) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=0", MathUtils.compare(8000.0, f) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=0", MathUtils.compare(6000.0, f) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("3. year; offset=0", MathUtils.compare(4000.0, f) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertTrue("4. year; offset=0", MathUtils.compare(2000.0, f) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 5);
			Assert.assertTrue("5. year => RE-INVEST; offset=0", MathUtils.compare(10000.0, f) == 0);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 6);
			Assert.assertTrue("6. year => RE-INVEST; offset=0", MathUtils.compare(8000.0, f) == 0);

			// TODO test baseOffset;
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
