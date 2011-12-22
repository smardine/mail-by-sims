package fenetre.principale.MlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;

import releve.imap.util.messageUtilisateur;
import thread.Thread_Releve;
import thread.thread_Import;
import tools.GestionRepertoire;
import tools.Historique;
import tools.OpenWithDefaultViewer;
import bdd.accesTable.AccesTableCompte;
import fenetre.Patience;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionMain implements ActionListener {

	private JTree tree;
	private final Patience fenetre;

	public MlActionMain() {
		fenetre = new Patience("Import de mails");
	}

	public MlActionMain(JTree p_tree) {
		this.tree = p_tree;
		fenetre = new Patience("Import de mails");

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
			messageUtilisateur.affMessageInfo("not yet implemented");

		}
		if (e.getActionCommand().equals(EnActionMain.IMPORTER.getLib())) {
			thread_Import t = new thread.thread_Import(tree, fenetre);
			t.start();
		}

		if (e.getActionCommand().equals(EnActionMain.RECEVOIR.getLib())) {
			AccesTableCompte accesCompte = new AccesTableCompte();
			Thread_Releve t = new Thread_Releve(accesCompte.getListeDeComptes());
			t.start();

		}

	}
}
