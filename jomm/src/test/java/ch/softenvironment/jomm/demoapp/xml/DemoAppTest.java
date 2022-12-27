package ch.softenvironment.jomm.demoapp.xml;
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
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.demoapp.XmlObjectMapperTest;
import ch.softenvironment.jomm.demoapp.model.*;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.tools.DbDataGenerator;
import ch.softenvironment.util.DeveloperException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit Testcase to testsuite JOMM with DemoApp-Model. This Testcase will: - create some Test-Data - manipulate the Test-Data see ch.softenvironment.jomm.demoapp.*TestSuite to run this TestCase
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DemoAppTest {

	public static final String PROJECT_NAME = "MyProject";
	public static final long COUNTER = 15; // number of testsuite-records to be generated

	private DbObjectServer server = null;

	@Before
	public void setUp() {
		if (DbDomainNameServer.getDefaultServer() == null) {
			throw new DeveloperException("must be executed by XmlMapperSuite");
		}
		server = DbDomainNameServer.getDefaultServer();
	}

	public static Project createProject(@NonNull DbObjectServer server) {
		try {
			Project prj = (Project) server.createInstance(Project.class);
			assertTrue("DbState->NEW", prj.getPersistencyState().isNew());
			prj.setName(PROJECT_NAME);
			prj.setActive(Boolean.TRUE);
			prj.setStart(new Date());
			prj.save();

			assertTrue("DbState->SAVED", prj.getPersistencyState().isSaved());

			return prj;
		} catch (Exception e) {
			fail(e.getMessage());
		}
		return null;
	}

	/**
	 * @see XmlObjectMapperTest#modelMapping()
	 */
	@Test
	@Ignore
	public void modelMapping() {
		try {
			Project project = createProject(server);
			generatData(server, COUNTER);

			DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, "Count Person");
			builder.setAttributeList("COUNT(*)");
			builder.setTableList(Person.class);
			assertEquals("Number of generated Person's", COUNTER, ((Number) server.getFirstValue(builder)).longValue());
			// OR
			builder = server.createQueryBuilder(DbQueryBuilder.RAW, "Count Person");
			builder.setRaw("SELECT COUNT(*) FROM Person");
			assertEquals("Number of generated Person's", COUNTER, ((Number) server.getFirstValue(builder)).longValue());

			DbEnumeration enumeration = server.retrieveEnumeration(RoleType.class, RoleType.DEVELOPER);
			assertTrue("RolyType->Developer", (enumeration != null) && DbEnumeration.isIliCode(enumeration, RoleType.DEVELOPER));

			List<NaturalPerson> nps = queryData();
			assertTrue("NaturalPerson's", (nps != null) && (nps.size() == 8 /*depends on counter*/));

			List<ProjectSessionBean> prjs = querySessionBean();
			assertTrue("ProjectSessionBean's", (prjs != null) && (prjs.size() == 1));

			project.remove(true);
			assertTrue("Project->removed", project.getPersistencyState().isRemoved());
		} catch (Exception e) {
			log.error("testModelMapping", e);
			fail("testModelMapping()");
		}
	}

	public static List<Project> generatData(DbObjectServer server, long counter) throws Exception {
		final List<Project> projects = new ArrayList<>((int) counter);
		DbDataGenerator generator = new DbDataGenerator();
		boolean isLegalPerson = true;
		for (long i = 0; i < counter; i++) {
			Project project = (Project) server.createInstance(Project.class);
			project.setName(generator.getRandomString(255));
			//TODO         	project.setMemberId(ListUtils.createList(np));
			project.setActive(isLegalPerson);
			project.setStart(new Date());
			project.save();
			projects.add(project);

			isLegalPerson = !isLegalPerson;
			if (isLegalPerson) {
				LegalPerson lp = (LegalPerson) server.createInstance(LegalPerson.class);
				lp.setName(generator.getRandomString(255));
				lp.setFormation(generator.getRandomDate(1940, 2010));
				lp.setCompanyType((CompanyType) server.retrieveEnumeration(CompanyType.class, CompanyType.AG));
				lp.save();
			} else {
				NaturalPerson np = (NaturalPerson) server.createInstance(NaturalPerson.class);
				np.setName(generator.getRandomString(255));
				np.setBirthday(generator.getRandomDate(1940, 2010));
				np.setFirstName(generator.getRandomString(100));
				np.setSex(generator.getRandomBoolean());
				//TODO         		np.setProjectId(ListUtils.createList(project));
				np.save();

				Role role = (Role) server.createInstance(Role.class);
				role.setMemberId(np.getId());
				role.setProjectId(project.getId());
				role.setType((RoleType) server.retrieveEnumeration(RoleType.class, RoleType.DEVELOPER));
				role.setPercentage((double) generator.getGenerator().nextInt(100));
				role.save();

				Activity activity = (Activity) server.createInstance(Activity.class);
				activity.setDescription(generator.getRandomString(1024));
				/*24h*/
				activity.setEffort((double) generator.getGenerator().nextInt(5184000));
				activity.setProjectId(project.getId());
				activity.setRoleId(role.getId());
				//	         	activity.setWorkProductId(wp.getId());
				activity.setPhase((Phase) server.retrieveEnumeration(Phase.class, Phase.CONSTRUCTION));

				WorkProduct wp = (WorkProduct) server.createInstance(WorkProduct.class);
				wp.getName().setValue(generator.getRandomString(250));
				wp.save();
			}
		}
		return projects;
	}

	private List<NaturalPerson> queryData() throws Exception {
		DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, "All " + NaturalPerson.class.getName());
		builder.setTableList(NaturalPerson.class);

		return server.retrieveAll(NaturalPerson.class, builder);
	}

	private List<ProjectSessionBean> querySessionBean() throws Exception {
		DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, ProjectSessionBean.class.getName());
		builder.setTableList(Project.class);
		builder.addFilter("name", PROJECT_NAME, DbQueryBuilder.WILD);
		builder.addOrder("name");

		return server.retrieveAll(ProjectSessionBean.class, builder);
	}
}
