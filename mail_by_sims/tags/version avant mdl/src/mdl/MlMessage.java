package mdl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import tools.RecupDate;
import bdd.BDRequette;

public class MlMessage {
	private String NomDossier;
	private int idDossier;
	private int idCompte;
	private String cheminPhysique;
	private String expediteur;
	private ArrayList<String> destinataire;
	private String sujet;
	private String contenu;
	private Date dateReception;
	private String uIDMessage;
	private ArrayList<File> listePieceJointe;
	private int idMessage;

	public MlMessage() {
		listePieceJointe = new ArrayList<File>();
	}

	public MlMessage(int p_idMessage) {
		super();
		BDRequette bd = new BDRequette();
		ArrayList<ArrayList<String>> lstChamp = bd.getMessageById(p_idMessage);
		for (int i = 0; i < lstChamp.size(); i++) {
			ArrayList<String> unEnregistrement = lstChamp.get(i);

			setIdMessage(Integer.parseInt(unEnregistrement.get(0)));
			setUIDMessage(unEnregistrement.get(1));
			setExpediteur(bd.decodeHTMLFromBase(unEnregistrement.get(2)));
			String[] tabDestinaire = unEnregistrement.get(3).split(";");
			ArrayList<String> lstDest = new ArrayList<String>();
			for (String des : tabDestinaire) {
				lstDest.add(des);
			}
			setDestinataire(lstDest);
			setSujet(bd.decodeHTMLFromBase(unEnregistrement.get(4)));
			setContenu(unEnregistrement.get(5));
			setDateReception(RecupDate.getdateFromTimeStamp((unEnregistrement
					.get(6))));
			setIdDossier(Integer.parseInt(unEnregistrement.get(7)));
			setNomDossier(bd.getNomDossier(Integer.parseInt(unEnregistrement
					.get(7))));
			setIdCompte(Integer.parseInt(unEnregistrement.get(8)));

		}
	}

	/**
	 * @param p_idCompte the idCompte to set
	 */
	public void setIdCompte(int p_idCompte) {
		this.idCompte = p_idCompte;
	}

	/**
	 * @return the idCompte
	 */
	public int getIdCompte() {
		return idCompte;
	}

	/**
	 * @param p_i the idDossier to set
	 */
	public void setIdDossier(int p_i) {
		this.idDossier = p_i;
	}

	/**
	 * @return the idDossier
	 */
	public int getIdDossier() {
		return idDossier;
	}

	/**
	 * @param nomDossier the nomDossier to set
	 */
	public void setNomDossier(String nomDossier) {
		NomDossier = nomDossier;
	}

	/**
	 * @return the nomDossier
	 */
	public String getNomDossier() {
		return NomDossier;
	}

	/**
	 * @param cheminPhysique the cheminPhysique to set
	 */
	public void setCheminPhysique(String cheminPhysique) {
		this.cheminPhysique = cheminPhysique;
	}

	/**
	 * @return the cheminPhysique
	 */
	public String getCheminPhysique() {
		return cheminPhysique;
	}

	/**
	 * @return the destinataire
	 */
	public ArrayList<String> getDestinataire() {
		return destinataire;
	}

	/**
	 * @param destinataire the destinataire to set
	 */
	public void setDestinataire(ArrayList<String> destinataire) {
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

	/**
	 * @param messageID the messageID to set
	 */
	public void setUIDMessage(String messageID) {
		this.uIDMessage = messageID;
	}

	/**
	 * @return the messageID
	 */
	public String getUIDMessage() {
		return uIDMessage;
	}

	/**
	 * @param listePieceJointe the listePieceJointe to set
	 */
	public void setListePieceJointe(ArrayList<File> listePieceJointe) {
		this.listePieceJointe = listePieceJointe;
	}

	/**
	 * @return the listePieceJointe
	 */
	public ArrayList<File> getListePieceJointe() {
		return listePieceJointe;
	}

	/**
	 * @param expediteur the expediteur to set
	 */
	public void setExpediteur(String expediteur) {
		this.expediteur = expediteur;
	}

	/**
	 * @return the expediteur
	 */
	public String getExpediteur() {
		return expediteur;
	}

	public void setIdMessage(int p_idMessage) {
		idMessage = p_idMessage;

	}

	public int getIdMessage() {
		return idMessage;
	}

}
