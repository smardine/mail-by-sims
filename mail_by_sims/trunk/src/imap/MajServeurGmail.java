package imap;

import fenetre.comptes.EnDossierBase;
import imap.util.messageUtilisateur;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JProgressBar;

import mdl.MlListeMessage;
import mdl.MlMessage;

import com.sun.mail.imap.IMAPFolder;

public class MajServeurGmail {

	private final int idCpt;
	private final String user;
	private final String pass;
	private final String serveur;
	private final JProgressBar progressBar;
	private final MlListeMessage listeMessageASupprimer;

	public MajServeurGmail(MlListeMessage p_listeMessageASupprimer,
			int p_idCpt, String p_user, String p_pass, String p_serveur,
			JProgressBar p_progress) {
		this.listeMessageASupprimer = p_listeMessageASupprimer;
		this.idCpt = p_idCpt;
		this.user = p_user;
		this.pass = p_pass;
		this.serveur = p_serveur;
		this.progressBar = p_progress;
		try {
			Go();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la suppression des messages sur le serveur ");
		}
	}

	public void Go() throws MessagingException {
		Properties props = System.getProperties();

		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imaps.partialfetch", "false");

		Session session = Session.getInstance(props);
		// Get a Store object
		Store store = null;

		store = session.getStore("imaps");
		store.connect(serveur, user, pass);

		for (MlMessage m : listeMessageASupprimer) {
			IMAPFolder fldr = null;
			if (EnDossierBase.RECEPTION.getLib().equals(m.getNomDossier())) {
				// on ouvre le repertoire "INBOX"
				fldr = (IMAPFolder) store.getFolder("INBOX");
			} else if (EnDossierBase.CORBEILLE.getLib().equals(
					m.getNomDossier())) {
				fldr = (IMAPFolder) store.getFolder("[Gmail]/Corbeille");
			}
			fldr.open(Folder.READ_WRITE);
			Message messageServeur = fldr.getMessageByUID(Long.parseLong(m
					.getUIDMessage()));

			if (messageServeur != null) {
				messageServeur.setFlag(Flags.Flag.DELETED, true);
			}
			fldr.expunge();
			fldr.close(true);
		}
		store.close();

	}

}
