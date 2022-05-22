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

/**
 * Panel to manage a single URL.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class URLView extends javax.swing.JPanel {

	private javax.swing.JButton ivjBtnBrowser = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JTextField ivjTxtURL = null;
	protected transient ch.softenvironment.view.URLViewListener fieldURLViewListenerEventMulticaster = null;

	class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.KeyListener {

		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == URLView.this.getBtnBrowser()) {
				connEtoC1(e);
			}
		}

		@Override
		public void keyPressed(java.awt.event.KeyEvent e) {
		}

		@Override
		public void keyReleased(java.awt.event.KeyEvent e) {
			if (e.getSource() == URLView.this.getTxtURL()) {
				connEtoC2(e);
			}
		}

		@Override
		public void keyTyped(java.awt.event.KeyEvent e) {
		}
	}

	/**
	 * UrlView constructor comment.
	 */
	public URLView() {
		super();
		initialize();
	}

	/**
	 * UrlView constructor comment.
	 *
	 * @param layout java.awt.LayoutManager
	 */
	public URLView(java.awt.LayoutManager layout) {
		super(layout);
	}

	/**
	 * UrlView constructor comment.
	 *
	 * @param layout java.awt.LayoutManager
	 * @param isDoubleBuffered boolean
	 */
	public URLView(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/**
	 * UrlView constructor comment.
	 *
	 * @param isDoubleBuffered boolean
	 */
	public URLView(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * @param newListener ch.softenvironment.view.URLViewListener
	 */
	public void addURLViewListener(ch.softenvironment.view.URLViewListener newListener) {
		fieldURLViewListenerEventMulticaster = ch.softenvironment.view.URLViewListenerEventMulticaster.add(fieldURLViewListenerEventMulticaster, newListener);
		return;
	}

	/**
	 * Open the Standard Browser with given text.
	 */
	private void browse() {
		try {
			ch.ehi.basics.view.BrowserControl.displayURL(getTxtURLText());
		} catch (Throwable e) {
			BaseDialog.showWarning(this, ResourceManager.getResource(URLView.class, "CT_BrowserError"), e.toString());
		}
	}

	/**
	 * connEtoC1: (BtnBrowser.action.actionPerformed(java.awt.event.ActionEvent) --> URLView.browse()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.browse();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (TxtURL.key.keyReleased(java.awt.event.KeyEvent) --> URLView.fireUrlKeyReleased(Ljava.util.EventObject;)V)
	 *
	 * @param arg1 java.awt.event.KeyEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC2(java.awt.event.KeyEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.fireUrlKeyReleased(new java.util.EventObject(this));
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * Method to support listener events.
	 *
	 * @param newEvent java.util.EventObject
	 */
	protected void fireUrlKeyReleased(java.util.EventObject newEvent) {
		if (fieldURLViewListenerEventMulticaster == null) {
			return;
		}
		fieldURLViewListenerEventMulticaster.urlKeyReleased(newEvent);
	}

	/**
	 * Return the BtnBrowser property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnBrowser() {
		if (ivjBtnBrowser == null) {
			try {
				ivjBtnBrowser = new javax.swing.JButton();
				ivjBtnBrowser.setName("BtnBrowser");
				ivjBtnBrowser.setToolTipText("in Browser oeffnen...");
				ivjBtnBrowser.setText("...");
				ivjBtnBrowser.setEnabled(true);
				// user code begin {1}
				ivjBtnBrowser.setToolTipText(ResourceManager.getResource(URLView.class, "CW_OpenInBrowser"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnBrowser;
	}

	/**
	 * Method generated to support the promotion of the text attribute.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getText() {
		return getTxtURL().getText();
	}

	/**
	 * Return the TxtURL property value.
	 *
	 * @return javax.swing.JTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextField getTxtURL() {
		if (ivjTxtURL == null) {
			try {
				ivjTxtURL = new javax.swing.JTextField();
				ivjTxtURL.setName("TxtURL");
				ivjTxtURL.setToolTipText("URL");
				ivjTxtURL.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
				ivjTxtURL.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
				ivjTxtURL.setText("http://");
				// user code begin {1}
				ivjTxtURL.setToolTipText("URL (http://..; file://..; ..");
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtURL;
	}

	/**
	 * Method generated to support the promotion of the txtURLText attribute.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getTxtURLText() {
		return getTxtURL().getText();
	}

	/**
	 * Called whenever the part throws an exception.
	 *
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		BaseFrame.showException(null, exception);
	}

	/**
	 * Initializes connections
	 *
	 * @throws java.lang.Exception The exception description.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBtnBrowser().addActionListener(ivjEventHandler);
		getTxtURL().addKeyListener(ivjEventHandler);
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("URLView");
			setLayout(new java.awt.GridBagLayout());
			setSize(242, 28);

			java.awt.GridBagConstraints constraintsTxtURL = new java.awt.GridBagConstraints();
			constraintsTxtURL.gridx = 1;
			constraintsTxtURL.gridy = 1;
			constraintsTxtURL.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsTxtURL.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsTxtURL.weightx = 1.0;
			constraintsTxtURL.ipadx = 196;
			constraintsTxtURL.insets = new java.awt.Insets(3, 0, 5, 2);
			add(getTxtURL(), constraintsTxtURL);

			java.awt.GridBagConstraints constraintsBtnBrowser = new java.awt.GridBagConstraints();
			constraintsBtnBrowser.gridx = 2;
			constraintsBtnBrowser.gridy = 1;
			constraintsBtnBrowser.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBtnBrowser.ipadx = -7;
			constraintsBtnBrowser.insets = new java.awt.Insets(1, 3, 2, 1);
			add(getBtnBrowser(), constraintsBtnBrowser);
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * @param newListener ch.softenvironment.view.URLViewListener
	 */
	public void removeURLViewListener(ch.softenvironment.view.URLViewListener newListener) {
		fieldURLViewListenerEventMulticaster = ch.softenvironment.view.URLViewListenerEventMulticaster
			.remove(fieldURLViewListenerEventMulticaster, newListener);
		return;
	}

	/**
	 * Method generated to support the promotion of the text attribute.
	 *
	 * @param arg1 java.lang.String
	 */
	public void setText(java.lang.String arg1) {
		getTxtURL().setText(arg1);
	}

	/**
	 * Method generated to support the promotion of the txtURLText attribute.
	 *
	 * @param arg1 java.lang.String
	 */
	public void setTxtURLText(java.lang.String arg1) {
		getTxtURL().setText(arg1);
	}
}
