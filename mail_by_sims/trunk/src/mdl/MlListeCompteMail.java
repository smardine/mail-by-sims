package mdl;

import java.util.ArrayList;

import bdd.BDRequette;

public class MlListeCompteMail extends ArrayList<MlCompteMail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1950310049081717436L;

	// private final ArrayList<MlCompteMail> list = new
	// ArrayList<MlCompteMail>();

	public MlListeCompteMail() {
		BDRequette bd = new BDRequette();
		this.addAll(bd.getListeDeComptes());
		bd.closeConnexion();
	}

	public int getSize() {
		return this.size();
	}

}
