import ch.softenvironment.client.ApplicationOptionsTest;
import ch.softenvironment.client.ResourceManagerTest;
import ch.softenvironment.client.ViewManagerTestCase;
import ch.softenvironment.controller.DataBrowserTestCase;
import ch.softenvironment.math.FinancialUtilsTestCase;
import ch.softenvironment.math.MathUtilsTestCase;
import ch.softenvironment.util.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Run this class for all <b>seBase</b> TestCases.
 *
 * @author Peter Hirzel
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationOptionsTest.class,
        ResourceManagerTest.class,
        ViewManagerTestCase.class,
        DataBrowserTestCase.class,
        MathUtilsTestCase.class,
        FinancialUtilsTestCase.class,

        // *.util.test
        AmountFormatTestCase.class,
        BeanReflectorTestCase.class,
        DateUtilsTestCase.class,
        DeveloperExceptionTest.class,
        ImageUtilsTest.class,
        ListUtilsTestCase.class,
        UserExceptionTest.class,
        DOMUtilsTestCase.class,
        NlsUtilsTestCase.class,
        ParserCSVTestCase.class,
        StringUtilsTestCase.class
})
public class SeBaseTestSuite {
}
