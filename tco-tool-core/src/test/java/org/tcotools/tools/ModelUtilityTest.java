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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.*;
import org.tcotool.tools.ModelUtility;

/**
 * TestCase for {@link ModelUtility}.
 *
 * @author Peter Hirzel
 */
public class ModelUtilityTest {

    private TestModel m = null;

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
        //      javax.jdo.PersistenceManagerFactory factory = m.server.getPersistenceManagerFactory();
        m.server.close();
        //      factory.close();
    }

    @Test
    public void tcoModel() {
        Assert.assertEquals("TcoModel.author", ((TcoModel) m.utility.getRoot()).getAuthor(), System.getProperty("user.name"));
    }

    @Test
    public void multitude() {
        Assert.assertEquals("Multitude model", 2.0, ((TcoObject) m.utility.getRoot()).getMultitude(), 0.0);
        Assert.assertEquals("Multitude group", 3.0, m.group.getMultitude(), 0.0);
        Assert.assertEquals("Multitude service", 4.0, m.clientService.getMultitude(), 0.0);
        Assert.assertEquals("Multitude driver", 5.0, m.clientDriver.getMultitude(), 0.0);
    }

    @Test
    public void systemParameter() {
        try {
            Assert.assertEquals("Default Currency", m.utility.getSystemParameter().getDefaultCurrency(), m.server.getIliCode(Currency.class, Currency.CHF));
            Assert.assertNotNull("Default Usage", m.utility.getSystemParameter().getDefaultUsageDuration());
            Assert.assertNotNull("Default Depreciation", m.utility.getSystemParameter().getDefaultDepreciationDuration());

            m.utility.getSystemParameter().setDepreciationInterestRate(5.6);
            Assert.assertEquals(5.6, m.utility.getSystemParameter().getDepreciationInterestRate(), 0.0);
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void findParent() {
        Assert.assertEquals("findParent", m.model, m.utility.findParent(m.group));
        Assert.assertEquals("findParent", m.group, m.utility.findParent(m.clientService));
        Assert.assertEquals("findParent", m.clientService, m.utility.findParent(m.clientDriver));
        try {
            Cost cost = (Cost) m.utility.createTcoObject(m.server, PersonalCost.class);
            m.utility.addOwnedElement(m.clientDriver, cost);
            Assert.assertEquals("findParent", m.clientDriver, m.utility.findParent(cost));

            cost = (Cost) m.utility.createTcoObject(m.server, FactCost.class);
            m.utility.addOwnedElement(m.clientDriver, cost);
            Assert.assertEquals("findParent", m.clientDriver, m.utility.findParent(cost));

            TcoPackage pck = (TcoPackage) m.utility.createTcoObject(m.server, TcoPackage.class);
            m.utility.addOwnedElement(m.group, pck);
            Assert.assertEquals("findParent", m.group, m.utility.findParent(pck));
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void updateRole() {
        try {
            PersonalCost pcost = m.addClientPersonalCost();
            pcost.setRole(m.role);
            ModelUtility.updateRole(pcost);

            Assert.assertEquals("Personal->role(FTE)", pcost.getAmount(), m.role.getFullTimeEquivalent());
            Assert.assertEquals("Personal->role(INTERNAL)", pcost.getInternal(), m.role.getInternal());
            Assert.assertNull("Personal->role(hours)", pcost.getHours());
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void updateCatalogue() {
        try {
            FactCost cost = (FactCost) m.utility.createTcoObject(m.server, FactCost.class);
            cost.setUsageDuration(56L);
            Assert.assertNotSame("entry check", cost.getUsageDuration(), m.catalogue.getUsageDuration());
            cost.setDepreciationDuration(78L);
            Assert.assertNotSame("entry check", cost.getDepreciationDuration(), m.catalogue.getDepreciationDuration());
            cost.setAmount(231.15);
            Assert.assertNotSame("entry check", cost.getAmount(), m.catalogue.getPrice());
            cost.setExpendable(Boolean.TRUE);

            //do the real tests
            cost.setCatalogue(m.catalogue);
            Assert.assertNotSame("no cost attribute change yet", cost.getUsageDuration(), m.catalogue.getUsageDuration());
            Assert.assertNotSame("no cost attribute change yet", cost.getDepreciationDuration(), m.catalogue.getDepreciationDuration());
            Assert.assertNotSame("no cost attribute change yet", cost.getAmount(), m.catalogue.getPrice());
            ModelUtility.updateCatalogue(cost);
            Assert.assertEquals(cost.getUsageDuration(), m.catalogue.getUsageDuration());
            Assert.assertEquals(cost.getDepreciationDuration(), m.catalogue.getDepreciationDuration());
            Assert.assertEquals(cost.getAmount(), m.catalogue.getPrice());
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }
}
