package mdl.mlcomptemail;

import java.util.List;

import mdl.mldossier.MlListeDossier;
import releve.imap.util.messageUtilisateur;
import bdd.accesTable.AccesTableCompte;
import bdd.accesTable.AccesTableDossier;
import exception.DonneeAbsenteException;
import fenetre.comptes.EnDossierBase;
import fenetre.comptes.EnTypeCompte;

public class MlCompteMail {

	private int idCompte;
	private int idInbox, idBrouillons, idSpam, idCorbeille, idEnvoye;
	private long portPop, portSMTP;
	private String userName, password, serveurSMTP, serveurReception,
			nomCompte;
	private EnTypeCompte typeCompte;
	private final String TAG = this.getClass().getSimpleName();
	private int unreadMessageCount;
	private AccesTableCompte accesCompte;
	private AccesTableDossier accesDossier;

	/**
	 * Constructeur d'object, si on connait son Id, l'initialisation se charge
	 * d'aller en base pour recuperer ses infos.
	 * @param p_idCompte - int - l'id du compte
	 */
	public MlCompteMail(int p_idCompte) {
		accesDossier = new AccesTableDossier();
		accesCompte = new AccesTableCompte();
		this.idCompte = p_idCompte;
		initialiseCompte(idCompte);

	}

	public MlCompteMail(String p_nomCompte) {
		if (p_nomCompte != null && !p_nomCompte.equals("")) {
			accesCompte = new AccesTableCompte();
			accesDossier = new AccesTableDossier();
			this.idCompte = accesCompte.getIdComptes(p_nomCompte);
			initialiseCompte(idCompte);
		}

	}

	private void initialiseCompte(int p_idCompte) {
		List<String> defCompte = accesCompte.getCompteByID(p_idCompte);

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

		initDossierDeBase();
		this.unreadMessageCount = accesCompte
				.getUnreadMessageFromCompte(idCompte);

	}

	@Override
	public String toString() {
		int nb = getUnreadMessCount();
		if (nb > 0) {
			return nomCompte + " (" + nb + ")";
		}
		return nomCompte;

	}

	/**
	 * @param p_accesCompte
	 */
	private void initDossierDeBase() {
		EnDossierBase[] lstDossierBase = EnDossierBase.values();
		for (EnDossierBase unDossier : lstDossierBase) {
			switch (unDossier) {
				case RECEPTION:
					this.idInbox = (accesDossier.getIdDossier(unDossier
							.getLib(), idCompte));
					break;
				case BROUILLON:
					this.idBrouillons = (accesDossier.getIdDossier(unDossier
							.getLib(), idCompte));
					break;
				case SPAM:
					this.idSpam = (accesDossier.getIdDossier(
							unDossier.getLib(), idCompte));
					break;
				case CORBEILLE:
					this.idCorbeille = (accesDossier.getIdDossier(unDossier
							.getLib(), idCompte));
					break;
				case ENVOYES:
					this.idEnvoye = (accesDossier.getIdDossier(unDossier
							.getLib(), idCompte));
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
		if (accesDossier != null) {
			return accesDossier.getListeSousDossierBase(this);
		}
		accesDossier = new AccesTableDossier();
		return accesDossier.getListeSousDossierBase(this);

	}

	/**
	 * @return the ureadMessCount
	 */
	public int getUnreadMessCount() {
		return unreadMessageCount;
	}

	public void setUnreadMessCount(int p_count) {
		this.unreadMessageCount = p_count;
	}

	@Override
	public boolean equals(Object p_object) {
		if (!(p_object instanceof MlCompteMail)) {
			try {
				throw new DonneeAbsenteException(
						TAG,
						"l'objet présentée en entrée de la fonction equals doit etre de type MlCompteMail");
			} catch (DonneeAbsenteException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Erreur de programmation");
				return false;
			}
		}
		if (idCompte == ((MlCompteMail) p_object).getIdCompte()) {
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
