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
import mdl.MlDossier;
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

		TreePath treePath;
		if (row != -1) {
			treePath = tree.getPathForRow(row);
		} else {
			treePath = tree.getNextMatch(aNode.toString(), 0, Bias.Forward);
			if (treePath == null) {
				treePath = tree
						.getNextMatch(aNode.toString(), 0, Bias.Backward);
			}
			if (treePath == null) {
				treePath = tree.getSelectionPath();
			}
		}

		if (treePath == null) {
			return this;
		}
		// int pathCount = treePath.getPathCount();
		JLabel label = (JLabel) this;
		if (aNode.getUserObject() instanceof MlCompteMail) {
			traiteNomCompte((MlCompteMail) aNode.getUserObject(), label);
			JTreeFactory treeFact = new JTreeFactory();
			treeFact.refreshNode(treePath);

			return this;
		} else if (aNode.getUserObject() instanceof MlDossier) {
			traiteNomDossier((MlDossier) aNode.getUserObject(), label, leaf);
			JTreeFactory treeFact = new JTreeFactory();
			treeFact.refreshNode(treePath);
			return this;
		}

		return this;
	}

	/**
	 * @param value
	 * @param treePath
	 * @param label
	 * @param p_leaf
	 */
	private void traiteNomDossier(MlDossier p_dossier, JLabel label,
			boolean p_leaf) {
		// Object[] pathComplet = treePath.getPath();
		// for (MlCompteMail cpt : bd.getListeDeComptes()) {
		// if (pathComplet[1].toString().equals(cpt.getNomCompte())) {
		// MlCompteMail cptParent = new MlCompteMail(p_dossier.getIdCompte());
		String nomDossier = p_dossier.getNomDossier();
		if (nomDossier.equals(EnDossierBase.BROUILLON.getLib())) {
			label.setIcon(IconeTreeFactory.getBrouillon());
		} else if (nomDossier.equals(EnDossierBase.CORBEILLE.getLib())) {
			label.setIcon(IconeTreeFactory.getCorbeille());

		} else if (nomDossier.equals(EnDossierBase.ENVOYES.getLib())) {
			label.setIcon(IconeTreeFactory.getEnvoye());

		} else if (nomDossier.equals(EnDossierBase.RECEPTION.getLib())) {
			label.setIcon(IconeTreeFactory.getReception());

		} else if (nomDossier.equals(EnDossierBase.SPAM.getLib())) {
			label.setIcon(IconeTreeFactory.getSpam());

		} else if (p_leaf) {
			label.setIcon(IconeTreeFactory.getDossierFerme());
		} else {
			label.setIcon(IconeTreeFactory.getDossierOuvert());
		}

		int unreadMess = p_dossier.getUnreadMessCount();
		if (unreadMess > 0) {
			label.setText(nomDossier + " (" + unreadMess + ")");
			label.setFont(Fontfactory.getTREE_FONT_GRAS());
		} else {
			label.setText(nomDossier);
			label.setFont(Fontfactory.getTREE_FONT_PLAIN());
		}
		return;
	}

	// }
	// }

	/**
	 * @param value
	 * @param label
	 */
	private void traiteNomCompte(MlCompteMail p_compteMail, JLabel label) {
		int unreadMess = p_compteMail.getUreadMessCount();
		if (unreadMess > 0) {
			label
					.setText(p_compteMail.getNomCompte() + " (" + unreadMess
							+ ")");
			label.setFont(Fontfactory.getTREE_FONT_GRAS());
		} else {
			label.setText(p_compteMail.getNomCompte());
			label.setFont(Fontfactory.getTREE_FONT_PLAIN());
		}
		label.setIcon(IconeTreeFactory.getDossierOuvert());

	}
}
