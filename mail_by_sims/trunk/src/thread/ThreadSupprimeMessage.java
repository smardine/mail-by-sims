/**
 * 
 */
package thread;

import javax.mail.MessagingException;

import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeMessage;
import releve.imap.util.messageUtilisateur;
import factory.DeplaceOuSupprFactory;
import factory.JTableFactory;
import factory.JTreeFactory;
import fenetre.Patience;

/**
 * @author smardine
 */
public class ThreadSupprimeMessage extends Thread {

	private final MlListeMessage list;
	private final Patience fenetre;
	private final String TAG = this.getClass().getSimpleName();

	public ThreadSupprimeMessage(MlListeMessage p_list) {
		this.list = p_list;
		this.fenetre = new Patience("Suppression de message(s");
	}

	@Override
	public void run() {
		fenetre.setVisible(true);

		MlCompteMail cpt = new MlCompteMail(list.get(0).getIdCompte());
		MlDossier dossier = new MlDossier(list.get(0).getIdDossier());
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt, list,
				fenetre);
		try {
			fact.supprMessage();
			JTableFactory tableFactory = new JTableFactory();
			tableFactory.refreshJTable(dossier.getListMessage());
			JTreeFactory treeFactory = new JTreeFactory();
			treeFactory.refreshJTree();

		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur à la suppression des messages dans la corbeille");
		}
		fenetre.setVisible(false);

	}

}
