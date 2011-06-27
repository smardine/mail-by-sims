package fenetre.principale;

import importMail.MlListeMessage;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.TreePath;

import Verif.Thread_Verif;
import bdd.BDAcces;
import fenetre.EnTitreFenetre;
import fenetre.SizeAndPosition;
import fenetre.principale.MlAction.EnActionMain;
import fenetre.principale.MlAction.MlActionMain;
import fenetre.principale.jTable.MlActionJtable;
import fenetre.principale.jTable.MyTableModel;
import fenetre.principale.jTable.XTableColumnModel;
import fenetre.principale.jtree.ArborescenceBoiteMail;
import fenetre.principale.jtree.MlActionJtree;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JMenuBar jJMenuBar = null;
	private JMenu jMenuFichier = null;
	private JMenu jMenuAide = null;
	private JMenuItem jMenuCompte = null;
	private JMenuItem jMenuQuitter = null;
	private JMenuItem jMenuExplorer = null;
	private JMenuItem jMenuHistorique = null;
	private JMenuItem jMenuContact = null;
	private static JTree jTree = null;
	private JEditorPane jEditorPane = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPaneEditor = null;
	private JTable jTable = null;
	private final MyTableModel tableModel;
	private static TreePath treePath; // @jve:decl-index=0:
	private static String nomCompte;
	private JMenu jMenuImportExport = null;
	private JMenuItem jMenuItemImporter = null;
	private static XTableColumnModel ColoneModel;

	/**
	 * This is the default constructor
	 */

	public Main() {
		super();
		new BDAcces();
		initialize();

		Thread_Verif verif = new Thread_Verif(jTree);
		verif.start();
		ColoneModel = new XTableColumnModel();
		jTable.setColumnModel(ColoneModel);
		tableModel = new MyTableModel(new MlListeMessage(), ColoneModel);
		jTable.setModel(tableModel);
		jTable.addMouseListener(new MlActionJtable(jTable, jEditorPane));
		jMenuContact
				.addActionListener(new MlActionMain(tableModel, jEditorPane));
		jTree.addMouseListener(new MlActionJtree(jTree, jTable));
		jTree.addTreeSelectionListener(new MlActionJtree(jTree, jTable));
		jTree.addTreeExpansionListener(new MlActionJtree(jTree, jTable));
		jMenuItemImporter.addActionListener(new MlActionMain(jTree));

	}

	/**
	 * This method initializes jTree
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			// chargement du model du jTree
			new BDAcces();
			ArborescenceBoiteMail arbo = new ArborescenceBoiteMail();
			jTree = new JTree(arbo);
			jTree.setLocation(new Point(0, 1));
			jTree.setShowsRootHandles(true);
			jTree.setRootVisible(false);
			jTree.setSize(new Dimension(264, 281));
			jTree.setToggleClickCount(1);
			jTree.setExpandsSelectedPaths(true);
			jTree.setSize(SizeAndPosition.JTREE.getHauteur(),
					SizeAndPosition.JTREE.getLargeur());

		}
		return jTree;
	}

	/**
	 * This method initializes jEditorPane
	 * @return javax.swing.JEditorPane
	 * @throws IOException
	 */
	private JEditorPane getJEditorPane() {

		jEditorPane = new JEditorPane();

		jEditorPane.setComponentOrientation(ComponentOrientation.UNKNOWN);
		jEditorPane.setFont(new Font("Dialog", Font.PLAIN, 12));
		jEditorPane.setContentType("text/html");
		jEditorPane.setEditable(true);

		return jEditorPane;
	}

	/**
	 * This method initializes jScrollPane
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setLocation(new Point(SizeAndPosition.JTABLE
					.getPositionHorizontale(), SizeAndPosition.JTABLE
					.getPositionVerticale()));
			jScrollPane.setSize(new Dimension(SizeAndPosition.JTABLE
					.getHauteur(), SizeAndPosition.JTABLE.getLargeur()));
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	private JScrollPane getScrollPaneEditor() {
		if (jScrollPaneEditor == null) {
			jScrollPaneEditor = new JScrollPane();
			jScrollPaneEditor.setLocation(new Point(SizeAndPosition.JEDITOR
					.getPositionHorizontale(), SizeAndPosition.JEDITOR
					.getPositionVerticale()));
			jScrollPaneEditor.setViewportBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
			jScrollPaneEditor.setViewportView(getJEditorPane());
			jScrollPaneEditor.setSize(new Dimension(SizeAndPosition.JEDITOR
					.getHauteur(), SizeAndPosition.JEDITOR.getLargeur()));
		}
		return jScrollPaneEditor;
	}

	/**
	 * This method initializes jTable
	 * @return javax.swing.JTable
	 */
	JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
			jTable.setAutoCreateColumnsFromModel(false);
			jTable.setColumnSelectionAllowed(true);
			jTable
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jTable.setShowVerticalLines(true);

		}
		return jTable;
	}

	/**
	 * This method initializes jMenuImportExport
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuImportExport() {
		if (jMenuImportExport == null) {
			jMenuImportExport = new JMenu();
			jMenuImportExport.add(getJMenuItemImporter());
			jMenuImportExport.setText(EnActionMain.IMPORT_EXPORT.getLib());
		}
		return jMenuImportExport;
	}

	/**
	 * This method initializes jMenuItemImporter
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemImporter() {
		if (jMenuItemImporter == null) {
			jMenuItemImporter = new JMenuItem();
			jMenuItemImporter.setText(EnActionMain.IMPORTER.getLib());
			jMenuItemImporter.setActionCommand(EnActionMain.IMPORTER.getLib());

		}
		return jMenuItemImporter;
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main thisClass = new Main();
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
		this.setSize(800, 600);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle(EnTitreFenetre.PRINCIPALE.getLib());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		this.setLocationRelativeTo(null);// on centre la fenetre

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/logo_appli.png")));
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJTree(), null);
			jContentPane.add(getJEditorPane(), null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getScrollPaneEditor(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.setPreferredSize(new Dimension(20, 25));
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
			jMenuFichier.add(getJMenuImportExport());
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

	/**
	 * @param treePath the treePath to set
	 */
	public static void setTreePath(TreePath treePath) {
		Main.treePath = treePath;
	}

	public static void setNomCompte(String p_compte) {
		Main.nomCompte = p_compte;
	}

	public static String getNomCompte() {
		return Main.nomCompte;
	}

	/**
	 * @return the treePath
	 */
	public static TreePath getTreePath() {
		return treePath;
	}

	public static XTableColumnModel getColumnModel() {
		return ColoneModel;
	}

}
