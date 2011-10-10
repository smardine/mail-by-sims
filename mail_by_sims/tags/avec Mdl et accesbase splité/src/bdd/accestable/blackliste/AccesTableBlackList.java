/**
 * 
 */
package bdd.accestable.blackliste;

import java.sql.Connection;

import bdd.BDAcces;
import bdd.accestable.AccesTable;

/**
 * @author smardine
 */
public class AccesTableBlackList extends AccesTable {
	private final String TAG = this.getClass().getSimpleName();
	private final Connection laConnexion;
	private final BDAcces bd;

	public AccesTableBlackList() {
		connect();
		bd = AccesTable.bd;
		laConnexion = bd.getConnexion();
	}

}
