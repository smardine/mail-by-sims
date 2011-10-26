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
import bdd.BDRequette;

import com.sun.mail.imap.AppendUID;
import com.sun.mail.imap.IMAPFolder;

import exception.DonneeAbsenteException;

/**
 * @author smardine
 */
public class DeplaceOuSupprFactory {
	private final String TAG = this.getClass().getSimpleName();
	private final MlCompteMail compteMail;
	private final MlListeMessage listeMessage;
	private Store store;
	private final BDRequette bd;
	private final JProgressBar progressBar;

	public DeplaceOuSupprFactory(MlCompteMail p_cptMail,
			MlListeMessage p_message, JProgressBar p_progress) {
		this.compteMail = p_cptMail;
		this.listeMessage = p_message;
		this.progressBar = p_progress;
		this.bd = new BDRequette();

	}

	public boolean deplaceMessageVersCorbeille() throws MessagingException {
		if (checkParametreEntree()) {
			switch (compteMail.getTypeCompte()) {
				case GMAIL:
				case IMAP:
					StoreFactory storeFact = new StoreFactory(compteMail);
					store = storeFact.getConnectedStore();
					IMAPFolder dest = (IMAPFolder) store
							.getFolder("[Gmail]/Corbeille");
					Message[] tabMessIMAP = new Message[listeMessage.size()];
					IMAPFolder src = (IMAPFolder) store.getFolder(bd
							.getNomInternetDossier(listeMessage.get(0)
									.getIdDossier()));
					if (!src.isOpen()) {
						src.open(Folder.READ_WRITE);
					}
					if (!dest.isOpen()) {
						dest.open(Folder.READ_WRITE);
					}

					for (int i = 0; i < listeMessage.size(); i++) {
						MlMessage m = listeMessage.get(i);

						Message messImap = src.getMessageByUID(Long.parseLong(m
								.getUIDMessage()));

						tabMessIMAP[i] = messImap;
					}

					if (tabMessIMAP == null) {
						src.close(false);
						dest.close(false);
						return false;
					}
					AppendUID[] tabNewUId = dest.appendUIDMessages(tabMessIMAP);
					progressBar.setIndeterminate(false);
					for (int i = 0; i < tabNewUId.length; i++) {
						int nbMessage = i + 1;
						progressBar.setValue((100 * nbMessage)
								/ tabNewUId.length);
						progressBar.setString("maj message " + (i + 1)
								+ " sur " + tabNewUId.length);
						// on recupere les nouveaux uid et on met a jour les
						// message
						Message messImapOriginial = tabMessIMAP[i];
						messImapOriginial.setFlag(Flags.Flag.DELETED, true);
						listeMessage.get(i)
								.setUIDMessage("" + tabNewUId[i].uid);
						bd.updateUIDMessage(listeMessage.get(i));
					}
					src.expunge();
					src.close(true);// on confirme la suppression des messages
					// du
					// dossier d'origine
					dest.close(false);
					store.close();
					bd.closeConnexion();
					return true;

				case HOTMAIL:
					break;
				case POP:
					break;
			}
		}

		return false;
	}

	/**
	 * 
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

	public boolean supprMessage() throws MessagingException {
		if (checkParametreEntree()) {
			switch (compteMail.getTypeCompte()) {
				case IMAP:
				case GMAIL:
					StoreFactory storeFact = new StoreFactory(compteMail);
					store = storeFact.getConnectedStore();

					MlMessage messageTest = listeMessage.get(0);
					if (messageTest.getIdDossier() == compteMail
							.getIdCorbeille()) {
						IMAPFolder fldr = (IMAPFolder) store.getFolder(bd
								.getNomInternetDossier(messageTest
										.getIdDossier()));
						if (!fldr.isOpen()) {
							fldr.open(Folder.READ_WRITE);
						}
						for (MlMessage m : listeMessage) {
							Message messageServeur = fldr.getMessageByUID(Long
									.parseLong(m.getUIDMessage()));
							if (messageServeur != null) {
								messageServeur
										.setFlag(Flags.Flag.DELETED, true);
								bd.deleteMessageRecu(m.getIdMessage());
							}
						}
						fldr.expunge();
						fldr.close(true);
					}
					store.close();
					bd.closeConnexion();
					return true;
				case HOTMAIL:
					break;
				case POP:
					break;
			}
		}

		return false;
	}
}
