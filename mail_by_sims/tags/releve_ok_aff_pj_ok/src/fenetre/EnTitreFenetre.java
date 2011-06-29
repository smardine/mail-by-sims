package fenetre;

public enum EnTitreFenetre {

	PRINCIPALE(0, "Mail by Sims"), //
	GESTION_COMPTE(1, "Gestion des comptes"), CREATION_COMPTE(2,
			"Création d'un compte");

	private int code;
	private String lib;

	EnTitreFenetre(int p_code, String p_lib) {
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
