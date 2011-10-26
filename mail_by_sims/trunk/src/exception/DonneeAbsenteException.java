/**
 * 
 */
package exception;

/**
 * @author smardine
 */
public class DonneeAbsenteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6389366465326188733L;
	private final String TAG;
	private final String cause;

	public DonneeAbsenteException(String p_tag, String p_cause) {
		super(p_tag + p_cause);
		this.TAG = p_tag;
		this.cause = p_cause;

	}

}
