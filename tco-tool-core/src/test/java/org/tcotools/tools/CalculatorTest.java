package org.tcotools.tools;

import org.junit.Assert;
import org.junit.Test;
import org.tcotool.tools.Calculator;

/**
 * Testcase for org.tcotool.tools.Calculator
 *
 * @author Peter Hirzel
 */
public class CalculatorTest {

    @Test
    public void accumulateLists() {
        java.util.List<Double> results = new java.util.ArrayList<>();
        results.add(3.2);
        results.add(4.5);
        java.util.List<Double> summand = new java.util.ArrayList<>();
        summand.add(5.7);
        summand.add(6.2);
        Calculator.accumulateLists(results, summand);
        Assert.assertEquals(Double.valueOf(3.2 + 5.7), results.get(0));
        Assert.assertEquals(Double.valueOf(4.5 + 6.2), results.get(1));
    }
}