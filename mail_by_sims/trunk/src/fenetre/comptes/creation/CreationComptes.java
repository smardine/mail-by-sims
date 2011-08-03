package fenetre.comptes.creation;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

import fenetre.EnTitreFenetre;
import fenetre.comptes.creation.MlActionCreation.EnActionCreationComptes;
import fenetre.comptes.creation.MlActionCreation.MlActionCreationComptes;

public class CreationComptes extends JFrame {

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
	private final JTree tree;

	/**
	 * This is the default constructor
	 */
	public CreationComptes(JTree p_treeCompte) {
		super();
		this.tree = p_treeCompte;
		initialize();
		btValider.addActionListener(new MlActionCreationComptes(this,
				nomCompte, adresseMail, serveurPOP, serveurSMTP, user,
				password, tree));
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(513, 304);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle(EnTitreFenetre.CREATION_COMPTE.getLib());
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel5 = new JLabel();
			jLabel5.setBounds(new Rectangle(277, 159, 187, 26));
			jLabel5.setText("mot de passe");
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(45, 159, 187, 26));
			jLabel4.setText("nom d'utilisateur");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(277, 85, 186, 26));
			jLabel3.setText("serveur SMTP");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(42, 85, 192, 26));
			jLabel2.setText("serveur POP");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(277, 11, 187, 26));
			jLabel1.setText(" adresse mail complete");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(42, 11, 193, 26));
			jLabel.setText("nom du compte");
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
			adresseMail.setText("equinox.simon@laposte.net");
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
			serveurPOP.setBounds(new Rectangle(37, 122, 202, 26));
			serveurPOP.setText("pop.laposte.net");
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
			serveurSMTP.setBounds(new Rectangle(269, 122, 202, 26));
			serveurSMTP.setText("smtp.laposte.net");
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
			user.setBounds(new Rectangle(37, 196, 202, 26));
			user.setText("equinox.simon");
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
			password.setBounds(new Rectangle(269, 196, 202, 26));
			password.setText("gouranga");
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
			btAnnuler.addActionListener(new MlActionCreationComptes(this));
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
			nomCompte.setText("la poste");
		}
		return nomCompte;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
