package mdl;

import java.util.ArrayList;

import bdd.accestable.piece_jointe.AccesTablePieceJointe;

public class MlListePieceJointe extends ArrayList<MlPieceJointe> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5990457445790116461L;

	public MlListePieceJointe(int p_idMessage) {
		AccesTablePieceJointe accesPJ = new AccesTablePieceJointe();
		ArrayList<String> lstResult = accesPJ
				.getListeIdPieceJointe(p_idMessage);

		for (String s : lstResult) {
			this.add(new MlPieceJointe(Integer.parseInt(s)));
		}

	}

	public int getNbPieceJointe() {
		return this.size();
	}

}
