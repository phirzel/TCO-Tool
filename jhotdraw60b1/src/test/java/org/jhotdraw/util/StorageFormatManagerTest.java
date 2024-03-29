/*
 * @(#)Test.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	(c) by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.util;

import junit.framework.TestCase;

// JUnitDoclet begin import
import org.jhotdraw.util.StandardStorageFormat;
import org.jhotdraw.util.StorageFormatManager;
// JUnitDoclet end import

/*
 * Generated by JUnitDoclet, a tool provided by
 * ObjectFab GmbH under LGPL.
 * Please see www.junitdoclet.org, www.gnu.org
 * and www.objectfab.de for informations about
 * the tool, the licence and the authors.
 */

// JUnitDoclet begin javadoc_class
/**
 * TestCase StorageFormatManagerTest is generated by
 * JUnitDoclet to hold the tests for StorageFormatManager.
 * @see org.jhotdraw.util.StorageFormatManager
 */
// JUnitDoclet end javadoc_class
public class StorageFormatManagerTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private StorageFormatManager storageformatmanager;
	// JUnitDoclet end class

	/**
	 * Constructor StorageFormatManagerTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public StorageFormatManagerTest(String name) {
		// JUnitDoclet begin method StorageFormatManagerTest
		super(name);
		// JUnitDoclet end method StorageFormatManagerTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public StorageFormatManager createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new StorageFormatManager();
		// JUnitDoclet end method testcase.createInstance
	}

	/**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
	protected void setUp() throws Exception {
		// JUnitDoclet begin method testcase.setUp
		super.setUp();
		storageformatmanager = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		storageformatmanager = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method addStorageFormat()
	/**
	 * Method testAddStorageFormat is testing addStorageFormat
	 * @see org.jhotdraw.util.StorageFormatManager#addStorageFormat(org.jhotdraw.util.StorageFormat)
	 */
	// JUnitDoclet end javadoc_method addStorageFormat()
	public void testAddStorageFormat() throws Exception {
		// JUnitDoclet begin method addStorageFormat
		// JUnitDoclet end method addStorageFormat
	}

	// JUnitDoclet begin javadoc_method removeStorageFormat()
	/**
	 * Method testRemoveStorageFormat is testing removeStorageFormat
	 * @see org.jhotdraw.util.StorageFormatManager#removeStorageFormat(org.jhotdraw.util.StorageFormat)
	 */
	// JUnitDoclet end javadoc_method removeStorageFormat()
	public void testRemoveStorageFormat() throws Exception {
		// JUnitDoclet begin method removeStorageFormat
		// JUnitDoclet end method removeStorageFormat
	}

	// JUnitDoclet begin javadoc_method containsStorageFormat()
	/**
	 * Method testContainsStorageFormat is testing containsStorageFormat
	 * @see org.jhotdraw.util.StorageFormatManager#containsStorageFormat(org.jhotdraw.util.StorageFormat)
	 */
	// JUnitDoclet end javadoc_method containsStorageFormat()
	public void testContainsStorageFormat() throws Exception {
		// JUnitDoclet begin method containsStorageFormat
		// JUnitDoclet end method containsStorageFormat
	}

	// JUnitDoclet begin javadoc_method setDefaultStorageFormat()
	/**
	 * Method testSetGetDefaultStorageFormat is testing setDefaultStorageFormat
	 * and getDefaultStorageFormat together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.util.StorageFormatManager#setDefaultStorageFormat(org.jhotdraw.util.StorageFormat)
	 * @see org.jhotdraw.util.StorageFormatManager#getDefaultStorageFormat()
	 */
	// JUnitDoclet end javadoc_method setDefaultStorageFormat()
	public void testSetGetDefaultStorageFormat() throws Exception {
		// JUnitDoclet begin method setDefaultStorageFormat getDefaultStorageFormat
		org.jhotdraw.util.StorageFormat[] tests = { new StandardStorageFormat(), null };

		for (int i = 0; i < tests.length; i++) {
			storageformatmanager.setDefaultStorageFormat(tests[i]);
			assertEquals(tests[i], storageformatmanager.getDefaultStorageFormat());
		}
		// JUnitDoclet end method setDefaultStorageFormat getDefaultStorageFormat
	}

	// JUnitDoclet begin javadoc_method registerFileFilters()
	/**
	 * Method testRegisterFileFilters is testing registerFileFilters
	 * @see org.jhotdraw.util.StorageFormatManager#registerFileFilters(javax.swing.JFileChooser)
	 */
	// JUnitDoclet end javadoc_method registerFileFilters()
	public void testRegisterFileFilters() throws Exception {
		// JUnitDoclet begin method registerFileFilters
		// JUnitDoclet end method registerFileFilters
	}

	// JUnitDoclet begin javadoc_method findStorageFormat()
	/**
	 * Method testFindStorageFormat is testing findStorageFormat
	 * @see org.jhotdraw.util.StorageFormatManager#findStorageFormat(javax.swing.filechooser.FileFilter)
	 */
	// JUnitDoclet end javadoc_method findStorageFormat()
	public void testFindStorageFormat() throws Exception {
		// JUnitDoclet begin method findStorageFormat
		// JUnitDoclet end method findStorageFormat
	}

	// JUnitDoclet begin javadoc_method testVault
	/**
	 * JUnitDoclet moves marker to this method, if there is not match
	 * for them in the regenerated code and if the marker is not empty.
	 * This way, no test gets lost when regenerating after renaming.
	 * <b>Method testVault is supposed to be empty.</b>
	 */
	// JUnitDoclet end javadoc_method testVault
	public void testVault() throws Exception {
		// JUnitDoclet begin method testcase.testVault
		// JUnitDoclet end method testcase.testVault
	}

}
