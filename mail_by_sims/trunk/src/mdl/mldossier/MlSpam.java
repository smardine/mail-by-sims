/**
 * 
 */
package mdl.mldossier;

import mdl.mlcomptemail.MlCompteMail;

/**
 * @author smardine
 */
public class MlSpam extends MlDossier {

	public MlSpam(MlCompteMail p_cptMail) {
		super(p_cptMail.getIdSpam());
	}
}
