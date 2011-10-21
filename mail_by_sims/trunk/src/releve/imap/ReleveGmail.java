/**
 * 
 */
package releve.imap;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import releve.imap.util.messageUtilisateur;
import factory.ReleveFactory;

/**
 * @author smardine
 */
public class ReleveGmail {
	private final String TAG = this.getClass().getSimpleName();
	private final JProgressBar progressBar;
	private final JProgressBar progressPJ;
	private final MlCompteMail cptMail;

	public ReleveGmail(int p_idCompte, String p_user, String p_password,
			String p_host, JProgressBar progress,
			JProgressBar p_progressPieceJointe, JTextArea textArea,
			boolean p_isSynchro) {
		this.cptMail = new MlCompteMail(p_idCompte);
		this.progressBar = progress;
		this.progressPJ = p_progressPieceJointe;

		ReleveFactory releve = new ReleveFactory(cptMail, progressBar,
				progressPJ, textArea);
		try {
			releve.releveCourier();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + cptMail.getNomCompte());
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"Erreur a la releve du compte " + cptMail.getNomCompte());
		}
	}

	/**
	 * @param args
	 */

	// public void go(MlCompteMail p_compteMail, JProgressBar p_progress,
	// JProgressBar p_progressPJ) {
	// BDRequette bd = new BDRequette();
	// Properties props = System.getProperties();
	//
	// props.setProperty("mail.store.protocol", "imaps");
	// props.setProperty("mail.imap.socketFactory.class",
	// "javax.net.ssl.SSLSocketFactory");
	// props.setProperty("mail.imap.socketFactory.fallback", "false");
	// props.setProperty("mail.imaps.partialfetch", "false");
	//
	// if (isSynchro) {
	// // on commence par verifier les messages suprrim�s car sinon, on
	// // pert du
	// // temp a le faire apres une releve de la BAL
	// methodeImap.afficheText(textArea, "Mise a jour du compte "
	// + p_compteMail.getNomCompte());
	// methodeImap.afficheText(textArea,
	// "Synchronisation des messages supprim�s");
	// methodeImap.miseAJourMessage(props, p_compteMail.getIdCompte(),
	// p_progress, p_compteMail.getServeurReception(),
	// p_compteMail.getUserName(), p_compteMail.getPassword(),
	// textArea);
	// } else {
	//
	// Session session = Session.getInstance(props);
	//
	// // Get a Store object
	// Store store = null;
	//
	// try {
	// store = session.getStore("imaps");
	// if (!store.isConnected()) {
	// store.connect(host, user, password);
	// }
	//
	// methodeImap.afficheText(textArea, "Releve des dossiers");
	// Historique.ecrireReleveBal(cptMail, "Releve des dossiers");
	// Folder[] personnal = store.getPersonalNamespaces();
	// // Folder[] shared = store.getSharedNamespaces();
	// // Folder[] userNamedSpace = store.getUserNamespaces(user);
	//
	// for (Folder f : personnal) {
	// IMAPFolder[] sousfodler = methodeImap.getSousDossierIMAP(
	// store, f.getFullName());
	//
	// traiteListeDossier(p_compteMail, p_progress, p_progressPJ,
	// bd, props, sousfodler, store);
	// }
	//
	// methodeImap.afficheText(textArea, "Releve du compte "
	// + p_compteMail.getNomCompte() + " termin�e");
	// // store.close();
	// } catch (MessagingException e) {
	// messageUtilisateur.affMessageException(TAG, e,
	// "Erreur connexion");
	// }
	//
	// }
	//
	// methodeImap.afficheText(textArea,
	// "Fin des op�rations sur la boite GMAIL");
	// bd.closeConnexion();
	// }

	// /**
	// * @param p_compteMail
	// * @param p_progress
	// * @param p_progressPJ
	// * @param bd
	// * @param props
	// * @param lstSousDossier
	// */
	// private void traiteListeDossier(MlCompteMail p_compteMail,
	// JProgressBar p_progress, JProgressBar p_progressPJ, BDRequette bd,
	// Properties props, IMAPFolder[] lstSousDossier, Store p_store) {
	// if (lstSousDossier != null) {
	// for (IMAPFolder fldr : lstSousDossier) {
	//
	// // messageListener listener = new messageListener(p_compteMail,
	// // fldr);
	// // fldr.addMessageChangedListener(listener);
	// // fldr.addFolderListener(listener);
	// // fldr.addMessageCountListener(listener);
	// if ("[Gmail]".equals(fldr.getFullName())) {
	// // ce n'est pas vraiment un repertoire,
	// // c'est plus un conteneur
	// // on recupere juste ses sous dossier et on continue
	// IMAPFolder[] lstFolder = methodeImap.getSousDossierIMAP(
	// p_store, fldr.getFullName());
	//
	// traiteListeDossier(cptMail, progressBar, progressPJ, bd,
	// props, lstFolder, p_store);
	// continue;
	// }
	//
	// DossierFactory fact = new DossierFactory(fldr, p_compteMail);
	// fact.isDossierDejaPresentEnBase();
	// int idDossier = fact.getIdDossier();
	//
	// if (idDossier == (-1)) {// si le sous dossier est
	// // inconnu
	// // de la base, on en cr�er un
	//
	// Historique.ecrireReleveBal(cptMail,
	// "Cr�ation d'un nouveau dossier: "
	// + fldr.getFullName());
	// fact.createNewDossierEnBase();
	// idDossier = fact.getIdDossier();
	//
	// }
	// methodeImap.releveImap(props, p_compteMail, p_progress,
	// p_progressPJ, idDossier, fldr, textArea);
	//
	// IMAPFolder[] lstFolder = methodeImap.getSousDossierIMAP(
	// p_store, fldr.getFullName());
	//
	// traiteListeDossier(cptMail, progressBar, progressPJ, bd, props,
	// lstFolder, p_store);
	//
	// }
	// }
	//
	// }

	// /**
	// * @param p_compteMail
	// * @param bd
	// * @param fldr
	// * @return
	// */
	// private int verifieConnaissanceDossier(MlCompteMail p_compteMail,
	// BDRequette bd, IMAPFolder fldr) {
	// int idDossier;
	// if (isInbox(fldr)) {
	// idDossier = p_compteMail.getIdInbox();
	// } else if (isBrouillon(fldr)) {
	// idDossier = p_compteMail.getIdBrouillons();
	// } else if (isCorbeille(fldr)) {
	// idDossier = p_compteMail.getIdCorbeille();
	// } else if (isEnvoye(fldr)) {
	// idDossier = p_compteMail.getIdEnvoye();
	// } else if (isSpam(fldr)) {
	// idDossier = p_compteMail.getIdSpam();
	// } else {
	// idDossier = bd.getIdDossier(fldr.getName(), p_compteMail
	// .getIdCompte());
	// }
	// return idDossier;
	// }

}
