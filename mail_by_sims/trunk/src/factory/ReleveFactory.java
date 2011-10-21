/**
 * 
 */
package factory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlMessage;
import releve.imap.util.methodeImap;
import tools.GestionRepertoire;
import tools.Historique;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import fenetre.comptes.EnTypeCompte;

/**
 * @author smardine
 */
public class ReleveFactory {

	private final MlCompteMail compteMail;

	private Properties props;
	private Session session;
	private Store st;
	private final BDRequette bd;
	private final JProgressBar progressCompte;
	private final JProgressBar progressPJ;
	private final JTextArea textArea;

	public ReleveFactory(MlCompteMail p_cpt, JProgressBar p_progressCompte,
			JProgressBar p_progressPJ, JTextArea p_textArea) {
		this.compteMail = p_cpt;
		this.progressCompte = p_progressCompte;
		this.progressPJ = p_progressPJ;
		this.textArea = p_textArea;
		this.bd = new BDRequette();
	}

	public void releveCourier() throws MessagingException, IOException {

		switch (compteMail.getTypeCompte()) {
			case POP:
				props = System.getProperties();
				session = Session.getDefaultInstance(props, null);
				session.setDebug(true);
				st = session.getStore("pop3");
				if (!st.isConnected()) {
					st.connect(compteMail.getServeurReception(), compteMail
							.getUserName(), compteMail.getPassword());
				}

				releveDossier((POP3Folder) st.getFolder("INBOX"));

				break;
			case GMAIL:
			case IMAP:
				props = System.getProperties();

				props.setProperty("mail.store.protocol", "imaps");
				props.setProperty("mail.imap.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.setProperty("mail.imaps.partialfetch", "false");
				session = Session.getInstance(props);
				st = session.getStore("imaps");
				if (!st.isConnected()) {
					st.connect(compteMail.getServeurReception(), compteMail
							.getUserName(), compteMail.getPassword());
				}

				methodeImap.afficheText(textArea, "Releve des dossiers");
				Historique.ecrireReleveBal(compteMail, "Releve des dossiers");
				for (Folder f : st.getPersonalNamespaces()) {

					for (Folder unSousDossier : getSousDossier(f)) {
						releveDossier((IMAPFolder) unSousDossier);
					}

				}

				break;
			case HOTMAIL:
				break;
		}
		// folder.close(false);
		bd.closeConnexion();
		st.close();
	}

	/**
	 * @param p_folder
	 * @throws MessagingException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void releveDossier(Folder p_folder) throws MessagingException,
			IOException {
		if ("[Gmail]".equals(p_folder.getFullName())) {
			// ce n'est pas vraiment un repertoire,
			// c'est plus un conteneur
			// on recupere juste ses sous dossier et on continue
			for (Folder f : getSousDossier(p_folder)) {
				releveDossier(f);
			}
			return;

		}
		if (!p_folder.isOpen()) {
			p_folder.open(Folder.READ_ONLY);// ouverture de INBOX
		}

		if (compteMail.getTypeCompte() == EnTypeCompte.POP) {
			releveListeMessage(p_folder.getMessages(), p_folder, compteMail
					.getIdInbox());
		} else {
			DossierFactory fact = new DossierFactory(p_folder, compteMail);
			fact.isDossierDejaPresentEnBase();
			int idDossier = fact.getIdDossier();
			Message[] lstMess = checkMessARelever(p_folder, idDossier);
			releveListeMessage(lstMess, p_folder, idDossier);

		}
		if (compteMail.getTypeCompte() != EnTypeCompte.POP) {
			Folder[] lstSousDossier = getSousDossier(p_folder);
			if (lstSousDossier != null) {
				for (Folder f : lstSousDossier) {
					releveDossier(f);
				}
			}
		}

		p_folder.close(false);

	}

	/**
	 * @param p_folder
	 * @throws MessagingException
	 */
	private Message[] checkMessARelever(Folder p_folder, int p_idDossier)
			throws MessagingException {
		int imapcount = p_folder.getMessageCount();
		int messBaseCount = bd.getnbMessageParDossier(compteMail.getIdCompte(),
				p_idDossier);
		int nbMessARelever = imapcount - messBaseCount;

		Historique.ecrireReleveBal(compteMail, "Ouverture du dossier "
				+ p_folder.getFullName());
		Historique.ecrireReleveBal(compteMail,
				"Nombre de messages dans le dossier: " + imapcount);
		Historique.ecrireReleveBal(compteMail, "Nombre de message a relever: "
				+ nbMessARelever);

		if (nbMessARelever < 0) {
			// il y a moin de message sur le serveur qu'en
			// base, il faut faire une synchro
			SynchroFactory synchro = new SynchroFactory(compteMail,
					progressCompte);
			synchro.synchroniseUnDossier(p_folder);
			progressCompte.setValue(100);
			progressCompte.setString(compteMail.getNomCompte() + ": Releve de "
					+ p_folder.getFullName() + " :" + 100 + " %");

			return checkMessARelever(p_folder, p_idDossier);
		}
		Message[] tabMessageARelever = p_folder.getMessages(
				(imapcount - nbMessARelever) + 1, imapcount);
		return tabMessageARelever;
	}

	/**
	 * @param p_idDossier
	 * @throws MessagingException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void releveListeMessage(Message[] p_listeMessages, Folder p_folder,
			int p_idDossier) throws MessagingException, IOException {
		if (p_listeMessages == null) {
			return;
		}
		progressCompte.setValue(0);
		progressCompte.setString(compteMail.getNomCompte() + ": Releve de "
				+ p_folder.getFullName());
		Message[] lstMessages = p_listeMessages;
		int nbActu = 1;
		for (int i = lstMessages.length; i > 0; i--) {
			int pourcent = (nbActu++ * 100) / lstMessages.length;
			progressCompte.setValue(pourcent);
			progressCompte.setString(compteMail.getNomCompte() + ": Releve de "
					+ p_folder.getFullName() + " :" + pourcent + " %");
			Message m = lstMessages[i - 1];
			String uidMessage = getUIdMessage(p_folder, m);
			if (uidMessage == null
					|| bd.isMessageUIDAbsent(uidMessage, p_idDossier)) {
				MlMessage messPourBase = new MlMessage();
				messPourBase.setCheminPhysique(GestionRepertoire
						.RecupRepTravail()
						+ "/tempo/" + System.currentTimeMillis() + ".eml");

				m
						.writeTo(new FileOutputStream(messPourBase
								.getCheminPhysique()));
				MessageFactory fact = new MessageFactory();
				messPourBase = fact.createMessagePourBase(messPourBase,
						textArea, progressPJ);

				messPourBase.setIdCompte(compteMail.getIdCompte());
				RegleCourrierFactory couFact = new RegleCourrierFactory(
						compteMail, m, p_idDossier);
				messPourBase.setIdDossier(couFact.getIdDestinationCourrier());

				if (uidMessage == null) {
					messPourBase.setUIDMessage("");
				} else {
					messPourBase.setUIDMessage(uidMessage);
				}

				methodeImap.afficheText(textArea,
						"Enregistrement du message dans la base");
				bd.createNewMessage(messPourBase);
			}// fin de isMessageUIDAbsent
		}// fin de parcour des message
	}

	/**
	 * @param p_folder
	 * @param m
	 * @return
	 * @throws MessagingException
	 */
	private String getUIdMessage(Folder p_folder, Message m)
			throws MessagingException {
		String uidMessage = null;
		if (p_folder instanceof POP3Folder) {
			uidMessage = ((POP3Folder) p_folder).getUID(m);
		} else if (p_folder instanceof IMAPFolder) {
			uidMessage = "" + ((IMAPFolder) p_folder).getUID(m);
		}

		return uidMessage;
	}

	/**
	 * @param p_folder
	 * @return
	 * @throws MessagingException
	 */
	private Folder[] getSousDossier(Folder p_folder) throws MessagingException {
		Folder[] listFldr = null;
		if (st.getFolder(p_folder.getFullName()).list().length > 0) {
			listFldr = st.getFolder(p_folder.getFullName()).list();
		}
		return listFldr;
	}
}
