package fenetre.principale.jtree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.mlcomptemail.MlCompteMail;
import mdl.mldossier.MlDossier;
import mdl.mlmessage.MlListeMessageGrille;
import releve.imap.util.REPONSE;
import releve.imap.util.messageUtilisateur;
import thread.ThreadSupprimeMessage;
import bdd.accesTable.AccesTableDossier;
import factory.DossierFactory;
import factory.JTableFactory;
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
			DefaultMutableTreeNode nodeParent = (DefaultMutableTreeNode) selectionPath
					.getLastPathComponent();
			if (nodeParent.getUserObject() instanceof MlDossier) {
				MlDossier dossierParent = (MlDossier) nodeParent
						.getUserObject();
				AccesTableDossier accesDossier = new AccesTableDossier();
				if (accesDossier.createNewDossier(dossierParent.getIdCompte(),
						dossierParent.getIdDossier(), nomNewDossier, "")) {
					MlDossier aDossier = new MlDossier(accesDossier
							.getIdDossier(nomNewDossier, dossierParent
									.getIdCompte()));

					JTreeFactory treeFact = new JTreeFactory();
					treeFact.ajouteNode(selectionPath, nodeParent,
							new DefaultMutableTreeNode(aDossier));
					//					
					tree.setSelectionPath(selectionPath);

					ComposantVisuelCommun.setTree(tree);
					treeFact.refreshJTree();

				}

			}

		} else if ("SUPPRIMER".equals(actionCommand)) {
			// Object[] pathComplet = selectionPath.getPath();
			JTreeFactory treeFact = new JTreeFactory();
			final DefaultMutableTreeNode dossierNode = (DefaultMutableTreeNode) selectionPath
					.getLastPathComponent();
			MlDossier dossier = (MlDossier) dossierNode.getUserObject();
			DefaultMutableTreeNode cptNode = treeFact
					.rechercheCompteNode(dossier.getIdCompte());

			DossierFactory dossierFact = new DossierFactory(
					(MlCompteMail) cptNode.getUserObject());
			dossierFact.deleteDossier(dossier);

		} else if ("VIDER".equals(actionCommand)) {
			REPONSE reponse = messageUtilisateur.affMessageQuestionOuiNon(
					"Vider la corbeille",
					"Etes-vous sûr de vouloir vider la corbeille?");
			switch (reponse) {
				case OUI:
					JTreeFactory treeFactory = new JTreeFactory();
					DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) selectionPath
							.getLastPathComponent();
					MlDossier dossierAVider = (MlDossier) aNode.getUserObject();

					MlCompteMail cpt = treeFactory
							.rechercheCompteMail(dossierAVider.getIdCompte());

					ThreadSupprimeMessage t = new ThreadSupprimeMessage(
							dossierAVider.getListMessageGrille(), cpt);
					t.start();
					JTableFactory tableFact = new JTableFactory();
					tableFact.refreshJTable(new MlListeMessageGrille());
					break;
				case NON:
					break;
			}

		}

	}
}
