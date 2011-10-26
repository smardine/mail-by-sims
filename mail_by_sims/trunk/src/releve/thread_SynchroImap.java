package releve;

import javax.mail.MessagingException;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import mdl.MlListeMessage;
import releve.hotmail.ReleveHotmail;
import releve.imap.ReleveGmail;
import releve.imap.util.messageUtilisateur;
import releve.pop.ClientMail;
import factory.DeplaceOuSupprFactory;

public class thread_SynchroImap extends Thread {

	private static JProgressBar progress;
	private final JTextArea textArea;
	private final boolean isSynchro;
	private final JProgressBar progressPieceJointe;
	private final JScrollPane scrollPane;
	private final MlListeCompteMail listeDeCompte;
	private final String TAG = this.getClass().getSimpleName();;

	public thread_SynchroImap(JProgressBar p_progressBarReleve,
			JProgressBar p_progressPieceJointe, JTextArea jTextArea,
			JScrollPane p_scroll, boolean p_isSynchro,
			MlListeCompteMail p_mlListeCompteMail) {// ,

		this.textArea = jTextArea;
		thread_SynchroImap.progress = p_progressBarReleve;
		this.progressPieceJointe = p_progressPieceJointe;
		this.isSynchro = p_isSynchro;
		this.scrollPane = p_scroll;
		this.listeDeCompte = p_mlListeCompteMail;

	}

	@Override
	public void run() {
		initComposantVisuel(true);

		for (MlCompteMail cpt : listeDeCompte) {
			definiCompteARelever(cpt);
		}

		initComposantVisuel(false);

	}

	/**
	 * @param cpt
	 */
	private void definiCompteARelever(MlCompteMail cpt) {
		switch (cpt.getTypeCompte()) {
			case POP:
				messageUtilisateur.afficheText(textArea, "Releve du compte "
						+ cpt.getNomCompte());
				new ClientMail(cpt, progress, progressPieceJointe, textArea);
				break;
			case GMAIL:
				messageUtilisateur.afficheText(textArea, "Releve du compte "
						+ cpt.getNomCompte());
				new ReleveGmail(cpt.getIdCompte(), cpt.getUserName(), cpt
						.getPassword(), cpt.getServeurReception(), progress,
						progressPieceJointe, textArea, isSynchro);
				break;
			case HOTMAIL:
				messageUtilisateur.afficheText(textArea, "Releve du compte "
						+ cpt.getNomCompte());
				new ReleveHotmail(cpt, /* cpt.getServeurReception(), */
				progress, progressPieceJointe, textArea, isSynchro);
				break;
			case IMAP:
				break;
		}
	}

	public void ReleveOuSynchroParticulier(MlCompteMail p_compteMail) {
		initComposantVisuel(true);

		definiCompteARelever(p_compteMail);

		initComposantVisuel(false);

	}

	/**
	 * 
	 */
	private void initComposantVisuel(boolean p_visible) {
		textArea.setText("");
		progress.setValue(0);
		progress.setString("");
		textArea.setVisible(p_visible);
		scrollPane.setVisible(p_visible);
		progress.setVisible(p_visible);
	}

	public void SupprMessage(MlListeMessage p_listeMessageASupprimer) {
		MlCompteMail cpt = new MlCompteMail(p_listeMessageASupprimer.get(0)
				.getIdCompte());
		switch (cpt.getTypeCompte()) {
			case GMAIL:
				DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt,
						p_listeMessageASupprimer, progress);
				try {
					fact.supprMessage();
				} catch (MessagingException e) {
					messageUtilisateur
							.affMessageException(TAG, e,
									"erreur à la suppression des messages dans la corbeille");
				}
				// new MajServeurGmail(p_listeMessageASupprimer, cpt, progress,
				// textArea, true);
				break;
			case HOTMAIL:
				break;
			case IMAP:
				break;
			case POP:
				break;
		}

	}

	public void DeplaceMessageVersCorbeille(MlListeMessage p_listMess) {
		MlCompteMail cpt = new MlCompteMail(p_listMess.get(0).getIdCompte());
		switch (cpt.getTypeCompte()) {
			case GMAIL:
				DeplaceOuSupprFactory fact = new DeplaceOuSupprFactory(cpt,
						p_listMess, progress);
				try {
					fact.deplaceMessageVersCorbeille();
				} catch (MessagingException e) {
					messageUtilisateur
							.affMessageException(TAG, e,
									"erreur au deplacement des messages vers la corbeille");
				}
				// new MajServeurGmail(p_listMess, cpt, progress, textArea,
				// false);
				break;
			case HOTMAIL:
				break;
			case POP:
				break;
			case IMAP:

				break;
		}

	}
}
