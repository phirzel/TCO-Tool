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

import junit.framework.TestCase;

/**
 * Test class UserException.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.1 $ $Date: 2006-06-29 22:31:57 $
 */
public class UserExceptionTestCase extends TestCase {

    private static final String FAULT_TITLE = "My Title";
    private static final String FAULT_MSG = "My fault!";

    private static class NestedError {

        private NestedError() {
            throw new UserException("from nested class", FAULT_TITLE);
        }
    }

    public void testConstructor() {
        // must be implmented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace
        throwEx(null);
        throwEx(new NullPointerException("Test"));
        throwStaticEx(null);
        throwStaticEx(new NullPointerException("Test"));
    }

    public void testConstructorHere() {
        try {
            throw new UserException(FAULT_MSG, FAULT_TITLE);
        } catch (UserException e) {
            if (e.getCause() != null) {
                assertTrue(e.getCause() instanceof NullPointerException);
            }
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue(e.getTitle().equals(FAULT_TITLE));
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("testConstructorHere") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("UserExceptionTestCase") >= 0);
        }
    }

    private void throwEx(Throwable ex) {
        try {
            if (ex == null) {
                throw new UserException(FAULT_MSG, FAULT_TITLE);
            } else {
                throw new UserException(FAULT_MSG, FAULT_TITLE, ex);
            }
        } catch (UserException e) {
            if (e.getCause() != null) {
                assertTrue(e.getCause() instanceof NullPointerException);
            }
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue(e.getTitle().equals(FAULT_TITLE));
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("throwEx") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("UserExceptionTestCase") >= 0);
        }
    }

    private static void throwStaticEx(Throwable ex) {
        try {
            if (ex == null) {
                throw new UserException(FAULT_MSG, FAULT_TITLE);
            } else {
                throw new UserException(FAULT_MSG, FAULT_TITLE, ex);
            }
        } catch (UserException e) {
            if (e.getCause() != null) {
                assertTrue(e.getCause() instanceof NullPointerException);
            }
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue(e.getTitle().equals(FAULT_TITLE));
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("throwStaticEx") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("UserExceptionTestCase") >= 0);
        }
    }

    public void testNestedClassReference() {
        try {
            // must be implemented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace 
            throwWithinNested();
        } catch (DeveloperException e) {
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("UserExceptionTestCase") >= 0);
            //assertTrue("origin-method", e.getLocalizedMessage().indexOf("NestedError") >= 0);
        }
    }

    private void throwWithinNested() {
        new NestedError();
    }
}
