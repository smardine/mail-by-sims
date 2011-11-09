package thread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import mdl.MlListeMessage;
import mdl.MlMessage;
import releve.imap.util.messageUtilisateur;
import tools.GestionRepertoire;
import tools.Historique;
import tools.ReadFile;
import bdd.accesTable.AccesTableCompte;
import bdd.accesTable.AccesTableDossier;
import factory.DossierFactory;
import factory.JTreeFactory;
import factory.MessageFactory;
import fenetre.Patience;
import fenetre.comptes.EnDossierBase;

public class thread_Import extends Thread {
	private final JTree tree;
	private int idCompte;
	private String nomDossier;
	private TreePath newTp;
	private final Patience fenetre;

	public thread_Import(JTree p_tree, Patience p_fenetre) {
		this.tree = p_tree;
		this.fenetre = p_fenetre;

	}

	@Override
	public void run() {
		fenetre.setVisible(true);
		AccesTableCompte accesCompte = new AccesTableCompte();
		String chemin = GestionRepertoire
				.OpenFolder("Veuillez indiquer l'emplacement de vos mails Windows Mail");
		// messageUtilisateur.affMessageInfo("Vous avez choisi: " + chemin);
		MlListeCompteMail lst = accesCompte.getListeDeComptes();
		ArrayList<String> lstNomCompte = new ArrayList<String>(lst.getSize());
		for (MlCompteMail cpt : lst) {
			lstNomCompte.add(cpt.getNomCompte());
		}
		String choixCompte = messageUtilisateur.afficheChoixMultiple(
				"Choix du compte",
				"dans quel comptes souhaitez vous importer les messages?",
				lstNomCompte);

		// parcour du repertoire de facon recursive
		// les fichiers avec l'extension ".fol"contienne le nom du repertoire
		Historique.ecrire("choix du compte: " + choixCompte);
		idCompte = accesCompte.getIdComptes(choixCompte);

		Object[] path = new Object[3];
		path[0] = tree.getModel().getRoot();
		path[1] = choixCompte;
		path[2] = EnDossierBase.RECEPTION.getLib();

		TreePath treePathInitial = new TreePath(path);
		// ComposantVisuelCommun.setNomCompte(choixCompte);
		// ComposantVisuelCommun.setTreePath(treePathInitial);
		ComposantVisuelCommun.setTree(tree);
		// tree.setSelectionPath(treePathInitial);

		MlListeMessage listeDeMessage = parcoursDossier(chemin, choixCompte,
				treePathInitial, fenetre);
		// messageUtilisateur.affMessageInfo("il y a au total "
		// + listeDeMessage.size() + " message(s)");
		MessageFactory fact = new MessageFactory();
		for (MlMessage m : listeDeMessage) {
			fact.createMessagePourBase(m, fenetre);
		}

		Historique.ecrire("fin de l'enregistrement des messages en base");

		fenetre.setVisible(false);

	}

	private MlListeMessage parcoursDossier(String p_chemin, String p_compte,
			TreePath p_treePath, Patience p_fenetre) {
		AccesTableDossier accesDossier = new AccesTableDossier();

		File dossier = new File(p_chemin);
		File[] files = dossier.listFiles();
		ArrayList<File> lstSousDossier = new ArrayList<File>();
		MlListeMessage lstMessage = new MlListeMessage();
		try {
			for (File f : files) {// parcours de tout les fichiers

				parcourListeFichiers(p_compte, p_treePath, p_fenetre,
						lstSousDossier, lstMessage, f);

			}
			// parcour de la liste des message, valorisation
			for (MlMessage m : lstMessage) {
				m.setNomDossier(nomDossier);
				m.setIdCompte(idCompte);
				m.setIdDossier(accesDossier.getIdDossier(nomDossier, idCompte));
			}
			for (File f : lstSousDossier) {
				p_fenetre.afficheInfo("creation du dossier:" + f.getName(), "",
						0);

				MlListeMessage lstMessge = null;

				if (newTp != null) {
					lstMessge = parcoursDossier(f.getCanonicalPath(), p_compte,
							newTp, p_fenetre);
				} else {
					lstMessge = parcoursDossier(f.getCanonicalPath(), p_compte,
							p_treePath, p_fenetre);
				}

				lstMessage.addAll(lstMessge);
			}

		} catch (IOException e) {

		}

		return lstMessage;

	}

	/**
	 * @param p_compte
	 * @param p_treePath
	 * @param p_fenetre
	 * @param lstSousDossier
	 * @param lstMessage
	 * @param f
	 * @throws IOException
	 */
	private void parcourListeFichiers(String p_compte, TreePath p_treePath,
			Patience p_fenetre, ArrayList<File> lstSousDossier,
			MlListeMessage lstMessage, File f) throws IOException {
		if (f.isDirectory()) {
			lstSousDossier.add(f);
		} else {
			if (f.isFile()) {
				if (f.getName().endsWith("wlmail.fol")) {
					// on recupere le nom du dossier a ajouter dans le
					// jTree
					nomDossier = ReadFile.getContenuCaractere(f
							.getAbsolutePath());
					MlCompteMail cptMail = new MlCompteMail(p_compte);
					DossierFactory dossierFact = new DossierFactory(cptMail);
					if (!dossierFact.isDossierPresentImport(nomDossier)) {
						JTreeFactory treeFact = new JTreeFactory();
						newTp = treeFact.createNewDossierAndRefreshTree(
								p_treePath, nomDossier, cptMail.getIdCompte());
					}
				}
				if (f.getName().endsWith(".eml")) {
					MlMessage message = new MlMessage();

					message.setCheminPhysique(f.getCanonicalPath());
					lstMessage.add(message);
					p_fenetre.afficheInfo(lstMessage.size()
							+ " message(s) trouvé(s)", "", 0);

				}

			}

		}
	}

}
