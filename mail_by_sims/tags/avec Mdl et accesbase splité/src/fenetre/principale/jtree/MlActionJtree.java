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

import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeCompteMail;
import mdl.MlListeDossier;
import mdl.MlListeMessage;
import bdd.accestable.compte.AccesTableCompte;
import bdd.accestable.dossier.AccesTableDossier;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.Main;
import fenetre.principale.jTable.MyTableModel;

public class MlActionJtree implements TreeSelectionListener,
		TreeExpansionListener, MouseListener {

	private final JTree tree;
	private final JTable table;
	private JPopupMenu popUpMenu;
	private JMenuItem Ajouter;
	private JMenuItem Supprimer;
	private final MlListeCompteMail listCompteMail;

	public MlActionJtree(MlListeCompteMail p_lstCptMail, JTree p_jTree,
			JTable p_jTable) {
		this.tree = p_jTree;
		this.table = p_jTable;
		this.popUpMenu = getJPopupMenu();
		this.listCompteMail = p_lstCptMail;
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
			newPath = Main.getTreePath();
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
		// RequetteFactory bd = new RequetteFactory();

		MlListeCompteMail lstCpt = MlListeCompteMail.getListeCompte();
		// bd.closeConnexion();
		if (null != p_path) {

			for (MlCompteMail cpt : lstCpt) {
				if ((cpt.getNomCompte()).contains((CharSequence) p_path
						.getLastPathComponent())) {
					// on est dans le cas [superroot,gmail]
					Main.setNomCompte((String) p_path.getLastPathComponent());
				} else {
					String[] tabChaine = p_path.toString().split(",");
					if (tabChaine.length > 1) {
						Main.setNomCompte(tabChaine[1].trim());
					}
				}
				Main.setTreePath(p_path);
				tree.setSelectionPath(p_path);

			}

		}

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent p_event) {

		valoriseTreeEtNomCompte(Main.getTreePath());

	}

	@Override
	public void treeExpanded(TreeExpansionEvent p_event) {

		TreePath newPath = Main.getTreePath();
		if (null != newPath) {
			String nomDossierRacine = newPath.getLastPathComponent().toString();
			AccesTableDossier accesDossier = new AccesTableDossier();
			AccesTableCompte accesCompte = new AccesTableCompte();
			ArrayList<String> lstSousDossier = accesDossier
					.getListeSousDossier(accesDossier.getIdDossier(
							nomDossierRacine, accesCompte.getIdComptes(Main
									.getNomCompte())));
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
		if (e.getClickCount() == 1) {
			TreePath cheminTree = getPathFromEvent(e);
			if (null == getPathFromEvent(e)
					|| getPathFromEvent(e).getPathCount() == 2) {
				return;// on a cliqu� sur le nom d'un compte
			}
			String nomCompte = (String) cheminTree.getPathComponent(1);
			for (MlCompteMail cpt : listCompteMail) {
				if (cpt.getNomCompte().equals(nomCompte)) {
					String dossierChoisi = (String) getPathFromEvent(e)
							.getLastPathComponent();
					MlListeDossier lstDossier = cpt.getLstDossier();
					for (MlDossier d : lstDossier) {
						if (d.getNomEnBase().equals(dossierChoisi)) {
							MlListeMessage listeMessage = d.getLstMessage();
							MyTableModel modelDetable = (MyTableModel) table
									.getModel();
							modelDetable.valorisetable(listeMessage);
							return;
						}
					}

				}
			}
		}

		// if (!bd.getListeDeComptes().contains(getPathFromEvent(e))) {
		// // affiche le contenu de la base correspondant a ce que l'on a
		// // cliqu�
		// if (null != getPathFromEvent(e)) {
		// String dossierChoisi = (String) getPathFromEvent(e)
		// .getLastPathComponent();
		//
		// if (!bd.getListeDeComptes().contains(dossierChoisi)) {
		// int idCompte = bd.getIdComptes(Main.getNomCompte());
		// int idDossierChoisi = bd.getIdDossier(dossierChoisi,
		// idCompte);
		// MlListeMessage listeMessage = bd.getListeDeMessage(
		// idCompte, idDossierChoisi);
		//
		// MyTableModel modelDetable = (MyTableModel) table.getModel();
		// modelDetable.valorisetable(listeMessage);
		// // table.setVisible(true);
		//
		// }
		//
		// }
		//
		// }
		// bd.closeConnexion();
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
				AccesTableCompte accesCompte = new AccesTableCompte();
				if (getListeDossierdeBase().contains(lastPathComponent)//
						|| accesCompte.getListeDeComptes().contains(
								lastPathComponent)) {
					Supprimer.setEnabled(false);
				} else {
					Supprimer.setEnabled(true);
				}
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

	// private String getStringPathFromEvent(MouseEvent p_event) {
	// TreePath selPath = tree.getPathForLocation(p_event.getX(), p_event
	// .getY());
	// return selPath.toString();
	// }

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
