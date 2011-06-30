package fenetre.principale.jTable;

import importMail.MlListeMessage;
import importMail.MlMessage;

import java.util.Date;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import tools.RecupDate;

public class MyTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "n°", "Date reception",
			"Expediteur", "Sujet" };

	Class<?> types[] = new Class[] { Number.class, Date.class, String.class,
			String.class };
	private final XTableColumnModel coloneModel;

	// private final MlListeMessage rowValues = new MlListeMessage();
	public MyTableModel(MlListeMessage p_liste, XTableColumnModel p_coloneModel) {

		valorisetable(p_liste);
		this.coloneModel = p_coloneModel;
		coloneModel.addColumn(new TableColumn(0), "n°");
		coloneModel.addColumn(new TableColumn(1), "Date de reception");
		coloneModel.addColumn(new TableColumn(2), "Expediteur");
		coloneModel.addColumn(new TableColumn(3), "Sujet");

		coloneModel.getColumn(0).setMinWidth(0);
		coloneModel.getColumn(0).setMaxWidth(0);
		coloneModel.getColumn(0).setPreferredWidth(0);
		coloneModel.getColumn(1).setWidth(150);
		coloneModel.getColumn(2).setWidth(200);
		coloneModel.getColumn(3).setWidth(500);

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
			// data[i][4] = BDRequette.messageHavePieceJointe(m.getIdMessage());

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
