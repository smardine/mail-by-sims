package fenetre.comptes.gestion.MlActionGestion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mdl.ComposantVisuelCommun;
import mdl.mlcomptemail.MlCompteMail;
import releve.imap.util.REPONSE;
import releve.imap.util.messageUtilisateur;
import thread.threadSuppressCompte;
import fenetre.comptes.creation.choixFAI;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionComptes implements ActionListener {

	private final GestionCompte fenetre;

	// private final JTree tree;

	public MlActionComptes(GestionCompte p_fenetre) {
		this.fenetre = p_fenetre;
		// this.tree = p_treeCompte;
	}

	@Override
	public void actionPerformed(ActionEvent p_arg0) {
		if (EnActionComptes.CREER.getLib().equals(p_arg0.getActionCommand())) {
			fenetre.dispose();
			new choixFAI();
			// new CreationComptesPop(tree);
		}
		if (EnActionComptes.SUPPRIMER.getLib()
				.equals(p_arg0.getActionCommand())) {
			REPONSE reponse = messageUtilisateur
					.affMessageQuestionOuiNon(
							"Question?",
							"Etes vous sur de vouloir supprimer ce compte? Vous supprimerez egalement tous les messages qui y sont associés!!");
			if (reponse == REPONSE.OUI) {
				String nomCompte = (String) ComposantVisuelCommun
						.getJListCompteMail().getSelectedValue();
				threadSuppressCompte t = new threadSuppressCompte(
						new MlCompteMail(nomCompte));
				t.start();

			}
		}

	}

}
