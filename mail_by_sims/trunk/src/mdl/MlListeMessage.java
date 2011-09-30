package mdl;

import java.util.ArrayList;

import bdd.BDRequette;

public class MlListeMessage extends ArrayList<MlMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6787933564954722125L;
	private int idCompte;
	private int idDossierParent;

	public MlListeMessage(int p_idCompte, int p_idDossier) {

		this.idCompte = p_idCompte;
		this.idDossierParent = p_idDossier;
		BDRequette bd = new BDRequette();
		ArrayList<String> lstResultat = bd.getListeIDMessage(idCompte,
				idDossierParent);
		bd.closeConnexion();

		for (String idMessage : lstResultat) {
			this.add(new MlMessage(Integer.parseInt(idMessage)));
		}

	}

	public MlListeMessage() {

	}

	public MlListeMessage getlist() {
		return this;
	}

	public int getTailleListe() {
		return this.size();
	}

	/**
	 * @param idCompte the idCompte to set
	 */
	public void setIdCompte(int idCompte) {
		this.idCompte = idCompte;
	}

	/**
	 * @return the idCompte
	 */
	public int getIdCompte() {
		return idCompte;
	}

	/**
	 * @param idCompte the idCompte to set
	 */
	public void setIdDossierParent(int p_idDossierParent) {
		this.idDossierParent = p_idDossierParent;
	}

	/**
	 * @return the idCompte
	 */
	public int getIdDossierParent() {
		return idDossierParent;
	}

}
