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

// JUnitDoclet begin import
import org.jhotdraw.JHDTestCase;
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
 * TestCase UndoCommandTest is generated by
 * JUnitDoclet to hold the tests for UndoCommand.
 * @see org.jhotdraw.util.UndoCommand
 */
// JUnitDoclet end javadoc_class
public class UndoCommandTest
// JUnitDoclet begin extends_implements
extends JHDTestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private UndoCommand undocommand;
	// JUnitDoclet end class

	/**
	 * Constructor UndoCommandTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public UndoCommandTest(String name) {
		// JUnitDoclet begin method UndoCommandTest
		super(name);
		// JUnitDoclet end method UndoCommandTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public UndoCommand createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new UndoCommand("TestUndo", getDrawingEditor());
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
		undocommand = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		undocommand = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method execute()
	/**
	 * Method testExecute is testing execute
	 * @see org.jhotdraw.util.UndoCommand#execute()
	 */
	// JUnitDoclet end javadoc_method execute()
	public void testExecute() throws Exception {
		// JUnitDoclet begin method execute
		// JUnitDoclet end method execute
	}

	// JUnitDoclet begin javadoc_method isExecutableWithView()
	/**
	 * Method testIsExecutableWithView is testing isExecutableWithView
	 * @see org.jhotdraw.util.UndoCommand#isExecutableWithView()
	 */
	// JUnitDoclet end javadoc_method isExecutableWithView()
	public void testIsExecutableWithView() throws Exception {
		// JUnitDoclet begin method isExecutableWithView
		// JUnitDoclet end method isExecutableWithView
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
