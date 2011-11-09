/**
 * 
 */
package thread;

import javax.mail.MessagingException;

import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeMessage;
import releve.imap.util.messageUtilisateur;
import bdd.accesTable.AccesTableMailRecu;
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
	private final AccesTableMailRecu accesMail;

	public ThreadDeplaceMessage(MlListeMessage p_list) {
		this.list = p_list;
		this.fenetre = new Patience(
				"Deplacement des messages vers la corbeille");
		this.accesMail = new AccesTableMailRecu();

	}

	@Override
	public void run() {

		MlCompteMail cpt = new MlCompteMail(list.get(0).getIdCompte());
		MlDossier dossier = new MlDossier(list.get(0).getIdDossier());
		fenetre.setVisible(true);
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt, list,
				fenetre);

		try {
			fact.deplaceMessageVersCorbeille();
			accesMail.deplaceMessageVersCorbeille(list);
			JTableFactory tableFactory = new JTableFactory();
			tableFactory.refreshJTable(dossier.getListMessage());
			JTreeFactory treeFactory = new JTreeFactory();
			treeFactory.refreshJTree();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur au deplacement des messages vers la corbeille");
		}

		fenetre.setVisible(false);

	}

}
