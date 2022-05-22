package org.tcotool.standard.drawing;

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

import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.view.BaseDialog;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jhotdraw.contrib.CustomSelectionTool;
import org.jhotdraw.framework.ConnectionFigure;
import org.jhotdraw.framework.Connector;
import org.jhotdraw.framework.DrawingEditor;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.FigureEnumeration;
import org.jhotdraw.framework.Tool;
import org.jhotdraw.framework.ViewChangeListener;
import org.jhotdraw.standard.StandardDrawing;
import org.jhotdraw.util.UndoManager;
import org.tcotool.application.LauncherView;
import org.tcotool.application.NavigationView;
import org.tcotool.model.Dependency;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.presentation.Diagram;
import org.tcotool.presentation.PresentationElement;
import org.tcotool.presentation.PresentationNode;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Drawing View for Dependency-Diagram's (related to UML-Classdiagram's with Package-Dependencies).
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see CustomSelectionTool#handleMousePopupMenu(..)
 */
@SuppressWarnings("serial")
public class DependencyView extends org.jhotdraw.contrib.zoom.ZoomDrawingView implements DrawingEditor, PropertyChangeListener {

	// private ToolButton defaultToolButton = null;
	// private ToolButton selectedToolButton = null;
	private Tool tool = null;
	private final UndoManager undoManager = new UndoManager();
	private Calculator calculator = null;
	private Diagram diagram = null;
	private ModelUtility utility = null;

	// Menu Checkboxes
	private final boolean showDependencyDistribution = true;
	private int year = 0;

	// private boolean showCosts = true;

	/**
	 * ClassDiagramView constructor comment.
	 *
	 * @param diagram CH.ifa.draw.framework.DrawingEditor
	 * @param width
	 * @param height
	 * @diagram PresentationElement containing all visualization data (location, color,..)
	 * @dependencies list of modeled Dependency-elements
	 * @overallOnly relevant for Calculator setup!!!
	 */
	public DependencyView(ModelUtility utility, Diagram diagram, Set<Dependency> dependencies, Boolean overallOnly, int width, int height) throws Exception {
		// @see LauncherView.addInternalFrame(..) for setting the Editor
		super(null, width, height);
		this.utility = utility;
		setDrawing(new StandardDrawing());
		this.diagram = diagram;
		// setBackground(ch.ehi.umleditor.application.LauncherView.getSettings().getBackgroundColor());

		if (overallOnly) {
			year = 0;
			calculator = new CalculatorTco(utility);
		} else {
			year = 1;
			calculator = new CalculatorTco(utility, (TcoPackage) utility.getRoot(), utility.getUsageDuration());
		}

		Iterator<Dependency> iterator = dependencies.iterator();
		while (iterator.hasNext()) {
			addDependency(iterator.next());
		}

		refresh();
	}

	/**
	 * Adds a new figure to the drawing.
	 *
	 * @return the added figure.
	 * @see EdgeFigure#handleConnection(Figure, Figure)
	 */
	@Override
	public Figure add(Figure figure) {
		try {
			super.add(figure);
			if (figure instanceof NodeFigure) {
				int index = diagram.getPresentationElement().indexOf(((NodeFigure) figure).getNode());
				if (index < 0) {
					// not layout yet
					List<PresentationElement> elements = new ArrayList<PresentationElement>(diagram.getPresentationElement());
					elements.add(((NodeFigure) figure).getNode());
					diagram.setPresentationElement(elements);
				} else {
					// reuse layout from previously showing diagram
					PresentationNode node = (PresentationNode) diagram.getPresentationElement().get(index);
					((NodeFigure) figure).setEast(node.getEast() == null ? 0 : node.getEast().intValue());
					((NodeFigure) figure).setSouth(node.getSouth() == null ? 0 : node.getSouth().intValue());
					if (node.getBackground() != null) {
						((NodeFigure) figure).setFillColor(new Color(node.getBackground().intValue()));
					}
				}
				((NodeFigure) figure).updateView();
			}
		} catch (Exception e) {
			// TODO nasty ref to LauncherView
			BaseDialog.showError(LauncherView.getInstance(), "error", "CEFigureNotCreated", e); //$NON-NLS-1$
		}

		return figure;
	}

	/**
	 * Adds a given (Model)-Element to Diagram.
	 *
	 * @return the added figure.
	 * @see NavigationView#mniAddToDiagram()
	 */
	private Figure add(TcoObject element) throws Exception {
		Figure figure = findFigure(element);
		if (figure != null) {
			return figure;
		}

		// otherwise create the PresentationElement
		if (element instanceof Service) {
			return add(new ServiceFigure(this, element));
		} else if (element instanceof TcoPackage) {
			return add(new PackageFigure(this, element));
		} else {
			throw new DeveloperException("wrong type (TcoPackage or Service expected)", element.toString());
		}
	}

	private void addDependency(Dependency dependency) throws Exception {
		Figure client = add(utility.findClient(dependency));
		Figure supplier = add(utility.findSupplier(dependency));

		loadSimpleEdge(new DependencyLineConnection(this, client, supplier, dependency));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#addViewChangeListener(CH.ifa.draw .framework.ViewChangeListener)
	 */
	@Override
	public void addViewChangeListener(ViewChangeListener vsl) {
		// nothing to do
	}

	@Override
	@Deprecated
	public DrawingEditor editor() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#figureSelectionChanged(CH.ifa.draw .framework.DrawingView)
	 */
	@Override
	public void figureSelectionChanged(DrawingView view) {
	}

	/**
	 * Find the ServiceFigure in this DrawingView at the given coordinates.
	 *
	 * @return ServiceFigure
	 */
	@Deprecated
	protected ServiceFigure findServiceFigure(int x, int y) {
		FigureEnumeration enumeration = drawing().figures();
		while (enumeration.hasNextFigure()) {
			Figure figure = enumeration.nextFigure();
			if ((figure instanceof ServiceFigure) && (figure.containsPoint(x, y))) {
				return (ServiceFigure) figure;
			}
		}
		return null;
	}

	/**
	 * Find a possible NodeFigure as target for given connection at given coordinates.
	 */
	protected Figure findConnectableFigure(int x, int y, ConnectionFigure connection) {
		// copied code from JHotDraw's ConnectionTool#findConnectableFigure(..)
		FigureEnumeration k = drawing().figuresReverse();
		while (k.hasNextFigure()) {
			Figure figure = k.nextFigure();
			if (!figure.includes(connection) && figure.canConnect() && figure.containsPoint(x, y)) {
				return figure;
			}
		}
		return null;
	}

	/**
	 * Finds an existing ConnectionFigure (say Edge) at given Coordinates.
	 *
	 * @see ConnectionTool#findConnection(..) => copied Code
	 */
	protected ConnectionFigure findConnection(int x, int y) {
		// copied Method from JHotDraw's ConnectionTool#findConnection(..)
		FigureEnumeration k = drawing().figuresReverse();
		while (k.hasNextFigure()) {
			Figure figure = k.nextFigure();
			figure = figure.findFigureInside(x, y);
			if (figure != null && (figure instanceof ConnectionFigure)) {
				return (ConnectionFigure) figure;
			}
		}
		return null;
	}

	/**
	 * Find the Figure in this DrawingView containing the given Element. Remark: - RoleDefFigure and AssociationAttributeFigure cannot be found by this method (because they are artificial)
	 *
	 * @return Connector (of the found Figures)
	 */
	protected Figure findFigure(Object element) {
		// TUNE THIS METHOD!
		if (element != null) {
			FigureEnumeration enumeration = drawing().figures();
			while (enumeration.hasNextFigure()) {
				Figure figure = enumeration.nextFigure();
				/*
				 * if ((figure instanceof RoleDefFigure) || (figure instanceof AssociationAttributeFigure)) { // this are artificial figures and are to be
				 * maintained // by its Responsibles, such as: // - PresentantionRoleFigure->RoleDefFigure // - LinkFigure->AssociationAttributeFigure continue;
				 * }
				 */

				// try by ModelElement
				if ((figure instanceof ServiceFigure) || (figure instanceof PackageFigure)) {
					if (element.equals(((NodeFigure) figure).getModelElement())) {
						return figure;
					}
				} else if ((figure instanceof EdgeFigure) && element.equals(((EdgeFigure) figure).getModelElement())) {
					return figure;
				} // else Tracer.getInstance().nyi(this, "findFigure(Element)",
				// "for ModelElement <" + element.toString() + ">");
			}
		}

		return null;
	}

	/**
	 * Find the Figure in this DrawingView containing the given Element.
	 *
	 * @return Connector (of the found Figures)
	 */
	protected Connector findNodeConnector(Object element, int x, int y) {
		Figure figure = findFigure(element);
		if (figure == null) {
			return null;
		} else {
			return figure.connectorAt(x, y);
		}
	}

	public Calculator getCalculator() {
		return calculator;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#getUndoManager()
	 */
	@Override
	public UndoManager getUndoManager() {
		return undoManager;
	}

	protected boolean isShowDependencyDistribution() {
		return showDependencyDistribution;
	}

	/**
	 * Handles key down events. Cursor keys are handled by the view the other key events are delegated to the currently active tool.
	 */
	public void keyPressed(KeyEvent e) {
		/*
		 * int code = e.getKeyCode();
		 *
		 * addToSelection(figure); Command cmd = new DeleteCommand("Delete", this); cmd.execute();
		 */
		/*
		 * if ((code == KeyEvent.VK_BACK_SPACE) || (code == KeyEvent.VK_DELETE)) { FigureEnumeration figures = selection(); while (figures.hasNextFigure()) { //
		 * 1) remove Edges Figure figure = (Figure)figures.nextFigure(); if (figure instanceof EdgeFigure) { ((EdgeFigure)figure).removeVisually(); } } figures
		 * = selection(); while (figures.hasNextFigure()) { // 1) remove Nodes Figure figure = (Figure)figures.nextFigure(); if (figure instanceof NodeFigure) {
		 * ((NodeFigure)figure).removeVisually(); } } } else { // super.keyPressed(e); }
		 */
		checkDamage();
	}

	/**
	 * Show a simple Edge by means plain EdgeFigure as Dependency or Generalization.
	 */
	protected void loadSimpleEdge(EdgeFigure figure) {
		figure.connectNodes();

		// show Figure
		super.add(figure);
		figure.updateView();
	}

	/**
	 * Refresh Drawings.
	 */
	public void refresh() {
		// 1) clear all
		/*
		 * FigureEnumeration enumerator = drawing().figures(); while (enumerator.hasNextFigure()) { remove(enumerator.nextFigure()); }
		 *
		 * // 2) redraw while (enumerator.hasNextFigure()) { add(enumerator.nextFigure()); }
		 */
		repairDamage();
		repaint();
	}

	/**
	 * Removes a figure from the drawing. No influence on Model.
	 *
	 * @return the removed figure
	 */
	@Override
	public Figure remove(Figure figure) {
		// seems to be tricky in JHotDraw, to get what is wanted
		/* Figure fig = */
		super.remove(figure);
		clearSelection();
		repaint();
		repairDamage();
		// removeFromSelection(figure);
		/*
		 * if ((figure != null) && (editor().view() != null)) { // remove Node from Drawing clearSelection(); addToSelection(figure); Command deleteCommand =
		 * new UndoableCommand(new DeleteCommand(JHotDrawConstants.DELECTE_COMMAND, editor())); deleteCommand.execute(); }
		 */
		return figure;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#removeViewChangeListener(CH.ifa.draw .framework.ViewChangeListener)
	 */
	@Override
	public void removeViewChangeListener(ViewChangeListener vsl) {
		// nothing to do
	}

	/**
	 * Show/hide all Attributes names in Diagram. Settings are stored on each class separately.
	 */
	protected void showAllCosts(Integer year /* boolean visible */) {
		this.year = year;
		// showCosts = visible;

		FigureEnumeration enumeration = drawing().figures();
		while (enumeration.hasNextFigure()) {
			Figure figure = enumeration.nextFigure();
			if (figure instanceof NodeFigure) {
				((NodeFigure) figure).updateView();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#showStatus(java.lang.String)
	 */
	@Override
	public void showStatus(String string) {
		// nothing to do
	}

	/**
	 * Gets the editor's current tool.
	 *
	 * @see DrawingEditor
	 */
	@Override
	public Tool tool() {
		if (tool == null) {
			tool = new CustomSelectionTool(this);
			tool.setUsable(true);
		}
		return tool;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#toolDone()
	 */
	@Override
	public void toolDone() {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#view()
	 */
	@Override
	public DrawingView view() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CH.ifa.draw.framework.DrawingEditor#views()
	 */
	@Override
	public DrawingView[] views() {
		return null;
	}

	protected Diagram getDiagram() {
		return diagram;
	}

	/**
	 * Return the year of interest for current Node-costs.
	 *
	 * @return
	 */
	protected int getYear() {
		return year;
	}

	/**
	 * Catch Toolbar's change current object-event.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("currentObject")) {
			System.out.println(evt.getPropertyName());
			showAllCosts((Integer) evt.getNewValue());
		}
	}
}
