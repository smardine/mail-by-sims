package fenetre.principale.jtree;

import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import fenetre.principale.MlAction.MlActionMainCombo;

public final class utiljTree {

	private utiljTree() {

	}

	public static void reloadJtree(JTree p_tree) {
		TreePath originalTreePath = p_tree.getSelectionPath();
		int[] originalSelectionRow = p_tree.getSelectionRows();
		p_tree.setModel(new ArborescenceBoiteMail());
		p_tree.setSelectionPath(originalTreePath);
		p_tree.setSelectionRows(originalSelectionRow);
		for (MouseListener unListener : ComposantVisuelCommun.getBtChoixReleve()
				.getMouseListeners()) {
			if (unListener instanceof MlActionMainCombo) {
				((MlActionMainCombo) unListener).refreshPopup();
			}

		}

	}

}
