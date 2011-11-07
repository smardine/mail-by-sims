/**
 * 
 */
package factory;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author smardine
 */
public final class IconeTreeFactory {
	private static Icon Reception;
	private static Icon Corbeille;
	private static Icon Spam;
	private static Icon Envoye;
	private static Icon Brouillon;
	private static Icon DossierFerme;
	private static Icon DossierOuvert;

	private IconeTreeFactory() {

	}

	/**
	 * @return the reception
	 */
	public static Icon getReception() {
		if (Reception == null) {
			Reception = new ImageIcon("Images/recevoir_16.png");
		}
		return Reception;
	}

	/**
	 * @return the corbeille
	 */
	public static Icon getCorbeille() {
		if (Corbeille == null) {
			Corbeille = new ImageIcon("Images/supprimer_16.png");
		}
		return Corbeille;
	}

	/**
	 * @return the spam
	 */
	public static Icon getSpam() {
		if (Spam == null) {
			Spam = new ImageIcon("Images/spam_16.png");
		}
		return Spam;
	}

	/**
	 * @return the envoye
	 */
	public static Icon getEnvoye() {
		if (Envoye == null) {
			Envoye = new ImageIcon("Images/envoyer_16.png");
		}
		return Envoye;
	}

	/**
	 * @return the brouillon
	 */
	public static Icon getBrouillon() {
		if (Brouillon == null) {
			Brouillon = new ImageIcon("Images/brouillon_16.png");
		}
		return Brouillon;
	}

	/**
	 * @return the dossierFerme
	 */
	public static Icon getDossierFerme() {
		if (DossierFerme == null) {
			DossierFerme = new ImageIcon("Images/dossier-ferme-16.png");
		}
		return DossierFerme;
	}

	/**
	 * @return the dossierOuvert
	 */
	public static Icon getDossierOuvert() {
		if (DossierOuvert == null) {
			DossierOuvert = new ImageIcon("Images/dossier-ouvert-16.png");
		}
		return DossierOuvert;
	}

}
