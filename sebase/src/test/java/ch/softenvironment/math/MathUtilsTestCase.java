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
        assertEquals(2.0, log2);
        log2 = MathUtils.ld(8.0);
        assertEquals(3.0, log2);
    }

    public void testFix() {
        assertEquals(0.0, MathUtils.fix(0.0, 0));
        assertEquals(0.0, MathUtils.fix(0.0, 2));
        assertEquals(12.89, MathUtils.fix(12.896, 2));
        assertEquals(-76.8966, MathUtils.fix(-76.89667, 4));
        assertEquals(30.9, MathUtils.fix(30.989, 1));
        assertEquals(-76.0, MathUtils.fix(-76.89667, 0));
    }

    public void testRound() {
        assertEquals(0.0, MathUtils.round(0.0, 0));
        assertEquals(0.0, MathUtils.round(0.0, 2));
        assertEquals(12.88, MathUtils.round(12.876, 2));
        assertEquals(-76.8967, MathUtils.round(-76.89667, 4));
        assertEquals(31.0, MathUtils.round(30.989, 1));
        assertEquals(-77.0, MathUtils.round(-76.89667, 0));
        assertEquals(303.33, MathUtils.round(303.33333333333333333, 2));
        assertEquals("round(double)", 12345.57, MathUtils.round(12345.5698, 2));
        assertEquals("round(double)", 12345.40, MathUtils.round(12345.403, 2));
        assertEquals("round(double)", 12345.9, MathUtils.round(12345.9, 2));
        assertEquals("round(double)", 0.0, MathUtils.round(0.0, 2));
        assertEquals("round(double)", 1.23, MathUtils.round(1.23, 2));
        assertEquals("round(double)", 1.24, MathUtils.round(1.24, 2));
        assertEquals("round(double)", 1.25, MathUtils.round(1.25, 2));
        assertEquals("round(double)", 1.26, MathUtils.round(1.26, 2));
    }

    public void testCompare() {
        double left = 0.0;
        double right = 0.0;
        assertEquals(0, MathUtils.compare(left, right));
        left = 0.0;
        right = -0.0;
        assertEquals("0.0 == -0.0", 0, MathUtils.compare(left, right));

        left = 0.00239000000001;
        right = 0.00239000000002;
        assertEquals(0, MathUtils.compare(left, right));
        left = 0.002394;
        right = 0.002395;
        assertEquals(-1, MathUtils.compare(left, right));
        assertEquals(1, MathUtils.compare(right, left));
    }

    public void testNegate() {
        Double number = Double.valueOf(0.0023);
        Double res = MathUtils.negate(number);
        assertEquals(res, Double.valueOf(-0.0023));
        assertEquals(-0.0023, res.doubleValue());

        number = Double.valueOf(-0.0023);
        res = MathUtils.negate(number);
        assertEquals(res, Double.valueOf(0.0023));
        assertEquals(0.0023, res.doubleValue());

        number = Double.valueOf(0.0);
        res = MathUtils.negate(number);
        assertEquals(res, Double.valueOf(0.0));
        assertEquals(0.0, res.doubleValue());
        assertTrue(res.doubleValue() >= 0.0);
    }
}
