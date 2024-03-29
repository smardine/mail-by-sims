package fenetre.principale.MlAction;

import imap.thread_SynchroImap;
import importMail.thread_Import;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

import mdl.MlListeCompteMail;
import tools.GestionRepertoire;
import tools.Historique;
import tools.OpenWithDefaultViewer;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionMain implements ActionListener {

	private JTree tree;
	private JProgressBar pbReleve;
	private JProgressBar pbPieceJointe;
	private JTextArea text;
	private JScrollPane scrollPane;
	private MlListeCompteMail listeCompteMail;

	public MlActionMain(JTree p_treeCompte) {

		this.tree = p_treeCompte;
	}

	public MlActionMain() {

	}

	public MlActionMain(MlListeCompteMail p_listeCompte, JTree p_tree,
			JProgressBar p_jProgressBarReleve,
			JProgressBar p_jProgressBarPieceJointe, JTextArea p_jTextArea,
			JScrollPane p_scroll) {
		this.listeCompteMail = p_listeCompte;
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

		}
		if (e.getActionCommand().equals(EnActionMain.IMPORTER.getLib())) {
			thread_Import t = new importMail.thread_Import(tree, pbReleve,
					pbPieceJointe, text, scrollPane);
			t.start();
		}
		if (e.getActionCommand().equals(EnActionMain.ENVOYER_RECEVOIR.getLib())) {
			// RequetteFactory bd = new RequetteFactory();

			thread_SynchroImap t = new thread_SynchroImap(pbReleve,
					pbPieceJointe, text, scrollPane, true, listeCompteMail);
			t.start();
			// bd.closeConnexion();

		}
		if (e.getActionCommand().equals(EnActionMain.RECEVOIR.getLib())) {
			// RequetteFactory bd = new RequetteFactory();
			thread_SynchroImap t = new thread_SynchroImap(pbReleve,
					pbPieceJointe, text, scrollPane, false, listeCompteMail);
			t.start();
			// bd.closeConnexion();
		}

	}
}
