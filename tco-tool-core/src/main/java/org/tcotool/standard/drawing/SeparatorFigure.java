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

import java.awt.Graphics;
import org.jhotdraw.figures.LineFigure;

/**
 * A SeparatorFigure is similar to a LineFigure but draws only a horizontal line and separates from other figures beneath in addition.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
class SeparatorFigure extends LineFigure {

    private boolean lineVisible = true;

    /**
     * Create a new SeparatorFigure
     */
    public SeparatorFigure() {
        super();
    }

    /**
     * Draw the separation line and to hold some space free
     */
    @Override
    public void draw(Graphics g) {
        if (lineVisible) {
            g.setColor(getFrameColor());
            g.drawLine(startPoint().x, startPoint().y, endPoint().x - 1, startPoint().y);
        }
    }

    /**
     * Set whether line shall be drawn visible or invisible.
     */
    public void setLineVisible(boolean visible) {
        lineVisible = visible;
    }
}
