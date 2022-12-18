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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import org.jhotdraw.contrib.DiamondFigure;
import org.jhotdraw.figures.RectangleFigure;
import org.jhotdraw.framework.FigureAttributeConstant;

/**
 * A PackageFigure is a graphical representation for TcoPackage in Dependency-Diagram.
 *
 * @author Peter Hirzel
 */

class PackageFigure extends NodeFigure {

	/**
	 * Create a new instance of PackageFigure with a RectangleFigure as presentation figure.
	 */
	public PackageFigure(DependencyView diagram, Object element) throws Exception {
		// this(new RectangleFigure(new Point(0,0), new Point(200, 150)));
		super(diagram, new RectangleFigure(), element);
		defineComposite(15);

		setAttribute(FigureAttributeConstant.FILL_COLOR, Color.yellow);
	}

	/**
	 * Represent this Figure with an UML-Package Symbol.
	 */
	@Override
	public void draw(Graphics g) {
		// show a package
		g.setColor((Color) getAttribute(FigureAttributeConstant.FILL_COLOR));
		g.fillPolygon(getPolygon());
		g.setColor((Color) getAttribute(FigureAttributeConstant.FRAME_COLOR));
		g.drawPolygon(getPolygon());

		nameFigure.draw(g);
		factCostFigure.draw(g);
		personalCostFigure.draw(g);
		dependencyCostFigure.draw(g);
		totalCostFigure.draw(g);
	}

	/**
	 * Return default handles on all four edges for this figure.
	 *
	 * @see DiamondFigure #getPolygon()
	 */
	private java.awt.Polygon getPolygon() {
		Rectangle r = displayBox();
		Polygon p = new Polygon();

		// rose relations 13:32=x:width
		int widthUpperRectangle = 13 * r.width / 32;
		// rose relations 6:26=y:height
		int heightUpperRectangle = 4 * r.height / 26;

		// offset of current position
		int dx = r.x;
		int dy = r.y;

		// draw middle line
		p.addPoint(0 + dx, heightUpperRectangle + dy);
		p.addPoint(r.width + dx - 1, heightUpperRectangle + dy);
		// draw right Edge of taller Rectangle
		p.addPoint(r.width + dx - 1, r.height + dy - 1);
		// draw bottom edge
		p.addPoint(0 + dx, r.height + dy - 1);
		// draw left edge
		p.addPoint(0 + dx, 0 + dy);
		// draw short upper edge
		p.addPoint(widthUpperRectangle + dx, 0 + dy);
		// draw right edge of upper rectangle
		p.addPoint(widthUpperRectangle + dx, heightUpperRectangle + dy);

		return p;
	}
}
