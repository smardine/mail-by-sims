package fenetre.comptes.gestion.MlActionGestion;

public enum EnActionComptes {
	CREER(0, "Créer"), //
	MODIFIER(1, "Modifier"), //
	SUPPRIMER(2, "Supprimer");
	private int code;
	private String lib;

	EnActionComptes(int p_code, String p_lib) {
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
