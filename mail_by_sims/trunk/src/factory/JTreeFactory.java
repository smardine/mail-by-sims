/**
 * 
 */
package factory;

import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlListeMessage;
import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
import exception.DonneeAbsenteException;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.MlAction.MlActionMainCombo;
import fenetre.principale.jtree.ArborescenceBoiteMail;

/**
 * @author smardine
 */
public class JTreeFactory {

	private DefaultMutableTreeNode treeNode;
	private final BDRequette bd;
	private final JTree tree;
	private final String TAG = this.getClass().getSimpleName();

	public JTreeFactory() {
		this.tree = ComposantVisuelCommun.getJTree();
		this.bd = new BDRequette();
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

	public DefaultMutableTreeNode refreshJTree() {
		if (tree == null) {
			try {
				throw new DonneeAbsenteException(TAG,
						"la variable tree doit etre differente de NULL");
			} catch (DonneeAbsenteException e) {
				messageUtilisateur.affMessageException(TAG, e, "Erreur UI");
				return null;
			}
		}
		final TreePath treePath = tree.getSelectionPath();
		if (treePath != null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					((ArborescenceBoiteMail) tree.getModel())
							.fireTreeStructureChanged(new TreeModelEvent(this,
									treePath));
				}
			}).start();

			return (DefaultMutableTreeNode) treePath.getLastPathComponent();
		}
		return null;

	}

	public void refreshJTreeAndJTable() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DefaultMutableTreeNode dossierChoisi = refreshJTree();

				if (!bd.getListeDeComptes().contains(dossierChoisi.toString())) {
					Object[] pathComplet = dossierChoisi.getPath();
					int idCompte = bd.getIdComptes(pathComplet[1].toString());
					int idDossierChoisi = bd.getIdDossier(dossierChoisi
							.toString(), idCompte);
					MlListeMessage listeMessage = bd.getListeDeMessage(
							idCompte, idDossierChoisi);
					JTableFactory tableFact = new JTableFactory();
					tableFact.refreshJTable(listeMessage);
				}
			}
		}).start();

	}

	public void reloadJtree() {
		TreePath originalTreePath = tree.getSelectionPath();
		int[] originalSelectionRow = tree.getSelectionRows();
		JTreeFactory treeFact = new JTreeFactory();

		tree.setModel(new ArborescenceBoiteMail(treeFact.getTreeNode()));
		tree.setSelectionPath(originalTreePath);
		tree.setSelectionRows(originalSelectionRow);
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
	public TreePath createNewDossierAndRefreshTree(TreePath p_treePath,
			String nomDossier, int p_idCompte) {

		TreePath newTp = p_treePath.pathByAddingChild(nomDossier);
		String dossierParent = (String) p_treePath.getLastPathComponent();
		int idDossierParent = bd.getIdDossier(dossierParent, p_idCompte);
		bd.createNewDossier(p_idCompte, idDossierParent, nomDossier, "");
		ajouteNode(p_treePath, new DefaultMutableTreeNode(nomDossier));
		// tree.getModel()
		// .valueForPathChanged(newTp, newTp.getLastPathComponent());

		return newTp;

	}

	public void refreshNode(TreePath p_path) {
		((ArborescenceBoiteMail) tree.getModel()).reload((TreeNode) p_path
				.getLastPathComponent());

		// ((ArborescenceBoiteMail) tree.getModel())
		// .fireTreeStructureChanged(new TreeModelEvent(this, p_path));
	}

	public boolean ajouteNode(TreePath p_path, TreeNode p_newValue) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) p_path
				.getLastPathComponent();

		node.add((DefaultMutableTreeNode) p_newValue);
		int childCount = node.getChildCount();
		int[] tabIdx = new int[childCount];
		Object[] tabChild = new Object[childCount];
		for (int i = 0; i < childCount; i++) {
			tabChild[i] = node.getChildAt(i);
			tabIdx[i] = node.getIndex((TreeNode) tabChild[i]);
		}
		((ArborescenceBoiteMail) tree.getModel())
				.fireTreeStructureChanged(new TreeModelEvent(this, p_path,
						tabIdx, tabChild));
		return true;
	}

	public boolean supprimeNode(TreePath p_path, TreeNode p_newValue) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) p_path
				.getLastPathComponent();
		node = (DefaultMutableTreeNode) node.getParent();
		node.remove((DefaultMutableTreeNode) p_newValue);

		int childCount = node.getChildCount();
		int[] tabIdx = new int[childCount];
		Object[] tabChild = new Object[childCount];
		for (int i = 0; i < childCount; i++) {
			tabChild[i] = node.getChildAt(i);
			tabIdx[i] = node.getIndex((TreeNode) tabChild[i]);
		}

		((ArborescenceBoiteMail) tree.getModel())
				.fireTreeStructureChanged(new TreeModelEvent(this, p_path
						.getParentPath(), tabIdx, tabChild));
		return true;
	}

}
