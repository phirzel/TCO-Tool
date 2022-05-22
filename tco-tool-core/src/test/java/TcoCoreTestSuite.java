

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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tcotools.tools.CalculatorDegressiveDepreciationTest;
import org.tcotools.tools.CalculatorLinearDepreciationCompoundInterestTest;
import org.tcotools.tools.CalculatorLinearDepreciationTest;
import org.tcotools.tools.CalculatorTcoTest;
import org.tcotools.tools.CalculatorTest;
import org.tcotools.tools.CodeRefBuilderTest;
import org.tcotools.tools.FindToolTest;
import org.tcotools.tools.ModelCopyToolTest;
import org.tcotools.tools.ModelUtilityTest;

/**
 * Define a set/suite of TestCases to test TCO-Tool.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CalculatorDegressiveDepreciationTest.class,
    CalculatorLinearDepreciationCompoundInterestTest.class,
    CalculatorLinearDepreciationTest.class,
    CalculatorTcoTest.class,
    CalculatorTest.class,
    CodeRefBuilderTest.class,
    FindToolTest.class,
    ModelCopyToolTest.class,
    ModelUtilityTest.class})
public class TcoCoreTestSuite {
    /*
     * JUnit 3 public TcoTestSuite(String arg) { super(arg); }
     *
     * public static void main(String[] args) { Tracer.start(Tracer.ALL);
     *
     * //junit.textui.TestRunner.run(suite());
     * junit.swingui.TestRunner.run(TcoTestSuite.class);
     *
     * Tracer.getInstance().stop(); }
     *
     * public static junit.framework.Test suite() { TestSuite suite = new
     * TestSuite("TCO-Tool tests"); suite.addTest(new
     * TestSuite(ModelUtilityTestCase.class)); suite.addTest(new
     * TestSuite(CalculatorTestCase.class)); suite.addTest(new
     * TestSuite(CalculatorTcoTestCase.class)); suite.addTest(new
     * TestSuite(CalculatorLinearDepreciationTestCase.class)); suite.addTest(new
     * TestSuite(CalculatorLinearDepreciationCompoundInterestTestCase.class));
     * suite.addTest(new
     * TestSuite(CalculatorDegressiveDepreciationTestCase.class));
     * suite.addTest(new TestSuite(CodeRefBuilderTestCase.class));
     * suite.addTest(new TestSuite(InterviewMaschineTestCase.class)); //TODO
     * include suite.addTest(new XmlMapperSuite()); return suite; }
     */
}
