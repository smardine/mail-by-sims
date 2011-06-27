/**
 * 
 */
package imap;

import imap.util.messageUtilisateur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SubjectTerm;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

import tools.GestionRepertoire;
import tools.WriteFile;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

import fenetre.principale.jTable.MyTableModel;

/**
 * @author smardine
 */
public class imap {
	protected MyTableModel tablemodel;
	protected JEditorPane editor;
	private final String idCompte;
	private final String user;
	private final String password;
	private final String host;
	private final static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public imap(MyTableModel model, String p_idCompte, String p_user,
			String p_password, String p_host) {
		this.tablemodel = model;
		this.user = p_user;
		this.password = p_password;
		this.host = p_host;
		this.idCompte = p_idCompte;
		imap.main(model, editor, idCompte);
	}

	/**
	 * @param args
	 */

	public static void main(MyTableModel model, JEditorPane editor,
			String p_idCompte) {
		// TODO Auto-generated method stub

		// SUBSTITUTE YOUR ISP's POP3 SERVER HERE!!!
		String host = "imap.gmail.com";
		// SUBSTITUTE YOUR USERNAME AND PASSWORD TO ACCESS E-MAIL HERE!!!
		String user = "s.mardine@gmail.com";
		String password = "&Gouranga2010";
		// SUBSTITUTE YOUR ISP's POP3 SERVER HERE!!!
		// String host = "pop3.club-internet.fr";
		// // SUBSTITUTE YOUR USERNAME AND PASSWORD TO ACCESS E-MAIL HERE!!!
		// String user = "sims26";
		// String password = "gouranga";
		// SUBSTITUTE YOUR SUBJECT SUBSTRING TO SEARCH HERE!!!
		String subjectSubstringToSearch = "iphondroid";

		// Get a session. Use a blank Properties object.

		Properties props = System.getProperties();

		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imaps.partialfetch", "false");
		Session session = Session.getInstance(props);
		try {

			// Get a Store object
			Store store = session.getStore("imaps");
			store.connect(host, user, password);

			// Get "INBOX"
			IMAPFolder fldr = (IMAPFolder) store.getFolder("INBOX");

			fldr.open(Folder.READ_WRITE);
			int count = fldr.getMessageCount();
			System.out.println(count + " total messages");

			// Message numebers start at 1

			for (Message m : fldr.getMessages()) {

				// Get some headers

				@SuppressWarnings("unused")
				Object content = m.getContent();
				Date date = m.getSentDate();
				Address[] from = m.getFrom();
				Address[] receiver = m.getAllRecipients();
				String subj = m.getSubject();
				String mimeType = m.getContentType();
				long uid = fldr.getUID(m);
				String d = df.format(date);

				Enumeration<Header> allHeaders = m.getAllHeaders();
				StringBuilder sb = new StringBuilder();

				while (allHeaders.hasMoreElements()) {
					Header e = allHeaders.nextElement();
					sb.append(e.getName() + ": " + e.getValue() + "\r\n");

				}
				boolean complet = true;
				// long dateActu = new Date().getTime();
				String contenuComplet = recupContenuMail(m, complet, uid);
				complet = false;
				String contenu = recupContenuMail(m, complet, uid);
				sb.append("\r\n" + contenuComplet);

				// creation du message:
				if (sb != null) {
					WriteFile.WriteFullFile(sb.toString(), GestionRepertoire
							.RecupRepTravail()
							+ "/eml/" + uid + ".eml");
				}
				if (contenu != null) {
					WriteFile.WriteFullFile(contenu, GestionRepertoire
							.RecupRepTravail()
							+ "/html/" + uid + ".html");
					// editor.setText(contenu);
					editor.setPage(new URL("file:///"
							+ GestionRepertoire.RecupRepTravail() + "/html/"
							+ uid + ".html"));
				}
				StringBuilder sbRequette = new StringBuilder();
				sbRequette
						.append("INSERT INTO MAIL_RECU (ID_COMPTE, ID_DOSSIER_STOCKAGE, UID_MESSAGE, EXPEDITEUR, DESTINATAIRE, SUJET, CONTENU, DATE_RECEPTION)  VALUES (");
				sbRequette.append("'" + p_idCompte + "','1'," + uid + ",'"
						+ from.toString() + "','" + receiver.toString() + "','"
						+ subj + "','" + contenu + "'," + date + ")");
				BDRequette.executeRequete(sbRequette.toString());

			}

			// Search for e-mails by some subject substring
			String pattern = subjectSubstringToSearch;
			SubjectTerm st = new SubjectTerm(pattern);
			// Get some message references
			Message[] found = fldr.search(st);

			System.out.println(found.length
					+ " messages matched Subject pattern \"" + pattern + "\"");

			for (int i = 0; i < found.length; i++) {
				StringBuilder sb = new StringBuilder();
				Message m = found[i];
				// Get some headers

				// Uncomment to set "delete" flag on the message
				// m.setFlag(Flags.Flag.DELETED,true);

			} // End of for

			// "true" actually deletes flagged messages from folder
			fldr.close(true);
			store.close();
			System.out.println("close");

		} catch (MessagingException mex) {
			// Prints all nested (chained) exceptions as well
			messageUtilisateur.affMessageException(mex, "Message Exception");
		} catch (IOException ioex) {
			messageUtilisateur.affMessageException(ioex,
					"erreur lors de la releve des messages");
		}
	}

	public static String recupContenuMail(Message p_message, boolean p_complet,
			long p_prefixeNomFichier) throws MessagingException, IOException {
		StringBuilder sb = new StringBuilder();
		int messageNumber = p_message.getMessageNumber();
		String messageName = p_message.getFileName();
		// Enumeration<Header> allHeaders = p_message.getAllHeaders();
		if (p_complet) {
			System.out.println("n° message: " + messageNumber
					+ "\rnom du message: " + messageName + "\r ");
		}
		Object o = p_message.getContent();
		if (o instanceof String) {

			sb.append((String) o);

		} else if (o instanceof Multipart) {
			Multipart mp = (Multipart) o;
			decodeMultipart(mp, p_complet, sb, p_prefixeNomFichier);

		} else if (o instanceof InputStream) {
			System.out.println("on ne devrait jamais passé par là");
			// recuperePieceJointe(p_complet, p_prefixeNomFichier, b, o);
		}
		return sb.toString();
	}

	public static void decodeMultipart(Multipart mp, boolean p_complet,
			StringBuilder sb, long p_prefixeNomFichier)
			throws MessagingException, IOException {
		for (int j = 0; j < mp.getCount(); j++) {
			// Part are numbered starting at 0
			BodyPart b = mp.getBodyPart(j);
			String contentType = b.getContentType();
			Object o2 = b.getContent();
			if (o2 instanceof String) {
				if (!p_complet && j == mp.getCount() - 1) {
					// on ne veut que la partie html du message
					sb.append(o2);
				}
				if (p_complet) {// on veut tout
					sb.append(o2 + "\r\n");
				}

			} else if (o2 instanceof Multipart) {
				System.out.print("**MultiPart Imbriqué.  ");
				Multipart mp2 = (Multipart) o2;
				decodeMultipart(mp2, p_complet, sb, p_prefixeNomFichier);

			} else if (o2 instanceof InputStream) {

				recuperePieceJointe(p_complet, p_prefixeNomFichier, b, o2);

			}

		}

	}

	/**
	 * @param p_complet
	 * @param p_prefixeNomFichier
	 * @param p_bodyPart
	 * @param p_inputStream
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void recuperePieceJointe(boolean p_complet,
			long p_prefixeNomFichier, BodyPart p_bodyPart, Object p_inputStream)
			throws MessagingException, FileNotFoundException {

		if (p_complet) {// on ne recupere la piece jointe
			// qu'une seule fois
			InputStream input = (InputStream) p_inputStream;
			String fileName = p_bodyPart.getFileName();

			if (fileName != null) {
				fileName = fileName.substring(p_bodyPart.getFileName()
						.lastIndexOf("\\") + 1);
			} else {
				fileName = "inconnu";
			}
			System.out.println("**C'est une piece jointe dont le nom est :"
					+ fileName);
			if (fileName.contains("ISO")) {
				fileName = decodeurIso(fileName);
			}

			File fichier = new File(GestionRepertoire.RecupRepTravail()
					+ "/pieces jointes/" + p_prefixeNomFichier + "_" + fileName);
			if (!fichier.exists()) {
				FileOutputStream writeFile = new FileOutputStream(fichier);
				byte[] buffer = new byte[p_bodyPart.getSize()];
				int read;

				try {
					while ((read = input.read(buffer)) != -1) {
						writeFile.write(buffer, 0, read);

					}
					writeFile.flush();
					writeFile.close();
					input.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e,
							"Impossible de recuperer le fichier joint",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					if (writeFile != null) {
						try {
							writeFile.close();
							input.close();
						} catch (IOException e) {
							JOptionPane
									.showMessageDialog(
											null,
											e,
											"Impossible de fermer les flux lors de la recuperation du fichier joint",
											JOptionPane.ERROR_MESSAGE);
						}

					}
				}
			}

		}
	}

	public static String decodeurIso(String fileName) {

		fileName = fileName.replaceAll("ISO-8859-1", "").replace("?", "")
				.replace("=", "").replace(",", "");

		return fileName;
	}

}
