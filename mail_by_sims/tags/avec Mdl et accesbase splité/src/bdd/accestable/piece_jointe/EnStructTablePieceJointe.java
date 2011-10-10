/**
 * 
 */
package bdd.accestable.piece_jointe;

import java.sql.Blob;

import bdd.accestable.Structure;

/**
 * @author smardine
 */
public enum EnStructTablePieceJointe implements Structure {
	ID("ID_PIECE_JOINTE", Integer.class, null), //
	CONTENU("CONTENU_PIECE_JOINTE", Blob.class, null), //
	NOM("NOM_PIECE_JOINTE", String.class, 500), //
	ID_MESSAGE_PARENT("ID_MESSAGE", Integer.class, null);//

	private String nomChamp;
	private Class<?> typeChamp;
	private Integer tailleChamp;

	/**
	 * 
	 */
	EnStructTablePieceJointe(String p_nomChamp, Class<?> p_typeChamp,
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
