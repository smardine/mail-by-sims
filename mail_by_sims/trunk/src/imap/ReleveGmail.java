/**
 * 
 */
package imap;

import fenetre.comptes.EnDossierBase;
import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

/**
 * @author smardine
 */
public class ReleveGmail {

	private final int idCompte;
	private final JProgressBar progressBar;
	private static JLabel label;
	private static JTextArea textArea;
	private static String user;
	private static String password;
	private static String host;

	public ReleveGmail(int p_idCompte, String p_user, String p_password,
			String p_host, JProgressBar progress, JTextArea textArea,
			JLabel label) {

		ReleveGmail.user = p_user;
		ReleveGmail.password = p_password;
		ReleveGmail.host = p_host;
		this.idCompte = p_idCompte;
		this.progressBar = progress;
		ReleveGmail.textArea = textArea;
		ReleveGmail.label = label;
		ReleveGmail.go(idCompte, progressBar);
	}

	/**
	 * @param args
	 */

	public static void go(int p_idCompte, JProgressBar p_progress) {

		Properties props = System.getProperties();

		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imaps.partialfetch", "false");

		// on commence par verifier les messages suprrimés car sinon, on pert du
		// temp a le faire apres une releve de la BAL
		methodeImap.afficheText(textArea, "Mise a jour de la boite Gmail");
		methodeImap.afficheText(textArea,
				"Synchronisation des messages supprimés");
		methodeImap.miseAJourMessage(props, p_idCompte, p_progress, host, user,
				password, textArea, label);

		Session session = Session.getInstance(props);

		// Get a Store object
		Store store = null;
		try {
			store = session.getStore("imaps");
			store.connect(host, user, password);
		} catch (NoSuchProviderException e) {
			messageUtilisateur.affMessageException(e, "Erreur de connexion");
			return;
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e, "Erreur de connexion");
			return;
		}
		// releve de la boite de reception
		IMAPFolder inbox = null;
		try {
			methodeImap.afficheText(textArea,
					"Ouverture de la boite de reception");
			inbox = (IMAPFolder) store.getFolder("INBOX");
		} catch (MessagingException e1) {
			messageUtilisateur.affMessageException(e1,
					"Impossible d'acceder à la boite de reception");
		}
		int id_Dossier;
		if (inbox != null) {
			id_Dossier = BDRequette.getIdDossier(EnDossierBase.RECEPTION
					.getLib(), p_idCompte);

			methodeImap.releveImap(props, p_idCompte, p_progress, id_Dossier,
					inbox, textArea, label);
			// on recupere ensuite les sous dossiers de la boite de reception
			IMAPFolder[] lstFolder = methodeImap.getSousDossierIMAP(store,
					inbox.getFullName());

			for (IMAPFolder folder : lstFolder) {
				int idSousDossier = BDRequette.getIdDossier(folder.getName(),
						p_idCompte);
				if (idSousDossier == (-1)) {// si le sous dossier est inconnu
					// de la base, on en créer un
					BDRequette.createNewDossier(p_idCompte, id_Dossier, folder
							.getName());
					idSousDossier = BDRequette.getIdDossier(folder.getName(),
							p_idCompte);
				}
				methodeImap.releveImap(props, p_idCompte, p_progress,
						idSousDossier, folder, textArea, label);

			}
		}

		// recuperation des autres dossiers:envoyé, brouillons, spam, corbeille
		IMAPFolder[] lstFolder = methodeImap.getSousDossierIMAP(store,
				"[Gmail]");

		for (IMAPFolder folder : lstFolder) {
			if (folder.getFullName().equals("[Gmail]/Drafts") || //
					folder.getFullName().equals("[Gmail]/Brouillons")) {
				id_Dossier = BDRequette.getIdDossier(EnDossierBase.BROUILLON
						.getLib(), p_idCompte);
				methodeImap.releveImap(props, p_idCompte, p_progress,
						id_Dossier, folder, textArea, label);
			} else if (folder.getFullName().equals("[Gmail]/Sent Mail") || //
					folder.getFullName().equals("[Gmail]/Messages envoyés")) {
				id_Dossier = BDRequette.getIdDossier(EnDossierBase.ENVOYES
						.getLib(), p_idCompte);
				methodeImap.releveImap(props, p_idCompte, p_progress,
						id_Dossier, folder, textArea, label);
			} else if (folder.getFullName().equals("[Gmail]/Spam")) {
				id_Dossier = BDRequette.getIdDossier(EnDossierBase.SPAM
						.getLib(), p_idCompte);
				methodeImap.releveImap(props, p_idCompte, p_progress,
						id_Dossier, folder, textArea, label);
			} else if (folder.getFullName().equals("[Gmail]/Trash") || //
					folder.getFullName().equals("[Gmail]/Corbeille")) {
				id_Dossier = BDRequette.getIdDossier(EnDossierBase.CORBEILLE
						.getLib(), p_idCompte);
				methodeImap.releveImap(props, p_idCompte, p_progress,
						id_Dossier, folder, textArea, label);
			}

		}

		try {
			methodeImap.afficheText(textArea,
					"Releve de la boite Gmail terminée");
			store.close();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e, "Erreur connexion");
		}

		methodeImap.afficheText(textArea,
				"Fin des opérations sur la boite GMAIL");

	}

}
