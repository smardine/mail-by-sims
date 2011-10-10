package fenetre.comptes.creation.MlActionCreation;

import javax.swing.JTextField;

public final class utilityCreation {

	private utilityCreation() {

	}

	protected static boolean verifAdresseEtNom(JTextField p_adresse,
			JTextField p_nomCompte) {
		return verifchampAdresse(p_adresse) || verifchampNom(p_nomCompte);
	}

	protected static boolean verifPopSmtp(JTextField p_pop, JTextField p_smtp) {
		return verifchampPop(p_pop) || verifChampSmtp(p_smtp);
	}

	protected static boolean verifUserPassword(JTextField p_user,
			JTextField p_password) {
		return verifChampUser(p_user) || verifChampPassword(p_password);
	}

	protected static boolean verifchampAdresse(JTextField p_adresse) {
		return p_adresse.getText() == null || p_adresse.getText().equals("");
	}

	protected static boolean verifchampNom(JTextField p_nomCompte) {
		return p_nomCompte.getText() == null
				|| p_nomCompte.getText().equals("");
	}

	protected static boolean verifchampPop(JTextField p_pop) {
		return p_pop.getText() == null || p_pop.getText().equals("");
	}

	protected static boolean verifChampSmtp(JTextField p_smtp) {
		return p_smtp.getText() == null || p_smtp.getText().equals("");
	}

	protected static boolean verifChampUser(JTextField p_user) {
		return p_user.getText() == null || p_user.getText().equals("");
	}

	protected static boolean verifChampPassword(JTextField p_password) {
		return p_password.getText() == null || p_password.getText().equals("");
	}

}
