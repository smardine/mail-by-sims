package fenetre.principale.jTable;

import imap.util.REPONSE;
import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JTable;

import bdd.BDRequette;

public class MlActionPopupJTable implements ActionListener {

	private final JTable table;

	public MlActionPopupJTable(JTable p_table) {
		this.table = p_table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		int selectedLine = table.getSelectedRow();

		Integer idMessage = (Integer) table.getModel().getValueAt(selectedLine,
				0);// le
		// n°
		// du
		// message
		// (meme
		// si il est caché.
		Date dateReception = (Date) table.getModel()
				.getValueAt(selectedLine, 1);// la date de reception

		String expediteur = (String) table.getModel().getValueAt(selectedLine,
				2);// l'expediteur

		String sujet = (String) table.getModel().getValueAt(selectedLine, 3);// le
		// sujet
		// du
		// message

		if ("SUPPRIMER".equals(actionCommand)) {
			REPONSE reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Suppression de message",
					"Voulez vous vraiment supprimer ce message?");
			if (reponse == REPONSE.OUI) {
				BDRequette.deleteMessageRecu(idMessage);

			}

		}

	}

}
