package releve.imap.util;

import java.util.List;

import javax.swing.JOptionPane;

import tools.Historique;

public final class messageUtilisateur {

	private messageUtilisateur() {

	}

	public static void affMessageException(String p_tag, Exception p_exception,
			String p_titre) {
		if (p_exception == null) {
			JOptionPane.showMessageDialog(null, null, p_titre,
					JOptionPane.ERROR_MESSAGE);
			Historique.ecrire("[EXCEPTION]" + p_tag + " Exception relevée: \n"
					+ p_titre);
		} else {
			JOptionPane.showMessageDialog(null, p_exception.getClass() + " "
					+ p_exception.getMessage(), p_titre,
					JOptionPane.ERROR_MESSAGE);
			Historique.ecrire("[EXCEPTION] " + p_tag + " " + p_titre + " "
					+ p_exception.toString());
			Throwable instack = p_exception.fillInStackTrace();
			for (StackTraceElement s : instack.getStackTrace()) {
				Historique.ecrire("[EXCEPTION] " + s.toString());
			}

		}

	}

	public static void affMessageErreur(String p_tag, String p_message) {

		JOptionPane.showMessageDialog(null, p_message,
				"Une erreur est survenue", JOptionPane.ERROR_MESSAGE);
		Historique.ecrire(p_tag + " [ERROR] " + p_message);
	}

	public static void affMessageInfo(String p_message) {
		JOptionPane.showMessageDialog(null, p_message, "Pour information",
				JOptionPane.INFORMATION_MESSAGE);
		Historique.ecrire("[INFO] " + p_message);

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
			List<String> p_valeurPossible) {

		Object[] tabObj = new Object[p_valeurPossible.size()];
		for (int i = 0; i < p_valeurPossible.size(); i++) {
			tabObj[i] = p_valeurPossible.get(i);
		}

		return (String) JOptionPane.showInputDialog(null, p_message, p_titre,
				JOptionPane.QUESTION_MESSAGE, null, tabObj, tabObj[0]);

	}

	// public static void afficheText(JTextArea p_textArea, String p_text) {
	//
	// p_textArea.append(p_text + "\n");
	// p_textArea.setCaretPosition(p_textArea.getDocument().getLength());
	//
	// }

}
