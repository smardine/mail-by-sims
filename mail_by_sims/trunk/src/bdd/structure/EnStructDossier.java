/**
 * 
 */
package bdd.structure;

/**
 * @author smardine
 */
public enum EnStructDossier implements StructureTable {
	ID_COMPTE("ID_COMPTE", Integer.class, null), //
	ID_DOSSIER("ID_DOSSIER", Integer.class, null), //
	ID_DOSSIER_PARENT("ID_DOSSIER_PARENT", Integer.class, null), //
	NOM("NOM_DOSSIER", String.class, 500), //
	NOM_INTERNET("NOM_INTERNET", String.class, 999);
	private String nomChamp;
	private Class<?> typeClass;
	private Integer tailleMax;

	/**
	 * 
	 */
	EnStructDossier(String p_nomChamp, Class<?> p_typeChamp, Integer p_tailleMax) {
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
