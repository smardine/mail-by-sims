package mdl;

import java.util.ArrayList;

public class MlListeCompteMail {

	private final ArrayList<MlCompteMail> list = new ArrayList<MlCompteMail>();

	public ArrayList<MlCompteMail> getlisteCompte() {
		return list;
	}

	public int getSize() {
		return list.size();
	}

}
