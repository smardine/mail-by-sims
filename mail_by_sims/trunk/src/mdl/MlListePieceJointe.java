package mdl;

import java.util.ArrayList;

import bdd.BDRequette;

public class MlListePieceJointe extends ArrayList<MlPieceJointe> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5990457445790116461L;

	public MlListePieceJointe(int p_idMessage) {
		BDRequette bd = new BDRequette();
		ArrayList<String> lstResult = bd.getListeIdPieceJointe(p_idMessage);
		bd.closeConnexion();
		for (String s : lstResult) {
			this.add(new MlPieceJointe(Integer.parseInt(s)));
		}

	}

	public int getNbPieceJointe() {
		return this.size();
	}

}
