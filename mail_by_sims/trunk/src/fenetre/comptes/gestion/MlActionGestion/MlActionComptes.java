package fenetre.comptes.gestion.MlActionGestion;

import imap.util.REPONSE;
import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JTree;

import bdd.BDRequette;
import fenetre.comptes.creation.CreationComptes;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionComptes implements ActionListener {
	private final GestionCompte fenetre;
	private final JTree tree;

	public MlActionComptes(GestionCompte p_fenetre, JTree p_treeCompte) {
		this.fenetre = p_fenetre;
		this.tree = p_treeCompte;
	}

	@Override
	public void actionPerformed(ActionEvent p_arg0) {
		if (EnActionComptes.CREER.getLib().equals(p_arg0.getActionCommand())) {
			fenetre.dispose();
			new CreationComptes(tree);
		}
		if (EnActionComptes.SUPPRIMER.getLib()
				.equals(p_arg0.getActionCommand())) {
			REPONSE reponse = messageUtilisateur
					.affMessageQuestionOuiNon(
							"Question?",
							"Etes vous sur de vouloir supprimer ce compte? Vous supprimerez egalement tous les messages qui y sont associés!!");
			if (reponse == REPONSE.OUI) {
				String nomCompte = (String) GestionCompte.jList
						.getSelectedValue();

				BDRequette bd = new BDRequette();
				boolean result = bd.deleteCompte(bd.getIdComptes(nomCompte));
				if (result) {
					messageUtilisateur
							.affMessageInfo("Suppression du compte réussie");
					// on recupere la liste des comptes et on l'affiche
					ArrayList<String> lst = bd.getListeDeComptes();
					DefaultListModel model = (DefaultListModel) GestionCompte.jList
							.getModel();
					model.clear();
					for (String s : lst) {
						model.addElement(s);
					}
				} else {
					messageUtilisateur
							.affMessageErreur("Erreur lors de la suppression du compte");
				}
				bd.closeConnexion();

			}
		}

	}

}
