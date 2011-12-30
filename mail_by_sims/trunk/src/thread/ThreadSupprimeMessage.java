/**
 * 
 */
package thread;

import javax.mail.MessagingException;

import mdl.mlcomptemail.MlCompteMail;
import mdl.mlmessage.MlListeMessageGrille;
import releve.imap.util.messageUtilisateur;
import factory.DeplaceOuSupprFactory;
import factory.JTreeFactory;
import fenetre.Patience;

/**
 * @author smardine
 */
public class ThreadSupprimeMessage extends Thread {

	private final MlListeMessageGrille list;
	private final Patience fenetre;
	private final String TAG = this.getClass().getSimpleName();
	private final MlCompteMail compteMail;

	public ThreadSupprimeMessage(MlListeMessageGrille p_mlListeMessageGrille,
			MlCompteMail p_cptMail) {
		this.list = p_mlListeMessageGrille;
		this.compteMail = p_cptMail;

		this.fenetre = new Patience("Suppression de message(s");

	}

	@Override
	public void run() {
		fenetre.setVisible(true);
		JTreeFactory treeFactory = new JTreeFactory();
		treeFactory.majUnreadCount(list);
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(compteMail,
				list, fenetre);
		try {
			fact.supprMessage();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur à la suppression des messages dans la corbeille");
		}
		fenetre.setVisible(false);

	}
}
