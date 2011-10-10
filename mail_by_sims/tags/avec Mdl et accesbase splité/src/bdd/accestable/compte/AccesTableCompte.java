package bdd.accestable.compte;

import java.sql.Connection;
import java.util.ArrayList;

import mdl.MlCompteMail;
import bdd.BDAcces;
import bdd.RequetteFactory;
import bdd.accestable.AccesTable;
import bdd.accestable.EnTable;
import bdd.accestable.dossier.AccesTableDossier;

public class AccesTableCompte extends AccesTable {
	private final String TAG = this.getClass().getSimpleName();

	private final BDAcces bd;
	private final Connection laConnexion;
	private final RequetteFactory reqFactory;
	private final AccesTableDossier accesDossier;

	public AccesTableCompte() {
		connect();
		bd = AccesTable.bd;
		laConnexion = bd.getConnexion();
		reqFactory = new RequetteFactory(laConnexion);
		accesDossier = new AccesTableDossier();
	}

	/**
	 * Obtenir la liste des compte mails enregistrés en base
	 * @return
	 */
	public ArrayList<MlCompteMail> getListeDeComptes() {
		String requete = "Select " + EnStructTableCompte.ID.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable();
		ArrayList<String> lst = reqFactory.getListeDeChamp(requete);
		ArrayList<MlCompteMail> listeCompte = new ArrayList<MlCompteMail>(lst
				.size());
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

		String requete = "Select " + EnStructTableCompte.ID.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable() + " where "
				+ EnStructTableCompte.NOM.getNomChamp() + " ='" + p_nomCompte
				+ "'";
		if ("".equals(reqFactory.get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(reqFactory.get1Champ(requete));
		}

	}

	public ArrayList<String> getCompteByID(int p_idCompte) {
		String script = "SELECT a.NOM_COMPTE," + " a.SERVEUR_POP,"
				+ " a.PORT_POP," + " a.SERVEUR_SMTP," + " a.PORT_SMTP,"
				+ " a.USERNAME," + " a.PWD," + " a.TYPE_COMPTE"
				+ " FROM COMPTES" + " a where a.ID_COMPTE=" + p_idCompte;

		ArrayList<ArrayList<String>> lstResultat = reqFactory
				.getListeDenregistrement(script);

		return lstResultat.get(0);

	}

	public boolean deleteCompte(int p_idCompte) {
		ArrayList<String> lsiteDossier = accesDossier
				.getListeDossier(p_idCompte);
		for (String unDossier : lsiteDossier) {
			accesDossier.deleteDossier(p_idCompte, accesDossier.getIdDossier(
					unDossier, p_idCompte));
		}
		String requete = "DELETE FROM COMPTES WHERE ID_COMPTE=" + p_idCompte;
		return reqFactory.executeRequete(requete);

	}

	public boolean createNewCompte(MlCompteMail p_compte) {
		StringBuilder sb = new StringBuilder();
		sb
				.append("Insert into "
						+ EnTable.COMPTES.getNomTable()
						+ " (NOM_COMPTE, SERVEUR_POP, PORT_POP, SERVEUR_SMTP, PORT_SMTP, USERNAME, PWD,TYPE_COMPTE)");
		sb.append(" Values ");
		sb.append("( '" + p_compte.getNomCompte() + "',");
		sb.append("'" + p_compte.getServeurReception() + "',");
		sb.append("'" + p_compte.getPortPop() + "',");
		sb.append("'" + p_compte.getServeurSMTP() + "',");
		sb.append("'" + p_compte.getPortSMTP() + "',");
		sb.append("'" + p_compte.getUserName() + "',");
		sb.append("'" + p_compte.getPassword() + "',");
		sb.append("'" + p_compte.getTypeCompte().getLib() + "')");

		return reqFactory.executeRequete(sb.toString());
	}

	/**
	 *Obtenir le nombre de compte enregistree dans la base
	 * @return
	 */
	public int getNbCompte() {
		return getListeDeComptes().size();

	}

}
