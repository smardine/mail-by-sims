package fenetre.principale.jTable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import mdl.MlMessage;
import thread.threadMarquageLu;
import factory.MessageFactory;
import fenetre.LectureMessagePleinEcran;
import fenetre.principale.MlAction.EnActionMain;

public class JTableMouseListener implements MouseListener {

	private JPopupMenu popUpMenu;
	private JMenuItem CreerRegle;
	private JMenuItem Supprimer;
	private final JTable table;

	private JMenuItem MarquerSpam;
	private JMenuItem MarquerLu;

	// private static final String TAG = MlActionJtable.class.getSimpleName();

	public JTableMouseListener(JTable p_table) {

		this.table = p_table;
		// MlActionJtable.editor = jEditorPane;
		// this.jList = jList;
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
			popUpMenu.add(getCreerRegle());
			popUpMenu.add(getMarquerSpam());
			popUpMenu.add(getMarquerLu());

		}
		return popUpMenu;
	}

	/**
	 * @return
	 */
	private JMenuItem getMarquerLu() {
		if (MarquerLu == null) {
			MarquerLu = new JMenuItem();
			MarquerLu.setText(EnActionMain.MARQUER_LU.getLib());
			MarquerLu.setActionCommand(EnActionMain.MARQUER_LU.getLib());
			MarquerLu.addActionListener(new MlActionPopupJTable(table));
		}
		return MarquerLu;
	}

	/**
	 * This method initializes Supprimer
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSupprimer() {
		if (Supprimer == null) {
			Supprimer = new JMenuItem();
			Supprimer.setText(EnActionMain.SUPPRIMER.getLib());
			Supprimer.setActionCommand(EnActionMain.SUPPRIMER.getLib());
			Supprimer.addActionListener(new MlActionPopupJTable(table));
		}
		return Supprimer;
	}

	private JMenuItem getCreerRegle() {
		if (CreerRegle == null) {
			CreerRegle = new JMenuItem();
			CreerRegle.setText(EnActionMain.CREER_REGLE.getLib());
			CreerRegle.setActionCommand(EnActionMain.CREER_REGLE.getLib());
			CreerRegle.addActionListener(new MlActionPopupJTable(table));
		}
		return CreerRegle;
	}

	private JMenuItem getMarquerSpam() {
		if (MarquerSpam == null) {
			MarquerSpam = new JMenuItem();
			MarquerSpam.setText(EnActionMain.MARQUER_SPAM.getLib());
			MarquerSpam.setActionCommand(EnActionMain.MARQUER_SPAM.getLib());
			MarquerSpam.addActionListener(new MlActionPopupJTable(table));
		}
		return MarquerSpam;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {// double click sur la ligne
			// on recupere la ligne selectionnée
			// Point p = e.getPoint();
			// get the row index that contains that coordinate
			// int rowNumber = table.rowAtPoint(p);
			int idMessage = jTableHelper.getReelIdMessage(table, table
					.getSelectedRow());

			// on clear le panelHTML de la page d'acceuil
			// Document doc = editor.getDocument();
			// doc.putProperty(Document.StreamDescriptionProperty, null);
			// editor.setDocument(doc);
			new LectureMessagePleinEcran(table, idMessage);

		}

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
		// Point p = e.getPoint();

		// get the row index that contains that coordinate
		// int rowNumber = table.rowAtPoint(p);

		if (e.isPopupTrigger()) {
			popUpMenu.show(e.getComponent(), e.getX(), e.getY());

		} else {
			if (table.getSelectedRowCount() == 1) {
				// on affiche le contenu du mail et on rafraichi le status de
				// lecture uniquement
				// si une seule ligne est selectionnée
				table.getModel().setValueAt(true, table.getSelectedRow(),
						table.getModel().getColumnCount() - 1);
				Integer idMessage = jTableHelper.getReelIdMessage(table, table
						.getSelectedRow());
				MlMessage m = new MlMessage(idMessage);
				MessageFactory messFact = new MessageFactory();
				messFact.afficheContenuMail(m);
				int[] tabID = new int[1];
				tabID[0] = m.getIdMessage();
				threadMarquageLu t = new threadMarquageLu(tabID);
				t.start();
				// AccesTableMailRecu accesMail = new AccesTableMailRecu();
				// boolean succes = accesMail.updateStatusLecture(m, true);
				//
				// JTreeFactory treeFact = new JTreeFactory();
				// treeFact.refreshJTree();
				// if (succes) {

				// }
			}

		}

	}

}
