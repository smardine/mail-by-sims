package fenetre.principale.MlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

import releve.imap.util.messageUtilisateur;
import thread.Thread_Releve;
import thread.thread_Import;
import tools.GestionRepertoire;
import tools.Historique;
import tools.OpenWithDefaultViewer;
import bdd.BDRequette;
import fenetre.Patience;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionMain implements ActionListener {

	private JTree tree;
	private JProgressBar pbReleve;
	private JProgressBar pbPieceJointe;
	private JTextArea text;
	private JScrollPane scrollPane;

	public MlActionMain(JTree p_treeCompte) {

		this.tree = p_treeCompte;
	}

	public MlActionMain() {

	}

	public MlActionMain(JTree p_tree, JProgressBar p_jProgressBarReleve,
			JProgressBar p_jProgressBarPieceJointe, JTextArea p_jTextArea,
			JScrollPane p_scroll) {
		this.tree = p_tree;
		this.pbReleve = p_jProgressBarReleve;
		this.pbPieceJointe = p_jProgressBarPieceJointe;
		this.text = p_jTextArea;
		this.scrollPane = p_scroll;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(EnActionMain.QUITTER.getLib())) {
			System.exit(0);
		}
		if (e.getActionCommand().equals(EnActionMain.GESTION_COMPTE.getLib())) {
			new GestionCompte(tree);
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
			thread_Import t = new thread.thread_Import(tree, new Patience(
					"Import de mails"));
			t.start();
		}

		if (e.getActionCommand().equals(EnActionMain.RECEVOIR.getLib())) {
			BDRequette bd = new BDRequette();
			Thread_Releve t = new Thread_Releve(bd.getListeDeComptes());
			t.start();
			bd.closeConnexion();
		}

	}
}
