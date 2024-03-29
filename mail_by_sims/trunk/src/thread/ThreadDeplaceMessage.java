/**
 * 
 */
package thread;

import javax.mail.MessagingException;

import mdl.mlcomptemail.MlCompteMail;
import mdl.mldossier.MlDossier;
import mdl.mlmessage.MlListeMessageGrille;
import releve.imap.util.messageUtilisateur;
import bdd.accesTable.AccesTableDossier;
import bdd.accesTable.AccesTableMailRecu;
import factory.DeplaceOuSupprFactory;
import factory.JTreeFactory;
import fenetre.Patience;

/**
 * @author smardine
 */
public class ThreadDeplaceMessage extends Thread {

	private final MlListeMessageGrille list;
	private final Patience fenetre;
	private final String TAG = this.getClass().getSimpleName();
	private final AccesTableMailRecu accesMail;
	private final AccesTableDossier accesDossier;

	private final MlCompteMail compteMail;

	private final MlDossier corbeille;
	private final JTreeFactory treeFactory;

	public ThreadDeplaceMessage(MlListeMessageGrille p_lstADepl,
			MlCompteMail p_cptMail) {
		this.list = p_lstADepl;
		this.compteMail = p_cptMail;
		this.treeFactory = new JTreeFactory();

		this.corbeille = treeFactory.rechercheDossier(compteMail
				.getIdCorbeille(), compteMail.getIdCompte());

		this.fenetre = new Patience(
				"Deplacement des messages vers la corbeille");
		this.accesMail = new AccesTableMailRecu();
		this.accesDossier = new AccesTableDossier();

	}

	@Override
	public void run() {

		fenetre.setVisible(true);
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(compteMail,
				list, fenetre);

		try {
			fact.deplaceMessageVersCorbeille();
			accesMail.deplaceMessageVersCorbeille(list);

			// dossierMail.setUnreadMessageCount(accesDossier
			// .getUnreadMessageFromFolder(dossierMail.getIdCompte(),
			// dossierMail.getIdDossier()));
			// while (dossierMail.getIdDossierParent() != 0) {
			// dossierMail = treeFactory.rechercheDossier(dossierMail
			// .getIdDossierParent(), compteMail.getIdCompte());
			// dossierMail.setUnreadMessageCount(accesDossier
			// .getUnreadMessageFromFolder(dossierMail.getIdCompte(),
			// dossierMail.getIdDossier()));
			// }

			corbeille.setUnreadMessageCount(accesDossier
					.getUnreadMessageFromFolder(corbeille.getIdCompte(),
							corbeille.getIdDossier()));
			treeFactory.majUnreadCount(list);
			treeFactory.refreshJTree();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur au deplacement des messages vers la corbeille");
		}

		fenetre.setVisible(false);

	}
}
