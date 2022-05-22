package ch.softenvironment.jomm.descriptor;
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

/**
 * Define a Cardinality for an Association defined by DbDescriptor.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2006-07-05 15:59:53 $
 */
public class DbMultiplicityRange {

    private long lower = 0;
    private long upper = 1;

    public static final double UNBOUND = Double.POSITIVE_INFINITY;

    /**
     * DbCardinality constructor comment. Range: exactly [value]
     *
     * @param value Both lower and Upper Range
     */
    public DbMultiplicityRange(long value) {
        this(value, value);
    }

    /**
     * DbCardinality constructor comment. Range: [lower..upper]
     *
     * @param lower >=0
     * @param upper >=1
     */
    public DbMultiplicityRange(long lower, double upper) {
        this(lower, Long.MAX_VALUE);

        if (upper != UNBOUND) {
            throw new ch.softenvironment.util.DeveloperException("only UNBOUND is accepted as double here");
        }
    }

    /**
     * DbCardinality constructor comment. Range: [lower..upper]
     *
     * @param lower >=0
     * @param upper >=1
     */
    public DbMultiplicityRange(long lower, long upper) {
        super();

        if (lower < 0) {
            throw new ch.softenvironment.util.DeveloperException("[lower] must be >= 0");
        }
        if (upper < 1) {
            throw new ch.softenvironment.util.DeveloperException("[upper] must be >= 1");
        }

        if (lower > upper) {
            ch.softenvironment.util.Tracer.getInstance().developerWarning("Range: (upper < lower)");
            this.lower = upper;
            this.upper = lower;
        } else {
            this.lower = lower;
            this.upper = upper;
        }
    }

    /**
     * Return the lower bound range of Cardinality.
     */
    public long getLower() {
        return lower;
    }

    /**
     * Return the upper bound range of Cardinality.
     */
    public long getUpper() {
        return upper;
    }

    /**
     * Return whether lower bound Range starts with [0..].
     */
    public boolean isNullAllowed() {
        return lower == 0;
    }
}
