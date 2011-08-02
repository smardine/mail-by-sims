package Pop3;

/**
 * ClientMail.java Client simple pour serveur pop3
 * @author ISSAMBN pushmailp3a@gmail.com comericsson
 */

import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import Pop3.util.methodePop;

import com.sun.mail.pop3.POP3Folder;

public class ClientMail {

	/** Dans cette chaine c'est le nom du serveur courier */

	private final String host;
	private final String Identifiant;
	private final String MotDePasse;
	private final JProgressBar progressReleve;
	private final JProgressBar progressPieceJointe;
	private final JTextArea text;
	private final MlCompteMail comptePop;

	public ClientMail(MlCompteMail p_compte, JProgressBar p_progress,
			JProgressBar p_progressPieceJointe, JTextArea p_textArea) {
		this.Identifiant = p_compte.getUserName();
		this.MotDePasse = p_compte.getPassword();
		this.host = p_compte.getServeurReception();
		this.progressReleve = p_progress;
		this.progressPieceJointe = p_progressPieceJointe;
		this.text = p_textArea;
		this.comptePop = p_compte;
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
			st.connect(host, Identifiant, MotDePasse);
			System.out.println("Connexion ok");
			System.out.println("st=:" + st);
			System.out.println("Obtention d'un folder");
			POP3Folder f = (POP3Folder) st.getFolder("INBOX");
			methodeImap.afficheText(text, "Ouverture de la boite de reception");
			f.open(Folder.READ_ONLY);// ouverture de INBOX
			methodePop.releveCompte(comptePop, f, text, progressReleve,
					progressPieceJointe);

			f.close(false);
			st.close();

		} catch (NoSuchProviderException e) {
			methodeImap.afficheText(text, "connexion impossible au compte pop");
			messageUtilisateur.affMessageException(e, "Connexion impossible");
			return;

		} catch (MessagingException e) {
			methodeImap.afficheText(text, "releve de la boite impossible");
			messageUtilisateur.affMessageException(e,
					"releve de la boite impossible");
			return;
		}

		/* Récupération des messages */
		// System.out.println("Obtention des messages");
		//
		// Message Messages[] = null;
		//
		// try {
		// Messages = f.getMessages();
		//
		// System.out.println("Nombre de message(s) : " + f.getMessageCount());
		//
		// System.out.println("Nombre de nouveau messages : "
		// + f.getNewMessageCount());
		//
		// for (Message mess : Messages) {
		// // System.out.println(mess.getFileName());
		// System.out.println("dossier: " + mess.getFolder());
		// System.out.println("sujet: " + mess.getSubject());
		// System.out.println("date d'envoi: " + mess.getSentDate());
		// Address[] listAdressFrom = mess.getFrom();
		// System.out.println("from: " + listAdressFrom[0].toString());
		// Address[] listAdress = mess.getAllRecipients();
		// for (Address adress : listAdress) {
		// System.out.println("to: " + adress);
		// }
		//
		// }
		// System.out.println("liste des nouveau messages");
		//
		// } catch (MessagingException e) {
		//
		// System.out.println(e.getMessage());
		//
		// try {
		// st.close();
		// } catch (MessagingException e1) {
		// System.out.println(e1.getMessage());
		//
		// }
		// return;
		//
		// }

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
