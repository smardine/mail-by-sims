package bdd;

import imap.util.messageUtilisateur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

import mdl.MlListeMessage;
import mdl.MlMessage;
import tools.GestionRepertoire;
import tools.Historique;
import tools.RecupDate;
import tools.WriteFile;

/**
 * cette classe s'occupe uniquement des requetes
 * @author sims
 */
public class BDRequette {

	private static Connection laConnexion = BDAcces.connexion;

	/**
	 * Constructeur priv� car c'est une classe utilitaire (que des methode
	 * static)
	 */
	private BDRequette() {

	}

	/**
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectu�e (delete, truncate...)
	 * @return vrai si ca a march�, sinon faux
	 */
	public static boolean executeRequete(final String requete) {

		Statement state = null;
		try {
			state = laConnexion.createStatement();
			state.execute(requete);
			laConnexion.commit();
		} catch (final SQLException e) {
			messageUtilisateur.affMessageException(e,
					"erreur a l'execution d'une requete");
			Historique.ecrire("Message d'erreur: " + e
					+ "\n\r sur la requete : " + requete);

			return false;
		} finally {
			try {
				state.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"erreur a l'execution d'une requete");
				Historique.ecrire("Message d'erreur: " + e
						+ "\n\r sur la requete : " + requete);

				return false;
			}
		}
		return true;
	}

	/**
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectu�e (delete, truncate...)
	 * @param p_fDestinataires
	 * @return vrai si ca a march�, sinon faux
	 */
	public static boolean executeRequeteWithBlob(String requete,
			File p_fContenu, File p_fDestinataires) {

		PreparedStatement ps = null;
		FileInputStream inputContenu = null;
		FileInputStream inputDestinataire = null;
		try {
			ps = laConnexion.prepareStatement(requete);

			inputDestinataire = new FileInputStream(p_fDestinataires);
			inputContenu = new FileInputStream(p_fContenu);
			ps.setBinaryStream(1, inputDestinataire, (int) p_fDestinataires
					.length());
			ps.setBinaryStream(2, inputContenu, (int) p_fContenu.length());

			ps.executeUpdate();
			laConnexion.commit();
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur � l'insertion d'un blob");
			return false;
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"impossible de trouver le fichier");
			return false;
		} finally {
			try {
				ps.close();
				inputContenu.close();
				inputDestinataire.close();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Erreur � l'insertion d'un blob");
			} catch (IOException e) {
				messageUtilisateur.affMessageException(e, "fichier non trouv�");

			}
		}
		return true;
	}

	/**
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectu�e (delete, truncate...)
	 * @return vrai si ca a march�, sinon faux
	 */
	public static boolean executeMiseAJour(final String requete) {
		String[] tab = requete.split(";");

		Statement state = null;
		try {
			state = laConnexion.createStatement();
			for (String s : tab) {
				state.addBatch(s);
			}
			state.executeBatch();
			laConnexion.commit();
		} catch (final SQLException e) {
			System.out.println(e);
			Historique.ecrire("Message d'erreur: " + e
					+ "\n\r sur la requete : " + requete);
			return false;
		} finally {
			try {
				state.close();
			} catch (SQLException e) {
				Historique.ecrire("Message d'erreur: " + e
						+ "\n\r sur la requete : " + requete);
			}
		}
		return true;
	}

	/**
	 * Obtenir le nombre d'enregistrement d'une table
	 * @param p_table - {@link EnTable}
	 * @param p_champ - {@link EnStructureTable} - le nom d'un champ de la table
	 * @return le nombre de ligne dans cette table
	 */
	public static int getNbEnregistrementFromTable(EnTable p_table,
			EnStructureTable p_champ) {

		int nbRecords = 0;
		String requete = "Select " + p_champ.getNomChamp() + " from "
				+ p_table.getNomTable();

		Statement state;
		try {
			state = laConnexion.createStatement();
			ResultSet jeuEnregistrements = state.executeQuery(requete);
			while (jeuEnregistrements.next()) {
				nbRecords++;

			}
			jeuEnregistrements.close();
			state.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return nbRecords;
	}

	/**
	 * Obtenir la liste des compte mails enregistr�s en base
	 * @return
	 */
	public static ArrayList<String> getListeDeComptes() {
		String requete = "Select " + EnStructureTable.COMPTES_NOM.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable();
		return getListeDeChamp(requete);

	}

	/**
	 * Obtenir l'id d'un compte a partir de son nom
	 * @param p_nomCompte
	 * @return
	 */
	public static int getIdComptes(String p_nomCompte) {

		String requete = "Select " + EnStructureTable.COMPTES_ID.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable() + " where "
				+ EnStructureTable.COMPTES_NOM.getNomChamp() + " ='"
				+ p_nomCompte + "'";
		if ("".equals(get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(get1Champ(requete));
		}

	}

	/**
	 * Obtenir l'id d'un dossier a partir de son nom et d'un id de compte
	 * @param p_nomDossier
	 * @param p_idCompte
	 * @return
	 */
	public static int getIdDossier(String p_nomDossier, int p_idCompte) {

		String requete = "Select " + EnStructureTable.DOSSIER_ID.getNomChamp()
				+ " from " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructureTable.DOSSIER_NOM.getNomChamp() + " ='"
				+ p_nomDossier + "' AND "
				+ EnStructureTable.COMPTES_ID.getNomChamp() + "='" + p_idCompte
				+ "'";

		if ("".equals(get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(get1Champ(requete));
		}

	}

	/**
	 * Obtenir une liste des sous dossier "de base"
	 * (reception,envoy�,spam,corbeille) a partir d'un id de compte
	 * @param p_idCompte
	 * @return
	 */
	public static ArrayList<String> getListeSousDossierBase(int p_idCompte) {
		String requete = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_COMPTE='"
				+ p_idCompte
				+ "' and a.ID_DOSSIER_PARENT=0 ORDER BY a.NOM_DOSSIER";
		return getListeDeChamp(requete);

	}

	/**
	 * obtenir le nombre de sous dossier de base pour un compte
	 * @param p_idCompte
	 * @return
	 */
	public static int getnbSousDossierBase(int p_idCompte) {
		return getListeSousDossierBase(p_idCompte).size();

	}

	/**
	 * obtenir la liste des sous dossier a partir du nom d'un dossier racine
	 * @param p_idDossier
	 * @return
	 */
	public static ArrayList<String> getListeSousDossier(int p_idDossier) {
		String requete = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_DOSSIER_PARENT='"
				+ p_idDossier + "' ORDER BY a.NOM_DOSSIER";
		return getListeDeChamp(requete);

	}

	/**
	 * obtenir la liste des message a partir du nom d'un dossier racine
	 * @param p_idDossierracine
	 * @return
	 */
	public static ArrayList<String> getListeMessage(int p_idDossierracine) {
		String requete = "SELECT a.ID_MESSAGE_RECU FROM MAIL_RECU a where a.ID_DOSSIER_STOCKAGE='"
				+ p_idDossierracine + "'";
		return getListeDeChamp(requete);

	}

	/**
	 * Obtenir le nombre de sous dossier a partir du nom d'un dossier racine
	 * @param p_idDossierracine
	 * @return
	 */
	public static int getnbSousDossier(int p_idDossierracine) {
		return getListeSousDossier(p_idDossierracine).size();
	}

	/**
	 * Obtenir le nombre de message a partir du nom d'un dossier racine
	 * @param p_idDossierracine
	 * @return
	 */
	public static int getnbMessage(int p_idDossierracine) {
		return getListeMessage(p_idDossierracine).size();
	}

	/**
	 * Obtenir le champ "USER" a partir d'un id de compte
	 * @param p_idCpt
	 * @return
	 */
	public static String getUserFromIdCompte(int p_idCpt) {
		StringBuilder sb = new StringBuilder();
		sb.append("Select " + EnStructureTable.COMPTES_USERNAME.getNomChamp()
				+ " from ");
		sb.append(EnTable.COMPTES.getNomTable() + " where ");
		sb.append(EnStructureTable.COMPTES_ID.getNomChamp() + " ='" + p_idCpt
				+ "'");

		return get1Champ(sb.toString());
	}

	/**
	 * Obtenir le champ "Password" a partir d'un id de compte
	 * @param p_idCpt
	 * @return
	 */
	public static String getPasswordFromIdCompte(int p_idCpt) {
		String requete = "Select " + EnStructureTable.COMPTES_PWD.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable() + " where "
				+ EnStructureTable.COMPTES_ID.getNomChamp() + " ='" + p_idCpt
				+ "'";

		return get1Champ(requete);
	}

	/**
	 * Obtenir le servuer pop a partir d'un id de compte
	 * @param p_idCpt
	 * @return
	 */
	public static String getHostFromIdCompte(int p_idCpt) {
		String requete = "Select "
				+ EnStructureTable.COMPTES_SERVEURPOP.getNomChamp() + " from "
				+ EnTable.COMPTES.getNomTable() + " where "
				+ EnStructureTable.COMPTES_ID.getNomChamp() + " ='" + p_idCpt
				+ "'";

		return get1Champ(requete);
	}

	/**
	 * Cr�er un nouveau dossier en base
	 * @param p_idCompte - String - l'id de compte
	 * @param p_idDossierParent - String - l'id du dossier parent
	 * @param p_nomNewDossier - String le nom du nouveau dossier
	 * @return true ou false
	 */
	public static boolean createNewDossier(int p_idCompte,
			int p_idDossierParent, String p_nomNewDossier) {
		String requette = "INSERT "
				+ "INTO DOSSIER (ID_COMPTE,  ID_DOSSIER_PARENT, NOM_DOSSIER) "
				+ " VALUES (" //
				+ "'" + p_idCompte + //
				"','" + p_idDossierParent + "','"//
				+ p_nomNewDossier + "')";
		return executeRequete(requette);

	}

	/**
	 * Obtenir un champ a partir d'une requette
	 * @param requete
	 * @return
	 */
	private static String get1Champ(String requete) {

		String chaine_champ = "";
		try {
			Statement state = laConnexion.createStatement();
			final ResultSet jeuEnregistrements = state.executeQuery(requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {
				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					chaine_champ = jeuEnregistrements.getString(i);
				}
			}
			laConnexion.commit();
			jeuEnregistrements.close();
			state.close();
		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
			messageUtilisateur.affMessageException(e, "Erreur SQL");
		}
		return chaine_champ;
	}

	/**
	 * Obtenir une liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	private static ArrayList<String> getListeDeChamp(String p_requete) {
		Statement state;
		ArrayList<String> lst = new ArrayList<String>();
		try {
			state = laConnexion.createStatement();
			final ResultSet jeuEnregistrements = state.executeQuery(p_requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {

				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					String chaine_champ = jeuEnregistrements.getString(i);
					lst.add(chaine_champ);
				}
			}
			laConnexion.commit();
			jeuEnregistrements.close();
			state.close();
		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
		}
		return lst;
	}

	/**
	 * Obtenir une liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	private static ArrayList<ArrayList<String>> getListeDenregistrement(
			String p_requete) {
		Statement state;
		ArrayList<ArrayList<String>> lstRetour = new ArrayList<ArrayList<String>>();
		try {
			state = laConnexion.createStatement();
			final ResultSet jeuEnregistrements = state.executeQuery(p_requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {
				ArrayList<String> lstintermediaire = new ArrayList<String>();
				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					String chaine_champ = jeuEnregistrements.getString(i);
					lstintermediaire.add(chaine_champ);
				}
				lstRetour.add(lstintermediaire);
			}
			laConnexion.commit();
			jeuEnregistrements.close();
			state.close();
		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
		}
		return lstRetour;
	}

	/**
	 * Effacer un dossier en base de facon recursive (tout les enfants sont
	 * egalement effac�)
	 * @param p_idCompte
	 * @param p_idDossier
	 * @return
	 */
	public static boolean deleteDossier(int p_idCompte, int p_idDossier) {

		ArrayList<String> lstSousDossier = getListeSousDossier(p_idDossier);
		for (String dossier : lstSousDossier) {
			deleteDossier(p_idCompte, BDRequette.getIdDossier(dossier,
					p_idCompte));

		}
		String requette = "DELETE FROM DOSSIER WHERE ID_COMPTE='" + p_idCompte
				+ "' AND ID_DOSSIER='" + p_idDossier + "'";
		String requetteBis = "DELETE FROM MAIL_RECU WHERE ID_COMPTE='"
				+ p_idCompte + "' AND ID_DOSSIER_STOCKAGE='" + p_idDossier
				+ "'";
		if (executeRequete(requette)) {
			// si on a reussi a supprimer le dossier en base,
			// on suppr tt les mails enregist�s
			return executeRequete(requetteBis);
		}
		return false;

	}

	public static boolean deleteMessageRecu(int p_idMessage) {
		// on commence par effacer les piece jointe associ�es au message.
		ArrayList<String> lstPieceJointe = getListeIdPieceJointe(p_idMessage);
		for (String pieceJointe : lstPieceJointe) {
			String requete = "DELETE FROM PIECE_JOINTE WHERE ID_PIECE_JOINTE='"
					+ pieceJointe + "'";
			executeRequete(requete);
		}

		// on peut ensuite supprimer les messages
		String requetteMessage = "DELETE FROM MAIL_RECU WHERE ID_MESSAGE_RECU='"
				+ p_idMessage + "'";
		return executeRequete(requetteMessage);

	}

	public static ArrayList<String> getListeIdPieceJointe(int p_idMessage) {
		String requette = "SELECT a.ID_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return getListeDeChamp(requette);
	}

	public static ArrayList<String> getListNomPieceJointe(int p_idMessage) {
		String requette = "SELECT a.NOM_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return getListeDeChamp(requette);
	}

	public static ArrayList<String> getListeDossier(int p_idComptes) {
		String requette = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_COMPTE='"
				+ p_idComptes + "' ORDER BY a.NOM_DOSSIER";
		return getListeDeChamp(requette);

	}

	public static void createNewMessage(MlMessage m) {
		int idCompte = m.getIdCompte();
		int idDossierStockage = m.getIdDossier();
		String uidMessage = m.getUIDMessage();
		String expediteur = encodeHTMLforBase(m.getExpediteur());
		ArrayList<String> listeDestinataire = m.getDestinataire();
		StringBuilder sbDest = new StringBuilder();
		for (String dest : listeDestinataire) {
			sbDest.append(dest);
		}
		String destinataires = sbDest.toString();
		File fileToBlobDestinataires = createFileForBlob(destinataires,
				"destinataires");
		String sujet = encodeHTMLforBase(m.getSujet());
		String contenu = m.getContenu();
		File fileToBlobContenu = createFileForBlob(contenu, "contenu");

		String dateReception = RecupDate.getTimeStamp(m.getDateReception());

		int tailleStringBuilder = uidMessage.length() + expediteur.length()
				+ destinataires.length() + sujet.length() + 500;
		// on construit la requette
		StringBuilder requette = new StringBuilder(tailleStringBuilder);
		requette.ensureCapacity(tailleStringBuilder);
		requette.append("INSERT INTO MAIL_RECU");
		requette
				.append("(ID_COMPTE, ID_DOSSIER_STOCKAGE, UID_MESSAGE, EXPEDITEUR, DESTINATAIRE, SUJET,CONTENU,DATE_RECEPTION,STATUT)");
		requette.append("VALUES (");
		requette.append("'" + idCompte + "','");
		requette.append(idDossierStockage + "','");
		requette.append(uidMessage + "','");
		requette.append(expediteur + "',");
		requette.append("?,'");
		requette.append(sujet + "',");
		requette.append("?,'");// c'est pour le contenu qui sera stock� dans un
		// blob
		requette.append(dateReception + "',");
		requette.append("0)");// le status de lecture du message (0= non
		// lu,1=lu)

		// on l'execute

		boolean succes = executeRequeteWithBlob(requette.toString(),
				fileToBlobContenu, fileToBlobDestinataires);
		if (succes) {
			// on recupere le nouvel id du message que l'on vient d'enregistrer
			String getMaxId = "SELECT max (ID_MESSAGE_RECU) FROM MAIL_RECU a";
			String maxId = get1Champ(getMaxId);
			System.out
					.println("l'id de message que l'on vient d'enregistrer est: "
							+ maxId);
			fileToBlobContenu.delete();
			fileToBlobDestinataires.delete();
			// on insere le contenu en base
			// si des pieces jointe sont presente, on enregistre leur chemin en
			// base avec l'id du message
			for (File f : m.getListePieceJointe()) {
				if (enregistrePieceJointe(maxId, f)) {
					f.delete();
				}
			}

		}

	}

	private static boolean enregistrePieceJointe(String maxId,
			File p_PieceJointe) {
		String requette = "INSERT INTO PIECE_JOINTE (CONTENU_PIECE_JOINTE,NOM_PIECE_JOINTE, ID_MESSAGE) VALUES ("
				+ "?,'" + p_PieceJointe.getName() + "','" + maxId + "')";
		PreparedStatement ps = null;
		FileInputStream inPieceJointe = null;
		try {
			ps = laConnexion.prepareStatement(requette);
			inPieceJointe = new FileInputStream(p_PieceJointe);
			ps.setBinaryStream(1, inPieceJointe, (int) p_PieceJointe.length());
			ps.executeUpdate();
			laConnexion.commit();
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur � l'insertion d'un blob");
			return false;
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"impossible de trouver le fichier");
			return false;
		} finally {
			try {
				ps.close();
				inPieceJointe.close();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Erreur � l'insertion d'un blob");
			} catch (IOException e) {
				messageUtilisateur.affMessageException(e, "fichier non trouv�");

			}
		}
		return true;

	}

	private static File createFileForBlob(String contenu, String p_extension) {
		File reptempo = new File(GestionRepertoire.RecupRepTravail()
				+ "\\tempo");
		if (!reptempo.exists()) {
			reptempo.mkdirs();
		}
		File f = new File(reptempo + "\\temfile" + p_extension);
		try {
			f.createNewFile();
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"impossible de cr�er le fichier " + f.getName());
			return null;
		}
		WriteFile.WriteFullFile(contenu, reptempo + "\\temfile" + p_extension);
		return f;
	}

	public static String encodeHTMLforBase(String aText) {
		if (aText == null) {
			aText = "inconnu";
		}
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '&') {
				result.append("&amp;");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public static String decodeHTMLFromBase(String p_input) {
		return p_input//
				.replaceAll("&lt;", "<")//
				.replaceAll("&gt;", ">")//
				.replaceAll("&quot;", "\"")//
				.replaceAll("&#039;", "\'")//
				.replaceAll("&amp;", "&");
	}

	public static MlListeMessage getListeDeMessage(int p_idCompte,
			int p_idDossierChoisi) {
		MlListeMessage lstMessage = new MlListeMessage();
		String requette = "SELECT " + "a.ID_MESSAGE_RECU, " + "a.UID_MESSAGE, "
				+ "a.EXPEDITEUR, " + "a.DESTINATAIRE, " + "a.SUJET, "
				+ "a.CONTENU, " + "a.DATE_RECEPTION " + "FROM MAIL_RECU a "
				+ "where a.ID_COMPTE='" + p_idCompte
				+ "' and a.ID_DOSSIER_STOCKAGE='" + p_idDossierChoisi
				+ "' ORDER BY a.DATE_RECEPTION DESC";
		ArrayList<ArrayList<String>> lstResultat = getListeDenregistrement(requette);
		for (int i = 0; i < lstResultat.size(); i++) {
			ArrayList<String> unEnregistrement = lstResultat.get(i);
			MlMessage m = new MlMessage();
			m.setIdMessage(Integer.parseInt(unEnregistrement.get(0)));
			m.setUIDMessage(unEnregistrement.get(1));
			m.setExpediteur(decodeHTMLFromBase(unEnregistrement.get(2)));
			String[] tabDestinaire = unEnregistrement.get(3).split(";");
			ArrayList<String> lstDest = new ArrayList<String>();
			for (String des : tabDestinaire) {
				lstDest.add(des);
			}
			m.setDestinataire(lstDest);
			m.setSujet(decodeHTMLFromBase(unEnregistrement.get(4)));
			m.setContenu(unEnregistrement.get(5));
			m.setDateReception(RecupDate.getdateFromTimeStamp((unEnregistrement
					.get(6))));

			lstMessage.add(m);

		}

		return lstMessage;
		// return null;
	}

	public static boolean messageHavePieceJointe(int p_idMessage) {
		String requette = "SELECT COUNT (*) FROM PIECE_JOINTE WHERE ID_MESSAGE='"
				+ p_idMessage + "'";
		int messageCount = Integer.parseInt(get1Champ(requette));

		return messageCount > 0;
	}

	public static File getContenuFromId(int p_idMessage, boolean p_pleinecran) {
		String requette = "SELECT CONTENU FROM MAIL_RECU WHERE ID_MESSAGE_RECU='"
				+ p_idMessage + "'";
		File contenuHTML;
		if (p_pleinecran) {
			contenuHTML = new File(GestionRepertoire.RecupRepTravail()
					+ "/tempo/contenu_p.html");
		} else {
			contenuHTML = new File(GestionRepertoire.RecupRepTravail()
					+ "/tempo/contenu.html");
		}
		if (contenuHTML.exists()) {
			contenuHTML.delete();
		}

		contenuHTML.deleteOnExit();

		return writeBlobToFile(requette, contenuHTML);
	}

	private static File writeBlobToFile(String requette, File p_file) {

		try {
			PreparedStatement stmt = laConnexion.prepareStatement(requette);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				// String name = resultSet.getString(1);

				FileOutputStream fos = new FileOutputStream(p_file);

				byte[] buffer = new byte[256];

				//
				// Get the binary stream of our BLOB data
				//
				InputStream is = resultSet.getBinaryStream(1);
				while (is.read(buffer) > 0) {
					fos.write(buffer);
				}

				fos.close();
			}
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur Affichage du message");
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible d'afficher le message");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible d'afficher le message");
		} finally {

		}
		return p_file;

	}

	public static boolean verifieAbscenceUID(long uid) {
		String requete = "SELECT count (*) from MAIL_RECU a where a.UID_MESSAGE="
				+ uid;
		return ("0".equals(get1Champ(requete)));

	}

	public static String getVersionBase() {
		String script = "SELECT a.VERSION_BASE FROM PARAM a";
		return get1Champ(script);

	}

	public static boolean isMessageLu(int p_idMessageRecu) {
		String script = "SELECT a.STATUT FROM MAIL_RECU a where a.ID_MESSAGE_RECU='"
				+ p_idMessageRecu + "'";
		if ("1".equals(get1Champ(script))) {
			return true;
		}
		return false;

	}

	public static boolean setStatusLecture(int p_idMessage) {
		String script = "UPDATE MAIL_RECU a SET STATUT=1 WHERE a.ID_MESSAGE_RECU="
				+ p_idMessage;
		return executeRequete(script);

	}

	public static MlMessage getMessageById(int p_idMessage) {
		String script = "SELECT a.ID_MESSAGE_RECU, a.UID_MESSAGE, a.EXPEDITEUR, a.DESTINATAIRE, "
				+ "a.SUJET, a.CONTENU, a.DATE_RECEPTION, a.ID_DOSSIER_STOCKAGE"
				+ " FROM MAIL_RECU a WHERE a.ID_MESSAGE_RECU=" + p_idMessage;

		MlMessage m = new MlMessage();
		ArrayList<ArrayList<String>> lstResultat = getListeDenregistrement(script);
		for (int i = 0; i < lstResultat.size(); i++) {
			ArrayList<String> unEnregistrement = lstResultat.get(i);

			m.setIdMessage(Integer.parseInt(unEnregistrement.get(0)));
			m.setUIDMessage(unEnregistrement.get(1));
			m.setExpediteur(decodeHTMLFromBase(unEnregistrement.get(2)));
			String[] tabDestinaire = unEnregistrement.get(3).split(";");
			ArrayList<String> lstDest = new ArrayList<String>();
			for (String des : tabDestinaire) {
				lstDest.add(des);
			}
			m.setDestinataire(lstDest);
			m.setSujet(decodeHTMLFromBase(unEnregistrement.get(4)));
			m.setContenu(unEnregistrement.get(5));
			m.setDateReception(RecupDate.getdateFromTimeStamp((unEnregistrement
					.get(6))));
			m.setNomDossier(BDRequette.getNomDossier(Integer
					.parseInt(unEnregistrement.get(7))));

		}
		return m;
	}

	public static String getNomDossier(int p_idDossierStockage) {
		String script = "SELECT a.NOM_DOSSIER FROM DOSSIER a WHERE a.ID_DOSSIER="
				+ p_idDossierStockage;
		return get1Champ(script);

	}

	public static ArrayList<String> getCompteByID(int p_idCompte) {
		String script = "SELECT a.NOM_COMPTE," + " a.SERVEUR_POP,"
				+ " a.PORT_POP," + " a.SERVEUR_SMTP," + " a.PORT_SMTP,"
				+ " a.USERNAME," + " a.PWD," + " a.TYPE_COMPTE"
				+ " FROM COMPTES" + " a where a.ID_COMPTE=" + p_idCompte;

		ArrayList<ArrayList<String>> lstResultat = getListeDenregistrement(script);

		return lstResultat.get(0);

	}

	public static boolean deleteCompte(int p_idCompte) {
		ArrayList<String> lsiteDossier = getListeDossier(p_idCompte);
		for (String unDossier : lsiteDossier) {
			deleteDossier(p_idCompte, getIdDossier(unDossier, p_idCompte));
		}
		String requete = "DELETE FROM COMPTES WHERE ID_COMPTE=" + p_idCompte;
		return executeRequete(requete);

	}

	public static String getSujetFromId(int p_idMessage) {
		String requette = "SELECT SUJET FROM MAIL_RECU WHERE ID_MESSAGE_RECU="
				+ p_idMessage;
		return get1Champ(requette);
	}

}
