package fenetre.principale.MlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mdl.mlcomptemail.MlCompteMail;
import mdl.mlcomptemail.MlListeCompteMail;
import thread.Thread_Releve;
import bdd.accesTable.AccesTableCompte;

public class MlActionPopupButton implements ActionListener {

	public MlActionPopupButton() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		AccesTableCompte accesCompte = new AccesTableCompte();
		if ("[releve ]Relever tous les comptes".equals(actionCommand)) {

			Thread_Releve t = new Thread_Releve(accesCompte.getListeDeComptes());

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
