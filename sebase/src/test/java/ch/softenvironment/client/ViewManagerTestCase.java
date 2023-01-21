package ch.softenvironment.client;

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

import junit.framework.TestCase;

/**
 * TestCase for  ViewManager
 *
 * @author Peter Hirzel
 * @since 1.2 (2008-01-16)
 */
public class ViewManagerTestCase extends TestCase {

    private ViewManager mgr = null;

    @Override
    public void setUp() {
        mgr = new ViewManager();
    }

    public void testRights() {
        UserActionRights rights = mgr.getRights(ViewManager.class /*not a real model-class*/);
        assertTrue("no rights defined at all => all rights expected!", rights.isReadObjectAllowed() && rights.isNewObjectAllowed() && rights.isRemoveObjectsAllowed() && rights.isSaveObjectAllowed());

        mgr.setRights(new UserActionRights(UserActionRights.READONLY));
        rights = mgr.getRights(ViewManager.class /*not a real model-class*/);
        assertTrue("default RO", rights.isReadObjectAllowed() && !rights.isNewObjectAllowed() && !rights.isRemoveObjectsAllowed() && !rights.isSaveObjectAllowed());

        mgr.setRights(new UserActionRights(UserActionRights.NONE), ViewManager.class);
        rights = mgr.getRights(ViewManager.class /*not a real model-class*/);
        assertTrue("overwirtes RO for specific model-class", !rights.isReadObjectAllowed() && !rights.isNewObjectAllowed() && !rights.isRemoveObjectsAllowed() && !rights.isSaveObjectAllowed());
    }
}
