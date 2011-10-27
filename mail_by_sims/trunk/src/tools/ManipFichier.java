package tools;

import java.awt.FileDialog;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import releve.imap.util.messageUtilisateur;

public final class ManipFichier {
	private static String TAG = "ManipFichier";

	/**
	 * Constructeur privé pour classe utilitaire
	 */
	private ManipFichier() {

	}

	/**
	 * Deplace un fichier
	 * @param source -File le fichier source
	 * @param destination -File le fichier de destination
	 * @return result -Boolean vrai si ca a marché
	 */

	public static boolean deplacer(final File source, final File destination) {
		if (!destination.exists()) {
			// On essaye avec renameTo
			boolean result = source.renameTo(destination);
			if (!result) {
				// On essaye de copier
				result = true;
				result &= copier(source, destination);
				result &= source.delete();

			}
			return (result);
		} else {
			// Si le fichier destination existe, on annule ...
			return (false);
		}
	}

	/**
	 * copie un fichier
	 * @param source -File le fichier source
	 * @param destination -File le fichier de destination
	 * @return resultat -Boolean vrai si ca a marché
	 */
	public static boolean copier(final File source, final File destination) {
		return copier(source, destination, null);
	}

	/**
	 * Affiche une boite de dialogue pour la création d'un fichier
	 * @return nomdossier+nomfichier -String
	 */
	public static String SaveFile() {

		String nomfichier;
		String nomdossier;

		final JFrame frame = new JFrame();
		final FileDialog fd = new FileDialog(frame,
				"Sélectionner le nouveau fichier qui contiendra les adresses",
				FileDialog.SAVE);
		fd.setVisible(true);
		nomfichier = fd.getFile();
		nomdossier = fd.getDirectory();
		return nomdossier + nomfichier;
	}

	/**
	 * Affiche une boite de dialogue pour l'ouverture d'un fichier
	 * @param p_cheminAafficher -String definit le chemin a afficher, si celui
	 *            ci n'existe pas, ce sera le repertoire de l'install qui
	 *            apparait
	 * @param p_texteTitre -String Le texte affiché en haut de la fenetre de
	 *            selection
	 * @return nomdossier+nomfichier -String
	 */
	public static String OpenFile(final String p_cheminAafficher,
			final String p_texteTitre) {

		String nomfichier;
		String nomdossier;

		final JFrame frame = new JFrame();
		final FileDialog fd = new FileDialog(frame, p_texteTitre,
				FileDialog.LOAD);

		fd.setDirectory(p_cheminAafficher);
		fd.setVisible(true);
		nomfichier = fd.getFile();
		nomdossier = fd.getDirectory();
		if (nomfichier == null) {
			// on a cliqué sur le bouton Annuler
			return nomdossier;
		}
		return nomdossier + nomfichier;
	}

	/**
	 * Affiche une boite de dialogue pour la création d'un dossier
	 * @return nomdossier -String
	 */
	public static String OpenFolder() {
		String nomdossier = null;
		final JFrame frame = new JFrame();

		final JFileChooser choiceFolder = new JFileChooser();
		choiceFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choiceFolder.showOpenDialog(frame);

		final File file = choiceFolder.getSelectedFile();
		if (file != null) {
			try {
				// on utilise le getCanonicalPath pour recuperer le pathname
				// sous la forme windows
				nomdossier = file.getCanonicalPath();

			} catch (final IOException e) {

				Historique.ecrire("Message d'erreur: " + e);
			}
		}

		return nomdossier;

	}

	/**
	 * Suppresion du contenu d'un repertoire avec un filtre sur l'extension
	 * @param p_repAVider - File repertoire à vider.
	 * @param p_extension -Final String extension sous la forme ".eml"
	 */
	public static void DeleteContenuRepertoireAvecFiltre(
			final File p_repAVider, final String p_extension) {
		if (p_repAVider.isDirectory()) {
			final File[] list = p_repAVider.listFiles(new FilenameFilter() {
				public boolean accept(final File dir, final String name) {
					return name.toLowerCase().endsWith(p_extension);
				}
			});
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					// Appel récursif sur les sous-répertoires
					DeleteContenuRepertoireAvecFiltre(list[i], p_extension);
				}
			} else {
				messageUtilisateur.affMessageErreur(TAG, p_repAVider
						+ " : Erreur de lecture.");

			}

		}

		if (p_repAVider.isFile()) {
			// listModel.addElement (RepAVider.getName());
			p_repAVider.delete();
			// nbFichier++;
			// nbFichierLabel.setText("Nombre de fichier(s) a traiter:"+nbFichier);

		}
	}

	public static String SaveFile(final String fileName) {
		String nomfichier;
		String nomdossier;

		final JFrame frame = new JFrame();
		final FileDialog fd = new FileDialog(frame,
				"Sélectionner l'emplacement du fichier", FileDialog.SAVE);
		fd.setFile(fileName);
		fd.setVisible(true);

		nomfichier = fd.getFile();
		nomdossier = fd.getDirectory();
		return nomdossier + nomfichier;
	}

	public static boolean copier(final File source, final File destination,
			final JProgressBar p_progressBar) {
		boolean resultat = false;

		// Declaration des flux
		java.io.FileInputStream sourceFile = null;
		java.io.FileOutputStream destinationFile = null;

		try {
			// Création du fichier :
			destination.createNewFile();

			// Ouverture des flux
			sourceFile = new java.io.FileInputStream(source);
			destinationFile = new java.io.FileOutputStream(destination);

			// Lecture par segment de 0.5Mo
			final byte buffer[] = new byte[512 * 1024];
			int nbLecture;
			long progressionEnCours = 0;

			while ((nbLecture = sourceFile.read(buffer)) != -1) {
				destinationFile.write(buffer, 0, nbLecture);
				if (p_progressBar != null) {
					progressionEnCours = (100 * destination.length())
							/ source.length();
					final int progression = (int) progressionEnCours;
					p_progressBar.setValue(progression);
					p_progressBar.setString(progression + " %");
				}

			}

			// Copie réussie
			resultat = true;
		} catch (final java.io.FileNotFoundException f) {

		} catch (final java.io.IOException e) {

		} finally {
			// Quoi qu'il arrive, on ferme les flux
			try {
				sourceFile.close();
			} catch (final Exception e) {
			}
			try {
				destinationFile.close();
			} catch (final Exception e) {
			}
		}
		return (resultat);
	}
}
