package org.tcotool.pluginsupport;

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

import org.tcotool.application.LauncherView;

/**
 * The main Application/Core-Plugin to define an extension-point for other Plugins, extending TCO-Tool. Plugin-in ID = "org.tcotool.core.runtime"
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class ApplicationPlugin {

    private static final String CLIENT_PROPERTY = "extension";
    private static final String TOOL_INSTANCE = "toolInstance";
    private static final String SELECTION_SPECIFIC = "selectionSpecific";

    private ApplicationPlugin() {
        throw new IllegalStateException("utility class");
    }

    /**
     * Application entry point. This method should be called once during application start up by convention.
     */
    public static void invokePlugins(Plugin plugin) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                plugin.setUp();
            }
        });
    }

    public static void showBusy(Runnable block) {
        LauncherView.getInstance().runBlock(block);
    }
}