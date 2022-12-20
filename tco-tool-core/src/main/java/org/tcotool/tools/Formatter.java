package org.tcotool.tools;

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
import ch.softenvironment.math.MathUtils;
import lombok.extern.slf4j.Slf4j;
import org.tcotool.application.FactCostDetailView;
import org.tcotool.model.*;

/**
 * Format-Tool for Table's in *DetailView.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class Formatter implements ch.softenvironment.util.Evaluator {

	public static final String COSTDRIVER_FACT_COST = "_factCost";
	public static final String COSTDRIVER_PERSONAL_COST = "_personalCost";
	public static final String COSTDRIVER_TOTAL = "_costdriverTotal";
	public static final String COSTDRIVER_CURRENCY = "_costdriverCurrency";
	public static final String COSTDRIVER_PERCENTAGE = "_costdriverPercentage";
	public static final String COST_TOTAL = "_costTotal";
	public static final String COST_TYPE = "_costType";
	public static final String COST_PERCENTAGE = "_costPercentage";
	public static final String SUPPLIER_NAME = "_supplierName";
	public static final String SUPPLIER_TYPE = "_supplierType";
	public static final String SUPPLIER_COST = "_supplierCost";
	public static final String SUPPLIER_CLIENT_COST = "_supplierClientCost";

	private final ModelUtility utility;
	private double totalCostDriver = 0.0;
	private Double costDriverMultitude = null;
	private double totalCost = 0.0;

	protected static final Double NOT_AVAILABLE = Double.valueOf(-1.0);

	/**
	 * CostDriverFormatter constructor comment.
	 */
	public Formatter(ModelUtility utility) {
		super();
		this.utility = utility;
	}

	/**
	 * Visitor-Pattern. The given owner's property will be formatted for Table-Cell displayment and returned.
	 */
	@Override
	public Object evaluate(Object owner, final String property) {
		if (owner instanceof org.tcotool.model.CostDriver) {
			// @see ServiceDetailView#initializeView()
			// TUNE recalc
			double[] sums = Calculator.calculate((CostDriver) owner);
			double total = sums[0] + sums[1];
			double factor = ModelUtility.getMultitudeFactor((CostDriver) owner);
			if (property.equals(COSTDRIVER_FACT_COST)) {
				if (factor > 0.0) {
                    return Double.valueOf(sums[0] / factor);
				} else {
                    return Double.valueOf(0.0);
				}
			} else if (property.equals(COSTDRIVER_PERSONAL_COST)) {
				if (factor > 0.0) {
                    return Double.valueOf(sums[1] / factor);
				} else {
                    return Double.valueOf(0.0);
				}
			} else if (property.equals(COSTDRIVER_TOTAL)) {
                return Double.valueOf(total);
			} else if (property.equals(COSTDRIVER_CURRENCY)) {
				try {
					return utility.getSystemParameter().getDefaultCurrency().getNameString(ModelUtility.getCodeTypeLocale());
				} catch (Exception e) {
					log.warn("IGNORE: SystemParameters.defaultCurrency reference");
					return "<" + ResourceManager.getResource(FactCostDetailView.class, "LblCurrency_text") + ">";
				}
			} else if (property.equals(COSTDRIVER_PERCENTAGE)) {
				if (totalCostDriver == 0.0) {
					return NOT_AVAILABLE;
				} else {
                    return Double.valueOf(MathUtils.fix(100.0 / totalCostDriver * total, 1));
				}
			}
		} else if (owner instanceof org.tcotool.model.Cost) {
			Cost cost = (Cost) owner;
			double total = cost.getAmount() == null ? 0.0 : cost.getAmount().doubleValue();
			if (property.equals(COST_TOTAL)) {
                return Double.valueOf(ModelUtility.getMultitudeFactor(cost) * total);
			} else if (property.equals(COST_TYPE)) {
				if (cost instanceof FactCost) {
					return ResourceManager.getResource(Formatter.class, "CIFactCostAbbreviation");
				} else {
					return ResourceManager.getResource(Formatter.class, "CIPersonalCostAbbreviation");
				}
			} else if (property.equals(COST_PERCENTAGE)) {
				if (totalCost == 0.0) {
					return NOT_AVAILABLE;
				} else {
					if ((costDriverMultitude == null) || (costDriverMultitude.doubleValue() == 0.0)) {
						return NOT_AVAILABLE;
					} else {
                        return Double.valueOf(MathUtils.fix(
                                100.0 / (totalCost / costDriverMultitude.doubleValue()) * (total * ModelUtility.getMultitudeFactor(cost)), 1));
					}
				}
			} else if (property.equals("currency")) {
				return cost.getCurrency().getNameString(ModelUtility.getCodeTypeLocale());
			}
		} else if (owner instanceof Dependency) {
			// @see ServiceDetailView#initializeView()
			TcoObject supplier = utility.findSupplier((Dependency) owner);
			// TcoObject client =
			// utility.findClient((Dependency)owner);
			if (supplier == null) {
				return ResourceManager.getResource(Formatter.class, "CWDependencyFault");
			}
			if (property.equals(SUPPLIER_NAME)) {
				return supplier.getName();
			} else if (property.equals(SUPPLIER_TYPE)) {
				return ModelUtility.getTypeString(supplier.getClass());
			} else if (property.equals(SUPPLIER_COST)) {
				org.tcotool.tools.Calculator calculator = new org.tcotool.tools.CalculatorTco(utility);
                return Double.valueOf(calculator.calculateOverallCosts(supplier));
			} else if (property.equals(SUPPLIER_CLIENT_COST)) {
				org.tcotool.tools.Calculator calculator = new org.tcotool.tools.CalculatorTco(utility);
				double cost = calculator.calculateOverallCosts(supplier);
                return Double.valueOf(MathUtils.fix(cost / 100.0 * ((Dependency) owner).getDistribution().doubleValue(), 1));
			}
		}

		return NOT_AVAILABLE;
	}

	/**
	 * @see #evaluate(Object, String)
	 */
	public void setTotalCost(double total, Double costDriverMultitude) {
		this.totalCost = total;
		this.costDriverMultitude = costDriverMultitude;
	}

	/**
	 * @see #evaluate(Object, String)
	 */
	public void setTotalCostDriver(double total) {
		this.totalCostDriver = total;
	}
}
