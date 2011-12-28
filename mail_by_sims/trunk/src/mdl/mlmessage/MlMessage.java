package mdl.mlmessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tools.RecupDate;
import bdd.accesTable.AccesTableDossier;
import bdd.accesTable.AccesTableMailRecu;

public class MlMessage {
	private String NomDossier;
	private int idDossier;
	private int idCompte;
	private String cheminPhysique;
	private String expediteur;
	private List<String> destinataire;
	private List<String> destinataireCopy;
	private List<String> destinataireCache;
	private String sujet;
	private String contenu;
	private Date dateReception;
	private String uIDMessage;
	private List<File> listePieceJointe;
	private int idMessage;
	private boolean statuLecture;
	private boolean havePieceJointe;

	private enum statutLecture {
		T, F;
	}

	public MlMessage() {
		listePieceJointe = new ArrayList<File>();
	}

	public MlMessage(int p_idMessage) {
		super();
		constructMessage(p_idMessage);

	}

	/**
	 * @param p_idMessage
	 */
	private void constructMessage(int p_idMessage) {
		AccesTableMailRecu accesMail = new AccesTableMailRecu();
		List<ArrayList<String>> lstChamp = accesMail
				.getMessageById(p_idMessage);
		for (int i = 0; i < lstChamp.size(); i++) {
			List<String> unEnregistrement = lstChamp.get(i);

			this.idMessage = (Integer.parseInt(unEnregistrement.get(0)));
			this.uIDMessage = (unEnregistrement.get(1));
			if (unEnregistrement.get(2) != null) {
				this.expediteur = (accesMail
						.decodeHTMLFromBase(unEnregistrement.get(2)));
			}
			if (unEnregistrement.get(3) != null) {
				String[] tabDestinaire = unEnregistrement.get(3).split(";");
				List<String> lstDest = new ArrayList<String>();
				for (String des : tabDestinaire) {
					lstDest.add(des);
				}
				this.destinataire = (lstDest);
			}
			if (unEnregistrement.get(4) != null) {
				String[] tabDestinaireCopy = unEnregistrement.get(4).split(";");
				List<String> lstDest = new ArrayList<String>();
				for (String des : tabDestinaireCopy) {
					lstDest.add(des);
				}
				this.destinataireCopy = (lstDest);
			}
			if (unEnregistrement.get(5) != null) {
				String[] tabDestinaireCache = unEnregistrement.get(5)
						.split(";");
				List<String> lstDest = new ArrayList<String>();
				for (String des : tabDestinaireCache) {
					lstDest.add(des);
				}
				this.destinataireCache = (lstDest);
			}

			this.sujet = (accesMail.decodeHTMLFromBase(unEnregistrement.get(6)));
			// this.contenu = (unEnregistrement.get(7));
			this.dateReception = (RecupDate
					.getdateFromTimeStamp((unEnregistrement.get(7))));
			this.idDossier = (Integer.parseInt(unEnregistrement.get(8)));
			this.NomDossier = (new AccesTableDossier().getNomDossier(Integer
					.parseInt(unEnregistrement.get(8))));

			this.idCompte = (Integer.parseInt(unEnregistrement.get(9)));
			if (statutLecture.T.name().equals(unEnregistrement.get(10))) {
				this.statuLecture = true;
			} else {
				this.statuLecture = false;
			}

		}
		this.setHavePieceJointe(accesMail.messageHavePieceJointe(idMessage));
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
	public List<String> getDestinataire() {
		return destinataire;
	}

	/**
	 * @param destinataire the destinataire to set
	 */
	public void setDestinataire(List<String> destinataire) {
		this.destinataire = destinataire;
	}

	/**
	 * @param destinataireCopy the destinataireCopy to set
	 */
	public void setDestinataireCopy(List<String> destinataireCopy) {
		this.destinataireCopy = destinataireCopy;
	}

	/**
	 * @return the destinataireCopy
	 */
	public List<String> getDestinataireCopy() {
		return destinataireCopy;
	}

	/**
	 * @param destinataireCache the destinataireCache to set
	 */
	public void setDestinataireCache(List<String> destinataireCache) {
		this.destinataireCache = destinataireCache;
	}

	/**
	 * @return the destinataireCache
	 */
	public List<String> getDestinataireCache() {
		return destinataireCache;
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
		if (contenu == null) {
			contenu = new AccesTableMailRecu()
					.getContenuFromIdForString(idMessage);
		}
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
	public void setListePieceJointe(List<File> listePieceJointe) {
		this.listePieceJointe = listePieceJointe;
	}

	/**
	 * @return the listePieceJointe
	 */
	public List<File> getListePieceJointe() {
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

	/**
	 * @param statuLecture true si le message est lu, false si il est non lu
	 */
	public void setLu(boolean statuLecture) {
		this.statuLecture = statuLecture;
	}

	/**
	 * @return true si le message est lu, false si il est non lu
	 */
	public boolean isLu() {
		return statuLecture;
	}

	/**
	 * @param havePieceJointe the havePieceJointe to set
	 */
	public void setHavePieceJointe(boolean havePieceJointe) {
		this.havePieceJointe = havePieceJointe;
	}

	/**
	 * @return the havePieceJointe
	 */
	public boolean isHavePieceJointe() {
		return havePieceJointe;
	}

}
