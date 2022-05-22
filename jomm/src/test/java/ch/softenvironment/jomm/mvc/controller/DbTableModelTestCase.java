package ch.softenvironment.jomm.mvc.controller;

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

import ch.softenvironment.util.ListUtils;
import junit.framework.TestCase;

/**
 * Testcase for DbTableModel.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DbTableModelTestCase extends TestCase {

    public void testGet() throws Exception {
        DbTableModel model = new DbTableModel();
        boolean illegalArgExThrown = false;
        try {
            model.get(0);
        } catch (IllegalArgumentException e) {
            illegalArgExThrown = true;
        }
        assertTrue("no elements", illegalArgExThrown);

        illegalArgExThrown = false;
        try {
            model.get(-1);
        } catch (IllegalArgumentException e) {
            illegalArgExThrown = true;
        }
        assertTrue("no neg. index", illegalArgExThrown);
    }

    public void testGetRaw() throws Exception {
        DbTableModel model = new DbTableModel();
        boolean illegalArgExThrown = false;
        try {
            model.getRaw(0);
        } catch (IllegalArgumentException e) {
            illegalArgExThrown = true;
        }
        assertTrue("no raw elements", illegalArgExThrown);

        model.setAll(ListUtils.createList(new DbTableModel()));
        assertTrue("correct raw instance", model.getRaw(0) instanceof DbTableModel);

        illegalArgExThrown = false;
        try {
            model.getRaw(1);
        } catch (IllegalArgumentException e) {
            illegalArgExThrown = true;
        }
        assertTrue("no more elements", illegalArgExThrown);
    }
}
