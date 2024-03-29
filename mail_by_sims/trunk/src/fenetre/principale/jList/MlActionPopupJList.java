package fenetre.principale.jList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTable;

import releve.imap.util.messageUtilisateur;
import tools.ManipFichier;
import tools.OpenWithDefaultViewer;
import bdd.accesTable.AccesTablePieceJointe;
import fenetre.principale.MlAction.EnActionMain;
import fenetre.principale.jTable.jTableHelper;

public class MlActionPopupJList implements ActionListener {

	private final JTable table;
	private final JList list;

	public MlActionPopupJList(JTable p_table, JList p_list) {
		table = p_table;
		list = p_list;
	}

	@Override
	public void actionPerformed(ActionEvent p_e) {
		String actionCommand = p_e.getActionCommand();

		EnActionMain actionMain = EnActionMain.getEnumFromLib(actionCommand);
		switch (actionMain) {
			case OUVRIR_PJ:
				if (verifieSlection()) {
					traiteOuvrirPJ();
				}

				break;
			case ENREGISTRER_PJ:
				if (verifieSlection()) {
					traiteEnregistrerPJ();
				}
				break;
			case TOUT_ENREGISTRER_PJ:
				traiteToutEnregistrerPJ();
				break;

			default:
				break;
		}
	}

	private boolean verifieSlection() {
		if (list.getSelectedValue() == null) {
			messageUtilisateur
					.affMessageInfo("Merci de selectionner un fichier dans la liste ");
			return false;
		}

		return true;
	}

	private void traiteToutEnregistrerPJ() {
		int idMessage = jTableHelper.getReelIdMessage(table, table
				.getSelectedRow());
		AccesTablePieceJointe accesPJ = new AccesTablePieceJointe();
		List<String> lstPJ = accesPJ.getListeNomPieceJointe(idMessage);
		String emplacementEnregistrement = ManipFichier.OpenFolder();

		for (String unePJ : lstPJ) {
			File fichier = accesPJ
					.getPieceJointeFromIDMessage(idMessage, unePJ);
			File emplacementFichier = new File(emplacementEnregistrement + "/"
					+ unePJ);
			ManipFichier.deplacer(fichier, emplacementFichier);
		}

	}

	private void traiteEnregistrerPJ() {
		int idMessage = jTableHelper.getReelIdMessage(table, table
				.getSelectedRow());
		String nomPieceJointe = (String) list.getSelectedValue();
		AccesTablePieceJointe accesPJ = new AccesTablePieceJointe();
		File fichier = accesPJ.getPieceJointeFromIDMessage(idMessage,
				nomPieceJointe);

		File emplacementFichier = new File(ManipFichier.SaveFile(fichier
				.getName()));
		ManipFichier.deplacer(fichier, emplacementFichier);

	}

	protected void traiteOuvrirPJ() {
		int idMessage = jTableHelper.getReelIdMessage(table, table
				.getSelectedRow());
		String nomPieceJointe = (String) list.getSelectedValue();

		AccesTablePieceJointe accesPJ = new AccesTablePieceJointe();
		File fichier = accesPJ.getPieceJointeFromIDMessage(idMessage,
				nomPieceJointe);

		OpenWithDefaultViewer.open(fichier.getAbsolutePath());

	}

}
