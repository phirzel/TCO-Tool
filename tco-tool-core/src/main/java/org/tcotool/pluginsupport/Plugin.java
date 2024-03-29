package org.tcotool.pluginsupport;

import ch.softenvironment.tcotool.tco.TCOPlugin;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcotool.application.LauncherView;

public interface Plugin {

    Logger log = LoggerFactory.getLogger(Plugin.class);

    String SELECTION_SPECIFIC = "selectionSpecific";
    String CLIENT_PROPERTY = "extension";

    void setUp();

    /**
     * Attach all Plugins extended for a specific Menu.
     *
     * @param menu
     */
    default void loadPlugins(String menu, boolean checkNavTree, boolean hasLeadingSeparator, boolean isGroupSpecific, MenuExtension extension) {
        // if (ext.getExtendedPointId().equals("MenuReportTco")) {
        JMenu entryMenu;
        Boolean selectionSpecific;
        if (checkNavTree) {
            entryMenu = LauncherView.getInstance().getPnlNavigation().findMenu("Mnu" + menu);
            selectionSpecific = Boolean.TRUE;
        } else {
            entryMenu = LauncherView.getInstance().findMenu("Mnu" + menu);
            selectionSpecific = Boolean.FALSE;
        }
        if (entryMenu == null) {
            log.warn("Developer warning: Menu not found in Launcher for: {}", menu);
            return;
        }

        if (hasLeadingSeparator) {
            entryMenu.add(new JSeparator());
        }

        try {
            //ExtensionPoint toolExtPoint = getManager().getRegistry().getExtensionPoint(getDescriptor().getId(), menu);
            //Iterator<MenuExtension> it = extensions.iterator(); // toolExtPoint.getConnectedExtensions().iterator();

            final JMenuItem item = createMenuItem(extension, isGroupSpecific);
            item.putClientProperty(SELECTION_SPECIFIC, selectionSpecific);
            entryMenu.add(item);

        } catch (Throwable e) {
            log.error("Could not load Plugin for menu <{}>", extension, e);
        }
    }

    /**
     * Create JMenuItem out of the extension-plugin contents.
     * <p>
     * See resource/plugin.xml
     *
     * @param ext
     * @return
     */
    default JMenuItem createMenuItem(MenuExtension ext, boolean isGroupSpecific) {
        final JMenuItem item = new JMenuItem();
        item.putClientProperty(CLIENT_PROPERTY, ext); // !!!
        item.setActionCommand(ext.getId());

        item.setIcon(new javax.swing.ImageIcon(TCOPlugin.getIconURL()));

        item.setText(ext.getText());
        item.setToolTipText(ext.getToolTipText()); // ResourceManager.getResource(NavigationView.class,
        // "CICostBlock")

        if (isGroupSpecific) {
            // @see NavigationTree#adaptSelection()
            item.putClientProperty(Menu.GROUP_SPECIFIC, Boolean.TRUE);
        }
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // call after extension-points are realized of extension
                activate(item, ext);
            }
        });

        return item;
    }

    /**
     * This will cause dependent plug-in activation, resp. triggers MenuItem.actionPerformed().
     *
     * @param component of TCO-Tool
     */
    private void activate(final JComponent component, MenuExtension ext) {
        Object node = null;
        if ((Boolean) component.getClientProperty(SELECTION_SPECIFIC)) {
            node = LauncherView.getInstance().getPnlNavigation().getSelectedNode();
        }

        actionPerform(component, node);
    }

    void actionPerform(final JComponent item, final Object object);
}
