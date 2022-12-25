package ch.softenvironment.licence.user;

import lombok.extern.slf4j.Slf4j;

/**
 * GUI for a Customer to licence a Product.
 *
 * @author Peter Hirzel
 */

@Slf4j
class LicenceSetupView extends ch.softenvironment.view.BaseDialog {

	private transient LicenceChecker checker = null;
	private javax.swing.JPanel ivjBaseFrameContentPane = null;
	private javax.swing.JTextArea ivjTxtHolder = null;
	private javax.swing.JTextField ivjTxtKey = null;
	private javax.swing.JTextField ivjTxtNumberOfUsers = null;
	private javax.swing.JTextField ivjTxtProduct = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private ch.softenvironment.view.swingext.DateTextField ivjTxtExpirationDate = null;
	private javax.swing.JButton ivjBtnCancel = null;
	private javax.swing.JButton ivjBtnOK = null;
	private javax.swing.JButton ivjBtnLicence = null;
	private javax.swing.JLabel ivjLblExpirationDate = null;
	private javax.swing.JLabel ivjLblHolder = null;
	private javax.swing.JLabel ivjLblKey = null;
	private javax.swing.JLabel ivjLblNrOfUsers = null;
	private javax.swing.JLabel ivjLblProductName = null;

	class IvjEventHandler implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == LicenceSetupView.this.getBtnLicence()) {
				connEtoC1(e);
			}
			if (e.getSource() == LicenceSetupView.this.getBtnOK()) {
				connEtoC3(e);
			}
			if (e.getSource() == LicenceSetupView.this.getBtnCancel()) {
				connEtoC4(e);
			}
		}
	}

	/**
	 * LicenceGeneratorView constructor comment.
	 */
	protected LicenceSetupView(LicenceChecker checker, String productName) {
		super((javax.swing.JFrame) null, true);
		initialize();
		getTxtProduct().setText(productName);
		this.checker = checker;
		setVisible(true);
	}

	/**
	 * Constructor
	 *
	 * @param owner Symbol
	 * @param modal Symbol
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	public LicenceSetupView(java.awt.Frame owner, boolean modal) {
		super(owner, modal);
		initialize();
	}

	/**
	 * connEtoC1:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> LicenceSetupView.validateLicence()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.validateLicence();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3:  (BtnOK.action.actionPerformed(java.awt.event.ActionEvent) --> LicenceSetupView.okPressed()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC3(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.okPressed();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4:  (BtnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> LicenceSetupView.cancelPressed()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC4(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.cancelPressed();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * Return the BaseFrameContentPane property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getBaseFrameContentPane() {
		if (ivjBaseFrameContentPane == null) {
			try {
				ivjBaseFrameContentPane = new javax.swing.JPanel();
				ivjBaseFrameContentPane.setName("BaseFrameContentPane");
				ivjBaseFrameContentPane.setLayout(null);
				getBaseFrameContentPane().add(getLblKey(), getLblKey().getName());
				getBaseFrameContentPane().add(getTxtExpirationDate(), getTxtExpirationDate().getName());
				getBaseFrameContentPane().add(getLblNrOfUsers(), getLblNrOfUsers().getName());
				getBaseFrameContentPane().add(getTxtNumberOfUsers(), getTxtNumberOfUsers().getName());
				getBaseFrameContentPane().add(getLblProductName(), getLblProductName().getName());
				getBaseFrameContentPane().add(getTxtProduct(), getTxtProduct().getName());
				getBaseFrameContentPane().add(getBtnLicence(), getBtnLicence().getName());
				getBaseFrameContentPane().add(getTxtKey(), getTxtKey().getName());
				getBaseFrameContentPane().add(getLblExpirationDate(), getLblExpirationDate().getName());
				getBaseFrameContentPane().add(getLblHolder(), getLblHolder().getName());
				getBaseFrameContentPane().add(getTxtHolder(), getTxtHolder().getName());
				getBaseFrameContentPane().add(getBtnOK(), getBtnOK().getName());
				getBaseFrameContentPane().add(getBtnCancel(), getBtnCancel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBaseFrameContentPane;
	}

	/**
	 * Return the BtnCancel property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnCancel() {
		if (ivjBtnCancel == null) {
			try {
				ivjBtnCancel = new javax.swing.JButton();
				ivjBtnCancel.setName("BtnCancel");
				ivjBtnCancel.setText("Abbrechen");
				ivjBtnCancel.setBounds(266, 295, 109, 25);
				// user code begin {1}
				ivjBtnCancel.setText(getCancelString());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnCancel;
	}

	/**
	 * Return the JButton1 property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnLicence() {
		if (ivjBtnLicence == null) {
			try {
				ivjBtnLicence = new javax.swing.JButton();
				ivjBtnLicence.setName("BtnLicence");
				ivjBtnLicence.setText("Lizenzieren");
				ivjBtnLicence.setBounds(168, 153, 105, 25);
				// user code begin {1}
				ivjBtnLicence.setText(getResourceString("BtnLicence_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnLicence;
	}

	/**
	 * Return the BtnOK property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnOK() {
		if (ivjBtnOK == null) {
			try {
				ivjBtnOK = new javax.swing.JButton();
				ivjBtnOK.setName("BtnOK");
				ivjBtnOK.setText("OK");
				ivjBtnOK.setBounds(166, 295, 90, 25);
				ivjBtnOK.setEnabled(false);
				// user code begin {1}
				ivjBtnOK.setText(getOKString());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnOK;
	}

	/**
	 * Return the JLabel22 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblExpirationDate() {
		if (ivjLblExpirationDate == null) {
			try {
				ivjLblExpirationDate = new javax.swing.JLabel();
				ivjLblExpirationDate.setName("LblExpirationDate");
				ivjLblExpirationDate.setText("Ablaufdatum:");
				ivjLblExpirationDate.setBounds(16, 253, 145, 14);
				// user code begin {1}
				ivjLblExpirationDate.setText(getResourceString("LblExpirationDate_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblExpirationDate;
	}

	/**
	 * Return the JLabel31 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblHolder() {
		if (ivjLblHolder == null) {
			try {
				ivjLblHolder = new javax.swing.JLabel();
				ivjLblHolder.setName("LblHolder");
				ivjLblHolder.setText("Lizenznehmer:");
				ivjLblHolder.setBounds(16, 92, 145, 14);
				// user code begin {1}
				ivjLblHolder.setText(getResourceString("LblHolder_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblHolder;
	}

	/**
	 * Return the JLabel1 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblKey() {
		if (ivjLblKey == null) {
			try {
				ivjLblKey = new javax.swing.JLabel();
				ivjLblKey.setName("LblKey");
				ivjLblKey.setText("Lizenz-Schl�ssel:");
				ivjLblKey.setBounds(16, 31, 145, 14);
				// user code begin {1}
				ivjLblKey.setText(getResourceString("LblKey_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblKey;
	}

	/**
	 * Return the JLabel2 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblNrOfUsers() {
		if (ivjLblNrOfUsers == null) {
			try {
				ivjLblNrOfUsers = new javax.swing.JLabel();
				ivjLblNrOfUsers.setName("LblNrOfUsers");
				ivjLblNrOfUsers.setText("Anzahl Benutzer:");
				ivjLblNrOfUsers.setBounds(16, 225, 145, 14);
				// user code begin {1}
				ivjLblNrOfUsers.setText(getResourceString("LblNrOfUsers_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblNrOfUsers;
	}

	/**
	 * Return the JLabel3 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblProductName() {
		if (ivjLblProductName == null) {
			try {
				ivjLblProductName = new javax.swing.JLabel();
				ivjLblProductName.setName("LblProductName");
				ivjLblProductName.setText("Produktname:");
				ivjLblProductName.setBounds(16, 60, 145, 14);
				// user code begin {1}
				ivjLblProductName.setText(getResourceString("LblProductName_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblProductName;
	}

	/**
	 * Return the TxtExpirationDate property value.
	 *
	 * @return ch.softenvironment.view.swingext.DateTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ch.softenvironment.view.swingext.DateTextField getTxtExpirationDate() {
		if (ivjTxtExpirationDate == null) {
			try {
				ivjTxtExpirationDate = new ch.softenvironment.view.swingext.DateTextField();
				ivjTxtExpirationDate.setName("TxtExpirationDate");
				ivjTxtExpirationDate.setText("");
				ivjTxtExpirationDate.setBounds(168, 252, 82, 20);
				ivjTxtExpirationDate.setEditable(false);
				ivjTxtExpirationDate.setEnabled(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtExpirationDate;
	}

	/**
	 * Return the TxtHolder property value.
	 *
	 * @return javax.swing.JTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextArea getTxtHolder() {
		if (ivjTxtHolder == null) {
			try {
				ivjTxtHolder = new javax.swing.JTextArea();
				ivjTxtHolder.setName("TxtHolder");
				ivjTxtHolder.setText("");
				ivjTxtHolder.setBounds(168, 86, 199, 57);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtHolder;
	}

	/**
	 * Return the JTextField21 property value.
	 *
	 * @return javax.swing.JTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextField getTxtKey() {
		if (ivjTxtKey == null) {
			try {
				ivjTxtKey = new javax.swing.JTextField();
				ivjTxtKey.setName("TxtKey");
				ivjTxtKey.setText("");
				ivjTxtKey.setBounds(168, 29, 197, 20);
				ivjTxtKey.setEnabled(true);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtKey;
	}

	/**
	 * Return the JTextField1 property value.
	 *
	 * @return javax.swing.JTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextField getTxtNumberOfUsers() {
		if (ivjTxtNumberOfUsers == null) {
			try {
				ivjTxtNumberOfUsers = new javax.swing.JTextField();
				ivjTxtNumberOfUsers.setName("TxtNumberOfUsers");
				ivjTxtNumberOfUsers.setText("");
				ivjTxtNumberOfUsers.setBounds(168, 224, 82, 20);
				ivjTxtNumberOfUsers.setEditable(false);
				ivjTxtNumberOfUsers.setEnabled(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtNumberOfUsers;
	}

	/**
	 * Return the JTextField2 property value.
	 *
	 * @return javax.swing.JTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextField getTxtProduct() {
		if (ivjTxtProduct == null) {
			try {
				ivjTxtProduct = new javax.swing.JTextField();
				ivjTxtProduct.setName("TxtProduct");
				ivjTxtProduct.setText("");
				ivjTxtProduct.setBounds(168, 56, 197, 20);
				ivjTxtProduct.setEditable(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtProduct;
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
	 * Initializes connections
	 *
	 *
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() {
		// user code begin {1}
		// user code end
		getBtnLicence().addActionListener(ivjEventHandler);
		getBtnOK().addActionListener(ivjEventHandler);
		getBtnCancel().addActionListener(ivjEventHandler);
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("LicenceGeneratorView");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(396, 373);
			setTitle("Lizenzierung");
			setContentPane(getBaseFrameContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setTitle(getResourceString("CI_Title"));
		// user code end
	}

	/**
	 * Initialize View. Call this method in a View's standard initialize method to setup your stuff.
	 *
	 * see #initialize()  // user code begin {1}..user code end
	 */
	protected void initializeView() throws Exception {
	}

	/**
	 * Check entered licence data of User-Input.
	 */
	private void validateLicence() {
		try {
			checker.checkIn(getTxtKey().getText(), getTxtHolder().getText());
			log.debug("checkIn=>OK");
			getTxtNumberOfUsers().setText("" + checker.getNumberOfUsers());
			log.debug("usr={}", checker.getNumberOfUsers());
			getTxtExpirationDate().setDate(checker.getExpirationDate());
			log.debug("exp={}", checker.getExpirationDate());
			getBtnLicence().setEnabled(false);
			getBtnOK().setEnabled(true);
			getBtnCancel().setEnabled(false);
		} catch (Throwable e) {
			log.debug(e.getLocalizedMessage());
			getTxtNumberOfUsers().setText(getResourceString("CEError"));
			getTxtExpirationDate().setText(getResourceString("CEError"));
		}
	}
}
