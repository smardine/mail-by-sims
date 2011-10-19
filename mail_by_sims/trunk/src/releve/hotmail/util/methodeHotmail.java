package releve.hotmail.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;
import releve.imap.util.methodeImap;
import tools.GestionRepertoire;
import bdd.BDRequette;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;
import com.googlecode.jdeltasync.DeltaSyncException;
import com.googlecode.jdeltasync.Message;

import factory.MessageFactory;

public final class methodeHotmail {
	private final static String TAG = "methodeHotmail";

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
			messageUtilisateur.affMessageException(TAG, e,
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
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation des sous dossiers");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
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

				if ("Junk".equals(f.getName())// 
						|| "Drafts".equals(f.getName())
						|| "Sent".equals(f.getName())
						|| "Deleted".equals(f.getName())) {
					dossierPrincipaux.add(f);
				}
				if ("Inbox".equals(f.getName())) {
					dossierPrincipaux.add(f);
				}
			}
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation des dossiers principaux");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation des dossiers principaux");
		}

		return dossierPrincipaux;
	}

	public static void releveHotmail(int p_idCompte, JProgressBar p_progress,
			JProgressBar p_progressPJ, int p_idDossier, Message[] p_messages,
			com.googlecode.jdeltasync.Folder p_Folder,
			DeltaSyncClientHelper p_client, JTextArea textArea) {

		// MlListeMessage lstMessage = new MlListeMessage();

		releveDossier(p_idCompte, p_idDossier, p_progress, p_progressPJ,
		// lstMessage,
				p_messages, p_Folder, p_client, textArea);

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
			Message[] p_tableauMessage,
			com.googlecode.jdeltasync.Folder p_folder,
			DeltaSyncClientHelper p_client, JTextArea textArea) {
		afficheText(textArea, "Releve du dossier " + p_folder.getName());
		int count = p_tableauMessage.length;
		MlCompteMail compteMail = new MlCompteMail(p_idCompte);
		afficheText(textArea, "Nombre de messages: " + count);
		// Message numbers start at 1
		int nbActu = 1;
		for (Message messageHotmail : p_tableauMessage) {

			int pourcent = (nbActu++ * 100) / count;
			p_progress.setValue(pourcent);
			p_progress.setString(compteMail.getNomCompte() + ": Releve de "
					+ p_folder.getName() + " :" + pourcent + " %");

			BDRequette bd = new BDRequette();
			if (bd.verifieAbscenceUID(messageHotmail.getId(), p_idDossier)) {
				MlMessage messPourBase = new MlMessage();
				messPourBase.setUIDMessage(messageHotmail.getId());
				messPourBase.setCheminPhysique(GestionRepertoire
						.RecupRepTravail()
						+ "/tempo/" + messageHotmail.getId() + ".eml");

				messPourBase = recupContenuMail(p_client, messPourBase,
						messageHotmail, textArea, p_progressPJ);

				messPourBase.setIdCompte(p_idCompte);
				messPourBase.setIdDossier(verifieRegle(messPourBase
						.getExpediteur(), p_idDossier));

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
			JTextArea textArea, JProgressBar p_progressPJ) {
		MlMessage aMessage = p_mlMessage;
		methodeImap.afficheText(textArea,
				"Téléchargement du contenu du message");
		// Object o;
		try {
			client.downloadMessageContent(p_messageHotmail,
					new FileOutputStream(aMessage.getCheminPhysique()));
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation du mail");
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation du mail");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation du mail");
		}
		MessageFactory fact = new MessageFactory();
		aMessage = fact.createMessagePourBase(aMessage, textArea, p_progressPJ);

		return aMessage;
	}

	public static void miseAJourMessagerie(BDRequette p_bd,
			DeltaSyncClientHelper p_client, MlCompteMail p_compteMail,
			JProgressBar p_progressBar, JProgressBar p_progressBarPJ,
			JTextArea p_textArea) {

		try {
			p_client.login();
			com.googlecode.jdeltasync.Folder[] lstFolder = p_client
					.getFolders();
			for (com.googlecode.jdeltasync.Folder f : lstFolder) {
				afficheText(p_textArea, "Verification du dossier: "
						+ f.getName());
				int idDossier = getIdDossierPrincipaux(p_compteMail, f);
				if (idDossier == -1) {
					idDossier = p_bd.getIdDossier(f.getName(), p_compteMail
							.getIdCompte());
				}

				if (idDossier != -1) {
					Message[] lstMessagesHotmail = p_client.getMessages(f);
					int messBaseCount = p_bd.getnbMessageParDossier(
							p_compteMail.getIdCompte(), idDossier);
					int nbMessARelever = lstMessagesHotmail.length
							- messBaseCount;
					if (nbMessARelever < 0) {// il y a + de message en base que
						// sur
						// le serveur, on analyse fichier
						// par fichier
						MlListeMessage lstMessageBase = p_bd.getListeDeMessage(
								p_compteMail.getIdCompte(), idDossier);
						int nbActu = 1;
						for (MlMessage m : lstMessageBase) {
							int pourcent = (nbActu++ * 100)
									/ lstMessageBase.size();
							p_progressBar.setValue(pourcent);
							p_progressBar.setString(p_compteMail.getNomCompte()
									+ ": Vérif de " + f.getName() + " :"
									+ pourcent + " %");

							if (!verifUID(m.getUIDMessage(), lstMessagesHotmail)) {
								p_bd.deleteMessageRecu(m.getIdMessage());
								afficheText(p_textArea,
										"Message suprimé du serveur, effacement du message de la base");
							}
						}

					}
				}

			}

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeltaSyncException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static boolean verifUID(String p_uidMessage,
			Message[] p_lstMessagesHotmail) {
		for (Message m : p_lstMessagesHotmail) {
			if (p_uidMessage.equals(m.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param cpt
	 * @param p_f
	 * @param idDossier
	 * @return
	 */
	public static int getIdDossierPrincipaux(MlCompteMail cpt,
			com.googlecode.jdeltasync.Folder p_f) {
		int idDossier = -1;
		if ("Inbox".equals(p_f.getName())) {
			idDossier = cpt.getIdInbox();
		} else if ("Junk".equals(p_f.getName())) {
			idDossier = cpt.getIdSpam();
		} else if ("Drafts".equals(p_f.getName())) {
			idDossier = cpt.getIdBrouillons();
		} else if ("Sent".equals(p_f.getName())) {
			idDossier = cpt.getIdEnvoye();
		} else if ("Deleted".equals(p_f.getName())) {
			idDossier = cpt.getIdCorbeille();
		}
		return idDossier;
	}

}
