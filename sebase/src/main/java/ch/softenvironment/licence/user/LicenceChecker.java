package ch.softenvironment.licence.user;

import ch.softenvironment.cipher.CipherTool;
import ch.softenvironment.cipher.DecipherTool;
import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.UserException;
import ch.softenvironment.view.BaseDialog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * Tool for an Application to check wether licence is ok.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 */
@Slf4j
@Deprecated(since = "1.6.0")
public class LicenceChecker {

    private transient String licenceFileName = null;
    private transient java.security.Key cryptoKey = null;

    private transient java.util.Date startDate = null;
    private transient java.util.GregorianCalendar expirationDate = null;
    private transient int users = 0;
    private transient String productName = null;
    private final transient String productVersion = null;

    /**
     * Start LicenceChecker checking out licence kept in a file.
     *
     * @param key Containing the licence-Parameters
     */
    public LicenceChecker(String key) {
        super();

        startDate = new java.util.Date();
        checkLicence(key);
    }

    /**
     * Start LicenceChecker checking out licence kept in a file.
     *
     * @param product Name of SW-Product the key is meant for ("MyApp")
     * @param cryptoKey Key to decipher licenceFile
     * @param licenceFile File containing the Licence-Parameters
     */
    public LicenceChecker(String product, java.security.Key cryptoKey, String licenceFile) throws UserException {
        super();

        this.licenceFileName = licenceFile;
        this.cryptoKey = cryptoKey;

        try {
            File inFile = new File(licenceFile);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DecipherTool tool = new DecipherTool(cryptoKey);
            tool.decrypt(new FileInputStream(inFile), out);
            String key = out.toString().substring(0, 19);

            // if already licenced go on here
            int index = out.toString().indexOf(';', 20);
            startDate = new java.util.Date((Long.valueOf(out.toString().substring(20, index))));
            checkLicence(key);
        } catch (FileNotFoundException e) {
            // if not licenced yet
            BaseDialog dialog = new LicenceSetupView(this, product);
            if (!dialog.isSaved()) {
                throw new UserException(ResourceManager.getResource(LicenceChecker.class, "CEInvalidLicence"), null);
            }
        }
    }

    /**
     * Try key and save it if ok.
     */
    protected void checkIn(String key, String holder) throws Exception {
        try {
            // write down the licence
            startDate = new Date();
            checkLicence(key);
            ByteArrayInputStream in = new ByteArrayInputStream((key + ";" + startDate.getTime() + ";" + holder).getBytes());
            File outFile = new File(licenceFileName);
            FileOutputStream out = new FileOutputStream(outFile);
            CipherTool tool = new CipherTool(cryptoKey);
            tool.encrypt(in, out);
        } catch (Exception e) {
            log.warn("", e);
            throw e;
        }
    }

    /**
     * Check a licence-String.
     *
     * @param userKey
     * @throws IllegalStateException
     */
    public void checkLicence(String userKey) throws IllegalStateException {
        // check length
        if ((userKey == null) || (userKey.length() != 19)) {
            throw new IllegalStateException(ResourceManager.getResource(LicenceChecker.class, "CEInvalidLicence"));
        }

        String key = ch.softenvironment.util.StringUtils.replace(userKey, "-", "");
        if (key.length() != 16) {
            throw new IllegalStateException(ResourceManager.getResource(LicenceChecker.class, "CEInvalidLicence"));
        }

        // check Random Fields
        if (!((key.charAt(10) >= 'a') && (key.charAt(10) <= 'z') &&
            (key.charAt(14) >= 'A') && (key.charAt(14) <= 'Z'))) {
            throw new IllegalStateException(ResourceManager.getResource(LicenceChecker.class, "CEInvalidLicence"));
        }

        // check number of users
        users = 0;
        if (key.charAt(15) <= 80) {
            users = (80 - key.charAt(15)) * 100;
        }
        if ((key.charAt(2) >= 'a') && (key.charAt(2) <= 'z')) {
            users = users + (112 - key.charAt(2)) * 10;
        }
        users = users + (81 - key.charAt(0));
        if (users < 1) {
            throw new IllegalStateException(ResourceManager.getResource(LicenceChecker.class, "CELimitedUsers"));
        }

        // check productName
        productName = "" + key.charAt(8) + key.charAt(5) + key.charAt(12) + key.charAt(3);

        // check expirationDate
        expirationDate = new java.util.GregorianCalendar();
        expirationDate.setTime(startDate);
        int expirationIndex = key.charAt(1) - 103;
        switch (expirationIndex) {
            case 0:
                expirationDate.add(java.util.Calendar.MONTH, 1);
                break;
            case 1:
                expirationDate.add(java.util.Calendar.MONTH, 2);
                break;
            case 2:
                expirationDate.add(java.util.Calendar.MONTH, 3);
                break;
            case 3:
                expirationDate.add(java.util.Calendar.MONTH, 6);
                break;
            case 4:
                expirationDate.add(java.util.Calendar.MONTH, 12);
                break;
            case 5:
                expirationDate.add(java.util.Calendar.MONTH, 24);
                break;
            case 6:
                // never ending
                expirationDate = new java.util.GregorianCalendar(2999, java.util.Calendar.DECEMBER, 31);
                break;
            default:
                // expired already
                expirationDate = new java.util.GregorianCalendar(1900, java.util.Calendar.JANUARY, 1);
                break;
        }
        if (expirationDate.getTime().getTime() < (new java.util.Date()).getTime()) {
            throw new IllegalStateException(ResourceManager.getResource(LicenceChecker.class, "CELicenceExpired"));
        }

        //TODO NYI: productVersion
    }

    /**
     * Return Expiration Date.
     */
    public java.util.Date getExpirationDate() {
        return expirationDate.getTime();
    }

    /**
     * Return Number of Users.
     */
    public int getNumberOfUsers() {
        return users;
    }

    /**
     * Return Product-Name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Return Product-Version.
     */
    public String getProductVersion() {
        return productVersion;
    }
}
