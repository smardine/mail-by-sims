/**
 * 
 */
package releve.imap;


import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import releve.imap.listener.messageListener;
import releve.imap.util.messageUtilisateur;
import releve.imap.util.methodeImap;
import tools.Historique;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

/**
 * @author smardine
 */
public class ReleveGmail {
	private final String TAG = this.getClass().getSimpleName();
	private final JProgressBar progressBar;
	private final JProgressBar progressPJ;
	private static JTextArea textArea;
	private static String user;
	private static String password;
	private static String host;
	private static boolean isSynchro;
	private static MlCompteMail cptMail;

	public ReleveGmail(int p_idCompte, String p_user, String p_password,
			String p_host, JProgressBar progress,
			JProgressBar p_progressPieceJointe, JTextArea textArea,
			boolean p_isSynchro) {
		cptMail = new MlCompteMail(p_idCompte);
		ReleveGmail.user = p_user;
		ReleveGmail.password = p_password;
		ReleveGmail.host = p_host;

		this.progressBar = progress;
		this.progressPJ = p_progressPieceJointe;
		ReleveGmail.isSynchro = p_isSynchro;
		ReleveGmail.textArea = textArea;

		go(cptMail, progressBar, progressPJ);
	}

	/**
	 * @param args
	 */

	public void go(MlCompteMail p_compteMail, JProgressBar p_progress,
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
			methodeImap.afficheText(textArea, "Mise a jour du compte "
					+ p_compteMail.getNomCompte());
			methodeImap.afficheText(textArea,
					"Synchronisation des messages supprimés");
			methodeImap.miseAJourMessage(props, p_compteMail.getIdCompte(),
					p_progress, p_compteMail.getServeurReception(),
					p_compteMail.getUserName(), p_compteMail.getPassword(),
					textArea);
		} else {
			Session session = Session.getInstance(props);

			// Get a Store object
			Store store = null;

			try {
				store = session.getStore("imaps");
				if (!store.isConnected()) {
					store.connect(host, user, password);
				}

				methodeImap.afficheText(textArea, "Releve des dossiers");
				Historique.ecrireReleveBal(cptMail, "Releve des dossiers");
				Folder[] personnal = store.getPersonalNamespaces();
				// Folder[] shared = store.getSharedNamespaces();
				// Folder[] userNamedSpace = store.getUserNamespaces(user);

				for (Folder f : personnal) {
					IMAPFolder[] sousfodler = methodeImap.getSousDossierIMAP(
							store, f.getFullName());

					traiteListeDossier(p_compteMail, p_progress, p_progressPJ,
							bd, props, sousfodler, store);
				}

				methodeImap.afficheText(textArea, "Releve du compte "
						+ p_compteMail.getNomCompte() + " terminée");
				// store.close();
			} catch (MessagingException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Erreur connexion");
			}

		}

		methodeImap.afficheText(textArea,
				"Fin des opérations sur la boite GMAIL");
		bd.closeConnexion();
	}

	/**
	 * @param p_compteMail
	 * @param p_progress
	 * @param p_progressPJ
	 * @param bd
	 * @param props
	 * @param lstSousDossier
	 */
	private void traiteListeDossier(MlCompteMail p_compteMail,
			JProgressBar p_progress, JProgressBar p_progressPJ, BDRequette bd,
			Properties props, IMAPFolder[] lstSousDossier, Store p_store) {
		if (lstSousDossier != null) {
			for (IMAPFolder fldr : lstSousDossier) {
				messageListener listener = new messageListener(p_compteMail,
						fldr);
				fldr.addMessageChangedListener(listener);
				fldr.addFolderListener(listener);
				fldr.addMessageCountListener(listener);
				int idDossier = -1;
				idDossier = verifieConnaissanceDossier(p_compteMail, bd, fldr);
				if (idDossier == (-1)) {// si le sous dossier est
					// inconnu
					// de la base, on en créer un

					if ("[Gmail]".equals(fldr.getFullName())) {
						// ce n'est pas vraiment un repertoire,
						// c'est plus un conteneur
						// on recupere juste ses sous dossier et on continue
						IMAPFolder[] lstFolder = methodeImap
								.getSousDossierIMAP(p_store, fldr.getFullName());

						traiteListeDossier(cptMail, progressBar, progressPJ,
								bd, props, lstFolder, p_store);
						continue;
					}
					Historique.ecrireReleveBal(cptMail,
							"Création d'un nouveau dossier: "
									+ fldr.getFullName());
					if (fldr.getFullName().contains("[Gmail]")) {
						// de cette facon, les dossier
						// "Important","Tous les messages"...
						// seront a la racine du compte dans le jtree
						bd.createNewDossier(p_compteMail.getIdCompte(), 0, fldr
								.getName(), fldr.getFullName());
					} else {
						// sinon, les dossiers créés le seront sous INBOX
						bd.createNewDossier(p_compteMail.getIdCompte(),
								p_compteMail.getIdInbox(), fldr.getName(), fldr
										.getFullName());
					}

					idDossier = bd.getIdDossier(fldr.getName(), p_compteMail
							.getIdCompte());
				}
				methodeImap.releveImap(props, p_compteMail, p_progress,
						p_progressPJ, idDossier, fldr, textArea);

				IMAPFolder[] lstFolder = methodeImap.getSousDossierIMAP(
						p_store, fldr.getFullName());

				traiteListeDossier(cptMail, progressBar, progressPJ, bd, props,
						lstFolder, p_store);

			}
		}

	}

	/**
	 * @param p_compteMail
	 * @param bd
	 * @param fldr
	 * @return
	 */
	private int verifieConnaissanceDossier(MlCompteMail p_compteMail,
			BDRequette bd, IMAPFolder fldr) {
		int idDossier;
		if (isInbox(fldr)) {
			idDossier = p_compteMail.getIdInbox();
		} else if (isBrouillon(fldr)) {
			idDossier = p_compteMail.getIdBrouillons();
		} else if (isCorbeille(fldr)) {
			idDossier = p_compteMail.getIdCorbeille();
		} else if (isEnvoye(fldr)) {
			idDossier = p_compteMail.getIdEnvoye();
		} else if (isSpam(fldr)) {
			idDossier = p_compteMail.getIdSpam();
		} else {
			idDossier = bd.getIdDossier(fldr.getName(), p_compteMail
					.getIdCompte());
		}
		return idDossier;
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

	private boolean isInbox(IMAPFolder folder) {
		return folder.getFullName().equals("INBOX");
	}

	private boolean isSpam(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Spam");
	}

}
