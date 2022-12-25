package ch.softenvironment.view;

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.UserException;

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
 * Output Dialog for application failures.
 *
 * @author Peter Hirzel
 * @deprecated (replace by JOptionPane)
 */
@Deprecated
class ErrorDialog extends BaseDialog {

	// exception to show
	private Throwable exception = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JPanel ivjJDialogContentPane = null;
	private javax.swing.JButton ivjBtnCancel = null;
	private javax.swing.JLabel ivjLblMessage = null;
	private javax.swing.JLabel ivjLblExceptionIcon = null;
	private javax.swing.JButton ivjBtnStacktrace = null;
	private javax.swing.JPanel ivjJPanel1 = null;
	private javax.swing.JPanel ivjJPanel2 = null;
	private javax.swing.JPanel ivjJPanel3 = null;
	private javax.swing.JDialog ivjDlgException = null;
	private javax.swing.JPanel ivjJDialogContentPane1 = null;
	private SimpleEditorPanel ivjPnlErrorText = null;
	private SimpleEditorPanel ivjPnlException = null;

	class IvjEventHandler implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ErrorDialog.this.getBtnCancel()) {
				connEtoM1(e);
			}
			if (e.getSource() == ErrorDialog.this.getBtnStacktrace()) {
				connEtoC1(e);
			}
		}
	}

	/**
	 * Create and open ErrorDialog modally.
	 */
	protected ErrorDialog(java.awt.Dialog owner, String title, String message, Throwable original) {
		super(owner, true);
		setUp(owner, title, message, original);
	}

	protected ErrorDialog(java.awt.Frame owner, String title, String message, Throwable original) {
		super(owner, true);
		setUp(owner, title, message, original);
	}

	/**
	 * connEtoC1: (BtnStacktrace.action.actionPerformed(java.awt.event.ActionEvent) --> ErrorDialog.stacktrace()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.stacktrace();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoM1: (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> ErrorDialog.dispose()V)
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
	 * Disposes the Dialog and then causes show() to return if it is currently blocked.
	 */
	@Override
	public void dispose() {
		getDlgException().dispose();
		super.dispose();
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
				ivjBtnCancel.setText("Cancel");
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
	 * Return the BtnStacktrace property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnStacktrace() {
		if (ivjBtnStacktrace == null) {
			try {
				ivjBtnStacktrace = new javax.swing.JButton();
				ivjBtnStacktrace.setName("BtnStacktrace");
				ivjBtnStacktrace.setText("Stacktrace");
				// user code begin {1}
				ivjBtnStacktrace.setText(getResourceString("BtnStacktrace_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnStacktrace;
	}

	/**
	 * Return the DlgException property value.
	 *
	 * @return javax.swing.JDialog
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JDialog getDlgException() {
		if (ivjDlgException == null) {
			try {
				ivjDlgException = new javax.swing.JDialog(this);
				ivjDlgException.setName("DlgException");
				ivjDlgException.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				ivjDlgException.setBounds(67, 186, 660, 240);
				ivjDlgException.setTitle("Exception");
				getDlgException().setContentPane(getJDialogContentPane1());
				// user code begin {1}
				ivjDlgException.setTitle(getResourceString("DlgException_title"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjDlgException;
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
				ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
				getJDialogContentPane().add(getJPanel1(), "West");
				getJDialogContentPane().add(getJPanel2(), "South");
				getJDialogContentPane().add(getJPanel3(), "Center");
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
	 * Return the JDialogContentPane1 property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJDialogContentPane1() {
		if (ivjJDialogContentPane1 == null) {
			try {
				ivjJDialogContentPane1 = new javax.swing.JPanel();
				ivjJDialogContentPane1.setName("JDialogContentPane1");
				ivjJDialogContentPane1.setLayout(new javax.swing.BoxLayout(getJDialogContentPane1(), javax.swing.BoxLayout.X_AXIS));
				getJDialogContentPane1().add(getPnlException(), getPnlException().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJDialogContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1() {
		if (ivjJPanel1 == null) {
			try {
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.FlowLayout());
				getJPanel1().add(getLblExceptionIcon(), getLblExceptionIcon().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel2() {
		if (ivjJPanel2 == null) {
			try {
				ivjJPanel2 = new javax.swing.JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(new java.awt.FlowLayout());
				getJPanel2().add(getBtnCancel(), getBtnCancel().getName());
				getJPanel2().add(getBtnStacktrace(), getBtnStacktrace().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the JPanel3 property value.
	 *
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel3() {
		if (ivjJPanel3 == null) {
			try {
				ivjJPanel3 = new javax.swing.JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsLblMessage = new java.awt.GridBagConstraints();
				constraintsLblMessage.gridx = 1;
				constraintsLblMessage.gridy = 1;
				constraintsLblMessage.anchor = java.awt.GridBagConstraints.WEST;
				constraintsLblMessage.ipadx = 94;
				constraintsLblMessage.insets = new java.awt.Insets(23, 11, 3, 176);
				getJPanel3().add(getLblMessage(), constraintsLblMessage);

				java.awt.GridBagConstraints constraintsPnlErrorText = new java.awt.GridBagConstraints();
				constraintsPnlErrorText.gridx = 1;
				constraintsPnlErrorText.gridy = 2;
				constraintsPnlErrorText.fill = java.awt.GridBagConstraints.BOTH;
				constraintsPnlErrorText.weightx = 1.0;
				constraintsPnlErrorText.weighty = 1.0;
				constraintsPnlErrorText.ipadx = 284;
				constraintsPnlErrorText.ipady = 73;
				constraintsPnlErrorText.insets = new java.awt.Insets(3, 13, 9, 13);
				getJPanel3().add(getPnlErrorText(), constraintsPnlErrorText);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Return the JLabel1 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblExceptionIcon() {
		if (ivjLblExceptionIcon == null) {
			try {
				ivjLblExceptionIcon = new javax.swing.JLabel();
				ivjLblExceptionIcon.setName("LblExceptionIcon");
				ivjLblExceptionIcon.setIcon(null);
				ivjLblExceptionIcon.setText("");
				// user code begin {1}
				ivjLblExceptionIcon.setIcon(ResourceManager.getImageIcon(ErrorDialog.class, "exception-icon.gif"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblExceptionIcon;
	}

	/**
	 * Return the JLabel2 property value.
	 *
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getLblMessage() {
		if (ivjLblMessage == null) {
			try {
				ivjLblMessage = new javax.swing.JLabel();
				ivjLblMessage.setName("LblMessage");
				ivjLblMessage.setText("");
				ivjLblMessage.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
				// user code begin {1}
				ivjLblMessage.setText(getResourceString("LblMessage_text"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblMessage;
	}

	/**
	 * Return the PnlErrorText property value.
	 *
	 * @return ch.softenvironment.view.SimpleEditorPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private SimpleEditorPanel getPnlErrorText() {
		if (ivjPnlErrorText == null) {
			try {
				ivjPnlErrorText = new ch.softenvironment.view.SimpleEditorPanel();
				ivjPnlErrorText.setName("PnlErrorText");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlErrorText;
	}

	/**
	 * Return the PnlException property value.
	 *
	 * @return ch.softenvironment.view.SimpleEditorPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private SimpleEditorPanel getPnlException() {
		if (ivjPnlException == null) {
			try {
				ivjPnlException = new ch.softenvironment.view.SimpleEditorPanel();
				ivjPnlException.setName("PnlException");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlException;
	}

	/**
	 * Called whenever the part throws an exception.
	 *
	 * @param exception java.lang.Throwable
	 */
	@Override
	protected void handleException(java.lang.Throwable exception) {
		super.traceOnly(exception);
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
		getBtnCancel().addActionListener(ivjEventHandler);
		getBtnStacktrace().addActionListener(ivjEventHandler);
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ErrorDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(433, 182);
			setTitle("Fehler");
			setContentPane(getJDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setSize(600, 250);
		getPnlErrorText().setEditable(false);
		getPnlException().setEditable(false);
		// user code end
	}

	/**
	 * Display the exception context.
	 */
	private void setException(Throwable exception) {
		if (exception == null) {
			getBtnStacktrace().setEnabled(false);
		} else {
			this.exception = exception;
			getPnlException().setText(exception.getLocalizedMessage());
			if (exception instanceof UserException) {
				// typically exception is contained in localizedMessage completely
				getBtnStacktrace().setEnabled(exception.getCause() != null);
			} else {
				// always allow Stacktrace
				getBtnStacktrace().setEnabled(true);
			}
		}
	}

	/**
	 * Set the text.
	 */
	public void setText(String text) {
		getPnlErrorText().setText(text);
	}

	/**
	 * Initialize the Dialog.
	 */
	private void setUp(java.awt.Component owner, String title, String message, Throwable original) {
		initialize();
		setTitle(title == null ? getResourceString("CTUnexpectedError") : title); //$NON-NLS-1$
		getPnlErrorText().setText(message);
		setException(original);
		setRelativeLocation(owner);
		setVisible(true);
	}

	/**
	 * Show the stacktrace of an exception.
	 */
	private void stacktrace() {
		getDlgException().setVisible(true);
		getPnlException().setEditable(false);
		getPnlException().setText(BaseFrame.exceptionToString(exception));
	}
}
