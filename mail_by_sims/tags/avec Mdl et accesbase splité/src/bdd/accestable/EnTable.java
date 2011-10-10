package bdd.accestable;

public enum EnTable {
	BLACK_LIST("BLACK_LISTE"), //
	COMPTES("COMPTES"), //
	DOSSIER("DOSSIER"), //
	MAIL_ENVOYE("MAIL_ENVOYE"), //
	MAIL_RECU("MAIL_RECU"), //
	PARAM("PARAM"), //
	PIECE_JOINTE("PIECE_JOINTE"), //
	REGLES("REGLES");

	private String nomTable;

	EnTable(String p_nomTable) {
		this.nomTable = p_nomTable;
	}

	/**
	 * @return the nomTable
	 */
	public String getNomTable() {
		return nomTable;
	}

}
