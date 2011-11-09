package mdl;

import java.util.List;

import bdd.accesTable.AccesTableDossier;
import bdd.accesTable.AccesTableMailRecu;

public class MlDossier {

	private final int idDossier;
	private int idDossierParent;
	private int idCompte;
	private String nomDossier;
	private String nomInternet;
	private final AccesTableDossier accesDossier;

	public MlDossier(int p_idDossier) {
		this.idDossier = p_idDossier;
		accesDossier = new AccesTableDossier();
		initialiseDossier(idDossier);
	}

	private void initialiseDossier(int p_idDossier) {

		List<String> defDossier = accesDossier.getDossierByID(p_idDossier);

		for (int i = 0; i < defDossier.size(); i++) {
			switch (i) {
				case 0:
					this.idCompte = Integer.parseInt(defDossier.get(0));
					break;
				case 1:
					this.idDossierParent = Integer.parseInt(defDossier.get(1));
					break;
				case 2:
					this.nomDossier = defDossier.get(2);
					break;
				case 3:
					this.nomInternet = defDossier.get(3);
					break;

			}// fin de switch
		}// fin de for

	}

	@Override
	public String toString() {
		int nb = getUnreadMessCount();
		if (nb > 0) {
			return nomDossier + " (" + nb + ")";
		}
		return nomDossier;

	}

	public int getIdDossierParent() {
		return idDossierParent;
	}

	public void setIdDossierParent(int p_idDossierParent) {
		idDossierParent = p_idDossierParent;
	}

	public int getIdCompte() {
		return idCompte;
	}

	public void setIdCompte(int p_idCompte) {
		idCompte = p_idCompte;
	}

	public String getNomDossier() {
		return nomDossier;
	}

	public void setNomDossier(String p_nomDossier) {
		nomDossier = p_nomDossier;
	}

	public String getNomInternet() {
		return nomInternet;
	}

	public void setNomInternet(String p_nomInternet) {
		nomInternet = p_nomInternet;
	}

	public MlListeDossier getListSousDossier() {
		return accesDossier.getListeSousDossier(idDossier);
	}

	public int getIdDossier() {
		return idDossier;
	}

	/**
	 * @return the unreadMessCount
	 */
	public int getUnreadMessCount() {
		int nb = accesDossier.getUnreadMessageFromFolder(idCompte, idDossier);
		for (MlDossier d : getListSousDossier()) {
			nb = nb + d.getUnreadMessCount();
		}
		return nb;
	}

	/**
	 * @return
	 */
	public MlListeMessage getListMessage() {
		return new AccesTableMailRecu().getListeDeMessage(idCompte, idDossier);
	}

}
