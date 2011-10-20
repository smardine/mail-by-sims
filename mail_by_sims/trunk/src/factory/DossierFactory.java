/**
 * 
 */
package factory;

import javax.mail.Folder;
import javax.mail.MessagingException;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import tools.Historique;
import bdd.BDRequette;

import com.sun.mail.imap.IMAPFolder;

import fenetre.principale.jtree.utiljTree;

/**
 * @author smardine
 */
public class DossierFactory {

	private final Folder fldr;
	private final MlCompteMail cptMail;
	private final BDRequette bd;
	private int idDossier = -1;

	public DossierFactory(Folder p_fldr, MlCompteMail p_compteMail) {
		this.fldr = p_fldr;
		this.cptMail = p_compteMail;
		this.bd = new BDRequette();
	}

	public void isDossierDejaPresentEnBase() {
		/**
		 * verif si dossier present: 1- fldr.nom est connu de la base 2- si
		 * fldr.getFullName!= dossierBase.nomInternet - maj numero dossierParent
		 * en prenant fldr.getParent.getName et en le recherchant en base =>
		 * donne l'id du dossier parent
		 */
		switch (cptMail.getTypeCompte()) {
			case GMAIL:
			case IMAP:
				IMAPFolder imapF = (IMAPFolder) fldr;
				verifGmail(imapF);
				break;
			case HOTMAIL:

		}

		return;
	}

	/**
	 * @param p_imapF
	 */
	private void verifGmail(IMAPFolder p_imapF) {

		if (isInbox(p_imapF)) {
			idDossier = cptMail.getIdInbox();
			bd.updateNomDossierInternet(idDossier, p_imapF.getFullName(), 0);
		} else if (isBrouillon(p_imapF)) {
			idDossier = cptMail.getIdBrouillons();
			bd.updateNomDossierInternet(idDossier, p_imapF.getFullName(), 0);
		} else if (isCorbeille(p_imapF)) {
			idDossier = cptMail.getIdCorbeille();
			bd.updateNomDossierInternet(idDossier, p_imapF.getFullName(), 0);
		} else if (isEnvoye(p_imapF)) {
			idDossier = cptMail.getIdEnvoye();
			bd.updateNomDossierInternet(idDossier, p_imapF.getFullName(), 0);
		} else if (isSpam(p_imapF)) {
			idDossier = cptMail.getIdSpam();
			bd.updateNomDossierInternet(idDossier, p_imapF.getFullName(), 0);
		} else {
			idDossier = bd.getIdDossierWithFullName(p_imapF.getName(), p_imapF
					.getFullName(), cptMail.getIdCompte());
			if (idDossier != -1) {
				// le dossier est connu, reste a verifier si le nom internet en
				// base est le meme que
				// le dossier testé
				if (!p_imapF.getFullName().equals(
						bd.getNomInternetDossier(idDossier))) {
					// le dossier a été deplacé sur le serveur, maj du nom
					// internet et de l'id dossier parent
					// dans le cas où l'utilisateur a créer un dossier du meme
					// nom mais dans un endroit différent, il faut pouvoir le
					// recuperer
					try {

						int idDossierParent;

						idDossierParent = bd.getIdDossierWithFullName(p_imapF
								.getParent().getName(), p_imapF.getParent()
								.getFullName(), cptMail.getIdCompte());

						bd.updateNomDossierInternet(idDossier, p_imapF
								.getFullName(), idDossierParent);
					} catch (MessagingException e) {
						Historique
								.ecrireReleveBal(cptMail,
										"erreur a la recuperatio nde l'idDossierParent");
						return;
					}
				}
			}

		}
	}

	/**
	 * @param folder
	 * @return
	 */
	private boolean isCorbeille(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Trash") || //
				folder.getFullName().equals("[Gmail]/Corbeille");
	}

	/**
	 * @param folder
	 * @return
	 */
	private boolean isEnvoye(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Sent Mail") || //
				folder.getFullName().equals("[Gmail]/Messages envoyés");
	}

	/**
	 * @param folder
	 * @return
	 */
	private boolean isBrouillon(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Drafts") || //
				folder.getFullName().equals("[Gmail]/Brouillons");
	}

	private boolean isInbox(IMAPFolder folder) {
		return folder.getFullName().equals("INBOX");
	}

	private boolean isSpam(IMAPFolder folder) {
		return folder.getFullName().equals("[Gmail]/Spam");
	}

	/**
	 * @return the idDossier
	 */
	public int getIdDossier() {
		return this.idDossier;
	}

	/**
	 * 
	 */
	public void createNewDossierEnBase() {
		if (fldr.getFullName().contains("[Gmail]")) {
			// de cette facon, les dossier
			// "Important","Tous les messages"...
			// seront a la racine du compte dans le jtree
			bd.createNewDossier(cptMail.getIdCompte(), 0, fldr.getName(), fldr
					.getFullName());
		} else {
			// sinon, les dossiers créés le seront sous INBOX
			String nomDossierParent = "";
			String nomDossierParentComplet = "";
			try {
				nomDossierParent = fldr.getParent().getName();
				nomDossierParentComplet = fldr.getParent().getFullName();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int idDossierParent = bd.getIdDossierWithFullName(nomDossierParent,
					nomDossierParentComplet, cptMail.getIdCompte());
			if (idDossierParent != -1) {
				bd.createNewDossier(cptMail.getIdCompte(), idDossierParent,
						fldr.getName(), fldr.getFullName());
			} else {
				bd.createNewDossier(cptMail.getIdCompte(),
						cptMail.getIdInbox(), fldr.getName().trim(), fldr
								.getFullName().trim());
			}

		}

		idDossier = bd.getIdDossierWithFullName(fldr.getName().trim(), fldr
				.getFullName().trim(), cptMail.getIdCompte());
		utiljTree.reloadJtree(ComposantVisuelCommun.getJTree());

	}

}
