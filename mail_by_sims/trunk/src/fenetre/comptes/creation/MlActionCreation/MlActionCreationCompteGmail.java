package fenetre.comptes.creation.MlActionCreation;

import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JTree;

import mdl.MlCompteMail;
import bdd.BDRequette;
import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.EnDossierBase;
import fenetre.comptes.EnTypeCompte;
import fenetre.comptes.creation.CreationComptesGmail;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionCreationCompteGmail implements ActionListener {
	private JTextField adresse;
	private JTextField pop;
	private JTextField smtp;
	private JTextField user;
	private JTextField password;
	private JTextField nomCompte;
	private final CreationComptesGmail fenetre;

	private JTree tree;
	private EnDefFournisseur defFournisseur;

	/**
	 * Constructeur par defaut
	 */
	public MlActionCreationCompteGmail(CreationComptesGmail p_fenetre) {
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
	public MlActionCreationCompteGmail(
			CreationComptesGmail p_creationComptesGmail,
			EnDefFournisseur p_defFournisseur, JTextField p_nomCompte,
			JTextField p_adresse, JTextField p_password, JTree p_tree) {
		this.fenetre = p_creationComptesGmail;
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
			MlCompteMail compteMail = new MlCompteMail();
			compteMail.setNomCompte(nomCompte.getText());
			compteMail.setServeurReception(defFournisseur.getServeurPop());
			compteMail.setPortPop(defFournisseur.getPortPop());
			compteMail.setServeurSMTP(defFournisseur.getServeurSMTP());
			compteMail.setPortSMTP(defFournisseur.getPortSMTP());
			compteMail.setUserName(adresse.getText().replace("@gmail.com", ""));
			compteMail.setPassword(password.getText());
			compteMail.setTypeCompte(EnTypeCompte.GMAIL);

			if (!methodeImap.testBalIMAP(compteMail)) {
				messageUtilisateur
						.affMessageErreur("Le test de connexion a votre boite aux lettres à échoué.\r\nVeuillez vérifier voter saisie");
				return;
			} else {
				BDRequette bd = new BDRequette();
				boolean result = bd.createNewCompte(compteMail);
				if (result) {
					int idCpt = bd.getIdComptes(nomCompte.getText());
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

					result = bd.createListeDossierDeBase(compteMail,
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

				bd.closeConnexion();
			}

		}
		if (EnActionCreationComptes.ANNULER.getLib().equals(
				p_arg0.getActionCommand())) {
			// simple fermeture de la fenetre.
			fenetre.dispose();
		}

	}

	private boolean verifChamp() {
		if (adresse.getText() == null || adresse.getText().equals("") || //
				nomCompte.getText() == null || nomCompte.getText().equals("") || //
				password.getText() == null || password.getText().equals("")) {
			messageUtilisateur
					.affMessageErreur("Veuillez vérifier votre saisie");
			return false;
		}
		return true;

	}

}
