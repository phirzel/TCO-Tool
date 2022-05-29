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

import javax.swing.JComponent;

/**
 * Application Interface any Plugin to extend TCO-Tool may implement.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@Deprecated(since = "own plugin")
public interface Menu {

    // @see plugins->org.tcotool.core.runtime/plugins.xml [ReportFinance.isGroupSpecific]
    String GROUP_SPECIFIC = "GROUP_SPECIFIC";

    /**
     * Any time #actionPerformed() on extended MenuItem is triggered by user this method is called via Plugin-Mechanism.
     *
     * @param item MenuItem triggered
     * @param object according to plugin.xml Manifest, the selected node in NavigationTree
     */
    @Deprecated(since = "own plugin")
    void actionPerform(final JComponent item, final Object object);
}
