/**
 * 
 */
package factory;

import javax.swing.JList;
import javax.swing.JTable;

import mdl.ComposantVisuelCommun;
import mdl.MlListeMessage;
import fenetre.principale.jTable.MlActionJtable;
import fenetre.principale.jTable.MyTableModel;

/**
 * @author smardine
 */
public class JTableFactory {

	private final JTable table;
	private final JList list;

	public JTableFactory() {
		this.table = ComposantVisuelCommun.getJTable();
		this.list = ComposantVisuelCommun.getJListPJ();
	}

	public void refreshJTable(MlListeMessage p_list) {
		MyTableModel modelDetable = (MyTableModel) table.getModel();
		modelDetable.valorisetable(p_list);
		if (table.getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
		}

		MlActionJtable.afficheContenuMail(table, list);
	}

}
