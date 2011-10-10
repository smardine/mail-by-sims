package mdl;

import java.util.ArrayList;

import bdd.accestable.compte.AccesTableCompte;

public class MlListeCompteMail {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1950310049081717436L;

	private static final ArrayList<MlCompteMail> list = new ArrayList<MlCompteMail>();

	private MlListeCompteMail() {

	}

	/**
	 * @param p_arrayList
	 */
	private static void refreshList(ArrayList<MlCompteMail> p_arrayList) {
		for (MlCompteMail cpt : p_arrayList) {
			if (!list.contains(cpt)) {
				list.add(cpt);
			}
		}
	}

	public static int getSize() {
		return list.size();
	}

	public static void add(MlCompteMail p_cpt) {
		list.add(p_cpt);
	}

	public static MlListeCompteMail getListeCompte() {
		AccesTableCompte accesCompte = new AccesTableCompte();
		if (list == null || getSize() == 0) {
			list.addAll(accesCompte.getListeDeComptes());
		} else {
			refreshList(accesCompte.getListeDeComptes());
		}
		MlListeCompteMail listOfficelle = new MlListeCompteMail();
		list.copyTo(listOfficelle);
		listOfficelle.addAll(list);
		return listOfficelle;
	}

	public boolean contains(Object p_compteMail) {
		for (MlCompteMail cpt : list) {
			if (cpt.getIdCompte() == ((MlCompteMail) p_compteMail)
					.getIdCompte()) {
				return true;
			}
		}
		return false;

	}

}
