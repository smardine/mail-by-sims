package fenetre.principale;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import thread.ThreadDemarrage;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JLabel jLabel = null;
	private JLabel operation_jLabel = null;
	private JTextField jTextField = null;
	private JProgressBar jProgressBar = null;

	// private JPanel jPanel = null;
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(15, 35, 117, 119));
			jButton.setIcon(new ImageIcon(getClass().getResource(
					"/logo_appli.png")));
		}
		return jButton;
	}

	/**
	 * This method initializes jTextField
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setBounds(new Rectangle(149, 135, 357, 27));
			jTextField.setEnabled(true);
			jTextField.setEditable(false);
			jTextField.setVisible(false);
		}
		return jTextField;
	}

	/**
	 * This method initializes jProgressBar
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setBounds(new Rectangle(150, 175, 358, 18));
		}
		return jProgressBar;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main thisClass = new Main();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public Main() {
		super();
		initialize();

		ThreadDemarrage verifAuDemarrage = new ThreadDemarrage(this, jLabel,
				operation_jLabel, jProgressBar);
		verifAuDemarrage.start();

	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(526, 233);
		this.setPreferredSize(new Dimension(526, 233));
		this.setMaximumSize(new Dimension(526, 233));
		this.setMinimumSize(new Dimension(526, 233));
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/logo_appli.png")));
		this.setContentPane(getJContentPane());
		this.setLocationRelativeTo(null); // On centre la fenêtre sur l'écran
		this.setTitle("Démarrage");
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			operation_jLabel = new JLabel();
			operation_jLabel.setBounds(new Rectangle(150, 86, 355, 27));
			operation_jLabel.setText("Textmessage");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(150, 36, 136, 26));
			jLabel.setText(" Opération (s) en cours:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(operation_jLabel, null);
			jContentPane.add(getJTextField(), null);
			jContentPane.add(getJProgressBar(), null);

		}
		return jContentPane;
	}

}
