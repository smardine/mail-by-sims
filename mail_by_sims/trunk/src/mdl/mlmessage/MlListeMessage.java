package mdl.mlmessage;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

public class MlListeMessage extends ArrayList<MlMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6787933564954722125L;

	// private final List<MlMessage> list = new ArrayList<MlMessage>();

	public MlListeMessage() {

	}

	public List<MlMessage> getlist() {
		return this;
	}

	public int getTailleListe() {
		return this.size();
	}

	public boolean contains(Message p_messageJavaMail) {
		Folder folder = p_messageJavaMail.getFolder();
		for (MlMessage m : this) {
			try {

				if (folder instanceof POP3Folder) {

					if (((POP3Folder) folder).getUID(p_messageJavaMail).equals(
							m.getUIDMessage())) {
						return true;
					}

				} else if (folder instanceof IMAPFolder) {
					String uid;

					uid = "" + ((IMAPFolder) folder).getUID(p_messageJavaMail);

					if (uid.equals(m.getUIDMessage())) {
						return true;
					}
				} else {
					return false;
				}
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;

	}
}
