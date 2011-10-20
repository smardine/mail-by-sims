package fenetre.principale.jtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import mdl.MlListeMessage;
import bdd.BDRequette;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.jTable.MyTableModel;

public class MlActionJtree implements TreeSelectionListener,
		TreeExpansionListener, MouseListener {

	private final JTree tree;
	private final JTable table;
	private JPopupMenu popUpMenu;
	private JMenuItem Ajouter;
	private JMenuItem Supprimer;

	public MlActionJtree(JTree p_jTree, JTable p_jTable) {
		this.tree = p_jTree;
		this.table = p_jTable;
		this.popUpMenu = getJPopupMenu();
	}

	/**
	 * This method initializes jPopupMenu
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenu() {
		if (popUpMenu == null) {
			popUpMenu = new JPopupMenu();
			popUpMenu.add(getAjouter());
			popUpMenu.add(getSupprimer());

		}
		return popUpMenu;
	}

	/**
	 * This method initializes Ajouter
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAjouter() {
		if (Ajouter == null) {
			Ajouter = new JMenuItem();
			Ajouter.setText("Ajouter un dossier");
			Ajouter.setActionCommand("AJOUTER");
			Ajouter.addActionListener(new MlActionPopupJTree(tree));

		}
		return Ajouter;
	}

	/**
	 * This method initializes Supprimer
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSupprimer() {
		if (Supprimer == null) {
			Supprimer = new JMenuItem();
			Supprimer.setText("Supprimer ce dossier");
			Supprimer.setActionCommand("SUPPRIMER");
			Supprimer.addActionListener(new MlActionPopupJTree(tree));
		}
		return Supprimer;
	}

	@Override
	public void valueChanged(TreeSelectionEvent p_event) {
		TreePath newPath = null;
		if (null != p_event) {
			newPath = p_event.getNewLeadSelectionPath();
		}
		if (null == p_event) {
			newPath = ComposantVisuelCommun.getTreePath();
		}
		if (null != newPath) {
			valoriseTreeEtNomCompte(newPath);
		} else {
			valoriseTreeEtNomCompte(p_event.getOldLeadSelectionPath());
		}

	}

	/**
	 * @param p_event
	 */
	private void valoriseTreeEtNomCompte(TreePath p_path) {
		BDRequette bd = new BDRequette();
		MlListeCompteMail lstCpt = bd.getListeDeComptes();
		bd.closeConnexion();
		if (null != p_path) {

			for (MlCompteMail cpt : lstCpt) {
				if ((cpt.getNomCompte()).contains((CharSequence) p_path
						.getLastPathComponent())) {
					// on est dans le cas [superroot,gmail]
					ComposantVisuelCommun.setNomCompte((String) p_path
							.getLastPathComponent());
				} else {
					String[] tabChaine = p_path.toString().split(",");
					if (tabChaine.length > 1) {
						ComposantVisuelCommun.setNomCompte(tabChaine[1].trim());
					}
				}
				ComposantVisuelCommun.setTreePath(p_path);
				tree.setSelectionPath(p_path);
				ComposantVisuelCommun.setTree(tree);

			}

		}

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent p_event) {

		valoriseTreeEtNomCompte(ComposantVisuelCommun.getTreePath());

	}

	@Override
	public void treeExpanded(TreeExpansionEvent p_event) {

		TreePath newPath = ComposantVisuelCommun.getTreePath();
		if (null != newPath) {
			String nomDossierRacine = newPath.getLastPathComponent().toString();
			BDRequette bd = new BDRequette();
			ArrayList<String> lstSousDossier = bd
					.getListeSousDossier(bd
							.getIdDossier(nomDossierRacine, bd
									.getIdComptes(ComposantVisuelCommun
											.getNomCompte())));
			bd.closeConnexion();
			if (lstSousDossier.size() > 0) {
				TreePath un = new TreePath(newPath.toString().replace("[", "")
						.replace("]", "")
						+ ", " + lstSousDossier.get(0));
				valoriseTreeEtNomCompte(un);
			} else {
				valoriseTreeEtNomCompte(newPath);
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		BDRequette bd = new BDRequette();
		if (!bd.getListeDeComptes().contains(getPathFromEvent(e))) {
			// affiche le contenu de la base correspondant a ce que l'on a
			// cliqué
			if (null != getPathFromEvent(e)) {
				String dossierChoisi = (String) getPathFromEvent(e)
						.getLastPathComponent();

				if (!bd.getListeDeComptes().contains(dossierChoisi)) {
					int idCompte = bd.getIdComptes(ComposantVisuelCommun
							.getNomCompte());
					int idDossierChoisi = bd.getIdDossier(dossierChoisi,
							idCompte);
					MlListeMessage listeMessage = bd.getListeDeMessage(
							idCompte, idDossierChoisi);

					MyTableModel modelDetable = (MyTableModel) table.getModel();
					modelDetable.valorisetable(listeMessage);
					// table.setVisible(true);

				}

			}

		}
		bd.closeConnexion();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int selRow = tree.getRowForLocation(e.getX(), e.getY());
		TreePath selPath = getPathFromEvent(e);

		tree.setSelectionPath(selPath);
		tree.setSelectionRow(selRow);
		if (selRow != -1) {
			if (e.isPopupTrigger()) {

				Object lastPathComponent = selPath.getLastPathComponent();
				BDRequette bd = new BDRequette();
				if (getListeDossierdeBase().contains(lastPathComponent)//
						|| bd.getListeDeComptes().contains(lastPathComponent)) {
					Supprimer.setEnabled(false);
				} else {
					Supprimer.setEnabled(true);
				}
				bd.closeConnexion();

				popUpMenu.show(e.getComponent(), e.getX(), e.getY());

			}

			// if (e.getClickCount() == 1) {
			// System.out.println("mySingleClick(selRow, selPath)");
			// } else if (e.getClickCount() == 2) {
			// System.out.println("myDoubleClick(selRow, selPath)");
			// }
		}

	}

	/**
	 * @param e
	 * @return
	 */
	private TreePath getPathFromEvent(MouseEvent e) {
		TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
		return selPath;
	}

	private ArrayList<String> getListeDossierdeBase() {
		ArrayList<String> lstDossierBase = new ArrayList<String>(4);
		EnDossierBase[] lstEnum = EnDossierBase.values();
		for (EnDossierBase dossier : lstEnum) {
			if (dossier != EnDossierBase.ROOT) {
				lstDossierBase.add(dossier.getLib());
			}

		}
		return lstDossierBase;
	}

}
