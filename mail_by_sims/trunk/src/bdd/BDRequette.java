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
import java.util.List;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
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

	private final Connection laConnexion;

	/**
	 * Constructeur privé car c'est une classe utilitaire (que des methode )
	 */
	public BDRequette() {
		BDAcces acess = new BDAcces();
		laConnexion = acess.getConnexion();
	}

	/**
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectuée (delete, truncate...)
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeRequete(final String requete) {

		Statement state = null;
		boolean resultatRequete = false;
		try {
			state = laConnexion.createStatement();
			resultatRequete = state.execute(requete);

		} catch (final SQLException e) {
			messageUtilisateur.affMessageException(e,
					"erreur a l'execution d'une requete");
			Historique.ecrire("Message d'erreur: " + e
					+ "\n\r sur la requete : " + requete);

			return false;
		} finally {
			try {
				if (resultatRequete) {
					laConnexion.commit();
				} else {
					if (state.getUpdateCount() > 0) {
						laConnexion.commit();
					} else {
						laConnexion.rollback();
					}

				}

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
	 * @param requete -String la recherche effectuée (delete, truncate...)
	 * @param p_fDestinataires
	 * @return vrai si ca a marché, sinon faux
	 */
	private boolean executeRequeteWithBlob(String requete, File p_fContenu,
			File p_fDestinataires) {

		PreparedStatement ps = null;
		FileInputStream inputContenu = null;
		FileInputStream inputDestinataire = null;
		boolean resultatRequete = false;
		try {
			ps = laConnexion.prepareStatement(requete);

			inputDestinataire = new FileInputStream(p_fDestinataires);
			inputContenu = new FileInputStream(p_fContenu);
			ps.setBinaryStream(1, inputDestinataire, (int) p_fDestinataires
					.length());
			ps.setBinaryStream(2, inputContenu, (int) p_fContenu.length());

			ps.executeUpdate();
			resultatRequete = true;

		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur à l'insertion d'un blob");
			return false;
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
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
				inputContenu.close();
				inputDestinataire.close();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Erreur à l'insertion d'un blob");
			} catch (IOException e) {
				messageUtilisateur.affMessageException(e, "fichier non trouvé");

			}
		}
		return true;
	}

	/**
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectuée (delete, truncate...)
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeMiseAJour(final String requete) {
		String[] tab = requete.split(";");
		int EXECUTE_FAILED = -3;
		Statement state = null;
		boolean resultatRequete = true;
		try {
			state = laConnexion.createStatement();
			for (String s : tab) {
				state.addBatch(s);
			}
			int[] results = state.executeBatch();

			for (int unresultat : results) {
				if (unresultat == EXECUTE_FAILED) {
					resultatRequete = false;
					break;
				}
			}

		} catch (final SQLException e) {
			System.out.println(e);
			Historique.ecrire("Message d'erreur: " + e
					+ "\n\r sur la requete : " + requete);
			return false;
		} finally {
			try {
				if (resultatRequete) {
					laConnexion.commit();
				} else {
					laConnexion.rollback();
				}

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
	public int getNbEnregistrementFromTable(EnTable p_table,
			EnStructureTable p_champ) {

		int nbRecords = 0;
		String requete = "Select " + p_champ.getNomChamp() + " from "
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
			messageUtilisateur.affMessageException(e,
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
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la base");
			}

		}

		return nbRecords;
	}

	/**
	 * Obtenir la liste des compte mails enregistrés en base
	 * @return
	 */
	public MlListeCompteMail getListeDeComptes() {
		String requete = "Select " + EnStructureTable.COMPTES_ID.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable();
		ArrayList<String> lst = getListeDeChamp(requete);
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
	public int getIdDossier(String p_nomDossier, int p_idCompte) {

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
	 * (reception,envoyé,spam,corbeille) a partir d'un id de compte
	 * @param p_idCompte
	 * @return
	 */
	public ArrayList<String> getListeSousDossierBase(int p_idCompte) {
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
		return getListeDeChamp(requete);

	}

	// /**
	// * obtenir la liste des message a partir du nom d'un dossier racine
	// * @param p_idDossierracine
	// * @return
	// */
	// public ArrayList<String> getListeMessage(int p_idDossierracine) {
	// String requete =
	// "SELECT a.ID_MESSAGE_RECU FROM MAIL_RECU a where a.ID_DOSSIER_STOCKAGE='"
	// + p_idDossierracine + "'";
	// return getListeDeChamp(requete);
	//
	// }

	/**
	 * Obtenir le nombre de sous dossier a partir du nom d'un dossier racine
	 * @param p_idDossierracine
	 * @return
	 */
	public int getnbSousDossier(int p_idDossierracine) {
		return getListeSousDossier(p_idDossierracine).size();
	}

	// /**
	// * Obtenir le nombre de message a partir du nom d'un dossier racine
	// * @param p_idDossierracine
	// * @return
	// */
	// public int getnbMessage(int p_idDossierracine) {
	// return getListeMessage(p_idDossierracine).size();
	// }

	// /**
	// * Obtenir le champ "USER" a partir d'un id de compte
	// * @param p_idCpt
	// * @return
	// */
	// public String getUserFromIdCompte(int p_idCpt) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("Select " + EnStructureTable.COMPTES_USERNAME.getNomChamp()
	// + " from ");
	// sb.append(EnTable.COMPTES.getNomTable() + " where ");
	// sb.append(EnStructureTable.COMPTES_ID.getNomChamp() + " ='" + p_idCpt
	// + "'");
	//
	// return get1Champ(sb.toString());
	// }

	/**
	 * Obtenir le champ "Password" a partir d'un id de compte
	 * @param p_idCpt
	 * @return
	 */
	// public String getPasswordFromIdCompte(int p_idCpt) {
	// String requete = "Select " + EnStructureTable.COMPTES_PWD.getNomChamp()
	// + " from " + EnTable.COMPTES.getNomTable() + " where "
	// + EnStructureTable.COMPTES_ID.getNomChamp() + " ='" + p_idCpt
	// + "'";
	//
	// return get1Champ(requete);
	// }

	/**
	 * Obtenir le servuer pop a partir d'un id de compte
	 * @param p_idCpt
	 * @return
	 */
	// public String getHostFromIdCompte(int p_idCpt) {
	// String requete = "Select "
	// + EnStructureTable.COMPTES_SERVEURPOP.getNomChamp() + " from "
	// + EnTable.COMPTES.getNomTable() + " where "
	// + EnStructureTable.COMPTES_ID.getNomChamp() + " ='" + p_idCpt
	// + "'";
	//
	// return get1Champ(requete);
	// }

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

		return executeRequete(requette);

	}

	/**
	 * Obtenir un champ a partir d'une requette
	 * @param requete
	 * @return
	 */
	private String get1Champ(String requete) {

		String chaine_champ = "";
		ResultSet jeuEnregistrements = null;
		Statement state = null;
		try {
			state = laConnexion.createStatement();
			jeuEnregistrements = state.executeQuery(requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {
				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					chaine_champ = jeuEnregistrements.getString(i);
				}
			}

		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
			messageUtilisateur.affMessageException(e, "Erreur SQL");
		} finally {
			try {
				jeuEnregistrements.close();
				state.close();
				laConnexion.rollback();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}

		}
		return chaine_champ;
	}

	/**
	 * Obtenir une liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	private ArrayList<String> getListeDeChamp(String p_requete) {
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		ArrayList<String> lst = new ArrayList<String>();
		try {
			state = laConnexion.createStatement();
			jeuEnregistrements = state.executeQuery(p_requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {

				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					String chaine_champ = jeuEnregistrements.getString(i);
					lst.add(chaine_champ);
				}
			}

		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
		} finally {
			try {
				laConnexion.rollback();
				jeuEnregistrements.close();
				state.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}// c'est une lecture, pas de commit;

		}
		return lst;
	}

	/**
	 * Obtenir une liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	private ArrayList<ArrayList<String>> getListeDenregistrement(
			String p_requete) {
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		ArrayList<ArrayList<String>> lstRetour = new ArrayList<ArrayList<String>>();
		try {
			state = laConnexion.createStatement();
			jeuEnregistrements = state.executeQuery(p_requete);
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

		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
		} finally {
			try {
				laConnexion.rollback();

				jeuEnregistrements.close();
				state.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}

		}
		return lstRetour;
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
		if (executeRequete(requette)) {
			// si on a reussi a supprimer le dossier en base,
			// on suppr tt les mails enregistés
			return executeRequete(requetteBis);
		}
		return false;

	}

	public boolean deleteMessageRecu(int p_idMessage) {
		// on commence par effacer les piece jointe associées au message.
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

	public ArrayList<String> getListeIdPieceJointe(int p_idMessage) {
		String requette = "SELECT a.ID_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return getListeDeChamp(requette);
	}

	public ArrayList<String> getListNomPieceJointe(int p_idMessage) {
		String requette = "SELECT a.NOM_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return getListeDeChamp(requette);
	}

	public ArrayList<String> getListeDossier(int p_idComptes) {
		String requette = "SELECT a.NOM_DOSSIER FROM DOSSIER a where a.ID_COMPTE='"
				+ p_idComptes + "' ORDER BY a.NOM_DOSSIER";
		return getListeDeChamp(requette);

	}

	public void createNewMessage(MlMessage m) {
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
		requette.append("?,'");// c'est pour le contenu qui sera stocké dans un
		// blob
		requette.append(dateReception + "',");
		requette.append("0)");// le status de lecture du message (0= non
		// lu,1=lu)

		// on l'execute

		boolean succes = executeRequeteWithBlob(requette.toString(),
				fileToBlobContenu, fileToBlobDestinataires);
		if (succes) {
			File f = new File(m.getCheminPhysique());
			if (f.exists()) {
				if (!f.delete()) {
					f.deleteOnExit();
				}
			}
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
			for (File f1 : m.getListePieceJointe()) {
				if (enregistrePieceJointe(maxId, f1)) {
					f1.delete();
				}
			}

		}

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
			messageUtilisateur.affMessageException(e,
					"Erreur à l'insertion d'un blob");
			return false;
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
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
				messageUtilisateur.affMessageException(e,
						"Erreur à l'insertion d'un blob");
				return false;
			} catch (IOException e) {
				messageUtilisateur.affMessageException(e, "fichier non trouvé");
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
			messageUtilisateur.affMessageException(e,
					"impossible de créer le fichier " + f.getName());
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

	public boolean messageHavePieceJointe(int p_idMessage) {
		String requette = "SELECT COUNT (*) FROM PIECE_JOINTE WHERE ID_MESSAGE='"
				+ p_idMessage + "'";
		int messageCount = Integer.parseInt(get1Champ(requette));

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

		return writeBlobToFile(requette, contenuHTML);
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
		return writeBlobToFile(requette, contenuPieceJointe);

	}

	private File writeBlobToFile(String requette, File p_file) {
		ResultSet resultSet = null;
		try {
			PreparedStatement stmt = laConnexion.prepareStatement(requette);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				FileOutputStream fos = new FileOutputStream(p_file);
				byte[] buffer = new byte[256];
				// Get the binary stream of our BLOB data
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
			try {
				resultSet.close();
				laConnexion.rollback();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}

		}
		return p_file;

	}

	public boolean verifieAbscenceUID(String uid, int p_idDossier) {
		String requete = "SELECT count (*) from MAIL_RECU a where a.UID_MESSAGE='"
				+ uid.trim() + "' and a.ID_DOSSIER_STOCKAGE=" + p_idDossier;
		return ("0".equals(get1Champ(requete)));

	}

	public boolean verifieAbscenceUID(long p_uid, int p_idDossier) {

		return verifieAbscenceUID("" + p_uid, p_idDossier);
	}

	public boolean isMessageLu(int p_idMessageRecu) {
		String script = "SELECT a.STATUT FROM MAIL_RECU a where a.ID_MESSAGE_RECU='"
				+ p_idMessageRecu + "'";
		if ("1".equals(get1Champ(script))) {
			return true;
		}
		return false;

	}

	public boolean setStatusLecture(int p_idMessage) {
		String script = "UPDATE MAIL_RECU a SET STATUT=1 WHERE a.ID_MESSAGE_RECU="
				+ p_idMessage;
		return executeRequete(script);

	}

	public MlMessage getMessageById(int p_idMessage) {
		String script = "SELECT a.ID_MESSAGE_RECU, a.UID_MESSAGE, a.EXPEDITEUR, a.DESTINATAIRE, "
				+ "a.SUJET, a.CONTENU, a.DATE_RECEPTION, a.ID_DOSSIER_STOCKAGE,a.ID_COMPTE"
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
			m.setNomDossier(getNomDossier(Integer.parseInt(unEnregistrement
					.get(7))));
			m.setIdCompte(Integer.parseInt(unEnregistrement.get(8)));

		}

		return m;
	}

	public String getNomDossier(int p_idDossierStockage) {
		String script = "SELECT a.NOM_DOSSIER FROM DOSSIER a WHERE a.ID_DOSSIER="
				+ p_idDossierStockage;
		return get1Champ(script);

	}

	public ArrayList<String> getCompteByID(int p_idCompte) {
		String script = "SELECT a.NOM_COMPTE," + " a.SERVEUR_POP,"
				+ " a.PORT_POP," + " a.SERVEUR_SMTP," + " a.PORT_SMTP,"
				+ " a.USERNAME," + " a.PWD," + " a.TYPE_COMPTE"
				+ " FROM COMPTES" + " a where a.ID_COMPTE=" + p_idCompte;

		ArrayList<ArrayList<String>> lstResultat = getListeDenregistrement(script);

		return lstResultat.get(0);

	}

	public boolean deleteCompte(int p_idCompte) {
		ArrayList<String> lsiteDossier = getListeDossier(p_idCompte);
		for (String unDossier : lsiteDossier) {
			deleteDossier(p_idCompte, getIdDossier(unDossier, p_idCompte));
		}
		String requete = "DELETE FROM COMPTES WHERE ID_COMPTE=" + p_idCompte;
		return executeRequete(requete);

	}

	public String getSujetFromId(int p_idMessage) {
		String requette = "SELECT SUJET FROM MAIL_RECU WHERE ID_MESSAGE_RECU="
				+ p_idMessage;
		return get1Champ(requette);
	}

	public void closeConnexion() {
		try {
			laConnexion.close();
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
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

		return executeRequete(sb.toString());
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
			if (!executeRequete(sb.toString())) {
				messageUtilisateur
						.affMessageErreur("Erreur a la creation du dossier "
								+ nomDossier + " pour le compte "
								+ p_compte.getNomCompte());
				return false;
			}

		}
		return true;
	}

	public boolean verifieNecessiteDeplacementCorbeille(int p_idMessage) {
		String requete = "SELECT a.ID_DOSSIER_STOCKAGE FROM MAIL_RECU a WHERE a.ID_MESSAGE_RECU="
				+ p_idMessage;
		int idDossierStock = Integer.parseInt(get1Champ(requete));
		MlCompteMail cpt = getMlCompteFromIdMessage(p_idMessage);

		if (idDossierStock != cpt.getIdCorbeille()) {
			return true;
		}
		return false;
	}

	public MlCompteMail getMlCompteFromIdMessage(int p_idMessage) {
		String Requete = "SELECT a.ID_COMPTE FROM MAIL_RECU a where a.ID_MESSAGE_RECU="
				+ p_idMessage;
		int idCpt = Integer.parseInt(get1Champ(Requete));
		return new MlCompteMail(idCpt);
	}

	public boolean deplaceMessageVersCorbeille(MlListeMessage p_list) {

		for (MlMessage m : p_list) {
			MlCompteMail cpt = getMlCompteFromIdMessage(m.getIdMessage());

			String requete = "UPDATE MAIL_RECU a SET ID_DOSSIER_STOCKAGE="
					+ cpt.getIdCorbeille() + " WHERE a.ID_MESSAGE_RECU="
					+ m.getIdMessage();
			boolean succes = executeRequete(requete);
			if (!succes) {
				return false;
			}
		}

		return true;// si tout s'est bien passé ou si la liste etait vide
	}

	public boolean updateUIDMessage(MlMessage p_m) {
		String requete = "UPDATE MAIL_RECU a set a.UID_MESSAGE='"
				+ p_m.getUIDMessage() + "' where ID_MESSAGE_RECU="
				+ p_m.getIdMessage();
		return executeRequete(requete);

	}

}
