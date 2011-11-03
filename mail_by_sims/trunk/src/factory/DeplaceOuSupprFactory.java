/**
 * 
 */
package factory;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.JProgressBar;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import tools.Historique;
import bdd.BDRequette;

import com.sun.mail.imap.AppendUID;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import exception.DonneeAbsenteException;
import fenetre.Patience;

/**
 * Permet de deplacer des messages d'un dossier vers un autre ou de supprimer
 * des messages.
 * @author smardine
 */
public class DeplaceOuSupprFactory {
	private final String TAG = this.getClass().getSimpleName();
	private final MlCompteMail compteMail;
	private final MlListeMessage listeMessage;
	private Store store;
	private final BDRequette bd;
	private final JProgressBar progressBar;

	/**
	 * Constructeur
	 * @param p_cptMail - le compte mail concerné
	 * @param p_lstMessage - la liste des messages a traiter
	 * @param p_fenetre - une barrre de progression
	 */
	public DeplaceOuSupprFactory(MlCompteMail p_cptMail,
			MlListeMessage p_lstMessage, Patience p_fenetre) {
		this.compteMail = p_cptMail;
		this.listeMessage = p_lstMessage;
		this.progressBar = p_fenetre.getjProgressBar();
		this.bd = new BDRequette();

	}

	public boolean deplaceMessageVersCorbeille() throws MessagingException {
		if (checkParametreEntree()) {
			StoreFactory storeFact = new StoreFactory(compteMail);
			store = storeFact.getConnectedStore();
			switch (compteMail.getTypeCompte()) {
				case GMAIL:
				case IMAP:
					return deplaceGMAIL();
				case HOTMAIL:
					break;
				case POP:
					return deplacePOP();
			}
		}

		return false;
	}

	/**
	 * S'occupe de deplacer les messages de la boite de reception, en reéalité,
	 * comme on est sur un compte POP, on supprime les message de la boite de
	 * reception.
	 * @return true si ca a reussi
	 * @throws MessagingException - Si une erreur intervient
	 */
	private boolean deplacePOP() throws MessagingException {
		boolean supprAFaire = false;
		POP3Folder inbox = (POP3Folder) store.getFolder("INBOX");
		gestionOuvertureDossier(inbox);
		Message[] tabMessage = inbox.getMessages();
		for (Message popMessage : tabMessage) {
			if (listeMessage.contains(popMessage)) {
				popMessage.setFlag(Flags.Flag.DELETED, true);
				supprAFaire = true;
			}
		}
		inbox.close(supprAFaire);
		store.close();
		return true;
	}

	/**
	 * Deplace les messages d'un dossier vers un autre (uniquement pour les
	 * comptes de type GMAIL
	 * @return true si ca a reussi
	 * @throws MessagingException - si une erreur intervient
	 */
	private boolean deplaceGMAIL() throws MessagingException {
		IMAPFolder dest = (IMAPFolder) store.getFolder("[Gmail]/Corbeille");
		Message[] tabMessIMAP = new Message[listeMessage.size()];
		IMAPFolder src = (IMAPFolder) store.getFolder(bd
				.getNomInternetDossier(listeMessage.get(0).getIdDossier()));
		gestionOuvertureDossier(src);
		gestionOuvertureDossier(dest);
		progressBar.setIndeterminate(true);
		progressBar.setValue(0);
		progressBar.setString("Récuperation des infos sur le serveur");
		for (int i = 0; i < listeMessage.size(); i++) {
			MlMessage m = listeMessage.get(i);
			recupNouvelUID(tabMessIMAP, src, i, m);
		}

		if (tabMessIMAP == null) {
			Historique
					.ecrireReleveBal(compteMail, TAG,
							"impossible de recuperer les nouveau UID des messages a deplacer");
			src.close(false);
			dest.close(false);
			return false;
		}
		AppendUID[] tabNewUId = dest.appendUIDMessages(tabMessIMAP);
		progressBar.setIndeterminate(false);
		for (int i = 0; i < tabNewUId.length && tabNewUId[i] != null; i++) {
			int nbMessage = i + 1;
			Historique.ecrireReleveBal(compteMail, src.getFullName(),
					"Deplacement du message de " + src.getFullName() + " vers "
							+ dest.getFullName());
			progressBar.setValue((100 * nbMessage) / tabNewUId.length);
			progressBar.setString("maj message " + (i + 1) + " sur "
					+ tabNewUId.length);
			// on recupere les nouveaux uid et on met a jour les
			// message
			Message messImapOriginial = tabMessIMAP[i];
			messImapOriginial.setFlag(Flags.Flag.DELETED, true);
			listeMessage.get(i).setUIDMessage("" + tabNewUId[i].uid);
			bd.updateUIDMessage(listeMessage.get(i));
		}
		src.expunge();
		src.close(true);// on confirme la suppression des messages
		// du
		// dossier d'origine
		dest.close(false);
		store.close();
		Historique.ecrireReleveBal(compteMail, TAG, "Fermeture des dossiers "
				+ src.getFullName() + " et " + dest.getFullName());
		bd.closeConnexion();
		Historique.ecrireReleveBal(compteMail, TAG,
				"Déplacement des messages réussi");
		return true;
	}

	/**
	 * lors du deplacement (une copie en réalité) d'un ou plusieur message d'un
	 * dossier vers un autre il convient de recuperer les nouvel UID du message
	 * copié.
	 * @param tabMessIMAP - le tableau de messages (IMAP) a traiter
	 * @param src - le dossier source
	 * @param i - l'index de message a traiter
	 * @param m - le message enregistré dans la base de données.
	 * @throws MessagingException - si une erreur est survenue
	 */
	private void recupNouvelUID(Message[] tabMessIMAP, IMAPFolder src, int i,
			MlMessage m) throws MessagingException {
		Message messImap = src.getMessageByUID(Long
				.parseLong(m.getUIDMessage()));
		if (messImap != null) {
			tabMessIMAP[i] = messImap;
		}
	}

	/**
	 * Gerer l'ouverture d'un dossier, si le dossier N'EST PAS ouvert, on
	 * l'ouvre en mode "lecture/ecriture"
	 * @param src - le dossier ciblé
	 * @throws MessagingException - si une erreur intervient (dossier
	 *             inexistant, erreur de connexion...)
	 */
	private void gestionOuvertureDossier(Folder src) throws MessagingException {
		if (!src.isOpen()) {
			src.open(Folder.READ_WRITE);
			Historique.ecrireReleveBal(compteMail, src.getFullName(),
					"Ouverture en lecture-ecriture");
		}
	}

	/**
	 * Permet de verifier si le dvp a bien valorisé tout les parametres
	 * d'entrée. return true si AUCUN parametre n'est à NULL
	 */
	private boolean checkParametreEntree() {
		if (listeMessage == null || compteMail == null || progressBar == null) {
			try {
				throw new DonneeAbsenteException(TAG,
						"un des parametres d'entrée est null");
			} catch (DonneeAbsenteException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Permet de supprimer definitivement un message sur le serveur et de le
	 * supprimer ensuite de la base.
	 * @return true si tout s'est bien passé
	 * @throws MessagingException - si une erreur survient
	 */
	public boolean supprMessage() throws MessagingException {
		if (checkParametreEntree()) {
			switch (compteMail.getTypeCompte()) {
				case IMAP:
				case GMAIL:
					supprGMAIL();
					return true;
				case HOTMAIL:
					break;
				case POP:
					for (MlMessage m : listeMessage) {
						bd.deleteMessageRecu(m.getIdMessage());
					}
					bd.closeConnexion();
					return true;
			}
		}

		return false;
	}

	/**
	 * Routine de suppression dédiée aux compte de type GMAIL
	 * @throws MessagingException - si une erreur survient.
	 */
	private void supprGMAIL() throws MessagingException {
		StoreFactory storeFact = new StoreFactory(compteMail);
		store = storeFact.getConnectedStore();

		MlMessage messageTest = listeMessage.get(0);
		if (messageTest.getIdDossier() == compteMail.getIdCorbeille()) {
			IMAPFolder fldr = (IMAPFolder) store.getFolder(bd
					.getNomInternetDossier(messageTest.getIdDossier()));
			if (!fldr.isOpen()) {
				fldr.open(Folder.READ_WRITE);
			}
			for (MlMessage m : listeMessage) {
				Message messageServeur = fldr.getMessageByUID(Long.parseLong(m
						.getUIDMessage()));
				if (messageServeur != null) {
					messageServeur.setFlag(Flags.Flag.DELETED, true);
					bd.deleteMessageRecu(m.getIdMessage());
				}
			}
			fldr.expunge();
			fldr.close(true);
		}
		store.close();
		bd.closeConnexion();
	}
}
