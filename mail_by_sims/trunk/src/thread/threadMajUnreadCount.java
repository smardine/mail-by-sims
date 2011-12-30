/**
 * 
 */
package thread;

import mdl.mlcomptemail.MlCompteMail;
import mdl.mldossier.MlDossier;
import mdl.mlmessage.MlMessageGrille;
import bdd.accesTable.AccesTableCompte;
import bdd.accesTable.AccesTableDossier;
import factory.JTreeFactory;

/**
 * @author smardine
 */
public class threadMajUnreadCount extends Thread {

	private final MlMessageGrille messageGrille;
	private final JTreeFactory treeFact;
	private final AccesTableDossier accesDossier;
	private final AccesTableCompte accesCompte;
	private final boolean isLu;

	public threadMajUnreadCount(MlMessageGrille p_message,
			AccesTableCompte p_accesCompte) {
		this.messageGrille = p_message;
		this.accesCompte = p_accesCompte;
		this.isLu = messageGrille.isLu();
		treeFact = new JTreeFactory();
		accesDossier = new AccesTableDossier();

	}

	@Override
	public void run() {

		MlCompteMail cptMail = treeFact.rechercheCompteMail(messageGrille
				.getIdCompte());
		cptMail.setUnreadMessCount(accesCompte
				.getUnreadMessageFromCompte(cptMail.getIdCompte()));
		MlDossier dossier = treeFact.rechercheDossier(messageGrille
				.getIdDossier(), messageGrille.getIdCompte());
		dossier.setUnreadMessageCount(accesDossier.getUnreadMessageFromFolder(
				cptMail.getIdCompte(), dossier.getIdDossier()));
		while (dossier.getIdDossierParent() != 0) {
			dossier = treeFact.rechercheDossier(dossier.getIdDossierParent(),
					dossier.getIdCompte());
			if (isLu) {
				dossier
						.setUnreadMessageCount(dossier.getUnreadMessageCount() - 1);
			} else {
				dossier
						.setUnreadMessageCount(dossier.getUnreadMessageCount() + 1);
			}

		}

		treeFact.refreshJTree();
	}

}
