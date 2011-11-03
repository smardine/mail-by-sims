package fenetre.principale.jTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;

import releve.imap.util.REPONSE;
import releve.imap.util.messageUtilisateur;
import thread.thread_deplaceOuSuppr;
import fenetre.Patience;
import fenetre.principale.MlAction.EnActionMain;

public class MlActionPopupJTable implements ActionListener {

	private final JTable table;
	private final JList list;
	private final JTree tree;

	public MlActionPopupJTable(JTree p_tree, JTable p_table, JList jList) {
		this.tree = p_tree;
		this.table = p_table;
		this.list = jList;

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
				traiteCreerRegle();
				break;
			case MARQUER_SPAM:
				traiteMarquerSpam();
				break;

			default:
				break;
		}

	}

	private void traiteCreerRegle() {
		// TODO Auto-generated method stub

	}

	private void traiteMarquerSpam() {
		ArrayList<String> lst = new ArrayList<String>();
		int idRow = table.getSelectedRow();

		// Integer idMessage = (Integer) table.getModel().getValueAt(idRow, 0);
		// le n° du message (meme si il est caché).
		// Date dateReception = (Date) table.getModel().getValueAt(
		// idRow, 1);// la date de reception

		// Integer idMessage = getReelIdMessage(idRow);

		String expediteur = (String) table.getModel().getValueAt(idRow, 2);// l'expediteur

		// String sujet = (String) table.getModel()
		// .getValueAt(idRow, 3);// le

		lst.add("traiter l'expediteur " + expediteur + " comme indésirable");
		lst.add("Traiter le nom de dommaine "
				+ expediteur.substring(expediteur.lastIndexOf('@'))
				+ " comme indésirable");
		String choix = messageUtilisateur.afficheChoixMultiple(
				"Courrier indésirable", "Que voulez-vous faire", lst);

		if (("traiter l'expediteur " + expediteur + " comme indésirable")
				.equals(choix)) {
			messageUtilisateur.affMessageInfo("Not yet implemented");

		} else if (("Traiter le nom de dommaine "
				+ expediteur.substring(expediteur.lastIndexOf('@')) + " comme indésirable")
				.equals(choix)) {
			messageUtilisateur.affMessageInfo("Not yet implemented");
		}

	}

	/**
	 * 
	 */
	private void traiteSupprimer() {
		int[] tabIdLigneSelectionnee = table.getSelectedRows();
		REPONSE reponse = REPONSE.NON;
		if (tabIdLigneSelectionnee.length == 0) {
			messageUtilisateur
					.affMessageInfo("Merci de d'abord selectionner un message  à supprimer");
		} else if (tabIdLigneSelectionnee.length == 1) {
			reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Suppression de message",
					"Voulez vous vraiment supprimer ce message?");
		} else if (tabIdLigneSelectionnee.length > 1) {
			reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Suppression de message",
					"Voulez vous vraiment supprimer tous ces messages?");
		}

		if (reponse == REPONSE.OUI) {

			thread_deplaceOuSuppr t = new thread_deplaceOuSuppr(tree, table,
					list, new Patience("Deplacement de message"),
					tabIdLigneSelectionnee);
			t.start();

		}
	}

}
