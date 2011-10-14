package imap;

import fenetre.comptes.EnDossierBase;
import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

public class MajServeurGmail {
	private final String TAG = this.getClass().getSimpleName();
	private final JProgressBar progressBar;
	private MlListeMessage listeMessageASupprimer;
	private final boolean isSuppr;
	private final MlCompteMail compteMail;
	private final JTextArea text;

	public MajServeurGmail(MlListeMessage p_listeMessageASupprimer,
			MlCompteMail p_compteMail, JProgressBar p_progress,
			JTextArea p_text, boolean p_SupprOuDepl) {
		this.listeMessageASupprimer = p_listeMessageASupprimer;
		this.compteMail = p_compteMail;
		this.progressBar = p_progress;
		this.isSuppr = p_SupprOuDepl;
		this.text = p_text;
		if (isSuppr) {
			try {
				LanceSuppression();
			} catch (MessagingException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Erreur a la suppression des messages sur le serveur ");
			}
		} else {
			LanceDeplacementVersCorbeille();
		}

	}

	public void LanceSuppression() throws MessagingException {
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
		store.connect(compteMail.getServeurReception(), compteMail
				.getUserName(), compteMail.getPassword());
		IMAPFolder fldr = null;
		int nbMessage = 0;
		for (MlMessage m : listeMessageASupprimer) {
			progressBar.setValue((100 * nbMessage++)
					/ listeMessageASupprimer.size());
			progressBar.setString((100 * nbMessage)
					/ listeMessageASupprimer.size() + " %");
			methodeImap.afficheText(text, "supression du message " + nbMessage
					+ " sur " + listeMessageASupprimer.size());
			if (EnDossierBase.RECEPTION.getLib().equals(m.getNomDossier())) {
				// on ouvre le repertoire "INBOX"
				fldr = (IMAPFolder) store.getFolder("INBOX");
			} else if (EnDossierBase.CORBEILLE.getLib().equals(
					m.getNomDossier())) {
				fldr = (IMAPFolder) store.getFolder("[Gmail]/Corbeille");
			}
			if (!fldr.isOpen()) {
				fldr.open(Folder.READ_WRITE);
			}

			Message messageServeur = fldr.getMessageByUID(Long.parseLong(m
					.getUIDMessage()));

			if (messageServeur != null) {
				messageServeur.setFlag(Flags.Flag.DELETED, true);
			}
		}

		fldr.expunge();
		fldr.close(true);

		store.close();

	}

	public void LanceDeplacementVersCorbeille() {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imaps.partialfetch", "false");

		Session session = Session.getInstance(props);
		// Get a Store object
		Store store = null;

		try {
			store = session.getStore("imaps");
			store.connect(compteMail.getServeurReception(), compteMail
					.getUserName(), compteMail.getPassword());

			MlMessage unMessage = listeMessageASupprimer.get(0);
			String nomDossierStockage = unMessage.getNomDossier();
			EnDossierBase dossierBase;
			IMAPFolder src = null;
			if (EnDossierBase.isDossierBase(nomDossierStockage)) {
				dossierBase = EnDossierBase.getDossierbase(nomDossierStockage);
				if (dossierBase != null) {
					src = EnDossierBase.getDossierGmail(dossierBase, store);
				} else {
					src = EnDossierBase.getSousDossierInbox(nomDossierStockage,
							unMessage.getIdCompte(), store);

				}

			}

			IMAPFolder dest = (IMAPFolder) store.getFolder("[Gmail]/Corbeille");
			methodeImap.afficheText(text, "maj du serveur ");
			listeMessageASupprimer = methodeImap.deplaceMessage(
					listeMessageASupprimer, src, dest, progressBar);
			BDRequette bd = new BDRequette();
			for (MlMessage m : listeMessageASupprimer) {
				bd.updateUIDMessage(m);
			}
			bd.closeConnexion();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
