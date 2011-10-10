/**
 * 
 */
package bdd.accestable.piece_jointe;

import imap.util.messageUtilisateur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import mdl.MlPieceJointe;
import tools.GestionRepertoire;
import bdd.BDAcces;
import bdd.RequetteFactory;
import bdd.accestable.AccesTable;

/**
 * @author smardine
 */
public class AccesTablePieceJointe extends AccesTable {

	private final Connection laConnexion;
	BDAcces bd;
	private final RequetteFactory reqFactory;

	public AccesTablePieceJointe() {
		connect();
		bd = AccesTable.bd;
		laConnexion = bd.getConnexion();
		reqFactory = new RequetteFactory(laConnexion);
	}

	public ArrayList<String> getListeIdPieceJointe(int p_idMessage) {
		String requette = "SELECT a.ID_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return reqFactory.getListeDeChamp(requette);
	}

	public ArrayList<String> getListNomPieceJointe(int p_idMessage) {
		String requette = "SELECT a.NOM_PIECE_JOINTE FROM PIECE_JOINTE a where a.ID_MESSAGE='"
				+ p_idMessage + "' ORDER BY a.ID_PIECE_JOINTE";
		return reqFactory.getListeDeChamp(requette);
	}

	public boolean enregistrePieceJointe(String maxId, File p_PieceJointe) {
		String requette = "INSERT INTO PIECE_JOINTE (CONTENU_PIECE_JOINTE,NOM_PIECE_JOINTE, ID_MESSAGE) VALUES ("
				+ "?,'"
				+ p_PieceJointe.getName().replace("'", "_")
				+ "','"
				+ maxId + "')";
		PreparedStatement ps = null;
		FileInputStream inPieceJointe = null;
		boolean resultatRequete = false;
		try {
			ps = laConnexion.prepareStatement(requette);
			inPieceJointe = new FileInputStream(p_PieceJointe);
			ps.setBinaryStream(1, inPieceJointe, (int) p_PieceJointe.length());
			ps.executeUpdate();
			resultatRequete = true;

		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur à l'insertion d'un blob");
			return false;
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"impossible de trouver le fichier");
			return false;
		} finally {
			try {
				if (resultatRequete) {
					laConnexion.commit();
				} else {
					laConnexion.rollback();
				}
				ps.close();
				inPieceJointe.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Erreur à l'insertion d'un blob");
				return false;
			} catch (IOException e) {
				messageUtilisateur.affMessageException(e, "fichier non trouvé");
				return false;
			}
		}
		return true;
	}

	public boolean messageHavePieceJointe(int p_idMessage) {
		String requette = "SELECT COUNT (*) FROM PIECE_JOINTE WHERE ID_MESSAGE='"
				+ p_idMessage + "'";
		int messageCount = Integer.parseInt(reqFactory.get1Champ(requette));
		return messageCount > 0;
	}

	public File getPieceJointeFromIDMessage(int p_idMessage, String p_nameFile) {
		String requette = "SELECT CONTENU_PIECE_JOINTE FROM PIECE_JOINTE WHERE ID_MESSAGE="
				+ p_idMessage
				+ " and NOM_PIECE_JOINTE='"
				+ p_nameFile.trim()
				+ "'";
		File contenuPieceJointe = new File(GestionRepertoire.RecupRepTravail()
				+ "/tempo/" + p_nameFile);
		if (contenuPieceJointe.exists()) {
			contenuPieceJointe.delete();
		}
		return reqFactory.writeBlobToFile(requette, contenuPieceJointe);
	}

	public File getContenuPieceJointeFromIdPieceJointeId(
			MlPieceJointe p_pieceJointe) {
		String requette = "SELECT CONTENU_PIECE_JOINTE FROM PIECE_JOINTE WHERE ID_PIECE_JOINTE="
				+ p_pieceJointe.getIdPiecejointe();
		File contenuPieceJointe = new File(GestionRepertoire.RecupRepTravail()
				+ "/tempo/" + p_pieceJointe.getNomPieceJointe());
		return reqFactory.writeBlobToFile(requette, contenuPieceJointe);
	}

	public ArrayList<String> getPieceJointeById(int p_idPieceJointe) {
		String script = "SELECT a.NOM_PIECE_JOINTE, a.ID_MESSAGE"
				+ " FROM PIECE_JOINTE a" + " where a.ID_PIECE_JOINTE="
				+ p_idPieceJointe;
		ArrayList<ArrayList<String>> lstResultat = reqFactory
				.getListeDenregistrement(script);
		return lstResultat.get(0);
	}

}
