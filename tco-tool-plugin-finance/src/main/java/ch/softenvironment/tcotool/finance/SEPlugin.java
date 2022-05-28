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

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import org.java.plugin.Plugin;
import org.tcotool.application.LauncherView;
import org.tcotool.core.runtime.ApplicationPlugin;
import org.tcotool.model.TcoObject;
import org.tcotool.pluginsupport.PluginUtility;

/**
 * Free <i>soft</i>Environment Plugins (might serve as sample). PLUGIN_ID = "ch.softenvironment.tcotool.finance";
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class SEPlugin extends Plugin implements org.tcotool.pluginsupport.Menu {

    protected static final String REPORT_HEADER = "ReportHeader.png";

    /**
     * Constructor.
     */
    public SEPlugin() {
        super();
    }

    /**
     * @see org.java.plugin.Plugin#doStart()
     */
    @Override
    protected void doStart() throws Exception {
    }

    /**
     * @see org.java.plugin.Plugin#doStop()
     */
    @Override
    protected void doStop() throws Exception {
    }

    /**
     * Menu was clicked -> generate the report.
     *
     * @see org.tcotool.pluginsupport.Menu#init(javax.swing.JComponent)
     */
    public void actionPerform(final JComponent item, final Object object) {
        ApplicationPlugin.showBusy(new Runnable() {
            @Override
            public void run() {
                try {
                    String actionCommand = ((JMenuItem) item).getActionCommand();
                    TcoObject base = (object == null) ? LauncherView.getInstance().getTcoPackageOrRoot(object) : (TcoObject) object;
                    if (actionCommand.equals("depreciation" /*
                     * @see plugin.xml=>id
                     */)) {
                        reportFinancialTotal(LauncherView.getInstance().getTcoPackageOrRoot(object));
                    } else if (actionCommand.equals("depreciationCostBlockTotal")) {
                        reportFinancialCostBlockTotal(LauncherView.getInstance().getTcoPackageOrRoot(object));
                    } else if (actionCommand.equals("depreciationCostBlockDetailed")) {
                        reportFinancialCostBlockDetailed(base);
                    } else if (actionCommand.equals("checkPersonalEffort")) {
                        checkPersonalEffort((object == null) ? LauncherView.getInstance().getTcoPackageOrRoot(object) : (TcoObject) object);
                    } else if (actionCommand.equals("budgetPlan")) {
                        reportBudgetPlan(base);
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
                .getDepreciationDuration(), PluginUtility.getClassLoader(this)));
    }

    /**
     * Depreciation-Report by cost-block.
     */
    private void reportFinancialCostBlockTotal(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            ReportDepreciationCostBlock.createBlockDepreciation(LauncherView.getInstance().getUtility(),
                LauncherView.getInstance().getTcoPackageOrRoot(root), LauncherView.getInstance().getUtility().getDepreciationDuration(),
                PluginUtility.getClassLoader(this)));
    }

    /**
     * Depreciation-Report by cost-block.
     */
    private void reportFinancialCostBlockDetailed(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            ReportDepreciationCostBlock.createBlockDetailedDepreciation(LauncherView.getInstance().getUtility(), root, LauncherView.getInstance()
                .getUtility().getDepreciationDuration(), PluginUtility.getClassLoader(this)));
    }

    private void reportBudgetPlan(TcoObject root) throws Exception {
        LauncherView.getInstance().addReport(
            new ReportBudgetPlan(LauncherView.getInstance().getUtility(), root, (int) LauncherView.getInstance().getUtility().getDepreciationDuration(),
                PluginUtility.getClassLoader(this) /* loader */));
    }

    /**
     * Check whether assigned personal resources are over-assigned or not.
     *
     * @param object
     * @throws Exception
     */
    private void checkPersonalEffort(TcoObject object) throws Exception {
        LauncherView.getInstance().addReport(new ReportPersonalEffort(LauncherView.getInstance().getUtility(), object, PluginUtility.getClassLoader(this)));
    }
}