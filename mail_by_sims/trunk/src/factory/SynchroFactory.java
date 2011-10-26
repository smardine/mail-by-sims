/**
 * 
 */
package factory;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JProgressBar;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

/**
 * @author smardine
 */
public class SynchroFactory {

	private final MlCompteMail compteMail;

	private final JProgressBar progress;

	public SynchroFactory(MlCompteMail p_cptMail, JProgressBar p_progressBar) {
		this.compteMail = p_cptMail;
		this.progress = p_progressBar;
	}

	public void synchroniseUnDossierDeltaSync(
			com.googlecode.jdeltasync.Folder p_folder,
			com.googlecode.jdeltasync.Message[] p_lstMessageHotmail) {
		BDRequette bd = new BDRequette();
		DossierFactory dossierFact = new DossierFactory(p_folder, compteMail);
		dossierFact.isDossierDejaPresentEnBase();

		int messBaseCount = bd.getnbMessageParDossier(compteMail.getIdCompte(),
				dossierFact.getIdDossier());
		int nbMessASynchroniser = p_lstMessageHotmail.length - messBaseCount;

		if (nbMessASynchroniser >= 0) {
			// meme nb de message pas de recherche de mess supprimé a
			// faire
			return;
		}

		MlListeMessage listeMessage = bd.getListeDeMessage(compteMail
				.getIdCompte(), dossierFact.getIdDossier());
		int nbActu = 0;
		for (MlMessage m : listeMessage) {
			nbActu++;
			int pourcent = (nbActu * 100) / listeMessage.size();
			progress.setValue(pourcent);
			progress.setString("Maj dossier " + p_folder.getName() + " : "
					+ pourcent + " %");

			if (!verifUIDDeltaMessage(m.getUIDMessage(), p_lstMessageHotmail)) {
				bd.deleteMessageRecu(m.getIdMessage());
			}

		}

	}

	public boolean verifUIDDeltaMessage(String p_uidMessage,
			com.googlecode.jdeltasync.Message[] p_lstMessagesHotmail) {
		for (com.googlecode.jdeltasync.Message m : p_lstMessagesHotmail) {
			if (p_uidMessage.equals(m.getId())) {
				return true;
			}
		}
		return false;
	}

	public void synchroniseUnDossier(Folder p_folder) throws MessagingException {
		BDRequette bd = new BDRequette();
		if (!p_folder.isOpen()) {
			p_folder.open(Folder.READ_WRITE);
		}
		DossierFactory dossierFact = new DossierFactory(p_folder, compteMail);
		dossierFact.isDossierDejaPresentEnBase();

		// int idDossier = bd.getIdDossierWithFullName(p_folder.getName(),
		// p_folder.getFullName(), compteMail.getIdCompte());
		int imapcount = p_folder.getMessageCount();
		int messBaseCount = bd.getnbMessageParDossier(compteMail.getIdCompte(),
				dossierFact.getIdDossier());
		int nbMessASynchroniser = imapcount - messBaseCount;

		if (nbMessASynchroniser >= 0) {
			// meme nb de message pas de recherche de mess supprimé a
			// faire
			return;

		}

		MlListeMessage listeMessage = bd.getListeDeMessage(compteMail
				.getIdCompte(), dossierFact.getIdDossier());
		int nbActu = 0;
		for (MlMessage m : listeMessage) {
			nbActu++;
			int pourcent = (nbActu * 100) / listeMessage.size();
			progress.setValue(pourcent);
			progress.setString("Maj dossier " + p_folder.getFullName() + " : "
					+ pourcent + " %");

			Message messToCheck = ((IMAPFolder) p_folder).getMessageByUID(Long
					.parseLong(m.getUIDMessage()));

			if (messToCheck == null) {
				bd.deleteMessageRecu(m.getIdMessage());
			}

		}
	}

}
