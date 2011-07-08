package fenetre.principale;

import importMail.MlListeMessage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.TreePath;

import Verif.Thread_Verif;
import bdd.BDAcces;
import fenetre.EnTitreFenetre;
import fenetre.principale.MlAction.EnActionMain;
import fenetre.principale.MlAction.MlActionMain;
import fenetre.principale.jTable.DateTimeCellRenderer;
import fenetre.principale.jTable.MlActionJtable;
import fenetre.principale.jTable.MlActionPopupJTable;
import fenetre.principale.jTable.MyTableModel;
import fenetre.principale.jTable.XTableColumnModel;
import fenetre.principale.jtree.ArborescenceBoiteMail;
import fenetre.principale.jtree.MlActionJtree;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private static TreePath treePath;
	private static String nomCompte;
	private JPanel jContentPane = null;
	private JDesktopPane jDesktopPaneHaut = null;
	private JDesktopPane jDesktopPaneGauche = null;
	private JDesktopPane jDesktopPaneDroite = null;
	private JDesktopPane jDesktopPaneBas = null;
	private JButton btNewMessage = null;
	private JButton btSupprMessage = null;
	private JButton btEnvoyerRecevoir = null;
	private JTree jTree = null;
	private JEditorPane htmlPane = null;
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
	private static XTableColumnModel ColoneModel;
	private final MyTableModel tableModel;
	private JMenu jMenuImportExport;
	private JMenuItem jMenuItemImporter; // @jve:decl-index=0:
	private JScrollPane jScrollPane1 = null;
	private JMenu jMenuReleve = null;
	private JMenu jMenuEnvoiReception = null;
	private JMenuItem jMenuItemReleve = null;
	private DefaultListModel modelList = null;

	/**
	 * This method initializes jDesktopPaneHaut
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneHaut() {
		if (jDesktopPaneHaut == null) {
			jDesktopPaneHaut = new JDesktopPane();

			jDesktopPaneHaut
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jDesktopPaneHaut.setPreferredSize(new Dimension(
					EnNomComposant.PANEL_BOUTON.getLargeurInitiale(),
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			jDesktopPaneHaut.setMinimumSize(new Dimension(
					EnNomComposant.PANEL_BOUTON.getLargeurInitiale(),
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			jDesktopPaneHaut.setName(EnNomComposant.PANEL_BOUTON.getLib());
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
			jDesktopPaneGauche.setName(EnNomComposant.PANEL_TREE.getLib());
			jDesktopPaneGauche.setLayout(borderLayout);
			jDesktopPaneGauche.setPreferredSize(new Dimension(
					EnNomComposant.PANEL_TREE.getLargeurInitiale(),
					EnNomComposant.PANEL_TREE.getHauteurInitiale()));
			jDesktopPaneGauche.setMinimumSize(new Dimension(
					EnNomComposant.PANEL_TREE.getLargeurInitiale(),
					EnNomComposant.PANEL_TREE.getHauteurInitiale()));
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
			jDesktopPaneDroite.setPreferredSize(new Dimension(
					EnNomComposant.PANEL_TABLE_ET_LISTE.getLargeurInitiale(),
					EnNomComposant.PANEL_TABLE_ET_LISTE.getHauteurInitiale()));
			jDesktopPaneDroite.setMinimumSize(new Dimension(
					EnNomComposant.PANEL_TABLE_ET_LISTE.getLargeurInitiale(),
					EnNomComposant.PANEL_TABLE_ET_LISTE.getHauteurInitiale()));
			jDesktopPaneDroite.setLayout(new BorderLayout());
			jDesktopPaneDroite.setName(EnNomComposant.PANEL_TABLE_ET_LISTE
					.getLib());
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
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(1);
			borderLayout.setVgap(1);
			jDesktopPaneBas = new JDesktopPane();
			jDesktopPaneBas.setPreferredSize(new Dimension(
					EnNomComposant.PANEL_HTML.getLargeurInitiale(),
					EnNomComposant.PANEL_HTML.getHauteurInitiale()));
			jDesktopPaneBas.setMinimumSize(new Dimension(
					EnNomComposant.PANEL_HTML.getLargeurInitiale(),
					EnNomComposant.PANEL_HTML.getHauteurInitiale()));
			jDesktopPaneBas.setName(EnNomComposant.PANEL_HTML.getLib());
			jDesktopPaneBas.setLayout(borderLayout);
			jDesktopPaneBas.add(getJScrollPane1(), BorderLayout.CENTER);
		}
		return jDesktopPaneBas;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (btNewMessage == null) {
			btNewMessage = new JButton();
			btNewMessage.setLocation(new Point(1, 1));
			btNewMessage.setText("");
			btNewMessage.setIcon(new ImageIcon(getClass().getResource(
					"/nouveau_message.png")));
			btNewMessage.setSize(new Dimension(75, 50));
		}
		return btNewMessage;
	}

	/**
	 * This method initializes btSupprMessage
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (btSupprMessage == null) {
			btSupprMessage = new JButton();
			btSupprMessage.setLocation(new Point(75, 1));
			btSupprMessage.setText("");
			btSupprMessage.setActionCommand(EnActionMain.SUPPRIMER.getLib());
			btSupprMessage.setIcon(new ImageIcon(getClass().getResource(
					"/supprimer.png")));
			btSupprMessage.setSize(new Dimension(75, 50));
		}
		return btSupprMessage;
	}

	/**
	 * This method initializes btEnvoyerRecevoir
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (btEnvoyerRecevoir == null) {
			btEnvoyerRecevoir = new JButton();
			btEnvoyerRecevoir.setLocation(new Point(149, 0));
			btEnvoyerRecevoir.setText("");
			btEnvoyerRecevoir.setIcon(new ImageIcon(getClass().getResource(
					"/envoyer_recevoir.png")));
			btEnvoyerRecevoir.setSize(new Dimension(75, 50));
			btEnvoyerRecevoir.setActionCommand(EnActionMain.RECEVOIR.getLib());
		}
		return btEnvoyerRecevoir;
	}

	/**
	 * This method initializes jTree
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			new BDAcces();
			ArborescenceBoiteMail arbo = new ArborescenceBoiteMail();
			jTree = new JTree(arbo);
			jTree.setBounds(new Rectangle(3, 5, 290, 255));
			jTree.setShowsRootHandles(true);
			jTree.setRootVisible(false);
			jTree.setToggleClickCount(1);
			jTree.setFont(new Font("Perpetua", Font.BOLD, 12));
			jTree.setExpandsSelectedPaths(true);
		}
		return jTree;
	}

	/**
	 * This method initializes jEditorPane
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPane() {
		htmlPane = new JEditorPane();
		htmlPane.setComponentOrientation(ComponentOrientation.UNKNOWN);
		htmlPane.setFont(new Font("Dialog", Font.PLAIN, 12));
		// htmlPane.setContentType("text/html");
		// htmlPane.setContentType("text/html");
		htmlPane.setEditable(false);
		htmlPane.addHyperlinkListener(new MlActionHtmlPane());

		return htmlPane;
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
			jTable.setColumnSelectionAllowed(true);
			jTable
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			jTable.setShowHorizontalLines(false);

			jTable.setShowVerticalLines(false);
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

			jList.setBackground(Color.LIGHT_GRAY);
			jList.setPreferredSize(new Dimension(50, 50));
		}
		return jList;
	}

	/**
	 * This method initializes jScrollPane1
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJEditorPane());
			jScrollPane1.setViewportBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jMenuReleve
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuReleve() {
		if (jMenuReleve == null) {
			jMenuReleve = new JMenu();
			jMenuReleve.setActionCommand("dfsdffdsf");
			jMenuReleve.setText("Outils");
			jMenuReleve.add(getJMenuEnvoiReception());
		}
		return jMenuReleve;
	}

	/**
	 * This method initializes jMenuEnvoiReception
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuEnvoiReception() {
		if (jMenuEnvoiReception == null) {
			jMenuEnvoiReception = new JMenu();
			jMenuEnvoiReception.setText("Envoyer / Recevoir");
			jMenuEnvoiReception.add(getJMenuItemReleve());
		}
		return jMenuEnvoiReception;
	}

	/**
	 * This method initializes jMenuItemReleve
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemReleve() {
		if (jMenuItemReleve == null) {
			jMenuItemReleve = new JMenuItem();
			jMenuItemReleve.setText(EnActionMain.RECEVOIR.getLib());
			jMenuItemReleve.setActionCommand(EnActionMain.RECEVOIR.getLib());
		}
		return jMenuItemReleve;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		new BDAcces();
		initialize();
		this.addComponentListener(new MlComposantListener(jContentPane));
		Thread_Verif verif = new Thread_Verif(jTree);
		verif.start();
		modelList = new DefaultListModel();
		jList.setModel(modelList);
		ColoneModel = new XTableColumnModel();
		jTable.setColumnModel(ColoneModel);
		jTable.setDefaultRenderer(Date.class, new DateTimeCellRenderer());
		tableModel = new MyTableModel(new MlListeMessage(), ColoneModel);
		jTable.setModel(tableModel);
		jTable.addMouseListener(new MlActionJtable(jTable, htmlPane, jList));

		jMenuContact.addActionListener(new MlActionMain());
		jTree.addMouseListener(new MlActionJtree(jTree, jTable));
		jTree.addTreeSelectionListener(new MlActionJtree(jTree, jTable));
		jTree.addTreeExpansionListener(new MlActionJtree(jTree, jTable));
		jMenuItemImporter.addActionListener(new MlActionMain(jTree));
		jMenuItemReleve.addActionListener(new MlActionMain(jTree));
		btEnvoyerRecevoir.addActionListener(new MlActionMain(jTree));
		btSupprMessage
				.addActionListener(new MlActionPopupJTable(jTable, jList));
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
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(3);
			borderLayout1.setVgap(3);
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout1);
			jContentPane.add(getJDesktopPaneHaut(), BorderLayout.NORTH);
			jContentPane.add(getJDesktopPaneGauche(), BorderLayout.WEST);
			jContentPane.add(getJDesktopPaneDroite(), BorderLayout.CENTER);
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
			jJMenuBar.add(getJMenuReleve());
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
			jMenuFichier.add(getJMenuImportExport());
			jMenuFichier.add(getJMenuQuitter());
		}
		return jMenuFichier;
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

} // @jve:decl-index=0:visual-constraint="10,10"
