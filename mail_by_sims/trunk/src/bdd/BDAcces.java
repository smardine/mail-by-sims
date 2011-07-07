package bdd;

import imap.util.messageUtilisateur;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.firebirdsql.management.FBManager;

/**
 * classe s'occupant de l'acces a la base de données
 * @author sims
 */
public class BDAcces {
	private final BDParam parametres;
	static boolean etatConnexion;
	static Connection connexion;
	static FBManager firebirdManager;
	static final String VERSION_BASE = "2";

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
			boolean exist = isDataBaseExist(parametres.getEmplacementBase());

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

			try {
				verifVersionBDD(exist);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void createDatabase() {

	}

	public boolean verifVersionBDD(boolean p_exist) throws SQLException {
		if (p_exist == false) {// la base n'existait pas quand on s'y est
			// connecté, il faut passer les scripts de
			// creation
			BDScripts scripts = new BDScripts();
			ArrayList<String> version1 = scripts.getVersion1();
			boolean succes = true;
			ScriptExecutor se = new ScriptExecutor();
			LanceMiseAJour(se, version1);
			ArrayList<String> version2 = scripts.getVersion2();
			LanceMiseAJour(se, version2);

		} else {
			String versionActuelle = BDRequette.getVersionBase();
			if (!VERSION_BASE.equals(versionActuelle)) {
				ScriptExecutor se = new ScriptExecutor();
				BDScripts scripts = new BDScripts();
				if ("1".equals(versionActuelle)) {
					LanceMiseAJour(se, scripts.getVersion2());
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
			ArrayList<String> versionApasser) throws SQLException {
		boolean succes;
		for (String s : versionApasser) {
			succes = se.executeScriptSQL(connexion, s);
			if (!succes) {
				connexion.rollback();
				messageUtilisateur
						.affMessageErreur("Impossible de mettre a jour la base de données \r\n Votre logiciel va se fermer");
			} else {
				connexion.commit();
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
		// f.createNewFile();

		return false;
	}

	public boolean getEtatConnexion() {
		return etatConnexion;
	}

}
