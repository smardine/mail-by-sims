package Pop3.util;

import imap.util.methodeImap;
import importMail.thread_Import;

import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import tools.GestionRepertoire;
import bdd.BDRequette;

import com.sun.mail.pop3.POP3Folder;

public class methodePop {

	private static MlCompteMail comptePop;
	private static POP3Folder folder;
	private static JTextArea text;
	private static JProgressBar progressReleve;
	private static JProgressBar progressPJ;

	private methodePop() {

	}

	public static void releveCompte(MlCompteMail p_comptePop,
			POP3Folder p_folder, JTextArea p_textArea, JProgressBar p_progress,
			JProgressBar p_progressPJ) {
		BDRequette bd = new BDRequette();
		methodePop.comptePop = p_comptePop;
		methodePop.folder = p_folder;
		methodePop.text = p_textArea;
		methodePop.progressReleve = p_progress;
		methodePop.progressPJ = p_progressPJ;
		// lecture des messages
		try {
			int count = folder.getMessageCount();

			methodeImap.afficheText(p_textArea, "Nombre de messages: " + count);
			// Message numbers start at 1
			int nbActu = 1;
			MlListeMessage lstMessage = new MlListeMessage();
			Message[] lstMessagesPop = folder.getMessages();
			for (Message m : lstMessagesPop) {

				int pourcent = (nbActu++ * 100) / count;
				progressReleve.setValue(pourcent);
				progressReleve.setString("Releve de " + folder.getFullName()
						+ " :" + pourcent + " %");

				if (bd.verifieAbscenceUID(folder.getUID(m))) {
					MlMessage messPourBase = new MlMessage();

					messPourBase.setCheminPhysique(GestionRepertoire
							.RecupRepTravail()
							+ "/tempo/" + System.currentTimeMillis() + ".eml");

					messPourBase.setContenu(thread_Import.recupContenuMail(
							messPourBase, p_progressPJ, m, text));
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
					messPourBase.setIdCompte(p_comptePop.getIdCompte());
					messPourBase.setIdDossier(verifieRegle(messPourBase
							.getExpediteur(), p_comptePop.getIdInbox()));
					messPourBase.setUIDMessage("" + folder.getUID(m));
					messPourBase.setSujet(m.getSubject());
					lstMessage.add(messPourBase);
					methodeImap.afficheText(text,
							"Enregistrement du message dans la base");
					bd.createNewMessage(messPourBase);
				}

			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bd.closeConnexion();
	}

	private static int verifieRegle(String expediteur, int p_idDossier) {
		if (p_idDossier != 0) {
			return p_idDossier;
		}
		return 7;
	}
}
