package tools;

import java.io.FileReader;

import releve.imap.util.messageUtilisateur;

public final class ReadFile {
	private static final String TAG = "ReadFile";

	private ReadFile() {

	}

	private static final long serialVersionUID = 1L;

	public static String getContenuCaractere(String p_chemin) {
		StringBuilder sb = new StringBuilder();
		try {
			FileReader entree = new FileReader(p_chemin);
			int c;

			while ((c = entree.read()) != -1) {
				if (c != 0) {// correspond a un caractere null
					// System.out.println((char) c);
					sb.append((char) c);
				}

			}
			entree.close();
		} catch (Exception e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Impossible de lire le fichier " + p_chemin);
		}
		return sb.toString();
	}
}
