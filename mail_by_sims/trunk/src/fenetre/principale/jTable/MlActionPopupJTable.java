package fenetre.principale.jTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

public class MlActionPopupJTable implements ActionListener {

	private final JTable table;

	public MlActionPopupJTable(JTable p_table) {
		this.table = p_table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		int selectedLine = table.getSelectedRow();

		table.getModel().getValueAt(selectedLine, 0);// le n° du message (meme
		// si il est caché.
		table.getModel().getValueAt(selectedLine, 1);// la date de reception

		table.getModel().getValueAt(selectedLine, 2);// l'expediteur

		table.getModel().getValueAt(selectedLine, 3);// le sujet du message

		if ("SUPPRIMER".equals(actionCommand)) {

		}

	}

}
