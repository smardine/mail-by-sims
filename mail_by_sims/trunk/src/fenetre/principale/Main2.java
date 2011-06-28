package fenetre.principale;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import fenetre.principale.MlAction.EnActionMain;
import fenetre.principale.MlAction.MlActionMain;

public class Main2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JDesktopPane jDesktopPaneHaut = null;
	private JDesktopPane jDesktopPaneGauche = null;
	private JDesktopPane jDesktopPaneDroite = null;
	private JDesktopPane jDesktopPaneBas = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JTree jTree = null;
	private JEditorPane jEditorPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JList jList = null;
	private JMenuBar jJMenuBar;
	private JMenu jMenuFichier;
	private JMenu jMenuAide; // @jve:decl-index=0:
	private JMenuItem jMenuCompte;
	private JMenuItem jMenuQuitter;
	private JMenuItem jMenuExplorer;
	private JMenuItem jMenuHistorique;
	private JMenuItem jMenuContact;

	/**
	 * This method initializes jDesktopPaneHaut
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneHaut() {
		if (jDesktopPaneHaut == null) {
			jDesktopPaneHaut = new JDesktopPane();
			jDesktopPaneHaut
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jDesktopPaneHaut.setPreferredSize(new Dimension(600, 50));
			jDesktopPaneHaut.add(getJButton(), null);
			jDesktopPaneHaut.add(getJButton1(), null);
			jDesktopPaneHaut.add(getJButton2(), null);
		}
		return jDesktopPaneHaut;
	}

	/**
	 * This method initializes jDesktopPaneGauche
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneGauche() {
		if (jDesktopPaneGauche == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(1);
			borderLayout.setVgap(1);
			jDesktopPaneGauche = new JDesktopPane();
			jDesktopPaneGauche.setLayout(borderLayout);
			jDesktopPaneGauche.setPreferredSize(new Dimension(310, 300));
			jDesktopPaneGauche.setMinimumSize(new Dimension(310, 300));
			jDesktopPaneGauche.add(getJTree(), BorderLayout.CENTER);
		}
		return jDesktopPaneGauche;
	}

	/**
	 * This method initializes jDesktopPaneDroite
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneDroite() {
		if (jDesktopPaneDroite == null) {
			jDesktopPaneDroite = new JDesktopPane();
			jDesktopPaneDroite.setPreferredSize(new Dimension(465, 50));
			jDesktopPaneDroite.setLayout(new BorderLayout());
			jDesktopPaneDroite.add(getJScrollPane(), BorderLayout.CENTER);
			jDesktopPaneDroite.add(getJList(), BorderLayout.SOUTH);
		}
		return jDesktopPaneDroite;
	}

	/**
	 * This method initializes jDesktopPaneBas
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneBas() {
		if (jDesktopPaneBas == null) {
			jDesktopPaneBas = new JDesktopPane();
			jDesktopPaneBas.setPreferredSize(new Dimension(50, 250));
			jDesktopPaneBas.add(getJEditorPane(), null);
		}
		return jDesktopPaneBas;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(9, 6, 59, 19));
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(78, 6, 55, 20));
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new Rectangle(143, 7, 71, 21));
		}
		return jButton2;
	}

	/**
	 * This method initializes jTree
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new JTree();
			jTree.setBounds(new Rectangle(3, 5, 290, 255));
		}
		return jTree;
	}

	/**
	 * This method initializes jEditorPane
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPane() {
		if (jEditorPane == null) {
			jEditorPane = new JEditorPane();
			jEditorPane.setBounds(new Rectangle(0, 0, 0, 0));
		}
		return jEditorPane;
	}

	/**
	 * This method initializes jScrollPane
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable
	 * @return javax.swing.JTable
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
			jTable.setAutoCreateColumnsFromModel(false);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return jTable;
	}

	/**
	 * This method initializes jList
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new JList();

			jList.setBackground(Color.black);
			jList.setPreferredSize(new Dimension(50, 50));
		}
		return jList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main2 thisClass = new Main2();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public Main2() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		// Dimension screenSize = getToolkit().getScreenSize();
		// int width = screenSize.width * 8 / 10;
		// int height = screenSize.height * 8 / 10;
		// setBounds(width / 8, height / 8, width, height);
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(800, 600));
		this.setContentPane(getJContentPane());
		this.setJMenuBar(getJJMenuBar());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			// GridLayout gridLayout = new GridLayout();
			// gridLayout.setRows(4);
			// gridLayout.setColumns(2);
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(3);
			borderLayout1.setVgap(3);
			jContentPane = new JPanel();
			// jContentPane.setLayout();
			jContentPane.setLayout(borderLayout1);
			jContentPane.add(getJDesktopPaneHaut(), BorderLayout.NORTH);
			jContentPane.add(getJDesktopPaneGauche(), BorderLayout.WEST);
			jContentPane.add(getJDesktopPaneDroite(), BorderLayout.EAST);
			jContentPane.add(getJDesktopPaneBas(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.setPreferredSize(new Dimension(0, 25));
			jJMenuBar.add(getJMenuFichier());
			jJMenuBar.add(getJMenuAide());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenuFichier
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuFichier() {

		if (jMenuFichier == null) {
			jMenuFichier = new JMenu();
			jMenuFichier.setText("Fichier");
			jMenuFichier.add(getJMenuCompte());
			jMenuFichier.add(getJMenuQuitter());
		}
		return jMenuFichier;
	}

	/**
	 * This method initializes jMenuAide
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuAide() {
		if (jMenuAide == null) {
			jMenuAide = new JMenu();
			jMenuAide.setText("Aide");
			jMenuAide.add(getJMenuExplorer());
			jMenuAide.add(getJMenuHistorique());
			jMenuAide.add(getJMenuContact());
		}
		return jMenuAide;
	}

	/**
	 * This method initializes jMenuCompte
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuCompte() {
		if (jMenuCompte == null) {
			jMenuCompte = new JMenuItem();
			jMenuCompte.setText(EnActionMain.GESTION_COMPTE.getLib());
			jMenuCompte.setActionCommand(EnActionMain.GESTION_COMPTE.getLib());
			jMenuCompte.addActionListener(new MlActionMain(this));
		}
		return jMenuCompte;
	}

	/**
	 * This method initializes jMenuQuitter
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuQuitter() {
		if (jMenuQuitter == null) {
			jMenuQuitter = new JMenuItem();
			jMenuQuitter.setText(EnActionMain.QUITTER.getLib());
			jMenuQuitter.setActionCommand(EnActionMain.QUITTER.getLib());
			jMenuQuitter.addActionListener(new MlActionMain());
		}
		return jMenuQuitter;
	}

	/**
	 * This method initializes jMenuExplorer
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuExplorer() {
		if (jMenuExplorer == null) {
			jMenuExplorer = new JMenuItem();
			jMenuExplorer.setText(EnActionMain.EXPLORER.getLib());
			jMenuExplorer.setActionCommand(EnActionMain.EXPLORER.getLib());
			jMenuExplorer.addActionListener(new MlActionMain());
		}
		return jMenuExplorer;
	}

	/**
	 * This method initializes jMenuHistorique
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuHistorique() {
		if (jMenuHistorique == null) {
			jMenuHistorique = new JMenuItem();
			jMenuHistorique.setText(EnActionMain.HISTORIQUE.getLib());
			jMenuHistorique.setActionCommand(EnActionMain.HISTORIQUE.getLib());
			jMenuHistorique.addActionListener(new MlActionMain());
		}
		return jMenuHistorique;
	}

	/**
	 * This method initializes jMenuContact
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuContact() {
		if (jMenuContact == null) {
			jMenuContact = new JMenuItem();
			jMenuContact.setText(EnActionMain.CONTACT.getLib());
			jMenuContact.setActionCommand(EnActionMain.CONTACT.getLib());

		}
		return jMenuContact;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
