package org.tcotool.application;

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

import java.awt.*;

/**
 * Keep Settings in a <user.home>/.tcoTool file.
 *
 * @author Peter Hirzel
 */
public class ApplicationOptions extends ch.softenvironment.client.ApplicationOptions {

    private static final long serialVersionUID = 1L; //TODO HIP not necessary

    // chart-colors
    private static final String CHART_BACKGROUND = "CHART_BG";
    private static final String CHART_DOMAINGRIDLINE = "CHART_DOMAIN_GRIDLINE";
    private static final String CHART_RANGEGRIDLINE = "CHART_RANGE_GRIDLINE";

    /**
     * Create Default settings.
     */
    protected ApplicationOptions() {
        super();
        setChartBackground(Color.lightGray);
        setChartDomainGridLine(Color.white);
        setChartRangeGridLine(Color.white);
    }

    protected ApplicationOptions(final String filename) {
        super(filename, new ApplicationOptions());
    }

    public java.awt.Color getChartBackground() {
        return new java.awt.Color(Integer.parseInt(getProperty(CHART_BACKGROUND)));
    }

    public void setChartBackground(java.awt.Color color) {
        setProperty(CHART_BACKGROUND, (Integer.valueOf(color.getRGB())).toString());
    }

    public java.awt.Color getChartDomainGridLine() {
        return new java.awt.Color(Integer.parseInt(getProperty(CHART_DOMAINGRIDLINE)));
    }

    public void setChartDomainGridLine(java.awt.Color color) {
        setProperty(CHART_DOMAINGRIDLINE, (Integer.valueOf(color.getRGB())).toString());
    }

    public java.awt.Color getChartRangeGridLine() {
        return new java.awt.Color(Integer.parseInt(getProperty(CHART_RANGEGRIDLINE)));
    }

    public void setChartRangeGridLine(java.awt.Color color) {
        setProperty(CHART_RANGEGRIDLINE, (Integer.valueOf(color.getRGB())).toString());
    }

    /**
     * Display mode.
     *
     * @return
     * @deprecated
     */
    public boolean isModeAdvanced() {
        return false;
    }
}
