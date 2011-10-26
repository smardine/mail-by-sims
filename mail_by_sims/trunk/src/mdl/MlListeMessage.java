package mdl;

import java.util.ArrayList;
import java.util.List;

public class MlListeMessage extends ArrayList<MlMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6787933564954722125L;
	private final List<MlMessage> list = new ArrayList<MlMessage>();

	public MlListeMessage() {

	}

	public List<MlMessage> getlist() {
		return list;
	}

	public int getTailleListe() {
		return list.size();
	}

}
