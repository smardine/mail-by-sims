package fenetre.comptes;

import java.util.ArrayList;

public enum EnDefFournisseur {

	NEUF_TELECOM("9 Telecom", "pop.neuf.fr", 110, "smtp.neuf.fr", 25,
			EnTypeCompte.POP), //
	NEUF_ONLINE("9 Online", "pop.9Online.fr", 110, "smtp.9online.fr", 25,
			EnTypeCompte.POP), //
	ALICE("Alice", "pop.alice.fr", 110, "smtp.alice.fr", 25, EnTypeCompte.POP), //
	ALICE_ADSL("Alice ADSL", "pop.aliceadsl.fr", 110, "smpt.aliceadsl.fr", 25,
			EnTypeCompte.POP), //
	AOL("Aol", "pop.aol.com", 110, "smtp.fr.aol.com", 25, EnTypeCompte.POP), //
	ALTERN_ORG("Altern.org", "pop.altern.org", 110, "nean", 25,
			EnTypeCompte.POP), //
	BOUYGUES_TELECOM("Bouyges Telecom", "pop.bbox.fr", 110, "smtp.bbox.fr", 25,
			EnTypeCompte.POP), //
	LYCOS("Lycos", "pop.lycos.co.uk", 110, "smtp.lycos.co.uk", 25,
			EnTypeCompte.POP), //
	CEGETEL("Cegetel", "pop.cegetel.net", 110, "smtp.cegetel.net", 25,
			EnTypeCompte.POP), //
	CLUB_INTERNET("Club Internet", "pop3.club-internet.fr", 110,
			"smtp-auth.sfr.fr", 25, EnTypeCompte.POP), //
	DARTY_BOX("Darty Box", "pop.dbmail.com", 110, "smtpauth.dbmail.com", 25,
			EnTypeCompte.HOTMAIL), //
	EST_VIDEO_COMMUNICATION("Est Video Communication", "pop.evhr.net", 110,
			"smtp.evhr.net", 25, EnTypeCompte.POP), //
	FREE("Free", "pop.free.fr", 110, "smtp.free.fr", 25, EnTypeCompte.POP), //
	FREE_SURF("Free Surf", "pop.freesurf.fr", 110, "smtp.free.fr", 25,
			EnTypeCompte.POP), //
	GAWAB("Gawab", "pop.gawab.com", 110, "smtp.gawab.com", 25, EnTypeCompte.POP), //

	GMAIL("Gmail", "imap.gmail.com", 110, "imap.gmail.com", 25,
			EnTypeCompte.GMAIL), //
	HOTMAIL("Hotmail", "pop3.live.com", 995, "smtp.live.com", 25,
			EnTypeCompte.HOTMAIL), //
	IFRANCE("IFrance", "pop.ifirance.com", 110, "smtp.ifrance.com", 25,
			EnTypeCompte.POP), //
	LA_POSTE("La poste", "pop.laposte.net", 110, "smtp.laposte.net", 25,
			EnTypeCompte.POP), //
	MAGIC_ONLINE("Magic Online", "pop2.magic.fr", 110, "smtp.magic.fr", 25,
			EnTypeCompte.POP), //
	NERIM("Nerim", "pop.nerim.net", 110, "smtp.nerim.net", 25, EnTypeCompte.POP), //
	NET_COURRIER("Net Courrier", "mail.netcourrier.com", 110, "nean", 25,
			EnTypeCompte.POP), //
	NOOS("Noos", "pop.noos.fr", 110, "mail.noos.fr", 25, EnTypeCompte.POP), //
	ORANGE("Orange", "pop.orange.fr", 110, "smtp.orange.fr", 25,
			EnTypeCompte.POP), //
	SFR("SFR", "pop.sfr.fr", 110, "smtp.sfr.fr", 25, EnTypeCompte.POP), //
	SYMPATICO("Sympatico", "pop1.sympatico.ca", 110, "smtp1.sympatico.ca", 25,
			EnTypeCompte.POP), //
	TELE2("Télé 2", "pop.tele2.fr", 110, "smtp.tele2.fr", 25, EnTypeCompte.POP), //
	TISCALI("Tiscali", "pop.tiscali.fr", 110, "smtp.tiscali.fr", 25,
			EnTypeCompte.POP), //
	TISCALI_FREESBEE("Tiscali Freesbee", "pop.freesbee.fr", 110,
			"smtp.freesbee.fr", 25, EnTypeCompte.POP), //
	WANADOO("Wanadoo", "pop.wanadoo.fr", 110, "smtp.wanadoo.fr", 25,
			EnTypeCompte.POP), //
	YAHOO("Yahoo", "pop.mail.yahoo.fr", 110, "smtp.mail.yahoo.fr", 25,
			EnTypeCompte.POP);

	private String lib;
	private String serveurPop;
	private int portPop;
	private String serveurSMTP;
	private int portSMTP;
	private EnTypeCompte typeCompte;

	EnDefFournisseur(String p_lib, String p_serveurPop, int p_portPop,
			String p_serveurSMTP, int p_portSMTP, EnTypeCompte p_typeCompte) {
		this.lib = p_lib;
		this.serveurPop = p_serveurPop;
		this.portPop = p_portPop;
		this.serveurSMTP = p_serveurSMTP;
		this.portSMTP = p_portSMTP;
		this.typeCompte = p_typeCompte;
	}

	/**
	 * @return the lib
	 */
	public String getLib() {
		return this.lib;
	}

	/**
	 * @return the serveurPop
	 */
	public String getServeurPop() {
		return this.serveurPop;
	}

	/**
	 * @return the portPop
	 */
	public int getPortPop() {
		return this.portPop;
	}

	/**
	 * @return the serveurSMTP
	 */
	public String getServeurSMTP() {
		return this.serveurSMTP;
	}

	/**
	 * @return the portSMTP
	 */
	public int getPortSMTP() {
		return this.portSMTP;
	}

	/**
	 * @return the typeCompte
	 */
	public EnTypeCompte getTypeCompte() {
		return this.typeCompte;
	}

	public static final ArrayList<EnDefFournisseur> getComptePop() {
		ArrayList<EnDefFournisseur> lst = new ArrayList<EnDefFournisseur>();
		for (EnDefFournisseur uneEnum : values()) {
			if (uneEnum.getTypeCompte() == EnTypeCompte.POP) {
				lst.add(uneEnum);
			}
		}
		return lst;
	}

	public static EnDefFournisseur getEnumFromLib(String p_lib) {
		for (EnDefFournisseur uneEnum : values()) {
			if (uneEnum.getLib().equals(p_lib)) {
				return uneEnum;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.getLib() + this.getPortPop() + this.getServeurPop()
				+ this.getServeurSMTP();

	}
}
