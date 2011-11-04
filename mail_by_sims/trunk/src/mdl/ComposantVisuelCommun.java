/**
 * 
 */
package mdl;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;

/**
 * @author smardine
 */
public final class ComposantVisuelCommun {
	private static JList jlistPJ;
	private static JList listCompteMail;
	private static JButton btChoixReleve;
	private static JTree tree;
	private static JEditorPane htmlPane;
	private static JTable jTable;

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

	/**
	 * @param p_htmlPane
	 */
	public static void setHTMLPane(JEditorPane p_htmlPane) {
		htmlPane = p_htmlPane;
	}

	/**
	 * @return the htmlPane
	 */
	public static JEditorPane getHtmlPane() {
		return htmlPane;
	}

	/**
	 * @return the jTable
	 */
	public static JTable getJTable() {
		return jTable;
	}

	/**
	 * @param p_jTable the jTable to set
	 */
	public static void setjTable(JTable p_jTable) {
		jTable = p_jTable;
	}

}
