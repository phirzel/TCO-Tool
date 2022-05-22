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
 * Test class DeveloperException.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.4 $ $Date: 2007-02-20 12:59:54 $
 */
public class DeveloperExceptionTestCase extends TestCase {

    //  private static final String METHOD = "MyMethod()";
    private static final String FAULT_TITLE = "My Title";
    private static final String FAULT_MSG = "My fault!";

    private static class NestedError {

        private NestedError() {
            throw new DeveloperException("from nested class");
        }
    }

    public void testConstructorMessageTitleException() {
        try {
            // must be implmented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace 
            throwDeveloperException(FAULT_TITLE, new NullPointerException("test null ex"));
        } catch (DeveloperException e) {
            assertTrue(e.getCause() instanceof NullPointerException);
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue(e.getTitle().equals(FAULT_TITLE));
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("throwDeveloperException") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("DeveloperExceptionTestCase") >= 0);
        }
    }

    public void testConstructorMessageTitleExceptionStatic() {
        try {
            // must be implmented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace 
            throwStaticDeveloperException(FAULT_TITLE, new NullPointerException("test null ex"));
        } catch (DeveloperException e) {
            assertTrue(e.getCause() instanceof NullPointerException);
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue(e.getTitle().equals(FAULT_TITLE));
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("throwStaticDeveloperException") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("DeveloperExceptionTestCase") >= 0);
        }
    }

    public void testConstructorMessageTitle() {
        try {
            // must be implmented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace 
            throwDeveloperException(FAULT_TITLE, null);
        } catch (DeveloperException e) {
            assertTrue(e.getCause() == null);
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue(e.getTitle().equals(FAULT_TITLE));
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("throwDeveloperException") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("DeveloperExceptionTestCase") >= 0);
        }
    }

    public void testConstructorMessage() {
        try {
            // must be implmented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace 
            throwDeveloperException(null, null);
        } catch (DeveloperException e) {
            assertTrue(e.getCause() == null);
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue("default title", e.getTitle() != null);
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("throwDeveloperException") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("DeveloperExceptionTestCase") >= 0);
        }
    }

    public void testConstructorMessageHere() {
        try {
            // must be implmented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace 
            throw new DeveloperException(FAULT_MSG, null);
        } catch (DeveloperException e) {
            assertTrue(e.getCause() == null);
            assertTrue(e.getMessage().equals(FAULT_MSG));
            assertTrue("default title", e.getTitle() != null);
            assertTrue("origin-method", e.getLocalizedMessage().indexOf("testConstructorMessageHere") >= 0);
            assertTrue("origin-class", e.getLocalizedMessage().indexOf("DeveloperExceptionTestCase") >= 0);
        }
    }

    private void throwDeveloperException(String title, Throwable ex) {
        throw new DeveloperException(FAULT_MSG, title, ex);
    }

    private static void throwStaticDeveloperException(String title, Throwable ex) {
        throw new DeveloperException(FAULT_MSG, title, ex);
    }

    public void testNestedClassReference() {
        try {
            // must be implmented in separate method, otherwise #invoke(test*) of JUnit will disturb proper Stacktrace 
            throwWithinNested();
        } catch (DeveloperException e) {
            assertTrue(e.getLocalizedMessage().indexOf("DeveloperExceptionTestCase") >= 0);
            //assertTrue("origin-method", e.getLocalizedMessage().indexOf("NestedError") >= 0);
            //assertTrue("origin-class", e.getLocalizedMessage().indexOf("NestedError") >= 0);
        }
    }

    public void testAsBlock() {
        //TODO
   /*     public static void updateProgressThread(final int percentage, final String currentActivity) {
//          TODO Tune!!!
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        waitDialogThread.getPrgBar().setVisible(true);
                        if (percentage > 0) {
                            waitDialogThread.getPrgBar().setValue(percentage);
                        } else {
                            waitDialogThread.getPrgBar().setIndeterminate(false);
                        }

                        if (currentActivity != null) {
                            waitDialogThread.getLblText().setText(currentActivity);
                        }
//          waitDialog.paint(waitDialog.getGraphics()); // force refresh
                        waitDialogThread.toFront();
                    } catch(Throwable e) {
                        // waitDialogThread could be closed before Update is made
                        Tracer.getInstance().developerWarning("updateProgressThread(..)->Ignoring: " + e.getLocalizedMessage());
                    }
                }
            });
          }
        */
    }

    private void throwWithinNested() {
        new NestedError();
    }
}
