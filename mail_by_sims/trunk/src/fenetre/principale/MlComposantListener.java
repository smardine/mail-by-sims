/**
 * 
 */
package fenetre.principale;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

/**
 * @author smardine
 */
public class MlComposantListener implements ComponentListener {

	private final JPanel panelPrincipal;

	public MlComposantListener(JPanel jContentPane) {
		this.panelPrincipal = jContentPane;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		Component composant = e.getComponent();
		Dimension taille = composant.getSize();
		double hauteur = taille.getHeight();
		double largeur = taille.getWidth();
		System.out.println("taille de la fenetre: hauteur=" + hauteur
				+ " largeur=" + largeur);
		int nbComposant = panelPrincipal.getComponentCount();
		// System.out.println("il y a " + nbComposant
		// + " composant dans la fenetre");

		for (int i = 0; i < nbComposant; i++) {
			String nomCompo = panelPrincipal.getComponent(i).getName();
			Dimension dim = null;
			if (EnNomComposant.PANEL_TREE.getLib().equals(nomCompo)) {
				dim = EnNomComposant.PANEL_TREE.calculNouvelleDimension(
						largeur, hauteur);
				System.out.println("taille jtree=" + dim);

			}
			if (EnNomComposant.PANEL_BOUTON.getLib().equals(nomCompo)) {
				dim = EnNomComposant.PANEL_BOUTON.calculNouvelleDimension(
						largeur, hauteur);
				System.out.println("taille bouton=" + dim);

			}
			if (EnNomComposant.PANEL_HTML.getLib().equals(nomCompo)) {
				dim = EnNomComposant.PANEL_HTML.calculNouvelleDimension(
						largeur, hauteur);
				System.out.println("taille html=" + dim);

			}
			if (EnNomComposant.PANEL_TABLE_ET_LISTE.getLib().equals(nomCompo)) {
				dim = EnNomComposant.PANEL_TABLE_ET_LISTE
						.calculNouvelleDimension(largeur, hauteur);

			}

			panelPrincipal.getComponent(i).setSize(dim);
			panelPrincipal.getComponent(i).repaint();

		}
		panelPrincipal.repaint();
		panelPrincipal.setVisible(false);
		panelPrincipal.setVisible(true);

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
