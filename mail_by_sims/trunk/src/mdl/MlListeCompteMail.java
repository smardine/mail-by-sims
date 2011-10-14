package mdl;

import java.util.ArrayList;

public class MlListeCompteMail extends ArrayList<MlCompteMail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1950310049081717436L;
	private final ArrayList<MlCompteMail> list = new ArrayList<MlCompteMail>();

	public ArrayList<MlCompteMail> getlisteCompte() {
		return list;
	}

	public int getSize() {
		return list.size();
	}

}
