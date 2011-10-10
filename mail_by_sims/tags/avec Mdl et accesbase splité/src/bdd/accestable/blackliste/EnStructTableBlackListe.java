package bdd.accestable.blackliste;

import bdd.accestable.Structure;

public enum EnStructTableBlackListe implements Structure {

	ID("ID_BLACKLIST", Integer.class, null), //
	ADRESSE("ADRESSE_BLACKLIST", String.class, 500), //
	ID_DOSSIER("ID_DOSSIER", Integer.class, null);

	private String nomChamp;
	private Class<?> typeChamp;
	private Integer tailleChamp;

	EnStructTableBlackListe(String p_nomChamp, Class<?> p_class,
			Integer p_tailleChamp) {
		this.nomChamp = p_nomChamp;
		this.typeChamp = p_class;
		this.tailleChamp = p_tailleChamp;
	}

	@Override
	public String getNomChamp() {

		return nomChamp;
	}

	@Override
	public Integer getTailleChamp() {

		return tailleChamp;
	}

	@Override
	public Class<?> getTypeChamp() {

		return typeChamp;
	}

}
