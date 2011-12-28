package fenetre.principale.jtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import mdl.mlcomptemail.MlCompteMail;
import mdl.mldossier.MlBrouillon;
import mdl.mldossier.MlCorbeille;
import mdl.mldossier.MlDossier;
import mdl.mldossier.MlEnvoye;
import mdl.mldossier.MlReception;
import mdl.mldossier.MlSpam;
import bdd.accesTable.AccesTableMailRecu;

public class CustomTreeClickListener implements MouseListener {

	private final JTree tree;

	private JPopupMenu popUpMenu;
	private JMenuItem Ajouter;
	private JMenuItem Supprimer;

	private JMenuItem viderCorbeille;

	public CustomTreeClickListener(JTree p_jTree) {
		this.tree = p_jTree;

	}

	/**
	 * This method initializes jPopupMenu
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenu() {
		// if (popUpMenu == null) {
		popUpMenu = new JPopupMenu();
		popUpMenu.add(getAjouter());
		popUpMenu.add(getSupprimer());

		// }
		return popUpMenu;
	}

	/**
	 * This method initializes Ajouter
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAjouter() {
		// if (Ajouter == null) {
		Ajouter = new JMenuItem();
		Ajouter.setText("Ajouter un dossier");
		Ajouter.setActionCommand("AJOUTER");
		Ajouter.addActionListener(new MlActionPopupJTree(tree));

		// }
		return Ajouter;
	}

	/**
	 * This method initializes Supprimer
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSupprimer() {
		// if (Supprimer == null) {
		Supprimer = new JMenuItem();
		Supprimer.setText("Supprimer ce dossier");
		Supprimer.setActionCommand("SUPPRIMER");
		Supprimer.addActionListener(new MlActionPopupJTree(tree));
		// }
		return Supprimer;
	}

	private JMenuItem getViderCorbeille() {
		// if (viderCorbeille == null) {
		viderCorbeille = new JMenuItem();
		viderCorbeille.setText("Vider la corbeille");
		viderCorbeille.setActionCommand("VIDER");
		viderCorbeille.addActionListener(new MlActionPopupJTree(tree));
		// }
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

			tree.setSelectionRow(selRow);

			construitPopUpMenu(pathFromEvent);
			popUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}

	}

	/**
	 * @param p_pathFromEvent
	 */
	private void construitPopUpMenu(TreePath p_pathFromEvent) {
		DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) p_pathFromEvent
				.getLastPathComponent();
		Object userObject = aNode.getUserObject();
		if (userObject instanceof MlCompteMail) {
			popUpMenu.setVisible(false);
			Ajouter.setVisible(false);
			Supprimer.setVisible(false);

		} else if (userObject instanceof MlDossier) {
			if (userObject instanceof MlReception
					|| userObject instanceof MlEnvoye
					|| userObject instanceof MlSpam
					|| userObject instanceof MlBrouillon) {
				Supprimer.setVisible(false);
			} else if (userObject instanceof MlCorbeille) {
				Supprimer.setVisible(false);
				Ajouter.setVisible(false);
				popUpMenu.add(getViderCorbeille());
				if (new AccesTableMailRecu().getListeMessageGrille(
						((MlCorbeille) userObject).getIdCompte(),
						((MlCorbeille) userObject).getIdDossier()).size() == 0) {
					viderCorbeille.setEnabled(false);
				} else {
					viderCorbeille.setEnabled(true);
				}
			} else {
				Supprimer.setEnabled(true);
				Supprimer.setVisible(true);
				Ajouter.setVisible(true);

			}
		}
	}

	/**
	 * @param e
	 * @return
	 */
	private TreePath getPathFromEvent(MouseEvent e) {
		return tree.getPathForLocation(e.getX(), e.getY());
	}

}
