/**
 * 
 */
package fenetre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 * @author smardine
 */
public class Patience extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JProgressBar jProgressBar = null;

	/**
	 * This is the default constructor
	 */
	public Patience(String p_titre) {
		super(p_titre);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setLocationRelativeTo(null);// on centre la fenetre
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setText("");
			jLabel.setSize(new Dimension(284, 35));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(getJProgressBar(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jProgressBar
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setStringPainted(true);
			jProgressBar.setBackground(new Color(238, 238, 238));
			jProgressBar.setSize(new Dimension(284, 25));
			jProgressBar.setLocation(new Point(0, 58));
			jProgressBar.setForeground(Color.blue);
		}
		return jProgressBar;
	}

	/**
	 * @return the jLabel
	 */
	public JLabel getjLabel() {
		return this.jLabel;
	}

	/**
	 * @return the jProgressBar
	 */
	public JProgressBar getjProgressBar() {
		return this.jProgressBar;
	}

}
