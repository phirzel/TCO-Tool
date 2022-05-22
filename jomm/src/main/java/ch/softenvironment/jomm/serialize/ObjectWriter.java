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

import java.io.IOException;

/**
 * Definition of a Writer capable to write a certain object into a serialized stream.
 *
 * @author ce
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.1.1.1 $ $Date: 2005-04-25 10:01:11 $
 */
public interface ObjectWriter {

    /**
     * Called by the encoder to write an object.
     *
     * @param object to write into a file
     * @param serializer Writing-Tool for a specific Target-Format
     */
    void writeObject(Object object, Serializer serializer) throws IOException;
}
