package bdd.accestable.compte;

import bdd.accestable.Structure;

public enum EnStructTableCompte implements Structure {
	ID("ID_COMPTE", int.class, null), //
	NOM("NOM_COMPTE", String.class, 500), //
	SERVEURPOP("SERVEUR_POP", String.class, 500), //
	PORTPOP("PORT_POP", int.class, null), //
	SERVEUR_SMTP("SERVEUR_SMTP", String.class, 500), //
	PORT_SMTP("PORT_SMTP", int.class, null), //
	USERNAME("USERNAME", String.class, 500), //
	PWD("PWD", String.class, 500), //
	TYPE_COMPTES("TYPE_COMPTE", String.class, 7);

	private String nomChamp;
	private Class<?> typeChamp;
	private Integer tailleChamp;

	EnStructTableCompte(String p_nomChamp, Class<?> p_typeChamp,
			Integer p_tailleChamp) {
		this.nomChamp = p_nomChamp;
		this.typeChamp = p_typeChamp;
		this.tailleChamp = p_tailleChamp;
	}

	public String getNomChamp() {
		return nomChamp;
	}

	/**
	 * @return the typeChamp
	 */
	public Class<?> getTypeChamp() {
		return this.typeChamp;
	}

	/**
	 * @return the tailleChamp
	 */
	public Integer getTailleChamp() {
		return this.tailleChamp;
	}
}
