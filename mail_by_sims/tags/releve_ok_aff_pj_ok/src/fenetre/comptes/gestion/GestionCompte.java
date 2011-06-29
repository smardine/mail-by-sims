package fenetre.comptes.gestion;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import bdd.BDRequette;
import fenetre.EnTitreFenetre;
import fenetre.comptes.gestion.MlActionGestion.EnActionComptes;
import fenetre.comptes.gestion.MlActionGestion.MlActionComptes;
import fenetre.principale.Main;

public class GestionCompte extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JButton btCreer = null;
	private JButton btModifier = null;
	private JButton btSupprimer = null;
	private DefaultListModel modelList = null;
	private JPanel jPanel1 = null;
	private JList jList = null;

	/**
	 * This is the default constructor
	 */
	public GestionCompte() {
		super();
		initialize();
		modelList = new DefaultListModel();
		jList.setModel(modelList);
		// on recupere la liste des comptes et on l'affiche
		ArrayList<String> lst = BDRequette.getListeDeComptes();
		for (String s : lst) {
			modelList.addElement(s);
		}
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(601, 286);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle(EnTitreFenetre.GESTION_COMPTE.getLib());
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/logo_appli.png")));
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				dispose();
				new Main();
			}
		});

	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setColumns(2);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);
			jContentPane.add(getJPanel1(), null);
			jContentPane.add(getJPanel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 4;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJButton(), gridBagConstraints);
			jPanel.add(getJButton1(), gridBagConstraints1);
			jPanel.add(getJButton2(), gridBagConstraints2);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (btCreer == null) {
			btCreer = new JButton();
			btCreer.setText(EnActionComptes.CREER.getLib());
			btCreer.setActionCommand(EnActionComptes.CREER.getLib());
			btCreer.addActionListener(new MlActionComptes(this));
		}
		return btCreer;
	}

	/**
	 * This method initializes jButton1
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (btModifier == null) {
			btModifier = new JButton();
			btModifier.setText(EnActionComptes.MODIFIER.getLib());
			btModifier.setActionCommand(EnActionComptes.MODIFIER.getLib());
			btModifier.addActionListener(new MlActionComptes(this));
		}
		return btModifier;
	}

	/**
	 * This method initializes jButton2
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (btSupprimer == null) {
			btSupprimer = new JButton();
			btSupprimer.setText(EnActionComptes.SUPPRIMER.getLib());
			btSupprimer.setActionCommand(EnActionComptes.SUPPRIMER.getLib());
			btSupprimer.addActionListener(new MlActionComptes(this));
		}
		return btSupprimer;
	}

	/**
	 * This method initializes jPanel1
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJList(), BorderLayout.CENTER);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jList
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new JList();
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jList;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
