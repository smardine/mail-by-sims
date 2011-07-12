package mdl;

import java.util.ArrayList;

import bdd.BDRequette;

public class MlCompteMail {

	private final int idCompte;
	private String nomCompte;
	private String serveurReception;
	private long portPop;
	private String serveurSMTP;
	private long portSMTP;
	private String userName;
	private String password;
	private boolean isImap;

	public MlCompteMail(int p_idCompte) {
		this.idCompte = p_idCompte;
		initialiseCompte(idCompte);
	}

	private void initialiseCompte(int p_idCompte) {
		ArrayList<String> defCompte = BDRequette.getCompteByID(p_idCompte);
		for (int i = 0; i < defCompte.size(); i++) {
			switch (i) {
				case 0:
					setNomCompte(defCompte.get(0));
					break;
				case 1:
					setServeurReception(defCompte.get(1));
					break;
				case 2:
					setPortPop(Long.parseLong(defCompte.get(2)));
					break;
				case 3:
					setServeurSMTP(defCompte.get(3));
					break;
				case 4:
					setPortSMTP(Long.parseLong(defCompte.get(4)));
					break;
				case 5:
					setUserName(defCompte.get(5));
					break;
				case 6:
					setPassword(defCompte.get(6));
					break;
				case 7:
					if ("imap".equals(defCompte.get(7))) {
						setImap(true);
					} else {
						setImap(false);
					}
					break;

			}
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
	 * @return the isImap
	 */
	public boolean isImap() {
		return this.isImap;
	}

	/**
	 * @param p_isImap the isImap to set
	 */
	public void setImap(boolean p_isImap) {
		this.isImap = p_isImap;
	}

	/**
	 * @return the idCompte
	 */
	public int getIdCompte() {
		return this.idCompte;
	}

}
