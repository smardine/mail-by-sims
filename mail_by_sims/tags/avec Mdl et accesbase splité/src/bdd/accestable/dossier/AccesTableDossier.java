/**
 * 
 */
package bdd.accestable.dossier;

import imap.util.messageUtilisateur;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import mdl.MlCompteMail;
import bdd.BDAcces;
import bdd.RequetteFactory;
import bdd.accestable.AccesTable;
import bdd.accestable.EnTable;
import bdd.accestable.compte.EnStructTableCompte;

/**
 * @author smardine
 */
public class AccesTableDossier extends AccesTable {

	private final Connection laConnexion;
	private final BDAcces bd;
	private final RequetteFactory reqFactory;

	public AccesTableDossier() {
		connect();
		bd = AccesTable.bd;
		laConnexion = bd.getConnexion();
		reqFactory = new RequetteFactory(laConnexion);
	}

	/**
	 * Obtenir l'id d'un dossier a partir de son nom et d'un id de compte
	 * @param p_nomDossier
	 * @param p_idCompte
	 * @return
	 */
	public int getIdDossier(String p_nomDossier, int p_idCompte) {

		String requete = "Select " + EnStructTableDossier.ID.getNomChamp()
				+ " from " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructTableDossier.NOM_DOSSIER.getNomChamp() + " ='"
				+ p_nomDossier + "' AND "
				+ EnStructTableCompte.ID.getNomChamp() + "='" + p_idCompte
				+ "'";

		if ("".equals(reqFactory.get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(reqFactory.get1Champ(requete));
		}

	}

	/**
	 * Obtenir une liste des sous dossier "de base"
	 * (reception,envoyé,spam,corbeille) a partir d'un id de compte
	 * @param p_idCompte
	 * @return
	 */
	public ArrayList<String> getListeSousDossierBase(int p_idCompte) {
		String requete = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_COMPTE='"
				+ p_idCompte
				+ "' and a.ID_DOSSIER_PARENT=0 ORDER BY a.NOM_DOSSIER";
		return reqFactory.getListeDeChamp(requete);

	}

	/**
	 * obtenir le nombre de sous dossier de base pour un compte
	 * @param p_idCompte
	 * @return
	 */
	public int getnbSousDossierBase(int p_idCompte) {
		return getListeSousDossierBase(p_idCompte).size();

	}

	/**
	 * obtenir la liste des sous dossier a partir du nom d'un dossier racine
	 * @param p_idDossier
	 * @return
	 */
	public ArrayList<String> getListeSousDossier(int p_idDossier) {
		String requete = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_DOSSIER_PARENT='"
				+ p_idDossier + "' ORDER BY a.NOM_DOSSIER";
		return reqFactory.getListeDeChamp(requete);

	}

	/**
	 * Obtenir le nombre de sous dossier a partir du nom d'un dossier racine
	 * @param p_idDossierracine
	 * @return
	 */
	public int getnbSousDossier(int p_idDossierracine) {
		return getListeSousDossier(p_idDossierracine).size();
	}

	/**
	 * Créer un nouveau dossier en base
	 * @param p_idCompte - String - l'id de compte
	 * @param p_idDossierParent - String - l'id du dossier parent
	 * @param p_nomNewDossier - String le nom du nouveau dossier
	 * @param p_nomDossierInternet
	 * @return true ou false
	 */
	public boolean createNewDossier(int p_idCompte, int p_idDossierParent,
			String p_nomNewDossier, String p_nomDossierInternet) {
		String requette = "INSERT "
				+ "INTO DOSSIER (ID_COMPTE,  ID_DOSSIER_PARENT, NOM_DOSSIER, NOM_INTERNET) "
				+ " VALUES (" //
				+ "'" + p_idCompte + //
				"','" + p_idDossierParent + // 
				"','" + p_nomNewDossier + // 
				"','" + p_nomDossierInternet + "')";

		return reqFactory.executeRequete(requette);

	}

	/**
	 * Effacer un dossier en base de facon recursive (tout les enfants sont
	 * egalement effacé)
	 * @param p_idCompte
	 * @param p_idDossier
	 * @return
	 */
	public boolean deleteDossier(int p_idCompte, int p_idDossier) {

		ArrayList<String> lstSousDossier = getListeSousDossier(p_idDossier);
		for (String dossier : lstSousDossier) {
			deleteDossier(p_idCompte, getIdDossier(dossier, p_idCompte));

		}
		String requette = "DELETE FROM DOSSIER WHERE ID_COMPTE='" + p_idCompte
				+ "' AND ID_DOSSIER='" + p_idDossier + "'";
		String requetteBis = "DELETE FROM MAIL_RECU WHERE ID_COMPTE='"
				+ p_idCompte + "' AND ID_DOSSIER_STOCKAGE='" + p_idDossier
				+ "'";
		if (reqFactory.executeRequete(requette)) {
			// si on a reussi a supprimer le dossier en base,
			// on suppr tt les mails enregistés
			return reqFactory.executeRequete(requetteBis);
		}
		return false;

	}

	public ArrayList<String> getListeDossier(int p_idComptes) {
		String requette = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_COMPTE='"
				+ p_idComptes + "' ORDER BY a.NOM_DOSSIER";
		return reqFactory.getListeDeChamp(requette);

	}

	public String getNomDossier(int p_idDossierStockage) {
		String script = "SELECT a.NOM_DOSSIER FROM DOSSIER a WHERE a.ID_DOSSIER="
				+ p_idDossierStockage;
		return reqFactory.get1Champ(script);

	}

	public String getNomDossierInternet(int p_idDossierStockage) {

		String script = "SELECT a.NOM_INTERNET from DOSSIER a where a.ID_DOSSIER="
				+ p_idDossierStockage;
		return reqFactory.get1Champ(script);

	}

	public boolean createListeDossierDeBase(MlCompteMail p_compte,
			List<String> p_lstDossierBase) {
		for (String nomDossier : p_lstDossierBase) {
			StringBuilder sb = new StringBuilder();
			sb
					.append("INSERT INTO DOSSIER (ID_COMPTE, ID_DOSSIER_PARENT, NOM_DOSSIER) VALUES (");
			sb.append("'" + p_compte.getIdCompte() + "',");
			sb.append(0 + ",");
			sb.append("'" + nomDossier + "')");
			if (!reqFactory.executeRequete(sb.toString())) {
				messageUtilisateur
						.affMessageErreur("Erreur a la creation du dossier "
								+ nomDossier + " pour le compte "
								+ p_compte.getNomCompte());
				return false;
			}

		}
		return true;
	}

	public void majNomDossierInternet(int p_idDossier, String p_fullName) {
		String script = "UPDATE DOSSIER a set a.NOM_INTERNET='" + p_fullName
				+ "' where a.ID_DOSSIER=" + p_idDossier;
		reqFactory.executeRequete(script);

	}

}
