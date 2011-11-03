package thread;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import bdd.BDRequette;
import fenetre.Patience;
import fenetre.principale.jTable.MlActionJtable;
import fenetre.principale.jTable.MyTableModel;
import fenetre.principale.jTable.jTableHelper;

public class thread_deplaceOuSuppr extends Thread {
	private final JTable table;
	private final int[] tabIdLigneSelectionnee;
	private final JList list;
	private final Patience fenetre;
	private final JTree tree;

	public thread_deplaceOuSuppr(JTree p_tree, JTable p_table, JList p_list,
			Patience p_fenetre, int[] p_tabIdLigneSelectionnee) {
		this.tree = p_tree;
		this.table = p_table;
		this.list = p_list;
		this.fenetre = p_fenetre;
		this.tabIdLigneSelectionnee = p_tabIdLigneSelectionnee;

	}

	@Override
	public void run() {
		fenetre.setVisible(true);
		lanceSuppressionOuDeplacementCorbeille(tabIdLigneSelectionnee);
		refreshJtreeAndJTable();
		fenetre.setVisible(false);

	}

	/**
	 * @param p_tabIdLigneSelectionnee
	 */
	private void lanceSuppressionOuDeplacementCorbeille(
			int[] p_tabIdLigneSelectionnee) {
		MlListeMessage lstASuppr = new MlListeMessage();
		MlListeMessage lstADepl = new MlListeMessage();

		int nbMessTraite = 1;
		for (int i = 0; i < p_tabIdLigneSelectionnee.length; i++) {
			fenetre.afficheInfo("Creation de la liste ...", "message "
					+ nbMessTraite + " sur " + p_tabIdLigneSelectionnee.length,
					(100 * nbMessTraite) / p_tabIdLigneSelectionnee.length);

			int selectedLine = p_tabIdLigneSelectionnee[i];
			Integer idMessage = jTableHelper.getReelIdMessage(table,
					selectedLine);

			MlMessage m = new MlMessage(idMessage);
			MlCompteMail cpt = new MlCompteMail(m.getIdCompte());

			if (m.getIdDossier() != cpt.getIdCorbeille()) {
				lstADepl.add(m);
				fenetre.afficheInfo("Creation de la liste ...", "message n° "
						+ nbMessTraite++ + " à deplacer vers la corbeille", 0);

			} else {
				lstASuppr.add(m);
				fenetre.afficheInfo("Creation de la liste ...", "message n° "
						+ nbMessTraite++ + " à supprimer", 0);

			}

		}// fin de for
		BDRequette bd = new BDRequette();
		if (lstADepl.size() > 0) {
			Thread_Releve t = new Thread_Releve(null);
			t.DeplaceMessageVersCorbeille(lstADepl);
			bd.deplaceMessageVersCorbeille(lstADepl);

		}
		if (lstASuppr.size() > 0) {
			Thread_Releve t = new Thread_Releve(null);
			t.SupprMessage(lstASuppr);
			// for (MlMessage m : lstASuppr) {
			// bd.deleteMessageRecu(m.getIdMessage());
			// }
		}
		bd.closeConnexion();
	}

	/**
	 * 
	 */
	private void refreshJtreeAndJTable() {
		TreePath treePath = tree.getSelectionPath();
		String dossierChoisi = (String) treePath.getLastPathComponent()
				.toString();
		BDRequette bd = new BDRequette();
		if (!bd.getListeDeComptes().contains(dossierChoisi)) {
			Object[] pathComplet = treePath.getPath();
			int idCompte = bd.getIdComptes(pathComplet[1].toString());
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
