package fenetre.principale.jTable;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.text.Document;

import bdd.BDRequette;

public class MlActionJtable implements MouseListener {

	private JPopupMenu popUpMenu;

	private JMenuItem Supprimer;
	private final JTable table;
	private final JList jList;
	private final JEditorPane editor;

	public MlActionJtable(JTable p_table, JEditorPane jEditorPane, JList jList) {
		this.table = p_table;
		this.popUpMenu = getJPopupMenu();
		this.editor = jEditorPane;
		this.jList = jList;
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
			popUpMenu.show(e.getComponent(), e.getX(), e.getY());

		} else {
			afficheContenuMail(table, jList);
		}

	}

	private void afficheContenuMail(JTable table, JList jList) {
		int selectedLine = table.getSelectedRow();

		Integer idMessage = (Integer) table.getModel().getValueAt(selectedLine,
				0);
		// le n° du message (meme si il est caché).

		File contenu = BDRequette.getContenuFromId(idMessage);

		// on RAZ le contenu du panelEditor
		Document doc = editor.getDocument();
		doc.putProperty(Document.StreamDescriptionProperty, null);
		try {
			editor.setPage("file:///" + contenu.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// affichage des piece jointe dans la liste (si il y en a)
		ArrayList<String> lstPj = BDRequette.getListNomPieceJointe(idMessage);
		DefaultListModel model = (DefaultListModel) jList.getModel();
		int nbLigne = model.getSize();
		if (nbLigne > 0) {// si la liste est deja repli, on la vide
			model.removeAllElements();
		}
		if (lstPj.size() > 0) {
			for (String s : lstPj) {
				model.addElement(s);
			}

		}
	}

}
