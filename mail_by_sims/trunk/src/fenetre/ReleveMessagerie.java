package fenetre;

import imap.thread_SynchroImap;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ReleveMessagerie extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextArea jTextArea = null;
	private JProgressBar jProgressBar = null;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel = null;

	/**
	 * This is the default constructor
	 */
	public ReleveMessagerie(boolean p_isSynchro) {
		super();
		initialize();
		this.setVisible(true);
		thread_SynchroImap t = new thread_SynchroImap(jProgressBar, jTextArea,
				jLabel, p_isSynchro);
		t.start();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(390, 434);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle(EnTitreFenetre.RELEVE_MESSAGERIE.getLib());
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(30, 353, 315, 19));
			jLabel.setText("JLabel");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJProgressBar(), null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(jLabel, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextArea
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setWrapStyleWord(true);
			jTextArea.setLineWrap(true);
		}
		return jTextArea;
	}

	/**
	 * This method initializes jProgressBar
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setBounds(new Rectangle(29, 375, 318, 20));
			jProgressBar.setBackground(new Color(0, 41, 159));
			jProgressBar.setStringPainted(true);
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jScrollPane
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(29, 5, 319, 345));
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
