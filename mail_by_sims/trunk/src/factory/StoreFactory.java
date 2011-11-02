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
 * Cette classe s'occupe de l'etablissment de connexion a un serveur
 * @author smardine
 */
public class StoreFactory {
	private final MlCompteMail compteMail;
	private Properties props;
	private Store store;

	/**
	 * Constructeur
	 * @param p_compteMail - le compte mail concerné
	 */
	public StoreFactory(MlCompteMail p_compteMail) {
		this.compteMail = p_compteMail;
	}

	/**
	 * @return l'objet {@link Store} NON CONNECTE
	 * @throws MessagingException
	 */
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
				props = System.getProperties();
				Session sess = Session.getDefaultInstance(props, null);
				sess.setDebug(false);
				store = sess.getStore("pop3");
				return store;
			default:
				break;
		}
		return null;
	}

	/**
	 * @return l'objet {@link Store} CONNECTE
	 * @throws MessagingException
	 */
	public Store getConnectedStore() throws MessagingException {
		getStore();
		if (store != null && !store.isConnected()) {
			store.connect(compteMail.getServeurReception(), compteMail
					.getUserName(), compteMail.getPassword());

		}
		return store;

	}

}
