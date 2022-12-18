package ch.softenvironment.view;

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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.view.swingext.SwingWorker;
import java.awt.Frame;
import lombok.extern.slf4j.Slf4j;

/**
 * Wait-Dialog for busy actions. Design Pattern: Singleton
 *
 * @author Peter Hirzel
 * @see BaseFrame#showBusy(Runnable)
 */

@Slf4j
public class WaitDialog extends BaseDialog {

    public static final int UNKNOWN_PROGRESS = -1;

    // single instance #showBusy
    private static volatile WaitDialog waitDialog = null;
    private static volatile int waitCounter = 0;
    // single instance #showBusyThread
    private static volatile WaitDialog waitDialogThread = null;
    private static volatile int waitCounterThread = 0;

    private javax.swing.JPanel ivjBaseDialogContentPane = null;
    private javax.swing.JLabel ivjLblText = null;
    private javax.swing.JLabel ivjLblImage = null;
    private javax.swing.JProgressBar ivjPrgBar = null;

    /**
     * WaitDialog constructor comment.
     *
     * @param owner java.awt.Frame
     * @param title
     */
    private WaitDialog(java.awt.Frame owner, String title) {
        super(owner,
            //	        title,
            false /* otherwise will not terminate */);
        setTitle(title);
        initialize();
        //	setTitle(title == null ? getResourceString(WaitDialog.class, "DlgTitle") : title);
    }

    private WaitDialog(java.awt.Dialog owner, String title) {
        super(owner,
            //	        title,
            false /* otherwise will not terminate */);
        setTitle(title);
        initialize();
        //	setTitle(title == null ? getResourceString(WaitDialog.class, "DlgTitle") : title);
    }

    /**
     * Return the BaseDialogContentPane property value.
     *
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getBaseDialogContentPane() {
        if (ivjBaseDialogContentPane == null) {
            try {
                ivjBaseDialogContentPane = new javax.swing.JPanel();
                ivjBaseDialogContentPane.setName("BaseDialogContentPane");
                ivjBaseDialogContentPane.setLayout(null);
                getBaseDialogContentPane().add(getLblImage(), getLblImage().getName());
                getBaseDialogContentPane().add(getLblText(), getLblText().getName());
                getBaseDialogContentPane().add(getPrgBar(), getPrgBar().getName());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjBaseDialogContentPane;
    }

    /**
     * Return the JLabel1 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getLblImage() {
        if (ivjLblImage == null) {
            try {
                ivjLblImage = new javax.swing.JLabel();
                ivjLblImage.setName("LblImage");
                ivjLblImage.setText("JLabel1");
                ivjLblImage.setBounds(16, 14, 120, 232);
                // user code begin {1}
                ivjLblImage.setText("");
                ivjLblImage.setIcon(ch.ehi.basics.i18n.ResourceBundle.getImageIcon(WaitDialog.class, "traffic_redlight.png"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblImage;
    }

    /**
     * Return the LblText property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    protected javax.swing.JLabel getLblText() {
        if (ivjLblText == null) {
            try {
                ivjLblText = new javax.swing.JLabel();
                ivjLblText.setName("LblText");
                ivjLblText.setText("Bitte gedulden Sie sich einen Moment...");
                ivjLblText.setBounds(162, 176, 254, 14);
                // user code begin {1}
                ivjLblText.setText(ResourceManager.getResource(WaitDialog.class, "LblText_text"));
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjLblText;
    }

    /**
     * Return the bar property value.
     *
     * @return javax.swing.JProgressBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    protected javax.swing.JProgressBar getPrgBar() {
        if (ivjPrgBar == null) {
            try {
                ivjPrgBar = new javax.swing.JProgressBar();
                ivjPrgBar.setName("PrgBar");
                ivjPrgBar.setBounds(162, 93, 167, 14);
                ivjPrgBar.setValue(0);
                // user code begin {1}
                getPrgBar().setStringPainted(true);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPrgBar;
    }

    /**
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    @Override
    protected void handleException(java.lang.Throwable exception) {
        super.handleException(exception);
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            //		setLocation(100, 100);
            // user code end
            this.setResizable(false);
            setName("WaitDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(426, 289);
            //		setTitle("Kurze Pause");
            setContentPane(getBaseDialogContentPane());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        getPrgBar().setVisible(false);
        // user code end
    }

    /**
     * Execute a given Block and show Busy-Cursor and WaitDialog meanwhile. There is only ONE WaitDialog in case of nested calls of this method. Use #updateProgress(..) to show any Progress-Messages
     * meanwhile in the WaitDialog. (The given Block is not threaded)
     * <p>
     * The actions within the given Block are handled by a Throwable-Handler resp. #handleException().
     * <p>
     * Ex. class MyView extends BaseFrame { public void doAnything(Integer c, String s) { showBusy(new Runnable() { public void run() { // do anything ... updateProgress(10, "start activity"); ... }
     * }); }
     *
     * @param block executable Block that might take a while
     * @see #updateProgress(int, String)
     */
    public static void showBusy(final java.awt.Frame owner, final Runnable block) {
        if (owner == null) {
            log.warn("Developer warning: WaitDialog suppressed because owner unknown!");
        }
        if (++waitCounter == 1) {
            // show ONE WaitDialog only
            try {
                //Tracer.getInstance().debug("opening WaitDialog");
                owner.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                waitDialog = new WaitDialog(owner, ResourceManager.getResource(WaitDialog.class, "DlgTitle"));
                waitDialog.setVisible(true);
                // make sure refresh is forced NOW
                waitDialog.paint(waitDialog.getGraphics());
            } catch (Throwable e) {
                log.warn("fork WaitDialog failed", e);
                waitDialog = null;
                owner.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                waitCounter = 0;
            }
        }

        // always execute block => non-threaded
        try {
            block.run();
        } catch (Throwable e) {
            // handler is necessary, otherwise #finished() won't be called
            // in case of a failure in block
            log.error("Block failed", e);
            BaseFrame.showException(owner, e);
        }

        if (--waitCounter == 0) {
            // close WaitDialog when last actions are done
            //Tracer.getInstance().debug("closing WaitDialog");
            try {
                // @see Exeption-Handler at opening WaitDialog
                waitDialog.dispose();
            } catch (Throwable e) {
                log.warn("#finished() could not dispose() WaitDialog correctly!");
            }
            waitDialog = null;
            owner.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * <b>Execute a given Block in an own Thread</b> and show WaitDialog meanwhile.
     * There is only ONE WaitDialog in case of nested calls of this method. Use #updateProgressThread(..) to show any Progress-Messages meanwhile in the WaitDialog.
     * <p>
     * The actions within the given Block are handled by a Throwable-Handler resp. #handleException().
     * <p>
     * Ex. class MyView extends BaseFrame { public void doAnything(Integer c, String s) { showBusy(new Runnable() { public void run() { // do anything ... updateProgressThread(10, "start activity");
     * ... } }); }
     *
     * @param block executable Block to fork that might take a while
     * @see #updateProgressThread(int, String)
     */
    public static void showBusyThread(final java.awt.Frame owner, final Runnable block) {
        if (++waitCounterThread == 1) {
            // show ONE WaitDialog only
            try {
                // Tracer.getInstance().debug("Thread: opening WaitDialog");
                //          owner.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                waitDialogThread = new WaitDialog(owner, ResourceManager.getResource(WaitDialog.class, "DlgTitleThread"));
                waitDialogThread.setVisible(true);
                updateProgressThread(UNKNOWN_PROGRESS, ResourceManager.getResource(WaitDialog.class, "LblText_text"));
            } catch (Throwable e) {
                log.warn("show WaitDialog failed", e);
                waitDialogThread = null;
                //		    owner.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                waitCounterThread = 0;
            }
        }

        // define Swing-Thread
        SwingWorker worker = new SwingWorker() {
            @Override
            public Object construct() {
                try {
                    block.run();
                } catch (Throwable e) {
                    // handler is necessary, otherwise #finished() won't be called
                    // in case of a failure in block
                    log.error("showBusyThread(Runnable)->Block failed", e);
                    BaseFrame.showException(owner, e);
                }
                return null;
            }

            /**
             * Will be called when #construct() has finished.
             * Close the WaitDialog and reset Cursor.
             */
            @Override
            public void finished() {
                if (--waitCounterThread == 0) {
                    // close WaitDialog when last actions are done
                    //Tracer.getInstance().debug("Thread: closing WaitDialog");
                    try {
                        // @see Exeption-Handler at opening WaitDialog
                        waitDialogThread.dispose();
                    } catch (Throwable e) {
                        log.warn("showBusyThread()#finished() could not dispose() WaitDialog correctly!");
                    }
                    waitDialogThread = null;
                    //		    	owner.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            }
        };

        // fork the block
        worker.start();
    }

    /**
     * Show Progress in WaitDialog started by #showBusy(..).
     *
     * @param percentage Progress of current activity [0..100] or WaitDialog.UNKNOWN_PROGRESS
     * @param currentActivity User friendly description of current activity
     * @see #showBusy(Frame, Runnable)
     */
    public static void updateProgress(final int percentage, final String currentActivity) {
        //TODO Tune!!!
        //	javax.swing.SwingUtilities.invokeLater(new Runnable() {
        //		public void run() {
        try {
            waitDialog.getPrgBar().setVisible(true);
            if (percentage > 0) {
                waitDialog.getPrgBar().setValue(percentage);
            } else {
                waitDialog.getPrgBar().setIndeterminate(false);
            }

            if (currentActivity != null) {
                waitDialog.getLblText().setText(currentActivity);
            }
            waitDialog.paint(waitDialog.getGraphics()); // force refresh
            //waitDialog.toFront();
        } catch (NullPointerException ex) {
            // no waitDialog probably
            log.warn("Developer warning: IGNORE: use #showBusy() first: %=" + percentage + " activity: " + currentActivity);
        } catch (Throwable e) {
            // waitDialog could be closed before Update is made
            log.warn("Developer warning: IGNORE", e);
        }
        //		}
        //	});
    }

    /**
     * Show Progress in WaitDialog started by #showBusyThread(..).
     *
     * @param percentage Progress of current activity [0..100] or WaitDialog.UNKNOWN_PROGRESS
     * @param currentActivity User friendly description of current activity
     * @see #showBusyThread(Frame, Runnable)
     */
    public static void updateProgressThread(final int percentage, final String currentActivity) {
        //TODO Tune!!!
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
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
                    //waitDialog.paint(waitDialog.getGraphics()); // force refresh
                    //waitDialogThread.toFront();
                } catch (Throwable e) {
                    // waitDialogThread could be closed before Update is made
                    log.warn("Developer warning: updateProgressThread(..)->Ignoring", e);
                }
            }
        });
    }
}
