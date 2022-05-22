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
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.tcotool.application.CostDriverDetailView;
import org.tcotool.application.PersonalCostDetailView;
import org.tcotool.application.RoleDetailView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.Activity;
import org.tcotool.model.CostCause;
import org.tcotool.model.Role;
import org.tcotool.model.SystemParameter;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to report Personal-Costs in HTML or CSV. Design Pattern: Visitor
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class ReportStaff extends ReportTool {

    private ReportStaff(ModelUtility utility, final String title) {
        super(utility, title);
    }

    /**
     * Summarize all personalCost.
     */
    public static ReportTool createPersonalCosts(ModelUtility utility, TcoPackage root) throws Exception {
        ReportStaff tool = new ReportStaff(utility, ResourceManager.getResourceAsLabel(PersonalCostDetailView.class, "FrmWindow_text") + root.getName());
        tool.calculator = new CalculatorTco(utility, root, 0 /* irrelevant */);
        tool.costPersonalEffort(root);
        return tool;
    }

    /**
     *
     */
    private void costPersonalEffort(TcoPackage rootObject) throws Exception {
        startHtml();
        startBody(null);
        header(1, title);
        // print the model
        TcoObject element = rootObject;
        if (element instanceof TcoPackage) {
            double totalPersonalInternalHours = Calculator.getValue(calculator.getCosts(element, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_INTERNAL),
                Calculator.INDEX_TOTAL);// Calculator.getValue(hours, 0);
            double totalPersonalExternalHours = Calculator.getValue(calculator.getCosts(element, Calculator.KIND_PC, Calculator.PERSONAL_HOURS_EXTERNAL),
                Calculator.INDEX_TOTAL);
            double totalPersonalInternalLumpSum = Calculator.getValue(calculator.getCosts(element, Calculator.KIND_PC, Calculator.PERSONAL_COST_INTERNAL_LUMP),
                Calculator.INDEX_TOTAL);
            double totalPersonalExternalLumpSum = Calculator.getValue(calculator.getCosts(element, Calculator.KIND_PC, Calculator.PERSONAL_COST_EXTERNAL_LUMP),
                Calculator.INDEX_TOTAL);

            SystemParameter sysPar = utility.getSystemParameter();
            java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(1);
            nf.setMaximumFractionDigits(1);

            header(1, getRsc("CIPersonalPartialEffort"));

            header(2, getRsc("CIPersonalRapportedEffort"));
            startParagraph();
            startTable(1);
            startTableRow();
            tableHeader(ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text"));
            tableHeader("[h]");
            tableHeader(ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblYearlyHours_text") + " ~");
            endTableRow();
            startTableRow();
            tableData(StringUtils.getBooleanString(Boolean.TRUE));
            tableDataHour(totalPersonalInternalHours, false);
            tableDataHour(totalPersonalInternalHours / sysPar.getManYearHoursInternal().doubleValue(), false);
            endTableRow();
            startTableRow();
            tableData(StringUtils.getBooleanString(Boolean.FALSE));
            tableDataHour(totalPersonalExternalHours, false);
            tableDataHour(totalPersonalExternalHours / sysPar.getManYearHoursExternal().doubleValue(), false);
            endTableRow();
            startTableRow();
            // TODO innacurate because man-year may be different duration in
            // Role's compared to SystemParameter#manYear
            tableData(" ");
            tableDataHour(totalPersonalInternalHours + totalPersonalExternalHours, true);
            tableDataHour(totalPersonalInternalHours / sysPar.getManYearHoursInternal().doubleValue() + totalPersonalExternalHours
                / sysPar.getManYearHoursExternal().doubleValue(), true);
            endTableRow();
            endTable();
            endParagraph();

            header(2, getRsc("CIOtherPersonalCost"));
            startParagraph();
            startTable(1);
            startTableRow();
            tableHeader(ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text"));
            tableHeader(NlsUtils.formatMessage(ResourceManager.getResource(ReportTool.class, "CTLump"), ModelUtility.getTypeString(Role.class)) + " ["
                + getCostUnit() + "]");
            endTableRow();
            startTableRow();
            tableDataLinked(Calculator.PERSONAL_COST_INTERNAL, StringUtils.getBooleanString(Boolean.TRUE), false);
            tableDataAmount(totalPersonalInternalLumpSum, false);
            endTableRow();
            startTableRow();
            tableDataLinked(Calculator.PERSONAL_COST_EXTERNAL, StringUtils.getBooleanString(Boolean.FALSE), false);
            tableDataAmount(totalPersonalExternalLumpSum, false);
            endTableRow();
            startTableRow();
            tableData(" ");
            tableDataAmount(totalPersonalInternalLumpSum + totalPersonalExternalLumpSum, true);
            endTableRow();
            endTable();
            endParagraph();

            header(1, getRsc("CIPersonalTotal"));
            // only print Codes relevant for Personal ressources
            startParagraph();
            encodeCostBlock(null, element.getObjectServer().retrieveCodes(CostCause.class), Calculator.COST_CAUSE_UNDEFINED,
                ResourceManager.getResource(CostDriverDetailView.class, "TbcCosttype_text"));
            endParagraph();
            startParagraph();
            encodeCostBlock(null, element.getObjectServer().retrieveCodes(Role.class), Calculator.PERSONAL_ROLE_UNDEFINED,
                ResourceManager.getResource(RoleDetailView.class, "FrmWindow_text"));
            endParagraph();
            startParagraph();
            encodeCostBlock(null, element.getObjectServer().retrieveCodes(Activity.class), Calculator.PERSONAL_ACTIVITY_UNDEFINED,
                ResourceManager.getResourceAsNonLabeled(PersonalCostDetailView.class, "LblActivity_text"));
            endParagraph();
        }
        endBody();
        endHtml();
    }

    @Override
    protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, String title) throws IOException {
        if (codes.size() == 0) {
            // ignore
            return;
        }

        bold(getRsc("CIBy") + " <" + title + ">:");
        startTable(1);
        startTableRow();
        // print header
        tableHeader(title);
        tableHeader(getRsc("CIPersonalCostFirstYear"));
        endTableRow();

        // print data by CostType
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
        List<Double> totalPersonal = calculator.getTotalCosts(root, Calculator.KIND_PC);

        // ch.softenvironment.jomm.serialize.AttributeList attrs =
        // getAlignRight();

        String cellName = ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "TbcTotal_text");

        // the final summarization without Dependencies
        startTableRow();
        tableDataBold(cellName);
        tableDataAmount(Calculator.getValue(totalPersonal, 0), true, 2);
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
        // tableData(getCodeName(code));
        tableDataCode(code);

        List<Double> personalCosts = calculator.getCosts(root, Calculator.KIND_PC, code);
        double personalTotal = Calculator.getValue(personalCosts, Calculator.INDEX_TOTAL);
        tableDataAmount(personalTotal, false);

        endTableRow();
    }
}