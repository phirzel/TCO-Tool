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

/**
 * Panel with a Percentage-Slider and Percentage-Number.
 *
 * @author Peter Hirzel
 */
public class PercentageSliderPanel extends BasePanel {

    private javax.swing.JLabel ivjJLabel231 = null;
    private javax.swing.JScrollPane ivjJScrollPane11 = null;
    private javax.swing.JSlider ivjJSlider11 = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private javax.swing.JTextField ivjJTextField1 = null;
    private boolean ivjConnPtoP1Aligning = false;

    class IvjEventHandler implements java.awt.event.KeyListener, java.awt.event.MouseListener {

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            if (e.getSource() == PercentageSliderPanel.this.getJTextField1()) {
                connPtoP1SetSource();
            }
        }

        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.getSource() == PercentageSliderPanel.this.getJSlider11()) {
                connPtoP1SetTarget();
            }
        }
    }

    /**
     * PercentageSliderPanel constructor comment.
     */
    public PercentageSliderPanel() {
        super();
        initialize();
    }

    /**
     * PercentageSliderPanel constructor comment.
     *
     * @param layout java.awt.LayoutManager
     */
    public PercentageSliderPanel(java.awt.LayoutManager layout) {
        super(layout);
    }

    /**
     * PercentageSliderPanel constructor comment.
     *
     * @param layout java.awt.LayoutManager
     * @param isDoubleBuffered boolean
     */
    public PercentageSliderPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
     * PercentageSliderPanel constructor comment.
     *
     * @param isDoubleBuffered boolean
     */
    public PercentageSliderPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
     * connPtoP1SetSource:  (JSlider11.value <--> JTextField1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP1SetSource() {
        /* Set the source from the target */
        try {
            if (!ivjConnPtoP1Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP1Aligning = true;
                getJSlider11().setValue(Integer.parseInt(getJTextField1().getText()));
                // user code begin {2}
                // user code end
                ivjConnPtoP1Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP1Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * connPtoP1SetTarget:  (JSlider11.value <--> JTextField1.text)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connPtoP1SetTarget() {
        /* Set the target from the source */
        try {
            if (!ivjConnPtoP1Aligning) {
                // user code begin {1}
                // user code end
                ivjConnPtoP1Aligning = true;
                getJTextField1().setText(String.valueOf(getJSlider11().getValue()));
                // user code begin {2}
                // user code end
                ivjConnPtoP1Aligning = false;
            }
        } catch (java.lang.Throwable ivjExc) {
            ivjConnPtoP1Aligning = false;
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /**
     * Return the JLabel231 property value.
     *
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel231() {
        if (ivjJLabel231 == null) {
            try {
                ivjJLabel231 = new javax.swing.JLabel();
                ivjJLabel231.setName("JLabel231");
                ivjJLabel231.setText("%");
                ivjJLabel231.setBounds(160, 7, 13, 14);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabel231;
    }

    /**
     * Return the JScrollPane11 property value.
     *
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getJScrollPane11() {
        if (ivjJScrollPane11 == null) {
            try {
                ivjJScrollPane11 = new javax.swing.JScrollPane();
                ivjJScrollPane11.setName("JScrollPane11");
                ivjJScrollPane11.setBounds(0, 0, 120, 31);
                getJScrollPane11().setViewportView(getJSlider11());
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane11;
    }

    /**
     * Return the JSlider11 property value.
     *
     * @return javax.swing.JSlider
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSlider getJSlider11() {
        if (ivjJSlider11 == null) {
            try {
                ivjJSlider11 = new javax.swing.JSlider();
                ivjJSlider11.setName("JSlider11");
                ivjJSlider11.setPaintLabels(false);
                ivjJSlider11.setPaintTicks(true);
                ivjJSlider11.setValue(0);
                ivjJSlider11.setMajorTickSpacing(10);
                ivjJSlider11.setPreferredSize(new java.awt.Dimension(100, 16));
                ivjJSlider11.setBounds(0, 0, 131, 16);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSlider11;
    }

    /**
     * Return the JTextField1 property value.
     *
     * @return javax.swing.JTextField
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextField getJTextField1() {
        if (ivjJTextField1 == null) {
            try {
                ivjJTextField1 = new javax.swing.JTextField();
                ivjJTextField1.setName("JTextField1");
                ivjJTextField1.setBounds(125, 5, 32, 20);
                ivjJTextField1.setEditable(true);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTextField1;
    }

    /**
     * Called whenever the part throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    @Override
    protected void handleException(java.lang.Throwable exception) {
        if (exception instanceof NumberFormatException) {
            // ignore
        } else {
            super.handleException(exception);
        }
    }

    /**
     * Initializes connections
     *
     *
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() {
        // user code begin {1}
        // user code end
        getJSlider11().addMouseListener(ivjEventHandler);
        getJTextField1().addKeyListener(ivjEventHandler);
        connPtoP1SetTarget();
    }

    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("PercentageSliderPanel");
            setLayout(null);
            setSize(179, 31);
            add(getJLabel231(), getJLabel231().getName());
            add(getJScrollPane11(), getJScrollPane11().getName());
            add(getJTextField1(), getJTextField1().getName());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        // user code end
    }
}
