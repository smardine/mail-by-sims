package fenetre.principale.jTable;

import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import mdl.MlListeMessage;
import mdl.MlMessage;
import tools.RecupDate;
import bdd.BDRequette;

public class MyTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "n°", "Date reception",
			"Expediteur", "Sujet", "Piece Jointe", "Statut" };

	Class<?> types[] = new Class[] { Number.class, Date.class, String.class,
			String.class, ImageIcon.class, Boolean.class };
	private final XTableColumnModel coloneModel;

	// private final MlListeMessage rowValues = new MlListeMessage();
	public MyTableModel(XTableColumnModel p_coloneModel) {

		this.coloneModel = p_coloneModel;
		coloneModel.addColumn(new TableColumn(0), "n°");
		coloneModel.addColumn(new TableColumn(1), "Date de reception");
		coloneModel.addColumn(new TableColumn(2), "Expediteur");
		coloneModel.addColumn(new TableColumn(3), "Sujet");
		coloneModel.addColumn(new TableColumn(4), "Piece Jointe");
		coloneModel.addColumn(new TableColumn(5), "Statut");

		TableColumn columnId = coloneModel.getColumn(0);
		TableColumn columnDate = coloneModel.getColumn(1);
		TableColumn columnExpediteur = coloneModel.getColumn(2);
		TableColumn columnSujet = coloneModel.getColumn(3);
		TableColumn columnPieceJointe = coloneModel.getColumn(4);
		TableColumn columnStatus = coloneModel.getColumn(5);

		// int largeurJTable = EnNomComposant.PANEL_TABLE_ET_LISTE
		// .getLargeurInitiale();

		columnId.setMinWidth(0);
		columnId.setMaxWidth(0);
		columnId.setPreferredWidth(0);

		columnDate.setMinWidth(120);
		columnDate.setWidth(120);
		columnDate.setMaxWidth(120);
		columnDate.setPreferredWidth(120);

		columnExpediteur.setMinWidth(150);
		columnExpediteur.setWidth(150);
		columnExpediteur.setPreferredWidth(150);

		columnSujet.setMinWidth(200);
		columnSujet.setWidth(200);
		columnSujet.setPreferredWidth(200);

		columnPieceJointe.setMinWidth(20);
		columnPieceJointe.setWidth(20);
		columnPieceJointe.setPreferredWidth(20);
		columnPieceJointe.setMaxWidth(20);

		columnPieceJointe.setHeaderRenderer(new IconRenderer());
		columnPieceJointe.setHeaderValue(new txtIcon("", new ImageIcon(
				getClass().getResource("/piece_jointe16.png"))));

		columnStatus.setMinWidth(20);
		columnStatus.setWidth(20);
		columnStatus.setPreferredWidth(20);
		columnStatus.setMaxWidth(20);

		columnStatus.setHeaderRenderer(new IconRenderer());
		columnStatus.setHeaderValue(new txtIcon("", new ImageIcon(getClass()
				.getResource("/lu16.png"))));

	}

	private Object[][] data;

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getColumnClass(int c) {
		return types[c];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	// /*
	// * Don't need to implement this method unless your table's editable.
	// */
	// @Override
	// public boolean isCellEditable(int row, int col) {
	// // Note that the data/cell address is constant,
	// // no matter where the cell appears onscreen.
	// // if (col < 2) {
	// // return false;
	// // } else {
	// // return true;
	// // }
	// return false;
	// }

	public void valorisetable(MlListeMessage p_liste) {
		data = new Object[p_liste.size()][columnNames.length];
		if (p_liste.size() == 0) {
			fireTableDataChanged();
			return;
		}
		BDRequette bd = new BDRequette();
		for (int i = 0; i < p_liste.size(); i++) {// on parcour la liste des
			// messages
			MlMessage m = p_liste.get(i);
			// numero de message
			data[i][0] = m.getIdMessage();
			// date de reception
			data[i][1] = RecupDate.getDatepourTable(m.getDateReception());
			// expediteur
			data[i][2] = m.getExpediteur();
			// sujet
			data[i][3] = m.getSujet();

			if (bd.messageHavePieceJointe(m.getIdMessage())) {
				data[i][4] = new ImageIcon(getClass().getResource(
						"/piece_jointe16.png"));
			} else {
				data[i][4] = null;
			}
			if (bd.isMessageLu(m.getIdMessage())) {
				data[i][5] = Boolean.TRUE;
			} else {
				data[i][5] = Boolean.FALSE;
			}
			fireTableRowsInserted(i, i);

		}

		fireTableDataChanged();

	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
