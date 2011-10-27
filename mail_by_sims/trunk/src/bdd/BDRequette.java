package bdd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;
import tools.GestionRepertoire;
import tools.RecupDate;
import tools.WriteFile;
import exception.DonneeAbsenteException;
import factory.RequetteFactory;

/**
 * cette classe s'occupe uniquement des requetes
 * @author sims
 */
public class BDRequette {
	/**
	 * 
	 */
	private static final String SELECT = "Select ";
	/**
	 * 
	 */

	private final String TAG = this.getClass().getSimpleName();
	private final Connection laConnexion;
	private final RequetteFactory requeteFact;

	/**
	 * Constructeur priv� car c'est une classe utilitaire (que des methode )
	 */
	public BDRequette() {
		BDAcces acess = new BDAcces();
		laConnexion = acess.getConnexion();
		requeteFact = new RequetteFactory(laConnexion);

	}

	/**
	 * Obtenir le nombre d'enregistrement d'une table
	 * @param p_table - {@link EnTable}
	 * @param p_champ - {@link EnStructureTable} - le nom d'un champ de la table
	 * @return le nombre de ligne dans cette table
	 */
	public int getNbEnregistrementFromTable(EnTable p_table,
			EnStructureTable p_champ) {

		int nbRecords = 0;
		String requete = BDRequette.SELECT + p_champ.getNomChamp() + " from "
				+ p_table.getNomTable();

		Statement state = null;
		ResultSet jeuEnregistrements = null;
		try {
			state = laConnexion.createStatement();
			jeuEnregistrements = state.executeQuery(requete);
			while (jeuEnregistrements.next()) {
				nbRecords++;
			}

		} catch (SQLException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible de recuperer le nombre de champ dans la table "
							+ p_table.getNomTable());
		} finally {
			try {
				jeuEnregistrements.close();
				state.close();
				laConnexion.rollback();// on ne fait que de la lecture, donc on
				// peut
				// faire un rollback
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Impossible de fermer la base");
			}

		}

		return nbRecords;
	}

	/**
	 * Obtenir la liste des compte mails enregistr�s en base
	 * @return
	 */
	public MlListeCompteMail getListeDeComptes() {
		String requete = BDRequette.SELECT
				+ EnStructureTable.COMPTES_ID.getNomChamp() + " from "
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

		String requete = BDRequette.SELECT
				+ EnStructureTable.COMPTES_ID.getNomChamp() + " from "
				+ EnTable.COMPTES.getNomTable() + " where "
				+ EnStructureTable.COMPTES_NOM.getNomChamp() + " ='"
				+ p_nomCompte + "'";
		if ("".equals(requeteFact.get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(requeteFact.get1Champ(requete));
		}

	}

	/**
	 * Obtenir l'id d'un dossier a partir de son nom et d'un id de compte
	 * @param p_nomDossier
	 * @param p_idCompte
	 * @return
	 */
	public int getIdDossier(String p_nomDossier, int p_idCompte) {

		String requete = BDRequette.SELECT
				+ EnStructureTable.DOSSIER_ID.getNomChamp() + " from "
				+ EnTable.DOSSIER.getNomTable() + " where ("
				+ EnStructureTable.DOSSIER_NOM.getNomChamp() + " ='"
				+ p_nomDossier + "' AND "
				+ EnStructureTable.COMPTES_ID.getNomChamp() + "=" + p_idCompte
				+ ")";

		if ("".equals(requeteFact.get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(requeteFact.get1Champ(requete));
		}

	}

	/**
	 * Obtenir l'id d'un dossier a partir de son nom et d'un id de compte
	 * @param p_nomDossier
	 * @param p_idCompte
	 * @return
	 */
	public int getIdDossierWithFullName(String p_nomDossier, String p_fullName,
			int p_idCompte) {

		String requete = BDRequette.SELECT
				+ EnStructureTable.DOSSIER_ID.getNomChamp() + " from "
				+ EnTable.DOSSIER.getNomTable() + " where ("
				+ EnStructureTable.DOSSIER_NOM.getNomChamp() + " ='"
				+ p_nomDossier + "' AND "
				+ EnStructureTable.DOSSIER_NOM_INTERNET.getNomChamp() + "='"
				+ p_fullName + "' AND "
				+ EnStructureTable.COMPTES_ID.getNomChamp() + "=" + p_idCompte
				+ ")";

		if ("".equals(requeteFact.get1Champ(requete))) {
			return -1;
		} else {
			return Integer.parseInt(requeteFact.get1Champ(requete));
		}

	}

	/**
	 * Obtenir une liste des sous dossier "de base"
	 * (reception,envoy�,spam,corbeille) a partir d'un id de compte
	 * @param p_idCompte
	 * @return
	 */
	public List<String> getListeSousDossierBase(int p_idCompte) {
		String requete = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_COMPTE='"
				+ p_idCompte
				+ "' and a.ID_DOSSIER_PARENT=0 ORDER BY a.NOM_DOSSIER";
		return requeteFact.getListeDeChamp(requete);

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
	public List<String> getListeSousDossier(int p_idDossier) {
		String requete = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_DOSSIER_PARENT='"
				+ p_idDossier + "' ORDER BY a.NOM_DOSSIER";
		return requeteFact.getListeDeChamp(requete);

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
	 * Cr�er un nouveau dossier en base
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

		return requeteFact.executeRequete(requette);

	}

	/**
	 * Effacer un dossier en base de facon recursive (tout les enfants sont
	 * egalement effac�)
	 * @param p_idCompte
	 * @param p_idDossier
	 * @return
	 */
	public boolean deleteDossier(int p_idCompte, int p_idDossier) {

		List<String> lstSousDossier = getListeSousDossier(p_idDossier);
		for (String dossier : lstSousDossier) {
			deleteDossier(p_idCompte, getIdDossierWithFullName(dossier,
					getNomInternetDossier(p_idDossier), p_idCompte));
		}
		String requette = "DELETE FROM DOSSIER WHERE ID_COMPTE='" + p_idCompte
				+ "' AND ID_DOSSIER='" + p_idDossier + "'";

		// pour chaque dossier, on supprime la liste des messages associ�s
		for (MlMessage unMessage : getListeDeMessage(p_idCompte, p_idDossier)) {
			if (!deleteMessageRecu(unMessage.getIdMessage())) {
				return false;
			}
		}

		return requeteFact.executeRequete(requette);

	}

	public boolean deleteMessageRecu(int p_idMessage) {
		// on commence par effacer les piece jointe associ�es au message.
		List<String> lstPieceJointe = getListeIdPieceJointe(p_idMessage);
		for (String pieceJointe : lstPieceJointe) {
			String requete = "DELETE FROM PIECE_JOINTE WHERE ID_PIECE_JOINTE='"
					+ pieceJointe + "'";
			requeteFact.executeRequete(requete);
		}

		// on peut ensuite supprimer les messages
		String requetteMessage = "DELETE FROM MAIL_RECU WHERE ID_MESSAGE_RECU='"
				+ p_idMessage + "'";
		return requeteFact.executeRequete(requetteMessage);

	}

	public List<String> getListeIdPieceJointe(int p_idMessage) {
		String requette = "SELECT a.ID_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return requeteFact.getListeDeChamp(requette);
	}

	public List<String> getListeNomPieceJointe(int p_idMessage) {
		String requette = "SELECT a.NOM_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return requeteFact.getListeDeChamp(requette);
	}

	public List<String> getListeDossier(int p_idComptes) {
		String requette = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_COMPTE='"
				+ p_idComptes + "' ORDER BY a.NOM_DOSSIER";
		return requeteFact.getListeDeChamp(requette);

	}

	public void createNewMessage(MlMessage m) {
		int idCompte = m.getIdCompte();
		int idDossierStockage = m.getIdDossier();
		String uidMessage = m.getUIDMessage();
		String expediteur = encodeHTMLforBase(m.getExpediteur());
		List<String> listeDestinataire = m.getDestinataire();
		List<String> listCopy = m.getDestinataireCopy();
		List<String> listCachee = m.getDestinataireCache();

		File fileToBlobDestinataires = createBlobFileFromlist(
				listeDestinataire, "destinataire");
		File fileToBlobDestCopy = createBlobFileFromlist(listCopy, "dest_copy");
		File fileToBlobDestHide = createBlobFileFromlist(listCachee,
				"hide_dest");

		String sujet = encodeHTMLforBase(m.getSujet());
		String contenu = m.getContenu();
		File fileToBlobContenu = createFileForBlob(contenu, "contenu");

		String dateReception = RecupDate.getTimeStamp(m.getDateReception());

		int tailleStringBuilder = uidMessage.length() + expediteur.length()
		/* + destinataires.length() */+ sujet.length() + 500;
		// on construit la requette
		StringBuilder requette = new StringBuilder(tailleStringBuilder);
		requette.ensureCapacity(tailleStringBuilder);
		requette.append("INSERT INTO MAIL_RECU");
		requette
				.append("(ID_COMPTE, ID_DOSSIER_STOCKAGE, UID_MESSAGE, EXPEDITEUR, DESTINATAIRE,DESTINATAIRE_COPY,DESTINATAIRE_CACHE, SUJET,CONTENU,DATE_RECEPTION,STATUT)");
		requette.append("VALUES (");
		requette.append("'" + idCompte + "','");
		requette.append(idDossierStockage + "','");
		requette.append(uidMessage + "','");
		requette.append(expediteur + "',");
		requette.append("?,");// lstDest
		requette.append("?,");// lstDestCC
		requette.append("?,'");// lstDestBCC
		requette.append(sujet + "',");
		requette.append("?,'");// c'est pour le contenu qui sera stock� dans un
		// blob
		requette.append(dateReception + "',");
		requette.append("0)");// le status de lecture du message (0= non
		// lu,1=lu)

		// on l'execute

		boolean succes = requeteFact
				.executeRequeteWithBlob(requette.toString(), fileToBlobContenu,
						fileToBlobDestinataires, fileToBlobDestCopy,
						fileToBlobDestHide);
		if (succes) {

			// on recupere le nouvel id du message que l'on vient d'enregistrer
			String getMaxId = "SELECT max (ID_MESSAGE_RECU) FROM MAIL_RECU a";
			String maxId = requeteFact.get1Champ(getMaxId);
			// ("l'id de message que l'on vient d'enregistrer est: "
			// + maxId);
			verifEtSuppressionBlob(new File(m.getCheminPhysique()));
			verifEtSuppressionBlob(fileToBlobContenu);
			verifEtSuppressionBlob(fileToBlobDestinataires);
			verifEtSuppressionBlob(fileToBlobDestCopy);
			verifEtSuppressionBlob(fileToBlobDestHide);

			// on insere le contenu en base
			// si des pieces jointe sont presente, on enregistre leur chemin en
			// base avec l'id du message
			for (File f1 : m.getListePieceJointe()) {
				if (enregistrePieceJointe(maxId, f1)) {
					f1.delete();
				}
			}

		}

	}

	/**
	 * @param p_file
	 */
	private void verifEtSuppressionBlob(File p_file) {
		if (null != p_file && p_file.exists() && !p_file.delete()) {
			p_file.deleteOnExit();
		}
	}

	/**
	 * @param p_listeDestinataire
	 * @param p_string
	 * @return
	 */
	private File createBlobFileFromlist(List<String> p_liste, String p_extension) {
		if (p_liste == null) {
			return null;
		}
		StringBuilder sbDest = new StringBuilder();
		for (String dest : p_liste) {
			sbDest.append(dest);
		}
		String destinataires = sbDest.toString();
		return createFileForBlob(destinataires, p_extension);
	}

	private boolean enregistrePieceJointe(String maxId, File p_PieceJointe) {
		String requette = "INSERT INTO PIECE_JOINTE (CONTENU_PIECE_JOINTE,NOM_PIECE_JOINTE, ID_MESSAGE) VALUES ("
				+ "?,'"
				+ p_PieceJointe.getName().replace("'", "_")
				+ "','"
				+ maxId + "')";
		PreparedStatement ps = null;
		FileInputStream inPieceJointe = null;
		boolean resultatRequete = false;
		try {
			ps = laConnexion.prepareStatement(requette);
			inPieceJointe = new FileInputStream(p_PieceJointe);
			ps.setBinaryStream(1, inPieceJointe, (int) p_PieceJointe.length());
			ps.executeUpdate();
			resultatRequete = true;

		} catch (SQLException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur � l'insertion d'un blob");
			return false;
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"impossible de trouver le fichier");
			return false;
		} finally {
			try {
				if (resultatRequete) {
					laConnexion.commit();
				} else {
					laConnexion.rollback();
				}
				ps.close();
				inPieceJointe.close();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Erreur � l'insertion d'un blob");
				return false;
			} catch (IOException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"fichier non trouv�");
				return false;

			}
		}
		return true;

	}

	private File createFileForBlob(String contenu, String p_extension) {
		File reptempo = new File(GestionRepertoire.RecupRepTravail()
				+ "\\tempo");
		if (!reptempo.exists()) {
			reptempo.mkdirs();
		}
		File f = new File(reptempo + "\\temfile" + p_extension);
		try {
			f.createNewFile();
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"impossible de cr�er le fichier " + f.getName());
			return null;
		}
		WriteFile.WriteFullFile(contenu, reptempo + "\\temfile" + p_extension);
		return f;
	}

	public String encodeHTMLforBase(String aText) {
		if (aText == null) {
			return "inconnu";
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

	public String decodeHTMLFromBase(String p_input) {
		return p_input//
				.replaceAll("&lt;", "<")//
				.replaceAll("&gt;", ">")//
				.replaceAll("&quot;", "\"")//
				.replaceAll("&#039;", "\'")//
				.replaceAll("&amp;", "&");
	}

	public MlListeMessage getListeDeMessage(int p_idCompte,
			int p_idDossierChoisi) {
		MlListeMessage lstMessage = new MlListeMessage();
		String requette = "SELECT " //
				+ "a.ID_MESSAGE_RECU, " // idx0
				+ "a.UID_MESSAGE, "// idx1
				+ "a.EXPEDITEUR, " // idx2
				+ "a.DESTINATAIRE, a.DESTINATAIRE_COPY,a.DESTINATAIRE_CACHE, " // idx
				// 3,4,5
				+ "a.SUJET, "// idx6
				+ "a.CONTENU, " // idx7
				+ "a.DATE_RECEPTION " // idx8
				+ "FROM MAIL_RECU a " //
				+ "where a.ID_COMPTE='"
				+ p_idCompte
				+ "' and a.ID_DOSSIER_STOCKAGE='"
				+ p_idDossierChoisi
				+ "' ORDER BY a.DATE_RECEPTION DESC";
		List<ArrayList<String>> lstResultat = requeteFact
				.getListeDenregistrement(requette);
		for (int i = 0; i < lstResultat.size(); i++) {
			ArrayList<String> unEnregistrement = lstResultat.get(i);
			MlMessage m = new MlMessage();
			m.setIdMessage(Integer.parseInt(unEnregistrement.get(0)));
			m.setUIDMessage(unEnregistrement.get(1));
			m.setExpediteur(decodeHTMLFromBase(unEnregistrement.get(2)));

			if (unEnregistrement.get(3) != null) {// DESTINATAIRE
				String[] tabDestinaire = unEnregistrement.get(3).split(";");
				ArrayList<String> lstDest = new ArrayList<String>();
				for (String des : tabDestinaire) {
					lstDest.add(des);
				}
				m.setDestinataire(lstDest);
			}
			if (unEnregistrement.get(4) != null) {// DESTINATAIRE COPY
				String[] tabDestinaire = unEnregistrement.get(4).split(";");
				ArrayList<String> lstDest = new ArrayList<String>();
				for (String des : tabDestinaire) {
					lstDest.add(des);
				}
				m.setDestinataireCopy(lstDest);
			}
			if (unEnregistrement.get(5) != null) {// DESTINATAIRE CACHE
				String[] tabDestinaire = unEnregistrement.get(5).split(";");
				ArrayList<String> lstDest = new ArrayList<String>();
				for (String des : tabDestinaire) {
					lstDest.add(des);
				}
				m.setDestinataireCache(lstDest);
			}

			m.setSujet(decodeHTMLFromBase(unEnregistrement.get(6)));
			m.setContenu(unEnregistrement.get(7));
			m.setDateReception(RecupDate.getdateFromTimeStamp((unEnregistrement
					.get(8))));

			lstMessage.add(m);

		}

		return lstMessage;
		// return null;
	}

	public boolean messageHavePieceJointe(int p_idMessage) {
		String requette = "SELECT COUNT (*) FROM PIECE_JOINTE WHERE ID_MESSAGE='"
				+ p_idMessage + "'";
		int messageCount = Integer.parseInt(requeteFact.get1Champ(requette));

		return messageCount > 0;
	}

	public File getContenuFromId(int p_idMessage, boolean p_pleinecran) {
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

		return requeteFact.writeBlobToFile(requette, contenuHTML);
	}

	public File getPieceJointeFromIDMessage(int p_idMessage, String p_nameFile) {
		String requette = "SELECT CONTENU_PIECE_JOINTE FROM PIECE_JOINTE WHERE ID_MESSAGE="
				+ p_idMessage
				+ " and NOM_PIECE_JOINTE='"
				+ p_nameFile.trim()
				+ "'";
		File contenuPieceJointe = new File(GestionRepertoire.RecupRepTravail()
				+ "/tempo/" + p_nameFile);
		if (contenuPieceJointe.exists()) {
			contenuPieceJointe.delete();
		}
		return requeteFact.writeBlobToFile(requette, contenuPieceJointe);

	}

	/**
	 * savoir si l'uid est connu de la base
	 * @param uid - l'UID du message que l'on est en train de relever
	 * @param p_idDossier - l'id du dossier dans lequel on fait la recherche
	 * @return true si le message n'est pas present, false si le message est
	 *         deja en base
	 */
	public boolean isMessageUIDAbsent(String uid, int p_idDossier) {
		if (null == uid) {
			return true;// le message N'EST PAS EN BASE
		}
		String requete = "SELECT count (*) from MAIL_RECU a where a.UID_MESSAGE='"
				+ uid.trim() + "' and a.ID_DOSSIER_STOCKAGE=" + p_idDossier;
		return ("0".equals(requeteFact.get1Champ(requete)));

	}

	public boolean isMessageUIDAbsent(long p_uid, int p_idDossier) {

		return isMessageUIDAbsent("" + p_uid, p_idDossier);
	}

	public boolean isMessageLu(int p_idMessageRecu) {
		String script = "SELECT a.STATUT FROM MAIL_RECU a where a.ID_MESSAGE_RECU='"
				+ p_idMessageRecu + "'";
		if ("1".equals(requeteFact.get1Champ(script))) {
			return true;
		}
		return false;

	}

	public boolean setStatusLecture(int p_idMessage) {
		String script = "UPDATE MAIL_RECU a SET STATUT=1 WHERE a.ID_MESSAGE_RECU="
				+ p_idMessage;
		return requeteFact.executeRequete(script);

	}

	public List<ArrayList<String>> getMessageById(int p_idMessage) {
		String script = "SELECT a.ID_MESSAGE_RECU," + //
				" a.UID_MESSAGE, " + //
				"a.EXPEDITEUR, " + //
				"a.DESTINATAIRE, " + //
				"a.DESTINATAIRE_COPY, " + //
				"a.DESTINATAIRE_CACHE, " + //
				"a.SUJET, " + //
				"a.CONTENU, " + //
				"a.DATE_RECEPTION, " + //
				"a.ID_DOSSIER_STOCKAGE, " + //
				"a.ID_COMPTE" + //
				" FROM MAIL_RECU a " + // 
				"WHERE a.ID_MESSAGE_RECU=" + p_idMessage;

		// MlMessage m = new MlMessage();
		List<ArrayList<String>> lstResultat = requeteFact
				.getListeDenregistrement(script);
		// for (int i = 0; i < lstResultat.size(); i++) {
		// ArrayList<String> unEnregistrement = lstResultat.get(i);
		//
		// m.setIdMessage(Integer.parseInt(unEnregistrement.get(0)));
		// m.setUIDMessage(unEnregistrement.get(1));
		// m.setExpediteur(decodeHTMLFromBase(unEnregistrement.get(2)));
		// String[] tabDestinaire = unEnregistrement.get(3).split(";");
		// ArrayList<String> lstDest = new ArrayList<String>();
		// for (String des : tabDestinaire) {
		// lstDest.add(des);
		// }
		// m.setDestinataire(lstDest);
		// m.setSujet(decodeHTMLFromBase(unEnregistrement.get(4)));
		// m.setContenu(unEnregistrement.get(5));
		// m.setDateReception(RecupDate.getdateFromTimeStamp((unEnregistrement
		// .get(6))));
		// m.setIdDossier(Integer.parseInt(unEnregistrement
		// .get(7)));
		// m.setNomDossier(getNomDossier(Integer.parseInt(unEnregistrement
		// .get(7))));
		// m.setIdCompte(Integer.parseInt(unEnregistrement.get(8)));
		//
		// }

		return lstResultat;
	}

	public String getNomDossier(int p_idDossierStockage) {
		String script = "SELECT a.NOM_DOSSIER FROM DOSSIER a WHERE a.ID_DOSSIER="
				+ p_idDossierStockage;
		return requeteFact.get1Champ(script);

	}

	public String getNomInternetDossier(int p_idDossierStockage) {
		String script = "SELECT a.NOM_INTERNET FROM DOSSIER a WHERE a.ID_DOSSIER="
				+ p_idDossierStockage;
		return requeteFact.get1Champ(script);

	}

	public List<String> getCompteByID(int p_idCompte) {
		String script = "SELECT a.NOM_COMPTE," + " a.SERVEUR_POP,"
				+ " a.PORT_POP," + " a.SERVEUR_SMTP," + " a.PORT_SMTP,"
				+ " a.USERNAME," + " a.PWD," + " a.TYPE_COMPTE"
				+ " FROM COMPTES" + " a where a.ID_COMPTE=" + p_idCompte;

		List<ArrayList<String>> lstResultat = requeteFact
				.getListeDenregistrement(script);

		return lstResultat.get(0);

	}

	/**
	 * Cette fonction parcours tout les dossier enregistr� du compte mail 1 par
	 * 1 pour supprimer en cascade les messages et PJ associ�es.
	 * @param p_idCompte
	 * @return resultat
	 * @throws DonneeAbsenteException
	 */
	public boolean deleteCompte(int p_idCompte) throws DonneeAbsenteException {
		if (p_idCompte == 0) {
			throw new DonneeAbsenteException(TAG,
					"le compte � supprim� n'existe pas en base");
		}
		List<String> lsiteDossier = getListeDossier(p_idCompte);
		for (String unDossier : lsiteDossier) {
			deleteDossier(p_idCompte, getIdDossier(unDossier, p_idCompte));
		}
		String requete = "DELETE FROM COMPTES WHERE ID_COMPTE=" + p_idCompte;
		return requeteFact.executeRequete(requete);

	}

	public String getSujetFromId(int p_idMessage) {
		String requette = "SELECT SUJET FROM MAIL_RECU WHERE ID_MESSAGE_RECU="
				+ p_idMessage;
		return requeteFact.get1Champ(requette);
	}

	public void closeConnexion() {
		try {
			laConnexion.close();
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible de fermer la base");
		}

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

		return requeteFact.executeRequete(sb.toString());
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
			if (!requeteFact.executeRequete(sb.toString())) {
				messageUtilisateur.affMessageErreur(TAG,
						"Erreur a la creation du dossier " + nomDossier
								+ " pour le compte " + p_compte.getNomCompte());
				return false;
			}

		}
		return true;
	}

	public boolean deplaceMessageVersCorbeille(MlListeMessage p_list) {

		for (MlMessage m : p_list) {
			MlCompteMail cpt = new MlCompteMail(m.getIdCompte());

			String requete = "UPDATE MAIL_RECU a SET ID_DOSSIER_STOCKAGE="
					+ cpt.getIdCorbeille() + " WHERE a.ID_MESSAGE_RECU="
					+ m.getIdMessage();
			boolean succes = requeteFact.executeRequete(requete);
			if (!succes) {
				return false;
			}
		}

		return true;// si tout s'est bien pass� ou si la liste etait vide
	}

	public boolean updateUIDMessage(MlMessage p_m) {
		String requete = "UPDATE MAIL_RECU a set a.UID_MESSAGE='"
				+ p_m.getUIDMessage() + "' where ID_MESSAGE_RECU="
				+ p_m.getIdMessage();
		return requeteFact.executeRequete(requete);

	}

	public boolean updateNomDossierInternet(int p_idDossier,
			String p_nomDossierInternet, int p_idDossierParent) {
		String requete = "UPDATE DOSSIER a set a.NOM_INTERNET='"
				+ p_nomDossierInternet.trim() + "', a.ID_DOSSIER_PARENT="
				+ p_idDossierParent + " where a.ID_DOSSIER=" + p_idDossier;
		return requeteFact.executeRequete(requete);

	}

	public int getnbMessageParDossier(int p_idCompte, int p_idDossier) {
		String requette = "select count (a.UID_MESSAGE) FROM MAIL_RECU a where a.ID_COMPTE="
				+ p_idCompte + " and a.ID_DOSSIER_STOCKAGE=" + p_idDossier;
		return Integer.parseInt(requeteFact.get1Champ(requette));

	}

}
