/**
 * 
 */
package releve.imap;

import java.io.IOException;

import javax.mail.MessagingException;

import mdl.mlcomptemail.MlCompteMail;
import releve.imap.util.messageUtilisateur;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncException;

import factory.ReleveFactory;
import fenetre.Patience;

/**
 * @author smardine
 */
public class ReleveGmail {
	/**
	 * 
	 */
	private static final String ERREUR_A_LA_RELEVE_DU_COMPTE = "Erreur a la releve du compte ";
	private final String TAG = this.getClass().getSimpleName();
	private final MlCompteMail cptMail;
	private final Patience fenetre;

	public ReleveGmail(MlCompteMail p_commpteMail, Patience p_fenetre) {
		this.cptMail = p_commpteMail;
		this.fenetre = p_fenetre;

		ReleveFactory releve = new ReleveFactory(cptMail, fenetre);
		try {
			releve.releveCourier();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ReleveGmail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ cptMail.getNomCompte());
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ReleveGmail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ cptMail.getNomCompte());
		} catch (AuthenticationException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ReleveGmail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ cptMail.getNomCompte());
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ReleveGmail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ cptMail.getNomCompte());
		}
	}

}
