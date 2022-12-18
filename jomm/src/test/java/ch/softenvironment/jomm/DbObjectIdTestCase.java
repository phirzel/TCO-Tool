package ch.softenvironment.jomm;
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

import ch.softenvironment.jomm.datatypes.DbNlsString;
import junit.framework.TestCase;

/**
 * Test DbObjectId.
 *
 * @author Peter Hirzel
 */
public class DbObjectIdTestCase extends TestCase {

    public void testEquals() {
        DbObjectId id = new DbObjectId(DbNlsString.class, Long.valueOf(13));
        assertTrue("DbObjectId->class", id.getModelClass().equals(DbNlsString.class));
        assertTrue("DbObjectId->id", id.getId().longValue() == 13);
        assertTrue("DbObjectId->identityType", id.getIdentityType().equals(DbIdentity.APPLICATION));

        DbObjectId idCloned = new DbObjectId(DbNlsString.class, Long.valueOf(13)); // (DbObjectId)id.clone();
        assertTrue("DbObjectId", id.equals(idCloned));
    }
}
