package fenetre.comptes;

import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.Store;

import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

public enum EnDossierBase {
	RECEPTION("Bo�te de r�ception"), //
	ENVOYES("�l�ments envoy�s"), //
	SPAM("Courrier ind�sirable"), //
	CORBEILLE("Messages supprim�s"), //
	BROUILLON("Brouillons"), //
	ROOT("superRoot");

	private String lib;

	EnDossierBase(String p_lib) {
		this.lib = p_lib;
	}

	public String getLib() {
		return lib;
	}

	public static boolean isDossierBase(String p_nomDossierStockage) {
		for (EnDossierBase enumeration : values()) {
			if (enumeration.getLib().equals(p_nomDossierStockage)) {
				return true;
			}
		}
		return false;

	}

	public static EnDossierBase getDossierbase(String p_nomDossierStockage) {
		for (EnDossierBase enumeration : values()) {
			if (enumeration.getLib().equals(p_nomDossierStockage)) {
				return enumeration;
			}
		}
		return null;
	}

	public static IMAPFolder getDossierGmail(EnDossierBase p_dossierbase,
			Store p_store) throws MessagingException {
		IMAPFolder src = null;
		switch (p_dossierbase) {
			case RECEPTION:
				src = (IMAPFolder) p_store.getFolder("INBOX");
				break;
			case ENVOYES:
				if (p_store.getFolder("[Gmail]/Sent Mail").exists()) {
					src = (IMAPFolder) p_store.getFolder("[Gmail]/Sent Mail");
				} else {
					src = (IMAPFolder) p_store
							.getFolder("[Gmail]/Messages envoy�s");
				}
				break;
			case BROUILLON:
				if (p_store.getFolder("[Gmail]/Drafts").exists()) {
					src = (IMAPFolder) p_store.getFolder("[Gmail]/Drafts");
				} else {
					src = (IMAPFolder) p_store.getFolder("[Gmail]/Brouillons");
				}
				break;
			case SPAM:
				src = (IMAPFolder) p_store.getFolder("[Gmail]/Spam");
				break;
			case CORBEILLE:
				if (p_store.getFolder("[Gmail]/Trash").exists()) {
					src = (IMAPFolder) p_store.getFolder("[Gmail]/Trash");
				} else {
					src = (IMAPFolder) p_store.getFolder("[Gmail]/Corbeille");
				}
				break;
		}
		return src;
	}

	public static IMAPFolder getSousDossierInbox(String p_nomDossier,
			int p_idCompte, Store p_store) throws MessagingException {
		BDRequette bd = new BDRequette();
		ArrayList<String> lstSousDossierInbox = bd.getListeSousDossier(bd
				.getIdDossier(EnDossierBase.RECEPTION.getLib(), p_idCompte));
		IMAPFolder fldr;
		if (lstSousDossierInbox.contains(p_nomDossier)) {
			fldr = (IMAPFolder) p_store.getFolder("INBOX/" + p_nomDossier);
		} else {
			fldr = (IMAPFolder) p_store.getFolder(p_nomDossier);
		}

		return fldr;

	}

}
