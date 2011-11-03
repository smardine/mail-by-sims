package fenetre.comptes;

public enum EnDossierBase {
	RECEPTION("Boîte de réception"), //
	ENVOYES("Éléments envoyés"), //
	SPAM("Spam"), //
	CORBEILLE("Messages supprimés"), //
	BROUILLON("Brouillons"), //
	ROOT("superRoot");

	private String lib;

	EnDossierBase(String p_lib) {
		this.lib = p_lib;
	}

	public String getLib() {
		return lib;
	}

	// public static boolean isDossierBase(String p_nomDossierStockage) {
	// for (EnDossierBase enumeration : values()) {
	// if (enumeration.getLib().equals(p_nomDossierStockage)) {
	// return true;
	// }
	// }
	// return false;
	//
	// }

	// public static EnDossierBase getDossierbase(String p_nomDossierStockage) {
	// for (EnDossierBase enumeration : values()) {
	// if (enumeration.getLib().equals(p_nomDossierStockage)) {
	// return enumeration;
	// }
	// }
	// return null;
	// }

	// public static IMAPFolder getDossierGmail(EnDossierBase p_dossierbase,
	// Store p_store) throws MessagingException {
	// IMAPFolder src = null;
	// switch (p_dossierbase) {
	// case RECEPTION:
	// src = (IMAPFolder) p_store.getFolder("INBOX");
	// break;
	// case ENVOYES:
	// if (p_store.getFolder("[Gmail]/Sent Mail").exists()) {
	// src = (IMAPFolder) p_store.getFolder("[Gmail]/Sent Mail");
	// } else {
	// src = (IMAPFolder) p_store
	// .getFolder("[Gmail]/Messages envoyés");
	// }
	// break;
	// case BROUILLON:
	// if (p_store.getFolder("[Gmail]/Drafts").exists()) {
	// src = (IMAPFolder) p_store.getFolder("[Gmail]/Drafts");
	// } else {
	// src = (IMAPFolder) p_store.getFolder("[Gmail]/Brouillons");
	// }
	// break;
	// case SPAM:
	// src = (IMAPFolder) p_store.getFolder("[Gmail]/Spam");
	// break;
	// case CORBEILLE:
	// if (p_store.getFolder("[Gmail]/Trash").exists()) {
	// src = (IMAPFolder) p_store.getFolder("[Gmail]/Trash");
	// } else {
	// src = (IMAPFolder) p_store.getFolder("[Gmail]/Corbeille");
	// }
	// break;
	// }
	// return src;
	// }

	// public static IMAPFolder getSousDossierInbox(String p_nomDossier,
	// int p_idCompte, Store p_store) throws MessagingException {
	// BDRequette bd = new BDRequette();
	// List<String> lstSousDossierInbox = bd.getListeSousDossier(bd
	// .getIdDossier(EnDossierBase.RECEPTION.getLib(), p_idCompte));
	// IMAPFolder fldr;
	// if (lstSousDossierInbox.contains(p_nomDossier)) {
	// fldr = (IMAPFolder) p_store.getFolder("INBOX/" + p_nomDossier);
	// } else {
	// fldr = (IMAPFolder) p_store.getFolder("[Gmail]/" + p_nomDossier);
	// }
	// // on verifie si le dossier existe
	// if (verifFolder(fldr, p_nomDossier)) {
	// return fldr;
	// }
	// // on essaye avec l'autre syntaxe possible
	// fldr = (IMAPFolder) p_store.getFolder(p_nomDossier);
	// if (fldr.exists()) {
	// return fldr;
	// }
	// return fldr;
	//
	// }
	//
	// private static boolean verifFolder(IMAPFolder p_fldr, String
	// p_nomDossier) {
	// try {
	// if (p_fldr.exists()) {
	// return true;
	// }
	// } catch (MessagingException e) {

	// return false;
	// }
	// return false;
	// }

}
