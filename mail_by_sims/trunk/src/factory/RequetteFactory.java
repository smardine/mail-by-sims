/**
 * 
 */
package factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import releve.imap.util.messageUtilisateur;
import tools.Historique;
import bdd.BDAccesDll;
import bdd.structure.EnStructPieceJointe;
import bdd.structure.EnTable;

/**
 * Cette classe s'occupent des toutes les requettes effectuée en base
 * @author smardine
 */
public class RequetteFactory {
	public static final String IMPOSSIBLE_DE_FERMER_LA_TRANSACTION = "Impossible de fermer la transaction";
	private final String TAG = this.getClass().getSimpleName();

	/**
	 * Constructeur
	 * @param p_connexion
	 * @param p_connexion - la connexion a la base
	 */
	public RequetteFactory() {

	}

	/**
	 * Obtenir la valeur d'un champ a partir d'une requette
	 * @param requete
	 * @return la valeur recherchée, "" si aune valeur trouvée.
	 */
	public String get1Champ(String requete) {
		Connection connexion = checkConnexion();
		String chaine_champ = "";
		ResultSet jeuEnregistrements = null;
		Statement state = null;
		try {
			state = connexion.createStatement();
			jeuEnregistrements = state.executeQuery(requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {
				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					chaine_champ = jeuEnregistrements.getString(i);
				}
			}

		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
			messageUtilisateur.affMessageException(TAG, e, "Erreur SQL");
		} finally {
			try {
				jeuEnregistrements.close();
				state.close();
				connexion.rollback();
				connexion.close();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}

		}
		return chaine_champ;
	}

	/**
	 * Obtenir une liste de valeur de champ a partir d'une requette
	 * @param p_requete
	 * @return la liste des valeurs de champs trouvée, liste vide si rien trouvé
	 */
	public List<String> getListeDeChamp(String p_requete) {
		Connection connexion = checkConnexion();
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		ArrayList<String> lst = new ArrayList<String>();
		try {
			state = connexion.createStatement();
			jeuEnregistrements = state.executeQuery(p_requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {

				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					String chaine_champ = jeuEnregistrements.getString(i);
					lst.add(chaine_champ);
				}
			}

		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
		} finally {
			try {
				connexion.rollback();
				jeuEnregistrements.close();
				state.close();
				connexion.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}// c'est une lecture, pas de commit;

		}
		return lst;
	}

	/**
	 * Obtenir une liste de liste de valeur de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	public List<ArrayList<String>> getListeDenregistrement(String p_requete) {
		Connection connexion = checkConnexion();
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		ArrayList<ArrayList<String>> lstRetour = new ArrayList<ArrayList<String>>();
		try {
			state = connexion.createStatement();
			jeuEnregistrements = state.executeQuery(p_requete);
			final ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {
				ArrayList<String> lstintermediaire = new ArrayList<String>();
				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					String chaine_champ = jeuEnregistrements.getString(i);
					lstintermediaire.add(chaine_champ);
				}
				lstRetour.add(lstintermediaire);
			}

		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
		} finally {
			try {
				connexion.rollback();
				jeuEnregistrements.close();
				state.close();
				connexion.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}

		}
		return lstRetour;
	}

	/**
	 * On execute simplement une requete sur la base
	 * @param requete -String la requette à passer (delete, truncate...)
	 * @return true si ca a marché, sinon false
	 */
	public boolean executeRequete(final String requete) {
		Connection connexion = checkConnexion();
		Statement state = null;
		boolean resultatRequete = false;
		try {
			state = connexion.createStatement();
			resultatRequete = state.execute(requete);

		} catch (final SQLException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur a l'execution d'une requete");
			Historique.ecrire("Message d'erreur: " + e
					+ "\n\r sur la requete : " + requete);
			return false;
		} finally {
			try {
				if (resultatRequete) {
					connexion.commit();
				} else {
					if (state.getUpdateCount() > 0) {
						connexion.commit();
					} else {
						connexion.rollback();
					}
				}
				state.close();
				connexion.close();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"erreur a l'execution d'une requete");
				Historique.ecrire("Message d'erreur: " + e
						+ "\n\r sur la requete : " + requete);

				return false;
			}
		}
		return true;
	}

	/**
	 * Execute une requette preparée avec des {@link File} en parametres
	 * d'entrée a destination de blob dans la base
	 * @param requete -String la recherche a passer
	 * @param p_fDest - un fichier
	 * @param p_fDestCache - un autre fichier
	 * @param p_fDestCopy - un troisieme fichier
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeRequeteWithBlob(String requete, File p_fContenu,
			File p_fDest, File p_fDestCopy, File p_fDestCache) {
		Connection connexion = checkConnexion();
		PreparedStatement ps = null;
		FileInputStream inputContenu = null;
		FileInputStream inputDestinataire = null;
		FileInputStream inputDestCopy = null;
		FileInputStream inputDestcache = null;
		boolean resultatRequete = false;
		try {
			ps = connexion.prepareStatement(requete);
			inputDestinataire = checkFileForBlob(p_fDest);
			inputDestCopy = checkFileForBlob(p_fDestCopy);
			inputDestcache = checkFileForBlob(p_fDestCache);
			inputContenu = checkFileForBlob(p_fContenu);

			ps = checkBinaryStreamForBlob(ps, 1, inputDestinataire, p_fDest);
			ps = checkBinaryStreamForBlob(ps, 2, inputDestCopy, p_fDestCopy);
			ps = checkBinaryStreamForBlob(ps, 3, inputDestcache, p_fDestCache);
			ps = checkBinaryStreamForBlob(ps, 4, inputContenu, p_fContenu);

			ps.executeUpdate();
			resultatRequete = true;

		} catch (SQLException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur à l'insertion d'un blob");
			return false;
		} finally {
			try {
				if (resultatRequete) {
					connexion.commit();
				} else {
					connexion.rollback();
				}
				if (ps != null) {
					ps.close();
				}
				connexion.close();
				checkBinarayStreamOnClose(inputContenu);
				checkBinarayStreamOnClose(inputDestinataire);
				checkBinarayStreamOnClose(inputDestCopy);
				checkBinarayStreamOnClose(inputDestcache);

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Erreur à l'insertion d'un blob");
			} catch (IOException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"fichier non trouvé");
			}
		}
		return true;
	}

	/**
	 * Verifie si un flux est fermé, le ferme si besoin
	 * @param p_inputStream - le flux a verifier
	 * @throws IOException
	 */
	private void checkBinarayStreamOnClose(FileInputStream p_inputStream)
			throws IOException {
		if (p_inputStream != null) {
			p_inputStream.close();
		}
	}

	/**
	 * Valorise la requette preparée avec le fichier a enregistrer sous forme de
	 * blob
	 * @param p_ps - le statment préparé
	 * @param p_idxParam - l'index de parametres dans la requette preparée
	 * @param p_inputStream - le flux d'entrée
	 * @param p_fileToBlob - le fichier d'origine
	 * @return le statment préparé valorisé
	 * @throws SQLException
	 */
	private PreparedStatement checkBinaryStreamForBlob(PreparedStatement p_ps,
			int p_idxParam, FileInputStream p_inputStream, File p_fileToBlob)
			throws SQLException {
		if (null != p_inputStream && null != p_fileToBlob) {
			p_ps.setBinaryStream(p_idxParam, p_inputStream, (int) p_fileToBlob
					.length());
		} else {
			p_ps.setBinaryStream(p_idxParam, p_inputStream, 0);
		}

		return p_ps;
	}

	/**
	 * Verifie qu'un fichie rpeut etre inseré en base sous forme de blob et le
	 * transforme en flux
	 * @param p_fileForBlob - le fichier ciblé
	 * @return - le flux correspondant
	 */
	private FileInputStream checkFileForBlob(File p_fileForBlob) {
		if (p_fileForBlob != null && p_fileForBlob.exists()) {
			try {
				return new FileInputStream(p_fileForBlob);
			} catch (FileNotFoundException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Execution d'un script de mise a jour sur la base
	 * @param requete -String la requette a passer
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeMiseAJour(final String requete) {
		Connection connexion = checkConnexion();
		String[] tab = requete.split(";");
		int EXECUTE_FAILED = -3;
		Statement state = null;
		boolean resultatRequete = true;
		try {
			state = connexion.createStatement();
			for (String s : tab) {
				state.addBatch(s);
			}
			int[] results = state.executeBatch();

			for (int unresultat : results) {
				if (unresultat == EXECUTE_FAILED) {
					resultatRequete = false;
					break;
				}
			}

		} catch (final SQLException e) {
			messageUtilisateur.affMessageException(TAG, e, "sur la requete : "
					+ requete);
			return false;
		} finally {
			try {
				if (resultatRequete) {
					connexion.commit();
				} else {
					connexion.rollback();
				}

				state.close();
				connexion.close();
			} catch (SQLException e) {
				Historique.ecrire("Message d'erreur: " + e
						+ "\n\r sur la requete : " + requete);
			}
		}
		return true;
	}

	/**
	 * A partir d'une requette SQL entregister un blob sous firme de fichier
	 * @param requette - le requette
	 * @param p_file - le fichier a créer
	 * @return le fichier crée
	 */
	public File writeBlobToFile(String requette, File p_file) {
		Connection connexion = checkConnexion();
		ResultSet resultSet = null;
		try {
			PreparedStatement stmt = connexion.prepareStatement(requette);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				FileOutputStream fos = new FileOutputStream(p_file);
				byte[] buffer = new byte[256];
				// Get the binary stream of our BLOB data
				InputStream is = resultSet.getBinaryStream(1);
				while (is.read(buffer) > 0) {
					fos.write(buffer);
				}
				fos.close();
			}
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur Affichage du message");
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible d'afficher le message");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible d'afficher le message");
		} finally {
			try {
				resultSet.close();
				connexion.rollback();
				connexion.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}

		}
		return p_file;

	}

	/**
	 * Permet de verifier l'etat de la connexion a la base, si connexion est
	 * null ou bien si la connexion est férmée, on relance une connexion a la
	 * bdd
	 */
	private Connection checkConnexion() {
		return new BDAccesDll().getConnexion();
	}

	/**
	 * enregistrer une piece jointe en base sous forme de blob
	 * @param p_idMessageParent l'id du message parent
	 * @param p_PieceJointe le fichier piece jointe a enregistrer en base
	 * @return true ou false suivant si ca a reussi
	 */
	public boolean enregistrePieceJointe(String p_idMessageParent,
			File p_PieceJointe) {
		Connection connexion = checkConnexion();
		String requette = "INSERT INTO " + EnTable.PIECE_JOINTE.getNomTable()
				+ " (" + EnStructPieceJointe.CONTENU.getNomChamp() + ","
				+ EnStructPieceJointe.NOM.getNomChamp() + ","
				+ EnStructPieceJointe.ID_MESSAGE.getNomChamp() + ") VALUES ("
				+ "?,'" + p_PieceJointe.getName().replace("'", "_") + "','"
				+ p_idMessageParent + "')";
		PreparedStatement ps = null;
		FileInputStream inPieceJointe = null;
		boolean resultatRequete = false;
		try {
			ps = connexion.prepareStatement(requette);
			inPieceJointe = new FileInputStream(p_PieceJointe);
			ps.setBinaryStream(1, inPieceJointe, (int) p_PieceJointe.length());
			ps.executeUpdate();
			resultatRequete = true;

		} catch (SQLException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur à l'insertion d'un blob");
			return false;
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"impossible de trouver le fichier");
			return false;
		} finally {
			try {
				if (resultatRequete) {
					connexion.commit();
				} else {
					connexion.rollback();
				}
				ps.close();
				inPieceJointe.close();
				connexion.close();

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Erreur à l'insertion d'un blob");
				return false;
			} catch (IOException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"fichier non trouvé");
				return false;

			}
		}
		return true;

	}
}
