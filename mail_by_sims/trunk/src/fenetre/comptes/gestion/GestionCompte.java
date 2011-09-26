package fenetre.comptes.gestion;

import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import bdd.BDRequette;
import fenetre.EnTitreFenetre;
import fenetre.comptes.gestion.MlActionGestion.EnActionComptes;
import fenetre.comptes.gestion.MlActionGestion.MlActionComptes;
import fenetre.principale.jtree.utiljTree;

public class GestionCompte extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btCreer = null;
	private JButton btModifier = null;
	private JButton btSupprimer = null;
	private DefaultListModel modelList = null;
	public static JList jList = null;
	private JScrollPane jScrollPane = null;
	private final JTree tree;

	/**
	 * This is the default constructor
	 */
	public GestionCompte(JTree p_treeCompte) {
		super();
		this.tree = p_treeCompte;
		initialize();
		modelList = new DefaultListModel();
		jList.setModel(modelList);
		// on recupere la liste des comptes et on l'affiche
		BDRequette bd = new BDRequette();
		MlListeCompteMail lst = bd.getListeDeComptes();
		bd.closeConnexion();
		for (MlCompteMail cpt : lst) {
			modelList.addElement(cpt.getNomCompte());
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
				utiljTree.reloadJtree(tree);
			}
		});

	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButton2(), null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton1(), null);
			jContentPane.add(getJScrollPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (btCreer == null) {
			btCreer = new JButton();
			btCreer.setText(EnActionComptes.CREER.getLib());
			btCreer.setBounds(new Rectangle(387, 34, 99, 41));
			btCreer.setActionCommand(EnActionComptes.CREER.getLib());
			btCreer.addActionListener(new MlActionComptes(this, tree));
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
			btModifier.setBounds(new Rectangle(387, 109, 99, 41));
			btModifier.setActionCommand(EnActionComptes.MODIFIER.getLib());
			btModifier.addActionListener(new MlActionComptes(this, tree));
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
			btSupprimer.setBounds(new Rectangle(387, 184, 99, 41));
			btSupprimer.setActionCommand(EnActionComptes.SUPPRIMER.getLib());
			btSupprimer.addActionListener(new MlActionComptes(this, tree));
		}
		return btSupprimer;
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

	/**
	 * This method initializes jScrollPane
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(9, 7, 294, 238));
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
