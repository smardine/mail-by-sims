package fenetre.principale.jtree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import bdd.BDRequette;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.Main;

public class ArborescenceBoiteMail implements TreeModel {
	private final String root; // The root identifier

	private final Vector<TreeModelListener> listeners; // Declare the listeners
	// vector
	private final ArrayList<String> lstCpt;

	public ArborescenceBoiteMail() {
		BDRequette bd = new BDRequette();
		root = EnDossierBase.ROOT.getLib();
		listeners = new Vector<TreeModelListener>();
		lstCpt = bd.getListeDeComptes();
		bd.closeConnexion();
		UIManager.put("Tree.openIcon", new ImageIcon(
				"Images/dossier-ouvert-16.png"));
		UIManager.put("Tree.closedIcon", new ImageIcon(
				"Images/dossier-ferme-16.png"));
		// UIManager.put("Tree.leafIcon", new ImageIcon(
		// "Images/dossier-ferme-16.png"));
	}

	public Object getRoot() {
		return root;

	}

	public Object getChild(Object parent, int index) {
		BDRequette bd = new BDRequette();
		if (root.equals(parent)) {
			bd.closeConnexion();
			return lstCpt.get(index);
		}
		for (String compte : lstCpt) {
			if (compte.equals(parent)) {
				ArrayList<String> list = bd.getListeSousDossierBase(bd
						.getIdComptes(compte));
				bd.closeConnexion();
				return list.get(index);
			}
		}
		int idCompte = bd.getIdComptes(getCompte());
		ArrayList<String> list = bd.getListeSousDossier(bd.getIdDossier(
				(String) parent, idCompte));
		bd.closeConnexion();
		return list.get(index);

	}

	public int getChildCount(Object parent) {
		if (parent.equals(root)) {
			return lstCpt.size();
		}

		for (String compte : lstCpt) {
			if (compte.equals(parent)) {
				BDRequette bd = new BDRequette();
				int retour = bd.getnbSousDossierBase(bd.getIdComptes(compte));
				bd.closeConnexion();
				return retour;
			}
		}
		BDRequette bd = new BDRequette();
		int idCompte = bd.getIdComptes(getCompte());
		int retour = bd.getnbSousDossier(bd.getIdDossier((String) parent,
				idCompte));
		bd.closeConnexion();
		return retour;

	}

	private String getCompte() {

		return Main.getNomCompte();

	}

	public int getIndexOfChild(Object parent, Object child) {

		for (String compte : lstCpt) {
			if (compte.equals(parent)) {
				BDRequette bd = new BDRequette();
				int idDossier = bd.getIdDossier((String) child, bd
						.getIdComptes(compte));
				bd.closeConnexion();
				if ("".equals(idDossier)) {
					return -1;
				}
				return idDossier;
			}
		}

		return -1;
	}

	public boolean isLeaf(Object node) {
		// String idCompte = BDRequette.getIdComptes(getCompte());
		// String idDossier = BDRequette.getIdDossier((String) node, idCompte);
		// if (BDRequette.getnbMessage(idDossier) > 0) {
		// return false;
		// }
		// return true;
		return false;

	}

	public void addTreeModelListener(TreeModelListener l) {
		if (l != null && !listeners.contains(l)) {
			listeners.addElement(l);
		}
	}

	public void removeTreeModelListener(TreeModelListener l) {
		if (l != null) {
			listeners.removeElement(l);
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
			System.out.println(e.getMessage());
		}

	}

	public void fireTreeNodesInserted(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesInserted(e);
		}
	}

	public void fireTreeNodesRemoved(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesRemoved(e);
		}

	}

	public void fireTreeNodesChanged(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesChanged(e);
		}

	}

	public void fireTreeStructureChanged(TreeModelEvent e) {
		Enumeration<TreeModelListener> listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements()) {
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeStructureChanged(e);
		}

	}

}
