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

import lombok.extern.slf4j.Slf4j;

/**
 * Describes any Numeric types.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DbNumericFieldDescriptor extends DbFieldTypeDescriptor {

    public static final int YEAR = 0;
    public static final int AMOUNT = 1;
    public static final int POSITIVE_INTEGER = 2;
    public static final int POSITIVE_DOUBLE = 3;
    public static final int POSITIVE_LONG = 4;
    public static final long MAX_ACCURACY = -1;
    private transient double min;
    private transient double max;
    private transient long accuracy;

    /**
     * Describe a default java.lang.Numeric type.
     */
    public DbNumericFieldDescriptor(final int type) {
        switch (type) {
            case YEAR: {
                baseType = java.lang.Integer.class;
                min = 1900.0;
                max = 9999.0;
                accuracy = 0;
                break;
            }
            case AMOUNT: {
                baseType = java.lang.Double.class;
                min = Double.NEGATIVE_INFINITY;
                max = Double.POSITIVE_INFINITY;
                accuracy = 2;
                break;
            }
            case POSITIVE_INTEGER: {
                baseType = java.lang.Integer.class;
                min = 0.0;
                max = Integer.MAX_VALUE;
                accuracy = 0;
                break;
            }
            case POSITIVE_LONG: {
                baseType = java.lang.Long.class;
                min = 0;
                max = Long.MAX_VALUE;
                accuracy = 0;
                break;
            }
            case POSITIVE_DOUBLE: {
                baseType = java.lang.Double.class;
                min = 0.0;
                max = Double.MAX_VALUE;
                accuracy = MAX_ACCURACY;
                break;
            }
            default:
                throw new ch.softenvironment.util.DeveloperException("unknown type <" + type + ">");
        }
    }

    /**
     * Describe a default java.lang.Numeric type.
     */
    public DbNumericFieldDescriptor(Class<?> baseType) {
        super(baseType);

        if (baseType.equals(Integer.class)) {
            min = Integer.MIN_VALUE;
            max = Integer.MAX_VALUE;
            accuracy = 0;
        } else if (baseType.equals(Long.class)) {
            min = Long.MIN_VALUE;
            max = Long.MAX_VALUE;
            accuracy = 0;
        } else if (!baseType.equals(Double.class)) {
            log.error("Developer error: Non-mapped type <{}>", baseType.getName());
        }
        min = Double.NEGATIVE_INFINITY;
        max = Double.POSITIVE_INFINITY;
        accuracy = Long.MAX_VALUE;
    }

    /**
     * DbNumericFieldDescriptor constructor comment.
     */
    public DbNumericFieldDescriptor(Class<?> baseType, double min, double max, long accuracy) {
        super(baseType);

        this.min = min;
        this.max = max;
        this.accuracy = accuracy;
    }

    /**
     * Return the accuracy of the value.
     */
    public long getAccuracy() {
        return accuracy;
    }

    /**
     * Return the Maximal Value.
     */
    public double getMaxValue() {
        return max;
    }

    /**
     * Return the Minimal-Value.
     */
    public double getMinValue() {
        return min;
    }
}
