package imap;

import hotmail.ReleveHotmail;
import imap.util.methodeImap;

import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import Pop3.ClientMail;
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

			switch (cpt.getTypeCompte()) {
				case POP:
					methodeImap.afficheText(textArea, "Releve du compte " + s);
					new ClientMail(cpt, progress, progressPieceJointe, textArea);
					break;
				case GMAIL:
					methodeImap.afficheText(textArea, "Releve du compte " + s);
					new ReleveGmail(idCpt, cpt.getUserName(),
							cpt.getPassword(), cpt.getServeurReception(),
							progress, progressPieceJointe, textArea, isSynchro);
					break;
				case HOTMAIL:
					methodeImap.afficheText(textArea, "Releve du compte " + s);
					new ReleveHotmail(idCpt, cpt.getUserName(), cpt
							.getPassword(), cpt.getServeurReception(),
							progress, progressPieceJointe, textArea, isSynchro);
					break;
				case IMAP:
					break;
			}

		}
		bd.closeConnexion();
		progress.setVisible(false);
		textArea.setVisible(false);
		scrollPane.setVisible(false);

	}

	public static void SupprMessage(MlListeMessage p_listeMessageASupprimer) {
		MlCompteMail cpt = new MlCompteMail(p_listeMessageASupprimer.get(0)
				.getIdCompte());
		new MajServeurGmail(p_listeMessageASupprimer, cpt, progress, true);
	}

	public static void DeplaceMessage(MlListeMessage p_listMess) {
		MlCompteMail cpt = new MlCompteMail(p_listMess.get(0).getIdCompte());
		new MajServeurGmail(p_listMess, cpt, progress, false);

	}
}
