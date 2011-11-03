/**
 * 
 */
package bdd.structure;

import fenetre.comptes.EnTypeCompte;

/**
 * @author smardine
 */
public enum EnStructCompte implements StructureTable {
	ID("ID_COMPTE", Integer.class, null), //
	NOM("NOM_COMPTE", String.class, 500), //
	POP_SERVEUR("SERVEUR_POP", String.class, 500), //
	POP_PORT("PORT_POP", Integer.class, null), //
	SMTP_SERVEUR("SERVEUR_SMTP", String.class, 500), //
	SMTP_PORT("PORT_SMTP", Integer.class, null), //
	USER("USERNAME", String.class, 500), //
	PWD("PWD", String.class, 500), //
	TYPE("TYPE_COMPTE", EnTypeCompte.class, null);

	private String nomChamp;
	private Class<?> typeClass;
	private Integer tailleMax;

	EnStructCompte(String p_nomChamp, Class<?> p_typeClass, Integer p_tailleMax) {
		this.nomChamp = p_nomChamp;
		this.typeClass = p_typeClass;
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
