package ch.softenvironment.math;

/**
 * Utility for extended Mathematics.
 *
 * @author Peter Hirzel
 */
public class MathUtils {

    /**
     * Calclulate logarithm according to given base, where log(base=a; value=b) = c corresponds to a^c = b => 2^x=8 corresponds to ld(8) => x=3 Transformation: log(base=e; value=b) == ln(value=b)
     * <p>
     * java.util.Math.log(a) represents actually the natural logarithm ln(a), therefore this log-function is not exactly the same.
     *
     * @param base
     * @param value
     * @return
     * @see ln()
     */
    public final static double log(double base, double value) {
        // log[base]value = log[10]value / log[10]base
        if ((base > 0) && (base != 1.0) && (value > 0)) {
            return ln(value) / ln(base);
        } else {
            throw new IllegalArgumentException("base [" + base + "] or value [" + value + "] invalid");
        }
    }

    /**
     * Decade Logarithm (base=10).
     *
     * @param value
     * @return
     */
    public final static double lg(double value) {
        return log(10.0, value);
    }

    /**
     * Dual Logarithm (base=2).
     *
     * @param value
     * @return
     */
    public final static double ld(double value) {
        return log(2.0, value);
    }

    /**
     * Natural Logarithm (base=e)
     *
     * @param value
     * @return
     */
    public final static double ln(double value) {
        return Math.log(value);
    }

    /**
     * Negate a given value. This method prevents "0.0 * (-1.0) => -0.0" "-0.0" is produced when a floating-point operation results in a negative floating-point number so close to 0 that cannot be
     * represented normally. Be aware that "-0.0" is numerically identical to "0.0". However, some operations involving "-0.0" are different than the same operation with "0.0".
     *
     * @param value
     * @return
     */
    public final static Double negate(Double value) {
        if (value == null) {
            return null;
        } else if (value.doubleValue() == 0.0) {
            // prevent "-0.0" 
            return value;
        } else {
            return Double.valueOf(value.doubleValue() * -1.0);
        }
    }

    /**
     * @param value
     * @return
     * @see java.lang.Math#abs(double)
     */
    public final static Double abs(Double value) {
        if (value == null) {
            return null;
        } else {
            return Double.valueOf(java.lang.Math.abs(value.doubleValue()));
        }
    }

    /**
     * Cut the fraction part to accuracy length, for e.g. fix(12.876, 2) => 12.87
     *
     * @param value
     * @param accuracy
     * @return
     */
    public final static double fix(double value, int accuracy) {
        if (accuracy < 0) {
            throw new IllegalArgumentException("accuracy must be >=0");
        }
        if (accuracy == 0) {
            // cast decimal fraction away
            return (double) ((long) value);
        }
        double factor = Math.pow(10, accuracy);
        return ((long) (value * factor)) / factor;
    }

    /**
     * Round the fraction part to accuracy length, for e.g. round(12.876, 2) => 12.88
     *
     * @param value
     * @param accuracy
     * @return
     * @see java.util.Math#round()
     */
    public final static double round(double value, int accuracy) {
        if (accuracy < 0) {
            throw new IllegalArgumentException("accuracy must be >=0");
        }
        if (accuracy == 0) {
            return Math.round(value);
        } else {
            double val = value;
            for (int i = 0; i < accuracy; i++) {
                val = val * 10;
            }
            val = (long) Math.floor(val + 0.5d);
            for (int i = 0; i < accuracy; i++) {
                val = val / 10;
            }
            return fix(val, accuracy); // remove any division inaccuracies
        }
    }

    /**
     * This operation compares two floating point values for equality. Because floating point calculations may involve rounding, calculated float and double values may not be accurate. For values that
     * must be precise, such as monetary values, consider using a fixed-precision type such as BigDecimal. For values that need not be precise, consider comparing for equality within some range, for
     * example: if ( Math.abs(x - y) < .0000001 )
     * <p>
     * See the Java Language Specification, section 4.2.4. See Findbugs: Test for floating point equality
     *
     * @param value1
     * @param value2
     * @return integer according to java.util.Comparator definitions
     */
    public final static int compare(double value1, double value2) {
        if (Math.abs(value1 - value2) < 0.0000001) {
            return 0;
        } else if (value1 < value2) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Sum the given values.
     *
     * @param value1
     * @param value2
     * @return null if both are null
     */
    public final static Double sum(Double value1, Double value2) {
        if (value1 == null) {
            return value2;
        } else if (value2 == null) {
            return value1;
        }
        return Double.valueOf(value1.doubleValue() + value2.doubleValue());
    }
}
