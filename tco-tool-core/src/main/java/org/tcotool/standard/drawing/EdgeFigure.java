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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.view.ColorChooserDialog;
import ch.softenvironment.view.CommonUserAccess;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import lombok.extern.slf4j.Slf4j;
import org.jhotdraw.figures.AbstractLineDecoration;
import org.jhotdraw.figures.LineConnection;
import org.jhotdraw.figures.PolyLineFigure;
import org.jhotdraw.framework.Connector;
import org.jhotdraw.framework.FigureAttributeConstant;
import org.jhotdraw.framework.FigureChangeEvent;
import org.tcotool.application.LauncherView;
import org.tcotool.model.Dependency;
import org.tcotool.model.TcoObject;
import org.tcotool.presentation.WayPoint;

/**
 * Figure Specification for all Elements treated as edges in Diagram.
 *
 * @author Peter Hirzel
 * @see NodeFigure
 */

@Slf4j
abstract class EdgeFigure extends LineConnection {

	// keep reference to real model's presentation
	private DependencyView view = null;
	private Object modelElement = null;
	private final Vector<WayPoint> wayPoints = new Vector<WayPoint>();
	// edge#wayPoint-List

	// mouse-Event suppression
	protected NodeFigure startFigure = null;
	protected NodeFigure endFigure = null;

	/**
	 * EdgeFigure constructor comment. Used by any UML-Tool.
	 */
	private EdgeFigure() {
		super();
		setLineColor(Color.black); // determineForegroundColor(null));
		showDecoration();
	}

	/**
	 * EdgeFigure constructor comment.
	 */
	public EdgeFigure(DependencyView view, NodeFigure start, NodeFigure end, Dependency dependency) {
		this();
		this.view = view;
		this.startFigure = start;
		this.endFigure = end;
		this.modelElement = dependency;
	}

	/**
	 * Factory method to create a default pop-up menu.
	 *
	 * @return newly created pop-up menu
	 * @see NodeFigure
	 */
	protected JPopupMenu adaptPopupMenu(javax.swing.JPopupMenu popupMenu) {
		addFormatMenu(popupMenu);

		popupMenu.setLightWeightPopupEnabled(true);
		return popupMenu;
	}

	/**
	 * Add a Format sub-menu to a PopupMenu.
	 *
	 * @return newly created pop-up menu
	 * @see NodeFigure
	 */
	protected void addFormatMenu(javax.swing.JPopupMenu popupMenu) {
		JMenu formatMenu = new JMenu(CommonUserAccess.getMnuFormatText());
		/*
		 * JPopupMenu fontSizeMenu = new JPopupMenu(); fontSizeMenu.setName("Schriftgrösse"); formatMenu.add(fontSizeMenu);
		 */
		formatMenu.add(new AbstractAction(CommonUserAccess.getMniFormatLineColorText()) {
			@Override
			public void actionPerformed(ActionEvent event) {
				mniLineColor();
			}
		});

		popupMenu.add(formatMenu);
	}

	/**
	 * Connect Handles of the two Nodes connected to this Edge.
	 * <p>
	 * see #setEdge(..)
	 */
	protected final void connectNodes() {
		try {
			// start -> assume dummy value
			int x = 0;
			int y = 0;
			startPoint(x, y);
			Connector start = null;
			start = getDiagram().findNodeConnector(getStartElement(), x, y);
			if (start == null) {
				log.warn("Developer warning: AUTO-CORRECT: Missing StartNode->there must have been an improper deletion of nodes/edges before=>" + getSourceName(
					getStartElement()));//$NON-NLS-2$//$NON-NLS-1$
				//			shouldWarn(NlsUtils.formatMessage(resEdgeFigure.getString("CWMissingStartNode"), getSourceName(getStartElement()))); //$NON-NLS-1$
				// removeVisually();
			} else {
				wayPoints.add(createWayPoint(((DbObject) getStartElement()).getObjectServer(), startPoint()));
				Connector end = getDiagram().findNodeConnector(getEndElement(), x, y);
				if (end == null) {
					log.warn("Developer warning: AUTO-CORRECT: Missing EndNode->there must have been an improper deletion of nodes/edges before=>" + getSourceName(
						getEndElement()));//$NON-NLS-2$//$NON-NLS-1$
					//shouldWarn(NlsUtils.formatMessage(resEdgeFigure.getString("CWMissingEndNode"), getSourceName(getEndElement()))); //$NON-NLS-1$
					// removeVisually();
				} else {
					wayPoints.add(createWayPoint(((DbObject) getEndElement()).getObjectServer(), endPoint()));
					connectStart(start);
					setEndConnector(end);

					// set split-points
					for (int i = 0; i < wayPoints.size() - 1; i++) {
						WayPoint currentPoint = wayPoints.get(i);
						insertPointAt(new Point(currentPoint.getEast().intValue(), currentPoint.getSouth().intValue()), i + 1);
					}

					// create start and endPoint()
					updateConnection();
					endFigure().addFigureChangeListener(this);
				}
			}
		} catch (Exception e) {
			log.warn("Developer warning", e);
		}
	}

	/**
	 * Create a WayPoint.
	 *
	 * @param p Coordinates of WayPoint
	 */
	protected final static WayPoint createWayPoint(DbObjectServer server, Point p) throws Exception {
		WayPoint wayPoint = (WayPoint) server.createInstance(WayPoint.class);
		wayPoint.setEast(Long.valueOf(p.x));
		wayPoint.setSouth(Long.valueOf(p.y));

		return wayPoint;
	}

	@Override
	public Object getAttribute(FigureAttributeConstant constant) {
		if (constant.equals(FigureAttributeConstant.POPUP_MENU)) {
			return adaptPopupMenu(new JPopupMenu());
		}
		/*
		 * else if (name.equals(JHotDrawConstants.FONT_NAME)) { if ((getEdge() != null) && (getEdge().getFont() != null)) { return getEdge().getFont(); } }
		 */
		return super.getAttribute(constant);
	}

	/**
	 * Return the classDiagram where this Figure is shown.
	 */
	private DependencyView getDiagram() {
		return view;
	}

	/**
	 * Return the ending Element of the Relationship.
	 *
	 * @return Element
	 */
	protected Object getEndElement() {
		return endFigure.getModelElement();
	}

	/**
	 * Return the name of the Font.
	 */
	protected java.awt.Font getFont() {
		String font = (String) getAttribute(FigureAttributeConstant.FONT_NAME);
		if (font == null) {
			return null; // java.awt.Font.decode(LauncherView.getSettings().getFont());
		} else {
			return java.awt.Font.decode(font);
		}
	}

	/**
	 * Return the drawing color for the Frame.
	 *
	 * @see NodeFigure
	 */
	protected java.awt.Color getLineColor() {
		java.awt.Color color = (java.awt.Color) getAttribute(FigureAttributeConstant.FRAME_COLOR);
		if (color == null) {
			return Color.black; // LauncherView.getSettings().getForegroundColor();
		} else {
			return (java.awt.Color) getAttribute(FigureAttributeConstant.FRAME_COLOR);
		}
	}

	/**
	 * Return the ModelElement displayed by this Figure.
	 *
	 * @return ModelElement
	 */
	public Object getModelElement() {
		return modelElement;
	}

	private String getSourceName(Object element) {
		String edgeName = StringUtils.getPureClassName(this.getClass()) + ":";
		if (element == null) {
			return edgeName + "<???>";
		} else if (element instanceof TcoObject) {
			return edgeName + ((TcoObject) element).getName();
		} else {
			return edgeName + element.toString();
		}
	}

	/**
	 * Return the starting Element of the Relationship.
	 *
	 * @return Element
	 */
	protected Object getStartElement() {
		return startFigure.getModelElement();
	}

	/**
	 * Return index of WayPoint if there is one close to the given arguments.
	 *
	 * @see PolyLineFigure#joinSegments(int, int)  for Algorithm.
	 */
	protected int getWayPointIndex(int x, int y) {
		for (int i = 1; i < (fPoints.size() - 1); i++) {
			Point p = pointAt(i);
			if (org.jhotdraw.util.Geom.length(x, y, p.x, p.y) < 4 /*
			 * LauncherView. getSettings (). getConnectorZone ( ).intValue( )
			 */) {
				// removePointAt(i);
				return i;
			}
		}
		return -1;
	}

	/**
	 * LineColor Action.
	 *
	 * see #addEditMenu(..)
	 */
	private void mniLineColor() {
		ColorChooserDialog dialog = new ColorChooserDialog(LauncherView.getInstance(), true);
		if (dialog.isSaved()) {
			setLineColor(dialog.getChosenColor());
		}
	}

	/**
	 * Extend Attribute-map with pop-up menu and make sure LineDecorations change Colors as well.
	 */
	@Override
	public void setAttribute(FigureAttributeConstant constant, Object value) {
		if (constant.equals(FigureAttributeConstant.FRAME_COLOR)) {
			// don't forget to change color of Decoration
			if (getStartDecoration() != null) {
				((AbstractLineDecoration) getStartDecoration()).setBorderColor((java.awt.Color) value);
				changed();
			}
			if (getEndDecoration() != null) {
				((AbstractLineDecoration) getEndDecoration()).setBorderColor((java.awt.Color) value);
				changed();
			}
		}
		super.setAttribute(constant, value);
	}

	/**
	 * Set the Font.
	 *
	 * @param font (like "family-style-size")
	 * @see EdgeFigure
	 * @see #setAttribute(FigureAttributeConstant, Object)
	 */
	protected void setFont(String font) {
		setAttribute(FigureAttributeConstant.FONT_NAME, font);
	}

	/**
	 * Set the Line/Frame Color.
	 *
	 * @see NodeFigure
	 * @see #setAttribute(FigureAttributeConstant, Object)
	 */
	protected void setLineColor(java.awt.Color color) {
		setAttribute(FigureAttributeConstant.FRAME_COLOR, color);
		/*
		 * if ((getEdge() != null) && (!ColorConverter.isSame(getEdge().getForeground(), color))) { // prevent ping-pong by MetaModelChange
		 * getEdge().setForeground(ColorConverter.createColor(color)); }
		 */
	}

	/**
	 * Decorate the RelationShip-Ends. Overwrite this method for specific behaviour.
	 */
	protected void showDecoration() {
		setStartDecoration(null);
		setEndDecoration(null);
	}

	/**
	 * Make the changes visible immediately.
	 */
	protected void updateImmediately() {
		/*
		 * comment by Wolfram changing a figure and drawing the changes (-> updating) are two separate methods. This provides more flexibility if you have a
		 * sequence of changes but want to redraw only once. Thus, changed() marks the figure as dirty (e.g calling figureInvalidated()) whereas
		 * figureRequestUpdate() performs the redraw afterwards (unfortunately the programmer must take care of this).
		 *
		 * I admit that in most circumstances figureChanged() and figureRequestUpdate() should probably do the same. The concept is inconsistent in so far that
		 * one time you call a method (changed) on a figure to do something and the other time you notify a listener (figureRequestUpdate). Perhaps we need a
		 * method (Figure.update() ?) that makes this more consistent.
		 */
		willChange();
		if (listener() != null) {
			listener().figureRequestUpdate(new FigureChangeEvent(this));
		}
		changed();
	}

	/**
	 * ModelElement changed from outside. Therefore a refresh of the View is needed.
	 *
	 * see #setClassDiagram(ClassDiagramView)
	 */
	public void updateView() {
		// don't remove while establishing connection
		if (getDiagram() != null) {
			if ((getStartElement() == null) || (getEndElement() == null) || (getDiagram().findFigure(getStartElement()) == null)
				|| (getDiagram().findFigure(getEndElement()) == null)) {
				// both presentationNodes are to be found in Diagram
				// removeVisually();
			}
		}
	}
}
