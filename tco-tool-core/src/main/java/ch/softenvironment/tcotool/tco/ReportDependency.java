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
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.serialize.AttributeList;
import ch.softenvironment.math.MathUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.tcotool.application.DependencyDetailView;
import org.tcotool.application.FactCostDetailView;
import org.tcotool.application.LauncherView;
import org.tcotool.application.PersonalCostDetailView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.Dependency;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report Dependency-Calculations as Plugin for TCO-Tool in HTML or CSV. Design Pattern: Visitor
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
class ReportDependency extends ReportTool {

    private static final String EMPTY_CELL = " ";
    protected boolean allServices = false;
    protected int tcoYear = Calculator.INDEX_TOTAL + 1; // 1st year of TCO-usage

    // of interest here

    protected ReportDependency(ModelUtility utility, final String title) {
        super(utility, title);
    }

    /**
     * Summarize all Depreciation-Cost for given root-object and sub-packages.
     */
    public static ReportTool createBlock(ModelUtility utility, TcoObject root, long maxUsage, boolean allServices) throws Exception {
        ReportDependency tool = new ReportDependency(utility, ResourceManager.getResource(TCOPlugin.class,
            allServices ? "MniReportTcoDepenendencyDetailsAll_text" : "MniReportTcoDepenendencyDetails_text"));
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
        tool.costBlockCostCause(root);
        return tool;
    }

    @Override
    protected void encodeCostBlock(TcoObject object, List<? extends DbCodeType> codes, final String codeUndefined, final String costTypeTitle)
        throws IOException {
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

            encodeDependency(util, object, depth + 1, 0, null);

            endTable();
            encodeCostUnit("");
        }
    }

    /**
     * Print all Client/Supplier dependencies one after each other: - FactCost - PersonalCost - DependencyCost - Total
     *
     * @param util
     * @param object
     * @param depth
     * @param currentDepth
     * @throws IOException
     */
    protected double encodeDependency(ModelUtility util, TcoObject object, int depth, int currentDepth, String color) throws IOException {
        double sum = 0.0;
        double multitude = util.getMultitudeFactor(object, true);
        String dividor = (color == null ? "" + multitude : color + multitude + "</bold></font>");
        String multitudeText = "(" + ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblMultitude_text") + "=" + dividor + ")";

        // print object data
        startTableRow();
        nativeContent("<td colspan=\"" + (depth + 1) + "\"><i><b>" + linkObject(object, true, true) + "</b> " + multitudeText + "</i></td>");
        getCsvWriter().cell(object.getName() + " " + multitudeText);
        getCsvWriter().colSpan(depth + 1);
        endTableRow();
        startTableRow();
        tableData(ResourceManager.getResource(FactCostDetailView.class, "FrmWindow_text"));
        for (int col = 0; col < depth; col++) {
            if (col == currentDepth) {
                sum = Calculator.getValue(calculator.getTotalCosts(object, Calculator.KIND_FC), tcoYear);
                tableDataAmount(sum, false);
            } else {
                tableData(EMPTY_CELL);
            }
        }
        endTableRow();
        startTableRow();
        tableData(ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text"));
        for (int col = 0; col < depth; col++) {
            if (col == currentDepth) {
                double temp = Calculator.getValue(calculator.getTotalCosts(object, Calculator.KIND_PC), tcoYear);
                tableDataAmount(temp, false);
                sum += temp;
            } else {
                tableData(EMPTY_CELL);
            }
        }
        endTableRow();

        // print object-suppliers data
        Iterator<Dependency> it = object.getSupplierId().iterator();
        while (it.hasNext()) {
            Dependency dep = it.next();
            TcoObject supplier = util.findObject(dep.getSupplierId(), (TcoObject) util.getRoot());

            // ch.softenvironment.jomm.serialize.AttributeList attrs =
            // getAlignRight();
            startTableRow();
            String s = "--[" + MathUtils.fix(dep.getDistribution(), 1) + "%]--> ";
            nativeContent("<td>" + s + linkObject(supplier, true, false) + "</td>");
            getCsvWriter().cell(s + supplier.getName());
            for (int col = 0; col < depth; col++) {
                if (col == currentDepth) {
                    // calculate influence of dependency on this element
                    double temp = calcDependency(util, dep, tcoYear);
                    tableDataAmount(temp, false);
                    sum += temp;
                } else {
                    tableData(EMPTY_CELL);
                }
            }

            endTableRow();
        }
        // print object total
        startTableRow();
        tableDataBold(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text"));
        for (int col = 0; col < depth; col++) {
            if (col == currentDepth) {
                tableDataAmount(sum, true);
            } else {
                tableData(EMPTY_CELL);
            }
        }
        endTableRow();

        // print details of sub-suppliers in own detailed column
        it = object.getSupplierId().iterator();
        while (it.hasNext()) {
            Dependency dep = it.next();
            TcoObject supplier = util.findObject(dep.getSupplierId(), (TcoObject) util.getRoot());
            encodeDependency(util, supplier, depth, currentDepth + 1, null);
        }
        return sum;
    }

    /**
     * Called when a Service is given as selected-node in NavTree.
     */
    @Override
    protected void encodeCodes(DbObjectServer server, TcoObject root) {
        // suppress any output by parent implementation
        // encodeCostBlock(root, null, null, null);
    }

    @Override
    public void startBody(AttributeList list) throws IOException {
        super.startBody(list);
        image(TCOPlugin.getHeader(), "", 0);
    }

    /**
     * Return the costs a dependency has for a given TCO-Year, including fact-, personal- and sub-dependency cost.
     *
     * @param dependency (to be calculated)
     * @param tcoYear (interesting year within TCO-Usage duration)
     * @return
     */
    private double calcDependency(ModelUtility util, Dependency dependency, int tcoYear) {
        TcoObject supplier = util.findObject(dependency.getSupplierId(), (TcoObject) util.getRoot());
        // FactCost
        double sum = Calculator.getValue(calculator.getTotalCosts(supplier, Calculator.KIND_FC), tcoYear);
        // PersonalCost
        sum = sum + Calculator.getValue(calculator.getTotalCosts(supplier, Calculator.KIND_PC), tcoYear);

        // Supplier-influence of dependency (not subject to local multitude!)
        for (Dependency subDependency : supplier.getSupplierId()) {
            // add indirect sub-dependencies recursively
            sum = sum + calcDependency(util, subDependency, tcoYear);
        }
        // dependency's percentage
        return sum / 100.0 * dependency.getDistribution();
    }
}