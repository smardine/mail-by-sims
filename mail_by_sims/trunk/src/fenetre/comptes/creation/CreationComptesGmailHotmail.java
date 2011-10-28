package fenetre.comptes.creation;

import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

import fenetre.EnTitreFenetre;
import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.creation.MlActionCreation.EnActionCreationComptes;
import fenetre.comptes.creation.MlActionCreation.MlActionCreationCompteGmailHotmail;

public class CreationComptesGmailHotmail extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextField adresseMail = null;
	private JTextField password = null;
	private JButton btValider = null;
	private JButton btAnnuler = null;
	private JTextField nomCompte = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel5 = null;
	private final JTree tree;
	private final EnDefFournisseur defFournisseur;

	/**
	 * This is the default constructor
	 */
	public CreationComptesGmailHotmail(EnDefFournisseur p_enumChoisi,
			JTree p_tree) {
		super();
		this.defFournisseur = p_enumChoisi;
		this.tree = p_tree;
		initialize();

		btValider.addActionListener(new MlActionCreationCompteGmailHotmail(
				this, defFournisseur, nomCompte, adresseMail, password, tree));
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(269, 311);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle(EnTitreFenetre.CREATION_COMPTE.getLib());
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/logo_appli.png")));
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel5 = new JLabel();
			jLabel5.setBounds(new Rectangle(35, 160, 202, 26));
			jLabel5.setText("Votre mot de passe");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(36, 84, 202, 26));
			jLabel1.setText(" Votre adresse mail complete");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(37, 12, 202, 22));
			jLabel.setText(" Donnez un nom à votre compte");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getNomCompte(), null);
			jContentPane.add(getAdresseMail(), null);
			jContentPane.add(getPassword(), null);
			jContentPane.add(getBtValider(), null);
			jContentPane.add(getBtAnnuler(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel5, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes adresseMail
	 * @return javax.swing.JTextField
	 */
	private JTextField getAdresseMail() {
		if (adresseMail == null) {
			adresseMail = new JTextField();
			adresseMail.setBounds(new Rectangle(36, 122, 202, 26));

			adresseMail.setToolTipText("adresse mail complete");
		}
		return adresseMail;
	}

	/**
	 * This method initializes password
	 * @return javax.swing.JTextField
	 */
	private JTextField getPassword() {
		if (password == null) {
			password = new JTextField();
			password.setBounds(new Rectangle(35, 198, 202, 26));

			password.setToolTipText("mot de passe");
		}
		return password;
	}

	/**
	 * This method initializes btValider
	 * @return javax.swing.JButton
	 */
	private JButton getBtValider() {
		if (btValider == null) {
			btValider = new JButton();
			btValider.setBounds(new Rectangle(29, 236, 88, 32));
			btValider.setText(EnActionCreationComptes.VALIDER.getLib());
			btValider
					.setActionCommand(EnActionCreationComptes.VALIDER.getLib());

		}
		return btValider;
	}

	/**
	 * This method initializes btAnnuler
	 * @return javax.swing.JButton
	 */
	private JButton getBtAnnuler() {
		if (btAnnuler == null) {
			btAnnuler = new JButton();
			btAnnuler.setBounds(new Rectangle(146, 236, 88, 32));
			btAnnuler.setText(EnActionCreationComptes.ANNULER.getLib());
			btAnnuler
					.setActionCommand(EnActionCreationComptes.ANNULER.getLib());
			btAnnuler.addActionListener(new MlActionCreationCompteGmailHotmail(
					this));
		}
		return btAnnuler;
	}

	/**
	 * This method initializes nomCompte
	 * @return javax.swing.JTextField
	 */
	private JTextField getNomCompte() {
		if (nomCompte == null) {
			nomCompte = new JTextField();
			nomCompte.setBounds(new Rectangle(37, 46, 202, 26));

		}
		return nomCompte;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
