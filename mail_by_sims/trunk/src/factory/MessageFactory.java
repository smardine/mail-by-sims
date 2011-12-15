/**
 * 
 */
package factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.text.Document;

import mdl.ComposantVisuelCommun;
import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;
import tools.GestionRepertoire;
import tools.Historique;
import bdd.accesTable.AccesTableMailRecu;
import bdd.accesTable.AccesTablePieceJointe;
import fenetre.Patience;

/**
 * Cette classe s'occupent de tout ce qui a trait aux messages.
 * @author smardine
 */
public class MessageFactory {
	private final String TAG = this.getClass().getSimpleName();

	/**
	 * Constructeur
	 */
	public MessageFactory() {

	}

	/**
	 * Créer un nouvel enregistrement en base
	 * @param p_message - le message a enregistrer
	 * @param p_label - affiche des infos a l'utilisateur
	 * @param p_progressPJ - une barre de progression
	 * @return MlMessage créée.
	 */
	public MlMessage createMessagePourBase(MlMessage p_message,
			Patience p_fenetre) {

		/** On simule la reception d'un message */
		Properties props = System.getProperties();
		props.put("mail.host", "smtp.dummydomain.com");
		props.put("mail.transport.protocol", "smtp");
		Session mailSession = Session.getDefaultInstance(props, null);
		/***/
		String cheminPhysique = p_message.getCheminPhysique();
		InputStream source;
		try {
			source = new FileInputStream(cheminPhysique);
			MimeMessage mime;

			mime = new MimeMessage(mailSession, source);
			p_message.setSujet(mime.getSubject());
			p_message.setDateReception(mime.getSentDate());
			try {
				p_message.setExpediteur(mime.getFrom()[0].toString());

			} catch (AddressException e) {
				p_message.setExpediteur("inconnu");
			}

			// ******************************//
			try {
				// liste des destinataires
				valoriseListeDestCopyEtcache(mime, p_message);

			} catch (AddressException e) {
				Historique.ecrire(TAG + e.toString());
			}
			if (p_message.getUIDMessage() == null) {
				if (mime.getContentID() != null) {
					p_message.setUIDMessage(mime.getContentID());
				} else {
					p_message.setUIDMessage("" + System.currentTimeMillis());// getMessageID());
				}
			}

			p_message.setLu(mime.getFlags().contains(Flags.Flag.SEEN));

			/**
			 * il faut decoder le message de maniere a voir si il y a des piece
			 * jointe
			 */

			p_message.setContenu(recupContenuMail(p_fenetre, p_message, mime));
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
	 * Valorise {@link MlMessage} avec les listes de contact, contact en copie,
	 * contact caché
	 * @param p_mime - le message d'origine
	 * @param p_message - le message a destination de la base
	 * @return le {@link MlMessage} valorisé avec les listes de contacts
	 * @throws MessagingException - si une erreur survient
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

	private String recupContenuMail(Patience p_fenetre, MlMessage p_mlMessage,
			Message p_messageJavaMail) {
		StringBuilder sb = new StringBuilder();
		// int messageNumber = p_messageJavaMail.getMessageNumber();
		// String messageName = p_messageJavaMail.getFileName();
		p_fenetre.afficheInfo("Recupération du contenu du message", "", 0);
		Object o;
		try {
			o = p_messageJavaMail.getContent();
			if (o instanceof String) {
				sb.append((String) o);
			} else if (o instanceof Multipart) {
				Multipart mp = (Multipart) o;
				decodeMultipart(p_mlMessage, mp, sb, p_fenetre);// ,
				// p_prefixeNomFichier);

			} else if (o instanceof InputStream) {
				Historique.ecrire("on ne devrait jamais passer par là");

			}
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation du mail");
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la recuperation du mail");
		}

		return sb.toString();
	}

	private void decodeMultipart(MlMessage p_mlMessage, Multipart mp,
			StringBuilder sb, Patience p_fenetre) {
		try {
			for (int j = 0; j < mp.getCount(); j++) {
				// Part are numbered starting at 0
				BodyPart b = mp.getBodyPart(j);
				Object o2 = b.getContent();
				if (o2 instanceof String) {
					if (j == mp.getCount() - 1) {
						// on ne veut que la partie html du message
						sb.append(o2);
					}

				} else if (o2 instanceof Multipart) {
					Multipart mp2 = (Multipart) o2;
					decodeMultipart(p_mlMessage, mp2, sb, p_fenetre);
				} else if (o2 instanceof InputStream) {
					recuperePieceJointe(p_mlMessage, b, o2, p_fenetre);
				}

			}
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur Decodage MultiPart");
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur Decodage MultiPart");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur Decodage MultiPart");
		}

	}

	private void recuperePieceJointe(MlMessage p_mlMessage,
			BodyPart p_bodyPart, Object p_inputStream, Patience p_fenetre)
			throws MessagingException, FileNotFoundException {

		InputStream input = (InputStream) p_inputStream;
		String fileName = p_bodyPart.getFileName();

		fileName = traiteFileName(p_bodyPart, fileName);
		p_fenetre.afficheInfo("Récuperation d'une piece jointe", "", 0);

		// creation du repertoire qui va acceuillir les pieces jointes
		File repPieceJointe = new File(GestionRepertoire.RecupRepTravail()
				+ "/tempo/pieces jointes");
		if (!repPieceJointe.exists()) {
			repPieceJointe.mkdirs();
		}
		File fichier = new File(repPieceJointe.getAbsolutePath() + "/"
				+ fileName);
		if (!fichier.exists()) {

			FileOutputStream writeFile = new FileOutputStream(fichier);

			byte[] buffer = new byte[256 * 1024];// par segment de 256Ko
			int read;
			final long tailleTotale = p_bodyPart.getSize();
			try {
				while ((read = input.read(buffer)) != -1) {
					writeFile.write(buffer, 0, read);
					long tailleEnCours = fichier.length();
					long PourcentEnCours = ((100 * (tailleEnCours + 1)) / tailleTotale);
					int Pourcent = (int) PourcentEnCours;
					p_fenetre.afficheInfo("Récuperation d'une piece jointe",
							Pourcent + " %", Pourcent);

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
						p_mlMessage.getListePieceJointe().add(fichier);

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

	/**
	 * @param p_bodyPart
	 * @param fileName
	 * @return
	 * @throws MessagingException
	 */
	private static String traiteFileName(BodyPart p_bodyPart, String p_fileName)
			throws MessagingException {
		String nomFichier = p_fileName;
		if (nomFichier != null) {
			nomFichier = nomFichier.substring(p_bodyPart.getFileName()
					.lastIndexOf("\\") + 1);
		} else {
			nomFichier = "inconnu";
		}

		if (nomFichier.contains("ISO") || nomFichier.contains("UTF")
				|| nomFichier.contains("iso") || nomFichier.contains("utf")) {
			nomFichier = decodeurIso(nomFichier);
		}
		return nomFichier;
	}

	public static String decodeurIso(String p_fileName) {
		String s = p_fileName.replaceAll("=?ISO-8859-1?Q?", "").replaceAll(
				"=?iso-8859-1?Q?", "").replace("?", "").replace(",", "")
				.replaceAll("=?UTF-8?Q?", "").replaceAll("=?utf-8?Q?", "")
				.replace("=5F", "_").replace("=E9", "é").replace("=Q", "")
				.replace("=CC=81e", "é").replace("=", "").replace("2E", ".")
				.replaceAll("\t", "");
		return s;
	}

	public void afficheContenuMail(int p_idMessage) {

		// le n° du message (meme si il est caché).
		AccesTableMailRecu accesMail = new AccesTableMailRecu();
		File contenu = accesMail.getContenuFromIdForFile(p_idMessage, false);

		// on RAZ le contenu du panelEditor
		Document doc = ComposantVisuelCommun.getHtmlPane().getDocument();
		doc.putProperty(Document.StreamDescriptionProperty, null);
		if (contenu != null && contenu.exists()) {
			try {
				ComposantVisuelCommun.getHtmlPane().setPage(
						"file:///" + contenu.getAbsolutePath());
				// affichage des piece jointe dans la liste (si il y en a)
				AccesTablePieceJointe accesPJ = new AccesTablePieceJointe();
				List<String> lstPj = accesPJ
						.getListeNomPieceJointe(p_idMessage);
				DefaultListModel model = (DefaultListModel) ComposantVisuelCommun
						.getJListPJ().getModel();
				int nbLigne = model.getSize();
				if (nbLigne > 0) {// si la liste est deja repli, on la vide
					model.removeAllElements();
				}
				if (lstPj.size() > 0) {
					for (String s : lstPj) {
						model.addElement(s);
					}

				}
			} catch (IOException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"impossible d'afficher le mail");
			}

		} else {
			ComposantVisuelCommun.getJTable().removeAll();
			ComposantVisuelCommun.getJListPJ().removeAll();
			doc = ComposantVisuelCommun.getHtmlPane().getDocument();
			doc.putProperty(Document.StreamDescriptionProperty, null);
			try {
				ComposantVisuelCommun.getHtmlPane().setPage(
						"file:///" + GestionRepertoire.RecupRepTemplate()
								+ "/vide.html");
			} catch (IOException e1) {
				return;
			}
		}

	}
}
