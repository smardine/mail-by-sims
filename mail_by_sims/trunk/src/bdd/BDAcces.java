package bdd;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.firebirdsql.management.FBManager;

import releve.imap.util.messageUtilisateur;
import tools.Historique;

/**
 * classe s'occupant de l'acces a la base de données
 * @author sims
 */
public class BDAcces {
	private final BDParam parametres;
	private final String TAG = this.getClass().getSimpleName();
	private boolean etatConnexion;
	private Connection connexion;
	private FBManager firebirdManager;
	final String VERSION_BASE = "6";

	private boolean ttACreer;

	/**
	 * constructeur
	 */
	public BDAcces() {

		parametres = new BDParam();
		try {
			Class.forName("org.firebirdsql.jdbc.FBDriver");
			etatConnexion = true;
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Classes non trouvées"
					+ " pour le chargement du pilote Firebird", "ALERTE",
					JOptionPane.ERROR_MESSAGE);
			// ok = false;
			etatConnexion = false;
		}

		if (etatConnexion) {
			String UrlDeConnexion = parametres.getDriverSGBD() + ":"
					+ parametres.getHOSTNAME() + ":"
					+ parametres.getEmplacementBase()
					+ "?encoding=ISO8859_1&user=" + parametres.getUSER()
					+ "&password=" + parametres.getPASSWORD();
			if (!isDataBaseExist(parametres.getEmplacementBase())) {
				ttACreer = true;
			}
			FBManager fbManager = new FBManager();
			firebirdManager = fbManager;
			fbManager.setCreateOnStart(true);// si le fichier base n'existe pas
			// , il est créer
			fbManager.setUserName(parametres.getUSER());
			fbManager.setPassword(parametres.getPASSWORD());
			fbManager.setServer(parametres.getHOSTNAME());
			fbManager.setPort(3050);
			fbManager.setFileName(parametres.getEmplacementBase());

			try {
				fbManager.start();
			} catch (Exception e2) {
				System.out.println("impossible de lancer la connexion");
			}

			try {

				connexion = DriverManager.getConnection(UrlDeConnexion);
				connexion.setAutoCommit(false);
				// connexion
				// .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				// connexion.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
				etatConnexion = true;
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,
						"Impossible de se connecter"
								+ " à la base de données\n\r" + e, "ALERTE",
						JOptionPane.ERROR_MESSAGE);
				etatConnexion = false;
				JOptionPane
						.showMessageDialog(
								null,
								"     Pour des raisons de sécurité, le programme va maintenant être fermé"
										+ " \n\r  il faudra le relancer et vérifier les login, mot de passe, nom du serveur et chemin de la base de données avant de lancer l'import",
								"ALERTE", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

			// try {
			// verifVersionBDD(exist);
			// } catch (SQLException e) {
			// messageUtilisateur.affMessageException(e,
			// "Erreur a la vérification de la BDD");
			// }

		}

	}

	public boolean verifVersionBDD(boolean p_exist) {

		if (ttACreer) {// la base n'existait pas quand on s'y est
			// connecté, il faut passer les scripts de
			// creation
			BDScripts scripts = new BDScripts();
			ScriptExecutor se = new ScriptExecutor();
			LanceMiseAJour(se, scripts.getAll());

		} else {

			String versionActuelle = getVersionActuelle();
			if (!VERSION_BASE.equals(versionActuelle)) {
				ScriptExecutor se = new ScriptExecutor();
				BDScripts scripts = new BDScripts();
				if ("1".equals(versionActuelle)) {
					LanceMiseAJour(se, scripts.getVersion2());
					verifVersionBDD(true);
				}
				if ("2".equals(versionActuelle)) {
					LanceMiseAJour(se, scripts.getVersion3());
					verifVersionBDD(true);
				}
				if ("3".equals(versionActuelle)) {
					LanceMiseAJour(se, scripts.getVersion4());
					verifVersionBDD(true);
				}
				if ("4".equals(versionActuelle)) {
					LanceMiseAJour(se, scripts.getVersion5());
					verifVersionBDD(true);
				}
				if ("5".equals(versionActuelle)) {
					LanceMiseAJour(se, scripts.getVersion6());
					verifVersionBDD(true);
				}
			}
		}
		return true;

	}

	/**
	 * @param se
	 * @param versionApasser
	 * @throws SQLException
	 */
	private void LanceMiseAJour(ScriptExecutor se,
			ArrayList<String> versionApasser) {
		boolean succes;
		for (String s : versionApasser) {
			succes = se.executeScriptSQL(connexion, s);
			if (!succes) {
				try {
					connexion.rollback();
				} catch (SQLException e) {

				}
				messageUtilisateur
						.affMessageErreur(
								TAG,
								"Impossible de mettre a jour la base de données \r\n Votre logiciel va se fermer");
				Historique.ecrire("Erreur a la mise a jour sur le script:\n\r"
						+ s);
				System.exit(0);
			} else {
				try {
					connexion.commit();
				} catch (SQLException e) {

				}
			}
		}
	}

	private boolean isDataBaseExist(String p_emplacementBase) {
		File f = new File(p_emplacementBase);
		File d = new File(p_emplacementBase.substring(0, p_emplacementBase
				.lastIndexOf("\\")));
		if (f.exists()) {// le fichier de base existe
			return true;
		}
		d.mkdirs();// si des dossier du repertoire n'existe pas, il seront créer
		return false;
	}

	public boolean getEtatConnexion() {
		return etatConnexion;
	}

	/**
	 * @return the parametres
	 */
	public BDParam getParametres() {
		return this.parametres;
	}

	/**
	 * @return the connexion
	 */
	public Connection getConnexion() {
		return this.connexion;
	}

	/**
	 * @return the firebirdManager
	 */
	public FBManager getFirebirdManager() {
		return this.firebirdManager;
	}

	/**
	 * @return the vERSION_BASE
	 */
	public String getVERSION_BASE() {
		return this.VERSION_BASE;
	}

	public String getVersionActuelle() {
		String script = "SELECT a.VERSION_BASE FROM PARAM a";

		String chaine_champ = "";
		Statement state = null;
		ResultSet jeuEnregistrements = null;
		try {
			state = connexion.createStatement();
			jeuEnregistrements = state.executeQuery(script);
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
						"Impossible de fermer la transaction");
			}

		}
		return chaine_champ;
	}

	public boolean isExist() {
		return isDataBaseExist(parametres.getEmplacementBase());
	}
}
