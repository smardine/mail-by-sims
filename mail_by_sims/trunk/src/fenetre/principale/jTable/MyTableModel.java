package fenetre.principale.jTable;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import mdl.MlListeMessageGrille;
import mdl.MlMessageGrille;
import releve.imap.util.messageUtilisateur;
import exception.DonneeAbsenteException;

public class MyTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "n°", "Date reception",
			"Expediteur", "Sujet", "Piece Jointe", "Statut" };
	private final int COL_ID_MESS = 0, COL_DATE_REC = 1, COL_EXPE = 2,
			COL_SUJET = 3, COL_PJ = 4, COL_STATU = 5;

	Class<?> types[] = new Class[] { Number.class, Date.class, String.class,
			String.class, ImageIcon.class, Boolean.class };
	private final XTableColumnModel coloneModel;

	private Vector<MlMessageGrille> vData;
	private final String TAG = this.getClass().getSimpleName();
	private final ImageIcon IconePJ = new ImageIcon(getClass().getResource(
			"/piece_jointe16.png"));

	// private Object[][] data;

	// private final MlListeMessage rowValues = new MlListeMessage();
	public MyTableModel(XTableColumnModel p_coloneModel) {

		this.coloneModel = p_coloneModel;
		coloneModel.addColumn(new TableColumn(COL_ID_MESS), "n°");
		coloneModel.addColumn(new TableColumn(COL_DATE_REC),
				"Date de reception");
		coloneModel.addColumn(new TableColumn(COL_EXPE), "Expediteur");
		coloneModel.addColumn(new TableColumn(COL_SUJET), "Sujet");
		coloneModel.addColumn(new TableColumn(COL_PJ), "Piece Jointe");
		coloneModel.addColumn(new TableColumn(COL_STATU), "Statut");

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

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return vData.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return types[c];
	}

	public Object getValueAt(int row, int col) {

		MlMessageGrille aRow = vData.get(row);

		switch (col) {
			case COL_ID_MESS:
				return aRow.getIdMessage();
			case COL_DATE_REC:
				return aRow.getDateReception();
			case COL_EXPE:
				return aRow.getExpediteur();
			case COL_SUJET:
				return aRow.getSujet();
			case COL_PJ:
				if (aRow.isHavePieceJointe()) {
					return IconePJ;
				}
				return null;
			case COL_STATU:
				return aRow.isLu();
		}
		// return vData.get(row).data[row][col];
		return aRow;
	}

	public void valorisetable(final MlListeMessageGrille p_liste) {
		vData = new Vector<MlMessageGrille>(p_liste.size());
		// data = new Object[p_liste.size()][columnNames.length];
		if (p_liste.size() == 0) {
			fireTableDataChanged();
			return;
		}
		for (MlMessageGrille m : p_liste) {
			vData.add(m);
			fireTableRowsInserted(0, 0);
		}

		// fireTableDataChanged();

	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		MlMessageGrille aRow = vData.get(row);
		switch (col) {
			case COL_ID_MESS:
			case COL_DATE_REC:
			case COL_EXPE:
			case COL_SUJET:
			case COL_PJ:
				messageUtilisateur.affMessageException(TAG,
						new DonneeAbsenteException(TAG,
								"Interdit de modifier ces données"),
						"Erreur de programmation");
			case COL_STATU:
				Boolean statut = (Boolean) value;
				aRow.setLu(statut);
				break;
		}

		fireTableCellUpdated(row, col);
	}

	/**
	 * permet de supprimer une ligne dans une Jtable a partir de son index (de 0
	 * à jTable.getRowCount)
	 * @param p_rows tableau de int correspondant à l'index de la ligne a
	 *            supprimer
	 */
	public void removeMessagesRows(int[] p_rows) {
		Arrays.sort(p_rows);
		for (int i = p_rows.length - 1; i >= 0; i--) {
			this.vData.remove(p_rows[i]);
			fireTableRowsDeleted(p_rows[i], p_rows[i]);

		}

	}
}
