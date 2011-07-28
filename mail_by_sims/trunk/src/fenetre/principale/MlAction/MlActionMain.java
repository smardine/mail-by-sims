package fenetre.principale.MlAction;

import importMail.thread_Import;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;

import tools.GestionRepertoire;
import tools.Historique;
import tools.OpenWithDefaultViewer;
import fenetre.ReleveMessagerie;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionMain implements ActionListener {

	private Window fenetre;
	private JTree tree;

	public MlActionMain(Window p_fenetre) {
		this.fenetre = p_fenetre;
	}

	public MlActionMain() {

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
		if (e.getActionCommand().equals(EnActionMain.ENVOYER_RECEVOIR.getLib())) {
			new ReleveMessagerie(true);
		}
		if (e.getActionCommand().equals(EnActionMain.RECEVOIR.getLib())) {
			new ReleveMessagerie(false);
		}

	}

}
