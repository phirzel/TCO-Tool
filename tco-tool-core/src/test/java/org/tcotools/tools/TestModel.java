package org.tcotools.tools;

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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import org.tcotool.model.Process;
import org.tcotool.model.*;
import org.tcotool.tools.ModelUtility;

/**
 * Create a simple Test TcoModel.
 *
 * @author Peter Hirzel
 */
public class TestModel {

	// define public for easier test-access
	public XmlObjectServer server;
	public ModelUtility utility;
	public TcoModel model;
	public TcoPackage group;
	public Service clientService;
	public Service supplierService;
	public CostDriver clientDriver;
	public CostDriver supplierDriver;
	public Role role;
	public Catalogue catalogue;
	public Site site;
	public Process process;
	public ProjectPhase phase;

	// public double driverFactor = 2.0 * 3.0 * 4.0 * 5.0;

	private DbObjectServer initializeDatabase() {
		javax.jdo.PersistenceManagerFactory pmFactory = new ch.softenvironment.jomm.target.xml.XmlObjectServerFactory();
		pmFactory.setConnectionURL("tcotool.model");
		// pmFactory.setNontransactionalRead(false); // NO autoCommit while
		// reading
		// pmFactory.setNontransactionalWrite(false); // NO autoCommit while
		// writing

		DbObjectServer server = (DbObjectServer) pmFactory.getPersistenceManager(System.getProperty("user.name"), null);
		org.tcotool.TcotoolUtility.registerClasses(server);
		return server;
	}

	public TestModel() throws Exception {
		// connection & Utility initialization
		server = (XmlObjectServer) initializeDatabase();
		utility = ModelUtility.createDefaultConfiguration(server); // new
		// model.ModelUtility(server);

		// add own codes
		role = (Role) server.createInstance(Role.class);
		role.setFullTimeEquivalent(124000.0);
		role.setYearlyHours(1750L);
		role.setCurrency(utility.getSystemParameter().getDefaultCurrency());
		role.setInternal(Boolean.TRUE);
		role.setEmploymentPercentageAvailable(Long.valueOf(50));
		server.cacheCode(role);

		catalogue = (Catalogue) server.createInstance(Catalogue.class);
		// TODO catalogue.setName(NLS);
		catalogue.setCurrency(utility.getSystemParameter().getDefaultCurrency());
		catalogue.setUsageDuration(Long.valueOf(12));
		catalogue.setDepreciationDuration(Long.valueOf(12));
		catalogue.setPrice(Double.valueOf(100.0));
		// catalogue.setExpendable(Boolean.FALSE);
		catalogue.setDocumentation("Test Catalogue");
		server.cacheCode(catalogue);

		site = (Site) server.createInstance(Site.class);
		// TODO site.setName(name);
		site.setAddress("Lonely Road 15, 3999 Neverland");
		// TODO site.setCoordinates(coordinates);
		server.cacheCode(site);

		process = (Process) server.createInstance(Process.class);
		// TODO process.setName(NLS);
		server.cacheCode(process);

		phase = (ProjectPhase) server.createInstance(ProjectPhase.class);
		// TODO process.setName(NLS);
		server.cacheCode(phase);

		// create Model structure without FactCost or PersonalCost
		model = (TcoModel) utility.getRoot();
		model.setMultitude(2.0);
		group = (TcoPackage) utility.createTcoObject(server, TcoPackage.class);
		group.setMultitude(3.0);
		utility.addOwnedElement(model, group);
		clientService = (Service) utility.createTcoObject(server, Service.class);
		clientService.setMultitude(4.0);
		utility.addOwnedElement(group, clientService);
		clientDriver = (CostDriver) utility.createTcoObject(server, CostDriver.class);
		clientDriver.setMultitude(5.0);
		clientDriver.setPhase(phase);
		clientDriver.setProcess(process);
		utility.addOwnedElement(clientService, clientDriver);
		supplierService = (Service) utility.createTcoObject(server, Service.class);
		supplierService.setMultitude(1.0);
		utility.addOwnedElement(group, supplierService);
		supplierDriver = (CostDriver) utility.createTcoObject(server, CostDriver.class);
		supplierDriver.setMultitude(1.0);
		utility.addOwnedElement(supplierService, supplierDriver);
	}

	public TcoPackage addGroup(TcoPackage owner) throws Exception {
		TcoPackage group = (TcoPackage) utility.createTcoObject(server, TcoPackage.class);
		utility.addOwnedElement(owner, group);
		return group;
	}

	public PersonalCost addClientPersonalCost() throws Exception {
		return addPersonalCost(clientDriver);
	}

	public PersonalCost addPersonalCost(CostDriver driver) throws Exception {
		PersonalCost pCost = (PersonalCost) utility.createTcoObject(server, PersonalCost.class);
		utility.addOwnedElement(driver, pCost);
		pCost.setMultitude(6.0);
		return pCost;
	}

	public FactCost addClientFactCost() throws Exception {
		return addFactCost(clientDriver);
	}

	public FactCost addFactCost(CostDriver driver) throws Exception {
		FactCost fCost = (FactCost) utility.createTcoObject(server, FactCost.class);
		utility.addOwnedElement(driver, fCost);
		fCost.setMultitude(7.0);
		return fCost;
	}

	/**
	 * Return the multiplicity of this TestModel.driver specific driver!
	 *
	 * @return
	 */
	protected double getClientDriverFactor() {
		// return 2.0 * 3.0 * 4.0 * 5.0;
		// return m.utility.getMultitudeFactor(driver, true);
		return model.getMultitude().doubleValue() * // 2.0
			group.getMultitude().doubleValue() * // 3.0
			clientService.getMultitude().doubleValue() * // 4.0
			clientDriver.getMultitude().doubleValue(); // 5.0

	}

	protected double getSupplierDriverFactor() {
		// return 2.0 * 3.0 * 4.0 * 5.0;
		// return m.utility.getMultitudeFactor(driver, true);
		return model.getMultitude().doubleValue() * // 2.0
			group.getMultitude().doubleValue() * // 3.0
			supplierService.getMultitude().doubleValue() * supplierDriver.getMultitude().doubleValue();

	}
}
