package fenetre.principale.MlAction;

import imap.thread_ReleveImap;
import importMail.thread_Import;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JTree;

import tools.GestionRepertoire;
import tools.Historique;
import tools.OpenWithDefaultViewer;
import fenetre.comptes.gestion.GestionCompte;
import fenetre.principale.jTable.MyTableModel;

public class MlActionMain implements ActionListener {
	private MyTableModel model;
	private JEditorPane editor;
	private Window fenetre;
	private JTree tree;

	public MlActionMain() {

	}

	public MlActionMain(Window p_fenetre) {
		this.fenetre = p_fenetre;
	}

	public MlActionMain(MyTableModel p_tableModel, JEditorPane jEditorPane) {
		this.model = p_tableModel;
		this.editor = jEditorPane;
	}

	public MlActionMain(JTree p_tree) {
		this.tree = p_tree;
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
			thread_ReleveImap t = new thread_ReleveImap(model);
			t.start();
		}

	}

}
