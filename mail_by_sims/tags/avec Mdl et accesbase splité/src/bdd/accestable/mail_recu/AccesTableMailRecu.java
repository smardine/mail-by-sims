/**
 * 
 */
package bdd.accestable.mail_recu;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import mdl.MlPieceJointe;
import tools.GestionRepertoire;
import tools.RecupDate;
import bdd.BDAcces;
import bdd.RequetteFactory;
import bdd.accestable.AccesTable;
import bdd.accestable.piece_jointe.AccesTablePieceJointe;
import bdd.helper.EncodeDecode;

/**
 * @author smardine
 */
public class AccesTableMailRecu extends AccesTable {

	private final Connection laConnexion;
	private final BDAcces bd;
	private final RequetteFactory reqFactory;
	private final AccesTablePieceJointe accesPJ;

	public AccesTableMailRecu() {
		connect();
		bd = AccesTable.bd;
		laConnexion = bd.getConnexion();
		reqFactory = new RequetteFactory(laConnexion);
		accesPJ = new AccesTablePieceJointe();
	}

	public boolean deleteMessageRecu(int p_idMessage) {
		// on commence par effacer les piece jointe associées au message.
		ArrayList<String> lstPieceJointe = accesPJ
				.getListeIdPieceJointe(p_idMessage);
		for (String pieceJointe : lstPieceJointe) {
			String requete = "DELETE FROM PIECE_JOINTE WHERE ID_PIECE_JOINTE='"
					+ pieceJointe + "'";
			reqFactory.executeRequete(requete);
		}
		// on peut ensuite supprimer les messages
		String requetteMessage = "DELETE FROM MAIL_RECU WHERE ID_MESSAGE_RECU='"
				+ p_idMessage + "'";
		return reqFactory.executeRequete(requetteMessage);

	}

	public void createNewMessage(MlMessage m) {
		int idCompte = m.getIdCompte();
		int idDossierStockage = m.getIdDossier();
		String uidMessage = m.getUIDMessage();
		String expediteur = EncodeDecode.encodeHTMLforBase(m.getExpediteur());
		ArrayList<String> listeDestinataire = m.getDestinataire();
		StringBuilder sbDest = new StringBuilder();
		for (String dest : listeDestinataire) {
			sbDest.append(dest);
		}
		String destinataires = sbDest.toString();
		File fileToBlobDestinataires = reqFactory.createFileForBlob(
				destinataires, "destinataires");
		String sujet = EncodeDecode.encodeHTMLforBase(m.getSujet());
		String contenu = m.getContenu();
		File fileToBlobContenu = reqFactory.createFileForBlob(contenu,
				"contenu");
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
		boolean succes = reqFactory.executeRequeteWithBlob(requette.toString(),
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
			String maxId = reqFactory.get1Champ(getMaxId);
			System.out
					.println("l'id de message que l'on vient d'enregistrer est: "
							+ maxId);
			fileToBlobContenu.delete();
			fileToBlobDestinataires.delete();
			// on insere le contenu en base
			// si des pieces jointe sont presente, on enregistre leur chemin en
			// base avec l'id du message
			if (null != m.getListePieceJointe()) {
				for (MlPieceJointe pj : m.getListePieceJointe()) {
					if (accesPJ.enregistrePieceJointe(maxId, pj
							.getContenuPiecejointe())) {
						if (!pj.getContenuPiecejointe().delete()) {
							pj.getContenuPiecejointe().deleteOnExit();
						}
					}
				}
			}
		}
	}

	public ArrayList<String> getListeIDMessage(int p_idCompte,
			int p_idDossierChoisi) {
		String requette = "SELECT a.ID_MESSAGE_RECU FROM MAIL_RECU a where a.ID_COMPTE="
				+ p_idCompte
				+ " and a.ID_DOSSIER_STOCKAGE="
				+ p_idDossierChoisi + " ORDER BY a.DATE_RECEPTION DESC";
		return reqFactory.getListeDeChamp(requette);
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
		return reqFactory.writeBlobToFile(requette, contenuHTML);
	}

	public boolean isMessageLu(int p_idMessageRecu) {
		String script = "SELECT a.STATUT FROM MAIL_RECU a where a.ID_MESSAGE_RECU='"
				+ p_idMessageRecu + "'";
		if ("1".equals(reqFactory.get1Champ(script))) {
			return true;
		}
		return false;
	}

	public boolean setStatusLecture(int p_idMessage) {
		String script = "UPDATE MAIL_RECU a SET STATUT=1 WHERE a.ID_MESSAGE_RECU="
				+ p_idMessage;
		return reqFactory.executeRequete(script);
	}

	public ArrayList<ArrayList<String>> getMessageById(int p_idMessage) {
		String script = "SELECT a.ID_MESSAGE_RECU, a.UID_MESSAGE, a.EXPEDITEUR, a.DESTINATAIRE, "
				+ "a.SUJET, a.DATE_RECEPTION, a.ID_DOSSIER_STOCKAGE,a.ID_COMPTE,a.STATUT"
				+ " FROM MAIL_RECU a WHERE a.ID_MESSAGE_RECU=" + p_idMessage;
		return reqFactory.getListeDenregistrement(script);
	}

	public String getContenuFromId(int p_idMessage) {
		String requette = "SELECT CONTENU FROM MAIL_RECU WHERE ID_MESSAGE_RECU='"
				+ p_idMessage + "'";

		return reqFactory.get1Champ(requette);
	}

	public String getSujetFromId(int p_idMessage) {
		String requette = "SELECT SUJET FROM MAIL_RECU WHERE ID_MESSAGE_RECU="
				+ p_idMessage;
		return reqFactory.get1Champ(requette);
	}

	public boolean deplaceMessageVersCorbeille(MlListeMessage p_list) {
		for (MlMessage m : p_list) {
			MlCompteMail cpt = new MlCompteMail(m.getIdCompte());
			String requete = "UPDATE MAIL_RECU a SET ID_DOSSIER_STOCKAGE="
					+ cpt.getIdCorbeille() + " WHERE a.ID_MESSAGE_RECU="
					+ m.getIdMessage();
			boolean succes = reqFactory.executeRequete(requete);
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
		return reqFactory.executeRequete(requete);
	}

	public int getnbMessageParDossier(int p_idCompte, int p_idDossier) {
		String requette = "select count (a.UID_MESSAGE) FROM MAIL_RECU a where a.ID_COMPTE="
				+ p_idCompte + " and a.ID_DOSSIER_STOCKAGE=" + p_idDossier;
		return Integer.parseInt(reqFactory.get1Champ(requette));
	}

	public boolean verifieAbscenceUID(String uid, int p_idDossier) {
		String requete = "SELECT count (*) from MAIL_RECU a where a.UID_MESSAGE='"
				+ uid.trim() + "' and a.ID_DOSSIER_STOCKAGE=" + p_idDossier;
		return ("0".equals(reqFactory.get1Champ(requete)));

	}

	public boolean verifieAbscenceUID(long p_uid, int p_idDossier) {

		return verifieAbscenceUID("" + p_uid, p_idDossier);
	}

}
