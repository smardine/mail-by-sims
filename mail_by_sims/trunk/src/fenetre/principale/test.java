package fenetre.principale;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import java.awt.Rectangle;
import javax.swing.JTree;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JMenuBar;

import fenetre.principale.MlAction.EnActionMain;
import fenetre.principale.MlAction.MlActionMain;


public class test extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JDesktopPane jDesktopPane = null;
	private JDesktopPane jDesktopPane1 = null;
	private JTree jTree = null;
	private JEditorPane jEditorPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JMenuBar jJMenuBar = null;
	private JMenuItem jMenuContact;
	private JMenuItem jMenuHistorique;
	private JMenuItem jMenuExplorer;
	private JMenuItem jMenuQuitter;
	private JMenuItem jMenuCompte;
	private JMenu jMenuAide; // @jve:decl-index=0:
	private JMenu jMenuFichier; // @jve:decl-index=0:

	/**
	 * This is the default constructor
	 */
	public test() {
		super();
		initialize();
	}

	/**
	 * This method initializes jJMenuBar
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.setPreferredSize(new Dimension(0, 25));
			jJMenuBar.add(getJMenuFichier());
			jJMenuBar.add(getJMenuAide());
		}
		return jJMenuBar;
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				test thisClass = new test();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(695, 238);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setColumns(2);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);
			jContentPane.add(getJDesktopPane(), null);
			jContentPane.add(getJDesktopPane1(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jDesktopPane
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPane() {
		if (jDesktopPane == null) {
			jDesktopPane = new JDesktopPane();
			jDesktopPane.setLayout(new BorderLayout());
			jDesktopPane.add(getJTree(), BorderLayout.CENTER);
		}
		return jDesktopPane;
	}

	/**
	 * This method initializes jDesktopPane1
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPane1() {
		if (jDesktopPane1 == null) {
			jDesktopPane1 = new JDesktopPane();
			jDesktopPane1.setLayout(new BoxLayout(getJDesktopPane1(),
					BoxLayout.Y_AXIS));
			jDesktopPane1.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
			jDesktopPane1.add(getJScrollPane(), null);
			jDesktopPane1.add(getJEditorPane(), null);
		}
		return jDesktopPane1;
	}

	/**
	 * This method initializes jTree
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new JTree();
			jTree.setBackground(Color.red);
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
			jEditorPane.setBackground(Color.orange);
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
			jScrollPane.setPreferredSize(new Dimension(128, 256));
			jScrollPane.setViewportBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
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
			jTable.setAutoCreateColumnsFromModel(true);
			jTable.setCellSelectionEnabled(true);
			jTable
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jTable.setShowGrid(false);
			jTable.setShowHorizontalLines(true);
			jTable.setRowHeight(16);
		}
		return jTable;
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
