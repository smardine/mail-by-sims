package fenetre.comptes.creation.MlActionCreation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import mdl.MlCompteMail;
import releve.imap.util.messageUtilisateur;
import thread.threadVerifCompte;
import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.creation.CreationComptesGmailHotmail;

public class MlActionCreationCompteGmailHotmail implements ActionListener {
	private final String TAG = this.getClass().getSimpleName();
	private JTextField adresse;
	private JTextField password;
	private JTextField nomCompte;
	private final CreationComptesGmailHotmail fenetre;

	// private JTree tree;
	private EnDefFournisseur defFournisseur;

	/**
	 * Constructeur par defaut
	 */
	public MlActionCreationCompteGmailHotmail(
			CreationComptesGmailHotmail p_fenetre) {
		this.fenetre = p_fenetre;
	}

	/**
	 * Constructeur qui permet de recuperer les infos en provenance de la
	 * fenetre
	 * @param p_defFournisseur
	 * @param p_adresse
	 * @param p_pop
	 * @param p_tree
	 */
	public MlActionCreationCompteGmailHotmail(
			CreationComptesGmailHotmail p_creationComptesGmailHotmail,
			EnDefFournisseur p_defFournisseur, JTextField p_nomCompte,
			JTextField p_adresse, JTextField p_password) {
		this.fenetre = p_creationComptesGmailHotmail;
		this.defFournisseur = p_defFournisseur;
		this.nomCompte = p_nomCompte;
		this.adresse = p_adresse;
		this.password = p_password;
		// this.tree = p_tree;

	}

	@Override
	public void actionPerformed(ActionEvent p_arg0) {
		if (EnActionCreationComptes.VALIDER.getLib().equals(
				p_arg0.getActionCommand())) {
			// verification des information saisie par l'utilisateur
			if (!verifChamp()) {
				return;
			}
			MlCompteMail compteMail = valorisationMlCompte();
			threadVerifCompte t = new threadVerifCompte(compteMail, fenetre);
			t.start();
			// CompteMailFactory crea = new CompteMailFactory();
			//
			// boolean resultConnexion = crea.testBal(compteMail);
			// if (!resultConnexion) {
			// messageUtilisateur
			// .affMessageErreur(
			// TAG,
			// "Le test de connexion a votre boite aux lettres à échoué.\r\nMerci de vérifier votre saisie");
			// return;
			// } else {
			// boolean result = crea.creationCompteMail(compteMail);
			// if (!result) {
			// messageUtilisateur.affMessageErreur(TAG,
			// "le compte n'a pas été correctement enregistré");
			// } else {
			// messageUtilisateur
			// .affMessageInfo("Le compte à été créer correctement");
			// fenetre.dispose();
			// new GestionCompte(tree);
			// }
			// }

		}
		if (EnActionCreationComptes.ANNULER.getLib().equals(
				p_arg0.getActionCommand())) {
			// simple fermeture de la fenetre.
			fenetre.dispose();
		}

	}

	/**
	 * @return
	 */
	private MlCompteMail valorisationMlCompte() {
		MlCompteMail compteMail = new MlCompteMail(null);
		compteMail.setNomCompte(nomCompte.getText());
		compteMail.setServeurReception(defFournisseur.getServeurPop());
		compteMail.setPortPop(defFournisseur.getPortPop());
		compteMail.setServeurSMTP(defFournisseur.getServeurSMTP());
		compteMail.setPortSMTP(defFournisseur.getPortSMTP());
		compteMail.setUserName(adresse.getText().replace("@gmail.com", ""));
		compteMail.setPassword(password.getText());
		compteMail.setTypeCompte(defFournisseur.getTypeCompte());
		return compteMail;
	}

	private boolean verifChamp() {
		if (utilityCreation.verifAdresseEtNom(adresse, nomCompte) || //
				utilityCreation.verifChampPassword(password)) {
			messageUtilisateur.affMessageErreur(TAG,
					"Veuillez vérifier votre saisie");
			return false;
		}
		return true;

	}

}
