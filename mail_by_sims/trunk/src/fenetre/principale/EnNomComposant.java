package fenetre.principale;

import java.awt.Dimension;

public enum EnNomComposant {
	PANEL_HTML("panelHtml", 800, 300), //
	PANEL_BOUTON("panelBouton", 800, 75), //
	PANEL_TREE("panelTree", 250, 200), //
	PANEL_TABLE_ET_LISTE("panelTableEtListe", 525, 200);

	private String lib;
	private int hauteurInitiale;
	private int largeurInitiale;

	EnNomComposant(String p_lib, int p_largeurInitiale, int p_hauteurInitiale) {
		this.lib = (p_lib);
		this.hauteurInitiale = p_hauteurInitiale;
		this.largeurInitiale = p_largeurInitiale;
	}

	/**
	 * @return the lib
	 */
	public String getLib() {
		return lib;
	}

	/**
	 * @return the hauteurInitiale
	 */
	public int getHauteurInitiale() {
		return hauteurInitiale;
	}

	/**
	 * @return the largeurInitiale
	 */
	public int getLargeurInitiale() {
		return largeurInitiale;
	}

	public Dimension calculNouvelleDimension(double largeur, double hauteur) {
		Dimension dim = new Dimension();
		dim.setSize((largeur * this.getLargeurInitiale()) / 800,
				(hauteur * this.getHauteurInitiale()) / 600);
		return dim;
	}
}
