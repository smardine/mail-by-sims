package releve.imap.util;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlListeMessage;

import com.sun.mail.imap.AppendUID;
import com.sun.mail.imap.IMAPFolder;

public final class methodeImap {
	private static final String TAG = "methodeImap";

	private methodeImap() {

	}

	/**
	 * @param imapFolder
	 * @param textArea
	 */
	public static void afficheText(JTextArea p_textArea, String p_text) {

		p_textArea.append(p_text + "\n");
		p_textArea.setCaretPosition(p_textArea.getDocument().getLength());

	}

	public static MlListeMessage deplaceMessage(
			MlListeMessage p_listeMessageASupprimer, IMAPFolder p_src,
			IMAPFolder p_dest, JProgressBar p_progress) {
		try {
			p_progress.setValue(0);
			p_progress.setString("maj sur le serveur");
			p_progress.setIndeterminate(true);
			p_src.open(Folder.READ_WRITE);
			p_dest.open(Folder.READ_WRITE);
			Message[] tabMessIMAP = new Message[p_listeMessageASupprimer.size()];
			for (int i = 0; i < p_listeMessageASupprimer.size(); i++) {
				Message messImap = p_src.getMessageByUID(Long
						.parseLong(p_listeMessageASupprimer.get(i)
								.getUIDMessage()));

				tabMessIMAP[i] = messImap;
			}
			if (tabMessIMAP == null) {
				return p_listeMessageASupprimer;
			}
			AppendUID[] tabNewUId = p_dest.appendUIDMessages(tabMessIMAP);
			p_progress.setIndeterminate(false);
			for (int i = 0; i < tabNewUId.length; i++) {
				int nbMessage = i + 1;
				p_progress.setValue((100 * nbMessage) / tabNewUId.length);
				p_progress.setString("maj message " + i + 1 + " sur "
						+ tabNewUId.length);
				// on recupere les nouveaux uid et on met a jour les message
				Message messImapOriginial = tabMessIMAP[i];
				messImapOriginial.setFlag(Flags.Flag.DELETED, true);
				p_listeMessageASupprimer.get(i).setUIDMessage(
						"" + tabNewUId[i].uid);
			}

		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible de copier le message depuis " + p_src.getName()
							+ " vers " + p_dest.getName());
		} finally {
			try {
				p_src.expunge();
				p_src.close(true);// on confirme la suppression des messages du
				// dossier d'origine
				p_dest.close(false);

			} catch (MessagingException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Impossible de fermer le dossier");
			}

		}
		return p_listeMessageASupprimer;

	}

}
