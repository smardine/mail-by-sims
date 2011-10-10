package mdl;

import java.util.ArrayList;

import bdd.accestable.dossier.AccesTableDossier;

public class MlListeDossier extends ArrayList<MlDossier> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6785878149116351053L;

	// private final ArrayList<MlDossier> list = new ArrayList<MlDossier>();

	public MlListeDossier(int p_idCompte) {
		AccesTableDossier accesDossier = new AccesTableDossier();

		for (String nomDossier : accesDossier.getListeDossier(p_idCompte)) {
			this.add(new MlDossier(p_idCompte, accesDossier.getIdDossier(
					nomDossier, p_idCompte)));
		}

	}

	public int getNbDossier() {
		return this.size();
	}

}
