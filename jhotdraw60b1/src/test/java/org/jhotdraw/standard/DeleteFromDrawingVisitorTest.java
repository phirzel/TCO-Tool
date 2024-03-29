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
package org.jhotdraw.standard;

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
 * TestCase DeleteFromDrawingVisitorTest is generated by
 * JUnitDoclet to hold the tests for DeleteFromDrawingVisitor.
 * @see org.jhotdraw.standard.DeleteFromDrawingVisitor
 */
// JUnitDoclet end javadoc_class
public class DeleteFromDrawingVisitorTest
// JUnitDoclet begin extends_implements
extends JHDTestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private DeleteFromDrawingVisitor deletefromdrawingvisitor;
	// JUnitDoclet end class

	/**
	 * Constructor DeleteFromDrawingVisitorTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public DeleteFromDrawingVisitorTest(String name) {
		// JUnitDoclet begin method DeleteFromDrawingVisitorTest
		super(name);
		// JUnitDoclet end method DeleteFromDrawingVisitorTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public DeleteFromDrawingVisitor createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new DeleteFromDrawingVisitor(getDrawingEditor().view().drawing());
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
		getDrawingEditor().open();
		deletefromdrawingvisitor = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		deletefromdrawingvisitor = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method visitFigure()
	/**
	 * Method testVisitFigure is testing visitFigure
	 * @see org.jhotdraw.standard.DeleteFromDrawingVisitor#visitFigure(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method visitFigure()
	public void testVisitFigure() throws Exception {
		// JUnitDoclet begin method visitFigure
		// JUnitDoclet end method visitFigure
	}

	// JUnitDoclet begin javadoc_method visitHandle()
	/**
	 * Method testVisitHandle is testing visitHandle
	 * @see org.jhotdraw.standard.DeleteFromDrawingVisitor#visitHandle(org.jhotdraw.framework.Handle)
	 */
	// JUnitDoclet end javadoc_method visitHandle()
	public void testVisitHandle() throws Exception {
		// JUnitDoclet begin method visitHandle
		// JUnitDoclet end method visitHandle
	}

	// JUnitDoclet begin javadoc_method visitFigureChangeListener()
	/**
	 * Method testVisitFigureChangeListener is testing visitFigureChangeListener
	 * @see org.jhotdraw.standard.DeleteFromDrawingVisitor#visitFigureChangeListener(org.jhotdraw.framework.FigureChangeListener)
	 */
	// JUnitDoclet end javadoc_method visitFigureChangeListener()
	public void testVisitFigureChangeListener() throws Exception {
		// JUnitDoclet begin method visitFigureChangeListener
		// JUnitDoclet end method visitFigureChangeListener
	}

	// JUnitDoclet begin javadoc_method getDeletedFigures()
	/**
	 * Method testGetDeletedFigures is testing getDeletedFigures
	 * @see org.jhotdraw.standard.DeleteFromDrawingVisitor#getDeletedFigures()
	 */
	// JUnitDoclet end javadoc_method getDeletedFigures()
	public void testGetDeletedFigures() throws Exception {
		// JUnitDoclet begin method getDeletedFigures
		// JUnitDoclet end method getDeletedFigures
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
