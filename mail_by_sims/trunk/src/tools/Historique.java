package tools;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mdl.MlCompteMail;
import releve.imap.util.messageUtilisateur;

public final class Historique {

	private static String TAG = "Historique";

	private Historique() {

	}

	/**
	 * ecrire une info dans le fichier historique.txt
	 * @param p_text -String L'info souhaitée.
	 */
	public static void ecrire(final String p_text) {
		final Date actuelle = new Date();
		final DateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");
		final String Date = dateFormat.format(actuelle);

		final String repTravail = GestionRepertoire.RecupRepTravail();
		final String ligne = "Le " + Date + "   " + p_text + "\r\n";

		FileWriter writer = null;
		try {
			writer = new FileWriter(repTravail + "/historique.txt", true);
			writer.write(ligne, 0, ligne.length());

		} catch (final IOException ex) {
			messageUtilisateur.affMessageException(TAG, ex,
					"Impossible d'ecrire dans l'historique");
			Historique.ecrire("Message d'erreur: " + ex);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
					messageUtilisateur.affMessageException(TAG, e,
							"Impossible d'ecrire dans l'historique");
				}
			}
		}

	}

	public static void ecrireReleveBal(MlCompteMail p_compteMail,
			String p_foldername, String p_text) {
		ecrire("[" + p_compteMail.getNomCompte() + "] [" + p_foldername + "]"
				+ p_text);
	}

	/**
	 * ouvrir le fichier historique.txt avec le programme par defaut du systeme.
	 */
	public static void lire() {
		final String repTravail = GestionRepertoire.RecupRepTravail();
		OpenWithDefaultViewer.open(repTravail + "/historique.txt");
	}

	// public static String getCheminEmail() {
	// final String repTravail = GestionRepertoire.RecupRepTravail();
	// return
	// "file:///E:/Mes%20documents/wrkspace%20VE/Mail%20by%20sims/mail%20windows%20mail/Club-intern%208f1/Inbox/0A0F638A-00000268.eml";
	// }

	// public static String readEmail() {
	// final String repTravail = GestionRepertoire.RecupRepTravail();
	// FileInputStream fstream;
	// try {
	// fstream = new FileInputStream(
	// repTravail
	// + "/mail windows mail/Club-intern 8f1/Inbox/0A0F638A-00000268.eml");
	// } catch (FileNotFoundException e) {
	//
	// return "";
	// }
	// // Get the object of DataInputStream
	// DataInputStream in = new DataInputStream(fstream);
	// BufferedReader br = new BufferedReader(new InputStreamReader(in));
	// String strLine;
	// // Read File Line By Line
	// StringBuilder sb = new StringBuilder();
	// try {
	// while ((strLine = br.readLine()) != null) {
	// sb.append(strLine);
	// // Print the content on the console
	// System.out.println(strLine);
	// }
	// } catch (IOException e) {
	// return "";
	// }
	// return sb.toString();
	// }

	// public static File getHistorique() {
	// return new File(GestionRepertoire.RecupRepTravail() + "/historique.txt");
	// }

}
