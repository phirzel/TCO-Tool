import ch.softenvironment.cipher.CipherTest;
import ch.softenvironment.licence.LicenceCheckerTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Run this class for all these TestCases.
 *
 * @author Peter Hirzel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CipherTest.class,
        LicenceCheckerTestCase.class
})
public class SeMiscTestSuite {
}
