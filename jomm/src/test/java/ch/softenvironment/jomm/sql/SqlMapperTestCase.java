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
import ch.softenvironment.util.DeveloperException;

/**
 * Test DbMapper
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.4 $ $Date: 2007-09-18 12:47:30 $
 * @deprecated not used by TCO-Tool
 */
@Deprecated(since = "1.6.0")
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
		if (DbDomainNameServer.getDefaultServer() == null) {
			throw new DeveloperException("must be executed within a <DB-specific> *TestSuite");
		}
		mapper = DbDomainNameServer.getDefaultServer().getMapper();
	}

	/**
	 *
	 */
	public void testMapToTarget_String() {
		assertEquals("DbMapper", "'Hello'", mapper.mapToTarget("Hello"));
		assertEquals("'a''d and o''d'", mapper.mapToTarget("a'd and o'd"));
	}

	public void testMapToTarget_Boolean() {
		assertEquals("DbMapper", "'T'", mapper.mapToTarget(Boolean.TRUE));
		assertEquals("DbMapper", "'F'", mapper.mapToTarget(Boolean.FALSE));
		assertEquals("DbMapper", SqlQueryBuilder.SQL_NULL, mapper.mapToTarget((Boolean) null));
	}
}
