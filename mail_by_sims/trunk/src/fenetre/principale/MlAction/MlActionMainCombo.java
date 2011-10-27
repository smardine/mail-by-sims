package fenetre.principale.MlAction;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mdl.MlCompteMail;
import mdl.MlListeCompteMail;
import bdd.BDRequette;

public class MlActionMainCombo implements MouseListener {

	private JPopupMenu popUpMenu;
	private final JProgressBar progressReleve;
	private final JProgressBar progressPieceJointe;
	private final JTextArea textArea;
	private final JScrollPane scrollPane;

	public MlActionMainCombo(JProgressBar p_progressReleve,
			JProgressBar p_progressPieceJointe, JTextArea p_text,
			JScrollPane p_scroll) {
		this.progressReleve = p_progressReleve;
		this.progressPieceJointe = p_progressPieceJointe;
		this.textArea = p_text;
		this.scrollPane = p_scroll;
		this.popUpMenu = getJPopupMenu();
	}

	public void refreshPopup() {
		getJPopupMenu();
		return;
	}

	/**
	 * This method initializes jPopupMenu
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenu() {

		popUpMenu = new JPopupMenu();
		BDRequette bd = new BDRequette();
		MlListeCompteMail lstCpt = bd.getListeDeComptes();
		bd.closeConnexion();

		popUpMenu.add(creerNouveauItem("Relever tous les comptes"));

		for (MlCompteMail cpt : lstCpt) {
			popUpMenu.add(creerNouveauItem(cpt.getNomCompte()));
		}

		return popUpMenu;
	}

	private JMenuItem creerNouveauItem(String p_text) {

		JMenuItem popUpItem = new JMenuItem();
		popUpItem.setText(p_text);
		popUpItem.setActionCommand("[releve ]" + p_text);

		popUpItem.addActionListener(new MlActionPopupButton(progressReleve,
				progressPieceJointe, textArea, scrollPane));

		return popUpItem;
	}

	@Override
	public void mouseClicked(MouseEvent p_e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent p_e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent p_e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent p_e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent p_event) {
		popUpMenu.show(p_event.getComponent(), p_event.getX(), p_event.getY());

	}
}
