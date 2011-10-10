package fenetre;

public enum EnTitreFenetre {

	PRINCIPALE("Mail by Sims"), //
	GESTION_COMPTE("Gestion des comptes"), //
	CREATION_COMPTE("Création d'un compte"), //
	RELEVE_MESSAGERIE("Releve de la messagerie");

	private String lib;

	EnTitreFenetre(String p_lib) {

		lib = p_lib;
	}

	public String getLib() {
		return lib;
	}
}
