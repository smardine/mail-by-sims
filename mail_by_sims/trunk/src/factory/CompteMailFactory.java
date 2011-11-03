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

import mdl.MlCompteMail;
import bdd.BDRequette;

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
	 * Créer un compte mail en base
	 * @param p_compteMail
	 */
	public boolean creationCompteMail(MlCompteMail p_compteMail) {
		BDRequette bd = new BDRequette();
		boolean result = bd.createNewCompte(p_compteMail);
		if (result) {
			int idCpt = bd.getIdComptes(p_compteMail.getNomCompte());
			p_compteMail.setIdCompte(idCpt);

			// creation des dossiers de base (boite de reception,
			// message
			// envoyé, corbeille, spam) avec un id Dossierparent=0
			List<String> lstDossierBase = new ArrayList<String>();
			EnDossierBase[] lstEnum = EnDossierBase.values();
			for (EnDossierBase dossier : lstEnum) {
				if (dossier != EnDossierBase.ROOT) {
					lstDossierBase.add(dossier.getLib());
				}
			}
			result = bd.createListeDossierDeBase(p_compteMail, lstDossierBase);
			p_compteMail.setIdInbox(bd.getIdInbox(idCpt));
			p_compteMail.setIdBrouillons(bd.getIdBrouillon(idCpt));
			p_compteMail.setIdCorbeille(bd.getIdCorbeille(idCpt));
			p_compteMail.setIdEnvoye(bd.getIdEnvoye(idCpt));
			p_compteMail.setIdSpam(bd.getIdSpam(idCpt));
		}
		bd.closeConnexion();
		return result;

	}

	/**
	 * Supprime un compte mail
	 * @param p_cpt - le compte mail a supprimer
	 * @param p_label - un label pour afficher des infos a l'utilisateur
	 * @param p_progressBar - une barre de progression
	 * @return true si ca a reussi
	 * @throws DonneeAbsenteException - si des données sont à null
	 */
	public boolean suppressionCompteMail(MlCompteMail p_cpt, JLabel p_label,
			JProgressBar p_progressBar) throws DonneeAbsenteException {
		BDRequette bd = new BDRequette();
		boolean result = bd.deleteCompte(p_cpt.getIdCompte(), p_label,
				p_progressBar);
		bd.closeConnexion();
		return result;
	}

	/**
	 * Test d'une boite au lettre (connexion, ouverture inbox,fermeture)
	 * @param p_compteMail - le compte mail a tester
	 * @param p_label - un label pour afficher des infos
	 * @param p_progressBar - une barre de progression
	 * @return true si ca a reussi, false si une exception est levée
	 */
	public boolean testBal(MlCompteMail p_compteMail, Patience p_fenetre) {

		Store st = null;
		try {
			switch (p_compteMail.getTypeCompte()) {
				case POP:
				case GMAIL:
				case IMAP:
					p_fenetre.afficheInfo("Connexion en cours...", "25 %", 25);

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
			p_fenetre.afficheInfo("Ouverture de la boite de reception...",
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

}
