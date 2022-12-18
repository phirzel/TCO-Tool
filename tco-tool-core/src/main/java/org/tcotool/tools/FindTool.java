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
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tcotool.application.PersonalCostDetailView;
import org.tcotool.model.Activity;
import org.tcotool.model.Cost;
import org.tcotool.model.CostCause;
import org.tcotool.model.CostCentre;
import org.tcotool.model.CostDriver;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Process;
import org.tcotool.model.Responsibility;
import org.tcotool.model.Role;
import org.tcotool.model.Service;
import org.tcotool.model.ServiceCategory;
import org.tcotool.model.Site;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.standard.report.ReportTool;

/**
 * Utility to find References in TCO-tree and present results in a search-list.
 *
 * @author Peter Hirzel
 * @see ModelUtility#findParent(Object)
 */
public class FindTool {

	private ModelUtility utility = null;
	// hits after find*()
	private List<String> searchList = new ArrayList<>();
	private DbCodeType code = null;
	private String expression = null;
	private String undefinedLabel = "<UNDEFINED>";

	public FindTool(ModelUtility utility) {
		super();
		this.utility = utility;
	}

	/**
	 * Return a list of found hits for given code in model-tree.
	 *
	 * @param code
	 * @return HTML formatted href's
	 */
	public List<String> findByCode(DbCodeType code) {
		this.code = code;
		searchList = new ArrayList<>();
		CodeRefBuilder builder = new CodeRefBuilder((TcoModel) utility.getRoot(), code);
		Iterator<TcoObject> hits = builder.getHits().iterator();
		while (hits.hasNext()) {
			addHit(hits.next());
		}
		return searchList;
	}

	/**
	 * Find all object's in model where expression matches name. PackagableElement's go down the tree recursively as well.
	 *
	 * @param expression to be searched for
	 * @param checkName
	 * @param checkDocumentation
	 * @param checkCaseSensitive
	 * @return HTML formatted href's
	 */
	public List<String> findByText(final String expression, boolean checkName, boolean checkDocumentation, boolean checkCaseSensitive) {
		searchList = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(expression)) {
			this.expression = expression;
			find((TcoModel) utility.getRoot(), null, checkName, checkDocumentation, checkCaseSensitive);
		}
		return searchList;
	}

	/**
	 * Find all hits belonging to code.
	 *
	 * @param undefinedCode
	 * @return
	 */
	public List<String> findByUndefinedCode(final String undefinedCode) {
		searchList = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(undefinedCode)) {
			find((TcoModel) utility.getRoot(), undefinedCode, false, false, false);
		}
		return searchList;
	}

	/**
	 * Will be determined while searching.
	 *
	 * @return
	 * @see #findByUndefinedCode(String)
	 */
	public String getUndefinedLabel() {
		return undefinedLabel;
	}

	private void find(TcoObject object, final String undefinedCode, boolean checkName, boolean checkDocumentation, boolean checkCaseSensitive) {
		// 1) find matches
		if (undefinedCode != null) {
			// expression is irrelevant
			if (Calculator.PERSONAL_COST_INTERNAL.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(PersonalCost.class) + ": "
					+ ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text") + "=" + StringUtils.getBooleanString(Boolean.TRUE)
					+ "; " + ModelUtility.getTypeString(Role.class) + "=" + StringUtils.getBooleanString(Boolean.FALSE);
				if ((object instanceof PersonalCost) && (((PersonalCost) object).getRole() == null) && ((PersonalCost) object).getInternal().booleanValue()) {
					addHit(object);
				}
			} else if (Calculator.PERSONAL_COST_EXTERNAL.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(PersonalCost.class) + ": "
					+ ResourceManager.getResource(PersonalCostDetailView.class, "ChxInternal_text") + "=" + StringUtils.getBooleanString(Boolean.FALSE)
					+ "; " + ModelUtility.getTypeString(Role.class) + "=" + StringUtils.getBooleanString(Boolean.FALSE);
				if ((object instanceof PersonalCost) && (((PersonalCost) object).getRole() == null) && (!((PersonalCost) object).getInternal().booleanValue())) {
					addHit(object);
				}
			} else if (Calculator.COST_CAUSE_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(CostCause.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof Cost) && (((Cost) object).getCause() == null)) {
					addHit(object);
				}
				// TODO ProjectPhase
			} else if (Calculator.PROCESS_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(Process.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof CostDriver) && (((CostDriver) object).getProcess() == null)) {
					addHit(object);
				}
			} else if (Calculator.CATEGORY_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(ServiceCategory.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof Service) && (((Service) object).getCategory() == null)) {
					addHit(object);
				}
			} else if (Calculator.COSTCENTRE_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(CostCentre.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof Service) && (((Service) object).getCostCentre() == null)) {
					addHit(object);
				}
			} else if (Calculator.RESPONSIBILITY_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(Responsibility.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof Service) && (((Service) object).getResponsibility() == null)) {
					addHit(object);
				}
			} else if (Calculator.SITE_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(Site.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof CostDriver) && (((CostDriver) object).getOccurrance().size() == 0)) {
					addHit(object);
				}
				// } else if (Calculator.CATALOGUE_UNDEFINED.equals(id)) { =>
				// makes no sense
			} else if (Calculator.PERSONAL_ROLE_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(Role.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof PersonalCost) && (((PersonalCost) object).getRole() == null)) {
					addHit(object);
				}
			} else if (Calculator.PERSONAL_ACTIVITY_UNDEFINED.equals(undefinedCode)) {
				undefinedLabel = ModelUtility.getTypeString(Activity.class) + ": " + ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
				if ((object instanceof PersonalCost) && (((PersonalCost) object).getActivity() == null)) {
					addHit(object);
				}
			}
		} else {
			// try parameters in FindDialog
			if ((checkName && findText(object.getName(), expression, checkCaseSensitive))
				|| (checkDocumentation && findText(object.getDocumentation(), expression, checkCaseSensitive))) {
				// only add an object once
				addHit(object);
			}
		}

		// 2) go on recursively
		if (object instanceof TcoPackage) {
			java.util.Iterator<TcoPackage> it = ((TcoPackage) object).getOwnedElement().iterator();
			while (it.hasNext()) {
				TcoPackage group = it.next();
				find(group, undefinedCode, checkName, checkDocumentation, checkCaseSensitive);
			}

			java.util.Iterator<Service> itService = ((TcoPackage) object).getService().iterator();
			while (itService.hasNext()) {
				Service service = itService.next();
				find(service, undefinedCode, checkName, checkDocumentation, checkCaseSensitive);
			}
		} else if (object instanceof Service) {
			java.util.Iterator<CostDriver> it = ((Service) object).getDriver().iterator();
			while (it.hasNext()) {
				CostDriver driver = it.next();
				find(driver, undefinedCode, checkName, checkDocumentation, checkCaseSensitive);
				Iterator<Cost> itCosts = driver.getCost().iterator();
				while (itCosts.hasNext()) {
					Cost cost = itCosts.next();
					/*
					 * URL costUrl = personalCostURL; if (cost instanceof
					 * FactCost) { costUrl = factCostURL; }
					 */
					find(cost /* , costUrl */, undefinedCode, checkName, checkDocumentation, checkCaseSensitive);
				}
			}
		}
	}

	private boolean findText(final String text, final String expression, boolean checkCaseSensitive) {
		if (!StringUtils.isNullOrEmpty(text)) {
			String tmp = text;
			// TODO NYI: match whole word
			if (checkCaseSensitive) {
				return tmp.indexOf(expression) > -1;
			} else {
				return tmp.toUpperCase().indexOf(expression.toUpperCase()) > -1;
			}
		}
		return false;
	}

	/**
	 * Convert given object to HTML formatted String and add it to hit-list.
	 *
	 * @param object
	 */
	private void addHit(TcoObject object) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ReportTool.getTcoObjectImage(utility, object.getClass()));
		buffer.append(" <a href=\"");
		buffer.append(object.getId());
		buffer.append("\"> ");
		buffer.append(object.getName());
		buffer.append("</a>");
		if (code != null) {
			if ((code instanceof Role) && (object instanceof PersonalCost)) {
				buffer.append(" " + Calculator.calculateHours(((PersonalCost) object)) * utility.getMultitudeFactor(object, true) + " / "
					+ Calculator.calculateAvailableHours((Role) code) + " [h]");
			} else if ((code instanceof CostCause) && (object instanceof Cost)) {
				buffer.append(" " + AmountFormat.toString(Calculator.calculate((Cost) object)) + " " + ((Cost) object).getCurrency().getNameString());
			}
		}

		searchList.add(buffer.toString());
	}
}
