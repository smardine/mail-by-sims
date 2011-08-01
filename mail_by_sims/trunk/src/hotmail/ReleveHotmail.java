package hotmail;

import fenetre.comptes.EnDossierBase;
import hotmail.util.methodeHotmail;
import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import bdd.BDRequette;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;
import com.googlecode.jdeltasync.DeltaSyncException;
import com.googlecode.jdeltasync.Folder;
import com.googlecode.jdeltasync.Message;

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

		// props.setProperty("mail.store.protocol", "davmail");

		if (isSynchro) {
			// on commence par verifier les messages suprrimés car sinon, on
			// pert du
			// temp a le faire apres une releve de la BAL
			methodeImap
					.afficheText(textArea, "Mise a jour de la boite hotmail");
			methodeImap.afficheText(textArea,
					"Synchronisation des messages supprimés");
			// methodeImap.miseAJourMessage(props, p_idCompte, progressBar,
			// host,
			// user, password, textArea, label);
		}

		DeltaSyncClientHelper client = new DeltaSyncClientHelper(
				new DeltaSyncClient(), user, password);

		// lance la connexion
		try {
			client.login();
			Folder inbox = client.getInbox();
			Message[] messages = client.getMessages(inbox);
			int id_Dossier;
			if (inbox != null) {
				id_Dossier = BDRequette.getIdDossier(EnDossierBase.RECEPTION
						.getLib(), p_idCompte);
				methodeHotmail.releveHotmail(p_idCompte, progressBar,
						id_Dossier, messages, inbox, client, textArea, label);
			}

		} catch (AuthenticationException e) {
			messageUtilisateur.affMessageException(e, "Erreur de connexion");
			return;
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur de protocole DeltaSync");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e, "Erreur E/S");
		}

		// releve de la boite de reception

		// IMAPFolder inbox = null;
		// try {
		// methodeHotmail.afficheText(textArea,
		// "Ouverture de la boite de reception");
		// inbox = (IMAPFolder) store.getFolder("INBOX");
		// } catch (MessagingException e1) {
		// messageUtilisateur.affMessageException(e1,
		// "Impossible d'acceder à la boite de reception");
		// }
		// int id_Dossier;
		// if (inbox != null) {
		// id_Dossier = BDRequette.getIdDossier(EnDossierBase.RECEPTION
		// .getLib(), p_idCompte);
		//
		// methodeHotmail.releveImap(props, p_idCompte, progressBar, id_Dossier,
		// inbox, textArea, label);
		// // on recupere ensuite les sous dossiers de la boite de reception
		// IMAPFolder[] lstFolder = methodeHotmail.getSousDossierIMAP(store,
		// inbox.getFullName());
		//
		// for (IMAPFolder folder : lstFolder) {
		// int idSousDossier = BDRequette.getIdDossier(folder.getName(),
		// p_idCompte);
		// if (idSousDossier == (-1)) {// si le sous dossier est inconnu
		// // de la base, on en créer un
		// BDRequette.createNewDossier(p_idCompte, id_Dossier, folder
		// .getName());
		// idSousDossier = BDRequette.getIdDossier(folder.getName(),
		// p_idCompte);
		// }
		// methodeHotmail.releveImap(props, p_idCompte, progressBar,
		// idSousDossier, folder, textArea, label);
		//
		// }
		// }

	}

}
