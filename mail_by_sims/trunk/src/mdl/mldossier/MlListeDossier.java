package mdl.mldossier;

import java.util.ArrayList;
import java.util.List;


public class MlListeDossier extends ArrayList<MlDossier> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1583001959267990227L;

	public MlListeDossier() {

	}

	public List<MlDossier> getlist() {
		return this;
	}

	public int getTailleListe() {
		return this.size();
	}

	public boolean contains(MlDossier p_dossier) {

		for (MlDossier m : this) {
			if (m.getIdDossier() == p_dossier.getIdDossier()) {
				return true;
			}
		}

		return false;

	}
}
