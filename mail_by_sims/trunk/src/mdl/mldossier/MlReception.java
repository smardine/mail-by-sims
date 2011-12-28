/**
 * 
 */
package mdl.mldossier;

import mdl.mlcomptemail.MlCompteMail;

/**
 * @author smardine
 */
public class MlReception extends MlDossier {

	public MlReception(MlCompteMail p_cptMail) {
		super(p_cptMail.getIdInbox());
	}

}
