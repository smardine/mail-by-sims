package fenetre.comptes.creation;

import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fenetre.EnTitreFenetre;
import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.creation.MlActionCreation.EnActionCreationComptes;
import fenetre.comptes.creation.MlActionCreation.MlActionCreationComptesPop;

public class CreationComptesPop extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextField adresseMail = null;
	private JTextField serveurPOP = null;
	private JTextField serveurSMTP = null;
	private JTextField user = null;
	private JTextField password = null;
	private JButton btValider = null;
	private JButton btAnnuler = null;
	private JTextField nomCompte = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	// private final JTree tree;
	private final EnDefFournisseur defFournisseur;

	/**
	 * This is the default constructor
	 */
	public CreationComptesPop(EnDefFournisseur p_enumChoisi) {
		super();
		this.defFournisseur = p_enumChoisi;
		// this.tree = p_tree;
		initialize();
		rempliChamp(defFournisseur);
		btValider.addActionListener(new MlActionCreationComptesPop(this,
				nomCompte, adresseMail, serveurPOP, serveurSMTP, user,
				password, defFournisseur));
	}

	private void rempliChamp(EnDefFournisseur p_defFournisseur) {
		serveurPOP.setText(p_defFournisseur.getServeurPop());
		serveurSMTP.setText(p_defFournisseur.getServeurSMTP());

	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(513, 311);
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
			jLabel5.setBounds(new Rectangle(269, 88, 202, 26));
			jLabel5.setText("Votre mot de passe");
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(37, 88, 202, 26));
			jLabel4.setText("Votre nom d'utilisateur");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(269, 163, 202, 26));
			jLabel3.setText("serveur SMTP (pour info)");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(37, 163, 202, 26));
			jLabel2.setText("serveur POP (pour info)");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(269, 11, 202, 26));
			jLabel1.setText(" Votre adresse mail complete");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(37, 11, 202, 22));
			jLabel.setText("Donnez un nom à votre compte");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getNomCompte(), null);
			jContentPane.add(getAdresseMail(), null);
			jContentPane.add(getServeurPOP(), null);
			jContentPane.add(getServeurSMTP(), null);
			jContentPane.add(getUser(), null);
			jContentPane.add(getPassword(), null);
			jContentPane.add(getBtValider(), null);
			jContentPane.add(getBtAnnuler(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(jLabel3, null);
			jContentPane.add(jLabel4, null);
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
			adresseMail.setBounds(new Rectangle(269, 48, 202, 26));

			adresseMail.setToolTipText("adresse mail complete");
		}
		return adresseMail;
	}

	/**
	 * This method initializes serveurPOP
	 * @return javax.swing.JTextField
	 */
	private JTextField getServeurPOP() {
		if (serveurPOP == null) {
			serveurPOP = new JTextField();
			serveurPOP.setBounds(new Rectangle(37, 200, 202, 26));

			serveurPOP.setEditable(false);
			serveurPOP.setToolTipText("serveur POP");
		}
		return serveurPOP;
	}

	/**
	 * This method initializes serveurSMTP
	 * @return javax.swing.JTextField
	 */
	private JTextField getServeurSMTP() {
		if (serveurSMTP == null) {
			serveurSMTP = new JTextField();
			serveurSMTP.setBounds(new Rectangle(269, 200, 202, 26));

			serveurSMTP.setEditable(false);
			serveurSMTP.setToolTipText("serveur SMTP");
		}
		return serveurSMTP;
	}

	/**
	 * This method initializes user
	 * @return javax.swing.JTextField
	 */
	private JTextField getUser() {
		if (user == null) {
			user = new JTextField();
			user.setBounds(new Rectangle(37, 125, 202, 26));

			user.setToolTipText("nom d'utilisateur");
		}
		return user;
	}

	/**
	 * This method initializes password
	 * @return javax.swing.JTextField
	 */
	private JTextField getPassword() {
		if (password == null) {
			password = new JTextField();
			password.setBounds(new Rectangle(269, 125, 202, 26));

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
			btValider.setBounds(new Rectangle(90, 233, 97, 32));
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
			btAnnuler.setBounds(new Rectangle(322, 233, 97, 32));
			btAnnuler.setText(EnActionCreationComptes.ANNULER.getLib());
			btAnnuler
					.setActionCommand(EnActionCreationComptes.ANNULER.getLib());
			btAnnuler.addActionListener(new MlActionCreationComptesPop(this));
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
			nomCompte.setBounds(new Rectangle(37, 48, 202, 26));

		}
		return nomCompte;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
