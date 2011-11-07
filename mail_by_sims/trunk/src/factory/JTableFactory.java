/**
 * 
 */
package factory;

import javax.swing.JTable;

import mdl.ComposantVisuelCommun;
import mdl.MlListeMessage;
import fenetre.principale.jTable.MyTableModel;

/**
 * @author smardine
 */
public class JTableFactory {

	private final JTable table;

	public JTableFactory() {
		this.table = ComposantVisuelCommun.getJTable();

	}

	public void refreshJTable(MlListeMessage p_list) {
		MyTableModel modelDetable = (MyTableModel) table.getModel();
		modelDetable.valorisetable(p_list);
		if (table.getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
			MessageFactory messFact = new MessageFactory();
			messFact.afficheContenuMail(p_list.get(0));
		}

	}

}
