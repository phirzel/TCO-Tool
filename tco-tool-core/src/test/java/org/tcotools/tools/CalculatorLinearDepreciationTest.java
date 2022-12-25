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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.FactCost;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorLinearDepreciation;

import java.util.List;

/**
 * Tests org.tcotool.tools.CalculatorLinearDepreciation
 *
 * @author Peter Hirzel
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

            m.model.setMultitude(Double.valueOf(1.0));
            m.group.setMultitude(Double.valueOf(1.0));
            m.clientService.setMultitude(Double.valueOf(1.0));
            m.clientDriver.setMultitude(Double.valueOf(1.0));
            FactCost fCost = m.addClientFactCost();
            fCost.setMultitude(Double.valueOf(1.0));
            fCost.setDepreciationDuration(Long.valueOf(durationMonths)); // !!!
            fCost.setAmount(Double.valueOf(10000.0));
            fCost.setBaseOffset(Long.valueOf(0));
            fCost.setRepeatable(Boolean.FALSE);

            // 1st TCO-Year
            Calculator c = new CalculatorLinearDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /* must be longer */);
            List<Double> list = c.getCostBlock( /* getDepreciationCostBlock( */(TcoObject) m.utility.getRoot());
			double f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
					Calculator.INDEX_TOTAL);
			Assert.assertEquals("Total cost; offset=0", 0, MathUtils.compare(10000.0, f));
			Assert.assertEquals("Total cost; offset=0", 0, MathUtils.compare(10000.0, list.get(1 /* FC-Totall */).doubleValue()));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertEquals("1. year; offset=0", 0, MathUtils.compare(8000.0, f));
			Assert.assertEquals("1. year; offset=0", 0, MathUtils.compare(8000.0, list.get(2).doubleValue()));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertEquals("2. year; offset=0", 0, MathUtils.compare(6000.0, f));
			Assert.assertEquals("2. year; offset=0", 0, MathUtils.compare(6000.0, list.get(3).doubleValue()));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertEquals("3. year; offset=0", 0, MathUtils.compare(4000.0, f));
			Assert.assertEquals("3. year; offset=0", 0, MathUtils.compare(4000.0, list.get(4).doubleValue()));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertEquals("4. year; offset=0", 0, MathUtils.compare(2000.0, f));
			Assert.assertEquals("4. year; offset=0", 0, MathUtils.compare(2000.0, list.get(5).doubleValue()));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 5);
			Assert.assertEquals("5. year; offset=0", 0, MathUtils.compare(0.0, f));
			Assert.assertEquals("5. year; offset=0", 0, MathUtils.compare(0.0, list.get(6).doubleValue()));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 6);
			Assert.assertEquals("6. year => no more depreciatable; offset=0", 0, MathUtils.compare(0.0, f));
			// assertTrue("6. year => no more depreciatable; offset=0", MathUtils.compare(0.0, ((Double)list.get(6)).doubleValue()) == 0);

			// TODO test repeatable;
			fCost.setRepeatable(Boolean.TRUE);
			c = new CalculatorLinearDepreciation(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /* must be longer */);
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
					Calculator.INDEX_TOTAL);
			Assert.assertEquals("Total cost; offset=0", 0, MathUtils.compare(10000.0, f));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertEquals("1. year; offset=0", 0, MathUtils.compare(8000.0, f));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertEquals("2. year; offset=0", 0, MathUtils.compare(6000.0, f));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertEquals("3. year; offset=0", 0, MathUtils.compare(4000.0, f));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertEquals("4. year; offset=0", 0, MathUtils.compare(2000.0, f));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 5);
			Assert.assertEquals("5. year => RE-INVEST; offset=0", 0, MathUtils.compare(10000.0, f));
			f = Calculator.getValue(c.getCosts((TcoObject) m.utility.getRoot(), Calculator.KIND_FC, Calculator.COST_CAUSE_UNDEFINED),
				Calculator.INDEX_TOTAL + 6);
			Assert.assertEquals("6. year => RE-INVEST; offset=0", 0, MathUtils.compare(8000.0, f));

			// TODO test baseOffset;
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
