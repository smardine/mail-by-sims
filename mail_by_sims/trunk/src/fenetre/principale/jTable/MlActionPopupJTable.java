package fenetre.principale.jTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;

import mdl.MlCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.imap.util.REPONSE;
import releve.imap.util.messageUtilisateur;
import thread.ThreadDeplaceMessage;
import thread.ThreadSupprimeMessage;
import bdd.BDRequette;
import factory.JTableFactory;
import factory.JTreeFactory;
import fenetre.Patience;
import fenetre.principale.MlAction.EnActionMain;

public class MlActionPopupJTable implements ActionListener {

	private final JTable table;

	private final Patience fenetre;

	public MlActionPopupJTable(JTable p_table) {
		this.table = p_table;
		this.fenetre = new Patience("");

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();

		EnActionMain actionMain = EnActionMain.getEnumFromLib(actionCommand);
		switch (actionMain) {
			case SUPPRIMER:
				traiteSupprimer();
				break;
			case CREER_REGLE:
				traiteCreerRegle();
				break;
			case MARQUER_SPAM:
				traiteMarquerSpam();
				break;
			case MARQUER_LU:
				traiteMarquerLu();
				break;
			default:
				break;
		}

	}

	/**
	 * 
	 */
	private void traiteMarquerLu() {
		int[] tabIdLigneSelectionnee = table.getSelectedRows();
		if (tabIdLigneSelectionnee.length == 0) {
			messageUtilisateur
					.affMessageInfo("Merci de d'abord selectionner un message  à supprimer");
			return;
		}
		lanceMarquageLu(tabIdLigneSelectionnee);

	}

	/**
	 * @param p_tabIdLigneSelectionnee
	 */
	private void lanceMarquageLu(int[] p_tabIdLigneSelectionnee) {
		BDRequette bd = new BDRequette();
		MlListeMessage lst = new MlListeMessage();
		int idDossier = -1;
		int idCompte = -1;
		for (int i = 0; i < p_tabIdLigneSelectionnee.length; i++) {

			int selectedLine = p_tabIdLigneSelectionnee[i];
			Integer idMessage = jTableHelper.getReelIdMessage(table,
					selectedLine);

			MlMessage m = new MlMessage(idMessage);
			idDossier = m.getIdDossier();
			idCompte = m.getIdCompte();
			lst.add(m);

		}// fin de for

		for (MlMessage m : lst) {
			m.setStatuLecture(true);
			bd.updateStatusLecture(m.getIdMessage(), true);
		}

		MlListeMessage lstDossier = bd.getListeDeMessage(idCompte, idDossier);
		bd.closeConnexion();
		JTreeFactory treeFact = new JTreeFactory();
		treeFact.refreshJTree();
		JTableFactory tableFact = new JTableFactory();
		tableFact.refreshJTable(lstDossier);
	}

	private void traiteCreerRegle() {

	}

	private void traiteMarquerSpam() {
		ArrayList<String> lst = new ArrayList<String>();
		int idRow = table.getSelectedRow();

		// Integer idMessage = (Integer) table.getModel().getValueAt(idRow, 0);
		// le n° du message (meme si il est caché).
		// Date dateReception = (Date) table.getModel().getValueAt(
		// idRow, 1);// la date de reception

		// Integer idMessage = getReelIdMessage(idRow);

		String expediteur = (String) table.getModel().getValueAt(idRow, 2);// l'expediteur

		// String sujet = (String) table.getModel()
		// .getValueAt(idRow, 3);// le

		lst.add("traiter l'expediteur " + expediteur + " comme indésirable");
		lst.add("Traiter le nom de dommaine "
				+ expediteur.substring(expediteur.lastIndexOf('@'))
				+ " comme indésirable");
		String choix = messageUtilisateur.afficheChoixMultiple(
				"Courrier indésirable", "Que voulez-vous faire", lst);

		if (("traiter l'expediteur " + expediteur + " comme indésirable")
				.equals(choix)) {
			messageUtilisateur.affMessageInfo("Not yet implemented");

		} else if (("Traiter le nom de dommaine "
				+ expediteur.substring(expediteur.lastIndexOf('@')) + " comme indésirable")
				.equals(choix)) {
			messageUtilisateur.affMessageInfo("Not yet implemented");
		}

	}

	/**
	 * 
	 */
	private void traiteSupprimer() {
		int[] tabIdLigneSelectionnee = table.getSelectedRows();
		REPONSE reponse = REPONSE.NON;
		if (tabIdLigneSelectionnee.length == 0) {
			messageUtilisateur
					.affMessageInfo("Merci de d'abord selectionner un message  à supprimer");
		} else if (tabIdLigneSelectionnee.length == 1) {
			reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Suppression de message",
					"Voulez vous vraiment supprimer ce message?");
		} else if (tabIdLigneSelectionnee.length > 1) {
			reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Suppression de message",
					"Voulez vous vraiment supprimer tous ces messages?");
		}

		if (reponse == REPONSE.OUI) {
			lanceSuppressionOuDeplacementCorbeille(tabIdLigneSelectionnee);
		}
	}

	/**
	 * @param p_tabIdLigneSelectionnee
	 */
	private void lanceSuppressionOuDeplacementCorbeille(
			int[] p_tabIdLigneSelectionnee) {
		MlListeMessage lstASuppr = new MlListeMessage();
		MlListeMessage lstADepl = new MlListeMessage();
		fenetre.setVisible(true);
		int nbMessTraite = 1;
		for (int i = 0; i < p_tabIdLigneSelectionnee.length; i++) {
			fenetre.afficheInfo("Creation de la liste ...", "message "
					+ nbMessTraite + " sur " + p_tabIdLigneSelectionnee.length,
					(100 * nbMessTraite) / p_tabIdLigneSelectionnee.length);

			int selectedLine = p_tabIdLigneSelectionnee[i];
			Integer idMessage = jTableHelper.getReelIdMessage(table,
					selectedLine);

			MlMessage m = new MlMessage(idMessage);
			MlCompteMail cpt = new MlCompteMail(m.getIdCompte());

			if (m.getIdDossier() != cpt.getIdCorbeille()) {
				lstADepl.add(m);
				fenetre.afficheInfo("Creation de la liste ...", "message n° "
						+ nbMessTraite++ + " à deplacer vers la corbeille", 0);

			} else {
				lstASuppr.add(m);
				fenetre.afficheInfo("Creation de la liste ...", "message n° "
						+ nbMessTraite++ + " à supprimer", 0);

			}

		}// fin de for
		fenetre.setVisible(false);

		if (lstADepl.size() > 0) {
			ThreadDeplaceMessage t = new ThreadDeplaceMessage(lstADepl);
			t.start();
		}
		if (lstASuppr.size() > 0) {
			ThreadSupprimeMessage t = new ThreadSupprimeMessage(lstASuppr);
			t.start();
		}

	}

}
