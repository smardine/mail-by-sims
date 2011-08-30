package tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Historique {

	private Historique() {

	}

	/**
	 * ecrire une info dans le fichier historique.txt
	 * @param p_text -String L'info souhaitée.
	 */
	public static void ecrire(final String p_text) {
		// String Date = RecupDate.date();

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
			Historique.ecrire("Message d'erreur: " + ex);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
					System.out.println(e);
				}
			}
		}

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

	public static String readEmail() {
		final String repTravail = GestionRepertoire.RecupRepTravail();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(
					repTravail
							+ "/mail windows mail/Club-intern 8f1/Inbox/0A0F638A-00000268.eml");
		} catch (FileNotFoundException e) {

			return "";
		}
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		// Read File Line By Line
		StringBuilder sb = new StringBuilder();
		try {
			while ((strLine = br.readLine()) != null) {
				sb.append(strLine);
				// Print the content on the console
				System.out.println(strLine);
			}
		} catch (IOException e) {
			return "";
		}
		return sb.toString();
	}

}
