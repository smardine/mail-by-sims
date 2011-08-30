package import_mail;

import java.util.Date;

public class MlContenuMessage {

	private String expediteur, destinataire, sujet, contenu;
	private Date dateReception;

	public MlContenuMessage() {

	}

	/**
	 * @return the expediteur
	 */
	public String getExpediteur() {
		return expediteur;
	}

	/**
	 * @param expediteur the expediteur to set
	 */
	public void setExpediteur(String expediteur) {
		this.expediteur = expediteur;
	}

	/**
	 * @return the destinataire
	 */
	public String getDestinataire() {
		return destinataire;
	}

	/**
	 * @param destinataire the destinataire to set
	 */
	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	/**
	 * @return the sujet
	 */
	public String getSujet() {
		return sujet;
	}

	/**
	 * @param sujet the sujet to set
	 */
	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	/**
	 * @return the contenu
	 */
	public String getContenu() {
		return contenu;
	}

	/**
	 * @param contenu the contenu to set
	 */
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	/**
	 * @return the dateReception
	 */
	public Date getDateReception() {
		return dateReception;
	}

	/**
	 * @param dateReception the dateReception to set
	 */
	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
	}

}
