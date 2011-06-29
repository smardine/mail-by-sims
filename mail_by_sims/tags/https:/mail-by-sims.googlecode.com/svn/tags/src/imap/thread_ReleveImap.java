package imap;

import java.util.ArrayList;

import bdd.BDRequette;
import fenetre.principale.jTable.MyTableModel;

public class thread_ReleveImap extends Thread {

	private final MyTableModel model;

	// private final JEditorPane pane;

	public thread_ReleveImap(MyTableModel p_tableModel) {// , JEditorPane
		// editor) {
		this.model = p_tableModel;
		// this.pane = editor;
	}

	@Override
	public void run() {

		ArrayList<String> lst = BDRequette.getListeDeComptes();

		for (String s : lst) {
			String idCpt = BDRequette.getIdComptes(s);
			String user = BDRequette.getUserFromIdCompte(idCpt);
			String pass = BDRequette.getPasswordFromIdCompte(idCpt);
			String serveur = BDRequette.getHostFromIdCompte(idCpt);
			if (serveur.contains("pop")) {
				System.out.println("not yet implemented");
				// new ClientMail(user, pass, serveur);
			} else {
				new imap(model, idCpt, user, pass, serveur, true);
			}

		}

	}

}
