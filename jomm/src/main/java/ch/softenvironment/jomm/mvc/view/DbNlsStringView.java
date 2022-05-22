package ch.softenvironment.jomm.mvc.view;

import java.util.Locale;

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

/**
 * Represent a DbNlsString to manage all its Translations.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DbNlsStringView extends ch.softenvironment.view.BasePanel {

	private javax.swing.JButton ivjBtnTranslation = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private ch.softenvironment.view.SimpleEditorPanel ivjTxtNls = null;
	private boolean ivjConnPtoP1Aligning = false;
	private ch.softenvironment.jomm.datatypes.DbNlsString ivjnlsString = null;

	class IvjEventHandler implements ch.softenvironment.view.SimpleEditorPanelListener, java.awt.event.ActionListener, java.beans.PropertyChangeListener {

		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DbNlsStringView.this.getBtnTranslation()) {
				connEtoC1(e);
			}
		}

		@Override
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() == DbNlsStringView.this.getnlsString() && (evt.getPropertyName().equals("value"))) {
				connPtoP1SetTarget();
			}
		}

		@Override
		public void txaEditorKey_keyReleased(java.util.EventObject newEvent) {
			if (newEvent.getSource() == DbNlsStringView.this.getTxtNls()) {
				connPtoP1SetSource();
			}
		}
	}
	//	private ch.softenvironment.jomm.DbNlsString fieldNlsString = new ch.softenvironment.jomm.DbNlsString(null);

	/**
	 * DbNlsStringView constructor comment.
	 */
	public DbNlsStringView() {
		super();
		initialize();
	}

	/**
	 * connEtoC1:  (BtnTranslation.action.actionPerformed(java.awt.event.ActionEvent) --> DbNlsStringView.showTranslation()V)
	 *
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.showTranslation();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP1SetSource:  (DbNlsString.value <--> TxtNls.text)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP1SetSource() {
		/* Set the source from the target */
		try {
			if (ivjConnPtoP1Aligning == false) {
				// user code begin {1}
				// user code end
				ivjConnPtoP1Aligning = true;
				if ((getnlsString() != null)) {
					getnlsString().setValue(getTxtNls().getText());
				}
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
	 * connPtoP1SetTarget:  (DbNlsString.value <--> TxtNls.text)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP1SetTarget() {
		/* Set the target from the source */
		try {
			if (ivjConnPtoP1Aligning == false) {
				// user code begin {1}
				// user code end
				ivjConnPtoP1Aligning = true;
				if ((getnlsString() != null)) {
					getTxtNls().setText(getnlsString().getValue());
				}
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
	 * Return the BtnTranslation property value.
	 *
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getBtnTranslation() {
		if (ivjBtnTranslation == null) {
			try {
				ivjBtnTranslation = new javax.swing.JButton();
				ivjBtnTranslation.setName("BtnTranslation");
				ivjBtnTranslation.setToolTipText("Sprache wechseln");
				ivjBtnTranslation.setText("...");
				ivjBtnTranslation.setEnabled(true);
				// user code begin {1}
				ivjBtnTranslation.setEnabled(false);
				//			ivjBtnTranslation.setVisible(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnTranslation;
	}

	/**
	 * Method generated to support the promotion of the dbNlsString attribute.
	 *
	 * @return ch.softenvironment.jomm.DbNlsString
	 */
	public ch.softenvironment.jomm.datatypes.DbNlsString getDbNlsString() {
		return getnlsString();
	}

	/**
	 * Return the DbNlsString property value.
	 *
	 * @return ch.softenvironment.jomm.DbNlsString
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ch.softenvironment.jomm.datatypes.DbNlsString getnlsString() {
		// user code begin {1}
		// user code end
		return ivjnlsString;
	}

	/**
	 * Return the TxtNls property value.
	 *
	 * @return ch.softenvironment.view.SimpleEditorPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ch.softenvironment.view.SimpleEditorPanel getTxtNls() {
		if (ivjTxtNls == null) {
			try {
				ivjTxtNls = new ch.softenvironment.view.SimpleEditorPanel();
				ivjTxtNls.setName("TxtNls");
				// user code begin {1}
				ivjTxtNls.setToolTipText("Mehrsprachige Bezeichnung");
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtNls;
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
		getBtnTranslation().addActionListener(ivjEventHandler);
		getTxtNls().addSimpleEditorPanelListener(ivjEventHandler);
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
			setName("DbNlsStringView");
			setLayout(new java.awt.GridBagLayout());
			setSize(211, 26);

			java.awt.GridBagConstraints constraintsTxtNls = new java.awt.GridBagConstraints();
			constraintsTxtNls.gridx = 1;
			constraintsTxtNls.gridy = 1;
			constraintsTxtNls.fill = java.awt.GridBagConstraints.BOTH;
			constraintsTxtNls.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsTxtNls.weightx = 1.0;
			constraintsTxtNls.weighty = 1.0;
			constraintsTxtNls.ipadx = 144;
			constraintsTxtNls.ipady = -2;
			constraintsTxtNls.insets = new java.awt.Insets(0, 0, 0, 1);
			add(getTxtNls(), constraintsTxtNls);

			java.awt.GridBagConstraints constraintsBtnTranslation = new java.awt.GridBagConstraints();
			constraintsBtnTranslation.gridx = 2;
			constraintsBtnTranslation.gridy = 1;
			constraintsBtnTranslation.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsBtnTranslation.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBtnTranslation.insets = new java.awt.Insets(0, 1, 1, 0);
			add(getBtnTranslation(), constraintsBtnTranslation);
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Method generated to support the promotion of the dbNlsString attribute.
	 *
	 * @param arg1 ch.softenvironment.jomm.DbNlsString
	 */
	public void setDbNlsString(ch.softenvironment.jomm.datatypes.DbNlsString nlsString) {
		setnlsString(nlsString);
		if (nlsString == null) {
			getBtnTranslation().setText("...");
			getBtnTranslation().setToolTipText("Sprache wechseln (aktuell: <ohne>)");
		} else {
			String language = Locale.getDefault().getLanguage();
			getBtnTranslation().setText(language);
			getBtnTranslation().setToolTipText("Sprache wechseln (aktuell: <" + language + ">)");
		}
	}

	/**
	 * Set the DbNlsString to a new value.
	 *
	 * @param newValue ch.softenvironment.jomm.DbNlsString
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void setnlsString(ch.softenvironment.jomm.datatypes.DbNlsString newValue) {
		if (ivjnlsString != newValue) {
			try {
				ch.softenvironment.jomm.datatypes.DbNlsString oldValue = getnlsString();
				/* Stop listening for events from the current object */
				if (ivjnlsString != null) {
					ivjnlsString.removePropertyChangeListener(ivjEventHandler);
				}
				ivjnlsString = newValue;

				/* Listen for events from the new object */
				if (ivjnlsString != null) {
					ivjnlsString.addPropertyChangeListener(ivjEventHandler);
				}
				connPtoP1SetTarget();
				firePropertyChange("dbNlsString", oldValue, newValue);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		// user code begin {3}
		// user code end
	}

	/**
	 *
	 */
	private void showTranslation() {
		//TODO
		ch.softenvironment.view.BaseDialog.showWarning(this, "Uebersetzungen definieren", "Diese Funktion ist leider noch nicht implementiert.");
	}
}
