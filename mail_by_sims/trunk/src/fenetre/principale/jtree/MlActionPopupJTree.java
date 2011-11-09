package fenetre.principale.jtree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlDossier;
import releve.imap.util.REPONSE;
import releve.imap.util.messageUtilisateur;
import thread.ThreadSupprimeMessage;
import bdd.accesTable.AccesTableCompte;
import bdd.accesTable.AccesTableDossier;
import factory.DossierFactory;
import factory.JTreeFactory;

public class MlActionPopupJTree implements ActionListener {

	private final JTree tree;

	public MlActionPopupJTree(JTree p_jTree) {
		this.tree = p_jTree;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// pour avoir la selection courante du jtree
		// on peut en deduire le compte, donc l'id Dssier correspondant

		String actionCommand = arg0.getActionCommand();
		TreePath selectionPath = tree.getSelectionPath();

		if ("AJOUTER".equals(actionCommand)) {
			String nomNewDossier = messageUtilisateur
					.affMessageInput("Quel est le nom du dossier?");
			if (null == nomNewDossier || nomNewDossier.equals("")) {
				return;
			}
			DefaultMutableTreeNode dossierParent = (DefaultMutableTreeNode) selectionPath
					.getLastPathComponent();
			AccesTableCompte accesCompte = new AccesTableCompte();
			int idCompte = accesCompte.getIdComptes((String) selectionPath
					.getPathComponent(1).toString());
			AccesTableDossier accesDossier = new AccesTableDossier();
			int idDossierParent = accesDossier.getIdDossier(dossierParent
					.toString(), idCompte);

			if ("".equals(idDossierParent)) {
				idDossierParent = 0;
			}
			if (accesDossier.createNewDossier(idCompte, idDossierParent,
					nomNewDossier, "")) {
				MlDossier aDossier = new MlDossier(accesDossier.getIdDossier(
						nomNewDossier, idCompte));
				TreePath newTreePath = selectionPath
						.pathByAddingChild(new DefaultMutableTreeNode(aDossier));
				JTreeFactory treeFact = new JTreeFactory();
				treeFact.ajouteNode(selectionPath,
						(DefaultMutableTreeNode) selectionPath
								.getLastPathComponent(),
						(DefaultMutableTreeNode) newTreePath
								.getLastPathComponent());
				tree.setSelectionPath(newTreePath);
				ComposantVisuelCommun.setTree(tree);

			}

		} else if ("SUPPRIMER".equals(actionCommand)) {
			Object[] pathComplet = selectionPath.getPath();
			DossierFactory dossierFact = new DossierFactory(new MlCompteMail(
					pathComplet[1].toString()));
			dossierFact.deleteDossier(selectionPath);

		} else if ("VIDER".equals(actionCommand)) {
			REPONSE reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Vider la corbeille",
					"Etes-vous sûr de vouloir vider la corbeille?");
			switch (reponse) {
				case OUI:
					DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) selectionPath
							.getLastPathComponent();
					MlDossier aDossier = (MlDossier) aNode.getUserObject();

					ThreadSupprimeMessage t = new ThreadSupprimeMessage(
							aDossier.getListMessage());
					t.start();
					break;
				case NON:
					break;
			}

		}

	}
}
