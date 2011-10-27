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

/**
 * @author smardine
 */
public class RequetteFactory {
	private static final String IMPOSSIBLE_DE_FERMER_LA_TRANSACTION = "Impossible de fermer la transaction";
	private final String TAG = this.getClass().getSimpleName();
	private final Connection connexion;

	public RequetteFactory(Connection p_connexion) {
		this.connexion = p_connexion;
	}

	/**
	 * Obtenir un champ a partir d'une requette
	 * @param requete
	 * @return
	 */
	public String get1Champ(String requete) {

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

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}

		}
		return chaine_champ;
	}

	/**
	 * Obtenir une liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	public List<String> getListeDeChamp(String p_requete) {
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
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}// c'est une lecture, pas de commit;

		}
		return lst;
	}

	/**
	 * Obtenir une liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	public List<ArrayList<String>> getListeDenregistrement(String p_requete) {
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
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}

		}
		return lstRetour;
	}

	/**
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectuée (delete, truncate...)
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeRequete(final String requete) {

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
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectuée (delete, truncate...)
	 * @param p_fDest
	 * @param p_fDestCache
	 * @param p_fDestCopy
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeRequeteWithBlob(String requete, File p_fContenu,
			File p_fDest, File p_fDestCopy, File p_fDestCache) {

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
	 * @param p_inputStream
	 * @throws IOException
	 */
	private void checkBinarayStreamOnClose(FileInputStream p_inputStream)
			throws IOException {
		if (p_inputStream != null) {
			p_inputStream.close();
		}
	}

	/**
	 * @param p_ps
	 * @param p_i
	 * @param p_inputDestinataire
	 * @param p_fDestinataires
	 * @return
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
	 * @param p_fDestinataires
	 * @return
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
	 * On execute simplement une requete sur la base
	 * @param requete -String la recherche effectuée (delete, truncate...)
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeMiseAJour(final String requete) {
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

			} catch (SQLException e) {
				Historique.ecrire("Message d'erreur: " + e
						+ "\n\r sur la requete : " + requete);
			}
		}
		return true;
	}

	public File writeBlobToFile(String requette, File p_file) {
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

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}

		}
		return p_file;

	}
}
