/**
 * 
 */
package mdl;

import javax.swing.JList;
import javax.swing.tree.TreePath;

/**
 * @author smardine
 */
public final class ComposantVisuelCommun {
	private static JList list;
	private static String nomCompte;
	private static TreePath treepath;

	/**
	 * Constructeur privé pour classe utilitaire
	 */
	private ComposantVisuelCommun() {

	}

	public static void setJList(JList p_list) {
		list = p_list;
	}

	public static JList getJList() {
		return list;
	}

	public static void setNomCompte(String p_nom) {
		nomCompte = p_nom;
	}

	public static String getNomCompte() {
		return nomCompte;
	}

	public static void setTreePath(TreePath p_treePath) {
		treepath = p_treePath;
	}

	public static TreePath getTreePath() {
		return treepath;
	}

}
