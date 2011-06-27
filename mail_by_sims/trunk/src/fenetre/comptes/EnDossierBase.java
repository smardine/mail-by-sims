package fenetre.comptes;

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

}
