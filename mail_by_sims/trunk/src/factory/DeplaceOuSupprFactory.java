/**
 * 
 */
package factory;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import mdl.MlCompteMail;
import mdl.MlListeMessageGrille;
import mdl.MlMessageGrille;
import tools.Historique;
import bdd.accesTable.AccesTableDossier;
import bdd.accesTable.AccesTableMailRecu;

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
	private final MlListeMessageGrille listeMessage;
	private Store store;
	private final Patience fenetre;

	/**
	 * Constructeur
	 * @param p_cptMail - le compte mail concerné
	 * @param p_list - la liste des messages a traiter
	 * @param p_fenetre - une barrre de progression
	 */
	public DeplaceOuSupprFactory(MlCompteMail p_cptMail,
			MlListeMessageGrille p_list, Patience p_fenetre) {
		this.compteMail = p_cptMail;
		this.listeMessage = p_list;
		this.fenetre = p_fenetre;

	}

	public boolean deplaceMessageVersCorbeille() throws MessagingException {
		if (checkParametreEntree()) {
			fenetre.setVisible(true);
			StoreFactory storeFact = new StoreFactory(compteMail);
			store = storeFact.getConnectedStore();
			switch (compteMail.getTypeCompte()) {
				case GMAIL:
				case IMAP:
					deplaceGMAIL();
					break;
				case HOTMAIL:
					break;
				case POP:
					deplacePOP();
					break;
			}
			fenetre.setVisible(false);
			return true;
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
		int count = 1;
		int tailleListe = tabMessage.length;
		for (Message popMessage : tabMessage) {
			int pourcent = ((count + 1) * 100) / tailleListe;
			fenetre.afficheInfo("Déplacement vers la corbeille", pourcent
					+ " %", pourcent);
			count++;
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
		AccesTableDossier accesDossier = new AccesTableDossier();
		IMAPFolder dest = (IMAPFolder) store.getFolder("[Gmail]/Corbeille");
		Message[] tabMessIMAPOriginaux = new Message[listeMessage.size()];
		IMAPFolder src = (IMAPFolder) store.getFolder(accesDossier
				.getNomInternetDossier(listeMessage.get(0).getIdDossier()));
		gestionOuvertureDossier(src);
		gestionOuvertureDossier(dest);
		fenetre.afficheInfo("Récuperation des infos sur le serveur", 0 + " %",
				0);
		fenetre.getjProgressBar().setIndeterminate(true);
		for (int i = 0; i < listeMessage.size(); i++) {
			MlMessageGrille m = listeMessage.get(i);
			tabMessIMAPOriginaux[i] = recupNouvelUID(src, m);
		}

		fenetre.getjProgressBar().setIndeterminate(false);
		AccesTableMailRecu accesMail = new AccesTableMailRecu();

		for (int i = 0; i < tabMessIMAPOriginaux.length; i++) {
			fenetre.afficheInfo("Deplacement du message de "
					+ src.getFullName() + " vers " + dest.getFullName(),
					"maj message " + (i + 1) + " sur "
							+ tabMessIMAPOriginaux.length, (100 * (i + 1))
							/ tabMessIMAPOriginaux.length);

			if (tabMessIMAPOriginaux[i] == null) {
				Historique.ecrireReleveBal(compteMail, TAG,
						"le message n'existe plus sur le serveur");
			} else {
				Message[] tabUnMessImap = new Message[1];
				tabUnMessImap[0] = tabMessIMAPOriginaux[i];
				AppendUID[] tabNewUId = dest.appendUIDMessages(tabUnMessImap);
				// Message messImapOriginial = tabMessIMAPOriginaux[i];
				tabMessIMAPOriginaux[i].setFlag(Flags.Flag.DELETED, true);
				listeMessage.get(i).setUidMessage("" + tabNewUId[0].uid);
				accesMail.updateUIDMessage(listeMessage.get(i));

			}
		}

		src.expunge();
		src.close(true);// on confirme la suppression des messages
		// du
		// dossier d'origine
		dest.close(false);
		store.close();
		Historique.ecrireReleveBal(compteMail, TAG, "Fermeture des dossiers "
				+ src.getFullName() + " et " + dest.getFullName());

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
	 * @param p_m - le message enregistré dans la base de données.
	 * @return
	 * @throws MessagingException - si une erreur est survenue
	 */
	private Message recupNouvelUID(IMAPFolder src, MlMessageGrille p_m)
			throws MessagingException {
		Message messImap = src.getMessageByUID(Long.parseLong(p_m
				.getUidMessage()));
		return messImap;

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
		if (listeMessage == null || compteMail == null || fenetre == null) {
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
					int count = 1;
					for (MlMessageGrille m : listeMessage) {
						fenetre.afficheInfo("Suppression de message(s) ",
								(count) + " sur " + listeMessage.size(),
								(100 * count) / listeMessage.size());
						count++;
						AccesTableMailRecu accesMail = new AccesTableMailRecu();
						accesMail.deleteMessageRecu(m.getIdMessage());
					}

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

		MlMessageGrille messageTest = listeMessage.get(0);
		if (messageTest.getIdDossier() == compteMail.getIdCorbeille()) {
			AccesTableDossier accesDossier = new AccesTableDossier();
			IMAPFolder fldr = (IMAPFolder) store.getFolder(accesDossier
					.getNomInternetDossier(messageTest.getIdDossier()));
			if (!fldr.isOpen()) {
				fldr.open(Folder.READ_WRITE);
			}

			AccesTableMailRecu accesMail = new AccesTableMailRecu();
			for (int i = 0; i < listeMessage.size(); i++) {
				MlMessageGrille messBase = listeMessage.get(i);
				fenetre.afficheInfo("Suppression de message(s) ", (i) + " sur "
						+ listeMessage.size(), (100 * (i + 1))
						/ listeMessage.size());
				if (messBase != null) {
					Message messServeur = fldr.getMessageByUID(Long
							.parseLong(messBase.getUidMessage()));
					if (messServeur != null) {
						messServeur.setFlag(Flags.Flag.DELETED, true);
					}
					accesMail.deleteMessageRecu(messBase.getIdMessage());
				}
			}

			fldr.expunge();
			fldr.close(true);
		}
		store.close();

	}
}
