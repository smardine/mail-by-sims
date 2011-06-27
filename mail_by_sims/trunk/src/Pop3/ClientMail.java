package Pop3;

/**
 * ClientMail.java Client simple pour serveur pop3
 * @author ISSAMBN pushmailp3a@gmail.com comericsson
 */

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;

public class ClientMail {

	/** Dans cette chaine c'est le nom du serveur courier */

	private final String host;
	private final String Identifiant;
	private final String MotDePasse;

	public ClientMail(String p_user, String p_password, String p_serveur) {
		this.Identifiant = p_user;
		this.MotDePasse = p_password;
		this.host = p_serveur;
		main();
	}

	public void main() {
		Properties prop = System.getProperties();
		Session sess = Session.getDefaultInstance(prop, null);
		sess.setDebug(true);

		// prop.list(System.out);

		/* No comment ;-) */

		/* Création de l'object qui va récupéré le contenu de la boite */
		// System.out.println("obtention d'un objet store");
		Store st = null;
		try {
			st = sess.getStore("pop3");
		} catch (NoSuchProviderException e1) {
			System.out.println(e1.getMessage());
			JOptionPane.showMessageDialog(null,
					"Le Test de la bal a echoué!! \n\r " + e1, "Erreur",
					JOptionPane.WARNING_MESSAGE, null);

			try {
				st.close();
			} catch (MessagingException e) {
				System.out.println(e.getMessage());
			}

		}

		/* Connection au serveur */
		// POP3SSLStore st = new POP3SSLStore(sess, new URLName(""));

		try {
			st.connect(host, Identifiant, MotDePasse);
		} catch (MessagingException e) {

			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null,
					"Le Test de la bal é echoué!! \n\r " + e, "Erreur",
					JOptionPane.WARNING_MESSAGE, null);

			try {
				st.close();
			} catch (MessagingException e1) {

				System.out.println(e1.getMessage());

			}

			return;

		}

		System.out.println("Connexion ok");

		System.out.println("st=:" + st);
		// modelDeListe.addElement("st=:" + st);

		System.out.println("Obtention d'un folder");

		/*
		 * Ouverture du répertoire contenent les mails Par defaut INBOX
		 */
		Folder f = null;
		try {
			f = st.getFolder("INBOX");
		} catch (MessagingException e) {

			System.out.println(e.getMessage());

			JOptionPane.showMessageDialog(null,
					"Le Test de la bal a echoué!! \n\r " + e, "Erreur",
					JOptionPane.WARNING_MESSAGE, null);

			try {
				st.close();
			} catch (MessagingException e1) {
				System.out.println(e1.getMessage());
			}
			return;
		}
		try {
			f.open(Folder.READ_ONLY);
		} catch (MessagingException e) {

			System.out.println(e.getMessage());

			JOptionPane.showMessageDialog(null,
					"Le Test de la bal a echoué!! \n\r " + e, "Erreur",
					JOptionPane.WARNING_MESSAGE, null);

			return;

		}

		/* Récupération des messages */
		System.out.println("Obtention des messages");

		Message Messages[] = null;

		try {
			Messages = f.getMessages();

			System.out.println("Nombre de message(s) : " + f.getMessageCount());

			System.out.println("Nombre de nouveau messages : "
					+ f.getNewMessageCount());

			for (Message mess : Messages) {
				// System.out.println(mess.getFileName());
				System.out.println("dossier: " + mess.getFolder());
				System.out.println("sujet: " + mess.getSubject());
				System.out.println("date d'envoi: " + mess.getSentDate());
				Address[] listAdressFrom = mess.getFrom();
				System.out.println("from: " + listAdressFrom[0].toString());
				Address[] listAdress = mess.getAllRecipients();
				for (Address adress : listAdress) {
					System.out.println("to: " + adress);
				}

			}
			System.out.println("liste des nouveau messages");

		} catch (MessagingException e) {

			System.out.println(e.getMessage());

			try {
				st.close();
			} catch (MessagingException e1) {
				System.out.println(e1.getMessage());

			}
			return;

		}

		/*
		 * for (int i = 0; i < msg.length; i++) { if
		 * (msg[i].isMimeType("text/plain")) { System.out.println("Expediteur: "
		 * + msg[i].getFrom()[0]); System.out.println("Sujet: " +
		 * msg[i].getSubject()); System.out.println("Texte: " + (String)
		 * msg[i].getContent()); }
		 */

		System.out.println("Fin des messages");

	}

} /* fin de main */
