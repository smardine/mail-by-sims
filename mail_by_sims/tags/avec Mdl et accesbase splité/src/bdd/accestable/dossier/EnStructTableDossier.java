package bdd.accestable.dossier;

import bdd.accestable.Structure;

public enum EnStructTableDossier implements Structure {

	ID("ID_DOSSIER", Integer.class, null), //
	ID_COMPTE("ID_COMPTE", Integer.class, null), //
	ID_DOSSIER_PARENT("ID_DOSSIER_PARENT", Integer.class, null), //
	NOM_DOSSIER("NOM_DOSSIER", String.class, null), //
	NOM_INTERNET("NOM_INTERNET", String.class, null);

	private String nomChamp;
	private Class<?> typeChamp;
	private Integer tailleChamp;

	private EnStructTableDossier(String p_nomChamp, Class<?> p_typeChamp,
			Integer p_tailleChamp) {
		this.nomChamp = p_nomChamp;
		this.typeChamp = p_typeChamp;
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
