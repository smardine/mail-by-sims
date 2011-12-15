/**
 * 
 */
package mdl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tools.RecupDate;
import bdd.accesTable.AccesTableMailRecu;

/**
 * @author smardine
 */
public class MlMessageGrille {

	private int idMessage;
	private String expediteur;
	private String sujet;
	private Date dateReception;
	private boolean isLu;
	private boolean HavePieceJointe;
	private String uidMessage;
	private int idDossier;
	private int idCompte;

	public enum statutLecture {
		T, F;
	}

	public MlMessageGrille(int p_idMessage) {
		this.idMessage = p_idMessage;
		AccesTableMailRecu accesMail = new AccesTableMailRecu();
		List<ArrayList<String>> lstChamp = accesMail
				.getMessageGrilleById(p_idMessage);
		for (int i = 0; i < lstChamp.size(); i++) {
			List<String> aRecord = lstChamp.get(i);

			this.expediteur = accesMail.decodeHTMLFromBase(aRecord.get(0));
			this.sujet = accesMail.decodeHTMLFromBase(aRecord.get(1));
			this.dateReception = RecupDate
					.getdateFromTimeStamp((aRecord.get(2)));
			this.isLu = statutLecture.T.name().equals(aRecord.get(3));
			this.uidMessage = aRecord.get(4);
			this.idCompte = Integer.parseInt(aRecord.get(5));
			this.idDossier = Integer.parseInt(aRecord.get(6));
			this.HavePieceJointe = accesMail.messageHavePieceJointe(idMessage);

		}
	}

	/**
	 * 
	 */
	public MlMessageGrille() {
	}

	/**
	 * @return the expediteur
	 */
	public String getExpediteur() {
		return this.expediteur;
	}

	/**
	 * @param p_expediteur the expediteur to set
	 */
	public void setExpediteur(String p_expediteur) {
		this.expediteur = p_expediteur;
	}

	/**
	 * @return the sujet
	 */
	public String getSujet() {
		return this.sujet;
	}

	/**
	 * @param p_sujet the sujet to set
	 */
	public void setSujet(String p_sujet) {
		this.sujet = p_sujet;
	}

	/**
	 * @return the dateReception
	 */
	public Date getDateReception() {
		return this.dateReception;
	}

	/**
	 * @param p_dateReception the dateReception to set
	 */
	public void setDateReception(Date p_dateReception) {
		this.dateReception = p_dateReception;
	}

	/**
	 * @return the isLu
	 */
	public boolean isLu() {
		return this.isLu;
	}

	/**
	 * @param p_isLu the isLu to set
	 */
	public void setLu(boolean p_isLu) {
		this.isLu = p_isLu;
	}

	/**
	 * @return the havePieceJointe
	 */
	public boolean isHavePieceJointe() {
		return this.HavePieceJointe;
	}

	/**
	 * @param p_havePieceJointe the havePieceJointe to set
	 */
	public void setHavePieceJointe(boolean p_havePieceJointe) {
		this.HavePieceJointe = p_havePieceJointe;
	}

	/**
	 * @return the idMessage
	 */
	public int getIdMessage() {
		return this.idMessage;
	}

	/**
	 * @param p_idMessage the idMessage to set
	 */
	public void setIdMessage(int p_idMessage) {
		this.idMessage = p_idMessage;
	}

	/**
	 * @param uidMessage the uidMessage to set
	 */
	public void setUidMessage(String uidMessage) {
		this.uidMessage = uidMessage;
	}

	/**
	 * @return the uidMessage
	 */
	public String getUidMessage() {
		return uidMessage;
	}

	/**
	 * @param p_idDossierChoisi
	 */
	public void setIdDossier(int p_idDossierChoisi) {
		this.idDossier = p_idDossierChoisi;
	}

	/**
	 * @param p_idCompte
	 */
	public void setIdCompte(int p_idCompte) {
		this.idCompte = p_idCompte;
	}

	/**
	 * @return the idDossier
	 */
	public int getIdDossier() {
		return this.idDossier;
	}

	/**
	 * @return the idCompte
	 */
	public int getIdCompte() {
		return this.idCompte;
	}

}
