package fenetre;

public enum SizeAndPosition {

	MAIN(800, 600, 0, 0), JTREE(265, 545, 0, 0), JEDITOR(535, 350, 266, 190), JTABLE(
			530, 191, 265, 0);

	private int hauteur;
	private int largeur;
	private int positionHorizontale;
	private int positionVerticale;

	SizeAndPosition(int p_largeur, int p_hauteur, int p_horizontale,
			int p_verticale) {
		this.hauteur = p_largeur;
		this.largeur = p_hauteur;
		this.positionHorizontale = p_horizontale;
		this.positionVerticale = p_verticale;
	}

	/**
	 * @return the hauteur
	 */
	public int getHauteur() {
		return hauteur;
	}

	/**
	 * @return the largeur
	 */
	public int getLargeur() {
		return largeur;
	}

	/**
	 * @return the positionHorizontale
	 */
	public int getPositionHorizontale() {
		return positionHorizontale;
	}

	/**
	 * @return the positionVerticale
	 */
	public int getPositionVerticale() {
		return positionVerticale;
	}

}
