package imap.util;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.JOptionPane;

import com.sun.mail.imap.IMAPFolder;

public class methodeImap {

	private methodeImap() {

	}

	/**
	 * Ouvrir un dossier IMAP (par defaut, la
	 * "boite de reception s'appelle "INBOX"
	 * @param p_store - Store - l'instance de connexion
	 * @param p_fldrName - String - le nom du sossier a ouvrir
	 * @return le dossier IMAP
	 */
	public static IMAPFolder getImapFoler(Store p_store, String p_fldrName) {
		IMAPFolder fldr = null;
		try {
			fldr = (IMAPFolder) p_store.getFolder(p_fldrName);
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(e,
					"Impossible de se connecter au dossier " + p_fldrName);

		}
		return fldr;
	}

	public static IMAPFolder[] getSousDossierIMAP(Store p_store,
			String p_fldrName) {
		IMAPFolder[] listFldr = null;
		try {
			listFldr = (IMAPFolder[]) p_store.getFolder(p_fldrName).list();
		} catch (MessagingException e) {
			if (e instanceof javax.mail.FolderNotFoundException) {
				messageUtilisateur.affMessageException(e,
						"Pas de sous dossier a ce repertoire " + p_fldrName);
				return null;
			}
			messageUtilisateur.affMessageException(e,
					"Impossible d'obtenir la liste des sous dossiers du repertoire "
							+ p_fldrName);

		}
		return listFldr;
	}

}
