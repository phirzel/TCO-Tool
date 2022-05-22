package ch.softenvironment.licence;

import ch.softenvironment.licence.vendor.LicenceGenerator;

/**
 * TestCase for LicenceChecker.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class LicenceCheckerTestCase extends junit.framework.TestCase {

    private ch.softenvironment.licence.vendor.LicenceGenerator generator = null;
    private ch.softenvironment.licence.user.LicenceChecker checker = null;

    // private java.util.Date now = null;

    /**
     * LicenceGeneratorTest constructor comment.
     */
    public LicenceCheckerTestCase(String arg1) {
        super(arg1);
    }

    @Override
    protected void setUp() throws java.lang.Exception {
        generator = new ch.softenvironment.licence.vendor.LicenceGenerator();
        // now = new java.util.Date();
    }

    public void test1User1Month() {
        // create key
        generator.setExpirationDuration(0);
        generator.setNumberOfUsers(Integer.valueOf(1));
        generator.setProductName("TRAr"); // Traffic regional
        generator.setProductVersion("010000");

        // verify key
        checker = new ch.softenvironment.licence.user.LicenceChecker(generator.getKey());
        // assertTrue("expirationDate",
        // now.getYear()checker.getExpirationDate()..);
        assertTrue("User", 1 == checker.getNumberOfUsers());
        assertTrue("Product", checker.getProductName().equals("TRAr"));
    }

    public void test35User2Month() {
        // create key
        generator.setExpirationDuration(1);
        generator.setNumberOfUsers(Integer.valueOf(35));
        generator.setProductName("TRAn"); // Traffic regional
        generator.setProductVersion("010000");

        // verify key
        checker = new ch.softenvironment.licence.user.LicenceChecker(generator.getKey());
        // assertTrue("expirationDate",
        // now.getYear()checker.getExpirationDate()..);
        assertTrue("User", 35 == checker.getNumberOfUsers());
        assertTrue("Product", checker.getProductName().equals("TRAn"));
    }

    public void test998User3Month() {
        // create key
        generator.setExpirationDuration(2);
        generator.setNumberOfUsers(Integer.valueOf(998));
        generator.setProductName("TRAr"); // Traffic regional
        generator.setProductVersion("010000");

        // verify key
        checker = new ch.softenvironment.licence.user.LicenceChecker(generator.getKey());
        // assertTrue("expirationDate",
        // now.getYear()checker.getExpirationDate()..);
        assertTrue("User", 998 == checker.getNumberOfUsers());
        assertTrue("Product", checker.getProductName().equals("TRAr"));
    }

    /**
     * Test expirationDates by hand...
     *
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 8; i++) { // should throw exception if i==7
            LicenceGenerator generator = new ch.softenvironment.licence.vendor.LicenceGenerator();
            generator.setExpirationDuration(i);
            System.out.println("ExpirationIndex: " + i);

            int users = i * 10 + 1;
            if (i == 6) {
                // will throw an exception because expired
                users = 999;
            }
            generator.setNumberOfUsers(Integer.valueOf(users));
            System.out.println("Users: " + users);
            if (i % 2 == 0) {
                generator.setProductName("TRAr"); // regional
                generator.setProductVersion("010000");
                System.out.println("Product/Version: TRAr/010000");
            } else {
                generator.setProductName("TRAn"); // national
                generator.setProductVersion("010000");
                System.out.println("Product/Version: TRAn/010000");
            }
            String key = generator.getKey();
            System.out.println("=> key: " + key);
            System.out.println("----------------------------");

            System.out.println("Verification: ");
            ch.softenvironment.licence.user.LicenceChecker checker = new ch.softenvironment.licence.user.LicenceChecker(key);
            System.out.println("ExpirationDate: " + checker.getExpirationDate());
            System.out.println("Users: " + checker.getNumberOfUsers());
            System.out.println("Product/Version: " + checker.getProductName() + "/" + checker.getProductVersion());
            System.out.println("============================");
        }
    }
}
