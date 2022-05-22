package ch.softenvironment.view;

import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Tracer;
import java.awt.BorderLayout;
import javax.swing.JButton;

/**
 * Test class BaseFrame.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class BaseFrameTestCase extends BaseFrame {

    private javax.swing.JPanel jContentPane = null;
    private JButton btnShowBusy = null;
    private JButton btnShowBusyTwice = null;
    private JButton btnShowBusySequential = null;
    private JButton btnShowDeveloperException = null;

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getBtnShowBusy() {
        if (btnShowBusy == null) {
            btnShowBusy = new JButton();
            btnShowBusy.setText("#showBusy() [once]");
            btnShowBusy.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showBusy(new Runnable() {
                        @Override
                        public void run() {
                            doLongAction(1);
                        }
                    });
                }
            });
        }
        return btnShowBusy;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getBtnShowBusyTwice() {
        if (btnShowBusyTwice == null) {
            btnShowBusyTwice = new JButton();
            btnShowBusyTwice.setText("#showBusy() [recursive]");
            btnShowBusyTwice.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showBusy(new Runnable() {
                        @Override
                        public void run() {
                            doLongAction(3);
                        }
                    });
                }
            });
        }
        return btnShowBusyTwice;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getBtnShowBusySequential() {
        if (btnShowBusySequential == null) {
            btnShowBusySequential = new JButton();
            btnShowBusySequential.setText("#showBusy() [sequential]");
            btnShowBusySequential.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showBusy(new Runnable() {
                        @Override
                        public void run() {
                            doLongAction(1);
                        }
                    });
                    showBusy(new Runnable() {
                        @Override
                        public void run() {
                            doLongAction(1);
                        }
                    });
                }
            });
        }
        return btnShowBusySequential;
    }

    private JButton getBtnShowDeveloperException() {
        if (btnShowDeveloperException == null) {
            btnShowDeveloperException = new JButton();
            btnShowDeveloperException.setText("throw new DeveloperException");
            btnShowDeveloperException.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showBusy(new Runnable() {
                        @Override
                        public void run() {
                            // WaitDialog#showBusy() will handle it
                            throw new DeveloperException("dev-msg", this + "#getBtnShowDeveloperException()", new RuntimeException("other hint"));
                        }
                    });
                }
            });
        }
        return btnShowDeveloperException;
    }

    public static void main(String[] args) {
        Tracer.start(Tracer.ALL);

        BaseFrame view = new BaseFrameTestCase();
        view.pack();
        view.setVisible(true);
    }

    /**
     * This is the default constructor
     */
    public BaseFrameTestCase() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setTitle("Test: BaseFrame");
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getBtnShowBusy(), java.awt.BorderLayout.SOUTH);
            jContentPane.add(getBtnShowBusyTwice(), java.awt.BorderLayout.NORTH);
            jContentPane.add(getBtnShowBusySequential(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getBtnShowDeveloperException(), java.awt.BorderLayout.EAST);
        }
        return jContentPane;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.view.BaseFrame#initializeView()
     */
    @Override
    protected void initializeView() throws Exception {
    }

    private void doLongAction(int recursionLevel) {
        try {
            if (--recursionLevel > 0) {
                doLongAction(recursionLevel);
            }
            Tracer.getInstance().debug("level=" + recursionLevel + " going to sleep 1s");
            WaitDialog.updateProgress(20, "level=" + recursionLevel + " going to sleep 1s");
            Thread.sleep(1000);
            Tracer.getInstance().debug("level=" + recursionLevel + " going to sleep 3s");
            WaitDialog.updateProgress(50, "level=" + recursionLevel + " going to sleep 3s");
            Thread.sleep(3000);
            WaitDialog.updateProgress(95, "level=" + recursionLevel + " done");
        } catch (Throwable e) {
            Tracer.getInstance().runtimeError("sleep failed", e);
        }
    }
}
