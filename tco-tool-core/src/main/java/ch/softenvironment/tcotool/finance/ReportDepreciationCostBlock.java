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
import ch.softenvironment.jomm.mvc.controller.DbObjectEvaluator;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.serialize.AttributeList;
import ch.softenvironment.tcotool.tco.TCOPlugin;
import ch.softenvironment.util.ListUtils;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.Tracer;
import ch.softenvironment.view.BaseDialog;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.tcotool.application.LauncherView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.application.SystemParameterDetailView;
import org.tcotool.model.TcoObject;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorLinearDepreciationCompountInterest;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report financial depreciation Calculations as Plugin for TCO-Tool in HTML or CSV. Design Pattern: Visitor
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
class ReportDepreciationCostBlock extends ReportTool {

	protected ReportDepreciationCostBlock(ModelUtility utility, final String title) {
		super(utility, title + " [" + ResourceManager.getResource(FinancePlugin.class, "CTCompoundInterest") + "]");
	}

	/**
	 * Summarize all Depreciation-Cost within given object and sub-packages.
	 */
	public static ReportTool createBlockDepreciation(ModelUtility utility, TcoObject root, long maxDepreciation) throws java.lang.Exception {
		ReportDepreciationCostBlock tool = new ReportDepreciationCostBlock(utility, ResourceManager.getResource(FinancePlugin.class,
			"MniReportFinancialCostBlockTotal_text"));
		tool.calculator = new CalculatorLinearDepreciationCompountInterest(LauncherView.getInstance().getUtility(), root, maxDepreciation);
		if (tool.calculator.isBaseOffsetInvolved()) {
			// TODO NLS
			BaseDialog.showWarning(LauncherView.getInstance(), "Calculation without warranty",
				"The Base-Offset (de: Basis-Offset) is not yet considered in this report, sorry.");
		}
		tool.totalCost(root);
		return tool;
	}

	/**
	 * Summarize Depreciation-Cost for each Service (including Sub-Packages).
	 */
	public static ReportTool createBlockDetailedDepreciation(ModelUtility utility, TcoObject root, long maxDepreciation)
		throws java.lang.Exception {
		ReportDepreciationCostBlock tool = new ReportDepreciationCostBlock(utility, ResourceManager.getResource(FinancePlugin.class,
			"MniReportFinancialCostBlockDetailed_text"));
		tool.calculator = new CalculatorLinearDepreciationCompountInterest(LauncherView.getInstance().getUtility(), root, maxDepreciation);
		if (tool.calculator.isBaseOffsetInvolved()) {
			// TODO NLS
			BaseDialog.showWarning(LauncherView.getInstance(), "Calculation without warranty",
				"The Base-Offset (de: Basis-Offset) is not yet considered in this report, sorry.");
		}
		tool.costBlockCostCause(root);
		return tool;
	}

	@Override
	protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, final String title) throws IOException {
		if (codes.size() == 0) {
			// ignore
			return;
		}

		bold(getRsc("CIBy") + " <" + title + ">:");
		startTable(getAttributeTableFixedWidth(80));
		startTableRow();
		// print header
		tableHeader(title, getAttributeWidth(250));

		tableHeader(getRsc("CIFactCostOverAll"));
		for (int year = 0; year < calculator.getDurationYears(); year++) {
			String partialYear = "";
			if ((calculator.getMaxDurationMonths() - year * 12) < 12) {
				partialYear = NlsUtils.formatMessage("<br>" + getRsc("CIPartialYear"), (int) calculator.getMaxDurationMonths() % 12);
			}
			String header = (getRsc("CIDepreciationValue")) + partialYear;
			String tmp = (year + 1) + ". " + getRsc("CIYear");
			nativeContent("<th " + YEAR_COLUMN_WITH_STYLE + ">" + header + "<br>" + tmp + "</th>");
			getCsvWriter().cell(header + tmp);
		}
		endTableRow();

		// print data by CostCause
		try {
			Iterator<? extends DbCodeType> types = ListUtils.sort(codes, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()), DbObject.PROPERTY_NAME)
				.iterator();
			while (types.hasNext()) {
				encodeCostBlockColumns(root, types.next());
			}
			encodeCostBlockColumns(root, codeUndefined);
		} catch (Exception e) {
			Tracer.getInstance().runtimeError(null, e);
		}

		// print totals
		// List totalPersonal = calculator.getTotalCosts(root,
		// Calculator.PERSONAL_TCO);
		// List totalFacts = calculator.getTotalCosts(root,
		// Calculator.FACT_TCO);
		// List totalDepreciation = calculator.getTotalCosts(root,
		// Calculator.DEPRECIATION_COSTS);
		List<Double> totalCosts = calculator.getCostBlock(root); // .getDepreciationCostBlock(root,
		// CalculatorDepreciation.DEPRECIATION_LINEAR_COMPOUND_INTEREST);

		// ch.softenvironment.jomm.serialize.AttributeList attrs =
		// getAlignRight();
		startTableRow();
		tableDataBold(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text"));
		// double sumTcoOverUsage = 0.0;
		for (int i = 1 /* skip PersonalCost total */; i < totalCosts.size(); i++) {
			double amount = Calculator.getValue(totalCosts, i);
			tableDataAmount(amount, true);
			/*
			 * if (i>=2) { sumTcoOverUsage += amount; }
			 */
		}
		endTableRow();

		endTable();

		String tmp = "; " + ResourceManager.getResourceAsNonLabeled(SystemParameterDetailView.class, "LblDepreciationInterestRate_text") + " = "
			+ LauncherView.getInstance().getUtility().getInterestRate() + "%";

		encodeCostUnit(tmp);
	}

	/**
	 * Print Columns for TCO in HTML: [CostType] [PersonTotal] [[FactTotal] [TCO_Y1] [TCO_Y2]...]
	 */
	private void encodeCostBlockColumns(TcoObject root, Object code) throws IOException {
		// ch.softenvironment.jomm.serialize.AttributeList attrs =
		// getAlignRight();
		startTableRow();
		// name of CostType
		// tableData(getCodeName(code));
		tableDataCode(code);

		List<Double> factCosts = calculator.getCosts(root, Calculator.KIND_FC /*
		 * CalculatorDepreciation . DEPRECIATION_LINEAR_COMPOUND_INTEREST
		 */, code);
		// List personalCosts = new ArrayList();

		tableDataAmount(Calculator.getValue(factCosts, Calculator.INDEX_TOTAL), false);

		// sum Personal- & FactCost's together for Depreciation-Year-columns
		int year = 0;
		double sumTcoOverUsage = 0.0;
		for (; year < calculator.getMaxDurationMonths() / 12; year++) {
			double amount = 0.0;
			// if (personalCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
			// amount += Calculator.getValue(personalCosts, year +
			// Calculator.INDEX_TOTAL + 1);
			// }
			if (factCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
				amount += Calculator.getValue(factCosts, year + Calculator.INDEX_TOTAL + 1);
			}
			tableDataAmount(amount, false);
			sumTcoOverUsage += amount;
		}

		// add partial costs
		if (calculator.getMaxDurationMonths() % 12 > 0) {
			double amount = 0.0;
			// if (personalCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
			// amount += Calculator.getValue(personalCosts, year +
			// Calculator.INDEX_TOTAL + 1);
			// }
			if (factCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
				amount += Calculator.getValue(factCosts, year + Calculator.INDEX_TOTAL + 1);
			}
			tableDataAmount(amount, false);
			//TODO never used yet
			sumTcoOverUsage += amount;
		}

		endTableRow();
	}

	@Override
	public void startBody(AttributeList list) throws IOException {
		super.startBody(list);
		image(TCOPlugin.getHeader(), "", 0);
	}
}
