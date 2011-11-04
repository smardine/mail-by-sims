/**
 * 
 */
package fenetre.principale.jtree;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import mdl.MlCompteMail;
import bdd.BDRequette;
import fenetre.comptes.EnDossierBase;

/**
 * @author smardine
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3666054799792985018L;
	private final BDRequette bd;

	public CustomTreeCellRenderer() {
		bd = new BDRequette();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);
		if (!value.toString().equals(EnDossierBase.ROOT.getLib())) {

			TreePath treePath = tree.getPathForRow(row);
			if (treePath == null) {
				return this;
			}
			int pathCount = treePath.getPathCount();
			JLabel label = (JLabel) this;
			if (pathCount == 2) {
				traiteNomCompte(value, label);
				return this;

			} else if (pathCount >= 3) {
				traiteNomDossier(value, treePath, label, leaf);
				return this;
			}
		}

		return this;
	}

	/**
	 * @param value
	 * @param treePath
	 * @param label
	 * @param p_leaf
	 */
	private void traiteNomDossier(Object value, TreePath treePath,
			JLabel label, boolean p_leaf) {
		Object[] pathComplet = treePath.getPath();
		for (MlCompteMail cpt : bd.getListeDeComptes()) {
			if (pathComplet[1].toString().equals(cpt.getNomCompte())) {
				if (value.toString().equals(EnDossierBase.BROUILLON.getLib())) {
					label.setIcon(new ImageIcon("Images/brouillon_16.png"));
				} else if (value.toString().equals(
						EnDossierBase.CORBEILLE.getLib())) {
					label.setIcon(new ImageIcon("Images/supprimer_16.png"));

				} else if (value.toString().equals(
						EnDossierBase.ENVOYES.getLib())) {
					label.setIcon(new ImageIcon("Images/envoyer_16.png"));

				} else if (value.toString().equals(
						EnDossierBase.RECEPTION.getLib())) {
					label.setIcon(new ImageIcon("Images/recevoir_16.png"));

				} else if (value.toString().equals(EnDossierBase.SPAM.getLib())) {
					label.setIcon(new ImageIcon("Images/spam_16.png"));

				} else {
					if (p_leaf) {
						label.setIcon(new ImageIcon(
								"Images/dossier-ferme-16.png"));
					} else {
						label.setIcon(new ImageIcon(
								"Images/dossier-ouvert-16.png"));
					}

				}

				int unreadMess = bd.getUnreadMessageFromFolder(cpt
						.getIdCompte(), bd.getIdDossier(value.toString(), cpt
						.getIdCompte()));
				if (unreadMess > 0) {
					label.setText(value.toString() + " (" + unreadMess + ")");
					label.setFont(new Font("Perpetua", Font.PLAIN, 12));
				} else {
					label.setText(value.toString());
					label.setFont(new Font("Perpetua", Font.PLAIN, 12));
				}
				return;
			}
		}
	}

	/**
	 * @param value
	 * @param label
	 */
	private void traiteNomCompte(Object value, JLabel label) {
		int unreadMess = bd.getUnreadMessageFromCompte(bd.getIdComptes(value
				.toString()));
		if (unreadMess > 0) {
			label.setText(value.toString() + " (" + unreadMess + ")");
			label.setFont(new Font("Perpetua", Font.BOLD, 12));
		} else {
			label.setText(value.toString());
			label.setFont(new Font("Perpetua", Font.PLAIN, 12));
		}
		label.setIcon(new ImageIcon("Images/dossier-ouvert-16.png"));

	}
}
