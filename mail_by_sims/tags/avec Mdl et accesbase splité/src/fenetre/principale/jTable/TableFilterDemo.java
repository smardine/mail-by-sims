package fenetre.principale.jTable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableFilterDemo extends JPanel {

	public static void main(String args[]) {
		JFrame frame = new JFrame("Sorting JTable");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyTableModelbis model = new MyTableModelbis();
		JTable table = new JTable(model);
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				model);
		table.setRowSorter(sorter);
		JScrollPane pane = new JScrollPane(table);
		frame.add(pane, BorderLayout.CENTER);
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Filter");
		panel.add(label, BorderLayout.WEST);
		final JTextField filterText = new JTextField("Y");
		panel.add(filterText, BorderLayout.CENTER);
		frame.add(panel, BorderLayout.NORTH);
		JButton button = new JButton("Filter");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = filterText.getText();
				try {
					sorter.setRowFilter(RowFilter.regexFilter(text));
				} catch (PatternSyntaxException pse) {
					System.err.println("Bad regex pattern");
				}
			}
		});
		frame.add(button, BorderLayout.SOUTH);
		frame.setSize(300, 250);
		frame.setVisible(true);
	}
}

class MyTableModelbis extends DefaultTableModel {
	Object rows[][] = { { "A", "A", 1 }, { "E", "E", 4 }, { "Y", "Y", 3 } };

	String columns[] = { "Symbol", "Name", "Price" };

	@Override
	public Class getColumnClass(int column) {
		Class returnValue;
		if ((column >= 0) && (column < getColumnCount())) {
			returnValue = getValueAt(0, column).getClass();
		} else {
			returnValue = Object.class;
		}
		return returnValue;
	}
}
