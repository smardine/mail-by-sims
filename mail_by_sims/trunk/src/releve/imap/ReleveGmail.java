/**
 * 
 */
package releve.imap;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import releve.imap.util.messageUtilisateur;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncException;

import factory.ReleveFactory;

/**
 * @author smardine
 */
public class ReleveGmail {
	private final String TAG = this.getClass().getSimpleName();
	private final JProgressBar progressBar;
	private final JProgressBar progressPJ;
	private final MlCompteMail cptMail;

	public ReleveGmail(int p_idCompte, String p_user, String p_password,
			String p_host, JProgressBar progress,
			JProgressBar p_progressPieceJointe, JTextArea textArea,
			boolean p_isSynchro) {
		this.cptMail = new MlCompteMail(p_idCompte);
		this.progressBar = progress;
		this.progressPJ = p_progressPieceJointe;

		ReleveFactory releve = new ReleveFactory(cptMail, progressBar,
				progressPJ, textArea);
		try {
			releve.releveCourier();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + cptMail.getNomCompte());
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + cptMail.getNomCompte());
		} catch (AuthenticationException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + cptMail.getNomCompte());
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + cptMail.getNomCompte());
		}
	}

}
