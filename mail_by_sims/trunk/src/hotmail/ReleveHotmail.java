package hotmail;

import hotmail.util.methodeHotmail;
import imap.util.messageUtilisateur;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
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
	// private final String host;
	private final int idCompte;
	private final JProgressBar progressBar;
	private final boolean isSynchro;
	private final JTextArea textArea;
	private final JProgressBar progressPJ;

	public ReleveHotmail(int p_idCpt, String p_user, String p_password,
	/* String p_host, */JProgressBar progress,
			JProgressBar p_progressPieceJointe, JTextArea p_textArea,
			boolean p_isSynchro) {

		this.user = p_user;
		this.password = p_password;
		// this.host = p_host;
		this.idCompte = p_idCpt;
		this.progressBar = progress;
		this.textArea = p_textArea;
		this.progressPJ = p_progressPieceJointe;
		this.isSynchro = p_isSynchro;

		this.main(idCompte, progressBar, progressPJ);

	}

	private void main(int p_idCompte, JProgressBar p_progressBar,
			JProgressBar p_progressBarPJ) {

		// props.setProperty("mail.store.protocol", "davmail");

		if (isSynchro) {
			// on commence par verifier les messages suprrimés car sinon, on
			// pert du
			// temp a le faire apres une releve de la BAL
			// methodeImap
			// .afficheText(textArea, "Mise a jour de la boite hotmail");
			// methodeImap.afficheText(textArea,
			// "Synchronisation des messages supprimés");
			// methodeImap.miseAJourMessage(props, p_idCompte, progressBar,
			// host,
			// user, password, textArea, label);
		} else {
			DeltaSyncClientHelper client = new DeltaSyncClientHelper(
					new DeltaSyncClient(), user, password);

			// lance la connexion
			try {
				client.login();
				ArrayList<Folder> dossierDeBase = methodeHotmail
						.getDossierPrincipaux(client);
				MlCompteMail cpt = new MlCompteMail(p_idCompte);
				for (Folder unDossier : dossierDeBase) {
					int idDossier = 0;
					idDossier = getIdDossier(cpt, unDossier, idDossier);
					Message[] messages = client.getMessages(unDossier);
					methodeHotmail.releveHotmail(p_idCompte, progressBar,
							progressPJ, idDossier, messages, unDossier, client,
							textArea);
				}

				ArrayList<Folder> sousDossier = methodeHotmail
						.getSousDossierHotmail(client);
				BDRequette bd = new BDRequette();
				for (Folder f : sousDossier) {
					traiteSousDossier(p_idCompte, p_progressBar,
							p_progressBarPJ, client, cpt, bd, f);

				}

			} catch (AuthenticationException e) {
				messageUtilisateur
						.affMessageException(e, "Erreur de connexion");
				return;
			} catch (DeltaSyncException e) {
				messageUtilisateur.affMessageException(e,
						"Erreur de protocole DeltaSync");
			} catch (IOException e) {
				messageUtilisateur.affMessageException(e, "Erreur E/S");
			}
		}

	}

	/**
	 * @param cpt
	 * @param unDossier
	 * @param idDossier
	 * @return
	 */
	private int getIdDossier(MlCompteMail cpt, Folder unDossier, int idDossier) {
		if ("Inbox".equals(unDossier.getName())) {
			idDossier = cpt.getIdInbox();
		} else if ("Junk".equals(unDossier.getName())) {
			idDossier = cpt.getIdSpam();
		} else if ("Drafts".equals(unDossier.getName())) {
			idDossier = cpt.getIdBrouillons();
		} else if ("Sent".equals(unDossier.getName())) {
			idDossier = cpt.getIdEnvoye();
		} else if ("Deleted".equals(unDossier.getName())) {
			idDossier = cpt.getIdCorbeille();
		}
		return idDossier;
	}

	/**
	 * @param p_idCompte
	 * @param p_progressBar
	 * @param p_progressBarPJ
	 * @param client
	 * @param cpt
	 * @param bd
	 * @param f
	 * @throws DeltaSyncException
	 * @throws IOException
	 */
	private void traiteSousDossier(int p_idCompte, JProgressBar p_progressBar,
			JProgressBar p_progressBarPJ, DeltaSyncClientHelper client,
			MlCompteMail cpt, BDRequette bd, Folder f)
			throws DeltaSyncException, IOException {
		int idDossier = bd.getIdDossier(f.getName(), p_idCompte);
		if (idDossier == -1) {// le dossier n'existe pas, on le crée
			bd.createNewDossier(p_idCompte, cpt.getIdInbox(), f
					.getName());
			idDossier = bd.getIdDossier(f.getName(), p_idCompte);
		}
		Message[] messages = client.getMessages(f);
		methodeHotmail.releveHotmail(p_idCompte, p_progressBar,
				p_progressBarPJ, idDossier, messages, f, client,
				textArea);
	}

}
