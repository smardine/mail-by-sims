package fenetre.principale.jtree;

import javax.swing.JTree;

public class utiljTree {

	private utiljTree() {

	}

	public static void reloadJtree(JTree p_tree) {
		p_tree.setModel(new ArborescenceBoiteMail());

	}

}
