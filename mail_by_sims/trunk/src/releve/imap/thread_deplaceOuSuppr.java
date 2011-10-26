package releve.imap;

import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.thread_SynchroImap;
import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
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

	public thread_deplaceOuSuppr(JTable p_table, JList p_list,
			JProgressBar p_progress, JTextArea p_text, JScrollPane p_scroll,
			int[] p_tabIdLigneSelectionnee) {
		this.table = p_table;
		this.list = p_list;
		this.progressBar = p_progress;
		this.textArea = p_text;
		this.scroll = p_scroll;
		this.tabIdLigneSelectionnee = p_tabIdLigneSelectionnee;

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

		int nbMessTraite = 1;
		for (int i = 0; i < p_tabIdLigneSelectionnee.length; i++) {
			progressBar.setValue((100 * nbMessTraite)
					/ p_tabIdLigneSelectionnee.length);
			progressBar.setString("message " + nbMessTraite + " sur "
					+ p_tabIdLigneSelectionnee.length);

			int selectedLine = p_tabIdLigneSelectionnee[i];
			Integer idMessage = jTableHelper.getReelIdMessage(table,
					selectedLine);

			MlMessage m = new MlMessage(idMessage);
			MlCompteMail cpt = new MlCompteMail(m.getIdCompte());

			if (m.getIdDossier() != cpt.getIdCorbeille()) {
				lstADepl.add(m);
				messageUtilisateur.afficheText(textArea, "message° "
						+ nbMessTraite++ + " à deplacer vers la corbeille");

			} else {
				lstASuppr.add(m);
				messageUtilisateur.afficheText(textArea, "message° "
						+ nbMessTraite++ + " à supprimer");
			}

		}// fin de for
		BDRequette bd = new BDRequette();
		if (lstADepl.size() > 0) {
			messageUtilisateur.afficheText(textArea,
					"Déplacement du ou des messages vers la corbeille");
			thread_SynchroImap t = new thread_SynchroImap(progressBar,
					progressBar, textArea, scroll, false, null);
			t.DeplaceMessageVersCorbeille(lstADepl);
			bd.deplaceMessageVersCorbeille(lstADepl);

		}
		if (lstASuppr.size() > 0) {
			messageUtilisateur.afficheText(textArea,
					"Supression du ou des messages");
			thread_SynchroImap t = new thread_SynchroImap(progressBar,
					progressBar, textArea, scroll, false, null);
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
		TreePath treePath = ComposantVisuelCommun.getTreePath();
		String dossierChoisi = (String) treePath.getLastPathComponent();
		BDRequette bd = new BDRequette();
		if (!bd.getListeDeComptes().contains(dossierChoisi)) {
			int idCompte = bd
					.getIdComptes(ComposantVisuelCommun.getNomCompte());
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
