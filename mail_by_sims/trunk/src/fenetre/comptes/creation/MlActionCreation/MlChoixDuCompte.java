package fenetre.comptes.creation.MlActionCreation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.creation.CreationComptesGmailHotmail;
import fenetre.comptes.creation.CreationComptesPop;
import fenetre.comptes.creation.choixFAI;

public class MlChoixDuCompte implements ActionListener {

	// private final EnDefFournisseur choixFournisseur;
	// private final JTree tree;
	private final choixFAI fenetre;

	public MlChoixDuCompte(choixFAI p_fenetre) {
		this.fenetre = p_fenetre;
		// this.choixFournisseur = p_defFournisseur;
		// this.tree = p_tree;
	}

	@Override
	public void actionPerformed(ActionEvent p_e) {
		EnDefFournisseur enumChoisi = EnDefFournisseur.getEnumFromLib(p_e
				.getActionCommand());
		switch (enumChoisi) {
			case GMAIL:
				new CreationComptesGmailHotmail(enumChoisi);
				break;

			case HOTMAIL:
				new CreationComptesGmailHotmail(enumChoisi);
				break;
			default:
				new CreationComptesPop(enumChoisi);
				break;

		}
		fenetre.dispose();

	}

}
