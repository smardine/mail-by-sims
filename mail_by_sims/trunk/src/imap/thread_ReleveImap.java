package imap;

import imap.util.methodeImap;
import importMail.MlListeMessage;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import bdd.BDRequette;

public class thread_ReleveImap extends Thread {

	private final JProgressBar progress;
	private final JTextArea textArea;
	private final JLabel label;

	public thread_ReleveImap(JProgressBar p_progressBar, JTextArea jTextArea,
			JLabel jLabel) {// ,
		this.label = jLabel;
		this.textArea = jTextArea;
		this.progress = p_progressBar;

	}

	@Override
	public void run() {

		ArrayList<String> lst = BDRequette.getListeDeComptes();

		for (String s : lst) {
			String idCpt = BDRequette.getIdComptes(s);
			String user = BDRequette.getUserFromIdCompte(idCpt);
			String pass = BDRequette.getPasswordFromIdCompte(idCpt);
			String serveur = BDRequette.getHostFromIdCompte(idCpt);
			if (serveur.contains("pop")) {
				System.out.println("not yet implemented");
				// new ClientMail(user, pass, serveur);
			} else if (serveur.contains("gmail")) {
				methodeImap.afficheText(textArea, "Releve du compte " + s);
				new ReleveGmail(idCpt, user, pass, serveur, progress, textArea,
						label);
			}

			else {
				new ReleveAutreImap(idCpt, user, pass, serveur, progress);
			}

		}

	}

	public void SupprMessage(MlListeMessage p_listeMessageASupprimer,
			String nomCompte) {

		String idCpt = BDRequette.getIdComptes(nomCompte);
		String user = BDRequette.getUserFromIdCompte(idCpt);
		String pass = BDRequette.getPasswordFromIdCompte(idCpt);
		String serveur = BDRequette.getHostFromIdCompte(idCpt);
		new MajServeurGmail(p_listeMessageASupprimer, idCpt, user, pass, serveur,
				progress);

	}
}
