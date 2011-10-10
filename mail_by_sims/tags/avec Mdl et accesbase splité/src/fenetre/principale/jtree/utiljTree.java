package fenetre.principale.jtree;

import javax.swing.JTree;

import mdl.MlListeCompteMail;

public final class utiljTree {

	private utiljTree() {

	}

	public static void reloadJtree(JTree p_tree) {
		p_tree.setModel(new ArborescenceBoiteMail(MlListeCompteMail
				.getListeCompte()));

	}

}
