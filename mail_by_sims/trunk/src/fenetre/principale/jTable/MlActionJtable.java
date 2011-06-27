package fenetre.principale.jTable;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.swing.JEditorPane;
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

	private final JEditorPane editor;

	public MlActionJtable(JTable p_table, JEditorPane p_editor) {
		this.table = p_table;
		this.popUpMenu = getJPopupMenu();
		this.editor = p_editor;
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
			afficheContenuMail(table);
		}

	}

	private void afficheContenuMail(JTable table) {
		int selectedLine = table.getSelectedRow();

		Integer idMessage = (Integer) table.getModel().getValueAt(selectedLine,
				0);
		// le n° du message (meme si il est caché).
		Date dateReception = (Date) table.getModel()
				.getValueAt(selectedLine, 1);// la date de reception

		String expediteur = (String) table.getModel().getValueAt(selectedLine,
				2);// l'expediteur

		String sujet = (String) table.getModel().getValueAt(selectedLine, 3);// le
		// sujet

		File contenu = BDRequette.getContenuFromId(idMessage);
		try {
			// on RAZ le contenu du panelEditor
			Document doc = editor.getDocument();
			doc.putProperty(Document.StreamDescriptionProperty, null);

			editor.setPage(new URL("file:///" + contenu.getAbsolutePath()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
