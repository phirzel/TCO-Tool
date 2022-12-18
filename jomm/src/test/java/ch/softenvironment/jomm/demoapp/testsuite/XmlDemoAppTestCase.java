package ch.softenvironment.jomm.demoapp.testsuite;

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
 */

import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbUserTransactionBlock;
import ch.softenvironment.jomm.demoapp.XmlMapperSuite;
import ch.softenvironment.jomm.demoapp.model.Project;
import ch.softenvironment.jomm.demoapp.model.RoleType;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.target.xml.IliBasket;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.util.ListUtils;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * JUnit Testcase to testsuite JOMM with DemoApp-Model. This Testcase will: - create some Test-Data - manipulate the Test-Data
 *
 * @author Peter Hirzel
 * @see @see ch.softenvironment.demoapp.*TestSuite to run this TestCase
 */
@Slf4j
public class XmlDemoAppTestCase extends TestCase {

	private final String filename = "C:\\Temp\\" + XmlMapperSuite.SCHEMA + ".xml";
	private XmlObjectServer server = null;
	private XmlDemoAppModel root = null;

	@Override
	protected void setUp() {
		server = (XmlObjectServer) DbDomainNameServer.getDefaultServer();
		try {
			root = (XmlDemoAppModel) server
				.createInstance(XmlDemoAppModel.class);
			root.save();
		} catch (Exception e) {
			fail("creation of root failed: " + e.getLocalizedMessage());
		}
	}

	public void testModelMapping() {
		try {
			Project prj = (Project) server.createInstance(Project.class);
			assertTrue("DbState->NEW", prj.getPersistencyState().isNew());
			prj.setName(DemoAppTestCase.PROJECT_NAME);
			prj.setActive(Boolean.TRUE);
			prj.setStart(new Date());
			prj.save();
			assertTrue("DbState->SAVED", prj.getPersistencyState().isSaved());

			List projects = DemoAppTestCase.generatData(server,
				DemoAppTestCase.COUNTER);
			projects.add(prj);
			root.setProjects(projects);
			reload();
			assertTrue("Number of generated Projects's", root.getProjects()
				.size() /*
			 * ((Number)server.getFirstValue(builder)).longValue(
			 * )
			 */ == (1 + DemoAppTestCase.COUNTER));

			DbEnumeration enumeration = server.retrieveEnumeration(
				RoleType.class, RoleType.DEVELOPER);
			assertTrue("RoleType->Developer", (enumeration != null)
				&& DbEnumeration.isIliCode(enumeration, RoleType.DEVELOPER));

			// List nps = queryData();
			// assertTrue("NaturalPerson's", (nps != null) && (nps.size() == 8
			// /*depends on counter*/));

			// List prjs = querySessionBean();
			// assertTrue("ProjectSessionBean's", (prjs != null) && (prjs.size()
			// == 1));

			prj.remove(true);
			assertTrue("Project->removed", prj.getPersistencyState()
				.isRemoved());
		} catch (Exception e) {
			log.error("testModelMapping", e);
			fail("testModelMapping()");
		}
	}

	protected XmlDemoAppModel openFile() throws Exception {
		try {
			server.reconnect();
			XmlMapperSuite.mapEnumerations(server);

			server.setUserObject(new ch.softenvironment.jomm.target.xml.XmlUserObject(
				filename));
			List baskets = ListUtils.createList(new XmlDemoAppBasket());
			server.retrieveAll(baskets);
			server.setUserObject(null);
			return (XmlDemoAppModel) (((IliBasket) baskets.get(0))
				.getModelRoot());
		} catch (javax.jdo.JDODataStoreException e) {
			if (e.getCause() instanceof FileNotFoundException) {
				fail("File missing: " + filename + "->"
					+ e.getLocalizedMessage());
			} else {
				fail("unknown read error for: " + filename + "->"
					+ e.getLocalizedMessage());
			}
			return null;
		}
	}

	private void saveFile() throws Exception {
		// ((TcoModel)getUtility().getRoot()).setName(StringUtils.getPureFileName(filename));

		ch.softenvironment.jomm.target.xml.XmlUserObject userObject = new ch.softenvironment.jomm.target.xml.XmlUserObject(
			filename);
		userObject.setSender("ch.softenvironment.jomm.xml.testsuite (V1.0.0)");
		// TODO NYI: XSD
		userObject.setXsd("http://www.interlis.ch/INTERLIS2.2 "
			+ XmlMapperSuite.SCHEMA + ".xsd");
		// userObject.setXsl("http://www.tcotool.org/XSLT/TCOModel_Tree.xsl");
		final DbObjectServer server = DbDomainNameServer.getDefaultServer();
		server.setUserObject(userObject);
		final DbUserTransactionBlock block = server
			.createUserTransactionBlock(true);
		block.execute(new Runnable() {
			@Override
			public void run() {
				IliBasket basket = new XmlDemoAppBasket();
				basket.setModelRoot(/* LauncherView.getInstance(). */root);
				block.getObjectServer().makePersistentAll(
					ListUtils.createList(basket));
			}
		});
		// server.makePersistent(getUtility().getRoot());
		server.setUserObject(null);
		// PersistenceService service = new PersistenceService();
		// service.writeFile(filename, getModel());

		ch.softenvironment.jomm.mvc.controller.ConsistencyController
			.cascadedSaved();
	}

	private void reload() {
		try {
			saveFile();
			root = openFile();
		} catch (Exception e) {
			fail("reload()->" + e.getLocalizedMessage());
		}
	}
}
