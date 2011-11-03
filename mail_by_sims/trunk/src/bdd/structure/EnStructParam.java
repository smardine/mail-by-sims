/**
 * 
 */
package bdd.structure;

/**
 * @author smardine
 */
public enum EnStructParam implements StructureTable {
	VERSION_BASE("VERSION_BASE", Integer.class, null);
	private String nomChamp;
	private Class<?> typeClass;
	private Integer tailleMax;

	EnStructParam(String p_nomChamp, Class<?> p_typeChamp, Integer p_tailleMax) {
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
