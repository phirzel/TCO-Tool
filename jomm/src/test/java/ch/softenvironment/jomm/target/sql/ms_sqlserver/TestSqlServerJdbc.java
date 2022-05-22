package ch.softenvironment.jomm.target.sql.ms_sqlserver;

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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

/**
 * Microsoft SQL Server 2005 JDBC testsuite program, using 2 different Drivers: - Microsoft JDBC Driver (original) - jdts open source Driver (sourceforge.net)
 * <p>
 * Make sure the following services run in Windows to enable connection: - SQL Server (Express or commercial edition) - SQL Browser
 * <p>
 * http://msdn.microsoft.com/downloads/default.asp?URL=/downloads/sample.asp?url=/MSDN-FILES/027/001/779/msdncompositedoc.xml http://support.microsoft.com/default.aspx?scid=KB;EN-US;Q313100&
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.1 $ $Date: 2006-04-25 14:57:54 $
 */
public class TestSqlServerJdbc {

    public static void testConnect(String driverClass, String url) {
        try {
            Class.forName(driverClass);

            //When the driver is loaded, you can establish a connection by using a connection URL:

            Connection connection = DriverManager.getConnection(url);

            if (connection != null) {
                System.out.println("CONNECT->OK (" /*+ driverClass + "-"*/ + url + ")");

                // Meta data
                DatabaseMetaData meta = connection.getMetaData();

                System.out.println("Driver Information");
                System.out.println("\tDriver Name: " + meta.getDriverName());
                System.out.println("\tDriver Version: " + meta.getDriverVersion());
                System.out.println("\nDatabase Information ");
                System.out.println("\tDatabase Name: " + meta.getDatabaseProductName());
                System.out.println("\tDatabase Version: " + meta.getDatabaseProductVersion());

                // Select some data
    /*
    	    Statement select = connection.createStatement();
    	    ResultSet result = select.executeQuery("SELECT msisdn FROM member");
    
    	    while (result.next()) {
    
    	      System.out.println(result.getString(1));
    	    }
    */
                connection.close();
            }
        } catch (Throwable e) {
            System.out.println("FAILED (" /*+ driverClass + "-"*/ + url + "): " + e.getLocalizedMessage());
        }
    }

    /**
     * Use Microsoft JDBC Driver.
     *
     * @param url
     */
    private static void testMS(String url) {
      /*
      1) DRIVER LOAD CONFLICT WITH SQL SERVER 2000 JDBC DRIVER

      If you load both the Microsoft SQL Server 2000 JDBC Driver and the
      Microsoft SQL Server 2005 JDBC Driver in the same process, in some
      cases the 2000 version of the JDBC driver will improperly accept a
      DriverManager.getConnection method call that is targeted for the 
      2005 version of the JDBC driver.

      The problem is caused by the 2000 version of the JDBC driver
      incorrectly accepting the "jdbc:sqlserver://" URL prefix if it is
      loaded first.  

      Microsoft is aware of this issue and is planning to fix it in the
      2000 version of the JDBC Driver.

      To work around this issue, load the 2005 version of the JDBC driver
      class first.
      */
        testConnect("com.microsoft.sqlserver.jdbc.SQLServerDriver", // 2005
            //            "com.microsoft.jdbc.sqlserver.SQLServerDriver", // 2000 version
            "jdbc:sqlserver://" + url);
    }

    /**
     * Use open source Driver jdts.
     *
     * @param url
     */
    private static void testJtds(String url) {
        testConnect("net.sourceforge.jtds.jdbc.Driver",
            "jdbc:jtds:sqlserver://" + url);
    }

    public static void main(String[] args) throws Exception {
        String instance = "SANDFLYER\\SQLEXPRESS";
        String db = "ph;";
        String id = "user=phirzel;password=xxx"; //"user=sa;password=r00t";
        //"integrated security=SSPI"; // use Windows authentication

        System.out.println("MS SQL Server Driver");
        //testMS(instance);
        testMS(instance + ";" + id);
        testMS(instance + ";databaseName=ph;" + id);
        //testMS(instance + ";databaseName=ph;" + "user=sa;password=WRONG");
        testMS(instance + ";databaseName=ph;" + "user=sa;password=r00t");
        //testMS(instance + ":1433;databaseName=ph;" + "user=sa;password=r00t");
        //testMS(instance + ":1433;databaseName=ph;" + id);
        testMS(instance + ";" + "user=sa;password=r00t"); // root user
        //testMS(instance + ";" + "user=sa;password=r00t;");
        //testMS("localhost;" + id);
        testMS("localhost\\SQLEXPRESS;" + id);
        //testMS("localhost\\SQLEXPRESS:1433;" + id);
          /*UnkownHostException
          testMS("localhost:1433;"+ id);
          testMS("SQLEXPRESS:1433;" + id);
          testMS("SQLEXPRESS;"+ id);
          */

        System.out.println("jtds Driver");
        //To check whether TCP/IP is enabled and the port is not blocked you can use "telnet <server_host> 1433". Until telnet doesn't connect, jTDS won't either. If you can't figure out why, ask your network administrator for help.
        testJtds("localhost:1433/ph;" + id + ";instance=SQLEXPRESS"); //+ "TDS=8.0;";
        testJtds("SANDFLYER:1433/ph;" + id + ";instance=SQLEXPRESS");
          /*
          testJtds(instance + "/ph;" + id);
          testJtds(instance + ";" + id);
          testJtds("localhost\\SQLEXPRESS/ph;" + id);
          testJtds("localhost\\SQLEXPRESS;" + id);
          testJtds("SQLEXPRESS/ph;" + id);
          testJtds("SQLEXPRESS;" + id);
          */
        //TODO NamedPipe connection fails
        testJtds("localhost/ph;" + id + ";instance=SQLEXPRESS" + ";namedPipe=true");
        testJtds("localhost;" + id + ";instance=SQLEXPRESS" + ";namedPipe=true");
        //        Unkown server host name
          /*
          testJtds(instance);
          testJtds(instance + ";" + id);
          testJtds("localhost\\SQLEXPRESS:1433/ph;" + id);
          testJtds("SQLEXPRESS:1433/ph;" + id);
          testJtds("SQLEXPRESS/ph;" + id);
          testJtds("SQLEXPRESS/ph;TDS=8.0;" + id);
          */
    }
}