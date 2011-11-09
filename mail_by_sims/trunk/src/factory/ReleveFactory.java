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

import mdl.MlCompteMail;
import mdl.MlMessage;
import tools.GestionRepertoire;
import tools.Historique;
import bdd.accesTable.AccesTableMailRecu;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;
import com.googlecode.jdeltasync.DeltaSyncException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import fenetre.Patience;
import fenetre.comptes.EnTypeCompte;

/**
 * Cette classe s'occupe de tout ce qui a trait a la releve d'un compte de
 * messagerie
 * @author smardine
 */
public class ReleveFactory {

	/**
	 * 
	 */
	private static final String RELEVE_DE = "Releve de ";

	private final MlCompteMail compteMail;

	private Store st;
	private final AccesTableMailRecu bd;

	private DeltaSyncClientHelper client;
	private final Patience fenetre;

	/**
	 * Constructeur
	 * @param p_cpt - le compte mail a relever
	 * @param p_fenetre - une barre de progression generale
	 * @param p_progressPJ - une barre de progression secondaire (dediée au
	 *            piece jointe)
	 * @param p_label - pour afficher des infos à l'utlisateur.
	 */
	public ReleveFactory(MlCompteMail p_cpt, Patience p_fenetre) {
		this.compteMail = p_cpt;
		this.fenetre = p_fenetre;
		this.bd = new AccesTableMailRecu();
	}

	/**
	 * lance la releve du courier en fonction du type de compte
	 * @throws MessagingException
	 * @throws IOException
	 * @throws DeltaSyncException
	 */
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
				fenetre.afficheInfo("Releve des dossiers", "", 0);

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

				}
				client.getStore().resetFolders(compteMail.getUserName());
				client.disconnect();
				break;
		}

	}

	/**
	 * Obtenir la liste des dossiers d'un compt email, ATTENTION, ne concerne
	 * que les compte de type {@link EnTypeCompte.GMAIL},
	 * {@link EnTypeCompte.IMAP},{@link EnTypeCompte.HOTMAIL}
	 * @return true si tt s'est bien passé.
	 */
	public boolean recupereListeDossier() {
		StoreFactory storeFact = new StoreFactory(compteMail);
		try {
			st = storeFact.getConnectedStore();
			switch (compteMail.getTypeCompte()) {
				case IMAP:
				case GMAIL:
					Folder[] f = st.getPersonalNamespaces();
					Folder[] lstDossier = getSousDossier(f[0]);
					int count = 0;
					for (Folder unSousDossier : lstDossier) {
						int pourcent = (count++ * 100) / lstDossier.length;
						fenetre.afficheInfo(
								"Création de la liste des dossiers",
								unSousDossier.getFullName() + " :" + pourcent
										+ " %", pourcent);
						recupereDossier((IMAPFolder) unSousDossier);
					}

					st.close();
					break;
				case HOTMAIL:
					client = new DeltaSyncClientHelper(new DeltaSyncClient(),
							compteMail.getUserName(), compteMail.getPassword());
					client.login();
					com.googlecode.jdeltasync.Folder[] lstFolder = client
							.getFolders();
					recupereDossierDelta(lstFolder);

					client.disconnect();
					break;
				case POP:
					break;
			}
		} catch (MessagingException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (AuthenticationException e) {
			return false;
		} catch (DeltaSyncException e) {
			return false;
		}
		return true;

	}

	/**
	 * Recupere un dossier en particulier
	 * @param p_folder - le dossier ciblé.
	 * @throws MessagingException - si une erreur intervient
	 */
	private void recupereDossier(IMAPFolder p_folder) throws MessagingException {

		if ("[Gmail]".equals(p_folder.getFullName())) {
			// ce n'est pas vraiment un repertoire,
			// c'est plus un conteneur
			// on recupere juste ses sous dossier et on continue
			for (Folder f : getSousDossier(p_folder)) {
				recupereDossier((IMAPFolder) f);
			}
			return;
		}
		DossierFactory fact = new DossierFactory(p_folder, compteMail);
		fact.isDossierDejaPresentEnBase();
		Folder[] lstSousDossier = getSousDossier(p_folder);
		if (null != lstSousDossier) {
			for (Folder f : lstSousDossier) {
				recupereDossier((IMAPFolder) f);
			}
		}

	}

	/**
	 * Recuperation d'une liste de dossier dedié au dossier de type
	 * {@link com.googlecode.jdeltasync.Folder}
	 * @param p_lstFolder la liste des dossiers ciblés
	 */
	private void recupereDossierDelta(
			com.googlecode.jdeltasync.Folder[] p_lstFolder) {
		int count = 0;
		for (com.googlecode.jdeltasync.Folder unDossier : p_lstFolder) {
			int pourcent = (count++ * 100) / p_lstFolder.length;
			fenetre.afficheInfo("Recuperation de la liste des dossiers",
					unDossier.getName() + " :" + pourcent + " %", pourcent);
			DossierFactory fact = new DossierFactory(unDossier, compteMail);
			fact.isDossierDejaPresentEnBase();

		}
		client.getStore().resetFolders(compteMail.getUserName());

	}

	/**
	 * recuperation d'un dossier de type
	 * {@link com.googlecode.jdeltasync.Folder}
	 * @param p_folder - le dossier ciblé
	 * @throws DeltaSyncException
	 * @throws IOException
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
	 * Releve les messages d'un dossier de type
	 * {@link com.googlecode.jdeltasync.Folder}
	 * @param p_lstMess - la liste des messages deltasync a relever
	 * @param p_folder - le dossier deltasync concerné
	 * @param p_idDossier - l'id de dossier correspondant en base
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
		fenetre.afficheInfo("Releve liste de messages", compteMail
				.getNomCompte()
				+ ReleveFactory.RELEVE_DE + p_folder.getName(), 0);

		com.googlecode.jdeltasync.Message[] lstMessages = p_lstMess;
		int nbActu = 1;
		for (int i = lstMessages.length; i > 0; i--) {
			int pourcent = (nbActu++ * 100) / lstMessages.length;
			fenetre.afficheInfo("Releve liste de messages", compteMail
					.getNomCompte()
					+ ReleveFactory.RELEVE_DE
					+ p_folder.getName()
					+ " :"
					+ pourcent + " %", pourcent);

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
				messPourBase = fact
						.createMessagePourBase(messPourBase, fenetre);

				messPourBase.setIdCompte(compteMail.getIdCompte());
				RegleCourrierFactory couFact = new RegleCourrierFactory(
						compteMail, messageDelta, p_idDossier);
				messPourBase.setIdDossier(couFact.getIdDestinationCourrier());

				if (uidMessage == null) {
					messPourBase.setUIDMessage("");
				} else {
					messPourBase.setUIDMessage(uidMessage);
				}

				bd.createNewMessage(messPourBase);
			}// fin de isMessageUIDAbsent
		}// fin de parcour des message
	}

	/**
	 * Verification des messages a recuperer lors d'une releve de dossier
	 * @param p_folder - le dossier ciblé
	 * @param p_idDossier - l'id correspondant au dossier en base
	 * @return la liste des messages a relever
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

		if (nbMessARelever < 0) {
			Historique.ecrireReleveBal(compteMail, p_folder.getName(),
					"Mise a jour du dossier necessaire");
			// il y a moin de message sur le serveur qu'en
			// base, il faut faire une synchro
			SynchroFactory synchro = new SynchroFactory(compteMail, fenetre);
			synchro.synchroniseUnDossierDeltaSync(p_folder, lstMessagesHotmail);
			fenetre.afficheInfo(compteMail.getNomCompte(),
					ReleveFactory.RELEVE_DE + p_folder.getName() + " :" + 100
							+ " %", 100);
			return checkMessDeltaARelever(p_folder, p_idDossier);
		}
		if (nbMessARelever > 0) {
			Historique.ecrireReleveBal(compteMail, p_folder.getName(),
					"Ouverture du dossier ");
			Historique.ecrireReleveBal(compteMail, p_folder.getName(),
					"Nombre de messages dans le dossier: "
							+ lstMessagesHotmail.length);
			Historique.ecrireReleveBal(compteMail, p_folder.getName(),
					"Nombre de message a relever: " + nbMessARelever);
		}

		return client.getMessages(p_folder);

	}

	/**
	 * releve un dossier de type {@link Folder} (IMAP ou POP)
	 * @param p_folder - le dossier ciblé
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
	 * Verifie les messages a relever pour un dossier de type {@link Folder }
	 * (IMAP uiquement, pour les compte pop, on releve tout)
	 * @param p_folder - le dossier ciblé
	 * @throws MessagingException
	 */
	private Message[] checkMessARelever(Folder p_folder, int p_idDossier)
			throws MessagingException {
		int imapcount = p_folder.getMessageCount();
		int messBaseCount = bd.getnbMessageParDossier(compteMail.getIdCompte(),
				p_idDossier);
		int nbMessARelever = imapcount - messBaseCount;

		if (nbMessARelever < 0) {
			// il y a moin de message sur le serveur qu'en
			// base, il faut faire une synchro
			Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
					"Mise a jour du dossier necessaire");
			SynchroFactory synchro = new SynchroFactory(compteMail, fenetre);
			synchro.synchroniseUnDossier(p_folder);
			fenetre.afficheInfo(compteMail.getNomCompte(),
					ReleveFactory.RELEVE_DE + p_folder.getFullName() + " :"
							+ 100 + " %", 100);
			return checkMessARelever(p_folder, p_idDossier);
		}
		if (nbMessARelever > 0) {
			Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
					"Ouverture du dossier ");
			Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
					"Nombre de messages dans le dossier: " + imapcount);
			Historique.ecrireReleveBal(compteMail, p_folder.getFullName(),
					"Nombre de message a relever: " + nbMessARelever);
		}
		Message[] tabMessageARelever = p_folder.getMessages(
				(imapcount - nbMessARelever) + 1, imapcount);

		return tabMessageARelever;
	}

	/**
	 * releve les messages d'un dossier de type {@link Folder} (IMAP ou POP)
	 * @param p_idDossier - l'id correspondant au dossier en base
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void releveListeMessage(Message[] p_listeMessages, Folder p_folder,
			int p_idDossier) throws MessagingException, IOException {
		if (p_listeMessages == null) {
			return;
		}

		fenetre.afficheInfo(compteMail.getNomCompte(), ReleveFactory.RELEVE_DE
				+ p_folder.getFullName(), 0);
		Message[] lstMessages = p_listeMessages;

		int nbActu = 1;
		for (int i = lstMessages.length; i > 0; i--) {
			if (!p_folder.isOpen()) {
				p_folder.open(Folder.READ_ONLY);// ouverture de INBOX
			}
			int pourcent = (nbActu++ * 100) / lstMessages.length;
			fenetre.afficheInfo(compteMail.getNomCompte() + " "
					+ ReleveFactory.RELEVE_DE + p_folder.getFullName(),
					"message " + (nbActu - 1) + " / " + lstMessages.length
							+ " " + pourcent + " %", pourcent);

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
				messPourBase = fact
						.createMessagePourBase(messPourBase, fenetre);

				messPourBase.setIdCompte(compteMail.getIdCompte());
				RegleCourrierFactory couFact = new RegleCourrierFactory(
						compteMail, messageJavaMail, p_idDossier);
				messPourBase.setIdDossier(couFact.getIdDestinationCourrier());

				if (uidMessage == null) {
					messPourBase.setUIDMessage("");
				} else {
					messPourBase.setUIDMessage(uidMessage);
				}

				bd.createNewMessage(messPourBase);
			}// fin de isMessageUIDAbsent
		}// fin de parcour des message
	}

	/**
	 * Obtenir l'UID d'un message
	 * @param p_folder - le dossier ciblé
	 * @param p_message - le message a analyser
	 * @return l'uid du message sous forme de string
	 * @throws MessagingException
	 */
	private String getUIdMessage(Folder p_folder, Message p_message)
			throws MessagingException {
		String uidMessage = null;
		if (p_folder instanceof POP3Folder) {
			uidMessage = ((POP3Folder) p_folder).getUID(p_message);
		} else if (p_folder instanceof IMAPFolder) {
			uidMessage = "" + ((IMAPFolder) p_folder).getUID(p_message);
		}

		return uidMessage;
	}

	/**
	 * Obtenir la liste des sous dossiers d'un dossier parent
	 * @param p_folder - le dossier parent
	 * @return la liste des sous dossier
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
