/**
 * 
 */
package thread;

import javax.mail.MessagingException;

import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeMessage;
import releve.imap.util.messageUtilisateur;
import bdd.accesTable.AccesTableCompte;
import bdd.accesTable.AccesTableDossier;
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
	private final AccesTableDossier accesDossier;
	private final AccesTableCompte accesCompte;

	public ThreadSupprimeMessage(MlListeMessage p_list) {
		this.list = p_list;
		this.fenetre = new Patience("Suppression de message(s");
		accesDossier = new AccesTableDossier();
		accesCompte = new AccesTableCompte();
	}

	@Override
	public void run() {
		fenetre.setVisible(true);
		JTreeFactory treeFactory = new JTreeFactory();
		MlCompteMail cpt = treeFactory.rechercheCompteMail(list.get(0)
				.getIdCompte());
		MlDossier dossier = treeFactory.rechercheDossier(list.get(0)
				.getIdDossier(), cpt.getIdCompte());

		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt, list,
				fenetre);
		try {
			fact.supprMessage();
			cpt.setUnreadMessCount(accesCompte.getUnreadMessageFromCompte(cpt
					.getIdCompte()));
			dossier.setUnreadMessageCount(accesDossier
					.getUnreadMessageFromFolder(dossier.getIdCompte(), dossier
							.getIdDossier()));
			while (dossier.getIdDossierParent() != 0) {
				dossier = treeFactory.rechercheDossier(dossier
						.getIdDossierParent(), cpt.getIdCompte());
				dossier.setUnreadMessageCount(accesDossier
						.getUnreadMessageFromFolder(dossier.getIdCompte(),
								dossier.getIdDossier()));
			}
			JTableFactory tableFactory = new JTableFactory();
			tableFactory.refreshJTable(dossier.getListMessage());

			treeFactory.refreshJTree();

		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur à la suppression des messages dans la corbeille");
		}
		fenetre.setVisible(false);

	}

}
