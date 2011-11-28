/**
 * 
 */
package bdd.accesTable;

import java.io.File;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;
import tools.GestionRepertoire;
import tools.RecupDate;
import tools.WriteFile;
import bdd.structure.EnStructMailRecu;
import bdd.structure.EnStructPieceJointe;
import bdd.structure.EnTable;
import factory.JTreeFactory;
import factory.RequetteFactory;

/**
 * @author smardine
 */
public class AccesTableMailRecu {
	private final String TAG = this.getClass().getSimpleName();
	private final RequetteFactory requeteFact;

	public AccesTableMailRecu() {
		requeteFact = new RequetteFactory();
	}

	/**
	 * Effacer un message (et ses pieces jointes si il y en a) a partir de son
	 * ID
	 * @param p_idMessage
	 * @return
	 */
	public boolean deleteMessageRecu(int p_idMessage) {
		// on commence par effacer les piece jointe associ�es au message.
		AccesTablePieceJointe accesPJ = new AccesTablePieceJointe();
		List<String> lstPieceJointe = accesPJ
				.getListeIdPieceJointe(p_idMessage);
		for (String pieceJointe : lstPieceJointe) {
			String requete = "DELETE FROM "
					+ EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
					+ EnStructPieceJointe.ID.getNomChamp() + "='" + pieceJointe
					+ "'";
			requeteFact.executeRequete(requete);
		}

		// on peut ensuite supprimer les messages
		String requetteMessage = "DELETE FROM "
				+ EnTable.MAIL_RECU.getNomTable() + " WHERE "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "=" + p_idMessage;
		return requeteFact.executeRequete(requetteMessage);

	}

	/**
	 * Enregistre un message dans la base
	 * @param m
	 */
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
		requette.append("INSERT INTO " + EnTable.MAIL_RECU.getNomTable());//
		requette.append("(");//
		requette.append(EnStructMailRecu.ID_COMPTE.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.ID_DOSSIER.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.UID.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.EXPEDITEUR.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.DEST.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.DEST_COPY.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.DEST_CACHE.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.SUJET.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.CONTENU.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.DATE_RECEPTION.getNomChamp() + ",");//
		requette.append(EnStructMailRecu.STATUT.getNomChamp() + ")");//
		requette.append("VALUES (");//
		requette.append(idCompte + ",");//
		requette.append(idDossierStockage + ",'");//
		requette.append(uidMessage + "','");//
		requette.append(expediteur + "',");//
		requette.append("?,");// lstDest
		requette.append("?,");// lstDestCC
		requette.append("?,'");// lstDestBCC
		requette.append(sujet + "',");
		requette.append("?,'");// c'est pour le contenu qui sera stock� dans un
		// blob
		requette.append(dateReception + "',");//
		requette.append("'F')");
		// le status de lecture du message (F= nonlu,T=lu)

		// on l'execute

		boolean succes = requeteFact
				.executeRequeteWithBlob(requette.toString(), fileToBlobContenu,
						fileToBlobDestinataires, fileToBlobDestCopy,
						fileToBlobDestHide);
		if (succes) {

			// on recupere le nouvel id du message que l'on vient d'enregistrer
			String getMaxId = "SELECT max ("
					+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + ") FROM "
					+ EnTable.MAIL_RECU.getNomTable();
			String maxId = requeteFact.get1Champ(getMaxId);
			// ("l'id de message que l'on vient d'enregistrer est: "
			// + maxId);
			MlListeMessage lst = new MlListeMessage();
			lst.add(new MlMessage(Integer.parseInt(maxId)));

			updateStatusLecture(lst, false);
			verifEtSuppressionBlob(new File(m.getCheminPhysique()));
			verifEtSuppressionBlob(fileToBlobContenu);
			verifEtSuppressionBlob(fileToBlobDestinataires);
			verifEtSuppressionBlob(fileToBlobDestCopy);
			verifEtSuppressionBlob(fileToBlobDestHide);

			// on insere le contenu en base
			// si des pieces jointe sont presente, on enregistre leur chemin en
			// base avec l'id du message
			for (File f1 : m.getListePieceJointe()) {
				if (requeteFact.enregistrePieceJointe(maxId, f1)) {
					f1.delete();
				}
			}

		}
	}

	/**
	 * Verifie si un fichier a bien �t� supprim�
	 * @param p_file
	 */
	private void verifEtSuppressionBlob(File p_file) {
		if (null != p_file && p_file.exists() && !p_file.delete()) {
			p_file.deleteOnExit();
		}
	}

	/**
	 * Creer un blob a partir d'une liste de contact
	 * @param p_liste la liste de contact � "blober"
	 * @param p_string l'extension du fichier cr��
	 * @return tout ca sous forme d'un File
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

	/**
	 * Ecrit le contenu d'un string dans un File
	 * @param contenu ce qu'il faut ecrire
	 * @param p_extension l'extension du fichier
	 * @return le fichier cr��
	 */
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

	/**
	 * permet de modifier le contenu d'un string avec les equivalent HTML pour
	 * eviter l'injection SQL
	 * @param p_text
	 * @return le texte modifi�
	 */
	private String encodeHTMLforBase(String p_text) {
		if (p_text == null) {
			return "inconnu";
		}
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				p_text);
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

	/**
	 * operation inverse de {@link AccesTableMailRecu.encodeHTMLforBase}
	 * @param p_input
	 * @return le texte modifi�
	 */
	public String decodeHTMLFromBase(String p_input) {
		return p_input//
				.replaceAll("&lt;", "<")//
				.replaceAll("&gt;", ">")//
				.replaceAll("&quot;", "\"")//
				.replaceAll("&#039;", "\'")//
				.replaceAll("&amp;", "&");
	}

	/**
	 * Obtenir une liste des messages contenu dans un {@link MlDossier}
	 * @param p_idCompte
	 * @param p_idDossierChoisi
	 * @return la lsite des messages
	 */
	public MlListeMessage getListeDeMessage(int p_idCompte,
			int p_idDossierChoisi) {
		AccesTablePieceJointe accesPJ = new AccesTablePieceJointe();
		MlListeMessage lstMessage = new MlListeMessage();
		String requette = "SELECT " //
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp()
				+ ", " // idx0
				+ EnStructMailRecu.UID.getNomChamp()
				+ ", "// idx1
				+ EnStructMailRecu.EXPEDITEUR.getNomChamp()
				+ ", " // idx2
				+ EnStructMailRecu.DEST.getNomChamp()
				+ ", "
				+ EnStructMailRecu.DEST_COPY.getNomChamp()
				+ ", "
				+ EnStructMailRecu.DEST_CACHE.getNomChamp()
				+ ", " // idx
				// 3,4,5
				+ EnStructMailRecu.SUJET.getNomChamp()
				+ ", "// idx6
				// + EnStructMailRecu.CONTENU.getNomChamp()
				// + ", " // idx7
				+ EnStructMailRecu.DATE_RECEPTION.getNomChamp() // idx8
				+ ", "
				+ EnStructMailRecu.STATUT.getNomChamp()
				+ " FROM "
				+ EnTable.MAIL_RECU.getNomTable() //
				+ " where " + EnStructMailRecu.ID_COMPTE.getNomChamp()
				+ "="
				+ p_idCompte
				+ " and "
				+ EnStructMailRecu.ID_DOSSIER.getNomChamp()
				+ "="
				+ p_idDossierChoisi
				+ " ORDER BY "
				+ EnStructMailRecu.DATE_RECEPTION.getNomChamp() + " DESC";
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
			// m.setContenu(unEnregistrement.get(7));
			m.setDateReception(RecupDate.getdateFromTimeStamp((unEnregistrement
					.get(7))));
			m.setLu((unEnregistrement.get(8).equals("T")));
			m.setIdDossier(p_idDossierChoisi);
			m.setIdCompte(p_idCompte);

			m.setHavePieceJointe(accesPJ
					.getListeIdPieceJointe(m.getIdMessage()).size() > 0);

			lstMessage.add(m);

		}

		return lstMessage;
		// return null;
	}

	/**
	 * Est ce que le message � au moins une piece jointe?
	 * @param p_idMessage
	 * @return true ou false
	 */
	public boolean messageHavePieceJointe(int p_idMessage) {
		String requette = "SELECT COUNT (*) FROM "
				+ EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage;
		int messageCount = Integer.parseInt(requeteFact.get1Champ(requette));
		return messageCount > 0;
	}

	/**
	 * Obtenir le contenu d'un message a partir de son id et le stocke dans un
	 * File
	 * @param p_idMessage
	 * @param p_pleinecran
	 * @return le contenu sous forme de File
	 */
	public File getContenuFromIdForFile(int p_idMessage, boolean p_pleinecran) {
		String requette = "SELECT " + EnStructMailRecu.CONTENU.getNomChamp()
				+ " FROM " + EnTable.MAIL_RECU.getNomTable() + " WHERE "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "=" + p_idMessage;
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

	/**
	 * Obtenir le contenu d'un message a partir de son ID et le stocke dans un
	 * String
	 * @param p_idMessage
	 * @return
	 */
	public String getContenuFromIdForString(int p_idMessage) {
		String requette = "SELECT " + EnStructMailRecu.CONTENU.getNomChamp()
				+ " FROM " + EnTable.MAIL_RECU.getNomTable() + " WHERE "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "=" + p_idMessage;
		return requeteFact.get1Champ(requette);
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
		String requete = "SELECT count (*) from "
				+ EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.UID.getNomChamp() + "='" + uid.trim()
				+ "' and " + EnStructMailRecu.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossier;
		return ("0".equals(requeteFact.get1Champ(requete)));

	}

	public boolean isMessageUIDAbsent(long p_uid, int p_idDossier) {
		return isMessageUIDAbsent("" + p_uid, p_idDossier);
	}

	/**
	 * Savoir si un message est lu a partr de son ID
	 * @param p_idMessageRecu
	 * @return
	 */
	public boolean isMessageLu(int p_idMessageRecu) {
		String script = "SELECT " + EnStructMailRecu.STATUT.getNomChamp()
				+ " FROM " + EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessageRecu;
		if ("T".equals(requeteFact.get1Champ(script))) {
			return true;
		}
		return false;
	}

	public boolean updateStatusLecture(MlListeMessage p_list, boolean p_isLu) {
		JTreeFactory treeFact = new JTreeFactory();
		AccesTableDossier accesDossier = new AccesTableDossier();
		AccesTableCompte accesCompte = new AccesTableCompte();
		MlCompteMail cptMail = treeFact.rechercheCompteMail(p_list.get(0)
				.getIdCompte());

		for (MlMessage m : p_list) {
			updateStatusLecture(m, p_isLu);
			cptMail.setUnreadMessCount(accesCompte
					.getUnreadMessageFromCompte(cptMail.getIdCompte()));
			MlDossier dossier = treeFact.rechercheDossier(m.getIdDossier(), m
					.getIdCompte());
			dossier.setUnreadMessageCount(accesDossier
					.getUnreadMessageFromFolder(cptMail.getIdCompte(), dossier
							.getIdDossier()));
			while (dossier.getIdDossierParent() != 0) {
				dossier = treeFact.rechercheDossier(dossier
						.getIdDossierParent(), dossier.getIdCompte());
				dossier
						.setUnreadMessageCount(dossier.getUnreadMessageCount() - 1);
			}

			treeFact.refreshJTree();
		}

		return true;
	}

	/**
	 * change le statut de lecture d'un message
	 * @param p_message l'id du message concern�
	 * @param p_isLu true si le message est lu, false si le message est non lu
	 * @return true si ca a reussi
	 */
	private boolean updateStatusLecture(MlMessage p_message, boolean p_isLu) {
		String script;
		if (p_isLu) {
			if (p_message.isLu()) {
				return true;// pas besoin de le refaire
			}
			script = "UPDATE " + EnTable.MAIL_RECU.getNomTable()
					+ " SET STATUT='T' WHERE "
					+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
					+ p_message.getIdMessage();
		} else {
			if (!p_message.isLu()) {
				return true;// pas besoin de le refaire
			}
			script = "UPDATE " + EnTable.MAIL_RECU.getNomTable()
					+ " SET STATUT='F' WHERE "
					+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
					+ p_message.getIdMessage();
		}

		return requeteFact.executeRequete(script);

	}

	/**
	 * Obtenir toutes les infos d'un MlMessage sauf son contenu
	 * @param p_idMessage
	 * @return
	 */
	public List<ArrayList<String>> getMessageById(int p_idMessage) {
		String script = "SELECT "// 
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "," + //
				EnStructMailRecu.UID.getNomChamp() + ", " + //
				EnStructMailRecu.EXPEDITEUR.getNomChamp() + ", " + //
				EnStructMailRecu.DEST.getNomChamp() + ", " + //
				EnStructMailRecu.DEST_COPY.getNomChamp() + ", " + //
				EnStructMailRecu.DEST_CACHE.getNomChamp() + ", " + //
				EnStructMailRecu.SUJET.getNomChamp() + ", " + //
				// EnStructMailRecu.CONTENU.getNomChamp() + ", " + //
				EnStructMailRecu.DATE_RECEPTION.getNomChamp() + ", " + //
				EnStructMailRecu.ID_DOSSIER.getNomChamp() + ", " + //
				EnStructMailRecu.ID_COMPTE.getNomChamp() + ", " + //
				EnStructMailRecu.STATUT.getNomChamp() + //
				" FROM " + EnTable.MAIL_RECU.getNomTable() + // 
				" WHERE "// 
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "=" + p_idMessage;

		List<ArrayList<String>> lstResultat = requeteFact
				.getListeDenregistrement(script);
		return lstResultat;
	}

	/**
	 * Obtenir le sujet d'un message a partir de son ID
	 * @param p_idMessage
	 * @return
	 */
	public String getSujetFromId(int p_idMessage) {
		String requette = "SELECT " + EnStructMailRecu.SUJET.getNomChamp()
				+ " FROM " + EnTable.MAIL_RECU.getNomTable() + " WHERE "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "=" + p_idMessage;
		return requeteFact.get1Champ(requette);
	}

	/**
	 * Deplace une liste de message vers la corbeille
	 * @param p_list
	 * @return
	 */
	public boolean deplaceMessageVersCorbeille(MlListeMessage p_list) {

		for (MlMessage m : p_list) {
			MlCompteMail cpt = new MlCompteMail(m.getIdCompte());

			String requete = "UPDATE " + EnTable.MAIL_RECU.getNomTable()
					+ " SET " + EnStructMailRecu.ID_DOSSIER.getNomChamp() + "="
					+ cpt.getIdCorbeille() + " WHERE "
					+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
					+ m.getIdMessage();
			boolean succes = requeteFact.executeRequete(requete);
			if (!succes) {
				return false;
			}
		}

		return true;// si tout s'est bien pass� ou si la liste etait vide
	}

	/**
	 * Met a jour l'UID d'un message (lors du deplacement d'un message d'un
	 * dossier vers un autre (Serveur IMAP par exemple)
	 * @param p_m
	 * @return
	 */
	public boolean updateUIDMessage(MlMessage p_m) {
		String requete = "UPDATE " + EnTable.MAIL_RECU.getNomTable() + " SET "
				+ EnStructMailRecu.UID.getNomChamp() + "='"
				+ p_m.getUIDMessage() + "' WHERE "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
				+ p_m.getIdMessage();
		return requeteFact.executeRequete(requete);

	}

	/**
	 * Obtenir le nombre de message dans un dossier
	 * @param p_idCompte
	 * @param p_idDossier
	 * @return
	 */
	public int getnbMessageParDossier(int p_idCompte, int p_idDossier) {
		String requette = "select count (" + EnStructMailRecu.UID.getNomChamp()
				+ ") FROM " + EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_COMPTE.getNomChamp() + "=" + p_idCompte
				+ " and " + EnStructMailRecu.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossier;
		return Integer.parseInt(requeteFact.get1Champ(requette));

	}
}