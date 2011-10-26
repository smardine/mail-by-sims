package releve.hotmail;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import releve.imap.util.messageUtilisateur;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncException;

import factory.ReleveFactory;

public class ReleveHotmail {
	private final String TAG = this.getClass().getSimpleName();

	private final JProgressBar progressBar;
	private final JTextArea textArea;
	private final JProgressBar progressPJ;

	public ReleveHotmail(MlCompteMail p_compteMail, JProgressBar progress,
			JProgressBar p_progressPieceJointe, JTextArea p_textArea,
			boolean p_isSynchro) {

		this.progressBar = progress;
		this.textArea = p_textArea;
		this.progressPJ = p_progressPieceJointe;
		ReleveFactory releve = new ReleveFactory(p_compteMail, progressBar,
				progressPJ, textArea);
		try {
			releve.releveCourier();
		} catch (AuthenticationException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte "
							+ p_compteMail.getNomCompte());
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte "
							+ p_compteMail.getNomCompte());
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte "
							+ p_compteMail.getNomCompte());
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte "
							+ p_compteMail.getNomCompte());
		}

		// this.main(p_compteMail, progressBar, progressPJ);

	}

	// private void main(MlCompteMail p_compteMail, JProgressBar p_progressBar,
	// JProgressBar p_progressBarPJ) {
	//
	// // props.setProperty("mail.store.protocol", "davmail");
	// BDRequette bd = new BDRequette();
	// DeltaSyncClientHelper client = new DeltaSyncClientHelper(
	// new DeltaSyncClient(), p_compteMail.getUserName(), p_compteMail
	// .getPassword());
	//
	// if (isSynchro) {
	//
	// methodeHotmail.miseAJourMessagerie(bd, client, p_compteMail,
	// p_progressBar, p_progressBarPJ, textArea);
	//
	// } else {
	//
	// // lance la connexion
	// try {
	// client.login();
	// ArrayList<Folder> dossierDeBase = methodeHotmail
	// .getDossierPrincipaux(client);
	//
	// for (Folder unDossier : dossierDeBase) {
	// int idDossier = 0;
	// idDossier = methodeHotmail.getIdDossierPrincipaux(
	// p_compteMail, unDossier);
	// Message[] messages = client.getMessages(unDossier);
	// int messBaseCount = bd.getnbMessageParDossier(p_compteMail
	// .getIdCompte(), idDossier);
	//
	// int nbMessARelever = messages.length - messBaseCount;
	// Historique.ecrireReleveBal(p_compteMail,
	// "Nombre de messages total: " + messages.length);
	// Historique
	// .ecrireReleveBal(p_compteMail,
	// "Nombre de message(s) à relever: "
	// + nbMessARelever);
	// if (nbMessARelever > 0) {
	// methodeHotmail.releveHotmail(
	// p_compteMail.getIdCompte(), progressBar,
	// progressPJ, idDossier, messages, unDossier,
	// client, textArea);
	// } else {
	// methodeHotmail.afficheText(textArea,
	// "Pas de nouveau message a relever dans le dossier "
	// + unDossier.getName());
	// }
	// client.getStore().resetFolders(p_compteMail.getUserName());
	// }
	//
	// ArrayList<Folder> sousDossier = methodeHotmail
	// .getSousDossierHotmail(client);
	//
	// for (Folder f : sousDossier) {
	// traiteSousDossier(p_progressBar, p_progressBarPJ, client,
	// p_compteMail, bd, f);
	// client.getStore().resetFolders(p_compteMail.getUserName());
	// }
	// bd.closeConnexion();
	//
	// } catch (AuthenticationException e) {
	// messageUtilisateur.affMessageException(TAG, e,
	// "Erreur de connexion");
	// return;
	// } catch (DeltaSyncException e) {
	// messageUtilisateur.affMessageException(TAG, e,
	// "Erreur de protocole DeltaSync");
	// } catch (IOException e) {
	// messageUtilisateur.affMessageException(TAG, e, "Erreur E/S");
	// }
	// }
	// client.disconnect();
	//
	// }

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
	// private void traiteSousDossier(JProgressBar p_progressBar,
	// JProgressBar p_progressBarPJ, DeltaSyncClientHelper client,
	// MlCompteMail cpt, BDRequette bd, Folder f)
	// throws DeltaSyncException, IOException {
	// int idDossier = bd.getIdDossier(f.getName(), cpt.getIdCompte());
	// if (idDossier == -1) {// le dossier n'existe pas, on le crée
	// bd.createNewDossier(cpt.getIdCompte(), cpt.getIdInbox(), f
	// .getName(), f.getName());
	// idDossier = bd.getIdDossier(f.getName(), cpt.getIdCompte());
	// }
	// Message[] messages = client.getMessages(f);
	//
	// int messBaseCount = bd.getnbMessageParDossier(cpt.getIdCompte(),
	// idDossier);
	//
	// int nbMessARelever = messages.length - messBaseCount;
	// if (nbMessARelever > 0) {
	// methodeHotmail.releveHotmail(cpt.getIdCompte(), p_progressBar,
	// p_progressBarPJ, idDossier, messages, f, client, textArea);
	// } else {
	// methodeHotmail.afficheText(textArea,
	// "Pas de nouveau message a relever dans le dossier "
	// + f.getName());
	// }
	//
	// }

}
