/**
 * 
 */
package factory;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import mdl.MlCompteMail;
import bdd.BDRequette;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.jtree.ArborescenceBoiteMail;

/**
 * @author smardine
 */
public class JTreeFactory {

	private DefaultMutableTreeNode treeNode;
	private final BDRequette bd;

	public JTreeFactory() {
		bd = new BDRequette();
	}

	public DefaultMutableTreeNode getTreeNode() {
		if (treeNode == null) {
			treeNode = new DefaultMutableTreeNode(EnDossierBase.ROOT.getLib());
			for (MlCompteMail cpt : bd.getListeDeComptes()) {
				DefaultMutableTreeNode compteNode = new DefaultMutableTreeNode(
						cpt.getNomCompte());

				for (String nomDossier : bd.getListeSousDossierBase(cpt
						.getIdCompte())) {
					DefaultMutableTreeNode dossierBaseNode = new DefaultMutableTreeNode(
							nomDossier);
					// pour chacun des dossier de base, on prend la liste des
					// sous dossier
					dossierBaseNode = recupereSousDossier(dossierBaseNode,
							nomDossier, cpt);
					compteNode.add(dossierBaseNode);

				}
				treeNode.add(compteNode);
			}

		}
		return treeNode;
	}

	/**
	 * @param p_dossierBaseNode
	 */
	private DefaultMutableTreeNode recupereSousDossier(
			DefaultMutableTreeNode p_dossierBaseNode, String p_nomDossier,
			MlCompteMail p_cptMail) {
		for (String nomSousDossier : bd.getListeSousDossier(bd.getIdDossier(
				p_nomDossier, p_cptMail.getIdCompte()))) {
			DefaultMutableTreeNode sousDossierTreeNode = new DefaultMutableTreeNode(
					nomSousDossier);
			p_dossierBaseNode.add(sousDossierTreeNode);
			sousDossierTreeNode = recupereSousDossier(sousDossierTreeNode,
					nomSousDossier, p_cptMail);
		}
		return p_dossierBaseNode;
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) ((ArborescenceBoiteMail) tree.getModel())
				.getRootNode();

		// Traverse tree from root
		expandAll(tree, new TreePath(root), expand);
	}

	/**
	 * @return Whether an expandPath was called for the last node in the parent
	 *         path
	 */
	private boolean expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() > 0) {
			boolean childExpandCalled = false;
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				childExpandCalled = expandAll(tree, path, expand)
						|| childExpandCalled; // the OR order is important here,
				// don't let childExpand first.
				// func calls will be optimized
				// out !
			}

			if (!childExpandCalled) { // only if one of the children hasn't
				// called already expand
				// Expansion or collapse must be done bottom-up, BUT only for
				// non-leaf nodes
				if (expand) {
					tree.expandPath(parent);
				} else {
					tree.collapsePath(parent);
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
