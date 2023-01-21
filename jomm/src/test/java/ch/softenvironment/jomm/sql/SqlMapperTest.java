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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test DbMapper
 *
 * @author Peter Hirzel
 * @since 1.4 (2007-09-18)
 * @deprecated not used by TCO-Tool
 */
@Deprecated(since = "1.6.0")
public class SqlMapperTest {

    private DbMapper mapper = null;

    @Before
    public void setUp() throws java.lang.Exception {
        if (DbDomainNameServer.getDefaultServer() == null) {
            throw new DeveloperException("must be executed within a <DB-specific> *TestSuite");
        }
        mapper = DbDomainNameServer.getDefaultServer().getMapper();
    }

    @Test
    @Ignore
    public void mapToTarget_String() {
        assertEquals("DbMapper", "'Hello'", mapper.mapToTarget("Hello"));
        assertEquals("'a''d and o''d'", mapper.mapToTarget("a'd and o'd"));
    }

    @Test
    @Ignore
    public void mapToTarget_Boolean() {
        assertEquals("DbMapper", "'T'", mapper.mapToTarget(Boolean.TRUE));
        assertEquals("DbMapper", "'F'", mapper.mapToTarget(Boolean.FALSE));
        assertEquals("DbMapper", SqlQueryBuilder.SQL_NULL, mapper.mapToTarget((Boolean) null));
    }
}
