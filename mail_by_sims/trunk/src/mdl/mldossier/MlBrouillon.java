/**
 * 
 */
package mdl.mldossier;

import mdl.mlcomptemail.MlCompteMail;

/**
 * @author smardine
 */
public class MlBrouillon extends MlDossier {

	public MlBrouillon(MlCompteMail p_cptMail) {
		super(p_cptMail.getIdBrouillons());
	}

}
