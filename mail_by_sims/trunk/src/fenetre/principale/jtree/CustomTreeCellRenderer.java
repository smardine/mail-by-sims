/**
 * 
 */
package fenetre.principale.jtree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.text.Position.Bias;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import mdl.MlCompteMail;
import bdd.BDRequette;
import factory.Fontfactory;
import factory.IconeTreeFactory;
import factory.JTreeFactory;
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
	public Component getTreeCellRendererComponent(JTree tree, Object p_value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, p_value, selected, expanded,
				leaf, row, hasFocus);
		DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) p_value;
		if (!aNode.toString().equals(EnDossierBase.ROOT.getLib())) {

			TreePath treePath;
			if (row != -1) {
				treePath = tree.getPathForRow(row);
			} else {
				treePath = tree.getNextMatch(aNode.toString(), 0, Bias.Forward);
				if (treePath == null) {
					treePath = tree.getNextMatch(aNode.toString(), 0,
							Bias.Backward);
				}
				if (treePath == null) {
					treePath = tree.getSelectionPath();
				}
			}

			if (treePath == null) {
				return this;
			}
			int pathCount = treePath.getPathCount();
			JLabel label = (JLabel) this;
			if (pathCount == 2) {
				traiteNomCompte(aNode, label);
				JTreeFactory treeFact = new JTreeFactory();
				treeFact.refreshNode(treePath);

				return this;

			} else if (pathCount >= 3) {
				traiteNomDossier(aNode, treePath, label, leaf);
				JTreeFactory treeFact = new JTreeFactory();
				treeFact.refreshNode(treePath);
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
					label.setIcon(IconeTreeFactory.getBrouillon());
				} else if (value.toString().equals(
						EnDossierBase.CORBEILLE.getLib())) {
					label.setIcon(IconeTreeFactory.getCorbeille());

				} else if (value.toString().equals(
						EnDossierBase.ENVOYES.getLib())) {
					label.setIcon(IconeTreeFactory.getEnvoye());

				} else if (value.toString().equals(
						EnDossierBase.RECEPTION.getLib())) {
					label.setIcon(IconeTreeFactory.getReception());

				} else if (value.toString().equals(EnDossierBase.SPAM.getLib())) {
					label.setIcon(IconeTreeFactory.getSpam());

				} else {
					if (p_leaf) {
						label.setIcon(IconeTreeFactory.getDossierFerme());
					} else {
						label.setIcon(IconeTreeFactory.getDossierOuvert());
					}

				}

				int unreadMess = bd.getUnreadMessageFromFolder(cpt
						.getIdCompte(), bd.getIdDossier(value.toString(), cpt
						.getIdCompte()));
				if (unreadMess > 0) {
					label.setText(value.toString() + " (" + unreadMess + ")");
					label.setFont(Fontfactory.getTREE_FONT_GRAS());
				} else {
					label.setText(value.toString());
					label.setFont(Fontfactory.getTREE_FONT_PLAIN());
				}
				return;
			}
		}
	}

	/**
	 * @param value
	 * @param label
	 */
	private void traiteNomCompte(DefaultMutableTreeNode value, JLabel label) {
		int unreadMess = bd.getUnreadMessageFromCompte(bd.getIdComptes(value
				.toString()));
		if (unreadMess > 0) {
			label.setText(value.toString() + " (" + unreadMess + ")");
			label.setFont(Fontfactory.getTREE_FONT_GRAS());
		} else {
			label.setText(value.toString());
			label.setFont(Fontfactory.getTREE_FONT_PLAIN());
		}
		label.setIcon(IconeTreeFactory.getDossierOuvert());

	}
}
