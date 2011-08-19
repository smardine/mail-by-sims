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
	ENVOYER_RECEVOIR("Envoyer / Recevoir"), //
	ENVOYER("Envoyer"), //
	RECEVOIR("Recevoir"), //
	SUPPRIMER("Supprimer"), //
	CREER_REGLE("Créer une règle a partir de ce message"), //
	MARQUER_SPAM("Marquer ce courrier comme indésirable");

	private String lib;

	EnActionMain(String p_lib) {

		lib = p_lib;

	}

	public String getLib() {
		return lib;
	}

	public static EnActionMain getEnumFromLib(String p_actionCommand) {
		for (EnActionMain enumeration : values()) {
			if (enumeration.getLib().equals(p_actionCommand)) {
				return enumeration;
			}
		}
		return null;
	}

}
