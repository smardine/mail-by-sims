package tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import releve.imap.util.messageUtilisateur;

public final class WriteFile {
	private static final String TAG = "WriteFile";

	private WriteFile() {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Ecrire le contenu d'un String dans un fichier
	 * @param contenu -String ce qu'il faut ecrire
	 * @param chemin -String le chemin du fichier
	 */

	public static void WriteFullFile(String contenu, String chemin) {

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(chemin));
			out.write(contenu);
			out.close();
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible de créer le fichier " + chemin);

		}
	}

}
