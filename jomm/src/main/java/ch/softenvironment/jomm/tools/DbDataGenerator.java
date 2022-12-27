package ch.softenvironment.jomm.tools;
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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.ListUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Generator utility to create Random literals.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DbDataGenerator {

    private final java.util.Random generator = new java.util.Random((new Date()).getTime());

    public java.util.Random getGenerator() {
        return generator;
    }

    public String getRandomString(int maxLength) {
        StringBuffer value = new StringBuffer();
        int len = generator.nextInt(maxLength);
        for (int i = 0; i < len; i++) {
            int offset = generator.nextInt(26);
            // every 5th letter
            if (i % 5 == 0) {
                // uppercase letter
                value.append((char) (65 + offset));
            } else if (i % 8 == 0) {
                value.append(" ");
            } else {
                // lowercase letter
                value.append((char) (97 + offset));
            }
        }
        return value.toString();
    }

    public java.util.Date getRandomDate(int startYear, int endYear) {
        return (new java.util.GregorianCalendar(startYear + generator.nextInt(endYear - startYear), generator.nextInt(GregorianCalendar.DECEMBER), generator.nextInt(31))).getTime();
    }

    /**
     * Return Tri-State.
     *
     * @see #getGenerator().getBoolean()
     */
    public Boolean getRandomBoolean() {
        int n = generator.nextInt(99);
        if (n > 50) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
        /*else if (n<35) {
        }
            return Boolean.FALSE;
        }  else {            
            return null;
        }*/

    }

    /**
     * Generate the SQL-Code of the given file against the target-server. Use formatted code as it would be understood by the target-server with a native commandline shell. (This is not a pre-compiler
     * or syntax-checker!)
     * <p>
     * Each command is executed separately with the following conditions: - "--" comment is skipped - "/* .." blocks are skipped (even multiline) - terminate each SQL-Statement by ";" - normal style
     * is expected (nasty formatting might not be parsed well)
     * <p>
     * Each line separated by ';' (Semicolon) will be executed separately in the given sequence provided by the file.
     *
     * @param server
     * @param sqlFileName
     */
    public static void executeSqlCode(@NonNull DbObjectServer server, @NonNull Class sqlMarkerClazz, @NonNull String sqlFileName) throws Exception {
        URL tmp = //"."+sqlFileName;
                //"../jomm/src/main/resources/"+sqlFileName;
                ResourceManager.getURL(sqlMarkerClazz, sqlFileName);
        if (tmp == null) {
            throw new DeveloperException("Resources problem with file: " + sqlFileName + " marked by: " + sqlMarkerClazz);
        }
        log.info("Generating file: {}  on server: {}", tmp, server.getPersistenceManagerFactory().getConnectionURL());
        FileReader reader = null;
        Exception ex = null;
        try {
            File file = new File(tmp.toURI());
            reader = new FileReader(file);
            BufferedReader inputStream = new BufferedReader(reader);
            String line = "";
            String currentLine = "";
            while ((currentLine = inputStream.readLine()) != null) {
                currentLine = currentLine.trim();
                if (currentLine.startsWith("--")) {
                    // skip comment line
                    continue;
                } else if (currentLine.startsWith("/*")) {
                    //TODO for e.g. MS SQL Server
                    if (currentLine.endsWith("*/")) {
                        // skip line
                        continue;
                    } else {
                        while ((currentLine = inputStream.readLine()) != null) {
                            if (currentLine.trim().endsWith("*/")) {
                                currentLine = "";
                                break;
                            } else {
                                // skip comment block
                                continue;
                            }
                        }
                    }
                }

                /*
                 * keep line formatting of input file
                 * CREATE TABLE X ( -- my table comment"
                 *   Id CHAR(1),    -- my field comment
                 *   ..
                 *   );
                 */
                line = line + System.getProperty("line.separator") + currentLine;
                if (currentLine.endsWith(";")) {
                    try {
                        server.execute("..from InputFile", ListUtils.createList(line));
                    } catch (Throwable e) {
                        log.error("line generation failed: {}", line, e);
                    }
                    line = "";
                }
            }
            
/*            
    		
    		int currentChar = -1;
    //TODO tune
    		while ((currentChar = inputStream.read()) != -1) {
    //TODO BUG for INSERT-statements containing Data with ';'
    			if (currentChar == ';') {
    				line = line + ";";
    				server.execute("..from InputFile", ListUtils.createList(line));
                    line = "";
    			} else {
    				line = line + ((char)currentChar);
    			}
    		}
*/
            inputStream.close();
            reader.close();
        } catch (FileNotFoundException e) {
            log.error("could not open File <{}>", sqlFileName, e);
            ex = e;
        } catch (IOException e) {
            log.error("IO-failure <{}>", sqlFileName, e);
            ex = e;
        }
        if (ex != null) {
            throw ex;
        }
        log.info("Generating file: DONE!");
    }
}
