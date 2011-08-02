package imap.util;

import fenetre.comptes.EnDossierBase;
import importMail.thread_Import;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlListeMessage;
import mdl.MlMessage;
import tools.GestionRepertoire;
import tools.Historique;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

public class methodeImap {

	private methodeImap() {

	}

	/**
	 * Ouvrir un dossier IMAP (par defaut, la
	 * "boite de reception s'appelle "INBOX"
	 * @param p_store - Store - l'instance de connexion
	 * @param p_fldrName - String - le nom du sossier a ouvrir
	 * @return le dossier IMAP
	 */
	public static IMAPFolder getImapFoler(Store p_store, String p_fldrName) {
		IMAPFolder fldr = null;
		try {
			fldr = (IMAPFolder) p_store.getFolder(p_fldrName);
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible de se connecter au dossier " + p_fldrName);

		}
		return fldr;
	}

	public static IMAPFolder[] getSousDossierIMAP(Store p_store,
			String p_fldrName) {
		IMAPFolder[] listFldr = null;
		try {
			listFldr = (IMAPFolder[]) p_store.getFolder(p_fldrName).list();
		} catch (MessagingException e) {
			if (e instanceof javax.mail.FolderNotFoundException) {
				messageUtilisateur.affMessageException(e,
						"Pas de sous dossier a ce repertoire " + p_fldrName);
				return null;
			}
			messageUtilisateur.affMessageException(e,
					"Impossible d'obtenir la liste des sous dossiers du repertoire "
							+ p_fldrName);

		}
		return listFldr;
	}

	public static void miseAJourMessage(Properties props, int pIdCompte,
			JProgressBar p_progress, String host, String user, String password,
			JTextArea textArea) {

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
			IMAPFolder fldr;
			try {
				if (EnDossierBase.RECEPTION.getLib().equals(dossier)) {
					fldr = (IMAPFolder) store.getFolder("INBOX");
				} else if (EnDossierBase.BROUILLON.getLib().equals(dossier)) {
					if (store.getFolder("[Gmail]/Drafts").exists()) {
						fldr = (IMAPFolder) store.getFolder("[Gmail]/Drafts");
					} else {
						fldr = (IMAPFolder) store
								.getFolder("[Gmail]/Brouillons");
					}
				} else if (EnDossierBase.ENVOYES.getLib().equals(dossier)) {
					if (store.getFolder("[Gmail]/Sent Mail").exists()) {
						fldr = (IMAPFolder) store
								.getFolder("[Gmail]/Sent Mail");
					} else {
						fldr = (IMAPFolder) store
								.getFolder("[Gmail]/Messages envoyés");
					}

				} else if (EnDossierBase.SPAM.getLib().equals(dossier)) {
					fldr = (IMAPFolder) store.getFolder("[Gmail]/Spam");
				} else if (EnDossierBase.CORBEILLE.getLib().equals(dossier)) {
					if (store.getFolder("[Gmail]/Trash").exists()) {
						fldr = (IMAPFolder) store.getFolder("[Gmail]/Trash");
					} else {
						fldr = (IMAPFolder) store
								.getFolder("[Gmail]/Corbeille");
					}

				} else {
					ArrayList<String> lstSousDossierInbox = bd
							.getListeSousDossier(bd
									.getIdDossier(EnDossierBase.RECEPTION
											.getLib(), pIdCompte));
					if (lstSousDossierInbox.contains(dossier)) {
						fldr = (IMAPFolder) store.getFolder("INBOX/" + dossier);
					} else {
						fldr = (IMAPFolder) store.getFolder(dossier);
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
				// label.setText("Message traité n° " + nbActu
				// + " sur un total de " + listeMessage.size());
				try {
					Message messageImap = fldr.getMessageByUID(Long.parseLong(m
							.getUIDMessage()));
					if (messageImap == null) {
						afficheText(textArea,
								"Message supprimé sur le serveur, mise a jour de la base");
						bd.deleteMessageRecu(m.getIdMessage());
					}
				} catch (NumberFormatException e) {
					messageUtilisateur.affMessageException(e,
							"Erreur conversion UID");
				} catch (MessagingException e) {
					messageUtilisateur.affMessageException(e,
							"Erreur connexion");
				}
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
		}

		bd.closeConnexion();

	}

	public static void releveImap(Properties props, int p_idCompte,
			JProgressBar p_progress, JProgressBar p_progressPJ,
			int p_idDossier, IMAPFolder folder, JTextArea textArea) {

		MlListeMessage lstMessage = new MlListeMessage();

		releveDossier(p_idCompte, p_idDossier, p_progress, p_progressPJ,
				lstMessage, folder, textArea);

	}

	/**
	 * @param p_idCompte
	 * @param p_idDossier
	 * @param p_progress
	 * @param p_progressPJ
	 * @param lstMessage
	 * @param imapFolder
	 * @param textArea
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static void releveDossier(int p_idCompte, int p_idDossier,
			JProgressBar p_progress, JProgressBar p_progressPJ,
			MlListeMessage lstMessage, IMAPFolder imapFolder, JTextArea textArea) {
		afficheText(textArea, "Releve du dossier " + imapFolder.getFullName());
		BDRequette bd = new BDRequette();
		try {
			imapFolder.open(Folder.READ_WRITE);
			int count = imapFolder.getMessageCount();

			afficheText(textArea, "Nombre de messages: " + count);
			// Message numbers start at 1
			int nbActu = 1;
			for (Message m : imapFolder.getMessages()) {
				// p_label.setText("Message traité n° " + nbActu++
				// + " sur un total de " + count);

				int pourcent = (nbActu++ * 100) / count;
				p_progress.setValue(pourcent);
				p_progress.setString("Releve de " + imapFolder.getFullName()
						+ " :" + pourcent + " %");
				// on commence par verifier si le message est deja enregistré
				// dans la base
				// pour cela, comme on est en IMAp,
				// on se base sur l'UID du message.

				if (bd.verifieAbscenceUID(imapFolder.getUID(m))) {
					MlMessage messPourBase = new MlMessage();

					messPourBase.setCheminPhysique(GestionRepertoire
							.RecupRepTravail()
							+ "/tempo/" + System.currentTimeMillis() + ".eml");

					messPourBase.setContenu(thread_Import.recupContenuMail(
							messPourBase, p_progressPJ, m, textArea));
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
							.getExpediteur(), p_idDossier));
					messPourBase.setUIDMessage("" + imapFolder.getUID(m));
					messPourBase.setSujet(m.getSubject());
					lstMessage.add(messPourBase);
					afficheText(textArea,
							"Enregistrement du message dans la base");
					bd.createNewMessage(messPourBase);

				}

			}

			// "true" actually deletes flagged messages from folder
			afficheText(textArea, "Fermeture du repertoire "
					+ imapFolder.getFullName());

			imapFolder.close(false);

		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la releve des messages");
		} finally {
			bd.closeConnexion();
		}

	}

	/**
	 * @param imapFolder
	 * @param textArea
	 */
	public static void afficheText(JTextArea p_textArea, String p_text) {

		p_textArea.append(p_text + "\n");
		p_textArea.setCaretPosition(p_textArea.getDocument().getLength());
		Historique.ecrire(p_text);
	}

	private static int verifieRegle(String expediteur, int p_idDossier) {
		if (p_idDossier != 0) {
			return p_idDossier;
		}
		return 7;
	}

}
