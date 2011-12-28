/**
 * 
 */
package factory;

import javax.swing.JTable;

import mdl.ComposantVisuelCommun;
import mdl.mlmessage.MlListeMessageGrille;
import fenetre.principale.jTable.MyTableModel;

/**
 * @author smardine
 */
public class JTableFactory {

	private final JTable table;

	public JTableFactory() {
		this.table = ComposantVisuelCommun.getJTable();

	}

	public void refreshJTable(MlListeMessageGrille p_list) {
		MyTableModel modelDetable = (MyTableModel) table.getModel();
		modelDetable.valorisetable(p_list);
	}

}
