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

import org.jhotdraw.util.Filler;
import junit.framework.TestCase;
// JUnitDoclet begin import
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
 * TestCase FillerTest is generated by
 * JUnitDoclet to hold the tests for Filler.
 * @see org.jhotdraw.util.Filler
 */
// JUnitDoclet end javadoc_class
public class FillerTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private Filler filler;
	// JUnitDoclet end class

	/**
	 * Constructor FillerTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public FillerTest(String name) {
		// JUnitDoclet begin method FillerTest
		super(name);
		// JUnitDoclet end method FillerTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public Filler createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new Filler(10, 10);
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
		filler = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		filler = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method getMinimumSize()
	/**
	 * Method testGetMinimumSize is testing getMinimumSize
	 * @see org.jhotdraw.util.Filler#getMinimumSize()
	 */
	// JUnitDoclet end javadoc_method getMinimumSize()
	public void testGetMinimumSize() throws Exception {
		// JUnitDoclet begin method getMinimumSize
		// JUnitDoclet end method getMinimumSize
	}

	// JUnitDoclet begin javadoc_method getPreferredSize()
	/**
	 * Method testGetPreferredSize is testing getPreferredSize
	 * @see org.jhotdraw.util.Filler#getPreferredSize()
	 */
	// JUnitDoclet end javadoc_method getPreferredSize()
	public void testGetPreferredSize() throws Exception {
		// JUnitDoclet begin method getPreferredSize
		// JUnitDoclet end method getPreferredSize
	}

	// JUnitDoclet begin javadoc_method getBackground()
	/**
	 * Method testGetBackground is testing getBackground
	 * @see org.jhotdraw.util.Filler#getBackground()
	 */
	// JUnitDoclet end javadoc_method getBackground()
	public void testGetBackground() throws Exception {
		// JUnitDoclet begin method getBackground
		// JUnitDoclet end method getBackground
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
