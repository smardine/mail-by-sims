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
	private final ArrayList<String> listeDeCompte;

	public thread_SynchroImap(JProgressBar p_progressBarReleve,
			JProgressBar p_progressPieceJointe, JTextArea jTextArea,
			JScrollPane p_scroll, boolean p_isSynchro,
			ArrayList<String> p_listDeCompte) {// ,

		this.textArea = jTextArea;
		thread_SynchroImap.progress = p_progressBarReleve;
		this.progressPieceJointe = p_progressPieceJointe;
		this.isSynchro = p_isSynchro;
		this.scrollPane = p_scroll;
		this.listeDeCompte = p_listDeCompte;

	}

	@Override
	public void run() {
		initComposantVisuel(true);
		BDRequette bd = new BDRequette();

		for (String s : listeDeCompte) {
			int idCpt = bd.getIdComptes(s);
			MlCompteMail cpt = new MlCompteMail(idCpt);
			definiCompteARelever(cpt);

		}
		bd.closeConnexion();
		initComposantVisuel(false);

	}

	/**
	 * @param cpt
	 */
	private void definiCompteARelever(MlCompteMail cpt) {
		switch (cpt.getTypeCompte()) {
			case POP:
				methodeImap.afficheText(textArea, "Releve du compte "
						+ cpt.getNomCompte());
				new ClientMail(cpt, progress, progressPieceJointe, textArea);
				break;
			case GMAIL:
				methodeImap.afficheText(textArea, "Releve du compte "
						+ cpt.getNomCompte());
				new ReleveGmail(cpt.getIdCompte(), cpt.getUserName(), cpt
						.getPassword(), cpt.getServeurReception(), progress,
						progressPieceJointe, textArea, isSynchro);
				break;
			case HOTMAIL:
				methodeImap.afficheText(textArea, "Releve du compte "
						+ cpt.getNomCompte());
				new ReleveHotmail(cpt.getIdCompte(), cpt.getUserName(), cpt
						.getPassword(), cpt.getServeurReception(), progress,
						progressPieceJointe, textArea, isSynchro);
				break;
			case IMAP:
				break;
		}
	}

	public void ReleveOuSynchroParticulier(MlCompteMail p_compteMail) {
		initComposantVisuel(true);

		definiCompteARelever(p_compteMail);

		initComposantVisuel(false);

	}

	/**
	 * 
	 */
	private void initComposantVisuel(boolean p_visible) {
		textArea.setText("");
		progress.setValue(0);
		progress.setString("");
		textArea.setVisible(p_visible);
		scrollPane.setVisible(p_visible);
		progress.setVisible(p_visible);
	}

	public void SupprMessage(MlListeMessage p_listeMessageASupprimer) {
		MlCompteMail cpt = new MlCompteMail(p_listeMessageASupprimer.get(0)
				.getIdCompte());
		switch (cpt.getTypeCompte()) {
			case GMAIL:
				new MajServeurGmail(p_listeMessageASupprimer, cpt, progress,
						textArea, true);
				break;
			case HOTMAIL:
				break;
			case IMAP:
				break;
			case POP:
				break;
		}

	}

	public void DeplaceMessageVersCorbeille(MlListeMessage p_listMess) {
		MlCompteMail cpt = new MlCompteMail(p_listMess.get(0).getIdCompte());
		switch (cpt.getTypeCompte()) {
			case GMAIL:
				new MajServeurGmail(p_listMess, cpt, progress, textArea, false);
				break;

			case HOTMAIL:
				break;
			case POP:
				break;
			case IMAP:

				break;
		}

	}
}
