package fenetre.principale.jTable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class MyTableModelListener implements TableModelListener {

	@Override
	public void tableChanged(TableModelEvent p_e) {
		// super(source, p_e.getFirstRow(), p_e.getLastRow(), p_e.getType());
		MyTableModel source = (MyTableModel) p_e.getSource();

		System.out.println(p_e.getFirstRow());
		p_e.getLastRow();
		switch (p_e.getType()) {
			case TableModelEvent.INSERT:
			case TableModelEvent.UPDATE:
				break;
			case TableModelEvent.DELETE:
				// source
				// .fireTableRowsDeleted(p_e.getFirstRow(), p_e
				// .getLastRow());

		}
		// TODO Auto-generated method stub

	}
}
