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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.util.NlsUtils;
import java.io.IOException;
import java.util.List;
import org.tcotool.application.DependencyDetailView;
import org.tcotool.application.LauncherView;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report Cost per Service as Plugin for TCO-Tool in HTML or CSV. Design Pattern: Visitor
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
class ReportPricing extends ReportDependency {

    protected static final double GENERIC_FACTOR = -1.0;
    private double factor = GENERIC_FACTOR;

    private ReportPricing(ModelUtility utility, final String title) {
        super(utility, title);
    }

    /**
     * Summarize all Depreciation-Cost for given root-object and sub-packages.
     */
    public static ReportTool createBlock(ModelUtility utility, TcoObject root, long maxUsage, boolean allServices, double multitude)
        throws Exception {
        ReportPricing tool = new ReportPricing(utility, ResourceManager.getResource(SEPlugin.class, "MniReportPricing_text"));
        tool.calculator = new CalculatorTco(LauncherView.getInstance().getUtility(), (TcoPackage) LauncherView.getInstance().getUtility().getRoot() /*
         * always
         * calculate
         * over all
         * because
         * of
         * related
         * dependencies
         */,
            maxUsage);
        tool.allServices = allServices;
        tool.factor = multitude;
        tool.costBlockCostCause(root);
        return tool;
    }

    @Override
    protected void encodeCostBlock(TcoObject object, List<? extends DbCodeType> codes, final String codeUndefined, String costTypeTitle) throws IOException {
        ModelUtility util = LauncherView.getInstance().getUtility();
        int depth = Calculator.calculateDependencyDepth(util, object, 0);

        if (allServices || (!allServices && (depth > 0))) {
            startTable(1);
            // print header
            startTableRow();
            tableHeader(ResourceManager.getResource(ReportTool.class, "CITCO") + " " + tcoYear + "." + ResourceManager.getResource(ReportTool.class, "CIYear"));
            tableHeader(ResourceManager.getResourceAsNonLabeled(DependencyDetailView.class, "PnlClient_text"));

            for (int col = 0; col < depth; col++) {
                tableHeader(ResourceManager.getResourceAsNonLabeled(DependencyDetailView.class, "PnlSupplier_text") + " " + (col + 1) + "."
                    + ResourceManager.getResource(ReportDependency.class, "CILevelIndirect"));
            }
            endTableRow();

            // TODO color the multitude of service
            double sum = encodeDependency(util, object, depth + 1, 0, "<font color='#FF0080'><bold>");

            endTable();

            double tmpFactor;
            if (factor == GENERIC_FACTOR) {
                sum = sum / util.getMultitudeFactor(object, true);
                tmpFactor = util.getMultitudeFactor(object, true);
            } else {
                sum = sum / factor;
                tmpFactor = factor;
            }
            Object[] tokens = {tmpFactor};
            nativeContent("<font color='#FF0080'><bold>"
                + NlsUtils.formatMessage(ResourceManager.getResource(ReportPricing.class, "CICostFactor"), tokens) + " = " + af.format(sum)
                + "</bold></font>");
            getCsvWriter().cell(NlsUtils.formatMessage(ResourceManager.getResource(ReportPricing.class, "CICostFactor"), tokens));
            getCsvWriter().cell(getCsvWriter().encodeNumber(sum));
            breakLine();
            encodeCostUnit("");
        }
    }
}