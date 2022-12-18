package ch.softenvironment.tcotool.finance;

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
import ch.softenvironment.jomm.serialize.AttributeList;
import ch.softenvironment.tcotool.tco.TCOPlugin;
import ch.softenvironment.view.BaseDialog;
import java.io.IOException;
import java.util.List;
import org.tcotool.application.LauncherView;
import org.tcotool.application.SystemParameterDetailView;
import org.tcotool.model.TcoObject;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorDegressiveDepreciation;
import org.tcotool.tools.CalculatorLinearDepreciation;
import org.tcotool.tools.CalculatorLinearDepreciationCompountInterest;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report financial depreciation (bookings) Calculations as plugin for TCO-Tool. Design Pattern: Visitor
 *
 * @author Peter Hirzel
 */
class ReportDepreciation extends ReportTool {

    private long maxDepreciation = 0;

    protected ReportDepreciation(ModelUtility utility, final String title) {
        super(utility, title);
    }

    /**
     * Summarize all Depreciation-Costs within given object and sub-packages.
     */
    public static ReportTool createBlockDepreciation(ModelUtility utility, TcoObject root, long maxDepreciation) throws Exception {
        ReportDepreciation tool = new ReportDepreciation(utility, ResourceManager.getResource(FinancePlugin.class, "MniReportFinancialTotal_text"));
        tool.maxDepreciation = maxDepreciation;
        // tool.calculator = new
        // CalculatorLinearDepreciation(LauncherView.getInstance().getUtility(),
        // root, maxDepreciation);
        tool.totalCost(root);
        return tool;
    }

    @Override
    public void startBody(AttributeList list) throws IOException {
        super.startBody(list);

        image(TCOPlugin.getHeader(), "", 0);
    }

    @Override
    protected void encodeCodes(DbObjectServer server, TcoObject root) throws Exception {
        calculator = new CalculatorLinearDepreciation(LauncherView.getInstance().getUtility(), root, maxDepreciation);
        encodeDepreciation(root, ResourceManager.getResource(FinancePlugin.class, "CTLinearDepreciation"));
        calculator = new CalculatorLinearDepreciationCompountInterest(LauncherView.getInstance().getUtility(), root, maxDepreciation);
        if (calculator.isBaseOffsetInvolved()) {
            // TODO NLS
            BaseDialog.showWarning(LauncherView.getInstance(), "Calculation without warranty",
                "The Base-Offset (de: Basis-Offset) is not yet considered in this report, sorry.");
        }
        encodeDepreciation(root, ResourceManager.getResource(FinancePlugin.class, "CTLinearDepreciationCompoundInterest"));
        calculator = new CalculatorDegressiveDepreciation(LauncherView.getInstance().getUtility(), root, maxDepreciation);
        encodeDepreciation(root, ResourceManager.getResource(FinancePlugin.class, "CTDegressiveDepreciation"));
    }

    private void encodeDepreciation(TcoObject root, String title/*
     * , final String type
     */) throws IOException {
        startParagraph();
        List<Double> totalCosts = calculator.getTotalCosts(root, Calculator.KIND_FC); // getDepreciationCostBlock(root,
        // type);
        bold(title);
        simpleContent(" " + ResourceManager.getResource(FinancePlugin.class, "CQFactCostIncluded") + ":" /*
         * getRsc ( "CIFactCostOverAll" )
         */);
        breakLine();

        startTable(1);
        startTableRow();
        tableHeader("0. " + getRsc("CIYear"));
        for (int year = 0; year < calculator.getDurationYears(); year++) {
            // TODO consider partial year
            /*
             * String partialYear = ""; if ((calculator.getMaxDurationMonths() - year * 12) < 12) { partialYear = " [" + NlsUtils.formatMessage("<br>" +
             * getRsc("CIPartialYear"), (int)calculator.getMaxDurationMonths() % 12) + "]"; }
             */
            String tmp = (year + 1) + ". " + getRsc("CIYear");
            tableHeader(tmp /* + partialYear */);

        }
        endTableRow();

        startTableRow();
        for (int i = 0; i < calculator.getDurationYears() + 1; i++) {
            double amount = Calculator.getValue(totalCosts, i);
            tableDataAmount(amount, true);
        }
        endTableRow();
        endTable();

        // if (type.equals(Calculator.DEPRECIATION_GEOMETRIC_DEGRESSIVE)) {
        String tmp = "; " + ResourceManager.getResourceAsNonLabeled(SystemParameterDetailView.class, "LblDepreciationInterestRate_text") + " = "
            + LauncherView.getInstance().getUtility().getInterestRate() + "%";
        // }
        encodeCostUnit(tmp);

        endParagraph();
    }
}