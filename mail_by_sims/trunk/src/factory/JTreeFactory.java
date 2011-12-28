/**
 * 
 */
package factory;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.mlcomptemail.MlCompteMail;
import mdl.mlcomptemail.MlListeCompteMail;
import mdl.mldossier.MlDossier;
import mdl.mlmessage.MlMessageGrille;
import releve.imap.util.messageUtilisateur;
import thread.threadMajUnreadCount;
import bdd.accesTable.AccesTableCompte;
import bdd.accesTable.AccesTableDossier;
import exception.DonneeAbsenteException;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.Main;
import fenetre.principale.MlAction.MlActionMainCombo;
import fenetre.principale.jtree.ArborescenceBoiteMail;

/**
 * @author smardine
 */
public class JTreeFactory {

	private static DefaultMutableTreeNode treeNode;
	private final AccesTableCompte accesCompte;
	private final JTree tree;
	private final String TAG = this.getClass().getSimpleName();
	private final AccesTableDossier accesDossier;

	public JTreeFactory() {
		this.tree = ComposantVisuelCommun.getJTree();
		this.accesCompte = new AccesTableCompte();
		this.accesDossier = new AccesTableDossier();
	}

	public DefaultMutableTreeNode getTreeNode(Main p_fenetre,
			JProgressBar p_progressbar) {
		if (treeNode == null) {
			treeNode = new DefaultMutableTreeNode(EnDossierBase.ROOT.getLib());
			MlListeCompteMail lstCpt = accesCompte.getListeDeComptes();
			int cptCount = 1;
			for (MlCompteMail cpt : lstCpt) {
				if (p_progressbar != null) {
					int progress = (100 * cptCount) / lstCpt.size();
					p_progressbar.setValue(progress);
					p_progressbar.setString(progress + " %");
				}
				treeNode = ajouteCompteNode(cpt);

				cptCount++;
			}

		}
		return treeNode;
	}

	public TreePath getTreePathFromTreeNode(TreeNode p_node) {
		TreeNode aNode = p_node;
		List<TreeNode> lst = new ArrayList<TreeNode>();
		// Add all nodes to list
		while (aNode != null) {
			lst.add(aNode);
			aNode = aNode.getParent();
		}
		Collections.reverse(lst);

		// Convert array of nodes to TreePath
		return new TreePath(lst.toArray());

	}

	public DefaultMutableTreeNode ajouteCompteNode(MlCompteMail p_cptMail) {

		DefaultMutableTreeNode compteNode = new DefaultMutableTreeNode(
				p_cptMail);
		for (MlDossier unDossier : p_cptMail.getListDossierPrincipaux()) {
			DefaultMutableTreeNode dossierBaseNode = new DefaultMutableTreeNode(
					unDossier);
			// pour chacun des dossier de base, on prend la liste des
			// sous dossier
			dossierBaseNode = recupereSousDossier(dossierBaseNode, unDossier,
					p_cptMail);
			compteNode.add(dossierBaseNode);
		}
		treeNode.add(compteNode);

		return treeNode;

	}

	public DefaultMutableTreeNode supprimeCompteNode(MlCompteMail p_cptMail) {
		DefaultMutableTreeNode compteNode = rechercheCompteNode(p_cptMail
				.getIdCompte());
		// int nbDossier = compteNode.getChildCount();
		while (compteNode.getChildCount() > 0) {
			compteNode.remove(0);
		}
		// for (int i = 0; i < nbDossier; i++) {
		// compteNode.remove(i);
		// }
		treeNode.remove(compteNode);
		TreePath treePath = ComposantVisuelCommun.getJTree().getPathForRow(0);
		((ArborescenceBoiteMail) tree.getModel())

		.fireTreeNodesRemoved(new TreeModelEvent(this, treePath));
		// return (DefaultMutableTreeNode) treePath.getLastPathComponent();

		return treeNode;

	}

	/**
	 * @param p_dossierBaseNode
	 */
	private DefaultMutableTreeNode recupereSousDossier(
			DefaultMutableTreeNode p_dossierBaseNode, MlDossier p_sousDossier,
			MlCompteMail p_cptMail) {
		for (MlDossier nomSousDossier : p_sousDossier.getListSousDossier()) {
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
			((ArborescenceBoiteMail) tree.getModel())
					.fireTreeStructureChanged(new TreeModelEvent(this, treePath));
			return (DefaultMutableTreeNode) treePath.getLastPathComponent();
		}
		return null;

	}

	public void refreshJTreeAndJTable() {
		DefaultMutableTreeNode aNode = refreshJTree();
		Object userObject = aNode.getUserObject();
		if (userObject instanceof MlDossier) {
			JTableFactory tableFact = new JTableFactory();
			tableFact.refreshJTable(((MlDossier) userObject)
					.getListMessageGrille());
		}
	}

	public void reloadJtree() {
		TreePath originalTreePath = tree.getSelectionPath();
		int[] originalSelectionRow = tree.getSelectionRows();

		tree.setModel(new ArborescenceBoiteMail(this.getTreeNode(null, null)));
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
		int idDossierParent = accesDossier.getIdDossier(dossierParent,
				p_idCompte);
		accesDossier.createNewDossier(p_idCompte, idDossierParent, nomDossier,
				"");
		MlDossier aDossier = new MlDossier(accesDossier.getIdDossier(
				nomDossier, p_idCompte));
		ajouteNode(p_treePath, (DefaultMutableTreeNode) p_treePath
				.getLastPathComponent(), new DefaultMutableTreeNode(aDossier));
		return newTp;

	}

	public boolean ajouteNode(TreePath p_path,
			DefaultMutableTreeNode p_nodeParent,
			DefaultMutableTreeNode p_newNode) {

		p_nodeParent.add(p_newNode);
		int childCount = p_nodeParent.getChildCount();
		int[] tabIdx = new int[childCount];
		Object[] tabChild = new Object[childCount];
		for (int i = 0; i < childCount; i++) {
			tabChild[i] = p_nodeParent.getChildAt(i);
			tabIdx[i] = p_nodeParent.getIndex((TreeNode) tabChild[i]);
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

	public MlCompteMail rechercheCompteMail(int p_idCompte) {
		TreePath selPath = ComposantVisuelCommun.getJTree().getSelectionPath();
		if (selPath == null) {
			selPath = ComposantVisuelCommun.getJTree().getPathForRow(0);
		}
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) selPath
				.getPathComponent(0);
		MlCompteMail cptCandidat = new MlCompteMail(p_idCompte);
		int nbChildCount = rootNode.getChildCount();
		for (int i = 0; i < nbChildCount; i++) {
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) rootNode
					.getChildAt(i);
			MlCompteMail userObject = (MlCompteMail) aNode.getUserObject();
			if (userObject.equals(cptCandidat)) {
				return userObject;
			}
		}
		try {
			throw new DonneeAbsenteException(TAG,
					"Impossible de trouver le compteMail correspondant");
		} catch (DonneeAbsenteException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur de programmation");
			return null;
		}
		// return (MlCompteMail) compteNode.getUserObject();
	}

	public DefaultMutableTreeNode rechercheCompteNode(int p_idCompte) {
		TreePath selPath = ComposantVisuelCommun.getJTree().getSelectionPath();
		if (selPath == null) {
			selPath = ComposantVisuelCommun.getJTree().getPathForRow(0);
		}
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) selPath
				.getPathComponent(0);
		MlCompteMail cptCandidat = new MlCompteMail(p_idCompte);
		int nbChildCount = rootNode.getChildCount();
		for (int i = 0; i < nbChildCount; i++) {
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) rootNode
					.getChildAt(i);
			MlCompteMail userObject = (MlCompteMail) aNode.getUserObject();
			if (userObject.equals(cptCandidat)) {
				return aNode;
			}
		}
		try {
			throw new DonneeAbsenteException(TAG,
					"Impossible de trouver le compteMail correspondant");
		} catch (DonneeAbsenteException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur de programmation");
			return null;
		}
	}

	public MlDossier rechercheDossier(int p_idDossier, int p_idCompte) {
		// TreePath selPath =
		// ComposantVisuelCommun.getJTree().getSelectionPath();
		DefaultMutableTreeNode compteNode;
		// if (selPath == null) {
		compteNode = rechercheCompteNode(p_idCompte);
		// } else {
		// compteNode = (DefaultMutableTreeNode) selPath.getPathComponent(1);
		// }

		MlDossier dossierCandidat = new MlDossier();
		dossierCandidat.setIdDossier(p_idDossier);
		int nbcandidat = compteNode.getChildCount();
		for (int i = 0; i < nbcandidat; i++) {
			DefaultMutableTreeNode aChild = (DefaultMutableTreeNode) compteNode
					.getChildAt(i);
			MlDossier userObject = (MlDossier) aChild.getUserObject();
			if (dossierCandidat.equals(userObject)) {
				return userObject;
			}
			MlDossier dossierPotentiel = parcoursSousDossier(aChild,
					dossierCandidat);
			if (dossierPotentiel != null) {
				return dossierPotentiel;
			}
		}
		return null;

	}

	public DefaultMutableTreeNode rechercheDossierNode(int p_idDossier,
			int p_idCompte) {
		// TreePath selPath =
		// ComposantVisuelCommun.getJTree().getSelectionPath();
		DefaultMutableTreeNode compteNode;
		// if (selPath == null) {
		compteNode = rechercheCompteNode(p_idCompte);
		// } else {
		// compteNode = (DefaultMutableTreeNode) selPath.getPathComponent(1);
		// }

		MlDossier dossierCandidat = new MlDossier();
		dossierCandidat.setIdDossier(p_idDossier);
		int nbcandidat = compteNode.getChildCount();
		for (int i = 0; i < nbcandidat; i++) {
			DefaultMutableTreeNode aChildNode = (DefaultMutableTreeNode) compteNode
					.getChildAt(i);
			MlDossier userObject = (MlDossier) aChildNode.getUserObject();
			if (dossierCandidat.equals(userObject)) {
				return aChildNode;
			}
			DefaultMutableTreeNode nodePotentiel = parcoursSousDossierNode(
					aChildNode, dossierCandidat);
			if (nodePotentiel != null) {
				return nodePotentiel;
			}
		}
		return null;

	}

	/**
	 * @param p_aNode
	 * @param p_dossierCandidat
	 * @return
	 */
	private DefaultMutableTreeNode parcoursSousDossierNode(
			DefaultMutableTreeNode p_aNode, MlDossier p_dossierCandidat) {
		int nbcandidat = p_aNode.getChildCount();
		for (int i = 0; i < nbcandidat; i++) {
			DefaultMutableTreeNode aChildNode = (DefaultMutableTreeNode) p_aNode
					.getChildAt(i);
			MlDossier userObject = (MlDossier) aChildNode.getUserObject();
			if (p_dossierCandidat.equals(userObject)) {
				return aChildNode;
			}
			if (aChildNode.getChildCount() > 0) {
				DefaultMutableTreeNode nodePotentiel = parcoursSousDossierNode(
						aChildNode, p_dossierCandidat);
				if (nodePotentiel != null) {
					return nodePotentiel;
				}
			}

		}
		return null;
	}

	/**
	 * @param p_aNode
	 * @param p_dossierCandidat
	 * @return
	 */
	private MlDossier parcoursSousDossier(DefaultMutableTreeNode p_aNode,
			MlDossier p_dossierCandidat) {
		int nbcandidat = p_aNode.getChildCount();
		for (int i = 0; i < nbcandidat; i++) {
			DefaultMutableTreeNode aChild = (DefaultMutableTreeNode) p_aNode
					.getChildAt(i);
			MlDossier userObject = (MlDossier) aChild.getUserObject();
			if (p_dossierCandidat.equals(userObject)) {
				return userObject;
			}
			if (aChild.getChildCount() > 0) {
				MlDossier dossierPotentiel = parcoursSousDossier(aChild,
						p_dossierCandidat);
				if (dossierPotentiel != null) {
					return dossierPotentiel;
				}
			}

		}
		return null;
	}

	/**
	 * Mise a jour du compteur de message non lu pour l'afficher a l'utilisateur
	 * @param p_messageGrille le MlMessage que l'on vient de créer ou de mette a
	 *            jour (statut lecture)
	 */
	public void majUnreadCount(final MlMessageGrille p_messageGrille) {
		new threadMajUnreadCount(p_messageGrille, accesCompte).start();

	}
}
