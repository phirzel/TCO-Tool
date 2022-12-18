package ch.softenvironment.util;

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
 */

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Statistical utility to evaluate performance for named Use-Cases within an application (for e.g. DBMS Transaction times).
 *
 * @author Peter Hirzel
 */
public final class Statistic {

    private volatile transient long count;
    private volatile transient long max;
    private volatile transient long min;
    private volatile transient long total;

    private static final TreeMap<String, Statistic> statisticMap = new TreeMap<>();

    /**
     * Create a new Statistic for given UseCase if not already existing.
     *
     * <code>
     * Statistic stat = Statistic.createEntry("My performant loop"); long start = Statistic.getTimeMeasure(); doMyPerformantLoop(); long end = Statistic.getTimeMeasure(); stat.add(end-start); ..
     * Statistic.dump(..);
     * </code>
     *
     * @param useCase of UseCase to measure in time.
     */
    public static Statistic createEntry(final String useCase) {
        synchronized (statisticMap) {
            final Statistic statistic;
            if (statisticMap.containsKey(useCase)) {
                statistic = statisticMap.get(useCase);
            } else {
                statistic = new Statistic();
                statisticMap.put(useCase, statistic);
            }
            return statistic;
        }
    }

    /**
     * Convenience Method to measure current time resolution.
     *
     * @return long (milliseconds)
     */
    public static long getTimeMeasure() {
        // TODO be aware of different algorithms and OS support => accuracy
        // problems

        // needs additional Java3D package => good accuracy
        // com.sun.j3d.utils.timer.J3DTimer.getResolution()

        // only on J2SE 1.4.2
        // sun.misc.Perf hiResTimer = sun.misc.Perf.getPerf();
        // long freq = hiResTimer.highResFrequency();
        // long startTime = hiResTimer.highResCounter(); }

        // J2SE V1.5/5.0
        // System.nanoTime() => more accurate than System.currentTimeMillis()

        // J2SE V1.4 default => ok for Win/XP (bad for older Win/OS)
        return System.currentTimeMillis();
    }

    private Statistic() {
        clear();
    }

    /**
     * Add a new measured value (usually in milli-seconds) for current UseCase.
     *
     * @param sampleValue (other values than >0 are ignored)
     * @see #getTimeMeasure()
     */
    public final void add(long sampleValue) {
        if (sampleValue > 0) {
            if (sampleValue > max) {
                max = sampleValue;
            }
            if ((sampleValue < min) || (min == 0 /* not yet set */)) {
                min = sampleValue;
            }
            total += sampleValue;
            count++;
        }
    }

    /**
     * Reset all measures for UseCase-Statistic.
     */
    public void clear() {
        count = 0;
        max = 0;
        min = 0;
        total = 0;
    }

    /**
     * Reset all values for all Statistic-instances, where given UseCases remain in List.
     */
    public static void clearAll() {
        Iterator<Statistic> iterator = statisticMap.values().iterator();
        while (iterator.hasNext()) {
            (iterator.next()).clear();
        }
    }

    /**
     * Print measured statistics to given Writer.
     */
    public static void dump(PrintWriter out) {
        Iterator<Map.Entry<String, Statistic>> iterator = statisticMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Statistic> e = iterator.next();
            Statistic s = e.getValue();
            if (s.count > 0) {
                out.println("Use-Case: " + e.getKey());
                out.println("  Count  = " + s.count);
                out.println("  Min    = " + s.min);
                out.println("  Max    = " + s.max);
                out.println("  Average= " + (double) s.total / (double) s.count);
                out.println("  Total  = " + s.total);
            }
        }
        out.flush();
    }
}