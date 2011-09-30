package fenetre.principale.jTable;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

import tools.RecupDate;

public class DateTimeCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4938643150850316302L;
	private static final DateFormat format = RecupDate.getFormatPourTable();

	public DateTimeCellRenderer() {
		super();
	}

	@Override
	public void setValue(Object value) {
		Date date = (Date) value;
		super.setValue(format.format(date));
	}

}
