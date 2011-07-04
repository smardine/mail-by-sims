package fenetre.principale.MlAction;

public enum EnActionMain {

	QUITTER("Quitter"), //
	GESTION_COMPTE("Gestion des comptes"), //
	IMPORT_EXPORT("Import /Export"), //
	IMPORTER("Importer"), //
	CHOIX_COMPTE("Choix du compte"), //
	EXPLORER("Explorer"), //
	HISTORIQUE("Historique"), //
	CONTACT("Contact"), //
	RECEVOIR("Recevoir"), //
	SUPPRIMER("Supprimer");

	private String lib;

	EnActionMain(String p_lib) {

		lib = p_lib;

	}

	public String getLib() {
		return lib;
	}

}
