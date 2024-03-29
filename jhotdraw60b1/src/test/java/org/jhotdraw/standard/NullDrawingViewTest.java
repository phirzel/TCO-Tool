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
 * TestCase NullDrawingViewTest is generated by
 * JUnitDoclet to hold the tests for NullDrawingView.
 * @see org.jhotdraw.standard.NullDrawingView
 */
// JUnitDoclet end javadoc_class
public class NullDrawingViewTest
// JUnitDoclet begin extends_implements
extends JHDTestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private NullDrawingView nulldrawingview;
	// JUnitDoclet end class

	/**
	 * Constructor NullDrawingViewTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public NullDrawingViewTest(String name) {
		// JUnitDoclet begin method NullDrawingViewTest
		super(name);
		// JUnitDoclet end method NullDrawingViewTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public NullDrawingView createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return (NullDrawingView)NullDrawingView.getManagedDrawingView(getDrawingEditor());
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
		nulldrawingview = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		nulldrawingview = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method setEditor()
	/**
	 * Method testSetEditor is testing setEditor
	 * @see org.jhotdraw.standard.NullDrawingView#setEditor(org.jhotdraw.framework.DrawingEditor)
	 */
	// JUnitDoclet end javadoc_method setEditor()
	public void testSetEditor() throws Exception {
		// JUnitDoclet begin method setEditor
		// JUnitDoclet end method setEditor
	}

	// JUnitDoclet begin javadoc_method tool()
	/**
	 * Method testTool is testing tool
	 * @see org.jhotdraw.standard.NullDrawingView#tool()
	 */
	// JUnitDoclet end javadoc_method tool()
	public void testTool() throws Exception {
		// JUnitDoclet begin method tool
		// JUnitDoclet end method tool
	}

	// JUnitDoclet begin javadoc_method drawing()
	/**
	 * Method testDrawing is testing drawing
	 * @see org.jhotdraw.standard.NullDrawingView#drawing()
	 */
	// JUnitDoclet end javadoc_method drawing()
	public void testDrawing() throws Exception {
		// JUnitDoclet begin method drawing
		// JUnitDoclet end method drawing
	}

	// JUnitDoclet begin javadoc_method setDrawing()
	/**
	 * Method testSetDrawing is testing setDrawing
	 * @see org.jhotdraw.standard.NullDrawingView#setDrawing(org.jhotdraw.framework.Drawing)
	 */
	// JUnitDoclet end javadoc_method setDrawing()
	public void testSetDrawing() throws Exception {
		// JUnitDoclet begin method setDrawing
		// JUnitDoclet end method setDrawing
	}

	// JUnitDoclet begin javadoc_method editor()
	/**
	 * Method testEditor is testing editor
	 * @see org.jhotdraw.standard.NullDrawingView#editor()
	 */
	// JUnitDoclet end javadoc_method editor()
	public void testEditor() throws Exception {
		// JUnitDoclet begin method editor
		// JUnitDoclet end method editor
	}

	// JUnitDoclet begin javadoc_method add()
	/**
	 * Method testAdd is testing add
	 * @see org.jhotdraw.standard.NullDrawingView#add(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method add()
	public void testAdd() throws Exception {
		// JUnitDoclet begin method add
		// JUnitDoclet end method add
	}

	// JUnitDoclet begin javadoc_method remove()
	/**
	 * Method testRemove is testing remove
	 * @see org.jhotdraw.standard.NullDrawingView#remove(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method remove()
	public void testRemove() throws Exception {
		// JUnitDoclet begin method remove
		// JUnitDoclet end method remove
	}

	// JUnitDoclet begin javadoc_method addAll()
	/**
	 * Method testAddAll is testing addAll
	 * @see org.jhotdraw.standard.NullDrawingView#addAll(java.util.Collection)
	 */
	// JUnitDoclet end javadoc_method addAll()
	public void testAddAll() throws Exception {
		// JUnitDoclet begin method addAll
		// JUnitDoclet end method addAll
	}

	// JUnitDoclet begin javadoc_method getSize()
	/**
	 * Method testGetSize is testing getSize
	 * @see org.jhotdraw.standard.NullDrawingView#getSize()
	 */
	// JUnitDoclet end javadoc_method getSize()
	public void testGetSize() throws Exception {
		// JUnitDoclet begin method getSize
		// JUnitDoclet end method getSize
	}

	// JUnitDoclet begin javadoc_method getMinimumSize()
	/**
	 * Method testGetMinimumSize is testing getMinimumSize
	 * @see org.jhotdraw.standard.NullDrawingView#getMinimumSize()
	 */
	// JUnitDoclet end javadoc_method getMinimumSize()
	public void testGetMinimumSize() throws Exception {
		// JUnitDoclet begin method getMinimumSize
		// JUnitDoclet end method getMinimumSize
	}

	// JUnitDoclet begin javadoc_method getPreferredSize()
	/**
	 * Method testGetPreferredSize is testing getPreferredSize
	 * @see org.jhotdraw.standard.NullDrawingView#getPreferredSize()
	 */
	// JUnitDoclet end javadoc_method getPreferredSize()
	public void testGetPreferredSize() throws Exception {
		// JUnitDoclet begin method getPreferredSize
		// JUnitDoclet end method getPreferredSize
	}

	// JUnitDoclet begin javadoc_method setDisplayUpdate()
	/**
	 * Method testSetGetDisplayUpdate is testing setDisplayUpdate
	 * and getDisplayUpdate together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.standard.NullDrawingView#setDisplayUpdate(org.jhotdraw.framework.Painter)
	 * @see org.jhotdraw.standard.NullDrawingView#getDisplayUpdate()
	 */
	// JUnitDoclet end javadoc_method setDisplayUpdate()
	public void testSetGetDisplayUpdate() throws Exception {
		// JUnitDoclet begin method setDisplayUpdate getDisplayUpdate
		org.jhotdraw.framework.Painter[] tests = { new SimpleUpdateStrategy(), null };

		for (int i = 0; i < tests.length; i++) {
			nulldrawingview.setDisplayUpdate(tests[i]);
			assertEquals(tests[i], nulldrawingview.getDisplayUpdate());
		}
		// JUnitDoclet end method setDisplayUpdate getDisplayUpdate
	}

	// JUnitDoclet begin javadoc_method selection()
	/**
	 * Method testSelection is testing selection
	 * @see org.jhotdraw.standard.NullDrawingView#selection()
	 */
	// JUnitDoclet end javadoc_method selection()
	public void testSelection() throws Exception {
		// JUnitDoclet begin method selection
		// JUnitDoclet end method selection
	}

	// JUnitDoclet begin javadoc_method selectionZOrdered()
	/**
	 * Method testSelectionZOrdered is testing selectionZOrdered
	 * @see org.jhotdraw.standard.NullDrawingView#selectionZOrdered()
	 */
	// JUnitDoclet end javadoc_method selectionZOrdered()
	public void testSelectionZOrdered() throws Exception {
		// JUnitDoclet begin method selectionZOrdered
		// JUnitDoclet end method selectionZOrdered
	}

	// JUnitDoclet begin javadoc_method selectionCount()
	/**
	 * Method testSelectionCount is testing selectionCount
	 * @see org.jhotdraw.standard.NullDrawingView#selectionCount()
	 */
	// JUnitDoclet end javadoc_method selectionCount()
	public void testSelectionCount() throws Exception {
		// JUnitDoclet begin method selectionCount
		// JUnitDoclet end method selectionCount
	}

	// JUnitDoclet begin javadoc_method isFigureSelected()
	/**
	 * Method testIsFigureSelected is testing isFigureSelected
	 * @see org.jhotdraw.standard.NullDrawingView#isFigureSelected(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method isFigureSelected()
	public void testIsFigureSelected() throws Exception {
		// JUnitDoclet begin method isFigureSelected
		// JUnitDoclet end method isFigureSelected
	}

	// JUnitDoclet begin javadoc_method addToSelection()
	/**
	 * Method testAddToSelection is testing addToSelection
	 * @see org.jhotdraw.standard.NullDrawingView#addToSelection(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method addToSelection()
	public void testAddToSelection() throws Exception {
		// JUnitDoclet begin method addToSelection
		// JUnitDoclet end method addToSelection
	}

	// JUnitDoclet begin javadoc_method addToSelectionAll()
	/**
	 * Method testAddToSelectionAll is testing addToSelectionAll
	 * @see org.jhotdraw.standard.NullDrawingView#addToSelectionAll(java.util.Collection)
	 */
	// JUnitDoclet end javadoc_method addToSelectionAll()
	public void testAddToSelectionAll() throws Exception {
		// JUnitDoclet begin method addToSelectionAll
		// JUnitDoclet end method addToSelectionAll
	}

	// JUnitDoclet begin javadoc_method removeFromSelection()
	/**
	 * Method testRemoveFromSelection is testing removeFromSelection
	 * @see org.jhotdraw.standard.NullDrawingView#removeFromSelection(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method removeFromSelection()
	public void testRemoveFromSelection() throws Exception {
		// JUnitDoclet begin method removeFromSelection
		// JUnitDoclet end method removeFromSelection
	}

	// JUnitDoclet begin javadoc_method toggleSelection()
	/**
	 * Method testToggleSelection is testing toggleSelection
	 * @see org.jhotdraw.standard.NullDrawingView#toggleSelection(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method toggleSelection()
	public void testToggleSelection() throws Exception {
		// JUnitDoclet begin method toggleSelection
		// JUnitDoclet end method toggleSelection
	}

	// JUnitDoclet begin javadoc_method clearSelection()
	/**
	 * Method testClearSelection is testing clearSelection
	 * @see org.jhotdraw.standard.NullDrawingView#clearSelection()
	 */
	// JUnitDoclet end javadoc_method clearSelection()
	public void testClearSelection() throws Exception {
		// JUnitDoclet begin method clearSelection
		// JUnitDoclet end method clearSelection
	}

	// JUnitDoclet begin javadoc_method getFigureSelection()
	/**
	 * Method testGetFigureSelection is testing getFigureSelection
	 * @see org.jhotdraw.standard.NullDrawingView#getFigureSelection()
	 */
	// JUnitDoclet end javadoc_method getFigureSelection()
	public void testGetFigureSelection() throws Exception {
		// JUnitDoclet begin method getFigureSelection
		// JUnitDoclet end method getFigureSelection
	}

	// JUnitDoclet begin javadoc_method findHandle()
	/**
	 * Method testFindHandle is testing findHandle
	 * @see org.jhotdraw.standard.NullDrawingView#findHandle(int, int)
	 */
	// JUnitDoclet end javadoc_method findHandle()
	public void testFindHandle() throws Exception {
		// JUnitDoclet begin method findHandle
		// JUnitDoclet end method findHandle
	}

	// JUnitDoclet begin javadoc_method lastClick()
	/**
	 * Method testLastClick is testing lastClick
	 * @see org.jhotdraw.standard.NullDrawingView#lastClick()
	 */
	// JUnitDoclet end javadoc_method lastClick()
	public void testLastClick() throws Exception {
		// JUnitDoclet begin method lastClick
		// JUnitDoclet end method lastClick
	}

	// JUnitDoclet begin javadoc_method setConstrainer()
	/**
	 * NullDrawingView.setConstrainer is a no-op.
	 *   
	 * @see org.jhotdraw.standard.NullDrawingView#setConstrainer(org.jhotdraw.framework.PointConstrainer)
	 * @see org.jhotdraw.standard.NullDrawingView#getConstrainer()
	 */
	// JUnitDoclet end javadoc_method setConstrainer()
	public void testSetGetConstrainer() throws Exception {
		// JUnitDoclet begin method setConstrainer getConstrainer
		// Do nothing: NullDrawingView.setConstrainer is a no-op.
		// JUnitDoclet end method setConstrainer getConstrainer
	}

	// JUnitDoclet begin javadoc_method checkDamage()
	/**
	 * Method testCheckDamage is testing checkDamage
	 * @see org.jhotdraw.standard.NullDrawingView#checkDamage()
	 */
	// JUnitDoclet end javadoc_method checkDamage()
	public void testCheckDamage() throws Exception {
		// JUnitDoclet begin method checkDamage
		// JUnitDoclet end method checkDamage
	}

	// JUnitDoclet begin javadoc_method repairDamage()
	/**
	 * Method testRepairDamage is testing repairDamage
	 * @see org.jhotdraw.standard.NullDrawingView#repairDamage()
	 */
	// JUnitDoclet end javadoc_method repairDamage()
	public void testRepairDamage() throws Exception {
		// JUnitDoclet begin method repairDamage
		// JUnitDoclet end method repairDamage
	}

	// JUnitDoclet begin javadoc_method paint()
	/**
	 * Method testPaint is testing paint
	 * @see org.jhotdraw.standard.NullDrawingView#paint(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method paint()
	public void testPaint() throws Exception {
		// JUnitDoclet begin method paint
		// JUnitDoclet end method paint
	}

	// JUnitDoclet begin javadoc_method createImage()
	/**
	 * Method testCreateImage is testing createImage
	 * @see org.jhotdraw.standard.NullDrawingView#createImage(int, int)
	 */
	// JUnitDoclet end javadoc_method createImage()
	public void testCreateImage() throws Exception {
		// JUnitDoclet begin method createImage
		// JUnitDoclet end method createImage
	}

	// JUnitDoclet begin javadoc_method getGraphics()
	/**
	 * Method testGetGraphics is testing getGraphics
	 * @see org.jhotdraw.standard.NullDrawingView#getGraphics()
	 */
	// JUnitDoclet end javadoc_method getGraphics()
	public void testGetGraphics() throws Exception {
		// JUnitDoclet begin method getGraphics
		// JUnitDoclet end method getGraphics
	}

	// JUnitDoclet begin javadoc_method setBackground()
	/**
	 * Method testSetGetBackground is testing setBackground
	 * and getBackground together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.standard.NullDrawingView#setBackground(java.awt.Color)
	 * @see org.jhotdraw.standard.NullDrawingView#getBackground()
	 */
	// JUnitDoclet end javadoc_method setBackground()
	public void testSetGetBackground() throws Exception {
		// JUnitDoclet begin method setBackground getBackground
		java.awt.Color[] tests = { java.awt.Color.BLACK, null };

		for (int i = 0; i < tests.length; i++) {
			nulldrawingview.setBackground(tests[i]);
			assertEquals(tests[i], nulldrawingview.getBackground());
		}
		// JUnitDoclet end method setBackground getBackground
	}

	// JUnitDoclet begin javadoc_method drawAll()
	/**
	 * Method testDrawAll is testing drawAll
	 * @see org.jhotdraw.standard.NullDrawingView#drawAll(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method drawAll()
	public void testDrawAll() throws Exception {
		// JUnitDoclet begin method drawAll
		// JUnitDoclet end method drawAll
	}

	// JUnitDoclet begin javadoc_method draw()
	/**
	 * Method testDraw is testing draw
	 * @see org.jhotdraw.standard.NullDrawingView#draw(java.awt.Graphics, org.jhotdraw.framework.FigureEnumeration)
	 */
	// JUnitDoclet end javadoc_method draw()
	public void testDraw() throws Exception {
		// JUnitDoclet begin method draw
		// JUnitDoclet end method draw
	}

	// JUnitDoclet begin javadoc_method drawHandles()
	/**
	 * Method testDrawHandles is testing drawHandles
	 * @see org.jhotdraw.standard.NullDrawingView#drawHandles(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method drawHandles()
	public void testDrawHandles() throws Exception {
		// JUnitDoclet begin method drawHandles
		// JUnitDoclet end method drawHandles
	}

	// JUnitDoclet begin javadoc_method drawDrawing()
	/**
	 * Method testDrawDrawing is testing drawDrawing
	 * @see org.jhotdraw.standard.NullDrawingView#drawDrawing(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method drawDrawing()
	public void testDrawDrawing() throws Exception {
		// JUnitDoclet begin method drawDrawing
		// JUnitDoclet end method drawDrawing
	}

	// JUnitDoclet begin javadoc_method drawBackground()
	/**
	 * Method testDrawBackground is testing drawBackground
	 * @see org.jhotdraw.standard.NullDrawingView#drawBackground(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method drawBackground()
	public void testDrawBackground() throws Exception {
		// JUnitDoclet begin method drawBackground
		// JUnitDoclet end method drawBackground
	}

	// JUnitDoclet begin javadoc_method setCursor()
	/**
	 * Method testSetCursor is testing setCursor
	 * @see org.jhotdraw.standard.NullDrawingView#setCursor(java.awt.Cursor)
	 */
	// JUnitDoclet end javadoc_method setCursor()
	public void testSetCursor() throws Exception {
		// JUnitDoclet begin method setCursor
		// JUnitDoclet end method setCursor
	}

	// JUnitDoclet begin javadoc_method freezeView()
	/**
	 * Method testFreezeView is testing freezeView
	 * @see org.jhotdraw.standard.NullDrawingView#freezeView()
	 */
	// JUnitDoclet end javadoc_method freezeView()
	public void testFreezeView() throws Exception {
		// JUnitDoclet begin method freezeView
		// JUnitDoclet end method freezeView
	}

	// JUnitDoclet begin javadoc_method unfreezeView()
	/**
	 * Method testUnfreezeView is testing unfreezeView
	 * @see org.jhotdraw.standard.NullDrawingView#unfreezeView()
	 */
	// JUnitDoclet end javadoc_method unfreezeView()
	public void testUnfreezeView() throws Exception {
		// JUnitDoclet begin method unfreezeView
		// JUnitDoclet end method unfreezeView
	}

	// JUnitDoclet begin javadoc_method addFigureSelectionListener()
	/**
	 * Method testAddFigureSelectionListener is testing addFigureSelectionListener
	 * @see org.jhotdraw.standard.NullDrawingView#addFigureSelectionListener(org.jhotdraw.framework.FigureSelectionListener)
	 */
	// JUnitDoclet end javadoc_method addFigureSelectionListener()
	public void testAddFigureSelectionListener() throws Exception {
		// JUnitDoclet begin method addFigureSelectionListener
		// JUnitDoclet end method addFigureSelectionListener
	}

	// JUnitDoclet begin javadoc_method removeFigureSelectionListener()
	/**
	 * Method testRemoveFigureSelectionListener is testing removeFigureSelectionListener
	 * @see org.jhotdraw.standard.NullDrawingView#removeFigureSelectionListener(org.jhotdraw.framework.FigureSelectionListener)
	 */
	// JUnitDoclet end javadoc_method removeFigureSelectionListener()
	public void testRemoveFigureSelectionListener() throws Exception {
		// JUnitDoclet begin method removeFigureSelectionListener
		// JUnitDoclet end method removeFigureSelectionListener
	}

	// JUnitDoclet begin javadoc_method getConnectionFigures()
	/**
	 * Method testGetConnectionFigures is testing getConnectionFigures
	 * @see org.jhotdraw.standard.NullDrawingView#getConnectionFigures(org.jhotdraw.framework.Figure)
	 */
	// JUnitDoclet end javadoc_method getConnectionFigures()
	public void testGetConnectionFigures() throws Exception {
		// JUnitDoclet begin method getConnectionFigures
		// JUnitDoclet end method getConnectionFigures
	}

	// JUnitDoclet begin javadoc_method insertFigures()
	/**
	 * Method testInsertFigures is testing insertFigures
	 * @see org.jhotdraw.standard.NullDrawingView#insertFigures(org.jhotdraw.framework.FigureEnumeration, int, int, boolean)
	 */
	// JUnitDoclet end javadoc_method insertFigures()
	public void testInsertFigures() throws Exception {
		// JUnitDoclet begin method insertFigures
		// JUnitDoclet end method insertFigures
	}

	// JUnitDoclet begin javadoc_method drawingInvalidated()
	/**
	 * Method testDrawingInvalidated is testing drawingInvalidated
	 * @see org.jhotdraw.standard.NullDrawingView#drawingInvalidated(org.jhotdraw.framework.DrawingChangeEvent)
	 */
	// JUnitDoclet end javadoc_method drawingInvalidated()
	public void testDrawingInvalidated() throws Exception {
		// JUnitDoclet begin method drawingInvalidated
		// JUnitDoclet end method drawingInvalidated
	}

	// JUnitDoclet begin javadoc_method drawingRequestUpdate()
	/**
	 * Method testDrawingRequestUpdate is testing drawingRequestUpdate
	 * @see org.jhotdraw.standard.NullDrawingView#drawingRequestUpdate(org.jhotdraw.framework.DrawingChangeEvent)
	 */
	// JUnitDoclet end javadoc_method drawingRequestUpdate()
	public void testDrawingRequestUpdate() throws Exception {
		// JUnitDoclet begin method drawingRequestUpdate
		// JUnitDoclet end method drawingRequestUpdate
	}

	// JUnitDoclet begin javadoc_method drawingTitleChanged()
	/**
	 * Method testDrawingTitleChanged is testing drawingTitleChanged
	 * @see org.jhotdraw.standard.NullDrawingView#drawingTitleChanged(org.jhotdraw.framework.DrawingChangeEvent)
	 */
	// JUnitDoclet end javadoc_method drawingTitleChanged()
	public void testDrawingTitleChanged() throws Exception {
		// JUnitDoclet begin method drawingTitleChanged
		// JUnitDoclet end method drawingTitleChanged
	}

	// JUnitDoclet begin javadoc_method isInteractive()
	/**
	 * Method testIsInteractive is testing isInteractive
	 * @see org.jhotdraw.standard.NullDrawingView#isInteractive()
	 */
	// JUnitDoclet end javadoc_method isInteractive()
	public void testIsInteractive() throws Exception {
		// JUnitDoclet begin method isInteractive
		// JUnitDoclet end method isInteractive
	}

	// JUnitDoclet begin javadoc_method getManagedDrawingView()
	/**
	 * Method testGetManagedDrawingView is testing getManagedDrawingView
	 * @see org.jhotdraw.standard.NullDrawingView#getManagedDrawingView(org.jhotdraw.framework.DrawingEditor)
	 */
	// JUnitDoclet end javadoc_method getManagedDrawingView()
	public void testGetManagedDrawingView() throws Exception {
		// JUnitDoclet begin method getManagedDrawingView
		// JUnitDoclet end method getManagedDrawingView
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
