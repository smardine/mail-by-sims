package fenetre.principale.jTable;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class IconRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column) {
		txtIcon i = (txtIcon) obj;
		if (obj == i) {
			setIcon(i.imageIcon);
			setText(i.txt);
		}
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(JLabel.CENTER);

		return this;
	}
}

class txtIcon {
	String txt;
	ImageIcon imageIcon;

	txtIcon(String text, ImageIcon icon) {
		txt = text;
		imageIcon = icon;
	}
}