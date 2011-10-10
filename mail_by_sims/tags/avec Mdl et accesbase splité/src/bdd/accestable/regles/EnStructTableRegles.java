/**
 * 
 */
package bdd.accestable.regles;

import bdd.accestable.Structure;

/**
 * @author smardine
 */
public enum EnStructTableRegles implements Structure {
	ID("ID_REGLE", Integer.class, null), //
	ADRESSE("ADRESSE_REGLE", String.class, 500), //
	ID_DOSSIER_STOCKAGE("ID_DOSSIER_STOCKAGE", Integer.class, null);//

	private String nomChamp;
	private Class<?> typeChamp;
	private Integer tailleChamp;

	EnStructTableRegles(String p_nomChamp, Class<?> p_typeChamp,
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
