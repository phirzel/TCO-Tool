package ch.softenvironment.jomm.serialize;
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

/**
 * Contains a set of mapping methods to transform given value into a serializable Stream.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2008-08-07 05:30:59 $
 */
public interface Serializer {

    /**
     * Encode a String in a serialized manner.
     *
     * @return String
     */
    String encodeString(String value);

    /**
     * Encode a Number in a serialized manner.
     *
     * @return String
     */
    String encodeNumber(Number value);

    /**
     * Encode a Boolean in a serialized manner.
     *
     * @return String
     */
    String encodeBoolean(Boolean value);

    /**
     * Encode a Date in a serialized manner.
     *
     * @return String
     */
    String encodeDate(java.util.Date value);

    /**
     * Encode a Time in a serialized manner.
     *
     * @return String
     */
    String encodeTime(java.util.Date value);

    /**
     * Encode a Timestamp in a serialized manner.
     *
     * @return String
     */
    String encodeDateTime(java.util.Date value);
}
