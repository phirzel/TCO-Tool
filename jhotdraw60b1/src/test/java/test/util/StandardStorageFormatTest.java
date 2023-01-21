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
package org.jhotdraw.test.util;

// JUnitDoclet begin import
import org.jhotdraw.util.StandardStorageFormat;
import junit.framework.TestCase;
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
 * TestCase StandardStorageFormatTest is generated by
 * JUnitDoclet to hold the tests for StandardStorageFormat.
 * @see org.jhotdraw.util.StandardStorageFormat
 */
// JUnitDoclet end javadoc_class
public class StandardStorageFormatTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private StandardStorageFormat standardstorageformat;
	// JUnitDoclet end class

	/**
	 * Constructor StandardStorageFormatTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public StandardStorageFormatTest(String name) {
		// JUnitDoclet begin method StandardStorageFormatTest
		super(name);
		// JUnitDoclet end method StandardStorageFormatTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public StandardStorageFormat createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new StandardStorageFormat();
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
		standardstorageformat = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		standardstorageformat = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method setFileExtension()
	/**
	 * Method testSetGetFileExtension is testing setFileExtension
	 * and getFileExtension together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.util.StandardStorageFormat#setFileExtension(java.lang.String)
	 * @see org.jhotdraw.util.StandardStorageFormat#getFileExtension()
	 */
	// JUnitDoclet end javadoc_method setFileExtension()
	public void testSetGetFileExtension() throws Exception {
		// JUnitDoclet begin method setFileExtension getFileExtension
		java.lang.String[] tests = { "", " ", "a", "A", "�", "�", "0123456789", "012345678901234567890", "\n", null };

		for (int i = 0; i < tests.length; i++) {
			standardstorageformat.setFileExtension(tests[i]);
			assertEquals(tests[i], standardstorageformat.getFileExtension());
		}
		// JUnitDoclet end method setFileExtension getFileExtension
	}

	// JUnitDoclet begin javadoc_method createFileDescription()
	/**
	 * Method testCreateFileDescription is testing createFileDescription
	 * @see org.jhotdraw.util.StandardStorageFormat#createFileDescription()
	 */
	// JUnitDoclet end javadoc_method createFileDescription()
	public void testCreateFileDescription() throws Exception {
		// JUnitDoclet begin method createFileDescription
		// JUnitDoclet end method createFileDescription
	}

	// JUnitDoclet begin javadoc_method setFileDescription()
	/**
	 * Method testSetGetFileDescription is testing setFileDescription
	 * and getFileDescription together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.util.StandardStorageFormat#setFileDescription(java.lang.String)
	 * @see org.jhotdraw.util.StandardStorageFormat#getFileDescription()
	 */
	// JUnitDoclet end javadoc_method setFileDescription()
	public void testSetGetFileDescription() throws Exception {
		// JUnitDoclet begin method setFileDescription getFileDescription
		java.lang.String[] tests = { "", " ", "a", "A", "�", "�", "0123456789", "012345678901234567890", "\n", null };

		for (int i = 0; i < tests.length; i++) {
			standardstorageformat.setFileDescription(tests[i]);
			assertEquals(tests[i], standardstorageformat.getFileDescription());
		}
		// JUnitDoclet end method setFileDescription getFileDescription
	}

	// JUnitDoclet begin javadoc_method setFileFilter()
	/**
	 * Method testSetGetFileFilter is testing setFileFilter
	 * and getFileFilter together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.util.StandardStorageFormat#setFileFilter(javax.swing.filechooser.FileFilter)
	 * @see org.jhotdraw.util.StandardStorageFormat#getFileFilter()
	 */
	// JUnitDoclet end javadoc_method setFileFilter()
	public void testSetGetFileFilter() throws Exception {
		// JUnitDoclet begin method setFileFilter getFileFilter
		javax.swing.filechooser.FileFilter[] tests = { null, null };

		for (int i = 0; i < tests.length; i++) {
			standardstorageformat.setFileFilter(tests[i]);
			assertEquals(tests[i], standardstorageformat.getFileFilter());
		}
		// JUnitDoclet end method setFileFilter getFileFilter
	}

	// JUnitDoclet begin javadoc_method store()
	/**
	 * Method testStore is testing store
	 * @see org.jhotdraw.util.StandardStorageFormat#store(java.lang.String, org.jhotdraw.framework.Drawing)
	 */
	// JUnitDoclet end javadoc_method store()
	public void testStore() throws Exception {
		// JUnitDoclet begin method store
		// JUnitDoclet end method store
	}

	// JUnitDoclet begin javadoc_method restore()
	/**
	 * Method testRestore is testing restore
	 * @see org.jhotdraw.util.StandardStorageFormat#restore(java.lang.String)
	 */
	// JUnitDoclet end javadoc_method restore()
	public void testRestore() throws Exception {
		// JUnitDoclet begin method restore
		// JUnitDoclet end method restore
	}

	// JUnitDoclet begin javadoc_method equals()
	/**
	 * Method testEquals is testing equals
	 * @see org.jhotdraw.util.StandardStorageFormat#equals(java.lang.Object)
	 */
	// JUnitDoclet end javadoc_method equals()
	public void testEquals() throws Exception {
		// JUnitDoclet begin method equals
		// JUnitDoclet end method equals
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