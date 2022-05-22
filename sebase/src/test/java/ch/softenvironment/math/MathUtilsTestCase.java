package ch.softenvironment.math;

import junit.framework.TestCase;

/**
 * TestCase for MathUtils.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.4 $ $Date: 2008-04-18 18:45:44 $
 */
public class MathUtilsTestCase extends TestCase {

    /**
     * This is typically the log-function provided by Computers. Actually its called ln(number) instead of log(number).
     */
    public void testLogarithmNatural() {
        double logNatural = MathUtils.ln(59.0);
        // calculated by HP 28S
        assertTrue((logNatural > 4.077) && (logNatural < 4.078));
    }

    public void testLog10() {
        double log10 = MathUtils.lg(3.0);
        // value from Bartsch
        assertTrue((log10 > 0.477) && (log10 < 0.478));
        log10 = MathUtils.lg(59.0);
        // value HP28S [log]
        assertTrue((log10 > 1.77) && (log10 < 1.78));
    }

    public void testLog2() {
        double log2 = MathUtils.ld(4.0);
        assertTrue(log2 == 2.0);
        log2 = MathUtils.ld(8.0);
        assertTrue(log2 == 3.0);
    }

    public void testFix() {
        assertTrue(MathUtils.fix(0.0, 0) == 0.0);
        assertTrue(MathUtils.fix(0.0, 2) == 0.0);
        assertTrue(MathUtils.fix(12.896, 2) == 12.89);
        assertTrue(MathUtils.fix(-76.89667, 4) == -76.8966);
        assertTrue(MathUtils.fix(30.989, 1) == 30.9);
        assertTrue(MathUtils.fix(-76.89667, 0) == -76.0);
    }

    public void testRound() {
        assertTrue(MathUtils.round(0.0, 0) == 0.0);
        assertTrue(MathUtils.round(0.0, 2) == 0.0);
        assertTrue(MathUtils.round(12.876, 2) == 12.88);
        assertTrue(MathUtils.round(-76.89667, 4) == -76.8967);
        assertTrue(MathUtils.round(30.989, 1) == 31.0);
        assertTrue(MathUtils.round(-76.89667, 0) == -77.0);
        assertTrue(MathUtils.round(303.33333333333333333, 2) == 303.33);
        assertTrue("round(double)", MathUtils.round(12345.5698, 2) == 12345.57);
        assertTrue("round(double)", MathUtils.round(12345.403, 2) == 12345.40);
        assertTrue("round(double)", MathUtils.round(12345.9, 2) == 12345.9);
        assertTrue("round(double)", MathUtils.round(0.0, 2) == 0.0);
        assertTrue("round(double)", MathUtils.round(1.23, 2) == 1.23);
        assertTrue("round(double)", MathUtils.round(1.24, 2) == 1.24);
        assertTrue("round(double)", MathUtils.round(1.25, 2) == 1.25);
        assertTrue("round(double)", MathUtils.round(1.26, 2) == 1.26);
    }

    public void testCompare() {
        double left = 0.0;
        double right = 0.0;
        assertTrue(MathUtils.compare(left, right) == 0);
        left = 0.0;
        right = -0.0;
        assertTrue("0.0 == -0.0", MathUtils.compare(left, right) == 0);

        left = 0.00239000000001;
        right = 0.00239000000002;
        assertTrue(MathUtils.compare(left, right) == 0);
        left = 0.002394;
        right = 0.002395;
        assertTrue(MathUtils.compare(left, right) == -1);
        assertTrue(MathUtils.compare(right, left) == 1);
    }

    public void testNegate() {
        Double number = new Double(0.0023);
        Double res = MathUtils.negate(number);
        assertTrue(res.equals(new Double(-0.0023)));
        assertTrue(res.doubleValue() == -0.0023);

        number = new Double(-0.0023);
        res = MathUtils.negate(number);
        assertTrue(res.equals(new Double(0.0023)));
        assertTrue(res.doubleValue() == 0.0023);

        number = new Double(0.0);
        res = MathUtils.negate(number);
        assertTrue(res.equals(new Double(0.0)));
        assertTrue(res.doubleValue() == 0.0);
        assertTrue(res.doubleValue() >= 0.0);
    }
}
