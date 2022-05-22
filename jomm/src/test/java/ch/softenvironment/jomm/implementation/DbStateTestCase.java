package ch.softenvironment.jomm.implementation;
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
 * Test DbState.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.1 $ $Date: 2005-05-06 12:37:17 $
 */
public class DbStateTestCase extends junit.framework.TestCase {

	/**
	 * Test class DbState.
	 *
	 * @param name java.lang.String
	 */
	public DbStateTestCase(String name) {
		super(name);
	}

	public void testChangeableObject() {
		DbState state = new DbState();
		assertTrue("DbState => undefined", state.isUndefined());

		state.setNext(DbState.NEW);
		assertTrue("DbState => new", state.isNew());

		state.setNext(DbState.SAVED);
		assertTrue("DbState => saved", state.isSaved());

		state.setNext(DbState.CHANGED);
		assertTrue("DbState => changed", state.isChanged());

		state.setNext(DbState.REMOVED_PENDING);
		assertTrue("DbState => removedPEnding", state.isRemovedPending());

		state.setNext(DbState.REMOVED);
		assertTrue("DbState => removed", state.isRemoved());
	}

	public void testReadOnly() {
		DbState state = new DbState();
		assertTrue("DbState => undefined", state.isUndefined());

		state.setNext(DbState.READ_ONLY);
		assertTrue("DbState => readOnly", state.isReadOnly());
	}
}
