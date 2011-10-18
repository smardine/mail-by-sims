/**
 * 
 */
package releve;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import mdl.MlCompteMail;

/**
 * @author smardine
 */
public class thread_VerifNewMess extends Thread {

	private final MlCompteMail compteMail;
	private final Folder folder;
	private int originalMessageCount;
	private final Store store;

	public thread_VerifNewMess(MlCompteMail p_cpt, Folder p_folder,
			Store p_store) throws MessagingException {
		this.compteMail = p_cpt;
		this.folder = p_folder;
		this.store = p_store;
		originalMessageCount = folder.getMessageCount();

	}

	@Override
	public void run() {
		startTimer();
	}

	private void startTimer() {
		int seconds = 0;
		while (true) {
			try {
				// Thread.sleep(300000);
				Thread.sleep(1000);
				// folder = (IMAPFolder) store.getFolder("INBOX");
				if (folder.HOLDS_MESSAGES != 1) {
					return;
				}
				if (!folder.isOpen()) {
					folder.open(Folder.READ_ONLY);
				}
				int c = folder.getMessageCount();
				if (originalMessageCount > c) {
					originalMessageCount = c;
					System.out.println("des message ont ete supprimé");
				} else if (originalMessageCount < c) {
					originalMessageCount = c;
					System.out.println("des messages ont été ajouté");
				}
			} catch (InterruptedException ex) {
			} catch (MessagingException me) {
			}
		}
	}

}
