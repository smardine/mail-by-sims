package fenetre.principale;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

import tools.RecupDate;

public class DateTimeCellRenderer extends DefaultTableCellRenderer {

	final static DateFormat format = RecupDate.formatPourTable;

	public DateTimeCellRenderer() {
		super();
	}

	@Override
	public void setValue(Object value) {
		Date date = (Date) value;
		super.setValue(format.format(date));
	}

}
