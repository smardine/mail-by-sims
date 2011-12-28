/**
 * 
 */
package mdl.mldossier;

import mdl.mlcomptemail.MlCompteMail;

/**
 * @author smardine
 */
public class MlEnvoye extends MlDossier {

	public MlEnvoye(MlCompteMail p_cptMail) {
		super(p_cptMail.getIdEnvoye());
	}

}
