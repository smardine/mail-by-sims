package hotmail.util;

import fenetre.comptes.EnDossierBase;
import imap.util.messageUtilisateur;
import imap.util.methodeImap;
import importMail.thread_Import;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import tools.GestionRepertoire;
import bdd.BDRequette;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;
import com.googlecode.jdeltasync.DeltaSyncException;
import com.googlecode.jdeltasync.Message;

public class methodeHotmail {

	private methodeHotmail() {

	}

	/**
	 * Ouvrir un dossier IMAP (par defaut, la
	 * "boite de reception s'appelle "INBOX"
	 * @param p_store - Store - l'instance de connexion
	 * @param p_fldrName - String - le nom du sossier a ouvrir
	 * @return le dossier IMAP
	 */
	public static Folder getFoler(Store p_store, String p_fldrName) {
		Folder fldr = null;
		try {
			fldr = p_store.getFolder(p_fldrName);
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible de se connecter au dossier " + p_fldrName);

		}
		return fldr;
	}

	public static ArrayList<com.googlecode.jdeltasync.Folder> getSousDossierHotmail(
			DeltaSyncClientHelper p_client) {
		ArrayList<com.googlecode.jdeltasync.Folder> sousDossier = new ArrayList<com.googlecode.jdeltasync.Folder>();
		com.googlecode.jdeltasync.Folder[] listFldr;
		try {
			listFldr = p_client.getFolders();
			for (com.googlecode.jdeltasync.Folder f : listFldr) {

				if (!f.getName().equals("Junk")
						&& !f.getName().equals("Drafts")
						&& !f.getName().equals("Sent")
						&& !f.getName().equals("Deleted")) {
					sousDossier.add(f);
				}
			}
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation des sous dossiers");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation des sous dossiers");
		}

		return sousDossier;
	}

	public static ArrayList<com.googlecode.jdeltasync.Folder> getDossierPrincipaux(
			DeltaSyncClientHelper p_client) {
		ArrayList<com.googlecode.jdeltasync.Folder> dossierPrincipaux = new ArrayList<com.googlecode.jdeltasync.Folder>();
		com.googlecode.jdeltasync.Folder[] listFldr;
		try {
			listFldr = p_client.getFolders();
			for (com.googlecode.jdeltasync.Folder f : listFldr) {

				if (f.getName().equals("Junk")// 
						|| f.getName().equals("Drafts")
						|| f.getName().equals("Sent")
						|| f.getName().equals("Deleted")
						|| f.getName().equals("Inbox")) {
					dossierPrincipaux.add(f);
				}
			}
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation des dossiers principaux");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation des dossiers principaux");
		}

		return dossierPrincipaux;
	}

	public static void miseAJourMessage(Properties props, int pIdCompte,
			JProgressBar p_progress, String host, String user, String password,
			JTextArea textArea, JLabel label) {

		Session session = Session.getInstance(props);
		// Get a Store object
		Store store = null;
		try {
			store = session.getStore("imaps");
			store.connect(host, user, password);
		} catch (Exception e) {
			messageUtilisateur.affMessageException(e, "Erreur connexion");
			return;
		}
		BDRequette bd = new BDRequette();
		ArrayList<String> listeDossier = bd.getListeDossier(pIdCompte);
		for (String dossier : listeDossier) {
			Folder fldr;
			try {
				if (EnDossierBase.RECEPTION.getLib().equals(dossier)) {
					fldr = (Folder) store.getFolder("INBOX");
				} else if (EnDossierBase.BROUILLON.getLib().equals(dossier)) {
					if (store.getFolder("[Gmail]/Drafts").exists()) {
						fldr = (Folder) store.getFolder("[Gmail]/Drafts");
					} else {
						fldr = (Folder) store.getFolder("[Gmail]/Brouillons");
					}
				} else if (EnDossierBase.ENVOYES.getLib().equals(dossier)) {
					if (store.getFolder("[Gmail]/Sent Mail").exists()) {
						fldr = (Folder) store.getFolder("[Gmail]/Sent Mail");
					} else {
						fldr = (Folder) store
								.getFolder("[Gmail]/Messages envoyés");
					}

				} else if (EnDossierBase.SPAM.getLib().equals(dossier)) {
					fldr = (Folder) store.getFolder("[Gmail]/Spam");
				} else if (EnDossierBase.CORBEILLE.getLib().equals(dossier)) {
					if (store.getFolder("[Gmail]/Trash").exists()) {
						fldr = (Folder) store.getFolder("[Gmail]/Trash");
					} else {
						fldr = (Folder) store.getFolder("[Gmail]/Corbeille");
					}

				} else {
					ArrayList<String> lstSousDossierInbox = bd
							.getListeSousDossier(bd
									.getIdDossier(EnDossierBase.RECEPTION
											.getLib(), pIdCompte));
					if (lstSousDossierInbox.contains(dossier)) {
						fldr = (Folder) store.getFolder("INBOX/" + dossier);
					} else {
						fldr = (Folder) store.getFolder(dossier);
					}

				}

				afficheText(textArea, "Ouverture de " + fldr.getFullName());
				fldr.open(Folder.READ_WRITE);
			} catch (MessagingException e) {
				messageUtilisateur.affMessageException(e, "Erreur connexion");
				return;
			}
			afficheText(textArea, "Parcours des messages sur le serveur");
			afficheText(textArea, "à la recherche des messages supprimés");
			MlListeMessage listeMessage = bd.getListeDeMessage(pIdCompte, bd
					.getIdDossier(dossier, pIdCompte));
			int nbActu = 0;
			for (MlMessage m : listeMessage) {
				nbActu++;
				int pourcent = (nbActu * 100) / listeMessage.size();
				p_progress.setValue(pourcent);
				p_progress.setString("Mise a jour messagerie: " + pourcent
						+ " %");
				label.setText("Message traité n° " + nbActu
						+ " sur un total de " + listeMessage.size());
				try {
					// Message messageImap =
					// fldr.getMessageByUID(Long.parseLong(m
					// .getUIDMessage()));
					// if (messageImap == null) {
					// afficheText(textArea,
					// "Message supprimé sur le serveur, mise a jour de la base");
					// BDRequette.deleteMessageRecu(m.getIdMessage());
					// }
				} catch (NumberFormatException e) {
					messageUtilisateur.affMessageException(e,
							"Erreur conversion UID");
				}// catch (MessagingException e) {
				// messageUtilisateur.affMessageException(e,
				// "Erreur connexion");
				// }
			}
			try {
				afficheText(textArea, "Fin de la verification des messages");
				afficheText(textArea, "pour le dossier " + fldr.getFullName());
				fldr.close(false);
			} catch (MessagingException e) {
				messageUtilisateur.affMessageException(e, "Erreur connexion");
			}

		}
		try {
			store.close();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e, "Erreur connexion");
		} finally {
			bd.closeConnexion();
		}

	}

	public static void releveHotmail(int p_idCompte, JProgressBar p_progress,
			JProgressBar p_progressPJ, int p_idDossier, Message[] p_messages,
			com.googlecode.jdeltasync.Folder p_Folder,
			DeltaSyncClientHelper p_client, JTextArea textArea) {

		MlListeMessage lstMessage = new MlListeMessage();

		releveDossier(p_idCompte, p_idDossier, p_progress, p_progressPJ,
				lstMessage, p_messages, p_Folder, p_client, textArea);

	}

	/**
	 * @param p_idCompte
	 * @param p_idDossier
	 * @param p_progress
	 * @param lstMessage
	 * @param p_tableauMessage
	 * @param p_folder
	 * @param textArea
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static void releveDossier(int p_idCompte, int p_idDossier,
			JProgressBar p_progress, JProgressBar p_progressPJ,
			MlListeMessage lstMessage, Message[] p_tableauMessage,
			com.googlecode.jdeltasync.Folder p_folder,
			DeltaSyncClientHelper p_client, JTextArea textArea) {
		afficheText(textArea, "Releve du dossier " + p_folder.getName());
		int count = p_tableauMessage.length;

		afficheText(textArea, "Nombre de messages: " + count);
		// Message numbers start at 1
		int nbActu = 1;
		for (Message messageHotmail : p_tableauMessage) {
			// p_label.setText("Message traité n° " + nbActu++
			// + " sur un total de " + count);

			int pourcent = (nbActu++ * 100) / count;
			p_progress.setValue(pourcent);
			p_progress.setString("Releve de " + p_folder.getName() + " :"
					+ pourcent + " %");
			// on commence par verifier si le message est deja enregistré
			// dans la base
			// pour cela, comme on est en IMAp,
			// on se base sur l'UID du message.
			BDRequette bd = new BDRequette();
			if (bd.verifieAbscenceUID(messageHotmail.getId())) {
				MlMessage messPourBase = new MlMessage();
				messPourBase.setUIDMessage(messageHotmail.getId());
				messPourBase.setCheminPhysique(GestionRepertoire
						.RecupRepTravail()
						+ "/tempo/" + messageHotmail.getId() + ".eml");

				messPourBase = recupContenuMail(p_client, messPourBase,
						messageHotmail, textArea, p_progressPJ);
				// messPourBase.setDateReception(messageHotmail.getDateReceived());
				// ArrayList<String> listeDestinataires;
				// if (null != m.getFrom()getAllRecipients()) {// si on
				// connait la
				// // taille de
				// // la liste, on la fixe
				// listeDestinataires = new ArrayList<String>(m
				// .getAllRecipients().length);
				// for (Address uneAdresse : m.getAllRecipients()) {
				// listeDestinataires.add(uneAdresse.toString());
				// }
				// } else {
				// listeDestinataires = new ArrayList<String>(1);
				// listeDestinataires.add("Destinataire(s) masqué(s)");
				// }

				// messPourBase.setDestinataire(listeDestinataires);
				// messPourBase.setExpediteur(messageHotmail.getFrom());
				messPourBase.setIdCompte(p_idCompte);
				messPourBase.setIdDossier(verifieRegle(messPourBase
						.getExpediteur(), p_idDossier));
				// messPourBase.setUIDMessage("" + messageHotmail.getId());
				// messPourBase.setSujet(messageHotmail.getSubject());
				// lstMessage.add(messPourBase);
				afficheText(textArea, "Enregistrement du message dans la base");
				bd.createNewMessage(messPourBase);

			}
			bd.closeConnexion();

		}

		// "true" actually deletes flagged messages from folder
		afficheText(textArea, "Fermeture du repertoire " + p_folder.getName());

	}

	/**
	 * @param Folder
	 * @param textArea
	 */
	public static void afficheText(JTextArea p_textArea, String p_text) {

		p_textArea.append(p_text + "\n");
		p_textArea.setCaretPosition(p_textArea.getDocument().getLength());
	}

	private static int verifieRegle(String expediteur, int p_idDossier) {
		if (p_idDossier != 0) {
			return p_idDossier;
		}
		return 7;
	}

	public static MlMessage recupContenuMail(DeltaSyncClientHelper client,
			MlMessage p_mlMessage, Message p_messageHotmail,
			JTextArea textArea, JProgressBar p_progressPJ)// ,
	// long
	// p_prefixeNomFichier)//
	{
		// StringBuilder sb = new StringBuilder();
		// int messageNumber = p_messageJavaMail.getMessageNumber();
		// String messageName = p_messageJavaMail.getFileName();
		methodeImap.afficheText(textArea,
				"Téléchargement du contenu du message");
		// Object o;
		try {
			client.downloadMessageContent(p_messageHotmail,
					new FileOutputStream(p_mlMessage.getCheminPhysique()));
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation du mail");
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation du mail");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation du mail");
		}

		p_mlMessage = getMessagePourBase(p_mlMessage, textArea, p_progressPJ);

		return p_mlMessage;
	}

	public static MlMessage getMessagePourBase(MlMessage p_messagePourBase,
			JTextArea p_textArea, JProgressBar p_progressPJ) {

		/** On simule la reception d'un message */
		Properties props = System.getProperties();
		props.put("mail.host", "smtp.dummydomain.com");
		props.put("mail.transport.protocol", "smtp");

		Session mailSession = Session.getDefaultInstance(props, null);
		/***/
		int messNumber = 1;

		String cheminPhysique = p_messagePourBase.getCheminPhysique();
		InputStream source;

		try {
			source = new FileInputStream(cheminPhysique);
			MimeMessage mime;

			mime = new MimeMessage(mailSession, source);
			p_messagePourBase.setSujet(mime.getSubject());
			p_messagePourBase.setDateReception(mime.getSentDate());
			p_messagePourBase.setExpediteur(mime.getFrom()[0].toString());
			// ******************************//
			ArrayList<String> listeDestinataires = new ArrayList<String>(mime
					.getAllRecipients().length);
			for (Address uneAdresse : mime.getAllRecipients()) {
				listeDestinataires.add(uneAdresse.toString());
			}
			p_messagePourBase.setDestinataire(listeDestinataires);
			if (p_messagePourBase.getUIDMessage() == null) {
				if (mime.getContentID() != null) {
					p_messagePourBase.setUIDMessage(mime.getContentID());
				} else {
					p_messagePourBase.setUIDMessage(""
							+ System.currentTimeMillis());// getMessageID());
				}
			}

			/**
			 * il faut decoder le message de maniere a voir si il y a des piece
			 * jointe
			 */

			p_messagePourBase.setContenu(thread_Import.recupContenuMail(
					p_messagePourBase, p_progressPJ, mime, p_textArea));
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la récupération du message");
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la récupération du message");
		}
		return p_messagePourBase;

	}

	public static boolean testBalHotmail(MlCompteMail p_compte) {
		DeltaSyncClientHelper client = new DeltaSyncClientHelper(
				new DeltaSyncClient(), p_compte.getUserName(), p_compte
						.getPassword());
		try {
			client.login();
			client.getInbox();

		} catch (AuthenticationException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur au test de connexion");
			return false;
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur au test de connexion");
			return false;
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur au test de connexion");
			return false;
		}
		return true;

	}

}
