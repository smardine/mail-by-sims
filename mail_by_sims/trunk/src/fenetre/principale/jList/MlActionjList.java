/**
 * 
 */
package fenetre.principale.jList;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import fenetre.principale.MlAction.EnActionMain;

/**
 * @author smardine
 */
public class MlActionjList implements MouseListener {

	private final JTable table;
	private final JList list;
	private JPopupMenu popUpMenu;
	private JMenuItem Ouvrir;
	private JMenuItem Enregistrer;
	private JMenuItem ToutEnregistrer;
	private final MlActionPopupJList actionPopup;

	public MlActionjList(JTable p_table, JList p_list) {
		this.table = p_table;
		this.list = p_list;
		actionPopup = new MlActionPopupJList(table, list);
		popUpMenu = getJPopupMenu();

	}

	/**
	 * This method initializes jPopupMenu
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenu() {
		if (popUpMenu == null) {
			popUpMenu = new JPopupMenu();
			popUpMenu.add(getOuvrir());
			popUpMenu.add(getEnregistrer());
			popUpMenu.add(getToutEnregistrer());

		}
		return popUpMenu;
	}

	/**
	 * This method initializes Ouvrir
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOuvrir() {
		if (Ouvrir == null) {
			Ouvrir = new JMenuItem();
			Ouvrir.setText(EnActionMain.OUVRIR_PJ.getLib());
			Ouvrir.setActionCommand(EnActionMain.OUVRIR_PJ.getLib());
			Ouvrir.addActionListener(actionPopup);
		}
		return Ouvrir;
	}

	private JMenuItem getEnregistrer() {
		if (Enregistrer == null) {
			Enregistrer = new JMenuItem();
			Enregistrer.setText(EnActionMain.ENREGISTRER_PJ.getLib());
			Enregistrer.setActionCommand(EnActionMain.ENREGISTRER_PJ.getLib());
			Enregistrer.addActionListener(actionPopup);

		}
		return Enregistrer;
	}

	private JMenuItem getToutEnregistrer() {
		if (ToutEnregistrer == null) {
			ToutEnregistrer = new JMenuItem();
			ToutEnregistrer.setText(EnActionMain.TOUT_ENREGISTRER_PJ.getLib());
			ToutEnregistrer.setActionCommand(EnActionMain.TOUT_ENREGISTRER_PJ
					.getLib());
			ToutEnregistrer.addActionListener(actionPopup);
		}
		return ToutEnregistrer;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent p_e) {
		if (p_e.getClickCount() == 2) {// double click sur la ligne

			actionPopup.traiteOuvrirPJ();
			// int idMessage = MlActionPopupJTable.getReelIdMessage(table
			// .getSelectedRow());
			// String nomPieceJointe = (String) list.getSelectedValue();
			//
			// BDRequette bd = new BDRequette();
			// File fichier = bd.getPieceJointeFromIDMessage(idMessage,
			// nomPieceJointe);
			// OpenWithDefaultViewer.open(fichier.getAbsolutePath());

		}

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent p_e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent p_e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent p_e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent p_e) {
		// Point p = p_e.getPoint();
		//
		// // get the row index that contains that coordinate
		// int rowNumber = table.rowAtPoint(p);

		// Get the ListSelectionModel of the JTable
		// ListSelectionModel model = table.getSelectionModel();

		// set the selected interval of rows. Using the "rowNumber"
		// variable for the beginning and end selects only that one row.
		// model.setSelectionInterval(rowNumber, rowNumber);

		if (p_e.isPopupTrigger()) {
			popUpMenu.show(p_e.getComponent(), p_e.getX(), p_e.getY());
		}

	}

}
