/**
 * 
 */
package factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;

/**
 * @author smardine
 */
public class MessageFactory {
	public MessageFactory() {

	}

	public MlMessage createMessagePourBase(MlMessage p_message,
			JTextArea p_textArea, JProgressBar p_progressPJ) {
		final String TAG = "createMessagePourBase";
		/** On simule la reception d'un message */
		Properties props = System.getProperties();
		props.put("mail.host", "smtp.dummydomain.com");
		props.put("mail.transport.protocol", "smtp");

		Session mailSession = Session.getDefaultInstance(props, null);
		/***/
		// int messNumber = 1;

		String cheminPhysique = p_message.getCheminPhysique();
		InputStream source;

		try {
			source = new FileInputStream(cheminPhysique);
			MimeMessage mime;

			mime = new MimeMessage(mailSession, source);
			p_message.setSujet(mime.getSubject());
			p_message.setDateReception(mime.getSentDate());
			p_message.setExpediteur(mime.getFrom()[0].toString());
			// ******************************//
			try {
				// liste des destinataires
				valoriseListeDestCopyEtcache(mime, p_message);

			} catch (AddressException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"erreur a la recupération des adresses");

			}

			if (p_message.getUIDMessage() == null) {
				if (mime.getContentID() != null) {
					p_message.setUIDMessage(mime.getContentID());
				} else {
					p_message.setUIDMessage("" + System.currentTimeMillis());// getMessageID());
				}
			}

			/**
			 * il faut decoder le message de maniere a voir si il y a des piece
			 * jointe
			 */

			p_message.setContenu(importMail.thread_Import.recupContenuMail(
					p_message, p_progressPJ, mime, p_textArea));
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la récupération du message");
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la récupération du message");
		}
		return p_message;
	}

	/**
	 * @param p_mime
	 * @param p_message
	 * @return
	 * @throws MessagingException
	 */
	private void valoriseListeDestCopyEtcache(MimeMessage p_mime,
			MlMessage p_message) throws MessagingException {
		if (null != p_mime.getRecipients(RecipientType.TO)) {
			ArrayList<String> listeDestinataires = new ArrayList<String>(p_mime
					.getRecipients(RecipientType.TO).length);
			for (Address uneAdresse : p_mime.getRecipients(RecipientType.TO)) {
				listeDestinataires.add(uneAdresse.toString());
			}
			p_message.setDestinataire(listeDestinataires);
		}

		// liste des madresse en copy
		if (null != p_mime.getRecipients(RecipientType.CC)) {
			ArrayList<String> listCopyTo = new ArrayList<String>(p_mime
					.getRecipients(RecipientType.CC).length);
			for (Address uneAdress : p_mime.getRecipients(RecipientType.CC)) {
				listCopyTo.add(uneAdress.toString());
			}
			p_message.setDestinataireCopy(listCopyTo);
		}

		// liste des adresse en copie cachée
		if (null != p_mime.getRecipients(RecipientType.BCC)) {
			ArrayList<String> listBCC = new ArrayList<String>(p_mime
					.getRecipients(RecipientType.BCC).length);
			for (Address uneAdress : p_mime.getRecipients(RecipientType.BCC)) {
				listBCC.add(uneAdress.toString());
			}
			p_message.setDestinataireCache(listBCC);
		}
		return;
	}

}
