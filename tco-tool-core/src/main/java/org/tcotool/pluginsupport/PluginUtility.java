package org.tcotool.pluginsupport;

import ch.softenvironment.util.Tracer;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.java.plugin.ObjectFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.registry.IntegrityCheckReport;

/**
 * Utility to introduce a Core/Application-Plugin which defines a general entry-point for derived Plugins to extend TCO-Tool. Plugin-mechanism is based on the "Java Plugin Framework (JPF)", for more
 * info see http://jpf.sourceforge.net/.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@Deprecated(since = "own mechanism")
public final class PluginUtility {

    private static final String PLUGINS_FOLDER = "./plugins";
    private static final String DATA_FOLDER = "./data";
    private static final String MANIFEST = "plugin.xml";

    /**
     * Scan for available Plugins for e.g. at startup and register them. The ApplicationCore-Plugin must be passed.
     *
     * @param corePluginId Id of an extension-Point
     * @throws Exception if any error occurred
     * @see org.tcotool.core.runtime.ApplicationPlugin
     */
    public static PluginManager invokePlugins(final String corePluginId) throws Exception {
        try {
            // suppress any logs by commons-logging.jar
            // org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
            // "org.apache.commons.logging.impl.NoOpLog");

            // Prepare parameters to start plug-in manager
            StringTokenizer st = new StringTokenizer(PLUGINS_FOLDER, ",", false);
            List<PluginManager.PluginLocation> pluginLocations = new LinkedList<PluginManager.PluginLocation>(); // V0.5:
            // Map
            // pluginLocations
            // =
            // new
            // HashMap();
            for (; st.hasMoreTokens(); ) {
                File folder = new File(st.nextToken().trim()).getCanonicalFile();
                // Tracer.getInstance().debug(PluginUtility.class,
                // "invokePlugins()", "plug-ins folder - " + folder);

                if (!folder.isDirectory()) {
                    Tracer.getInstance().runtimeInfo("no Plugins-Folder yet");
                    return null;
                }
                File[] pluginFolders = folder.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(final File file) {
                        return file.isDirectory();
                    }
                });
                for (int i = 0; i < pluginFolders.length; i++) {
                    File pluginFolder = pluginFolders[i];
                    File manifestFile = getManifestFile(pluginFolder);
                    if (manifestFile != null) {
                        Tracer.getInstance().debug("Found Plugin-Manifest: " + manifestFile.toString());
                        // V0.5: pluginLocations.put(manifestFile,
                        // pluginFolder);
                        final URL manifestUrl = manifestFile.toURL();
                        final URL contextUrl = pluginFolder.toURL();
                        pluginLocations.add(new PluginManager.PluginLocation() {
                            @Override
                            public java.net.URL getManifestLocation() {
                                return manifestUrl;
                            }

                            @Override
                            public java.net.URL getContextLocation() {
                                return contextUrl;
                            }
                        });
                    }
                }
            }
            // Create instance of plug-in manager providing collection
            // of discovered plug-in manifests and corresponding plug-in folders
            PluginManager pluginManager = ObjectFactory.newInstance().createManager(); // V0.5:
            // PluginManager.createStandardManager(pluginLocations);
            pluginManager.publishPlugins(pluginLocations.toArray(new PluginLocation[pluginLocations.size()]));

            // Check plug-in's integrity
            IntegrityCheckReport integrityCheckReport = pluginManager.getRegistry().checkIntegrity(pluginManager.getPathResolver() /*
             * ,
             * true
             */);
            if (integrityCheckReport.countErrors() != 0) {
                // Something wrong in plug-ins set
                Tracer.getInstance().runtimeError("integrity check done: errors - " + integrityCheckReport.countErrors() + ", warnings - " + integrityCheckReport.countWarnings());

                Tracer.getInstance().runtimeError(integrityCheckReport2str(integrityCheckReport));
                // throw new Exception("plug-ins integrity check failed");
            }
            // Tracer.getInstance().debug(PluginUtility.class,
            // "invokePlugins()",
            // integrityCheckReport2str(integrityCheckReport));

            // get the start-up plug-in (plugin-id => see plugin.xml)
            Plugin corePlugin = pluginManager.getPlugin(corePluginId);
            if (corePlugin == null) {
                throw new Exception("can't get Plugin: " + corePluginId);
            }
            // Now we are ready to RUN! Let's do it!
            // Run Forest, run!!!

            // We need to use reflection here to call any plug-in method outside
            // of plug-in itself because any directly referenced plug-in class
            // shouldn't be loaded before JPF initializing.
            //
            // We also want to avoid any compile-time dependencies on plugin's
            // classes.
            corePlugin.getClass().getMethod("run", File.class).invoke(corePlugin, new File(DATA_FOLDER));
            return pluginManager;
        } catch (InvocationTargetException ite) {
            // Something get wrong during start up.
            throw new Exception("can't start application, see log file for details");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Read the Manifest-file in specific Plugin-Folder named: - "plugin.xml"
     *
     * @param folder (for e.g. ./plugins/my.plugin/plugin.xml)
     * @return
     */
    private static File getManifestFile(final File folder) {
        File result = new File(folder, MANIFEST);
        if (result.isFile()) {
            return result;
        }
        return null;
    }

    private static String integrityCheckReport2str(final IntegrityCheckReport report) {
        StringBuffer buf = new StringBuffer();
        buf.append("integrity check report:\r\n");
        buf.append("-------------- REPORT BEGIN -----------------\r\n");
        for (Iterator<IntegrityCheckReport.ReportItem> it = report.getItems().iterator(); it.hasNext(); ) {
            IntegrityCheckReport.ReportItem item = it.next();
            buf.append("\tseverity=").append(item.getSeverity()).append("; code=").append(item.getCode()).append("; message=").append(item.getMessage()).append("; source=").append(item.getSource())
                .append("\r\n");
        }
        buf.append("-------------- REPORT END -----------------");
        return buf.toString();
    }

    public static ClassLoader getClassLoader(Plugin plugin) {
        return plugin.getManager().getPluginClassLoader(plugin.getDescriptor());
    }
}