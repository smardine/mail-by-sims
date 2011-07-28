package fenetre.comptes.creation.MlActionCreation;

import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import bdd.BDRequette;
import bdd.EnTable;
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
			JTextField p_smtp, JTextField p_user, JTextField p_password) {
		this.fenetre = p_fenetre;
		this.nomCompte = p_nomCompte;
		this.adresse = p_adresse;
		this.pop = p_pop;
		this.smtp = p_smtp;
		this.user = p_user;
		this.password = p_password;
	}

	@Override
	public void actionPerformed(ActionEvent p_arg0) {
		if (EnActionCreationComptes.VALIDER.getLib().equals(
				p_arg0.getActionCommand())) {
			// verification des information saisie par l'utilisateur
			if (!verifChamp()) {
				return;
			}
			// enregistrement du compte dans la base
			StringBuilder sb = new StringBuilder();
			sb
					.append("Insert into "
							+ EnTable.COMPTES.getNomTable()
							+ " (NOM_COMPTE, SERVEUR_POP, PORT_POP, SERVEUR_SMTP, PORT_SMTP, USERNAME, PWD,TYPE_COMPTE)");
			sb.append(" Values ");
			sb.append("( '" + nomCompte.getText() + "',");
			sb.append("'" + pop.getText() + "',110,");
			sb.append("'" + smtp.getText() + "',25,");
			sb.append("'" + user.getText() + "',");
			sb.append("'" + password.getText() + "',");
			sb.append("'imap')");

			boolean result = BDRequette.executeRequete(sb.toString());
			// creation des dossiers de base (boite de reception, message
			// envoyé, corbeille, spam) avec un id Dossierparent=0
			int idCpt = BDRequette.getIdComptes(nomCompte.getText());

			List<String> lstDossierBase = new ArrayList<String>();

			EnDossierBase[] lstEnum = EnDossierBase.values();
			for (EnDossierBase dossier : lstEnum) {
				if (dossier != EnDossierBase.ROOT) {
					lstDossierBase.add(dossier.getLib());
				}

			}

			for (String nomDossier : lstDossierBase) {
				sb = new StringBuilder();
				sb
						.append("INSERT INTO DOSSIER (ID_COMPTE, ID_DOSSIER_PARENT, NOM_DOSSIER) VALUES (");
				sb.append("'" + idCpt + "',");
				sb.append(0 + ",");
				sb.append("'" + nomDossier + "')");
				if (!BDRequette.executeRequete(sb.toString())) {
					messageUtilisateur
							.affMessageErreur("Erreur a la creation du dossier "
									+ nomDossier
									+ " pour le compte "
									+ nomCompte.getText());
				}

			}

			if (!result) {
				messageUtilisateur
						.affMessageErreur("le compte n'a pas été correctement enregistré");
			} else {
				messageUtilisateur
						.affMessageInfo("Le compte à été créer correctement");
				fenetre.dispose();
				new GestionCompte();
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
				pop.getText() == null || pop.getText().equals("") || //
				smtp.getText() == null || smtp.getText().equals("") || //
				user.getText() == null || user.getText().equals("") || //
				password.getText() == null || password.getText().equals("")) {
			messageUtilisateur
					.affMessageErreur("Veuillez verifier votre saisie");
			return false;
		}
		return true;

	}

}
