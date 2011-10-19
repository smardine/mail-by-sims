package fenetre.principale.jtree;

import java.awt.event.MouseListener;

import javax.swing.JTree;

import mdl.ComposantVisuelCommun;
import fenetre.principale.MlAction.MlActionMainCombo;

public final class utiljTree {

	private utiljTree() {

	}

	public static void reloadJtree(JTree p_tree) {
		p_tree.setModel(new ArborescenceBoiteMail());
		for (MouseListener alistener : ComposantVisuelCommun.getBtChoixReleve()
				.getMouseListeners()) {
			if (alistener instanceof MlActionMainCombo) {
				((MlActionMainCombo) alistener).refreshPopup();
			}

		}
		for (MouseListener aListener : ComposantVisuelCommun
				.getBtChoixSynchro().getMouseListeners()) {
			if (aListener instanceof MlActionMainCombo) {
				((MlActionMainCombo) aListener).refreshPopup();
			}
		}

	}

}
