package fenetre.principale.MlAction;

import imap.util.REPONSE;
import imap.util.messageUtilisateur;
import importMail.MlListeMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.tree.TreePath;

import bdd.BDRequette;
import fenetre.principale.Main;
import fenetre.principale.jTable.MyTableModel;

public class MlActionBouton implements ActionListener {

	private final JTable table;

	public MlActionBouton(JTable p_table) {
		this.table = p_table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(EnActionMain.SUPPRIMER.getLib())) {
			int selectedLine = table.getSelectedRow();

			Integer idMessage = (Integer) table.getModel().getValueAt(
					selectedLine, 0);

			REPONSE reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Suppression de message",
					"Voulez vous vraiment supprimer ce message?");
			if (reponse == REPONSE.OUI) {
				BDRequette.deleteMessageRecu(idMessage);
				TreePath treePath = Main.getTreePath();
				String dossierChoisi = (String) treePath.getLastPathComponent();

				if (!BDRequette.getListeDeComptes().contains(dossierChoisi)) {
					String idCompte = BDRequette.getIdComptes(Main
							.getNomCompte());
					String idDossierChoisi = BDRequette.getIdDossier(
							dossierChoisi, idCompte);
					MlListeMessage listeMessage = BDRequette.getListeDeMessage(
							idCompte, idDossierChoisi);

					MyTableModel modelDetable = (MyTableModel) table.getModel();
					modelDetable.valorisetable(listeMessage);

				}

			}

		}

	}
}
