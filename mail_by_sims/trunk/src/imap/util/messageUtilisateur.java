package imap.util;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import tools.Historique;

public class messageUtilisateur {

	private messageUtilisateur() {

	}

	public static void affMessageException(Exception p_exception, String p_titre) {
		if (p_exception == null) {
			JOptionPane.showMessageDialog(null, null, p_titre,
					JOptionPane.ERROR_MESSAGE);
			Historique.ecrire("Exception relevée: \n" + p_titre);
		} else {
			JOptionPane.showMessageDialog(null, p_exception.getClass() + " "
					+ p_exception.getMessage(), p_titre,
					JOptionPane.ERROR_MESSAGE);
			Historique.ecrire("Titre: " + p_titre);
			Historique.ecrire("Exception dans la class "
					+ p_exception.getClass());
			Historique.ecrire("Cause de l'erreur: " + p_exception.getMessage());
		}

	}

	public static void affMessageErreur(String p_message) {

		JOptionPane.showMessageDialog(null, p_message,
				"Une erreur est survenue", JOptionPane.ERROR_MESSAGE);
	}

	public static void affMessageInfo(String p_message) {
		JOptionPane.showMessageDialog(null, p_message, "Pour information",
				JOptionPane.INFORMATION_MESSAGE);

	}

	public static String affMessageInput(String p_texteDemande) {
		return JOptionPane.showInputDialog(p_texteDemande);

	}

	public static REPONSE affMessageQuestionOuiNon(String p_titre,
			String p_message) {

		int reponse = JOptionPane.showConfirmDialog(null, p_message, p_titre,
				JOptionPane.YES_NO_OPTION);
		switch (reponse) {
			case 0:
				return REPONSE.OUI;
			case 1:
				return REPONSE.NON;
			default:
				return REPONSE.NON;
		}

	}

	public static String afficheChoixMultiple(String p_titre, String p_message,
			ArrayList<String> p_valeurPossible) {

		Object[] tabObj = new Object[p_valeurPossible.size()];
		for (int i = 0; i < p_valeurPossible.size(); i++) {
			tabObj[i] = p_valeurPossible.get(i);
		}

		return (String) JOptionPane.showInputDialog(null, "message", "titre",
				JOptionPane.QUESTION_MESSAGE, null, tabObj, tabObj[0]);

	}

}
