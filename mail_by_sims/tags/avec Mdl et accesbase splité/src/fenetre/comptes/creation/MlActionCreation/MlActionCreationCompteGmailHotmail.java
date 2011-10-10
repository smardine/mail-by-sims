package fenetre.comptes.creation.MlActionCreation;

import hotmail.util.methodeHotmail;
import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JTree;

import mdl.MlCompteMail;
import bdd.accestable.compte.AccesTableCompte;
import bdd.accestable.dossier.AccesTableDossier;
import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.EnDossierBase;
import fenetre.comptes.creation.CreationComptesGmailHotmail;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionCreationCompteGmailHotmail implements ActionListener {
	private JTextField adresse;
	private JTextField password;
	private JTextField nomCompte;
	private final CreationComptesGmailHotmail fenetre;

	private JTree tree;
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
			JTextField p_adresse, JTextField p_password, JTree p_tree) {
		this.fenetre = p_creationComptesGmailHotmail;
		this.defFournisseur = p_defFournisseur;
		this.nomCompte = p_nomCompte;
		this.adresse = p_adresse;
		this.password = p_password;
		this.tree = p_tree;

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
			boolean resultConnexion = testConnexionMlCompte(compteMail);
			if (!resultConnexion) {
				messageUtilisateur
						.affMessageErreur("Le test de connexion a votre boite aux lettres à échoué.\r\nMerci de vérifier votre saisie");
				return;
			} else {
				enregistrementMlCompte(compteMail);
			}

		}
		if (EnActionCreationComptes.ANNULER.getLib().equals(
				p_arg0.getActionCommand())) {
			// simple fermeture de la fenetre.
			fenetre.dispose();
		}

	}

	/**
	 * @param compteMail
	 */
	private void enregistrementMlCompte(MlCompteMail compteMail) {
		AccesTableCompte accesTableCompte = new AccesTableCompte();

		boolean result = accesTableCompte.createNewCompte(compteMail);
		if (result) {
			int idCpt = accesTableCompte.getIdComptes(nomCompte.getText());
			compteMail.setIdCompte(idCpt);
			// creation des dossiers de base (boite de reception,
			// message
			// envoyé, corbeille, spam) avec un id Dossierparent=0
			List<String> lstDossierBase = new ArrayList<String>();
			EnDossierBase[] lstEnum = EnDossierBase.values();
			for (EnDossierBase dossier : lstEnum) {
				if (dossier != EnDossierBase.ROOT) {
					lstDossierBase.add(dossier.getLib());
				}
			}
			AccesTableDossier accesTableDossier = new AccesTableDossier();
			result = accesTableDossier.createListeDossierDeBase(compteMail,
					lstDossierBase);
		}

		if (!result) {
			messageUtilisateur
					.affMessageErreur("le compte n'a pas été correctement enregistré");
		} else {
			messageUtilisateur
					.affMessageInfo("Le compte à été créer correctement");
			fenetre.dispose();
			// MlActionJtree actionTree = new MlActionJtree(tree, null);
			// actionTree.valueChanged(null);

			new GestionCompte(tree);
		}

		// bd.closeConnexion();
	}

	/**
	 * @param compteMail
	 * @return
	 */
	private boolean testConnexionMlCompte(MlCompteMail compteMail) {
		boolean resultConnexion = false;
		switch (defFournisseur.getTypeCompte()) {
			case GMAIL:
				resultConnexion = methodeImap.testBalIMAP(compteMail);
				break;
			case HOTMAIL:
				resultConnexion = methodeHotmail.testBalHotmail(compteMail);
				break;
		}
		return resultConnexion;
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
			messageUtilisateur
					.affMessageErreur("Veuillez vérifier votre saisie");
			return false;
		}
		return true;

	}

}
