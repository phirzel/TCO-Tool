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

import ch.softenvironment.util.Tracer;
import java.awt.Graphics;
import org.jhotdraw.figures.ArrowTip;
import org.jhotdraw.framework.Figure;
import org.tcotool.model.Dependency;

/**
 * Draw a dependency line between two Figures representing dependable TcoObject's. A dependency relation is a uses-a relation with a direction where the connection points to the class used by another
 * one. The start class (Client) depends on the end class (Supplier). A DependencyLineConnection has an arrow at the end point and is dotted (analog UML).
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
class DependencyLineConnection extends EdgeFigure {

	/**
	 * Used to display a given Model-Dependency in given ClassDiagram.
	 */
	public DependencyLineConnection(DependencyView classDiagram, Figure start, Figure end, Dependency dependency) {
		super(classDiagram, (NodeFigure) start, (NodeFigure) end, dependency);
		// setEdge(new ch.ehi.umleditor.umlpresentation.Dependency(), start,
		// end);
		// addModelElement(dependency);
	}

	/**
	 * Tests whether the two figures may be connected.
	 *
	 * @param start figure representing the start/client class which is dependent on the end class
	 * @param end figure representing the end/supplier class
	 */
	@Override
	public boolean canConnect(Figure start, Figure end) {
		// TcoObject client = null;
		// TcoObject supplier = null;

		try {
			// only GeneralizableElement's are valid types
			/* client = (TcoObject) */
			((NodeFigure) start).getModelElement();
			/* supplier = (TcoObject) */
			((NodeFigure) end).getModelElement();
		} catch (ClassCastException e) {
			Tracer.getInstance().developerWarning("Wrong dependency");
			return false;
		}

		return true;
	}

	/**
	 * Draw the line which is a dotted line for a dependency connection. Instead of drawing one line from start point to end point, the line is divided into several small lines each 5 pixels long and
	 * 5 pixels away from the previous line. Some minor inaccuracy are possible due to rounding errors or incomplete last lines.
	 *
	 * @param g graphics context into which the line is drawn
	 * @param x1 start x point
	 * @param y1 start y point
	 * @param x2 end x point
	 * @param y2 end y point
	 */
	@Override
	protected void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
		int xDistance = x2 - x1;
		int yDistance = y2 - y1;
		double direction = Math.PI / 2 - Math.atan2(xDistance, yDistance);

		double xAngle = Math.cos(direction);
		double yAngle = Math.sin(direction);
		int lineLength = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);

		for (int i = 0; i + 5 < lineLength; i = i + 10) {
			int p1x = x1 + (int) (i * xAngle);
			int p1y = y1 + (int) (i * yAngle);
			int p2x = x1 + (int) ((i + 5) * xAngle);
			int p2y = y1 + (int) ((i + 5) * yAngle);
			g.drawLine(p1x, p1y, p2x, p2y);
		}

		// write dependency-percentage
		/*
		 * int offsetX = -50; if (x1 > x2) { offsetX = 10; } int offsetY = -10;
		 * if (y1 > y2) { offsetY = 20; }
		 * g.drawString(((Dependency)getModelElement()).getDistribution() + "%",
		 * x2+offsetX, y2+offsetY);
		 */
		int xp = -1;
		int yp = -1;
		if (x1 > x2) {
			xp = x2 + (x1 - x2) / 2;
		} else {
			xp = x1 + (x2 - x1) / 2;
		}
		if (y1 > y2) {
			yp = y2 + (y1 - y2) / 2;
		} else {
			yp = y1 + (y2 - y1) / 2;
		}
		g.drawString(((Dependency) getModelElement()).getDistribution() + "%", xp + 8, yp - 8);
	}

	/**
	 * Decorate the RelationShip-Ends.
	 */
	@Override
	protected void showDecoration() {
		setStartDecoration(null);
		ArrowTip arrow = new ArrowTip(0.4, 12.0, 0.0);
		arrow.setBorderColor(getLineColor());
		setEndDecoration(arrow);
	}
}
