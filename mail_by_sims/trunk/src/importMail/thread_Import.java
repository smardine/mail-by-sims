package importMail;

import fenetre.comptes.EnDossierBase;
import fenetre.principale.Main;
import fenetre.principale.jtree.ActionTree;
import imap.util.messageUtilisateur;
import imap.util.methodeImap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import mdl.MlListeMessage;
import mdl.MlMessage;
import tools.GestionRepertoire;
import tools.ReadFile;
import bdd.BDAcces;
import bdd.BDRequette;

public class thread_Import extends Thread {

	private final JTree tree;
	private int idCompte;

	public thread_Import(JTree p_tree) {
		this.tree = p_tree;
		new BDAcces();
	}

	@Override
	public void run() {
		String chemin = GestionRepertoire
				.OpenFolder("Veuillez indiquer l'emplacement de vos mails Windows Mail");
		// messageUtilisateur.affMessageInfo("Vous avez choisi: " + chemin);

		String choixCompte = messageUtilisateur.afficheChoixMultiple(
				"Choix du compte",
				"dans quel comptes souhaitez vous importer les messages?",
				BDRequette.getListeDeComptes());

		// parcour du repertoire de facon recursive
		// les fichiers avec l'extension ".fol"contienne le nom du repertoire
		System.out.println(choixCompte);
		idCompte = BDRequette.getIdComptes(choixCompte);

		Object[] path = new Object[3];
		path[0] = tree.getModel().getRoot();
		path[1] = choixCompte;
		path[2] = EnDossierBase.RECEPTION.getLib();

		TreePath treePathInitial = new TreePath(path);
		Main.setNomCompte(choixCompte);
		Main.setTreePath(treePathInitial);
		// tree.setSelectionPath(treePathInitial);

		MlListeMessage listeDeMessage = parcoursDossier(chemin, choixCompte,
				treePathInitial);
		// messageUtilisateur.affMessageInfo("il y a au total "
		// + listeDeMessage.size() + " message(s)");

		enregistreMessageEnBase(listeDeMessage);

		System.out.println("fin de l'enregistrement des message en base");

	}

	@SuppressWarnings("hiding")
	public static void enregistreMessageEnBase(MlListeMessage listeDeMessage) {

		/** On simule la reception d'un message */
		Properties props = System.getProperties();
		props.put("mail.host", "smtp.dummydomain.com");
		props.put("mail.transport.protocol", "smtp");

		Session mailSession = Session.getDefaultInstance(props, null);
		/***/
		int messNumber = 1;
		for (MlMessage m : listeDeMessage) {
			String cheminPhysique = m.getCheminPhysique();
			// String idCpt = m.getIdCompte();
			// String idDossier = m.getIdDossier();
			// String nomDossier = m.getNomDossier();
			System.out.println("importation du message " + messNumber++
					+ "sur " + listeDeMessage.size());
			InputStream source;
			try {
				source = new FileInputStream(cheminPhysique);

				MimeMessage mime;

				mime = new MimeMessage(mailSession, source);
				m.setSujet(mime.getSubject());
				m.setDateReception(mime.getSentDate());
				m.setExpediteur(mime.getFrom()[0].toString());
				// ******************************//
				ArrayList<String> listeDestinataires = new ArrayList<String>(
						mime.getAllRecipients().length);
				for (Address uneAdresse : mime.getAllRecipients()) {
					listeDestinataires.add(uneAdresse.toString());
				}
				m.setDestinataire(listeDestinataires);
				m.setUIDMessage("" + System.currentTimeMillis());// getMessageID());

				/**
				 * il faut decoder le message de maniere a voir si il y a des
				 * piece jointe
				 */

				m.setContenu(recupContenuMail(m, mime, new JTextArea()));// ,
				// m.getDateReception()
				// .getTime()));

			} catch (FileNotFoundException e) {
				messageUtilisateur.affMessageException(e, "le fichier "
						+ cheminPhysique + " est introuvable");

			} catch (MessagingException e) {
				messageUtilisateur.affMessageException(e,
						"Erreur de format eml");

			} catch (IOException e) {
				messageUtilisateur.affMessageException(e,
						"impossible d'acceder au fichier " + cheminPhysique);
			}
			BDRequette.createNewMessage(m);

		}

	}

	public static String recupContenuMail(MlMessage p_mlMessage,
			Message p_messageJavaMail, JTextArea textArea)// , long
	// p_prefixeNomFichier)//
	{
		StringBuilder sb = new StringBuilder();
		// int messageNumber = p_messageJavaMail.getMessageNumber();
		// String messageName = p_messageJavaMail.getFileName();
		methodeImap.afficheText(textArea, "Recupération du contenu du message");
		Object o;
		try {
			o = p_messageJavaMail.getContent();
			if (o instanceof String) {
				sb.append((String) o);
			} else if (o instanceof Multipart) {
				Multipart mp = (Multipart) o;
				decodeMultipart(p_mlMessage, mp, sb, textArea);// ,
				// p_prefixeNomFichier);

			} else if (o instanceof InputStream) {
				System.out.println("on ne devrait jamais passé par là");
				// recuperePieceJointe(p_complet, p_prefixeNomFichier, b, o);
			}
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation du mail");
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur a la recuperation du mail");
		}

		return sb.toString();
	}

	public static void decodeMultipart(MlMessage p_mlMessage, Multipart mp,
			StringBuilder sb, JTextArea textArea) // , long p_prefixeNomFichier)
	{
		try {
			for (int j = 0; j < mp.getCount(); j++) {
				// Part are numbered starting at 0
				BodyPart b = mp.getBodyPart(j);
				// String contentType = b.getContentType();
				Object o2 = b.getContent();
				if (o2 instanceof String) {
					if (j == mp.getCount() - 1) {
						// on ne veut que la partie html du message
						sb.append(o2);
					}

				} else if (o2 instanceof Multipart) {
					System.out.print("**MultiPart Imbriqué.  ");
					Multipart mp2 = (Multipart) o2;
					decodeMultipart(p_mlMessage, mp2, sb, textArea);// ,
					// p_prefixeNomFichier);

				} else if (o2 instanceof InputStream) {

					recuperePieceJointe(p_mlMessage,// p_prefixeNomFichier,//
							b, o2, textArea);

				}

			}
		} catch (FileNotFoundException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur Decodage MultiPart");
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur Decodage MultiPart");
		} catch (IOException e) {
			messageUtilisateur.affMessageException(e,
					"Erreur Decodage MultiPart");
		}

	}

	/**
	 * @param p_complet
	 * @param p_prefixeNomFichier
	 * @param p_bodyPart
	 * @param p_inputStream
	 * @param textArea
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void recuperePieceJointe(MlMessage p_mlMessage,
	// long p_prefixeNomFichier, //
			BodyPart p_bodyPart, Object p_inputStream, JTextArea textArea)
			throws MessagingException, FileNotFoundException {

		InputStream input = (InputStream) p_inputStream;
		String fileName = p_bodyPart.getFileName();

		if (fileName != null) {
			fileName = fileName.substring(p_bodyPart.getFileName().lastIndexOf(
					"\\") + 1);
		} else {
			fileName = "inconnu";
		}

		System.out.println("**C'est une piece jointe dont le nom est :"
				+ fileName);
		if (fileName.contains("ISO") || fileName.contains("UTF")) {
			fileName = decodeurIso(fileName);
		}
		methodeImap
				.afficheText(textArea,
						"Recuperation d'une piece jointe dont le nom est \n"
								+ fileName);

		// creation du repertoire qui va acceuillir les pieces jointes
		File repPieceJointe = new File(GestionRepertoire.RecupRepTravail()
				+ "/tempo/pieces jointes");
		if (!repPieceJointe.exists()) {
			repPieceJointe.mkdirs();
		}
		File fichier = new File(repPieceJointe.getAbsolutePath() + "/"
				+ fileName);
		if (!fichier.exists()) {
			FileOutputStream writeFile = new FileOutputStream(fichier);
			byte[] buffer = new byte[p_bodyPart.getSize()];
			int read;

			try {
				while ((read = input.read(buffer)) != -1) {
					writeFile.write(buffer, 0, read);

				}
				writeFile.flush();
				writeFile.close();
				input.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e,
						"Impossible de recuperer le fichier joint",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				if (writeFile != null) {
					try {
						writeFile.close();
						input.close();
						p_mlMessage.getListePieceJointe().add(fichier);
					} catch (IOException e) {
						JOptionPane
								.showMessageDialog(
										null,
										e,
										"Impossible de fermer les flux lors de la recuperation du fichier joint",
										JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		}

	}

	public static String decodeurIso(String fileName) {

		fileName = fileName.replaceAll("=?ISO-8859-1?Q?", "").replace("?", "")
				.replace(",", "").replaceAll("=?UTF-8?Q?", "").replace("=5F",
						"_").replace("=E9", "é").replace("=Q", "").replace(
						"=CC=81e", "é").replace("=", "");

		return fileName;
	}

	private MlListeMessage parcoursDossier(String p_chemin, String p_compte,
			TreePath p_treePath) {
		TreePath newTp = null;
		String nomDossier = null;
		File dossier = new File(p_chemin);
		File[] files = dossier.listFiles();
		ArrayList<File> lstSousDossier = new ArrayList<File>();
		MlListeMessage lstMessage = new MlListeMessage();
		for (File f : files) {// parcours de tout les fichiers

			if (f.isDirectory()) {
				lstSousDossier.add(f);
			} else {
				if (f.isFile()) {

					if (f.getName().endsWith("wlmail.fol")) {
						// on recupere le nom du dossier a ajouter dans le jTree
						nomDossier = ReadFile.getContenuCaractere(f
								.getAbsolutePath());
						if (!isDossierDejaExistant(nomDossier, p_compte)) {
							newTp = createNewDossierAndRefreshTree(p_treePath,
									nomDossier);
						}
					}
					if (f.getName().endsWith(".eml")) {
						MlMessage message = new MlMessage();
						try {
							message.setCheminPhysique(f.getCanonicalPath());
							lstMessage.add(message);
							System.out.println(lstMessage.size()
									+ " message(s) trouvé(s)");
						} catch (IOException e) {
							messageUtilisateur.affMessageException(e,
									"impossible d'acceder au repertoire :\n\r");
						}
					}

				}

			}

		}
		// parcour de la liste des message, valorisation
		for (MlMessage m : lstMessage) {
			m.setNomDossier(nomDossier);
			m.setIdCompte(idCompte);
			m.setIdDossier(BDRequette.getIdDossier(nomDossier, idCompte));
		}
		for (File f : lstSousDossier) {
			System.out.println("creation du dossier:" + f.getName());
			MlListeMessage lstMessge = null;
			try {
				if (newTp != null) {
					lstMessge = parcoursDossier(f.getCanonicalPath(), p_compte,
							newTp);
				} else {
					lstMessge = parcoursDossier(f.getCanonicalPath(), p_compte,
							p_treePath);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lstMessage.addAll(lstMessge);
		}
		return lstMessage;
	}

	/**
	 * @param p_treePath
	 * @param nomDossier
	 */
	private TreePath createNewDossierAndRefreshTree(TreePath p_treePath,
			String nomDossier) {
		// Object[] path = new Object[p_treePath.getPathCount()+1];
		TreePath newTp = p_treePath.pathByAddingChild(nomDossier);
		// TreePath newTp = new TreePath(p_treePath.toString().replace("[", "")
		// .replace("]", "")
		// + ", " + nomDossier);

		String dossierParent = (String) p_treePath.getLastPathComponent();
		int idDossierParent = BDRequette.getIdDossier(dossierParent, idCompte);
		BDRequette.createNewDossier(idCompte, idDossierParent, nomDossier);
		tree.getModel().valueForPathChanged(newTp, ActionTree.AJOUTER);
		// tree.setSelectionPath(newTp);
		Main.setTreePath(newTp);
		return newTp;
		// tree.setSelectionPath(newTp.getParentPath());
	}

	private boolean isDossierDejaExistant(String nomDossier, String pCompte) {
		int idCpt = BDRequette.getIdComptes(pCompte);
		return BDRequette.getListeDossier(idCpt).contains(nomDossier);

	}

}
