/**
 * 
 */
package bdd.accestable.param;

import bdd.accestable.Structure;

/**
 * @author smardine
 */
public enum EnStructTableParam implements Structure {
	VERSION("VERSION_BASE", Integer.class, null);

	private String nomChamp;
	private Class<?> typeChamp;
	private Integer tailleChamp;

	/**
	 * 
	 */
	EnStructTableParam(String p_nomChamp, Class<?> p_typeChamp,
			Integer p_tailleChamp) {
		this.nomChamp = p_nomChamp;
		this.typeChamp = p_typeChamp;
		this.tailleChamp = p_tailleChamp;
	}

	/*
	 * (non-Javadoc)
	 * @see bdd.accestable.Structure#getNomChamp()
	 */
	@Override
	public String getNomChamp() {
		return nomChamp;
	}

	/*
	 * (non-Javadoc)
	 * @see bdd.accestable.Structure#getTailleChamp()
	 */
	@Override
	public Integer getTailleChamp() {
		return tailleChamp;
	}

	/*
	 * (non-Javadoc)
	 * @see bdd.accestable.Structure#getTypeChamp()
	 */
	@Override
	public Class<?> getTypeChamp() {
		return typeChamp;
	}

}
