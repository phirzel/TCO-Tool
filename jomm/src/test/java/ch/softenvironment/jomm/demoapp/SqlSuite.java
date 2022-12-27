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

import ch.softenvironment.jomm.datatypes.DbNlsStringTestCase;
import ch.softenvironment.jomm.sql.SqlMapperTestCase;
import ch.softenvironment.jomm.sql.SqlQueryBuilderTestCase;
import junit.framework.TestSuite;

/**
 * Suite of SQL-Target TestCases to testsuite JOMM against a real SQL Target-System.
 *
 * @author Peter Hirzel
 * @deprecated not used by TCO-Tool
 */
@Deprecated(since = "1.6.0")
class SqlSuite extends TestSuite {

    public SqlSuite() {
        super("SQL Target dependent Test-Suite for JOMM");

        addTest(new TestSuite(SqlMapperTestCase.class));
        addTest(new TestSuite(SqlQueryBuilderTestCase.class));
        addTest(new TestSuite(DbNlsStringTestCase.class));
        //TODO HIP addTest(new TestSuite(DbObjectServerTestCase.class));
    }
}
