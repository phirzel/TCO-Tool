package ch.softenvironment.client;

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

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Font;
import org.junit.Before;
import org.junit.Test;

/**
 * TestCase for ApplicationOptions.
 *
 * @author Peter Hirzel <i>soft</i>Environment GmbH
 * @version $Revision: 1.2 $ $Date: 2006-05-07 14:53:51 $
 */
public class ApplicationOptionsTest {

    private final String fileName = "ApplicationOptions.test";
    private ApplicationOptions settings = null;

    @Before
    public void setUp() {
        //        super.setUp();
        settings = new ApplicationOptions(fileName);
    }

    @Test
    public void setFont() {
        Font font = new java.awt.Font("Arial", java.awt.Font.BOLD, 12);
        settings.setFont(font);
        Font fontGet = settings.getFont();
        assertTrue("Font-Family", font.getFamily().equals(fontGet.getFamily()));
        assertTrue("Font-Style", font.getStyle() == fontGet.getStyle());
        assertTrue("Font-Size", font.getSize() == fontGet.getSize());
    }

    @Test
    public void setColor() {
        Color color = Color.MAGENTA;
        settings.setBackgroundColor(color);
        Color colorGet = settings.getBackgroundColor();
        assertTrue("BackgroundColor", color.equals(colorGet));

        color = new Color(10, 99, 87);
        settings.setForegroundColor(color);
        colorGet = settings.getForegroundColor();
        assertTrue("ForegroundColor", color.equals(colorGet));
    }
}
