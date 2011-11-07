package fenetre.principale.jtree;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import releve.imap.util.messageUtilisateur;

public class ArborescenceBoiteMail extends DefaultTreeModel implements
		TreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7347327240892103181L;

	private final List<TreeModelListener> listeners; // Declare the listeners
	// vector

	private final String TAG = this.getClass().getSimpleName();

	private final DefaultMutableTreeNode rootNode;

	public ArborescenceBoiteMail(DefaultMutableTreeNode p_rootNode) {
		super(p_rootNode);
		this.rootNode = p_rootNode;
		listeners = new Vector<TreeModelListener>();

		UIManager.put("Tree.openIcon", new ImageIcon(
				"Images/dossier-ouvert-16.png"));
		UIManager.put("Tree.closedIcon", new ImageIcon(
				"Images/dossier-ouvert-16.png"));
		UIManager.put("Tree.leafIcon", new ImageIcon(
				"Images/dossier-ferme-16.png"));
	}

	@Override
	public DefaultMutableTreeNode getRoot() {
		return rootNode;
	}

	@Override
	public TreeNode getChild(Object p_parent, int p_index) {
		return ((DefaultMutableTreeNode) p_parent).getChildAt(p_index);

	}

	@Override
	public int getChildCount(Object p_parent) {
		return ((DefaultMutableTreeNode) p_parent).getChildCount();

	}

	@Override
	public int getIndexOfChild(Object p_parent, Object p_child) {
		DefaultMutableTreeNode unParent = (DefaultMutableTreeNode) p_parent;
		DefaultMutableTreeNode unChild = (DefaultMutableTreeNode) p_child;
		return unParent.getIndex(unChild);

	}

	@Override
	public boolean isLeaf(Object p_node) {
		return ((DefaultMutableTreeNode) p_node).isLeaf();

	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		if (l != null && !listeners.contains(l)) {
			((Vector<TreeModelListener>) listeners).addElement(l);
		}
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		if (l != null) {
			((Vector<TreeModelListener>) listeners).removeElement(l);
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

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
	 * java.lang.Object)
	 */

	@Override
	public void valueForPathChanged(TreePath p_path, Object p_newValue) {
		try {
			if (p_newValue == null) {// rafraichir
				fireTreeStructureChanged(new TreeModelEvent(this, p_path));
			}
		} catch (Exception e) {
			messageUtilisateur.affMessageException(TAG, e,
					"valueForPathChanged");
		}
	}

}
