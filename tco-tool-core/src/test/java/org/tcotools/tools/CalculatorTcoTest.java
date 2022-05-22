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
import org.tcotool.model.Dependency;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Tests org.tcotool.tools.CalculatorTco
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class CalculatorTcoTest {

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
		// javax.jdo.PersistenceManagerFactory factory =
		// m.server.getPersistenceManagerFactory();
		m.server.close();
		// factory.close();
	}

	@Test
	public void multitudeFactor() {
		Assert.assertTrue("Multitude model", ModelUtility.getMultitudeFactor(m.model) == 2.0);
		Assert.assertTrue("Multitude group", ModelUtility.getMultitudeFactor(m.group) == 3.0);
		Assert.assertTrue("Multitude service", ModelUtility.getMultitudeFactor(m.clientService) == 4.0);
		Assert.assertTrue("Multitude driver", ModelUtility.getMultitudeFactor(m.clientDriver) == 5.0);

		Assert.assertTrue("Multitude driver", m.utility.getMultitudeFactor(m.clientDriver, true) == m.getClientDriverFactor());

		try {
			PersonalCost pcost = (PersonalCost) m.utility.createTcoObject(m.server, PersonalCost.class);
			Assert.assertTrue("Multitude DEFAULT", ModelUtility.getMultitudeFactor(pcost) == 1.0);

			pcost.setMultitude(null);
			Assert.assertTrue("Multitude lazy init", ModelUtility.getMultitudeFactor(pcost) == 0.0);

			m.utility.addOwnedElement(m.clientDriver, pcost);
			Assert.assertTrue("Multitude driver", m.utility.getMultitudeFactor(pcost, true) == 0.0);
			pcost.setMultitude(new Double(6.0));
			Assert.assertTrue("Multitude driver", m.utility.getMultitudeFactor(pcost, true) == m.getClientDriverFactor() * 6.0);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * PersonalCost with internal hours
	 *
	 * @see Calculator#getPersonalHours()
	 */
	@Test
	public void tcoPersonalHoursInternal() {
		try {
			double hours = 65.0;
			double total = m.getClientDriverFactor() * 6.0 * hours;
			PersonalCost pcost = m.addClientPersonalCost();
			// pcost.setAmount(new Double(195));
			pcost.setInternal(Boolean.TRUE);
			pcost.setHours(new Double(hours));

			Calculator c = new CalculatorTco(m.utility);
			double val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_INTERNAL /* "INTERNAL" */),
				Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal", val == total);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_EXTERNAL /* "EXTERNAL" */), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_INTERNAL_LUMP /* "INTERNAL_LUMP" */),
				Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal Lump", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_EXTERNAL_LUMP /* "EXTERNAL_LUMP" */),
				Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External Lump", val == 0.0);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * PersonalCost with internal hours
	 *
	 * @see Calculator#getPersonalHours()
	 */
	@Test
	public void tcoPersonalHoursExternal() {
		try {
			double hours = 65.0;
			double total = m.getClientDriverFactor() * 6.0 * hours;
			PersonalCost pcost = m.addClientPersonalCost();
			pcost.setAmount(new Double(195));
			pcost.setInternal(Boolean.FALSE);
			pcost.setHours(new Double(hours));

			Calculator c = new CalculatorTco(m.utility);
			double val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_INTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_EXTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External", val == total);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_INTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal Lump", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_EXTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External Lump", val == 0.0);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * PersonalCost with internal amount only => Lump-sum
	 *
	 * @see Calculator#getPersonalHours()
	 */
	@Test
	public void tcoPersonalCostLumpInternal() {
		try {
			double amount = 195.0;
			double total = m.getClientDriverFactor() * 6.0 * amount;
			PersonalCost pcost = m.addClientPersonalCost();
			pcost.setAmount(new Double(amount));
			pcost.setInternal(Boolean.TRUE);

			Calculator c = new CalculatorTco(m.utility);
			double val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_INTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_EXTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_INTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal Lump", val == total);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_EXTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External Lump", val == 0.0);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * PersonalCost with external amount only => Lump-sum
	 *
	 * @see Calculator#getPersonalHours()
	 */
	@Test
	public void tcoPersonalCostLumpExternal() {
		try {
			double amount = 195.0;
			double total = m.getClientDriverFactor() * 6.0 * amount;
			PersonalCost pcost = m.addClientPersonalCost();
			pcost.setAmount(new Double(amount));
			pcost.setInternal(Boolean.FALSE);

			Calculator c = new CalculatorTco(m.utility);
			double val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_INTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_EXTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_INTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal Lump", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_EXTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External Lump", val == total);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void tcoPersonalRoleFTE() {
		try {
			// double total = m.getDriverFactor() * 6.0 * amount;
			PersonalCost pcost = m.addClientPersonalCost();
			pcost.setRole(m.role);
			ModelUtility.updateRole(pcost);
			double hours = m.getClientDriverFactor() * 6.0 * m.role.getYearlyHours().doubleValue();

			Calculator c = new CalculatorTco(m.utility);
			double val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_INTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal", val == hours);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_EXTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_INTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal Lump", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_EXTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External Lump", val == 0.0);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void tcoPersonalRoleHours() {
		try {
			// double total = m.getDriverFactor() * 6.0 * amount;
			PersonalCost pcost = m.addClientPersonalCost();
			pcost.setRole(m.role);
			pcost.setHours(new Double(13.0));
			ModelUtility.updateRole(pcost);
			double hours = m.getClientDriverFactor() * 6.0 * 13.0;

			Calculator c = new CalculatorTco(m.utility);
			double val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_INTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal", val == hours);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_EXTERNAL), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_INTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC Internal Lump", val == 0.0);
			val = Calculator.getValue(c.getCosts(m.model, Calculator.KIND_PC, Calculator.PERSONAL_COST_EXTERNAL_LUMP), Calculator.INDEX_TOTAL);
			Assert.assertTrue("PC External Lump", val == 0.0);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test the DIRECT/INDIRECT costs defined by CostCause.
	 */
	@Test
	public void tcoCostDirect() {
		try {
			Calculator c = new CalculatorTco(m.utility /* , durationMonths */);
			// PersonalCost
			double pDirect = Calculator
				.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT, Calculator.KIND_PC), Calculator.INDEX_TOTAL);
			double pIndirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.INDIRECT, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			double pUndefined = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			// FactCost
			double fDirect = Calculator
				.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT, Calculator.KIND_FC), Calculator.INDEX_TOTAL);
			double fIndirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.INDIRECT, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			double fUndefined = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			Assert.assertTrue("Personal DIRECT", MathUtils.compare(0.0, pDirect) == 0);
			Assert.assertTrue("Personal INDIRECT", MathUtils.compare(0.0, pIndirect) == 0);
			Assert.assertTrue("Personal UNDEFINED", MathUtils.compare(0.0, pUndefined) == 0);
			Assert.assertTrue("Fact DIRECT", MathUtils.compare(0.0, fDirect) == 0);
			Assert.assertTrue("Fact INDIRECT", MathUtils.compare(0.0, fIndirect) == 0);
			Assert.assertTrue("Fact UNDEFINED", MathUtils.compare(0.0, fUndefined) == 0);

			PersonalCost pCost = m.addClientPersonalCost();
			// pCost.setRole(m.role);
			// pCost.setHours(new Double(13.0));
			pCost.setAmount(new Double(100.45));
			c = new CalculatorTco(m.utility);
			pDirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */DIRECT, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			pIndirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */INDIRECT, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			pUndefined = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			fDirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */DIRECT, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			fIndirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */INDIRECT, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			fUndefined = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			Assert.assertTrue("Personal DIRECT", MathUtils.compare(0.0, pDirect) == 0);
			Assert.assertTrue("Personal INDIRECT", MathUtils.compare(0.0, pIndirect) == 0);
			Assert.assertTrue("Personal UNDEFINED", MathUtils.compare(m.getClientDriverFactor() * 6.0 * 100.45, pUndefined) == 0);
			Assert.assertTrue("Fact DIRECT", MathUtils.compare(0.0, fDirect) == 0);
			Assert.assertTrue("Fact INDIRECT", MathUtils.compare(0.0, fIndirect) == 0);
			Assert.assertTrue("Fact UNDEFINED", MathUtils.compare(0.0, fUndefined) == 0);

			FactCost fCost = m.addClientFactCost();
			fCost.setAmount(new Double(93.17));
			c = new CalculatorTco(m.utility);
			pDirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */DIRECT, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			pIndirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */INDIRECT, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			pUndefined = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			fDirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */DIRECT, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			fIndirect = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */INDIRECT, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			fUndefined = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			Assert.assertTrue("Personal DIRECT", MathUtils.compare(0.0, pDirect) == 0);
			Assert.assertTrue("Personal INDIRECT", MathUtils.compare(0.0, pIndirect) == 0);
			Assert.assertTrue("Personal UNDEFINED", MathUtils.compare(m.getClientDriverFactor() * 6.0 * 100.45, pUndefined) == 0);
			Assert.assertTrue("Fact DIRECT", MathUtils.compare(0.0, fDirect) == 0);
			Assert.assertTrue("Fact INDIRECT", MathUtils.compare(0.0, fIndirect) == 0);
			Assert.assertTrue("Fact UNDEFINED", MathUtils.compare(m.getClientDriverFactor() * 7.0 * 93.17, fUndefined) == 0);
			// TODO for real CostCause instances
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void tcoCostEstimated() {
		try {
			Calculator c = new CalculatorTco(m.utility /* , durationMonths */);
			double pEstimated = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */ESTIMATED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			double pKnown = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */KNOWN, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			double fEstimated = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */ESTIMATED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			double fKnown = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */KNOWN, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			double totalEstimated = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.ESTIMATED), Calculator.INDEX_TOTAL);
			double totalKnown = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.KNOWN), Calculator.INDEX_TOTAL);
			Assert.assertTrue("Personal ESTIMATED", MathUtils.compare(0.0, pEstimated) == 0);
			Assert.assertTrue("Personal KNOWN", MathUtils.compare(0.0, pKnown) == 0);
			Assert.assertTrue("Fact ESTIMATED", MathUtils.compare(0.0, fEstimated) == 0);
			Assert.assertTrue("Fact KNOWN", MathUtils.compare(0.0, fKnown) == 0);
			Assert.assertTrue("Total ESTIMATED", MathUtils.compare(0.0, totalEstimated) == 0);
			Assert.assertTrue("TOTAL KNOWN", MathUtils.compare(0.0, totalKnown) == 0);

			PersonalCost pCost = m.addClientPersonalCost();
			pCost.setAmount(new Double(100.0));
			pCost.setEstimated(Boolean.TRUE);
			pCost = m.addClientPersonalCost();
			pCost.setAmount(new Double(50.0));
			pCost.setEstimated(Boolean.FALSE);
			FactCost fCost = m.addClientFactCost();
			fCost.setAmount(new Double(70.0));
			fCost.setEstimated(Boolean.TRUE);
			fCost = m.addClientFactCost();
			fCost.setAmount(new Double(30.0));
			fCost.setEstimated(Boolean.FALSE);

			c = new CalculatorTco(m.utility);
			pEstimated = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */ESTIMATED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			pKnown = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* PERSONAL_ */KNOWN, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL);
			fEstimated = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */ESTIMATED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			fKnown = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator./* FACT_ */KNOWN, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL);
			totalEstimated = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.ESTIMATED), Calculator.INDEX_TOTAL);
			totalKnown = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.KNOWN), Calculator.INDEX_TOTAL);
			double pTmp = m.getClientDriverFactor() * 6.0 * 100.0;
			Assert.assertTrue("Personal ESTIMATED", MathUtils.compare(pTmp, pEstimated) == 0);
			double fTmp = m.getClientDriverFactor() * 7.0 * 70.0;
			Assert.assertTrue("Fact ESTIMATED", MathUtils.compare(fTmp, fEstimated) == 0);
			Assert.assertTrue("Total ESTIMATED", MathUtils.compare(pTmp + fTmp, totalEstimated) == 0);
			pTmp = m.getClientDriverFactor() * 6.0 * 50.0;
			Assert.assertTrue("Personal KNOWN", MathUtils.compare(pTmp, pKnown) == 0);
			fTmp = m.getClientDriverFactor() * 7.0 * 30.0;
			Assert.assertTrue("Fact KNOWN", MathUtils.compare(fTmp, fKnown) == 0);
			Assert.assertTrue("TOTAL KNOWN", MathUtils.compare(pTmp + fTmp, totalKnown) == 0);
			// TODO for real CostCause instances
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void tcoFactCostBaseOffset() {
		try {
			long durationMonths = 48;
			double perYear = durationMonths / 12;
			m.utility.getSystemParameter().setDefaultUsageDuration(Long.valueOf(durationMonths));

			FactCost fCost = m.addClientFactCost();
			fCost.setUsageDuration(Long.valueOf(durationMonths));
			fCost.setAmount(new Double(2000.0));
			fCost.setBaseOffset(Long.valueOf(0));
			fCost.setRepeatable(Boolean.FALSE);

			double total0 = m.getClientDriverFactor() * (7.0 * 2000.0);

			// 1st TCO-Year
			Calculator c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /*
			 * must be longer
			 */);
			double f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=0", MathUtils.compare(total0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=0", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=0", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("3. year; offset=0", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertTrue("4. year; offset=0", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 5);
			Assert.assertTrue("5. year; offset=0, non-repeatable", MathUtils.compare(0.0, f) == 0);

			fCost.setBaseOffset(Long.valueOf(12));
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /*
			 * must be longer
			 */);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=12", MathUtils.compare(total0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=12", MathUtils.compare(0.0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=12", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("3. year; offset=12", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertTrue("4. year; offset=12", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 5);
			Assert.assertTrue("5. year; offset=12;", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 6);
			Assert.assertTrue("6. year; offset=12; non-repeatable", MathUtils.compare(0.0, f) == 0);

			fCost.setRepeatable(Boolean.TRUE);
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /*
			 * must be longer
			 */);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=12", MathUtils.compare(total0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=12", MathUtils.compare(0.0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=12", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("3. year; offset=12", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertTrue("4. year; offset=12", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 5);
			Assert.assertTrue("5. year; offset=12;", MathUtils.compare(total0 / perYear, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 6);
			Assert.assertTrue("6. year; offset=12; repeatable=>never ending", MathUtils.compare(total0 / perYear, f) == 0);

			fCost.setBaseOffset(Long.valueOf(3)); // check offset in the middle
			// of the year
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /*
			 * must be longer
			 */);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=12", MathUtils.compare(total0, f) == 0);
			// TODO test years 1..n

			fCost.setUsageDuration(Long.valueOf(3));
			fCost.setBaseOffset(Long.valueOf(0));
			fCost.setRepeatable(Boolean.TRUE);
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /*
			 * must be longer
			 */);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("total; offset=0; usage=3 => 1x for Total", MathUtils.compare(total0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=0; usage=3 =>4x if repeatable", MathUtils.compare(total0 * 4, f) == 0);

			fCost.setRepeatable(Boolean.FALSE);
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), durationMonths + 24 /*
			 * must be longer
			 */);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("total; offset=0; usage=3 => 1x for Total", MathUtils.compare(total0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=0; usage=3 =>1x cause non-repeatable", MathUtils.compare(total0, f) == 0);
			f = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_FC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=0; usage=3 =>0x cause non-repeatable", MathUtils.compare(0.0, f) == 0);

			// TODO test repeatable 4 times in same year with baseOffset
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void tcoPersonalCostBaseOffset() {
		try {
			int tcoDurationMonths = 36;
			Long baseOffset = Long.valueOf(0);

			// PersonalCost always invested for 1 year!
			PersonalCost pCost = m.addClientPersonalCost();
			pCost.setMultitude(new Double(7.0));
			pCost.setAmount(new Double(2000.0));
			pCost.setBaseOffset(baseOffset);
			pCost.setRepeatable(Boolean.FALSE);

			double total0 = m.getClientDriverFactor() * (7.0 * 2000.0);

			// 1st TCO-Year, where duration is 36 months totally; BaseOffset=0
			Calculator c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), tcoDurationMonths);
			double p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=0", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=0", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=0", MathUtils.compare(0.0, p) == 0);

			// start after 1 year
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), tcoDurationMonths);
			baseOffset = baseOffset + 12;
			pCost.setBaseOffset(baseOffset);
			// calculate at least for 24 months
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), tcoDurationMonths);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=12", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=12", MathUtils.compare(0.0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=12", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("3. year; offset=12; non-repeatable", MathUtils.compare(0.0, p) == 0);

			// extend duration and make costs repeatable
			tcoDurationMonths = 48;
			pCost.setRepeatable(Boolean.TRUE);
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), tcoDurationMonths);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=12", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=12", MathUtils.compare(0.0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=12", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("3. year; offset=12, repeatable!!!", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 4);
			Assert.assertTrue("4. year; offset=12, repeatable!!!", MathUtils.compare(total0, p) == 0);

			// check offset in the middle of the year (after 6 months)
			tcoDurationMonths = 36;
			baseOffset = Long.valueOf(6);
			pCost.setBaseOffset(baseOffset);
			pCost.setRepeatable(Boolean.TRUE);
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), tcoDurationMonths);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("Total cost; offset=6 (same overall costs, investment just a bit later)", MathUtils.compare(total0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 1);
			Assert.assertTrue("1. year; offset=6 => only half the costs results end of year", MathUtils.compare(total0 / 2.0, p) == 0);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 2);
			Assert.assertTrue("2. year; offset=6 => full cost again, offset irrelevant in 2nd year", MathUtils.compare(total0, p) == 0);

			pCost.setRepeatable(Boolean.FALSE);
			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), tcoDurationMonths);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 3);
			Assert.assertTrue("2. year; offset=6 => no investment any more", MathUtils.compare(0.0, p) == 0);
		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/*
	 * Client(own costs 100 CHF) ----50%- Supplier (own costs 500 CHF) => Client-costs (incl. dependencies) = 100 CHF + 50% of 500 CHF = 350 CHF
	 */
	@Test
	public void tcoDependency() {
		try {
			PersonalCost pCost = m.addClientPersonalCost();
			pCost.setMultitude(new Double(4.0));
			pCost.setAmount(new Double(25.0));
			pCost.setBaseOffset(Long.valueOf(0));
			pCost.setRepeatable(Boolean.TRUE);
			double totalClientPC = m.getClientDriverFactor() * (4.0 * 25.0);

			pCost = m.addPersonalCost(m.supplierDriver);
			pCost.setMultitude(new Double(2.0));
			pCost.setAmount(new Double(250.0));
			pCost.setBaseOffset(Long.valueOf(0));
			pCost.setRepeatable(Boolean.TRUE);
			double totalSupplierPC = m.getSupplierDriverFactor() * 2.0 * 250.0;

			Calculator c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), 36);
			double p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("setup", MathUtils.compare(totalClientPC + totalSupplierPC, p) == 0);

			Dependency dependency = ModelUtility.createDependency(m.server);
			m.utility.addDependencyEnds(dependency, m.clientService, m.supplierService);
			dependency.setDistribution(Double.valueOf(50.0));

			c = new CalculatorTco(m.utility, (TcoObject) m.utility.getRoot(), 36);
			p = Calculator.getValue(c.getCodeTotal((TcoObject) m.utility.getRoot(), Calculator.DIRECT_UNDEFINED, Calculator.KIND_PC),
				Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("setup not influenced by additional Dependency", MathUtils.compare(totalClientPC + totalSupplierPC, p) == 0);
			p = Calculator.getValue(c.calculateDependency(m.supplierService), Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("supplier of Dependency has no additional costs", MathUtils.compare(0.0, p) == 0);
			// TODO check for NullPointerException
			p = Calculator.getValue(c.calculateDependency(m.clientService), Calculator.INDEX_TOTAL + 0);
			Assert.assertTrue("client gets 50% of supplier", MathUtils.compare(totalSupplierPC * 0.5, p) == 0);

		} catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	// @Test
	public void tcoOccurrence() {
		// TODO
	}
}
