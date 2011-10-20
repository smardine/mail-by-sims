/**
 * 
 */
package mdl;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * @author smardine
 */
public final class ComposantVisuelCommun {
	private static JList jlistPJ;
	private static String nomCompte;
	private static TreePath treepath;
	private static JList listCompteMail;
	private static JButton btChoixSynchro;
	private static JButton btChoixReleve;
	private static JTree tree;

	/**
	 * Constructeur privé pour classe utilitaire
	 */
	private ComposantVisuelCommun() {

	}

	public static void setJListPJ(JList p_list) {
		jlistPJ = p_list;
	}

	public static JList getJListPJ() {
		return jlistPJ;
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

	/**
	 * @param p_jList
	 */
	public static void setJListCompteMail(JList p_jList) {
		listCompteMail = p_jList;
	}

	public static JList getJListCompteMail() {
		return listCompteMail;
	}

	/**
	 * @param p_btChoixSynchro
	 */
	public static void setbtChoixSynchro(JButton p_btChoixSynchro) {
		btChoixSynchro = p_btChoixSynchro;
	}

	public static JButton getBtChoixSynchro() {
		return btChoixSynchro;
	}

	/**
	 * @param p_btChoixReleve
	 */
	public static void setbtChoixReleve(JButton p_btChoixReleve) {
		btChoixReleve = p_btChoixReleve;
	}

	public static JButton getBtChoixReleve() {
		return btChoixReleve;
	}

	/**
	 * @param p_jTree
	 */
	public static void setTree(JTree p_jTree) {
		tree = p_jTree;
	}

	public static JTree getJTree() {
		return tree;
	}

}
