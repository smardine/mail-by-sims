package thread;

import mdl.mlcomptemail.MlCompteMail;
import mdl.mlcomptemail.MlListeCompteMail;
import releve.hotmail.ReleveHotmail;
import releve.imap.ReleveGmail;
import releve.pop.ClientMail;
import fenetre.Patience;

public class Thread_Releve extends Thread {

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
				new ReleveGmail(cpt, fenetre);
				break;
		}
	}

}
