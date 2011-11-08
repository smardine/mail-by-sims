package mdl;

import java.util.List;

import bdd.BDRequette;

public class MlDossier {

	private final int idDossier;
	private int idDossierParent;
	private int idCompte;
	private String nomDossier;
	private String nomInternet;
	private MlListeDossier listSousDossier;
	private int unreadMessCount;
	private MlListeMessage listMessage;

	public MlDossier(int p_idDossier) {
		this.idDossier = p_idDossier;
		initialiseDossier(idDossier);
	}

	private void initialiseDossier(int p_idDossier) {
		BDRequette bd = new BDRequette();
		List<String> defDossier = bd.getDossierByID(p_idDossier);

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
		this.listSousDossier = bd.getListeSousDossier(idDossier);
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
		return listSousDossier;
	}

	public void setListSousDossier(MlListeDossier p_listSousDossier) {
		listSousDossier = p_listSousDossier;
	}

	public int getIdDossier() {
		return idDossier;
	}

	/**
	 * @return the unreadMessCount
	 */
	public int getUnreadMessCount() {
		this.unreadMessCount = new BDRequette().getUnreadMessageFromFolder(
				idCompte, idDossier);
		return unreadMessCount;
	}

	/**
	 * @return
	 */
	public MlListeMessage getListMessage() {
		this.listMessage = new BDRequette().getListeDeMessage(idCompte,
				idDossier);
		return listMessage;
	}

}
