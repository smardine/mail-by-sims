/**
 * 
 */
package factory;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import mdl.mlcomptemail.MlCompteMail;
import bdd.accesTable.AccesTableCompte;
import bdd.accesTable.AccesTableDossier;

import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;

import exception.DonneeAbsenteException;
import fenetre.Patience;
import fenetre.comptes.EnDossierBase;

/**
 * Cette classe s'occupent de tout ce qui a trait aux compte mail.
 * @author smardine
 */
public class CompteMailFactory {
	/**
	 * Constructeur
	 */
	public CompteMailFactory() {

	}

	/**
	 * Cr�er un compte mail en base
	 * @param p_compteMail
	 */
	public boolean creationCompteMail(MlCompteMail p_compteMail) {
		AccesTableCompte accesCompte = new AccesTableCompte();
		boolean result = accesCompte.createNewCompte(p_compteMail);
		if (result) {
			int idCpt = accesCompte.getIdComptes(p_compteMail.getNomCompte());
			p_compteMail.setIdCompte(idCpt);

			// creation des dossiers de base (boite de reception,
			// message
			// envoy�, corbeille, spam) avec un id Dossierparent=0
			List<String> lstDossierBase = new ArrayList<String>();
			EnDossierBase[] lstEnum = EnDossierBase.values();
			for (EnDossierBase dossier : lstEnum) {
				if (dossier != EnDossierBase.ROOT) {
					lstDossierBase.add(dossier.getLib());
				}
			}
			AccesTableDossier accesDossier = new AccesTableDossier();
			result = accesDossier.createListeDossierDeBase(p_compteMail,
					lstDossierBase);
			p_compteMail.setIdInbox(accesCompte.getIdInbox(idCpt));
			p_compteMail.setIdBrouillons(accesCompte.getIdBrouillon(idCpt));
			p_compteMail.setIdCorbeille(accesCompte.getIdCorbeille(idCpt));
			p_compteMail.setIdEnvoye(accesCompte.getIdEnvoye(idCpt));
			p_compteMail.setIdSpam(accesCompte.getIdSpam(idCpt));
		}

		return result;

	}

	/**
	 * Supprime un compte mail
	 * @param p_cpt - le compte mail a supprimer
	 * @param p_label - un label pour afficher des infos a l'utilisateur
	 * @param p_progressBar - une barre de progression
	 * @return true si ca a reussi
	 * @throws DonneeAbsenteException - si des donn�es sont � null
	 */
	public boolean suppressionCompteMail(MlCompteMail p_cpt, JLabel p_label,
			JProgressBar p_progressBar) throws DonneeAbsenteException {
		AccesTableCompte accesCompte = new AccesTableCompte();
		boolean result = accesCompte.deleteCompte(p_cpt.getIdCompte(), p_label,
				p_progressBar);

		return result;
	}

	/**
	 * Test d'une boite au lettre (connexion, ouverture inbox,fermeture)
	 * @param p_compteMail - le compte mail a tester
	 * @param p_label - un label pour afficher des infos
	 * @param p_progressBar - une barre de progression
	 * @return true si ca a reussi, false si une exception est lev�e
	 */
	public boolean testBal(MlCompteMail p_compteMail, Patience p_fenetre) {

		Store st = null;
		try {
			switch (p_compteMail.getTypeCompte()) {
				case POP:
				case GMAIL:
				case IMAP:
					afficheInfo(p_fenetre, "Connexion en cours...", "25 %", 25);
					StoreFactory storeFact = new StoreFactory(p_compteMail);
					st = storeFact.getConnectedStore();
					break;
				case HOTMAIL:
					DeltaSyncClientHelper client = new DeltaSyncClientHelper(
							new DeltaSyncClient(), p_compteMail.getUserName(),
							p_compteMail.getPassword());
					client.login();
					if (null != client.getInbox()) {
						return true;
					}
					return false;
			}
			afficheInfo(p_fenetre, "Ouverture de la boite de reception...",
					"50 %", 50);

			Folder f = st.getFolder("INBOX");
			f.open(Folder.READ_ONLY);
			f.close(false);
		} catch (Exception e) {
			// erreur de protocle
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (MessagingException e) {
				return false;
			}
		}

		return true;
	}

	private void afficheInfo(Patience p_fenetre, String p_textLabel,
			String p_textProgress, int p_progressValue) {
		if (p_fenetre != null) {
			p_fenetre.afficheInfo(p_textLabel, p_textProgress, p_progressValue);
		}
	}

}
