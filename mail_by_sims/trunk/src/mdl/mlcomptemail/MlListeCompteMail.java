package mdl.mlcomptemail;

import java.util.ArrayList;
import java.util.List;

public class MlListeCompteMail extends ArrayList<MlCompteMail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1950310049081717436L;
	private final List<MlCompteMail> list = new ArrayList<MlCompteMail>();

	public List<MlCompteMail> getlisteCompte() {
		return list;
	}

}
