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

	public RegleCourrierFactory(MlCompteMail p_cptMail, Message p_message,
			int p_idDossier) {
		this.compteMail = p_cptMail;
		this.message = p_message;
		this.idDossier = p_idDossier;
	}

	public int getIdDestinationCourrier() {
		if (idDossier != 0) {
			return idDossier;
		}
		return 7;

	}

}
