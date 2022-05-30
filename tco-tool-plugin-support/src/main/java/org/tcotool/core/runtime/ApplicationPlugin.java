//package org.tcotool.core.runtime;
//
///*
// * This library is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License as published by the Free Software Foundation; either
// * version 2.1 of the License, or (at your option) any later version.
// *
// * This library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public
// * License along with this library; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// */
//
//import ch.softenvironment.client.ResourceManager;
//import ch.softenvironment.util.StringUtils;
//import ch.softenvironment.util.Tracer;
//import java.io.File;
//import java.net.URL;
//import java.util.Iterator;
//import javax.swing.JComponent;
//import javax.swing.JMenu;
//import javax.swing.JMenuItem;
//import javax.swing.JSeparator;
//import org.java.plugin.Plugin;
//import org.java.plugin.registry.Extension;
//import org.java.plugin.registry.Extension.Parameter;
//import org.java.plugin.registry.ExtensionPoint;
//import org.java.plugin.registry.PluginDescriptor;
//import org.tcotool.application.LauncherView;
//import org.tcotool.pluginsupport.Menu;
//
///**
// * The main Application/Core-Plugin to define an extension-point for other Plugins, extending TCO-Tool. Plugin-in ID = "org.tcotool.core.runtime"
// *
// * @author Peter Hirzel, softEnvironment GmbH
// * @see org.tcotool.pluginsupport.Menu
// */
//@Deprecated(since = "new plugin")
//public final class ApplicationPlugin extends Plugin {
//
//    private static final String CLIENT_PROPERTY = "extension";
//    private static final String TOOL_INSTANCE = "toolInstance";
//    private static final String SELECTION_SPECIFIC = "selectionSpecific";
//
//    private File dataFolder = null;
//
//    public ApplicationPlugin() {
//        super();
//    }
//
//    /**
//     * Returns folder where given plug-in can store it's data.
//     *
//     * @param descr plug-in descriptor
//     * @return plug-in data folder
//     * @deprecated
//     */
//    @Deprecated
//    public File getDataFolder(final PluginDescriptor descr) {
//        File result = new File(dataFolder, descr.getId());
//        if (!result.isDirectory() && !result.mkdirs()) {
//            // throw new IOException("can't create data folder " + result + "
//            // for plug-in " + descr.getId());
//        }
//        return result;
//    }
//
//    /**
//     * @see org.java.plugin.Plugin#doStart()
//     */
//    @Override
//    protected void doStart() throws Exception {
//        // no-op
//    }
//
//    /**
//     * @see org.java.plugin.Plugin#doStop()
//     */
//    @Override
//    protected void doStop() throws Exception {
//        // no-op
//    }
//
//    /**
//     * Application entry point. This method should be called once during application start up by convention.
//     *
//     * @param theDataFolder plug-ins data folder
//     * @see org.tcotool.pluginsupport.PluginUtility#invokePlugins(String)
//     */
//    public void run(final File theDataFolder) {
//        // started as "Plugin-Collection"
//        this.dataFolder = theDataFolder;
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                // @see plugins/org.tcotool.core.runtime/plugin.xml
//                // [extension-point id="*"]
//                loadPlugins("ReportTco", false);
//                loadPlugins("ReportTco", true);
//                loadPlugins("ReportFinance", false);
//                loadPlugins("ReportFinance", true);
//                loadPlugins("Extras", false);
//            }
//        });
//    }
//
//    /**
//     * Attach all Plugins extended for a specific Menu.
//     *
//     * @param menu
//     */
//    private void loadPlugins(String menu, boolean checkNavTree) {
//        // if (ext.getExtendedPointId().equals("MenuReportTco")) {
//        JMenu entryMenu = null;
//        Boolean selectionSpecific = null;
//        if (checkNavTree) {
//            entryMenu = LauncherView.getInstance().getPnlNavigation().findMenu("Mnu" + menu);
//            selectionSpecific = Boolean.TRUE;
//        } else {
//            entryMenu = LauncherView.getInstance().findMenu("Mnu" + menu);
//            selectionSpecific = Boolean.FALSE;
//        }
//        if (entryMenu == null) {
//            Tracer.getInstance().developerWarning("Menu not found in Launcher for: " + menu);
//            return;
//        }
//
//        Extension ext = null;
//        try {
//            ExtensionPoint toolExtPoint = getManager().getRegistry().getExtensionPoint(getDescriptor().getId(), menu);
//            Iterator<Extension> it = toolExtPoint.getConnectedExtensions().iterator();
//            while (it.hasNext()) {
//                ext = it.next();
//                Parameter isLeading = ext.getParameter("hasLeadingSeparator");
//                if (isLeading.valueAsBoolean().booleanValue()) {
//                    entryMenu.add(new JSeparator());
//                }
//                final JMenuItem item = createMenuItem(ext);
//                item.putClientProperty(SELECTION_SPECIFIC, selectionSpecific);
//                entryMenu.add(item);
//            }
//        } catch (Throwable e) {
//            Tracer.getInstance().runtimeError("Could not load Plugin for menu <" + ext.getId() + ">", e);
//        }
//    }
//
//    /**
//     * This will cause dependent plug-in activation, resp. triggers MenuItem.actionPerformed().
//     *
//     * @param component of TCO-Tool
//     */
//    private void activate(final JComponent component, final ClassLoader loader) {
//        Extension ext = (Extension) component.getClientProperty(CLIENT_PROPERTY);
//
//        Menu menu = (Menu) component.getClientProperty(TOOL_INSTANCE);
//        if (menu == null) {
//            try {
//                // This will cause other plug-in activation!!!
//                Plugin toolPlugin = getManager().getPlugin(ext.getDeclaringPluginDescriptor().getId());
//                Class<? extends Plugin> pluginCls = toolPlugin.getClass();
//                Class<?> toolCls = pluginCls.getClassLoader().loadClass(ext.getParameter("class").valueAsString());
//                if (pluginCls.isAssignableFrom(toolCls)) {
//                    menu = (Menu) toolPlugin;
//                } else {
//                    menu = (Menu) toolCls.newInstance();
//                }
//            } catch (Throwable e) {
//                Tracer.getInstance().runtimeError("Plugin not started", e);
//                return;
//            }
//            component.putClientProperty(TOOL_INSTANCE, menu);
//        }
//        Object node = null;
//        if (((Boolean) component.getClientProperty(SELECTION_SPECIFIC)).booleanValue()) {
//            node = LauncherView.getInstance().getPnlNavigation().getSelectedNode();
//        }
//        menu.actionPerform(component, node);
//    }
//
//    /**
//     * Create JMenuItem out of the extension-plugin contents.
//     * <p>
//     * See resource/plugin.xml
//     *
//     * @param ext
//     * @return
//     */
//    private JMenuItem createMenuItem(Extension ext) {
//        final JMenuItem item = new JMenuItem();
//        item.putClientProperty(CLIENT_PROPERTY, ext); // !!!
//        item.setActionCommand(ext.getId());
//
//        final ClassLoader loader = getManager().getPluginClassLoader(ext.getDeclaringPluginDescriptor());
//        Parameter iconParam = ext.getParameter("icon");
//        URL iconUrl = null;
//        if (iconParam != null) {
//            iconUrl = loader.getResource(iconParam.valueAsString());
//            item.setIcon(new javax.swing.ImageIcon(iconUrl));
//        }
//
//        item.setText(getResourceString(ext, "textKey", loader));
//        item.setToolTipText(getResourceString(ext, "descriptionKey", loader)); // ResourceManager.getResource(NavigationView.class,
//        // "CICostBlock")
//        Parameter isGroupSpecific = ext.getParameter("isGroupSpecific");
//        if ((isGroupSpecific != null) && isGroupSpecific.valueAsBoolean().booleanValue()) {
//            // @see NavigationTree#adaptSelection()
//            item.putClientProperty(Menu.GROUP_SPECIFIC, Boolean.TRUE);
//        }
//        item.addActionListener(new java.awt.event.ActionListener() {
//            @Override
//            public void actionPerformed(java.awt.event.ActionEvent e) {
//                // call after extension-points are realized of extension
//                activate(item, loader);
//            }
//        });
//
//        return item;
//    }
//
//    private String getResourceString(Extension ext, String key, ClassLoader loader) {
//        try {
//            Plugin toolPlugin = getManager().getPlugin(ext.getDeclaringPluginDescriptor().getId());
//            Class<? extends Plugin> pluginClass = toolPlugin.getClass();
//            Class<?> toolClass = pluginClass.getClassLoader().loadClass(ext.getParameter("class").valueAsString());
//
//            String text = null;
//            Parameter param = ext.getParameter(key);
//            if (param != null) {
//                text = ResourceManager.getResource(toolClass, param.valueAsString(), loader);
//            }
//
//            if (StringUtils.isNullOrEmpty(text)) {
//                Tracer.getInstance().runtimeWarning("No property in plugin.xml for <parameter id=\"" + key + "\"> (evtl. optional?)");
//                return "";
//            } else {
//                return text;
//            }
//        } catch (Throwable e) {
//            Tracer.getInstance().runtimeWarning("see plugin.xml for <parameter id=\"" + key + "\"> " + e.getLocalizedMessage());
//            return "";
//        }
//    }
//
//    public static void showBusy(Runnable block) {
//        LauncherView.getInstance().runBlock(block);
//    }
//}