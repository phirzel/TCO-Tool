package ch.softenvironment.util;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility to read Console-Input.
 *
 * @author Markus Ruggiero
 */
public abstract class ConsoleReader {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Read a single Character.
     *
     * @return char
     */
    public static char readChar(String prompt) throws IOException {
        char value = 0;
        while (true) {
            try {
                System.out.print(prompt + " _>");
                value = (char) reader.read();
                // alternative value = (char)reader.readLine().charAt(0);
                break;
            } catch (IOException ex) {
                System.err.println(ex.getLocalizedMessage());
                throw ex;
            }
        }
        return value;
    }

    /**
     * Read a Double value.
     *
     * @return double
     */
    public static double readDouble(String prompt) throws IOException {
        double value = 0.0;
        System.out.print(prompt + " _>");
        while (true) {
            try {
                value = Double.valueOf(reader.readLine()).doubleValue();
                break;
            } catch (IOException ex) {
                System.out.println("*** Read Error ***");
                System.out.println("*** " + ex.getMessage());
                System.exit(1);
            } catch (NumberFormatException ex) {
                System.err.println(ex.getLocalizedMessage());
                throw ex;
            }
        }
        return value;
    }

    /**
     * Read an Integer value.
     *
     * @return int
     */
    public static int readInteger(String prompt) throws IOException {
        int value = 0;
        while (true) {
            try {
                System.out.print(prompt + " _>");
                value = Integer.valueOf(reader.readLine()).intValue();
                break;
            } catch (IOException ex) {
                System.err.println(ex.getLocalizedMessage());
                throw ex;
            } catch (NumberFormatException ex) {
                System.err.println("Conversion Error: " + ex.getLocalizedMessage());
                return readInteger("Try again: " + prompt);
            }
        }
        return value;
    }

    /**
     * Read a Long value.
     *
     * @return long
     */
    public static long readLong(String prompt) throws IOException {
        long value = 0;
        while (true) {
            try {
                System.out.print(prompt + " _>");
                value = Long.valueOf(reader.readLine()).longValue();
                break;
            } catch (IOException ex) {
                System.err.println(ex.getLocalizedMessage());
                throw ex;
            } catch (NumberFormatException ex) {
                System.err.println("Conversion Error: " + ex.getLocalizedMessage());
                return readLong("Try again: " + prompt);
            }
        }
        return value;
    }

    /**
     * Read a String value.
     *
     * @return String
     */
    public static String readString(String prompt) throws IOException {
        String value = "";
        while (true) {
            try {
                System.out.print(prompt + " _>");
                value = reader.readLine();
                break;
            } catch (IOException ex) {
                System.err.println(ex.getLocalizedMessage());
                throw ex;
            }
        }
        return value;
    }
}