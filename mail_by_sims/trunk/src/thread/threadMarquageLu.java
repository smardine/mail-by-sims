/**
 * 
 */
package thread;

import mdl.MlListeMessageGrille;
import mdl.MlMessageGrille;
import bdd.accesTable.AccesTableMailRecu;
import fenetre.Patience;

/**
 * @author smardine
 */
public class threadMarquageLu extends Thread {

	private final int[] tabId;
	private final AccesTableMailRecu accesMail;
	private final Patience fenetre;

	public threadMarquageLu(int[] p_tabId) {
		this.tabId = p_tabId;
		accesMail = new AccesTableMailRecu();
		fenetre = new Patience("Marquage LU");

	}

	@Override
	public void run() {
		fenetre.setVisible(true);
		MlListeMessageGrille lstMailLu = new MlListeMessageGrille();
		for (int i = 0; i < tabId.length; i++) {
			int prctage = ((i + 1) * 100) / tabId.length;
			fenetre.afficheInfo("Marquage comme lu", prctage + " %", prctage);
			MlMessageGrille m = new MlMessageGrille(tabId[i]);
			// m.setLu(true);
			lstMailLu.add(m);

		}// fin de for
		accesMail.updateStatusLecture(lstMailLu, true);
		fenetre.setVisible(false);
	}
}
