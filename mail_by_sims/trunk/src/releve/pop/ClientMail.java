package releve.pop;

/**
 * ClientMail.java Client simple pour serveur pop3
 * @author ISSAMBN pushmailp3a@gmail.com comericsson
 */

import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import releve.imap.util.messageUtilisateur;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncException;

import factory.ReleveFactory;

public class ClientMail {

	private final String TAG = this.getClass().getSimpleName();
	private final JProgressBar progressReleve;
	private final JProgressBar progressPieceJointe;
	private final JTextArea text;
	private final MlCompteMail comptePop;

	public ClientMail(MlCompteMail p_compte, JProgressBar p_progress,
			JProgressBar p_progressPieceJointe, JTextArea p_textArea) {
		this.progressReleve = p_progress;
		this.progressPieceJointe = p_progressPieceJointe;
		this.text = p_textArea;
		this.comptePop = p_compte;

		// main();
		ReleveFactory releve = new ReleveFactory(comptePop, progressReleve,
				progressPieceJointe, text);
		try {
			releve.releveCourier();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + comptePop.getNomCompte());
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + comptePop.getNomCompte());
		} catch (AuthenticationException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + comptePop.getNomCompte());
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + comptePop.getNomCompte());
		}
	}

} /* fin de main */
