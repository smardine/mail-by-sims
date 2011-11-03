/**
 * 
 */
package bdd.structure;

import java.sql.Blob;

/**
 * @author smardine
 */
public enum EnStructPieceJointe implements StructureTable {
	ID("ID_PIECE_JOINTE", Integer.class, null), //
	CONTENU("CONTENU_PIECE_JOINTE", Blob.class, null), //
	NOM("NOM_PIECE_JOINTE", String.class, 500), //
	ID_MESSAGE("ID_MESSAGE", Integer.class, null);

	private String nomChamp;
	private Class<?> typeClass;
	private Integer tailleMax;

	//
	;

	EnStructPieceJointe(String p_nomChamp, Class<?> p_typeChamp,
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
