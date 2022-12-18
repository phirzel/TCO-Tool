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

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.Occurance;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.ModelCopyTool;

/**
 * Tests org.tcotool.tools.ModelCopyTool
 *
 * @author Peter Hirzel
 */
public class ModelCopyToolTest {

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

    public void copyPackage() {
        try {
            TcoPackage subPackage = m.addGroup(m.group);
            subPackage.setMultitude(Double.valueOf(19.2));

            TcoPackage copy = (TcoPackage) ModelCopyTool.copy(m.utility, m.group);
            Assert.assertNotSame(m.group, copy);
            Assert.assertTrue(copy.getMultitude().intValue() == 3);
            Assert.assertTrue(copy.getService().size() == 2);
            Assert.assertTrue(copy.getOwnedElement().size() == 1);
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    public void copyService() {
        try {
            Service copy = (Service) ModelCopyTool.copy(m.utility, m.clientService);
            Assert.assertNotSame(m.clientService, copy);
            Assert.assertTrue(copy.getMultitude().intValue() == 4);
            Assert.assertTrue(copy.getDriver().size() == 1);
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void copyCostDriver() {
        try {
            m.clientDriver.setName("Driver");
            m.clientDriver.setPhase(m.phase);
            m.clientDriver.setProcess(m.process);
            Occurance occurance = (Occurance) m.server.createInstance(Occurance.class);
            occurance.setSite(m.site);
            m.utility.addOwnedElement(m.clientDriver, occurance);

            CostDriver copy = (CostDriver) ModelCopyTool.copy(m.utility, m.clientDriver);
            Assert.assertNotSame(m.clientDriver, copy);
            Assert.assertTrue(copy.getName().contains("Driver"));
            Assert.assertTrue(copy.getName().length() > m.clientDriver.getName().length());
            Assert.assertTrue(copy.getMultitude().intValue() == 5);
            Assert.assertTrue(m.clientDriver.getPhase().getId().equals(copy.getPhase().getId()));
            Assert.assertTrue(m.clientDriver.getProcess().getId().equals(copy.getProcess().getId()));
            Assert.assertTrue(m.clientDriver.getOccurrance().size() == 1);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void copyFactCost() {
        try {
            FactCost original = m.addClientFactCost();
            original.setName("FC");
            original.setCatalogue(m.catalogue);
            original.setAmount(Double.valueOf(17.99));

            FactCost copy = (FactCost) ModelCopyTool.copy(m.utility, original);
            Assert.assertNotSame(original, copy);
            Assert.assertTrue(copy.getName().contains("FC"));
            Assert.assertTrue(copy.getName().length() > original.getName().length());
            Assert.assertTrue(copy.getMultitude().intValue() == 7);
            Assert.assertTrue(copy.getAmount().doubleValue() == 17.99);
            Assert.assertTrue(original.getCatalogue().getId().equals(copy.getCatalogue().getId()));
            Assert.assertNull(copy.getCause());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void copyPersonalCost() {
        try {
            PersonalCost original = m.addClientPersonalCost();
            original.setName("PC");
            original.setRole(m.role);
            original.setAmount(Double.valueOf(17.99));

            PersonalCost copy = (PersonalCost) ModelCopyTool.copy(m.utility, original);
            Assert.assertNotSame(original, copy);
            Assert.assertTrue(copy.getName().contains("PC"));
            Assert.assertTrue(copy.getName().length() > original.getName().length());
            Assert.assertTrue(copy.getMultitude().intValue() == 6);
            Assert.assertTrue(copy.getAmount().doubleValue() == 17.99);
            Assert.assertTrue(original.getRole().getId().equals(copy.getRole().getId()));
            Assert.assertNull(copy.getCause());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
