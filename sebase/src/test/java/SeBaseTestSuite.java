import ch.softenvironment.client.ApplicationOptionsTest;
import ch.softenvironment.client.ResourceManagerTest;
import ch.softenvironment.client.ViewManagerTestCase;
import ch.softenvironment.controller.DataBrowserTestCase;
import ch.softenvironment.math.FinancialUtilsTestCase;
import ch.softenvironment.math.MathUtilsTestCase;
import ch.softenvironment.util.AmountFormatTestCase;
import ch.softenvironment.util.BeanReflectorTestCase;
import ch.softenvironment.util.DOMUtilsTestCase;
import ch.softenvironment.util.DateUtilsTestCase;
import ch.softenvironment.util.DeveloperExceptionTest;
import ch.softenvironment.util.ImageUtilsTest;
import ch.softenvironment.util.ListUtilsTestCase;
import ch.softenvironment.util.NlsUtilsTestCase;
import ch.softenvironment.util.ParserCSVTestCase;
import ch.softenvironment.util.StringUtilsTestCase;
import ch.softenvironment.util.UserExceptionTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;

/**
 * Run this class for all <b>seBase</b> TestCases.
 *
 * @author Peter Hirzel
 */
public class SeBaseTestSuite extends junit.framework.TestSuite {

    /**
     * DemoTestSuite constructor comment.
     */
    public SeBaseTestSuite() {
        super();
    }

    /**
     * DemoTestSuite constructor comment.
     *
     * @param arg1 java.lang.String
     */
    public SeBaseTestSuite(String arg1) {
        super(arg1);
    }

    /**
     * @return junit.framework.Test
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite()); // JUnit 3 runner
        // junit.swingui.TestRunner.run(SeBaseTestSuite.class);
    }

    /**
     * @return junit.framework.Test
     */
    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("Testing: seBase");

        // *.client.test
        suite.addTest(new TestSuite(ApplicationOptionsTest.class));
        suite.addTest(new TestSuite(ResourceManagerTest.class));
        suite.addTest(new TestSuite(ViewManagerTestCase.class));

        // *.controller.test
        suite.addTest(new TestSuite(DataBrowserTestCase.class));

        // *.math.test
        suite.addTest(new TestSuite(MathUtilsTestCase.class));
        suite.addTest(new TestSuite(FinancialUtilsTestCase.class));

        // *.util.test
        suite.addTest(new TestSuite(AmountFormatTestCase.class));
        suite.addTest(new TestSuite(BeanReflectorTestCase.class));
        suite.addTest(new TestSuite(DateUtilsTestCase.class));
        suite.addTest(new TestSuite(DeveloperExceptionTest.class));
        suite.addTest(new JUnit4TestAdapter(ImageUtilsTest.class));
        suite.addTest(new TestSuite(ListUtilsTestCase.class));
        suite.addTest(new TestSuite(UserExceptionTest.class));
        suite.addTest(new TestSuite(DOMUtilsTestCase.class));
        suite.addTest(new TestSuite(NlsUtilsTestCase.class));
        suite.addTest(new TestSuite(ParserCSVTestCase.class));
        suite.addTest(new TestSuite(StringUtilsTestCase.class));

        return (suite);
    }
}
