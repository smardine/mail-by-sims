package hotmail;

import hotmail.util.methodeHotmail;
import imap.util.messageUtilisateur;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import tools.Historique;
import bdd.BDRequette;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;
import com.googlecode.jdeltasync.DeltaSyncException;
import com.googlecode.jdeltasync.Folder;
import com.googlecode.jdeltasync.Message;

public class ReleveHotmail {

	// private final String user;
	// private final String password;
	// private final String host;
	// private final int idCompte;
	private final JProgressBar progressBar;
	private final boolean isSynchro;
	private final JTextArea textArea;
	private final JProgressBar progressPJ;

	public ReleveHotmail(MlCompteMail p_compteMail,
	/* String p_host, */JProgressBar progress,
			JProgressBar p_progressPieceJointe, JTextArea p_textArea,
			boolean p_isSynchro) {

		// this.user = p_user;
		// this.password = p_password;
		// // this.host = p_host;
		// this.idCompte = p_idCpt;
		this.progressBar = progress;
		this.textArea = p_textArea;
		this.progressPJ = p_progressPieceJointe;
		this.isSynchro = p_isSynchro;

		this.main(p_compteMail, progressBar, progressPJ);

	}

	private void main(MlCompteMail p_compteMail, JProgressBar p_progressBar,
			JProgressBar p_progressBarPJ) {

		// props.setProperty("mail.store.protocol", "davmail");
		BDRequette bd = new BDRequette();
		DeltaSyncClientHelper client = new DeltaSyncClientHelper(
				new DeltaSyncClient(), p_compteMail.getUserName(), p_compteMail
						.getPassword());

		if (isSynchro) {

			methodeHotmail.miseAJourMessagerie(bd, client, p_compteMail,
					p_progressBar, p_progressBarPJ, textArea);

		} else {

			// lance la connexion
			try {
				client.login();
				ArrayList<Folder> dossierDeBase = methodeHotmail
						.getDossierPrincipaux(client);

				for (Folder unDossier : dossierDeBase) {
					int idDossier = 0;
					idDossier = methodeHotmail.getIdDossierPrincipaux(
							p_compteMail, unDossier);
					Message[] messages = client.getMessages(unDossier);
					int messBaseCount = bd.getnbMessageParDossier(p_compteMail
							.getIdCompte(), idDossier);

					int nbMessARelever = messages.length - messBaseCount;
					Historique.ecrireReleveBal(p_compteMail,
							"Nombre de messages total: " + messages.length);
					Historique
							.ecrireReleveBal(p_compteMail,
									"Nombre de message(s) à relever: "
											+ nbMessARelever);
					if (nbMessARelever > 0) {
						methodeHotmail.releveHotmail(
								p_compteMail.getIdCompte(), progressBar,
								progressPJ, idDossier, messages, unDossier,
								client, textArea);
					} else {
						methodeHotmail.afficheText(textArea,
								"Pas de nouveau message a relever dans le dossier "
										+ unDossier.getName());
					}

				}

				ArrayList<Folder> sousDossier = methodeHotmail
						.getSousDossierHotmail(client);

				for (Folder f : sousDossier) {
					traiteSousDossier(p_progressBar, p_progressBarPJ, client,
							p_compteMail, bd, f);

				}
				bd.closeConnexion();

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
		client.disconnect();

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
	private void traiteSousDossier(JProgressBar p_progressBar,
			JProgressBar p_progressBarPJ, DeltaSyncClientHelper client,
			MlCompteMail cpt, BDRequette bd, Folder f)
			throws DeltaSyncException, IOException {
		int idDossier = bd.getIdDossier(f.getName(), cpt.getIdCompte());
		if (idDossier == -1) {// le dossier n'existe pas, on le crée
			bd.createNewDossier(cpt.getIdCompte(), cpt.getIdInbox(), f
					.getName(), f.getName());
			idDossier = bd.getIdDossier(f.getName(), cpt.getIdCompte());
		}
		Message[] messages = client.getMessages(f);

		int messBaseCount = bd.getnbMessageParDossier(cpt.getIdCompte(),
				idDossier);

		int nbMessARelever = messages.length - messBaseCount;
		if (nbMessARelever > 0) {
			methodeHotmail.releveHotmail(cpt.getIdCompte(), p_progressBar,
					p_progressBarPJ, idDossier, messages, f, client, textArea);
		} else {
			methodeHotmail.afficheText(textArea,
					"Pas de nouveau message a relever dans le dossier "
							+ f.getName());
		}

	}

}
