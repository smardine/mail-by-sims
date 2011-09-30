package fenetre.principale.jTable;

import javax.swing.JTable;

public final class jTableHelper {

	private jTableHelper() {

	}

	public static Integer getReelIdMessage(JTable p_table,
			int p_selectedIndexLine) {
		int row = p_table.convertRowIndexToModel(p_selectedIndexLine);
		return (Integer) p_table.getModel().getValueAt(row, 0);
	}

}
