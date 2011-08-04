package fenetre.principale.jTable;

import imap.thread_SynchroImap;
import imap.util.REPONSE;
import imap.util.messageUtilisateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.tree.TreePath;

import mdl.MlListeMessage;
import mdl.MlMessage;
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
			if (reponse == REPONSE.OUI) {
				MlListeMessage lstASuppr = new MlListeMessage();
				MlListeMessage lstADepl = new MlListeMessage();
				BDRequette bd = new BDRequette();

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

					boolean isDeplacementVersCorbeille = bd
							.verifieNecessiteDeplacementCorbeille(idMessage);
					if (isDeplacementVersCorbeille) {
						lstADepl.add(bd.getMessageById(idMessage));

					} else {
						lstASuppr.add(bd.getMessageById(idMessage));

					}

				}// fin de for
				if (lstADepl.size() > 0) {
					thread_SynchroImap.DeplaceMessage(lstADepl);
					bd.deplaceMessageVersCorbeille(lstADepl);

				}
				if (lstASuppr.size() > 0) {
					thread_SynchroImap.SupprMessage(lstASuppr);
					for (MlMessage m : lstASuppr) {
						bd.deleteMessageRecu(m.getIdMessage());
					}
				}
			}

			TreePath treePath = Main.getTreePath();
			String dossierChoisi = (String) treePath.getLastPathComponent();
			BDRequette bd = new BDRequette();
			if (!bd.getListeDeComptes().contains(dossierChoisi)) {
				int idCompte = bd.getIdComptes(Main.getNomCompte());
				int idDossierChoisi = bd.getIdDossier(dossierChoisi, idCompte);
				MlListeMessage listeMessage = bd.getListeDeMessage(idCompte,
						idDossierChoisi);

				MyTableModel modelDetable = (MyTableModel) table.getModel();
				modelDetable.valorisetable(listeMessage);
				bd.closeConnexion();

			}
			if (table.getRowCount() > 0) {
				table.setRowSelectionInterval(0, 0);
			}

			MlActionJtable.afficheContenuMail(table, list);
		}

	}
}
