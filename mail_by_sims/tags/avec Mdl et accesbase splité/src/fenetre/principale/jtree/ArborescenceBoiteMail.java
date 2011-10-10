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

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import bdd.accestable.compte.AccesTableCompte;
import bdd.accestable.dossier.AccesTableDossier;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.Main;

public class ArborescenceBoiteMail implements TreeModel {
	private final String root; // The root identifier

	private final Vector<TreeModelListener> listeners; // Declare the listeners
	// vector
	private final MlListeCompteMail lstCpt;

	public ArborescenceBoiteMail(MlListeCompteMail p_lstCompte) {
		// RequetteFactory bd = new RequetteFactory();
		root = EnDossierBase.ROOT.getLib();
		listeners = new Vector<TreeModelListener>();
		// lstCpt = new MlListeCompteMail();
		lstCpt = p_lstCompte;
		// bd.closeConnexion();
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
		AccesTableDossier accesDossier = new AccesTableDossier();
		if (root.equals(parent)) {
			return lstCpt.get(index).getNomCompte();
		}
		for (MlCompteMail compte : lstCpt) {
			if (compte.getNomCompte().equals(parent)) {
				ArrayList<String> list = accesDossier
						.getListeSousDossierBase(compte.getIdCompte());
				return list.get(index);
			}
		}
		AccesTableCompte accesCompte = new AccesTableCompte();
		int idCompte = accesCompte.getIdComptes(getCompte());
		ArrayList<String> list = accesDossier.getListeSousDossier(accesDossier
				.getIdDossier((String) parent, idCompte));
		return list.get(index);

	}

	public int getChildCount(Object parent) {
		if (parent.equals(root)) {
			return lstCpt.size();
		}
		AccesTableCompte accesCompte = new AccesTableCompte();
		AccesTableDossier accesDossier = new AccesTableDossier();
		for (MlCompteMail compte : lstCpt) {
			if (compte.getNomCompte().equals(parent)) {

				int retour = accesDossier.getnbSousDossierBase(compte
						.getIdCompte());
				return retour;
			}
		}
		int idCompte = accesCompte.getIdComptes(getCompte());
		int retour = accesDossier.getnbSousDossier(accesDossier.getIdDossier(
				(String) parent, idCompte));
		return retour;

	}

	private String getCompte() {
		return Main.getNomCompte();
	}

	public int getIndexOfChild(Object parent, Object child) {
		AccesTableDossier accesDossier = new AccesTableDossier();
		for (MlCompteMail compte : lstCpt) {
			if (compte.getNomCompte().equals(parent)) {
				int idDossier = accesDossier.getIdDossier((String) child,
						compte.getIdCompte());
				if ("".equals(idDossier)) {
					return -1;
				}
				return idDossier;
			}
		}
		return -1;
	}

	public boolean isLeaf(Object node) {
		// String idCompte = RequetteFactory.getIdComptes(getCompte());
		// String idDossier = RequetteFactory.getIdDossier((String) node,
		// idCompte);
		// if (RequetteFactory.getnbMessage(idDossier) > 0) {
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

	/**
	 * @return the lstCpt
	 */
	public MlListeCompteMail getLstCpt() {
		return this.lstCpt;
	}

}
