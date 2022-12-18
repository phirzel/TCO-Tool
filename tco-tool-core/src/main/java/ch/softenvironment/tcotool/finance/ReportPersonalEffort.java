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
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.serialize.AttributeList;
import ch.softenvironment.tcotool.tco.TCOPlugin;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.StringUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.tcotool.application.LauncherView;
import org.tcotool.application.PersonalCostDetailView;
import org.tcotool.application.RoleDetailView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Role;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;
import org.tcotool.tools.TreeTool;
import org.tcotool.tools.TreeToolListener;

/**
 * Utility to report Personal-Efforts as Plugin for TCO-Tool in HTML or CSV.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class ReportPersonalEffort extends ReportTool implements TreeToolListener {

    private static final String KEY_INTERN_HOURS = "INTERN_H";
    private static final String KEY_EXTERN_HOURS = "EXTERN_H";

    private final Map<Object, Double> availablity = new HashMap<>();

    private double lumpInternalNoHours = 0.0;
    private double lumpExternalNoHours = 0.0;
    private double lumpInternalWithHours = 0.0;
    private double lumpExternalWithHours = 0.0;

    /**
     *
     */
    public ReportPersonalEffort(ModelUtility utility, TcoObject root) throws IOException {
        super(utility, ResourceManager.getResource(FinancePlugin.class, "MniCheckPersonalEffort_text"));

        calculator = new CalculatorTco(LauncherView.getInstance().getUtility());
        TreeTool treeTool = new TreeTool(this);
        treeTool.walkTree(root);

        encodeCostBlock(root, root.getObjectServer().retrieveCodes(Role.class), Calculator.PERSONAL_ROLE_UNDEFINED, null);
    }

    @Override
    protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, String codeUndefined, String title) throws IOException {
        double sumAvailable = 0.0;
        double sumBooked = 0.0;
        double tmp;

        startHtml();
        startBody(null);
        header(1, ResourceManager.getResource(ReportPersonalEffort.class, "CTPersonalEffort") + ": " + root.getName());
        header(2, ResourceManager.getResource(ReportTool.class, "CIBy") + " " + ModelUtility.getTypeString(Role.class));
        startParagraph();

        AttributeList right = getAttributeAlignRight();
        startTable(1);

        // header
        startTableRow();
        tableHeader(ModelUtility.getTypeString(Role.class));
        tableHeader(ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text"));
        tableHeader(ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblYearlyHours_text") + " [h]");
        tableHeader(ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblEmploymentPercentageAvailable_text") + " [%]");
        tableHeader(ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblEmploymentPercentageAvailable_text") + " [h]");
        tableHeader(ResourceManager.getResource(ReportPersonalEffort.class, "CIBooked") + " [%]");
        tableHeader(ResourceManager.getResource(ReportPersonalEffort.class, "CIBooked") + " [h]");
        tableHeader(ResourceManager.getResource(ReportPersonalEffort.class, "CIDifference") + " [%]");
        tableHeader(ResourceManager.getResource(ReportPersonalEffort.class, "CIDifference") + " [h] *)");
        endTableRow();

        // hours according to Role's
        double sumPercentage = 0.0;
        double bookedPercentage = 0.0;
        for (DbCodeType code : codes) {
            Role role = (Role) code;
            startTableRow();
            tableDataLinked(role, true); // role.getNameString());
            tableData(StringUtils.getBooleanString(role.getInternal()));
            tableDataHour(role.getYearlyHours(), false);
            sumPercentage = sumPercentage + role.getEmploymentPercentageAvailable().doubleValue();
            tableDataHour(role.getEmploymentPercentageAvailable(), false);
            double hoursAvailable = 0.0;
            if ((role.getYearlyHours() != null) && (role.getYearlyHours().doubleValue() > 0.0)) {
                hoursAvailable = Calculator.calculateAvailableHours(role);
                tableDataHour(hoursAvailable, false);
                sumAvailable += hoursAvailable;
            } else {
                tableData(ResourceManager.getResource(ReportTool.class, "CINotAvailable"), right);
            }
            double hoursBooked = 0.0;
            if (availablity.containsKey(role)) {
                hoursBooked = availablity.get(role);
                sumBooked += hoursBooked;
            }
            tmp = 100.0 / hoursAvailable * hoursBooked;
            bookedPercentage = bookedPercentage + tmp;
            tableDataHour(tmp, false);
            tableDataHour(hoursBooked, false);
            double diff = hoursAvailable - hoursBooked;
            AttributeList attr = getAttributeAlignRight();
            if (diff < 0.0) {
                // red
                attr.add("style", "background-color:#FF0080");
            } else {
                // green
                attr.add("style", "background-color:#00FF40");
            }
            tableDataHour(100.0 - tmp, false);
            tableData(hourFormat.format(diff), attr);
            endTableRow();
        }

        // total
        startTableRow();
        tableDataBold(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "TbcTotal_text"));
        tableDataBold("");
        tableDataBold("");
        tableDataHour(sumPercentage, true);
        tableDataHour(sumAvailable, true);
        tableDataBold(ResourceManager.getResource(ReportTool.class, "CINotAvailable")); // tableDataHour(bookedPercentage,
        // true);
        tableDataHour(sumBooked, true);
        tableDataBold(ResourceManager.getResource(ReportTool.class, "CINotAvailable")); // tableDataHour(sumPercentage
        // -
        // bookedPercentage,
        // true);
        tableDataHour(sumAvailable - sumBooked, true);
        endTableRow();
        endTable();
        nativeContent("*) <font color='#FF0080'>" + ResourceManager.getResource(ReportPersonalEffort.class, "CIOverAllocated")
            + "</font>; <font color='#00FF40'>" + ResourceManager.getResource(ReportPersonalEffort.class, "CINonAllocated") + "</font>"); // ignore
        // for
        // CSV
        endParagraph();

        header(2, NlsUtils.formatMessage(ResourceManager.getResource(ReportTool.class, "CTLump"), ModelUtility.getTypeString(Role.class)));
        startParagraph();
        // lump figures
        // hours by lump
        /*
         * List hours = calculator.getPersonalHours(null); String lumpInternal = ""; if (Calculator.getValue(hours, 2) > 0.0) { lumpInternal = "*) "; } String
         * lumpExternal = ""; if (Calculator.getValue(hours,as 3) > 0.0) { lumpExternal = "*) "; }
         */
        try {
            startTable(1);
            startTableRow();
            tableHeader(ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text"));
            tableHeader(ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblYearlyHours_text") + " [h]");
            tableHeader(ResourceManager.getResource(ReportPersonalEffort.class, "CIBooked") + " [h]");
            tableHeader(ResourceManager.getResource(ReportPersonalEffort.class, "CILumpCostsWithHours") + " [" + getCostUnit() + "]");
            tableHeader(ResourceManager.getResource(ReportPersonalEffort.class, "CILumpCostsNoHours") + " [" + getCostUnit() + "]");
            endTableRow();

            startTableRow();
            tableDataLinked(Calculator.PERSONAL_COST_INTERNAL, StringUtils.getBooleanString(Boolean.TRUE), false);
            tableDataHour(LauncherView.getInstance().getUtility().getSystemParameter().getManYearHoursInternal(), false);
            // tmp = Calculator.getValue(hours, 0);
            tmp = availablity.getOrDefault(KEY_INTERN_HOURS, 0.0);
            sumBooked = tmp; // hour Total
            tableDataHour(tmp, false);
            tableDataAmount(lumpInternalWithHours, false);
            // TODO Link for search
            tableDataAmount(lumpInternalNoHours, false);
            endTableRow();

            startTableRow();
            tableDataLinked(Calculator.PERSONAL_COST_EXTERNAL, StringUtils.getBooleanString(Boolean.FALSE), false);
            tableDataHour(LauncherView.getInstance().getUtility().getSystemParameter().getManYearHoursExternal(), false);
            // tmp = Calculator.getValue(hours, 1);
            tmp = availablity.getOrDefault(KEY_EXTERN_HOURS, 0.0);
            sumBooked += tmp; // hour Total
            tableDataHour(tmp, false);
            tableDataAmount(lumpExternalWithHours, false);
            // TODO Link for search
            tableDataAmount(lumpExternalNoHours, false);
            endTableRow();
            startTableRow();
            tableDataBold(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "TbcTotal_text"));
            tableDataBold("");
            tableDataHour(sumBooked, true);
            tableDataAmount(lumpInternalWithHours + lumpExternalWithHours, true);
            tableDataAmount(lumpInternalNoHours + lumpExternalNoHours, true);
            endTableRow();
            endTable();
        } catch (Exception e) {
            log.warn("", e);
        }
        endParagraph();

        endElement(/* body */);
        endElement(/* html */);
    }

    @Override
    public void startBody(AttributeList list) throws IOException {
        super.startBody(list);
        java.net.URL url = TCOPlugin.getHeader();
        image(url, "", 0);
    }

    /*
     * @see org.tcotool.tools.TreeToolListener#treatTcoPackage(org.tcotool.model.TcoPackage)
     */
    @Override
    public void treatTcoPackage(TcoPackage group) {
    }

    /*
     * @see org.tcotool.tools.TreeToolListener#treatService(org.tcotool.model.Service)
     */
    @Override
    public void treatService(Service service) {
    }

    /*
     * @see org.tcotool.tools.TreeToolListener#treatCostDriver(org.tcotool.model.CostDriver)
     */
    @Override
    public void treatCostDriver(CostDriver driver) {
    }

    /*
     * @see org.tcotool.tools.TreeToolListener#treatFactCost(org.tcotool.model.FactCost)
     */
    @Override
    public void treatFactCost(FactCost cost) {
    }

    /*
     * @see org.tcotool.tools.TreeToolListener#treatPersonalCost(org.tcotool.model.PersonalCost)
     */
    @Override
    public void treatPersonalCost(PersonalCost cost) {
        double factor = LauncherView.getInstance().getUtility().getMultitudeFactor(cost, true);
        double hours = Calculator.calculateHours(cost);
        Object roleKey = cost.getRole();
        if (cost.getRole() == null) {
            // calc hours and costs for all non-Role's
            if (hours == Calculator.PERSONAL_HOURS_UNDEFINED) {
                // consider as Lump-Cost (without known hours)
                if (cost.getInternal()) {
                    lumpInternalNoHours += (cost.getAmount() == null ? 0.0 : cost.getAmount()) * factor;
                } else {
                    lumpExternalNoHours += (cost.getAmount() == null ? 0.0 : cost.getAmount()) * factor;
                }
            } else {
                // consider as Lump-Cost (without known hours)
                if (cost.getInternal()) {
                    roleKey = KEY_INTERN_HOURS;
                    lumpInternalWithHours += (cost.getAmount() == null ? 0.0 : cost.getAmount()) * factor;
                } else {
                    roleKey = KEY_EXTERN_HOURS;
                    lumpExternalWithHours += (cost.getAmount() == null ? 0.0 : cost.getAmount()) * factor;
                }
            }
        }

        if (!availablity.containsKey(roleKey)) {
            availablity.put(roleKey, 0.0);
        }
        double value = availablity.get(roleKey);
        availablity.put(roleKey, value + hours * factor);
    }
}