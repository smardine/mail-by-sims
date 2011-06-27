package fenetre.principale.jTable;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class MlActionJtable implements MouseListener {

	private JPopupMenu popUpMenu;

	private JMenuItem Supprimer;
	private final JTable table;

	public MlActionJtable(JTable p_table) {
		this.table = p_table;
		this.popUpMenu = getJPopupMenu();
	}

	/**
	 * This method initializes jPopupMenu
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenu() {
		if (popUpMenu == null) {
			popUpMenu = new JPopupMenu();
			popUpMenu.add(getSupprimer());

		}
		return popUpMenu;
	}

	/**
	 * This method initializes Supprimer
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSupprimer() {
		if (Supprimer == null) {
			Supprimer = new JMenuItem();
			Supprimer.setText("Supprimer ce message");
			Supprimer.setActionCommand("SUPPRIMER");
			Supprimer.addActionListener(new MlActionPopupJTable(table));
		}
		return Supprimer;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point p = e.getPoint();

		// get the row index that contains that coordinate
		int rowNumber = table.rowAtPoint(p);

		// Get the ListSelectionModel of the JTable
		ListSelectionModel model = table.getSelectionModel();

		// set the selected interval of rows. Using the "rowNumber"
		// variable for the beginning and end selects only that one row.
		model.setSelectionInterval(rowNumber, rowNumber);

		if (e.isPopupTrigger()) {

			// Object lastPathComponent = selPath.getLastPathComponent();
			//
			// if (getListeDossierdeBase().contains(lastPathComponent)//
			// || BDRequette.getListeDeComptes().contains(
			// lastPathComponent)) {
			// Supprimer.setEnabled(false);
			// } else {
			// Supprimer.setEnabled(true);
			// }

			popUpMenu.show(e.getComponent(), e.getX(), e.getY());

		}

	}

}
