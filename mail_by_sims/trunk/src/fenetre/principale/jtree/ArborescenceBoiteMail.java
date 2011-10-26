package fenetre.principale.jtree;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
import fenetre.comptes.EnDossierBase;

public class ArborescenceBoiteMail implements TreeModel {
	private final String root; // The root identifier

	private final List<TreeModelListener> listeners; // Declare the listeners
	// vector
	private final MlListeCompteMail lstCpt;

	private final String TAG = this.getClass().getSimpleName();

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
			return lstCpt.get(index).getNomCompte();
		}
		for (MlCompteMail compte : lstCpt) {
			if (compte.getNomCompte().equals(parent)) {
				List<String> list = bd.getListeSousDossierBase(compte
						.getIdCompte());
				bd.closeConnexion();
				return list.get(index);
			}
		}
		int idCompte = bd.getIdComptes(getCompte());
		List<String> list = bd.getListeSousDossier(bd.getIdDossier(
				(String) parent, idCompte));
		bd.closeConnexion();
		return list.get(index);

	}

	public int getChildCount(Object parent) {
		if (parent.equals(root)) {
			return lstCpt.size();
		}

		for (MlCompteMail compte : lstCpt) {
			if (compte.getNomCompte().equals(parent)) {
				BDRequette bd = new BDRequette();
				int retour = bd.getnbSousDossierBase(compte.getIdCompte());
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

		return ComposantVisuelCommun.getNomCompte();

	}

	public int getIndexOfChild(Object parent, Object child) {

		for (MlCompteMail compte : lstCpt) {
			if (compte.getNomCompte().equals(parent)) {
				BDRequette bd = new BDRequette();
				int idDossier = bd.getIdDossier((String) child, compte
						.getIdCompte());
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
		return false;
		// BDRequette bd = new BDRequette();
		// int idCompte = bd.getIdComptes(getCompte());
		// int idDossier = bd.getIdDossier((String) node, idCompte);
		//
		// return bd.getListeSousDossier(idDossier).size() == 0;

		// if (BDRequette.getnbMessage(idDossier) > 0) {
		// return false;
		// }
		// return true;
		// return false;

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

}
