package fenetre.comptes.creation.MlActionCreation;

import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JTree;

import mdl.MlCompteMail;
import pop.util.methodePop;
import bdd.accestable.compte.AccesTableCompte;
import bdd.accestable.dossier.AccesTableDossier;
import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.EnDossierBase;
import fenetre.comptes.creation.CreationComptesPop;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionCreationComptesPop implements ActionListener {
	private JTextField adresse;
	private JTextField pop;
	private JTextField smtp;
	private JTextField user;
	private JTextField password;
	private JTextField nomCompte;
	private final CreationComptesPop fenetre;

	private JTree tree;
	private EnDefFournisseur defFournisseur;

	/**
	 * Constructeur par defaut
	 */
	public MlActionCreationComptesPop(CreationComptesPop p_fenetre) {
		this.fenetre = p_fenetre;
	}

	/**
	 * Constructeur qui permet de recuperer les infos en provenance de la
	 * fenetre
	 * @param p_adresse
	 * @param p_pop
	 * @param p_smtp
	 * @param p_user
	 * @param p_password
	 * @param p_tree
	 * @param p_defFournisseur
	 */
	public MlActionCreationComptesPop(CreationComptesPop p_fenetre,
			JTextField p_nomCompte, JTextField p_adresse, JTextField p_pop,
			JTextField p_smtp, JTextField p_user, JTextField p_password,
			JTree p_tree, EnDefFournisseur p_defFournisseur) {
		this.fenetre = p_fenetre;
		this.nomCompte = p_nomCompte;
		this.adresse = p_adresse;
		this.pop = p_pop;
		this.smtp = p_smtp;
		this.user = p_user;
		this.password = p_password;
		this.tree = p_tree;
		this.defFournisseur = p_defFournisseur;

	}

	@Override
	public void actionPerformed(ActionEvent p_arg0) {
		if (EnActionCreationComptes.VALIDER.getLib().equals(
				p_arg0.getActionCommand())) {
			// verification des information saisie par l'utilisateur
			if (!verifChamp()) {
				return;
			}

			MlCompteMail compteMail = new MlCompteMail("");
			compteMail.setNomCompte(nomCompte.getText());
			compteMail.setServeurReception(pop.getText());
			compteMail.setPortPop(110);
			compteMail.setServeurSMTP(smtp.getText());
			compteMail.setPortSMTP(25);
			compteMail.setUserName(user.getText());
			compteMail.setPassword(password.getText());
			compteMail.setTypeCompte(defFournisseur.getTypeCompte());

			boolean resultatTestBal = methodePop.testBalPop(compteMail);
			if (!resultatTestBal) {
				messageUtilisateur
						.affMessageErreur("Le test de connexion a votre boite aux lettres à échoué.\r\nVeuillez vérifier voter saisie");
				return;
			}
			if (resultatTestBal) {
				AccesTableCompte accesTableCompte = new AccesTableCompte();
				boolean result = accesTableCompte.createNewCompte(compteMail);
				if (result) {
					int idCpt = accesTableCompte.getIdComptes(nomCompte
							.getText());
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
					result = new AccesTableDossier().createListeDossierDeBase(
							compteMail, lstDossierBase);
				}
				if (!result) {
					messageUtilisateur
							.affMessageErreur("le compte n'a pas été correctement enregistré");
				} else {
					messageUtilisateur
							.affMessageInfo("Le compte à été créer correctement");
					fenetre.dispose();

					new GestionCompte(tree);
				}

				// bd.closeConnexion();
			}

		}
		if (EnActionCreationComptes.ANNULER.getLib().equals(
				p_arg0.getActionCommand())) {
			// simple fermeture de la fenetre.
			fenetre.dispose();
		}

	}

	private boolean verifChamp() {
		if (utilityCreation.verifAdresseEtNom(adresse, nomCompte)
				|| utilityCreation.verifPopSmtp(pop, smtp)
				|| utilityCreation.verifUserPassword(user, password)) {
			messageUtilisateur
					.affMessageErreur("Veuillez vérifier votre saisie");
			return false;
		}
		return true;

	}

}
