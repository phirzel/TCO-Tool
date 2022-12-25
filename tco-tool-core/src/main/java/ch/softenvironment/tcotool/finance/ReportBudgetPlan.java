package ch.softenvironment.tcotool.finance;

/*
 * Copyright (C) 2004-2006 Peter Hirzel softEnvironment (http://www.softenvironment.ch).
 * All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.serialize.AttributeList;
import ch.softenvironment.tcotool.tco.TCOPlugin;
import org.tcotool.application.FactCostDetailView;
import org.tcotool.application.LauncherView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.*;
import org.tcotool.standard.report.ReportTool;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.ModelUtility;
import org.tcotool.tools.TreeTool;
import org.tcotool.tools.TreeToolListener;

import java.io.IOException;
import java.util.List;

/**
 * Utility to report a financial Budget-Plan as Plugin for TCO-Tool in HTML or CSV. Design Pattern: Visitor
 *
 * @author Peter Hirzel
 */
class ReportBudgetPlan extends ReportTool implements TreeToolListener {

    private final int duration;
    private final double[] pCost;
    private final double[] fCost;

    /**
     * Summarize all Depreciation-Cost within given object and sub-packages.
     */
    public ReportBudgetPlan(ModelUtility utility, TcoObject root, int durationMonths) throws IOException {
        super(utility, ResourceManager.getResource(FinancePlugin.class, "MniReportBudgetPlan_text"));

        duration = calcYearlyDuration(durationMonths);
        pCost = new double[duration];
        fCost = new double[duration];
        for (int i = 0; i < duration; i++) {
            pCost[i] = 0.0;
            fCost[i] = 0.0;
        }

        TreeTool tool = new TreeTool(this);
        tool.walkTree(root);

        encodeCostBlock(root, root.getObjectServer().retrieveCodes(Role.class), Calculator.PERSONAL_ROLE_UNDEFINED, null);
    }

    @Override
    protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, String title) throws IOException {
        startHtml();
        startBody(null);
        header(1, ResourceManager.getResource(FinancePlugin.class, "MniReportBudgetPlan_text") + ": " + root.getName());
        startParagraph();
        simpleContent(ResourceManager.getResource(ReportBudgetPlan.class, "CIAspects"));
        getCsvWriter().cell(ResourceManager.getResource(ReportBudgetPlan.class, "CIAspects"));
        getCsvWriter().newline();
        startUnorderedList();
        listItem(ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblDepreciationDuration_text"));
        getCsvWriter().cell("  " + ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblDepreciationDuration_text"));
        getCsvWriter().newline();
        listItem(ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblRepeatable_text"));
        getCsvWriter().cell("  " + ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblRepeatable_text"));
        getCsvWriter().newline();
        endUnorderedList();

        // AttributeList right = getAlignRight();
        startTable(1);

        // header
        startTableRow();
        tableHeader("");
        for (long i = 0; i < duration; i++) {
            tableHeader(ResourceManager.getResource(ReportBudgetPlan.class, "CIInvestment") + " " + (i + 1) + ". " + getRsc("CIYear"));
        }
        endTableRow();

        // salaries to pay
        startTableRow();
        tableDataBold(ModelUtility.getTypeString(PersonalCost.class));
        for (int i = 0; i < duration; i++) {
            tableDataAmount(pCost[i], false);
        }
        endTableRow();

        // investments
        startTableRow();
        tableDataBold(ModelUtility.getTypeString(FactCost.class));
        for (int i = 0; i < duration; i++) {
            tableDataAmount(fCost[i], false);
        }
        endTableRow();

        // Total
        startTableRow();
        tableDataBold(ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "TbcTotal_text"));
        for (int i = 0; i < duration; i++) {
            tableDataAmount(fCost[i] + pCost[i], true);
        }
        endTableRow();

        endTable();
        endParagraph();
        endBody();
        endHtml();
    }

    @Override
    public void startBody(AttributeList list) throws IOException {
        super.startBody(list);
        image(TCOPlugin.getHeader(), "", 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tcotool.tools.TreeToolListener#treatTcoPackage(org.tcotool.model. TcoPackage)
     */
    @Override
    public void treatTcoPackage(TcoPackage group) {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tcotool.tools.TreeToolListener#treatService(org.tcotool.model.Service )
     */
    @Override
    public void treatService(Service service) {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tcotool.tools.TreeToolListener#treatCostDriver(org.tcotool.model. CostDriver)
     */
    @Override
    public void treatCostDriver(CostDriver driver) {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tcotool.tools.TreeToolListener#treatFactCost(org.tcotool.model.FactCost )
     */
    @Override
    public void treatFactCost(FactCost cost) {
        double amount = cost.getAmount() * LauncherView.getInstance().getUtility().getMultitudeFactor(cost, true);
        fCost[0] += amount;
        if (cost.getRepeatable()) {
            // TODO BUG: for fractional years offset is rounded up in an inaccurate way (2*1.5=3 but this results in 2*2=4)
            int offset = calcYearlyDuration(cost.getDepreciationDuration().intValue());
            for (int j = offset; j < duration; j += offset) {
                fCost[j] += amount;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tcotool.tools.TreeToolListener#treatPersonalCost(org.tcotool.model .PersonalCost)
     */
    @Override
    public void treatPersonalCost(PersonalCost cost) {
        double amount = cost.getAmount() * LauncherView.getInstance().getUtility().getMultitudeFactor(cost, true);
        pCost[0] += amount;
        if (cost.getRepeatable()) {
            int offset = 1;
            for (int j = offset; j < duration; j += offset) {
                pCost[j] += amount;
            }
        }
    }

    /**
     * Calculate years out of Months. For Fractional year greater 12 months the next year is considered.
     *
     * @param durationMonths
     * @return
     */
    private int calcYearlyDuration(int durationMonths) {
        if (durationMonths < 12) {
            return 1;
        } else {
            int durationYears = durationMonths / 12;
            if (durationMonths % 12 > 0) {
                durationYears++;
            }
            return durationYears;
        }
    }
}