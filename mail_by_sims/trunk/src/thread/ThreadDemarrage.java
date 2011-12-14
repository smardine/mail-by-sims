/**
 * 
 */
package thread;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import factory.JTreeFactory;
import fenetre.principale.Main;
import fenetre.principale.Principale;
import fenetre.principale.jtree.ArborescenceBoiteMail;

/**
 * @author smardine
 */
public class ThreadDemarrage extends Thread {

	private final Main fenetre;
	private final JLabel labelPrincipal;
	private final JLabel labelSecondaire;
	private final JProgressBar progressbar;

	public ThreadDemarrage(Main p_main, JLabel p_labelPrincipal,
			JLabel p_labelSecondaire, JProgressBar p_jProgressBar) {
		this.fenetre = p_main;
		this.labelPrincipal = p_labelPrincipal;
		this.labelSecondaire = p_labelSecondaire;
		this.progressbar = p_jProgressBar;
	}

	@Override
	public void run() {
		labelPrincipal.setText("Bienvenue");
		labelSecondaire.setText("Chargement en cours...");
		JTreeFactory treeFact = new JTreeFactory();
		ArborescenceBoiteMail arbo = new ArborescenceBoiteMail(treeFact
				.getTreeNode(fenetre, progressbar));
		new Principale(arbo);
		fenetre.setVisible(false);

	}

}
