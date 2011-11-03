package fenetre.principale.jtree;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import releve.imap.util.messageUtilisateur;

public class ArborescenceBoiteMail implements TreeModel {

	private final List<TreeModelListener> listeners; // Declare the listeners
	// vector

	private final String TAG = this.getClass().getSimpleName();

	private final DefaultMutableTreeNode rootNode;

	public ArborescenceBoiteMail(DefaultMutableTreeNode p_rootNode) {
		this.rootNode = p_rootNode;
		listeners = new Vector<TreeModelListener>();

		UIManager.put("Tree.openIcon", new ImageIcon(
				"Images/dossier-ouvert-16.png"));
		UIManager.put("Tree.closedIcon", new ImageIcon(
				"Images/dossier-ouvert-16.png"));
		UIManager.put("Tree.leafIcon", new ImageIcon(
				"Images/dossier-ferme-16.png"));
	}

	public DefaultMutableTreeNode getRoot() {
		return rootNode;
	}

	public TreeNode getChild(Object p_parent, int p_index) {
		return ((DefaultMutableTreeNode) p_parent).getChildAt(p_index);

	}

	public int getChildCount(Object p_parent) {
		return ((DefaultMutableTreeNode) p_parent).getChildCount();

	}

	public int getIndexOfChild(Object p_parent, Object p_child) {
		DefaultMutableTreeNode unParent = (DefaultMutableTreeNode) p_parent;
		DefaultMutableTreeNode unChild = (DefaultMutableTreeNode) p_child;
		return unParent.getIndex(unChild);

	}

	public boolean isLeaf(Object p_node) {
		return ((DefaultMutableTreeNode) p_node).isLeaf();

	}

	public void addTreeModelListener(TreeModelListener l) {
		if (l != null && !listeners.contains(l)) {
			((Vector<TreeModelListener>) listeners).addElement(l);
		}
	}

	public void removeTreeModelListener(TreeModelListener l) {
		if (l != null) {
			((Vector<TreeModelListener>) listeners).removeElement(l);
		}
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		try {
			ActionTree action = (ActionTree) newValue;
			switch (action) {
				case AJOUTER:
					fireTreeStructureChanged(new TreeModelEvent(this, path));
					fireTreeNodesInserted(new TreeModelEvent(this, path));
					break;
				case SUPPRIMER:
					// 
					fireTreeNodesRemoved(new TreeModelEvent(this, path));
					fireTreeStructureChanged(new TreeModelEvent(this, path
							.getParentPath()));
					break;
				case RAFFRAICHIR:
					fireTreeStructureChanged(new TreeModelEvent(this, path));
					break;
			}
		} catch (Exception e) {
			messageUtilisateur.affMessageException(TAG, e, "Dans l'action "
					+ (ActionTree) newValue);
		}

	}

	public void fireTreeNodesInserted(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = ((Vector<TreeModelListener>) listeners)
				.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesInserted(e);
		}
	}

	public void fireTreeNodesRemoved(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = ((Vector<TreeModelListener>) listeners)
				.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesRemoved(e);
		}

	}

	public void fireTreeNodesChanged(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = ((Vector<TreeModelListener>) listeners)
				.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesChanged(e);
		}

	}

	public void fireTreeStructureChanged(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = ((Vector<TreeModelListener>) listeners)
				.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeStructureChanged(e);
		}

	}

	/**
	 * @return the rootNode
	 */
	public TreeNode getRootNode() {
		return rootNode;
	}

}
