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

import com.posisoft.jdavmail.JDAVMailStore;
import com.sun.mail.imap.IMAPFolder;

public class ReleveHotmail {

	private final String user;
	private final String password;
	private final String host;
	private final int idCompte;
	private final JProgressBar progressBar;
	private final boolean isSynchro;
	private final JTextArea textArea;
	private final JLabel label;

	public ReleveHotmail(int p_idCpt, String p_user, String p_password,
			String p_host, JProgressBar progress, JTextArea p_textArea,
			JLabel p_label, boolean p_isSynchro) {

		this.user = p_user;
		this.password = p_password;
		this.host = p_host;
		this.idCompte = p_idCpt;
		this.progressBar = progress;
		this.textArea = p_textArea;
		this.label = p_label;
		this.isSynchro = p_isSynchro;
		this.main(idCompte, progressBar);

	}

	private void main(int p_idCompte, JProgressBar p_progressBar) {

		Properties props = new Properties();
		// props.setProperty("mail.store.protocol", "davmail");

		if (isSynchro) {
			// on commence par verifier les messages suprrimés car sinon, on
			// pert du
			// temp a le faire apres une releve de la BAL
			methodeImap
					.afficheText(textArea, "Mise a jour de la boite hotmail");
			methodeImap.afficheText(textArea,
					"Synchronisation des messages supprimés");
			methodeImap.miseAJourMessage(props, p_idCompte, progressBar, host,
					user, password, textArea, label);
		}
		Session session = Session.getInstance(props);

		// Get a Store object
		// JDAVMail Provider
		Store store = new JDAVMailStore(session, null);

		try {
			store.connect(null, user, password);
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

			methodeImap.releveImap(props, p_idCompte, progressBar, id_Dossier,
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
				methodeImap.releveImap(props, p_idCompte, progressBar,
						idSousDossier, folder, textArea, label);

			}
		}

	}

}
