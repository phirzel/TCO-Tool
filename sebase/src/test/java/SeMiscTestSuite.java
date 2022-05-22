import ch.softenvironment.cipher.CipherTestCase;
import ch.softenvironment.licence.LicenceCheckerTestCase;
import junit.framework.TestSuite;

/**
 * Run this class for all these TestCases.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class SeMiscTestSuite extends junit.framework.TestSuite {

    /**
     * DemoTestSuite constructor comment.
     */
    public SeMiscTestSuite() {
        super();
    }

    /**
     * DemoTestSuite constructor comment.
     *
     * @param arg1 java.lang.Class
     */
    public SeMiscTestSuite(Class<junit.framework.TestCase> arg1) {
        super(arg1);
    }

    /**
     * DemoTestSuite constructor comment.
     *
     * @param arg1 java.lang.String
     */
    public SeMiscTestSuite(String arg1) {
        super(arg1);
    }

    /**
     * Insert the method's description here. Creation date: (10.05.2001 12:14:54)
     *
     * @return junit.framework.Test
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        // junit.awtui.TestRunner.run(SandfishTestCase.class);
        // junit.swingui.TestRunner.run(SeMiscTestSuite.class);
    }

    /**
     * @return junit.framework.Test
     */
    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("Testing: seMisc");

        suite.addTest(new TestSuite(CipherTestCase.class));
        suite.addTest(new TestSuite(LicenceCheckerTestCase.class));

        /*
         * in non VAJ environments: suite.addTest(new
         * TestSuite(TestTestCaseClassLoader.class));
         */

        return (suite);
    }
}
