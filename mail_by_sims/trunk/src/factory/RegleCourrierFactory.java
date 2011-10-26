/**
 * 
 */
package factory;

import javax.mail.Message;

import mdl.MlCompteMail;

/**
 * @author smardine
 */
public class RegleCourrierFactory {

	@SuppressWarnings("unused")
	private final MlCompteMail compteMail;
	@SuppressWarnings("unused")
	private final Message message;
	private final int idDossier;
	private final com.googlecode.jdeltasync.Message deltaMessage;

	public RegleCourrierFactory(MlCompteMail p_cptMail, Message p_message,
			int p_idDossier) {
		this.compteMail = p_cptMail;
		this.message = p_message;
		this.idDossier = p_idDossier;
		this.deltaMessage = null;
	}

	/**
	 * @param p_compteMail
	 * @param p_m
	 * @param p_idDossier
	 */
	public RegleCourrierFactory(MlCompteMail p_compteMail,
			com.googlecode.jdeltasync.Message p_m, int p_idDossier) {
		this.compteMail = p_compteMail;
		this.deltaMessage = p_m;
		this.idDossier = p_idDossier;
		this.message = null;
	}

	public int getIdDestinationCourrier() {
		if (idDossier != 0) {
			return idDossier;
		}
		return 7;

	}

}
