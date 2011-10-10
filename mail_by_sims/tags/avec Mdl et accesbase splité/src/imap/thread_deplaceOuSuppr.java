package imap;

import imap.util.methodeImap;

import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.tree.TreePath;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import bdd.accestable.compte.AccesTableCompte;
import bdd.accestable.dossier.AccesTableDossier;
import bdd.accestable.mail_recu.AccesTableMailRecu;
import fenetre.principale.Main;
import fenetre.principale.jTable.MlActionJtable;
import fenetre.principale.jTable.MyTableModel;
import fenetre.principale.jTable.jTableHelper;

public class thread_deplaceOuSuppr extends Thread {
	private final JProgressBar progressBar;
	private final JTextArea textArea;
	private final JScrollPane scroll;
	private final JTable table;
	private final int[] tabIdLigneSelectionnee;
	private final JList list;
	private final AccesTableMailRecu accesMail;
	private final AccesTableDossier accesDossier;
	private final AccesTableCompte accesCompte;

	public thread_deplaceOuSuppr(JTable p_table, JList p_list,
			JProgressBar p_progress, JTextArea p_text, JScrollPane p_scroll,
			int[] p_tabIdLigneSelectionnee) {
		this.table = p_table;
		this.list = p_list;
		this.progressBar = p_progress;
		this.textArea = p_text;
		this.scroll = p_scroll;
		this.tabIdLigneSelectionnee = p_tabIdLigneSelectionnee;
		accesDossier = new AccesTableDossier();
		accesMail = new AccesTableMailRecu();
		accesCompte = new AccesTableCompte();

	}

	@Override
	public void run() {
		progressBar.setValue(0);
		progressBar.setString("");
		textArea.setText("");
		scroll.setVisible(true);
		progressBar.setVisible(true);
		textArea.setVisible(true);

		lanceSuppressionOuDeplacementCorbeille(tabIdLigneSelectionnee);

		scroll.setVisible(false);
		progressBar.setVisible(false);
		textArea.setVisible(false);
		progressBar.setValue(0);
		progressBar.setString("");
		textArea.setText("");

		refreshJtreeAndJTable();

	}

	/**
	 * @param p_tabIdLigneSelectionnee
	 */
	private void lanceSuppressionOuDeplacementCorbeille(
			int[] p_tabIdLigneSelectionnee) {
		MlListeMessage lstASuppr = new MlListeMessage();
		MlListeMessage lstADepl = new MlListeMessage();
		MlCompteMail cpt = null;

		int nbMessTraite = 1;
		for (int i = 0; i < p_tabIdLigneSelectionnee.length; i++) {
			progressBar.setValue((100 * nbMessTraite)
					/ p_tabIdLigneSelectionnee.length);
			progressBar.setString("message " + nbMessTraite + " sur "
					+ p_tabIdLigneSelectionnee.length);

			int selectedLine = p_tabIdLigneSelectionnee[i];
			Integer idMessage = jTableHelper.getReelIdMessage(table,
					selectedLine);
			// int row = table.convertRowIndexToModel(selectedLine);
			//
			// Integer idMessage = (Integer) table.getModel().getValueAt(row,
			// 0);
			// le n° du message (meme si il est caché).
			// Date dateReception = (Date) table.getModel().getValueAt(
			// selectedLine, 1);// la date de reception

			// String expediteur = (String) table.getModel().getValueAt(
			// selectedLine, 2);// l'expediteur

			// String sujet = (String) table.getModel()
			// .getValueAt(selectedLine, 3);// le
			MlMessage m = new MlMessage(idMessage);
			cpt = new MlCompteMail(m.getIdCompte());
			// boolean isDeplacementVersCorbeille = bd
			// .verifieNecessiteDeplacementCorbeille(idMessage);
			if (m.getIdDossier() != cpt.getIdCorbeille()) {
				lstADepl.add(m);
				methodeImap.afficheText(textArea, "message° " + nbMessTraite++
						+ " à deplacer vers la corbeille");

			} else {
				lstASuppr.add(m);
				methodeImap.afficheText(textArea, "message° " + nbMessTraite++
						+ " à supprimer");
			}

		}// fin de for

		if (lstADepl.size() > 0) {
			methodeImap.afficheText(textArea,
					"Déplacement du ou des messages vers la corbeille");
			thread_SynchroImap t = new thread_SynchroImap(progressBar,
					progressBar, textArea, scroll, false, cpt);
			t.DeplaceMessageVersCorbeille(lstADepl);
			accesMail.deplaceMessageVersCorbeille(lstADepl);

		}
		if (lstASuppr.size() > 0) {
			methodeImap.afficheText(textArea, "Supression du ou des messages");
			thread_SynchroImap t = new thread_SynchroImap(progressBar,
					progressBar, textArea, scroll, false, cpt);
			t.SupprMessage(lstASuppr);
			for (MlMessage m : lstASuppr) {
				accesMail.deleteMessageRecu(m.getIdMessage());
			}
		}

	}

	/**
	 * 
	 */
	private void refreshJtreeAndJTable() {
		TreePath treePath = Main.getTreePath();
		String dossierChoisi = (String) treePath.getLastPathComponent();

		if (!accesCompte.getListeDeComptes().contains(dossierChoisi)) {
			int idCompte = accesCompte.getIdComptes(Main.getNomCompte());
			int idDossierChoisi = accesDossier.getIdDossier(dossierChoisi,
					idCompte);
			MlListeMessage listeMessage = new MlListeMessage(idCompte,
					idDossierChoisi);

			MyTableModel modelDetable = (MyTableModel) table.getModel();
			modelDetable.valorisetable(listeMessage);

		}
		if (table.getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
		}

		MlActionJtable.afficheContenuMail(table, list);
	}

}
