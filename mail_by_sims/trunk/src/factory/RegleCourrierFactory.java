/**
 * 
 */
package factory;

import javax.mail.Message;

import mdl.MlCompteMail;

/**
 * Cette classe s'occupent de tout ce qui a trait aux regles de messageries
 * (gestion des spam, deplacement des messages dans un dossier en fonction de
 * l'expediteur...)
 * @author smardine
 */
public class RegleCourrierFactory {

	@SuppressWarnings("unused")
	private final MlCompteMail compteMail;
	@SuppressWarnings("unused")
	private final Message message;
	private final int idDossier;
	@SuppressWarnings("unused")
	private final com.googlecode.jdeltasync.Message deltaMessage;

	/**
	 * Contructeur pour les message de type {@link Message}
	 * @param p_cptMail - Le compte mail concerné
	 * @param p_message - le message a analyser
	 * @param p_idDossier - l'id du dossier de stockage actuel du message
	 */
	public RegleCourrierFactory(MlCompteMail p_cptMail, Message p_message,
			int p_idDossier) {
		this.compteMail = p_cptMail;
		this.message = p_message;
		this.idDossier = p_idDossier;
		this.deltaMessage = null;
	}

	/**
	 * Contructeur pour les message de type
	 * {@link com.googlecode.jdeltasync.Message}
	 * @param p_cptMail - Le compte mail concerné
	 * @param p_message - le message a analyser
	 * @param p_idDossier - l'id du dossier de stockage actuel du message
	 */
	public RegleCourrierFactory(MlCompteMail p_compteMail,
			com.googlecode.jdeltasync.Message p_message, int p_idDossier) {
		this.compteMail = p_compteMail;
		this.deltaMessage = p_message;
		this.idDossier = p_idDossier;
		this.message = null;
	}

	/**
	 * Connaitre le dossier de destination du message
	 * @return l'id de dossier correspondant
	 */
	public int getIdDestinationCourrier() {
		if (idDossier != 0) {
			return idDossier;
		}
		return 7;

	}

}
