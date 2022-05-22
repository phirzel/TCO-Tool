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

import ch.softenvironment.util.StringUtils;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tcotool.model.PersonalCost;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.FindTool;

/**
 * Tests org.tcotool.tools.FindTool
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class FindToolTest {

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
    public void findByCode() throws Exception {
        Assert.assertTrue("# Role codes", (new FindTool(m.utility)).findByCode(null).size() == 0);

        PersonalCost pCost = m.addClientPersonalCost();
        pCost.setRole(m.role);
        Assert.assertTrue("# Role codes", (new FindTool(m.utility)).findByCode(m.role).size() == 1);

        pCost = m.addClientPersonalCost();
        pCost.setRole(m.role);
        Assert.assertTrue("# Role codes", (new FindTool(m.utility)).findByCode(m.role).size() == 2);
    }

    @Test
    public void findByText() throws Exception {
        PersonalCost pCost = m.addClientPersonalCost();
        pCost.setRole(m.role);

        List<String> hits = (new FindTool(m.utility)).findByText(null, true, true, true);
        Assert.assertTrue("<Name=null> NOT contained in given group tree", hits.size() == 0);
        (new FindTool(m.utility)).findByText("", true, true, true);
        Assert.assertTrue("<Name=\"\"> NOT contained in given group tree", hits.size() == 0);

        hits = (new FindTool(m.utility)).findByText("MyExpression", true, true, true);
        Assert.assertTrue("<Name> NOT contained in given group tree", hits.size() == 0);

        m.group.setName("MyExpression");
        hits = (new FindTool(m.utility)).findByText("MyExpression", true, true, true);
        Assert.assertTrue("<Name> contained in given group tree", hits.size() == 1);

        hits = (new FindTool(m.utility)).findByText("MyExpression", false, true, true);
        Assert.assertTrue("<Documentation> NOT contained in given group tree", hits.size() == 0);

        m.group.setDocumentation("MyExpression");
        hits = (new FindTool(m.utility)).findByText("MyExpression", false, true, true);
        Assert.assertTrue("<Documentation> contained in given group tree", hits.size() == 1);

        hits = (new FindTool(m.utility)).findByText("MyExpression", true, true, true);
        Assert.assertTrue("<Name>, <Documentation> both contained in given group tree, though ONE hit at all", hits.size() == 1);

        hits = (new FindTool(m.utility)).findByText("MYEXPRESSION", true, false, false);
        Assert.assertTrue("<Name, caseSensitive> NOT contained in given group tree, though ONE hit at all", hits.size() == 1);
        hits = (new FindTool(m.utility)).findByText("MYEXPRESSION", true, false, true);
        Assert.assertTrue("<Name, caseSensitive> NOT contained in given group tree, though ONE hit at all", hits.size() == 0);

        pCost.setName("MyExpression");
        hits = (new FindTool(m.utility)).findByText("MyExpression", true, false, true);
        Assert.assertTrue("<Name> contained in given group tree", hits.size() == 2);
    }

    @Test
    public void findByUndefinedCode() throws Exception {
        FindTool finder = new FindTool(m.utility);
        List<String> hits = finder.findByUndefinedCode(Calculator.PERSONAL_ROLE_UNDEFINED);
        Assert.assertTrue("<PERSONAL_ROLE_UNDEFINED>", hits.size() == 0);
        hits = finder.findByUndefinedCode(Calculator.COST_CAUSE_UNDEFINED);
        Assert.assertTrue("<COST_CAUSE_UNDEFINED>", hits.size() == 0);

        PersonalCost pCost = m.addClientPersonalCost();
        //pCost.setRole(m.role);
        finder = new FindTool(m.utility);
        hits = finder.findByUndefinedCode(Calculator.PERSONAL_ROLE_UNDEFINED);
        String label = finder.getUndefinedLabel();
        Assert.assertTrue(!StringUtils.isNullOrEmpty(label));
        Assert.assertTrue("<PERSONAL_ROLE_UNDEFINED>", hits.size() == 1);
        hits = finder.findByUndefinedCode(Calculator.COST_CAUSE_UNDEFINED);
        Assert.assertFalse(label.equals(finder.getUndefinedLabel()));
        Assert.assertTrue("<COST_CAUSE_UNDEFINED>", hits.size() == 1);

        pCost.setRole(m.role);
        finder = new FindTool(m.utility);
        hits = finder.findByUndefinedCode(Calculator.PERSONAL_ROLE_UNDEFINED);
        Assert.assertTrue(!StringUtils.isNullOrEmpty(finder.getUndefinedLabel()));
        Assert.assertTrue("<PERSONAL_ROLE_UNDEFINED>", hits.size() == 0);
        hits = finder.findByUndefinedCode(Calculator.COST_CAUSE_UNDEFINED);
        Assert.assertTrue("<COST_CAUSE_UNDEFINED>", hits.size() == 1);
    }
}
