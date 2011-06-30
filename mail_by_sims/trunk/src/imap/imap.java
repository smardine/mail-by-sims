/**
 * 
 */
package imap;

import imap.util.messageUtilisateur;
import importMail.MlListeMessage;
import importMail.MlMessage;
import importMail.thread_Import;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JEditorPane;

import tools.GestionRepertoire;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

import fenetre.comptes.EnDossierBase;
import fenetre.principale.jTable.MyTableModel;

/**
 * @author smardine
 */
public class imap {
	protected MyTableModel tablemodel;
	protected JEditorPane editor;
	private final String idCompte;
	private static String user;
	private static String password;
	private static String host;
	private final static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private static boolean isImap;

	public imap(MyTableModel model, String p_idCompte, String p_user,
			String p_password, String p_host, boolean isImap) {
		this.tablemodel = model;
		this.user = p_user;
		this.password = p_password;
		this.host = p_host;
		this.idCompte = p_idCompte;
		this.isImap = isImap;
		imap.main(model, editor, idCompte);
	}

	/**
	 * @param args
	 */

	public static void main(MyTableModel model, JEditorPane editor,
			String p_idCompte) {
		// TODO Auto-generated method stub

		// SUBSTITUTE YOUR ISP's POP3 SERVER HERE!!!
		// String host = "pop3.club-internet.fr";
		// // SUBSTITUTE YOUR USERNAME AND PASSWORD TO ACCESS E-MAIL HERE!!!
		// String user = "sims26";
		// String password = "gouranga";
		// SUBSTITUTE YOUR SUBJECT SUBSTRING TO SEARCH HERE!!!
		// String subjectSubstringToSearch = "iphondroid";

		// Get a session. Use a blank Properties object.

		Properties props = System.getProperties();

		// Get a Store object

		// Get "INBOX"

		if (isImap) {
			releveImap(props, p_idCompte);
			// thread_Import.enregistreMessageEnBase(lstMess);
		} else {
			// relevePop();
			// fldr = store.getFolder("INBOX");
		}

	}

	private static MlListeMessage releveImap(Properties props, String p_idCompte) {

		MlListeMessage lstMessage = new MlListeMessage();
		try {
			props.setProperty("mail.store.protocol", "imaps");
			props.setProperty("mail.imap.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			props.setProperty("mail.imaps.partialfetch", "false");
			Session session = Session.getInstance(props);
			// Get a Store object
			Store store = session.getStore("imaps");
			store.connect(host, user, password);

			IMAPFolder fldr = (IMAPFolder) store.getFolder("INBOX");
			fldr.open(Folder.READ_WRITE);
			int count = fldr.getMessageCount();
			System.out.println(count + " total messages");
			String id_Dossier = BDRequette.getIdDossier(EnDossierBase.RECEPTION
					.getLib(), p_idCompte);

			// Message numbers start at 1
			int nbActu = 1;
			for (Message m : fldr.getMessages()) {
				System.out.println("message " + nbActu++ + "sur un total de "
						+ count);
				// on commence par verifier si le message est deja enregistré
				// dans la base
				// pour cela, comme on est en IMAp,
				// on se base sur l'UID du message.

				if (BDRequette.verifieAbscenceUID(fldr.getUID(m))) {
					MlMessage messPourBase = new MlMessage();

					messPourBase.setCheminPhysique(GestionRepertoire
							.RecupRepTravail()
							+ "/tempo/" + System.currentTimeMillis() + ".eml");
					messPourBase.setContenu(thread_Import.recupContenuMail(
							messPourBase, m));
					messPourBase.setDateReception(m.getReceivedDate());
					ArrayList<String> listeDestinataires;
					if (null != m.getAllRecipients()) {// si on connait la
						// taille de
						// la liste, on la fixe
						listeDestinataires = new ArrayList<String>(m
								.getAllRecipients().length);
						for (Address uneAdresse : m.getAllRecipients()) {
							listeDestinataires.add(uneAdresse.toString());
						}
					} else {
						listeDestinataires = new ArrayList<String>(1);
						listeDestinataires.add("Destinataire(s) masqué(s)");
					}

					messPourBase.setDestinataire(listeDestinataires);
					messPourBase.setExpediteur(m.getFrom()[0].toString());
					messPourBase.setIdCompte(p_idCompte);
					messPourBase.setIdDossier(verifieRegle(messPourBase
							.getExpediteur(), id_Dossier));
					messPourBase.setUIDMessage("" + fldr.getUID(m));
					messPourBase.setSujet(m.getSubject());
					lstMessage.add(messPourBase);
					BDRequette.createNewMessage(messPourBase);
				} else {
					System.out.println("message uid " + fldr.getUID(m)
							+ " deja enregistré en base");
				}

			}

			// "true" actually deletes flagged messages from folder
			fldr.close(false);
			store.close();
			System.out.println("close");

		} catch (MessagingException mex) {
			// Prints all nested (chained) exceptions as well
			messageUtilisateur.affMessageException(mex, "Message Exception");
		} catch (IOException ioex) {
			messageUtilisateur.affMessageException(ioex,
					"erreur lors de la releve des messages");
		}
		return lstMessage;

	}

	private static String verifieRegle(String expediteur, String p_idDossier) {
		if (p_idDossier != null) {
			return p_idDossier;
		}
		return "7";
	}

}
