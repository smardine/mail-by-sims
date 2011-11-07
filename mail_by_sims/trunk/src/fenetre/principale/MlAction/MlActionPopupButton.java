package fenetre.principale.MlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import thread.Thread_Releve;
import bdd.BDRequette;

public class MlActionPopupButton implements ActionListener {

	public MlActionPopupButton() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		BDRequette bd = new BDRequette();
		if ("[releve ]Relever tous les comptes".equals(actionCommand)) {

			Thread_Releve t = new Thread_Releve(bd.getListeDeComptes());

			t.start();

		} else if (actionCommand.contains("[releve ]")) {
			String nomCompte = actionCommand.substring(actionCommand
					.lastIndexOf(']') + 1);

			MlListeCompteMail lst = new MlListeCompteMail();
			MlCompteMail cpt = new MlCompteMail(nomCompte);
			cpt.setNomCompte(nomCompte);
			lst.add(cpt);
			Thread_Releve t = new Thread_Releve(lst);
			t.start();

		}

	}

}
