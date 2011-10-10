package verification;

import javax.swing.JOptionPane;
import javax.swing.JTree;

import bdd.accestable.compte.AccesTableCompte;
import fenetre.comptes.gestion.GestionCompte;

public class Thread_Verif extends Thread {

	private final JTree jTree;
	private final AccesTableCompte accesCompte;

	public Thread_Verif(JTree p_jTree) {
		this.jTree = p_jTree;
		accesCompte = new AccesTableCompte();
	}

	@Override
	public void run() {

		int nbRecord = accesCompte.getNbCompte();

		if (nbRecord == 0) {
			final int creerCompte = JOptionPane.showConfirmDialog(null,
					"Aucun comptes n'est actuellement configuré\n\r"
							+ "Voulez-vous en ajouter un maintenant?",
					"Information", JOptionPane.YES_NO_OPTION);
			if (creerCompte == 0) {// on accepte
				new GestionCompte(jTree);
			}// sinon, on ne fait rien
		}

	}

}
