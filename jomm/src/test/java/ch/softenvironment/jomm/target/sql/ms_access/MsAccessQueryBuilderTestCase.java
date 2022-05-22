package ch.softenvironment.jomm.target.sql.ms_access;
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

import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQueryBuilder;
import junit.framework.TestCase;

/**
 * Test MsAccessQueryBuilder.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class MsAccessQueryBuilderTestCase extends TestCase {

    //  @see ch.softenvironment.demoapp.MsAccessTestSuite to run this TestCase
    private DbObjectServer server = null;

    @Override
    protected void setUp() throws java.lang.Exception {
        server = DbDomainNameServer.getDefaultServer();
    }

    public void testSELECT() {
        DbQueryBuilder builder = new MsAccessQueryBuilder(server, Integer.valueOf(DbQueryBuilder.SELECT), "Test SELECT");
        builder.setAttributeList("*");
        builder.setTableList("MYTABLE");
        builder.addFilter("MYATTR", Long.valueOf(3));

        assertTrue("DbQueryBuilder", "SELECT * FROM MYTABLE WHERE (MYATTR=3)".equals(builder.getQuery()));
    }
}
