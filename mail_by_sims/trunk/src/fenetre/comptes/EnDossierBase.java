package fenetre.comptes;

public enum EnDossierBase {
	RECEPTION("Boite de récéption"), //
	ENVOYES("Messages envoyés"), //
	SPAM("Spam"), //
	CORBEILLE("Corbeille"), //
	ROOT("superRoot");

	private String lib;

	EnDossierBase(String p_lib) {
		this.lib = p_lib;
	}

	public String getLib() {
		return lib;
	}

}
