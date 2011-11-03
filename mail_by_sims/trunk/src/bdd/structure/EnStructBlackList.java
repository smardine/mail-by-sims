/**
 * 
 */
package bdd.structure;

/**
 * @author smardine
 */
public enum EnStructBlackList implements StructureTable {
	ID("ID_BLACKLIST", Integer.class, null), //
	ADRESSE("ADRESSE_BLACKLIST", String.class, 500), //
	ID_DOSSIER("ID_DOSSIER", Integer.class, null);

	private String nomChamp;
	private Class<?> typeClass;
	private Integer tailleMax;

	/**
	 * 
	 */
	private EnStructBlackList(String p_nomChamp, Class<?> p_class,
			Integer p_tailleMax) {
		this.nomChamp = p_nomChamp;
		this.typeClass = p_class;
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
