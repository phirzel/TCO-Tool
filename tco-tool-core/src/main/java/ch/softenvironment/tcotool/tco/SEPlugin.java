package ch.softenvironment.tcotool.tco;

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

import ch.ehi.basics.i18n.ResourceBundle;
import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.view.BaseDialog;
import java.net.URL;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.tcotool.application.LauncherView;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.pluginsupport.ApplicationPlugin;
import org.tcotool.pluginsupport.MenuExtension;
import org.tcotool.tools.ModelUtility;

/**
 * Free <i>soft</i>Environment Plugins (might serve as sample). PLUGIN_ID = "ch.softenvironment.tcotool.finance";
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class SEPlugin implements org.tcotool.pluginsupport.Plugin, org.tcotool.pluginsupport.Menu {

    private static URL header;

    public static URL getHeader() {
        if (header == null) {
            header = ResourceBundle.getURL(SEPlugin.class, "ReportHeader.png");
        }
        return header;
    }

    /**
     * Constructor.
     */
    public SEPlugin() {

    }

    @Override
    public void setUp() {
        // @see plugins/org.tcotool.core.runtime/plugin.xml
        // [extension-point id="*"]

        MenuExtension extension = new MenuExtension();
        extension.setId("tcoDirect");
        extension.setText(ResourceManager.getResource(SEPlugin.class, "MniReportTcoDirectIndirect_text"));
        extension.setIcon("MenuIcon.png");
        loadPlugins("ReportTco", false, false, false, extension);

        extension = new MenuExtension();
        extension.setId("tcoEstimated");
        extension.setText(ResourceManager.getResource(SEPlugin.class, "MniReportTcoEstimated_text"));
        extension.setIcon("MenuIcon.png");
        loadPlugins("ReportTco", true, false, false, extension);

        extension = new MenuExtension();
        extension.setId("dependencyDetailsAll");
        extension.setText(ResourceManager.getResource(SEPlugin.class, "MniReportTcoDepenendencyDetailsAll_text"));
        extension.setIcon("MenuIcon.png");
        loadPlugins("ReportTco", false, false, false, extension);

        extension = new MenuExtension();
        extension.setId("dependencyDetails");
        extension.setText(ResourceManager.getResource(SEPlugin.class, "MniReportTcoDepenendencyDetails_text"));
        extension.setIcon("MenuIcon.png");
        loadPlugins("ReportTco", true, false, false, extension);

        extension = new MenuExtension();
        extension.setId("pricing");
        extension.setText(ResourceManager.getResource(SEPlugin.class, "MniReportPricing_text"));
        extension.setIcon("MenuIcon.png");
        loadPlugins("ReportTco", false, false, false, extension);
    }

    /**
     * Menu was clicked -> generate the report.
     */
    @Override
    public void actionPerform(final JComponent item, final Object object) {
        ApplicationPlugin.showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    String actionCommand = ((JMenuItem) item).getActionCommand();
                    TcoObject base = (object == null) ? LauncherView.getInstance().getTcoPackageOrRoot(object) : (TcoObject) object;
                    if (actionCommand.equals("tcoDirect" /*
                     * @see plugin.xml=>extension -id
                     */)) {
                        reportTcoDirect(base);
                    } else if (actionCommand.equals("tcoEstimated" /*
                     * @see plugin .xml= >extension -id
                     */)) {
                        reportTcoEstimated(base);
                    } else if (actionCommand.equals("dependencyDetailsAll")) {
                        reportTcoDependencyDetails(base, true);
                    } else if (actionCommand.equals("dependencyDetails")) {
                        reportTcoDependencyDetails(base, false);
                    } else if (actionCommand.equals("pricing")) {
                        reportPricing(base, true);
                    }
                } catch (Exception e) {
                    LauncherView.getInstance().handleException(e);
                }
            }
        });
    }

    /**
     * Menu callback.
     */
    private void reportTcoDirect(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            ReportTcoDirect.createBlock(LauncherView.getInstance().getUtility(), root, LauncherView.getInstance().getUtility().getUsageDuration()));
    }

    /**
     * Menu callback.
     */
    private void reportTcoEstimated(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            ReportTcoEstimated.createBlock(LauncherView.getInstance().getUtility(), root, LauncherView.getInstance().getUtility().getUsageDuration()));
    }

    private void reportTcoDependencyDetails(TcoObject object, boolean allServices) throws Exception {
        LauncherView.getInstance().addReport(
            ReportDependency.createBlock(LauncherView.getInstance().getUtility(), object, LauncherView.getInstance().getUtility().getUsageDuration(),
                allServices));
    }

    private void reportPricing(TcoObject object, boolean allServices) throws Exception {
        if (object instanceof TcoPackage) {
            BaseDialog.showWarning(LauncherView.getInstance(), ResourceManager.getResource(SEPlugin.class, "CTSetProportions"),
                ResourceManager.getResource(SEPlugin.class, "CISetProportions"));
            LauncherView.getInstance().addReport(
                ReportPricing.createBlock(LauncherView.getInstance().getUtility(), object, LauncherView.getInstance().getUtility().getUsageDuration(),
                    allServices, ReportPricing.GENERIC_FACTOR));
        } else {
            // find max multitude in Service (default)
            double factor = ModelUtility.getMultitudeFactor(object);
            Iterator<CostDriver> cd = ((Service) object).getDriver().iterator();
            while (cd.hasNext()) {
                CostDriver driver = cd.next();
                if (ModelUtility.getMultitudeFactor(driver) > factor) {
                    factor = ModelUtility.getMultitudeFactor(driver);
                }
                for (Cost cost : driver.getCost()) {
                    if (ModelUtility.getMultitudeFactor(cost) > factor) {
                        factor = ModelUtility.getMultitudeFactor(cost);
                    }
                }
            }
            String input = JOptionPane.showInputDialog(LauncherView.getInstance(), ResourceManager.getResource(SEPlugin.class, "CISetDivisionFactor")/*
                 * ,
                 * "Mengen-Faktor"
                 */,
                factor/*
                 * JOptionPane . QUESTION_MESSAGE
                 */);
            if (StringUtils.isNullOrEmpty(input)) {
                // cancel pressed
                return;
            }
            LauncherView.getInstance().addReport(
                ReportPricing.createBlock(LauncherView.getInstance().getUtility(), object, LauncherView.getInstance().getUtility().getUsageDuration(),
                    allServices, Double.parseDouble(input)));
        }
    }
}