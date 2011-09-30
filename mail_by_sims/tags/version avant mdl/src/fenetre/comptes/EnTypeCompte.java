package fenetre.comptes;

public enum EnTypeCompte {
	POP("pop"), //
	IMAP("imap"), //
	GMAIL("gmail"), //
	HOTMAIL("hotmail");
	private String lib;

	EnTypeCompte(String p_lib) {
		this.lib = p_lib;
	}

	public String getLib() {
		return lib;
	}
}
