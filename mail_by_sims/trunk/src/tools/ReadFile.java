package tools;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import releve.imap.util.messageUtilisateur;

public final class ReadFile {
	private final static String TAG = "ReadFile";

	private ReadFile() {

	}

	private static final long serialVersionUID = 1L;

	/**
	 * Lecture ligne à ligne d'un fichier texte et affichage dans une jList
	 * @param chemin -String le chemin du fichier texte
	 * @param listModel -DefaultModelList le model de liste
	 * @param nbLigne -JLabel sert a afficher le nb de ligne
	 * @param nbAdresse -int le nb d'adresse trouvée.
	 */

	public static int ReadLine(String chemin, DefaultListModel listModel) {

		int nbAdresse = 0;
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(chemin);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				// Print the content on the console
				// System.out.println (strLine);

				if (!strLine.equals("")) {
					listModel.addElement(strLine);
					nbAdresse++;
				}
			}
			// Close the input stream
			in.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return nbAdresse;
	}

	/**
	 * Trouver une chaine de caracteres dans un fichier
	 * @param cheminFichier -String le chemin du fichier
	 * @param p_occurToFind -String la chaine a trouver ex "abc@hotmail.com"
	 * @return result -boolean vrai si on trouve la chaine de caracteres.
	 */
	public static boolean FindOccurInFile(String cheminFichier,
			String p_occurToFind) {

		String line = null;
		boolean result = false;

		try {
			BufferedReader br = new BufferedReader(
					new FileReader(cheminFichier));

			int i = 1; // initialisation du numero de ligne
			while ((line = br.readLine()) != null) {
				if (line.indexOf(p_occurToFind) != -1) {
					System.out.println("Mot trouve a la ligne " + i);
					result = true;
					return result;
				}
				i++;
			}
			br.close();
		} catch (FileNotFoundException exc) {
			System.out.println("File not found");
		} catch (IOException ioe) {
			System.out.println("Erreur IO");
		}
		return result;

	}

	public static ArrayList<String> getContenu(String p_chemin) {
		ArrayList<String> lst = new ArrayList<String>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(p_chemin);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				if (!strLine.equals("")) {
					// strLine = strLine.replaceAll(" ", "");
					lst.add(strLine);
				}
			}
			// Close the input stream
			in.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return lst;

	}

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
