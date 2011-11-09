/**
 * 
 */
package factory;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import bdd.accesTable.AccesTableMailRecu;

import com.sun.mail.imap.IMAPFolder;

import fenetre.Patience;
import fenetre.comptes.EnTypeCompte;

/**
 * Cette classe s'occupe de tout ce qui a trait a la synchronisation d'un compte
 * mail de type {@link EnTypeCompte.GMAIL}, {@link EnTypeCompte.IMAP},
 * {@link EnTypeCompte.HOTMAIL}
 * @author smardine
 */
public class SynchroFactory {

	private final MlCompteMail compteMail;

	private final Patience fenetre;

	/**
	 * Constructeur
	 * @param p_cptMail - le compte mail visé
	 * @param p_fenetre - une barre de progression
	 */
	public SynchroFactory(MlCompteMail p_cptMail, Patience p_fenetre) {
		this.compteMail = p_cptMail;
		this.fenetre = p_fenetre;
	}

	/**
	 * Synchronise UN dossier de type {@link com.googlecode.jdeltasync.Folder}
	 * @param p_folder - le dossier ciblé
	 * @param p_lstMessageHotmail - la listes des messages a verifier
	 */
	public void synchroniseUnDossierDeltaSync(
			com.googlecode.jdeltasync.Folder p_folder,
			com.googlecode.jdeltasync.Message[] p_lstMessageHotmail) {
		AccesTableMailRecu accesMail = new AccesTableMailRecu();
		DossierFactory dossierFact = new DossierFactory(p_folder, compteMail);
		dossierFact.isDossierDejaPresentEnBase();

		int messBaseCount = accesMail.getnbMessageParDossier(compteMail
				.getIdCompte(), dossierFact.getIdDossier());
		int nbMessASynchroniser = p_lstMessageHotmail.length - messBaseCount;

		if (nbMessASynchroniser >= 0) {
			// meme nb de message pas de recherche de mess supprimé a
			// faire
			return;
		}

		MlListeMessage listeMessage = accesMail.getListeDeMessage(compteMail
				.getIdCompte(), dossierFact.getIdDossier());
		int nbActu = 0;
		for (MlMessage m : listeMessage) {
			nbActu++;
			int pourcent = (nbActu * 100) / listeMessage.size();
			fenetre.afficheInfo("Maj dossier", p_folder.getName() + " : "
					+ pourcent + " %", pourcent);

			if (!verifUIDDeltaMessage(m.getUIDMessage(), p_lstMessageHotmail)) {
				accesMail.deleteMessageRecu(m.getIdMessage());
			}

		}

	}

	/**
	 * Comparaison de l'uid d'un message de la bdd avec un message de type
	 * {@link com.googlecode.jdeltasync.Message}
	 * @param p_uidMessage - l'uid a comparer
	 * @param p_lstMessagesHotmail - la liste de messages a verifier
	 * @return true si l'uid de message est connu de la liste verifiée
	 */
	public boolean verifUIDDeltaMessage(String p_uidMessage,
			com.googlecode.jdeltasync.Message[] p_lstMessagesHotmail) {
		for (com.googlecode.jdeltasync.Message m : p_lstMessagesHotmail) {
			if (p_uidMessage.equals(m.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Synchroniser UN dossier de type {@link Folder}
	 * @param p_folder - le dossier ciblé
	 * @throws MessagingException
	 */
	public void synchroniseUnDossier(Folder p_folder) throws MessagingException {
		AccesTableMailRecu bd = new AccesTableMailRecu();
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
			fenetre.afficheInfo("Maj dossier", p_folder.getFullName() + " : "
					+ pourcent + " %", pourcent);

			Message messToCheck = ((IMAPFolder) p_folder).getMessageByUID(Long
					.parseLong(m.getUIDMessage()));

			if (messToCheck == null) {
				bd.deleteMessageRecu(m.getIdMessage());
			}

		}
	}

}
