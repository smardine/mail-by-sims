/**
 * 
 */
package fenetre.principale.jtree;

import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeMessage;
import tools.GestionRepertoire;
import factory.JTableFactory;

/**
 * @author smardine
 */
public class CustomTreeSelectionListener implements TreeSelectionListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent p_e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) ComposantVisuelCommun
				.getJTree().getLastSelectedPathComponent();

		if (node == null) {
			// Nothing is selected.
			return;
		}

		Object userObject = node.getUserObject();
		if (userObject instanceof MlCompteMail) {
			JTableFactory tableFact = new JTableFactory();
			tableFact.refreshJTable(new MlListeMessage());
			((DefaultListModel) ComposantVisuelCommun.getJListPJ().getModel())
					.removeAllElements();
			Document doc = ComposantVisuelCommun.getHtmlPane().getDocument();
			doc.putProperty(Document.StreamDescriptionProperty, null);
			try {
				ComposantVisuelCommun.getHtmlPane().setPage(
						"file:///" + GestionRepertoire.RecupRepTemplate()
								+ "/vide.html");
				return;
			} catch (IOException e1) {
				return;
			}
		} else if (userObject instanceof MlDossier) {
			JTableFactory tableFact = new JTableFactory();
			tableFact.refreshJTable(((MlDossier) userObject).getListMessage());
			((DefaultListModel) ComposantVisuelCommun.getJListPJ().getModel())
					.removeAllElements();
			Document doc = ComposantVisuelCommun.getHtmlPane().getDocument();
			doc.putProperty(Document.StreamDescriptionProperty, null);
			try {
				ComposantVisuelCommun.getHtmlPane().setPage(
						"file:///" + GestionRepertoire.RecupRepTemplate()
								+ "/vide.html");
				return;
			} catch (IOException e1) {
				return;
			}

		}
	}

}
