package importMail;

import java.util.ArrayList;

public class MlListeMessage extends ArrayList<MlMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6787933564954722125L;
	private final ArrayList<MlMessage> list = new ArrayList<MlMessage>();

	public MlListeMessage() {

	}

	public ArrayList<MlMessage> getlist() {
		return list;
	}

	public int getTailleListe() {
		return list.size();
	}

}
