package fenetre.principale.jTable;

import imap.thread_deplaceOuSuppr;
import imap.util.REPONSE;
import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import fenetre.principale.MlAction.EnActionMain;

public class MlActionPopupJTable implements ActionListener {

	private final JTable table;
	private final JList list;
	private final JProgressBar progressBar;
	private final JTextArea textArea;
	private final JScrollPane scroll;

	public MlActionPopupJTable(JTable p_table, JList jList,
			JProgressBar p_progress, JTextArea p_text, JScrollPane p_scroll) {
		this.table = p_table;
		this.list = jList;
		this.progressBar = p_progress;
		this.textArea = p_text;
		this.scroll = p_scroll;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		EnActionMain actionMain = EnActionMain.getEnumFromLib(actionCommand);
		switch (actionMain) {
			case SUPPRIMER:
				traiteSupprimer();
				break;
			case CREER_REGLE:
				break;
			case MARQUER_SPAM:
				break;

			default:
				break;
		}

	}

	/**
	 * 
	 */
	private void traiteSupprimer() {
		int[] tabIdLigneSelectionnee = table.getSelectedRows();
		REPONSE reponse = REPONSE.NON;
		if (tabIdLigneSelectionnee.length == 1) {
			reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Suppression de message",
					"Voulez vous vraiment supprimer ce message?");
		} else {
			if (tabIdLigneSelectionnee.length > 1) {
				reponse = messageUtilisateur.affMessageQuestionOuiNon(
						"Suppression de message",
						"Voulez vous vraiment supprimer tous ces messages?");
			}
		}
		if (reponse == REPONSE.OUI) {

			thread_deplaceOuSuppr t = new thread_deplaceOuSuppr(table, list,
					progressBar, textArea, scroll, tabIdLigneSelectionnee);
			t.start();

		}
	}

}
