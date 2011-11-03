package fenetre.principale.MlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import thread.Thread_Releve;
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

			Thread_Releve t = new Thread_Releve(bd.getListeDeComptes());

			t.start();

		} else if (actionCommand.contains("[releve ]")) {
			String nomCompte = actionCommand.substring(actionCommand
					.lastIndexOf(']') + 1);

			MlListeCompteMail lst = new MlListeCompteMail();
			MlCompteMail cpt = new MlCompteMail(nomCompte);
			cpt.setNomCompte(nomCompte);
			lst.add(cpt);
			Thread_Releve t = new Thread_Releve(lst);
			t.start();

		}
		bd.closeConnexion();

	}

}
