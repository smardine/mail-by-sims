package fenetre.comptes.creation.MlActionCreation;

public enum EnActionCreationComptes {
	VALIDER(0, "Valider"), //
	ANNULER(1, "Annuler");
	private int code;
	private String lib;

	EnActionCreationComptes(int p_code, String p_lib) {
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
