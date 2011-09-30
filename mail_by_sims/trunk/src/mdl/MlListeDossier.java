package mdl;

import java.util.ArrayList;

import bdd.BDRequette;

public class MlListeDossier extends ArrayList<MlDossier> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6785878149116351053L;

	// private final ArrayList<MlDossier> list = new ArrayList<MlDossier>();

	public MlListeDossier(int p_idCompte) {
		BDRequette bd = new BDRequette();
		// ArrayList<String> lstDossier = bd.getListeDossier(p_idCompte);
		for (String nomDossier : bd.getListeDossier(p_idCompte)) {
			this.add(new MlDossier(p_idCompte, bd.getIdDossier(nomDossier,
					p_idCompte)));
		}
		bd.closeConnexion();
	}

	public int getNbDossier() {
		return this.size();
	}

}
