package bdd;

import tools.GestionRepertoire;

/**
 * contients les parametres de connexion a la base de données.
 * @author sims
 */
public class BDParamDll {

	private final String USER = "sysdba";
	private final String PASSWORD = "masterkey";
	private final String driverSGBD = "jdbc:firebirdsql:embedded";
	private final String HOSTNAME = "";
	private final String DatabaseName = "\\MAIL.FDB";
	private final String EmplacementBase = GestionRepertoire.RecupRepTravail()
			+ "\\Database" + DatabaseName;

	/**
	 * 
	 */
	public BDParamDll() {

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

	public String getDatabaseName() {
		return DatabaseName;
	}

}
