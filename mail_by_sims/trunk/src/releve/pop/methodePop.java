package releve.pop;

import importMail.thread_Import;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.imap.util.methodeImap;
import tools.GestionRepertoire;
import tools.Historique;
import bdd.BDRequette;

import com.sun.mail.pop3.POP3Folder;

public final class methodePop {

	private static MlCompteMail comptePop;
	private static POP3Folder folder;
	private static JTextArea text;
	private static JProgressBar progressReleve;
	@SuppressWarnings("unused")
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
			Historique.ecrireReleveBal(comptePop, "Nombre de messages: "
					+ count);
			// Message numbers start at 1
			int nbActu = 1;
			MlListeMessage lstMessage = new MlListeMessage();
			Message[] lstMessagesPop = folder.getMessages();

			for (int i = lstMessagesPop.length; i > 0; i--) {
				int pourcent = (nbActu++ * 100) / count;
				progressReleve.setValue(pourcent);
				progressReleve.setString(p_comptePop.getNomCompte()
						+ ": Releve de " + folder.getFullName() + " :"
						+ pourcent + " %");
				Message m = lstMessagesPop[i - 1];
				if (bd.verifieAbscenceUID(folder.getUID(m), p_comptePop
						.getIdInbox())) {
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

	public static boolean testBalPop(MlCompteMail p_compte) {
		Properties prop = System.getProperties();
		Session sess = Session.getDefaultInstance(prop, null);
		sess.setDebug(true);
		Store st = null;
		try {
			st = sess.getStore("pop3");
			st.connect(p_compte.getServeurReception(), p_compte.getUserName(),
					p_compte.getPassword());
			// System.out.println("st=:" + st);
			// System.out.println("Obtention d'un folder");
			POP3Folder f = (POP3Folder) st.getFolder("INBOX");
			f.open(Folder.READ_ONLY);
			// int nbMessageDansInbox = f.getMessageCount();
			f.close(false);
		} catch (NoSuchProviderException e) {
			// erreur de protocle
			return false;
		} catch (MessagingException e) {
			// erreur de connexion
			return false;
		} finally {
			try {
				st.close();
			} catch (MessagingException e) {
				return false;
			}
		}

		return true;
	}
}
