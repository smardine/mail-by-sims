package fenetre.comptes;

public enum EnDossierBase {
	RECEPTION("Boîte de réception"), //
	ENVOYES("Éléments envoyés"), //
	SPAM("Courrier indésirable"), //
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

}
