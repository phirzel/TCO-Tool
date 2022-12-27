package ch.softenvironment.jomm.target.sql.msaccess;
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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test MsAccessQueryBuilder.
 *
 * @author Peter Hirzel
 * @deprecated not used by TCO-Tool
 */
@Deprecated(since = "1.6.0")
public class MsAccessQueryBuilderTestCase {

    //  @see ch.softenvironment.demoapp.MsAccessTestSuite to run this TestCase
    private DbObjectServer server = null;

    @Before
    public void setUp() throws java.lang.Exception {
        server = DbDomainNameServer.getDefaultServer();
    }

    @Ignore("not used for TCO-Tool")
    @Test
    public void doSelect() {
        DbQueryBuilder builder = new MsAccessQueryBuilder(server, DbQueryBuilder.SELECT, "Test SELECT");
        builder.setAttributeList("*");
        builder.setTableList("MYTABLE");
        builder.addFilter("MYATTR", 3L);

        assertEquals("DbQueryBuilder", "SELECT * FROM MYTABLE WHERE (MYATTR=3)", builder.getQuery());
    }
}
