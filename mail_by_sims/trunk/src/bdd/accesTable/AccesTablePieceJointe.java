/**
 * 
 */
package bdd.accesTable;

import java.io.File;
import java.util.List;

import tools.GestionRepertoire;
import bdd.structure.EnStructPieceJointe;
import bdd.structure.EnTable;
import factory.RequetteFactory;

/**
 * @author smardine
 */
public class AccesTablePieceJointe {
	// private final String TAG = this.getClass().getSimpleName();
	private final RequetteFactory requeteFact;

	public AccesTablePieceJointe() {
		requeteFact = new RequetteFactory();
	}

	/**
	 * Obtenir la liste des ID de piece jointe a partir d'un ID de message
	 * @param p_idMessage
	 * @return
	 */
	public List<String> getListeIdPieceJointe(int p_idMessage) {
		String requette = "SELECT " + EnStructPieceJointe.ID.getNomChamp()
				+ " FROM " + EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage + " ORDER BY "
				+ EnStructPieceJointe.ID.getNomChamp();
		return requeteFact.getListeDeChamp(requette);
	}

	/**
	 * Obtenir la liste des NOM des piece jointe a partir d'un ID de message.
	 * @param p_idMessage
	 * @return
	 */
	public List<String> getListeNomPieceJointe(int p_idMessage) {
		String requette = "SELECT " + EnStructPieceJointe.NOM.getNomChamp()
				+ " FROM " + EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage + " ORDER BY "
				+ EnStructPieceJointe.ID.getNomChamp();
		return requeteFact.getListeDeChamp(requette);
	}

	/**
	 * Obtenir le contenu d'une piece jointe a partir d'un ID de message et du
	 * NOM de la piece jointe
	 * @param p_idMessage
	 * @param p_nameFile
	 * @return
	 */
	public File getPieceJointeFromIDMessage(int p_idMessage, String p_nameFile) {
		String requette = "SELECT " + EnStructPieceJointe.CONTENU.getNomChamp()
				+ " FROM " + EnTable.PIECE_JOINTE.getNomTable() + " WHERE "
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + "="
				+ p_idMessage + " and " + EnStructPieceJointe.NOM.getNomChamp()
				+ "='" + p_nameFile.trim() + "'";
		File contenuPieceJointe = new File(GestionRepertoire.RecupRepTravail()
				+ "/tempo/" + p_nameFile);
		if (contenuPieceJointe.exists()) {
			contenuPieceJointe.delete();
		}
		return requeteFact.writeBlobToFile(requette, contenuPieceJointe);

	}

}
