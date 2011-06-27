package fenetre.principale.MlAction;

public enum EnActionMain {

	QUITTER(0, "Quitter"), //
	GESTION_COMPTE(1, "Gestion des comptes"), //
	IMPORT_EXPORT(5, "Import /Export"), //
	IMPORTER(6, "Importer"), //
	CHOIX_COMPTE(5, "Choix du compte"), //
	EXPLORER(2, "Explorer"), //
	HISTORIQUE(3, "Historique"), //
	CONTACT(4, "Contact");

	private int code;
	private String lib;

	EnActionMain(int p_code, String p_lib) {
		code = p_code;
		lib = p_lib;

	}

	public int getCode() {
		return code;
	}

	public String getLib() {
		return lib;
	}

}
