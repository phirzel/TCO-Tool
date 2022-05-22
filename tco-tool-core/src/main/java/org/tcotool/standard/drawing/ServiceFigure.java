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
import org.jhotdraw.figures.RectangleFigure;
import org.jhotdraw.framework.FigureAttributeConstant;

/**
 * Graphical representation for a TCO-Service in a Dependency Diagram.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
class ServiceFigure extends NodeFigure /* implements ActionListener */ {

    /**
     * Create a new instance of ClassFigure with a RectangleFigure as presentation figure
     */
    public ServiceFigure(DependencyView diagram, Object element)
        throws Exception {
        super(diagram, new RectangleFigure(), element);
        defineComposite(0);

        setAttribute(FigureAttributeConstant.FILL_COLOR, Color.green);
    }
}