/**
 * 
 */
package bdd.accesTable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

import mdl.mlcomptemail.MlCompteMail;
import mdl.mldossier.MlBrouillon;
import mdl.mldossier.MlCorbeille;
import mdl.mldossier.MlDossier;
import mdl.mldossier.MlEnvoye;
import mdl.mldossier.MlListeDossier;
import mdl.mldossier.MlReception;
import mdl.mldossier.MlSpam;
import mdl.mlmessage.MlListeMessageGrille;
import mdl.mlmessage.MlMessageGrille;
import releve.imap.util.messageUtilisateur;
import bdd.structure.EnStructCompte;
import bdd.structure.EnStructDossier;
import bdd.structure.EnStructMailRecu;
import bdd.structure.EnTable;
import factory.RequetteFactory;

/**
 * @author smardine
 */
public class AccesTableDossier {
	private final String TAG = this.getClass().getSimpleName();
	private final RequetteFactory requeteFact;

	public AccesTableDossier() {
		requeteFact = new RequetteFactory();
	}

	/**
	 * Obtenir l'id d'un dossier a partir de son nom et d'un id de compte
	 * @param p_nomDossier
	 * @param p_idCompte
	 * @return
	 */
	public int getIdDossier(String p_nomDossier, int p_idCompte) {

		String requete = "SELECT "
				+ EnStructDossier.ID_DOSSIER.getNomChamp()// 
				+ " FROM "//
				+ EnTable.DOSSIER.getNomTable() //
				+ " WHERE ("//
				+ EnStructDossier.NOM.getNomChamp() + " ='" + p_nomDossier
				+ "' AND " + EnStructCompte.ID.getNomChamp() + "=" + p_idCompte
				+ ")";

		if ("".equals(requeteFact.get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(requeteFact.get1Champ(requete));
		}

	}

	/**
	 * Obtenir l'id d'un dossier a partir de son nom, son nom "internet" et d'un
	 * id de compte
	 * @param p_nomDossier
	 * @param p_idCompte
	 * @return
	 */
	public int getIdDossierWithFullName(String p_nomDossier, String p_fullName,
			int p_idCompte) {

		String requete = "SELECT "//
				+ EnStructDossier.ID_DOSSIER.getNomChamp() //
				+ " FROM "//
				+ EnTable.DOSSIER.getNomTable() //
				+ " WHERE ("//
				+ EnStructDossier.NOM.getNomChamp()
				+ " ='"
				+ p_nomDossier
				+ "' AND " //
				+ EnStructDossier.NOM_INTERNET.getNomChamp()
				+ "='"
				+ p_fullName //
				+ "' AND " //
				+ EnStructCompte.ID.getNomChamp() + "=" + p_idCompte + ")";

		if ("".equals(requeteFact.get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(requeteFact.get1Champ(requete));
		}

	}

	/**
	 * Obtenir une liste des sous dossier "de base"
	 * (reception,envoyé,spam,corbeille) a partir d'un id de compte
	 * @param p_idCompte
	 * @return
	 */
	public MlListeDossier getListeSousDossierBase(final MlCompteMail p_cptMail) {
		String requete = "SELECT "// 
				+ EnStructDossier.ID_DOSSIER.getNomChamp()//
				+ " FROM "// 
				+ EnTable.DOSSIER.getNomTable()// 
				+ " WHERE " //
				+ EnStructCompte.ID.getNomChamp()
				+ "="
				+ p_cptMail.getIdCompte() //
				+ " AND "//
				+ EnStructDossier.ID_DOSSIER_PARENT.getNomChamp() + "=0 "// 
				+ "ORDER BY "// 
				+ EnStructDossier.ID_DOSSIER.getNomChamp();
		List<String> lstRetour = requeteFact.getListeDeChamp(requete);
		MlListeDossier lst = new MlListeDossier();

		for (String s : lstRetour) {
			int idDossier = Integer.parseInt(s);
			if (idDossier == p_cptMail.getIdInbox()) {
				lst.add(new MlReception(p_cptMail));
			} else if (idDossier == p_cptMail.getIdEnvoye()) {
				lst.add(new MlEnvoye(p_cptMail));
			} else if (idDossier == p_cptMail.getIdSpam()) {
				lst.add(new MlSpam(p_cptMail));
			} else if (idDossier == p_cptMail.getIdCorbeille()) {
				lst.add(new MlCorbeille(p_cptMail));
			} else if (idDossier == p_cptMail.getIdBrouillons()) {
				lst.add(new MlBrouillon(p_cptMail));
			}
			// lst.add(new MlDossier(Integer.parseInt(s)));
		}
		return lst;

	}

	/**
	 * obtenir le nombre de sous dossier de base pour un compte
	 * @param p_idCompte
	 * @return
	 */
	public int getnbSousDossierBase(MlCompteMail p_cptMail) {
		return getListeSousDossierBase(p_cptMail).size();

	}

	/**
	 * obtenir la liste des sous dossier a partir d'un id d'un dossier racine
	 * @param p_idDossier
	 * @return
	 */
	public MlListeDossier getListeSousDossier(int p_idDossier) {
		String requete = "SELECT " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructDossier.ID_DOSSIER_PARENT.getNomChamp() + "="
				+ p_idDossier + " ORDER BY "
				+ EnStructDossier.NOM.getNomChamp();
		List<String> lstRetour = requeteFact.getListeDeChamp(requete);
		MlListeDossier lst = new MlListeDossier();
		for (String s : lstRetour) {
			lst.add(new MlDossier(Integer.parseInt(s)));
		}
		return lst;

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
		String requette = "INSERT " + "INTO " + EnTable.DOSSIER.getNomTable()
				+ " (" + EnStructDossier.ID_COMPTE.getNomChamp() + ", "
				+ EnStructDossier.ID_DOSSIER_PARENT.getNomChamp() + ", "
				+ EnStructDossier.NOM.getNomChamp() + ", "// 
				+ EnStructDossier.NOM_INTERNET.getNomChamp() + ") "// 
				+ " VALUES (" //
				+ p_idCompte + //
				"," + p_idDossierParent + // 
				",'" + p_nomNewDossier + // 
				"','" + p_nomDossierInternet + "')";

		return requeteFact.executeRequete(requette);

	}

	/**
	 * Effacer un dossier en base de facon recursive (tout les enfants sont
	 * egalement effacé)
	 * @param p_idCompte
	 * @param p_idDossier
	 * @param p_progressBar
	 * @return
	 */
	public boolean deleteDossier(int p_idCompte, int p_idDossier,
			JProgressBar p_progressBar) {

		MlListeDossier lstSousDossier = getListeSousDossier(p_idDossier);
		for (MlDossier dossier : lstSousDossier) {
			deleteDossier(p_idCompte, dossier.getIdDossier(), p_progressBar);
		}
		String requette = "DELETE FROM " + EnTable.DOSSIER.getNomTable()
				+ " WHERE " + EnStructDossier.ID_COMPTE.getNomChamp() + "="
				+ p_idCompte + " AND "
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + "=" + p_idDossier;
		int count = 0;

		AccesTableMailRecu accesMail = new AccesTableMailRecu();
		MlListeMessageGrille listeMessage = accesMail.getListeMessageGrille(
				p_idCompte, p_idDossier);
		int tailleListe = listeMessage.size();
		// pour chaque dossier, on supprime la liste des messages associés
		for (MlMessageGrille unMessage : listeMessage) {
			if (null != p_progressBar) {
				p_progressBar.setString("Suppression du message " + (count + 1)
						+ " sur " + tailleListe);
				int pourcent = ((count + 1) * 100) / tailleListe;
				p_progressBar.setValue(pourcent);
				count++;
			}

			if (!accesMail.deleteMessageRecu(unMessage.getIdMessage())) {
				return false;
			}
		}

		return requeteFact.executeRequete(requette);

	}

	/**
	 * Obtenir la liste de TOUT les dossier d'un compte
	 * @param p_idCompte
	 * @return
	 */
	public MlListeDossier getListeToutLesDossier(int p_idCompte) {
		String requette = "SELECT " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructDossier.ID_COMPTE.getNomChamp() + "=" + p_idCompte
				+ " ORDER BY " + EnStructDossier.NOM.getNomChamp();
		List<String> lstRetour = requeteFact.getListeDeChamp(requette);
		MlListeDossier lst = new MlListeDossier();
		for (String s : lstRetour) {
			lst.add(new MlDossier(Integer.parseInt(s)));
		}
		return lst;

	}

	/**
	 * Obtenir le nom d'un dossier a partir de son ID
	 * @param p_idDossierStockage
	 * @return
	 */
	public String getNomDossier(int p_idDossierStockage) {
		String script = "SELECT " + EnStructDossier.NOM.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable() + " WHERE "
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossierStockage;
		return requeteFact.get1Champ(script);

	}

	/**
	 * Obtenir le "nom internet" d'un dossier a partir de son id (utilisé pour
	 * les releve de compte type GMAIL)
	 * @param p_idDossierStockage
	 * @return
	 */
	public String getNomInternetDossier(int p_idDossierStockage) {
		String script = "SELECT " + EnStructDossier.NOM_INTERNET.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable() + " WHERE "
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossierStockage;
		return requeteFact.get1Champ(script);

	}

	/**
	 * Obtenir la structure d'un MlDossier a partir de son ID
	 * @param p_idDOssier
	 * @return
	 */
	public List<String> getDossierByID(int p_idDOssier) {
		String script = "Select " + EnStructDossier.ID_COMPTE.getNomChamp()
				+ ", "// 
				+ EnStructDossier.ID_DOSSIER_PARENT.getNomChamp() + ", "//
				+ EnStructDossier.NOM.getNomChamp() + ", "//
				+ EnStructDossier.NOM_INTERNET.getNomChamp() + " From "//
				+ EnTable.DOSSIER.getNomTable() + " where "//
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + "=" + p_idDOssier;
		List<ArrayList<String>> lstResultat = requeteFact
				.getListeDenregistrement(script);

		return lstResultat.get(0);

	}

	/**
	 * Creer la liste des dossier de base d'un MlCompteMail
	 * @param p_compte
	 * @param p_lstDossierBase
	 * @return
	 */
	public boolean createListeDossierDeBase(MlCompteMail p_compte,
			List<String> p_lstDossierBase) {
		for (String nomDossier : p_lstDossierBase) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO " + EnTable.DOSSIER.getNomTable() + " ("
					+ EnStructDossier.ID_COMPTE.getNomChamp() + ", "
					+ EnStructDossier.ID_DOSSIER_PARENT.getNomChamp() + ", "
					+ EnStructDossier.NOM.getNomChamp() + ") VALUES (");
			sb.append(p_compte.getIdCompte() + ",");
			sb.append(0 + ",");
			sb.append("'" + nomDossier + "')");
			if (!requeteFact.executeRequete(sb.toString())) {
				messageUtilisateur.affMessageErreur(TAG,
						"Erreur a la creation du dossier " + nomDossier
								+ " pour le compte " + p_compte.getNomCompte());
				return false;
			}

		}
		return true;
	}

	/**
	 * Mettre a jour le "nom internet" d'un dossier
	 * @param p_idDossier
	 * @param p_nomDossierInternet
	 * @param p_idDossierParent
	 * @return
	 */
	public boolean updateNomDossierInternet(int p_idDossier,
			String p_nomDossierInternet, int p_idDossierParent) {
		String requete = "UPDATE " + EnTable.DOSSIER.getNomTable() + " SET "
				+ EnStructDossier.NOM_INTERNET.getNomChamp() + "='"
				+ p_nomDossierInternet.trim() + "', "
				+ EnStructDossier.ID_DOSSIER_PARENT.getNomChamp() + "="
				+ p_idDossierParent + " where "
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + "=" + p_idDossier;
		return requeteFact.executeRequete(requete);

	}

	/**
	 * Obtenir le nombre de message non lu pour un dossier
	 * @param p_idCompte
	 * @param p_idDossier
	 * @return
	 */
	public int getUnreadMessageFromFolder(int p_idCompte, int p_idDossier) {
		String requette = "SELECT count (*) FROM "
				+ EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_COMPTE.getNomChamp() + "=" + p_idCompte
				+ " and " + EnStructMailRecu.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossier + " and " + EnStructMailRecu.STATUT.getNomChamp()
				+ "='F'";
		return Integer.parseInt(requeteFact.get1Champ(requette));
	}

}
