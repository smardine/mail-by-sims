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
import com.sun.mail.pop3.POP3Folder;

import fenetre.principale.jtree.JTreeHelper;

/**
 * Cette classe gere tout ce qui a trait aux dossier ({@link IMAPFolder},
 * {@link POP3Folder},{@link com.googlecode.jdeltasync.Folder})
 * @author smardine
 */
public class DossierFactory {

	private final Folder fldr;
	private final com.googlecode.jdeltasync.Folder deltaFldr;
	private final MlCompteMail cptMail;
	private final BDRequette bd;
	private int idDossier = -1;

	/**
	 * Constructeur pour les dossier de type {@link POP3Folder} ou
	 * {@link IMAPFolder}
	 * @param p_fldr - le dossier ciblé
	 * @param p_compteMail - le compte mail concerné
	 */
	public DossierFactory(Folder p_fldr, MlCompteMail p_compteMail) {
		this.deltaFldr = null;
		this.fldr = p_fldr;
		this.cptMail = p_compteMail;
		this.bd = new BDRequette();
	}

	/**
	 * Constructeur pour les dossier de type
	 * {@link com.googlecode.jdeltasync.Folder} UNIQUEMENT
	 * @param p_folder - le dossier ciblé
	 * @param p_compteMail - le compte mail concerné
	 */
	public DossierFactory(com.googlecode.jdeltasync.Folder p_folder,
			MlCompteMail p_compteMail) {
		this.fldr = null;
		this.deltaFldr = p_folder;
		this.cptMail = p_compteMail;
		this.bd = new BDRequette();
	}

	/**
	 * @param p_cptMail
	 */
	public DossierFactory(MlCompteMail p_cptMail) {
		this.fldr = null;
		this.deltaFldr = null;
		this.cptMail = p_cptMail;
		this.bd = new BDRequette();
	}

	public boolean isDossierPresentImport(String p_nameToCheck) {
		switch (cptMail.getTypeCompte()) {
			case GMAIL:
			case HOTMAIL:
			case IMAP:
			case POP:
				idDossier = bd.getIdDossier(p_nameToCheck, cptMail
						.getIdCompte());

		}
		if (idDossier != -1) {
			return true;
		}
		return false;

	}

	/**
	 * verif si dossier present:
	 * <ul>
	 * <li>1- fldr.nom est connu de la base</li>
	 * <li>2- si fldr.getFullName!= dossierBase.nomInternet</li>
	 * <li>3- maj numero dossierParent * en prenant fldr.getParent.getName et en
	 * le recherchant en base => donne l'id du dossier parent</li>
	 * </ul>
	 */
	public void isDossierDejaPresentEnBase() {

		switch (cptMail.getTypeCompte()) {
			case GMAIL:
			case IMAP:
				IMAPFolder imapF = (IMAPFolder) fldr;
				verifGmail(imapF);
				break;
			case HOTMAIL:
				verifHotmail(deltaFldr);

		}

		return;
	}

	/**
	 * Verification de la presence d'un dossier
	 * {@link com.googlecode.jdeltasync.Folder} en base
	 * @param p_deltaFldr - le dossier ciblé
	 */
	private void verifHotmail(com.googlecode.jdeltasync.Folder p_deltaFldr) {
		if (isInboxDelta(p_deltaFldr)) {
			idDossier = cptMail.getIdInbox();
			bd.updateNomDossierInternet(idDossier, p_deltaFldr.getName(), 0);
		} else if (isBrouillonDelta(p_deltaFldr)) {
			idDossier = cptMail.getIdBrouillons();
			bd.updateNomDossierInternet(idDossier, p_deltaFldr.getName(), 0);
		} else if (isCorbeilleDelta(p_deltaFldr)) {
			idDossier = cptMail.getIdCorbeille();
			bd.updateNomDossierInternet(idDossier, p_deltaFldr.getName(), 0);
		} else if (isEnvoyeDelta(p_deltaFldr)) {
			idDossier = cptMail.getIdEnvoye();
			bd.updateNomDossierInternet(idDossier, p_deltaFldr.getName(), 0);
		} else if (isSpamDelta(p_deltaFldr)) {
			idDossier = cptMail.getIdSpam();
			bd.updateNomDossierInternet(idDossier, p_deltaFldr.getName(), 0);
		} else {
			idDossier = bd.getIdDossierWithFullName(p_deltaFldr.getName(),
					p_deltaFldr.getName(), cptMail.getIdCompte());
			if (idDossier == -1) {
				createNewDossierDeltaEnBase();
			}

		}
	}

	/**
	 * Est-ce le dossier spam?
	 * @param p_deltaFldr - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isSpamDelta(com.googlecode.jdeltasync.Folder p_deltaFldr) {
		if ("Junk".equals(p_deltaFldr.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Est-ce le dossier envoyé?
	 * @param p_deltaFldr - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isEnvoyeDelta(com.googlecode.jdeltasync.Folder p_deltaFldr) {
		if ("Sent".equals(p_deltaFldr.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Est-ce le dossier corbeille?
	 * @param p_deltaFldr - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isCorbeilleDelta(
			com.googlecode.jdeltasync.Folder p_deltaFldr) {
		if ("Deleted".equals(p_deltaFldr.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Est-ce le dossier brouillon?
	 * @param p_deltaFldr - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isBrouillonDelta(
			com.googlecode.jdeltasync.Folder p_deltaFldr) {
		if ("Drafts".equals(p_deltaFldr.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Est-ce le dossier inbox?
	 * @param p_deltaFldr - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isInboxDelta(com.googlecode.jdeltasync.Folder p_deltaFldr) {
		if ("Inbox".equals(p_deltaFldr.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Verification de la presence d'un dossier {@link IMAPFolder} en base
	 * @param p_deltaFldr - le dossier ciblé
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
						Historique.ecrireReleveBal(cptMail, p_imapF
								.getFullName(), "Le repertoire "
								+ p_imapF.getFullName()
								+ " à été déplacée sur le serveur");
						Historique.ecrireReleveBal(cptMail, p_imapF
								.getFullName(),
								"mise a jour de la base de données");
						int idDossierParent;

						idDossierParent = bd.getIdDossierWithFullName(p_imapF
								.getParent().getName(), p_imapF.getParent()
								.getFullName(), cptMail.getIdCompte());

						bd.updateNomDossierInternet(idDossier, p_imapF
								.getFullName(), idDossierParent);
					} catch (MessagingException e) {
						Historique
								.ecrireReleveBal(cptMail,
										p_imapF.getFullName(),
										"erreur a la recuperation de l'idDossierParent");
						return;
					}

				}

			} else {
				createNewDossierEnBase();
			}
		}
	}

	/**
	 * Est-ce le dossier corbeille?
	 * @param p_folder - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isCorbeille(IMAPFolder p_folder) {
		return p_folder.getFullName().equals("[Gmail]/Trash") || //
				p_folder.getFullName().equals("[Gmail]/Corbeille");
	}

	/**
	 * Est-ce le dossier Envoyé?
	 * @param p_folder - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isEnvoye(IMAPFolder p_folder) {
		return p_folder.getFullName().equals("[Gmail]/Sent Mail") || //
				p_folder.getFullName().equals("[Gmail]/Messages envoyés");
	}

	/**
	 * Est-ce le dossier brouillon?
	 * @param p_folder - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isBrouillon(IMAPFolder p_folder) {
		return p_folder.getFullName().equals("[Gmail]/Drafts") || //
				p_folder.getFullName().equals("[Gmail]/Brouillons");
	}

	/**
	 * Est-ce le dossier inbox?
	 * @param p_folder - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isInbox(IMAPFolder p_folder) {
		return p_folder.getFullName().equals("INBOX");
	}

	/**
	 * Est-ce le dossier spam?
	 * @param p_folder - le dossier ciblé
	 * @return true si vrai
	 */
	private boolean isSpam(IMAPFolder p_folder) {
		return p_folder.getFullName().equals("[Gmail]/Spam");
	}

	/**
	 * Obtenir l'id du dossier, celuiç ci est valorisé lors de la verification
	 * de la presence d'un dossier en base
	 * @return the idDossier
	 */
	public int getIdDossier() {
		return this.idDossier;
	}

	/**
	 * Création d'un nouveau dossier dans la bdd
	 */
	public void createNewDossierEnBase() {
		Historique.ecrireReleveBal(cptMail, fldr.getFullName(),
				"Création d'un nouveau dossier");
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
				Historique.ecrireReleveBal(cptMail, fldr.getFullName(),
						"impossible de trouver le dossier parent");
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
		JTreeHelper.reloadJtree(ComposantVisuelCommun.getJTree());

	}

	/**
	 * Creer un dossier en base, pour les bal de type Hotmail, ils sont tous
	 * créer sous "Inbox"
	 */
	public void createNewDossierDeltaEnBase() {
		Historique.ecrireReleveBal(cptMail, deltaFldr.getName(),
				"Création d'un nouveau dossier");
		bd.createNewDossier(cptMail.getIdCompte(), cptMail.getIdInbox(),
				deltaFldr.getName(), deltaFldr.getName());
		idDossier = bd.getIdDossierWithFullName(deltaFldr.getName().trim(),
				deltaFldr.getName().trim(), cptMail.getIdCompte());
		JTreeHelper.reloadJtree(ComposantVisuelCommun.getJTree());
	}

}
