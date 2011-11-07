package fenetre.principale.jtree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
import factory.DossierFactory;
import factory.JTreeFactory;

public class MlActionPopupJTree implements ActionListener {

	private final JTree tree;

	public MlActionPopupJTree(JTree p_jTree) {
		this.tree = p_jTree;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// pour avoir la selection courante du jtree
		// on peut en deduire le compte, donc l'id Dssier correspondant

		String actionCommand = arg0.getActionCommand();
		TreePath selectionPath = tree.getSelectionPath();

		if ("AJOUTER".equals(actionCommand)) {
			String nomNewDossier = messageUtilisateur
					.affMessageInput("Quel est le nom du dossier?");
			if (null == nomNewDossier || nomNewDossier.equals("")) {
				return;
			}
			DefaultMutableTreeNode dossierParent = (DefaultMutableTreeNode) selectionPath
					.getLastPathComponent();
			BDRequette bd = new BDRequette();
			int idCompte = bd.getIdComptes((String) selectionPath
					.getPathComponent(1).toString());

			int idDossierParent = bd.getIdDossier(dossierParent.toString(),
					idCompte);

			if ("".equals(idDossierParent)) {
				idDossierParent = 0;
			}
			if (bd.createNewDossier(idCompte, idDossierParent, nomNewDossier,
					"")) {
				TreePath newTreePath = selectionPath
						.pathByAddingChild(new DefaultMutableTreeNode(
								nomNewDossier));
				// TreePath newTreePath = new TreePath(selectionPath.toString()
				// .replace("[", "").replace("]", "")
				// + ", " + nomNewDossier);
				// ComposantVisuelCommun.setTreePath(newTreePath);
				JTreeFactory treeFact = new JTreeFactory();
				treeFact.ajouteNode(selectionPath,
						(DefaultMutableTreeNode) newTreePath
								.getLastPathComponent());
				// tree.getModel().valueForPathChanged(selectionPath,
				// newTreePath.getLastPathComponent());
				tree.setSelectionPath(newTreePath);
				ComposantVisuelCommun.setTree(tree);

			}

		}
		if ("SUPPRIMER".equals(actionCommand)) {
			Object[] pathComplet = selectionPath.getPath();
			DossierFactory dossierFact = new DossierFactory(new MlCompteMail(
					pathComplet[1].toString()));
			dossierFact.deleteDossier(selectionPath);
			// String dossierASupprimer = (String) selectionPath
			// .getLastPathComponent();
			// BDRequette bd = new BDRequette();
			// int idCompte = bd.getIdComptes((String) selectionPath
			// .getPathComponent(1));
			// int idDossier = bd.getIdDossier(dossierASupprimer, idCompte);
			//
			// REPONSE rep = messageUtilisateur
			// .affMessageQuestionOuiNon(
			// "Question",
			// "Voulez vous supprimer le dossier \""
			// + dossierASupprimer
			// +
			// "\" ? \n\r Vous supprimerez egalement tous les messages contenu dans ce dossier et ses sous dossiers");
			//
			// switch (rep) {
			// case OUI:// on supprime le dossier
			// bd.deleteDossier(idCompte, idDossier, null);
			// // ComposantVisuelCommun.setTreePath(selectionPath
			// // .getParentPath());
			// tree.getModel().valueForPathChanged(selectionPath,
			// ActionTree.SUPPRIMER);
			// tree.setSelectionPath(selectionPath.getParentPath());
			// break;
			// case NON:// on ne fait rien
			// break;
			//
			// }
			// bd.closeConnexion();

		}

	}
}
