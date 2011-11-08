package mdl;

import java.util.List;

import bdd.BDRequette;
import fenetre.comptes.EnDossierBase;
import fenetre.comptes.EnTypeCompte;

public class MlCompteMail {

	private int idCompte;
	private int idInbox, idBrouillons, idSpam, idCorbeille, idEnvoye;
	private long portPop, portSMTP;
	private String userName, password, serveurSMTP, serveurReception,
			nomCompte;
	private MlListeDossier listDossier;
	private int ureadMessCount;
	private EnTypeCompte typeCompte;

	/**
	 * Constructeur d'object, si on connait son Id, l'initialisation se charge
	 * d'aller en base pour recuperer ses infos.
	 * @param p_idCompte - int - l'id du compte
	 */
	public MlCompteMail(int p_idCompte) {
		this.idCompte = p_idCompte;
		initialiseCompte(idCompte);
	}

	public MlCompteMail(String p_nomCompte) {
		if (p_nomCompte != null && !p_nomCompte.equals("")) {
			BDRequette bd = new BDRequette();
			this.idCompte = bd.getIdComptes(p_nomCompte);

			initialiseCompte(idCompte);
		}

	}

	private void initialiseCompte(int p_idCompte) {
		BDRequette bd = new BDRequette();
		List<String> defCompte = bd.getCompteByID(p_idCompte);

		for (int i = 0; i < defCompte.size(); i++) {
			switch (i) {
				case 0:
					this.nomCompte = (defCompte.get(0));
					break;
				case 1:
					this.serveurReception = (defCompte.get(1));
					break;
				case 2:
					this.portPop = (Long.parseLong(defCompte.get(2)));
					break;
				case 3:
					this.serveurSMTP = (defCompte.get(3));
					break;
				case 4:
					this.portSMTP = (Long.parseLong(defCompte.get(4)));
					break;
				case 5:
					this.userName = (defCompte.get(5));
					break;
				case 6:
					this.password = (defCompte.get(6));
					break;
				case 7:
					traiteTypeCompte(defCompte);
					break;

			}
		}// fin de for

		initDossierDeBase(bd);
		// this.listDossier =

	}

	/**
	 * @param bd
	 */
	private void initDossierDeBase(BDRequette bd) {
		EnDossierBase[] lstDossierBase = EnDossierBase.values();
		for (EnDossierBase unDossier : lstDossierBase) {
			switch (unDossier) {
				case RECEPTION:
					this.idInbox = (bd.getIdDossier(unDossier.getLib(),
							idCompte));
					break;
				case BROUILLON:
					this.idBrouillons = (bd.getIdDossier(unDossier.getLib(),
							idCompte));
					break;
				case SPAM:
					this.idSpam = (bd
							.getIdDossier(unDossier.getLib(), idCompte));
					break;
				case CORBEILLE:
					this.idCorbeille = (bd.getIdDossier(unDossier.getLib(),
							idCompte));
					break;
				case ENVOYES:
					this.idEnvoye = (bd.getIdDossier(unDossier.getLib(),
							idCompte));
					break;
			}
		}
	}

	/**
	 * @param p_defCompte
	 */
	private void traiteTypeCompte(List<String> p_defCompte) {
		if ("imap".equals(p_defCompte.get(7))) {
			this.typeCompte = (EnTypeCompte.IMAP);
		} else if ("pop".equals(p_defCompte.get(7))) {
			this.typeCompte = (EnTypeCompte.POP);
		} else if ("gmail".equals(p_defCompte.get(7))) {
			this.typeCompte = (EnTypeCompte.GMAIL);
		} else if ("hotmail".equals(p_defCompte.get(7))) {
			this.typeCompte = (EnTypeCompte.HOTMAIL);
		}
	}

	/**
	 * @return the nomCompte
	 */
	public String getNomCompte() {
		return this.nomCompte;
	}

	/**
	 * @param p_nomCompte the nomCompte to set
	 */
	public void setNomCompte(String p_nomCompte) {
		this.nomCompte = p_nomCompte;
	}

	/**
	 * @return the serveurReception
	 */
	public String getServeurReception() {
		return this.serveurReception;
	}

	/**
	 * @param p_serveurPop the serveurReception to set
	 */
	public void setServeurReception(String p_serveurPop) {
		this.serveurReception = p_serveurPop;
	}

	/**
	 * @return the portPop
	 */
	public long getPortPop() {
		return this.portPop;
	}

	/**
	 * @param p_portPop the portPop to set
	 */
	public void setPortPop(long p_portPop) {
		this.portPop = p_portPop;
	}

	/**
	 * @return the serveurSMTP
	 */
	public String getServeurSMTP() {
		return this.serveurSMTP;
	}

	/**
	 * @param p_serveurSMTP the serveurSMTP to set
	 */
	public void setServeurSMTP(String p_serveurSMTP) {
		this.serveurSMTP = p_serveurSMTP;
	}

	/**
	 * @return the portSMTP
	 */
	public long getPortSMTP() {
		return this.portSMTP;
	}

	/**
	 * @param p_portSMTP the portSMTP to set
	 */
	public void setPortSMTP(long p_portSMTP) {
		this.portSMTP = p_portSMTP;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param p_userName the userName to set
	 */
	public void setUserName(String p_userName) {
		this.userName = p_userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param p_password the password to set
	 */
	public void setPassword(String p_password) {
		this.password = p_password;
	}

	/**
	 * @return the typeCompte
	 */
	public EnTypeCompte getTypeCompte() {
		return this.typeCompte;
	}

	/**
	 * @param p_isImap the typeCompte to set
	 */
	public void setTypeCompte(EnTypeCompte p_isImap) {
		this.typeCompte = p_isImap;
	}

	/**
	 * @return the idCompte
	 */
	public int getIdCompte() {
		return this.idCompte;
	}

	/**
	 * @return the idInbox
	 */
	public int getIdInbox() {
		return this.idInbox;
	}

	/**
	 * @return the idBrouillons
	 */
	public final int getIdBrouillons() {
		return this.idBrouillons;
	}

	/**
	 * @return the idSpam
	 */
	public int getIdSpam() {
		return this.idSpam;
	}

	/**
	 * @return the idCorbeille
	 */
	public int getIdCorbeille() {
		return this.idCorbeille;
	}

	/**
	 * @return the idEnvoye
	 */
	public int getIdEnvoye() {
		return this.idEnvoye;
	}

	/**
	 * @param p_idInbox the idInbox to set
	 */
	public void setIdInbox(int p_idInbox) {
		this.idInbox = p_idInbox;
	}

	/**
	 * @param p_idBrouillons the idBrouillons to set
	 */
	public void setIdBrouillons(int p_idBrouillons) {
		this.idBrouillons = p_idBrouillons;
	}

	/**
	 * @param p_idSpam the idSpam to set
	 */
	public void setIdSpam(int p_idSpam) {
		this.idSpam = p_idSpam;
	}

	/**
	 * @param p_idCorbeille the idCorbeille to set
	 */
	public void setIdCorbeille(int p_idCorbeille) {
		this.idCorbeille = p_idCorbeille;
	}

	/**
	 * @param p_idEnvoye the idEnvoye to set
	 */
	public void setIdEnvoye(int p_idEnvoye) {
		this.idEnvoye = p_idEnvoye;
	}

	/**
	 * @param p_idCompte the idCompte to set
	 */
	public void setIdCompte(int p_idCompte) {
		this.idCompte = p_idCompte;
	}

	/**
	 * @return the listDossier
	 */
	public MlListeDossier getListDossierPrincipaux() {
		listDossier = new BDRequette().getListeSousDossierBase(idCompte);
		return listDossier;
	}

	/**
	 * @return the ureadMessCount
	 */
	public int getUreadMessCount() {
		ureadMessCount = new BDRequette().getUnreadMessageFromCompte(idCompte);
		return ureadMessCount;
	}

}
