package fenetre.comptes;

public enum EnDossierBase {
	RECEPTION("Réception"), //
	ENVOYES("Envoyés"), //
	SPAM("Spam"), //
	CORBEILLE("Corbeille"), //
	BROUILLON("Brouillons"), //
	ROOT("superRoot");

	private String lib;

	EnDossierBase(String p_lib) {
		this.lib = p_lib;
	}

	public String getLib() {
		return lib;
	}

}
