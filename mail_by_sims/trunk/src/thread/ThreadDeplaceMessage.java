/**
 * 
 */
package thread;

import javax.mail.MessagingException;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
import factory.DeplaceOuSupprFactory;
import factory.JTableFactory;
import factory.JTreeFactory;
import fenetre.Patience;

/**
 * @author smardine
 */
public class ThreadDeplaceMessage extends Thread {

	private final MlListeMessage list;
	private final Patience fenetre;
	private final String TAG = this.getClass().getSimpleName();
	private final BDRequette bd;

	public ThreadDeplaceMessage(MlListeMessage p_list) {
		this.list = p_list;
		this.fenetre = new Patience(
				"Deplacement des messages vers la corbeille");
		this.bd = new BDRequette();

	}

	@Override
	public void run() {

		MlCompteMail cpt = new MlCompteMail(list.get(0).getIdCompte());
		fenetre.setVisible(true);
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt, list,
				fenetre);

		try {
			fact.deplaceMessageVersCorbeille();
			bd.deplaceMessageVersCorbeille(list);
			JTableFactory tableFactory = new JTableFactory();
			tableFactory.refreshJTable(list);
			JTreeFactory treeFactory = new JTreeFactory();
			treeFactory.refreshJTree();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur au deplacement des messages vers la corbeille");
		}
		bd.closeConnexion();
		fenetre.setVisible(false);

	}

}
