package fenetre.principale.jTable;

import imap.thread_ReleveImap;
import imap.util.REPONSE;
import imap.util.messageUtilisateur;
import importMail.MlListeMessage;
import importMail.MlMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.tree.TreePath;

import bdd.BDRequette;
import fenetre.principale.Main;
import fenetre.principale.MlAction.EnActionMain;

public class MlActionPopupJTable implements ActionListener {

	private final JTable table;
	private final JList list;

	public MlActionPopupJTable(JTable p_table, JList jList) {
		this.table = p_table;
		this.list = jList;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();

		if (EnActionMain.SUPPRIMER.getLib().equals(actionCommand)) {

			int[] nbDeLigneSelectionnee = table.getSelectedRows();
			REPONSE reponse = REPONSE.NON;
			if (nbDeLigneSelectionnee.length == 1) {
				reponse = messageUtilisateur.affMessageQuestionOuiNon(
						"Suppression de message",
						"Voulez vous vraiment supprimer ce message?");
			} else {
				if (nbDeLigneSelectionnee.length > 1) {
					reponse = messageUtilisateur
							.affMessageQuestionOuiNon("Suppression de message",
									"Voulez vous vraiment supprimer tous ces messages?");
				}
			}
			MlListeMessage lst = new MlListeMessage();
			for (int i = 0; i < nbDeLigneSelectionnee.length; i++) {
				int selectedLine = nbDeLigneSelectionnee[i];
				Integer idMessage = (Integer) table.getModel().getValueAt(
						selectedLine, 0);
				// le n° du message (meme si il est caché).
				Date dateReception = (Date) table.getModel().getValueAt(
						selectedLine, 1);// la date de reception

				String expediteur = (String) table.getModel().getValueAt(
						selectedLine, 2);// l'expediteur

				String sujet = (String) table.getModel().getValueAt(
						selectedLine, 3);// le
				// sujet
				// du
				// message

				if (reponse == REPONSE.OUI) {
					MlMessage m = BDRequette.getMessageById(idMessage);
					lst.add(m);
					BDRequette.deleteMessageRecu(idMessage);

				}

			}
			thread_ReleveImap t = new thread_ReleveImap(null, null, null);
			t.SupprMessage(lst, Main.getNomCompte());

			TreePath treePath = Main.getTreePath();
			String dossierChoisi = (String) treePath.getLastPathComponent();

			if (!BDRequette.getListeDeComptes().contains(dossierChoisi)) {
				String idCompte = BDRequette.getIdComptes(Main.getNomCompte());
				String idDossierChoisi = BDRequette.getIdDossier(dossierChoisi,
						idCompte);
				MlListeMessage listeMessage = BDRequette.getListeDeMessage(
						idCompte, idDossierChoisi);

				MyTableModel modelDetable = (MyTableModel) table.getModel();
				modelDetable.valorisetable(listeMessage);

			}
			table.setRowSelectionInterval(0, 0);
			MlActionJtable.afficheContenuMail(table, list);
		}

	}
}
