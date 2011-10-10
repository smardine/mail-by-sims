/**
 * 
 */
package bdd.accestable.mail_recu;

import java.sql.Blob;
import java.util.Date;

import bdd.accestable.Structure;

/**
 * @author smardine
 */
public enum EnStructTableMailRecu implements Structure {

	ID("ID_MESSAGE_RECU", Integer.class, null), //
	ID_COMPTE("ID_COMPTE", Integer.class, null), //
	ID_DOSSIER_STOCKAGE("ID_DOSSIER_STOCKAGE", Integer.class, null), //
	UID("UID_MESSAGE", String.class, 1000), //
	EXPEDITEUR("EXPEDITEUR", String.class, 500), //
	DESTINATAIRE("DESTINATAIRE", Blob.class, null), //
	SUJET("SUJET", String.class, 1000), //
	CONTENU("CONTENU", Blob.class, null), //
	DATE_RECEPTION("DATE_RECEPTION", Date.class, null), //
	STATUT("STATU", String.class, 1);//
	private String nomChamp;
	private Class<?> typeChamp;
	private Integer tailleChamp;

	//

	/**
	 * 
	 */
	private EnStructTableMailRecu(String p_nomChamp, Class<?> p_typeChamp,
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
