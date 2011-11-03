/**
 * 
 */
package bdd.structure;

import java.sql.Blob;
import java.sql.Date;

/**
 * @author smardine
 */
public enum EnStructMailRecu implements StructureTable {
	ID_COMPTE("ID_COMPTE", Integer.class, null), //
	ID_DOSSIER("ID_DOSSIER_STOCKAGE", Integer.class, null), //
	ID_MESSAGE("ID_MESSAGE_RECU", Integer.class, null), //
	UID("UID_MESSAGE", String.class, 1000), //
	EXPEDITEUR("EXPEDITEUR", String.class, 500), //
	DEST("DESTINATAIRE", Blob.class, null), //
	DEST_COPY("DESTINATAIRE_COPY", Blob.class, null), //
	DEST_CACHE("DESTINATAIRE_CACHE", Blob.class, null), //
	SUJET("SUJET", String.class, 1000), //
	CONTENU("CONTENU", Blob.class, null), //
	DATE_RECEPTION("DATE_RECEPTION", Date.class, null), //
	STATUT("STATUT", String.class, 1), //
	FLAG("FLAG", EnFlag.class, null)//
	;
	private String nomChamp;
	private Class<?> typeClass;
	private Integer tailleMax;

	EnStructMailRecu(String p_nomChamp, Class<?> p_typeChamp,
			Integer p_tailleMax) {
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
