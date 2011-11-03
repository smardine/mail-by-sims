/**
 * 
 */
package bdd.structure;

/**
 * @author smardine
 */
public enum EnFlag {
	SUPPR("S");

	private String flag;

	EnFlag(String p_flag) {
		this.flag = p_flag;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

}
