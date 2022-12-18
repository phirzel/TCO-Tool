package ch.softenvironment.jomm.mvc.view;
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

import ch.ehi.basics.view.FileChooser;
import ch.softenvironment.jomm.target.sql.msaccess.MsAccessObjectServerFactory;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.CommonUserAccess;

/**
 * Login Dialog for Application startup.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.4 $ $Date: 2008-04-18 18:22:55 $
 */
public class DbLoginDialog extends BaseDialog {

	// vars
	private String userId = "";//$NON-NLS-1$
	private String password = "";//$NON-NLS-1$
	private java.util.List urls = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JPanel ivjJDialogContentPane = null;
	private javax.swing.JButton ivjBtnCancel = null;
	private javax.swing.JButton ivjBtnOk = null;
	private javax.swing.JTextField ivjTxtId = null;
	private javax.swing.JPasswordField ivjTxtPassword = null;
	private javax.swing.JLabel ivjLblId = null;
	private javax.swing.JLabel ivjLblPassword = null;
	private javax.swing.JLabel ivjLblUrl = null;
	private javax.swing.JComboBox ivjCbxUrl = null;
	private javax.swing.JButton ivjBtnExternalFile = null;
	private javax.swing.JCheckBox ivjChxAdvanced = null;
	private javax.swing.JRadioButton ivjRbtDbmsAccess = null;
	private javax.swing.JRadioButton ivjRbtDbmsOther = null;
	private javax.swing.JPanel ivjPnlAdvanced = null;

	class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener {

		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DbLoginDialog.this.getBtnCancel()) {
				connEtoM1(e);
			}
			if (e.getSource() == DbLoginDialog.this.getBtnOk()) {
				connEtoC1(e);
			}
			if (e.getSource() == DbLoginDialog.this.getBtnExternalFile()) {
				connEtoC2(e);
			}
		}

		@Override
		public void focusGained(java.awt.event.FocusEvent e) {
			if (e.getSource() == DbLoginDialog.this.getTxtId()) {
				connEtoM2(e);
			}
			if (e.getSource() == DbLoginDialog.this.getTxtPassword()) {
				connEtoM3(e);
			}
		}

		@Override
		public void focusLost(java.awt.event.FocusEvent e) {
		}

		@Override
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getSource() == DbLoginDialog.this.getChxAdvanced()) {
				connEtoC3(e);
			}
		}
	}

	/**
	 * Create and open LoginDialog modally.
	 */
	public DbLoginDialog(java.awt.Frame owner, String url) {
		this(owner, ch.softenvironment.util.ListUtils.createList(url));
	}

	/**
	 * Create and open LoginDialog modally.
	 */
	public DbLoginDialog(java.awt.Frame owner, java.util.List urlAlternatives) {
		super(owner, true);
		this.urls = urlAlternatives;
		initialize();
		setCenterLocation();
		getCbxUrl().setModel(new javax.swing.DefaultComboBoxModel(urlAlternatives.toArray()));
		changeAdvanced();
		show();

		//	getBtnOk().requestFocus();
	}

	/**
	 * Show/hide advanced settings.
	 */
	private void changeAdvanced() {
		if (getChxAdvanced().isSelected()) {
			setSize((int) getSize().getWidth(), 280);
		} else {
			setSize((int) getSize().getWidth(), 187);
		}
	}

	/**
	 * connEtoC1:  (BtnOk.action.actionPerformed(java.awt.event.ActionEvent) --> LoginDialog.btnOk_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
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
	 * connEtoC2:  (BtnExternalFile.action.actionPerformed(java.awt.event.ActionEvent) --> DbLoginDialog.externalFile()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.externalFile();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3:  (ChxAdvanced.item.itemStateChanged(java.awt.event.ItemEvent) --> DbLoginDialog.changeAdvanced()V)
	 *
	 * @param arg1 java.awt.event.ItemEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC3(java.awt.event.ItemEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.changeAdvanced();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoM1:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> ErrorDialog.dispose()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.dispose();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoM2:  (TxtId.focus.focusGained(java.awt.event.FocusEvent) --> TxtId.selectAll()V)
	 *
	 * @param arg1 java.awt.event.FocusEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM2(java.awt.event.FocusEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getTxtId().selectAll();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoM3:  (TxtPassword.focus.focusGained(java.awt.event.FocusEvent) --> TxtPassword.selectAll()V)
	 *
	 * @param arg1 java.awt.event.FocusEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoM3(java.awt.event.FocusEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			getTxtPassword().selectAll();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * Comment
	 */
	private void externalFile() {
		FileChooser openDialog = new FileChooser(System.getProperty("user.home"));
		openDialog.setDialogTitle(CommonUserAccess.getTitleFileOpen());
		openDialog.addChoosableFileFilter(MsAccessObjectServerFactory.createGenericFileFilter());

		if (openDialog.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
			//TODO extend for more generic usage
			String dbUrl = MsAccessObjectServerFactory.createOdbcUrl(openDialog.getSelectedFile().getAbsolutePath());
			urls.add(0, dbUrl);
			getCbxUrl().setModel(new javax.swing.DefaultComboBoxModel(urls.toArray()));
		}

	}

	/**
	 * Return the JButton1 property value.
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
				ivjBtnCancel.setBounds(279, 121, 103, 25);
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
	 * Return the BtnExternalFile property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnExternalFile() {
		if (ivjBtnExternalFile == null) {
			try {
				ivjBtnExternalFile = new javax.swing.JButton();
				ivjBtnExternalFile.setName("BtnExternalFile");
				ivjBtnExternalFile.setText("Have file...");
				ivjBtnExternalFile.setBounds(13, 31, 111, 25);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnExternalFile;
	}

	/**
	 * Return the BtnOk property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnOk() {
		if (ivjBtnOk == null) {
			try {
				ivjBtnOk = new javax.swing.JButton();
				ivjBtnOk.setName("BtnOk");
				ivjBtnOk.setSelected(false);
				ivjBtnOk.setText("OK");
				ivjBtnOk.setBounds(163, 121, 106, 25);
				ivjBtnOk.setNextFocusableComponent(getBtnCancel());
				// user code begin {1}
				ivjBtnOk.setText(getOKString());
				//			ivjBtnOk.setDefaultCapable(true);
				//Accelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.ActionEvent.ACTION_PERFORMED));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnOk;
	}

	/**
	 * Return the CbxUrl property value.
	 *
	 * @return javax.swing.JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getCbxUrl() {
		if (ivjCbxUrl == null) {
			try {
				ivjCbxUrl = new javax.swing.JComboBox();
				ivjCbxUrl.setName("CbxUrl");
				ivjCbxUrl.setBounds(163, 79, 335, 23);
				ivjCbxUrl.setEditable(true);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbxUrl;
	}

	/**
	 * Return the ChxAdvanced property value.
	 *
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getChxAdvanced() {
		if (ivjChxAdvanced == null) {
			try {
				ivjChxAdvanced = new javax.swing.JCheckBox();
				ivjChxAdvanced.setName("ChxAdvanced");
				ivjChxAdvanced.setText("Advanced");
				ivjChxAdvanced.setBounds(400, 122, 97, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjChxAdvanced;
	}

	/**
	 * Return the JDialogContentPane property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJDialogContentPane() {
		if (ivjJDialogContentPane == null) {
			try {
				ivjJDialogContentPane = new javax.swing.JPanel();
				ivjJDialogContentPane.setName("JDialogContentPane");
				ivjJDialogContentPane.setLayout(null);
				getJDialogContentPane().add(getLblPassword(), getLblPassword().getName());
				getJDialogContentPane().add(getLblId(), getLblId().getName());
				getJDialogContentPane().add(getTxtId(), getTxtId().getName());
				getJDialogContentPane().add(getTxtPassword(), getTxtPassword().getName());
				getJDialogContentPane().add(getBtnOk(), getBtnOk().getName());
				getJDialogContentPane().add(getBtnCancel(), getBtnCancel().getName());
				getJDialogContentPane().add(getLblUrl(), getLblUrl().getName());
				getJDialogContentPane().add(getCbxUrl(), getCbxUrl().getName());
				getJDialogContentPane().add(getChxAdvanced(), getChxAdvanced().getName());
				getJDialogContentPane().add(getPnlAdvanced(), getPnlAdvanced().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJDialogContentPane;
	}

	/**
	 * Return the JLabel1 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblId() {
		if (ivjLblId == null) {
			try {
				ivjLblId = new javax.swing.JLabel();
				ivjLblId.setName("LblId");
				ivjLblId.setText("Login-Name:");
				ivjLblId.setBounds(10, 26, 148, 14);
				// user code begin {1}
				ivjLblId.setText(getResourceString("LblId_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblId;
	}

	/**
	 * Return the JLabel11 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblPassword() {
		if (ivjLblPassword == null) {
			try {
				ivjLblPassword = new javax.swing.JLabel();
				ivjLblPassword.setName("LblPassword");
				ivjLblPassword.setText("Passwort:");
				ivjLblPassword.setBounds(10, 52, 148, 14);
				// user code begin {1}
				ivjLblPassword.setText(getResourceString("LblPassword_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblPassword;
	}

	/**
	 * Return the LblUrl property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblUrl() {
		if (ivjLblUrl == null) {
			try {
				ivjLblUrl = new javax.swing.JLabel();
				ivjLblUrl.setName("LblUrl");
				ivjLblUrl.setToolTipText("Datenbank-URL");
				ivjLblUrl.setText("URL:");
				ivjLblUrl.setBounds(10, 82, 148, 14);
				// user code begin {1}
				ivjLblUrl.setText(getResourceString("LblUrl_text"));
				ivjLblUrl.setToolTipText(getResourceString("LblUrl_toolTip"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblUrl;
	}

	/**
	 * Return Password for UserId.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Return the JPanel1 property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getPnlAdvanced() {
		if (ivjPnlAdvanced == null) {
			try {
				ivjPnlAdvanced = new javax.swing.JPanel();
				ivjPnlAdvanced.setName("PnlAdvanced");
				ivjPnlAdvanced.setLayout(null);
				ivjPnlAdvanced.setBounds(11, 159, 485, 70);
				getPnlAdvanced().add(getBtnExternalFile(), getBtnExternalFile().getName());
				getPnlAdvanced().add(getRbtDbmsAccess(), getRbtDbmsAccess().getName());
				getPnlAdvanced().add(getRbtDbmsOther(), getRbtDbmsOther().getName());
				// user code begin {1}
				getPnlAdvanced().setBorder(javax.swing.BorderFactory.createCompoundBorder(
					javax.swing.BorderFactory.createTitledBorder("Choose your own URL"),
					javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlAdvanced;
	}

	/**
	 * Return the RbtDbmsAccess property value.
	 *
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getRbtDbmsAccess() {
		if (ivjRbtDbmsAccess == null) {
			try {
				ivjRbtDbmsAccess = new javax.swing.JRadioButton();
				ivjRbtDbmsAccess.setName("RbtDbmsAccess");
				ivjRbtDbmsAccess.setSelected(true);
				ivjRbtDbmsAccess.setText("MS Access (mdb)");
				ivjRbtDbmsAccess.setBounds(153, 32, 137, 22);
				ivjRbtDbmsAccess.setEnabled(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRbtDbmsAccess;
	}

	/**
	 * Return the RbtDbmsOther property value.
	 *
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getRbtDbmsOther() {
		if (ivjRbtDbmsOther == null) {
			try {
				ivjRbtDbmsOther = new javax.swing.JRadioButton();
				ivjRbtDbmsOther.setName("RbtDbmsOther");
				ivjRbtDbmsOther.setText("Other");
				ivjRbtDbmsOther.setBounds(299, 32, 108, 22);
				ivjRbtDbmsOther.setEnabled(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRbtDbmsOther;
	}

	/**
	 * Return the TxtId property value.
	 *
	 * @return javax.swing.JTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextField getTxtId() {
		if (ivjTxtId == null) {
			try {
				ivjTxtId = new javax.swing.JTextField();
				ivjTxtId.setName("TxtId");
				ivjTxtId.setBounds(163, 23, 200, 20);
				ivjTxtId.setNextFocusableComponent(getTxtPassword());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtId;
	}

	/**
	 * Return the TxtPassword property value.
	 *
	 * @return javax.swing.JPasswordField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPasswordField getTxtPassword() {
		if (ivjTxtPassword == null) {
			try {
				ivjTxtPassword = new javax.swing.JPasswordField();
				ivjTxtPassword.setName("TxtPassword");
				ivjTxtPassword.setBounds(163, 49, 200, 20);
				ivjTxtPassword.setNextFocusableComponent(getBtnOk());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtPassword;
	}

	/**
	 * Return URL of Target-System.
	 */
	public String getUrl() {
		return (String) getCbxUrl().getSelectedItem();
	}

	/**
	 * Return UserId.
	 */
	public String getUserId() {
		return userId;
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
	 * @throws java.lang.Exception The exception description.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() {
		// user code begin {1}
		// user code end
		getBtnCancel().addActionListener(ivjEventHandler);
		getBtnOk().addActionListener(ivjEventHandler);
		getTxtId().addFocusListener(ivjEventHandler);
		getTxtPassword().addFocusListener(ivjEventHandler);
		getBtnExternalFile().addActionListener(ivjEventHandler);
		getChxAdvanced().addItemListener(ivjEventHandler);
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("LoginDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setResizable(false);
			setSize(508, 241);
			setTitle("Login");
			setContentPane(getJDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getTxtId().setText(System.getProperty("user.name"));
		setTitle(getResourceString("CT_title"));

		javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(this);
		if (root != null) {
			root.setDefaultButton(getBtnOk());
			getBtnCancel().setDefaultCapable(false);
		}
		// user code end
	}

	/**
	 * Keep Id and Password
	 */
	@Override
	protected boolean save() {
		userId = getTxtId().getText();
		password = new String(getTxtPassword().getPassword());

		return super.save();
	}
}
