/**
 * 
 */
package bdd.structure;

/**
 * @author smardine
 */
public enum EnStructRegle implements StructureTable {
	ID("ID_REGLE", Integer.class, null), //
	ADRESSE("ADRESSE_REGLE", String.class, 500), //
	ID_DOSSIER("ID_DOSSIER_STOCKAGE", Integer.class, 500);

	private String nomChamp;
	private Class<?> typeClass;
	private Integer tailleMax;

	EnStructRegle(String p_nomChamp, Class<?> p_typeChamp, Integer p_tailleMax) {
		this.nomChamp = p_nomChamp;
		this.typeClass = p_typeChamp;
		this.tailleMax = p_tailleMax;
	}

	/*
	 * (non-Javadoc)
	 * @see bdd.structure.StructureTable#getNomChamp()
	 */
	@Override
	public String getNomChamp() {
		return nomChamp;
	}

	/*
	 * (non-Javadoc)
	 * @see bdd.structure.StructureTable#getTailleMax()
	 */
	@Override
	public Integer getTailleMax() {
		return tailleMax;
	}

	/*
	 * (non-Javadoc)
	 * @see bdd.structure.StructureTable#getTypeChamp()
	 */
	@Override
	public Class<?> getTypeChamp() {
		return typeClass;
	}

}
