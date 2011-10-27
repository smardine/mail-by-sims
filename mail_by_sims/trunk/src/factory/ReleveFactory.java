/**
 * 
 */
package factory;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;
import tools.GestionRepertoire;
import tools.Historique;
import bdd.BDRequette;

import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;
import com.googlecode.jdeltasync.DeltaSyncException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import fenetre.comptes.EnTypeCompte;

/**
 * @author smardine
 */
public class ReleveFactory {

	/**
	 * 
	 */
	private static final String RELEVE_DE = ": Releve de ";

	private final MlCompteMail compteMail;

	private Store st;
	private final BDRequette bd;
	private final JProgressBar progressCompte;
	private final JProgressBar progressPJ;
	private final JTextArea textArea;

	private DeltaSyncClientHelper client;

	public ReleveFactory(MlCompteMail p_cpt, JProgressBar p_progressCompte,
			JProgressBar p_progressPJ, JTextArea p_textArea) {
		this.compteMail = p_cpt;
		this.progressCompte = p_progressCompte;
		this.progressPJ = p_progressPJ;
		this.textArea = p_textArea;
		this.bd = new BDRequette();
	}

	public void releveCourier() throws MessagingException, IOException,
			DeltaSyncException {
		StoreFactory storeFact = new StoreFactory(compteMail);
		st = storeFact.getConnectedStore();
		switch (compteMail.getTypeCompte()) {
			case POP:
				releveDossier((POP3Folder) st.getFolder("INBOX"));
				st.close();
				break;
			case GMAIL:
			case IMAP:
				messageUtilisateur.afficheText(textArea, "Releve des dossiers");

				for (Folder f : st.getPersonalNamespaces()) {
					for (Folder unSousDossier : getSousDossier(f)) {
						releveDossier((IMAPFolder) unSousDossier);
					}
				}
				st.close();

				break;
			case HOTMAIL:
				client = new DeltaSyncClientHelper(new DeltaSyncClient(),
						compteMail.getUserName(), compteMail.getPassword());
				client.login();
				com.googlecode.jdeltasync.Folder[] lstFolder = client
						.getFolders();
				for (com.googlecode.jdeltasync.Folder unDossier : lstFolder) {
					releveDossierDeltaSync(unDossier);
					client.getStore().resetFolders(compteMail.getUserName());
				}
				client.disconnect();
				break;
		}
		bd.closeConnexion();
	}

	/**
	 * @param p_folder
	 * @throws IOException
	 * @throws DeltaSyncException
	 */
	private void releveDossierDeltaSync(
			com.googlecode.jdeltasync.Folder p_folder)
			throws DeltaSyncException, IOException {
		DossierFactory fact = new DossierFactory(p_folder, compteMail);
		fact.isDossierDejaPresentEnBase();
		int idDossier = fact.getIdDossier();

		com.googlecode.jdeltasync.Message[] lstMess = checkMessDeltaARelever(
				p_folder, idDossier);
		releveListeMessageDelta(lstMess, p_folder, idDossier);
	}

	/**
	 * @param p_lstMess
	 * @param p_folder
	 * @param p_idDossier
	 * @throws IOException
	 * @throws DeltaSyncException
	 */
	private void releveListeMessageDelta(
			com.googlecode.jdeltasync.Message[] p_lstMess,
			com.googlecode.jdeltasync.Folder p_folder, int p_idDossier)
			throws DeltaSyncException, IOException {
		if (p_lstMess == null) {
			return;
		}
		progressCompte.setValue(0);
		progressCompte.setString(compteMail.getNomCompte()
				+ ReleveFactory.RELEVE_DE + p_folder.getName());
		com.googlecode.jdeltasync.Message[] lstMessages = p_lstMess;
		int nbActu = 1;
		for (int i = lstMessages.length; i > 0; i--) {
			int pourcent = (nbActu++ * 100) / lstMessages.length;
			progressCompte.setValue(pourcent);
			progressCompte.setString(compteMail.getNomCompte()
					+ ReleveFactory.RELEVE_DE + p_folder.getName() + " :"
					+ pourcent + " %");
			com.googlecode.jdeltasync.Message messageDelta = lstMessages[i - 1];
			String uidMessage = messageDelta.getId();
			if (uidMessage == null
					|| bd.isMessageUIDAbsent(uidMessage, p_idDossier)) {
				MlMessage messPourBase = new MlMessage();
				messPourBase.setCheminPhysique(GestionRepertoire
						.RecupRepTravail()
						+ "/tempo/" + System.currentTimeMillis() + ".eml");
				client.downloadMessageContent(messageDelta,
						new FileOutputStream(messPourBase.getCheminPhysique()));

				MessageFactory fact = new MessageFactory();
				messPourBase = fact.createMessagePourBase(messPourBase,
						textArea, progressPJ);

				messPourBase.setIdCompte(compteMail.getIdCompte());
				RegleCourrierFactory couFact = new RegleCourrierFactory(
						compteMail, messageDelta, p_idDossier);
				messPourBase.setIdDossier(couFact.getIdDestinationCourrier());

				if (uidMessage == null) {
					messPourBase.setUIDMessage("");
				} else {
					messPourBase.setUIDMessage(uidMessage);
				}

				messageUtilisateur.afficheText(textArea,
						"Enregistrement du message dans la base");
				bd.createNewMessage(messPourBase);
			}// fin de isMessageUIDAbsent
		}// fin de parcour des message
	}

	/**
	 * @param p_folder
	 * @param p_idDossier
	 * @return
	 * @throws IOException
	 * @throws DeltaSyncException
	 */
	private com.googlecode.jdeltasync.Message[] checkMessDeltaARelever(
			com.googlecode.jdeltasync.Folder p_folder, int p_idDossier)
			throws DeltaSyncException, IOException {
		com.googlecode.jdeltasync.Message[] lstMessagesHotmail = client
				.getMessages(p_folder);
		int messBaseCount = bd.getnbMessageParDossier(compteMail.getIdCompte(),
				p_idDossier);
		int nbMessARelever = lstMessagesHotmail.length - messBaseCount;
		Historique.ecrireReleveBal(compteMail, p_folder.getName(),
				"Ouverture du dossier ");
		Historique.ecrireReleveBal(compteMail, p_folder.getName(),
				"Nombre de messages dans le dossier: "
						+ lstMessagesHotmail.length);
		Historique.ecrireReleveBal(compteMail, p_folder.getName(),
				"Nombre de message a relever: " + nbMessARelever);

		if (nbMessARelever < 0) {
			// il y a moin de message sur le serveur qu'en
			// base, il faut faire une synchro
			SynchroFactory synchro = new SynchroFactory(compteMail,
					progressCompte);
			synchro.synchroniseUnDossierDeltaSync(p_folder, lstMessagesHotmail);
			progressCompte.setValue(100);
			progressCompte.setString(compteMail.getNomCompte()
					+ ReleveFactory.RELEVE_DE + p_folder.getName() + " :" + 100
					+ " %");

			return checkMessDeltaARelever(p_folder, p_idDossier);
		}

		return client.getMessages(p_folder);

	}

	/**
	 * @param p_folder
	 * @throws MessagingException
	 * @throws IOException
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

		Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
				"Ouverture du dossier ");
		Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
				"Nombre de messages dans le dossier: " + imapcount);

		if (nbMessARelever < 0) {
			// il y a moin de message sur le serveur qu'en
			// base, il faut faire une synchro
			Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
					"Mise a jour du dossier necessaire");
			SynchroFactory synchro = new SynchroFactory(compteMail,
					progressCompte);
			synchro.synchroniseUnDossier(p_folder);
			progressCompte.setValue(100);
			progressCompte.setString(compteMail.getNomCompte()
					+ ReleveFactory.RELEVE_DE + p_folder.getFullName() + " :"
					+ 100 + " %");

			return checkMessARelever(p_folder, p_idDossier);
		}
		Message[] tabMessageARelever = p_folder.getMessages(
				(imapcount - nbMessARelever) + 1, imapcount);
		Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
				"Nombre de message a relever: " + nbMessARelever);
		return tabMessageARelever;
	}

	/**
	 * @param p_idDossier
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void releveListeMessage(Message[] p_listeMessages, Folder p_folder,
			int p_idDossier) throws MessagingException, IOException {
		if (p_listeMessages == null) {
			return;
		}
		progressCompte.setValue(0);
		progressCompte.setString(compteMail.getNomCompte()
				+ ReleveFactory.RELEVE_DE + p_folder.getFullName());
		Message[] lstMessages = p_listeMessages;
		int nbActu = 1;
		for (int i = lstMessages.length; i > 0; i--) {
			int pourcent = (nbActu++ * 100) / lstMessages.length;
			progressCompte.setValue(pourcent);
			progressCompte.setString(compteMail.getNomCompte()
					+ ReleveFactory.RELEVE_DE + p_folder.getFullName() + " :"
					+ pourcent + " %");
			Message messageJavaMail = lstMessages[i - 1];
			String uidMessage = getUIdMessage(p_folder, messageJavaMail);
			if (uidMessage == null
					|| bd.isMessageUIDAbsent(uidMessage, p_idDossier)) {
				MlMessage messPourBase = new MlMessage();
				// messPourBase.setUIDMessage(uidMessage);
				messPourBase.setCheminPhysique(GestionRepertoire
						.RecupRepTravail()
						+ "/tempo/" + System.currentTimeMillis() + ".eml");

				messageJavaMail.writeTo(new FileOutputStream(messPourBase
						.getCheminPhysique()));
				MessageFactory fact = new MessageFactory();
				messPourBase = fact.createMessagePourBase(messPourBase,
						textArea, progressPJ);

				messPourBase.setIdCompte(compteMail.getIdCompte());
				RegleCourrierFactory couFact = new RegleCourrierFactory(
						compteMail, messageJavaMail, p_idDossier);
				messPourBase.setIdDossier(couFact.getIdDestinationCourrier());

				if (uidMessage == null) {
					messPourBase.setUIDMessage("");
				} else {
					messPourBase.setUIDMessage(uidMessage);
				}

				messageUtilisateur.afficheText(textArea,
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
