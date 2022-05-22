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
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.serialize.AttributeList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tcotool.application.CatalogueDetailView;
import org.tcotool.application.CostDriverDetailView;
import org.tcotool.application.FactCostDetailView;
import org.tcotool.application.LauncherView;
import org.tcotool.application.PersonalCostDetailView;
import org.tcotool.application.RoleDetailView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.Cost;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.Occurance;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.ModelUtility;

/**
 * Reporting Utility to generate detailed Output of whole contents as is.
 * <p>
 * Design Pattern: Visitor
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class ReportComplete extends ReportTool {

    private ReportComplete(ModelUtility utility, final String title) {
        super(utility, title);
    }

    /**
     * Summarize each Service (including Sub-Packages).
     */
    public static ReportTool createComplete(ModelUtility utility, TcoPackage root, long tcoMaxUsage) throws Exception {
        ReportComplete tool = new ReportComplete(utility, ResourceManager.getResource(LauncherView.class, "MniReportComplete_text"));
        // tool.calculator = new Calculator(root, tcoMaxUsage);
        tool.encodeCostBlock(root, null, null, null);
        return tool;
    }

    /**
     * Visitor.
     *
     * @param level recursion deepness of packages
     * @param packageMultitude Multitude of owning package
     */
    private void walkPackageComplete(org.tcotool.model.TcoPackage object, String prefix, int level) throws java.io.IOException {
        encodeCompleteAbstract(object, prefix, level);
        startTreeHeader(level);
        int subChapter = 1;

        // 1) report services within Package
        Iterator<Service> services = object.getService().iterator();
        while (services.hasNext()) {
            Service service = services.next();
            encodeCompleteAbstract(service, prefix + "." + subChapter++, level + 1);
            walkServiceComplete(service);
        }

        // report sub-packages
        Iterator<TcoPackage> groups = object.getOwnedElement().iterator();
        while (groups.hasNext()) {
            walkPackageComplete(groups.next(), prefix + "." + subChapter++, level + 1);
        }

        endTreeHeader(level);
    }

    /**
     * Visitor.
     *
     * @param level recursion deepness of packages
     * @param packageMultitude Multitude of owning package
     */
    private void walkServiceComplete(org.tcotool.model.Service object) throws java.io.IOException {
        java.util.Iterator<CostDriver> iterator = object.getDriver().iterator();
        while (iterator.hasNext()) {
            org.tcotool.model.CostDriver driver = iterator.next();
            encodeCompleteAbstract(driver, "<irrelevant>"/* NON-NLS */, -1);

            List</* Fact */Cost> facts = new ArrayList</* Fact */Cost>();
            List</* Personal */Cost> personal = new ArrayList</* Personal */Cost>();
            Iterator<Cost> costs = driver.getCost().iterator();
            while (costs.hasNext()) {
                Cost cost = costs.next();
                if (cost instanceof FactCost) {
                    facts.add(cost);
                } else {
                    personal.add(cost);
                }
            }
            header(4, "a) " + getTcoObjectImage(utility, FactCost.class) + " " + ModelUtility.getTypeString(FactCost.class) + ":");
            encodeCompleteCosts(FactCost.class, facts);
            header(4, "b) " + getTcoObjectImage(utility, PersonalCost.class) + " " + ModelUtility.getTypeString(PersonalCost.class) + ":");
            encodeCompleteCosts(PersonalCost.class, personal);
        }
    }

    /**
     * Print the general Informations of given object (The "Abstract"). Visitor.
     *
     * @param object
     */
    private void encodeCompleteAbstract(TcoObject object, String prefix, int level) throws java.io.IOException {
        header(prefix, level, object);

        // print documentation
        /*
         * if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(object. getDocumentation())) { AttributeList attrs = new AttributeList();
         * attrs.add(ATTRIBUTE_BORDER, "0"); attrs.add(ATTRIBUTE_BACKGROUND_COLOR, "#C0C0C0"); startTable(attrs); startTableRow();
         * tableData(object.getDocumentation()); endTableRow(); endTable(); }
         */

        // print properties
        startTable(0);
        // ch.softenvironment.w3.AttributeList attrs = new
        // ch.softenvironment.w3.AttributeList(ATTRIBUTE_ALIGN, "right");
        startTableRow();
        tableData(ResourceManager.getResource(ServiceDetailView.class, "LblMultitude_text"), abstractCell);
        tableData("" + ModelUtility.getMultitudeFactor(object));
        endElement(/* tr */);
        if (object instanceof Service) {
            encodeCompleteEnum(ResourceManager.getResource(ServiceDetailView.class, "LblCategory_text"), ((Service) object).getCategory());
            encodeCompleteEnum(ResourceManager.getResource(ServiceDetailView.class, "LblResponsibility_text"), ((Service) object).getResponsibility());
            encodeCompleteEnum(ResourceManager.getResource(ServiceDetailView.class, "LblCostCentre_text"), ((Service) object).getCostCentre());
        } else if (object instanceof CostDriver) {
            if (((CostDriver) object).getOccurrance().size() > 0) {
                Iterator<Occurance> iterator = ((CostDriver) object).getOccurrance().iterator();
                while (iterator.hasNext()) {
                    Occurance occurance = iterator.next();
                    startTableRow();
                    tableData(getRsc("CIOccurance") + " " + occurance.getSite().getNameString() + ":", abstractCell);
                    tableData("" + occurance.getMultitude());
                    endTableRow();
                }
            }
            encodeCompleteEnum(ResourceManager.getResource(CostDriverDetailView.class, "LblProcess_text"), ((CostDriver) object).getProcess());
            encodeCompleteEnum(ResourceManager.getResource(CostDriverDetailView.class, "LblPhase_text"), ((CostDriver) object).getPhase());
            encodeCompleteEnum(ResourceManager.getResource(CostDriverDetailView.class, "LblCycle_text"), ((CostDriver) object).getCycle());
        } else if (object instanceof FactCost) {
            // TODO NYI
            // technical data
            // risk data
            // encodeCompleteEnum(ResourceManager.getResource(ServiceDetailView.class,
            // "LblCatalogue_text"), ((Service)object).getCatalogue());
        } else if (object instanceof PersonalCost) {
            // TODO NYI

        }
        if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(object.getDocumentation())) {
            startTableRow();
            tableData(ResourceManager.getResourceAsLabel(ServiceDetailView.class, "PnlNote_text"), abstractCell);
            tableData(object.getDocumentation());
            endTableRow();
        }
        endTable();
    }

    private void encodeCompleteCosts(Class<? extends Cost> costClass, List<Cost> costs) throws IOException {
        AttributeList fixTableWidth = getAttributeTableFixedWidth(100);
        Iterator<Cost> iterator = costs.iterator();
        if (iterator.hasNext()) {
            startTable(fixTableWidth);
            startTableRow();
            // print header
            tableHeader(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblName_text"), getAttributeWidth(Integer.valueOf(190)));
            tableHeader(ResourceManager.getResource(CostDriverDetailView.class, "TbcCosttype_text"), getAttributeWidth(Integer.valueOf(180)));
            tableHeader(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblMultitude_text"), getAttributeWidth(Integer.valueOf(60)));
            tableHeader(ResourceManager.getResource(ServiceDetailView.class, "PnlCost_text"), getAttributeWidth(Integer.valueOf(80)));
            tableHeader(ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblCurrency_text"), getAttributeWidth(Integer.valueOf(50)));
            if (costClass.equals(PersonalCost.class)) {
                tableHeader(ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text"), getAttributeWidth(Integer.valueOf(50)));
                tableHeader(ResourceManager.getResource(RoleDetailView.class, "FrmWindow_text"), getAttributeWidth(Integer.valueOf(50)));
                tableHeader("h", getAttributeWidth(Integer.valueOf(20))); // [hours]
                tableHeader(ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "LblHourlyRate_text"), getAttributeWidth(Integer.valueOf(70)));
                tableHeader(ResourceManager.getResourceAsNonLabeled(PersonalCostDetailView.class, "LblActivity_text"));
            } else if (costClass.equals(FactCost.class)) {
                tableHeader(ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblBaseOffset_text"), getAttributeWidth(Integer.valueOf(50)));
                tableHeader(ResourceManager.getResourceAsNonLabeled(CatalogueDetailView.class, "FrmWindow_text"), getAttributeWidth(Integer.valueOf(70)));
            }
            tableHeader(ResourceManager.getResource(ServiceDetailView.class, "PnlNote_text"));
            endTableRow();

            while (iterator.hasNext()) {
                // print data
                // ch.softenvironment.jomm.serialize.AttributeList attrs =
                // getAlignRight();
                Cost cost = iterator.next();
                startTableRow();
                tableDataLinked(cost, false); // tableData(cost.getName());
                tableData(getCodeName(cost.getCause()));
                tableData("" + (long) ModelUtility.getMultitudeFactor(cost), getAttributeAlignRight());
                tableDataAmount(cost.getAmount() == null ? 0.0 : cost.getAmount().doubleValue(), false);
                tableData(getCostUnit(cost.getCurrency()));
                if (costClass.equals(PersonalCost.class)) {
                    tableData(ch.softenvironment.util.StringUtils.getBooleanString(((PersonalCost) cost).getInternal()));
                    tableData(((PersonalCost) cost).getRole() == null ? "" : ((PersonalCost) cost).getRole().getNameString());
                    tableData(((PersonalCost) cost).getHours() == null ? "" : ((PersonalCost) cost).getHours().toString(), getAttributeAlignRight());
                    tableData(((PersonalCost) cost).getHourlyRate() == null ? "" : af.format(((PersonalCost) cost).getHourlyRate().doubleValue()),
                        getAttributeAlignRight());
                    tableData(((PersonalCost) cost).getActivity() == null ? "" : ((PersonalCost) cost).getActivity().getNameString());
                } else if (costClass.equals(FactCost.class)) {
                    tableData(cost.getBaseOffset() == null ? "" : cost.getBaseOffset().toString(), getAttributeAlignRight());
                    tableData(((FactCost) cost).getCatalogue() == null ? "" : ((FactCost) cost).getCatalogue().getNameString());
                }
                tableData(cost.getDocumentation());
                endTableRow();
            }
            endTable();
        }
    }

    private void encodeCompleteEnum(String property, DbCodeType enumeration) throws IOException {
        if (enumeration != null) {
            startTableRow();
            tableData(property, abstractCell);
            tableData(enumeration.getNameString());
            endTableRow();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tcotool.tools.ReportTool#encodeCostBlock(org.tcotool.model.TcoObject, java.util.List, java.lang.String, java.lang.String)
     */
    @Override
    protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, final String title) throws IOException {
        startHtml();
        startBody(null);

        // print the model
        walkPackageComplete((TcoPackage) root, "1", 1);

        endBody();
        endHtml();
    }
}