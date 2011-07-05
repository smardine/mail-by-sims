package fenetre.principale.MlAction;

import importMail.thread_Import;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.JTree;

import tools.GestionRepertoire;
import tools.Historique;
import tools.OpenWithDefaultViewer;
import fenetre.ReleveMessagerie;
import fenetre.comptes.gestion.GestionCompte;
import fenetre.principale.jTable.MyTableModel;

public class MlActionMain implements ActionListener {
	private MyTableModel model;
	private Window fenetre;
	private JTree tree;
	private JProgressBar progress;

	public MlActionMain() {

	}

	public MlActionMain(Window p_fenetre) {
		this.fenetre = p_fenetre;
	}

	public MlActionMain(MyTableModel p_tableModel, JProgressBar p_progress) {
		this.model = p_tableModel;
		this.progress = p_progress;
	}

	public MlActionMain(JTree p_tree, JProgressBar p_progress) {
		this.tree = p_tree;
		this.progress = p_progress;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(EnActionMain.QUITTER.getLib())) {
			System.exit(0);
		}
		if (e.getActionCommand().equals(EnActionMain.GESTION_COMPTE.getLib())) {
			fenetre.dispose();
			new GestionCompte();
		}
		if (e.getActionCommand().equals(EnActionMain.EXPLORER.getLib())) {
			String Explorer = GestionRepertoire.RecupRepTravail();
			OpenWithDefaultViewer.open(Explorer);

		}
		if (e.getActionCommand().equals(EnActionMain.HISTORIQUE.getLib())) {
			Historique.lire();
		}
		if (e.getActionCommand().equals(EnActionMain.CONTACT.getLib())) {

		}
		if (e.getActionCommand().equals(EnActionMain.IMPORTER.getLib())) {
			thread_Import t = new thread_Import(tree);
			t.start();
		}
		if (e.getActionCommand().equals(EnActionMain.RECEVOIR.getLib())) {
			new ReleveMessagerie();

		}

	}

}
