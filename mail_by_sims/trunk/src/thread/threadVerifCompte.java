package thread;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
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
	private final JProgressBar progressBar;
	private final JLabel label;
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
		fenetrePatience.setVisible(true);
		progressBar = fenetrePatience.getjProgressBar();
		label = fenetrePatience.getjLabel();
	}

	public threadVerifCompte(MlCompteMail p_compteMail, JTree p_tree,
			CreationComptesGmailHotmail p_fenetre) {
		this.tree = p_tree;
		this.compteMail = p_compteMail;
		this.fenetreGmail = p_fenetre;
		this.fenetre = null;
		fenetrePatience = new Patience("Test du compte "
				+ compteMail.getNomCompte());
		fenetrePatience.setVisible(true);
		progressBar = fenetrePatience.getjProgressBar();
		label = fenetrePatience.getjLabel();
	}

	@Override
	public void run() {
		CompteMailFactory cptFact = new CompteMailFactory();
		if (cptFact.testBal(compteMail, progressBar, label)) {
			label.setText("Test de connexion réussie");
			boolean result = cptFact.creationCompteMail(compteMail);

			if (!result) {
				messageUtilisateur.affMessageErreur(TAG,
						"le compte n'a pas été correctement enregistré");

			} else {
				ReleveFactory relevFact = new ReleveFactory(compteMail,
						progressBar, null, null);
				label.setText("Récuperation des dossiers");
				relevFact.recupereListeDossier();

				messageUtilisateur
						.affMessageInfo("Le compte à été créer correctement");
				DefaultListModel model = (DefaultListModel) ComposantVisuelCommun
						.getJListCompteMail().getModel();

				BDRequette bd = new BDRequette();
				MlListeCompteMail lst = bd.getListeDeComptes();
				model.clear();
				bd.closeConnexion();
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

			}

		} else {
			fenetrePatience.setVisible(false);
			messageUtilisateur
					.affMessageErreur(
							TAG,
							"Le test de connexion a votre boite aux lettres à échoué.\r\n Merci de vérifier votre saisie");
			return;
		}
	}

}
