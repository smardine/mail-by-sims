package imap;

import imap.util.methodeImap;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import bdd.BDRequette;

public class thread_SynchroImap extends Thread {

	private static JProgressBar progress;
	private final JTextArea textArea;
	private final JLabel label;
	private final boolean isSynchro;

	public thread_SynchroImap(JProgressBar p_progressBar, JTextArea jTextArea,
			JLabel jLabel, boolean p_isSynchro) {// ,
		this.label = jLabel;
		this.textArea = jTextArea;
		thread_SynchroImap.progress = p_progressBar;
		this.isSynchro = p_isSynchro;

	}

	@Override
	public void run() {

		ArrayList<String> lst = BDRequette.getListeDeComptes();

		for (String s : lst) {
			int idCpt = BDRequette.getIdComptes(s);
			MlCompteMail cpt = new MlCompteMail(idCpt);
			// String user = BDRequette.getUserFromIdCompte(idCpt);
			// String pass = BDRequette.getPasswordFromIdCompte(idCpt);
			// String serveur = BDRequette.getHostFromIdCompte(idCpt);
			if (!cpt.isImap()) {
				System.out.println("not yet implemented");
				// new ClientMail(user, pass, serveur);
			}
			if (cpt.isImap()) {
				if (cpt.getServeurReception().contains("gmail")) {
					methodeImap.afficheText(textArea, "Releve du compte " + s);
					new ReleveGmail(idCpt, cpt.getUserName(),
							cpt.getPassword(), cpt.getServeurReception(),
							progress, textArea, label, isSynchro);
				}

				if (cpt.getServeurReception().contains("live")) {
					methodeImap.afficheText(textArea, "Releve du compte " + s);
					// new ReleveHotmail(idCpt, cpt.getUserName(), cpt
					// .getPassword(), cpt.getServeurReception(),
					// progress, textArea, label, isSynchro);
				}

				else {

				}
			}

		}

	}

	public static void SupprMessage(MlListeMessage p_listeMessageASupprimer,
			String nomCompte) {

		int idCpt = BDRequette.getIdComptes(nomCompte);
		String user = BDRequette.getUserFromIdCompte(idCpt);
		String pass = BDRequette.getPasswordFromIdCompte(idCpt);
		String serveur = BDRequette.getHostFromIdCompte(idCpt);
		new MajServeurGmail(p_listeMessageASupprimer, idCpt, user, pass,
				serveur, progress);

	}
}
