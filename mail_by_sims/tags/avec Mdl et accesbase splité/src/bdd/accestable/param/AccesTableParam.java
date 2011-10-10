/**
 * 
 */
package bdd.accestable.param;

import java.sql.Connection;

import bdd.BDAcces;
import bdd.accestable.AccesTable;

/**
 * @author smardine
 */
public class AccesTableParam extends AccesTable {

	private final Connection laConnexion;
	private final BDAcces bd;

	public AccesTableParam() {
		connect();
		bd = AccesTable.bd;
		laConnexion = bd.getConnexion();
	}

}
