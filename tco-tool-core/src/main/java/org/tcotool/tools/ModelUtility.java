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
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.UserException;
import lombok.extern.slf4j.Slf4j;
import org.tcotool.application.*;
import org.tcotool.model.Currency;
import org.tcotool.model.Process;
import org.tcotool.model.*;
import org.tcotool.presentation.Diagram;
import org.tcotool.presentation.PresentationElement;
import org.tcotool.standard.report.ReportTool;

import javax.swing.*;
import java.net.URL;
import java.util.*;

/**
 * Utility Class that knows how to <b>deal with Model (and TreeNode's)</b>.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class ModelUtility implements
		// TODO TreeNodeUtility should be implemented by NavigationView
		ch.softenvironment.view.tree.TreeNodeUtility {

	// public static final Long EXPENDABLE_DURATION = Long.valueOf(12); // 12
	// month
	public static final String XSD = "http://www.interlis.ch/INTERLIS2.2 TcoTool.xsd";
	private final TcoModel model;
	private String filename = null;
	private Diagram dependencyDiagram = null;
	private final Map<Class<? extends TcoObject>, URL> iconMap = new HashMap<Class<? extends TcoObject>, URL>();

	/**
	 * Create a ModelUtility with a NEW TcoModel.
	 */
	public ModelUtility(DbObjectServer server) throws Exception {
		super();
		filename = null;
		dependencyDiagram = null;
		// define DbEnumerations
		mapEnumerations((ch.softenvironment.jomm.target.xml.XmlObjectServer) server);

		// root element
		model = (TcoModel) createTcoObject(server, TcoModel.class);
		model.setName("<" + ResourceManager.getResource(ModelDetailView.class, "FrmWindow_text") + ">");
		model.setAuthor(System.getProperty("user.name"));
		model.save(); // make sure ROOT-ID is set for model
	}

	/**
	 * Create a new ModelUtility handling an EXISTING TcoModel.
	 *
	 * @param model root-element
	 */
	public ModelUtility(TcoModel model, String filename) throws Exception {
		super();
		this.model = model;
		setFilename(filename);

		FixBrokenModelTool.fixModel(this, model);
	}

	/**
	 * Add element to owner.
	 *
	 * @param owner ch.softenvironment.tree.core.Namespace
	 * @param element
	 * @return the newly instantiated element
	 * @deprecated
	 */
	private Object addOwnedElement(java.lang.Object owner, java.lang.Class<? extends TcoObject> element) throws Exception {
		// 1) create persistently
		TcoObject object = createTcoObject(((ch.softenvironment.jomm.mvc.model.DbChangeableBean) owner).getObjectServer(), element);
		// 2) add to ownedElements
		return addOwnedElement(owner, object);
	}

	/**
	 * Add element to owner.
	 *
	 * @param owner ch.softenvironment.tree.core.Namespace
	 * @param element
	 * @return the given element
	 */
	@Override
	public Object addOwnedElement(java.lang.Object owner, java.lang.Object element) throws Exception {
		if (((DbChangeableBean) owner).getId() == null) {
			((DbChangeableBean) owner).save();
		}

		if (element instanceof CostDriver) {
			List<CostDriver> driver = new java.util.ArrayList<CostDriver>(((Service) owner).getDriver());
			driver.add((CostDriver) element);
			((Service) owner).setDriver(driver);

			// reverse link: element to owner
			((CostDriver) element).setServiceId(((Service) owner).getId());
		} else if (element instanceof Cost) {
			List<Cost> costs = new java.util.ArrayList<Cost>(((CostDriver) owner).getCost());
			costs.add((Cost) element);
			((CostDriver) owner).setCost(costs);

			// reverse link: element to owner
			((Cost) element).setDriverId(((CostDriver) owner).getId());
		} else if (element instanceof Service) {
			List<Service> services = new java.util.ArrayList<>(((TcoPackage) owner).getService());
			services.add((Service) element);
			((TcoPackage) owner).setService(services); // triggers node
			// structure

			// reverse link: element to owner
			((Service) element).setGroupId(((TcoPackage) owner).getId());
		} else if (element instanceof Occurance) {
			List<Occurance> occurance = new java.util.ArrayList<>(((CostDriver) owner).getOccurrance());
			occurance.add((Occurance) element);
			((CostDriver) owner).setOccurrance(occurance);

			// reverse link: element to owner
			((Occurance) element).setDriverId(((CostDriver) owner).getId());
		} else if (element instanceof Course) {
			List<Course> course = new java.util.ArrayList<>(((SystemParameter) owner).getCourse());
			course.add((Course) element);
			((SystemParameter) owner).setCourse(course);

			// reverse link: element to owner
			((Course) element).setSystemParameterId(((SystemParameter) owner).getId());
		} else {
			// sub-package
			List<TcoPackage> ownedElement = new java.util.ArrayList<>(((TcoPackage) owner).getOwnedElement());
			ownedElement.add((TcoPackage) element);
			((TcoPackage) owner).setOwnedElement(ownedElement); // triggers node
			// structure

			// reverse link: element to owner
			((TcoPackage) element).setNamespaceId(((TcoPackage) owner).getId());
		}

		return element;
	}

	/**
	 * Compare two classes for sorting.
	 */
	@Override
	public int compareDefinition(Class<?> o1, Class<?> o2) {
		return 0;
	}

	/**
	 * Make sure negative temporaryID's are never used. *
	 */
	@Deprecated
	public static DbChangeableBean createDbObject(ch.softenvironment.jomm.DbObjectServer server, java.lang.Class<? extends DbObject> element) throws Exception {
		// 1) create persistently
		DbChangeableBean object = (DbChangeableBean) server.createInstance(element);
		// 2) make sure a unique TID is set
		object.save();

		return object;
	}

	/**
	 * Create a new TcoObject of given type.
	 *
	 * @param server DbObjectServer who shall manage new instance
	 */
	public TcoObject createTcoObject(ch.softenvironment.jomm.DbObjectServer server, java.lang.Class<? extends TcoObject> element) throws Exception {
		// 1) create persistently
		TcoObject object = (TcoObject) createDbObject(server, element);

		// 2) set default data
		object.setMultitude(Double.valueOf(1.0));
		if (object instanceof CostDriver) {
			object.setName(ResourceManager.getResource(ServiceDetailView.class, "CTNewObject"));
		} else if (object instanceof FactCost) {
			FactCost cost = (FactCost) object;
			cost.setName(ResourceManager.getResource(CostDriverDetailView.class, "CTFactCostName"));
			cost.setBaseOffset(Long.valueOf(0));
			cost.setAmount(Double.valueOf(0.0));
			cost.setEstimated(Boolean.FALSE);
			cost.setRepeatable(Boolean.FALSE);
			cost.setCurrency(getSystemParameter().getDefaultCurrency());
			cost.setDepreciationDuration(getSystemParameter().getDefaultDepreciationDuration());
			cost.setUsageDuration(getSystemParameter().getDefaultUsageDuration());
			cost.setExpendable(Boolean.FALSE);
		} else if (object instanceof PersonalCost) {
			PersonalCost cost = (PersonalCost) object;
			cost.setName(ResourceManager.getResource(CostDriverDetailView.class, "CTPersonalCostName"));
			cost.setBaseOffset(Long.valueOf(0));
			cost.setAmount(Double.valueOf(0.0));
			cost.setEstimated(Boolean.FALSE);
			cost.setRepeatable(Boolean.TRUE);
			cost.setCurrency(getSystemParameter().getDefaultCurrency());
			cost.setInternal(Boolean.TRUE);
		} else {
			object.setName("*" + getTypeString(element) + "*");
		}

		// object.setBaseDate(sysParams.getDefaultBaseDate());
		return object;
	}

	/**
	 * Create a Dependency without supplier and client.
	 *
	 * @param server
	 * @return
	 * @throws Exception
	 */
	public static Dependency createDependency(ch.softenvironment.jomm.DbObjectServer server) throws Exception {
		Dependency dependency = (Dependency) server.createInstance(Dependency.class);
		dependency.setDistribution(Double.valueOf(0.0));
		dependency.setSupplierInfluence((SupplierInfluence) server.getIliCode(SupplierInfluence.class, SupplierInfluence.NEUTRAL0));
		dependency.setVariant(Boolean.FALSE);
		return dependency;
	}

	/**
	 * Set client and supplier of Dependency, where client --depends--> on supplier.
	 *
	 * @param dependency
	 * @param client
	 * @param supplier TcoPackage or Service
	 * @return @see DependencyValidator#validate(ModelUtility, Service, TcoObject)
	 * @throws Exception
	 * @see {@link DependencyValidator#validate(ModelUtility, Service, TcoObject)}
	 */
	public String addDependencyEnds(Dependency dependency, Service client, TcoObject supplier) throws Exception {
		String fault = DependencyValidator.validate(this, client, supplier);
		if (fault != null) {
			return fault;
		}

		// TODO non-generically n:n mapping
		client.save(); // create Id
		dependency.setClientId(client.getId());
		client.getSupplierId().add(dependency);

		supplier.save(); // create Id
		dependency.setSupplierId(supplier.getId());
		supplier.getClientId().add(dependency);

		return null;
	}

	/**
	 * Initialize a NEW role with default values.
	 *
	 * @param role
	 * @param systemParameter
	 */
	public static void initializeRole(Role role, SystemParameter systemParameter) throws Exception {
		role.setInternal(Boolean.TRUE);
		role.setEmploymentPercentageAvailable(Long.valueOf(100));
		role.setCurrency(systemParameter.getDefaultCurrency());
		role.setFullTimeEquivalent(Double.valueOf(100000));
		role.setYearlyHours(systemParameter.getManYearHoursInternal());
		if ((role.getFullTimeEquivalent() != null) && (role.getYearlyHours() != null)) {
			role.setHourlyRate(Double.valueOf(Calculator.calculateHourlyRate(role)));
		}
	}

	/**
	 * Create a new predefined TCO-Configuration with default-Codes and Groups.
	 *
	 * @param server
	 * @return
	 */
	public static ModelUtility createDefaultConfiguration(ch.softenvironment.jomm.DbObjectServer server) throws Exception {
		ModelUtility utility = new ModelUtility(server);

		mapDefaultCodes((ch.softenvironment.jomm.target.xml.XmlObjectServer) server, utility.getSystemParameter());

		createDefaultTree(utility);

		return utility;
	}

	public static void createDefaultTree(ModelUtility utility) throws Exception {
		// children
		TcoPackage group = (TcoPackage) utility.addOwnedElement(utility.getRoot(), TcoPackage.class);
		group.setName(ResourceManager.getResource(ModelUtility.class, "CIClientSystems"));

		TcoPackage subGroup = (TcoPackage) utility.addOwnedElement(group, TcoPackage.class);
		subGroup.setName(ResourceManager.getResource(ModelUtility.class, "CIClientHWOS"));
		subGroup = (TcoPackage) utility.addOwnedElement(group, TcoPackage.class);
		subGroup.setName(ResourceManager.getResource(ModelUtility.class, "CIClientSW"));

		group = (TcoPackage) utility.addOwnedElement(utility.getRoot(), TcoPackage.class);
		group.setName(ResourceManager.getResource(ModelUtility.class, "CIBackoffice"));
		subGroup = (TcoPackage) utility.addOwnedElement(group, TcoPackage.class);
		subGroup.setName(ResourceManager.getResource(ModelUtility.class, "CIServerHWOS"));
		subGroup = (TcoPackage) utility.addOwnedElement(group, TcoPackage.class);
		subGroup.setName(ResourceManager.getResource(ModelUtility.class, "CIServerSW"));

		group = (TcoPackage) utility.addOwnedElement(utility.getRoot(), TcoPackage.class);
		group.setName(ResourceManager.getResource(ModelUtility.class, "CICommunication"));

		group = (TcoPackage) utility.addOwnedElement(utility.getRoot(), TcoPackage.class);
		group.setName(ResourceManager.getResource(ModelUtility.class, "CIItPlattform"));
	}

	public static CostCentre createCostCenter(XmlObjectServer server, Locale locale, final String iliCode, final String name) throws Exception {
		CostCentre centre = (CostCentre) server.createInstance(CostCentre.class);
		centre.setIliCode(iliCode);
		centre.getName().setValue(name, locale);
		// cause.setDirect(direct);
		centre.save();
		server.cacheCode(centre);
		centre.getNameString();
		return centre;
	}

	public static CostCause createCostCause(XmlObjectServer server, Locale locale, final String iliCode, final String name, Boolean direct) throws Exception {
		CostCause cause = (CostCause) server.createInstance(CostCause.class);
		cause.setIliCode(iliCode);
		cause.getName().setValue(name, locale);
		cause.setDirect(direct);
		cause.save();
		server.cacheCode(cause);
		cause.getNameString();
		return cause;
	}

	/**
	 * Return the client of a Dependency.
	 */
	public TcoObject findClient(Dependency dependency) {
		return findObject(dependency.getClientId(), model);
	}

	/**
	 * Return a list of all Dependencies for given object (recursively down the subtree if any).
	 *
	 * @return
	 */
	public Set<Dependency> findDependencies(TcoObject object) {
		Set<Dependency> dependencies = new HashSet<>();
		// determine current
		dependencies.addAll(object.getSupplierId());
		dependencies.addAll(object.getClientId());
		// recursively go down subtree
		if (object instanceof TcoPackage) {
			Iterator<TcoPackage> itPackage = ((TcoPackage) object).getOwnedElement().iterator();
			while (itPackage.hasNext()) {
				dependencies.addAll(findDependencies(itPackage.next()));
			}
			Iterator<Service> itService = ((TcoPackage) object).getService().iterator();
			while (itService.hasNext()) {
				dependencies.addAll(findDependencies(itService.next()));
			}
		}
		return dependencies;
	}

	/**
	 * Since the complete model is maintained as tree there must be an element with given id. All Id's are Unique within same tree. Recursive TreeWalker.
	 */
	public TcoObject findObject(Long id, TcoObject root) {
		// return root.getObjectServer().getObjectById(new
		// DbObjectId(TcoObject.class, id), false);

		if ((root.getId() != null) && root.getId().equals(id)) {
			// root is found object!
			return root;
		}

		if (root instanceof TcoPackage) {
			// check Service
			Iterator<Service> itService = ((TcoPackage) root).getService().iterator();
			while (itService.hasNext()) {
				Service service = itService.next();
				if (findObject(id, service) != null) {
					return service;
				}
				// check CostDriver
				Iterator<CostDriver> itDriver = service.getDriver().iterator();
				while (itDriver.hasNext()) {
					CostDriver driver = itDriver.next();
					if (findObject(id, driver) != null) {
						return driver;
					}
					// check *Cost
					Iterator<Cost> itCost = driver.getCost().iterator();
					while (itCost.hasNext()) {
						Cost cost = itCost.next();
						if (findObject(id, cost) != null) {
							return cost;
						}
					}
				}
			}

			// check sub-package
			Iterator<TcoPackage> itPackage = ((TcoPackage) root).getOwnedElement().iterator();
			while (itPackage.hasNext()) {
				TcoObject group = findObject(id, itPackage.next());
				if (group != null) {
					return group;
				}
			}
		}

		return null;
	}

	/**
	 * Return the parent of the given node. There must always be one except of the root (TcoModel).
	 *
	 * @param node TcoObject
	 */
	@Override
	public synchronized Object findParent(Object node) {
		if (node.equals(model)) {
			// no parent
			// ch.softenvironment.util.Tracer.getInstance().debug("findParen(" +
			// node + ") => " + null);
			return null;
		}

		// start searching at very bottom-element
		Object parent = findParent((TcoObject) node, model);
		if (parent == null) {
			// possible, for e.g. if drop to empty space in NavTree
			log.warn("Developer warning: parent not found!");
		}
		// ch.softenvironment.util.Tracer.getInstance().debug("findParent(" +
		// node + ") => " + parent);
		return parent;
	}

	private Object findParent(TcoObject node, TcoPackage root) {
		// 1:n ownedElement (sub-packages)
		java.util.Iterator<TcoPackage> itOwner = root.getOwnedElement().iterator();
		while (itOwner.hasNext()) {
			TcoPackage group = itOwner.next();
			if (group.equals(node)) {
				// found here!
				return root;
			} else {
				// search recursively down the tree until very last sub-package
				Object parent = findParent(node, group);
				if (parent != null) {
					// found one depth deeper!
					return parent;
				}
			}
		}
		// 1:n service (iterative algorithm)
		java.util.Iterator<Service> itService = root.getService().iterator();
		while (itService.hasNext()) {
			Service service = itService.next();
			if (service.equals(node)) {
				// found here!
				return root;
			} else {
				Iterator<CostDriver> itDriver = service.getDriver().iterator();
				while (itDriver.hasNext()) {
					CostDriver driver = itDriver.next();
					if (driver.equals(node)) {
						// found here!
						return service;
					} else {
						Iterator<Cost> itCost = driver.getCost().iterator();
						while (itCost.hasNext()) {
							Cost cost = itCost.next();
							if (cost.equals(node)) {
								// found here!
								return driver;
							}
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Return the supplier of a Dependency.
	 */
	public TcoObject findSupplier(Dependency dependency) {
		return findObject(dependency.getSupplierId(), model);
	}

	/**
	 * Return current file name of configuration, or null if not saved yet.
	 *
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
		if (getRoot() != null) {
			((TcoModel) getRoot()).setName(new java.io.File(filename).getName());
		}
	}

	/**
	 * One Dependency-Graph may be drawn per configuration. The Diagram saves any Presentation-info such as node location, color or alignment.
	 *
	 * @return
	 */
	public Diagram getDependencyDiagram() {
		// TODO NYI: multiple views of dependencies for large or imported
		// Configurations
		return dependencyDiagram;
	}

	public void setDependencyDiagram(Diagram diagram) {
		this.dependencyDiagram = diagram;
	}

	/**
	 * Role overwrites PersonalCost, therefore reset to current code values
	 *
	 * @param cost
	 * @return boolean (true->FTE manYear relevant; false->otherwise)
	 */
	public static boolean updateRole(PersonalCost cost) {
		if (cost.getRole() != null) {
			Role role = cost.getRole();
			// cost.setName(cost.getRole().getNameString());
			cost.setHourlyRate(role.getHourlyRate());
			cost.setCurrency(role.getCurrency());
			cost.setInternal(role.getInternal());
			if ((cost.getHourlyRate() != null) && (cost.getHours() != null)) {
				// calculate hourlyRate*hours
				cost.setAmount(Double.valueOf(AmountFormat.round(cost.getHourlyRate().doubleValue() * cost.getHours().doubleValue())));
			} else {
				// use FTE because whole salary is assumed
				cost.setAmount(role.getFullTimeEquivalent());
				return true;
			}
		}
		return false;
	}

	/**
	 * All Cost-element's might be updated, because assigned codes changed and over-rule.
	 */
	public void updateCost(TcoObject owner) {
		// TODO refactor as a TreeToolListener
		if (owner instanceof TcoPackage) {
			Iterator<TcoPackage> itOwnedElement = ((TcoPackage) owner).getOwnedElement().iterator();
			while (itOwnedElement.hasNext()) {
				TcoPackage ownedElement = itOwnedElement.next();
				updateCost(ownedElement);
			}

			Iterator<Service> itService = ((TcoPackage) owner).getService().iterator();
			while (itService.hasNext()) {
				updateCost(itService.next());
			}
		} else if (owner instanceof Service) {
			Iterator<CostDriver> itCostDriver = ((Service) owner).getDriver().iterator();
			while (itCostDriver.hasNext()) {
				CostDriver driver = itCostDriver.next();
				Iterator<Cost> itCost = driver.getCost().iterator();
				while (itCost.hasNext()) {
					Cost cost = itCost.next();
					if (cost instanceof PersonalCost) {
						// correct amount according to its role
						updateRole((PersonalCost) cost);
					} else {
						// determine the max Usage for TCO-Reports
						// setMaxUsage((fCost).getUsageDuration());
						// setMaxDepreciation((fCost).getDepreciationDuration());
						updateCatalogue((FactCost) cost);
					}
				}
			}
		}
	}

	/**
	 * Catalogue overwrites FactCost settings, therefore reset to current code values.
	 *
	 * @param cost
	 */
	public static void updateCatalogue(FactCost cost) {
		if (cost.getCatalogue() != null) {
			Catalogue catalogue = cost.getCatalogue();
			// cost.setName(catalogue.getNameString());
			cost.setAmount(catalogue.getPrice());
			cost.setCurrency(catalogue.getCurrency());
			cost.setUsageDuration(catalogue.getUsageDuration());
			cost.setDepreciationDuration(catalogue.getDepreciationDuration());
		}
	}

	/**
	 * Returns the appropriate icon for node Element.
	 */
	@Override
	public Icon getIcon(Class<?> type, boolean expanded) {
		URL url = getImageURL((Class<? extends TcoObject>) type);
		if (url != null) {
			try {
				return new ImageIcon(url);
			} catch (Exception e) {
				log.error("Developer error: image not found for: {}", type);
			}
		}
		return null;
	}

	/**
	 * Define icon image for TCO-instance representation in TreeNode.
	 *
	 * @param key (Service, CostDriver, FactCost, PersonalCost)
	 * @param url
	 */
	public void setImageURL(Class<? extends TcoObject> key, URL url) {
		iconMap.put(key, url);
	}

	public URL getImageURL(Class<? extends TcoObject> key) {
		try {
			if (iconMap.containsKey(key)) {
				return iconMap.get(key);
			} else {
				if (key.equals(TcoPackage.class) || key.equals(TcoModel.class)) {
					return null; // leave it to default Tree behaviour
				} else {
					String fileName = StringUtils.getPureClassName(key);
					// TODO IMPORT
					/*
					 * if (!((TcoObject)node).getObjectServer().equals(((TcoModel )getRoot()).getObjectServer())) { fileName = fileName + "_imported"; }
					 */
					setImageURL(key, ResourceManager.getURL(ModelUtility.class, fileName + ".png"));
					return iconMap.get(key);
				}
			}
		} catch (Exception e) {
			log.error("Developer error: image not found for:", key);
			return null;
		}
	}

	/**
	 * Returns the string to display for this object.
	 */
	@Override
	public String getName(Object element) {
		if (element instanceof TcoObject) {
			return ((TcoObject) element).getName();
		}

		// should not reach
		return "<Name>";
	}

	/**
	 * Return the root-element of the Model-Tree.
	 */
	@Override
	public Object /* TcoModel */getRoot() {
		return model;
	}

	/**
	 * Return the System-Parameters which are global within a configuration.
	 */
	public SystemParameter getSystemParameter() throws Exception {
		if (model.getSystemParameter() == null) {
			// create default
			SystemParameter tmp = (SystemParameter) getServer().createInstance(SystemParameter.class);
			tmp.setDepreciationInterestRate(Double.valueOf(4.0));
			tmp.setDefaultDepreciationDuration(Long.valueOf(48));
			tmp.setDefaultUsageDuration(Long.valueOf(48));
			tmp.setManYearHoursInternal(Long.valueOf(1700));
			tmp.setManYearHoursExternal(Long.valueOf(1800));
			tmp.setReportDepreciationDuration(tmp.getDefaultDepreciationDuration());
			tmp.setReportUsageDuration(tmp.getDefaultUsageDuration());
			tmp.setReportCostExponent((CostExponent) getServer().getIliCode(CostExponent.class, CostExponent.ASIS));
			try {
				tmp.setDefaultCurrency((Currency) getServer().getIliCode(Currency.class, Currency.CHF));
			} catch (Exception e) {
				log.warn("SystemParameter fault <Currency>=CHF (default)", e);
			}
			tmp.save();

			model.setSystemParameter(tmp);
			tmp.setModelId(model.getId());
		}
		return model.getSystemParameter();
	}

	/**
	 * Returns the a ToolTip if cursor is on a node in a TreeView.
	 */
	@Override
	public java.lang.String getToolTip(java.lang.Object node) {
		TcoObject object = (TcoObject) node;
		String toolTip = ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblMultitude_text") + " = " + object.getMultitude();
		if (object instanceof Cost) {
			toolTip = toolTip + "; " + ResourceManager.getResourceAsNonLabeled(FactCostDetailView.class, "LblBaseOffset_text") + "="
				+ ((Cost) object).getBaseOffset() + "; ";
			if (((Cost) object).getCause() != null) {
				toolTip = toolTip + ModelUtility.getTypeString(CostCause.class) + "=" + ((Cost) object).getCause().getNameString() + ";";
			}
		}
		return toolTip;
	}

	/**
	 * Return speaking name for given type.
	 */
	public static final java.lang.String getTypeString(@SuppressWarnings("rawtypes") java.lang.Class type) {
		// TcoObject's
		if (type.equals(org.tcotool.model.TcoPackage.class)) {
			return ResourceManager.getResource(PackageDetailView.class, "FrmWindow_text");
		} else if (type.equals(org.tcotool.model.Service.class)) {
			return ResourceManager.getResource(ServiceDetailView.class, "FrmWindow_text");
		} else if (type.equals(org.tcotool.model.TcoModel.class)) {
			return ResourceManager.getResource(ModelDetailView.class, "FrmWindow_text");
		} else if (type.equals(CostDriver.class)) {
			return ResourceManager.getResource(CostDriverDetailView.class, "FrmWindow_text");
		} else if (type.equals(FactCost.class)) {
			return ResourceManager.getResource(FactCostDetailView.class, "FrmWindow_text");
		} else if (type.equals(PersonalCost.class)) {
			return ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text");
		} else if (type.equals(Cost.class)) {
			return ResourceManager.getResource(FactCostDetailView.class, "FrmWindow_text") + "/"
				+ ResourceManager.getResource(PersonalCostDetailView.class, "FrmWindow_text");
			// DbEnumeration's
		} else if (type.equals(CostCause.class)) {
			return ResourceManager.getResourceAsNonLabeled(CostDriverDetailView.class, "TbcCosttype_text");
			// DbCode's
		} else if (type.equals(ServiceCategory.class)) {
			// TODO HACK
			// return "Clinical Symptom";
			return ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblCategory_text");
		} else if (type.equals(Responsibility.class)) {
			return ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblResponsibility_text");
		} else if (type.equals(CostCentre.class)) {
			return ResourceManager.getResourceAsNonLabeled(ServiceDetailView.class, "LblCostCentre_text");
		} else if (type.equals(Process.class)) {
			return ResourceManager.getResourceAsNonLabeled(CostDriverDetailView.class, "LblProcess_text");
		} else if (type.equals(ProjectPhase.class)) {
			return ResourceManager.getResourceAsNonLabeled(CostDriverDetailView.class, "LblPhase_text");
		} else if (type.equals(LifeCycle.class)) {
			return ResourceManager.getResourceAsNonLabeled(CostDriverDetailView.class, "LblCycle_text");
		} else if (type.equals(Site.class)) {
			return ResourceManager.getResourceAsNonLabeled(CostDriverDetailView.class, "LblSite_text");
		} else if (type.equals(Catalogue.class)) {
			return ResourceManager.getResourceAsNonLabeled(CatalogueDetailView.class, "FrmWindow_text");
		} else if (type.equals(Role.class)) {
			return ResourceManager.getResourceAsNonLabeled(RoleDetailView.class, "FrmWindow_text");
		} else if (type.equals(Activity.class)) {
			return ResourceManager.getResourceAsNonLabeled(PersonalCostDetailView.class, "LblActivity_text");
		}
		log.warn("Developer warning: String for type: {}", type);
		return "NYI"; // not yet implemented
	}

	/**
	 * Add an imported Model.
	 *
	 * @deprecated experimental trial
	 */
	@Deprecated
	public void importModel(TcoPackage owner, TcoModel importModel) throws Exception {
		FixBrokenModelTool.fixModel(this, importModel);
		addOwnedElement(owner, importModel);

		// set given model as imported
		/*
		 * ImportReference ref = new ImportReference(object.getObjectServer()); ref.setImportFile(filename); ref.setExternalId(object.getId());
		 * object.setReference(ref);
		 *
		 * // set services as imported java.util.Iterator services = object.getService().iterator(); if (services.hasNext()) { // report services within Package
		 * while (services.hasNext()) { Service service = (Service)services.next(); ref = new ImportReference(service.getObjectServer());
		 * ref.setImportFile(filename); ref.setExternalId(service.getId()); service.setReference(ref); } }
		 *
		 * // set sub-packages as imported java.util.Iterator groups = object.getOwnedElement().iterator(); while (groups.hasNext()) {
		 * importPackage((TcoPackage)groups.next(), filename); }
		 */
	}

	/**
	 * Return whether Node must not have any children.
	 */
	@Override
	public boolean isLeaf(java.lang.Object node) {
		return node instanceof Cost;
	}

	/**
	 * Return whether a node might have changed in model-tree visually. For e.g. a represented speaking name of a TreeNode.
	 *
	 * @param event => source and property changed
	 */
	@Override
	public boolean isNodeChanged(java.beans.PropertyChangeEvent event) {
		if (event.getPropertyName().equals("name")) {
			/*
			 * || (event.getSource() instanceof Service)
			 */
			return event.getSource() instanceof TcoObject;
		}
		return false;
	}

	/**
	 * Return whether Node mapping this TreeElement might be renamed.
	 *
	 * @see NavigationView
	 */
	@Override
	public boolean isNodeEditable(Object node) {
		return (node != null) && (!node.equals(model));
	}

	/**
	 * Return whether Node mapping this TreeElement might be moved.
	 *
	 * @see NavigationView
	 */
	@Override
	public boolean isNodeMovable(Object node) {
		return isNodeEditable(node);
	}

	/**
	 * Return whether Node mapping this TreeElement might be removed.
	 *
	 * @see NavigationView
	 */
	@Override
	public boolean isNodeRemovable(Object node) {
		return isNodeEditable(node);
	}

	/**
	 * Return whether the structure (sub-elements of a node) might have changed in model-tree visually.
	 *
	 * @param event => source and property changed
	 */
	@Override
	public boolean isNodeStructureChanged(java.beans.PropertyChangeEvent event) {
		// in case a 1:n entry changed within Configuration
		return event.getPropertyName().equals(/* TcoPackage# */"ownedElement") || event.getPropertyName().equals(/*
		 * TcoPackage #
		 */"service")
			|| event.getPropertyName().equals(/*
		 * Service #
		 */"driver") || event.getPropertyName().equals(/*
		 * CostDriver #
		 */"cost");
	}

	/**
	 * Return the iterator of children of given node.
	 */
	@Override
	public java.util.Iterator<TcoObject> iteratorChildren(java.lang.Object node) {
		java.util.List<TcoObject> children = new java.util.ArrayList<TcoObject>();
		if (node instanceof TcoPackage) {
			// 1) sub-packages
			children.addAll(((TcoPackage) node).getOwnedElement());
			// 2) services
			children.addAll(((TcoPackage) node).getService());
		} else if (node instanceof Service) {
			children.addAll(((Service) node).getDriver());
		} else if (node instanceof CostDriver) {
			children.addAll(((CostDriver) node).getCost());
		} // else ignore Cost's => leaves;

		return children.iterator();
	}

	/**
	 * Return the Locale instance for NLS-Codes.
	 */
	public static Locale getCodeTypeLocale() {
		return java.util.Locale.getDefault();
	}

	public static String getIliSender() {
		return "www.tcotool.org (" + LauncherView.getVersion() + ")";
	}

	/**
	 * Register the default DbCode's for new Configurations.
	 */
	private static void mapDefaultCodes(ch.softenvironment.jomm.target.xml.XmlObjectServer server, SystemParameter sysPar) throws Exception {
		java.util.Locale locale = getCodeTypeLocale();

		String[] nlsText = new String[]{ResourceManager.getResource(org.tcotool.model.Responsibility.class, "CIOperations"),
			ResourceManager.getResource(org.tcotool.model.Responsibility.class, "CIManagement"),
			ResourceManager.getResource(org.tcotool.model.Responsibility.class, "CIHelpDesk")};
		server.mapCodes(org.tcotool.model.Responsibility.class, nlsText, locale);

		nlsText = new String[]{ResourceManager.getResource(org.tcotool.model.ServiceCategory.class, "CISystemService"),
			ResourceManager.getResource(org.tcotool.model.ServiceCategory.class, "CISecurityService"),
			ResourceManager.getResource(org.tcotool.model.ServiceCategory.class, "CINetworkService"),
			ResourceManager.getResource(org.tcotool.model.ServiceCategory.class, "CIOutsourcingService")

		};
		server.mapCodes(org.tcotool.model.ServiceCategory.class, nlsText, locale);

		createCostCenter(server, locale, CostCentre.INTERNAL, ResourceManager.getResource(org.tcotool.model.CostCentre.class, "CIInternal"));
		createCostCenter(server, locale, CostCentre.INFRASTRUCTURE, ResourceManager.getResource(org.tcotool.model.CostCentre.class, "CIInfrastructure"));
		createCostCenter(server, locale, CostCentre.MERCHANDISING, ResourceManager.getResource(org.tcotool.model.CostCentre.class, "CIMerchandising"));
		createCostCenter(server, locale, CostCentre.EDUCATION, ResourceManager.getResource(org.tcotool.model.CostCentre.class, "CIEducation"));
		createCostCenter(server, locale, CostCentre.ADMINISTRATION, ResourceManager.getResource(org.tcotool.model.CostCentre.class, "CIAdministration"));
		createCostCenter(server, locale, CostCentre.PRODUCTION, ResourceManager.getResource(org.tcotool.model.CostCentre.class, "CIProduction"));

		nlsText = new String[]{ResourceManager.getResource(org.tcotool.model.Process.class, "CIControlIT"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIEducation"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIProviding"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIManageIT"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIDevelopSolution"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIMaintainInfrastructure"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIUserSupport"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIMaintainProcess"),
			ResourceManager.getResource(org.tcotool.model.Process.class, "CIFinancialAdministration")};
		server.mapCodes(org.tcotool.model.Process.class, nlsText, locale);

		// CostCause
		createCostCause(server, locale, CostCause.HARDWARE, ResourceManager.getResource(CostCause.class, "CIHardware"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.SOFTWARE, ResourceManager.getResource(CostCause.class, "CISoftware"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.INSTALLATION, ResourceManager.getResource(CostCause.class, "CIInstallation"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.INTEGRATION, ResourceManager.getResource(CostCause.class, "CIIntegration"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.OPERATION, ResourceManager.getResource(CostCause.class, "CIOperaion"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.OPERATIONHARDWARE, ResourceManager.getResource(CostCause.class, "CIOperationHardware"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.OPERATIONSOFTWARE, ResourceManager.getResource(CostCause.class, "CIOperationSoftware"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.INFRASTRUCTURE, ResourceManager.getResource(CostCause.class, "CIInfrastructure"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.SERVICE, ResourceManager.getResource(CostCause.class, "CIService"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.EDUCATION, ResourceManager.getResource(CostCause.class, "CIEducation"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.STORAGE, ResourceManager.getResource(CostCause.class, "CIStorage"), Boolean.TRUE);
		createCostCause(server, locale, CostCause.DOWNTIME, ResourceManager.getResource(CostCause.class, "CIDowntime"), Boolean.FALSE);
		createCostCause(server, locale, CostCause.END_USER_OPERATION, ResourceManager.getResource(CostCause.class, "CIEndUserOperation"), Boolean.FALSE);

		nlsText = new String[]{ResourceManager.getResource(org.tcotool.model.Activity.class, "CIFirstLevelSupport"),
			ResourceManager.getResource(org.tcotool.model.Activity.class, "CISecondLevelSupport"),
			ResourceManager.getResource(org.tcotool.model.Activity.class, "CIThirdLevelSupport")};
		server.mapCodes(org.tcotool.model.Activity.class, nlsText, locale);

		nlsText = new String[]{ResourceManager.getResource(org.tcotool.model.Role.class, "CIProjectManager"),
			ResourceManager.getResource(org.tcotool.model.Role.class, "CISolutionArchtitect"),
			ResourceManager.getResource(org.tcotool.model.Role.class, "CIQualityAssurance"),
			ResourceManager.getResource(org.tcotool.model.Role.class, "CIServiceProvider")};
		java.util.Iterator it = server.mapCodes(org.tcotool.model.Role.class, nlsText, locale).iterator();
		while (it.hasNext()) {
			initializeRole((Role) it.next(), sysPar);
		}

		// HERMES phase model (see http://www.hermes.admin.ch/)
		nlsText = new String[]{ResourceManager.getResource(org.tcotool.model.ProjectPhase.class, "CIInitialization"),
			ResourceManager.getResource(org.tcotool.model.ProjectPhase.class, "CIAnalysis"),
			ResourceManager.getResource(org.tcotool.model.ProjectPhase.class, "CIConcept"),
			ResourceManager.getResource(org.tcotool.model.ProjectPhase.class, "CIRealization"),
			ResourceManager.getResource(org.tcotool.model.ProjectPhase.class, "CIIntroduction"),
			ResourceManager.getResource(org.tcotool.model.ProjectPhase.class, "CITransition")};
		server.mapCodes(org.tcotool.model.ProjectPhase.class, nlsText, locale);

		nlsText = new String[]{ResourceManager.getResource(org.tcotool.model.Site.class, "CIMainSite"),
			ResourceManager.getResource(org.tcotool.model.Site.class, "CISubsidiary")};
		server.mapCodes(org.tcotool.model.Site.class, nlsText, locale);

		/*
		 * nlsText = new String[]{ "Monitor", "Printer", "DNS (Domain Name Service)" }; server.mapCodes(org.tcotool.model.Catalogue.class, nlsText, locale);
		 */

	}

	/**
	 * Register the default DbEnumeration's which are the same for all Configuration.
	 */
	public static void mapEnumerations(ch.softenvironment.jomm.target.xml.XmlObjectServer server) {
		// define default DbEnumeration's
		// TODO implement DbEnumeration according to proper NLS-Language
		java.util.Locale locale = getCodeTypeLocale();

		server.createEnumeration(Branch.class, Branch.GOVERNMENT, locale, ResourceManager.getResource(Branch.class, "EnumGOVERNMENT"));
		server.createEnumeration(Branch.class, Branch.BANKINSURANCE, locale, ResourceManager.getResource(Branch.class, "EnumBANKINSURANCE"));
		server.createEnumeration(Branch.class, Branch.INDUSTRY, locale, ResourceManager.getResource(Branch.class, "EnumINDUSTRY"));
		server.createEnumeration(Branch.class, Branch.CONSULTING, locale, ResourceManager.getResource(Branch.class, "EnumCONSULTING"));
		server.createEnumeration(Branch.class, Branch.CHEMICALINDUSTRY, locale, ResourceManager.getResource(Branch.class, "EnumCHEMICALINDUSTRY"));
		server.createEnumeration(Branch.class, Branch.INFORMATIONTECHNOLOGY, locale, ResourceManager.getResource(Branch.class, "EnumINFORMATIONTECHNOLOGY"));
		server.createEnumeration(Branch.class, Branch.HEALTHCARE, locale, ResourceManager.getResource(Branch.class, "EnumHEALTHCARE"));

		server.createEnumeration(Currency.class, Currency.CHF, locale, "CHF");
		server.createEnumeration(Currency.class, Currency.EUR, locale, "EUR");
		server.createEnumeration(Currency.class, Currency.USD, locale, "USD");
		server.createEnumeration(Currency.class, Currency.NOK, locale, "NOK");
		server.createEnumeration(Currency.class, Currency.DKK, locale, "DKK");
		server.createEnumeration(Currency.class, Currency.SEK, locale, "SEK");
		server.createEnumeration(Currency.class, Currency.ISK, locale, "ISK");
		server.createEnumeration(Currency.class, Currency.AUD, locale, "AUD");
		server.createEnumeration(Currency.class, Currency.JPY, locale, "JPY");
		server.createEnumeration(Currency.class, Currency.CNY, locale, "CNY");
		server.createEnumeration(Currency.class, Currency.INR, locale, "INR");
		server.createEnumeration(Currency.class, Currency.CFA, locale, "CFA");
		server.createEnumeration(Currency.class, Currency.CAD, locale, "CAD");
		server.createEnumeration(Currency.class, Currency.XAF, locale, "XAF");
		server.createEnumeration(Currency.class, Currency.BRL, locale, "BRL");
		server.createEnumeration(Currency.class, Currency.NZD, locale, "NZD");
		server.createEnumeration(Currency.class, Currency.HKD, locale, "HKD");
		server.createEnumeration(Currency.class, Currency.RUB, locale, "RUB");

		server.createEnumeration(CostExponent.class, CostExponent.ASIS, locale, ResourceManager.getResource(CostExponent.class, "CIAsIs"));
		server.createEnumeration(CostExponent.class, CostExponent.INTHOUSAND, locale, ResourceManager.getResource(CostExponent.class, "CIInThousand"));
		server.createEnumeration(CostExponent.class, CostExponent.INMILLION, locale, ResourceManager.getResource(CostExponent.class, "CIInMillions"));

		server.createEnumeration(SupplierInfluence.class, SupplierInfluence.REPRESSIVE2, locale,
			ResourceManager.getResource(SupplierInfluence.class, "CIRepressive2"));
		server.createEnumeration(SupplierInfluence.class, SupplierInfluence.REPRESSIVE1, locale,
			ResourceManager.getResource(SupplierInfluence.class, "CIRepressive1"));
		server.createEnumeration(SupplierInfluence.class, SupplierInfluence.NEUTRAL0, locale,
			ResourceManager.getResource(SupplierInfluence.class, "CINeutral0"));
		server.createEnumeration(SupplierInfluence.class, SupplierInfluence.AIDING1, locale, ResourceManager.getResource(SupplierInfluence.class, "CIAiding1"));
		server.createEnumeration(SupplierInfluence.class, SupplierInfluence.AIDING2, locale, ResourceManager.getResource(SupplierInfluence.class, "CIAiding2"));

		server.createEnumeration(LifeCycle.class, LifeCycle.DEVELOPMENT, locale, ResourceManager.getResource(LifeCycle.class, "CIDevelopment"));
		server.createEnumeration(LifeCycle.class, LifeCycle.OPERATION, locale, ResourceManager.getResource(LifeCycle.class, "CIOperation"));
		server.createEnumeration(LifeCycle.class, LifeCycle.DISPLACEMENT, locale, ResourceManager.getResource(LifeCycle.class, "CIDisplacement"));
	}

	/**
	 * Move an object from its "owner" to given target.
	 *
	 * @param object to be moved
	 * @param target new owner
	 * @see ch.softenvironment.view.tree.AutoScrollingTree, TreeDragSource, TreeDragTarget
	 */
	@Override
	public void relocateElement(java.lang.Object object, java.lang.Object target) throws Exception {
		TcoObject element = (TcoObject) object;

		// check whether child-object may be added to target
		String msg = isAddable(element, target);
		if (msg == null) {
			// 1) detach source
			removeOwnedElementSimple(element);
			// 2) attach destination (object is in the "air" now)
			addOwnedElement(target, element);
			LauncherView.getInstance().getPnlNavigation().selectElement(object);
		} else {
			throw new UserException(msg, "Move fault");
		}
	}

	/**
	 * Return whether object may be owned by owner.
	 *
	 * @param object child for potential owner
	 * @param owner
	 * @return non-acceptable reason or null if ok
	 */
	@Override
	public String isAddable(Object object, Object owner) {
		if ((object == null) || (owner == null)) {
			return ResourceManager.getResource(ModelUtility.class, "CWUnknownSourceTarget");
		}

		// TODO 0) analyze Dependency influence (circular for e.g.)
		/*
		 * if ((element.getClientId().size() > 0) || (element.getSupplierId().size() > 0)) { ch.softenvironment.util.Tracer.getInstance().nyi(this,
		 * "relocateElement()", "relocate Dependencies"); throw new ch.softenvironment.util.UserException(this, "relocateElement()",
		 * "Cannot be moved because of dependencies!" ); }
		 */

		// 1) object and owner must not be the same one => would be a recursive
		// copy
		if (object.equals(owner)) {
			return ResourceManager.getResource(ModelUtility.class, "CWSelfDrop");
		}

		// 2) check owner chain => relevant for "ownedElement" (sub-groups) only
		Object child = owner;
		Object parent = null;
		do {
			parent = findParent(child);
			if ((parent != null) && parent.equals(object)) {
				// object is parent of target => moving would loose object-tree
				return ResourceManager.getResource(ModelUtility.class, "CWMoveSubtree");
			}
			// go down to root
			child = parent;
		} while (parent != null);

		// 3) check potential owner-child combination
		boolean valid = ((owner instanceof TcoPackage) && ((object instanceof TcoPackage) || (object instanceof Service)))
			|| ((owner instanceof Service) && (object instanceof CostDriver)) || ((owner instanceof CostDriver) && (object instanceof Cost));
		if (!valid) {
			// Tracer.getInstance().debug(this, "isAddable()",
			// "checking: owner (" + ((TcoObject)owner).getName() +
			// ") becomes child(" + ((TcoObject)object).getName() + ")");
			return ResourceManager.getResource(ModelUtility.class, "CWAddOwnedElement");
		}

		// 4) object already attached to owner
		Long ownerId = ((TcoObject) owner).getId();
		if (((object instanceof Service) && (owner instanceof TcoPackage) && (((Service) object).getGroupId().equals(ownerId)))
			|| ((object instanceof TcoPackage) && (owner instanceof TcoPackage) && (((TcoPackage) object).getNamespaceId().equals(ownerId)))
			|| ((object instanceof CostDriver) && (owner instanceof Service) && (((CostDriver) object).getServiceId().equals(ownerId)))
			|| ((object instanceof Cost) && (owner instanceof CostDriver) && (((Cost) object).getDriverId().equals(ownerId)))) {
			return ResourceManager.getResource(ModelUtility.class, "CWAlreadyOwned");
		}

		return null;
	}

	/**
	 * A Dependency is an n:n Association between a Client- and a Supplier-TcoObject. Remove given dependency from given client.
	 *
	 * @param dependency
	 */
	public void removeDependencyByClient(TcoObject client, Dependency dependency) {
		TcoObject supplier = findSupplier(dependency);

		// unlink dependency
		supplier.getClientId().remove(dependency);
		client.getSupplierId().remove(dependency); // => MUST BE DONE BY CALLER!

		// unlink client/supplier
		// dependency.setSupplierId(null); => (DbState=DELETED already!!!)
		// dependency.setClientId(null);
	}

	/**
	 * Remove element from owner.
	 *
	 * @param element ch.softenvironment.tree.core.Element
	 */
	@Override
	public void removeOwnedElement(java.lang.Object element) {
		TcoObject object = (TcoObject) element;

		// TODO create TransactionBlock

		// remove presentation
		Diagram diagram = getDependencyDiagram();
		if (diagram != null) {
			Iterator<PresentationElement> pElements = diagram.getPresentationElement().iterator();
			while (pElements.hasNext()) {
				PresentationElement pel = pElements.next();
				if ((pel.getSubject() != null) && (pel.getSubject().equals(object))) {
					pElements.remove();
				}
			}
		}

		// any Dependencies must be unlinked from Supplier-View
		Iterator<Dependency> iterator = object.getClientId().iterator();
		while (iterator.hasNext()) {
			// Tracer.getInstance().debug(this, "removeOwnedElement()",
			// "removing Client-Dependency");
			Dependency dependency = iterator.next();
			dependency.getObjectServer().deletePersistent(dependency);

			// TcoObject supplier = object; //findSupplier(dependency);
			TcoObject client = findClient(dependency);

			// unlink client
			dependency.setClientId(null);
			client.getSupplierId().remove(dependency);

			// unlink supplier
			dependency.setSupplierId(null);
			iterator.remove(); // supplier.getClientId().remove(dependency);
		}
		// any Dependencies must be unlinked from Client-View
		iterator = object.getSupplierId().iterator();
		while (iterator.hasNext()) {
			// Tracer.getInstance().debug(this, "removeOwnedElement()",
			// "removing Supplier-Dependency");
			Dependency dependency = iterator.next();
			dependency.getObjectServer().deletePersistent(dependency);
			removeDependencyByClient(object, dependency);
			iterator.remove(); // client().getSupplierId().remove(dependency);
		}

		// do remove Dependencies down the subtree
		if (object instanceof TcoPackage) {
			Iterator<TcoPackage> itGroup = ((TcoPackage) object).getOwnedElement().iterator();
			while (itGroup.hasNext()) {
				removeOwnedElement(itGroup.next());
			}
			Iterator<Service> itService = ((TcoPackage) object).getService().iterator();
			while (itService.hasNext()) {
				removeOwnedElement(itService.next());
			}
		}

		// at last remove given element
		removeOwnedElementSimple(object);
		(object).getObjectServer().deletePersistent(object);
	}

	/**
	 * Remove element from owner.
	 *
	 * @param element either Object to be removed
	 */
	private void removeOwnedElementSimple(TcoObject element) {
		TcoObject owner = (TcoObject) findParent(element);

		if (element instanceof TcoPackage) {
			// 1) unlink owner from element
			((TcoPackage) element).setNamespaceId(null);

			// 2) unlink element
			java.util.List<TcoPackage> ownedElements = new java.util.ArrayList<>(((TcoPackage) owner).getOwnedElement());
			ownedElements.remove(element);
			((TcoPackage) owner).setOwnedElement(ownedElements); // triggers
			// node
			// structure
		} else if (element instanceof Service) {
			// 1) unlink owner from element
			((Service) element).setGroupId(null);

			// 2) unlink element
			java.util.List<Service> services = new java.util.ArrayList<>(((TcoPackage) owner).getService());
			services.remove(element);
			((TcoPackage) owner).setService(services); // triggers node
			// structure
		} else if (element instanceof CostDriver) {
			// 1) unlink owner from element
			((CostDriver) element).setServiceId(null);

			// 2) unlink element
			java.util.List<CostDriver> drivers = new java.util.ArrayList<>(((Service) owner).getDriver());
			drivers.remove(element);
			((Service) owner).setDriver(drivers); // triggers node structure
		} else if (element instanceof Cost) {
			// 1) unlink owner from element
			((Cost) element).setDriverId(null);

			// 2) unlink element
			java.util.List<Cost> costs = new java.util.ArrayList<>(((CostDriver) owner).getCost());
			costs.remove(element);
			((CostDriver) owner).setCost(costs); // triggers node structure
		}
	}

	public void removeCourse(List<Course> courses) throws Exception {
		// 1) unlink owner from element
		Iterator<Course> it = courses.iterator();
		while (it.hasNext()) {
			it.next().setSystemParameterId(null);
		}

		// 2) unlink element
		java.util.List<Course> copy = new java.util.ArrayList<>(getSystemParameter().getCourse());
		copy.removeAll(courses);
		getSystemParameter().setCourse(copy); // triggers node structure

		// 3) remove persistently
		((TcoModel) getRoot()).getObjectServer().deletePersistentAll(courses);
	}

	/**
	 * Return InterestRate according to SystemParameter settings.
	 */
	public double getInterestRate() {
		Double interest = null;
		try {
			if (getSystemParameter().getDepreciationInterestRate() == null) {
				getSystemParameter().setDepreciationInterestRate(Double.valueOf(4.0));
			}
			interest = getSystemParameter().getDepreciationInterestRate();
		} catch (Exception e) {
			log.warn("SystemParameter fault <DepreciationInterestRate>=4.0", e);
			interest = Double.valueOf(4.0);
		}
		return interest.doubleValue();
	}

	/**
	 * Return InterestRate according to SystemParameter settings.
	 */
	public long getManYearHoursInternal() {
		Long hours = null;
		try {
			if (getSystemParameter().getManYearHoursInternal() == null) {
				getSystemParameter().setManYearHoursInternal(Long.valueOf(1700));
			}
			hours = getSystemParameter().getManYearHoursInternal();
		} catch (Exception e) {
			log.warn("SystemParameter fault <ManYearHoursInternal>=1700", e);
			hours = Long.valueOf(1700);
		}
		return hours.longValue();
	}

	/**
	 * Sets the string to display for this object.
	 */
	@Override
	public void setName(Object node, String newName) {
		if (node instanceof TcoObject) {
			((TcoObject) node).setName(newName);
		}
	}

	/**
	 * @see #getMultitudeFactor(TcoObject, boolean)
	 */
	public static double getMultitudeFactor(TcoObject object) {
		return object.getMultitude() == null ? 0.0 : object.getMultitude().doubleValue();
	}

	/**
	 * Determine the multitude of given object.
	 *
	 * @param object
	 * @param recursive true->multiplied by hierarchy down to the root; false->only object's multitude is considered
	 */
	public double getMultitudeFactor(TcoObject object, boolean recursive) {
		double factor = getMultitudeFactor(object);
		if (recursive && !(object instanceof TcoModel)) {
			factor = factor * getMultitudeFactor((TcoObject) findParent(object), recursive);
		}
		return factor;
	}

	public DbObjectServer getServer() {
		if (model != null) {
			return model.getObjectServer();
		} else {
			return null;
		}
	}

	/**
	 * @return duration in month
	 * @see SystemParameter
	 */
	public long getDepreciationDuration() {
		Long duration = Long.valueOf(ReportTool.DEFAULT_DURATION);
		try {
			if (getSystemParameter().getReportDepreciationDuration() == null) {
				getSystemParameter().setReportDepreciationDuration(duration);
			} else {
				duration = getSystemParameter().getReportDepreciationDuration();
			}
		} catch (Exception e) {
			log.warn("SystemParameter fault <ReportDepreciationDuration>=default", e);
		}
		return duration.longValue();
	}

	/**
	 * @return duration in month
	 * @see SystemParameter#getReportUsageDuration()
	 */
	public long getUsageDuration() {
		Long duration = Long.valueOf(ReportTool.DEFAULT_DURATION);
		try {
			if (getSystemParameter().getReportUsageDuration() == null) {
				getSystemParameter().setReportUsageDuration(duration);
			} else {
				duration = getSystemParameter().getReportUsageDuration();
			}
		} catch (Exception e) {
			log.warn("SystemParameter fault <ReportUsageDuration>=default", e);
		}
		return duration.longValue();
	}
}
