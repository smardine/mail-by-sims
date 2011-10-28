package tools;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import releve.imap.util.messageUtilisateur;

public final class GestionRepertoire {
	private static final String TAG = "GestionRepertoire";

	private GestionRepertoire() {

	}

	/**
	 * Recupere le repertoire de travail de l'application
	 * @return Le repertoire de travail -String
	 */
	public static String RecupRepTravail() {
		final File dir = new File(".");
		String sAppliDir = "";
		try {
			sAppliDir = dir.getCanonicalPath();
		} catch (final Exception e1) {
			Historique.ecrire("Message d'erreur: " + e1);
		}
		return sAppliDir;
	}

	/**
	 * @return
	 */
	public static String RecupRepTemplate() {
		final File dir = new File("./template");
		String sAppliDir = "";
		try {
			sAppliDir = dir.getCanonicalPath();
		} catch (final Exception e1) {
			Historique.ecrire("Message d'erreur: " + e1);
		}
		return sAppliDir;
	}

	/**
	 * Affiche une fenetre permettant d'ouvrir un fichier
	 * @return le chemin complet -String
	 */
	public static String openFile() {
		final JFileChooser chooser = new JFileChooser();
		final int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			Historique.ecrire("You chose to open this file: "
					+ chooser.getSelectedFile().getName());

		}
		return chooser.getSelectedFile().getName();

	}

	/**
	 * Affiche une boite de dialogue pour la création d'un dossier
	 * @return nomdossier -String
	 */
	public static String OpenFolder(String p_titre) {
		String nomdossier = null;
		JFrame frame = new JFrame();
		frame.setTitle(p_titre);
		JFileChooser choiceFolder = new JFileChooser();
		choiceFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choiceFolder.showOpenDialog(frame);

		File file = choiceFolder.getSelectedFile();
		try {
			// on utilise le getCanonicalPath pour recuperer le pathname sous la
			// forme windows
			nomdossier = file.getCanonicalPath();

		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible d'acceder au dossier choisi");
		}

		return nomdossier;

	}

}
