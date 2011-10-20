package bdd;

public enum EnStructureTable {
	COMPTES_NOM("NOM_COMPTE"), //
	COMPTES_ID("ID_COMPTE"), //
	COMPTES_USERNAME("USERNAME"), //
	COMPTES_PWD("PWD"), //
	COMPTES_SERVEURPOP("SERVEUR_POP"), //
	DOSSIER_ID("ID_DOSSIER"), //
	DOSSIER_NOM("NOM_DOSSIER"), //
	DOSSIER_NOM_INTERNET("NOM_INTERNET"), //
	;

	private String nomChamp;

	EnStructureTable(String p_nomChamp) {
		this.nomChamp = p_nomChamp;
	}

	public String getNomChamp() {
		return nomChamp;
	}
}
