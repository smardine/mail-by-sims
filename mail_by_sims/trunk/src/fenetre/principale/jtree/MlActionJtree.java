package fenetre.principale.jtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import mdl.MlCompteMail;
import mdl.MlDossier;

public class MlActionJtree implements MouseListener {

	private final JTree tree;

	private JPopupMenu popUpMenu;
	private JMenuItem Ajouter;
	private JMenuItem Supprimer;

	private JMenuItem viderCorbeille;

	public MlActionJtree(JTree p_jTree) {
		this.tree = p_jTree;
		// this.popUpMenu = getJPopupMenu();
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

	private JMenuItem getViderCorbeille() {
		if (viderCorbeille == null) {
			viderCorbeille = new JMenuItem();
			viderCorbeille.setText("Vider la corbeille");
			viderCorbeille.setActionCommand("VIDER");
			viderCorbeille.addActionListener(new MlActionPopupJTree(tree));
		}
		return viderCorbeille;
	}

	@Override
	public void mousePressed(MouseEvent e) {
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
		TreePath pathFromEvent = getPathFromEvent(e);
		if (e.isPopupTrigger() && pathFromEvent != null) {
			popUpMenu = getJPopupMenu();
			int selRow = tree.getRowForLocation(e.getX(), e.getY());

			// tree.setSelectionPath(pathFromEvent);
			tree.setSelectionRow(selRow);

			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) pathFromEvent
					.getLastPathComponent();
			Object userObject = aNode.getUserObject();
			if (userObject instanceof MlCompteMail) {
				Supprimer.setEnabled(false);
			} else if (userObject instanceof MlDossier) {
				MlDossier dossier = (MlDossier) userObject;
				if (dossier.getIdDossierParent() == 0) {
					Supprimer.setEnabled(false);
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) aNode
							.getParent();
					MlCompteMail compteMailParent = (MlCompteMail) parent
							.getUserObject();
					if (compteMailParent.getIdCorbeille() == dossier
							.getIdDossier()) {
						popUpMenu.add(getViderCorbeille());
					}

				} else {
					Supprimer.setEnabled(true);
				}

			}

			popUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}

	}

	/**
	 * @param e
	 * @return
	 */
	private TreePath getPathFromEvent(MouseEvent e) {
		return tree.getPathForLocation(e.getX(), e.getY());
	}

	// private List<String> getListeDossierdeBase() {
	// ArrayList<String> lstDossierBase = new ArrayList<String>(4);
	// EnDossierBase[] lstEnum = EnDossierBase.values();
	// for (EnDossierBase dossier : lstEnum) {
	// if (dossier != EnDossierBase.ROOT) {
	// lstDossierBase.add(dossier.getLib());
	// }
	//
	// }
	// return lstDossierBase;
	// }

}
