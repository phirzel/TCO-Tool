package ch.softenvironment.jomm.sql;

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
import ch.softenvironment.jomm.DbMapper;
import ch.softenvironment.jomm.target.sql.SqlQueryBuilder;

/**
 * Test DbMapper
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.4 $ $Date: 2007-09-18 12:47:30 $
 */
public class SqlMapperTestCase extends junit.framework.TestCase {

	private DbMapper mapper = null;

	public SqlMapperTestCase() {
		super();
	}

	/**
	 * StringUtilsTestCase constructor comment.
	 *
	 * @param name java.lang.String
	 */
	public SqlMapperTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws java.lang.Exception {
		mapper = DbDomainNameServer.getDefaultServer().getMapper(); //new SqlMapper();
	}

	/**
	 *
	 */
	public void testMapToTarget_String() {
		assertTrue("DbMapper", "'Hello'".equals(mapper.mapToTarget("Hello")));
		assertTrue(mapper.mapToTarget("a'd and o'd").equals("'a''d and o''d'"));
	}

	public void testMapToTarget_Boolean() {
		assertTrue("DbMapper", "'T'".equals(mapper.mapToTarget(Boolean.TRUE)));
		assertTrue("DbMapper", "'F'".equals(mapper.mapToTarget(Boolean.FALSE)));
		assertTrue("DbMapper", SqlQueryBuilder.SQL_NULL.equals(mapper.mapToTarget((Boolean) null)));
	}
}
