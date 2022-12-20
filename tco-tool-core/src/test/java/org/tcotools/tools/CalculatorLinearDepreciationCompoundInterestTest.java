package org.tcotools.tools;

/*
 * Copyright (C) 2004-2006 Peter Hirzel softEnvironment (http://www.softenvironment.ch).
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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.FactCost;
import org.tcotool.model.TcoObject;
import org.tcotool.tools.CalculatorLinearDepreciationCompountInterest;

/**
 * Tests org.tcotool.tools.Calculator
 *
 * @author Peter Hirzel
 */
public class CalculatorLinearDepreciationCompoundInterestTest {

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

    @Test
    public void depreciationWithInterestRate() {
        try {
            double amount = 1000.0;
            long duration = 48;
            double interest = 5.0; // [%]
            m.utility.getSystemParameter().setDepreciationInterestRate(Double.valueOf(interest));
            FactCost fcost = (FactCost) m.utility.createTcoObject(m.server, FactCost.class);
            m.utility.addOwnedElement(m.clientDriver, fcost);
            fcost.setMultitude(Double.valueOf(1.0));
            fcost.setAmount(Double.valueOf(amount));
            fcost.setDepreciationDuration(Long.valueOf(duration));

            CalculatorLinearDepreciationCompountInterest c = new CalculatorLinearDepreciationCompountInterest(m.utility, (TcoObject) m.utility.getRoot(),
                    duration);
            // Calculator c = new Calculator(m.utility, (TcoObject)m.utility.getRoot(), duration);
            java.util.List<Double> list = c.getCostBlock(/* getDepreciationCostBlock( */m.clientService /* , Calculator.COST_CAUSE_UNDEFINED */);
            double total = list.get(1 /* FC-Total */).doubleValue(); // 120'000.0
            Assert.assertEquals("Total FactCost", total, m.utility.getMultitudeFactor(fcost, true) * amount, 0.0);

            double year = total - total / (duration / 12.0); // 90'000.0
            total = year + total / 100.0 * interest; // 96'000.0
            Assert.assertEquals("1.Year", (long) total, (long) list.get(2).doubleValue());

            year = year /* should be total */ - year /* should be total */ / ((duration - 12.0) / 12.0); // 60'000.0
            total = year + total / 100.0 * interest;// 64'800.0
            Assert.assertEquals("2.Year", (long) total, (long) list.get(3).doubleValue());

            year = year - year / ((duration - 12.0 - 12.0) / 12.0);
            total = year + total / 100.0 * interest; // 33'240
            Assert.assertEquals("3.Year", (long) total, (long) list.get(4).doubleValue());

            year = year - year / ((duration - 12.0 - 12.0 - 12.0) / 12.0);
            total = year + total / 100.0 * interest; // 1'662
            Assert.assertEquals("4.Year", (long) total, (long) list.get(5).doubleValue());
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void depreciationWithoutInterest() {
        try {
            double amount = 1000.0;
            long duration = 48;
            m.utility.getSystemParameter().setDepreciationInterestRate(Double.valueOf(0.0));
            FactCost fcost = (FactCost) m.utility.createTcoObject(m.server, FactCost.class);
            m.utility.addOwnedElement(m.clientDriver, fcost);
            fcost.setMultitude(Double.valueOf(1.0));
            fcost.setAmount(Double.valueOf(amount));
            fcost.setDepreciationDuration(Long.valueOf(duration));

            // Calculator c = new Calculator(m.utility, (TcoObject)m.utility.getRoot(), duration);
            CalculatorLinearDepreciationCompountInterest c = new CalculatorLinearDepreciationCompountInterest(m.utility, (TcoObject) m.utility.getRoot(),
                    duration);
            // java.util.List list = c.getDepreciationCostBlock(m.service);
            java.util.List<Double> list = c.getCostBlock(/* getDepreciationCostBlock( */m.clientService /* , Calculator.COST_CAUSE_UNDEFINED */);
            double total = list.get(1 /* FactCost-Total */).doubleValue(); // 120'000.0
            Assert.assertEquals("Total FactCost", total, m.utility.getMultitudeFactor(fcost, true) * amount, 0.0);

            double year = total - total / (duration / 12.0);
            total = year + total / 100.0 * 0.0;
            Assert.assertEquals("1.Year", (long) total, (long) list.get(2).doubleValue());

            year = total - total / ((duration - 12.0) / 12.0);
            total = year + total / 100.0 * 0.0;
            Assert.assertEquals("2.Year", (long) total, (long) list.get(3).doubleValue());

            year = total - total / ((duration - 12.0 - 12.0) / 12.0);
            total = year + total / 100.0 * 0.0;
            Assert.assertEquals("3.Year", (long) total, (long) list.get(4).doubleValue());

            Assert.assertEquals("4.Year", (long) 0, (long) list.get(5).doubleValue());
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }
}
