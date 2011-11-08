/**
 * 
 */
package fenetre.principale.jtree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import mdl.MlCompteMail;
import mdl.MlDossier;
import factory.Fontfactory;
import factory.IconeTreeFactory;
import fenetre.comptes.EnDossierBase;

/**
 * @author smardine
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3666054799792985018L;

	public CustomTreeCellRenderer() {

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object p_value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component c = super.getTreeCellRendererComponent(tree, p_value,
				selected, expanded, leaf, row, hasFocus);

		if (row != -1) {
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) p_value;
			TreePath treePath;

			treePath = tree.getPathForRow(row);
			if (treePath == null) {
				return this;
			}
			// int pathCount = treePath.getPathCount();
			JLabel label = (JLabel) c;
			if (aNode.getUserObject() instanceof MlCompteMail) {
				traiteNomCompte((MlCompteMail) aNode.getUserObject(), label);
			} else if (aNode.getUserObject() instanceof MlDossier) {
				traiteNomDossier((MlDossier) aNode.getUserObject(), label, leaf);
			}

		}

		return c;
	}

	/**
	 * @param label
	 */
	private void calculateStringSize(JLabel label) {
		if (label.getGraphics() != null) {
			final Rectangle2D r = getGraphics().getFontMetrics(getFont())
					.getStringBounds(label.getText(), getGraphics());

			final Dimension d = new Dimension((int) r.getWidth()
					+ getIcon().getIconWidth() + getIconTextGap() + 50, (int) r
					.getHeight());

			label.setMaximumSize(d);
			label.setPreferredSize(d);
			label.setMinimumSize(d);
		}
	}

	/**
	 * @param p_dossier
	 * @param label
	 * @param p_leaf
	 */
	private void traiteNomDossier(MlDossier p_dossier, JLabel label,
			boolean p_leaf) {
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
		calculateStringSize(label);
		return;
	}

	/**
	 * @param p_compteMail
	 * @param label
	 */
	private void traiteNomCompte(MlCompteMail p_compteMail, JLabel label) {
		int unreadMess = p_compteMail.getUreadMessCount();
		String nomCompte = p_compteMail.getNomCompte();
		if (unreadMess > 0) {
			label.setText(nomCompte + " (" + unreadMess + ")");
			label.setFont(Fontfactory.getTREE_FONT_GRAS());
		} else {
			label.setText(nomCompte);
			label.setFont(Fontfactory.getTREE_FONT_PLAIN());
		}
		label.setIcon(IconeTreeFactory.getDossierOuvert());
		calculateStringSize(label);

	}
}
