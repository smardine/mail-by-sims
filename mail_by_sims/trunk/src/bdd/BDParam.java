package bdd;

import tools.GestionRepertoire;

/**
 * contients les parametres de connexion a la base de données.
 * @author sims
 */
public class BDParam {

	private final String USER = "sysdba";
	private final String PASSWORD = "masterkey";
	private final String driverSGBD = "jdbc:firebirdsql";
	private final String HOSTNAME = "localhost";
	private final String DatabaseName = "\\MAIL.FDB";
	private final String EmplacementBase = GestionRepertoire.RecupRepTravail()
			+ "\\Database" + DatabaseName;

	/**
	 * 
	 */
	public BDParam() {

	}

	public String getUSER() {
		return USER;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public String getDriverSGBD() {
		return driverSGBD;
	}

	public String getHOSTNAME() {
		return HOSTNAME;
	}

	public String getEmplacementBase() {
		return EmplacementBase;
	}

	public String getName() {
		return DatabaseName;
	}

}
