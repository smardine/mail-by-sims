/**
 * 
 */
package mdl.mldossier;

import mdl.mlcomptemail.MlCompteMail;

/**
 * @author smardine
 */
public class MlCorbeille extends MlDossier {
	public MlCorbeille(MlCompteMail p_cptMail) {
		super(p_cptMail.getIdCorbeille());
	}

}
