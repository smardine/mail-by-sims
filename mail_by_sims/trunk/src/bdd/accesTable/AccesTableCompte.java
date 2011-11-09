/**
 * 
 */
package bdd.accesTable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import mdl.MlListeDossier;
import bdd.structure.EnStructCompte;
import bdd.structure.EnStructDossier;
import bdd.structure.EnStructMailRecu;
import bdd.structure.EnTable;
import exception.DonneeAbsenteException;
import factory.RequetteFactory;
import fenetre.comptes.EnDossierBase;

/**
 * @author smardine
 */
public class AccesTableCompte {
	private final String TAG = this.getClass().getSimpleName();
	private final RequetteFactory requeteFact;

	public AccesTableCompte() {
		requeteFact = new RequetteFactory();
	}

	/**
	 * Obtenir la liste des compte mails enregistrés en base
	 * @return la liste
	 */
	public MlListeCompteMail getListeDeComptes() {

		String requete = "SELECT " + EnStructCompte.ID.getNomChamp() + " FROM "
				+ EnTable.COMPTES.getNomTable();
		List<String> lst = requeteFact.getListeDeChamp(requete);
		MlListeCompteMail listeCompte = new MlListeCompteMail();
		for (String s : lst) {
			MlCompteMail cpt = new MlCompteMail(Integer.parseInt(s));
			listeCompte.add(cpt);
		}
		return listeCompte;

	}

	/**
	 * Obtenir l'id d'un compte a partir de son nom
	 * @param p_nomCompte
	 * @return
	 */
	public int getIdComptes(String p_nomCompte) {

		String requete = "SELECT " + EnStructCompte.ID.getNomChamp() + " FROM "
				+ EnTable.COMPTES.getNomTable() + " WHERE "
				+ EnStructCompte.NOM.getNomChamp() + " ='" + p_nomCompte + "'";
		String result = requeteFact.get1Champ(requete);
		if ("".equals(result)) {
			return -1;
		} else {
			return Integer.parseInt(result);
		}

	}

	/**
	 * Obtenir la structure d'un compte a partir de son id
	 * @param p_idCompte
	 * @return
	 */
	public List<String> getCompteByID(int p_idCompte) {
		String script = "SELECT " + EnStructCompte.NOM.getNomChamp() + ", "
				+ EnStructCompte.POP_SERVEUR.getNomChamp() + ", "
				+ EnStructCompte.POP_PORT.getNomChamp() + ", "
				+ EnStructCompte.SMTP_SERVEUR.getNomChamp() + ", "
				+ EnStructCompte.SMTP_PORT.getNomChamp() + ", "
				+ EnStructCompte.USER.getNomChamp() + ", "
				+ EnStructCompte.PWD.getNomChamp() + ", "
				+ EnStructCompte.TYPE.getNomChamp() + " FROM "
				+ EnTable.COMPTES.getNomTable() + " where "
				+ EnStructCompte.ID.getNomChamp() + "=" + p_idCompte;

		List<ArrayList<String>> lstResultat = requeteFact
				.getListeDenregistrement(script);

		return lstResultat.get(0);

	}

	/**
	 * Cette fonction parcours tout les dossier enregistré du compte mail 1 par
	 * 1 pour supprimer en cascade les messages et PJ associées.
	 * @param p_idCompte
	 * @param p_progressBar
	 * @param p_label
	 * @return resultat
	 * @throws DonneeAbsenteException
	 */
	public boolean deleteCompte(int p_idCompte, JLabel p_label,
			JProgressBar p_progressBar) throws DonneeAbsenteException {
		if (p_idCompte == 0) {
			throw new DonneeAbsenteException(TAG,
					"le compte à supprimé n'existe pas en base");
		}
		AccesTableDossier accesDossier = new AccesTableDossier();
		MlListeDossier listeDossier = accesDossier
				.getListeToutLesDossier(p_idCompte);
		for (int i = 0; i < listeDossier.size(); i++) {
			if (null != p_label) {
				p_label
						.setText("Suppression du dossier "
								+ listeDossier.get(i));
			}
			if (null != p_progressBar) {
				int pourcent = ((i + 1) * 100) / listeDossier.size();
				p_progressBar.setValue(pourcent);
				p_progressBar.setString(pourcent + " %");
			}
			accesDossier.deleteDossier(p_idCompte, listeDossier.get(i)
					.getIdDossier(), p_progressBar);
		}

		String requete = "DELETE FROM " + EnTable.COMPTES.getNomTable()
				+ " WHERE " + EnStructCompte.ID.getNomChamp() + "="
				+ p_idCompte;
		return requeteFact.executeRequete(requete);

	}

	/**
	 * Creation d'un MlCompteMail en base
	 * @param p_compte
	 * @return
	 */
	public boolean createNewCompte(MlCompteMail p_compte) {
		StringBuilder sb = new StringBuilder();
		sb.append("Insert into " + EnTable.COMPTES.getNomTable() + " ("
				+ EnStructCompte.NOM.getNomChamp() + ", "
				+ EnStructCompte.POP_SERVEUR.getNomChamp() + ", "
				+ EnStructCompte.POP_PORT.getNomChamp() + ", "
				+ EnStructCompte.SMTP_SERVEUR.getNomChamp() + ", "
				+ EnStructCompte.SMTP_PORT.getNomChamp() + ", "
				+ EnStructCompte.USER.getNomChamp() + ", "
				+ EnStructCompte.PWD.getNomChamp() + ", "
				+ EnStructCompte.TYPE.getNomChamp() + ")");
		sb.append(" Values ");
		sb.append("( '" + p_compte.getNomCompte() + "',");
		sb.append("'" + p_compte.getServeurReception() + "',");
		sb.append("'" + p_compte.getPortPop() + "',");
		sb.append("'" + p_compte.getServeurSMTP() + "',");
		sb.append("'" + p_compte.getPortSMTP() + "',");
		sb.append("'" + p_compte.getUserName() + "',");
		sb.append("'" + p_compte.getPassword() + "',");
		sb.append("'" + p_compte.getTypeCompte().getLib() + "')");

		return requeteFact.executeRequete(sb.toString());
	}

	/**
	 * obtenir l'id de la INBOX du MlCompteMail
	 * @param p_idCpt
	 * @return
	 */
	public int getIdInbox(int p_idCpt) {
		String requette = "SELECT " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable() + " WHERE "
				+ EnStructDossier.NOM.getNomChamp() + "='"
				+ EnDossierBase.RECEPTION.getLib() + "' AND "
				+ EnStructDossier.ID_COMPTE.getNomChamp() + "=" + p_idCpt;
		return Integer.parseInt(requeteFact.get1Champ(requette));

	}

	/**
	 * obtenir l'id du dossier brouillon du MlCompteMail
	 * @param p_idCpt
	 * @return
	 */
	public int getIdBrouillon(int p_idCpt) {
		String requette = "select " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " from " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructDossier.NOM.getNomChamp() + "='"
				+ EnDossierBase.BROUILLON.getLib() + "' and "
				+ EnStructDossier.ID_COMPTE.getNomChamp() + "=" + p_idCpt;
		return Integer.parseInt(requeteFact.get1Champ(requette));
	}

	/**
	 * obtenir l'id de la corbeille du MLCompteMail
	 * @param p_idCpt
	 * @return
	 */
	public int getIdCorbeille(int p_idCpt) {
		String requette = "select " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " from " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructDossier.NOM.getNomChamp() + "='"
				+ EnDossierBase.CORBEILLE.getLib() + "' and "
				+ EnStructDossier.ID_COMPTE.getNomChamp() + "=" + p_idCpt;
		return Integer.parseInt(requeteFact.get1Champ(requette));
	}

	/**
	 * obtenir l'id du dossier "Envoye" du MlCompteMail
	 * @param p_idCpt
	 * @return
	 */
	public int getIdEnvoye(int p_idCpt) {
		String requette = "select " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " from " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructDossier.NOM.getNomChamp() + "='"
				+ EnDossierBase.ENVOYES.getLib() + "' and "
				+ EnStructDossier.ID_COMPTE.getNomChamp() + "=" + p_idCpt;
		return Integer.parseInt(requeteFact.get1Champ(requette));
	}

	/**
	 * obtenir l'id du dossier SPAM du MlCompteMail
	 * @param p_idCpt
	 * @return
	 */
	public int getIdSpam(int p_idCpt) {
		String requette = "Select " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " from " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructDossier.NOM.getNomChamp() + "='"
				+ EnDossierBase.SPAM.getLib() + "' and "
				+ EnStructDossier.ID_COMPTE.getNomChamp() + "=" + p_idCpt;
		return Integer.parseInt(requeteFact.get1Champ(requette));
	}

	/**
	 * Obtenir le nombre de messages non lu pour un compte mail
	 * @param p_idCompte
	 * @return
	 */
	public int getUnreadMessageFromCompte(int p_idCompte) {
		String requette = "SELECT count (*) FROM "
				+ EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_COMPTE.getNomChamp() + "=" + p_idCompte
				+ " and " + EnStructMailRecu.STATUT.getNomChamp() + "='F'";
		return Integer.parseInt(requeteFact.get1Champ(requette));
	}

}
