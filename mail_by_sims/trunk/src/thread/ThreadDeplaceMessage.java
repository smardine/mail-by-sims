/**
 * 
 */
package thread;

import javax.mail.MessagingException;

import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeMessage;
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

	private final MlListeMessage list;
	private final Patience fenetre;
	private final String TAG = this.getClass().getSimpleName();
	private final AccesTableMailRecu accesMail;
	private final AccesTableDossier accesDossier;

	private final MlCompteMail compteMail;
	private MlDossier dossierMail;
	private final MlDossier corbeille;

	public ThreadDeplaceMessage(MlListeMessage p_list, MlCompteMail p_cptMail,
			MlDossier p_dossierMail, MlDossier p_corbeille) {
		this.list = p_list;
		this.compteMail = p_cptMail;
		this.dossierMail = p_dossierMail;
		this.corbeille = p_corbeille;

		this.fenetre = new Patience(
				"Deplacement des messages vers la corbeille");
		this.accesMail = new AccesTableMailRecu();
		this.accesDossier = new AccesTableDossier();

	}

	@Override
	public void run() {
		JTreeFactory treeFactory = new JTreeFactory();
		// MlCompteMail cpt = treeFactory.rechercheCompteMail(list.get(0)
		// .getIdCompte());
		// MlDossier dossier = treeFactory.rechercheDossier(list.get(0)
		// .getIdDossier(), cpt.getIdCompte());

		fenetre.setVisible(true);
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(compteMail,
				list, fenetre);

		try {
			fact.deplaceMessageVersCorbeille();
			accesMail.deplaceMessageVersCorbeille(list);
			dossierMail.setUnreadMessageCount(accesDossier
					.getUnreadMessageFromFolder(dossierMail.getIdCompte(),
							dossierMail.getIdDossier()));
			while (dossierMail.getIdDossierParent() != 0) {
				dossierMail = treeFactory.rechercheDossier(dossierMail
						.getIdDossierParent(), compteMail.getIdCompte());
				dossierMail.setUnreadMessageCount(accesDossier
						.getUnreadMessageFromFolder(dossierMail.getIdCompte(),
								dossierMail.getIdDossier()));
			}
			// MlDossier corbeille = treeFactory.rechercheDossier(cpt
			// .getIdCorbeille(), cpt.getIdCompte());
			corbeille.setUnreadMessageCount(accesDossier
					.getUnreadMessageFromFolder(corbeille.getIdCompte(),
							corbeille.getIdDossier()));
			// JTableFactory tableFactory = new JTableFactory();
			// tableFactory.refreshJTable(dossier.getListMessage());

			treeFactory.refreshJTree();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur au deplacement des messages vers la corbeille");
		}

		fenetre.setVisible(false);

	}
}
