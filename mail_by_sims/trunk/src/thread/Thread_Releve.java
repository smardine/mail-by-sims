package thread;

import javax.mail.MessagingException;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import mdl.MlListeMessage;
import releve.hotmail.ReleveHotmail;
import releve.imap.ReleveGmail;
import releve.imap.util.messageUtilisateur;
import releve.pop.ClientMail;
import factory.DeplaceOuSupprFactory;
import fenetre.Patience;

public class Thread_Releve extends Thread {
	private final String TAG = this.getClass().getSimpleName();

	private final MlListeCompteMail listeDeCompte;

	private final Patience fenetre;

	public Thread_Releve(MlListeCompteMail p_mlListeCompteMail) {// ,
		this.fenetre = new Patience("Releve de messagerie(s)");

		this.listeDeCompte = p_mlListeCompteMail;

	}

	@Override
	public void run() {
		fenetre.setVisible(true);
		for (MlCompteMail cpt : listeDeCompte) {
			definiCompteARelever(cpt);
		}
		fenetre.setVisible(false);

	}

	/**
	 * @param cpt
	 */
	private void definiCompteARelever(MlCompteMail cpt) {
		fenetre.afficheInfo("Releve du compte " + cpt.getNomCompte(), "", 0);

		switch (cpt.getTypeCompte()) {
			case POP:
				new ClientMail(cpt, fenetre);
				break;
			case GMAIL:
				new ReleveGmail(cpt, fenetre);
				break;
			case HOTMAIL:
				new ReleveHotmail(cpt, fenetre);
				break;
			case IMAP:
				break;
		}
	}

	public void SupprMessage(MlListeMessage p_listeMessageASupprimer) {
		MlCompteMail cpt = new MlCompteMail(p_listeMessageASupprimer.get(0)
				.getIdCompte());
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt,
				p_listeMessageASupprimer, fenetre);
		try {
			fact.supprMessage();

		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur à la suppression des messages dans la corbeille");
		}

	}

	public void DeplaceMessageVersCorbeille(MlListeMessage p_listMess) {
		MlCompteMail cpt = new MlCompteMail(p_listMess.get(0).getIdCompte());
		DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt, p_listMess,
				fenetre);

		try {
			fact.deplaceMessageVersCorbeille();
		} catch (MessagingException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur au deplacement des messages vers la corbeille");
		}

	}
}
