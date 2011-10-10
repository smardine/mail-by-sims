package bdd;

import tools.*;

/**
 * contients les parametres de connexion a la base de données.
 * @author sims
 */
public class BDParam {

	private final String USER = "sysdba";
	private final String PASSWORD = "masterkey";
	private final String driverSGBD = "jdbc:firebirdsql";
	private final String HOSTNAME = "localhost";
	private final String EmplacementBase = GestionRepertoire.RecupRepTravail()
			+ "\\Database\\MAIL.FDB";
	private final String Name = "\\MAIL.FDB";

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
		return Name;
	}

}
