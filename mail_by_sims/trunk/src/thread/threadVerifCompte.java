package thread;

import javax.swing.DefaultListModel;
import javax.swing.JTree;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
import factory.CompteMailFactory;
import factory.ReleveFactory;
import fenetre.Patience;
import fenetre.comptes.creation.CreationComptesGmailHotmail;
import fenetre.comptes.creation.CreationComptesPop;
import fenetre.comptes.gestion.GestionCompte;

public class threadVerifCompte extends Thread {

	private final MlCompteMail compteMail;
	private final Patience fenetrePatience;
	private final String TAG = this.getClass().getSimpleName();
	private final JTree tree;
	private final CreationComptesPop fenetre;
	private CreationComptesGmailHotmail fenetreGmail;

	public threadVerifCompte(MlCompteMail p_compteMail, JTree p_tree,
			CreationComptesPop p_fenetre) {
		this.tree = p_tree;
		this.fenetre = p_fenetre;
		this.compteMail = p_compteMail;
		fenetrePatience = new Patience("Test du compte "
				+ compteMail.getNomCompte());

	}

	public threadVerifCompte(MlCompteMail p_compteMail, JTree p_tree,
			CreationComptesGmailHotmail p_fenetre) {
		this.tree = p_tree;
		this.compteMail = p_compteMail;
		this.fenetreGmail = p_fenetre;
		this.fenetre = null;
		fenetrePatience = new Patience("Test du compte "
				+ compteMail.getNomCompte());

	}

	@Override
	public void run() {
		fenetrePatience.setVisible(true);
		CompteMailFactory cptFact = new CompteMailFactory();
		if (cptFact.testBal(compteMail, fenetrePatience)) {
			fenetrePatience.afficheInfo("Test de connexion r�ussie", "", 0);
			boolean result = cptFact.creationCompteMail(compteMail);

			if (!result) {
				messageUtilisateur.affMessageErreur(TAG,
						"le compte n'a pas �t� correctement enregistr�");

			} else {
				ReleveFactory relevFact = new ReleveFactory(compteMail,
						fenetrePatience);
				relevFact.recupereListeDossier();

				messageUtilisateur
						.affMessageInfo("Le compte � �t� cr�er correctement");
				DefaultListModel model = (DefaultListModel) ComposantVisuelCommun
						.getJListCompteMail().getModel();

				BDRequette bd = new BDRequette();
				MlListeCompteMail lst = bd.getListeDeComptes();
				model.clear();

				for (MlCompteMail cpt : lst) {
					model.addElement(cpt.getNomCompte());
				}
				if (fenetre != null) {
					fenetre.dispose();
				} else if (fenetreGmail != null) {
					fenetreGmail.dispose();
				}
				fenetrePatience.setVisible(false);
				new GestionCompte(tree);
				return;
			}

		} else {
			fenetrePatience.setVisible(false);
			messageUtilisateur
					.affMessageErreur(
							TAG,
							"Le test de connexion a votre boite aux lettres � �chou�.\r\n Merci de v�rifier votre saisie");
			return;
		}
	}

}
