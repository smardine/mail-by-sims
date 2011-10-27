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
import fenetre.comptes.EnDossierBase;

/**
 * @author smardine
 */
public class CompteMailFactory {

	public CompteMailFactory() {

	}

	/**
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
		}
		bd.closeConnexion();
		return result;

	}

	public boolean suppressionCompteMail(MlCompteMail p_cpt, JLabel p_label,
			JProgressBar p_progressBar) throws DonneeAbsenteException {
		BDRequette bd = new BDRequette();
		boolean result = bd.deleteCompte(p_cpt.getIdCompte(), p_label,
				p_progressBar);
		bd.closeConnexion();
		return result;
	}

	/**
	 * @param p_compteMail
	 * @return
	 */
	public boolean testBal(MlCompteMail p_compteMail) {

		Store st = null;
		try {
			switch (p_compteMail.getTypeCompte()) {
				case POP:
				case GMAIL:
				case IMAP:
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
