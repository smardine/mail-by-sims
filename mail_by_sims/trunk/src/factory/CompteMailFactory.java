/**
 * 
 */
package factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import mdl.MlCompteMail;
import bdd.BDRequette;

import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;

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

	public boolean suppressionCompteMail(MlCompteMail p_cpt) {
		BDRequette bd = new BDRequette();
		boolean result = bd.deleteCompte(p_cpt.getIdCompte());
		bd.closeConnexion();
		return result;
	}

	/**
	 * @param p_compteMail
	 * @return
	 */
	public boolean testBal(MlCompteMail p_compteMail) {
		Properties prop = System.getProperties();
		Session sess = Session.getDefaultInstance(prop, null);
		sess.setDebug(true);
		Store st = null;
		try {
			switch (p_compteMail.getTypeCompte()) {
				case POP:
					st = sess.getStore("pop3");
					break;
				case GMAIL:
				case IMAP:
					Properties props = System.getProperties();
					props.setProperty("mail.store.protocol", "imaps");
					props.setProperty("mail.imap.socketFactory.class",
							"javax.net.ssl.SSLSocketFactory");
					props.setProperty("mail.imap.socketFactory.fallback",
							"false");
					props.setProperty("mail.imaps.partialfetch", "false");

					Session session = Session.getInstance(props);
					st = session.getStore("imaps");
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
			st.connect(p_compteMail.getServeurReception(), p_compteMail
					.getUserName(), p_compteMail.getPassword());
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
