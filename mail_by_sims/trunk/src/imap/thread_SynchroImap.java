package imap;

import hotmail.ReleveHotmail;
import imap.util.methodeImap;

import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import bdd.BDRequette;

public class thread_SynchroImap extends Thread {

	private static JProgressBar progress;
	private final JTextArea textArea;
	private final boolean isSynchro;
	private final JProgressBar progressPieceJointe;
	private final JScrollPane scrollPane;

	public thread_SynchroImap(JProgressBar p_progressBarReleve,
			JProgressBar p_progressPieceJointe, JTextArea jTextArea,
			JScrollPane p_scroll, boolean p_isSynchro) {// ,

		this.textArea = jTextArea;
		thread_SynchroImap.progress = p_progressBarReleve;
		this.progressPieceJointe = p_progressPieceJointe;
		this.isSynchro = p_isSynchro;
		this.scrollPane = p_scroll;

	}

	@Override
	public void run() {
		textArea.setText("");
		textArea.setVisible(true);
		scrollPane.setVisible(true);
		progress.setVisible(true);
		BDRequette bd = new BDRequette();
		ArrayList<String> lst = bd.getListeDeComptes();

		for (String s : lst) {
			int idCpt = bd.getIdComptes(s);
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
							progress, progressPieceJointe, textArea, isSynchro);
				} else if (cpt.getServeurReception().contains("live")) {
					methodeImap.afficheText(textArea, "Releve du compte " + s);
					new ReleveHotmail(idCpt, cpt.getUserName(), cpt
							.getPassword(), cpt.getServeurReception(),
							progress, progressPieceJointe, textArea, isSynchro);
				}

			}

		}
		bd.closeConnexion();
		progress.setVisible(false);
		textArea.setVisible(false);
		scrollPane.setVisible(false);

	}

	public static void SupprMessage(MlListeMessage p_listeMessageASupprimer,
			String nomCompte) {
		BDRequette bd = new BDRequette();
		int idCpt = bd.getIdComptes(nomCompte);
		String user = bd.getUserFromIdCompte(idCpt);
		String pass = bd.getPasswordFromIdCompte(idCpt);
		String serveur = bd.getHostFromIdCompte(idCpt);
		new MajServeurGmail(p_listeMessageASupprimer, idCpt, user, pass,
				serveur, progress);
		bd.closeConnexion();

	}
}
