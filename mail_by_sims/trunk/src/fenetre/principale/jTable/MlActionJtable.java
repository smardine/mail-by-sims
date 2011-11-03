package fenetre.principale.jTable;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.text.Document;

import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
import fenetre.LectureMessagePleinEcran;
import fenetre.principale.MlAction.EnActionMain;

public class MlActionJtable implements MouseListener, ActionListener {

	private JPopupMenu popUpMenu;
	private JMenuItem CreerRegle;
	private JMenuItem Supprimer;
	private final JTable table;
	private final JList jList;

	private final JProgressBar progressBar;

	private final JTextArea textArea;

	private final JScrollPane scroll;
	private JMenuItem MarquerSpam;

	private static JEditorPane editor;
	private static final String TAG = MlActionJtable.class.getSimpleName();

	public MlActionJtable(JTable p_table, JEditorPane jEditorPane, JList jList,
			JProgressBar p_progress, JTextArea p_text, JScrollPane p_scroll) {
		this.table = p_table;

		MlActionJtable.editor = jEditorPane;
		this.jList = jList;
		this.progressBar = p_progress;
		this.textArea = p_text;
		this.scroll = p_scroll;
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
			Supprimer.setText(EnActionMain.SUPPRIMER.getLib());
			Supprimer.setActionCommand(EnActionMain.SUPPRIMER.getLib());
			Supprimer.addActionListener(new MlActionPopupJTable(table, jList));
		}
		return Supprimer;
	}

	private JMenuItem getCreerRegle() {
		if (CreerRegle == null) {
			CreerRegle = new JMenuItem();
			CreerRegle.setText(EnActionMain.CREER_REGLE.getLib());
			CreerRegle.setActionCommand(EnActionMain.CREER_REGLE.getLib());
			CreerRegle.addActionListener(new MlActionPopupJTable(table, jList));
		}
		return CreerRegle;
	}

	private JMenuItem getMarquerSpam() {
		if (MarquerSpam == null) {
			MarquerSpam = new JMenuItem();
			MarquerSpam.setText(EnActionMain.MARQUER_SPAM.getLib());
			MarquerSpam.setActionCommand(EnActionMain.MARQUER_SPAM.getLib());
			MarquerSpam
					.addActionListener(new MlActionPopupJTable(table, jList));
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
		Point p = e.getPoint();

		// get the row index that contains that coordinate
		int rowNumber = table.rowAtPoint(p);

		// Get the ListSelectionModel of the JTable
		// ListSelectionModel model = table.getSelectionModel();

		// set the selected interval of rows. Using the "rowNumber"
		// variable for the beginning and end selects only that one row.
		// model.setSelectionInterval(rowNumber, rowNumber);

		if (e.isPopupTrigger()) {
			popUpMenu.show(e.getComponent(), e.getX(), e.getY());

		} else {
			if (table.getSelectedRowCount() == 1) {
				// on affiche le contenu du mail et on rafraichi le status de
				// lecture uniquement
				// si une seule ligne est selectionnée
				afficheContenuMail(table, jList);
				BDRequette bd = new BDRequette();
				boolean succes = bd.setStatusLecture(jTableHelper
						.getReelIdMessage(table, rowNumber));
				bd.closeConnexion();
				if (succes) {
					table.getModel().setValueAt(true, rowNumber,
							table.getModel().getColumnCount() - 1);
				}
			}

		}

	}

	public static void afficheContenuMail(JTable table, JList jList) {
		int selectedLine = table.getSelectedRow();
		if (selectedLine >= 0) {
			Integer idMessage = jTableHelper.getReelIdMessage(table,
					selectedLine);
			// le n° du message (meme si il est caché).
			BDRequette bd = new BDRequette();
			File contenu = bd.getContenuFromId(idMessage, false);

			// on RAZ le contenu du panelEditor
			Document doc = editor.getDocument();
			doc.putProperty(Document.StreamDescriptionProperty, null);
			if (contenu != null) {
				try {
					editor.setPage("file:///" + contenu.getAbsolutePath());
					// affichage des piece jointe dans la liste (si il y en a)
					List<String> lstPj = bd.getListeNomPieceJointe(idMessage);
					DefaultListModel model = (DefaultListModel) jList
							.getModel();
					int nbLigne = model.getSize();
					if (nbLigne > 0) {// si la liste est deja repli, on la vide
						model.removeAllElements();
					}
					if (lstPj.size() > 0) {
						for (String s : lstPj) {
							model.addElement(s);
						}

					}
				} catch (IOException e) {
					messageUtilisateur.affMessageException(TAG, e,
							"impossible d'afficher le mail");
				}
			}

			bd.closeConnexion();
		} else {
			table.removeAll();
			jList.removeAll();
			Document doc = editor.getDocument();
			doc.putProperty(Document.StreamDescriptionProperty, null);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (table.getSelectedRowCount() == 1) {
			// on affiche le contenu du mail et on rafraichi le status de
			// lecture uniquement
			// si une seule ligne est selectionnée
			afficheContenuMail(table, jList);
			BDRequette bd = new BDRequette();
			boolean succes = bd.setStatusLecture(jTableHelper.getReelIdMessage(
					table, table.getSelectedRow()));
			bd.closeConnexion();
			if (succes) {
				table.getModel().setValueAt(true, table.getSelectedRow(),
						table.getModel().getColumnCount() - 1);
			}
		}

	}

}
