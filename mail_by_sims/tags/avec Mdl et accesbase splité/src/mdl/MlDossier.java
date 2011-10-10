/**
 * 
 */
package mdl;

import bdd.accestable.dossier.AccesTableDossier;

/**
 * @author smardine
 */
public class MlDossier {

	private final int idDossier;
	private String nomEnBase;
	private String nomInternet;
	private MlListeMessage lstMessage;
	private int idCompte;
	private final AccesTableDossier accesDossier;

	public MlDossier(int p_idCompte, int p_idDossier) {
		accesDossier = new AccesTableDossier();
		this.idDossier = p_idDossier;
		this.idCompte = p_idCompte;
		setNomEnBase(accesDossier.getNomDossier(p_idDossier));
		setNomInternet(accesDossier.getNomDossierInternet(p_idDossier));

	}

	/**
	 * @param nomEnBase the nomEnBase to set
	 */
	public void setNomEnBase(String nomEnBase) {
		this.nomEnBase = nomEnBase;
	}

	/**
	 * @return the nomEnBase
	 */
	public String getNomEnBase() {
		return nomEnBase;
	}

	/**
	 * @param nomInternet the nomInternet to set
	 */
	public void setNomInternet(String nomInternet) {
		this.nomInternet = nomInternet;
	}

	/**
	 * @return the nomInternet
	 */
	public String getNomInternet() {
		return nomInternet;
	}

	/**
	 * @return the idDossier
	 */
	public int getIdDossier() {
		return idDossier;
	}

	/**
	 * @param lstMessage the lstMessage to set
	 */
	public void setLstMessage(MlListeMessage lstMessage) {
		this.lstMessage = lstMessage;
	}

	/**
	 * @return the lstMessage
	 */
	public MlListeMessage getLstMessage() {
		lstMessage = new MlListeMessage(idCompte, idDossier);
		return lstMessage;
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

}
