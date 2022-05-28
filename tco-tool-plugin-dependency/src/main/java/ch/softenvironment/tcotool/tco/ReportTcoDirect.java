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
import ch.softenvironment.util.NlsUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.tcotool.application.CostCauseDetailView;
import org.tcotool.application.FactCostDetailView;
import org.tcotool.application.LauncherView;
import org.tcotool.application.PersonalCostDetailView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.Cost;
import org.tcotool.model.CostCause;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.TcoObject;
import org.tcotool.standard.report.ReportTco;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report direct/indirect TCO costs based on CostCause#direct assignment to Personal/Fact-Cost.
 * <p>
 * Calculations as Plugin for TCO-Tool. Design Pattern: Visitor
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
class ReportTcoDirect extends ReportTco {

    private ClassLoader loader = null;
    private final String labelDirect = ResourceManager.getResourceAsNonLabeled(CostCauseDetailView.class, "LblDirect_text");
    private final String labelIndirect = ResourceManager.getResourceAsNonLabeled(CostCauseDetailView.class, "LblIndirect_text");

    protected ReportTcoDirect(ModelUtility utility, final String title) {
        super(utility, title);
    }

    /**
     * Summarize all other TCO-Costs within given object and sub-packages.
     */
    public static ReportTool createBlock(ModelUtility utility, TcoObject root, long maxUsage, ClassLoader loader) throws Exception {
        ReportTcoDirect tool = new ReportTcoDirect(utility, ResourceManager.getResource(SEPlugin.class, "MniReportTcoDirectIndirect_text", loader));
        tool.loader = loader;
        tool.calculator = new CalculatorTco(LauncherView.getInstance().getUtility(), root, maxUsage);
        tool.totalCost(root);
        return tool;
    }

    @Override
    public void startBody(AttributeList list) throws IOException {
        super.startBody(list);
        java.net.URL url = loader.getResource(SEPlugin.REPORT_HEADER);
        image(url, "", 0);
    }

    @Override
    protected void encodeCodes(DbObjectServer server, TcoObject root) throws Exception {
        // List codes = server.retrieveCodes(CostCause.class); // Cost->CostCause (contains DIRECT/INDIRECT info)
        // if (codes.size() > 0) {
        startParagraph();
        encodeCostBlock(root, null /* codes */, null /* Calculator.COST_CAUSE_UNDEFINED */, ModelUtility.getTypeString(CostCause.class));
        endParagraph();
        // }
    }

    /**
     * Direct or Indirect costs are defined on Cost#CostCause only.
     */
    @Override
    protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, String title) throws IOException {
        bold(getRsc("CIBy") + " <" + title + " *>:");
        startTable(1);
        startTableRow();
        // print header
        tableHeader(""); // perhaps colspan with next column
        tableHeader(title);
        for (int year = 0; year < calculator.getDurationYears(); year++) {
            String partialYear = "";
            if ((calculator.getMaxDurationMonths() - year * 12) < 12) {
                partialYear = NlsUtils.formatMessage("<br>" + getRsc("CIPartialYear"), (int) calculator.getMaxDurationMonths() % 12);
            }
            String header = getRsc("CITCO") + partialYear;
            String tmp = (year + 1) + ". " + getRsc("CIYear");
            nativeContent("<th>" + header + "<br>" + tmp + "</th>");
            getCsvWriter().cell(header + tmp);
        }
        tableHeader(getRsc("CITCOOverAll"));
        endTableRow();

        List<Double> direct = encodeCostBlockColumns(root, PersonalCost.class, labelDirect, Calculator.DIRECT);
        List<Double> indirect = encodeCostBlockColumns(root, PersonalCost.class, labelIndirect, Calculator.INDIRECT);
        // TODO only show undefined PC if link is pressed
        List<Double> undefined = encodeCostBlockColumns(root, PersonalCost.class, null /* Undefined */, Calculator.DIRECT_UNDEFINED);
        Calculator.accumulateLists(direct, encodeCostBlockColumns(root, FactCost.class, labelDirect, Calculator.DIRECT));
        Calculator.accumulateLists(indirect, encodeCostBlockColumns(root, FactCost.class, labelIndirect, Calculator.INDIRECT));
        // TODO only show undefined FC if link is pressed
        Calculator.accumulateLists(undefined, encodeCostBlockColumns(root, FactCost.class, null /* Undefined */, Calculator.DIRECT_UNDEFINED));

        // print total bottom line
        List<Double> totalCosts = calculator.getCostBlock(root);
        startTableRow();
        tableData("*");
        tableDataBold(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text"));
        double sumTcoOverUsage = 0.0;
        for (int i = 2; i < totalCosts.size(); i++) {
            double amount = Calculator.getValue(totalCosts, i);
            tableDataAmount(amount, true);
            if (i >= 2) {
                sumTcoOverUsage += amount;
            }
        }
        tableDataAmount(sumTcoOverUsage, true);
        endTableRow();
        startTableRow();

        // sum up direct & indirect
        nativeContent("<td colspan='6'>" + SPACE + "</td>");
        getCsvWriter().newline();
        endTableRow();
        startTableRow();
        tableData("*");
        tableDataBold(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text") + " " + labelDirect);
        for (int i = 0; i < direct.size(); i++) {
            tableDataAmount(direct.get(i).doubleValue(), true);
        }
        endTableRow();
        startTableRow();
        tableData("*");
        tableDataBold(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text") + " " + labelIndirect);
        for (int i = 0; i < indirect.size(); i++) {
            tableDataAmount(indirect.get(i).doubleValue(), true);
        }
        endTableRow();
        startTableRow();
        tableData("*");
        tableDataBold(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text") + " " + getCodeName(Calculator.COST_CAUSE_UNDEFINED));
        for (int i = 0; i < undefined.size(); i++) {
            tableDataAmount(undefined.get(i).doubleValue(), true);
        }
        endTableRow();

        endTable();

        encodeCostUnit("");
    }

    /**
     * Print Columns for TCO in HTML: [Personal/Fact Direct/Indirect] [TCO_Y1] [TCO_Y2]... [TCO_Total]
     *
     * @param root print to data of given service (null for all services)
     */
    protected List<Double> encodeCostBlockColumns(TcoObject root, Class<? extends Cost> cost, final String rowTitle, final String code) throws IOException {
        String kind = cost == FactCost.class ? Calculator.KIND_FC : Calculator.KIND_PC;
        List<Double> figures = new ArrayList<Double>();

        startTableRow();
        nativeContent("<td>" + getTcoObjectImage(LauncherView.getInstance().getUtility(), cost) + "</td>");
        getCsvWriter().cell(
            cost == PersonalCost.class ? ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text") : ResourceManager.getResource(
                FactCostDetailView.class, "FrmWindow_text"));
        if (rowTitle == null) {
            tableDataCode(Calculator.COST_CAUSE_UNDEFINED);
        } else {
            tableData(rowTitle);
        }
        int year = 0;
        double sumTcoOverUsage = 0.0;
        for (; year < calculator.getMaxDurationMonths() / 12; year++) {
            double amount = Calculator.getValue(calculator.getCodeTotal(root, code, kind), year + Calculator.INDEX_TOTAL + 1);
            figures.add(new Double(amount));
            tableDataAmount(amount, false);
            sumTcoOverUsage += amount;
        }
        if (calculator.getMaxDurationMonths() % 12 > 0) {
            // partial year
            double amount = Calculator.getValue(calculator.getCodeTotal(root, code, kind), year + Calculator.INDEX_TOTAL + 1);
            figures.add(new Double(amount));
            tableDataAmount(amount, false);
            sumTcoOverUsage += amount;
        }
        figures.add(new Double(sumTcoOverUsage));
        tableDataAmount(sumTcoOverUsage, true);
        endTableRow();

        return figures;
    }
}