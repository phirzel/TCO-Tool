package org.tcotools.tools;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.PersonalCost;
import org.tcotool.tools.CodeRefBuilder;

public class CodeRefBuilderTest {

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
        // javax.jdo.PersistenceManagerFactory factory =
        // m.server.getPersistenceManagerFactory();
        m.server.close();
        // factory.close();
    }

    @Test
    public void getHits() throws Exception {
        PersonalCost pCost = m.addClientPersonalCost();
        pCost.setRole(m.role);
        pCost = m.addClientPersonalCost();
        pCost.setRole(m.role);

        CodeRefBuilder builder = new CodeRefBuilder(m.model, m.role);
        Assert.assertTrue(builder.getHits().size() == 2);
    }
}
