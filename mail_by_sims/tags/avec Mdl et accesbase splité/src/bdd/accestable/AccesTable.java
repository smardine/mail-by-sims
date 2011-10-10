package bdd.accestable;

import java.sql.SQLException;

import tools.Historique;
import bdd.BDAcces;

public class AccesTable {

	protected static BDAcces bd;

	protected void connect() {

		// if (bd == null || !bd.getEtatConnexion()) {
		bd = new BDAcces();
		// }

		// try {
		// System.out
		// .println("" + bd.getConnexion().getTransactionIsolation());
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	protected void Disconnect() {
		try {
			bd.getConnexion().close();
		} catch (SQLException e) {
			Historique.ecrire("Impossible de fermer la base.");
		}
	}
}
