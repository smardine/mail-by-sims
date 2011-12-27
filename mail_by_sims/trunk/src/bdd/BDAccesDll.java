package bdd;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.JOptionPane;

import org.firebirdsql.management.FBMaintenanceManager;
import org.firebirdsql.management.FBManager;

import releve.imap.util.messageUtilisateur;
import tools.Historique;
import bdd.structure.EnStructParam;
import bdd.structure.EnTable;
import factory.RequetteFactory;

/**
 * classe s'occupant de l'acces a la base de données
 * @author sims
 */
public class BDAccesDll {
	private final BDParamDll parametres;
	private final String TAG = this.getClass().getSimpleName();
	private boolean etatConnexion;
	private Connection connexion;
	private static FBManager firebirdManager;
	final int VERSION_BASE = 8;

	private static boolean ttACreer = false;
	private static FBMaintenanceManager maintenance;
	private static boolean librairyloaded = false;

	/**
	 * constructeur
	 */
	public BDAccesDll() {

		loadDlls();

		parametres = new BDParamDll();
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

			+ parametres.getEmplacementBase() + "?encoding=ISO8859_1&user="
					+ parametres.getUSER() + "&password="
					+ parametres.getPASSWORD();
			if (!isDataBaseExist(parametres.getEmplacementBase())) {
				ttACreer = true;
			}
			if (firebirdManager == null) {
				firebirdManager = new FBManager();
				firebirdManager.setCreateOnStart(true);// si le fichier base
				// n'existe pas
				// , il est créer
				firebirdManager.setType("embedded");
				firebirdManager.setUserName(parametres.getUSER());
				firebirdManager.setPassword(parametres.getPASSWORD());
				firebirdManager.setServer(parametres.getHOSTNAME());
				firebirdManager.setPort(3050);
				firebirdManager.setFileName(parametres.getEmplacementBase());

			}

			if (maintenance == null) {
				maintenance = new FBMaintenanceManager();
				maintenance.setDatabase(parametres.getEmplacementBase());
				maintenance.setHost(parametres.getHOSTNAME());
				maintenance.setUser(parametres.getUSER());
				maintenance.setPassword(parametres.getPASSWORD());
			}

			try {
				if (firebirdManager.getState().equals("Stopped")) {
					firebirdManager.start();
				}

			} catch (Exception e2) {
				messageUtilisateur.affMessageException(TAG, e2,
						"impossible de se connecter a la base");

			}

			try {

				connexion = DriverManager.getConnection(UrlDeConnexion);
				connexion.setAutoCommit(false);

				etatConnexion = true;
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Impossible de se connecter"
								+ " à la base de données\n\r");

				etatConnexion = false;

				System.exit(0);
			}
			if (ttACreer) {
				verifVersionBDD();
				ttACreer = false;
			}

		}

	}

	/**
	 * 
	 */
	private void loadDlls() {
		if (!librairyloaded) {
			try {
				System.loadLibrary("icudt30");
				System.loadLibrary("icuuc30");
				System.loadLibrary("icuin30");
				System.loadLibrary("fbembed");
			} catch (Exception e) {
				messageUtilisateur.affMessageException(TAG, e,
						"Impossible de charger les DLL firbird");
				librairyloaded = false;
			}
		}

		librairyloaded = true;
	}

	public boolean verifVersionBDD() {

		if (ttACreer) {// la base n'existait pas quand on s'y est
			// connecté, il faut passer les scripts de
			// creation
			BDScripts scripts = new BDScripts();
			ScriptExecutor se = new ScriptExecutor();
			LanceMiseAJour(se, scripts.getAll());

		} else {

			int versionActuelle = Integer.parseInt(getVersionActuelle());
			if (VERSION_BASE != versionActuelle) {
				ScriptExecutor se = new ScriptExecutor();
				BDScripts scripts = new BDScripts();
				switch (versionActuelle) {
					case 1:
						LanceMiseAJour(se, scripts.getVersion2());
					case 2:
						LanceMiseAJour(se, scripts.getVersion3());
					case 3:
						LanceMiseAJour(se, scripts.getVersion4());
					case 4:
						LanceMiseAJour(se, scripts.getVersion5());
					case 5:
						LanceMiseAJour(se, scripts.getVersion6());
					case 6:
						LanceMiseAJour(se, scripts.getVersion7());
					case 7:
						LanceMiseAJour(se, scripts.getVersion8());
				}
				verifVersionBDD();
			}

		}
		return true;

	}

	/**
	 * @param se
	 * @param p_list
	 * @throws SQLException
	 */
	private void LanceMiseAJour(ScriptExecutor se, List<String> p_list) {
		boolean succes;
		for (String s : p_list) {
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
				.lastIndexOf('\\')));
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
	public BDParamDll getParametres() {
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
		return BDAccesDll.firebirdManager;
	}

	/**
	 * @return the vERSION_BASE
	 */
	public int getVERSION_BASE() {
		return this.VERSION_BASE;
	}

	public String getVersionActuelle() {
		String script = "SELECT " + EnStructParam.VERSION_BASE + " FROM "
				+ EnTable.PARAM;
		String chaine_champ = "";
		ResultSet jeuEnregistrements = null;
		Statement state = null;
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
						RequetteFactory.IMPOSSIBLE_DE_FERMER_LA_TRANSACTION);
			}

		}

		return chaine_champ;
	}

	public boolean isExist() {
		return isDataBaseExist(parametres.getEmplacementBase());
	}

	/**
	 * @param maintenance the maintenance to set
	 */
	public void setMaintenanceManager(FBMaintenanceManager maintenance) {
		BDAccesDll.maintenance = maintenance;
	}

	/**
	 * @return the maintenance
	 */
	public FBMaintenanceManager getMaintenanceManager() {
		return maintenance;
	}
}
