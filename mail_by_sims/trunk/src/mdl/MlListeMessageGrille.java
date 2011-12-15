/**
 * 
 */
package mdl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

/**
 * @author smardine
 */
public class MlListeMessageGrille extends ArrayList<MlMessageGrille> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3351229409701816034L;

	public MlListeMessageGrille() {

	}

	public List<MlMessageGrille> getlist() {
		return this;
	}

	public int getTailleListe() {
		return this.size();
	}

	public boolean contains(Message p_messageJavaMail) {
		Folder folder = p_messageJavaMail.getFolder();
		for (MlMessageGrille m : this) {
			try {

				if (folder instanceof POP3Folder) {

					if (((POP3Folder) folder).getUID(p_messageJavaMail).equals(
							m.getUidMessage())) {
						return true;
					}

				} else if (folder instanceof IMAPFolder) {
					String uid;

					uid = "" + ((IMAPFolder) folder).getUID(p_messageJavaMail);

					if (uid.equals(m.getUidMessage())) {
						return true;
					}
				} else {
					return false;
				}
			} catch (MessagingException e) {

				e.printStackTrace();
			}

		}

		return false;

	}

}
