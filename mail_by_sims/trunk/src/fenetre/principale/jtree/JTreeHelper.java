package fenetre.principale.jtree;

import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import bdd.BDRequette;
import fenetre.principale.MlAction.MlActionMainCombo;

public final class JTreeHelper {

	private JTreeHelper() {

	}

	public static void reloadJtree(JTree p_tree) {
		TreePath originalTreePath = p_tree.getSelectionPath();
		int[] originalSelectionRow = p_tree.getSelectionRows();
		p_tree.setModel(new ArborescenceBoiteMail());
		p_tree.setSelectionPath(originalTreePath);
		p_tree.setSelectionRows(originalSelectionRow);
		for (MouseListener unListener : ComposantVisuelCommun
				.getBtChoixReleve().getMouseListeners()) {
			if (unListener instanceof MlActionMainCombo) {
				((MlActionMainCombo) unListener).refreshPopup();
			}

		}

	}

	/**
	 * @param p_treePath
	 * @param nomDossier
	 */
	public static TreePath createNewDossierAndRefreshTree(JTree p_tree,
			TreePath p_treePath, String nomDossier, int p_idCompte) {
		// Object[] path = new Object[p_treePath.getPathCount()+1];
		TreePath newTp = p_treePath.pathByAddingChild(nomDossier);
		// TreePath newTp = new TreePath(p_treePath.toString().replace("[", "")
		// .replace("]", "")
		// + ", " + nomDossier);
		BDRequette bd = new BDRequette();
		String dossierParent = (String) p_treePath.getLastPathComponent();
		int idDossierParent = bd.getIdDossier(dossierParent, p_idCompte);
		bd.createNewDossier(p_idCompte, idDossierParent, nomDossier, "");
		p_tree.getModel().valueForPathChanged(newTp, ActionTree.AJOUTER);
		// tree.setSelectionPath(newTp);
		ComposantVisuelCommun.setTreePath(newTp);
		ComposantVisuelCommun.setTree(p_tree);
		bd.closeConnexion();
		return newTp;
		// tree.setSelectionPath(newTp.getParentPath());
	}

}
