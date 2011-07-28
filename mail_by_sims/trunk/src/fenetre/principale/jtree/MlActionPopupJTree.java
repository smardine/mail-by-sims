package fenetre.principale.jtree;

import fenetre.principale.Main;
import imap.util.REPONSE;
import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import bdd.BDRequette;

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
			String dossierParent = (String) selectionPath
					.getLastPathComponent();

			int idCompte = BDRequette.getIdComptes((String) selectionPath
					.getPathComponent(1));

			int idDossierParent = BDRequette.getIdDossier(dossierParent,
					idCompte);

			if ("".equals(idDossierParent)) {
				idDossierParent = 0;
			}
			if (BDRequette.createNewDossier(idCompte, idDossierParent,
					nomNewDossier)) {
				TreePath newTreePath = new TreePath(selectionPath.toString()
						.replace("[", "").replace("]", "")
						+ ", " + nomNewDossier);
				Main.setTreePath(newTreePath);
				tree.getModel().valueForPathChanged(newTreePath,
						ActionTree.AJOUTER);
				tree.setSelectionPath(newTreePath);

			}

		}
		if ("SUPPRIMER".equals(actionCommand)) {
			String dossierASupprimer = (String) selectionPath
					.getLastPathComponent();

			int idCompte = BDRequette.getIdComptes((String) selectionPath
					.getPathComponent(1));
			int idDossier = BDRequette
					.getIdDossier(dossierASupprimer, idCompte);

			REPONSE rep = messageUtilisateur
					.affMessageQuestionOuiNon(
							"Question",
							"Voulez vous supprimer le dossier \""
									+ dossierASupprimer
									+ "\" ? \n\r Vous supprimerez egalement tous les messages contenu dans ce dossier et ses sous dossiers");

			switch (rep) {
				case OUI:// on supprime le dossier
					BDRequette.deleteDossier(idCompte, idDossier);
					Main.setTreePath(selectionPath.getParentPath());
					tree.getModel().valueForPathChanged(selectionPath,
							ActionTree.SUPPRIMER);
					tree.setSelectionPath(selectionPath.getParentPath());
					break;
				case NON:// on ne fait rien
					break;

			}

		}

	}
}
