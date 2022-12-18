package org.tcotool.standard.report;

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
import ch.softenvironment.util.ListUtils;
import ch.softenvironment.util.NlsUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.tcotool.application.LauncherView;
import org.tcotool.application.NavigationView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report TCO-Costs in HTML or CSV. Design Pattern: Visitor
 *
 * @author Peter Hirzel
 */
@Slf4j
public class ReportTco extends ReportTool {

	protected ReportTco(ModelUtility utility, final String title) {
		super(utility, title);
	}

	/**
	 * Summarize all TCO-Cost within given object and sub-packages.
	 */
	public static ReportTool createBlockTco(ModelUtility utility, TcoPackage root, long tcoMaxUsage) throws Exception {
		ReportTco tool = new ReportTco(utility, ResourceManager.getResource(LauncherView.class, "MniReportTotalTcoCost_text"));
		tool.calculator = new CalculatorTco(utility, root, tcoMaxUsage);
		tool.totalCost(root);
		return tool;
	}

	/**
	 * Summarize TCO-Cost for each Service (including Sub-Packages).
	 */
	public static ReportTool createBlockDetailedTco(ModelUtility utility, TcoObject root, long tcoMaxUsage) throws Exception {
		ReportTco tool = new ReportTco(utility, ResourceManager.getResource(NavigationView.class, "MniReportTcoCostDetailed_text"));
		tool.calculator = new CalculatorTco(utility, root, tcoMaxUsage);
		tool.costBlockCostCause(root);
		return tool;
	}

	@Override
	protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, final String title) throws IOException {
		bold(getRsc("CIBy") + " <" + title + ">:");
		startTable(getAttributeTableFixedWidth(100));
		startTableRow();
		// print header
		tableHeader(title, getAttributeWidth(Integer.valueOf(250)));
		// tableHeader(getRsc("CIPersonalCostFirstYear"));
		// tableHeader(getRsc("CIFactCostOverAll"));
		for (int year = 0; year < calculator.getDurationYears(); year++) {
			String partialYear = "";
			if ((calculator.getMaxDurationMonths() - year * 12) < 12) {
				partialYear = NlsUtils.formatMessage("<br>" + getRsc("CIPartialYear"), (int) calculator.getMaxDurationMonths() % 12);
			}
			String header = getRsc("CITCO") + partialYear;
			String tmp = (year + 1) + ". " + getRsc("CIYear");
			// getAttributeWidth(Integer.valueOf(190))
			nativeContent("<th " + YEAR_COLUMN_WITH_STYLE + ">" + header + "<br>" + tmp + "</th>");
			getCsvWriter().cell(header + tmp);
		}
		tableHeader(getRsc("CITCOOverAll"));
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
			log.error("", e);
		}

		// print total bottom lines
		// List totalPersonal = calculator.getTotalCosts(root,
		// Calculator.PERSONAL_TCO);
		// List totalFacts = calculator.getTotalCosts(root,
		// Calculator.FACT_TCO);
		List<Double> totalCosts = calculator.getCostBlock(root);
		// ch.softenvironment.jomm.serialize.AttributeList attrs =
		// getAlignRight();
		startTableRow();
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

		String cellName = getRsc("CITotalCost");
		if (printDetails) {
			// print dependencies only
			Calculator calculator = new CalculatorTco(utility, (TcoPackage) utility.getRoot(), utility.getUsageDuration());
			List<Double> serviceDependencyCosts = calculator.calculateDependency(root); // Calculator.calcDependency(ModelUtility utility, root);
			startTableRow();
			tableDataBold(getRsc("CIDependencyCost"));
			// TODO: show dependency by PersonalCost & FactCost separately
			sumTcoOverUsage = 0.0;
			// tableDataAmount(Calculator.getValue(serviceDependencyCosts, 0),
			// true, 2);
			for (int i = 1; i < calculator.getDurationYears() + 1; i++) {
				double amount = Calculator.getValue(serviceDependencyCosts, i);
				tableDataAmount(amount, true);
				sumTcoOverUsage += amount;
			}
			tableDataAmount(sumTcoOverUsage, true);
			endTableRow();

			// the final summarization with Dependencies
			startTableRow();
			tableDataBold(cellName + " & " + ResourceManager.getResource(ServiceDetailView.class, "PnlDependencies_text"));
			// make same array Structure as in serviceDependencyCost (cummulate
			// PC + FC in first cell)
			double totalPC_FC = Calculator.getValue(totalCosts, 0) + Calculator.getValue(totalCosts, 1);
			totalCosts.set(1, new Double(totalPC_FC));
			totalCosts.remove(0);
			Calculator.accumulateLists(totalCosts, serviceDependencyCosts);
			sumTcoOverUsage = 0.0;
			// tableDataAmount(Calculator.getValue(totalCosts, 0), true, 2);
			for (int i = 1; i < calculator.getDurationYears() + 1; i++) {
				double amount = Calculator.getValue(totalCosts, i);
				tableDataAmount(amount, true);
				sumTcoOverUsage += amount;
			}
			tableDataAmount(sumTcoOverUsage, true);
			endTableRow();
		} /*
		 * else { // the final summarization without Dependencies
		 *
		 * startTableRow(); tableDataBold(cellName); double totalAll = Calculator.getValue(totalCosts, 0) + Calculator.getValue(totalCosts, 1);
		 * tableDataAmount(totalAll, true, 2); nativeContent("<td colspan=\"" + (calculator.getDurationYears() + 1) +"\"></td>");
		 * getCsvWriter().colSpan(calculator.getDurationYears()); endTableRow(); }
		 */
		endTable();

		encodeCostUnit("");
	}

	/**
	 * Print Columns for TCO in HTML: [CostType] [TCO_Y1 (P+F)] [TCO_Y2 (P+F)]...
	 *
	 * @param root
	 * @param code
	 * @see Calculator#getCosts(TcoObject, String, Object)
	 */
	protected void encodeCostBlockColumns(TcoObject root, Object code) throws IOException {
		List<Double> factCosts = calculator.getCosts(root, Calculator.KIND_FC, code);
		List<Double> personalCosts = calculator.getCosts(root, Calculator.KIND_PC, code);

		startTableRow();
		tableDataCode(code);
		// sum Personal- & FactCost's together for each TCO-Year[n]-column
		int year = 0;
		double sumTcoOverUsage = 0.0;
		for (; year < calculator.getMaxDurationMonths() / 12; year++) {
			double amount = 0.0;
			if (personalCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
				amount += Calculator.getValue(personalCosts, year + Calculator.INDEX_TOTAL + 1);
			}
			if (factCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
				amount += Calculator.getValue(factCosts, year + Calculator.INDEX_TOTAL + 1);
			}
			tableDataAmount(amount, false);
			sumTcoOverUsage += amount;
		}

		// add partial costs
		if (calculator.getMaxDurationMonths() % 12 > 0) {
			double amount = 0.0;
			if (personalCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
				amount += Calculator.getValue(personalCosts, year + Calculator.INDEX_TOTAL + 1);
			}
			if (factCosts.size() > (year + Calculator.INDEX_TOTAL + 1)) {
				amount += Calculator.getValue(factCosts, year + Calculator.INDEX_TOTAL + 1);
			}
			tableDataAmount(amount, false);
			sumTcoOverUsage += amount;
		}
		tableDataAmount(sumTcoOverUsage, true);

		endTableRow();
	}
}
