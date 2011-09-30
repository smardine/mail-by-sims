package fenetre.principale.MlAction;

import imap.thread_SynchroImap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import bdd.BDRequette;

public class MlActionPopupButton implements ActionListener {

	private final JProgressBar pbReleve;
	private final JProgressBar pbPieceJointe;
	private final JTextArea text;
	private final JScrollPane scrollPane;

	public MlActionPopupButton(JProgressBar p_progressReleve,
			JProgressBar p_progressPieceJointe, JTextArea p_text,
			JScrollPane p_scroll) {
		this.pbReleve = p_progressReleve;
		this.pbPieceJointe = p_progressPieceJointe;
		this.text = p_text;
		this.scrollPane = p_scroll;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		BDRequette bd = new BDRequette();
		if ("[releve ]Relever tous les comptes".equals(actionCommand)) {

			thread_SynchroImap t = new thread_SynchroImap(pbReleve,
					pbPieceJointe, text, scrollPane, false, bd
							.getListeDeComptes());

			t.start();

		} else if ("[synchro ]Synchroniser tous les comptes"
				.equals(actionCommand)) {
			thread_SynchroImap t = new thread_SynchroImap(pbReleve,
					pbPieceJointe, text, scrollPane, true, bd
							.getListeDeComptes());
			t.start();
		} else if (actionCommand.contains("[releve ]")) {
			String nomCompte = actionCommand.substring(actionCommand
					.lastIndexOf("]") + 1);

			MlListeCompteMail lst = new MlListeCompteMail();
			MlCompteMail cpt = new MlCompteMail(nomCompte);
			cpt.setNomCompte(nomCompte);
			lst.add(cpt);
			thread_SynchroImap t = new thread_SynchroImap(pbReleve,
					pbPieceJointe, text, scrollPane, false, lst);
			t.start();

		} else if (actionCommand.contains("[synchro ]")) {
			String nomCompte = actionCommand.substring(actionCommand
					.lastIndexOf("]") + 1);
			MlListeCompteMail lst = new MlListeCompteMail();
			MlCompteMail cpt = new MlCompteMail(nomCompte);
			cpt.setNomCompte(nomCompte);
			lst.add(cpt);
			thread_SynchroImap t = new thread_SynchroImap(pbReleve,
					pbPieceJointe, text, scrollPane, true, lst);
			t.start();
		}
		bd.closeConnexion();

	}

}
