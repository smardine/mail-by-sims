package releve.pop;

/**
 * ClientMail.java Client simple pour serveur pop3
 * @author ISSAMBN pushmailp3a@gmail.com comericsson
 */

import java.io.IOException;

import javax.mail.MessagingException;

import mdl.mlcomptemail.MlCompteMail;
import releve.imap.util.messageUtilisateur;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncException;

import factory.ReleveFactory;
import fenetre.Patience;

public class ClientMail {

	/**
	 * 
	 */
	private static final String ERREUR_A_LA_RELEVE_DU_COMPTE = "Erreur a la releve du compte ";
	private final String TAG = this.getClass().getSimpleName();

	private final MlCompteMail comptePop;
	private final Patience fenetre;

	public ClientMail(MlCompteMail p_compte, Patience p_fenetre) {
		this.fenetre = p_fenetre;

		this.comptePop = p_compte;

		// main();
		ReleveFactory releve = new ReleveFactory(comptePop, fenetre);
		try {
			releve.releveCourier();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ClientMail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ comptePop.getNomCompte());
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ClientMail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ comptePop.getNomCompte());
		} catch (AuthenticationException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ClientMail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ comptePop.getNomCompte());
		} catch (DeltaSyncException e) {
			messageUtilisateur.affMessageException(TAG, e,
					ClientMail.ERREUR_A_LA_RELEVE_DU_COMPTE
							+ comptePop.getNomCompte());
		}
	}

} /* fin de main */
