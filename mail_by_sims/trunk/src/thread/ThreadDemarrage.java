/**
 * 
 */
package thread;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import factory.JTreeFactory;
import fenetre.principale.Main;
import fenetre.principale.Principale;
import fenetre.principale.jtree.ArborescenceBoiteMail;

/**
 * @author smardine
 */
public class ThreadDemarrage extends Thread {

	private final Main fenetre;
	private final JLabel label;
	private final JTextField textField;
	private final JProgressBar progressbar;

	public ThreadDemarrage(Main p_main, JLabel p_operationJLabel,
			JTextField p_jTextField, JProgressBar p_jProgressBar) {
		this.fenetre = p_main;
		this.label = p_operationJLabel;
		this.textField = p_jTextField;
		this.progressbar = p_jProgressBar;
	}

	@Override
	public void run() {
		label.setText("Bienvenue");
		textField.setText("Chargement en cours...");
		JTreeFactory treeFact = new JTreeFactory();
		ArborescenceBoiteMail arbo = new ArborescenceBoiteMail(treeFact
				.getTreeNode(fenetre, progressbar));
		new Principale(arbo);
		fenetre.setVisible(false);

	}

}
