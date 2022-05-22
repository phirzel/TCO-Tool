/*
 * @(#)Test.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	� by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.test.util;

import java.awt.Point;

import org.jhotdraw.util.PaletteLayout;

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
 * TestCase PaletteLayoutTest is generated by
 * JUnitDoclet to hold the tests for PaletteLayout.
 * @see org.jhotdraw.util.PaletteLayout
 */
// JUnitDoclet end javadoc_class
public class PaletteLayoutTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private PaletteLayout palettelayout;
	// JUnitDoclet end class

	/**
	 * Constructor PaletteLayoutTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public PaletteLayoutTest(String name) {
		// JUnitDoclet begin method PaletteLayoutTest
		super(name);
		// JUnitDoclet end method PaletteLayoutTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public PaletteLayout createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new PaletteLayout(10, new Point(5, 5));
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
		palettelayout = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		palettelayout = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method addLayoutComponent()
	/**
	 * Method testAddLayoutComponent is testing addLayoutComponent
	 * @see org.jhotdraw.util.PaletteLayout#addLayoutComponent(java.lang.String, java.awt.Component)
	 */
	// JUnitDoclet end javadoc_method addLayoutComponent()
	public void testAddLayoutComponent() throws Exception {
		// JUnitDoclet begin method addLayoutComponent
		// JUnitDoclet end method addLayoutComponent
	}

	// JUnitDoclet begin javadoc_method removeLayoutComponent()
	/**
	 * Method testRemoveLayoutComponent is testing removeLayoutComponent
	 * @see org.jhotdraw.util.PaletteLayout#removeLayoutComponent(java.awt.Component)
	 */
	// JUnitDoclet end javadoc_method removeLayoutComponent()
	public void testRemoveLayoutComponent() throws Exception {
		// JUnitDoclet begin method removeLayoutComponent
		// JUnitDoclet end method removeLayoutComponent
	}

	// JUnitDoclet begin javadoc_method preferredLayoutSize()
	/**
	 * Method testPreferredLayoutSize is testing preferredLayoutSize
	 * @see org.jhotdraw.util.PaletteLayout#preferredLayoutSize(java.awt.Container)
	 */
	// JUnitDoclet end javadoc_method preferredLayoutSize()
	public void testPreferredLayoutSize() throws Exception {
		// JUnitDoclet begin method preferredLayoutSize
		// JUnitDoclet end method preferredLayoutSize
	}

	// JUnitDoclet begin javadoc_method minimumLayoutSize()
	/**
	 * Method testMinimumLayoutSize is testing minimumLayoutSize
	 * @see org.jhotdraw.util.PaletteLayout#minimumLayoutSize(java.awt.Container)
	 */
	// JUnitDoclet end javadoc_method minimumLayoutSize()
	public void testMinimumLayoutSize() throws Exception {
		// JUnitDoclet begin method minimumLayoutSize
		// JUnitDoclet end method minimumLayoutSize
	}

	// JUnitDoclet begin javadoc_method layoutContainer()
	/**
	 * Method testLayoutContainer is testing layoutContainer
	 * @see org.jhotdraw.util.PaletteLayout#layoutContainer(java.awt.Container)
	 */
	// JUnitDoclet end javadoc_method layoutContainer()
	public void testLayoutContainer() throws Exception {
		// JUnitDoclet begin method layoutContainer
		// JUnitDoclet end method layoutContainer
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
