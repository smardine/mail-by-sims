package imap;

import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.posisoft.jdavmail.JDAVMailStore;

public class TestReceive {
	public static void main(String[] args) {

		try {
			Properties prop = System.getProperties();
			Enumeration<Object> elements = prop.elements();
			while (elements.hasMoreElements()) {
				String key = elements.nextElement().toString();
				System.out.println(key + ": " + prop.get(key));
			}
			prop.setProperty("mail.store.protocol", "davmail");

			Session ses = Session.getInstance(prop);
			// store = ses.getStore("davmail");

			// new JDAVMailStore(ses, null);
			JDAVMailStore store = new JDAVMailStore(ses, null);
			store.connect(null, "simon.mardine@hotmail.com", "gouranga08");
			if (store.isConnected()) {
				Folder inbox = store.getFolder("INBOX");
				if (inbox.exists()) {
					inbox.open(Folder.READ_ONLY);
					int nCount = inbox.getMessageCount();
					System.out
							.println("Inbox contains " + nCount + " messages");

					// Get the last message in the Inbox
					MimeMessage msg = (MimeMessage) inbox.getMessage(nCount);
					System.out.println("Subject : " + msg.getSubject());
					System.out.println("From : " + msg.getFrom()[0].toString());
					System.out
							.println("Content type : " + msg.getContentType());

					System.out.println(msg.getContent());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
