package bdd;

public enum EnTable {

	COMPTES("COMPTES"), //
	DOSSIER("DOSSIER");

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
