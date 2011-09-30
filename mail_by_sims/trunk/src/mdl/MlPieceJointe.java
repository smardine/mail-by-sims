package mdl;

import java.io.File;
import java.util.ArrayList;

import bdd.BDRequette;

public class MlPieceJointe {

	private int idPiecejointe;
	private String nomPieceJointe;
	private int idMessageParent;
	private File contenuPieceJointe;

	public MlPieceJointe(int p_idPiecejointe) {
		this.idPiecejointe = p_idPiecejointe;
		BDRequette bd = new BDRequette();
		ArrayList<String> defPj = bd.getPieceJointeById(idPiecejointe);
		for (int i = 0; i < defPj.size(); i++) {
			switch (i) {
				case 0:
					setNomPieceJointe(defPj.get(0));
					break;
				case 1:
					setIdMessageParent(Integer.parseInt(defPj.get(1)));
					break;

			}
		}// fin de for
		bd.closeConnexion();
	}

	public MlPieceJointe(File p_fichier) {
		setContenuPieceJointe(p_fichier);
		setNomPieceJointe(p_fichier.getName());
	}

	/**
	 * @param nomPieceJointe the nomPieceJointe to set
	 */
	public void setNomPieceJointe(String nomPieceJointe) {
		this.nomPieceJointe = nomPieceJointe;
	}

	/**
	 * @return the nomPieceJointe
	 */
	public String getNomPieceJointe() {
		return nomPieceJointe;
	}

	/**
	 * @param p_idMessageParent the p_idMessageParent to set
	 */
	public void setIdMessageParent(int p_idMessageParent) {
		this.idMessageParent = p_idMessageParent;
	}

	/**
	 * @return the p_idMessageParent
	 */
	public int getIdMessageParent() {
		return idMessageParent;
	}

	public File getContenuPiecejointe() {
		if (contenuPieceJointe == null || !contenuPieceJointe.exists()) {
			BDRequette bd = new BDRequette();
			setContenuPieceJointe(bd
					.getContenuPieceJointeFromIdPieceJointeId(this));
			bd.closeConnexion();

		}
		return contenuPieceJointe;

	}

	/**
	 * @param contenuPieceJointe the contenuPieceJointe to set
	 */
	public void setContenuPieceJointe(File contenuPieceJointe) {
		this.contenuPieceJointe = contenuPieceJointe;
	}

	/**
	 * @return the idPiecejointe
	 */
	public int getIdPiecejointe() {
		return this.idPiecejointe;
	}

}
