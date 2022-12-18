package org.tcotool.standard.charts;

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
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.UserException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.TableOrder;
import org.tcotool.application.ApplicationOptions;
import org.tcotool.application.FactCostDetailView;
import org.tcotool.application.PersonalCostDetailView;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.CalculatorTco;
import org.tcotool.tools.ModelUtility;

/**
 * Utility to for using JFreeChart to represent TCO-Tool data in Pie- or Bar-charts.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class ChartTool {

    private static final String TCO = "TCO "; // NLS
    protected Calculator calculator = null;
    // protected TcoPackage rootObject = null;
    private ModelUtility utility = null;
    private ApplicationOptions options = null;

    /**
     * @see ChartTool(TcoPackage, DbCodeType)
     */
    public ChartTool(ModelUtility utility, ApplicationOptions options) {
        this(utility, options, null);
    }

    /**
     * Create a ChartTool and calculate Costs for rootObject. If maskCode is set, all Service, CostDriver or Cost Objects are rejected if the code is not contained in hierarchy.
     *
     * @param utility
     * @param maskCode
     */
    public ChartTool(ModelUtility utility, ApplicationOptions options, DbCodeType maskCode) {
        super();
        this.utility = utility;
        this.options = options;
        calculator = new CalculatorTco(utility, (TcoPackage) utility.getRoot(), utility.getUsageDuration(), maskCode);
    }

    /**
     * Calculate Data-set for Total-Costs detailed in FactCost and PersonalCost.
     *
     * @param tco
     * @return
     */
    private CategoryDataset createTotalCostDataset(List<Double> tco) {
        // CostType (use different Colors)
        String personalCost = ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text");
        String factCost = ResourceManager.getResource(FactCostDetailView.class, "FrmWindow_text");
        String personalAndFactCost = ResourceManager.getResource(ChartTool.class, "CIPersonalFact");

        // grouped values (text per group)
        String total = ResourceManager.getResource(ChartTool.class, "CITotal");

        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        defaultcategorydataset.addValue(tco.get(0), personalCost, total);
        defaultcategorydataset.addValue(tco.get(1), factCost, total);
        for (int i = 2; i < tco.size(); i++) {
            defaultcategorydataset.addValue(tco.get(i), personalAndFactCost,
                TCO + (i - 1) + "." + ResourceManager.getResource(ChartTool.class, "CIYear"));
        }

        return defaultcategorydataset;
    }

    /**
     * Calculate Dataset for given codes.
     *
     * @param codes (List of DbCodeType)
     * @return
     */
    private CategoryDataset createTotalCodeDataset(java.util.List<? extends DbCodeType> codes) throws Exception {
        java.util.Map<Long, List<Double>> map = calculator.getCostBlock(null, codes);
        DefaultCategoryDataset set = new DefaultCategoryDataset();

        Iterator<? extends DbCodeType> it = codes.iterator();
        while (it.hasNext()) {
            DbCodeType code = it.next();
            java.util.List<Double> amounts = map.get(((DbCode) code).getId());
            for (int year = 1 /* Total makes no sense */; (year < amounts.size() && (year <= utility.getUsageDuration() / 12)); year++) {
                set.addValue(amounts.get(year), code.getNameString(), TCO + (year) + "." + ResourceManager.getResource(ChartTool.class, "CIYear"));
            }
        }

        return set;
    }

    /**
     * Create a CategoryPlot.
     *
     * @param categorydataset
     * @param title
     * @param currency
     * @return
     */
    private JFreeChart createBarChart(CategoryDataset categorydataset, String title, String currency) {
        JFreeChart jfreechart = ChartFactory.createBarChart(title,
            ch.softenvironment.client.ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblUsageDuration_text"), currency,
            categorydataset, PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        CategoryPlot categoryplot = jfreechart.getCategoryPlot();
        categoryplot.setBackgroundPaint(options.getChartBackground());
        categoryplot.setDomainGridlinePaint(options.getChartDomainGridLine());
        categoryplot.setDomainGridlinesVisible(true);
        categoryplot.setRangeGridlinePaint(options.getChartRangeGridLine());

        // jfreechart.getXYPlot().getDataRange(axis)fetdsetFixedRangeAxisSpace(new
        // AxisSpace());
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
        barrenderer.setDrawBarOutline(false);
        // TODO make colors configurable
        GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
        GradientPaint gradientpaint_7_ = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
        GradientPaint gradientpaint_8_ = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
        barrenderer.setSeriesPaint(0, gradientpaint);
        barrenderer.setSeriesPaint(1, gradientpaint_7_);
        barrenderer.setSeriesPaint(2, gradientpaint_8_);
        CategoryAxis categoryaxis = categoryplot.getDomainAxis();
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.5235987755982988));
        return jfreechart;
    }

    /**
     * Create a TCO Bar-Chart over all given instances of dbCodeType in detail over TCO-Duration.
     *
     * @param dbCodeType DbCodeType
     * @return
     */
    public JPanel createTcoBarChart(Class<? extends DbCodeType> dbCodeType) throws Exception {
        List<? extends DbCodeType> codes = ((TcoPackage) utility.getRoot()).getObjectServer().retrieveCodes(dbCodeType);
        if ((codes == null) || (codes.size() == 0)) {
            throw new UserException(ResourceManager.getResource(ChartTool.class, "CWNoCode_text"), ResourceManager.getResource(ChartTool.class,
                "CWNoCode_title") + " " + ModelUtility.getTypeString(dbCodeType));
        }
        CategoryDataset categorydataset = createTotalCodeDataset(codes);
        JFreeChart jfreechart = createBarChart(categorydataset,
            NlsUtils.formatMessage(ResourceManager.getResource(ChartTool.class, "CTCostByCodeType"), ModelUtility.getTypeString(dbCodeType)), utility
                .getSystemParameter().getDefaultCurrency().getNameString());
        ChartPanel chart = new ChartPanel(jfreechart);
        chart.setPreferredSize(new Dimension(500, 270));
        return chart;
    }

    /**
     * Create a Bar-Chart of total TCO-Cost over TCO-duration split in PersonalCost and FactCost.
     *
     * @return
     */
    public JPanel createTcoBarChart() throws Exception {
        CategoryDataset categorydataset = createTotalCostDataset(calculator.getCostBlock(null));
        JFreeChart jfreechart = createBarChart(categorydataset, ResourceManager.getResource(ChartTool.class, "CTCostTotal"), utility.getSystemParameter()
            .getDefaultCurrency().getNameString());
        ChartPanel chart = new ChartPanel(jfreechart);
        chart.setPreferredSize(new Dimension(500, 270));
        return chart;
    }

    /**
     * Create a Pie-Chart of total TCO-Cost over TCO-duration.
     *
     * @param rootObject
     * @return
     */
    public JPanel createTcoPieChart(Class<? extends DbCodeType> dbCodeType) throws Exception {
        List<? extends DbCodeType> codes = ((TcoPackage) utility.getRoot()).getObjectServer().retrieveCodes(dbCodeType);
        if ((codes == null) || (codes.size() == 0)) {
            throw new UserException(ResourceManager.getResource(ChartTool.class, "CWNoCode_text"), ResourceManager.getResource(ChartTool.class,
                "CWNoCode_title") + " " + ModelUtility.getTypeString(dbCodeType));
        }
        CategoryDataset categorydataset = createTotalCodeDataset(codes);
        JFreeChart jfreechart = createPieChart(categorydataset, dbCodeType);
        ChartPanel chartpanel = new ChartPanel(jfreechart);

        chartpanel.setPreferredSize(new Dimension(500, 270));
        return chartpanel;
    }

    /**
     * Create a MultiplePiePlot.
     *
     * @param categorydataset
     * @return
     */
    private JFreeChart createPieChart(CategoryDataset categorydataset, Class<? extends DbCodeType> dbCodeType) {
        JFreeChart jfreechart = ChartFactory.createMultiplePieChart(
            NlsUtils.formatMessage(ResourceManager.getResource(ChartTool.class, "CTCostByCodeType"), ModelUtility.getTypeString(dbCodeType)),
            categorydataset, TableOrder.BY_COLUMN, true, true, false);
        MultiplePiePlot multiplepieplot = (MultiplePiePlot) jfreechart.getPlot();
        // multiplepieplot.setBackgroundPaint(Color.white);
        multiplepieplot.setOutlineStroke(new BasicStroke(1.0F));
        JFreeChart pieChart = multiplepieplot.getPieChart();
        PiePlot pieplot = (PiePlot) pieChart.getPlot();
        // pieplot.setBackgroundPaint(null);
        // pieplot.setOutlineStroke(null);
        String currency = "";
        try {
            currency = utility.getSystemParameter().getDefaultCurrency().getNameString();
        } catch (Exception ex) {
            // ignore: don't show currency
            log.warn("SystemParameter fault <DefaultCurrency>", ex);
        }
        // pieplot.setLabelGenerator(new
        // StandardPieSectionLabelGenerator("{0}"));
        pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator// StandardPieItemLabelGenerator
            // (in
            // JFreeCharts
            // V0.9.6)
            (/* "{0} + */" {1}" + currency + " [{2}]", AmountFormat.getAmountInstance(options.getPlattformLocale()), NumberFormat.getPercentInstance())); // {0}=Text
        // corresponding
        // to color; {1}=partial
        // value; {2}=partial %
        pieplot.setLabelFont(new java.awt.Font("SansSerif", 0, 7));

        // only show existing parts
        pieplot.setIgnoreNullValues(true);
        pieplot.setIgnoreZeroValues(true);

        // set width of label and circle
        /*
         * pieplot.setMaximumLabelWidth(0.25); // ==> buggy in V1.0.9 pieplot.setInteriorGap(0.07); // influences circle size pieplot.setLabelGap(0.07);
         */
        pieplot.setSimpleLabels(true);

        return jfreechart;
    }
}