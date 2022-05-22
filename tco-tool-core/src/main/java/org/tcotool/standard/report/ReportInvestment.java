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
import ch.softenvironment.util.Tracer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.tcotool.application.LauncherView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report financial Investment cost in HTML or CSV.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class ReportInvestment extends ReportTool {

	private final ModelUtility utility = null;

	private ReportInvestment(ModelUtility utility, final String title) {
		super(utility, title);
	}

	/**
	 * Summarize all Investment-Cost within given root and sub-packages.
	 */
	public static ReportTool createInvestment(ModelUtility utility, TcoPackage root) throws Exception {
		ReportInvestment tool = new ReportInvestment(utility, ResourceManager.getResource(LauncherView.class, "MniReportInvestment_text"));
		tool.calculator = new CalculatorTco(utility, root, 0);
		tool.totalInvestment(root);
		return tool;
	}

	/**
	 * Summarize CostCause over all services below root.
	 */
	private void totalInvestment(TcoObject root) throws Exception {
		startHtml();
		startBody(null);
		// calculate the model
		header("1", 1, root);
		paragraph(getRsc("CIAllCost"));

		startParagraph();
		encodeCodes(root.getObjectServer(), null);
		endParagraph();

		endBody();
		endHtml();
	}

	@Override
	protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, String title) throws IOException {
		if (codes.size() == 0) {
			// ignore: do not print any code Details at all
			return;
		}

		bold(getRsc("CIBy") + " <" + title + ">:");
		startTable(getAttributeTableFixedWidth(70));
		startTableRow();
		// print header
		tableHeader(title, getAttributeWidth(Integer.valueOf(250)));
		tableHeader(getRsc("CIPersonalCostFirstYear"), getAttributeWidth(Integer.valueOf(120)));
		tableHeader(getRsc("CIFactCostOverAll"), getAttributeWidth(Integer.valueOf(120)));
		endTableRow();

		// print data by CostType
		try {
			Iterator types = ListUtils.sort(codes, new DbObjectEvaluator(ModelUtility.getCodeTypeLocale()), DbObject.PROPERTY_NAME).iterator();
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
		List<Double> totalCosts = calculator.getCostBlock(root);

		// ch.softenvironment.jomm.serialize.AttributeList attrs =
		// getAlignRight();
		startTableRow();
		tableDataBold(ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text"));
		for (int i = 0; i < totalCosts.size(); i++) {
			double amount = Calculator.getValue(totalCosts, i);
			tableDataAmount(amount, true);
		}
		endTableRow();

		String cellName = getRsc("CITotalCost");

		// the final summarization
		startTableRow();
		tableDataBold(cellName);
		double totalAll = Calculator.getValue(totalCosts, 0) + Calculator.getValue(totalCosts, 1);
		tableDataAmount(totalAll, true, 2);
		// nativeContent("<td colspan=\"" + (calculator.getDurationYears() + 1)
		// +"\"></td>");
		// getCsvWriter().colSpan(calculator.getDurationYears());
		endTableRow();

		endTable();

		encodeCostUnit("");
	}

	/**
	 * Print Columns for TCO in HTML: [CostType] [PersonTotal] [[FactTotal] [TCO_Y1] [TCO_Y2]...]
	 *
	 * @param service print to data of given service (null for all services)
	 */
	private void encodeCostBlockColumns(TcoObject root, Object code) throws IOException {
		// ch.softenvironment.jomm.serialize.AttributeList attrs =
		// getAlignRight();
		startTableRow();
		// name of CostType
		tableDataCode(code);

		List<Double> factCosts = calculator.getCosts(root, Calculator.KIND_FC, code);
		List<Double> personalCosts = calculator.getCosts(root, Calculator.KIND_PC, code);

		double investment = Calculator.getValue(personalCosts, Calculator.INDEX_TOTAL);
		tableDataAmount(investment, false);
		investment = Calculator.getValue(factCosts, Calculator.INDEX_TOTAL);
		tableDataAmount(investment, false);

		endTableRow();
	}
}
