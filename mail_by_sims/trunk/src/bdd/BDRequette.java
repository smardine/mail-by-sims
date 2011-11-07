package bdd;

import java.io.File;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeCompteMail;
import mdl.MlListeDossier;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;
import tools.GestionRepertoire;
import tools.RecupDate;
import tools.WriteFile;
import bdd.structure.EnStructCompte;
import bdd.structure.EnStructDossier;
import bdd.structure.EnStructMailRecu;
import bdd.structure.EnStructPieceJointe;
import bdd.structure.EnTable;
import exception.DonneeAbsenteException;
import factory.RequetteFactory;
import fenetre.comptes.EnDossierBase;

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
	private final RequetteFactory requeteFact;

	/**
	 * Constructeur privé car c'est une classe utilitaire (que des methode )
	 */
	public BDRequette() {
		requeteFact = new RequetteFactory();
	}

	/**
	 * Obtenir la liste des compte mails enregistrés en base
	 * @return
	 */
	public MlListeCompteMail getListeDeComptes() {

		String requete = BDRequette.SELECT + EnStructCompte.ID.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable();
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

		String requete = BDRequette.SELECT + EnStructCompte.ID.getNomChamp()
				+ " from " + EnTable.COMPTES.getNomTable() + " where "
				+ EnStructCompte.NOM.getNomChamp() + " ='" + p_nomCompte + "'";
		String result = requeteFact.get1Champ(requete);
		if ("".equals(result)) {
			return -1;
		} else {
			return Integer.parseInt(result);
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
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + " from "
				+ EnTable.DOSSIER.getNomTable() + " where ("
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
	 * Obtenir l'id d'un dossier a partir de son nom et d'un id de compte
	 * @param p_nomDossier
	 * @param p_idCompte
	 * @return
	 */
	public int getIdDossierWithFullName(String p_nomDossier, String p_fullName,
			int p_idCompte) {

		String requete = BDRequette.SELECT
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + " from "
				+ EnTable.DOSSIER.getNomTable() + " where ("
				+ EnStructDossier.NOM.getNomChamp() + " ='" + p_nomDossier
				+ "' AND " + EnStructDossier.NOM_INTERNET.getNomChamp() + "='"
				+ p_fullName + "' AND " + EnStructCompte.ID.getNomChamp() + "="
				+ p_idCompte + ")";

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
	public MlListeDossier getListeSousDossierBase(int p_idCompte) {
		String requete = "SELECT " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable()// 
				+ " where " //
				+ EnStructCompte.ID.getNomChamp() + "=" + p_idCompte //
				+ " and "//
				+ EnStructDossier.ID_DOSSIER_PARENT.getNomChamp() + "=0 "// 
				+ "ORDER BY " + EnStructDossier.ID_DOSSIER.getNomChamp();
		List<String> lstRetour = requeteFact.getListeDeChamp(requete);
		MlListeDossier lst = new MlListeDossier();
		for (String s : lstRetour) {
			lst.add(new MlDossier(Integer.parseInt(s)));
		}
		return lst;

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
		MlListeMessage listeMessage = getListeDeMessage(p_idCompte, p_idDossier);
		int tailleListe = listeMessage.size();
		// pour chaque dossier, on supprime la liste des messages associés
		for (MlMessage unMessage : getListeDeMessage(p_idCompte, p_idDossier)) {
			if (null != p_progressBar) {
				p_progressBar.setString("Suppression du message " + (count + 1)
						+ " sur " + tailleListe);
				int pourcent = ((count + 1) * 100) / tailleListe;
				p_progressBar.setValue(pourcent);
				count++;
			}

			if (!deleteMessageRecu(unMessage.getIdMessage())) {
				return false;
			}
		}

		return requeteFact.executeRequete(requette);

	}

	public boolean deleteMessageRecu(int p_idMessage) {
		// on commence par effacer les piece jointe associées au message.
		List<String> lstPieceJointe = getListeIdPieceJointe(p_idMessage);
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

	public List<String> getListeIdPieceJointe(int p_idMessage) {
		String requette = "SELECT " + EnStructPieceJointe.ID.getNomChamp()
				+ " FROM " + EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage + " ORDER BY "
				+ EnStructPieceJointe.ID.getNomChamp();
		return requeteFact.getListeDeChamp(requette);
	}

	public List<String> getListeNomPieceJointe(int p_idMessage) {
		String requette = "SELECT " + EnStructPieceJointe.NOM.getNomChamp()
				+ " FROM " + EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage + " ORDER BY "
				+ EnStructPieceJointe.ID.getNomChamp();
		return requeteFact.getListeDeChamp(requette);
	}

	public MlListeDossier getListeDossier(int p_idCompte) {
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
		requette.append("?,'");// c'est pour le contenu qui sera stocké dans un
		// blob
		requette.append(dateReception + "',");//
		requette.append("0)");
		// le status de lecture du message (0= nonlu,1=lu)

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
				+ EnStructMailRecu.CONTENU.getNomChamp()
				+ ", " // idx7
				+ EnStructMailRecu.DATE_RECEPTION.getNomChamp() // idx8
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
			m.setContenu(unEnregistrement.get(7));
			m.setDateReception(RecupDate.getdateFromTimeStamp((unEnregistrement
					.get(8))));

			lstMessage.add(m);

		}

		return lstMessage;
		// return null;
	}

	public boolean messageHavePieceJointe(int p_idMessage) {
		String requette = "SELECT COUNT (*) FROM "
				+ EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage;
		int messageCount = Integer.parseInt(requeteFact.get1Champ(requette));

		return messageCount > 0;
	}

	public File getContenuFromId(int p_idMessage, boolean p_pleinecran) {
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

	public File getPieceJointeFromIDMessage(int p_idMessage, String p_nameFile) {
		String requette = "SELECT " + EnStructPieceJointe.CONTENU.getNomChamp()
				+ " FROM " + EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage + " and " + EnStructPieceJointe.NOM.getNomChamp()
				+ "='" + p_nameFile.trim() + "'";
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

	public boolean isMessageLu(int p_idMessageRecu) {
		String script = "SELECT " + EnStructMailRecu.STATUT.getNomChamp()
				+ " FROM " + EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessageRecu;
		if ("1".equals(requeteFact.get1Champ(script))) {
			return true;
		}
		return false;

	}

	/**
	 * change le statut de lecture d'un message
	 * @param p_idMessage l'id du message concerné
	 * @param p_isLu true si le message est lu, false si le message est non lu
	 * @return true si ca a reussi
	 */
	public boolean updateStatusLecture(int p_idMessage, boolean p_isLu) {
		String script;
		if (p_isLu) {
			script = "UPDATE " + EnTable.MAIL_RECU.getNomTable()
					+ " SET STATUT=1 WHERE "
					+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
					+ p_idMessage;
		} else {
			script = "UPDATE " + EnTable.MAIL_RECU.getNomTable()
					+ " SET STATUT=0 WHERE "
					+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
					+ p_idMessage;
		}

		return requeteFact.executeRequete(script);

	}

	public List<ArrayList<String>> getMessageById(int p_idMessage) {
		String script = "SELECT "// 
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "," + //
				EnStructMailRecu.UID.getNomChamp() + ", " + //
				EnStructMailRecu.EXPEDITEUR.getNomChamp() + ", " + //
				EnStructMailRecu.DEST.getNomChamp() + ", " + //
				EnStructMailRecu.DEST_COPY.getNomChamp() + ", " + //
				EnStructMailRecu.DEST_CACHE.getNomChamp() + ", " + //
				EnStructMailRecu.SUJET.getNomChamp() + ", " + //
				EnStructMailRecu.CONTENU.getNomChamp() + ", " + //
				EnStructMailRecu.DATE_RECEPTION.getNomChamp() + ", " + //
				EnStructMailRecu.ID_DOSSIER.getNomChamp() + ", " + //
				EnStructMailRecu.ID_COMPTE.getNomChamp() + ", " + //
				EnStructMailRecu.STATUT.getNomChamp() + //
				" FROM " + EnTable.MAIL_RECU.getNomTable() + // 
				" WHERE "// 
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "=" + p_idMessage;

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
		String script = "SELECT " + EnStructDossier.NOM.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable() + " WHERE "
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossierStockage;
		return requeteFact.get1Champ(script);

	}

	public String getNomInternetDossier(int p_idDossierStockage) {
		String script = "SELECT " + EnStructDossier.NOM_INTERNET.getNomChamp()
				+ " FROM " + EnTable.DOSSIER.getNomTable() + " WHERE "
				+ EnStructDossier.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossierStockage;
		return requeteFact.get1Champ(script);

	}

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
		MlListeDossier listeDossier = getListeDossier(p_idCompte);
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
			deleteDossier(p_idCompte, listeDossier.get(i).getIdDossier(),
					p_progressBar);
		}

		String requete = "DELETE FROM " + EnTable.COMPTES.getNomTable()
				+ " WHERE " + EnStructCompte.ID.getNomChamp() + "="
				+ p_idCompte;
		return requeteFact.executeRequete(requete);

	}

	public String getSujetFromId(int p_idMessage) {
		String requette = "SELECT " + EnStructMailRecu.SUJET.getNomChamp()
				+ " FROM " + EnTable.MAIL_RECU.getNomTable() + " WHERE "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "=" + p_idMessage;
		return requeteFact.get1Champ(requette);
	}

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

		return true;// si tout s'est bien passé ou si la liste etait vide
	}

	public boolean updateUIDMessage(MlMessage p_m) {
		String requete = "UPDATE " + EnTable.MAIL_RECU.getNomTable() + " SET "
				+ EnStructMailRecu.UID.getNomChamp() + "='"
				+ p_m.getUIDMessage() + "' WHERE "
				+ EnStructMailRecu.ID_MESSAGE.getNomChamp() + "="
				+ p_m.getIdMessage();
		return requeteFact.executeRequete(requete);

	}

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

	public int getnbMessageParDossier(int p_idCompte, int p_idDossier) {
		String requette = "select count (" + EnStructMailRecu.UID.getNomChamp()
				+ ") FROM " + EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_COMPTE.getNomChamp() + "=" + p_idCompte
				+ " and " + EnStructMailRecu.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossier;
		return Integer.parseInt(requeteFact.get1Champ(requette));

	}

	/**
	 * @param p_idCpt
	 * @return
	 */
	public int getIdInbox(int p_idCpt) {
		String requette = "select " + EnStructDossier.ID_DOSSIER.getNomChamp()
				+ " from " + EnTable.DOSSIER.getNomTable() + " where "
				+ EnStructDossier.NOM.getNomChamp() + "='"
				+ EnDossierBase.RECEPTION.getLib() + "' and "
				+ EnStructDossier.ID_COMPTE.getNomChamp() + "=" + p_idCpt;
		return Integer.parseInt(requeteFact.get1Champ(requette));

	}

	/**
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

	public int getUnreadMessageFromCompte(int p_idCompte) {
		String requette = "SELECT count (*) FROM "
				+ EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_COMPTE.getNomChamp() + "=" + p_idCompte
				+ " and " + EnStructMailRecu.STATUT.getNomChamp() + "='0'";
		return Integer.parseInt(requeteFact.get1Champ(requette));
	}

	public int getUnreadMessageFromFolder(int p_idCompte, int p_idDossier) {
		String requette = "SELECT count (*) FROM "
				+ EnTable.MAIL_RECU.getNomTable() + " where "
				+ EnStructMailRecu.ID_COMPTE.getNomChamp() + "=" + p_idCompte
				+ " and " + EnStructMailRecu.ID_DOSSIER.getNomChamp() + "="
				+ p_idDossier + " and " + EnStructMailRecu.STATUT.getNomChamp()
				+ "='0'";
		int nb = Integer.parseInt(requeteFact.get1Champ(requette));
		for (MlDossier sousDossier : getListeSousDossier(p_idDossier)) {
			nb = nb
					+ getUnreadMessageFromFolder(p_idCompte, sousDossier
							.getIdDossier());
		}
		return nb;
	}

}
