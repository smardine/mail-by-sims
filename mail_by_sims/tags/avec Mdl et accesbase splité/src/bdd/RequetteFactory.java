package bdd;

import imap.util.messageUtilisateur;

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

import tools.GestionRepertoire;
import tools.Historique;
import tools.WriteFile;
import bdd.accestable.EnTable;
import bdd.accestable.Structure;
import bdd.accestable.compte.EnStructTableCompte;

/**
 * cette classe s'occupe uniquement des requetes
 * @author sims
 * @param <Connexion>
 */
public class RequetteFactory {

	private final Connection laConnexion;

	/**
	 * Constructeur privé car c'est une classe utilitaire (que des methode )
	 */
	public RequetteFactory(Connection p_connexion) {
		laConnexion = p_connexion;

	}

	/**
	 * Execution d'une requette sur la base
	 * @param requete La requette a executer
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeRequete(final String requete) {
		Statement state = null;
		boolean resultatRequete = false;
		try {
			state = laConnexion.createStatement();
			resultatRequete = state.execute(requete);
		} catch (final SQLException e) {
			messageUtilisateur.affMessageException(e,
					"erreur a l'execution d'une requete");
			Historique.ecrire("Message d'erreur: " + e
					+ "\n\r sur la requete : " + requete);
			return false;
		} finally {
			try {

				if (resultatRequete) {
					laConnexion.commit();
				} else {
					if (state.getUpdateCount() > 0) {
						laConnexion.commit();
					} else {
						laConnexion.rollback();
					}
				}
				if (state != null) {
					state.close();
				}

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"erreur a l'execution d'une requete");
				Historique.ecrire("Message d'erreur: " + e
						+ "\n\r sur la requete : " + requete);
				return false;
			}
		}
		return true;
	}

	/**
	 * Execute une requette avec 2 blob en parametres.
	 * @param requete la requette a executée
	 * @param p_fContenu - le contenu du mail sous forme de blob
	 * @param p_fDestinataires - la liste des destinataire sous forme de blob
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeRequeteWithBlob(String requete, File p_fContenu,
			File p_fDestinataires) {
		PreparedStatement statement = null;
		FileInputStream inputContenu = null;
		FileInputStream inputDestinataire = null;
		boolean resultatRequete = false;
		try {
			statement = laConnexion.prepareStatement(requete);
			inputDestinataire = new FileInputStream(p_fDestinataires);
			inputContenu = new FileInputStream(p_fContenu);
			statement.setBinaryStream(1, inputDestinataire,
					(int) p_fDestinataires.length());
			statement.setBinaryStream(2, inputContenu, (int) p_fContenu
					.length());
			statement.executeUpdate();
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
				if (statement != null) {
					statement.close();
				}
				if (inputContenu != null) {
					inputContenu.close();
				}
				if (inputDestinataire != null) {
					inputDestinataire.close();
				}
				if (resultatRequete) {
					laConnexion.commit();
				} else {
					laConnexion.rollback();
				}

			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Erreur à l'insertion d'un blob");
			} catch (IOException e) {
				messageUtilisateur.affMessageException(e, "fichier non trouvé");
			}
		}
		return true;
	}

	/**
	 * On execute une requette de mise a jour (cf {@link BDScripts})
	 * @param requete la requette a executer
	 * @return vrai si ca a marché, sinon faux
	 */
	public boolean executeMiseAJour(final String requete) {
		String[] tab = requete.split(";");
		int EXECUTE_FAILED = -3;
		Statement state = null;
		boolean resultatRequete = true;
		try {
			state = laConnexion.createStatement();
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
			System.out.println(e);
			Historique.ecrire("Message d'erreur: " + e
					+ "\n\r sur la requete : " + requete);
			return false;
		} finally {
			try {
				if (state != null) {
					state.close();
				}
				if (resultatRequete) {
					laConnexion.commit();
				} else {
					laConnexion.rollback();
				}

			} catch (SQLException e) {
				Historique.ecrire("Message d'erreur: " + e
						+ "\n\r sur la requete : " + requete);
			}
		}
		return true;
	}

	/**
	 * Obtenir le nombre d'enregistrement d'une table
	 * @param p_table - {@link EnTable}
	 * @param p_champ - {@link EnStructTableCompte} - le nom d'un champ de la
	 *            table
	 * @return le nombre de ligne dans cette table
	 */
	public int getNbEnregistrementFromTable(EnTable p_table, Structure p_champ) {
		int nbRecords = 0;
		String requete = "Select " + p_champ.getNomChamp() + " from "
				+ p_table.getNomTable();
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		try {
			state = laConnexion.createStatement();
			jeuEnregistrements = state.executeQuery(requete);
			while (jeuEnregistrements.next()) {
				nbRecords++;
			}
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible de recuperer le nombre de champ dans la table "
							+ p_table.getNomTable());
		} finally {
			try {
				if (jeuEnregistrements != null) {
					jeuEnregistrements.close();
				}
				if (state != null) {
					state.close();
				}
				if (laConnexion != null) {
					laConnexion.rollback();
				}
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la base");
			}
		}
		return nbRecords;
	}

	/**
	 * Obtenir un champ a partir d'une requette
	 * @param requete
	 * @return un champ correspondant a la requette
	 */
	public String get1Champ(String requete) {
		String chaine_champ = "";
		ResultSet jeuEnregistrements = null;
		Statement state = null;
		try {
			state = laConnexion.createStatement();

			jeuEnregistrements = state.executeQuery(requete);
			ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
					.getMetaData();

			while (jeuEnregistrements.next()) {
				for (int i = 1; i <= infojeuEnregistrements.getColumnCount(); i++) {
					chaine_champ = jeuEnregistrements.getString(i);
				}
			}
		} catch (SQLException e) {
			Historique.ecrire("Erreur SQL :" + e);
			messageUtilisateur.affMessageException(e, "Erreur SQL");
		} finally {
			try {
				if (jeuEnregistrements != null) {
					jeuEnregistrements.close();
				}
				if (state != null) {
					state.close();
				}
				if (laConnexion != null) {
					laConnexion.rollback();
				}
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}
		}
		return chaine_champ;
	}

	/**
	 * Obtenir une liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	public ArrayList<String> getListeDeChamp(String p_requete) {
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		ArrayList<String> lst = new ArrayList<String>();
		try {
			state = laConnexion.createStatement();

			jeuEnregistrements = state.executeQuery(p_requete);
			ResultSetMetaData infojeuEnregistrements = jeuEnregistrements
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
				if (jeuEnregistrements != null) {
					jeuEnregistrements.close();
				}
				if (state != null) {
					state.close();
				}
				if (laConnexion != null) {
					laConnexion.rollback();
				}
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}// c'est une lecture, pas de commit;
		}
		return lst;
	}

	/**
	 * Obtenir une liste de liste de champ a partir d'une requette
	 * @param p_requete
	 * @return
	 */
	public ArrayList<ArrayList<String>> getListeDenregistrement(String p_requete) {
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		ArrayList<ArrayList<String>> lstRetour = new ArrayList<ArrayList<String>>();
		try {
			state = laConnexion.createStatement();
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
				if (jeuEnregistrements != null) {
					jeuEnregistrements.close();
				}
				if (state != null) {
					state.close();
				}
				if (laConnexion != null) {
					laConnexion.rollback();
				}
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}
		}
		return lstRetour;
	}

	/**
	 * Prend {@link contenu} et le met dans un repertoire temporaire
	 * (GestionRepertoire.RecupRepTravail() + "\\tempo") avecl'extension
	 * precisée par {@link p_extension}
	 * @param contenu
	 * @param p_extension
	 * @return le ficheir créer sous forme de File
	 */
	public File createFileForBlob(String contenu, String p_extension) {
		File reptempo = new File(GestionRepertoire.RecupRepTravail()
				+ "\\tempo");
		if (!reptempo.exists()) {
			reptempo.mkdirs();
		}
		File f = new File(reptempo + "\\temfile" + p_extension);
		try {
			f.createNewFile();
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"impossible de créer le fichier " + f.getName());
			return null;
		}
		WriteFile.WriteFullFile(contenu, reptempo + "\\temfile" + p_extension);
		return f;
	}

	/**
	 * Prendre le contenu d'un blob (decrit par la requette) et le mettre dans
	 * p_file
	 * @param requette
	 * @param p_file
	 * @return
	 */
	public File writeBlobToFile(String requette, File p_file) {
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		try {
			stmt = laConnexion.prepareStatement(requette);
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
				is.close();
			}
		} catch (SQLException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur Affichage du message");
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible d'afficher le message");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible d'afficher le message");
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (laConnexion != null) {
					laConnexion.rollback();
				}
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(e,
						"Impossible de fermer la transaction");
			}

		}
		return p_file;
	}

}
