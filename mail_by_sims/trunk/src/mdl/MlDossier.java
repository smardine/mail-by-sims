package mdl;

import java.util.List;

import bdd.accesTable.AccesTableDossier;
import bdd.accesTable.AccesTableMailRecu;

public class MlDossier {

	private int idDossier;
	private int idDossierParent;
	private int idCompte;
	private int unreadMessageCount;
	private String nomDossier;
	private String nomInternet;
	private final AccesTableDossier accesDossier;
	private final AccesTableMailRecu accesMailRecu;

	public MlDossier(int p_idDossier) {
		this.idDossier = p_idDossier;
		accesDossier = new AccesTableDossier();
		accesMailRecu = new AccesTableMailRecu();
		initialiseDossier(idDossier);
	}

	/**
	 * 
	 */
	public MlDossier() {
		accesDossier = new AccesTableDossier();
		accesMailRecu = new AccesTableMailRecu();
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

		this.unreadMessageCount = accesDossier.getUnreadMessageFromFolder(
				idCompte, idDossier);

	}

	@Override
	public String toString() {
		if (unreadMessageCount > 0) {
			return nomDossier + " (" + unreadMessageCount + ")";
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
	 * @param p_idDossier the idDossier to set
	 */
	public void setIdDossier(int p_idDossier) {
		this.idDossier = p_idDossier;
	}

	/**
	 * @return
	 */
	public MlListeMessage getListMessage() {
		return accesMailRecu.getListeDeMessage(idCompte, idDossier);
	}

	/**
	 * @return the unreadMessageCount
	 */
	public int getUnreadMessageCount() {
		return this.unreadMessageCount;
	}

	/**
	 * @param p_unreadMessageCount the unreadMessageCount to set
	 */
	public void setUnreadMessageCount(int p_unreadMessageCount) {
		this.unreadMessageCount = p_unreadMessageCount;
	}

	@Override
	public boolean equals(Object p_object) {
		if (idDossier == ((MlDossier) p_object).getIdDossier()) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
