package fenetre.comptes.creation.MlActionCreation;

import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JTree;

import mdl.MlCompteMail;
import bdd.BDRequette;
import fenetre.comptes.EnDossierBase;
import fenetre.comptes.creation.CreationComptes;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionCreationComptes implements ActionListener {
	private JTextField adresse;
	private JTextField pop;
	private JTextField smtp;
	private JTextField user;
	private JTextField password;
	private JTextField nomCompte;
	private final CreationComptes fenetre;
	private JTree tree;

	/**
	 * Constructeur par defaut
	 */
	public MlActionCreationComptes(CreationComptes p_fenetre) {
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
	 */
	public MlActionCreationComptes(CreationComptes p_fenetre,
			JTextField p_nomCompte, JTextField p_adresse, JTextField p_pop,
			JTextField p_smtp, JTextField p_user, JTextField p_password,
			JTree p_treeCompte) {
		this.fenetre = p_fenetre;
		this.nomCompte = p_nomCompte;
		this.adresse = p_adresse;
		this.pop = p_pop;
		this.smtp = p_smtp;
		this.user = p_user;
		this.password = p_password;
		this.tree = p_treeCompte;
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
			compteMail.setServeurReception(pop.getText());
			compteMail.setPortPop(110);
			compteMail.setServeurSMTP(smtp.getText());
			compteMail.setPortSMTP(25);
			compteMail.setUserName(user.getText());
			compteMail.setPassword(password.getText());
			compteMail.setImap(true);
			BDRequette bd = new BDRequette();
			boolean result = bd.createNewCompte(compteMail);
			if (result) {
				int idCpt = bd.getIdComptes(nomCompte.getText());
				compteMail.setIdCompte(idCpt);
				// creation des dossiers de base (boite de reception, message
				// envoy�, corbeille, spam) avec un id Dossierparent=0
				List<String> lstDossierBase = new ArrayList<String>();
				EnDossierBase[] lstEnum = EnDossierBase.values();
				for (EnDossierBase dossier : lstEnum) {
					if (dossier != EnDossierBase.ROOT) {
						lstDossierBase.add(dossier.getLib());
					}
				}

				result = bd
						.createListeDossierDeBase(compteMail, lstDossierBase);
			}

			if (!result) {
				messageUtilisateur
						.affMessageErreur("le compte n'a pas �t� correctement enregistr�");
			} else {
				messageUtilisateur
						.affMessageInfo("Le compte � �t� cr�er correctement");
				fenetre.dispose();
				// MlActionJtree actionTree = new MlActionJtree(tree, null);
				// actionTree.valueChanged(null);

				new GestionCompte(tree);
			}

			bd.closeConnexion();

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
				pop.getText() == null || pop.getText().equals("") || //
				smtp.getText() == null || smtp.getText().equals("") || //
				user.getText() == null || user.getText().equals("") || //
				password.getText() == null || password.getText().equals("")) {
			messageUtilisateur
					.affMessageErreur("Veuillez v�rifier votre saisie");
			return false;
		}
		return true;

	}

}
