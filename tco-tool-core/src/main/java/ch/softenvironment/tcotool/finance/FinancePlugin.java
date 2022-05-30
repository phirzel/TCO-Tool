package ch.softenvironment.tcotool.finance;

/*
 * Copyright (C) 2006 Peter Hirzel softEnvironment (http://www.softenvironment.ch).
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
import ch.softenvironment.util.DeveloperException;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import org.tcotool.application.LauncherView;
import org.tcotool.model.TcoObject;
import org.tcotool.pluginsupport.ApplicationPlugin;
import org.tcotool.pluginsupport.MenuExtension;

/**
 * Free <i>soft</i>Environment Plugins (might serve as sample). PLUGIN_ID = "ch.softenvironment.tcotool.finance";
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class FinancePlugin implements org.tcotool.pluginsupport.Plugin, org.tcotool.pluginsupport.Menu {

    /**
     * Constructor.
     */
    public FinancePlugin() {
        super();
    }

    //
    @Override
    public void setUp() {
        /*
        TODO see runtime.ApplicationPlugin#loadPlugins for checkNavTree -> add for main Menu and NavTree Menu
        loadPlugins("ReportFinance", false);
//      loadPlugins("ReportFinance", true);
         */

        /*
        <extension plugin-id="org.tcotool.core.runtime" point-id="ReportFinance" id="depreciation">
        <parameter id="class" value="ch.softenvironment.tcotool.finance.FinancePlugin"/>
        <parameter id="textKey" value="MniReportFinancialTotal_text"/>
        <parameter id="icon" value="MenuIcon.png"/>
        <parameter id="hasLeadingSeparator" value="false"/>
        <parameter id="isGroupSpecific" value="true"/>
        </extension>
         */
        MenuExtension extension = new MenuExtension();
        extension.setId("depreciation");
        extension.setText(ResourceManager.getResource(FinancePlugin.class, "MniReportFinancialTotal_text"));
        loadPlugins("ReportFinance", false, false, true, extension);

        /*
        <extension plugin-id="org.tcotool.core.runtime" point-id="ReportFinance"
               id="depreciationCostBlockTotal">
        <parameter id="class" value="ch.softenvironment.tcotool.finance.FinancePlugin"/>
        <parameter id="textKey" value="MniReportFinancialCostBlockTotal_text"/>
        <parameter id="icon" value="MenuIcon.png"/>
        <parameter id="hasLeadingSeparator" value="false"/>
        <parameter id="isGroupSpecific" value="true"/>
        </extension>
         */
        extension = new MenuExtension();
        extension.setId("depreciationCostBlockTotal");
        extension.setText(ResourceManager.getResource(FinancePlugin.class, "MniReportFinancialCostBlockTotal_text"));
        loadPlugins("ReportFinance", true, false, true, extension);

        /*
        <extension plugin-id="org.tcotool.core.runtime" point-id="ReportFinance"
               id="depreciationCostBlockDetailed">
        <parameter id="class" value="ch.softenvironment.tcotool.finance.FinancePlugin"/>
        <parameter id="textKey" value="MniReportFinancialCostBlockDetailed_text"/>
        <parameter id="icon" value="MenuIcon.png"/>
        <parameter id="hasLeadingSeparator" value="false"/>
        <parameter id="isGroupSpecific" value="false"/>
        </extension>
         */
        extension = new MenuExtension();
        extension.setId("depreciationCostBlockDetailed");
        extension.setText(ResourceManager.getResource(FinancePlugin.class, "MniReportFinancialCostBlockDetailed_text"));
        loadPlugins("ReportFinance", false, true, false, extension);

        /*
        <extension plugin-id="org.tcotool.core.runtime" point-id="ReportFinance"
               id="checkPersonalEffort">
        <parameter id="class" value="ch.softenvironment.tcotool.finance.FinancePlugin"/>
        <parameter id="textKey" value="MniCheckPersonalEffort_text"/>
        <parameter id="descriptionKey" value="MniCheckPersonalEffort_toolTipText"/>
        <parameter id="icon" value="MenuIcon.png"/>
        <parameter id="hasLeadingSeparator" value="true"/>
        <parameter id="isGroupSpecific" value="false"/>
        </extension>
         */
        extension = new MenuExtension();
        extension.setId("checkPersonalEffort");
        extension.setText(ResourceManager.getResource(FinancePlugin.class, "MniCheckPersonalEffort_text"));
        extension.setToolTipText(ResourceManager.getResource(FinancePlugin.class, "MniCheckPersonalEffort_toolTipText"));
        loadPlugins("ReportFinance", true, false, false, extension);

        /*
        <extension plugin-id="org.tcotool.core.runtime" point-id="ReportFinance" id="budgetPlan">
        <parameter id="class" value="ch.softenvironment.tcotool.finance.FinancePlugin"/>
        <parameter id="textKey" value="MniReportBudgetPlan_text"/>
        <parameter id="descriptionKey" value="MniReportBudgetPlan_toolTipText"/>
        <parameter id="icon" value="MenuIcon.png"/>
        <parameter id="hasLeadingSeparator" value="false"/>
        <parameter id="isGroupSpecific" value="false"/>
        </extension>
         */
        extension = new MenuExtension();
        extension.setId("budgetPlan");
        extension.setText(ResourceManager.getResource(FinancePlugin.class, "MniReportBudgetPlan_text"));
        extension.setToolTipText(ResourceManager.getResource(FinancePlugin.class, "MniReportBudgetPlan_toolTipText"));
        loadPlugins("ReportFinance", false, false, false, extension);
    }

    /**
     * Menu was clicked -> generate the report.
     */
    @Override
    public void actionPerform(final JComponent item, final Object object) {
        ApplicationPlugin.showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    String actionCommand = ((JMenuItem) item).getActionCommand();
                    TcoObject base = (object == null) ? LauncherView.getInstance().getTcoPackageOrRoot(object) : (TcoObject) object;
                    switch (actionCommand) {
                        case "depreciation":
                            reportFinancialTotal(LauncherView.getInstance().getTcoPackageOrRoot(object));
                            break;
                        case "depreciationCostBlockTotal":
                            reportFinancialCostBlockTotal(LauncherView.getInstance().getTcoPackageOrRoot(object));
                            break;
                        case "depreciationCostBlockDetailed":
                            reportFinancialCostBlockDetailed(base);
                            break;
                        case "checkPersonalEffort":
                            checkPersonalEffort((object == null) ? LauncherView.getInstance().getTcoPackageOrRoot(object) : (TcoObject) object);
                            break;
                        case "budgetPlan":
                            reportBudgetPlan(base);
                            break;
                        default:
                            throw new DeveloperException("nyi: actionCommand=" + actionCommand);
                    }
                } catch (Exception e) {
                    LauncherView.getInstance().handleException(e);
                }
            }
        });
    }

    /**
     * Depreciation-Report.
     */
    private void reportFinancialTotal(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            ReportDepreciation.createBlockDepreciation(LauncherView.getInstance().getUtility(), root, LauncherView.getInstance().getUtility()
                .getDepreciationDuration()));
    }

    /**
     * Depreciation-Report by cost-block.
     */
    private void reportFinancialCostBlockTotal(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            ReportDepreciationCostBlock.createBlockDepreciation(LauncherView.getInstance().getUtility(),
                LauncherView.getInstance().getTcoPackageOrRoot(root), LauncherView.getInstance().getUtility().getDepreciationDuration()));
    }

    /**
     * Depreciation-Report by cost-block.
     */
    private void reportFinancialCostBlockDetailed(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            ReportDepreciationCostBlock.createBlockDetailedDepreciation(LauncherView.getInstance().getUtility(), root, LauncherView.getInstance()
                .getUtility().getDepreciationDuration()));
    }

    private void reportBudgetPlan(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            new ReportBudgetPlan(LauncherView.getInstance().getUtility(), root, (int) LauncherView.getInstance().getUtility().getDepreciationDuration()));
    }

    /**
     * Check whether assigned personal resources are over-assigned or not.
     *
     * @param object
     * @throws Exception
     */
    private void checkPersonalEffort(TcoObject object) throws Exception {
        LauncherView.getInstance().addReport(new ReportPersonalEffort(LauncherView.getInstance().getUtility(), object));
    }
}