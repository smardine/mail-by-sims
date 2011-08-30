/**
 * 
 */
package imap;

import fenetre.comptes.EnDossierBase;
import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
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
	private final JProgressBar progressPJ;
	private static JTextArea textArea;
	private static String user;
	private static String password;
	private static String host;
	private static boolean isSynchro;

	public ReleveGmail(int p_idCompte, String p_user, String p_password,
			String p_host, JProgressBar progress,
			JProgressBar p_progressPieceJointe, JTextArea textArea,
			boolean p_isSynchro) {

		ReleveGmail.user = p_user;
		ReleveGmail.password = p_password;
		ReleveGmail.host = p_host;
		this.idCompte = p_idCompte;
		this.progressBar = progress;
		this.progressPJ = p_progressPieceJointe;
		ReleveGmail.isSynchro = p_isSynchro;
		ReleveGmail.textArea = textArea;

		go(idCompte, progressBar, progressPJ);
	}

	/**
	 * @param args
	 */

	public void go(int p_idCompte, JProgressBar p_progress,
			JProgressBar p_progressPJ) {
		BDRequette bd = new BDRequette();
		Properties props = System.getProperties();

		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imaps.partialfetch", "false");

		if (isSynchro) {
			// on commence par verifier les messages suprrimés car sinon, on
			// pert du
			// temp a le faire apres une releve de la BAL
			methodeImap.afficheText(textArea, "Mise a jour de la boite Gmail");
			methodeImap.afficheText(textArea,
					"Synchronisation des messages supprimés");
			methodeImap.miseAJourMessage(props, p_idCompte, p_progress, host,
					user, password, textArea);
		} else {
			Session session = Session.getInstance(props);

			// Get a Store object
			Store store = null;
			IMAPFolder inbox = null;
			try {
				store = session.getStore("imaps");
				store.connect(host, user, password);

				methodeImap.afficheText(textArea,
						"Ouverture de la boite de reception");
				inbox = (IMAPFolder) store.getFolder("INBOX");

				// releve de la boite de reception

				int id_Dossier = 0;
				if (inbox != null) {

					id_Dossier = bd.getIdDossier(EnDossierBase.RECEPTION
							.getLib(), p_idCompte);

					methodeImap.releveImap(props, p_idCompte, p_progress,
							p_progressPJ, id_Dossier, inbox, textArea);
					// on recupere ensuite les sous dossiers de la boite de
					// reception
					IMAPFolder[] lstFolder = methodeImap.getSousDossierIMAP(
							store, inbox.getFullName());

					for (IMAPFolder folder : lstFolder) {
						int idSousDossier = bd.getIdDossier(folder.getName(),
								p_idCompte);
						if (idSousDossier == (-1)) {// si le sous dossier est
							// inconnu
							// de la base, on en créer un
							bd.createNewDossier(p_idCompte, id_Dossier, folder
									.getName());
							idSousDossier = bd.getIdDossier(folder.getName(),
									p_idCompte);
						}
						methodeImap.releveImap(props, p_idCompte, p_progress,
								p_progressPJ, idSousDossier, folder, textArea);

					}

				}

				// recuperation des autres dossiers:envoyé, brouillons, spam,
				// corbeille
				IMAPFolder[] lstFolder = methodeImap.getSousDossierIMAP(store,
						"[Gmail]");

				for (IMAPFolder folder : lstFolder) {
					id_Dossier = traiteListeDossier(p_idCompte, p_progress,
							p_progressPJ, bd, props, id_Dossier, folder);
				}

				methodeImap.afficheText(textArea,
						"Releve de la boite Gmail terminée");
				store.close();
			} catch (MessagingException e) {
				messageUtilisateur.affMessageException(e, "Erreur connexion");
			}

		}

		methodeImap.afficheText(textArea,
				"Fin des opérations sur la boite GMAIL");
		bd.closeConnexion();
	}

	/**
	 * @param p_idCompte
	 * @param p_progress
	 * @param p_progressPJ
	 * @param bd
	 * @param props
	 * @param id_Dossier
	 * @param folder
	 * @return
	 */
	private int traiteListeDossier(int p_idCompte, JProgressBar p_progress,
			JProgressBar p_progressPJ, BDRequette bd, Properties props,
			int id_Dossier, IMAPFolder folder) {
		if (isBrouillon(folder)) {
			id_Dossier = bd.getIdDossier(EnDossierBase.BROUILLON
					.getLib(), p_idCompte);

		} else if (isEnvoye(folder)) {
			id_Dossier = bd.getIdDossier(EnDossierBase.ENVOYES
					.getLib(), p_idCompte);

		} else if (folder.getFullName().equals("[Gmail]/Spam")) {
			id_Dossier = bd.getIdDossier(EnDossierBase.SPAM
					.getLib(), p_idCompte);

		} else if (isCorbeille(folder)) {
			id_Dossier = bd.getIdDossier(EnDossierBase.CORBEILLE
					.getLib(), p_idCompte);

		}
		methodeImap.releveImap(props, p_idCompte, p_progress,
				p_progressPJ, id_Dossier, folder, textArea);
		return id_Dossier;
	}

	/**
	 * @param folder
	 * @return
	 */
	private boolean isCorbeille(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Trash") || //
				folder.getFullName().equals("[Gmail]/Corbeille");
	}

	/**
	 * @param folder
	 * @return
	 */
	private boolean isEnvoye(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Sent Mail") || //
				folder.getFullName().equals("[Gmail]/Messages envoyés");
	}

	/**
	 * @param folder
	 * @return
	 */
	private boolean isBrouillon(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Drafts") || //
				folder.getFullName().equals("[Gmail]/Brouillons");
	}

}
