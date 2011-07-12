package fenetre.comptes.gestion.MlActionGestion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fenetre.comptes.creation.CreationComptes;
import fenetre.comptes.gestion.GestionCompte;

public class MlActionComptes implements ActionListener {
	private final GestionCompte fenetre;

	public MlActionComptes(GestionCompte p_fenetre) {
		this.fenetre = p_fenetre;
	}

	@Override
	public void actionPerformed(ActionEvent p_arg0) {
		if (EnActionComptes.CREER.getLib().equals(p_arg0.getActionCommand())) {
			fenetre.dispose();
			new CreationComptes();
		}

	}

}
