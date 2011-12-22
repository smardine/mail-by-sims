/**
 * 
 */
package thread;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import releve.imap.util.messageUtilisateur;
import bdd.accesTable.AccesTableCompte;
import exception.DonneeAbsenteException;
import factory.CompteMailFactory;
import factory.JTreeFactory;
import fenetre.Patience;

/**
 * @author smardine
 */
public class threadSuppressCompte extends Thread {

	private final Patience fenetrePatience;
	private final JProgressBar progressBar;
	private final JLabel label;
	private final MlCompteMail compteMail;
	private final String TAG = this.getClass().getSimpleName();

	public threadSuppressCompte(MlCompteMail p_compteMail) {
		this.compteMail = p_compteMail;
		fenetrePatience = new Patience("Suppresion du compte "
				+ compteMail.getNomCompte());

		progressBar = fenetrePatience.getjProgressBar();
		label = fenetrePatience.getjLabel();
	}

	@Override
	public void run() {
		fenetrePatience.setVisible(true);
		CompteMailFactory fact = new CompteMailFactory();
		boolean result;
		try {
			JTreeFactory treeFact = new JTreeFactory();
			treeFact.supprimeCompteNode(compteMail);
			treeFact.reloadJtree();
			result = fact.suppressionCompteMail(compteMail, label, progressBar);
		} catch (DonneeAbsenteException e) {
			result = false;
		}
		fenetrePatience.setVisible(false);
		// BDRequette bd = new BDRequette();
		// boolean result = bd.deleteCompte(bd.getIdComptes(nomCompte));
		if (result) {
			messageUtilisateur.affMessageInfo("Suppression du compte réussie");
			// on recupere la liste des comptes et on l'affiche
			AccesTableCompte accesCompte = new AccesTableCompte();
			MlListeCompteMail lst = accesCompte.getListeDeComptes();

			DefaultListModel model = (DefaultListModel) ComposantVisuelCommun
					.getJListCompteMail().getModel();
			model.clear();
			for (MlCompteMail cpt : lst) {
				model.addElement(cpt.getNomCompte());
			}

			// treeFact.refreshJTree();

		} else {
			messageUtilisateur.affMessageErreur(TAG,
					"Erreur lors de la suppression du compte");

		}
		fenetrePatience.setVisible(false);
	}

}
