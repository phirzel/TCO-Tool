package ch.softenvironment.jomm.demoapp;
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

import ch.softenvironment.jomm.DbObjectServerTest;
import ch.softenvironment.jomm.datatypes.DbNlsStringTest;
import ch.softenvironment.jomm.sql.SqlMapperTest;
import ch.softenvironment.jomm.sql.SqlQueryBuilderTest;
import junit.framework.TestSuite;

/**
 * Suite of SQL-Target TestCases to test JOMM against a real, concrete SQL Target-System.
 *
 * @author Peter Hirzel
 * @deprecated not used by TCO-Tool
 */
@Deprecated(since = "1.6.0")
class SqlSuite extends TestSuite {

    public SqlSuite() {
        super("SQL Target dependent Test-Suite for JOMM");

        addTest(new TestSuite(SqlMapperTest.class));
        addTest(new TestSuite(SqlQueryBuilderTest.class));
        addTest(new TestSuite(DbNlsStringTest.class));
        addTest(new TestSuite(DbObjectServerTest.class));
    }
}
