package Mail;

/* 
 * MailReceiver
 * Created on 28 oct. 2005
 * @author Toon from insia
 */
 

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import com.sun.net.ssl.internal.ssl.Provider;


public class MailReceiver {
    final private static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    final private static int DEFAULT_POP_PORT = 110;
    
    final private static int DEFAULT_POP_SSL_PORT = 995;
    
    final private String _popHost;
    
    final private String _popPort;
    
    final private String _userName;
    
    final private String _password;
    
    final private Properties _props;
    
    final private Store _store;
	
    public MailReceiver(final String host, final String userName,
            final String password, final int port, final boolean ssl)
    throws NoSuchProviderException {
        _popHost = host;
        _popPort = String.valueOf(port);
        _userName = userName;
        _password = password;
	    
	    _props = new Properties();
	    if (ssl) {
	        // Connection SSL
	      //  Security.addProvider(new Provider());  	  
	        _props.put("mail.pop3.socketFactory.class", SSL_FACTORY);
	        _props.put("mail.pop3.socketFactory.fallback", "false");
	        _props.put("mail.pop3.port", _popPort);
	        _props.put("mail.pop3.socketFactory.port", _popPort);
	    }
	    
	    final Session session = Session.getDefaultInstance(_props, null);
	    final URLName urln = new URLName("pop3", host, port, null, userName,
	            password);
	    _store = session.getStore(urln);
	}
    
    public MailReceiver(final String host, final String userName,
            final String password, final boolean ssl)
    throws NoSuchProviderException {
        this(host, userName, password, 
                (ssl ? DEFAULT_POP_SSL_PORT : DEFAULT_POP_PORT), ssl);
    }
    
    public MailReceiver(final String host, final String userName,
            final String password, final int port)
    throws NoSuchProviderException {
        this(host, userName, password, port, DEFAULT_POP_SSL_PORT == port);
    }
    
    public MailReceiver(final String host, final String userName,
            final String password)
    throws NoSuchProviderException {
        this(host, userName, password, DEFAULT_POP_PORT, false);
    }
    
    public MailMessage[] getMessages()
    throws UnsupportedEncodingException, IOException, MessagingException {
        MailMessage[] results = null;
        try {
	        _store.connect();
	        final Folder inbox = _store.getFolder("INBOX");
	        try {
		        inbox.open(Folder.READ_ONLY);
		        final Message[] messages = inbox.getMessages();
		        results = new MailMessage[messages.length];
		        for (int i = 0; i < messages.length; ++i)
		            results[i] = new MailMessage(messages[i]);
	        } finally {
	            inbox.close(false);
	        }
        } finally {
            _store.close();
        }
        return results;
    }
    
    
    public static void main(final String[] args)
    throws UnsupportedEncodingException, IOException, MessagingException {
        // Connexion
        final MailReceiver gmail = new MailReceiver("pop.xxxxxx.xxx",
                "xxxxxx", "xxxxxx");
        // Réception
        final MailMessage[] messages = gmail.getMessages();
        // Affichage
        final SimpleDateFormat datFormat =
            new SimpleDateFormat("'le' dd/MM/yyyy 'à' HH:mm");
        for (int i = 0; i < messages.length; ++i) {
            System.out.print(datFormat.format(messages[i].getSendDate()));
            System.out.print(" De: " + messages[i].getFrom().getAddress());
            System.out.print(" à: "); 
            final InternetAddress[] to = messages[i].getTo();
            for (int j = 0; j < to.length; ++j)
                System.out.print(to[i].getAddress() +
                        (to.length - 1 != j ? ", " : "\n"));
            System.out.println(" - " + messages[i].getSubject() + "\n");
        }
    }
}