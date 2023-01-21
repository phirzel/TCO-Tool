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

import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.view.BaseDialog;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Display a simple DbCode (by means concrete DbCode has no further propertiese than #name).
 *
 * @author Peter Hirzel
 * @since 1.4 (2006-07-05)
 * @see DbCodeManageView
 */
class DbSimpleCodeView extends BaseDialog implements PropertyChangeListener {

    private javax.swing.JPanel jContentPane = null;
    private JPanel PnlData = null;
    private JPanel PnlControl = null;
    private JButton btnOk = null;
    private JButton btnCancel = null;
    private JLabel lblName = null;
    private DbNlsStringView pnlNls = null;

    /**
     * This is the default constructor
     *
     * @see DbCodeManageView
     */
    protected DbSimpleCodeView(Component owner, String title, DbNlsString nlsString) {
        super(owner, true);
        initialize();
        setTitle(title);
        getPnlNls().setDbNlsString(nlsString);
        nlsString.addPropertyChangeListener(this);
        propertyChange(null);
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setTitle("<DbCode>");
        this.setSize(463, 157);
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
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getPnlData(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getPnlControl(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPnlData() {
        if (PnlData == null) {
            lblName = new JLabel();
            PnlData = new JPanel();
            PnlData.setLayout(null);
            lblName.setText("Name:");
            lblName.setBounds(14, 22, 114, 16);
            PnlData.add(lblName, null);
            PnlData.add(getPnlNls(), null);
        }
        return PnlData;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPnlControl() {
        if (PnlControl == null) {
            PnlControl = new JPanel();
            PnlControl.add(getBtnOk(), null);
            PnlControl.add(getBtnCancel(), null);
        }
        return PnlControl;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getBtnOk() {
        if (btnOk == null) {
            btnOk = new JButton();
            btnOk.setText(getOKString());
            btnOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    okPressed();
                }
            });
        }
        return btnOk;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton();
            btnCancel.setText(getCancelString());

            btnCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    cancelPressed();
                }
            });
        }
        return btnCancel;
    }

    /**
     * This method initializes dbNlsStringView
     *
     * @return ch.softenvironment.jomm.view.DbNlsStringView
     */
    private DbNlsStringView getPnlNls() {
        if (pnlNls == null) {
            pnlNls = new DbNlsStringView();
            pnlNls.setBounds(142, 18, 305, 27);
        }
        return pnlNls;
    }

    @Override
    public void dispose() {
        getPnlNls().getDbNlsString().removePropertyChangeListener(this);
        super.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // do not allow save of empty Strings
        getBtnOk().setEnabled(!StringUtils.isNullOrEmpty(getPnlNls().getDbNlsString().getValue()));
    }
}  //  @jve:decl-index=0:visual-constraint="11,25"
