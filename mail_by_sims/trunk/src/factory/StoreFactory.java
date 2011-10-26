/**
 * 
 */
package factory;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import mdl.MlCompteMail;

/**
 * @author smardine
 */
public class StoreFactory {
	private final MlCompteMail compteMail;
	private Properties props;
	private Store store;

	public StoreFactory(MlCompteMail p_compteMail) {
		this.compteMail = p_compteMail;
	}

	private Store getStore() throws MessagingException {
		switch (compteMail.getTypeCompte()) {
			case IMAP:
			case GMAIL:
				props = System.getProperties();
				props.setProperty("mail.store.protocol", "imaps");
				props.setProperty("mail.imap.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.setProperty("mail.imaps.partialfetch", "false");

				Session session = Session.getInstance(props);
				// Get a Store object
				store = session.getStore("imaps");

				return store;
			case POP:
				break;
			case HOTMAIL:
				break;
		}
		return null;
	}

	public Store getConnectedStore() throws MessagingException {
		getStore();
		if (!store.isConnected()) {
			store.connect(compteMail.getServeurReception(), compteMail
					.getUserName(), compteMail.getPassword());

		}
		return store;

	}

}
